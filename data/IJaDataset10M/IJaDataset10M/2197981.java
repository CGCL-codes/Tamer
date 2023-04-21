package net.sf.econtycoon.util;

/**
 * @author Konstantin
 *
 */
public class Glaettung {

    public static int[] expGlaettung(int[] data, double alpha) {
        if (alpha <= 1.0 && alpha >= 0.0) {
            int[] data_glatt = new int[data.length];
            data_glatt[0] = data[0];
            for (int i = 1; i < data.length; i++) {
                if (data[i] != 0) {
                    data_glatt[i] = (int) ((alpha * data_glatt[i - 1] + (1 - alpha) * data[i]));
                } else {
                    break;
                }
            }
            return data_glatt;
        } else {
            return null;
        }
    }

    public static int[] arithmMittelwertGlaettung(int[] data) {
        int[] data_glatt = new int[data.length];
        long summe = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0) {
                break;
            }
            summe += data[i];
            data_glatt[i] = (int) (summe / (i + 1));
        }
        return data_glatt;
    }

    public static int[] gleitenderMittelwertGlaettung(int[] data, int range) {
        int[] data_glatt = new int[data.length];
        for (int i = 0; i < range; i++) {
            data_glatt[i] = data[i];
            data_glatt[data.length - i - 1] = data[data.length - i - 1];
        }
        for (int i = range; i < data.length - range; i++) {
            if (data[i + range] == 0) {
                break;
            }
            int summe = 0;
            for (int j = 0; j < range; j++) {
                summe += data[i - (j + 1)];
                summe += data[i + (j + 1)];
            }
            summe += data[i];
            data_glatt[i] = (int) (summe / (2 * range + 1));
        }
        return data_glatt;
    }
}
