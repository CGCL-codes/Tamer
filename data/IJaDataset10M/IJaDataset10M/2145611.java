package org.omegat.gui.glossary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.omegat.util.OConsts;

/**
 * Reader for comma separated glossaries.
 * 
 * @author Keith Godfrey
 * @author Maxym Mykhalchuk
 * @author Alex Buloichik <alex73mail@gmail.com>
 */
public class GlossaryReaderCSV {

    /** Fields separator. Can be dependent of regional options. */
    protected static final char SEPARATOR = ',';

    public static List<GlossaryEntry> read(final File file) throws IOException {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), OConsts.UTF8);
        List<GlossaryEntry> result = new ArrayList<GlossaryEntry>();
        BufferedReader in = new BufferedReader(reader);
        try {
            in.mark(1);
            int ch = in.read();
            if (ch != 0xFEFF) in.reset();
            for (String s = in.readLine(); s != null; s = in.readLine()) {
                if (s.startsWith("#")) continue;
                String tokens[] = parseLine(s);
                if (tokens.length < 2 || tokens[0].length() == 0) continue;
                String comment = "";
                if (tokens.length >= 3) comment = tokens[2];
                result.add(new GlossaryEntry(tokens[0], tokens[1], comment));
            }
        } finally {
            in.close();
        }
        return result;
    }

    private static String[] parseLine(String line) {
        List<String> result = new ArrayList<String>();
        StringBuilder w = new StringBuilder();
        boolean fopened = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            char cn;
            try {
                cn = line.charAt(i + 1);
            } catch (StringIndexOutOfBoundsException ex) {
                cn = 0;
            }
            switch(c) {
                case '"':
                    if (w.length() == 0 && !fopened) {
                        fopened = true;
                    } else if (cn == '"') {
                        w.append(c);
                        i++;
                    } else {
                        fopened = false;
                    }
                    break;
                case SEPARATOR:
                    if (fopened) {
                        w.append(c);
                    } else {
                        result.add(w.toString());
                        w.setLength(0);
                    }
                    break;
                default:
                    w.append(c);
                    break;
            }
        }
        result.add(w.toString());
        return result.toArray(new String[result.size()]);
    }
}
