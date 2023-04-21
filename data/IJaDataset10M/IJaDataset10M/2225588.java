package org.mandiwala.selenium;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mandiwala.selenium.filter.Invocation;

/**
 * A utility class used to analyse a selenium html script and return it in a form that is easier to
 * work with.
 */
public final class SeleniumSourceAnalyser {

    private static final String FILTERED_INVOCATIONS_END_HERE = "<!-- FILTERED INVOCATIONS END HERE -->";

    private static final String FILTERED_INVOCATIONS_START_HERE = "<!-- FILTERED INVOCATIONS START HERE -->";

    /**
     * This class describes the contents of a selenium script. It consists of a:
     * <ul>
     * <li>{@code head} - a verbose copy of the part of the script before the list of commands</li>
     * <li>{@code tail} - a verbose copy of the part of the script after the list of commands</li>
     * <li>{@code invocations} - a list of selenium {@link Invocation}s (the commands)</li>
     * </ul>
     */
    public static class ExtractedSource {

        private List<Invocation> invocations;

        private StringBuffer head;

        private StringBuffer tail;

        /**
         * Instantiates a new {@link ExtractedSource}.
         */
        public ExtractedSource() {
            invocations = new ArrayList<Invocation>();
            head = new StringBuffer();
            tail = new StringBuffer();
        }

        /**
         * Gets the invocations.
         * 
         * @return the invocations
         */
        public List<Invocation> getInvocations() {
            return invocations;
        }

        /**
         * Sets the invocations.
         * 
         * @param invocations the new invocations
         */
        public void setInvocations(List<Invocation> invocations) {
            this.invocations = invocations;
        }

        /**
         * Gets the head.
         * 
         * @return the head
         */
        public StringBuffer getHead() {
            return head;
        }

        /**
         * Sets the head.
         * 
         * @param head the new head
         */
        public void setHead(StringBuffer head) {
            this.head = head;
        }

        /**
         * Gets the tail.
         * 
         * @return the tail
         */
        public StringBuffer getTail() {
            return tail;
        }

        /**
         * Sets the tail.
         * 
         * @param tail the new tail
         */
        public void setTail(StringBuffer tail) {
            this.tail = tail;
        }
    }

    private static final Log LOG = LogFactory.getLog("MANDIWALA");

    private static final Pattern HTML_COMMENT_PATTERN = Pattern.compile("<!--.*?-->", Pattern.DOTALL);

    private static final Pattern SELENESE_COMMAND_PATTERN = Pattern.compile("<tr(\\s+[^>]*)?>\\s*(<td(\\s+[^>]*)?>.*?</td\\s*>\\s*)</tr\\s*>", Pattern.DOTALL);

    private static final Pattern SELENESE_PARAMETER_PATTERN = Pattern.compile("<td(\\s+[^>]*)?>(.*?)</td\\s*>", Pattern.DOTALL);

    private static final String NEW_LINE = System.getProperty("line.separator");

    private static final String[] HEAD_ENDS_AT = new String[] { "<tbody>", "</thead>", "<table>" };

    private SeleniumSourceAnalyser() {
    }

    /**
     * Extracts selenium commands from the source script.
     * 
     * @param source Html source of the script.
     * 
     * @return An {@link ExtractedSource} instance containing the analysed script.
     */
    public static ExtractedSource analyse(String source) {
        ExtractedSource result = new ExtractedSource();
        source = dropHtmlComments(source);
        for (String token : HEAD_ENDS_AT) {
            int pos = source.indexOf(token);
            if (pos != -1) {
                result.head.append(source.substring(0, pos + token.length())).append(NEW_LINE);
                source = source.substring(pos + token.length());
                break;
            }
        }
        result.head.append(FILTERED_INVOCATIONS_START_HERE).append(NEW_LINE);
        Matcher seleniumCommandMatcher = SELENESE_COMMAND_PATTERN.matcher(source);
        int lastGroupEndPos = 0;
        while (seleniumCommandMatcher.find()) {
            lastGroupEndPos = seleniumCommandMatcher.end();
            Matcher seleniumParameterMatcher = SELENESE_PARAMETER_PATTERN.matcher(seleniumCommandMatcher.group());
            String[] cells = new String[3];
            for (int i = 0; seleniumParameterMatcher.find(); ++i) {
                if (i >= 3) {
                    LOG.warn("Command has too many parameters: ");
                    LOG.warn(seleniumCommandMatcher.group());
                    break;
                }
                cells[i] = seleniumParameterMatcher.group(2);
            }
            result.invocations.add(new Invocation(true, cells[0], new String[] { cells[1], cells[2] }));
        }
        result.tail.append(FILTERED_INVOCATIONS_END_HERE).append(NEW_LINE);
        result.tail.append(source.substring(lastGroupEndPos));
        return result;
    }

    private static String dropHtmlComments(String htmlContents) {
        Matcher m = HTML_COMMENT_PATTERN.matcher(htmlContents);
        while (m.find()) {
            htmlContents = htmlContents.replace(m.group(), "");
        }
        return htmlContents;
    }
}
