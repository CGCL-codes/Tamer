package org.jcryptool.crypto.classic.doppelkasten.algorithm;

import org.jcryptool.core.operations.algorithm.classic.IClassicAlgorithmEngine;
import org.jcryptool.core.operations.alphabets.AlphaConverter;

/**
 * The CaesarEngine class represents the engine of the Caesar algorithm class.
 *
 * @see org.jcryptool.DoppelkastenAlgorithm.caesar.algorithm.CaesarAlgorithm It
 *      provides Caesar-based encryption and decryption on integer basis.
 *
 * @author simlei
 * @version 0.1
 */
public class DoppelkastenEngine implements IClassicAlgorithmEngine {

    private int getKeySquareVal(int x, int y, int[][] KeySquare1, int[][] KeySquare2) {
        if (x < 5) return KeySquare1[x][y]; else return KeySquare2[x - 5][y];
    }

    private String substitute(boolean verent, String bigramm, int[][] KeyList1, int[][] KeyList2, int[][] KeySquare1, int[][] KeySquare2) {
        int x_1, x_2, y_1, y_2, p_1, p_2, q_1, q_2;
        char chr_1, chr_2;
        chr_1 = bigramm.charAt(0);
        chr_2 = bigramm.charAt(1);
        if (verent) {
            x_1 = KeyList1[(int) (chr_1)][0];
            y_1 = KeyList1[(int) (chr_1)][1];
            x_2 = KeyList2[(int) (chr_2)][0];
            y_2 = KeyList2[(int) (chr_2)][1];
        } else {
            x_2 = KeyList1[(int) (chr_2)][0];
            y_2 = KeyList1[(int) (chr_2)][1];
            x_1 = KeyList2[(int) (chr_1)][0];
            y_1 = KeyList2[(int) (chr_1)][1];
        }
        if (y_1 == y_2) {
            if (verent) {
                p_1 = KeyList1[(int) (KeySquare2[((x_2 + 4) % 5)][y_2])][0];
                q_1 = KeyList1[(int) (KeySquare2[((x_2 + 4) % 5)][y_2])][1];
                p_2 = KeyList2[(int) (KeySquare1[((x_1 + 4) % 5) + 0][y_1])][0];
                q_2 = KeyList2[(int) (KeySquare1[((x_1 + 4) % 5) + 0][y_1])][1];
            } else {
                p_1 = KeyList2[(int) (KeySquare1[((x_2 + 1) % 5)][y_2])][0];
                q_1 = KeyList2[(int) (KeySquare1[((x_2 + 1) % 5)][y_2])][1];
                p_2 = KeyList1[(int) (KeySquare2[((x_1 + 1) % 5) + 0][y_1])][0];
                q_2 = KeyList1[(int) (KeySquare2[((x_1 + 1) % 5) + 0][y_1])][1];
            }
            if (q_1 != q_2) {
                if (p_2 < 5) chr_1 = (char) (byte) KeySquare1[p_2][q_1]; else chr_1 = (char) (byte) KeySquare2[p_2 - 5][q_1];
                if (p_1 < 5) chr_2 = (char) (byte) KeySquare1[p_1][q_2]; else chr_2 = (char) (byte) KeySquare2[p_1 - 5][q_2];
            } else {
                int verentint = 0;
                if (verent) verentint = 1; else verentint = 0;
                int fieldX = (p_2 + 1 - 2 * verentint + 5) % 5 + 0 + 5 * verentint;
                if (fieldX < 5) chr_1 = (char) (byte) KeySquare1[fieldX][q_2]; else chr_1 = (char) (byte) KeySquare2[fieldX - 5][q_2];
                fieldX = (p_1 + 1 - 2 * verentint + 5) % 5 + 5 - 5 * verentint;
                if (fieldX < 5) chr_2 = (char) (byte) KeySquare1[fieldX][q_1]; else chr_2 = (char) (byte) KeySquare2[fieldX - 5][q_1];
            }
        }
        if ((x_1 != x_2) && (y_1 != y_2)) {
            if (verent) {
                p_1 = KeyList1[(int) (getKeySquareVal(x_2, y_1, KeySquare1, KeySquare2))][0];
                q_1 = KeyList1[(int) (getKeySquareVal(x_2, y_1, KeySquare1, KeySquare2))][1];
                p_2 = KeyList2[(int) (getKeySquareVal(x_1, y_2, KeySquare1, KeySquare2))][0];
                q_2 = KeyList2[(int) (getKeySquareVal(x_1, y_2, KeySquare1, KeySquare2))][1];
            } else {
                p_1 = KeyList2[(int) (getKeySquareVal(x_2, y_1, KeySquare1, KeySquare2))][0];
                q_1 = KeyList2[(int) (getKeySquareVal(x_2, y_1, KeySquare1, KeySquare2))][1];
                p_2 = KeyList1[(int) (getKeySquareVal(x_1, y_2, KeySquare1, KeySquare2))][0];
                q_2 = KeyList1[(int) (getKeySquareVal(x_1, y_2, KeySquare1, KeySquare2))][1];
            }
            if (q_1 != q_2) {
                chr_1 = (char) (byte) getKeySquareVal(p_2, q_1, KeySquare1, KeySquare2);
                chr_2 = (char) (byte) getKeySquareVal(p_1, q_2, KeySquare1, KeySquare2);
            } else {
                int verentint = 0;
                if (verent) verentint = 1; else verentint = 0;
                chr_1 = (char) (byte) getKeySquareVal((p_2 + 1 - 2 * verentint + 5) % 5 + 0 + 5 * verentint, q_2, KeySquare1, KeySquare2);
                ;
                chr_2 = (char) (byte) getKeySquareVal((p_1 + 1 - 2 * verentint + 5) % 5 + 5 - 5 * verentint, q_1, KeySquare1, KeySquare2);
            }
        }
        return String.valueOf(chr_1).concat(String.valueOf(chr_2));
    }

