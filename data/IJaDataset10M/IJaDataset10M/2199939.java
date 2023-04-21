package edu.stanford;

import java.io.IOException;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * junit4 tests for Stanford University's standard number fields
 * @author Naomi Dushay
 */
public class StandardNumberTests extends AbstractStanfordBlacklightTest {

    /**
	 * Test population of oclc field
	 */
    @Test
    public final void testOCLC() throws IOException, ParserConfigurationException, SAXException {
        String fldName = "oclc";
        createIxInitVars("oclcNumTests.mrc");
        assertDocHasFieldValue("035withOCoLC-M", fldName, "656729");
        assertDocHasNoFieldValue("035withOCoLC-MnoParens", fldName, "656729");
        assertDocHasFieldValue("Mult035onlyOneGood", fldName, "656729");
        assertDocHasNoFieldValue("Mult035onlyOneGood", fldName, "164324897");
        assertDocHasNoFieldValue("Mult035onlyOneGood", fldName, "1CSUO98-B6924");
        assertDocHasNoFieldValue("Mult035onlyOneGood", fldName, "180776170");
        assertDocHasFieldValue("079onlyocm", fldName, "38052115");
        assertDocHasFieldValue("079onlyocn", fldName, "122811369");
        assertDocHasFieldValue("079badPrefix", fldName, "180776170");
        assertDocHasNoFieldValue("079badPrefix", fldName, "66654321");
        assertDocHasFieldValue("079onlywithz", fldName, "46660954");
        assertDocHasNoFieldValue("079onlywithz", fldName, "38158328");
        assertDocHasFieldValue("079withbad035s", fldName, "12345666");
        assertDocHasNoFieldValue("079withbad035s", fldName, "164324897");
        assertDocHasNoFieldValue("079withbad035s", fldName, "CSUO98-B6924");
        assertDocHasFieldValue("Good035withGood079", fldName, "656729");
        assertDocHasNoFieldValue("Good035withGood079", fldName, "00666000");
        assertDocHasFieldValue("035OCoLConly", fldName, "180776170");
        assertDocHasNoFieldValue("035OCoLConly", fldName, "164324897");
        assertDocHasNoFieldValue("035OCoLConly", fldName, "CSUO98-B6924");
        assertDocHasFieldValue("035bad079OCoLConly", fldName, "180776170");
        assertDocHasNoFieldValue("035bad079OCoLConly", fldName, "bad 079");
        assertDocHasNoField("035and079butNoOclc", fldName);
        assertDocHasFieldValue("MultOclcNums", fldName, "656729");
        assertDocHasFieldValue("MultOclcNums", fldName, "38052115");
        assertDocHasFieldValue("MultOclcNums", fldName, "38403775");
        assertDocHasNoFieldValue("MultOclcNums", fldName, "180776170");
        assertDocHasNoFieldValue("MultOclcNums", fldName, "00666000");
        Set<String> docIds = new HashSet<String>();
        docIds.add("035withOCoLC-M");
        docIds.add("Mult035onlyOneGood");
        docIds.add("MultOclcNums");
        docIds.add("Good035withGood079");
        assertSearchResults(fldName, "656729", docIds);
        docIds.clear();
        docIds.add("079onlyocm");
        docIds.add("MultOclcNums");
        assertSearchResults(fldName, "38052115", docIds);
        docIds.clear();
        docIds.add("079badPrefix");
        docIds.add("035OCoLConly");
        docIds.add("035bad079OCoLConly");
        assertSearchResults(fldName, "180776170", docIds);
        assertSingleResult("079onlyocn", fldName, "122811369");
        assertSingleResult("079onlywithz", fldName, "46660954");
        assertSingleResult("079withbad035s", fldName, "12345666");
        assertSingleResult("MultOclcNums", fldName, "38403775");
        assertZeroResults(fldName, "1CSUO98-B6924");
        assertZeroResults(fldName, "CSUO98-B6924");
        assertZeroResults(fldName, "164324897");
        assertZeroResults(fldName, "00666000");
        assertZeroResults(fldName, "66654321");
        assertZeroResults(fldName, "38158328");
        assertZeroResults(fldName, "\"bad 079\"");
    }

