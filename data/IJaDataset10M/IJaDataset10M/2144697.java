package ca.cbc.sportwire.feed;

/**
 * <p><b>TSNFeedProperties.java</b>: Common properties for the ESPN config
 *
 * </p>
 *
 * Created: Wed Feb 27 14:03:40 2002
 * <pre>
 * $Log: not supported by cvs2svn $
 * Revision 1.10  2004/04/09 14:34:23  garym
 * moved DTD path to WireProps so it's available for the validators
 *
 * Revision 1.9  2003/10/08 16:44:43  garym
 * fixed attribute filepath for new ID attrib in TSN
 *
 * Revision 1.8  2003/08/01 17:49:26  garym
 * inserted optional validation and spooling of invalid XML on input and out
 *
 * Revision 1.7  2003/07/25 22:54:31  garym
 * added config options for input xml validation switch and to move the tsn dtds
 *
 * Revision 1.6  2002/09/18 23:58:07  garym
 * stupid typo in properties
 *
 * Revision 1.5  2002/09/18 05:04:32  garym
 * implemented a generic fix for all cases of attribs on a line by themselves.
 *
 * Revision 1.4  2002/04/16 18:45:52  garym
 * fixed cdata paragraph bug and added HtmlUtil escape
 *
 * Revision 1.3  2002/04/04 01:46:09  garym
 * Skeletal translation of TSN to SportsML
 *
 * Revision 1.2  2002/03/28 02:22:19  garym
 * Added RegexMap util and CDATA traps in the TSN-feed
 *
 * Revision 1.1  2002/03/26 21:38:14  garym
 * added initial support for the TSN XML feed
 *
 * Revision 1.3  2002/03/04 21:40:05  garym
 * updated docs
 *
 * Revision 1.2  2002/03/04 03:37:31  garym
 * Implement the basic ESPN tables
 *
 * Revision 1.1  2002/02/27 19:41:51  garym
 * misc bugfixes to ESPN handling
 *
 *
 * </pre>
 * @author <a href="mailto:garym@teledyn.com">Gary Lawrence Murphy</a>
 * @version $Id: TSNFeedProperties.java,v 1.11 2005-04-05 19:19:58 garym Exp $
 */
public interface TSNFeedProperties {

    public static final String TSN_SPOOL_PROPERTY = "tsn.log";

    public static final String TSN_HOST_PROPERTY = "tsn.host";

    public static final String TSN_PORT_PROPERTY = "tsn.port";

    public static final String TSN_RETRY_PROPERTY = "tsn.retry";

    public static final int TSN_RETRY_DEFAULT = 3;

    public static final String TSN_USER_PROPERTY = "tsn.user";

    public static final String TSN_PASSWORD_PROPERTY = "tsn.password";

    public static final String TSN_IDLE_PROPERTY = "tsn.idletimeout";

    public static final int TSN_IDLE_DEFAULT = 300;

    public static final String TSN_SOURCE_PROPERTY = "tsn.source";

    public static final String TSN_SOURCE_DEFAULT = "socket";

    public static final String TSN_PAGEBREAK_PROPERTY = "tsn.pagebreak";

    public static final String TSN_PAGEBREAK_DEFAULT = "</message>";

    public static final String ELEMENT_PATTERN_PROPERTY = "xml.element.regex";

    public static final String ELEMENT_PATTERN_DEFAULT = "(<[^>]+>)([^<]+)(</.+)";

    public static final String TSN_SGML_PROPERTY = "sgml.element.regex";

    public static final String TSN_SGML_DEFAULT = "^[\\t ]*<[^>]+>.*";

    public static final String TSN_ATTR_PROPERTY = "tsn.attr.regex";

    public static final String TSN_ATTR_DEFAULT = "^[A-Za-z][A-Za-z0-9_]*=\".*";
}
