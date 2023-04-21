package edu.mit.lcs.haystack.server.extensions.infoextraction.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * @author yks
 * a test file that reads a file containing
 * new line delimited records
 * stores the file into a vector.
 */
public class LineDelimitedFile {

    Vector data;

    public LineDelimitedFile(String file) {
        try {
            readFromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vector getContent() {
        return data;
    }

    public void reset() {
    }

    Vector readFromFile(String filename) throws FileNotFoundException, IOException {
        data = new Vector();
        File file = new File(filename);
        FileInputStream fin = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fin));
        String line;
        String sectionName = null;
        while (null != (line = br.readLine())) {
            line = line.trim();
            if (line.length() == 0) {
                sectionName = null;
                continue;
            }
            if (line.startsWith("#")) {
                continue;
            }
            data.add(line);
        }
        return data;
    }
}
