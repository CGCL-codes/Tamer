package org.gvsig.normalization.operations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.gvsig.normalization.patterns.NormalizationPattern;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.drivers.dbf.DBFDriver;

/**
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 * 
 */
public class TestAllTypeData extends TestCase {

    private static final Logger log = PluginServices.getLogger();

    NormalizationPattern pat = new NormalizationPattern();

    ArrayList<String> chains = new ArrayList<String>();

    File file;

    public void setUp() {
        File f = new File("src-test/org/gvsig/normalization/operations/testdata/pattotal.xml");
        try {
            pat.loadFromXML(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        chains.add("Javier#-7;-0.2578&01/10/2007&02/09&02-78");
        chains.add("Javier#-7;-0.2578&01/10/2007&02/09&02-78");
        chains.add("v");
        chains.add("");
        chains.add("Javier#-7;-0.2578&01/10/2007&02/09&02-78");
        chains.add("Javier#-7;-0.2578&01/10/2007&02/09&02-78");
    }

    public void testAllTypeData() {
        try {
            file = File.createTempFile("alltype", ".dbf");
            StringListNormalization norm = new StringListNormalization(pat, chains, file);
            norm.preProcess();
            for (int i = 0; i < chains.size(); i++) {
                try {
                    norm.fillRow(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            norm.postProcess();
            DBFDriver test = new DBFDriver();
            int nFields = -1;
            long nRows = -1;
            String val01 = null;
            String val03 = null;
            String val10 = null;
            String val30 = null;
            String val52 = null;
            try {
                test.open(file);
                nFields = test.getFieldCount();
                nRows = test.getRowCount();
                val01 = test.getFieldValue(0, 1).toString().trim();
                val03 = test.getFieldValue(0, 3).toString().trim();
                val10 = test.getFieldValue(1, 0).toString().trim();
                val30 = test.getFieldValue(3, 0).toString().trim();
                val52 = test.getFieldValue(5, 2).toString().trim();
                test.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            assertEquals(nFields, 6);
            assertEquals(nRows, 6);
            log.info(val01);
            log.info(val03);
            log.info(val10);
            log.info(val30);
            log.info(val52);
            assertEquals(val01, "-7");
            assertEquals(val03, "01-oct-2007");
            assertEquals(val10, "Javier");
            assertEquals(val30, "");
            assertEquals(val52, "-2578.0");
            file.delete();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void tearDown() {
        log.info("TEST FINISHED");
    }
}
