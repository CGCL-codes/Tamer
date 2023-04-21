package org.parosproxy.paros.core.spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ParserAttr {

    Pattern pattern = null;

    ParserAttr(String attr) {
        String attrPattern = "\\s*?" + attr.toUpperCase() + "\\s*?=\\s*([\"']{0,1})([^\"']*?)\\1(\\Z|\\s+)";
        pattern = Pattern.compile(attrPattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    }

    String getValue(String doc) {
        String result = "";
        Matcher matcher = pattern.matcher(doc);
        if (matcher.find()) {
            result = matcher.group(2);
        }
        return result.trim();
    }
}
