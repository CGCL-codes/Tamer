package com.softserve.mproject.utils.xml;

import com.softserve.mproject.utils.common.TestCommon;
import java.io.File;
import java.util.Collection;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author serrega
 */
public class ImportFromXMLTest {

    public ImportFromXMLTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testXMLImport() throws Exception {
        File f = new File(TestCommon.XML_STANDARD_FILEPATH);
        Collection expResult = TestCommon.getTestPersonCollection(TestCommon.SIMPLE_DATA);
        Collection result = ImportFromXML.Import(f);
        Assert.assertEquals(expResult, result);
    }
}
