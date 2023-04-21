package ch.ethz.mxquery.testsuite;

import junit.framework.TestCase;
import javax.xml.xquery.*;

public class XQJTestCase extends TestCase {

    protected XQDataSource xqds;

    protected XQConnection xqc;

    protected void setUp() throws Exception {
        String xqdsClassName = "ch.ethz.mxquery.xqj.MXQueryXQDataSource";
        Class xqdsClass = Class.forName(xqdsClassName);
        xqds = (XQDataSource) xqdsClass.newInstance();
        xqc = xqds.getConnection();
    }

    protected void tearDown() throws Exception {
        xqc.close();
    }
}
