package net.bpfurtado.ljcolligo.util;

public class Password {

    public static void main(String[] args) {
        new Password().encrypt("Vikao");
    }

    private void encrypt(String text) {
        StringBuffer buffer = new StringBuffer();
        for (int charPosition = 0; charPosition < text.length(); charPosition++) {
            char charAt = text.charAt(charPosition);
            char encryptedChar = '*';
            if (charAt >= '0' && charAt <= '9') {
                encryptedChar = encrypt(charAt, '0', '9');
            } else {
                encryptedChar = encrypt(charAt, 'A', 'z');
            }
            buffer.append(encryptedChar);
        }
        System.out.println(buffer);
    }

    private char encrypt(char origChar, char lowerBound, char upperBound) {
        int newCharNumber = (char) origChar + 45;
        if (newCharNumber > upperBound) {
            newCharNumber = (newCharNumber - upperBound) + lowerBound;
        }
        return (char) newCharNumber;
    }

    @SuppressWarnings("unused")
    private void print(char origChar, int charNumber) {
        print(origChar, charNumber, null);
    }

    private void print(char oldChar, int charNumber, String prefix) {
        char c = (char) charNumber;
        String completePrefix = prefix == null ? "" : prefix + ": ";
        System.out.println(completePrefix + oldChar + " = " + charNumber + "[" + c + "]");
    }
}
