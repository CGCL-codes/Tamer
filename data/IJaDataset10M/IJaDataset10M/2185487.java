package com.google.gwt.user.tools.util;

import com.google.gwt.util.tools.ArgHandlerString;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Parse the -addToClassPath argument. Appends a .jar or classpath to the
 * generated launch scripts.
 */
public class ArgHandlerAddToClassPath extends ArgHandlerString {

    private List<String> extraClassPathList = new ArrayList<String>();

    public List<String> getExtraClassPathList() {
        return extraClassPathList;
    }

    @Override
    public String getPurpose() {
        return "Adds extra elements to the class path.";
    }

    @Override
    public String getTag() {
        return "-addToClassPath";
    }

    @Override
    public String[] getTagArgs() {
        return new String[] { "classPathEntry" };
    }

    @Override
    public boolean setString(String str) {
        StringTokenizer st = new StringTokenizer(str, ",");
        while (st.hasMoreTokens()) {
            extraClassPathList.add(st.nextToken().trim());
        }
        return true;
    }
}
