package org.nex.ts.server.tago.model;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * @author park
 * <p>
 * References:<br/>
 * <a href="http://www.taguri.org/">Tag URI</a><br/>
 * <a href="http://www.faqs.org/rfcs/rfc4151.html>RFC 4151 Tag URI Scheme</a>
 * </p>
 * <p>
 * Examples:
 * <li>tag:tagomizer.org,2006:tag/global:nifty</li>
 * <li>
 */
public class RFC4151 {

    /**
       tag:tagomizer.org,2006:tag/global:java
       tag:tagomizer.org,2006:tag/global:to-read
       tag:tagomizer.org,2006:tag/wikipedia:Java_programming_language
       tag:tagomizer.org,2006:tag/sri.com:CALO_meetings
       tag:tagomizer.org,2006:tag/project:<someproject>
       tag:tagomizer.org,2006:tag/maturity:<alpha, beta, ...>
       tag:tagomizer.org,2006:tag/license:<gpl,bsd,...>
       tag:tagomizer.org,2006:tag/implementation:<java,php,...>
     */
    public static final String RFC4151_TAG_NAMESPACE = "tag:tagomizer.org,2006";

    public static final String TAG_TYPE = ":tag/";

    public static final String RESOURCE_TYPE = ":resource/";

    public static final String DEFAULT_PREPEND = "global:";

    /**
   * <p>
   * <code>nameSpace</code> example: "tagomizer.org", "2006"
   * </p>
   * @param nameSpace String
   */
    public RFC4151() {
    }

    /**
   * <p>
   * Pure RFC4151
   * </p>
   * @param prepend String e.g. "global" can be <code>null</code>
   * @param tagString String e.g. "foo", "http://www.joe.org/"
   * @return String
   */
    public static String toTagURI(String prepend, String tagString) {
        StringBuffer buf = new StringBuffer(RFC4151_TAG_NAMESPACE);
        buf.append(TAG_TYPE);
        String pre = prepend;
        if (pre == null) pre = DEFAULT_PREPEND; else pre = pre + ":";
        buf.append(pre + tagString);
        return buf.toString();
    }

    public static String toResourceURI(String prepend, String tagString) {
        StringBuffer buf = new StringBuffer(RFC4151_TAG_NAMESPACE);
        buf.append(RESOURCE_TYPE);
        String pre = prepend;
        if (pre == null) pre = DEFAULT_PREPEND; else pre = pre + ":";
        buf.append(pre + tagString);
        return buf.toString();
    }
}
