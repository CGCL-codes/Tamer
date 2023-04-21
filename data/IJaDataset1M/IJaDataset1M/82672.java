package jwebapp;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import jcommontk.utils.StringUtils;
import jcommontk.utils.XMLParser;
import jcommontk.utils.XMLParserException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@SuppressWarnings("unchecked")
class XMLDataManager extends DataManager {

    private static Logger logger = Logger.getLogger(XMLDataManager.class.getName());

    private URL fileUrl;

    private String filename;

    private boolean validate;

    private Map _validationMap;

    private List _validationList;

    private Map _urlDataMap;

    private Map _messageMap;

    private Map _requestMap;

    private List _requestRegularExpressionList;

    private XMLDataParser parser = new XMLDataParser();

    private XMLDataManager thisManager = this;

    XMLDataManager(URL fileUrl, String filename, boolean validate) throws Exception {
        this.fileUrl = fileUrl;
        this.filename = filename;
        this.validate = validate;
        loadData();
        if (filename != null) new ReloadThread().start();
    }

    boolean loadData() throws Exception {
        _validationMap = new HashMap();
        _validationList = new ArrayList();
        _urlDataMap = new HashMap();
        _messageMap = new HashMap();
        _requestMap = new HashMap();
        _requestRegularExpressionList = new ArrayList();
        parser.loadXMLData(fileUrl, validate);
        requestMap = _requestMap;
        requestRegularExpressionList = _requestRegularExpressionList;
        urlDataMap = _urlDataMap;
        validationMap = _validationMap;
        validationList = _validationList;
        messageMap = _messageMap;
        return true;
    }

    class ReloadThread extends Thread {

        long last;

        public void run() {
            try {
                do {
                    File file = new File(filename);
                    if (last != 0) if (last != file.lastModified()) {
                        thisManager.reload();
                        logger.info("jWebApp XML configuration reloaded");
                    }
                    last = file.lastModified();
                    try {
                        sleep(10000);
                    } catch (InterruptedException e) {
                    }
                } while (true);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Error running reload thread", ex);
            }
        }
    }

    class XMLDataParser extends XMLParser {

        public void loadXMLData(URL fileUrl, boolean validate) throws IOException, XMLParserException {
            loadData(fileUrl, validate);
        }

        public void processXML(Element e) throws Exception {
            loadRequests(e);
            loadUrlForwardsRedirects(e, "jwaUrl", false);
            loadUrlForwardsRedirects(e, "forward", false);
            loadUrlForwardsRedirects(e, "redirect", true);
            loadValidation(e);
            loadMessages(e);
        }
    }

