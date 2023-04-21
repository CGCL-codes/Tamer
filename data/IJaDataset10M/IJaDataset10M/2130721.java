package javax.crypto;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.spec.AlgorithmParameterSpec;

/**
 * This class provides the functionality of a "Message Authentication Code"
 * (MAC) algorithm.
 *
 * <p>A MAC provides a way to check the integrity of information transmitted
 * over or stored in an unreliable medium, based on a secret key. Typically,
 * message authentication codes are used between two parties that share a
 * secret key in order to validate information transmitted between these
 * parties.
 *
 * <p>A MAC mechanism that is based on cryptographic hash functions is referred
 * to as HMAC. HMAC can be used with any cryptographic hash function, e.g., MD5
 * or SHA-1, in combination with a secret shared key. HMAC is specified in
 * RFC 2104.
 *
 * @author Patric Kabus
 * @author Jan Peters
 * @version "$Id: Mac.java 1913 2007-08-08 02:41:53Z jpeters $"
 */
public class Mac extends Object {

    /**
     * The provider used by this instance.
     */
    private Provider provider_;

    /**
     * States wether this cipher has been initialised.
     */
    private boolean init_;

    /**
     * the algorithm used by this instance.
     */
    private String algorithm_;

    /**
     * The MAC used this instance.
    */
    private MacSpi macSpi_;

    /**
     * Creates a MAC object.
     *
     * @param macSpi the delegate.
     * @param provider the provider.
     * @param algorithm the algorithm.
    */
    protected Mac(MacSpi macSpi, Provider provider, String algorithm) {
        macSpi_ = macSpi;
        provider_ = provider;
        algorithm_ = algorithm;
        init_ = false;
    }

    /**
     * Returns the algorithm name of this <code>Mac</code> object.
     *
     * <p>This is the same name that was specified in one of the
     * <code>getInstance</code> calls that created this <code>Mac</code>
     * object. 
     *
     * @return the algorithm name of this <code>Mac</code> object.
     */
    public final String getAlgorithm() {
        return algorithm_;
    }

    /**
     * Generates an <code>Mac</code> object that implements the specified MAC
     * algorithm. If the default provider package provides an implementation
     * of the requested MAC algorithm, an instance of <code>Mac</code>
     * containing that implementation is returned. If the algorithm is not
     * available in the default provider package, other provider packages are
     * searched.
     *
     * @param algorithm the standard name of the requested MAC algorithm.
     *   See Appendix A in the 
     *   <a href="../../../guide/security/jce/JCERefGuide.html#AppA">
     *   Java Cryptography Extension Reference Guide</a> for information
     *   about standard algorithm names.
     *
     * @return the new <code>Mac</code> object.
     *
     * @throws NoSuchAlgorithmException if the specified algorithm is not
     *   available in the default provider package or any of the other
     *   provider packages that were searched. 
     */
    public static final Mac getInstance(String algorithm) throws NoSuchAlgorithmException {
        Object[] o;
        o = Util.getImpl(algorithm, Util.MAC);
        return new Mac((MacSpi) o[0], (Provider) o[1], algorithm);
    }

    /**
     * Generates an <code>Mac</code> object for the specified MAC algorithm
     * from the specified provider.
     *
     * @param algorithm the standard name of the requested MAC algorithm.
     *   See Appendix A in the 
     *   <a href="../../../guide/security/jce/JCERefGuide.html#AppA">
     *   Java Cryptography Extension Reference Guide</a> for information
     *   about standard algorithm names.
     * @param provider the name of the provider.
     *
     * @return the new <code>Mac</code> object.
     *
     * @throws NoSuchAlgorithmException if the specified algorithm is not
     *   available in the default provider package or any of the other
     *   provider packages that were searched. 
     * @throws NoSuchProviderException if the specified provider has not been
     *   configured.  
     * @throws IllegalArgumentException if the <code>provider</code> is null.
     */
    public static final Mac getInstance(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
        Object[] o;
        if (provider == null) {
            throw new IllegalArgumentException("missing provider");
        }
        o = Util.getImpl(algorithm, Util.MAC, provider);
        return new Mac((MacSpi) o[0], (Provider) o[1], algorithm);
    }

