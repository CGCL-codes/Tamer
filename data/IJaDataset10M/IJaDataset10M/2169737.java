package com.aptana.ide.scripting;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import com.aptana.ide.core.IdeLog;
import com.aptana.ide.core.StringUtils;
import com.aptana.ide.server.http.HttpContentTypes;
import com.aptana.ide.server.http.HttpServer;
import com.aptana.ide.server.resources.IHttpResource;

/**
 * @author Kevin Lindsey
 */
public class ScriptingHttpResource implements IHttpResource {

    private static String RUNAT_ATTR = "runat";

    private static String SERVER = "server";

    private static String SERVER_ONLY = "server-only";

    private static String BOTH = "both";

    private File _file;

    private String _text;

    private String _type;

    private Document _document;

    private ScriptingHttpServer _server;

    /**
	 * Try to retrieve the document node from a previous load of this page
	 * 
	 * @return Document
	 */
    private Document getCachedDocument() {
        ScriptInfo info = this.getScriptInfo();
        Document result = null;
        if (info != null) {
            Object doc = info.getScope().get("document", info.getScope());
            if (doc instanceof NativeJavaObject) {
                NativeJavaObject nativeObject = (NativeJavaObject) doc;
                result = (Document) nativeObject.unwrap();
            }
        }
        return result;
    }

    /**
	 * setCachedDocument
	 */
    private void setCachedDocument() {
        ScriptInfo info = this.getScriptInfo();
        if (info != null) {
            Context.enter();
            Scriptable global = info.getScope();
            Object wrappedDocument = Context.javaToJS(this._document, global);
            global.put("document", global, wrappedDocument);
            global.put("location", global, info.getFile().getAbsolutePath());
            Context.exit();
        }
    }

    /**
	 * @see com.aptana.ide.server.resources.IHttpResource#getContentLength()
	 */
    public long getContentLength() {
        long result;
        if (this._text != null) {
            result = this._text.length();
        } else {
            result = this._file.length();
        }
        return result;
    }

    /**
	 * @see com.aptana.ide.server.resources.IHttpResource#getContentType()
	 */
    public String getContentType() {
        return this._type;
    }

    /**
	 * getScriptInfo
	 * 
	 * @return ScriptInfo
	 */
    private ScriptInfo getScriptInfo() {
        Global global = this._server.getGlobal();
        String id = global.getXrefId(this.getUri());
        ScriptInfo result = null;
        if (global.hasScriptInfo(id)) {
            result = global.getScriptInfo(id);
        }
        return result;
    }

    /**
	 * Determines if this resource is one that we should process
	 * 
	 * @return Returns true if this a resource we need to pre-process
	 */
    private boolean isScriptanaResource() {
        boolean result = false;
        int fileExtIndex = this._file.getName().lastIndexOf('.');
        if (fileExtIndex != -1) {
            String fileExtension = this._file.getName().substring(fileExtIndex);
            this._type = HttpContentTypes.getContentType(fileExtension);
            if (this._type.equals("application/xhtml+xml")) {
                this._type = "text/html";
                result = true;
            }
        }
        return result;
    }

    /**
	 * Get the URI for this resource
	 * 
	 * @return String
	 */
    public String getUri() {
        String result = StringUtils.EMPTY;
        try {
            result = this._file.getCanonicalPath();
        } catch (IOException e) {
            IdeLog.logError(ScriptingPlugin.getDefault(), Messages.ScriptingHttpResource_Error, e);
        }
        return result;
    }

    /**
	 * Create a new instance of FileHttpResource
	 * 
	 * @param file
	 */
    public ScriptingHttpResource(File file) {
        this._file = file;
        this._text = null;
        this._type = "text/plain";
    }

