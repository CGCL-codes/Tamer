package junit.iono;

import org.junit.*;
import org.junit.Assert.*;
import junit.framework.JUnit4TestAdapter;
import java.io.*;
import java.util.regex.*;
import dbaccess.iono2.*;
import dbaccess.util2.*;

/** 
 * JUnit TestCase for SaoGrp1 class 
 */
public class SaoGrp1Test {

    SaoGrp1 sao;

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SaoGrp1Test.class);
    }

    /**
    * Before each test 
    */
    @Before
    public void init() {
        sao = new SaoGrp1();
    }

    /**
    * Test parseGrp() for SAO group 1.
    */
    @Test
    public void testParseGrp1() {
        String grp1data = "  0.000  0.000 40.000265.300 22.000                 ";
        sao.setGrpData(grp1data);
        int Nerror = sao.parseGrp();
        Assert.assertEquals(0.0f, sao.getGyroFreq(), 0f);
        Assert.assertEquals(0.0f, sao.getDipAngle(), 0f);
        Assert.assertEquals(40.000f, sao.getLat(), .0001f);
        Assert.assertEquals(265.300f, sao.getLon(), .0001f);
        Assert.assertEquals("22.000", sao.getSunspotNumber());
        Assert.assertEquals(0, Nerror);
    }

    /**
    * Test parseGrp() for SAO group 1.
    */
    @Test
    public void testParseGrp2() {
        String grp1data = "  1.200 66.000 41.900 12.500 80.000                 ";
        sao.setGrpData(grp1data);
        int Nerror = sao.parseGrp();
        Assert.assertEquals(1.200f, sao.getGyroFreq(), .0001f);
        Assert.assertEquals(66.000f, sao.getDipAngle(), .0001f);
        Assert.assertEquals(41.900f, sao.getLat(), .0001f);
        Assert.assertEquals(12.500f, sao.getLon(), .0001f);
        Assert.assertEquals("80.000", sao.getSunspotNumber());
        Assert.assertEquals(0, Nerror);
    }

    /**
    * Test sunspot for parseGrp() for SAO.
    */
    @Test
    public void testParseSunspot() {
        String grp1data = "  0.000  0.000 40.000265.300                        ";
        sao.setGrpData(grp1data);
        int Nerror = sao.parseGrp();
        Assert.assertNull(sao.getSunspotNumber());
        Assert.assertEquals(1, Nerror);
    }

    /**
    * Test parseGrp() for SAO group 1 - gyrofrequency exceptions.
    */
    @Test
    public void testParseGrpGyro() {
        String grpdata = "  x.200 66.000 41.900 12.500 80.000                 ";
        sao.setGrpData(grpdata);
        int Nerror = sao.parseGrp();
        Assert.assertEquals(1, Nerror);
    }

    /**
    * Test parseGrp() for SAO group 1 - dip angle exceptions.
    */
    @Test
    public void testParseGrpDip() {
        String grpdata = "  1.200 96.000 41.900 12.500 80.000                 ";
        sao.setGrpData(grpdata);
        int Nerror = sao.parseGrp();
        Assert.assertEquals(1, Nerror);
    }

    /**
    * Test parseGrp() for SAO group 1 - lat exceptions.
    */
    @Test
    public void testParseGrpLat() {
        String grpdata = "  1.200 66.000-91.900 12.500 80.000                 ";
        sao.setGrpData(grpdata);
        int Nerror = sao.parseGrp();
        Assert.assertEquals(1, Nerror);
    }

    /**
    * Test parseGrp() for SAO group 1 - lon exceptions.
    */
    @Test
    public void testParseGrpLon() {
        String grpdata = "  1.200 66.000 41.900512.500 80.000                 ";
        sao.setGrpData(grpdata);
        int Nerror = sao.parseGrp();
        Assert.assertEquals(1, Nerror);
    }

    /**
    * Test parseGrp() for SAO group 1 - sunspot exceptions.
    */
    @Test
    public void testParseGrpSunspot() {
        String grpdata = "  1.200 66.000 41.900 12.500 x0.000                 ";
        sao.setGrpData(grpdata);
        int Nerror = sao.parseGrp();
        Assert.assertEquals(1, Nerror);
    }
}
