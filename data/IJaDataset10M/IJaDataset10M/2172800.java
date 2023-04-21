package com.mangobop.query.qom;

/**
 * @author Stefan Meyer
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface Operator {

    public int getType();

    public static final int EQ = 0;

    public static final int NE = 1;

    public static final int GT = 2;

    public static final int LT = 3;

    public static final int GE = 4;

    public static final int LE = 5;

    public static final int PLUS = 6;

    public static final int MINUS = 7;

    public static final int MULTIPLICATION = 8;

    public static final int DIVISION = 9;
}
