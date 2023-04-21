package com.once.servicescout.indexer.impl;

import com.once.servicescout.exception.IndexingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.google.inject.Injector;
import com.once.servicescout.bootstrap.Starter;
import com.once.servicescout.data.meta.ServiceMetaData;
import com.once.servicescout.data.meta.impl.ServiceMetaDataImpl;
import com.once.servicescout.indexer.Indexer;

public class IndexerImplTest {

    Indexer indexer;

    ServiceMetaData testService;

    String id;

    @Before
    public void setUp() throws Exception {
        Injector injector = Starter.getInstance().getInjector();
        indexer = injector.getInstance(Indexer.class);
        assertNotNull(indexer);
        id = "000001";
        testService = new ServiceMetaDataImpl("TEST SERVICE DESC", "test", "http://localhost/test.wsdl");
        testService.setID(id);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testIndexService() throws IndexingException {
        long m1 = indexer.getMaxID();
        indexer.indexService(testService);
        long m2 = indexer.getMaxID();
        assertEquals(m2, m1 + 1l);
    }

    @Test
    public void testRemoveService() {
        try {
            indexer.removeService(id);
        } catch (IndexingException ex) {
            Logger.getLogger(IndexerImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
