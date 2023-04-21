package org.ethontos.owlwatcher.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * This class is probably defunct
 * @author peter
 * created Mar 17, 2007
 *
 */
public class OBOReader {

    private OWOntology ont;

    InputStream ins = null;

    boolean valid = false;

    static Logger logger = Logger.getLogger(OBOReader.class.getName());

    /**
     * 
     * @param uriSpec
     * @return true if uriSpec specifies the url of a readable file that contains an obo ontology
     */
    static boolean uriCheck(String uriSpec) {
        return false;
    }

    /**
     * 
     * @param f
     * @return true if f identifies a file that is readable and contains an obo ontology
     */
    static boolean fileCheck(File f) {
        logger.setLevel((Level) Level.DEBUG);
        if (f != null) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                OBOTokenizer ot = new OBOTokenizer(br);
                int type;
                type = ot.nextToken();
                if (type != OBOTokenizer.TT_WORD) return false;
                System.out.println("token is " + ot.sval);
                if (ot.sval.equalsIgnoreCase("format-version")) return true;
                return false;
            } catch (FileNotFoundException e) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    public OBOReader(File f) {
        if (f != null) {
            try {
                ins = new FileInputStream(f);
                valid = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public OBOReader(String urlSpec) {
        URL source = null;
        URLConnection conn = null;
        try {
            source = new URL(urlSpec);
            conn = source.openConnection();
            ins = conn.getInputStream();
            valid = true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean processFile() {
        return false;
    }

    public OWOntology getOntology() {
        return ont;
    }

    /**
     * 
     * @author peter
     * created Mar 17, 2007
     *
     */
    private static class OBOTokenizer extends StreamTokenizer {

        protected OBOTokenizer(Reader r) {
            super(r);
            resetSyntax();
            wordChars('A', 'Z');
            wordChars('a', 'z');
            wordChars('-', '-');
            whitespaceChars(0, 32);
        }

        private static boolean checkToken(StreamTokenizer ot, int type, String val, int count) throws IOException {
            ot.nextToken();
            if (ot.ttype != type) {
                logger.debug("Token number " + count + " has type " + ot.ttype);
                return false;
            } else if ((type == StreamTokenizer.TT_WORD) && (!ot.sval.equalsIgnoreCase(val))) {
                logger.debug("Token number " + count + " has value " + ot.sval);
                return false;
            }
            return true;
        }
    }
}