    /**
	 * getContentInputStream
	 * 
	 * @param server
	 * @return InputStream
	 */
    public InputStream getContentInputStream(HttpServer server) {
        InputStream result = null;
        this._server = (ScriptingHttpServer) server;
        try {
            if (this.isScriptanaResource()) {
                this._document = this.getCachedDocument();
                if (this._document == null) {
                    this.loadXHTML();
                } else {
                    ScriptInfo info = this.getScriptInfo();
                    if (info.needsRefresh()) {
                        this._server.removeScriptEnvironment(this.getUri());
                        this.loadXHTML();
                    } else {
                        Script[] scripts = info.getScripts();
                        Context cx = Context.enter();
                        for (int i = 0; i < scripts.length; i++) {
                            scripts[i].exec(cx, info.getScope());
                        }
                        Context.exit();
                    }
                }
                this._text = this.nodeToString(this._document);
                result = new ByteArrayInputStream(this._text.getBytes("UTF-8"));
            } else {
                result = new FileInputStream(this._file);
            }
        } catch (UnsupportedEncodingException e) {
            String message = StringUtils.format(Messages.ScriptingHttpResource_Processing_Error, this.getUri());
            IdeLog.logError(ScriptingPlugin.getDefault(), message, e);
        } catch (FileNotFoundException e) {
            String message = StringUtils.format(Messages.ScriptingHttpResource_Processing_Error, this.getUri());
            IdeLog.logError(ScriptingPlugin.getDefault(), message, e);
        } catch (TransformerFactoryConfigurationError e) {
            String message = StringUtils.format(Messages.ScriptingHttpResource_Processing_Error, this.getUri());
            IdeLog.logError(ScriptingPlugin.getDefault(), message, e);
        }
        return result;
    }

