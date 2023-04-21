package de.excrawler.server.Regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Static Regex for getting URLs out of content
 * @author Yves Hoppe <info at yves-hoppe.de>
 * @author Karpouzas George <www.webnetsoft.gr>
 */
public class UrlRegex extends Thread {

    /**
     * Static final regex vars for urls
     */
    public static final String URL_REGEX = "([A-Za-z][A-Za-z0-9+.-]{1,120}:" + "[A-Za-z0-9/](([A-Za-z0-9$_.+!*,;/?:@&~=-])|%[A-Fa-f0-9]{2}){1,333}" + "(#([a-zA-Z0-9][a-zA-Z0-9$_.+!*,;/?:@&~=%-]{0,1000}))?)";

    /**
     * Static method to extract a list with links (not used in crawler)
     * @param content
     * @return urls
     */
    public static List<String> extractLinks(String content) {
        List<String> urls = new ArrayList<String>();
        Pattern pat = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE + Pattern.MULTILINE);
        Matcher matcher = pat.matcher(content);
        while (matcher.find()) {
            urls.add(matcher.group());
        }
        return urls;
    }
}
