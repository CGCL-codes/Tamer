package com.manning.junitbook.ch07.mocks.configurations;

/**
 * We add the configuration interface as part of the refactoring process.
 * 
 * @version $Id: Configuration.java 503 2009-08-16 17:47:12Z paranoid12 $
 */
public interface Configuration {

    /**
     * Getter method to get the SQL query to execute.
     * 
     * @return
     */
    String getSQL(String sqlString);
}
