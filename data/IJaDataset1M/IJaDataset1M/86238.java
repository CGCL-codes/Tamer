package org.gvsig.symbology;

import java.io.File;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.gvsig.remoteClient.sld.symbolizerTests.TestSLDSymbolizers;
import org.gvsig.symbology.fmap.labeling.parse.TestLabelExpressionParser;
import org.gvsig.symbology.fmap.rendering.TestExpressionParser;
import org.gvsig.symbology.gui.layerproperties.TestILabelingStrategyPanel;
import org.gvsig.symbology.symbols.TestCartographicSupporForSymbol;
import org.gvsig.symbology.symbols.TestSymbols;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;

/**
 * extSymbology
 * AllTests.java
 *
 * 
 * @author jaume dominguez faus - jaume.dominguez@iver.es Jun 16, 2008
 *
 */
public class AllTests extends TestCase {

    private static File baseDataPath;

    private static File baseDriversPath;

    public static Test suite() {
        TestSuite suite = new TestSuite("All tests in extSymbology");
        suite.addTest(TestSLDSymbolizers.suite());
        suite.addTest(TestSymbols.suite());
        suite.addTestSuite(TestCartographicSupporForSymbol.class);
        suite.addTestSuite(TestExpressionParser.class);
        suite.addTestSuite(TestLabelExpressionParser.class);
        suite.addTest(TestILabelingStrategyPanel.suite());
        return suite;
    }

    public static void setUpDrivers() {
        try {
            baseDataPath = new File("src-test/test-data/layer-sample-files/");
            System.out.println(baseDataPath.getAbsolutePath());
            if (!baseDataPath.exists()) throw new Exception("No se encuentra el directorio con datos de prueba");
            String fwAndamiDriverPath = com.iver.cit.gvsig.fmap.AllTests.fwAndamiDriverPath;
            baseDriversPath = new File(fwAndamiDriverPath);
            if (!baseDriversPath.exists()) throw new Exception("Can't find drivers path: " + fwAndamiDriverPath);
            LayerFactory.setDriversPath(baseDriversPath.getAbsolutePath());
            if (LayerFactory.getDM().getDriverNames().length < 1) throw new Exception("Can't find drivers in path: " + fwAndamiDriverPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