    void loadRequests(Element e) throws JWebAppException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        NodeList nl = e.getElementsByTagName("request");
        for (int i = 0; i < nl.getLength(); i++) {
            Element e2 = (Element) nl.item(i);
            String requestId = StringUtils.emptyToNull(e2.getAttribute("id")), match = StringUtils.emptyToNull(e2.getAttribute("match")), regularExpression = StringUtils.emptyToNull(e2.getAttribute("matchRegExp")), isSecure = StringUtils.emptyToNull(e2.getAttribute("secureConnection")), handlerClass = StringUtils.emptyToNull(e2.getAttribute("handlerClass")), handlerMethod = StringUtils.emptyToNull(e2.getAttribute("handlerMethod"));
            if (handlerMethod != null) {
                if (handlerMethod.toLowerCase().startsWith("process") && handlerMethod.length() > "process".length()) handlerMethod = handlerMethod.substring("process".length());
            }
            RequestData r = new RequestData(requestId, match, regularExpression, handlerClass, handlerMethod, new Boolean(isSecure == null ? "false" : isSecure).booleanValue(), true);
            loadRequestParameters(r, e2);
            loadRequestRoles(r, e2);
            loadRequestForwards(r, e2);
            loadRequestRedirects(r, e2);
            loadRequestValidation(r, e2);
            if (requestId == null) throw new JWebAppException("Request can not have null request Id");
            if (regularExpression != null) _requestRegularExpressionList.add(r); else _requestMap.put(match != null ? match : requestId, r);
        }
    }

    void loadRequestParameters(RequestData r, Element e) {
        NodeList nl = e.getElementsByTagName("parameter");
        for (int i = 0; i < nl.getLength(); i++) r.getParameterMap().put(((Element) nl.item(i)).getAttribute("name"), ((Element) nl.item(i)).getAttribute("value"));
    }

    void loadRequestRoles(RequestData r, Element e) {
        NodeList nl = e.getElementsByTagName("security");
        for (int i = 0; i < nl.getLength(); i++) r.getRoleSet().add(((Element) nl.item(i)).getAttribute("role"));
    }

    void loadRequestForwards(RequestData r, Element e) throws JWebAppException {
        NodeList nl = e.getElementsByTagName("forward");
        for (int i = 0; i < nl.getLength(); i++) {
            String key = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("key")), forwardId = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("id"));
            if (key == null || forwardId == null) throw new JWebAppException("Forward can not have null key (=" + key + ") or Id (=" + forwardId + ")");
            r.getUrlDataMap().put(key, forwardId);
        }
        nl = e.getElementsByTagName("forwardReference");
        for (int i = 0; i < nl.getLength(); i++) {
            String key = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("key")), forwardId = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("idRef"));
            if (key == null || forwardId == null) throw new JWebAppException("Forward reference can not have null key (=" + key + ") or Id (=" + forwardId + ")");
            r.getUrlDataMap().put(key, forwardId);
        }
    }

    void loadRequestRedirects(RequestData r, Element e) throws JWebAppException {
        NodeList nl = e.getElementsByTagName("redirect");
        for (int i = 0; i < nl.getLength(); i++) {
            String key = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("key")), redirectId = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("id"));
            if (key == null || redirectId == null) throw new JWebAppException("Redirect can not have null key (=" + key + ") or Id (=" + redirectId + ")");
            r.getUrlDataMap().put(key, redirectId);
        }
        nl = e.getElementsByTagName("redirectReference");
        for (int i = 0; i < nl.getLength(); i++) {
            String key = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("key")), redirectId = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("idRef"));
            if (key == null || redirectId == null) throw new JWebAppException("Redirect reference can not have null key (=" + key + ") or Id (=" + redirectId + ")");
            r.getUrlDataMap().put(key, redirectId);
        }
    }

    void loadRequestValidation(RequestData r, Element e) throws JWebAppException {
        NodeList nl = e.getElementsByTagName("validation");
        for (int i = 0; i < nl.getLength(); i++) {
            String validationId = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("id")), parameterName = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("parameterName"));
            if (validationId == null || parameterName == null) throw new JWebAppException("Validation can not have null validation Id (=" + validationId + ") or parameter name (=" + parameterName + ")");
            r.getValidationList().add(parameterName + "|" + validationId);
        }
        nl = e.getElementsByTagName("validationReference");
        for (int i = 0; i < nl.getLength(); i++) {
            String validationId = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("idRef")), parameterName = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("parameterName"));
            if (validationId == null || parameterName == null) throw new JWebAppException("Validation reference can not have null validation Id (=" + validationId + ") or parameter name (=" + parameterName + ")");
            r.getValidationList().add(parameterName + "|" + validationId);
        }
    }

    void loadUrlForwardsRedirects(Element e, String xmlSearchElement, boolean isRedirect) throws JWebAppException {
        NodeList nl = e.getElementsByTagName(xmlSearchElement);
        for (int i = 0; i < nl.getLength(); i++) {
            String forwardId = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("id")), protocol = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("protocol")), url = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("url")), isRewrite = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("rewrite"));
            if (forwardId == null || url == null) throw new JWebAppException("Forwards, Redirects and Urls can not have null forward Id (=" + forwardId + ") or URL (=" + url + ")");
            _urlDataMap.put(forwardId, new UrlData(forwardId, protocol, url, isRedirect, new Boolean(isRewrite == null ? "true" : isRewrite).booleanValue()));
        }
    }

    void loadValidation(Element e) throws JWebAppException {
        NodeList nl = e.getElementsByTagName("validation");
        for (int i = 0; i < nl.getLength(); i++) {
            String validationId = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("id")), type = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("type")), arguments = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("arguments")), errorMessage = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("errorMessage"));
            if (validationId == null || type == null) throw new JWebAppException("Validation can not have null validation Id (=" + validationId + ")or type (=" + type + ")");
            _validationMap.put(validationId, new ValidationData(validationId, type, arguments, errorMessage));
            _validationList.add(new ValidationData(validationId, type, arguments, errorMessage));
        }
    }

    void loadMessages(Element e) {
        NodeList nl = e.getElementsByTagName("message");
        for (int i = 0; i < nl.getLength(); i++) {
            String messageId = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("id")), value = StringUtils.emptyToNull(((Element) nl.item(i)).getAttribute("value"));
            _messageMap.put(messageId, value);
        }
    }
}
