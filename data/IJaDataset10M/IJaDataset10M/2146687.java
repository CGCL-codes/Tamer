package wincrypt;

import java.io.File;
import java.io.UnsupportedEncodingException;
import cipher.AdditionalInformationRequester;
import cipher.Cipher;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.codec.binary.Base64;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.MiscUtilities;
import org.gjt.sp.jedit.OperatingSystem;
import org.gjt.sp.jedit.PluginJAR;

/**
 * <p>A wincrypt cipher implementation for the CipherPlugin.</p>
 * 
 * <p>The jEdit ServicesAPI name of this implementation is &quot;wincrypt&quot;.</p>
 * 
 * <p>This plugin uses the Windows CryptoAPI to encrypt data. It uses the
 * Windows user login credentials to encrypt data and so decryption can
 * only happen by the same logged in user, in most cases also only on the
 * same machine. This API works on {@code byte} arrays. Because of that, the
 * {@link #encryptToString()} method transforms the result with Base64
 * into a {@code String} and the {@link #setEncryptedData(String)}
 * method expects Base64 encoded data.</p>
 * 
 * <p>The additional information this implementation expects is
 * either exactly one {@code String}
 * object describing the data to be encrypted or decrypted, or
 * none in which case the description is assumed an empty string.
 * The description gets encrypted with the data
 * and on decryption, it is used to verify the process.</p>
 * 
 * <p>This class is thread-safe, the lock for the mutable state is this instance.
 * To ensure the atomicity of the encrypting or decrypting
 * process in a thread safe manner, the subsequent calls to
 * <ul><li>one of the {@code set*Data()},</li>
 * <li>one of the {@code setEntropy()},</li>
 * <li>the {@code setAdditionalInformation()} and</li>
 * <li>one of the {@code *cryptTo*()} methods</li></ul>
 * should all be made from within one {@code synchronized} block,
 * locking on this instance.</p>
 * 
 * @author Bj�rn "Vampire" Kautler
 * @since WincryptCipherPlugin 0.1
 * @see cipher.Cipher
 * @see <a href="http://msdn2.microsoft.com/en-us/library/aa380261.aspx">CryptProtectData</a>
 * @see <a href="http://msdn2.microsoft.com/en-us/library/aa380882.aspx">CryptUnprotectData</a>
 */
@SuppressWarnings(value = "DM_GC", justification = "Garbage Collection is needed to eventually free the DLL if it is blocked by a \"dead\" ClassLoaded")
@ThreadSafe
public class WincryptCipher implements Cipher {

    @GuardedBy("this")
    private byte[] rawData;

    @GuardedBy("this")
    private byte[] encryptedData;

    @GuardedBy("this")
    private byte[] entropy;

    @GuardedBy("this")
    private char[] description;

    static {
        if (OperatingSystem.isWindows()) {
            PluginJAR wincryptCipherPluginJAR = jEdit.getPlugin("wincrypt.WincryptCipherPlugin").getPluginJAR();
            String dllPath = MiscUtilities.constructPath(MiscUtilities.getParentOfPath(wincryptCipherPluginJAR.getPath()), "WincryptCipher.dll");
            if (!new File(dllPath).exists()) {
                jEdit.removePluginJAR(wincryptCipherPluginJAR, false);
                jEdit.addPluginJAR(wincryptCipherPluginJAR.getPath());
                throw new UnsatisfiedLinkError("Missing WincryptCipher.dll");
            }
            boolean loaded = false;
            int tries = 0;
            UnsatisfiedLinkError ule = null;
            while (!loaded && (tries < 5)) {
                tries++;
                try {
                    System.load(dllPath);
                    loaded = true;
                } catch (UnsatisfiedLinkError innnerUle) {
                    ule = innnerUle;
                    System.gc();
                }
            }
            if (!loaded) {
                throw ule;
            }
        }
    }

    /**
     * Constructs a new {@code WincryptCipher}.
     */
    public WincryptCipher() {
        rawData = new byte[0];
        encryptedData = new byte[0];
        entropy = new byte[0];
        description = new char[0];
    }

