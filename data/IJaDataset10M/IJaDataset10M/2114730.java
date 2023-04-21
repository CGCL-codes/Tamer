package edu.fit.it.blue;

import java.util.*;

/**
 * Class for holding any simple type from the config file.
 * eg "include ./schema/core.schema", "directory ./openldap-data", etc.
 */
public class OLConfFileSimpleType {

    /**
   * Keeps the original line in case we could use it later.
   */
    public String originalLine;

    /**
   * The rest of the string after the valid syntax has been parsed.
   */
    public String rest;

    /**
   * The parameter's name.
   */
    public String name;

    /**
   * The parameter's value(s).
   */
    public Vector values;

    /**
   * Set to false in case of syntax errors.
   */
    public boolean valid;

    /** 
   * Constructor for the OLConfFileSimpleType class
   * 
   * @param line The by clause for processing
   */
    public OLConfFileSimpleType(String line) throws OLConfFileParserException {
        originalLine = line;
        valid = false;
        name = null;
        values = new Vector();
        line = line.trim();
        StringTokenizer tok = new StringTokenizer(line, " \t");
        String currentToken;
        if (tok.hasMoreTokens()) {
            try {
                name = tok.nextToken();
                if (tok.hasMoreTokens() == false) throw new OLConfFileParserException("'" + name + "' has no values.");
                while (tok.hasMoreTokens()) values.add(tok.nextToken());
            } catch (NoSuchElementException e) {
                throw new OLConfFileParserException("premature end of '" + name + "' clause.");
            }
            try {
                if (tok.hasMoreTokens()) rest = tok.nextToken("\n");
            } catch (NoSuchElementException e) {
                ;
            }
            valid = true;
        }
    }

    /**
   * Print the simple clause.
   */
    public String toString() {
        String ret;
        if (valid == false) return " Invalid configuration statement.\n";
        ret = "\nname     == '" + name + "'\n" + "values   == ";
        for (int i = 0; i < values.size(); i++) ret += "'" + values.get(i).toString() + "' ";
        ret += "\noriginal line == '" + originalLine + "'\n" + "rest          == '" + rest + "'\n";
        return ret;
    }
}
