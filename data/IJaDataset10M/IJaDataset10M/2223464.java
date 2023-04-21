package org.archive.crawler.framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

/**
 * Parent class for objects which are configured via a 
 * backing XML node (possibly a full document, possibly
 * just a part of a full document). 
 * 
 * @author gojomo
 *
 */
public class XMLConfig {

    /**
	 * Backing node; default origin of any XPath accesses.
	 */
    protected Node xNode;

    protected HashMap cachedPathNodes = new HashMap();

    protected HashMap cachedIntegers = new HashMap();

    protected HashMap cachedStrings = new HashMap();

    protected String defaultFilePath = null;

    protected XMLConfig parentConfigurationFile;

    /**
	 * Convenience method for reading an XML file into a Document instance
	 * 
	 * @param filename
	 * @return
	 */
    public static Document readDocumentFromFile(String filename) throws IOException {
        File f = new File(filename);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(f);
            return document;
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    public void setDefaultFileLocation(String loc) {
        defaultFilePath = loc;
    }

    public String getDefaultFileLocation() {
        return defaultFilePath;
    }

    /** Same as readDocumentFromFile, but will use default path
	 *  if the file specified has no path information (relative or abs)
	 *  AND cannot be found in the current working directory.
	 * @param filename
	 * @return document
	 */
    public static Document readDocumentFromFile(String filename, String path) throws IOException {
        if (path == null || isAbsoluteFilePath(filename)) {
            return readDocumentFromFile(filename);
        } else {
            return readDocumentFromFile(path + File.separator + filename);
        }
    }

    /** Determines if a string represents an absolute path (e.g. /usr/local/file) */
    protected static boolean isAbsoluteFilePath(String path) {
        if (path.matches("^\\/") || path.matches("^\\w:\\\\")) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * If the supplied node has a 'src' attribute, treat that
	 * as a filename, and return the document in that file.
	 * Otherwise, return the supplied node. Simulates in-line
	 * inclusion of the other file, if specified.
	 * 
	 * @param node
	 * @return
	 */
    public static Node nodeOrSrc(Node node) {
        try {
            Node srcNode = XPathAPI.selectSingleNode(node, "@src");
            if (srcNode != null) {
                return (Node) readDocumentFromFile(srcNode.getNodeValue());
            }
        } catch (TransformerException te) {
            te.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return node;
    }

    public static Node nodeOrSrc(Node node, String path) throws IOException {
        try {
            Node srcNode = XPathAPI.selectSingleNode(node, "@src");
            if (srcNode != null) {
                return (Node) readDocumentFromFile(srcNode.getNodeValue(), path);
            }
        } catch (TransformerException te) {
            te.printStackTrace();
        } catch (IOException e) {
            throw e;
        }
        return node;
    }

    /**
	 * Get either the contents at the specified xpath, or the
	 * contents of the file given by the src attribute at the
	 * specified xpath. Useful for reading large blocks of text
	 * from within an XML element or in a referenced helper file. 
	 * (For example, long seed URI lists.)
	 * 
	 * @param xpath
	 * @return
	 */
    public BufferedReader nodeValueOrSrcReader(String xpath) throws IOException {
        Node node = getNodeAt(xpath);
        BufferedReader reader = nodeValueOrSrcReader(node);
        if (reader == null && parentConfigurationFile != null) {
            return parentConfigurationFile.nodeValueOrSrcReader(xpath);
        }
        return reader;
    }

    public BufferedReader nodeValueOrSrcReader(String xpath, String path) throws IOException {
        return nodeValueOrSrcReader(getNodeAt(xpath), path);
    }

    /**
	 * Return the node at the specified xpath, starting from
	 * the local origin node. 
	 * 
	 * @param xpath
	 * @return
	 */
    public Node getNodeAt(String xpath) {
        Node cacheNode = null;
        if (!cachedPathNodes.containsKey(xpath)) {
            try {
                cacheNode = XPathAPI.selectSingleNode(xNode, xpath);
            } catch (TransformerException e) {
                e.printStackTrace();
                cacheNode = null;
            }
            if (cacheNode == null && parentConfigurationFile != null) {
                cacheNode = parentConfigurationFile.getNodeAt(xpath);
            }
            cachedPathNodes.put(xpath, cacheNode);
        }
        return (Node) cachedPathNodes.get(xpath);
    }

    /**
	 * Get either the contents of the specified node, or the
	 * contents of the file given by the src attribute of the
	 * specified node. Useful for reading large blocks of text
	 * from within an XML element or in a referenced helper file. 
	 * (For example, long seed URI lists.)
	 * 
	 * @param node
	 * @return
	 */
    public static BufferedReader nodeValueOrSrcReader(Node node) throws IOException {
        try {
            Node srcNode = XPathAPI.selectSingleNode(node, "@src");
            if (srcNode != null) {
                return new BufferedReader(new FileReader(srcNode.getNodeValue()));
            }
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return new BufferedReader(new StringReader(textOf(node)));
    }

    public static BufferedReader nodeValueOrSrcReader(Node node, String path) throws IOException {
        if (path == null) {
            return nodeValueOrSrcReader(node);
        }
        try {
            Node srcNode = XPathAPI.selectSingleNode(node, "@src");
            if (srcNode == null) {
                return nodeValueOrSrcReader(node);
            } else {
                String srcFile;
                if (isAbsoluteFilePath(srcNode.getNodeValue())) {
                    srcFile = srcNode.getNodeValue();
                } else {
                    srcFile = path + File.separator + srcNode.getNodeValue();
                }
                return new BufferedReader(new FileReader(srcFile));
            }
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Retrieve a (positive) integer value from the given xpath;
	 * return -1 if none found or other error occurs. 
	 * 
	 * @param xpath
	 * @return
	 */
    public boolean getBooleanAt(String xpath, boolean defaultValue) {
        String value = getStringAt(xpath);
        if (value == null) {
            return defaultValue;
        }
        if (value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

    /**
	 * Retrieve a (positive) integer value from the given xpath;
	 * return -1 if none found or other error occurs. 
	 * 
	 * @param xpath
	 * @return
	 */
    public int getIntAt(String xpath) {
        return getIntAt(xpath, -1);
    }

    /**
	 * Retrieve a (positive) integer value from the given xpath;
	 * return the supplied default if none found or other error occurs. 
	 * 
	 * @param xpath
	 * @param defaultValue
	 * @return
	 */
    public int getIntAt(String xpath, int defaultValue) {
        Integer cacheInteger = null;
        if (!cachedIntegers.containsKey(xpath)) {
            try {
                String n = getStringAt(xpath);
                if (n != null) {
                    cacheInteger = new Integer(getStringAt(xpath));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (DOMException e) {
                e.printStackTrace();
            }
            if (cacheInteger == null && parentConfigurationFile != null) {
                cacheInteger = new Integer(parentConfigurationFile.getIntAt(xpath, defaultValue));
            }
            if (cacheInteger == null) {
                cacheInteger = new Integer(defaultValue);
            }
            cachedIntegers.put(xpath, cacheInteger);
        }
        return ((Integer) cachedIntegers.get(xpath)).intValue();
    }

    /**
	 * Retrieve a (positive) long  value from the given xpath;
	 * return the supplied default if none found or other error occurs. 
	 * 
	 * @param xpath
	 * @param defaultValue
	 * @return
	 */
    public long getLongAt(String xpath, long defaultValue) {
        String n = getStringAt(xpath);
        long l;
        if (n != null) {
            return Long.parseLong(n);
        }
        return defaultValue;
    }

    /**
	 * Return the text of the given node: the value if an
	 * attribute node, the concatenation of all text children
	 * if an element node.
	 * 
	 * @param node
	 * @return
	 */
    private static String textOf(Node node) {
        if (node == null) {
            return null;
        }
        if (node instanceof Attr) {
            return node.getNodeValue();
        }
        String value = "";
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node ci = children.item(i);
            if (ci.getNodeType() == Node.TEXT_NODE) {
                value = value + ci.getNodeValue();
            }
        }
        if (value.length() == 0) {
            return null;
        }
        return value;
    }

    /**
	 * Return the text at the specified xpath. 
	 * 
	 * @param xpath
	 * @return
	 */
    public String getStringAt(String xpath) {
        String cacheString = null;
        if (!cachedStrings.containsKey(xpath)) {
            try {
                cacheString = textOf(getNodeAt(xpath));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (DOMException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            if (cacheString == null && parentConfigurationFile != null) {
                cacheString = parentConfigurationFile.getStringAt(xpath);
            }
            cachedStrings.put(xpath, cacheString);
        }
        return (String) cachedStrings.get(xpath);
    }

    /**
	 * @param string
	 * @param arcPrefix
	 * @return
	 */
    public String getStringAt(String xpath, String defaultVal) {
        String retVal = getStringAt(xpath);
        if (retVal == null) {
            retVal = defaultVal;
        }
        return retVal;
    }

    /**
	 * Using the node at the specified xpath as a guide,
	 * create a Java instance. The node must supply a 
	 * 'class' attribute. 
	 * 
	 * @param xpath
	 * @return
	 */
    public Object instantiate(String xpath) {
        try {
            Node node = XPathAPI.selectSingleNode(xNode, xpath);
            if (node == null && parentConfigurationFile != null) {
                return parentConfigurationFile.instantiate(xpath);
            }
            if (node == null) {
                return null;
            }
            return instantiate(node);
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Using the specified node, create a Java instance. 
	 * The node must supply a 'class' attribute. If the
	 * given class itself extends XMLConfig, the instance
	 * will be handed to node to serve as its own backing 
	 * XML. 
	 * 
	 * @param n
	 * @return
	 */
    public Object instantiate(Node n) {
        try {
            Class c = Class.forName(n.getAttributes().getNamedItem("class").getNodeValue());
            Object instance = c.newInstance();
            if (instance instanceof XMLConfig) {
                ((XMLConfig) instance).setNode(n);
            }
            return instance;
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Set the backing XML node.
	 * 
	 * @param n
	 */
    public void setNode(Node n) {
        xNode = n;
    }

    /**
	 * Instantiate each node selected by the xpath using the
	 * included class specifications. If a results object is
	 * supplied, add all instantiated objects to it. (If it
	 * is a hashmap, add them under their 'name' attribute.)
	 * Return the first item instatiated. 
	 * 
	 * @param xpath
	 * @param results
	 * @return
	 */
    public Object instantiateAllInto(String xpath, Object results) {
        Object first = null;
        NodeIterator iter;
        if (parentConfigurationFile != null) {
            first = parentConfigurationFile.instantiateAllInto(xpath, results);
        }
        try {
            iter = XPathAPI.selectNodeIterator(xNode, xpath);
        } catch (TransformerException e) {
            e.printStackTrace();
            return null;
        }
        Node currentNode;
        while ((currentNode = iter.nextNode()) != null) {
            Object currentObject = instantiate(currentNode);
            if (first == null) {
                first = currentObject;
            }
            Node firstSet = currentNode.getAttributes().getNamedItem("isFirst");
            if (firstSet != null) {
                if ("true".equalsIgnoreCase(firstSet.getNodeValue())) {
                    first = currentObject;
                }
            }
            if (results instanceof HashMap) {
                if (currentNode.getAttributes().getNamedItem("name") != null) {
                    String name = currentNode.getAttributes().getNamedItem("name").getNodeValue();
                    ((HashMap) results).put(name, currentObject);
                }
            } else if (results instanceof Collection) {
                ((Collection) results).add(currentObject);
            }
        }
        return first;
    }

    public Node getXNode() {
        return xNode;
    }

    public void clearCaches() {
        cachedPathNodes.clear();
        cachedIntegers.clear();
        cachedStrings.clear();
    }

    public void setParentConfig(XMLConfig x) {
        parentConfigurationFile = x;
    }

    public XMLConfig getParentConfig() {
        return parentConfigurationFile;
    }
}