    /**
     * The actual encryption function which uses the Windows
     * CryptoAPI to encrypt the given data.
     * 
     * @param rawData     The raw data to encrypt
     * @param description The description used to verify decryption
     * @param entropy     The additional entropy like a password for example
     * @return The encrypted data as {@code byte} array or {@code null} if encryption was not successful
     * @see #decryptNative(byte[], char[], byte[])
     */
    @CheckForNull
    @CheckReturnValue(explanation = "If the encryption result is not of further interest, the process should not be invoked")
    private native byte[] encryptNative(@NonNull byte[] rawData, @NonNull char[] description, @NonNull byte[] entropy);

    /**
     * The actual decryption function which uses the Windows
     * CryptoAPI to decrypt the given data.
     * 
     * @param encryptedData The encrypted data to decrypt
     * @param description   The description used to encrypt the data to verify the data integrity
     * @param entropy       The additional entropy like a password for example
     * @return The decrpyted data as {@code byte} array or {@code null} if decryption was not successful
     * @see #encryptNative(byte[], char[], byte[])
     */
    @CheckForNull
    @CheckReturnValue(explanation = "If the decryption result is not of further interest, the process should not be invoked")
    private native byte[] decryptNative(@NonNull byte[] encryptedData, @NonNull char[] description, @NonNull byte[] entropy);

    /**
     * Checks if the Windows CryptoAPI is available.
     * 
     * @return Whether the Windows CryptoAPI is available or not.
     */
    @CheckReturnValue(explanation = "If the availability got requested it should be used")
    private native boolean isNativeAvailable();

    /**
     * <p>Sets the raw data for an encrypting process
     * from a {@code byte} array. If your raw data is a password you may
     * consider using {@link #setRawData(String)}.</p>
     * 
     * <p>If {@code rawData} is {@code null}, {@code rawData} is
     * set to an empty {@code byte} array.</p>
     * 
     * <p>The method is synchronized on this instance.</p>
     * 
     * @param rawData The raw data as a {@code byte} array
     * @see #setRawData(String)
     * @see cipher.Cipher#setRawData(byte[])
     */
    public synchronized void setRawData(@Nullable byte[] rawData) {
        if (null == rawData) {
            this.rawData = new byte[0];
        } else {
            this.rawData = new byte[rawData.length];
            System.arraycopy(rawData, 0, this.rawData, 0, rawData.length);
        }
    }

    /**
     * <p>Sets the raw data for an encrypting process
     * from a {@code String} object.</p>
     * 
     * <p>If {@code rawData} is {@code null}, {@code rawData} is
     * set to an empty {@code byte} array, otherwise
     * {@code rawData.getBytes("UTF-8")} is used to
     * transform the parameter to a {@code byte} array.</p>
     * 
     * <p>The method is synchronized on this instance.</p>
     * 
     * @param rawData The raw data as a {@code String} object
     * @see #setRawData(byte[])
     * @see cipher.Cipher#setRawData(java.lang.String)
     */
    public synchronized void setRawData(@Nullable String rawData) {
        if (null == rawData) {
            this.rawData = new byte[0];
        } else {
            try {
                this.rawData = rawData.getBytes("UTF-8");
            } catch (UnsupportedEncodingException uee) {
                InternalError ie = new InternalError("JVM doesn't support UTF-8");
                ie.initCause(uee);
                throw ie;
            }
        }
    }

    /**
     * <p>Sets the encrypted data for a decrypting process
     * from a {@code byte} array.</p>
     * 
     * <p>If {@code encryptedData} is {@code null},
     * {@code encryptedData} is set to an empty
     * {@code byte} array.</p>
     * 
     * <p>The method is synchronized on this instance.</p>
     * 
     * @param encryptedData The encrypted data as a {@code byte} array
     * @see #setEncryptedData(String)
     * @see cipher.Cipher#setEncryptedData(byte[])
     */
    public synchronized void setEncryptedData(@Nullable byte[] encryptedData) {
        if (null == encryptedData) {
            this.encryptedData = new byte[0];
        } else {
            this.encryptedData = new byte[encryptedData.length];
            System.arraycopy(encryptedData, 0, this.encryptedData, 0, encryptedData.length);
        }
    }

