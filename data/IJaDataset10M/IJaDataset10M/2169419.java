package net.redwarp.actions.tools;

import java.io.File;

public class Tools {

    public static final File[] sortByIsDirectory(File[] files) {
        if (files == null) {
            return null;
        }
        File temp;
        for (int i = 0; i < files.length - 1; i++) {
            for (int j = 0; j < files.length - 1 - i; j++) {
                if (files[j + 1].isFile()) {
                    temp = files[j + 1];
                    files[j + 1] = files[j];
                    files[j] = temp;
                }
            }
        }
        return files;
    }

    public static final File[] sortByIsDirectoryListCopy(final File[] files) {
        if (files == null) {
            return null;
        }
        File[] newList = new File[files.length];
        for (int i = 0; i < files.length; i++) {
            newList[i] = files[i];
        }
        return sortByIsDirectory(newList);
    }

    public static String toHexString(byte[] result) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : result) {
            String letter = Integer.toHexString(b & 0xFF);
            while (letter.length() < 2) {
                letter = "0" + letter;
            }
            hexString.append(letter);
        }
        return hexString.toString();
    }
}
