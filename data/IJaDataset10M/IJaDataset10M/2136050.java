package org.jcouchdb;

import java.util.ArrayList;
import java.util.List;
import org.jcouchdb.db.Database;
import org.jcouchdb.db.Server;
import org.jcouchdb.db.ServerImpl;
import org.jcouchdb.document.BaseDocument;
import org.junit.Ignore;
import org.junit.Test;

public class BulkWritePerfTestCase {

    private static final String DB_NAME = "jcouchdb_test_writeperf";

    @Test
    @Ignore
    public void test() {
        Server server = new ServerImpl("localhost");
        if (server.listDatabases().contains(DB_NAME)) {
            server.deleteDatabase(DB_NAME);
        }
        server.createDatabase(DB_NAME);
        Database db = new Database(server, DB_NAME);
        long start = System.currentTimeMillis();
        final int num_docs = 1000;
        List<BaseDocument> documents = new ArrayList<BaseDocument>(num_docs);
        for (int i = 0; i < num_docs; i++) {
            BaseDocument doc = new BaseDocument();
            doc.setProperty("data", "The quick brown fox jumps over the lazy dog.");
            doc.setId(null);
            doc.setRevision(null);
            documents.add(doc);
        }
        db.bulkCreateDocuments(documents);
        long dur = System.currentTimeMillis() - start;
        double seconds = dur / 1000.0;
        System.out.println("total: " + seconds + ", docs/sec = " + num_docs / seconds);
    }
}