    /**
     * <p>Sets the encrypted data for a decrypting process
     * from a {@code String} object. Encrypted data in String
     * form is supposed to be Base64 encoded by this implementation.</p>
     * 
     * <p>If {@code encryptedData} is {@code null},
     * {@code encryptedData} is set to an empty
     * {@code byte} array, otherwise the {@code String}
     * gets Base64 decoded to transform the parameter
     * to a {@code byte} array.</p>
     * 
     * <p>The method is synchronized on this instance.</p>
     * 
     * @param encryptedData The encrypted data as a {@code String} object
     * @see #setEncryptedData(byte[])
     * @see cipher.Cipher#setEncryptedData(java.lang.String)
     */
    public synchronized void setEncryptedData(@Nullable String encryptedData) {
        if (null == encryptedData) {
            this.encryptedData = new byte[0];
        } else {
            this.encryptedData = Base64.decodeBase64(encryptedData.getBytes());
        }
    }

    /**
     * <p>Sets the entropy for an encrypting or decrypting process
     * from a {@code byte} array. The entropy could e. g. be a
     * &quot;fingerprint&quot; in byte data.</p>
     * 
     * <p>If {@code entropy} is {@code null}, {@code entropy}
     * is set to an empty {@code byte} array.</p>
     * 
     * <p>The method is synchronized on this instance.</p>
     * 
     * @param entropy The entropy as a {@code byte} array
     * @see #setEntropy(String)
     * @see cipher.Cipher#setEntropy(byte[])
     */
    public synchronized void setEntropy(@Nullable byte[] entropy) {
        if (null == entropy) {
            this.entropy = new byte[0];
        } else {
            this.entropy = new byte[entropy.length];
            System.arraycopy(entropy, 0, this.entropy, 0, entropy.length);
        }
    }

    /**
     * <p>Sets the entropy for an encrypting or decrypting process
     * from a {@code String} object. The entropy could e. g. be a
     * password, like a master password that is used to encrypt
     * all stored passwords.</p>
     * 
     * <p>If {@code entropy} is {@code null}, {@code entropy} is
     * set to an empty {@code byte} array, otherwise
     * {@code entropy.getBytes("UTF-8")} is used to
     * transform the parameter to a {@code byte} array.</p>
     * 
     * <p>The method is synchronized on this instance.</p>
     * 
     * @param entropy The entropy as a {@code String} object
     * @see #setEntropy(byte[])
     * @see cipher.Cipher#setEntropy(java.lang.String)
     */
    public synchronized void setEntropy(@Nullable String entropy) {
        if (null == entropy) {
            this.entropy = new byte[0];
        } else {
            try {
                entropy.getBytes("UTF-8");
            } catch (UnsupportedEncodingException uee) {
                InternalError ie = new InternalError("JVM doesn't support UTF-8");
                ie.initCause(uee);
                throw ie;
            }
        }
    }

    /**
     * <p>Returns an {@code AdditionalInformationRequester} that request
     * the needed additional information for an encrypting or
     * decrypting process. If no additional information is needed,
     * the method should return {@code null}.</p>
     * 
     * @return The additional information requester
     * @see cipher.Cipher#getAdditionalInformationRequester()
     */
    @CheckForNull
    @CheckReturnValue(explanation = "If the additional information requester got requested it should be used")
    public AdditionalInformationRequester getAdditionalInformationRequester() {
        return new WincryptCipherAdditionalInformationRequester();
    }

    /**
     * <p>Sets the additional information for an encrypting or
     * decrypting process from an {@code Object} vararg.</p>
     * 
     * <p>This implementation expects either exactly one {@code String}
     * object describing the data to be encrypted or decrypted, or
     * none in which case the description is assumed an empty string.
     * The description gets encrypted with the data and on
     * decryption, it is used to verify the process.</p>
     * 
     * <p>The method is synchronized on this instance.</p>
     * 
     * @param additionalInformation The description for the data
     * @throws IllegalArgumentException If there is not exactly zero or one {@code String} object given
     * @see cipher.Cipher#setAdditionalInformation(java.lang.Object[])
     */
    public synchronized void setAdditionalInformation(@Nullable Object... additionalInformation) {
        if ((null == additionalInformation) || (0 == additionalInformation.length)) {
            description = new char[0];
        } else if ((1 != additionalInformation.length) || !(additionalInformation[0] instanceof String)) {
            throw new IllegalArgumentException("additionalInformation has to be exactly one String object");
        } else {
            description = ((String) additionalInformation[0]).toCharArray();
        }
    }

