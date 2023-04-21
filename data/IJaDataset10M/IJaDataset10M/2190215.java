package se.vgregion.metaservice.LemmatisationService.model;

import java.util.LinkedList;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author sture.svensson
 */
public class DictionaryTest extends TestCase {

    Dictionary dict = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        List<String> dictionaryList = new LinkedList<String>();
        dictionaryList.add("gris,grisen,grisarna");
        dictionaryList.add("grip,gripen,griparna");
        dictionaryList.add("apa,apan,aporna");
        dict = new Dictionary(dictionaryList);
    }

    /**
     * Test of getWords method, of class Dictionary.
     */
    public void testGetWords1() {
        System.out.println("getWords1");
        String query = "grisen";
        List<List<String>> expResult = new LinkedList<List<String>>();
        List<String> expSubResult = new LinkedList<String>();
        expSubResult.add("gris");
        expSubResult.add("grisen");
        expSubResult.add("grisarna");
        expResult.add(expSubResult);
        List<List<String>> result = dict.getWords(query);
        assertEquals(expResult, result);
    }

    /**
     * Test of getWords method, of class Dictionary.
     */
    public void testGetWords2() {
        System.out.println("getWords2");
        String query = "griparna";
        List<List<String>> expResult = new LinkedList<List<String>>();
        List<String> expSubResult = new LinkedList<String>();
        expSubResult.add("grip");
        expSubResult.add("gripen");
        expSubResult.add("griparna");
        expResult.add(expSubResult);
        List<List<String>> result = dict.getWords(query);
        assertEquals(expResult, result);
    }

    /**
     * Test of getWords method, of class Dictionary.
     */
    public void testGetWordsEmpty() {
        System.out.println("getWordsEmpty");
        String query = "gri";
        List<List<String>> expResult = new LinkedList<List<String>>();
        List<List<String>> result = dict.getWords(query);
        assertEquals(expResult, result);
    }

    /**
     * Test of getNrOfLemmas method, of class Dictionary.
     */
    public void testGetNrOfLemmas() {
        System.out.println("getNrOfLemmas");
        int expResult = 3;
        int result = dict.getNrOfLemmas();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNrOfWords method, of class Dictionary.
     */
    public void testGetNrOfWords() {
        System.out.println("getNrOfWords");
        int expResult = 9;
        int result = dict.getNrOfWords();
        assertEquals(expResult, result);
    }
}
