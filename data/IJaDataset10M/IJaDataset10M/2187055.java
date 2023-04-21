package com.lineadecodigo.java.file.encoding;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class LeerFicheroUTF8 {

    public static void main(String[] args) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("FicheroUTF8.txt"), "utf-8"));
            String sCadena;
            while ((sCadena = in.readLine()) != null) {
                System.out.println(sCadena);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
