package edu.ucdavis.genomics.metabolomics.binbase.bci.cache.impl;

import org.junit.After;
import org.junit.Before;
import edu.ucdavis.genomics.metabolomics.binbase.bci.cache.Cache;
import edu.ucdavis.genomics.metabolomics.binbase.bci.cache.CacheTest;

public class BinBaseTreeCacheTest extends CacheTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected Cache getCache() throws Exception {
        System.setProperty(BinBaseTreeCache.KEY_CONFIG, "jboss-cache.xml");
        return BinBaseTreeCache.getInstance();
    }
}
