package com.jc.xml.xpath.util;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.sun.org.apache.xerces.internal.dom.DeferredAttrNSImpl;
import com.sun.org.apache.xml.internal.serialize.Method;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;

/**
 * @author Jeff Matthes
 * 
 */
public class XPathHelper {

    public static final String DEFAULT_MODEL = Method.XML;

    public static final String DEFAULT_ENCODING = "ISO-8859-1";

    public static final boolean DEFAULT_TABBED_FORMAT = true;

    public static final int DEFAULT_TAB_AMOUNT = 2;

    public static final boolean DEFAULT_PRINT_XML_DECLARATION = false;

    public static final boolean DEFAULT_PRINT_STANDALONE = false;

    public static final boolean DEFAULT_PRINT_DOCUMENT_TYPE = false;

    protected String model;

    protected String encoding;

    protected boolean tabbedFormat;

    protected int indentAmount;

    protected boolean printXmlDeclaration;

    protected boolean printStandalone;

    protected boolean printDocumentType;

    protected String inputString;

    protected InputSource inputSource;

    protected DocumentBuilderFactory factory;

    protected DocumentBuilder builder;

    protected OutputFormat format;

    protected XPath xPath;

    protected XPathExpression activeXpathExpression;

    protected Node lastRanExpression;

    protected StringReader inputStringReader;

    protected String activeXpathExpressionString;

    private static final Logger LOG = Logger.getLogger(XPathHelper.class);

    static {
        if (LOG.isDebugEnabled()) {
            try {
                LOG.debug("Logger newed up in " + XPathHelper.class.getName());
            } catch (Exception ignore) {
            }
        }
    }

    public XPathHelper() {
        LOG.trace("Enter XPathHelper()");
        model = DEFAULT_MODEL;
        encoding = DEFAULT_ENCODING;
        tabbedFormat = DEFAULT_TABBED_FORMAT;
        indentAmount = DEFAULT_TAB_AMOUNT;
        printXmlDeclaration = DEFAULT_PRINT_XML_DECLARATION;
        printStandalone = DEFAULT_PRINT_STANDALONE;
        printDocumentType = DEFAULT_PRINT_DOCUMENT_TYPE;
    }

    public XPathHelper(String model, String encoding, boolean tabbedFormat, int indentAmount) {
        LOG.trace("Enter XPathHelper(String model, String encoding, boolean tabbedFormat, int indentAmount)");
        this.model = model;
        this.encoding = encoding;
        this.tabbedFormat = tabbedFormat;
        this.indentAmount = indentAmount;
        printXmlDeclaration = DEFAULT_PRINT_XML_DECLARATION;
        printStandalone = DEFAULT_PRINT_STANDALONE;
        printDocumentType = DEFAULT_PRINT_DOCUMENT_TYPE;
    }

    public void setXmlInput(String inputStringIn) {
        LOG.trace("Enter setXmlInput(String inputStringIn)");
        inputString = inputStringIn;
    }

    public void setXpathExpression(String xPathExpression) throws XPathExpressionException {
        LOG.trace("Enter setXpathExpression(String xPathExpression)");
        if (xPath == null) xPath = XPathFactory.newInstance().newXPath(); else xPath.reset();
        activeXpathExpressionString = xPathExpression;
        activeXpathExpression = xPath.compile(xPathExpression);
    }

    public NodeList executeForNodeList() throws XPathExpressionException, ParserConfigurationException {
        LOG.trace("Enter executeForNodeList()");
        setupFormat();
        Object myResult = null;
        NodeList nodeList = null;
        inputStringReader = new StringReader(inputString);
        inputSource = new InputSource(inputStringReader);
        myResult = activeXpathExpression.evaluate(inputSource, XPathConstants.NODESET);
        if (myResult == null) return null;
        nodeList = (NodeList) myResult;
        return nodeList;
    }

    public NodeList executeForNodeList(Node input) throws XPathExpressionException, ParserConfigurationException {
        LOG.trace("Enter executeForNodeList(Node input)");
        setupFormat();
        Object myResult = null;
        NodeList nodeList = null;
        myResult = activeXpathExpression.evaluate(input, XPathConstants.NODESET);
        if (myResult == null) return null;
        nodeList = (NodeList) myResult;
        return nodeList;
    }

