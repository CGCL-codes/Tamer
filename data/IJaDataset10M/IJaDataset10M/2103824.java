package jtt.latex.bibtex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Category;

/**
 * DOCUMENT ME!
 *
 * @.author $author$
 * @version $Revision: 1.6 $, $Date: 2005/02/17 16:48:43 $
 */
public class BibitemHolder {

    private static Category logger = Category.getInstance("jtt.latex.bibtex.BibitemHolder");

    private static BibitemHolder instance;

    public static final String eol = System.getProperty("line.separator", "\n");

    private final Hashtable bibitemEntries = new Hashtable();

    private BibitemHolder() {
    }

    /**
      *  Description of the Method
      *
      * @return   Description of the Return Value
      */
    public static synchronized BibitemHolder instance() {
        if (instance == null) {
            instance = new BibitemHolder();
        }
        return instance;
    }

    /**
     *  The main program for the TestSmarts class
     *
     * @param  args  The command line arguments
     */
    public static void main(String[] args) {
        BibitemHolder convert = new BibitemHolder();
        ArrayList list = convert.readBibTexFile("literature.bib");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    public static ArrayList readBibTexFile(String s) {
        ArrayList arraylist = new ArrayList();
        File file = new File(s);
        int i = 0;
        if (!file.exists() && !file.canRead() && !file.isFile()) {
            System.err.println("Error " + s + " is not a valid file and|or is not readable.");
            return null;
        }
        try {
            BufferedReader bufferedreader = new BufferedReader(new FileReader(s));
            StringBuffer stringbuffer = new StringBuffer();
            String s2;
            Vector v = new Vector();
            while ((s2 = bufferedreader.readLine()) != null) {
                s2 = s2.trim();
                if ((s2.length() != 0) && (s2.charAt(0) != '%')) {
                    int j;
                    if (((j = s2.indexOf("%")) > 0) && (s2.charAt(j - 1) != '\\')) {
                        s2 = s2.substring(0, j);
                    }
                    if (s2.charAt(0) == '@') {
                        v.add(stringbuffer.toString().trim());
                        stringbuffer = new StringBuffer();
                    }
                    stringbuffer.append(s2);
                    stringbuffer.append("\n");
                }
            }
            bufferedreader.close();
            String bibtexEntry;
            Bibitem item = null;
            for (int j = 0; j < v.size(); j++) {
                bibtexEntry = (String) v.get(j);
                if (bibtexEntry.length() != 0) {
                    try {
                        item = new Bibitem(bibtexEntry, "" + i++);
                        arraylist.add(item);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(e.getMessage() + ": '" + bibtexEntry + "'");
                    }
                }
            }
        } catch (IOException ioexception) {
            return null;
        }
        return arraylist;
    }

    public boolean containsKey(String key) {
        return bibitemEntries.containsKey(key);
    }

    public Bibitem getBibitem(String key) {
        return (Bibitem) bibitemEntries.get(key);
    }

    public Bibitem putBibitem(String key, Bibitem item) {
        if (key == null) {
            return null;
        }
        return (Bibitem) bibitemEntries.put(key, item);
    }
}
