package com.util;

import java.io.*;

public class DeleteFile {

    public static void delete(String fileName) {
        File file = new File(fileName);
        file.delete();
    }
}