    /**
     * Generates an <code>Mac</code> object for the specified MAC algorithm
     * from the specified provider.
     * Note: the <code>provider</code> doesn't have to be registered.
     *
     * @param algorithm the standard name of the requested MAC algorithm.
     *   See Appendix A in the 
     *   <a href="../../../guide/security/jce/JCERefGuide.html#AppA">
     *   Java Cryptography Extension Reference Guide</a> for information
     *   about standard algorithm names.
     * @param provider the provider.
     *
     * @return the new <code>Mac</code> object.
     *
     * @throws NoSuchAlgorithmException if the specified algorithm is not
     *   available in the default provider package or any of the other
     *   provider packages that were searched. 
     * @throws IllegalArgumentException if the <code>provider</code> is null.
     */
    public static final Mac getInstance(String algorithm, Provider provider) throws NoSuchAlgorithmException, NoSuchProviderException {
        Object[] o;
        if (provider == null) {
            throw new IllegalArgumentException("missing provider");
        }
        o = Util.getImpl(algorithm, Util.MAC, provider);
        return new Mac((MacSpi) o[0], (Provider) o[1], algorithm);
    }

    /**
     * Returns the provider of this <code>Mac</code> object.
     *
     * @return the provider of this <code>Mac</code> object.
     */
    public final Provider getProvider() {
        return provider_;
    }

    /**
     * Returns the length of the MAC in bytes.
     *
     * @return the length of the MAC in bytes.
     */
    public final int getMacLength() {
        return macSpi_.engineGetMacLength();
    }

