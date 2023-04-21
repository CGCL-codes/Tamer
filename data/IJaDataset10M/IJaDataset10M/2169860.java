package com.xmultra.processor.nitf;

import java.util.HashMap;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import com.xmultra.processor.news.Characters;
import com.xmultra.util.InitMapHolder;
import com.xmultra.util.Strings;

/**
 * Any empty implementation. Use as starting point for a new NitfXformer subclass.
 *
 * @author      Wayne W. Weber
 * @version     $Revision: #1 $
 * @since       1.4
 */
class Nxf_Conversions extends NitfXformer {

    static final String URL_ADDR_PATTERN_OPEN = "([\\s>(])";

    static final String URL_ADDR_PATTERN_CLOSE = "(([^ \\/a-zA-Z]|\\s+.)(?!\\/[aA]>))";

    static final String ADDRESS_DOMAIN_PATTERN = "[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}\\/?";

    static final String EMAIL_ADDR_PATTERN = URL_ADDR_PATTERN_OPEN + "([\\w\\.\\-]+\\@" + ADDRESS_DOMAIN_PATTERN + ")" + URL_ADDR_PATTERN_CLOSE;

    static final String WEB_URL_DIR_FILE = "(?:\\/?[a-zA-Z0-9~\\/_\\-\\.,]*[a-zA-Z0-9~\\/])?";

    static final String WEB_URL_ADDR_PATTERN = URL_ADDR_PATTERN_OPEN + "((?:https?:\\/\\/|www)" + ADDRESS_DOMAIN_PATTERN + WEB_URL_DIR_FILE + ")" + URL_ADDR_PATTERN_CLOSE;

    /**
     * A map of attributes in the CaseConversions element which describes
     * which Components of the document will have their case converted.
     */
    private NamedNodeMap caseConversionNodeAttrMap = null;

    /**
     * A map attributes in the UrlLinkConversions element which specify
     * whether URL's or Email address will be converted to links.
     */
    private NamedNodeMap urlConversionNodeAttrMap = null;

    private HashMap nitfPatternsMap = null;

    private Characters characters = null;

    /**
     * Does a setup be creating and getting references to utilities.
     *
     * @param conversionsEl       The configuration element associated with this Xformer.
     *
     * @param nitfProcessorConfig Holds the NitfProcessorConfig data & methods.
     *
     * @param imh                 Holds references to utility and log objects.
     *
     * @param nitfXformerUtils    Has utility methods shared by Xformers.
     *
     * @return True if initialization is successful.
     */
    boolean init(Element conversionsEl, NitfProcessorConfig nitfProcessorConfig, InitMapHolder imh, NitfXformerUtils nitfXformerUtils) {
        super.init(conversionsEl, nitfProcessorConfig, imh, nitfXformerUtils);
        this.characters = new Characters(imh);
        Node caseConversionsNode = xmlParseUtils.getChildNode(conversionsEl, NitfProcessorConfig.CASE_CONVERSIONS);
        if (caseConversionsNode != null) {
            this.caseConversionNodeAttrMap = caseConversionsNode.getAttributes();
        }
        Node urlConversionsNode = xmlParseUtils.getChildNode(conversionsEl, NitfProcessorConfig.URL_LINK_CONVERSIONS);
        if (urlConversionsNode != null) {
            this.urlConversionNodeAttrMap = urlConversionsNode.getAttributes();
        }
        nitfPatternsMap = this.buildNitfPatternsMap();
        return true;
    }

    /**
     * Applies a transform or process to an Nitf document.
     *
     */
    boolean xform(NitfDoc nitfDoc) {
        this.convertCase(nitfDoc);
        this.convertToUrlLink(nitfDoc);
        return true;
    }

