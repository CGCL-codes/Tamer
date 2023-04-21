package pl.matt.io.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author mateusz
 * 
 */
public class FileUtils {

    /**
	 * zapisuje bajty do pliku
	 * 
	 * @param filename
	 * @param bytes
	 * @throws IOException
	 */
    public static final void write(String filename, byte[] bytes) throws IOException {
        BufferedOutputStream fw = new BufferedOutputStream(new FileOutputStream(filename));
        fw.write(bytes, 0, bytes.length);
        fw.close();
    }

    /**
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
    public static final String readFile(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String str;
        StringBuilder builder = new StringBuilder();
        while ((str = in.readLine()) != null) {
            builder.append(str);
        }
        in.close();
        return builder.toString();
    }

    /**
	 * @param fileName
	 * @param lineNumber
	 * @return żądaną linię lub null, jeżeli plik nie ma tyle linii co trzeba
	 * @throws IOException
	 */
    public static final String readLineFromFile(String fileName, int lineNumber) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String str;
        int number = 1;
        while ((str = in.readLine()) != null) {
            if (number == lineNumber) {
                break;
            }
            number++;
        }
        in.close();
        return (number == lineNumber) ? str : null;
    }
}
