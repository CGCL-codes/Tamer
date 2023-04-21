package org.jaffa.presentation.portlet;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.jaffa.presentation.portlet.component.Component;
import org.jaffa.presentation.portlet.session.UserSession;
import org.jaffa.session.ContextManagerFactory;
import org.jaffa.util.StringHelper;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/** This is a helper class for marshalling the HistoryNavList into XML and vice versa.
 *
 * @author  GautamJ
 */
public class HistoryNav {

    private static Logger log = Logger.getLogger(HistoryNav.class);

    /** Constant to denote the 'historyNav' parameter.*/
    public static final String HISTORY_NAV_PARAMETER = "historyNav";

    private static final String XML_FORM_KEYS = "form-keys";

    private static final String XML_FORM_KEYS_START = '<' + XML_FORM_KEYS + '>';

    private static final String XML_FORM_KEYS_END = "</" + XML_FORM_KEYS + '>';

    private static final String XML_FORM_KEY = "form-key";

    private static final String XML_FORM_KEY_START = '<' + XML_FORM_KEY + '>';

    private static final String XML_FORM_KEY_END = "</" + XML_FORM_KEY + '>';

    private static final String XML_FORM_NAME = "form-name";

    private static final String XML_FORM_NAME_START = '<' + XML_FORM_NAME + '>';

    private static final String XML_FORM_NAME_END = "</" + XML_FORM_NAME + '>';

    private static final String XML_COMPONENT_ID = "component-id";

    private static final String XML_COMPONENT_ID_START = '<' + XML_COMPONENT_ID + '>';

    private static final String XML_COMPONENT_ID_END = "</" + XML_COMPONENT_ID + '>';

    private static final String XML_TITLE = "title";

    private static final String XML_TITLE_START = '<' + XML_TITLE + '>';

    private static final String XML_TITLE_END = "</" + XML_TITLE + '>';

    /** This will marshal the input List into XML. The List is assumed to contain FormKey objects.
     * @param historyNavList The List of FormKey objects.
     * @return The XML representation of the historyNavList.
     */
    public static String encode(List historyNavList) {
        StringBuffer buf = new StringBuffer();
        if (historyNavList != null && historyNavList.size() > 0) {
            buf.append(XML_FORM_KEYS_START);
            for (Iterator itr = historyNavList.iterator(); itr.hasNext(); ) {
                FormKey fk = (FormKey) itr.next();
                buf.append(XML_FORM_KEY_START);
                if (fk.getFormName() != null) {
                    buf.append(XML_FORM_NAME_START);
                    buf.append(fk.getFormName());
                    buf.append(XML_FORM_NAME_END);
                }
                if (fk.getComponentId() != null) {
                    buf.append(XML_COMPONENT_ID_START);
                    buf.append(fk.getComponentId());
                    buf.append(XML_COMPONENT_ID_END);
                }
                if (fk.getTitle() != null) {
                    buf.append(XML_TITLE_START);
                    buf.append(fk.getTitle());
                    buf.append(XML_TITLE_END);
                }
                buf.append(XML_FORM_KEY_END);
            }
            buf.append(XML_FORM_KEYS_END);
        }
        return buf.toString();
    }

