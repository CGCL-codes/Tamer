package ca.cbc.sportwire;

/**
 * <p><b>WireFeederProperties.java</b>: public properties for the
 * configuration file and their defaults;
 *
 * </p>
 *
 * Created: Wed Feb 27 13:48:38 2002
 * <pre>
 * $Log: not supported by cvs2svn $
 * Revision 1.19  2006/09/08 02:55:47  garym
 * long overdue cvs update brings us to xts-2006 state.
 *
 * Revision 1.18  2006/02/28 15:41:53  garym
 * performance improvements
 *
 * Revision 1.17  2005/04/21 06:48:39  garym
 * Fixed doc ID bug; deprecated the StoryReader methods
 *
 * Revision 1.16  2005/04/05 19:26:18  garym
 * watchdog interval < 1 min disables the periodic watchdog check.
 * see TSNFeed for how to detect low level socket status instead.
 *
 * Revision 1.15  2004/07/18 03:49:37  garym
 * Refactored the sportwire into a servlet queue process and independent feeders
 *
 * Revision 1.14  2004/02/09 21:54:11  garym
 * modified to pass only the dtd-name (and not path) into the XSL transform
 *
 * Revision 1.13  2003/09/30 20:47:13  garym
 * added debug code on file-path attrib; moved sportsml dtd and root to properties
 *
 * Revision 1.12  2003/08/01 20:21:33  garym
 * SportsML validation logging completed.
 *
 * Revision 1.11  2003/08/01 18:34:36  garym
 * relative doctype sysid in sportsml - config option for absolute url
 *
 * Revision 1.10  2003/08/01 17:49:26  garym
 * inserted optional validation and spooling of invalid XML on input and out
 *
 * Revision 1.9  2003/07/25 22:54:28  garym
 * added config options for input xml validation switch and to move the tsn dtds
 *
 * Revision 1.8  2003/03/24 23:00:37  garym
 * checkpoint release
 *
 * Revision 1.7  2002/04/15 05:10:12  garym
 * removed SystemID from generated (static) SportsML files
 *
 * Revision 1.6  2002/04/10 04:33:41  garym
 * fixed CDATA bug in TSN; implemented external HTML filter
 *
 * Revision 1.5  2002/04/09 04:23:55  garym
 * implemented xmlrpc status monitor
 *
 * Revision 1.4  2002/04/05 21:35:10  garym
 * implement config option paths for xsl and map files
 *
 * Revision 1.3  2002/04/04 01:46:08  garym
 * Skeletal translation of TSN to SportsML
 *
 * Revision 1.2  2002/03/28 02:22:19  garym
 * Added RegexMap util and CDATA traps in the TSN-feed
 *
 * Revision 1.1  2002/02/27 19:41:51  garym
 * misc bugfixes to ESPN handling
 *
 *
 * </pre>
 * @author <a href="mailto:garym@teledyn.com">Gary Lawrence Murphy</a>
 * @version $Id: WireFeederProperties.java,v 1.20 2008-12-17 23:00:06 garym Exp $
 */
public interface WireFeederProperties {

    public static final String CONFIGFILE_PROPERTY = "wirefeeder.config", CONFIGFILE_DEFAULT = "sportwire.conf";

    public static final String LOGFILE_PROPERTY = "wirefeeder.log4j.config", LOGFILE_DEFAULT = "log4j.conf";

    public static final String WEBSERVICE_POSTLIST_PROPERTY = "wirefeeder.webservice.postlist", WEBSERVICE_POSTLIST_DEFAULT = "http://feed.xmlteam.com:8080/bcast/";

    public static final String WEBSERVICE_VENDORLIST_PROPERTY = "wirefeeder.vendorlist", WEBSERVICE_VENDORLIST_DEFAULT = "hbc,tsn,pbp";

    public static final String WEBSERVICE_VENDORHANDLER_PROPERTY = "wirefeeder.%s.dochandler", WEBSERVICE_VENDORHANDLER_DEFAULT = "ToNewsMLFilter";