    /**
     * <p>Encrypts the given raw data with the given entropy
     * and the given additional information. If you want
     * to store the encrypted data on some text device like
     * a text file, consider using {@link #encryptToString()}.</p>
     * 
     * <p>The method is synchronized on this instance.</p>
     * 
     * @return The encrypted data as a {@code byte} array or {@code null} if encryption was not successful
     * @see #encryptToString()
     * @see cipher.Cipher#encryptToByteArray()
     */
    @CheckForNull
    @CheckReturnValue(explanation = "If the encryption result is not of further interest, the process should not be invoked")
    public synchronized byte[] encryptToByteArray() {
        return encryptNative(rawData, description, entropy);
    }

    /**
     * <p>Decrypts the given encrypted data with the given
     * entropy and the given additional information.
     * If you want to decrypt a password, consider using
     * {@link #decryptToString()}.</p>
     * 
     * <p>The method is synchronized on this instance.</p>
     * 
     * @return The decrypted data as a {@code byte} array or {@code null} if decryption was not successful
     * @see #decryptToString()
     * @see cipher.Cipher#decryptToByteArray()
     */
    @CheckForNull
    @CheckReturnValue(explanation = "If the decryption result is not of further interest, the process should not be invoked")
    public synchronized byte[] decryptToByteArray() {
        return decryptNative(encryptedData, description, entropy);
    }

    /**
     * <p>Encrypts the given raw data with the given entropy
     * and the given additional information. The returned
     * {@code String} object is a Base64 encoded representation
     * of the encryptedData.</p>
     * 
     * <p>The method is not synchronized but forwards the work
     * to the {@code encryptToByteArray()} method
     * which is synchronized on this instance.</p>
     * 
     * @return The encrypted data as a Base64 encoded {@code String} object or {@code null} if encryption was not successful
     * @see #encryptToByteArray()
     * @see cipher.Cipher#encryptToString()
     */
    @CheckForNull
    @CheckReturnValue(explanation = "If the encryption result is not of further interest, the process should not be invoked")
    public String encryptToString() {
        byte[] encryptedData = encryptToByteArray();
        if (null == encryptedData) {
            return null;
        }
        return new String(Base64.encodeBase64(encryptedData));
    }

    /**
     * <p>Decrypts the given encrypted data with the given
     * entropy and the given additional information.
     * The decrypted data is transformed to a {@code String}
     * by using the method {@code new String(decryptedData,"UTF-8")}.</p>
     * 
     * <p>The method is not synchronized but forwards the work
     * to the {@code encryptToByteArray()} method
     * which is synchronized on this instance.</p>
     * 
     * @return The decrypted data as a {@code String} object or {@code null} if decryption was not successful
     * @see #decryptToByteArray()
     * @see cipher.Cipher#decryptToString()
     */
    @CheckForNull
    @CheckReturnValue(explanation = "If the decryption result is not of further interest, the process should not be invoked")
    public String decryptToString() {
        byte[] decryptedData = decryptToByteArray();
        if (null == decryptedData) {
            return null;
        }
        try {
            return new String(decryptedData, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            InternalError ie = new InternalError("JVM doesn't support UTF-8");
            ie.initCause(uee);
            throw ie;
        }
    }

    /**
     * <p>Checks if this {@code Cipher} implementation is
     * currently available. This method should return
     * the same value during one JVM session.</p>
     * 
     * <p>This implementation is unavailable if the operating
     * system is not Windows or if the Windows CryptoAPI
     * is not available.</p>
     * 
     * @return Whether the implementation is currently available
     * @see cipher.Cipher#isAvailable()
     */
    @CheckReturnValue(explanation = "If the availability got requested it should be used")
    public boolean isAvailable() {
        if (OperatingSystem.isWindows()) {
            return isNativeAvailable();
        } else {
            return false;
        }
    }
}
