package net.maizegenetics.pal.alignment;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ed
 */
public class Pack1AlignmentTest {

    String infile = "test/datafiles/mdp_genotype.hmp.txt";

    Pack1Alignment p1a;

    public Pack1AlignmentTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        p1a = (Pack1Alignment) ImportUtils.readFromHapmap(infile, "1");
        System.out.println("setUp");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getDataChar method, of class Pack1Alignment.
     */
    @Test
    public void testGetBaseChar() {
        System.out.println("getDataChar");
        char result = p1a.getBaseChar(0, 2);
        assertEquals('G', result);
        result = p1a.getBaseChar(210, 3);
        assertEquals('W', result);
    }

    /**
     * Test of getData method, of class Pack1Alignment.
     */
    @Test
    public void testGetBase() {
        System.out.println("getData");
        byte result = p1a.getBase(0, 2);
        assertEquals((byte) 'G', result);
        result = p1a.getBase(210, 3);
        assertEquals((byte) 'W', result);
    }

    /**
     * Test of getDataRange method, of class Pack1Alignment.
     */
    @Test
    public void testGetBaseRange() {
        System.out.println("getDataRange");
        int seq = 2;
        int startSite = 0;
        int endSite = 5;
        Pack1Alignment instance = p1a;
        String expResult = "CCGTGT";
        String result = new String(instance.getBaseRange(seq, startSite, endSite));
        assertEquals(expResult, result);
    }

    /**
     * Test of getLocusName method, of class Pack1Alignment.
     */
    @Test
    public void testGetLocusName() {
        System.out.println("getLocusName");
        int site = 0;
        Pack1Alignment instance = p1a;
        String expResult = "1";
        String result = instance.getLocusName(site);
        assertEquals(expResult, result);
    }

    /**
     * Test of getLocusPosition method, of class Pack1Alignment.
     */
    @Test
    public void testGetPositionInLocus() {
        System.out.println("getLocusPosition");
        int site = 6;
        Pack1Alignment instance = p1a;
        int expResult = 2973508;
        int result = instance.getPositionInLocus(site);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPositionType method, of class Pack1Alignment.
     */
    @Test
    public void testGetPositionType() {
        System.out.println("getPositionType");
        int site = 0;
        Pack1Alignment instance = p1a;
        byte expResult = PositionType.ALL_GROUP;
        byte result = instance.getPositionType(site);
        assertEquals(expResult, result);
    }
}
