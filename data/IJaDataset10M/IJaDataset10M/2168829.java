package com.ecyrd.jspwiki.diff;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ecyrd.jspwiki.*;

/**
 * This DiffProvider allows external command line tools to be used to generate
 * the diff.
 */
public class ExternalDiffProvider implements DiffProvider {

    private static final Logger log = Logger.getLogger(ExternalDiffProvider.class.getName());

    /**
     * Determines the command to be used for 'diff'. This program must be able
     * to output diffs in the unified format. For example 'diff -u %s1 %s2'.
     */
    public static final String PROP_DIFFCOMMAND = "jspwiki.diffCommand";

    private String m_diffCommand = null;

    private String m_encoding;

    private static final char DIFF_ADDED_SYMBOL = '+';

    private static final char DIFF_REMOVED_SYMBOL = '-';

    private static final String CSS_DIFF_ADDED = "<tr><td bgcolor=\"#99FF99\" class=\"diffadd\">";

    private static final String CSS_DIFF_REMOVED = "<tr><td bgcolor=\"#FF9933\" class=\"diffrem\">";

    private static final String CSS_DIFF_UNCHANGED = "<tr><td class=\"diff\">";

    private static final String CSS_DIFF_CLOSE = "</td></tr>";

    private boolean m_traditionalColorization = true;

    /**
     *  Creates a new ExternalDiffProvider.
     */
    public ExternalDiffProvider() {
    }

    /**
     * @see com.ecyrd.jspwiki.WikiProvider#getProviderInfo()
     * {@inheritDoc}
     */
    public String getProviderInfo() {
        return "ExternalDiffProvider";
    }

    /**
     * {@inheritDoc}
     * @see com.ecyrd.jspwiki.WikiProvider#initialize(com.ecyrd.jspwiki.WikiEngine, java.util.Properties)
     */
    public void initialize(WikiEngine engine, Properties properties) throws NoRequiredPropertyException, IOException {
        m_diffCommand = properties.getProperty(PROP_DIFFCOMMAND);
        if ((null == m_diffCommand) || (m_diffCommand.trim().equals(""))) {
            throw new NoRequiredPropertyException("ExternalDiffProvider missing required property", PROP_DIFFCOMMAND);
        }
        m_encoding = engine.getContentEncoding();
    }

    /**
     * Makes the diff by calling "diff" program.
     * {@inheritDoc}
     */
    public String makeDiffHtml(WikiContext ctx, String p1, String p2) {
        File f1 = null;
        File f2 = null;
        String diff = null;
        try {
            f1 = FileUtil.newTmpFile(p1, m_encoding);
            f2 = FileUtil.newTmpFile(p2, m_encoding);
            String cmd = TextUtil.replaceString(m_diffCommand, "%s1", f1.getPath());
            cmd = TextUtil.replaceString(cmd, "%s2", f2.getPath());
            String output = FileUtil.runSimpleCommand(cmd, f1.getParent());
            String rawWikiDiff = new String(output.getBytes("ISO-8859-1"), m_encoding);
            String htmlWikiDiff = TextUtil.replaceEntities(rawWikiDiff);
            if (m_traditionalColorization) diff = colorizeDiff(htmlWikiDiff); else diff = htmlWikiDiff;
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to do file diff", e);
        } catch (InterruptedException e) {
            log.log(Level.SEVERE, "Interrupted", e);
        } finally {
            if (f1 != null) f1.delete();
            if (f2 != null) f2.delete();
        }
        return diff;
    }

    /**
     * Goes through output provided by a diff command and inserts HTML tags to
     * make the result more legible. Currently colors lines starting with a +
     * green, those starting with - reddish (hm, got to think of color blindness
     * here...).
     */
    static String colorizeDiff(String diffText) throws IOException {
        if (diffText == null) return "Invalid diff - probably something wrong with server setup.";
        String line = null;
        String start = null;
        String stop = null;
        BufferedReader in = new BufferedReader(new StringReader(diffText));
        StringBuffer out = new StringBuffer();
        out.append("<table class=\"diff\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
        while ((line = in.readLine()) != null) {
            stop = CSS_DIFF_CLOSE;
            if (line.length() > 0) {
                switch(line.charAt(0)) {
                    case DIFF_ADDED_SYMBOL:
                        start = CSS_DIFF_ADDED;
                        break;
                    case DIFF_REMOVED_SYMBOL:
                        start = CSS_DIFF_REMOVED;
                        break;
                    default:
                        start = CSS_DIFF_UNCHANGED;
                }
            } else {
                start = CSS_DIFF_UNCHANGED;
            }
            out.append(start);
            out.append(line.trim());
            out.append(stop + "\n");
        }
        out.append("</table>\n");
        return out.toString();
    }
}
