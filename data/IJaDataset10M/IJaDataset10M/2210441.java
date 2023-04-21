package misc;

public class URLBase64 {

    private static final byte[] codes = { (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '+', (byte) '_' };

    private static boolean debug = false;

    public static void setDebug() {
        debug = true;
    }

    public static void clearDebug() {
        debug = false;
    }

    /**
     * Base 64 decode the input byte array into
     */
    public static byte[] decode(byte[] input, int inOffset, int inLength) throws Exception {
        byte[] decoded = new byte[(inLength * 3) >>> 2];
        int state = 0, j = 0;
        byte d = 0;
        if (debug) {
            System.out.println("\nBase64.decode:");
        }
        for (int i = 0; i < inLength; i++) {
            int c = (int) (input[i + inOffset]);
            if (debug) {
                System.out.println("c = " + c + " byte = " + input[i + inOffset]);
            }
            if ((int) 'A' <= c && c <= (int) 'Z') c = c - (int) 'A'; else if ((int) 'a' <= c && c <= (int) 'z') c = c - (int) 'a' + 26; else if ((int) '0' <= c && c <= (int) '9') c = c - (int) '0' + 52; else if (c == (int) '+') c = 62; else if (c == (int) '/') c = 63; else {
                if (debug) {
                    System.out.println("\nc = " + c + " byte = " + input[i + inOffset]);
                }
                throw new Exception("Illegal value");
            }
            switch(state++) {
                case 0:
                    d = (byte) (c << 2);
                    break;
                case 1:
                    d = (byte) (d | (byte) (c >> 4));
                    decoded[j++] = d;
                    d = (byte) (c << 4);
                    break;
                case 2:
                    d = (byte) (d | (byte) (c >> 2));
                    decoded[j++] = d;
                    d = (byte) (c << 6);
                    break;
                case 3:
                    decoded[j++] = (byte) (d | (byte) c);
                    state = 0;
                    break;
            }
        }
        return decoded;
    }

    /**
     * Base 64 encode the input byte array
     */
    public static byte[] encode(byte[] input) {
        byte[] encoded = new byte[((input.length << 3) + 5) / 6];
        int i = input.length / 3;
        int j = input.length % 3;
        if (debug) {
            System.out.println("\nEncode[" + encoded.length + "]: i = " + i + " j = " + j);
        }
        int k = 0, e = 0;
        int c = 0, d = 0;
        for (int l = 0; l < i; l++) {
            if (debug) System.out.println("k = " + k);
            c = (int) (input[k++] & 0XFF);
            encoded[e++] = codes[c >> 2];
            d = ((c & 0x03) << 4);
            c = (int) (input[k++] & 0XFF);
            d |= ((c >> 4) & 0x0F);
            encoded[e++] = codes[d];
            d = (c & 0x0F) << 2;
            c = (int) (input[k++] & 0xFF);
            d |= ((c >> 6) & 0x0F);
            encoded[e++] = codes[d];
            encoded[e++] = codes[c & 0x3F];
        }
        if (j != 0) {
            c = (int) (input[k++] & 0xFF);
            encoded[e++] = codes[(c >> 2) & 0x3F];
            c = (c << 4) & 0x30;
            switch(j) {
                case 1:
                    encoded[e] = codes[c];
                    break;
                case 2:
                    d = (int) (input[k] & 0xFF);
                    c = (int) (c | ((d >> 4) & 0x0F));
                    encoded[e++] = codes[c];
                    d = (byte) ((d << 2) & 0x3C);
                    encoded[e] = codes[d];
                    break;
            }
        }
        return encoded;
    }
}