    /**
     * Searches the body of the document for addresses that can be converted
     * to Url links. For addresses that match email patterns puts the "mailto"
     * the link in the text. For example, if the text has:      <P>
     *
     * For info e-mail feedback@cars.com.                       <P>
     *
     * Then this routine will change it to:                     <P>
     *
     * For info e-mail &lt;a href="mailto:feedback@cars.com"&gt;feedback@cars.com&lt;/a&gt;. <P>
     *
     *
     * @param nitfDoc               Contains the body of the document which has
     *                              the components which may get case converted.
     *
     * @param conversionNodeAttrMap      */
    void convertToUrlLink(NitfDoc nitfDoc) {
        String body = nitfDoc.getBody();
        String addrPattern = null;
        if (this.urlConversionNodeAttrMap == null) {
            return;
        }
        String emailAddressAttrValue = this.urlConversionNodeAttrMap.getNamedItem(NitfProcessorConfig.EMAIL_ADDRESS).getNodeValue();
        if (!emailAddressAttrValue.equals(NitfProcessorConfig.NO_CONVERSION)) {
            if (emailAddressAttrValue.equals(NitfProcessorConfig.USE_DEFAULT)) {
                addrPattern = EMAIL_ADDR_PATTERN;
            } else {
                addrPattern = this.urlConversionNodeAttrMap.getNamedItem(NitfProcessorConfig.EMAIL_ADDR_CUSTOM_PATTRN).getNodeValue();
            }
            String replace = "$1<a href=\"mailto:$2\">$2<\\/a>$3";
            body = strings.substitute(addrPattern, replace, body);
        }
        String webAddressAttrValue = this.urlConversionNodeAttrMap.getNamedItem(NitfProcessorConfig.WEB_ADDRESS).getNodeValue();
        if (!webAddressAttrValue.equals(NitfProcessorConfig.NO_CONVERSION)) {
            if (webAddressAttrValue.equals(NitfProcessorConfig.USE_DEFAULT)) {
                addrPattern = WEB_URL_ADDR_PATTERN;
            } else {
                addrPattern = this.urlConversionNodeAttrMap.getNamedItem(NitfProcessorConfig.WEB_ADDR_CUSTOM_PATTERN).getNodeValue();
            }
            StringBuffer before = new StringBuffer(body.length());
            String address = null;
            String url = null;
            String link = null;
            String preAddressChar = null;
            String postAddressChar = null;
            while (strings.matchesIgnoreCase(addrPattern, body)) {
                before.append(strings.getPreMatch());
                preAddressChar = strings.getGroup(1);
                address = strings.getGroup(2);
                postAddressChar = strings.getGroup(3);
                body = strings.getPostMatch();
                if (strings.matchesIgnoreCase("https?://", address)) {
                    url = address;
                } else {
                    url = "http://" + address;
                }
                link = "<a href=\"" + url + "\">" + address + "</a>";
                before.append(preAddressChar).append(link).append(postAddressChar);
            }
            if (link != null) {
                before.append(body);
                body = before.toString();
            }
        }
        String urlInBold = NitfDoc.EM_BOLD_OPEN + "(\\s*<a [^>\\n]*?>[^<]+?<\\/a>[\\s,.]*)" + NitfDoc.EM_CLOSE;
        body = strings.substitute(urlInBold, "$1", body);
        nitfDoc.setBody(body);
    }

    /**
     * Converts the case of components (such as headlines or sigs). What
     * components to convert and which type of case conversion should
     * be used is derived from the configuration file.<P>
     *
     * Uses a HashMap of component patterns generated at init time.
     *
     * @param NItfDoc               Contains the body of the document which has
     *                              the components which may get case converted.
     *
     */
    void convertCase(NitfDoc nitfDoc) {
        if (this.caseConversionNodeAttrMap == null) {
            return;
        }
        String body = nitfDoc.getBody();
        String after = null;
        String openMark = null;
        String content = null;
        String closeMark = null;
        String attributeName = null;
        String attributeValue = null;
        String componentPattern = null;
        Object componentPatternObj = null;
        StringBuffer before = null;
        boolean foundMatch = false;
        for (int i = 0; i < this.caseConversionNodeAttrMap.getLength(); i++) {
            Node componentAttrNode = this.caseConversionNodeAttrMap.item(i);
            attributeName = componentAttrNode.getNodeName();
            attributeValue = componentAttrNode.getNodeValue();
            if (attributeValue.equals(NitfProcessorConfig.NO_CONVERSION)) {
                continue;
            }
            componentPatternObj = this.nitfPatternsMap.get(attributeName);
            if (componentPatternObj == null) {
                continue;
            }
            componentPattern = componentPatternObj.toString();
            before = new StringBuffer(body.length());
            after = body;
            foundMatch = false;
            while (strings.matches(componentPattern, after)) {
                before.append(strings.getPreMatch());
                after = strings.getPostMatch();
                openMark = strings.getGroup(1);
                content = strings.getGroup(2);
                closeMark = strings.getGroup(3);
                if (attributeValue.equals(NitfProcessorConfig.UPPER_CASE)) {
                    content = this.characters.convertStringWithEntitiesToUpperCase(content);
                }
                before.append(openMark).append(content).append(closeMark);
                foundMatch = true;
            }
            if (foundMatch) {
                body = before.append(after).toString();
            }
        }
        nitfDoc.setBody(body);
    }

