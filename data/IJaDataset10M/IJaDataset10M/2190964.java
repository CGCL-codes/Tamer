package com.xmultra.processor.digest;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.xmultra.log.ErrorLogEntry;
import com.xmultra.log.Logger;
import com.xmultra.util.InitMapHolder;
import com.xmultra.util.Strings;
import com.xmultra.util.XmlParseUtils;

/**
 * Contains constants and methods for parsing the DigestProcessor's config file.
 *
 * @version   $Revision: #1 $
 * @author Mei-chen Hung
 * @author Wayne W. Weber
 */
class DigestProcessorConfig {

    public static final String VERSION = "@version $Revision: #1 $";

    static final String DIGEST_CFG_DTD = "defs/digest_processor_cfg.dtd";

    static final String SITE_ID = "SiteId";

    static final String DIGEST_SOURCE = "DigestSource";

    static final String DIGEST = "Digest";

    static final String DIGEST_FILE_NAME = "DigestFileName";

    static final String DIGEST_TYPE = "DigestType";

    static final String CATEGORY = "Category";

    static final String ENTRY_NUMBER = "DigestEntryNumber";

    static final String ENTRY_SELECTION = "EntrySelection";

    static final String ENTRY_SELECTION_NEW = "NewEntriesOnly";

    static final String ENTRY_SEL_COMPONENT = "EntrySelectionComponent";

    static final String LINK = "Link";

    static final String HEADLINE = "Headline";

    static final String MIN_ENTRIES = "MinEntries";

    static final String MIN_NEW_ENTRIES = "MinNewEntries";

    static final String LINK_HREF_SEARCH = "LinkHrefSearch";

    static final String LINK_HREF_REPLACE = "LinkHrefReplace";

    static final String INSERT_VALUE_FORMAT = "InsertionValueFormat";

    static final String INSERT_POSITION_PATTERN = "InsertionPositionPattern";

    static final String DEST_LOCATION = "DestinationDirectory";

    static final String IDENTIFICATON = "Identification";

    static final String OPEN_TAG = "OpenMark";

    static final String CLOSE_TAG = "CloseMark";

    static final String CONTENT_PATTERN = "Content";

    static final String CONTENT_GROUP = "ContentGroup";

    static final String IGNORE_CASE = "IgnoreCase";

    static final String ENTRY = "Entry";

    static final String ENTRY_COMPONENTS = "EntryComponents";

    static final String REPLACE_COMPONENT_NAME = "%COMPONENT_NAME%";

    static final String REPLACE_COMPONENT_VALUE = "%COMPONENT_VALUE%";

    static final String MSG_INSERT_NAME_TAG = "InsertionPattern";

    static final String MSG_INSERT_VALUE_TAG = "InsertValue";

    static final String MSG_INSERT_POS_CASE_SENSITIVE = "InsertionPosCaseSensitive";

    static final String EMAIL = "Email";

    static final String SEND_EMAIL = "EmailNotice";

    static final String SUBJECT = "Subject";

    static final String TO = "To";

    static final String FROM = "From";

    static final String ADDRESS = "ToAddress";

    private XmlParseUtils xmlParseUtils = null;

    private Logger logger = null;

    private ErrorLogEntry errEntry = null;

    private Strings strings = null;

    /**
     * Constructor.
     */
    DigestProcessorConfig(InitMapHolder imh, Strings strings) {
        InitMapHolder initMapHolder = imh;
        this.strings = strings;
        logger = (Logger) initMapHolder.getEntry(InitMapHolder.LOGGER);
        errEntry = new ErrorLogEntry(this, VERSION);
        this.xmlParseUtils = (XmlParseUtils) initMapHolder.getEntry(InitMapHolder.XML_PARSE_UTILS);
    }

    /**
     * Reads a Identification element (node) and builds a parsing pattern.
     *
     * @param idNode          The Identification node to parse.
     *
     * @return The string with the parsed out pattern.
     */
    String buildIdPattern(Node idNode) {
        Element idElement = null;
        String openMark = null;
        String closeMark = null;
        String content = null;
        String idPattern = null;
        idElement = xmlParseUtils.getSingletonElement((Element) idNode, IDENTIFICATON);
        openMark = idElement.getAttribute(OPEN_TAG);
        closeMark = idElement.getAttribute(CLOSE_TAG);
        content = idElement.getAttribute(CONTENT_PATTERN);
        idPattern = openMark + content + closeMark;
        return idPattern;
    }

    /**
     * Gets the value of the "ContentGroup" attribute in the Identification
     * element. <P>
     *
     * Refers to the regular expression developed by concatenating the
     * OpenMark, Content, and CloseMark attributes in the Identification
     * element. The OpenMark and/or Content may contain groups (pairs of
     * parenthesis), and the setting in this attribute indicates the
     * actual group number that contains the content of interest
     * (the Link, Headline, or Lead). Must be a number from 0-9. <P>
     *
     * If set to "0", the entire value matched matched by concatenating
     * the OpenMark, Content, and CloseMark will be used.
     *
     * @param node  The Identification element.
     *
     * @return The group number in the config file.
     */
    String getContentGroup(Node node) {
        Element idElement = xmlParseUtils.getSingletonElement((Element) node, IDENTIFICATON);
        return idElement.getAttribute(CONTENT_GROUP);
    }

