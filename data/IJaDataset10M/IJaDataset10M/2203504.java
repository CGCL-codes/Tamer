package suchmaschine;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import java.util.StringTokenizer;
import java.io.File;
import org.apache.log4j.*;

/**
 * This is the part of the Serach-Engine wich does the connection to Lucene. It puts Artikles into Index but can laso delete them.
 * @author stowasser.h
 */
public class LuceneIndexer {

    static Logger cIdowa = Logger.getLogger("idowa.lucene.indexer");

    private String indexDir = Main.properties.datadir();

    private Analyzer analyzer;

    private IndexWriter index;

    private IndexReader mReader = null;

    private TermDocs mDocs = null;

    private boolean initalized = false;

    private boolean mCreate = false;

    /**
   * Gives back a reference to a org.apache.lucene.index.IndexReader Object.
   * @return Reader object.
   * @throws java.lang.Exception On Error
   */
    public IndexReader getReader() throws Exception {
        openReader();
        return mReader;
    }

    private void openReader() throws Exception {
        if (mReader == null) {
            mReader = IndexReader.open(indexDir);
        }
    }

    /**
   * clean up Reader and document objects!
   * @throws java.lang.Exception On Error
   */
    public void close() throws Exception {
        if (mDocs != null) {
            mDocs.close();
            mDocs = null;
        }
        if (mReader != null) {
            mReader.close();
            mReader = null;
        }
    }

    /**
   * Used for Iterating over !ALL! Documents in an area.
   * @return TermDocs object.
   * @param bereich String with area-name
   */
    public TermDocs iterBereich(String bereich) {
        cIdowa.debug("   ->iterBereich");
        mDocs = null;
        try {
            if (initalized == false) {
                openReader();
                Term term = new Term("bereich", bereich);
                mDocs = mReader.termDocs(term);
            } else {
                cIdowa.warn("Iterate is not posibile while startet index-session");
            }
        } catch (Exception e) {
            cIdowa.error("Kongruenzlauf imposibil for:" + bereich + ". Because:" + e.getMessage() + "\n" + e.toString());
        }
        return mDocs;
    }

    /**
   * Used for Deleting some Documents out of Index. Faster and needs less Memory then single delete!
   * @return int with number of deletet Documents
   * @param keywords String with keywords to delete. (Komma seperated)
   */
    public int deleteMany(String keywords) {
        int anzahl = 0;
        if (!mCreate) {
            try {
                if (initalized == false) {
                    openReader();
                    String[] temp = keywords.split(",");
                    for (int i = 0; i < temp.length; i++) {
                        Term term = new Term("keyword", temp[i]);
                        anzahl += mReader.delete(term);
                        term = null;
                    }
                    cIdowa.debug("deleted  t:" + anzahl);
                    close();
                } else {
                    cIdowa.warn("delete is not posibile while startet index-session");
                }
            } catch (Exception e) {
                cIdowa.error("Could not delete Documents:" + keywords + ". Because:" + e.getMessage() + "\n" + e.toString());
            }
        }
        return anzahl;
    }

    /**
   * Used for Deleting one Document out of Index
   * @return int with number of deletet Documents
   * @param keyword String with keyword
   */
    public int delete(String keyword) {
        int anzahl = 0;
        if (!mCreate) {
            try {
                if (initalized == false) {
                    openReader();
                    Term term = new Term("keyword", keyword);
                    anzahl = mReader.delete(term);
                    if (anzahl > 0) {
                        Runtime R = Runtime.getRuntime();
                        cIdowa.debug("deleted " + keyword + " t:" + R.totalMemory() + " f:" + R.freeMemory() + " m" + R.maxMemory());
                    } else if (anzahl > 1) {
                        cIdowa.error("More then one (" + anzahl + ") from Keyword:" + keyword + " ");
                    }
                    close();
                } else {
                    cIdowa.warn("delete is not posibile while startet index-session");
                }
            } catch (Exception e) {
                cIdowa.error("Could not delete Document:" + keyword + ". Because:" + e.getMessage() + "\n" + e.toString());
            }
        }
        return anzahl;
    }

