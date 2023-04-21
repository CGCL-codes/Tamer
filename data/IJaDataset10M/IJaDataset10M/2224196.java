package osdep.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * File utilities
 * @author SHZ Mar 14, 2008
 */
public class FileUtil {

    /**
	 * Transfers the content of a file to a string 
	 * @param listFile
	 * @return a string that is the content of the file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    public static String fileContentToString(File listFile) throws FileNotFoundException, IOException {
        StringBuilder b = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(listFile));
        try {
            String line;
            while ((line = reader.readLine()) != null) b.append(line).append('\n');
            return b.toString();
        } finally {
            reader.close();
        }
    }
}