    /**
     * Gets the value of the "IgnoreCase" attribute in the Identification
     * element. <P>
     *
     * Refers to the regular expression developed by concatenating the
     * OpenMark, Content, and CloseMark attributes in the Identification
     * element. If set to "Yes", case will be ignored in the regex.
     *
     * @param node  The Identification element.
     *
     * @return The group number in the config file.
     */
    boolean getIgnoreCase(Node node) {
        Element idElement = xmlParseUtils.getSingletonElement((Element) node, IDENTIFICATON);
        String ignoreCaseAttrValue = idElement.getAttribute(IGNORE_CASE);
        boolean ignoreCase = false;
        if (ignoreCaseAttrValue.equalsIgnoreCase("Yes")) {
            ignoreCase = true;
        }
        return ignoreCase;
    }

    /**
     * Validates selected attribute values of configuration file.
     *
     * @param Doc The configuration file.
     *
     * @return boolean True if validation succeeds.
     */
    boolean validateAttributeValues(Document Doc) {
        String minEntries = null;
        String minNewEntries = null;
        try {
            NodeList nl = Doc.getElementsByTagName(DigestProcessorConfig.DIGEST);
            for (int i = 0; i < nl.getLength(); i++) {
                Node digestNode = nl.item(i);
                minEntries = xmlParseUtils.getAttributeValueFromNode(digestNode, DigestProcessorConfig.MIN_ENTRIES);
                minNewEntries = xmlParseUtils.getAttributeValueFromNode(digestNode, DigestProcessorConfig.MIN_NEW_ENTRIES);
                Integer.parseInt(minEntries);
                Integer.parseInt(minNewEntries);
            }
        } catch (NumberFormatException e) {
            errEntry.setThrowable(e);
            errEntry.setAppContext("validateAttributeValues()");
            errEntry.setAppMessage("MinEntries or MinNewEntries missing or incorrect format.");
            logger.logError(errEntry);
            return false;
        }
        return true;
    }

    /**
     * Check to see if the total number of entries and number of new entries
     * meets the minimum required by the "MinEntries" and "MinNewEntries"
     * attributes of the "Digest" element in the config file. Generates error
     * message if not enough entries.
     *
     * @param listOfEntryComponentContentLists    List of the lists of content
     *                                            from each component in each
     *                                            entry.
     *
     * @param newListOfEntryComponentContentLists List of the lists of content
     *                                            from each component in each
     *                                            entry. If only retrieving
     *                                            "NewEntriesOnly", this list
     *                                            does not have previously
     *                                            retrieved digest entries.
     *
     * @param digestNode                          The Digest element from the
     *                                            config file.
     *
     * @return Error message indicating there are not enough entries.
     */
    String checkNumberOfEntries(ArrayList listOfEntryComponentContentLists, ArrayList newListOfEntryComponentContentLists, Node digestNode, String siteId, String categoryOfDigest) {
        String minNewEntriesStr = xmlParseUtils.getAttributeValueFromNode(digestNode, DigestProcessorConfig.MIN_NEW_ENTRIES);
        String minEntriesStr = xmlParseUtils.getAttributeValueFromNode(digestNode, DigestProcessorConfig.MIN_ENTRIES);
        String errMessage = null;
        int minEntries = Integer.parseInt(minEntriesStr);
        int minNewEntries = Integer.parseInt(minNewEntriesStr);
        int numOfEntries = listOfEntryComponentContentLists.size();
        if (numOfEntries < minEntries) {
            errMessage = "'" + numOfEntries + "' entries in a digest are less " + "than the required number at SiteId '" + siteId + "'" + " and Category '" + categoryOfDigest + "'. " + "Required number is: " + minEntries;
        }
        int numOfNewEntries = newListOfEntryComponentContentLists.size();
        if (numOfNewEntries < minNewEntries) {
            errMessage = "'" + numOfNewEntries + "' new entries in a digest are less " + "than the required number at SiteId '" + siteId + "'" + " and Category '" + categoryOfDigest + "'. " + "Required number is: " + minNewEntries;
        }
        return errMessage;
    }

    /**
     * Parses out and returns identified content from the doc. Uses the
     * passed in node's Identification element child to build a regular
     * expression.
     *
     * @param idParentNode
     *
     * @param content    source content
     *
     * @return matched content
     */
    String parseIdentifiedContent(Node idParentNode, String docContent) {
        String identifiedContent = null;
        String options = null;
        String idRegex = null;
        idRegex = buildIdPattern(idParentNode);
        idRegex = xmlParseUtils.conditionAttributeValue(idRegex, false);
        int matchGroup = Integer.parseInt(getContentGroup(idParentNode));
        boolean ignoreCase = getIgnoreCase(idParentNode);
        if (ignoreCase) options = "im"; else options = "m";
        if (strings.matchesWithOptions(idRegex, docContent, options)) {
            identifiedContent = strings.getMatchedGroup(matchGroup);
        }
        return identifiedContent;
    }
}
