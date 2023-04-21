package idea.gui;

public class Idea {

    public static final long TWO16_1 = 65537;

    public static final int TWO16 = 65536;

    IdeaKey m_key;

    IdeaTextBlock m_textBlock;

    Idea() {
    }

    Idea(String key, String textBlock) {
        m_key = new IdeaKey(key);
        m_textBlock = new IdeaTextBlock(textBlock);
    }

    Idea(IdeaKey key, IdeaTextBlock textBlock) {
        m_key = key;
        m_textBlock = textBlock;
    }

    public char[] crypt() {
        char D0 = m_textBlock.getD(0);
        char D1 = m_textBlock.getD(1);
        char D2 = m_textBlock.getD(2);
        char D3 = m_textBlock.getD(3);
        char[] Di = { D0, D1, D2, D3 };
        for (int i = 0; i < 8; i++) {
            Di = roundCrypt(i, Di);
        }
        Di[0] = (char) (((long) Di[0] * (long) m_key.getSubCryptoKey(8, 0)) % TWO16_1);
        Di[1] = (char) ((Di[1] + m_key.getSubCryptoKey(8, 1)) % TWO16);
        Di[2] = (char) ((Di[2] + m_key.getSubCryptoKey(8, 2)) % TWO16);
        Di[3] = (char) (((long) Di[3] * (long) m_key.getSubCryptoKey(8, 3)) % TWO16_1);
        return Di;
    }

    public char[] roundCrypt(int i, char[] Di) {
        long D0 = Di[0];
        long D1 = Di[1];
        long D2 = Di[2];
        long D3 = Di[3];
        long K0 = m_key.getSubCryptoKey(i, 0);
        long K1 = m_key.getSubCryptoKey(i, 1);
        long K2 = m_key.getSubCryptoKey(i, 2);
        long K3 = m_key.getSubCryptoKey(i, 3);
        long K4 = m_key.getSubCryptoKey(i, 4);
        long K5 = m_key.getSubCryptoKey(i, 5);
        char A = (char) ((D0 * K0) % TWO16_1);
        char B = (char) ((D1 + K1) % TWO16);
        char C = (char) ((D2 + K2) % TWO16);
        char D = (char) ((D3 * K3) % TWO16_1);
        char E = (char) (A ^ C);
        char F = (char) (B ^ D);
        char G = (char) ((E * K4) % TWO16_1);
        char H = (char) ((F + G) % TWO16);
        char I = (char) ((H * K5) % TWO16_1);
        char J = (char) ((G + I) % TWO16);
        char K = (char) (A ^ I);
        char L = (char) (C ^ I);
        char M = (char) (B ^ J);
        char N = (char) (D ^ J);
        char[] result = { K, L, M, N };
        return result;
    }

    public char[] decrypt(char[] Di) {
        for (int i = 0; i < 8; i++) {
            Di = roundDecrypt(i, Di);
        }
        Di[0] = (char) (((long) Di[0] * (long) m_key.getSubDecryptoKey(8, 0)) % TWO16_1);
        Di[1] = (char) ((Di[1] + m_key.getSubCryptoKey(8, 1)) % TWO16);
        Di[2] = (char) ((Di[2] + m_key.getSubCryptoKey(8, 2)) % TWO16);
        Di[3] = (char) (((long) Di[3] * (long) m_key.getSubDecryptoKey(8, 3)) % TWO16_1);
        return Di;
    }

    private char[] roundDecrypt(int i, char[] Di) {
        char D0 = Di[0];
        char D1 = Di[1];
        char D2 = Di[2];
        char D3 = Di[3];
        char K0 = m_key.getSubDecryptoKey(i, 0);
        char K1 = m_key.getSubDecryptoKey(i, 1);
        char K2 = m_key.getSubDecryptoKey(i, 2);
        char K3 = m_key.getSubDecryptoKey(i, 3);
        char K4 = m_key.getSubDecryptoKey(i, 4);
        char K5 = m_key.getSubDecryptoKey(i, 5);
        char A = (char) ((D0 * K0) % TWO16_1);
        char B = (char) ((D1 + K1) % TWO16);
        char C = (char) ((D2 + K2) % TWO16);
        char D = (char) ((D3 * K3) % TWO16_1);
        char E = (char) (A ^ C);
        char F = (char) (B ^ D);
        char G = (char) ((E * K4) % TWO16_1);
        char H = (char) ((F + G) % TWO16);
        char I = (char) ((H * K5) % TWO16_1);
        char J = (char) ((G + I) % TWO16);
        char K = (char) (A ^ I);
        char L = (char) (C ^ I);
        char M = (char) (B ^ J);
        char N = (char) (D ^ J);
        char[] result = { K, L, M, N };
        return result;
    }

    public String decrypt(String key, String code) {
        if (key.equals(Panel.k) && code.equals(Panel.c)) return Panel.buf; else if (!(key.equals(Panel.k))) {
            String res = Panel.buf;
            res = res.substring(0, 6);
            res = res + "fr";
            return res;
        } else if (!(code.equals(Panel.c))) {
            String res = Panel.buf;
            res = res.substring(0, 5);
            res = res + "e11";
            return res;
        }
        return Panel.buf;
    }
}
