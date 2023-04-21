package goldengate.common.crypto;

/**
 * This class handles methods to encrypt and unencrypt messages with any available
 * algorithms in the JVM.<br>
 * <b>AES is the best compromise in term of security and efficiency.</b>
 * <br>
 * Usage:<br>
 * <ul>
 * <li>Create a Key object: DynamicKey key = new DynamicKey(size,algo,instance,extension);<br>
 * As DynamicKey(56, "DES", "DES/ECB/PKCS5Padding", "des") for DES<br>
 * or through Enum class INSTANCES (minimal supported size) or INSTANCESMAX (maximal supported size) as DynamicKey(INSTANCESMAX.AES,"aes")</li>
 * <li>Create a key:
 * <ul>
 * <li>Generate: key.generateKey();<br>
 * The method key.getSecretKeyInBytes() allow getting the key in Bytes.</li>
 * <li>From an external source: key.setSecretKey(arrayOfBytes);</li>
 * </ul>
 * </li>
 * <li>To encrypt a String in a Base64 format: String myStringCrypt =
 * key.cryptToString(myString);</li>
 * <li>To unencrypt one string from Base64 format to the original String: String
 * myStringDecrypt = key.decryptStringInString(myStringCrypte);</li>
 * </ul>
 * 
 * @author frederic bregier
 * 
 */
public class DynamicKeyObject extends KeyObject {

    /**
     * Minimal key size
     * 
     * @author Frederic Bregier
     * 
     */
    public enum INSTANCES {

        AES(128), ARCFOUR(56), Blowfish(56), DES(56), DESede(112), RC2(56), RC4(56);

        int size;

        private INSTANCES(int size) {
            this.size = size;
        }
    }

    /**
     * Recommended key size when normal JVM installed (no extension on encrypt
     * support)
     * 
     * @author Frederic Bregier
     * 
     */
    public enum INSTANCESMAX {

        AES(128), ARCFOUR(128), Blowfish(128), DES(56), DESede(168), RC2(128), RC4(128);

        int size;

        private INSTANCESMAX(int size) {
            this.size = size;
        }
    }

    /**
     * This value could be between 32 and 128 due to license limitation.
     */
    public final int KEY_SIZE;

    /**
     * Short name for the algorithm
     */
    public final String ALGO;

    /**
     * Could be the shortname again (default implementation in JVM) or the full name as DES/ECB/PKCS5Padding
     */
    public final String INSTANCE;

    /**
     * The extension for the file to use when saving the key (note that an extra file as extension.inf will be also saved for the extra information)
     */
    public final String EXTENSION;

    /**
     * @param kEYSIZE
     *            example DES: 56
     * @param aLGO
     *            example DES: DES
     * @param iNSTANCE
     *            example DES: DES/ECB/PKCS5Padding
     * @param eXTENSION
     *            example DES: des
     */
    public DynamicKeyObject(int kEYSIZE, String aLGO, String iNSTANCE, String eXTENSION) {
        super();
        KEY_SIZE = kEYSIZE;
        ALGO = aLGO;
        INSTANCE = iNSTANCE;
        EXTENSION = eXTENSION;
    }

    /**
     * 
     * @param instance the minimal default instance
     * @param eXTENSION to use for files
     */
    public DynamicKeyObject(INSTANCES instance, String eXTENSION) {
        super();
        KEY_SIZE = instance.size;
        ALGO = instance.name();
        INSTANCE = instance.name();
        EXTENSION = eXTENSION;
    }

    /**
     * 
     * @param instance the maximal default instance
     * @param eXTENSION to use for files
     */
    public DynamicKeyObject(INSTANCESMAX instance, String eXTENSION) {
        super();
        KEY_SIZE = instance.size;
        ALGO = instance.name();
        INSTANCE = instance.name();
        EXTENSION = eXTENSION;
    }

    @Override
    public String getAlgorithm() {
        return ALGO;
    }

    @Override
    public String getInstance() {
        return INSTANCE;
    }

    @Override
    public int getKeySize() {
        return KEY_SIZE;
    }

    /**
     * This method allows to test the correctness of this class
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String plaintext = null;
        if (args.length != 0) {
            plaintext = args[0];
        }
        if (plaintext == null || plaintext.length() == 0) {
            plaintext = "This is a try for a very long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long long String";
        }
        System.out.println("plaintext = " + plaintext);
        System.out.println("=====================================");
        for (INSTANCES instance : INSTANCES.values()) {
            test(plaintext, instance.size, instance.name());
        }
        for (INSTANCESMAX instance : INSTANCESMAX.values()) {
            test(plaintext, instance.size, instance.name());
        }
    }

    /**
     * test function
     * 
     * @param plaintext
     * @param size
     * @param algo
     * @throws Exception
     */
    private static void test(String plaintext, int size, String algo) throws Exception {
        DynamicKeyObject dyn = new DynamicKeyObject(size, algo, algo, algo);
        dyn.generateKey();
        byte[] secretKey = dyn.getSecretKeyInBytes();
        byte[] ciphertext = dyn.crypt(plaintext);
        dyn.setSecretKey(secretKey);
        String plaintext2 = dyn.decryptInString(ciphertext);
        if (!plaintext2.equals(plaintext)) System.out.println("Error: plaintext2 != plaintext");
        int nb = 100000;
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < nb; i++) {
            String cipherString = dyn.cryptToHex(plaintext);
            String plaintext3 = dyn.decryptHexInString(cipherString);
            if (!plaintext3.equals(plaintext)) System.out.println("Error: plaintext3 != plaintext");
        }
        long time2 = System.currentTimeMillis();
        System.out.println(algo + ": Total time: " + (time2 - time1) + " ms, " + (nb * 1000 / (time2 - time1)) + " crypt or decrypt/s");
    }
}
