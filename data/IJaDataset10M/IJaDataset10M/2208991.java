package boolvarpb.utility;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Olivier Bailleux
 * Date: 17 nov. 2010
 * Time: 13:09:57
 */
public class Exploitation {

    public static void printFile(String s, String fileName) {
        try {
            FileWriter fstream = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(fstream, s.length() + 10);
            out.write(s);
            out.close();
        } catch (Exception e) {
            throw new RuntimeException("Error while opening or writing the file : " + fileName);
        }
    }
}
