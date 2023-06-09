package rhul_sc.bignat;

import javacard.framework.ISOException;
import javacard.security.RSAPublicKey;
import javacard.security.KeyBuilder;
import javacard.security.CryptoException;
import javacardx.crypto.Cipher;

/** 
 * Compute modular powers on the crypto coprocessor of the Java Card.
 * This class wraps an RSA_NOPAD cipher and uses its public key
 * encryption to compute exponents. <P>
 *
 * For a number of general topics <a
 * href="package-summary.html#package_description">see also the package
 * description.</a>
 *
 * <P>
 *
 * The interface is tailored towards applications where the modulus
 * stays constant and the exponent might stay constant. Therefore the
 * modulus must and the exponent can be set before computing the
 * power.
 * <P>
 *
 * Further, for compatibility with Montgomery multiplication the
 * methods here take an extra {@code offset} argument, that must be equal to
 * the number of montgomery digits used, which is, at the same time,
 * the number of bytes that the used Bignat's are longer than the
 * configured key size. Digits at index less than {@code offset} are
 * always ignored for arguments. For results they are set to zero.
 * <P>
 *
 * There are several limitations for using the RSA cipher on the card.
 * Some of them are general, some might be card specific:
 * <DL>
 * <DT><STRONG>key size</STRONG>
 *
 * <DD>The cipher works on a certain key size that must be set with
 * either the allocating constructor {@link #RSA_exponent(short)} or
 * {@link #init_key init_key}. The modulus, the base, the result and
 * the temporary in the methods {@link #set_modulus set_modulus},
 * {@link #set_exponent set_exponent}, {@link #power power} and {@link
 * #fixed_power fixed_power} must be precisely as long as given by the
 * key size after subtracting the Montgomery digits, whose number is
 * given in the {@code offset} argument of all those methods.
 *
 * <DT><STRONG>permitted key sizes</STRONG>
 *
 * <DD>The cards that I have access to permit key sizes between 512
 * and 1952 bit, ie. between 64 and 244 bytes. (NXP likes to print
 * ``RSA up to 2048 bit'' on the backside of their cards, but, the
 * longest key they accept is nevertheless 1952 bits.) Further, only
 * key sizes divisible by 4 bytes (or 32 bits) are permitted. So 544
 * bit keys are OK, but 520 bit ones are not. 
 *
 * <DT><STRONG>permitted moduli</STRONG>
 *
 * <DD>The first byte of the moduls must not be zero. Otherwise the
 * card hangs. Together with the restrictions on key sizes this means
 * that you cannot use this class, if your exponent is, for instance,
 * 520 bits long.
 * 
 *</DL>
 *
 * I have not observed any restrictions on the values of the bases and
 * the exponents. In particular, one can compute the modulus by
 * setting the exponent to 1. The cipher behaves strange if the public
 * key is zero, but the code here works around this problem by testing
 * all exponents for zero and returning 1 in that case. 
 * <P>
 *
 * The code of this class runs only on the card. See {@link
 * Fake_rsa_exponent} for a host equivalent.
 *
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET</a>,
 *   <a href="../../../overview-summary.html#TESTFRAME">TESTFRAME</a>,
 *   <a href="../../../overview-summary.html#JAVADOC">JAVADOC</a>
 *
 * @author Hendrik Tews
 * @version $Revision: 1.22 $
 * @commitdate $Date: 2009/05/18 15:06:45 $ by $Author: tews $
 * @environment card
 * @see ds.ov2.bignat.Fake_rsa_exponent Fake_rsa_exponent
 */
public class RSA_exponent implements RSA_exponent_interface {

    /**
     * Cipher used for computing the modular powers.
     */
    private Cipher cipher;

    /**
     * Public RSA key object for the modulus and the exponent. The
     * modulus stays constant, the exponent is updated when using
     * {@link #power}. 
     */
    private RSAPublicKey key;

    /**
     * 
     * My JCOP cards and the jcop emulator do not compute a power when
     * the public exponent is zero (they always return zero then).
     * Therefore we have to remember if an exponent of zero has been
     * set. 
     */
    private boolean exponent_is_zero = false;

