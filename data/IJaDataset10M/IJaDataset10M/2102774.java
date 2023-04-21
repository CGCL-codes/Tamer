package pck_tap.alg.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class File {

    public File() {
    }

    public void str2file(String data, String filename) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.write(data);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
   * return file as string.
   * @param filename
   * @return
   * @throws FileNotFoundException
   */
    public String file2str(String filename) throws FileNotFoundException {
        String vreturn = "";
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                vreturn = vreturn + line;
            }
            br.close();
            fr.close();
            return vreturn;
        } catch (FileNotFoundException e) {
            throw (e);
        } catch (IOException e) {
            return vreturn;
        }
    }
}
