package org.exist.eclipse.xquery.ui.internal.completion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.eclipse.core.runtime.Path;
import org.exist.eclipse.xquery.ui.completion.IXQueryMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Contains all methods of a given library for code completion and xquery
 * documentation/navigation.
 * 
 * @author oec
 * @version $Id: $
 */
public class LibDocument {

    private static String getKey(String methodName, int parameterCount) {
        return methodName + parameterCount;
    }

    private final String _prefix;

    private final Path _path;

    private String _document;

    private List<IXQueryMethod> _methods;

    /**
	 * From method key (name + parameter size) to line number in generated
	 * content.
	 */
    private Map<String, Integer> _index;

    private final String _namespaceUri;

    public LibDocument(String prefix, String namespaceUri, Path path) {
        _prefix = prefix;
        _namespaceUri = namespaceUri;
        _path = path;
    }

    public String getContent() {
        if (_document == null) {
            _document = generateContent();
        }
        return _document;
    }

    private String generateContent() {
        try {
            List<IXQueryMethod> all = getMethods();
            if (all.isEmpty()) {
                return "";
            }
            Map<String, Integer> index = getIndex();
            index.clear();
            String s = "module namespace " + getPrefix() + "='" + getNamespaceUri() + "';\n\n";
            int line = 2;
            IXQueryMethod last = all.get(all.size() - 1);
            for (IXQueryMethod it : all) {
                String comment = it.getComment();
                if (!comment.isEmpty()) {
                    s += "(:\n";
                    line++;
                    BufferedReader reader = new BufferedReader(new StringReader(comment));
                    String l;
                    while ((l = reader.readLine()) != null) {
                        s += " : " + l + "\n";
                        line++;
                    }
                    s += " :)\n";
                    line++;
                }
                s += it.getSignature();
                index.put(getKey(it.getName(), it.getParameterNames().length), Integer.valueOf(line));
                if (!it.equals(last)) {
                    s += "\n\n";
                    line += 2;
                }
            }
            return s;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<IXQueryMethod> getMethods() {
        if (_methods == null) {
            _methods = new ArrayList<IXQueryMethod>();
        }
        return _methods;
    }

    protected Map<String, Integer> getIndex() {
        if (_index == null) {
            _index = new HashMap<String, Integer>();
        }
        return _index;
    }

    public void parseMethods(InputStream inputStream) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            List<IXQueryMethod> methods = getMethods();
            methods.clear();
            for (Element fun : getElements(getElement(doc.getDocumentElement(), "functions"), "function")) {
                methods.add(new XQueryMethod(getPrefix() + ':' + getElement(fun, "name").getTextContent(), getElement(fun, "signature").getTextContent(), getComment(getElement(fun, "comment"))));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getComment(Element element) {
        StringBuilder result = new StringBuilder();
        for (Element e : getElements(element, null)) {
            if (result.length() > 0) {
                result.append('\n');
            }
            String nodeName = e.getNodeName();
            if (!"description".equals(nodeName)) {
                result.append(nodeName);
                result.append(' ');
            }
            result.append(e.getTextContent());
        }
        return result.toString();
    }

    protected String getPrefix() {
        return _prefix;
    }

    public void fillMethods(String prefix, List<IXQueryMethod> result) {
        for (IXQueryMethod method : getMethods()) {
            if (method.getName().startsWith(prefix)) {
                result.add(method);
            }
        }
    }

    public Path getPath() {
        return _path;
    }

    /**
	 * @return -1 if none
	 */
    public int getLineOfMethod(String function, int argCount) {
        getContent();
        Integer line = getIndex().get(getKey(function, argCount));
        return (line == null) ? -1 : line.intValue();
    }

    protected String getNamespaceUri() {
        return _namespaceUri;
    }

    private static List<Element> getElements(Element parent, String name) {
        List<Element> result = new ArrayList<Element>();
        NodeList kids = parent.getChildNodes();
        for (int i = 0, n = kids.getLength(); i < n; i++) {
            Node item = kids.item(i);
            if (item instanceof Element && (name == null || name.equals(item.getNodeName()))) {
                result.add((Element) item);
            }
        }
        return result;
    }

    private static Element getElement(Element parent, String name) {
        return getElements(parent, name).get(0);
    }
}