    /**
     *
     * Initialize the internal public RSA key. The key is needed
     * together with the cipher for doing encryption. The key is
     * initialized to size {@code key_byte_size} bytes. This key size
     * determins the size of the Bignat arguments for all the other
     * methods. 
     * <P>
     * 
     * Normally only called from within {@link #allocate allocate},
     * which can either be called directly or indirectly via the
     * allocating constructor {@link #RSA_exponent(short)}. 
     * <P>
     *
     * For changing the key size this method can be called directly.
     * However, this should only be done for testing purposes, because
     * the old key becomes garbage.
     * <P>
     *
     * Note that for some key sizes key initialization works, but then
     * exponent initialization does always throw an exception. So
     * normal termination of this method does not mean that the key
     * size {@code key_byte_size} is supported.
     * 
     * @param key_byte_size the key size in bytes.
     * @throws ISOException with reason {@link
     * ds.ov2.util.Response_status#OV_RSA_KEY_FAILURE} if a suitable key of
     * this size cannot be created.
     */
    public void init_key(short key_byte_size) {
        try {
            key = (RSAPublicKey) (KeyBuilder.buildKey(KeyBuilder.TYPE_RSA_PUBLIC, (short) (key_byte_size * 8), false));
        } catch (CryptoException e) {
            if (e.getReason() == CryptoException.NO_SUCH_ALGORITHM) ISOException.throwIt(Response_status.OV_RSA_KEY_FAILURE); else throw e;
        }
        return;
    }

    /**
     * 
     * Non-allocating constructor. If this constructor is used the
     * internal data structure must later be explicitely allocated
     * with {@link #allocate allocate}.
     * 
     */
    public RSA_exponent() {
        return;
    }

    /**
     *
     * Allocte internal data. Allocate the cipher and the key objects.
     * The key is initialized to size {@code key_byte_size} with
     * {@link #init_key init_key}. This key size determins the size of
     * the Bignat arguments for all the other methods. 
     * <P>
     *
     * This method should only used once and only if the
     * non-allocating constructor {@link #RSA_exponent()} has been used
     * to create this object. (The allocating constructor {@link
     * #RSA_exponent(short)} calls this method itself.)
     * <P>
     * 
     * Note that for some key sizes key initialization works, but then
     * exponent initialization does always throw an exception. So
     * normal termination of this method does not mean that the key
     * size {@code key_byte_size} is supported.
     * 
     * @param key_byte_size key size in bytes
     * @throws ISOException with reason {@link
     * ds.ov2.util.Response_status#OV_RSA_KEY_FAILURE} if a suitable key of
     * this size cannot be created.
     * @throws ISOException with reason {@link
     * ds.ov2.util.Response_status#OV_RSA_NOPAD_CIPHER_FAILURE} if the
     * {@link Cipher#ALG_RSA_NOPAD ALG_RSA_NOPAD} cipher is not available.
     */
    public void allocate(short key_byte_size) {
        try {
            cipher = Cipher.getInstance(Cipher.ALG_RSA_NOPAD, false);
        } catch (CryptoException e) {
            if (e.getReason() == CryptoException.NO_SUCH_ALGORITHM) ISOException.throwIt(Response_status.OV_RSA_NOPAD_CIPHER_FAILURE); else throw e;
        }
        init_key(key_byte_size);
        return;
    }

