package nmaf.digmap.ingest;

import java.io.File;
import java.util.Iterator;
import org.dom4j.Document;
import org.apache.log4j.Logger;
import pt.utl.ist.lucene.utils.Dom4jUtil;

public class IteratorXmlRecords implements Iterator<Document>, Iterable<Document> {

    File[] files;

    int nextIdx = -1;

    public IteratorXmlRecords(File[] files) {
        this.files = files;
        goToNext();
    }

    public boolean hasNext() {
        return nextIdx >= 0;
    }

    public Document next() {
        try {
            Document ret = Dom4jUtil.parse(files[nextIdx]);
            goToNext();
            return ret;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void remove() {
        throw new RuntimeException("Remove not supported");
    }

    private static final Logger logger = Logger.getLogger(IteratorXmlRecords.class);

    private void goToNext() {
        nextIdx++;
        if (files == null || nextIdx >= files.length) nextIdx = -1; else if (!files[nextIdx].isFile() || !files[nextIdx].getName().toLowerCase().endsWith(".digmap.xml")) {
            goToNext();
        } else if (!files[nextIdx].isFile() || !files[nextIdx].getName().toLowerCase().endsWith(".xml")) {
            logger.warn("Skip " + files[nextIdx].getName());
        }
    }

    public Iterator<Document> iterator() {
        return this;
    }
}
