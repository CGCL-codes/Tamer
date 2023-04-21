package cdc.standard.mode;

import cdc.standard.basic.*;
import cdc.standard.padding.*;
import java.security.Key;
import java.security.SecureRandom;
import java.security.AlgorithmParameters;
import java.security.spec.AlgorithmParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import javax.crypto.CipherSpi;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import javax.crypto.BadPaddingException;
import java.lang.reflect.Method;

/**
 * <P>
 * <I>Cipher feedback</I> (CFB) mode allows a block cipher to act like a stream cipher.
 * Like CBC, it uses an IV, but the internal process is more involved. The net result
 * is that a block cipher in CFB mode can encrypt pieces of data that are smaller than
 * the block size. In fact, CFB can be used to encrypt any data size from bit to the
 * block size. <BR>
 *
 * <dt><b>Progress:</b>
 * <dd> April 1999, Created</dd>
 * <dd> April 1999, Finished first Version</dd>
 * <dd> 8th Febuary 2000, AlgorithmParameterSpec handling added</dd>
 * <dd> 18th Febuary 2000, Error in AlgorithmParameterSpec handling corrected</dd>
 * <dd> 8th March 2000,    Moved to package cdc.standard.mode</dd>
 * <dd>25th April 2000,    Added AlgorithmParameterSpec to engineInit()</dd>
 * </dl>
 *
 * @author  Andre Maric
 * @author  Witold Wegner
 * @author  Sylvain Franke
 * @author  Oliver Seiler
 *
 * @version 1.02 Built and tested with SUN�s jdk1.2.2
 */
public class CFB extends Mode {

    /** the initaliziation vector	*/
    private byte[] IV;

    /** IBuffer is used by method doTheCFB(). It is filled with
     *  the IV at the beginning of encryption or decryption.
     *  This buffer is encrypted with the BlockCipher.singleBlockEncrypt-
     *  method and holds the result for the next method call.
     */
    private byte[] IBuffer;

    /** r is the blocklength of the CFB in bytes and should be
     *  less than theBlockCipher.getBlockSize().
     */
    private int r = 8;

    private int leftoverBufferLen = 0;

    private byte[] leftoverBuffer = new byte[r - 1];

    /** operation mode is either ENCRYPT_MODE or DECRYPT_MODE  */
    private int opmode;

    private byte[] AW = { 65, 78, 68, 82, 69, 32, 77, 65, 82, 73, 67, 32, 38, 32, 87, 73, 84, 79, 76, 68, 32, 87, 69, 71, 78, 69, 82, 32, 65, 82, 69, 32, 82, 69, 65, 76, 76, 89, 32, 67, 79, 79, 76 };

    private AlgorithmParameters algParams;

    private AlgorithmParameterSpec algParamSpec = null;

    /**
     * This method should be automatically called by the
     * BasicCipher-Class. It is not necessary to
     * call this precedure "by hand".
     * <p>
     * This method does the same as "setPaddingScheme()".
     *
     * @param paddingname the name of the padding to use for
     *    encrypting/decrypting
     * @exception NoSuchPaddingException if for some reason the
     *    supplied paddingname can not be found as algorithm
     */
    public void engineSetPadding(String padding) throws NoSuchPaddingException {
        thePaddingScheme = PaddingScheme.getInstance(padding);
        return;
    }

    /**
      * This method does nothing. Modes are set by the BasicCipher
      * class. This method is only because CipherSpi requires it.
      * <p>
      * @param modename the name of the mode to use for
      *    encrypting/decrypting
      * @exception NoSuchAlgorithmException will never be raised
      *
      */
    public void engineSetMode(String mode) throws NoSuchAlgorithmException {
        return;
    }

    /**
	   * Returns the block size (in bytes).
	   *
	   * @return the block size (in bytes).
	   */
    public int engineGetBlockSize() {
        return r;
    }

    public int engineGetOutputSize(int inputLen) {
        return (leftoverBufferLen + inputLen + thePaddingScheme.padLength(leftoverBufferLen + inputLen));
    }

    /**
	* Returns the initialisation vector (IV) in a new buffer.
	* <p>
	* This is useful in the context of password-based encryption or
	* decryption, where the IV is derived from a user-provided passphrase.
	*
	* @return the initialisation vector in a new buffer, or null if the
	*      underlying algorithm does not use an IV, or if the IV has
	*      not yet been set.
	*/
    public byte[] engineGetIV() {
        if (IV == null) System.arraycopy(AW, 0, IV, 0, theBlockCipher.getBlockSize());
        return IV;
    }

