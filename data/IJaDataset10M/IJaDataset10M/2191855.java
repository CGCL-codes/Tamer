package org.capelin.transaction.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.capelin.core.models.CapelinRecord;
import org.capelin.core.utils.AbstractMarcImporter;
import org.hibernate.FlushMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.Record;

/**
 * 
 * <a href="http://code.google.com/p/capline-opac/">Capelin-opac</a>
 * License: GNU AGPL v3 | http://www.gnu.org/licenses/agpl.html
 * 
 * Utility import marc data to database and write the index.
 * 
 * 
 * @author Jing Xiao <jing.xiao.ca at gmail dot com>
 * 
 * 
 */
public abstract class TXMarcRecordImporter extends AbstractMarcImporter implements TXRecrodImporter {

    protected static final Log log = LogFactory.getLog(TXMarcRecordImporter.class);

    protected SessionFactory sf;

    /**
	 * @deprecated Should not use it
	 * @see AbstractMarcImporter
	 */
    @Override
    @Deprecated
    public void run(boolean flag) throws Exception {
        throw new UnsupportedOperationException("Not Impelmented");
    }

    public void run(int command) throws Exception {
        setUp();
        long start = System.currentTimeMillis();
        log.info(getClass() + " start!");
        int total;
        switch(command) {
            case IMPORT_ONLY:
                total = importData();
                break;
            case INDEXES_THEN_IMPORT:
                total = buildIndexesThenImport();
                break;
            case INDEX_ONLY:
                total = buildIndexManually();
                break;
            default:
                total = 0;
        }
        input.close();
        log.info("Total Records: " + total);
        log.info(getClass() + " Time: " + (System.currentTimeMillis() - start));
    }

    @Override
    protected int importData() throws Exception {
        MarcReader reader = new MarcStreamReader(input);
        Session session = sf.getCurrentSession();
        Transaction tx = session.beginTransaction();
        int index = importRecords(reader, session);
        input.close();
        log.info("Flushing ...");
        tx.commit();
        return index;
    }

    protected static int BATCH_SIZE = 1000;

    protected int buildIndexesThenImport() throws Exception {
        MarcReader reader = new MarcStreamReader(input);
        Session session = sf.getCurrentSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);
        fullTextSession.createIndexer().startAndWait();
        Transaction tx = session.beginTransaction();
        int index = importRecords(reader, session);
        input.close();
        log.info("Flushing ...");
        tx.commit();
        return index;
    }

    protected int importRecords(MarcReader reader, Session session) {
        Record record = null;
        CapelinRecord data = null;
        int index = 0;
        while (reader.hasNext()) {
            index++;
            record = reader.next();
            data = buildRecord(record);
            if (null != data) session.save(data);
            if (index % BATCH_SIZE == 0) {
                session.flush();
                session.clear();
                log.info(index);
            }
        }
        return index;
    }

    protected int buildIndexManually() throws Exception {
        return ImportHelper.buildIndexManually(sf, getRecordClass(), BATCH_SIZE, log);
    }

    protected abstract Class<? extends CapelinRecord> getRecordClass();
}