    public static final String DOCHANDLER_PARM_PATTERN_PROPERTY = "wirefeeder.dochandler.parmpattern", DOCHANDLER_PARM_PATTERN_DEFAULT = "(.*\\.\\w+)<(\\w+)>$";

    public static final boolean NOTIFY_ENABLE_DEFAULT = false;

    public static final String NOTIFY_ENABLE_PROPERTY = "wirefeeder.notify", NOTIFY_POSTLIST_PROPERTY = "wirefeeder.notify.postlist", NOTIFY_POSTLIST_DEFAULT = "http://feed.xmlteam.com/bcast/";

    public static final String WATCHDOG_IDLE_PROPERTY = "wirefeeder.watchdog.timeout";

    public static final int WATCHDOG_IDLE_DEFAULT = 360;

    public static final String XMLRPC_PORT_PROPERTY = "xmlrpc.port";

    public static final int DEFAULT_XMLRPC_PORT = 8484;

    public static final String FEED_DUPCHECK_QSIZE = "wirefeeder.dupcheck.queuesize";

    public static final int FEED_DUPCHECK_DEFAULT = -1;

    public static final String FEEDCLASS_PROPERTY = "wirefeeder.feedclass", FEEDCLASS_DEFAULT = "ca.cbc.sportwire.feed.TSNFeed";

    public static final String TSN_DTD_URL_PROPERTY = "tsn.dtd.url", TSN_DTD_URL_DEFAULT = "tsn";

    public static final String TSN_DTD_PATH_PROPERTY = "tsn.dtd.path", TSN_DTD_PATH_DEFAULT = "";

    public static final String FEED_STORE_PROPERTY = "xml.feed.store", FEED_STORE_DEFAULT = "false";

    public static final String FEED_VALIDATE_PROPERTY = "xml.feed.validate", FEED_VALIDATE_DEFAULT = "false";

    public static final String SML_VALIDATE_PROPERTY = "xml.sportsml.validate", SML_VALIDATE_DEFAULT = "false";

    public static final String SML_ROOT_PROPERTY = "xml.sportsml.root", SML_ROOT_DEFAULT = "sports-content";

    public static final String SML_DTD_PROPERTY = "xml.sportsml.dtd", SML_DTD_DEFAULT = "dtds/sportsml-core.dtd";

    public static final String SML_PUBLIC_PROPERTY = "xml.sportsml.public", SML_PUBLIC_DEFAULT = "-//XMLTEAM//DTD sportsml-core 1.0/EN";

    public static final String SML_DTDPATH_PROPERTY = "xml.sportsml.dtdpath", SML_DTDPATH_DEFAULT = "file:///opt/feed/live";

    public static final String VALIDATE_SPOOLING_PROPERTY = "xml.validate.spooling", VALIDATE_SPOOLING_DEFAULT = "true";

    public static final String FEEDFILTER_PROPERTY = "wirefeeder.feed.filter", FEEDFILTER_DEFAULT = "txt2html -e";

    public static final String FEED_VENDOR_PROPERTY = "wirefeeder.feed.vendor", FEED_VENDOR_DEFAULT = "tsn";

    public static final String FEED_POSTMETHOD_PROPERTY = "wirefeeder.feed.method", FEED_POSTMETHOD_DEFAULT = "POST";

    public static final String DOCHANDLER_PROPERTY = "wirefeeder.dochandler.class", DOCHANDLER_DEFAULT = "ToJDOMFilter";

    public static final String DOCHANDLER_PKG_PROPERTY = "wirefeeder.dochandler.package", DOCHANDLER_PKG_DEFAULT = "ca.cbc.sportwire.dochandler";

    public static final String DOCWORKERS_PROPERTY = "wirefeeder.docqueue.workers";

    public static final String DOCWORKERS_DEFAULT = "0";

    public static final String IGNOREFILE_PROPERTY = "wirefeeder.ignorefile";

    public static final String FEED_REGEX_PROPERTY = "feed.regex";

