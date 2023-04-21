package net.sourceforge.gedapi.io;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;
import com.liferay.portal.servlet.filters.context.sso.SSOSubject;
import net.sourceforge.gedapi.util.GLinkPattern;
import net.sourceforge.gedapi.util.GLinkURL;

public class GegPullHandler extends GLinkPullHandler {

    private static final Logger LOG = Logger.getLogger(GegPullHandler.class.getName());

    private static final SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    static {
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private StringBuilder writeDelta = null;

    private int absInsertDelta;

    private String toName;

    private GLinkURL toURL;

    private String fromRelation;

    private String toRelation;

    private int deltaCount;

    private SSOSubject authenticated;

    public GegPullHandler() {
        this.deltaCount = 0;
    }

    public void handlePattern(GLinkPattern pattern, int ch) throws IOException {
        String chars = null;
        if (ch == -1) {
            chars = writeDelta.toString();
        } else {
            int cachedCh = ch;
            if (writeDelta != null) {
                writeDelta.append((char) ch);
                if (writeDelta.length() >= absInsertDelta) {
                    cachedCh = writeDelta.charAt(0);
                    writeDelta.deleteCharAt(0);
                } else {
                    cachedCh = -2;
                }
            }
            if (GLinkPattern.DEBUG) {
                LOG.finest("The writeDelta data member is: " + writeDelta);
            }
            if (cachedCh != -2) {
                chars = String.valueOf((char) cachedCh);
            }
            if ('\r' != (char) ch) {
                if (pattern.match(ch)) {
                    LOG.finest("Found pattern: " + pattern + " when ch is '" + (char) ch + "' and deltaCount is " + deltaCount + " and writeDelta is: " + writeDelta + " and cachedCh is: " + (char) cachedCh);
                    deltaCount = 0;
                    chars += writeDelta.substring(0, writeDelta.length() - pattern.getCompareSize());
                    LOG.finest("The first chars after the pattern was found are: " + chars);
                    writeDelta.delete(0, writeDelta.length() - pattern.getCompareSize());
                    LOG.finest("writeDelta is now: " + writeDelta);
                    chars += "<g:link>";
                    chars += "<g:glinkAnchor>" + toName + "</g:glinkAnchor>";
                    chars += "<g:glink>" + toURL + "</g:glink>";
                    chars += "<g:relation>" + fromRelation + GLinkPattern.RELATION_SEPARATOR + toRelation + "</g:relation>";
                    chars += "<g:lastAuthorId>" + ((authenticated == null || authenticated.getScreenName() == null) ? "" : authenticated.getScreenName()) + "</g:lastAuthorId>";
                    chars += "<g:lastChangeDate>" + isoFormat.format(new Date()) + "</g:lastChangeDate>";
                    chars += "</g:link>\r\n";
                }
            }
        }
        if (chars != null && chars.length() > 0 && outWriter != null) {
            outWriter.write(chars);
        }
    }

    public void handleInsertDelta(int insertDelta) {
        absInsertDelta = Math.abs(insertDelta);
        if (insertDelta < 0) {
            writeDelta = new StringBuilder();
        } else {
            writeDelta = null;
        }
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public GLinkURL getToURL() {
        return toURL;
    }

    public void setToURL(GLinkURL toURL) {
        this.toURL = toURL;
    }

    public void setAuthenticated(SSOSubject authenticated) {
        this.authenticated = authenticated;
    }

    public String getFromRelation() {
        return fromRelation;
    }

    public void setFromRelation(String fromRelation) {
        this.fromRelation = fromRelation;
    }

    public String getToRelation() {
        return toRelation;
    }

    public void setToRelation(String toRelation) {
        this.toRelation = toRelation;
    }
}