    /**
     * Returns the parameters used with this cipher.
     * <p>
     * The returned parameters may be the same that were used to initialise
     * this cipher, or may contain the default set of parameters or a set of
     * randomly generated parameters used by the underlying cipher
     * implementation (provided that the underlying cipher implementation
     * uses a default set of parameters or creates new parameters if it needs
     * parameters but was not initialised with any).
     *
     * @return the parameters used with this cipher, or null if this cipher
     *     does not use any parameters.
     */
    public AlgorithmParameters engineGetParameters() {
        return algParams;
    }

    /**
     * Initialises this cipher with a key and a source of randomness.
     * <p>
     * The cipher is initialised for encryption or decryption, depending
     * on the value of opmode.
     * <p>
     * If this cipher requires any algorithm parameters that cannot be
     * derived from the given key, the underlying cipher implementation
     * is supposed to generate the required parameters itself (using
     * provider-specific default or random values) if it is being
     * initialised for encryption, and raise an InvalidKeyException if it
     * is being initialised for decryption. The generated parameters can
     * be retrieved using engineGetParameters or engineGetIV (if the
     * parameter is an IV).
     * <p>
     * If this cipher (including its underlying feedback or padding
     * scheme) requires any random bytes (e.g., for parameter
     * generation), it will get them from random.
     * <p>
     * Note that when a Cipher object is initialised, it loses all
     * previously-acquired state. In other words, initialising a Cipher
     * is equivalent to creating a new instance of that Cipher and
     * initialising it
     *
     * @param opmode the operation mode of this cipher (this is either
     *		    ENCRYPT_MODE or DECRYPT_MODE)
     * @param key the encryption key
     * @param random the source of randomness
    * @exception InvalidKeyException if the given key is inappropriate
     *		    for initialising this cipher
     */
    public void engineInit(int opmode, Key key, SecureRandom random) throws InvalidKeyException {
        this.opmode = opmode;
        if (opmode == Cipher.ENCRYPT_MODE) {
            theBlockCipher.initEncrypt((SecretKey) key);
        } else if (opmode == Cipher.DECRYPT_MODE) {
            theBlockCipher.initDecrypt((SecretKey) key);
        }
        IV = new byte[theBlockCipher.getBlockSize()];
        for (int i = 0, j = 0; i < IV.length; i++, j = (j + 1) % AW.length) IV[i] = AW[j];
        IBuffer = new byte[theBlockCipher.getBlockSize()];
        System.arraycopy(IV, 0, IBuffer, 0, IBuffer.length);
        thePaddingScheme.setBlockSize(r);
    }

    /**
       * Initialises this cipher with a key, a set of algorithm parameters,
      * and a source of randomness.
      * <p>
     * The cipher is initialised for encryption or decryption, depending
     * on the value of opmode.
     * <p>
     * If this cipher requires any algorithm parameters and params is
     * null, the underlying cipher implementation is supposed to generate
     * the required parameters itself (using provider-specific default or
     * random values) if it is being initialised for encryption, and
    * raise an InvalidAlgorithmParameterException if it is being
      * initialised for decryption. The generated parameters can be
    * retrieved using engineGetParameters or engineGetIV (if the
     * parameter is an IV).
       * <p>
       * If this cipher (including its underlying feedback or padding
       * scheme) requires any random bytes (e.g., for parameter
       * generation), it will get them from random.
       * <p>
       * Note that when a Cipher object is initialised, it loses all
       * previously-acquired state. In other words, initialising a Cipher
       * is equivalent to creating a new instance of that Cipher and
       * initialising it.
       * <p>
       * @param opmode the operation mode of this cipher (this is either
       *		    ENCRYPT_MODE or DECRYPT_MODE)
       * @param key the encryption key
       * @param params the algorithm parameters
       * @param random the source of randomness
      *
       * @exception InvalidKeyException if the given key is inappropriate
       *    for initialising this cipher
       */
    public void engineInit(int opmode, Key key, AlgorithmParameterSpec params, SecureRandom random) throws InvalidKeyException, java.security.InvalidAlgorithmParameterException {
        algParamSpec = params;
        IV = getIVFromAlgorithmParameterSpec(params);
        this.opmode = opmode;
        if (opmode == Cipher.ENCRYPT_MODE) {
            theBlockCipher.initEncrypt((SecretKey) key, algParamSpec);
        } else if (opmode == Cipher.DECRYPT_MODE) {
            theBlockCipher.initDecrypt((SecretKey) key, algParamSpec);
        }
        thePaddingScheme.setBlockSize(r);
    }