    public static final String SAX_CLASS_PROPERTY = "dom.sax.class", DEFAULT_SAX_DRIVER_CLASS = "org.apache.xerces.parsers.SAXParser";

    public static final String XML_PATH_PROPERTY = "xml.path", DEFAULT_XML_PATH = "sportsml/";

    public static final String VOLATILE_PATH_REGEX_PROPERTY = "xml.volatile.path", DEFAULT_VOLATILE_PATH_REGEX = "resource/";

    public static final String FILENAME_XPATH_PROPERTY = "xml.filename.xpath", DEFAULT_FILENAME_XPATH = "/sports-content/@path-id";

    public static final String DATETIME_XPATH_PROPERTY = "xml.datetime.xpath", DEFAULT_DATETIME_XPATH = "//sports-metadata/@date-time";

    public static final String REVISION_XPATH_PROPERTY = "xml.revision.xpath", DEFAULT_REVISION_XPATH = "//sports-content/@revision-id";

    public static final String CONTENTSET_XPATH_PROPERTY = "xml.contentset.xpath", DEFAULT_CONTENTSET_XPATH = "//sports-content-set/sports-content";

    public static final String VAR_XPATH_PROPERTY = "xml.%s.xpath", DEFAULT_VAR_XPATH_PROPERTY = "//sports-metadata/@doc-id";

    public static final String XPATH_VARLIST_PROPERTY = "xml.xpath.varlist", XPATH_VARLIST_DEFAULT = "filename,revision,contentset";

    public static final String XSL_FILENAME_PROPERTY = "xsl.filename", DEFAULT_XSL_FILENAME = "sn2sml.xsl", X_XSL_FILENAME_PROPERTY = "xsl.%s.filename", DEFAULT_X_XSL_FILENAME = "%s2sml.xsl", ST_XSL_FILENAME_PROPERTY = "xsl.st.filename", DEFAULT_ST_XSL_FILENAME = "st2sml.xsl", FSS_XSL_FILENAME_PROPERTY = "xsl.fss.filename", DEFAULT_FSS_XSL_FILENAME = "fss2sml.xsl", ESA_XSL_FILENAME_PROPERTY = "xsl.esa.filename", DEFAULT_ESA_XSL_FILENAME = "esa2sml.xsl", ODDS_XSL_FILENAME_PROPERTY = "xsl.dbest.filename", DEFAULT_ODDS_XSL_FILENAME = "dbest2sml.xsl", TSN_PBP_XSL_FILENAME_PROPERTY = "xsl.tsn.pbp.filename", DEFAULT_TNS_PBP_XSL_FILENAME = "pbp2sml.xsl", RW_XSL_FILENAME_PROPERTY = "xsl.rw.filename", DEFAULT_RW_XSL_FILENAME = "rotoworld2sml.xsl", TSN_XSL_FILENAME_PROPERTY = "xsl.tsn.filename", DEFAULT_TSN_XSL_FILENAME = "sn2sml.xsl";

    public static final String XSL_SYSID_PARAM_PROPERTY = "dtdpath";

    public static final String XSL_INCREMENTAL_PROPERTY = "xsl.incremental", XSL_INCREMENTAL_DEFAULT = "false";

    public static final String XSL_OPTIMIZE_PROPERTY = "xsl.optimize", XSL_OPTIMIZE_DEFAULT = "true";

    public static final String XSL_PATH_PROPERTY = "xsl.path";

    public static final String MAP_PATH_PROPERTY = "xmldbms.map.path";

    public static final String XML_DOCTYPE_REGEX_PROPERTY = "xml.doctype.regex", XML_DOCTYPE_REGEX_DEFAULT = "(<!DOCTYPE.*\")([^\"]+)(\"[ ]*>)";

    public static final String XML_DOCTYPE_REGEX_SYSID_PROPERTY = "xml.doctype.regex.sysid", XML_DOCTYPE_REGEX_SYSID_DEFAULT = "(<!DOCTYPE[^\"]+\")([^\"]+)(\"[^>]*>)";

    public static final String DTD_PATH_PROPERTY = "dtd.path";
}
