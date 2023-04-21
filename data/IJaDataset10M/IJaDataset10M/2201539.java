package org.archive.crawler.url.canonicalize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StripExtraSlashes extends BaseRule {

    private static final String DESCRIPTION = "Strip any extra slashes, '/', found in the path. " + "Use this rule to equate 'http://www.archive.org//A//B/index.html' and " + "'http://www.archive.org/A/B/index.html'.";

    private static final Pattern REGEX = Pattern.compile("(^https?://.*?)//+(.*)");

    public StripExtraSlashes(String name) {
        super(name, DESCRIPTION);
    }

    public String canonicalize(String url, Object context) {
        Matcher matcher = REGEX.matcher(url);
        while (matcher.matches()) {
            url = matcher.group(1) + "/" + matcher.group(2);
            matcher = REGEX.matcher(url);
        }
        return url;
    }
}