    /**
     * Initialises this cipher with a key, a set of algorithm parameters,
     * and a source of randomness.
     * <p>
     * The cipher is initialised for encryption or decryption, depending
     * on the value of opmode.
     * <p>
     * If this cipher requires any algorithm parameters and params is
     * null, the underlying cipher implementation is supposed to generate
     * the required parameters itself (using provider-specific default or
     * random values) if it is being initialised for encryption, and
     * raise an InvalidAlgorithmParameterException if it is being
     * initialised for decryption. The generated parameters can be
     * retrieved using engineGetParameters or engineGetIV (if the
     * parameter is an IV).
    * <p>
     * If this cipher (including its underlying feedback or padding
     * scheme) requires any random bytes (e.g., for parameter
     * generation), it will get them from random.
     * <p>
     * Note that when a Cipher object is initialised, it loses all
     * previously-acquired state. In other words, initialising a Cipher
     * is equivalent to creating a new instance of that Cipher and
     * initialising it.
     * <p>
     * @param opmode the operation mode of this cipher (this is either
     *    ENCRYPT_MODE or DECRYPT_MODE)
     * @param key the encryption key
     * @param params the algorithm parameters
     * @param random the source of randomness
     * @exception InvalidKeyException if the given key is
     *    inappropriate for initialising this cipher
     */
    public void engineInit(int opmode, Key key, AlgorithmParameters params, SecureRandom random) throws java.security.InvalidKeyException, java.security.InvalidAlgorithmParameterException {
        algParamSpec = algorithmParametersToAlgorithmParameterSpec(params);
        IV = getIVFromAlgorithmParameterSpec(algParamSpec);
        this.opmode = opmode;
        if (opmode == Cipher.ENCRYPT_MODE) {
            theBlockCipher.initEncrypt((SecretKey) key, algParamSpec);
        } else if (opmode == Cipher.DECRYPT_MODE) {
            theBlockCipher.initDecrypt((SecretKey) key, algParamSpec);
        }
        thePaddingScheme.setBlockSize(r);
    }

    /**
     * Continues a multiple-part encryption or decryption operation
     * (depending on how this cipher was initialised), processing another
     * data part.
     * <p>
     * The first inputLen bytes in the input buffer, starting at
     * inputOffset, are processed, and the result is stored in a new buffer.
     *
     * @param input the input buffer
     * @param inputOffset the offset in input where the input starts
     * @param inputLen the input length
     * @return the new buffer with the result, or null if the underlying
     *    cipher is a block cipher and the input data is too short to
     *    result in a new block.
     */
    public byte[] engineUpdate(byte[] input, int inputOff, int inputLen) {
        int i;
        int inputBufferForCFBLen = leftoverBufferLen + inputLen;
        int tooMuchInput = inputBufferForCFBLen % r;
        inputBufferForCFBLen -= tooMuchInput;
        if (inputBufferForCFBLen == 0) {
            if (input != null) System.arraycopy(input, inputOff, leftoverBuffer, leftoverBufferLen, inputLen);
            leftoverBufferLen += inputLen;
            return null;
        } else {
            byte[] inputBufferForCFB = new byte[inputBufferForCFBLen];
            System.arraycopy(leftoverBuffer, 0, inputBufferForCFB, 0, leftoverBufferLen);
            if (input != null) {
                System.arraycopy(input, inputOff, inputBufferForCFB, leftoverBufferLen, inputBufferForCFBLen - leftoverBufferLen);
                System.arraycopy(input, inputLen + inputOff - tooMuchInput, leftoverBuffer, 0, tooMuchInput);
            }
            leftoverBufferLen = tooMuchInput;
            return doTheCFB(inputBufferForCFB);
        }
    }

