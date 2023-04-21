package org.equanda.generate;

import org.equanda.util.IniFile;
import junit.framework.TestCase;

/**
 * Checks for constraints for Blob/Clob fields
 *
 * @author Florin
 */
public class LobTest extends TestCase {

    private IniFile ini = new IniFile();

    protected void setUp() throws Exception {
        ini.addValue("config", "template", "null");
        ini.addValue("config", "outputdir", ".");
    }

    public void testBlobChoice() throws Exception {
        ini.addValue("config", "definition", "classpath://LobTest/BlobChoice.xml");
        Generator gen = new Generator(ini, System.getProperty("equanda-test.omroot.dir"));
        gen.generate();
        assertTrue(gen.getFailed());
    }

    public void testClobChoice() throws Exception {
        ini.addValue("config", "definition", "classpath://LobTest/ClobChoice.xml");
        Generator gen = new Generator(ini, System.getProperty("equanda-test.omroot.dir"));
        gen.generate();
        assertTrue(gen.getFailed());
    }

    public void testBlobReference() throws Exception {
        ini.addValue("config", "definition", "classpath://LobTest/BlobReference.xml");
        Generator gen = new Generator(ini, System.getProperty("equanda-test.omroot.dir"));
        gen.generate();
        assertTrue(gen.getFailed());
    }

    public void testClobReference() throws Exception {
        ini.addValue("config", "definition", "classpath://LobTest/ClobReference.xml");
        Generator gen = new Generator(ini, System.getProperty("equanda-test.omroot.dir"));
        gen.generate();
        assertTrue(gen.getFailed());
    }

    public void testBlobDescription() throws Exception {
        ini.addValue("config", "definition", "classpath://LobTest/BlobDescription.xml");
        Generator gen = new Generator(ini, System.getProperty("equanda-test.omroot.dir"));
        gen.generate();
        assertTrue(gen.getFailed());
    }

    public void testBlobSelection() throws Exception {
        ini.addValue("config", "definition", "classpath://LobTest/BlobSelection.xml");
        Generator gen = new Generator(ini, System.getProperty("equanda-test.omroot.dir"));
        gen.generate();
        assertTrue(gen.getFailed());
    }
}
