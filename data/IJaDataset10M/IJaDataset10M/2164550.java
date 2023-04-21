package edu.stanford;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.*;
import org.xml.sax.SAXException;

/**
 * junit4 tests for Stanford University's handling of diacritics
 * 
 * searching should not be sensitive to diacritics:  searches should return
 *  the same results with and without the diacritics (and with common expansions
 *  like ö (o umlaut) --> oe  .
 * 
 * @author Naomi Dushay
 */
public class DiacriticTests extends AbstractStanfordBlacklightTest {

    private String fldName = "author_1xx_search";

    @SuppressWarnings("unused")
    @Before
    public final void initVars() throws ParserConfigurationException, IOException, SAXException {
        createIxInitVars("diacriticTests.mrc");
    }

    /**
	 * Test searching of chars with umlaut
	 */
    @Test
    public final void testUmlaut() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("2797607", fldName, "Über");
        assertSingleResult("2797607", fldName, "über");
        assertSingleResult("2797607", fldName, "uber");
        assertSingleResult("2797607", fldName, "Fragwürdigkeit");
        assertSingleResult("2797607", fldName, "Fragwurdigkeit");
        assertSingleResult("2797607", fldName, "Murtomäki");
        assertSingleResult("2797607", fldName, "Murtomaki");
    }

    /**
	 * Test searching of chars with acute
	 */
    @Test
    public final void testAcute() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("1391080", fldName, "contemporánea");
        assertSingleResult("2442876", fldName, "piétons");
        assertSingleResult("1391080", fldName, "José María");
        assertSingleResult("1391080", fldName, "Gijón");
        assertSingleResult("1391080", fldName, "jesús.");
        assertSingleResult("1391080", fldName, "contemporanea");
        assertSingleResult("2442876", fldName, "pietons");
        assertSingleResult("1391080", fldName, "Jose Maria");
        assertSingleResult("1391080", fldName, "Gijon");
        assertSingleResult("1391080", fldName, "jesus.");
        assertSingleResult("1391080", fldName, "José Maria");
        assertSingleResult("1391080", fldName, "Jose María");
        assertSingleResult("1391080", fldName, "Gijón jesús");
        assertSingleResult("1391080", fldName, "Gijón jesus");
        assertSingleResult("1391080", fldName, "Gijon jesús");
        assertSingleResult("1391080", fldName, "Gijon jesus");
    }

    /**
	 * Test searching of chars with grave
	 */
    @Test
    public final void testGrave() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("3027805", fldName, "verità");
        assertSingleResult("3027805", fldName, "verita");
        assertSingleResult("3027805", fldName, "è");
        assertSingleResult("3027805", fldName, "e");
    }

    /**
	 * Test searching of chars with circumflex
	 */
    @Test
    public final void testCircumflex() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("3400092", fldName, "ancêtres");
        assertSingleResult("3400092", fldName, "ancetres");
    }

    /**
	 * Test searching of chars with tilde
	 */
    @Test
    public final void testTilde() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("6493823", fldName, "Muñoz");
        assertSingleResult("6493823", fldName, "munoz");
        assertSingleResult("6493823", fldName, "españa");
        assertSingleResult("6493823", fldName, "espana");
    }

    /**
	 * Test searching of chars with cedilla (and tilde)
	 */
    @Test
    public final void testCedilla() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("7651581", fldName, "exaltação");
        assertSingleResult("7651581", fldName, "exaltacão");
        assertSingleResult("7651581", fldName, "exaltaçao");
        assertSingleResult("7651581", fldName, "exaltacao");
    }

    /**
	 * Test searching of chars with ligature (russian)
	 */
    @Test
    public final void testLigature() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("5", fldName, "tysi͡acha");
        assertSingleResult("5", fldName, "tysi͡acha");
        assertSingleResult("5", fldName, "tysiacha");
    }

    /**
	 * Test searching of chars with soft sign - miagkii znak - (russian)
	 */
    @Test
    public final void testSoftZnak() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("66", fldName, "vosem'sot");
        assertSingleResult("66", fldName, "vosemsot");
    }

    public final void testSoftZnakLots() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("66", fldName, "vosem'sot");
        assertSingleResult("66", fldName, "vosemsot");
        assertSingleResult("66", fldName, "semьdesiatykhtysiacha");
        assertSingleResult("66", fldName, "sem'desiatykhtysiacha");
        assertSingleResult("66", fldName, "semdesiatykhtysiacha");
        assertSingleResult("66", fldName, "vosemьsot semьdesiatykhtysiacha");
        assertSingleResult("66", fldName, "vosemьsot semdesiatykhtysiacha");
        assertSingleResult("66", fldName, "vosemsot semьdesiatykhtysiacha");
        assertSingleResult("66", fldName, "vosemьsot sem'desiatykhtysiacha");
        assertSingleResult("66", fldName, "vosemsot sem'desiatykhtysiacha");
        assertSingleResult("66", fldName, "vosem'sot semdesiatykhtysiacha");
        assertSingleResult("66", fldName, "vosemsot semdesiatykhtysiacha");
    }

    /**
	 * Test searching of chars with hard sign - tverdyi znak - (russian)
	 */
    @Test
    public final void testHardZnak() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("6", fldName, "Obʺedinenie");
        assertSingleResult("6", fldName, "Obedinenie");
    }

    /**
	 * Test searching of chars with caron - (russian)
	 */
    @Test
    public final void testCaron() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("345228", fldName, "povesť");
        assertSingleResult("345228", fldName, "povest'");
        assertSingleResult("345228", fldName, "povest");
        assertSingleResult("345228", fldName, "dokumentalnai*");
    }

    /**
	 * Test searching of lower case i with macron - (russian)
	 */
    @Test
    public final void testRussianMacronOverI() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("2099904", fldName, "istorīi");
        assertSingleResult("2099904", fldName, "istorii");
    }

    /**
	 * Test searching of chars with macron - (Japanese)
	 */
    @Test
    public final void testMacron() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("6676531", fldName, "kaisō");
        assertSingleResult("6676531", fldName, "kaiso");
    }

    public final void testAeLigature() throws ParserConfigurationException, IOException, SAXException {
        tearDown();
        createIxInitVars("aeoeLigatureTests.mrc");
        String fldName = "title_245a_search";
        Set<String> docIds = new HashSet<String>();
        docIds.add("Ae1");
        docIds.add("Ae2");
        assertSearchResults(fldName, "Æon", docIds);
        assertSearchResults(fldName, "Aeon", docIds);
        docIds.clear();
        docIds.add("ae1");
        docIds.add("ae2");
        assertSearchResults(fldName, "Encyclopædia", docIds);
        assertSearchResults(fldName, "Encyclopaedia", docIds);
    }

    public final void testOeLigature() throws ParserConfigurationException, IOException, SAXException {
        tearDown();
        createIxInitVars("aeoeLigatureTests.mrc");
        String fldName = "title_245a_search";
        Set<String> docIds = new HashSet<String>();
        docIds.add("Oe1");
        docIds.add("Oe2");
        assertSearchResults(fldName, "Œlalala", docIds);
        assertSearchResults(fldName, "Oelalala", docIds);
        docIds.clear();
        docIds.add("oe1");
        docIds.add("oe2");
        assertSearchResults(fldName, "Cœurdevey", docIds);
        assertSearchResults(fldName, "Coeurdevey", docIds);
    }

    public final void testKreska() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("7", fldName, "Śpiewy");
        assertSingleResult("7", fldName, "spiewy");
    }

    public final void testSlash() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("8", fldName, "nnunn");
    }

    public final void testCaron2() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("9", fldName, "Latviesǔ");
        assertSingleResult("9", fldName, "Latviesu");
    }

    public final void testOgonek() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("10", fldName, "lokalių");
        assertSingleResult("10", fldName, "lokaliu");
        assertZeroResults(fldName, "lokaliy");
    }

    public final void testOverdot() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("10", fldName, "pridėtos");
        assertSingleResult("10", fldName, "pridetos");
    }

    public final void testVietnamese() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("6676531", fldName, "Lập");
        assertSingleResult("6676531", fldName, "lap");
    }

    public final void testHebrew() throws ParserConfigurationException, IOException, SAXException {
        assertSingleResult("13", fldName, "עברית");
        assertSingleResult("13", fldName, "ivrit");
    }
}
