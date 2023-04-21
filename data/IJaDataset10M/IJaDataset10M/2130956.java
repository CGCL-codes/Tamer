package org.openi.xmla;

import com.tonbeller.jpivot.olap.model.OlapException;
import com.tonbeller.jpivot.olap.model.OlapItem;
import com.tonbeller.jpivot.xmla.XMLA_SOAP;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.openi.test.Util;
import java.util.Iterator;
import java.util.List;

/**
 * @author plucas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestXmlaDiscover extends TestCase {

    private static Logger logger = Logger.getLogger(TestXmlaDiscover.class);

    private XMLA_SOAP olap;

    /**
     * Constructor for TestXmlaDiscover.
     * @param arg0
     */
    public TestXmlaDiscover(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        Util.setupLog4j();
        long start = System.currentTimeMillis();
        String uri = "http://moosehead/xmla/msxisapi.dll";
        logger.debug(uri);
        this.olap = new XMLA_SOAP(uri, null, null);
    }

    private void logOlapItemList(List olapItems) {
        Iterator items = olapItems.iterator();
        while (items.hasNext()) {
            OlapItem item = (OlapItem) items.next();
            logger.debug(item.getLabel());
        }
    }

    private String getFirstOlapItemLabel(List items) throws OlapException {
        return ((OlapItem) items.iterator().next()).getLabel();
    }

    public void testDiscoverCatalogs() throws OlapException {
        List catalogs = this.olap.discoverCat();
        logger.debug("discovered catalogs: " + catalogs.size());
        logOlapItemList(catalogs);
    }

    public void testDiscoverCube() throws OlapException {
        String firstCatalog = getFirstOlapItemLabel(this.olap.discoverCat());
        logger.debug("cubes: ");
        logOlapItemList(olap.discoverCube(firstCatalog));
    }

    public void testDiscoverDim() throws OlapException {
        String firstCatalog = getFirstOlapItemLabel(this.olap.discoverCat());
        String firstCube = getFirstOlapItemLabel(this.olap.discoverCube(firstCatalog));
        logger.debug("dimensions: ");
        logOlapItemList(olap.discoverDim(firstCatalog, firstCube));
    }

    public void testDiscoverDSProps() throws OlapException {
        logger.debug("DSProps: ");
        logOlapItemList(this.olap.discoverDSProps());
    }
}
