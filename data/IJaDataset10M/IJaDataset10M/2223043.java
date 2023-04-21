package triebag.tries;

/**
 * Helper class for Turkish alphabet.
 * 
 * @author mdakin
 *
 */
public class TurkishAlphabet implements Alphabet {

    public static final char CHAR_CC = 'Ç';

    public static final char CHAR_cc = 'ç';

    public static final char CHAR_GG = 'Ğ';

    public static final char CHAR_gg = 'ğ';

    public static final char CHAR_ii = 'ı';

    public static final char CHAR_II = 'İ';

    public static final char CHAR_OO = 'Ö';

    public static final char CHAR_oo = 'ö';

    public static final char CHAR_SS = 'Ş';

    public static final char CHAR_ss = 'ş';

    public static final char CHAR_UU = 'Ü';

    public static final char CHAR_uu = 'ü';

    public static final char CHAR_SAPKALI_A = 'Â';

    public static final char CHAR_SAPKALI_a = 'â';

    public static final char CHAR_SAPKALI_I = 'Î';

    public static final char CHAR_SAPKALI_i = 'î';

    public static final char CHAR_SAPKALI_U = 'Û';

    public static final char CHAR_SAPKALI_u = 'û';

    public static char[] alphabet = { 'a', 'b', 'c', CHAR_cc, 'd', 'e', 'f', 'g', CHAR_gg, 'h', CHAR_ii, 'i', 'j', 'k', 'l', 'm', 'n', 'o', CHAR_oo, 'p', 'r', 's', CHAR_ss, 't', 'u', CHAR_uu, 'v', 'y', 'z', CHAR_SAPKALI_a, CHAR_SAPKALI_i, CHAR_SAPKALI_u, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '#', '-', '\'', '"', ',', '!', '.' };

    public static final int ALPHABET_SIZE = alphabet.length;

    public static char[] alphabetCapital = { 'A', 'B', 'C', CHAR_CC, 'D', 'E', 'F', 'G', CHAR_GG, 'h', 'I', CHAR_II, 'J', 'K', 'L', 'M', 'N', 'O', CHAR_OO, 'P', 'R', 'S', CHAR_SS, 'T', 'U', CHAR_UU, 'V', 'Y', 'Z', CHAR_SAPKALI_A, CHAR_SAPKALI_I, CHAR_SAPKALI_U, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '#', '-', '\'', '"', ',', '!', '.' };

    public static final int TURKISH_CHAR_MAP_SIZE = 610;

    public static int turkishLetters[] = new int[TURKISH_CHAR_MAP_SIZE];

    public static int toLower[] = new int[TURKISH_CHAR_MAP_SIZE];

    public static char[] vowels = { 'a', 'e', CHAR_ii, 'i', 'o', CHAR_oo, 'u', CHAR_uu, CHAR_SAPKALI_a, CHAR_SAPKALI_i, CHAR_SAPKALI_u };

    public static char[] voiceless = { 'p', CHAR_cc, 't', 'k' };

    public static boolean vowelLookup[] = new boolean[alphabet.length];

    public static boolean voicelessLookup[] = new boolean[alphabet.length];

    static {
        for (int i = 0; i < TURKISH_CHAR_MAP_SIZE; i++) {
            turkishLetters[i] = -1;
            toLower[i] = -1;
        }
        for (int i = 0; i < alphabet.length; i++) {
            turkishLetters[alphabet[i]] = i;
            toLower[alphabetCapital[i]] = alphabet[i];
            vowelLookup[i] = false;
            voicelessLookup[i] = false;
        }
        for (char vowel : vowels) {
            vowelLookup[turkishLetters[vowel]] = true;
        }
        for (char c : voiceless) {
            voicelessLookup[turkishLetters[c]] = true;
        }
    }

    public boolean isValid(char c) {
        return getIndex(c) != -1;
    }

    public int getIndex(char c) {
        if (c < 0 || c > TURKISH_CHAR_MAP_SIZE) {
            throw new IllegalArgumentException("Illegal char: " + c);
        }
        if (turkishLetters[c] < 0) {
            throw new IllegalArgumentException("Char not in alphabet: " + c);
        }
        return turkishLetters[c];
    }

    public char getChar(int index) {
        assert (index > 0 && index < alphabet.length);
        if (index < 0 || index > alphabet.length) {
            throw new IllegalArgumentException("illegal index: " + index);
        }
        return alphabet[index];
    }

    public boolean isVowel(char c) {
        if (!isValid(c)) throw new IllegalArgumentException("not a valid char:" + c);
        return vowelLookup[getIndex(c)];
    }

    public boolean isVoiceless(char c) {
        if (!isValid(c)) throw new IllegalArgumentException("not a valid char:" + c);
        return voicelessLookup[getIndex(c)];
    }

    public int getSize() {
        return alphabet.length;
    }

    @Override
    public byte[] toIndexes(String s) {
        byte[] indexedChars = new byte[s.length()];
        for (int i = 0; i < indexedChars.length; i++) {
            indexedChars[i] = (byte) getIndex(s.charAt(i));
        }
        return indexedChars;
    }
}
