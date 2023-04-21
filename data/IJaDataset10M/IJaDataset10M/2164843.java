package gatchan.jedit.rfcreader;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.util.Log;
import java.io.File;
import java.io.IOException;

/**
 * @author Matthieu Casanova
 */
public class RFCIndexTitle extends AbstractRFCIndex {

    /** increment this */
    int INDEX_VERSION = 3;

    public RFCIndexTitle() throws IOException {
        RFCReaderPlugin plugin = (RFCReaderPlugin) jEdit.getPlugin("gatchan.jedit.rfcreader.RFCReaderPlugin");
        File home = plugin.getPluginHome();
        analyzer = new StandardAnalyzer(Version.LUCENE_30);
        directory = FSDirectory.open(new File(home, "lucene"));
    }

    protected String[] getFields() {
        return new String[] { "number", "title" };
    }

    @Override
    public void load() throws IOException {
        Log.log(Log.DEBUG, this, "load()");
        if (jEdit.getIntegerProperty("rfcreader.index.version", -1) != INDEX_VERSION || !IndexReader.indexExists(directory)) {
            IndexWriter writer = new IndexWriter(directory, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
            for (RFC rfc : rfcs.values()) {
                Document document = new Document();
                document.add(new Field("number", Integer.toString(rfc.getNumber()), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.NO));
                document.add(new Field("title", rfc.getTitle(), Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.NO));
                writer.addDocument(document);
            }
            writer.optimize();
            writer.close();
            jEdit.setIntegerProperty("rfcreader.index.version", INDEX_VERSION);
        }
        searcher = new IndexSearcher(directory, true);
    }
}
