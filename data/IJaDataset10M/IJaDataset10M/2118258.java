package com.isurf.gdssuclient.main;

import static org.junit.Assert.assertNotNull;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.isurf.gdssu.datapool.ws.Identifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.isurf.gdssuclient.util.GDSSUImpl;

/**
 * Main Entry Point to EPCClientTest
 * 
 * @author Patrick Cheevers
 */
public class GDSSUGetTradeTest {

    /****************************** GDSSU Constants **************************/
    private static final String GDSSUPROPERTY_FILE = "./src/main/resources/properties/gdssuclient.properties";

    private static final String GDSSUPROPERTY_URL = "defaultQuery.url";

    private static final String PROPERTY_GTN = "gdssu.gtn";

    private static final String PROPERTY_GLN = "gdssu.gln";

    private static final String PROPERTY_TM = "gdssu.tm";

    private static final String PROPERTY_IDENTCOUNT = "gdssu.count";

    private List<String> resultList = null;

    @Before
    public void setUp() {
        String path = GDSSUPROPERTY_FILE;
        Properties props = new Properties();
        InputStream is;
        try {
            is = new FileInputStream(path);
            if (is == null) {
                throw new RuntimeException("Unable to load properties from file " + path);
            }
            props.load(is);
            resultList = getTradeItemInformation(props);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load properties from file " + path);
        }
    }

    /**
     * Tears down.
     */
    @After
    public void tearDown() {
    }

    /**
     * Test method for
     * {@link com.isurf.gdssuclient.GDSSUImpl#getTradeItemInformation(gdssuPropURL, identifierarray)}
     * .
     */
    @Test
    public void testGetTradeItemInformation() {
        assertNotNull(resultList);
    }

    public static List<String> getTradeItemInformation(Properties props) {
        String gdssuPropURL = props.getProperty(GDSSUPROPERTY_URL);
        GDSSUImpl gdssuImpl = new GDSSUImpl();
        List<String> list = null;
        try {
            String identCount = props.getProperty(PROPERTY_IDENTCOUNT);
            if (identCount != null) {
                int count = Integer.valueOf(identCount);
                if (count > 0) {
                    Identifier identifier = null;
                    ArrayList<Identifier> identifierarray = new ArrayList<Identifier>(count);
                    for (int i = 1; i <= count; i++) {
                        identifier = new Identifier();
                        identifier.setGln(props.getProperty(PROPERTY_GLN + i));
                        identifier.setGtin(props.getProperty(PROPERTY_GTN + i));
                        identifier.setTm(props.getProperty(PROPERTY_TM + i));
                        identifierarray.add(identifier);
                    }
                    list = gdssuImpl.getTradeItemInformation(gdssuPropURL, identifierarray);
                    Iterator<String> tradeIter = list.iterator();
                    while (tradeIter.hasNext()) {
                        System.out.println(tradeIter.next());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("demoGDSSU error " + e);
        }
        return list;
    }
}
