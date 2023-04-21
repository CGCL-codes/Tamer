package main;

import log.LogTestSuite;
import util.UtilTestSuite;
import dao.DaoTestSuite;

/**
 * The main test suite of HQ server
 * @author HUNGPT
 *
 */
public class HQTestRunner {

    public static void main(String[] args) {
        System.out.println("Run HQ test suite");
        String argss[] = new String[] {};
        DaoTestSuite.main(argss);
        UtilTestSuite.main(argss);
        LogTestSuite.main(argss);
        System.out.println("End HQ test suite");
    }
}
