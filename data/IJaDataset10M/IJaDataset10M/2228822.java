package org.mmi.ont.registry;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bermudez
 */
public class RegistryTest {

    public RegistryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of search method, of class Registry.
     */
    @Test
    public void testSearch() {
        System.out.println("search");
        String text = "";
        Registry instance = new Registry();
        List<String> expResult = null;
        List<String> result = instance.search(text);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of createSearchableStringAll method, of class Registry.
     */
    @Test
    public void testCreateSearchableStringAll() {
        System.out.println("createSearchableStringAll");
        Resource r = null;
        Registry instance = new Registry();
        String expResult = "";
        String result = instance.createSearchableStringAll(r);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValueAsString_LocalName method, of class Registry.
     */
    @Test
    public void testGetValueAsString_LocalName() {
        System.out.println("getValueAsString_LocalName");
        Registry instance = new Registry();
        RDFNode node = null;
        assertEquals("", instance.getValueAsString_LocalName(node));
        Resource res = instance.getModel().createResource();
        assertEquals("", instance.getValueAsString_LocalName(res));
        res.addProperty(DC.subject, "subject");
        assertEquals("", instance.getValueAsString_LocalName(res));
    }

    @Test
    public void testBreakString() {
        Registry instance = new Registry();
        String s = "this_is_a_piece_of_cake";
        String s2 = instance.breakString(s, 5);
        assertEquals("this_ is_a_ piece _of_c ake", s2);
    }

    /**
     * Test of isContainAnd method, of class Registry.
     */
    @Test
    public void testIsContainAnd() {
        System.out.println("isContainAnd");
        String queryString = "";
        String targetText = "";
        Registry instance = new Registry();
        boolean expResult = false;
        boolean result = instance.isContainAnd(queryString, targetText);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of isContainAndNormal method, of class Registry.
     */
    @Test
    public void testIsContainAndNormal() {
        System.out.println("isContainAndNormal");
        String queryString = "";
        String targetText = "";
        Registry instance = new Registry();
        boolean expResult = false;
        boolean result = instance.isContainAndNormal(queryString, targetText);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of isContainAndMinus method, of class Registry.
     */
    @Test
    public void testIsContainAndMinus() {
        System.out.println("isContainAndMinus");
        String queryString = "";
        String targetText = "";
        Registry instance = new Registry();
        boolean expResult = false;
        boolean result = instance.isContainAndMinus(queryString, targetText);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of toArrayOfWords method, of class Registry.
     */
    @Test
    public void testToArrayOfWords() {
        System.out.println("toArrayOfWords");
        String s = "";
        Registry instance = new Registry();
        String[] expResult = null;
        String[] result = instance.toArrayOfWords(s);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of contains method, of class Registry.
     */
    @Test
    public void testContains() {
        System.out.println("contains");
        String target = "";
        String given = "";
        Registry instance = new Registry();
        boolean expResult = false;
        boolean result = instance.contains(target, given);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
}