    /**
       * Continues a multiple-part encryption or decryption operation
       * (depending on how this cipher was initialised), processing another
       * data part.
       * <p>
       * The first inputLen bytes in the input buffer, starting at
       * inputOffset, are processed, and the result is stored in the output
       * buffer, starting at outputOffset.
       * <p>
       * If the output buffer is too small to hold the result, a
       * ShortBufferException is thrown. In this case, repeat this call with
       * a larger output buffer. Use getOutputSize to determine how big the
       * output buffer should be.
       *
       * @param input the input buffer
       * @param inputOffset the offset in input where the input starts
       * @param inputLen the input length
       * @param output the buffer for the result
       * @param outputOffset the offset in output where the result is stored
       *
       * @exception  ShortBufferException if the given output buffer is too
       *     small to hold the result
       *
       * @return the number of bytes stored in output
       */
    public int engineUpdate(byte[] input, int inputOff, int inputLen, byte[] output, int outputOff) throws ShortBufferException {
        byte[] helpBuffer = engineUpdate(input, inputOff, inputLen);
        if (helpBuffer != null) {
            try {
                System.arraycopy(helpBuffer, 0, output, outputOff, helpBuffer.length);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw (new ShortBufferException("Output Buffer too short!"));
            }
            return (helpBuffer.length);
        } else {
            return (0);
        }
    }

    /**
     *
    * A plaintext, p, is encrypted to a ciphertext, c, in three steps:<BR>
    * <OL>
    *   <LI> A buffer as large as the blocksize of the underlying cipher
    *   is encrypted using the bockcipher. This buffer is filled, initally
    *  with the initialization verctor (IV).</LI>
    *
    *  <LI>The desired number of leftmost bits (<TT>r</TT>) of the encrypted
    *  bits of the encrypted buffer are XORed with the plaintext. The result
    *  is the ciphertext output. The remainder of the encrypted buffer is
    *  discarded. </LI>
    *
    *  <LI>The original buffer is shifted to the left by the desired number of bits.
    *  The ciphertext is used to fill in the empty space of the right side of the
    *  buffer. This buffer will be used in the next encryption. As this process
    *  continues, the buffer will become entirely filled with ciphertext.</LI>
    * </OL></P>
    * <P>
    * Decryptionfollows the same process except for step two:
    * <OL>
    *  <LI>The buffer is encrypted using the blockcipher. Even though we are
    *  <I>decrypting</I> a cipher text, we still use the block cipher to
    *  <I>encrpyt</I> the buffer.</LI>
    *
    *  <LI>The leftmost bits of the encrypted buffer are XORed with the ciphertext,
    *  producing the plaintext output. Again the remainder of the encrypted buffer is
    *  discarded.</LI>
    *
    *  <LI>The original buffer is shifted left and filled with the ciphertext. The
    *  buffer will be used again in the next decryption.</LI>
    * </OL></P>
    */
    private byte[] doTheCFB(byte[] input) {
        int i, j;
        byte[] OBuffer = new byte[IBuffer.length];
        byte[] cBuffer = new byte[input.length];
        int numberOfBlocks = input.length / r;
        for (i = 0; i < numberOfBlocks; i++) {
            theBlockCipher.singleBlockEncrypt(IBuffer, 0, OBuffer, 0);
            byte[] tBuffer = new byte[r];
            System.arraycopy(OBuffer, 0, tBuffer, 0, r);
            for (j = 0; j < r; j++) cBuffer[(i * r) + j] = (byte) ((int) input[(i * r) + j] ^ (int) tBuffer[j]);
            for (j = IBuffer.length - 1; j >= r; j--) IBuffer[j] = IBuffer[j - r];
            if (this.opmode == Cipher.ENCRYPT_MODE) {
                for (j = 0; j < r; j++) IBuffer[j] = cBuffer[(i * r) + j];
            } else {
                for (j = 0; j < r; j++) IBuffer[j] = input[(i * r) + j];
            }
        }
        return cBuffer;
    }

