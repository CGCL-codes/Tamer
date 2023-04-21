package br.org.eteg.curso.javaoo.capitulo10.io.novo;

import java.io.*;
import java.util.Scanner;

public class ScannerExemplo {

    public static void main(String[] args) throws IOException {
        Scanner s = null;
        try {
            s = new Scanner(new BufferedReader(new FileReader("xanadu.txt")));
            while (s.hasNext()) {
                System.out.println(s.next());
            }
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }
}
