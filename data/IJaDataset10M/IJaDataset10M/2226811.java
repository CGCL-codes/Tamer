package sto.orz.commons;

import org.apache.commons.lang.StringUtils;

public class DotStringUtils {

    public static String addRoot(String dotString, String root) {
        if (StringUtils.isBlank(root)) return dotString;
        return root + "." + dotString;
    }
}
