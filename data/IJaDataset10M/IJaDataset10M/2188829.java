package org.jdu.dao;

/**
 * Rappresentazione di una Sequence oracle
 * @author epelli
 *
 */
public class OracleSequence implements Sequence {

    private String name;

    public OracleSequence(String name) {
        this.name = name;
    }

    public String getQuery() {
        return "select " + name + ".nextval from dual";
    }
}
