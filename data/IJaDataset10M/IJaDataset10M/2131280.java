package org.gps.types;

import henson.midp.Float;
import j2meunit.framework.*;

public class GpsPositionTest extends TestCase {

    public GpsPositionTest() {
    }

    public GpsPositionTest(String testName, TestMethod testMethod) {
        super(testName, testMethod);
    }

    /**
     * Test of getAltitude method, of class org.gps.types.GpsPosition.
     */
    public void testgetAltitude() {
        System.out.println("getAltitude");
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        int expectedResult = 0;
        int result = instance.getAltitude();
        assertEquals(expectedResult, result);
    }

    /**
     * Test of setAltitude method, of class org.gps.types.GpsPosition.
     */
    public void testsetAltitude() {
        System.out.println("setAltitude");
        int altitude = 0;
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        instance.setAltitude(altitude);
        assertEquals(altitude, instance.getAltitude());
    }

    /**
     * Test of getVelocity method, of class org.gps.types.GpsPosition.
     */
    public void testgetVelocity() {
        System.out.println("getVelocity");
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        henson.midp.Float expectedResult = Float.ZERO;
        henson.midp.Float result = instance.getVelocity();
        assertEquals(expectedResult, result);
    }

    /**
     * Test of setVelocity method, of class org.gps.types.GpsPosition.
     */
    public void testsetVelocity() {
        System.out.println("setVelocity");
        henson.midp.Float velocity = Float.ONE;
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        instance.setVelocity(velocity);
        assertEquals(instance.getVelocity(), Float.ONE);
    }

    /**
     * Test of getQuality method, of class org.gps.types.GpsPosition.
     */
    public void testgetQuality() {
        System.out.println("getQuality");
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        int expectedResult = 0;
        int result = instance.getQuality();
        assertEquals(expectedResult, result);
    }

    /**
     * Test of setQuality method, of class org.gps.types.GpsPosition.
     */
    public void testsetQuality() {
        System.out.println("setQuality");
        int quality = 0;
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        instance.setQuality(quality);
        assertEquals(instance.getQuality(), quality);
    }

    /**
     * Test of getHorizontalDilution method, of class org.gps.types.GpsPosition.
     */
    public void testgetHorizontalDilution() {
        System.out.println("getHorizontalDilution");
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        int expectedResult = 0;
        int result = instance.getHorizontalDilution();
        assertEquals(expectedResult, result);
    }

    /**
     * Test of setHorizontalDilution method, of class org.gps.types.GpsPosition.
     */
    public void testsetHorizontalDilution() {
        System.out.println("setHorizontalDilution");
        int horizontalDilution = 0;
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        instance.setHorizontalDilution(horizontalDilution);
        assertEquals(instance.getHorizontalDilution(), horizontalDilution);
    }

    /**
     * Test of getLatitude method, of class org.gps.types.GpsPosition.
     */
    public void testgetLatitude() {
        System.out.println("getLatitude");
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        henson.midp.Float expectedResult = Float.ZERO;
        henson.midp.Float result = instance.getLatitude();
        assertEquals(expectedResult, result);
    }

    /**
     * Test of setLatitude method, of class org.gps.types.GpsPosition.
     */
    public void testsetLatitude() {
        System.out.println("setLatitude");
        henson.midp.Float latitude = Float.ONE;
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        instance.setLatitude(latitude);
        assertEquals(instance.getLatitude(), Float.ONE);
    }

    /**
     * Test of getLongitude method, of class org.gps.types.GpsPosition.
     */
    public void testgetLongitude() {
        System.out.println("getLongitude");
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        henson.midp.Float expectedResult = Float.ZERO;
        henson.midp.Float result = instance.getLongitude();
        assertEquals(expectedResult, result);
    }

    /**
     * Test of setLongitude method, of class org.gps.types.GpsPosition.
     */
    public void testsetLongitude() {
        System.out.println("setLongitude");
        henson.midp.Float longitude = Float.ONE;
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        instance.setLongitude(longitude);
        assertEquals(Float.ONE, instance.getLongitude());
    }

    /**
     * Test of getTimestamp method, of class org.gps.types.GpsPosition.
     */
    public void testgetTimestamp() {
        System.out.println("getTimestamp");
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        int expectedResult = 0;
        int result = instance.getTimestamp();
        assertEquals(expectedResult, result);
    }

    /**
     * Test of setTimestamp method, of class org.gps.types.GpsPosition.
     */
    public void testsetTimestamp() {
        System.out.println("setTimestamp");
        int timestamp = 0;
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        instance.setTimestamp(timestamp);
        assertEquals(timestamp, instance.getTimestamp());
    }

    /**
     * Test of getCourse method, of class org.gps.types.GpsPosition.
     */
    public void testgetCourse() {
        System.out.println("getCourse");
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        instance.setCourse(Float.ONE);
        henson.midp.Float expectedResult = Float.ONE;
        henson.midp.Float result = instance.getCourse();
        assertEquals(expectedResult, result);
    }

