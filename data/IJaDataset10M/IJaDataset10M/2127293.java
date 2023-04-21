package com.kwoksys.framework.util;

import junit.framework.TestCase;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * StringUtilTest
 */
public class StringUtilTest extends TestCase {

    public static void main(String args[]) {
        String httpRegex = "(?<!\")((http|https)://[^\\s]+)(?!\")";
        String emailRegex = "[a-z0-9\\-_\\.]++@[a-z0-9\\-]++(\\.[a-z0-9\\-]++)++";
        String string = "URL conversion test" + "Lowercase: http://example.com \n" + "Lowercase with punctuation: http://example.com. \n" + "URL parameters: http://example.com?a=1 \n" + "URL parameters: http://example.com?a=1, \n" + "Already a link: <a href=\"http://example.com\">example</a> \n" + "Upper case https: Https://example.com \n";
        Pattern p = Pattern.compile(httpRegex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        if (m.find()) {
            string = m.replaceAll("<a href=\"$1\">$1</a>");
        }
        System.out.println(string);
    }
}
