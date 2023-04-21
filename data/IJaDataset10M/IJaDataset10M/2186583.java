package com.qasystems.qstudio.configuration;

import java.io.PrintWriter;

/**
 * This class represents the QCF notion IntegerLiteral.
 *
 */
public class IntegerLiteral extends NumericLiteral {

    public long NUM = 0;

    /**
   * Default constructor.
   */
    public IntegerLiteral() {
        super();
    }

    /**
   * Default constructor.
   *
   * @param value the numeric value
   */
    public IntegerLiteral(long value) {
        this();
        setNum(value);
    }

    /**
   * Print this integer literal.
   *
   * @param out the output writer.
   */
    public void print(PrintWriter out) {
        out.print(NUM);
    }

    /**
   * Gets this object's value.
   *
   * @return the numeric value
   */
    public long getIntValue() {
        return (NUM);
    }

    private synchronized void setNum(long value) {
        NUM = value;
    }
}
