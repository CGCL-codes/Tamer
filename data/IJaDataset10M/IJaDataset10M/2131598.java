package net.sf.javadc.net;

import junit.framework.TestCase;

/**
 * @author Timo Westk�mper
 */
public class SearchRequestFactoryTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Constructor for SearchRequestFactoryTest.
     * 
     * @param arg0
     */
    public SearchRequestFactoryTest(String arg0) {
        super(arg0);
    }

    public void testBorrowingAndReturning() throws Exception {
        SearchRequestFactory factory = new SearchRequestFactory();
        SearchRequest sr = (SearchRequest) factory.borrowObject();
        factory.returnObject(sr);
    }
}