    /**
	   * Encrypts or decrypts data in a single-part operation, or
	   * finishes a multiple-part operation. The data is encrypted or
	   * decrypted, depending on how this cipher was initialised.
	   * <p>
	   * The first inputLen bytes in the input buffer, starting at
	   * inputOffset, and any input bytes that may have been buffered during
	   * a previous update operation, are processed, with padding (if
	   * requested) being applied. The result is stored in a new buffer.
	   * <p>
	   * The cipher is reset to its initial state (uninitialised) after
	   * this call.
	   *
	   * @param input the input buffer
	   * @param inputOffset the offset in input where the input starts
	   * @param inputLen the input length
	   *
	   * @exception IllegalBlockSizeException if this cipher is a block
	   *      cipher, no padding has been requested (only in encryption
	   *      mode), and the total input length of the data processed by
	   *      this cipher is not a multiple of block size
	   * @exception BadPaddingException if this cipher is in decryption mode,
	   *      and (un)padding has been requested, but the decrypted data is
	   *      not bounded by the appropriate padding bytes.
	   *
	   * @return the new buffer with the result
	   */
    public byte[] engineDoFinal(byte input[], int inputOffset, int inputLen) throws IllegalBlockSizeException, BadPaddingException {
        int i, j;
        int padLength = 0;
        int inputBufferForCFBLen = leftoverBufferLen + inputLen;
        thePaddingScheme.setBlockSize(r);
        if (opmode == Cipher.ENCRYPT_MODE) {
            padLength = thePaddingScheme.padLength(inputBufferForCFBLen);
            inputBufferForCFBLen += padLength;
        }
        byte[] inputBufferForCFB = new byte[inputBufferForCFBLen];
        System.arraycopy(leftoverBuffer, 0, inputBufferForCFB, 0, leftoverBufferLen);
        if (input != null) System.arraycopy(input, inputOffset, inputBufferForCFB, leftoverBufferLen, inputLen);
        leftoverBufferLen = 0;
        if (opmode == Cipher.ENCRYPT_MODE) {
            try {
                thePaddingScheme.pad(inputBufferForCFB, 0, inputBufferForCFBLen - padLength);
            } catch (BadPaddingException bpe) {
                System.err.println(bpe);
                return null;
            }
        }
        if (inputBufferForCFBLen == 0) {
            for (i = 0, j = 0; i < IV.length; i++, j = (j + 1) % AW.length) IV[i] = AW[j];
            System.arraycopy(IV, 0, IBuffer, 0, IBuffer.length);
            return null;
        } else {
            byte[] result = doTheCFB(inputBufferForCFB);
            if (opmode == Cipher.DECRYPT_MODE) {
                int finalLength = thePaddingScheme.unpad(result, 0, result.length);
                byte[] finalresult = new byte[finalLength];
                System.arraycopy(result, 0, finalresult, 0, finalLength);
                for (i = 0, j = 0; i < IV.length; i++, j = (j + 1) % AW.length) IV[i] = AW[j];
                System.arraycopy(IV, 0, IBuffer, 0, IBuffer.length);
                return finalresult;
            } else {
                for (i = 0, j = 0; i < IV.length; i++, j = (j + 1) % AW.length) IV[i] = AW[j];
                System.arraycopy(IV, 0, IBuffer, 0, IBuffer.length);
                return result;
            }
        }
    }

    /**
       * Encrypts or decrypts data in a single-part operation, or finishes
       * a multiple-part operation. The data is encrypted or decrypted,
       * depending on how this cipher was initialised.
       * <p>
       * The first inputLen bytes in the input buffer, starting at
       * inputOffset, and any input bytes that may have been buffered during
       * a previous update operation, are processed, with padding (if
       * requested) being applied. The result is stored in the output buffer,
       * starting at outputOffset.
       * <p>
       * If the output buffer is too small to hold the result, a
       * ShortBufferException is thrown.  In this case, repeat this call
       * with a larger output buffer. Use getOutputSize to determine how
       * big the output buffer should be.
       *
       * @param input the input buffer
       * @param inputOffset - the offset in input where the input starts
       * @param inputLen - the input length
       * @param output - the buffer for the result
       * @param outputOffset - the offset in output where the result is stored
       *
       * @exception IllegalBlockSizeException if this cipher is a block
       *    cipher, no padding has been requested (only in encryption mode),
       *    and the total input length of the data processed by this cipher
       *    is not a multiple of block size
       * @exception ShortBufferException if the given output buffer is too
       *    small to hold the result
       * @exception BadPaddingException if this cipher is in decryption mode,
       *     and (un)padding has been requested, but the decrypted data is
       *     not bounded by the appropriate padding bytes
       * @returns the number of bytes stored in output
       */
    public int engineDoFinal(byte[] input, int inputOff, int inputLen, byte[] output, int outputOff) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        byte[] helpBuffer = engineDoFinal(input, inputOff, inputLen);
        if (helpBuffer != null) {
            try {
                System.arraycopy(helpBuffer, 0, output, outputOff, helpBuffer.length);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw (new ShortBufferException("Outputbuffer too short!"));
            }
            return (helpBuffer.length);
        } else {
            return (0);
        }
    }
}