    public Element executeForElement() throws XPathExpressionException, ParserConfigurationException {
        LOG.trace("Enter executeForElement()");
        setupFormat();
        Object myResult = null;
        Element result = null;
        inputStringReader = new StringReader(inputString);
        inputSource = new InputSource(inputStringReader);
        myResult = activeXpathExpression.evaluate(inputSource, XPathConstants.NODE);
        if (myResult == null) return null;
        result = (Element) myResult;
        return result;
    }

    public Element executeForElement(Node input) throws XPathExpressionException, ParserConfigurationException {
        LOG.trace("Enter executeForElement(Node input)");
        setupFormat();
        Object myResult = null;
        Element result = null;
        myResult = activeXpathExpression.evaluate(input, XPathConstants.NODE);
        if (myResult == null) return null;
        result = (Element) myResult;
        return result;
    }

    public Element[] executeForElementArray() throws XPathExpressionException, ParserConfigurationException {
        LOG.trace("Enter executeForElementArray()");
        setupFormat();
        Object myResult = null;
        Element[] result = null;
        inputStringReader = new StringReader(inputString);
        inputSource = new InputSource(inputStringReader);
        myResult = activeXpathExpression.evaluate(inputSource, XPathConstants.NODESET);
        NodeList nodeList = (NodeList) myResult;
        result = new Element[nodeList.getLength()];
        for (int i = 0; i < nodeList.getLength(); i++) {
            result[i] = (Element) nodeList.item(i);
        }
        return result;
    }

    public Element[] executeForElementArray(Node input) throws XPathExpressionException, ParserConfigurationException {
        LOG.trace("Enter executeForElementArray(Node input)");
        setupFormat();
        Object myResult = null;
        Element[] result = null;
        myResult = activeXpathExpression.evaluate(input, XPathConstants.NODESET);
        NodeList nodeList = (NodeList) myResult;
        result = new Element[nodeList.getLength()];
        for (int i = 0; i < nodeList.getLength(); i++) {
            result[i] = (Element) nodeList.item(i);
        }
        return result;
    }

    public Node executeForNode(Node input) throws XPathExpressionException, ParserConfigurationException {
        LOG.trace("Enter executeForNode(Node input)");
        setupFormat();
        Object myResult = null;
        Node result = null;
        myResult = activeXpathExpression.evaluate(input, XPathConstants.NODE);
        if (myResult == null) return null;
        result = (Node) myResult;
        return result;
    }

    public Node executeForNode() throws XPathExpressionException, ParserConfigurationException {
        LOG.trace("Enter executeForNode()");
        setupFormat();
        Object myResult = null;
        Node result = null;
        inputStringReader = new StringReader(inputString);
        inputSource = new InputSource(inputStringReader);
        myResult = activeXpathExpression.evaluate(inputSource, XPathConstants.NODE);
        if (myResult == null) return null;
        result = (Node) myResult;
        return result;
    }

    public Double executeForNumber(Node input) throws XPathExpressionException, ParserConfigurationException {
        LOG.trace("Enter executeForNumber(Node input)");
        setupFormat();
        Object myResult = null;
        Double result = null;
        myResult = activeXpathExpression.evaluate(input, XPathConstants.NUMBER);
        result = (Double) myResult;
        return result;
    }

    public Double executeForNumber() throws XPathExpressionException, ParserConfigurationException {
        LOG.trace("Enter executeForNumber()");
        setupFormat();
        Object myResult = null;
        Double result = null;
        inputStringReader = new StringReader(inputString);
        inputSource = new InputSource(inputStringReader);
        myResult = activeXpathExpression.evaluate(inputSource, XPathConstants.NUMBER);
        result = (Double) myResult;
        return result;
    }

    public Boolean executeForBoolean() throws XPathExpressionException, ParserConfigurationException {
        LOG.trace("Enter executeForBoolean()");
        setupFormat();
        Object myResult = null;
        Boolean result = null;
        inputStringReader = new StringReader(inputString);
        inputSource = new InputSource(inputStringReader);
        myResult = activeXpathExpression.evaluate(inputSource, XPathConstants.BOOLEAN);
        result = (Boolean) myResult;
        return result;
    }

    public Boolean executeForBoolean(Node input) throws XPathExpressionException, ParserConfigurationException {
        LOG.trace("Enter executeForBoolean(Node input)");
        setupFormat();
        Object myResult = null;
        Boolean result = null;
        myResult = activeXpathExpression.evaluate(input, XPathConstants.BOOLEAN);
        result = (Boolean) myResult;
        return result;
    }