    /**
     * Builds a HashMap of patterns which find content within an Nitf document.
     * The key is the name of the component (like Headline) and
     * the value is the pattern. The open tag, content, and close
     * tag are all in parenthesis to allow the use of back
     * references in regular expressions.
     *
     * @return The build up HashMap of Nim component patterns.
     */
    private HashMap buildNitfPatternsMap() {
        HashMap compPatMap = new HashMap();
        compPatMap.put(NitfProcessorConfig.SIG, "(" + NitfDoc.HL2_SIG_OPEN + ")" + "(" + Strings.ANY_TEXT + ")" + "(" + NitfDoc.HL2_CLOSE + ")");
        compPatMap.put(NitfProcessorConfig.KICKER, "(" + NitfDoc.HL2_KICKER_OPEN + ")" + "(" + Strings.ANY_TEXT + ")" + "(" + NitfDoc.HL2_CLOSE + ")");
        compPatMap.put(NitfProcessorConfig.HEADLINE, "(" + NitfDoc.HL1_OPEN + ")" + "(" + Strings.ANY_TEXT + ")" + "(" + NitfDoc.HL1_CLOSE + ")");
        compPatMap.put(NitfProcessorConfig.DECK, "(" + NitfDoc.HL2_DECK_OPEN + ")" + "(" + Strings.ANY_TEXT + ")" + "(" + NitfDoc.HL2_CLOSE + ")");
        compPatMap.put(NitfProcessorConfig.BYLINE, "(" + NitfDoc.BYLINE_OPEN + ")" + "(" + Strings.ANY_TEXT + ")" + "(" + NitfDoc.HL2_CLOSE + ")");
        compPatMap.put(NitfProcessorConfig.CREDITLINE, "(" + NitfDoc.CREDITLINE_OPEN + ")" + "(" + Strings.ANY_TEXT + ")" + "(" + NitfDoc.HL2_CLOSE + ")");
        compPatMap.put(NitfProcessorConfig.DROPCAP_LARGE, "(" + NitfDoc.EM_DROP_CAP_LARGE_OPEN + ")" + "(" + Strings.ANY_TEXT + ")" + "(" + NitfDoc.EM_CLOSE + ")");
        compPatMap.put(NitfProcessorConfig.DROPCAP_SMALL, "(" + NitfDoc.EM_DROP_CAP_SMALL_OPEN + ")" + "(" + Strings.ANY_TEXT + ")" + "(" + NitfDoc.EM_CLOSE + ")");
        compPatMap.put(NitfProcessorConfig.SUBHEAD, "(" + NitfDoc.SUBHEAD_OPEN + ")" + "(" + Strings.ANY_TEXT + ")" + "(" + NitfDoc.HL2_CLOSE + ")");
        compPatMap.put(NitfProcessorConfig.BRIEFS_SECTION_HEAD, "(" + NitfDoc.HL2_BRIEFS_SEC_HED_OPEN + ")" + "(" + Strings.ANY_TEXT + ")" + "(" + NitfDoc.HL2_CLOSE + ")");
        compPatMap.put(NitfProcessorConfig.BRIEFS_SUBHEAD, "(" + NitfDoc.HL2_BRIEFS_SUBHEAD_OPEN + ")" + "(" + Strings.ANY_TEXT + ")" + "(" + NitfDoc.HL2_CLOSE + ")");
        return compPatMap;
    }
}