    /**
     *
     * Allocating constructor. Allocates the internal cipher and
     * initializes the key with a key size of {@code key_byte_size}
     * bytes. This key size determines the size of the Bignat arguments
     * for all the other methods. 
     * <P>
     * 
     * The constructor calls {@link #allocate allocate} internally. On
     * objects created with this constructor {@link #allocate
     * allocate} should be avoided.
     *
     * Different from {@link #allocate allocate} and {@link #init_key
     * init_key} this constructor will change the key size if it is
     * not one of the known working key sizes. First, the key size is
     * rounded upwards to the next number divisible by 4. Then it is
     * changed to fit into the interval between 64 and 244.
     * <P>
     *
     * Querying the key size is currently not supported. Therefore it
     * is probably not so a good idea to rely on the key size
     * adaption. 
     * <P>
     * 
     * Note that for some key sizes key initialization works, but then
     * exponent initialization does always throw an exception. So
     * normal termination of this constructor does not mean that the
     * possibly adapted key size is supported.
     *
     * 
     * @param key_byte_size key size in bytes,
     * @throws ISOException with reason {@link
     * ds.ov2.util.Response_status#OV_RSA_KEY_FAILURE} if a suitable key of
     * the possibly adopted key size cannot be created.
     * @throws ISOException with reason {@link
     * ds.ov2.util.Response_status#OV_RSA_NOPAD_CIPHER_FAILURE} if the
     * {@link Cipher#ALG_RSA_NOPAD ALG_RSA_NOPAD} cipher is not available.
     */
    public RSA_exponent(short key_byte_size) {
        this();
        if (key_byte_size % 4 != 0) key_byte_size += 4 - key_byte_size % 4;
        key_byte_size = key_byte_size < 64 ? 64 : key_byte_size;
        key_byte_size = key_byte_size > 244 ? 244 : key_byte_size;
        allocate(key_byte_size);
    }

    /**
     *
     * Set modulus. Must be called before {@link #power power} or
     * before {@link #set_exponent set_exponent}. Once set, a modulus
     * is used for all subsequently computed exponents until it is
     * changed via this method. The {@code offset} argument specifies
     * the number of ditits that {@code mod} is longer than the
     * configured key size. If {@code offset} is different from {@code
     * 0} all digits left of {@code offset} are assumed to be zero.
     * For a modulus that is used with Montgomery multiplication use
     * an {@code offset} of 2. <P>
     * 
     * @param mod modulus, 2 bytes longer than the key size
     * @param offset starting index of the most significant digit of
     * the modulus
     * @throws ISOException with reason {@link
     * ds.ov2.util.Response_status#OV_RSA_MOD_FAILURE} if the modulus
     * has the wrong size
     */
    public void set_modulus(Bignat mod, short offset) {
        ;
        try {
            key.setModulus(mod.get_digit_array(), offset, (short) (mod.size() - offset));
        } catch (CryptoException e) {
            if (e.getReason() == CryptoException.ILLEGAL_VALUE) ISOException.throwIt(Response_status.OV_RSA_MOD_FAILURE); else throw e;
        }
        return;
    }

    /**
     *
     * Initialize the exponent for subsequent use of this object with
     * {@link #fixed_power fixed_power}. This is advantageous if more
     * than one power is computed with the same exponent, because
     * exponent initializtion amounts for about 60% (for short keys
     * sizes) to 30% (for long key sizes) of the total computation
     * time. <P>
     *
     * The modulus must have been set with {@link #set_modulus
     * set_modulus} before calling this method. <P>
     *
     * The temporary is used here to permit exponents which are
     * shorter than the configured key size of the underlying cipher.
     * The {@code offset} argument specifies the number of digits
     * that {@code temp} is larger then the configured key size. For a
     * temporary that is used with <a
     * href="../bignat/package-summary.html#montgomery_factor">Montgomery
     * multiplication </a> use an offset of {@code 2}.
     * <P>
     *
     * @param exp exponent
     * @param temp temporary
     * @param offset number of digits {@code temp} is longer than the
     * configured key size
     * @throws ISOException with reason {@link
     * ds.ov2.util.Response_status#OV_RSA_EXP_FAILURE} if setting the
     * exponent fails (which I believe happens for invalid key sizes
     * that are not reported as such when initiliazing the key)
     */
    public void set_exponent(Bignat exp, Bignat temp, short offset) {
        if (exp.is_zero()) exponent_is_zero = true; else exponent_is_zero = false;
        temp.copy(exp);
        byte[] temp_digits = temp.get_digit_array();
        try {
            key.setExponent(temp_digits, offset, (short) (temp.size() - offset));
        } catch (CryptoException e) {
            if (e.getReason() == CryptoException.ILLEGAL_VALUE) ISOException.throwIt(Response_status.OV_RSA_EXP_FAILURE); else throw e;
        }
        cipher.init(key, Cipher.MODE_ENCRYPT);
    }

