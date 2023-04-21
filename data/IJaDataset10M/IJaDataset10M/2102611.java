package bwmorg.bouncycastle.bcpg;

/**
 * Basic tags for symmetric key algorithms
 */
public interface SymmetricKeyAlgorithmTags {

    public static final int NULL = 0;

    public static final int IDEA = 1;

    public static final int TRIPLE_DES = 2;

    public static final int CAST5 = 3;

    public static final int BLOWFISH = 4;

    public static final int SAFER = 5;

    public static final int DES = 6;

    public static final int AES_128 = 7;

    public static final int AES_192 = 8;

    public static final int AES_256 = 9;

    public static final int TWOFISH = 10;
}
