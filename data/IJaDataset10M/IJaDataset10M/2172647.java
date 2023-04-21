package com.flexiblewebsolutions.xmlstringparser.parser;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.flexiblewebsolutions.xmlstringparser.utils.ListUtils;
import com.flexiblewebsolutions.xmlstringparser.utils.XMLCleanup;

/**
 * Pulls values from an xml file using a simple XML parser.
 * 
 * @author dgbuss
 */
public class XMLStringParser {

    private static final Logger logger = LoggerFactory.getLogger(XMLStringParser.class);

    /**
	 * Return the value for a single tag inside a xml block.
	 * 
	 * @param pString
	 *            - XML string.
	 * @param pTag
	 *            - Name of tag to retrieve value for
	 * @return Value of tag
	 */
    public static String getValueForTag(StringBuffer pString, String pTag) {
        logger.debug("get value for tag: " + pTag);
        String startTag = "<" + pTag;
        String endTag = "</" + pTag + ">";
        String returnVal = null;
        int startIndex = pString.indexOf(startTag);
        if (startIndex != -1) {
            logger.debug("found tag in xml string");
            int endStartTag = pString.indexOf(">", startIndex);
            startTag = pString.substring(startIndex, endStartTag + 1);
            String fullTag = getEntireTag(pString, startIndex, endTag, startTag);
            returnVal = fullTag.substring(startTag.length(), fullTag.length() - endTag.length());
            returnVal = (XMLCleanup.removeTabChars(new StringBuffer(returnVal))).toString();
        } else {
            logger.debug("tag not found in xml string");
        }
        return (returnVal);
    }

    /**
	 * Retreive entire xml tag, ignoring embedded ending tags with same name
	 * 
	 * @param pXML
	 *            XML String
	 * @param pStartIndex
	 *            Location in XML to begin looking for tag.
	 * @param pEndTag
	 *            Ending Tag
	 * @param pStartTag
	 *            Starting XML tag
	 * @return Entire XML tag including start and end tag
	 */
    public static String getEntireTag(StringBuffer pXML, int pStartIndex, String pEndTag, String pStartTag) {
        logger.debug("get start and matching end tag and all contents, end tag specified");
        int startVal = pStartIndex + pStartTag.length();
        int endStartVal = pStartIndex + pStartTag.length();
        String tag = null;
        int possEndIndex = -1;
        int newStartIndex = -1;
        do {
            possEndIndex = pXML.indexOf(pEndTag, endStartVal);
            newStartIndex = pXML.indexOf(pStartTag, startVal);
            endStartVal = possEndIndex + pEndTag.length();
            startVal = newStartIndex + pStartTag.length();
        } while ((newStartIndex != -1) && (possEndIndex > newStartIndex));
        tag = pXML.substring(pStartIndex, possEndIndex + pEndTag.length());
        return (tag);
    }

    /**
	 * Retreive entire xml tag, ignoring embedded ending tags with same name.
	 * End tag is determined.
	 * 
	 * @param pXML
	 *            XML String
	 * @param pStartIndex
	 *            Location in XML to begin looking for tag.
	 * @param pStartTag
	 *            Starting XML tag
	 * @return Entire XML tag including start and end tag
	 */
    public static String getEntireTag(StringBuffer pXML, int pStartIndex, String pStartTag) {
        logger.debug("get start and matching end tag and all contents");
        StringBuffer tempEndTag = new StringBuffer(pStartTag.trim());
        tempEndTag.insert(1, "/");
        String endTag = tempEndTag.toString();
        return (getEntireTag(pXML, pStartIndex, endTag, pStartTag));
    }

    /**
	 * Retrieves the tags nested within the top level of the XML string matching
	 * a tag name.
	 * 
	 * @param pXmlString
	 *            XML string containing values.
	 * @param pTag
	 *            Name of tag to find values for.
	 * @return Array of Tag values.
	 */
    public static String[] getMatchingNestedTags(String pXmlString, String pTag) {
        logger.debug("find all tags inside another tag matching specified tag name");
        List<String> nestedVals = getNestedTags(pXmlString);
        String startTag = "<" + pTag + ">";
        List<String> returnVals = new ArrayList<String>();
        for (String tag : nestedVals) {
            if (tag.startsWith(startTag)) {
                returnVals.add(tag);
            }
        }
        return ListUtils.convertToStringArray(returnVals);
    }

    /**
	 * Retrieves all tags nested within the top level of the XML string.
	 * 
	 * @param pXmlString
	 *            XML string containing values.
	 * @return ArrayList of Tag Values.
	 */
    private static List<String> getNestedTags(String pXmlString) {
        logger.debug("find all tags inside another tag");
        StringBuffer xmlContents = XMLCleanup.removeNewlines(new StringBuffer(pXmlString));
        List<String> nestedVals = new ArrayList<String>();
        while (xmlContents.length() > 0) {
            int nextClosing = xmlContents.indexOf(">");
            if (nextClosing == -1) {
                xmlContents.delete(0, xmlContents.length());
            } else {
                String thisStartTag = xmlContents.substring(0, nextClosing + 1);
                String firstEntry = getEntireTag(xmlContents, 0, thisStartTag);
                xmlContents.delete(0, firstEntry.length());
                nestedVals.add(firstEntry);
            }
        }
        return (nestedVals);
    }

    /**
	 * Retrieves all tags nested within the tol level of the XML string.
	 * 
	 * @param pXmlString
	 *            XML string containing values
	 * @return String array of tag values.
	 */
    public static String[] getAllNestedTags(String pXmlString) {
        List<String> nestedTags = getNestedTags(pXmlString);
        return ListUtils.convertToStringArray(nestedTags);
    }
}
