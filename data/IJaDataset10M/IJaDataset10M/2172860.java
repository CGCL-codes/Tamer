package com.itmatter.jamwiki.bulkloader;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Hashtable;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.utils.NamespaceHandler;
import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.model.WikiUser;
import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;
import java.util.Stack;
import org.jamwiki.DataHandler;
import org.jamwiki.WikiBase;
import org.jamwiki.parser.bliki.BlikiProxyParserUtil;

/**
 *
 * @author Daniel.Fisla
 */
public class JAMWikiLoadHandler extends org.xml.sax.helpers.DefaultHandler {

    static Logger logger = Logger.getLogger(JAMWikiLoadHandler.class.getName());

    private StringBuffer currentElementBuffer = null;

    private final WikiUser user;

    private final String authorIpAddress;

    private String virtualWiki = "en";

    private Integer nsKey = null;

    private String nsVal = null;

    private String pageName = null;

    private String pageText = null;

    private String pageComment = null;

    private String ns14 = "Category";

    private String ns6 = "Image";

    private Hashtable<String, Object> namespaces = new Hashtable<String, Object>();

    private Stack<String> STACK = new Stack<String>();

    private Date startDate = new Date();

    private Date endDate = new Date();

    private DataHandler dataHandler = null;

    public String includePrefix = null;

    public String excludePrefix = null;

    public boolean excludeRedirects;

    /**
     *
     * @param virtualWiki
     * @param user
     * @param authorIpAddress
     */
    public JAMWikiLoadHandler(String virtualWiki, WikiUser user, String authorIpAddress) {
        this.currentElementBuffer = new StringBuffer();
        this.virtualWiki = virtualWiki;
        this.authorIpAddress = authorIpAddress;
        this.user = user;
        this.dataHandler = WikiBase.getDataHandler();
        this.excludeRedirects = false;
    }

    public void startDocument() throws org.xml.sax.SAXException {
        this.startDate = new Date(System.currentTimeMillis());
    }

    public void endDocument() throws org.xml.sax.SAXException {
        this.endDate = new Date(System.currentTimeMillis());
        logger.debug("XML-PARSE_TIME =>: (ms)" + (endDate.getTime() - startDate.getTime()));
    }