    private String makeDate(String datum) {
        StringTokenizer st = new StringTokenizer(datum, " -.:,/\\");
        int year = Integer.parseInt(st.nextToken());
        int month = Integer.parseInt(st.nextToken());
        int date = Integer.parseInt(st.nextToken());
        if (date > 40) {
            int swap = year;
            year = date;
            date = swap;
        }
        if (year == 0) {
            year = 1970;
        }
        if (month == 0) {
            month = 1;
        }
        if (date == 0) {
            date = 1;
        }
        String tempm = "0" + month;
        String tempd = "0" + date;
        return ("" + year + tempm.substring(tempm.length() - 2) + tempd.substring(tempd.length() - 2));
    }

    /**
   * Puts one document into Index.
   * @param keyword should be unique! To find document again!
   * @param bereich String area to put into.
   * @param link String with link to use in Browser. (link+content_id) would be used!
   * @param content String with the words to indicate
   * @param content_vorschau String with a litle preview of the text.
   * @param datum String with a date for sorting Documents by date. Most known Dateformats are suportet.
   * @param content_id int the PK of the Document (used in concaternation with link!)
   * @param zeitstempel String with the Timestamp od the Document.
   * @param boost int faktor for weighting the
   * @throws java.lang.Exception On Error
   */
    public void put(String keyword, String bereich, String link, String content, String content_vorschau, String datum, int content_id, String zeitstempel, float boost) throws Exception {
        String date = makeDate(datum);
        cIdowa.debug("   ->put:" + keyword + " ->" + boost + " ->" + date);
        if (initalized) {
            Document neu = new Document();
            try {
                neu.setBoost(boost);
                neu.add(Field.UnStored("content", content));
                neu.add(Field.Keyword("keyword", keyword));
                neu.add(Field.Keyword("datum", date));
                neu.add(Field.UnIndexed("content_vorschau", content_vorschau));
                neu.add(Field.UnIndexed("content_id", "" + content_id));
                neu.add(Field.UnIndexed("zeitstempel", zeitstempel));
                neu.add(Field.UnIndexed("link", link));
                neu.add(Field.Keyword("bereich", bereich));
                index.addDocument(neu);
            } catch (Exception e) {
                cIdowa.error("Could not Indexing Document:" + keyword + ". Because:" + e.getMessage() + "\n" + e.toString());
            }
        } else {
            cIdowa.error("IndexWriter not Initialized!");
            throw (new Exception("IndexWriter not Initialized"));
        }
    }

    private boolean exists(File f) {
        return f.exists();
    }

    /**
   * Is looking for the Index file(s), to realize if inexistent.
   * @return true if Exists.
   */
    public boolean indexReady() {
        return exists(new File(indexDir + "/segments"));
    }

    /**
   * Initialize the Indexer, Creates new index files if needed.
   * @param create boolean true if index sould be created new.
   * @throws java.lang.Exception On Error
   */
    public void start(boolean create) throws Exception {
        cIdowa.debug("   ->start");
        cIdowa.info("Start Indexing Chunk");
        mCreate = create;
        try {
            if (!indexReady()) {
                mCreate = true;
                cIdowa.warn("CREATING NEW INDEX!");
            }
            index = new IndexWriter(indexDir, analyzer, mCreate);
            initalized = true;
        } catch (Exception e) {
            initalized = false;
            cIdowa.error("IndexWriter can not start:" + e.getMessage() + "\n" + e.toString());
            throw (e);
        }
    }

    /**
   * Optimize the Index.
   * @throws java.lang.Exception On Error
   */
    public void optimize() throws Exception {
        cIdowa.debug("   ->Optimize");
        if (initalized) {
            index.optimize();
        } else {
            start(false);
            stop(true);
        }
    }

    /**
   * Initialize the Indexer, Creates new index files if needed.
   * @throws java.lang.Exception On Error
   */
    public void stop(boolean optimize) throws Exception {
        cIdowa.debug("   ->stop");
        mCreate = false;
        if (initalized) {
            try {
                if (optimize) {
                    cIdowa.info("Optimizing..");
                    cIdowa.info("Desctivated..");
                }
                index.close();
                index = null;
                initalized = false;
            } catch (Exception e) {
                initalized = false;
                cIdowa.error("IndexWriter is not closed!" + e.getMessage() + "\n" + e.toString());
                throw (e);
            }
        }
        cIdowa.info("Chunk Ready");
    }

    /**
   * Constructor Iitializes the Anlyzer.
   */
    public LuceneIndexer() {
        analyzer = new IdowaAnalyzer();
    }
}
