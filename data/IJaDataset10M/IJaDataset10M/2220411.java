package org.capelin.transaction.utils;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.IndexReader;
import org.capelin.core.models.CapelinRecord;
import org.capelin.core.utils.AbstractLuceneIndexImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import au.com.bytecode.opencsv.CSVReader;

/**
 * 
 * <a href="http://code.google.com/p/capline-opac/">Capelin-opac</a>
 * License: GNU AGPL v3 | http://www.gnu.org/licenses/agpl.html
 * 
 * Utility import lucene index data to database and write the index.
 * 
 * 
 * @author Jing Xiao <jing.xiao.ca at gmail dot com>
 * 
 * 
 */
public abstract class TXLuceneRecordImporter extends AbstractLuceneIndexImporter implements TXRecrodImporter {

    protected static final Log log = LogFactory.getLog(TXLuceneRecordImporter.class);

    protected SessionFactory sf;

    public void run(int command) throws Exception {
        setUp();
        long start = System.currentTimeMillis();
        log.info(getClass() + " start!");
        int total;
        switch(command) {
            case IMPORT_ONLY:
                total = importData();
                break;
            case INDEX_ONLY:
                total = buildIndexManually();
                break;
            default:
                total = 0;
        }
        dir.close();
        log.info("Total Records: " + total);
        log.info(getClass() + " Time: " + (System.currentTimeMillis() - start));
    }

    @Override
    @Deprecated
    public void run(boolean flag) throws Exception {
        throw new UnsupportedOperationException("Not Impelmented");
    }

    @Override
    protected int importData() throws IOException {
        IndexReader reader = IndexReader.open(dir);
        Session session = sf.getCurrentSession();
        Transaction tx = session.beginTransaction();
        int index = importRecords(reader, session);
        reader.close();
        log.info("Flushing ...");
        tx.commit();
        return index;
    }

    protected static int BATCH_SIZE = 1000;

    protected int importRecords(IndexReader reader, Session session) throws IOException {
        CapelinRecord data = null;
        int totalDoc = reader.numDocs();
        for (int i = 0; i < totalDoc; i++) {
            data = buildRecord(reader.document(i));
            if (null != data) session.save(data);
            if (i % BATCH_SIZE == 0) {
                session.flush();
                session.clear();
                log.info(i);
            }
        }
        return totalDoc;
    }

    protected int buildIndexManually() throws Exception {
        return ImportHelper.buildIndexManually(sf, getRecordClass(), BATCH_SIZE, log);
    }

    protected abstract Class<? extends CapelinRecord> getRecordClass();
}
