package com.tsadom.mysql;

/**
 * Use this class to extract MySQL QUERY:
 * SHOW VARIABLES; 
 * 
 * @author Uriel CHEMOUNI
 */
public class MySQLVar {

    public static final MySQLVar[] NO_ELM = new MySQLVar[0];

    public String Variable_name;

    public String Value;

    public String toString() {
        return Variable_name + " = " + Value;
    }
}
