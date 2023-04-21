package org.fingerlab.donut.doMaker.util.parser;

import java.io.*;
import java.util.*;

public abstract class BaseParser {

    /**
     *Abstract method to be implemented 
     *by extending parsers.
     *@param the file containing the parse data.
     *@param the destination directory of the auto generated classes.
     */
    public abstract void parse(String configFile, String inputFile);

    /**
     *Utility for converting a package string
     *into a path string.
     @param the package name.
     @return the path name.
     *
     */
    protected String xPkg(String packageName) {
        StringTokenizer stk = new StringTokenizer(packageName, ".");
        String pkg = "";
        while (stk.hasMoreTokens()) pkg += "/" + stk.nextToken();
        pkg += "/";
        return pkg;
    }
}
