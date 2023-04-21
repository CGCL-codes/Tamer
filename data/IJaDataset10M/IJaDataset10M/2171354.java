package org.xmlcml.cif;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;
import nu.xom.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * test CIFParser.
 * 
 * @author pm286
 * 
 */
public class ParserTest extends CIFTestBase {

    static final Logger logger = Logger.getLogger(ParserTest.class.getName());

    private static int nTest = 0;

    /**
     */
    @Before
    public void setUp() {
        if (nTest++ == 0) {
            header("ParserTest");
        }
    }

    /**
	 * Test method for 'uk.co.demon.ursus.cif.CIFParser.parse(BufferedReader)'
	 * @throws IOException 
	 * @throws CIFException 
	 */
    @Test
    public void testParseBufferedReader() throws CIFException, IOException {
        String cifS1 = "data_test";
        CIFParser parser = new CIFParser();
        parser.parse(new BufferedReader(new StringReader(cifS1)));
    }

    @Test
    public void testLoopParse1() throws Exception {
        String cifS2 = "" + "data_test\n" + "_foo bar\n" + "_foo1 'bar'\n" + "_foo2 '_bar'\n" + "#comment\n" + "_semicolon\n" + "; foo\n" + "bar\n" + ";\n" + "loop_\n" + "_a _b _c\n" + "11 12 13\n" + "21 22 23\n" + "31 32 33\n" + "41 42 43\n";
        CIFParser parser2 = new CIFParser();
        parser2.parse(new BufferedReader(new StringReader(cifS2)));
    }

    @Test
    public void testLoopParse2() throws Exception {
        String cifS2 = "" + "data_test\n" + "_foo bar\n" + "_foo1 'bar'\n" + "_foo2 '_bar'\n" + "#comment\n" + "_semicolon\n" + "; foo\n" + "bar\n" + ";\n" + "loop_ _a _b _c\n" + "11 12 13\n" + "21 22 23\n" + "31 32 33\n" + "41 42 43\n";
        CIFParser parser2 = new CIFParser();
        parser2.parse(new BufferedReader(new StringReader(cifS2)));
    }

    @Test
    public void testBadLoopParse1() throws Exception {
        String cifS2 = "" + "data_test\n" + "_foo bar\n" + "_foo1 'bar'\n" + "_foo2 '_bar'\n" + "#comment\n" + "_semicolon\n" + "; foo\n" + "bar\n" + ";\n" + "loop_\n" + "a b c\n" + "11 12 13\n" + "21 22 23\n" + "31 32 33\n" + "41 42 43\n";
        CIFParser parser2 = new CIFParser();
        try {
            parser2.parse(new BufferedReader(new StringReader(cifS2)));
            Assert.fail("should throw parse exception");
        } catch (CIFException e) {
            ;
        }
    }

    @Test
    public void testBadLoopParse() throws CIFException, IOException {
        String cifS2 = "" + "data_test\n" + "_foo bar\n" + "_foo1 'bar'\n" + "_foo2 '_bar'\n" + "#comment\n" + "_semicolon\n" + "; foo\n" + "bar\n" + ";\n" + "loop_ a b c\n" + "11 12 13\n" + "21 22 23\n" + "31 32 33\n" + "41 42 43\n";
        CIFParser parser2 = new CIFParser();
        try {
            parser2.parse(new BufferedReader(new StringReader(cifS2)));
            Assert.fail("should throw parse exception");
        } catch (CIFException e) {
            ;
        }
    }

    /**
	 * Test method for 'uk.co.demon.ursus.cif.CIFParser.parse(BufferedReader)'
	 */
    @Test
    public void testParseBufferedReaderSkip() {
        String cifS2 = "Supplementary Material (ESI) for Chemical Communications\n" + "This journal is � The Royal Society of Chemistry 2000\n" + "CCDC reference number 182/1852\n" + "\n" + "#-----------------------------------------------------------------------\n" + "# CIF data for Ms. No. B007347O\n" + "#\n" + "# Chem. Commun.\n" + "#-----------------------------------------------------------------------\n" + "\n" + "data_cmpd_1\n" + "\n" + "_audit_creation_method            SHELXL-97\n" + "_chemical_name_systematic \n" + "; \n" + " ? \n" + ";\n" + "";
        CIFParser parser = new CIFParser();
        parser.setSkipHeader(true);
        try {
            parser.parse(new BufferedReader(new StringReader(cifS2)));
        } catch (CIFException e) {
            Assert.fail("should never throw " + e);
        } catch (Exception ee) {
            ee.printStackTrace();
            Assert.fail("should never throw " + ee);
        }
        parser.setSkipHeader(false);
        try {
            parser.parse(new BufferedReader(new StringReader(cifS2)));
        } catch (CIFException e) {
            Assert.assertTrue(true);
        } catch (Exception ee) {
            ee.printStackTrace();
            Assert.fail("should never throw " + ee);
        }
    }

    static int parseFiles(URL url, boolean checkDuplicates, int maxCount) {
        boolean failOnError = true;
        return parseFiles(url, checkDuplicates, failOnError, maxCount);
    }

    static int parseFiles(URL url, boolean checkDuplicates, boolean failOnError, int maxCount) {
        File exampleDir = null;
        try {
            exampleDir = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("cannot find url?", e);
        }
        if (exampleDir == null) {
            throw new RuntimeException("Cannot find directory: " + url);
        }
        boolean fail = false;
        File[] files = exampleDir.listFiles();
        Assert.assertNotNull("there should be files in " + exampleDir, files);
        for (int i = 0; i < files.length; i++) {
            if (i == maxCount) {
                LOG.debug("FINISHED AFTER: " + maxCount + " FILES");
                break;
            }
            File file = files[i];
            LOG.debug("CIF: " + file);
            if (file.getName().endsWith(".cif")) {
                if (failOnError) {
                    LOG.debug("testing: " + file);
                }
                if (!parse(file, checkDuplicates)) {
                    if (!failOnError) {
                        logger.severe("fail to parse " + file);
                    } else {
                        fail = failOnError;
                    }
                }
            }
        }
        if (fail) {
            Assert.fail("one or more example files failed to parse");
        }
        return files.length;
    }

    @SuppressWarnings("unused")
    static boolean parse(File file, boolean checkDuplicates) {
        Exception ee = null;
        BufferedReader br = null;
        FileReader fr = null;
        try {
            CIFParser parser = new CIFParser();
            parser.setCheckDuplicates(checkDuplicates);
            Document doc = parser.parse(new BufferedReader(new FileReader(file)));
        } catch (CIFException e) {
            ee = e;
        } catch (FileNotFoundException e) {
            ee = e;
        } catch (IOException e) {
            ee = e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.info(e.getMessage());
                }
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    logger.info(e.getMessage());
                }
            }
        }
        if (ee != null) {
            logger.severe("failed to parse: " + file + "\n..... because: " + ee);
        }
        return (ee == null);
    }

    /** test */
    @Test
    public void testHeuristics() {
        String badCIF = "data_foo\n" + "_bar valok\n" + "_foo 'a b is ok'\n" + "_plugh c d is not ok\n" + "_back 'to normal'\n" + "";
        CIFParser parser = new CIFParser();
        CIF cif = null;
        try {
            cif = (CIF) parser.parse(new BufferedReader(new StringReader(badCIF))).getRootElement();
        } catch (CIFException e) {
            Assert.fail("should never throw " + e);
        } catch (Exception ee) {
            ee.printStackTrace();
            Assert.fail("should never throw " + ee);
        }
        cif.debug();
    }
}
