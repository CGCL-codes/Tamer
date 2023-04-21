package vqwiki;

import java.util.*;
import java.io.*;

public class SearchEngineDB {

    protected static final String INDEX_FILE = "searchIndex.table";

    private static SearchEngineDB instance;

    protected Hashtable table;

    protected static String sep = System.getProperty("file.separator");

    /**
     * Index the given text for the search engine database
     */
    public void indexText(String topic, String text) throws IOException {
        add(topic, text);
    }

    /**
     * Should be called by a monitor thread at regular intervals, rebuilds the
     * entire seach index to account for removed items. Due to the additive rather
     * than subtractive nature of a Wiki, it probably only needs to be called once
     * or twice a day
     */
    public void refreshIndex() {
        rebuild();
    }

    /**
     * Return results (topics where text occurs) from search for text
     */
    public Collection search(String text) {
        return findMultiple(text);
    }

    private SearchEngineDB() throws java.io.IOException {
        table = new Hashtable();
    }

    public static SearchEngineDB getInstance() throws java.io.IOException {
        if (instance == null) {
            instance = new SearchEngineDB();
        }
        return instance;
    }

    /**
   * Trawls all the files in the wiki directory and indexes them
   */
    public void rebuild() {
        table = new Hashtable();
        File file = new File(dir());
        String[] files = file.list(new TextFileFilter());
        for (int i = 0; i < files.length; i++) {
            try {
                BufferedReader fileReader = new BufferedReader(new FileReader(dir() + files[i]));
                SearchLexer lexer = new SearchLexer(fileReader);
                while (true) {
                    String token = lexer.yylex();
                    if (token == null) break;
                    token = prune(token);
                    token = token.toLowerCase();
                    if (token.length() > 1) {
                        TreeSet set;
                        if (table.get(token) == null) set = new TreeSet(); else set = (TreeSet) table.get(token);
                        set.add(files[i].substring(0, files[i].length() - 4));
                        table.put(token, set);
                    }
                }
                fileReader.close();
            } catch (IOException ex) {
                System.err.println("IO error rebuilding index");
            }
        }
    }

    /**
   * Remove any special characters from the given string
   */
    protected String prune(String text) {
        String pruned = "";
        for (int i = 0; i < text.length(); i++) {
            if (Character.isLetter(text.charAt(i)) || Character.isDigit(text.charAt(i))) pruned += text.charAt(i);
        }
        return pruned;
    }

    public Collection find(String text) {
        return (Collection) table.get(text.toLowerCase());
    }

    public Collection findMultiple(String text) {
        StringTokenizer terms = new StringTokenizer(text);
        Collection[] colls = new Collection[terms.countTokens()];
        for (int i = 0; terms.hasMoreTokens(); i++) {
            String term = terms.nextToken();
            colls[i] = (Collection) table.get(term.toLowerCase());
        }
        Hashtable hits = new Hashtable();
        for (int i = 0; i < colls.length; i++) {
            if (colls[i] != null) {
                Iterator it = colls[i].iterator();
                while (it.hasNext()) {
                    String topic = (String) it.next();
                    Integer soFar = (Integer) hits.get(topic);
                    if (soFar == null) hits.put(topic, new Integer(1)); else {
                        int n = soFar.intValue();
                        hits.put(topic, new Integer(n + 1));
                    }
                }
            }
        }
        TreeSet ordered = new TreeSet();
        Iterator it = hits.keySet().iterator();
        while (it.hasNext()) {
            String topic = (String) it.next();
            ordered.add(new ComparablePair((Integer) hits.get(topic), topic));
        }
        Vector v = new Vector();
        it = ordered.iterator();
        while (it.hasNext()) {
            v.add(((ComparablePair) it.next()).getTwo());
        }
        return v;
    }

    /**
   * Adds to the in-memory table. Does not remove indexed items that are
   * no longer valid due to deletions, edits etc.
   */
    public void add(String topic, String contents) throws IOException {
        StringReader reader = new StringReader(contents);
        SearchLexer lexer = new SearchLexer(reader);
        while (true) {
            String token = lexer.yylex();
            if (token == null) break;
            token = prune(token).toLowerCase();
            if (token.length() > 1) {
                TreeSet set;
                if (table.get(token) == null) {
                    set = new TreeSet();
                } else {
                    set = (TreeSet) table.get(token);
                }
                set.add(topic);
                table.put(token, set);
            }
        }
    }

    protected void saveIndex() {
        try {
            File file = new File(dir() + INDEX_FILE);
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(this.table);
            out.close();
        } catch (IOException ex) {
            System.err.println("IO error saving the search engine index " + ex);
        }
    }

    protected void loadIndex() {
        try {
            File file = new File(dir() + INDEX_FILE);
            if (!file.exists()) return;
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            this.table = (Hashtable) in.readObject();
            in.close();
        } catch (IOException ex) {
            System.err.println("IO Error loading search engine index " + ex);
        } catch (ClassNotFoundException ex) {
            System.err.println(ex);
        }
    }

    protected String dir() {
        return Environment.getInstance().getHomeDir() + System.getProperty("file.separator");
    }
}