    /**
	 * Test population of isbn_display: the ISBNs used for external 
	 *  lookups (e.g. Google Book Search)
	 */
    @Test
    public final void testISBNdisplay() throws IOException, ParserConfigurationException, SAXException {
        String fldName = "isbn_display";
        createIxInitVars("isbnTests.mrc");
        assertDocHasNoField("No020", fldName);
        assertDocHasNoField("020noSubaOrz", fldName);
        assertDocHasFieldValue("020suba10digit", fldName, "1417559128");
        assertDocHasFieldValue("020suba10endsX", fldName, "123456789X");
        assertDocHasFieldValue("020suba10trailingText", fldName, "1234567890");
        assertDocHasFieldValue("020suba10trailingText", fldName, "0123456789");
        assertDocHasFieldValue("020suba10trailingText", fldName, "0521672694");
        assertDocHasFieldValue("020suba10trailingText", fldName, "052185668X");
        assertDocHasFieldValue("020suba13", fldName, "9780809424887");
        assertDocHasFieldValue("020suba13endsX", fldName, "979123456789X");
        assertDocHasNoField("020suba13bad", fldName);
        assertDocHasNoFieldValue("020suba13bad", fldName, "000123456789X");
        assertDocHasFieldValue("020suba13trailingText", fldName, "978185585039X");
        assertDocHasFieldValue("020suba13trailingText", fldName, "9780809424887");
        assertDocHasFieldValue("020suba13trailingText", fldName, "9780809424870");
        assertDocHasFieldValue("020subaMult", fldName, "0809424886");
        assertDocHasFieldValue("020subaMult", fldName, "123456789X");
        assertDocHasFieldValue("020subaMult", fldName, "1234567890");
        assertDocHasFieldValue("020subaMult", fldName, "979123456789X");
        assertDocHasFieldValue("020subaMult", fldName, "9780809424887");
        assertDocHasFieldValue("020subaMult", fldName, "9781855850484");
        assertDocHasFieldValue("020subz10digit", fldName, "9876543210");
        assertDocHasFieldValue("020subz10endsX", fldName, "123456789X");
        assertDocHasFieldValue("020subz10trailingText", fldName, "1234567890");
        assertDocHasFieldValue("020subz10trailingText", fldName, "0123456789");
        assertDocHasFieldValue("020subz10trailingText", fldName, "0521672694");
        assertDocHasFieldValue("020subz13digit", fldName, "9780809424887");
        assertDocHasFieldValue("020subz13endsX", fldName, "979123456789X");
        assertDocHasFieldValue("020subz13trailingText", fldName, "978185585039X");
        assertDocHasFieldValue("020subz13trailingText", fldName, "9780809424887");
        assertDocHasFieldValue("020subz13trailingText", fldName, "9780809424870");
        assertDocHasFieldValue("020multSubz", fldName, "9802311987");
        assertDocHasFieldValue("020multSubz", fldName, "9802311995");
        assertDocHasFieldValue("020multSubz", fldName, "9802312002");
        assertDocHasFieldValue("020multSubz", fldName, "9876543210");
        assertDocHasFieldValue("020multSubz", fldName, "123456789X");
        assertDocHasFieldValue("020multSubz", fldName, "9780809424887");
        assertDocHasFieldValue("020multSubz", fldName, "979123456789X");
        assertDocHasFieldValue("020multSubz", fldName, "9780809424870");
        assertDocHasFieldValue("020SubaAndz", fldName, "0123456789");
        assertDocHasFieldValue("020SubaAndz", fldName, "0521672694");
        assertDocHasNoFieldValue("020SubaAndz", fldName, "9802311987");
        assertDocHasFieldValue("020SubaAndz", fldName, "052185668X");
        assertDocHasNoFieldValue("020SubaAndz", fldName, "123456789X");
        assertDocHasNoFieldValue("020SubaAndz", fldName, "9780809424887");
    }

    /**
	 * Test population of isbn_search field: the ISBNs that an end user can 
	 *  search for in our index
	 */
    @Test
    public final void testISBNsearch() throws IOException, ParserConfigurationException, SAXException {
        String fldName = "isbn_search";
        createIxInitVars("isbnTests.mrc");
        Set<String> docIds = new HashSet<String>();
        docIds.add("020suba10trailingText");
        docIds.add("020SubaAndz");
        assertSearchResults(fldName, "052185668X", docIds);
        docIds.clear();
        docIds.add("020suba13");
        docIds.add("020suba13trailingText");
        docIds.add("020subaMult");
        docIds.add("020subz13digit");
        docIds.add("020subz13trailingText");
        docIds.add("020multSubz");
        docIds.add("020SubaAndz");
        assertSearchResults(fldName, "9780809424887", docIds);
    }

