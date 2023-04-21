package org.adapit.wctoolkit.reverse.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class IndentHelper {

    /**
	 * Recebe uma String e retorna uma String Identada
	 * 
	 * @param code
	 * @return
	 */
    public static String IndentString(String code) {
        return code;
    }

    /**
	 * Recebe um File e Retorna um File com o C�digo Identado
	 * 
	 * @param file
	 * @return
	 */
    public static File IndentFile(File file) {
        String code = "", str = "";
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNext()) {
                code += " " + scanner.next();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        code = IndentString(code);
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file.getPath()));
            out.write(code);
            out.close();
        } catch (IOException e) {
        }
        return file;
    }
}
