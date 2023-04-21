package org.fao.gast.lib.druid;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.dlib.tools.FullTokenizer;

public class DdfLoader {

    public static interface Handler {

        public void handleFields(List<ImportField> fields) throws Exception;

        public void handleRow(List<String> values) throws Exception;

        public void cleanUp();
    }

    public void setHandler(Handler h) {
        handler = h;
    }

    public void load(String fileName) throws FileNotFoundException, IOException, Exception {
        BufferedReader rdr = new BufferedReader(new FileReader(fileName));
        String line;
        int status = START;
        ArrayList<ImportField> alFields = new ArrayList<ImportField>();
        try {
            while ((line = rdr.readLine()) != null) {
                if (line.equals("") || line.startsWith("#")) continue;
                if (line.equals("[FIELDS]")) status = FIELDS; else if (line.equals("[DATA]")) {
                    status = DATA;
                    handler.handleFields(alFields);
                } else {
                    if (status == FIELDS) alFields.add(new ImportField(line)); else if (status == DATA) handleRow(line); else throw new IllegalArgumentException("Data not allowed before [FIELDS] section");
                }
            }
            if (status != DATA) throw new IllegalArgumentException("Unexpected EOF encountered");
        } finally {
            handler.cleanUp();
            rdr.close();
        }
    }

    private void handleRow(String line) throws Exception {
        FullTokenizer ft = new FullTokenizer(line, "\t");
        String token;
        ArrayList<String> al = new ArrayList<String>();
        for (int i = 0; i < ft.countTokens(); i++) {
            token = ft.nextToken();
            al.add(token);
        }
        handler.handleRow(al);
    }

    private Handler handler;

    private static final int START = 0;

    private static final int FIELDS = 1;

    private static final int DATA = 2;
}
