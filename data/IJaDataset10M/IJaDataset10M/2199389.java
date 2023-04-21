package org.mandiwala.selenium.junit.errorpagenotifier;

import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.mandiwala.PrefixedPropertyReader;
import org.mandiwala.selenium.SeleniumConfiguration;

/**
 * A simple implementation of {@link ErrorPageNotifier}. Use the keys
 * {@link #CONTENT_ERROR_STRING} and {@link #TITLE_ERROR_STRING} to set up an
 * instance of this class. The values specified in configuration are treated as
 * regular expressions. If any of them occurs on the tested page, it is
 * considered to be an error page.
 */
public class SimpleErrorPageNotifier implements ErrorPageNotifier {

    /**
     * Regular expression that the page content will be tested against.
     */
    private static final String CONTENT_ERROR_STRING = "contentErrorString";

    /**
     * Regular expression that the page title will be tested against.
     */
    private static final String TITLE_ERROR_STRING = "titleErrorString";

    private SeleniumConfiguration seleniumConfiguration;

    private Pattern contentErrorPattern;

    private Pattern titleErrorPattern;

    /**
     * {@inheritDoc}
     */
    public boolean isErrorPage(String content) {
        if (content == null) {
            return false;
        }
        return isErrorTitle(content) || isErrorMessage(content);
    }

    private boolean isErrorMessage(String content) {
        if (contentErrorPattern == null) {
            return false;
        }
        return contentErrorPattern.matcher(content).find();
    }

    private boolean isErrorTitle(String content) {
        if (titleErrorPattern == null) {
            return false;
        }
        Locale browserLocale = seleniumConfiguration.getBrowserConfiguration().getBrowserLocale();
        String uppercaseContent = content.toUpperCase(browserLocale);
        int start = uppercaseContent.indexOf("<TITLE>");
        int end = uppercaseContent.indexOf("</TITLE>");
        if (start == -1) {
            return false;
        }
        return titleErrorPattern.matcher(content.substring(start + 7, end)).find();
    }

    /**
     * {@inheritDoc}
     */
    public void init(SeleniumConfiguration seleniumConfiguration) {
        this.seleniumConfiguration = seleniumConfiguration;
        PrefixedPropertyReader prefixedPropertyReader = new PrefixedPropertyReader(getClass().getName(), seleniumConfiguration.getProps());
        String contentErrorString = prefixedPropertyReader.getPrefixedProperty(CONTENT_ERROR_STRING, false);
        if (StringUtils.isNotBlank(contentErrorString)) {
            contentErrorPattern = Pattern.compile(contentErrorString);
        }
        String titleErrorString = prefixedPropertyReader.getPrefixedProperty(TITLE_ERROR_STRING, false);
        if (StringUtils.isNotBlank(titleErrorString)) {
            titleErrorPattern = Pattern.compile(titleErrorString);
        }
    }
}
