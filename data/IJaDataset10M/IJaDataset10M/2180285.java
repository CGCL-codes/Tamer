package org.jf.lib.jdbchelper.dbobjects.helpertest;

import org.jf.lib.jdbchelper.dbobjects.annotation.DBSourceConstructor;

/**
 * <p>This class is an example of an incorrect DBHelper object.  The library doesn't handle variable parameter constructors.</p>
 * 
 * @author Jim Foster <jdfcm73uk@gmail.com>
 */
public class ResultIllegalTestObject1 {

    private String theString;

    private int[] theInts;

    /**
   * <p>Disallowed constructor signature - no varargs allowed.</p>
   * 
   * @param theString
   * @param theInts 
   */
    @DBSourceConstructor
    public ResultIllegalTestObject1(String theString, int... theInts) {
        super();
        this.theString = theString;
        ;
        this.theInts = theInts;
    }

    public String getTheString() {
        return theString;
    }

    public int[] getTheInts() {
        return theInts;
    }
}
