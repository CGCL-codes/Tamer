package com.vayoodoot.research;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Sachin Shetty
 * Date: Dec 27, 2006
 * Time: 8:28:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoggerTest {

    private static Logger logger = Logger.getLogger(LoggerTest.class);

    public static void main(String args[]) {
        logger.info("I am here");
    }
}
