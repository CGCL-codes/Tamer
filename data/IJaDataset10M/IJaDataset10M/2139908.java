package net.openkoncept.vroom.util;

import net.openkoncept.vroom.bean.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * This class is designed with intension to be used only by the framework.
 * </p>
 *
 * @author Farrukh Ijaz (ijazfx)
 */
public class VroomUtilities {

    /**
     * <p>
     * This method returns the path where the original webpage has/will been/be
     * cached after preprocessing.
     * </p>
     *
     * @param path Actual path of the webpage.
     * @return Cached path
     */
    public static String getCachedPath(String path) {
        String cachedPath = path + ".cached";
        return cachedPath;
    }

    /**
     * <p>
     * This method is used to load webpage source code.
     * </p>
     *
     * @param pageFile Path where the page is located.
     */
    public static StringBuffer loadPageSourceCode(File pageFile) throws IOException {
        BufferedReader br = null;
        BufferedWriter bw = null;
        StringBuffer sourceCode = new StringBuffer();
        br = new BufferedReader(new FileReader(pageFile));
        StringWriter sw = new StringWriter();
        bw = new BufferedWriter(sw);
        String line = null;
        do {
            line = br.readLine();
            if (line == null) {
                break;
            }
            bw.append(line);
            bw.newLine();
        } while (line != null);
        bw.flush();
        br.close();
        bw.close();
        sourceCode = sw.getBuffer();
        return sourceCode;
    }

    /**
     * <p>
     * The method locates the document element. All documents are defined in an
     * xml file normally called "vroom-config.xml". The schema for this document
     * is defined in vroom-config.xsd.
     * </p>
     *
     * @param vc       instance of VroomConfig (it is normally unmarshalled from
     *                 vroom-config.xml)
     * @param pagePath Path of the web page.
     * @return Ths instance of DocumentComplexType or null.
     */
    @SuppressWarnings(value = "unchecked")
    public static Document loadPageProcessor(VroomConfig vc, String pagePath) {
        Document document = vc.getDocumentMap().get(pagePath);
        for (Document doc : vc.getDocumentMap().values()) {
            if (pagePath.equals(doc.getPath())) {
                document = doc;
                break;
            }
            String[] paths = doc.getPath().split("[|]");
            for (String path : paths) {
                if (pagePath.equals(path)) {
                    document = doc;
                    break;
                }
            }
        }
        return document;
    }