    /**
	 * isbn_search should be case insensitive
	 */
    @Test
    public final void testISBNCaseInsensitive() throws IOException, ParserConfigurationException, SAXException {
        String fldName = "isbn_search";
        createIxInitVars("isbnTests.mrc");
        Set<String> docIds = new HashSet<String>();
        docIds.add("020suba10trailingText");
        docIds.add("020SubaAndz");
        assertSearchResults(fldName, "052185668X", docIds);
        assertSearchResults(fldName, "052185668x", docIds);
    }

    /**
	 * Test population of issn_search field: the ISSNs that an end user can 
	 *  search for in our index
	 */
    @Test
    public final void testISSNsearch() throws IOException, ParserConfigurationException, SAXException {
        String fldName = "issn_search";
        createIxInitVars("issnTests.mrc");
        assertSingleResult("022suba", fldName, "1047-2010");
        assertSingleResult("022subaX", fldName, "1047-201X");
        Set<String> docIds = new HashSet<String>();
        docIds.add("022subL");
        docIds.add("022subAandL");
        docIds.add("022subLandM");
        assertSearchResults(fldName, "0796-5621", docIds);
        assertSingleResult("022subM", fldName, "0863-4564");
        assertSingleResult("022subY", fldName, "0813-1964");
        assertSingleResult("022subMandZ", fldName, "1144-5858");
        assertSingleResult("022subLandM", fldName, "0038-6073");
        assertSingleResult("022subMandZ", fldName, "0103-8915");
        assertSingleResult("022subZ", fldName, "1144-585X");
        assertSingleResult("022subAandL", fldName, "0945-2419");
        assertSingleResult("Two022a", fldName, "0666-7770");
        assertSingleResult("Two022a", fldName, "1221-2112");
        assertSingleResult("022subM", fldName, "08634564");
        assertSingleResult("022subZ", fldName, "1144585X");
    }

    /**
	 * ISSNs should be searchable with or without the hyphen
	 */
    @Test
    public final void testISSNhyphens() throws IOException, ParserConfigurationException, SAXException {
        String fldName = "issn_search";
        createIxInitVars("issnTests.mrc");
        assertSingleResult("022subM", fldName, "0863-4564");
        assertSingleResult("022subM", fldName, "08634564");
        assertSingleResult("022subZ", fldName, "1144-585X");
        assertSingleResult("022subZ", fldName, "1144585X");
    }

    /**
	 * issn_search should be case insensitive
	 */
    @Test
    public final void testISSNCaseInsensitive() throws IOException, ParserConfigurationException, SAXException {
        String fldName = "issn_search";
        createIxInitVars("issnTests.mrc");
        assertSingleResult("022subZ", fldName, "1144-585X");
        assertSingleResult("022subZ", fldName, "1144-585x");
    }

    /**
	 * Test population of lccn field
	 */
    @Test
    public final void testLCCN() throws ParserConfigurationException, IOException, SAXException {
        String fldName = "lccn";
        createIxInitVars("lccnTests.mrc");
        assertDocHasNoField("No010", fldName);
        assertDocHasFieldValue("010suba8digit", fldName, "85153773");
        assertDocHasFieldValue("010suba10digit", fldName, "2001627090");
        assertDocHasFieldValue("010suba8digitPfx", fldName, "a  60123456");
        assertDocHasFieldValue("010suba8digit2LetPfx", fldName, "bs 66654321");
        assertDocHasFieldValue("010suba8digit3LetPfx", fldName, "cad77665544");
        assertDocHasFieldValue("010suba10digitPfx", fldName, "r 2001336783");
        assertDocHasFieldValue("010suba10digit2LetPfx", fldName, "ne2001045944");
        assertDocHasFieldValue("010suba8digitSfx", fldName, "79139101");
        assertDocHasFieldValue("010suba10digitSfx", fldName, "2006002284");
        assertDocHasFieldValue("010suba8digitSfx2", fldName, "73002284");
        assertDocHasFieldValue("010subz", fldName, "20072692384");
        assertDocHasFieldValue("010subaAndZ", fldName, "76647633");
        assertDocHasNoFieldValue("010subaAndZ", fldName, "76000587");
        assertDocHasFieldValue("010multSubZ", fldName, "76647633");
        assertDocHasNoFieldValue("010multSubZ", fldName, "2000123456");
    }
}