    /** This will unmarshal the input XML into a List of FormKey objects.
     * @param historyNavXml The XML representation of the historyNavList.
     * @return The List of FormKey objects.
     */
    public static List decode(String historyNavXml) {
        List historyNavList = null;
        try {
            historyNavXml = StringHelper.replace(historyNavXml, "&", "&amp;");
            if (log.isDebugEnabled()) log.debug("Unmarshalling the historyNavXml " + historyNavXml);
            XMLReader reader = XMLReaderFactory.createXMLReader();
            HistoryNavHandler handler = new HistoryNavHandler();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new BufferedReader(new StringReader(historyNavXml))));
            historyNavList = handler.getHistoryNavList();
        } catch (Exception e) {
            if (log.isInfoEnabled()) log.info("Error while parsing the historyNavXml " + historyNavXml, e);
        }
        if (log.isDebugEnabled()) log.debug("Unmarshalled List: " + historyNavList);
        return historyNavList;
    }

    /** This will initialze a List with the 'jaffa_home' link and set the 'historyNav' attribute on the input request stream with the new List.
     * @param request The request stream
     * @return the newly initialized List.
     */
    public static List initializeHistoryNav(HttpServletRequest request) {
        try {
            return initializeHistoryNav(request, null);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /** This will initialze a List with the 'jaffa_home' link and set the 'historyNav' attribute on the input request stream with the new List.
     * It will also add a link for the finalUrl, the title for which should be passed in as the value for the paramter "desktopName" or "title", within the finalUrl.
     * @param request The request stream
     * @param finalUrl The final Url encoded in UTF-8 format.
     * @throws UnsupportedEncodingException if the UTF-8 format is not supported. should never happen.
     * @return the newly initialized List.
     */
    public static List initializeHistoryNav(HttpServletRequest request, String finalUrl) throws UnsupportedEncodingException {
        List historyNavList = new ArrayList();
        FormKey home = new FormKey("jaffa_home", null);
        home.setTitle("[title.Jaffa.HistoryNav.Home]");
        historyNavList.add(home);
        if (finalUrl != null && !finalUrl.equals(home.getFormName())) {
            FormKey desktop = new FormKey(finalUrl, null);
            desktop.setTitle(determineDesktopName(finalUrl));
            if (desktop.getTitle() != null) {
                if (desktop.getTitle().equals(home.getTitle())) log.debug("Desktop has same name as HOME, don't add it"); else historyNavList.add(desktop);
            } else log.debug("Desktop Link not added to history - No Title available");
        }
        request.setAttribute(HistoryNav.HISTORY_NAV_PARAMETER, historyNavList);
        return historyNavList;
    }

    /** This will search the request stream for the attribute 'historyNav'. If not found, it'll search for the parameter 'historyNav'.
     * This parameter is expected to be in XML format and will be decoded into a List
     * @param request The request stream.
     * @return The List containing the links for the HistoryNav.
     */
    public static List obtainHistoryNav(HttpServletRequest request) {
        List historyNavList = null;
        historyNavList = (List) request.getAttribute(HistoryNav.HISTORY_NAV_PARAMETER);
        if (historyNavList == null) {
            String historyNavXml = request.getParameter(HistoryNav.HISTORY_NAV_PARAMETER);
            if (historyNavXml != null) {
                historyNavList = decode(historyNavXml);
            }
        }
        return historyNavList;
    }

    /** This will add the input FormKey to the historyNav.
     * If the historyNav didn't exist, then one will be initialized.
     * If the input FormKey already existed on the historyNav, then all subsequent FormKeys will be removed from the list.
     * Also, all the subsequent components will be closed.
     * @param request The request stream.
     * @param fk The FormKey to add.
     */
    public static void addFormKeyToHistoryNav(HttpServletRequest request, FormKey fk) {
        List historyNavList = obtainHistoryNav(request);
        if (historyNavList == null) {
            historyNavList = initializeHistoryNav(request);
            historyNavList.add(fk);
        } else if (!historyNavList.contains(fk)) {
            historyNavList.add(fk);
        } else {
            int indexOfFk = historyNavList.indexOf(fk);
            for (int i = indexOfFk + 1; i < historyNavList.size(); i++) {
                FormKey subsequentFk = (FormKey) historyNavList.get(i);
                String subsequentComponentId = subsequentFk.getComponentId();
                if (subsequentComponentId != null && !subsequentComponentId.equals(fk.getComponentId())) {
                    Component subsequentComponent = UserSession.getUserSession(request).getComponent(subsequentComponentId);
                    if (subsequentComponent != null && subsequentComponent.isActive()) subsequentComponent.quit();
                }
            }
            if (indexOfFk + 1 < historyNavList.size()) historyNavList.subList(indexOfFk + 1, historyNavList.size()).clear();
        }
        for (Iterator i = historyNavList.iterator(); i.hasNext(); ) {
            FormKey aFormKey = (FormKey) i.next();
            String compId = aFormKey.getComponentId();
            if (compId != null) {
                Component comp = UserSession.getUserSession(request).getComponent(compId);
                if (comp == null || !comp.isActive()) i.remove();
            }
        }
        request.setAttribute(HistoryNav.HISTORY_NAV_PARAMETER, historyNavList);
    }

    /** Return the value of 'desktopName' or 'title' passed as a parameter within formName
     */
    private static String determineDesktopName(String formName) throws UnsupportedEncodingException {
        String desktopName = null;
        formName = URLDecoder.decode(formName, "UTF-8");
        int i = formName.indexOf('?');
        if (i > -1) {
            if (formName.length() > (i + 1)) {
                String parameterString = formName.substring(i + 1);
                StringTokenizer tokenizer = new StringTokenizer(parameterString, "=&");
                String attributeName = null;
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    if (attributeName == null) {
                        attributeName = token;
                    } else {
                        if (attributeName.equals("desktopName") || attributeName.equals("title")) {
                            desktopName = token;
                            break;
                        }
                        attributeName = null;
                    }
                }
            }
        }
        return desktopName;
    }

    /** A private class used for SAX parsing the encoded HistoryNav */
    private static class HistoryNavHandler extends DefaultHandler {

        private List historyNavList = new ArrayList();

        private Map currentElements = new HashMap();

        private String currentElement = null;

        /** Receive notification of the start of an element.
         * @param uri The uri.
         * @param sName The local name (without prefix), or the empty string if Namespace processing is not being performed.
         * @param qName The qualified name (with prefix), or the empty string if qualified names are not available.
         * @param atts The specified or defaulted attributes
         */
        public void startElement(String uri, String sName, String qName, Attributes atts) {
            if (sName.equals(XML_FORM_NAME)) {
                currentElement = XML_FORM_NAME;
            } else if (sName.equals(XML_COMPONENT_ID)) {
                currentElement = XML_COMPONENT_ID;
            } else if (sName.equals(XML_TITLE)) {
                currentElement = XML_TITLE;
            }
        }

        /** Receive notification of character data inside an element.
         * @param ch The characters.
         * @param start The start position in the character array.
         * @param length The number of characters to use from the character array.
         */
        public void characters(char[] ch, int start, int length) {
            String currentString = (String) currentElements.get(currentElement);
            String stringToAdd = new String(ch, start, length);
            if (currentString == null) {
                currentElements.put(currentElement, stringToAdd);
            } else {
                currentElements.put(currentElement, currentString + stringToAdd);
            }
        }

        /** Receive notification of the end of an element.
         * @param uri The uri.
         * @param sName The local name (without prefix), or the empty string if Namespace processing is not being performed.
         * @param qName The qualified name (with prefix), or the empty string if qualified names are not available.
         */
        public void endElement(String uri, String sName, String qName) {
            if (sName.equals(XML_FORM_KEY)) {
                FormKey fk = new FormKey((String) currentElements.get(XML_FORM_NAME), (String) currentElements.get(XML_COMPONENT_ID));
                fk.setTitle((String) currentElements.get(XML_TITLE));
                historyNavList.add(fk);
                currentElements.clear();
            }
        }

        public List getHistoryNavList() {
            return historyNavList;
        }
    }

    /** See if the request with the current thread was posted with history, or history has been
     * set as a request attribute.
     * This is used by the Component to determine if the ReturnToFormKey is allowed to be set, or
     * if there is no history, then the component should close the browser on exit
     * if there is no request, or no
     */
    public static boolean threadHasHistory() {
        HttpServletRequest req = (HttpServletRequest) ContextManagerFactory.instance().getProperty("request");
        if (req != null) {
            String historyNavXml = req.getParameter(HistoryNav.HISTORY_NAV_PARAMETER);
            if (historyNavXml != null && historyNavXml.length() > 0) return true;
            if (req.getAttribute(HistoryNav.HISTORY_NAV_PARAMETER) != null) return true;
        }
        return false;
    }
}