    /**
     * <p>
     * This method is used to process page source code using the document defined
     * for the page.
     * </p>
     */
    public static StringBuffer processPageSourceCode(StringBuffer sourceCode, Document document, HttpServletRequest request) {
        StringBuffer temp = new StringBuffer(sourceCode.toString());
        int formStartIndex;
        int formEndIndex;
        int replaceStartIndex;
        int replaceEndIndex;
        String contextPath = request.getContextPath();
        int index = temp.indexOf("</head>");
        if (index != -1) {
            String scriptTag = "\n<script type=\"text/javascript\" src=\"" + contextPath + "/vroom?resource=vroom.js" + "\"></script>\n";
            temp.insert(index, scriptTag);
        }
        for (Form form : document.getFormMap().values()) {
            String id = "id=\"" + form.getId() + "\"";
            formStartIndex = temp.indexOf(id);
            if (formStartIndex == -1) {
                String name = "name=\"" + form.getId() + "\"";
                formStartIndex = temp.indexOf(name);
                if (formStartIndex != -1) {
                    temp.insert(formStartIndex, id + " ");
                }
            }
            if (formStartIndex != -1) {
                formEndIndex = temp.indexOf(">", formStartIndex);
                formStartIndex = temp.substring(0, formEndIndex).lastIndexOf("<form");
                int eventStartIndex;
                int eventEndIndex;
                for (Event event : form.getEventMap().values()) {
                    String eventType = event.getEventType();
                    eventStartIndex = temp.indexOf(eventType, formStartIndex);
                    if (eventStartIndex == -1 || eventStartIndex > formEndIndex) {
                        temp.insert(formEndIndex, " " + eventType + "=\"#\"");
                        eventStartIndex = temp.indexOf(eventType, formStartIndex);
                        formEndIndex = temp.indexOf(">", formStartIndex);
                    }
                    if (eventStartIndex > -1 && eventStartIndex < formEndIndex) {
                        replaceStartIndex = temp.indexOf("\"", eventStartIndex);
                        replaceStartIndex = (replaceStartIndex > -1) ? replaceStartIndex + 1 : replaceStartIndex;
                        replaceEndIndex = temp.indexOf("\"", replaceStartIndex);
                        temp.delete(replaceStartIndex, replaceEndIndex);
                        String url = "path=" + document.getPath() + "&call=" + form.getId() + "." + event.getMethod();
                        String text = "javascript:" + event.getCallType() + "('" + form.getId() + "', '" + contextPath + "', '" + url + "'";
                        text += ", '";
                        for (Update update : event.getUpdateMap().values()) {
                            if (event.getCallType().equals("vroomAjaxSyncCall") || (event.getCallType().equals("vroomAjaxAsyncCall"))) {
                                String updateText = "";
                                if (update != null) {
                                    if (update.getTag() != null && update.getTag().trim().length() > 0) {
                                        updateText = update.getTag() + "|" + update.getAttribute() + ":" + update.getIndex() + "&";
                                    } else {
                                        updateText = update.getId() + "|" + update.getAttribute() + ":" + update.getIndex() + "&";
                                    }
                                }
                                text += updateText;
                            }
                        }
                        text += "');";
                        temp.insert(replaceStartIndex, text);
                    }
                    formEndIndex = temp.indexOf(">", formStartIndex);
                }
                int elemStartIndex;
                int elemEndIndex;
                for (Element elem : form.getElementMap().values()) {
                    id = "id=\"" + elem.getId() + "\"";
                    elemStartIndex = temp.indexOf(id);
                    if (elemStartIndex == -1) {
                        String name = "name=\"" + elem.getId() + "\"";
                        elemStartIndex = temp.indexOf(name);
                        if (elemStartIndex != -1) {
                            temp.insert(elemStartIndex, id + " ");
                        }
                    }
                    if (elemStartIndex != -1) {
                        elemEndIndex = temp.indexOf(">", elemStartIndex);
                        for (Event event : elem.getEventMap().values()) {
                            String eventType = event.getEventType();
                            eventStartIndex = temp.indexOf(eventType, elemStartIndex);
                            if (eventStartIndex == -1 || eventStartIndex > elemEndIndex) {
                                if (temp.charAt(elemEndIndex - 1) == '/') {
                                    elemEndIndex--;
                                }
                                temp.insert(elemEndIndex, " " + eventType + "=\"#\"");
                                eventStartIndex = temp.indexOf(eventType, elemStartIndex);
                                elemEndIndex = temp.indexOf(">", elemStartIndex);
                            }
                            if (eventStartIndex > -1 && eventStartIndex < elemEndIndex) {
                                replaceStartIndex = temp.indexOf("\"", eventStartIndex);
                                replaceStartIndex = (replaceStartIndex > -1) ? replaceStartIndex + 1 : replaceStartIndex;
                                replaceEndIndex = temp.indexOf("\"", replaceStartIndex);
                                temp.delete(replaceStartIndex, replaceEndIndex);
                                String url = "path=" + document.getPath() + "&call=" + elem.getId() + "." + event.getMethod();
                                String text = "javascript:" + event.getCallType() + "('" + form.getId() + "." + elem.getId() + "', '" + contextPath + "', '" + url + "'";
                                text += ", '";
                                for (Update update : event.getUpdateMap().values()) {
                                    if (event.getCallType().equals("vroomAjaxSyncCall") || (event.getCallType().equals("vroomAjaxAsyncCall"))) {
                                        String updateText = "";
                                        if (update != null) {
                                            if (update.getTag() != null && update.getTag().trim().length() > 0) {
                                                updateText = update.getTag() + "|" + update.getAttribute() + ":" + update.getIndex() + "&";
                                            } else {
                                                updateText = update.getId() + "|" + update.getAttribute() + ":" + update.getIndex() + "&";
                                            }
                                        }
                                        text += updateText;
                                    }
                                }
                                text += "');";
                                temp.insert(replaceStartIndex, text);
                            }
                            elemEndIndex = temp.indexOf(">", elemStartIndex);
                        }
                    }
                }
            }
        }
        int bodyStartIndex;
        int bodyEndIndex;
        int eventStartIndex;
        int eventEndIndex;
        StringBuffer sbAfterLoadCode = null;
        String body = "<body";
        bodyStartIndex = temp.indexOf(body);
        if (bodyStartIndex != -1) {
            bodyEndIndex = temp.indexOf(">", bodyStartIndex);
            for (Event event : document.getEventMap().values()) {
                String eventType = event.getEventType();
                if ("onLoad".equals(eventType) || "onUnload".equals(eventType)) {
                    eventStartIndex = temp.indexOf(eventType, bodyStartIndex);
                    if (eventStartIndex == -1 || eventStartIndex > bodyEndIndex) {
                        if (temp.charAt(bodyEndIndex - 1) == '/') {
                            bodyEndIndex--;
                        }
                        temp.insert(bodyEndIndex, " " + eventType + "=\"#\"");
                        eventStartIndex = temp.indexOf(eventType, bodyStartIndex);
                        bodyEndIndex = temp.indexOf(">", bodyStartIndex);
                    }
                    if (eventStartIndex > -1 && eventStartIndex < bodyEndIndex) {
                        replaceStartIndex = temp.indexOf("\"", eventStartIndex);
                        replaceStartIndex = (replaceStartIndex > -1) ? replaceStartIndex + 1 : replaceStartIndex;
                        replaceEndIndex = temp.indexOf("\"", replaceStartIndex);
                        temp.delete(replaceStartIndex, replaceEndIndex);
                        String url = "path=" + document.getPath() + "&call=" + event.getMethod();
                        String text = "javascript:" + event.getCallType() + "('', '" + contextPath + "', '" + url + "'";
                        text += ", '";
                        for (Update update : event.getUpdateMap().values()) {
                            if (event.getCallType().equals("vroomAjaxSyncCall") || (event.getCallType().equals("vroomAjaxAsyncCall"))) {
                                String updateText = "";
                                if (update != null) {
                                    if (update.getTag() != null && update.getTag().trim().length() > 0) {
                                        updateText = update.getTag() + "|" + update.getAttribute() + ":" + update.getIndex() + "&";
                                    } else {
                                        updateText = update.getId() + "|" + update.getAttribute() + ":" + update.getIndex() + "&";
                                    }
                                }
                                text += updateText;
                            }
                        }
                        text += "');";
                        temp.insert(replaceStartIndex, text);
                    }
                    bodyEndIndex = temp.indexOf(">", bodyStartIndex);
                }
            }
        }
        int elemStartIndex;
        int elemEndIndex;
        for (Element elem : document.getElementMap().values()) {
            String id = "id=\"" + elem.getId() + "\"";
            elemStartIndex = temp.indexOf(id);
            if (elemStartIndex == -1) {
                String name = "name=\"" + elem.getId() + "\"";
                elemStartIndex = temp.indexOf(name);
                if (elemStartIndex != -1) {
                    temp.insert(elemStartIndex, id + " ");
                }
            }
            if (elemStartIndex != -1) {
                elemEndIndex = temp.indexOf(">", elemStartIndex);
                for (Event event : elem.getEventMap().values()) {
                    String eventType = event.getEventType();
                    eventStartIndex = temp.indexOf(eventType, elemStartIndex);
                    if (eventStartIndex == -1 || eventStartIndex > elemEndIndex) {
                        if (temp.charAt(elemEndIndex - 1) == '/') {
                            elemEndIndex--;
                        }
                        temp.insert(elemEndIndex, " " + eventType + "=\"#\"");
                        eventStartIndex = temp.indexOf(eventType, elemStartIndex);
                        elemEndIndex = temp.indexOf(">", elemStartIndex);
                    }
                    if (eventStartIndex > -1 && eventStartIndex < elemEndIndex) {
                        replaceStartIndex = temp.indexOf("\"", eventStartIndex);
                        replaceStartIndex = (replaceStartIndex > -1) ? replaceStartIndex + 1 : replaceStartIndex;
                        replaceEndIndex = temp.indexOf("\"", replaceStartIndex);
                        temp.delete(replaceStartIndex, replaceEndIndex);
                        String url = "path=" + document.getPath() + "&call=" + elem.getId() + "." + event.getMethod();
                        String text = "javascript:" + event.getCallType() + "('" + elem.getId() + "', '" + contextPath + "', '" + url + "'";
                        text += ", '";
                        for (Update update : event.getUpdateMap().values()) {
                            if (event.getCallType().equals("vroomAjaxSyncCall") || (event.getCallType().equals("vroomAjaxAsyncCall"))) {
                                String updateText = "";
                                if (update != null) {
                                    if (update.getTag() != null && update.getTag().trim().length() > 0) {
                                        updateText = update.getTag() + "|" + update.getAttribute() + ":" + update.getIndex() + "&";
                                    } else {
                                        updateText = update.getId() + "|" + update.getAttribute() + ":" + update.getIndex() + "&";
                                    }
                                }
                                text += updateText;
                            }
                        }
                        text += "');";
                        temp.insert(replaceStartIndex, text);
                    }
                    elemEndIndex = temp.indexOf(">", elemStartIndex);
                }
            }
        }
        for (Event event : document.getEventMap().values()) {
            String eventType = event.getEventType();
            if ("onAfterLoad".equals(eventType)) {
                String url = "path=" + document.getPath() + "&call=" + event.getMethod();
                sbAfterLoadCode = new StringBuffer("\n<script type=\"text/javascript\">");
                sbAfterLoadCode.append("\nfunction callAfterLoad() {");
                String text = "\n" + event.getCallType() + "('', '" + contextPath + "', '" + url + "'";
                text += ", '";
                for (Update update : event.getUpdateMap().values()) {
                    if (event.getCallType().equals("vroomAjaxSyncCall") || (event.getCallType().equals("vroomAjaxAsyncCall"))) {
                        String updateText = "";
                        if (update != null) {
                            if (update.getTag() != null && update.getTag().trim().length() > 0) {
                                updateText = update.getTag() + "|" + update.getAttribute() + ":" + update.getIndex() + "&";
                            } else {
                                updateText = update.getId() + "|" + update.getAttribute() + ":" + update.getIndex() + "&";
                            }
                        }
                        text += updateText;
                    }
                }
                text += "');";
                sbAfterLoadCode.append(text);
                sbAfterLoadCode.append("\n}");
                sbAfterLoadCode.append("\ncallAfterLoad();");
                sbAfterLoadCode.append("\n</script>\n");
                int htmlEndIndex = temp.indexOf("</html>");
                if (htmlEndIndex != -1) {
                    temp.insert(htmlEndIndex, sbAfterLoadCode.toString());
                }
                break;
            }
        }
        int htmlEndIndex = temp.indexOf("</html>");
        if (htmlEndIndex != -1) {
            HttpSession session = request.getSession(true);
            String vroomSession = "\n<input type=\"hidden\" id=\"vroomSessionId\" value=\"" + session.getId() + "\"/>\n";
            temp.insert(htmlEndIndex, vroomSession);
        }
        return temp;
    }

    private static String getProcessorPath(String pagePath) {
        String path = (pagePath == null) ? "" : pagePath;
        String[] parts = path.split("/");
        String pageName = parts[parts.length - 1];
        String processorName = pageName.toUpperCase().charAt(0) + pageName.substring(1, pageName.indexOf('.'));
        path = path.substring(0, path.lastIndexOf('/')).replace('/', '.') + "." + processorName;
        if (path.charAt(0) == '.') {
            path = path.substring(1);
        }
        return path;
    }

    public static String normalize(String input) {
        String output = null;
        try {
            output = input.replaceAll("\"", "\\x22");
            output = output.replaceAll("\\(", "\\x28");
            output = output.replaceAll("\\)", "\\x29");
            output = output.replaceAll("\\[", "\\x5B");
            output = output.replaceAll("\\]", "\\x5D");
            output = output.replaceAll("\\{", "\\x7B");
            output = output.replaceAll("\\}", "\\x7D");
        } catch (Exception ex) {
            Logger.getLogger(VroomUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return output;
        }
    }

    public static String toUnicode(String input) {
        return input;
    }

    public static String encodeUTF8(String input) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(VroomUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return encoded;
        }
    }
}