    public void startElement(String namespaceURI, String localName, String qName, org.xml.sax.Attributes attributes) throws org.xml.sax.SAXException {
        String eName = localName;
        if ("".equals(eName)) {
            eName = qName;
        }
        this.currentElementBuffer = new StringBuffer();
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                String aName = attributes.getLocalName(i);
                if ("".equals(aName)) {
                    aName = attributes.getQName(i);
                }
            }
        }
        STACK.push(qName);
        try {
            if ("namespace".equals(eName)) {
                nsKey = Integer.valueOf(attributes.getValue("key"));
            } else if ("page".equals(eName)) {
                pageName = "";
                pageText = "";
                pageComment = "";
            }
        } catch (Exception ex) {
            logger.error("Character parse Error", ex);
        }
    }

    public void characters(char[] ch, int start, int length) throws org.xml.sax.SAXException {
        if ((ch != null) && (length > 0)) {
            this.currentElementBuffer.append(ch, start, length);
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws org.xml.sax.SAXException {
        if (!STACK.peek().equals(qName)) {
            logger.error("STACK-PARSE-ERROR =>: " + STACK.peek() + " NOT-EQUAL " + qName);
            System.exit(1);
        }
        String lastStr = null;
        byte[] contentBytes = null;
        try {
            contentBytes = currentElementBuffer.toString().getBytes("UTF-8");
            lastStr = new String(contentBytes, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            logger.error(uee.getMessage(), uee);
        }
        if ("namespace".equals(qName)) {
            namespaces.put(lastStr, nsKey);
            if (nsKey.intValue() == 14) {
                ns14 = lastStr;
            }
            if (nsKey.intValue() == 6) {
                ns6 = lastStr;
            }
        } else if ((STACK.size() == 3) && STACK.peek().equals("id")) {
            int id = Integer.parseInt(lastStr);
        } else if ((STACK.size() == 3) && STACK.peek().equals("title")) {
            pageName = lastStr;
        } else if ((STACK.size() == 4) && STACK.peek().equals("id")) {
            pageComment = "Wikipedia Revision: " + lastStr;
        } else if ((STACK.size() == 4) && STACK.peek().equals("timestamp")) {
        } else if ((STACK.size() == 5) && STACK.peek().equals("id")) {
        } else if ((STACK.size() == 5) && STACK.peek().equals("username")) {
        } else if ((STACK.size() == 4) && STACK.peek().equals("minor")) {
        } else if ((STACK.size() == 4) && STACK.peek().equals("comment")) {
        } else if ((STACK.size() == 4) && STACK.peek().equals("text")) {
            pageText = lastStr;
        } else if ((STACK.size() == 2) && STACK.peek().equals("page") && ("page".equals(qName))) {
            String sNamespace = "";
            int namespace = 0;
            int pos = pageName.indexOf(':');
            if (pos > -1) {
                sNamespace = pageName.substring(0, pos);
                if (namespaces.containsKey(sNamespace)) {
                    namespace = ((Integer) namespaces.get(sNamespace));
                } else {
                    namespace = -1;
                }
            } else {
                namespace = 0;
            }
            try {
                pageText = preprocessText(pageText);
                pageName = convertArticleNameFromWikipediaToJAMWiki(pageName);
                boolean passChecks = true;
                if ((this.excludePrefix != null) && (pageName != null) && (pageName.toLowerCase().startsWith(this.excludePrefix))) {
                    passChecks = false;
                }
                if ((this.includePrefix != null) && (pageName != null) && (pageName.toLowerCase().startsWith(this.includePrefix))) {
                    passChecks = true;
                }
                String redirectTo = BlikiProxyParserUtil.isRedirect(pageText);
                if ((redirectTo != null) && (this.excludeRedirects)) {
                    passChecks = false;
                }
                if (passChecks) {
                    Topic topic = new Topic();
                    topic.setName(pageName);
                    topic.setVirtualWiki(virtualWiki);
                    topic.setTopicContent(pageText);
                    int charactersChanged = StringUtils.length(pageText);
                    TopicVersion topicVersion = new TopicVersion(user, authorIpAddress, pageComment, pageText, charactersChanged);
                    topic.setTopicType(convertNamespaceFromMediaWikiToJAMWiki(namespace));
                    if (redirectTo != null) {
                        topic.setRedirectTo(redirectTo);
                        topic.setTopicType(Topic.TYPE_REDIRECT);
                    }
                    dataHandler.writeTopic(topic, topicVersion, null, null, true, false);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        STACK.pop();
    }

    /**
     *
     * @param text
     * @return
     */
    public String preprocessText(String text) {
        String ret = text;
        ret = StringUtils.replace(ret, "[[category:", "[[" + NamespaceHandler.NAMESPACE_CATEGORY + ":");
        if (!"Category".equals(NamespaceHandler.NAMESPACE_CATEGORY)) {
            ret = StringUtils.replace(ret, "[[Category:", "[[" + NamespaceHandler.NAMESPACE_CATEGORY + ":");
        }
        ret = StringUtils.replace(ret, "[[" + ns14 + ":", "[[" + NamespaceHandler.NAMESPACE_CATEGORY + ":");
        ret = StringUtils.replace(ret, "[[image:", "[[" + NamespaceHandler.NAMESPACE_IMAGE + ":");
        if (!"Image".equals(NamespaceHandler.NAMESPACE_CATEGORY)) {
            ret = StringUtils.replace(ret, "[[Image:", "[[" + NamespaceHandler.NAMESPACE_IMAGE + ":");
        }
        ret = StringUtils.replace(ret, "[[" + ns6 + ":", "[[" + NamespaceHandler.NAMESPACE_IMAGE + ":");
        return ret;
    }

    /**
     *
     */
    private String convertArticleNameFromWikipediaToJAMWiki(String fullName) {
        String ret = fullName;
        String sNamespace = "";
        String sJAMNamespace = "";
        String sTitle = pageName;
        int pos = pageName.indexOf(':');
        if (pos > -1) {
            sNamespace = pageName.substring(0, pos);
            if (namespaces.containsKey(sNamespace)) {
                int namespace = ((Integer) namespaces.get(sNamespace));
                sTitle = pageName.substring(pos + 1);
                sJAMNamespace = getJAMWikiNamespaceById(convertNamespaceFromMediaWikiToJAMWiki(namespace));
                if (sJAMNamespace.length() > 0) {
                    ret = sJAMNamespace + ":" + sTitle;
                } else {
                    ret = sNamespace + ":" + sTitle;
                }
            } else {
                ret = pageName;
            }
        } else {
            ret = pageName;
        }
        return ret;
    }

    /**
     * convert MediaWiki namespace-id to JAMWiki namespace-id
     * @param mediaWikiNamespaceId
     * @return
     */
    private int convertNamespaceFromMediaWikiToJAMWiki(int mediaWikiNamespaceId) {
        int ret = -1;
        switch(mediaWikiNamespaceId) {
            case 0:
                ret = Topic.TYPE_ARTICLE;
                break;
            case 6:
                ret = Topic.TYPE_IMAGE;
                break;
            case 14:
                ret = Topic.TYPE_CATEGORY;
                break;
            case 10:
                ret = Topic.TYPE_TEMPLATE;
                break;
        }
        return ret;
    }

    /**
     *
     */
    private String getJAMWikiNamespaceById(int jamWikiNamespaceId) {
        String ret = "";
        switch(jamWikiNamespaceId) {
            case Topic.TYPE_IMAGE:
                ret = NamespaceHandler.NAMESPACE_IMAGE;
                break;
            case Topic.TYPE_CATEGORY:
                ret = NamespaceHandler.NAMESPACE_CATEGORY;
                break;
            case Topic.TYPE_TEMPLATE:
                ret = NamespaceHandler.NAMESPACE_TEMPLATE;
                break;
        }
        return ret;
    }

    public void warning(SAXParseException x) {
        logger.warn("Wiki Parser Warning =>: ", x);
    }

    public void error(SAXParseException x) {
        logger.error("Wiki Parser Error =>: ", x);
    }

    public void fatalError(SAXParseException x) {
        logger.error("FATAL ERROR!", x);
    }

    public String getExcludePrefix() {
        return excludePrefix;
    }

    public void setExcludePrefix(String excludePrefix) {
        if (excludePrefix != null) {
            this.excludePrefix = excludePrefix.trim().toLowerCase();
        }
    }

    public boolean isExcludeRedirects() {
        return excludeRedirects;
    }

    public void setExcludeRedirects(boolean excludeRedirects) {
        this.excludeRedirects = excludeRedirects;
    }

    public String getIncludePrefix() {
        return includePrefix;
    }

    public void setIncludePrefix(String includePrefix) {
        if (includePrefix != null) {
            this.includePrefix = includePrefix.trim().toLowerCase();
        }
    }
}