    /**
     * Test of setCourse method, of class org.gps.types.GpsPosition.
     */
    public void testsetCourse() {
        System.out.println("setCourse");
        henson.midp.Float course = Float.ONE;
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        instance.setCourse(course);
        assertEquals(course, instance.getCourse());
    }

    /**
     * Test of distance method, of class org.gps.types.GpsPosition.
     */
    public void testdistance() {
        System.out.println("distance");
        org.gps.types.GpsPosition pos = new org.gps.types.GpsPosition();
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        int expectedResult = 0;
        int result = instance.distance(pos);
        assertEquals(expectedResult, result);
        pos.setLatitude(Float.ONE);
        pos.setLongitude(Float.ZERO);
        expectedResult = 111120;
        result = instance.distance(pos);
        assertEquals(expectedResult, result);
        pos.setLatitude(Float.ZERO);
        pos.setLongitude(Float.ONE);
        expectedResult = 111120;
        result = instance.distance(pos);
        assertEquals(expectedResult, result);
    }

    public void setUp() {
    }

    public void tearDown() {
    }

    /**
     * Test of course method, of class org.gps.types.GpsPosition.
     */
    public void testcourse() {
        System.out.println("course");
        org.gps.types.GpsPosition pos = new org.gps.types.GpsPosition();
        org.gps.types.GpsPosition instance = new org.gps.types.GpsPosition();
        int expectedResult = -1;
        int result = instance.course(pos);
        assertEquals(expectedResult, result);
        pos.setLatitude(Float.ONE);
        pos.setLongitude(Float.ZERO);
        expectedResult = 0;
        result = instance.course(pos);
        assertEquals(expectedResult, result);
        pos.setLatitude(Float.ONE);
        pos.setLongitude(Float.ONE);
        expectedResult = 44;
        result = instance.course(pos);
        assertEquals(expectedResult, result);
        pos.setLatitude(Float.ZERO);
        pos.setLongitude(Float.ONE);
        expectedResult = 90;
        result = instance.course(pos);
        assertEquals(expectedResult, result);
        pos.setLatitude(new Float(-1L));
        pos.setLongitude(Float.ONE);
        expectedResult = 136;
        result = instance.course(pos);
        assertEquals(expectedResult, result);
        pos.setLatitude(new Float(-1L));
        pos.setLongitude(Float.ZERO);
        expectedResult = 180;
        result = instance.course(pos);
        assertEquals(expectedResult, result);
        pos.setLatitude(new Float(-1L));
        pos.setLongitude(new Float(-1L));
        expectedResult = 224;
        result = instance.course(pos);
        assertEquals(expectedResult, result);
        pos.setLatitude(Float.ZERO);
        pos.setLongitude(new Float(-1L));
        expectedResult = 270;
        result = instance.course(pos);
        assertEquals(expectedResult, result);
        pos.setLatitude(Float.ONE);
        pos.setLongitude(new Float(-1L));
        expectedResult = 316;
        result = instance.course(pos);
        assertEquals(expectedResult, result);
    }

    public Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new GpsPositionTest("testgetAltitude", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testgetAltitude();
            }
        }));
        suite.addTest(new GpsPositionTest("testsetAltitude", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testsetAltitude();
            }
        }));
        suite.addTest(new GpsPositionTest("testgetVelocity", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testgetVelocity();
            }
        }));
        suite.addTest(new GpsPositionTest("testsetVelocity", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testsetVelocity();
            }
        }));
        suite.addTest(new GpsPositionTest("testgetQuality", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testgetQuality();
            }
        }));
        suite.addTest(new GpsPositionTest("testsetQuality", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testsetQuality();
            }
        }));
        suite.addTest(new GpsPositionTest("testgetHorizontalDilution", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testgetHorizontalDilution();
            }
        }));
        suite.addTest(new GpsPositionTest("testsetHorizontalDilution", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testsetHorizontalDilution();
            }
        }));
        suite.addTest(new GpsPositionTest("testgetLatitude", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testgetLatitude();
            }
        }));
        suite.addTest(new GpsPositionTest("testsetLatitude", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testsetLatitude();
            }
        }));
        suite.addTest(new GpsPositionTest("testgetLongitude", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testgetLongitude();
            }
        }));
        suite.addTest(new GpsPositionTest("testsetLongitude", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testsetLongitude();
            }
        }));
        suite.addTest(new GpsPositionTest("testgetTimestamp", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testgetTimestamp();
            }
        }));
        suite.addTest(new GpsPositionTest("testsetTimestamp", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testsetTimestamp();
            }
        }));
        suite.addTest(new GpsPositionTest("testgetCourse", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testgetCourse();
            }
        }));
        suite.addTest(new GpsPositionTest("testsetCourse", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testsetCourse();
            }
        }));
        suite.addTest(new GpsPositionTest("testdistance", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testdistance();
            }
        }));
        suite.addTest(new GpsPositionTest("testcourse", new TestMethod() {

            public void run(TestCase tc) {
                ((GpsPositionTest) tc).testcourse();
            }
        }));
        return suite;
    }
}
