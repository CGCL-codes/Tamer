package ch.ethz.mxquery.testsuite.internal;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import ch.ethz.mxquery.exceptions.DynamicException;
import ch.ethz.mxquery.exceptions.ErrorCodes;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.exceptions.QueryLocation;
import ch.ethz.mxquery.exceptions.StaticException;
import ch.ethz.mxquery.sms.ftstore.Stemmer;
import ch.ethz.mxquery.util.IOLib;
import ch.ethz.mxquery.util.LineReader;
import ch.ethz.mxquery.util.Utils;

public class DictionaryBasedStemmer implements Stemmer {

    HashMap stemEntries;

    public DictionaryBasedStemmer() {
        stemEntries = new HashMap();
    }

    public DictionaryBasedStemmer(String uri) throws MXQueryException {
        stemEntries = new HashMap();
        Reader ir = IOLib.getInput(uri, false, null, QueryLocation.OUTSIDE_QUERY_LOC);
        LineReader lr = new LineReader(ir);
        try {
            String str;
            while ((str = lr.readLine()) != null) {
                String[] stems = Utils.split(str, " ", false);
                for (int i = 1; i < stems.length; i++) {
                    stemEntries.put(stems[i], stems[0]);
                }
            }
        } catch (IOException e) {
            if (e instanceof UnsupportedEncodingException) throw new StaticException(ErrorCodes.E0087_STATIC_WRONG_ENCODING, "Unsupported Encoding - Error: " + e.toString(), QueryLocation.OUTSIDE_QUERY_LOC); else throw new DynamicException(ErrorCodes.A0007_EC_IO, "I/O-Error" + e.toString(), QueryLocation.OUTSIDE_QUERY_LOC);
        } finally {
            try {
                lr.close();
            } catch (IOException e) {
            }
        }
    }

    public String findStem(String text) {
        String stem = (String) stemEntries.get(text);
        if (stem == null) return text;
        return stem;
    }
}
