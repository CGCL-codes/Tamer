package com.pcmsolutions.system;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 14-Sep-2003
 * Time: 01:46:58
 * To change this template use Options | File Templates.
 */
public class TipFieldFormatter {

    private StringBuffer s;

    private boolean usingLightInfix = true;

    private static final String PREFIX = "[ ";

    private static final String INFIX = " ][ ";

    private static final String LIGHT_INFIX = " ] ";

    private static final String POSTFIX = " ]";

    public TipFieldFormatter() {
    }

    public TipFieldFormatter(boolean usingLightInfix) {
        this.usingLightInfix = usingLightInfix;
    }

    public void add(String f) {
        if (s == null) s = new StringBuffer(PREFIX + f); else if (usingLightInfix) s.append(LIGHT_INFIX + f); else s.append(INFIX + f);
    }

    public String toString() {
        return s.toString() + POSTFIX;
    }

    public void reset() {
        s = new StringBuffer();
    }
}
