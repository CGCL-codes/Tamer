package emulator.cbm;

public class Petscii {

    public static byte asc2pet(char ch) {
        if ((ch >= 0x5B && ch < 0x60) || (ch > 0x60 && ch <= 0x7E)) {
            return (byte) (ch ^ 0x20);
        } else if (ch >= 'A' && ch <= 'Z') {
            return (byte) (ch | 0x80);
        } else if (ch == 0x0a) {
            return 0x0D;
        }
        return (byte) ch;
    }

    public static char pet2asc(char ch) {
        if ((ch >= 0x41 && ch <= 0x5E) || (ch >= 0x7B && ch <= 0x7F)) {
            return (char) (ch ^ 0x20);
        } else if (ch >= ('A' | 0x80) && ch <= ('Z' | 0x80)) {
            return (char) (ch & 0x7F);
        }
        return ch;
    }

    public static String pet2asc(String pet_str) {
        if (pet_str != null) {
            char[] asc_str = new char[pet_str.length()];
            for (int i = 0; i < pet_str.length(); i++) {
                asc_str[i] = pet2asc(pet_str.charAt(i));
            }
            return new String(asc_str);
        }
        return null;
    }
}