    public String executeForString() throws ParserConfigurationException, IOException, XPathExpressionException {
        LOG.trace("Enter executeForString()");
        setupFormat();
        String outputString = null;
        Object myResult = null;
        NodeList nodeList = null;
        Node node = null;
        try {
            inputStringReader = new StringReader(inputString);
            inputSource = new InputSource(inputStringReader);
            myResult = activeXpathExpression.evaluate(inputSource, XPathConstants.NODESET);
            if (myResult == null) return null;
            nodeList = (NodeList) myResult;
            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);
                if (CharacterData.class.isAssignableFrom(node.getClass())) {
                    outputString = (outputString == null ? "" : outputString + "\n") + node.getTextContent();
                } else if (node.getClass().getName().toString().equals(DeferredAttrNSImpl.class.getName())) {
                    outputString = (outputString == null ? "" : outputString + "\n") + node.getTextContent();
                } else if (Element.class.isAssignableFrom(node.getClass())) {
                    outputString = (outputString == null ? "" : outputString) + XmlBeautifier.beautify((Element) node, model, encoding, indentAmount, printXmlDeclaration, printStandalone, printDocumentType);
                } else {
                    System.out.println("MyObject class is " + node.getClass().getName() + " myObject=" + node);
                    System.out.println("Node [" + i + "]=" + node.getTextContent());
                }
            }
        } catch (XPathExpressionException e) {
            inputStringReader = new StringReader(inputString);
            inputSource = new InputSource(inputStringReader);
            try {
                myResult = activeXpathExpression.evaluate(inputSource, XPathConstants.NUMBER);
                if (Double.class.isAssignableFrom(myResult.getClass())) {
                    outputString = (outputString == null ? "" : outputString + "\n") + myResult.toString();
                } else {
                    System.out.println("MyObject class is " + myResult.getClass().getName() + " myObject=" + myResult);
                }
            } catch (XPathExpressionException e1) {
                inputStringReader = new StringReader(inputString);
                inputSource = new InputSource(inputStringReader);
                myResult = activeXpathExpression.evaluate(inputSource, XPathConstants.BOOLEAN);
                if (Boolean.class.isAssignableFrom(myResult.getClass())) {
                    outputString = (outputString == null ? "" : outputString + "\n") + myResult.toString();
                } else {
                    System.out.println("MyObject class is " + myResult.getClass().getName() + " myObject=" + myResult);
                }
            }
        }
        return outputString;
    }

    public String executeForStringData(Element input) throws ParserConfigurationException, IOException, XPathExpressionException {
        LOG.trace("Enter executeForStringData(Element input)");
        setupFormat();
        Object myResult = null;
        String result = null;
        myResult = activeXpathExpression.evaluate(input, XPathConstants.STRING);
        result = (String) myResult;
        return result;
    }

    public String executeForString(Element inputTest) throws ParserConfigurationException, IOException, XPathExpressionException {
        LOG.trace("Enter executeForString(Element inputTest)");
        setupFormat();
        String outputString = null;
        Object myResult = null;
        NodeList nodeList = null;
        Node node = null;
        myResult = null;
        try {
            myResult = activeXpathExpression.evaluate(inputTest, XPathConstants.NODESET);
            if (myResult == null) return null;
            nodeList = (NodeList) myResult;
            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);
                if (CharacterData.class.isAssignableFrom(node.getClass())) {
                    outputString = (outputString == null ? "" : outputString + "\n") + node.getTextContent();
                } else if (node.getClass().getName().toString().equals(DeferredAttrNSImpl.class.getName())) {
                    outputString = (outputString == null ? "" : outputString + "\n") + node.getTextContent();
                } else if (Element.class.isAssignableFrom(node.getClass())) {
                    outputString = (outputString == null ? "" : outputString) + XmlBeautifier.beautify((Element) node, model, encoding, indentAmount, printXmlDeclaration, printStandalone, printDocumentType);
                } else {
                    System.out.println("MyObject class is " + node.getClass().getName() + " myObject=" + node);
                    System.out.println("Node [" + i + "]=" + node.getTextContent());
                }
            }
        } catch (XPathExpressionException e) {
            try {
                myResult = activeXpathExpression.evaluate(inputTest, XPathConstants.NUMBER);
                if (Double.class.isAssignableFrom(myResult.getClass())) {
                    outputString = (outputString == null ? "" : outputString + "\n") + myResult.toString();
                } else {
                    System.out.println("MyObject class is " + myResult.getClass().getName() + " myObject=" + myResult);
                }
            } catch (XPathExpressionException e1) {
                myResult = activeXpathExpression.evaluate(inputTest, XPathConstants.BOOLEAN);
                if (Boolean.class.isAssignableFrom(myResult.getClass())) {
                    outputString = (outputString == null ? "" : outputString + "\n") + myResult.toString();
                } else {
                    System.out.println("MyObject class is " + myResult.getClass().getName() + " myObject=" + myResult);
                }
            }
        }
        return outputString;
    }

    protected void setupFormat() throws ParserConfigurationException {
        LOG.trace("Enter setupFormat()");
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        format = new OutputFormat(model, encoding, (indentAmount > 0 ? false : tabbedFormat));
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getIndentAmount() {
        return indentAmount;
    }

    public void setIndentAmount(int indentAmount) {
        this.indentAmount = indentAmount;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isPrintDocumentType() {
        return printDocumentType;
    }

    public void setPrintDocumentType(boolean printDocumentType) {
        this.printDocumentType = printDocumentType;
    }

    public boolean isPrintStandalone() {
        return printStandalone;
    }

    public void setPrintStandalone(boolean printStandalone) {
        this.printStandalone = printStandalone;
    }

    public boolean isPrintXmlDeclaration() {
        return printXmlDeclaration;
    }

    public void setPrintXmlDeclaration(boolean printXmlDeclaration) {
        this.printXmlDeclaration = printXmlDeclaration;
    }

    public boolean isTabbedFormat() {
        return tabbedFormat;
    }

    public void setTabbedFormat(boolean tabbedFormat) {
        this.tabbedFormat = tabbedFormat;
    }

    public static void main(String[] args) {
        String xmlSource = "<books>" + "	<book id=\"1\" isbn=\"3-8266-0612-4\">" + "		<title><![CDATA[M & I Bank, a success in the making]]></title>" + "		<year>1921</year>" + "		<subject>M &amp; I Bank, a success in the making</subject>" + "       <price>49.99</price>" + "	</book>" + "	<book id=\"2\" isbn=\"3-8266-0550-0\">" + "		<title>Linux Internet and Intranet</title>" + "		<year>2000</year>" + "		<subject>Operating Systems</subject>" + "       <price>10.99</price>" + "	</book>" + "	<book id=\"3\" isbn=\"3-8266-0612-4\">" + "		<title><![CDATA[Apache Web-Server]]></title>" + "		<year>2000</year>" + "		<subject>Webserver</subject>" + "       <price>12.99</price>" + "	</book>" + "</books>";
        String expression = "";
        expression = "//book[contains(subject,\"System\")]";
        expression = "/books/book[child::subject='Webserver']";
        expression = "/books/book[(price>11)]";
        XPathHelper helper = new XPathHelper();
        XPathHelper helper2 = new XPathHelper();
        try {
            helper.setXmlInput(xmlSource);
            helper.setXpathExpression(expression);
            Element e = helper.executeForElement();
            Element[] eArray = helper.executeForElementArray();
            String xml1 = helper.executeForString();
            System.out.println("-------------Xml Returned:----------------");
            System.out.println(xml1);
            System.out.println("-------------Xml Returned:----------------");
            for (int i = 0; i < eArray.length; i++) {
                helper2.setXpathExpression("//book[position()=" + 3 + "]/price");
                String xml3 = helper2.executeForString(eArray[i]);
                Double xml2 = helper2.executeForNumber(eArray[i]);
                System.out.println(eArray[i]);
                System.out.println(eArray[i].getTextContent());
                System.out.println(xml2);
                System.out.println(xml3);
            }
            helper.setXpathExpression("//book[2]/price/text()");
            String xml = helper.executeForNumber(e).toString();
            System.out.println("-------------Xml Returned:----------------");
            System.out.println(xml);
            helper.setXpathExpression("//book/subject/text()");
            Node node = helper.executeForNode(e);
            System.out.println("-------------Xml Returned:----------------");
            System.out.println(node.getTextContent());
            helper.setXpathExpression("//book/price/text()");
            String xml3 = helper.executeForString(e);
            System.out.println("-------------Xml Returned:----------------");
            System.out.println(xml3);
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
