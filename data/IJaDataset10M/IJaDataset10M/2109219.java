package pub.test;

import pub.utils.GeneSaxReader;
import java.util.*;
import java.io.*;
import pub.utils.PubProperties;
import pub.beans.*;
import pub.db.LocusTable;

public class LocusBeanTest extends DatabaseTestCase {

    public GeneBean geneBean;

    public void setUp() throws Exception {
        super.setUp();
        Reader r = new FileReader(testDataPath("one_gene.xml"));
        GeneSaxReader reader = new GeneSaxReader(conn, "1");
        reader.readGenes(r);
        geneBean = BeanFactory.getGeneBean(conn, (String) reader.getGenesLoaded().get(0));
    }

    public void testSingleLocus() throws Exception {
        LocusBean locusBean = (LocusBean) geneBean.getLocusBeans().get(0);
        assertTrue(locusBean.isNull() == false);
        assertEquals("AT4G18960", locusBean.getName());
    }

    public void testNullLocusBean() throws Exception {
        assertTrue(BeanFactory.getLocusBean(conn, "-1").isNull());
        assertTrue(BeanFactory.getLocusBean(conn, "foobah!").isNull());
        assertTrue(BeanFactory.getLocusBean(conn, "0").isNull());
        assertTrue(BeanFactory.getLocusBean(conn, "").isNull());
    }

    public void testReplacement() {
        LocusTable locusTable = new LocusTable(conn);
    }
}
