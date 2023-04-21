package com.jstudio.util;

import com.jstudio.logging.Logger;

/**
 * User: jacky
 * Date: Apr 16, 2002
 * Time: 9:33:18 PM
 * @author Jacky Chen
 * @version 0.1
 */
public class Console {

    static Logger logger = Logger.newLogger(Console.class);

    public static void print(Object o) {
        if (logger.isDebugEnabled()) {
            if (o == null) {
                logger.debug(null);
            } else {
                logger.debug(o);
            }
        }
    }

    public static void print(Object[] o) {
        if (logger.isDebugEnabled()) {
            int length = o.length;
            for (int i = 0; i < length; i++) {
                logger.debug(o[i]);
            }
        }
    }
}