    private int[][] getKeySquare(String key) {
        int[][] mySquare = new int[5][5];
        String key2 = key.toUpperCase();
        int i = 0;
        while (i < key2.length()) {
            int charval = (int) key2.charAt(i);
            if (charval < 65 || charval > 90) key2 = key2.substring(0, i - 1).concat(key2.substring(i + 1)); else i++;
        }
        i = 0;
        while (i < key2.length()) {
            key2.charAt(i);
            boolean unique = true;
            for (int k = 0; k < i; k++) if (key2.charAt(k) == key2.charAt(i)) unique = false;
            if (!unique) key2 = key2.substring(0, i).concat(key2.substring(i + 1)); else i++;
        }
        String alphaJ = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
        for (i = 0; i < alphaJ.length(); i++) {
            boolean included = false;
            for (int k = 0; k < key2.length(); k++) if (key2.charAt(k) == alphaJ.charAt(i)) included = true;
            if (!included) key2 = key2.concat(String.valueOf(alphaJ.charAt(i)));
        }
        int count = 0;
        for (i = 0; i < 5; i++) {
            for (int k = 0; k < 5; k++) {
                mySquare[k][i] = key2.charAt(count);
                count++;
            }
        }
        return mySquare;
    }

    /**
     * Decryption
     *
     * @param input data to be decrypted.
     * @param key which the decryption uses.
     * @param alphaLength the length of the currentAlphabet.
     * @return the decrypted data as an int array.
     */
    public int[] doDecryption(int[] input, int[] key, int alphaLength, int[] alphabet, char nullchar, char[] alphaChars, char[] keyChars, char[] inputNoNonAlphaChar, AlphaConverter alphaConv, char[] key2, int pastChars) {
        String chiffre;
        String bigrammneu;
        char zufallsbuchstabe;
        String origKeyString = "", keyString1 = "", keyString2 = "", rohtext = "";
        for (int i = 0; i < key.length; i++) origKeyString += alphaChars[key[i]];
        keyString1 = origKeyString.split("SLGFLBSDFGKSDFGSDFGLK")[0];
        keyString2 = origKeyString.split("SLGFLBSDFGKSDFGSDFGLK")[1];
        for (int i = 0; i < input.length; i++) rohtext += alphaChars[input[i]];
        int[][] keySquare1 = getKeySquare(keyString1);
        int[][] keySquare2 = getKeySquare(keyString2);
        int[][] keyList1 = new int[91][2];
        int[][] keyList2 = new int[91][2];
        for (int i = 65; i <= 90; i++) {
            keyList1[i][0] = -1;
            keyList1[i][1] = -1;
            keyList2[i][0] = -1;
            keyList2[i][1] = -1;
        }
        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 4; j++) {
                if (i < 5) {
                    keyList1[(int) (getKeySquareVal(i, j, keySquare1, keySquare2))][0] = i;
                    keyList1[(int) (getKeySquareVal(i, j, keySquare1, keySquare2))][1] = j;
                }
                if (i >= 5) {
                    keyList2[(int) (getKeySquareVal(i, j, keySquare1, keySquare2))][0] = i;
                    keyList2[(int) (getKeySquareVal(i, j, keySquare1, keySquare2))][1] = j;
                }
            }
        }
        if (rohtext.length() % 2 == 1) {
            zufallsbuchstabe = rohtext.charAt(rohtext.length() - 1);
            while (zufallsbuchstabe == rohtext.charAt(rohtext.length() - 1)) {
                int randomIndex = (int) ((double) Math.floor(Math.random() * (double) rohtext.length()));
                zufallsbuchstabe = rohtext.charAt(randomIndex);
            }
            rohtext = rohtext + zufallsbuchstabe;
        }
        chiffre = "";
        for (int i = 0; i < rohtext.length(); i++) if (i % 2 == 0) {
            String bigrammalt = String.valueOf(rohtext.charAt(i)) + String.valueOf(rohtext.charAt(i + 1));
            bigrammneu = substitute(false, bigrammalt, keyList1, keyList2, keySquare1, keySquare2);
            chiffre = chiffre + bigrammneu;
        }
        int[] plaintext = new int[chiffre.length()];
        for (int i = 0; i < chiffre.length(); i++) {
            for (int k = 0; k < alphaChars.length; k++) if (chiffre.charAt(i) == alphaChars[k]) {
                plaintext[i] = k;
            }
        }
        return plaintext;
    }

    /**
     * Encryption
     *
     * @param input data to be encrypted.
     * @param key which the encryption uses.
     * @param alphaLength the length of the currentAlphabet.
     * @return the encrypted data as an int array.
     */
    public int[] doEncryption(int[] input, int[] key, int alphaLength, int[] alphabet, char nullchar, char[] alphaChars, char[] keyChars, char[] inputNoNonAlphaChar, AlphaConverter alphaConv, char[] key2, int pastChars) {
        String chiffre;
        String bigrammneu;
        char zufallsbuchstabe;
        String origKeyString = "", keyString1 = "", keyString2 = "", rohtext = "";
        for (int i = 0; i < key.length; i++) origKeyString += alphaChars[key[i]];
        keyString1 = origKeyString.split("SLGFLBSDFGKSDFGSDFGLK")[0];
        keyString2 = origKeyString.split("SLGFLBSDFGKSDFGSDFGLK")[1];
        for (int i = 0; i < input.length; i++) rohtext += alphaChars[input[i]];
        int[][] keySquare1 = getKeySquare(keyString1);
        int[][] keySquare2 = getKeySquare(keyString2);
        int[][] keyList1 = new int[91][2];
        int[][] keyList2 = new int[91][2];
        for (int i = 65; i <= 90; i++) {
            keyList1[i][0] = -1;
            keyList1[i][1] = -1;
            keyList2[i][0] = -1;
            keyList2[i][1] = -1;
        }
        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 4; j++) {
                if (i < 5) {
                    keyList1[(int) (getKeySquareVal(i, j, keySquare1, keySquare2))][0] = i;
                    keyList1[(int) (getKeySquareVal(i, j, keySquare1, keySquare2))][1] = j;
                }
                if (i >= 5) {
                    keyList2[(int) (getKeySquareVal(i, j, keySquare1, keySquare2))][0] = i;
                    keyList2[(int) (getKeySquareVal(i, j, keySquare1, keySquare2))][1] = j;
                }
            }
        }
        if (rohtext.length() % 2 == 1) {
            zufallsbuchstabe = rohtext.charAt(rohtext.length() - 1);
            while (zufallsbuchstabe == rohtext.charAt(rohtext.length() - 1)) {
                int randomIndex = (int) ((double) Math.floor(Math.random() * (double) rohtext.length()));
                zufallsbuchstabe = rohtext.charAt(randomIndex);
            }
            rohtext = rohtext + zufallsbuchstabe;
        }
        chiffre = "";
        for (int i = 0; i < rohtext.length(); i++) if (i % 2 == 0) {
            String bigrammalt = String.valueOf(rohtext.charAt(i)) + String.valueOf(rohtext.charAt(i + 1));
            bigrammneu = substitute(true, bigrammalt, keyList1, keyList2, keySquare1, keySquare2);
            chiffre = chiffre + bigrammneu;
        }
        int[] ciphertext = new int[chiffre.length()];
        for (int i = 0; i < chiffre.length(); i++) {
            for (int k = 0; k < alphaChars.length; k++) if (chiffre.charAt(i) == alphaChars[k]) {
                ciphertext[i] = k;
            }
        }
        return ciphertext;
    }
}