    /**
     * Initializes this <code>Mac</code> object with the given key.
     *
     * @param key the key.
     *
     * @throws InvalidKeyException if the given key is inappropriate for
     *   initializing this MAC.
     */
    public final void init(Key key) throws InvalidKeyException {
        try {
            init(key, null);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes this <code>Mac</code> object with the given key and
     * algorithm parameters.
     *
     * @param key the key.
     * @param params the algorithm parameters.
     *
     * @throws InvalidKeyException if the given key is inappropriate for
     *   initializing this MAC.  
     * @throws InvalidAlgorithmParameterException if the given algorithm
     *   parameters are inappropriate for this MAC.
     */
    public final void init(Key key, AlgorithmParameterSpec params) throws InvalidKeyException, InvalidAlgorithmParameterException {
        macSpi_.engineInit(key, params);
        init_ = true;
    }

    /**
     * Processes the given byte.
     *
     * @param input the input byte to be processed.
     *
     * @throws IllegalStateException if this <code>Mac</code> has not been
     *   initialized.
     */
    public final void update(byte input) throws IllegalStateException {
        if (!init_) {
            throw new IllegalStateException("not initialised");
        }
        macSpi_.engineUpdate(input);
    }

    /**
     * Processes the given array of bytes.
     *
     * @param input the array of bytes to be processed.
     *
     * @throws IllegalStateException if this <code>Mac</code> has not been
     *   initialized.
     * @throws NullPointerException if <code>output</code> is null.
     */
    public final void update(byte[] input) throws IllegalStateException {
        update(input, 0, input.length);
    }

    /**
     * Processes the first <code>len</code> bytes in <code>input</code>,
     * starting at <code>offset</code> inclusive.
     *
     * @param input the input buffer.
     * @param offset the offset in <code>input</code> where the input starts.
     * @param len the number of bytes to process.
     *
     * @throws IllegalStateException if this <code>Mac</code> has not been
     *   initialized.
     * @throws NullPointerException if <code>input</code> is null.  
     */
    public final void update(byte[] input, int offset, int len) throws IllegalStateException {
        if (!init_) {
            throw new IllegalStateException("not initialised");
        }
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset is <0");
        }
        if (len > (input.length - offset)) {
            throw new ArrayIndexOutOfBoundsException("input buffer too small for given length and offset");
        }
        macSpi_.engineUpdate(input, offset, len);
    }

    /**
     * Processes <code>input.remaining()</code> bytes in the ByteBuffer
     * <code>input</code>, starting at <code>input.position()</code>.
     * Upon return, the buffer's position will be equal to its limit;
     * its limit will not have changed.
     *
     * @param input the ByteBuffer
     *
     * @exception IllegalStateException if this <code>Mac</code> has not been
     * initialized or if <code>input<\code> is null.
     */
    public final void update(ByteBuffer input) {
        if (init_ == false) {
            throw new IllegalStateException("MAC not initialized");
        }
        if (input == null) {
            throw new IllegalArgumentException("Buffer must not be null");
        }
        macSpi_.engineUpdate(input);
    }

    /**
     * Finishes the MAC operation.
     *
     * <p>A call to this method resets this <code>Mac</code> object to the
     * state it was in when previously initialized via a call to
     * <code>init(Key)</code> or
     * <code>init(Key, AlgorithmParameterSpec)</code>. That is, the object is
     * reset and available to generate another MAC from the same key, if
     * desired, via new calls to <code>update</code> and <code>doFinal</code>.
     * (In order to reuse this <code>Mac</code> object with a different key,
     * it must be reinitialized via a call to <code>init(Key)</code> or
     * <code>init(Key, AlgorithmParameterSpec)</code>. 
     *
     * @return the MAC result.
     *
     * @throws IllegalStateException if this Mac has not been initialized.  
     */
    public final byte[] doFinal() throws IllegalStateException {
        if (!init_) {
            throw new IllegalStateException("not initialised");
        }
        return macSpi_.engineDoFinal();
    }

    /**
     * Finishes the MAC operation.
     *
     * <p>A call to this method resets this <code>Mac</code> object to the
     * state it was in when previously initialized via a call to
     * <code>init(Key)</code> or
     * <code>init(Key, AlgorithmParameterSpec)</code>. That is, the object is
     * reset and available to generate another MAC from the same key, if
     * desired, via new calls to <code>update</code> and <code>doFinal</code>.
     * (In order to reuse this <code>Mac</code> object with a different key,
     * it must be reinitialized via a call to <code>init(Key)</code> or
     * <code>init(Key, AlgorithmParameterSpec)</code>. 
     *
     * <p>The MAC result is stored in output, starting at outOffset inclusive.
     *
     * @param output the buffer where the MAC result is stored.
     * @param outOffset the offset in <code>output</code> where the MAC is
     *   stored
     *
     * @throws ShortBufferException if the given output buffer is too small
     *   to hold the result.
     * @throws IllegalStateException if this <code>Mac</code> has not been
     *   initialized.
     * @throws NullPointerException if <code>output</code> is null.
     */
    public final void doFinal(byte[] output, int outOffset) throws ShortBufferException, IllegalStateException {
        byte[] buf;
        if (!init_) {
            throw new IllegalStateException("Mac is not initialised");
        }
        if (output == null) {
            throw new NullPointerException("output is null!");
        }
        if (outOffset < 0) {
            throw new IllegalArgumentException("outOffset is <0");
        }
        if (output.length <= outOffset) {
            throw new ArrayIndexOutOfBoundsException("Output buffer is too small for the given offset!");
        }
        buf = macSpi_.engineDoFinal();
        if (output.length < (buf.length + outOffset)) {
            throw new ShortBufferException("The buffer is too small: cannot place result at given offset!");
        }
        System.arraycopy(buf, 0, output, outOffset, buf.length);
    }

    /**
     * Processes the given array of bytes and finishes the MAC operation.
     *
     * <p>A call to this method resets this <code>Mac</code> object to the
     * state it was in when previously initialized via a call to
     * <code>init(Key)</code> or
     * <code>init(Key, AlgorithmParameterSpec)</code>. That is, the object is
     * reset and available to generate another MAC from the same key, if
     * desired, via new calls to <code>update</code> and <code>doFinal</code>.
     * (In order to reuse this <code>Mac</code> object with a different key,
     * it must be reinitialized via a call to <code>init(Key)</code> or
     * <code>init(Key, AlgorithmParameterSpec)</code>. 
     *
     * @param input data in bytes.
     *
     * @return the MAC result.
     *
     * @throws IllegalStateException if this <code>Mac</code> has not been
     *   initialized.
     */
    public final byte[] doFinal(byte[] input) throws IllegalStateException {
        update(input);
        return doFinal();
    }

    /**
     * Resets this Mac object.
     *
     * <p>A call to this method resets this <code>Mac</code> object to the
     * state it was in when previously initialized via a call to
     * <code>init(Key)</code> or
     * <code>init(Key, AlgorithmParameterSpec)</code>. That is, the object
     * is reset and available to generate another MAC from the same key, if
     * desired, via new calls to <code>update</code> and <code>doFinal</code>.
     * (In order to reuse this <code>Mac</code> object with a different key,
     * it must be reinitialized via a call to <code>init(Key)</code> or
     * <code>init(Key, AlgorithmParameterSpec)</code>. 
     */
    public final void reset() {
        macSpi_.engineReset();
    }

    /**
     * Returns a clone if the provider implementation is cloneable.
     *
     * @return a clone if the provider implementation is cloneable.
     *
     * @throws CloneNotSupportedException if this is called on a delegate that
     *  does not support <code>Cloneable</code>.
     *
     * @see Clonable
     */
    public final Object clone() throws CloneNotSupportedException {
        return new Mac((MacSpi) macSpi_.clone(), provider_, algorithm_);
    }
}
