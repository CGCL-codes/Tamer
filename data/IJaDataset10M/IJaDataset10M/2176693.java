package com.asoft.common.util.random;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import org.apache.log4j.Logger;

/**
 * 随机取得数组 
 */
public class RandomArray {

    static Logger logger = Logger.getLogger(RandomArray.class);

    /**
         * int 的数组 
         */
    public static int[] parse(int v) {
        if (v <= 0) {
            throw new UnsupportedOperationException("必须大于0");
        }
        Random r = new Random();
        int[] ra = new int[v];
        for (int i = 0; i < v; i++) {
            ra[i] = i;
            logger.debug(ra[i]);
        }
        for (int i = 0; i < v; i++) {
            int di = r.nextInt(v);
            int sv = ra[i];
            int dv = ra[di];
            ra[i] = dv;
            ra[di] = sv;
            ShowIntArray(ra);
        }
        return ra;
    }

    private static void ShowIntArray(int[] ia) {
        logger.debug("***************");
        for (int i = 0; i < ia.length; i++) {
            logger.debug("ra[" + i + "]= " + ia[i]);
        }
        logger.debug("***************");
    }

    private static boolean contains(int[] ra, int v) {
        for (int i : ra) {
            if (i == v) {
                return true;
            }
        }
        return false;
    }
}
