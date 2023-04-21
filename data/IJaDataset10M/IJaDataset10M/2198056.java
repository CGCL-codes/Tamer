package net.jforum.view.install;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafael Steil
 * @version $Id: ParseDBDumpFile.java,v 1.6 2007/10/08 17:34:40 rafaelsteil Exp $
 */
public class ParseDBDumpFile {

    public static List parse(String filename) throws IOException {
        List statements = new ArrayList();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }
                char firstChar = line.charAt(0);
                if (firstChar == '-' || firstChar == '#') {
                    continue;
                }
                if (line.charAt(line.length() - 1) == ';') {
                    line = line.substring(0, line.length() - 1);
                }
                statements.add(line);
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
        }
        return statements;
    }
}