    /**
	 * Process an XHTML file
	 */
    private void loadXHTML() {
        this._server.createScriptEnvironment(this.getUri());
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this._document = builder.parse(this._file);
            this.setCachedDocument();
            this.processScriptElements();
        } catch (ParserConfigurationException e) {
            String message = StringUtils.format(Messages.ScriptingHttpResource_Processing_Error, this.getUri());
            IdeLog.logError(ScriptingPlugin.getDefault(), message, e);
        } catch (SAXException e) {
            String message = StringUtils.format(Messages.ScriptingHttpResource_Processing_Error, this.getUri());
            IdeLog.logError(ScriptingPlugin.getDefault(), message, e);
        } catch (IOException e) {
            String message = StringUtils.format(Messages.ScriptingHttpResource_Processing_Error, this.getUri());
            IdeLog.logError(ScriptingPlugin.getDefault(), message, e);
        } catch (TransformerFactoryConfigurationError e) {
            String message = StringUtils.format(Messages.ScriptingHttpResource_Processing_Error, this.getUri());
            IdeLog.logError(ScriptingPlugin.getDefault(), message, e);
        }
    }

    /**
	 * getNodeText
	 * 
	 * @param node
	 * @return String
	 */
    private String nodeToString(Node node) {
        String result = StringUtils.EMPTY;
        try {
            DOMSource source = new DOMSource(node);
            StringWriter writer = new StringWriter();
            StreamResult streamResult = new StreamResult(writer);
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, streamResult);
            result = writer.toString();
        } catch (TransformerConfigurationException e) {
            String message = StringUtils.format(Messages.ScriptingHttpResource_Processing_Error, this.getUri());
            IdeLog.logError(ScriptingPlugin.getDefault(), message, e);
        } catch (TransformerException e) {
            String message = StringUtils.format(Messages.ScriptingHttpResource_Processing_Error, this.getUri());
            IdeLog.logError(ScriptingPlugin.getDefault(), message, e);
        }
        return result;
    }

    /**
	 * processScriptElements
	 */
    private void processScriptElements() {
        NodeList scripts = this._document.getElementsByTagName("script");
        Element[] scriptElements = new Element[scripts.getLength()];
        ArrayList serverScripts = new ArrayList();
        ArrayList serverOnlyScripts = new ArrayList();
        ArrayList clientServerScripts = new ArrayList();
        for (int i = 0; i < scripts.getLength(); i++) {
            scriptElements[i] = (Element) scripts.item(i);
        }
        for (int i = 0; i < scriptElements.length; i++) {
            Element script = scriptElements[i];
            if (script.hasAttribute(RUNAT_ATTR)) {
                String runAt = script.getAttribute(RUNAT_ATTR);
                if (runAt.equals(SERVER)) {
                    serverScripts.add(script);
                } else if (runAt.equals(SERVER_ONLY)) {
                    serverOnlyScripts.add(script);
                } else if (runAt.equals(BOTH)) {
                    clientServerScripts.add(script);
                }
            }
        }
        HashSet baselineFunctions = this.getFunctionNames();
        HashSet bothFunctions = new HashSet();
        HashSet serverFunctions = new HashSet();
        for (int i = 0; i < clientServerScripts.size(); i++) {
            Element script = (Element) clientServerScripts.get(i);
            this.runScript(script);
            script.removeAttribute(RUNAT_ATTR);
            bothFunctions.addAll(this.getFunctionNames());
        }
        for (int i = 0; i < serverScripts.size(); i++) {
            Element script = (Element) serverScripts.get(i);
            this.runScript(script);
            script.getParentNode().removeChild(script);
            serverFunctions.addAll(this.getFunctionNames());
        }
        for (int i = 0; i < serverOnlyScripts.size(); i++) {
            Element script = (Element) serverOnlyScripts.get(i);
            this.runScript(script);
            script.getParentNode().removeChild(script);
        }
        bothFunctions.removeAll(baselineFunctions);
        serverFunctions.removeAll(baselineFunctions);
        serverFunctions.removeAll(bothFunctions);
        Element wrapper = this.createWrappers((String[]) serverFunctions.toArray(new String[0]));
        if (wrapper != null) {
            Element script = this._document.createElement("script");
            script.setAttribute("type", "text/javascript");
            script.setAttribute("src", "/aptana/libs/xmlhttp.js");
            NodeList heads = this._document.getElementsByTagName("head");
            Element head;
            if (heads.getLength() > 0) {
                head = (Element) heads.item(0);
            } else {
                Element html = this._document.getDocumentElement();
                head = this._document.createElement("head");
                if (html.hasChildNodes()) {
                    html.insertBefore(head, html.getFirstChild());
                } else {
                    html.appendChild(head);
                }
            }
            if (head.hasChildNodes()) {
                head.insertBefore(wrapper, head.getFirstChild());
            } else {
                head.appendChild(wrapper);
            }
            head.insertBefore(script, wrapper);
        }
    }

    /**
	 * createWrappers
	 */
    private Element createWrappers(String[] names) {
        Element result = null;
        if (names.length > 0) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < names.length; i++) {
                buffer.append("\n");
                buffer.append(this.createFunctionWrapper(names[i]));
            }
            buffer.append("\n");
            String genCode = buffer.toString();
            result = this._document.createElement("script");
            Text wrapperCode = this._document.createTextNode(genCode);
            result.setAttribute("type", "text/javascript");
            result.appendChild(wrapperCode);
        }
        return result;
    }

    /**
	 * @param name
	 * @return String
	 */
    private String createFunctionWrapper(String name) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("function ").append(name).append("() {").append(" ");
        buffer.append("return ___invokeFunction.call(null, \"").append(name).append("\", arguments);").append(" ");
        buffer.append("}");
        return buffer.toString();
    }

    /**
	 * getServerFunctionNames
	 * 
	 * @return HashSet
	 */
    private HashSet getFunctionNames() {
        ScriptInfo info = this.getScriptInfo();
        ScriptableObject global = (ScriptableObject) info.getScope();
        Object[] ids = global.getIds();
        HashSet names = new HashSet();
        for (int i = 0; i < ids.length; i++) {
            Object idObject = ids[i];
            if (idObject instanceof String) {
                String id = (String) idObject;
                int attrs = global.getAttributes(id);
                boolean readonly = (attrs & ScriptableObject.READONLY) == ScriptableObject.READONLY;
                if (readonly == false) {
                    Object value = global.get(id, global);
                    if (value instanceof Callable) {
                        names.add(id);
                    }
                }
            }
        }
        return names;
    }

    /**
	 * runScript
	 * 
	 * @param script
	 */
    private void runScript(Element script) {
        String code = null;
        if (script.hasAttribute("src")) {
            String filename = script.getAttribute("src");
            File file = new File(filename);
            if (file.exists() == false) {
                String parentDirectory = this._file.getParent();
                String candidate = parentDirectory + File.separator + filename;
                file = new File(candidate);
            }
            if (file.exists() == false && filename.startsWith("/")) {
                String rootServerPath = _server.getRootPath();
                String candidate = rootServerPath + filename;
                file = new File(candidate);
            }
            if (file.exists()) {
                try {
                    FileInputStream input = new FileInputStream(file);
                    code = FileUtilities.getStreamText(input);
                } catch (FileNotFoundException e) {
                    IdeLog.logError(ScriptingPlugin.getDefault(), Messages.ScriptingHttpResource_Error, e);
                }
            } else {
                String message = StringUtils.format(Messages.ScriptingHttpResource_File_Does_Not_Exist, filename);
                IdeLog.logError(ScriptingPlugin.getDefault(), message);
            }
        } else {
            StringBuffer codePieces = new StringBuffer();
            Node child = script.getFirstChild();
            while (child != null) {
                codePieces.append(child.getNodeValue());
                child = child.getNextSibling();
            }
            code = codePieces.toString();
        }
        if (code != null && code.length() > 0) {
            this.runScript(code);
        }
    }

    /**
	 * runScript
	 * 
	 * @param script
	 */
    private void runScript(String script) {
        this._server.include(this.getUri(), script);
    }
}
