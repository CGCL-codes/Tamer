package org.ft4j.test;

import java.util.Random;
import org.ft4j.in.*;
import junit.framework.TestCase;

public class SlotCacheTest extends TestCase {

    public void testCache() {
        SlotCache c = new SlotCache(3);
        assertEquals("hitrate:0.0,index:2,keys:[null, null, null],map:{}", c.toString());
        assertEquals(null, c.resolve("a"));
        assertEquals("hitrate:0.0,index:2,keys:[null, null, null],map:{}", c.toString());
        c.add("a", "AA");
        assertEquals("hitrate:0.0,index:1,keys:[null, null, a~2~AA],map:{a=a~2~AA}", c.toString());
        assertEquals("AA", c.resolve("a"));
        assertEquals("hitrate:0.5,index:1,keys:[null, null, a~2~AA],map:{a=a~2~AA}", c.toString());
        assertEquals(null, c.resolve("b"));
        assertEquals("hitrate:0.33333,index:1,keys:[null, null, a~2~AA],map:{a=a~2~AA}", c.toString());
        c.add("b", "BB");
        assertEquals("hitrate:0.33333,index:0,keys:[null, b~1~BB, a~2~AA],map:{a=a~2~AA, b=b~1~BB}", c.toString());
        assertEquals("AA", c.resolve("a"));
        assertEquals("hitrate:0.5,index:0,keys:[null, b~1~BB, a~2~AA],map:{a=a~2~AA, b=b~1~BB}", c.toString());
        assertEquals("BB", c.resolve("b"));
        assertEquals("hitrate:0.6,index:0,keys:[null, a~1~AA, b~2~BB],map:{a=a~1~AA, b=b~2~BB}", c.toString());
        assertEquals(null, c.resolve("c"));
        assertEquals("hitrate:0.5,index:0,keys:[null, a~1~AA, b~2~BB],map:{a=a~1~AA, b=b~2~BB}", c.toString());
        c.add("c", "CC");
        assertEquals("hitrate:0.5,index:0,keys:[c~0~CC, a~1~AA, b~2~BB],map:{a=a~1~AA, c=c~0~CC, b=b~2~BB}", c.toString());
        assertEquals("AA", c.resolve("a"));
        assertEquals("hitrate:0.57143,index:0,keys:[c~0~CC, b~1~BB, a~2~AA],map:{a=a~2~AA, c=c~0~CC, b=b~1~BB}", c.toString());
        assertEquals("BB", c.resolve("b"));
        assertEquals("hitrate:0.625,index:0,keys:[c~0~CC, a~1~AA, b~2~BB],map:{a=a~1~AA, c=c~0~CC, b=b~2~BB}", c.toString());
        assertEquals("CC", c.resolve("c"));
        assertEquals("hitrate:0.66667,index:0,keys:[a~0~AA, c~1~CC, b~2~BB],map:{a=a~0~AA, c=c~1~CC, b=b~2~BB}", c.toString());
        assertEquals(null, c.resolve("d"));
        assertEquals("hitrate:0.6,index:0,keys:[a~0~AA, c~1~CC, b~2~BB],map:{a=a~0~AA, c=c~1~CC, b=b~2~BB}", c.toString());
        c.add("d", "DD");
        assertEquals("hitrate:0.6,index:0,keys:[d~0~DD, c~1~CC, b~2~BB],map:{d=d~0~DD, c=c~1~CC, b=b~2~BB}", c.toString());
        assertEquals(null, c.resolve("a"));
        assertEquals("hitrate:0.54545,index:0,keys:[d~0~DD, c~1~CC, b~2~BB],map:{d=d~0~DD, c=c~1~CC, b=b~2~BB}", c.toString());
        assertEquals("DD", c.resolve("d"));
        assertEquals("hitrate:0.58333,index:0,keys:[c~0~CC, d~1~DD, b~2~BB],map:{d=d~1~DD, c=c~0~CC, b=b~2~BB}", c.toString());
        assertEquals("DD", c.resolve("d"));
        assertEquals("hitrate:0.61538,index:0,keys:[c~0~CC, b~1~BB, d~2~DD],map:{d=d~2~DD, c=c~0~CC, b=b~1~BB}", c.toString());
        assertEquals("DD", c.resolve("d"));
        assertEquals("hitrate:0.64286,index:0,keys:[c~0~CC, b~1~BB, d~2~DD],map:{d=d~2~DD, c=c~0~CC, b=b~1~BB}", c.toString());
        assertEquals("DD", c.resolve("d"));
        assertEquals("hitrate:0.66667,index:0,keys:[c~0~CC, b~1~BB, d~2~DD],map:{d=d~2~DD, c=c~0~CC, b=b~1~BB}", c.toString());
    }

    public static void main(String[] args) {
        hitrate();
    }

    public static void hitrate() {
        double[] points = longtail(0.4, 0.6, 20);
        SlotCache c = new SlotCache(4);
        for (int pI = 0; pI < points.length; pI++) {
            System.out.println(pI + ":" + points[pI]);
        }
        int[] counts = new int[points.length];
        Random r = new Random();
        for (int rI = 0; rI < 10000; rI++) {
            double next = r.nextDouble();
            for (int pI = 0; pI < points.length; pI++) {
                if (next < points[pI]) {
                    counts[pI]++;
                    Object res = c.resolve("" + pI);
                    if (null == res) {
                        c.add("" + pI, "" + pI);
                    }
                    break;
                }
            }
        }
        for (int cI = 0; cI < counts.length; cI++) {
            System.out.println(cI + ":" + counts[cI]);
        }
        System.out.println(c);
    }

    public static double[] longtail(double pInit, double pFactor, int pSize) {
        double next = pInit;
        double[] points = new double[pSize];
        points[0] = next;
        for (int pI = 1; pI < pSize - 1; pI++) {
            next = (next * pFactor);
            points[pI] = points[pI - 1] + next;
        }
        points[pSize - 1] = 1.0;
        return points;
    }
}
