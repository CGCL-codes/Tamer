package com.example.simple;

import java.util.Collection;
import java.util.Iterator;
import org.apache.log4j.Logger;

public class Simple {

    private static final Logger logger = Logger.getLogger(Simple.class);

    public int square(int x) {
        if (logger.isDebugEnabled()) {
            logger.debug("x: " + x);
        }
        int result = x * x;
        if (logger.isDebugEnabled()) {
            logger.debug("result: " + result);
        }
        return result;
    }

    public int f(int x) {
        if (logger.isDebugEnabled()) {
            logger.debug("x: " + x);
        }
        if (x < 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("negative x");
            }
            return square(x);
        } else if ((x >= 0) && (x <= 5)) {
            if (logger.isDebugEnabled()) {
                logger.debug("0<=x<=5");
            }
            return x + 3;
        } else {
            return 2 * x;
        }
    }

    public int sum(Collection c) {
        int result = 0;
        for (Iterator i = c.iterator(); i.hasNext(); ) {
            int value = ((Number) i.next()).intValue();
            if (logger.isDebugEnabled()) {
                logger.debug("value: " + value);
            }
            result += value;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("result: " + result);
        }
        return result;
    }
}