    public void set_exponent(Bignat exp, short offset) {
        if (exp.is_zero()) exponent_is_zero = true; else exponent_is_zero = false;
        try {
            key.setExponent(exp.get_digit_array(), offset, (short) (exp.size() - offset));
        } catch (CryptoException e) {
            if (e.getReason() == CryptoException.ILLEGAL_VALUE) ISOException.throwIt(Response_status.OV_RSA_EXP_FAILURE); else throw e;
        }
        cipher.init(key, Cipher.MODE_ENCRYPT);
    }

    /**
     *
     * Modular power with preconfigured modulus and exponent. Sets
     * {@code result} to {@code base}^{@code exp} mod {@code modulus},
     * where the {@code modulus} and {@code exp} must have been
     * configured before with {@link #set_modulus set_modulus} and
     * {@link #set_exponent set_exponent}, respectively. Note that
     * {@link #set_modulus set_modulus} must always be called before
     * {@link #set_exponent set_exponent}. <P>
     *
     * Using this method makes sense when more than one power is
     * computed with the same modulus and exponent. Measurements show
     * that initializing the modulus and the exponent amounts for
     * about 60% (for short keys sizes) to 30% (for long key sizes) of
     * the total computation time. This method is merely a wrapper
     * around {@link Cipher#doFinal doFinal}. <P>
     *
     * The modulus and the exponent must have been set before calling
     * this method with {@link #set_modulus set_modulus} and {@link
     * #set_exponent set exponent}, respectively. Note that {@link
     * #set_modulus set_modulus} must always be called before {@link
     * #set_exponent set_exponent}. <P>
     *
     * The argument {@code offset} specifies the number of digits that
     * the {@code base} and the {@code result} are longer than the
     * configured key size. For bases and results that are used with
     * <a
     * href="../bignat/package-summary.html#montgomery_factor">Montgomery
     * multiplication </a> use an {@code offset} of {@code 2}.
     * <P>
     *
     * {@code base} and {@code result} must not be the same reference,
     * otherwise the cipher may produce incorrect results.
     * <P>
     * 
     * @param base 
     * @param result reference for storing the result
     * @param offset number of digits {@code base} and {@code result}
     * is longer than the configured key size 
     */
    public void fixed_power(Bignat base, Bignat result, short offset) {
        if (exponent_is_zero) {
            result.one();
            return;
        }
        if (offset > 0) {
            byte[] result_digits = result.get_digit_array();
            for (short i = 0; i < offset; i++) {
                result_digits[i] = 0;
            }
        }
        cipher.doFinal(base.get_digit_array(), offset, (short) (base.size() - offset), result.get_digit_array(), offset);
        return;
    }

    /**
     *
     * Modular power. Sets {@code result} to {@code base}^{@code exp}
     * mod {@code modulus}, where the {@code modulus} must have been
     * configured before with {@link #set_modulus set_modulus}. Same
     * as {@link #set_exponent set_exponent} followed by {@link
     * #fixed_power fixed_power} <P>
     *
     * The argument {@code offset} specifies the index of the most
     * significant digits of {@code base} and {@code result}. If
     * {@code base} and {@code result} 
     * are used with <a
     * href="../bignat/package-summary.html#montgomery_factor">Montgomery
     * multiplication</a> the {@code offset} should be {@code 2}.
     * <P>
     *
     * The exponent must fit into key size many bytes but can
     * otherwise be arbitrarily long. <P>
     *
     * {@code base} and {@code result} must not be the same reference,
     * because {@code result} the exponent is first copied into {@code
     * result} to permit exponents shorter than key size. Moreover the
     * cipher on the card does not permit them to be the same
     * reference.
     * 
     * @param base
     * @param exp exponent
     * @param result reference for storing the result
     */
    public void power(Bignat base, Bignat exp, Bignat result, short offset) {
        set_exponent(exp, result, offset);
        fixed_power(base, result, offset);
    }
}
