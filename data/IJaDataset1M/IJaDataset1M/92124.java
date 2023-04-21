package com.hp.hpl.jena.db.impl;

/**
 * Override the buggy one in Jena. 
 * 
 * @author timp
 * @since 18 Jun 2009
 *
 */
public class Driver_HSQLDB_GABOTO extends com.hp.hpl.jena.db.impl.Driver_HSQL {

    public Driver_HSQLDB_GABOTO() {
        super();
        DATABASE_TYPE = "HSQLDB_GABOTO";
    }
}
