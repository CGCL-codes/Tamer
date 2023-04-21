package org.equanda.generate;

import org.equanda.util.IniFile;
import junit.framework.TestCase;

/**
 * Checks for invalid selects
 *
 * @author NetRom team
 */
public class SelectTest extends TestCase {

    private IniFile ini = new IniFile();

    protected void setUp() throws Exception {
        ini.addValue("config", "template", "null");
        ini.addValue("config", "outputdir", ".");
    }

    public void testDirectSelectionsAndSubSelects() throws Exception {
        ini.addValue("config", "definition", "classpath://SelectTest/DirectSelectionsAndSubselects.xml");
        Generator gen = new Generator(ini, System.getProperty("equanda-test.omroot.dir"));
        gen.generate();
        assertTrue(gen.getFailed());
    }

    public void testSelectionInvalidTest() throws Exception {
        ini.addValue("config", "definition", "classpath://SelectTest/SelectionInvalidTest.xml");
        Generator gen = new Generator(ini, System.getProperty("equanda-test.omroot.dir"));
        gen.generate();
        assertTrue(gen.getFailed());
    }

    public void testOrderInvalidField() throws Exception {
        ini.addValue("config", "definition", "classpath://SelectTest/OrderInvalidField.xml");
        Generator gen = new Generator(ini, System.getProperty("equanda-test.omroot.dir"));
        gen.generate();
        assertTrue(gen.getFailed());
    }

    public void testOrderBooleanField() throws Exception {
        ini.addValue("config", "definition", "classpath://SelectTest/OrderBooleanField.xml");
        Generator gen = new Generator(ini, System.getProperty("equanda-test.omroot.dir"));
        gen.generate();
        assertTrue(gen.getFailed());
    }

    public void testOrderMultipleField() throws Exception {
        ini.addValue("config", "definition", "classpath://SelectTest/OrderMultipleField.xml");
        Generator gen = new Generator(ini, System.getProperty("equanda-test.omroot.dir"));
        gen.generate();
        assertTrue(gen.getFailed());
    }

    public void testOrderLinkField() throws Exception {
        ini.addValue("config", "definition", "classpath://SelectTest/OrderLinkField.xml");
        Generator gen = new Generator(ini, System.getProperty("equanda-test.omroot.dir"));
        gen.generate();
        assertTrue(gen.getFailed());
    }

    public void testSingleSelectWithLimit() throws Exception {
        ini.addValue("config", "definition", "classpath://SelectTest/SingleSelectWithLimit.xml");
        Generator gen = new Generator(ini, System.getProperty("equanda-test.omroot.dir"));
        gen.generate();
        assertTrue(gen.getFailed());
    }

    public void testOperatorAddNoSubSelects() throws Exception {
        ini.addValue("config", "definition", "classpath://SelectTest/OperatorAddNoSubselects.xml");
        Generator gen = new Generator(ini, System.getProperty("equanda-test.omroot.dir"));
        gen.generate();
        assertTrue(gen.getFailed());
    }

    public void testOperatorTryNoSubSelects() throws Exception {
        ini.addValue("config", "definition", "classpath://SelectTest/OperatorTryNoSubselects.xml");
        Generator gen = new Generator(ini, System.getProperty("equanda-test.omroot.dir"));
        gen.generate();
        assertTrue(gen.getFailed());
    }

    public void testOperatorAddSingleSelect() throws Exception {
        ini.addValue("config", "definition", "classpath://SelectTest/OperatorAddSingleSelect.xml");
        Generator gen = new Generator(ini, System.getProperty("equanda-test.omroot.dir"));
        gen.generate();
        assertTrue(gen.getFailed());
    }
}
