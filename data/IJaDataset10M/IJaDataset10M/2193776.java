package net.maizegenetics.data;

import pal.util.Logger;
import java.util.*;

/**
 * Title:        TASSEL
 * Description:  A java program to deal with diversity
 * Copyright:    Copyright (c) 2000
 * Company:      USDA-ARS/NCSU
 * @author Ed Buckler
 * @version 1.0
 */
public class BasicLogger implements Logger {

    Vector debugVector = new Vector();

    Vector logVector = new Vector();

    public BasicLogger() {
    }

    public void debug(Object parm1) {
        System.out.println(parm1.toString());
    }

    public boolean isDebugging() {
        return true;
    }

    public void log(Object parm1) {
        logVector.add(parm1);
    }
}
