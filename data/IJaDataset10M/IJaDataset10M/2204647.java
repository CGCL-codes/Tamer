package br.com.dotec.util;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class XmlUtil {

    public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private static DocumentBuilderFactory documentBuilderFactory;

    private static DocumentBuilder documentBuilder = null;

    private static final Object UTF8Header = new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };

    private static final String XPATH_CONDITION_OPERATOR_EQUALS = "=";

    private static final String XPATH_CONDITION_OPERATOR_LESSTHAN = "<";

    private static final String XPATH_CONDITION_OPERATOR_GREATERTHAN = ">";

    private static final String XPATH_CONCATENATION_OPERATOR_AND = " and ";

    private static final String XPATH_CONCATENATION_OPERATOR_OR = " or ";

    private static Pattern encodingPattern = Pattern.compile("<\\? ?xml [^>]*encoding=\"([^\"]*)\"[^>]*\\?>");

    public static Node selectSingleNode(String childNameQuery, Node parentNode) {
        try {
            if (childNameQuery.startsWith("/")) {
                parentNode = (parentNode.getOwnerDocument() == null) ? parentNode : parentNode.getOwnerDocument();
                childNameQuery = childNameQuery.substring(1);
            }
            if (childNameQuery.indexOf("|") > -1) {
                String[] childNameSubQueries = childNameQuery.split("|");
                Node currentNode;
                for (String childNameSubQuery : childNameSubQueries) if ((currentNode = selectSingleNode(childNameSubQuery, parentNode)) != null) return currentNode;
            }
            if (childNameQuery.indexOf("/") > -1) {
                String localQuery = childNameQuery.substring(0, childNameQuery.indexOf("/"));
                Node[] localNodes;
                Node selectedNode;
                localNodes = selectNodes(localQuery, parentNode);
                for (int i = 0; i < localNodes.length; i++) {
                    selectedNode = selectSingleNode(childNameQuery.substring(childNameQuery.indexOf("/") + 1), localNodes[i]);
                    if (selectedNode != null) {
                        return selectedNode;
                    }
                }
                return null;
            }
            Node childNode = null;
            String childName = childNameQuery;
            String condition = null;
            if (childNameQuery.indexOf("[") > -1) {
                childName = childNameQuery.substring(0, childNameQuery.indexOf("["));
                condition = childNameQuery.substring(childNameQuery.indexOf("[") + 1, childNameQuery.length() - 1);
            }
            if (childName.length() > 1 && childName.substring(0, 1).equals("@")) {
                childNode = parentNode.getAttributes().getNamedItem(childName.substring(1));
            } else if (childName.equals(".")) {
                childNode = parentNode;
            } else if (childName.equals("..")) {
                childNode = parentNode.getParentNode();
            } else {
                childNode = parentNode.getFirstChild();
                while (childNode != null) {
                    if (childNode.getNodeName().equals(childName)) {
                        if (condition != null) {
                            if (XmlUtil.checkCondition(childNode, condition)) break;
                        } else break;
                    }
                    childNode = childNode.getNextSibling();
                }
            }
            return childNode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Node[] selectNodes(String childNameQuery, Node parentNode) {
        try {
            List<Node> nodesVector = new ArrayList<Node>();
            selectNodes(childNameQuery, parentNode, nodesVector);
            Node[] nodes = new Node[nodesVector.size()];
            return nodesVector.toArray(nodes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void selectNodes(String childNameQuery, Node parentNode, List<Node> nodes) throws Exception {
        if (childNameQuery.startsWith("/")) {
            parentNode = parentNode.getOwnerDocument();
            childNameQuery = childNameQuery.substring(1);
        }
        if (childNameQuery.indexOf("|") > -1) {
            String[] childNameSubQueries = childNameQuery.split("|");
            for (String childNameSubQuery : childNameSubQueries) selectNodes(childNameSubQuery, parentNode, nodes);
        }
        if (childNameQuery.indexOf("/") > -1) {
            int posFirstSlash = childNameQuery.indexOf("/");
            String firstNodeName = childNameQuery.substring(0, posFirstSlash);
            String remainderXPath = childNameQuery.substring(posFirstSlash + 1);
            Node[] firstNodes = selectNodes(firstNodeName, parentNode);
            for (Node firstNode : firstNodes) selectNodes(remainderXPath, firstNode, nodes);
            return;
        }
        Node childNode = parentNode.getFirstChild();
        String childName = childNameQuery;
        String condition = null;
        if (childNameQuery.indexOf("[") > -1) {
            childName = childNameQuery.substring(0, childNameQuery.indexOf("["));
            condition = childNameQuery.substring(childNameQuery.indexOf("[") + 1, childNameQuery.length() - 1);
        }
        if (childName.equals(".")) {
            childNode = parentNode;
            if (condition != null) {
                if (XmlUtil.checkCondition(childNode, condition)) nodes.add(childNode);
            } else {
                nodes.add(childNode);
            }
            return;
        }
        while (childNode != null) {
            if (childNode.getNodeName().equals(childName)) {
                if (condition != null) {
                    if (XmlUtil.checkCondition(childNode, condition)) nodes.add(childNode);
                } else {
                    nodes.add(childNode);
                }
            }
            childNode = childNode.getNextSibling();
        }
    }

    public static boolean checkCondition(Node conditionNode, String condition) throws Exception {
        boolean returnValue = false;
        String concatinationOperator = null;
        int posConcatination = condition.lastIndexOf(XPATH_CONCATENATION_OPERATOR_AND);
        if (posConcatination != -1) {
            concatinationOperator = XPATH_CONCATENATION_OPERATOR_AND;
        }
        int pos = condition.lastIndexOf(XPATH_CONCATENATION_OPERATOR_OR);
        if (pos > posConcatination) {
            posConcatination = pos;
            concatinationOperator = XPATH_CONCATENATION_OPERATOR_OR;
        }
        if (concatinationOperator != null) {
            String leftConditions = condition.substring(0, posConcatination);
            String rightCondition = condition.substring(posConcatination + concatinationOperator.length());
            if (concatinationOperator.equals(XPATH_CONCATENATION_OPERATOR_OR)) returnValue = checkCondition(conditionNode, leftConditions) || checkSingleCondition(conditionNode, rightCondition); else returnValue = checkCondition(conditionNode, leftConditions) && checkSingleCondition(conditionNode, rightCondition);
        } else {
            returnValue = checkSingleCondition(conditionNode, condition);
        }
        return returnValue;
    }

    public static boolean checkSingleCondition(Node conditionNode, String condition) throws Exception {
        boolean returnValue = false;
        String conditionOperator = XPATH_CONDITION_OPERATOR_EQUALS;
        condition = condition.trim();
        if (condition.indexOf(XPATH_CONDITION_OPERATOR_LESSTHAN) > -1) conditionOperator = XPATH_CONDITION_OPERATOR_LESSTHAN; else if (condition.indexOf(XPATH_CONDITION_OPERATOR_GREATERTHAN) > -1) conditionOperator = XPATH_CONDITION_OPERATOR_GREATERTHAN;
        String[] curCondition = condition.split(conditionOperator);
        String attributeName = curCondition[0];
        String attributeValue = curCondition[1];
        attributeValue = attributeValue.substring(1, attributeValue.length() - 1);
        if (attributeName.substring(0, 1).equals("@")) {
            attributeName = attributeName.substring(1);
            String actualAttributeValue = XmlUtil.readAttributeString(attributeName, conditionNode);
            if (actualAttributeValue == null) returnValue = false; else returnValue = checkCondition(actualAttributeValue, attributeValue, conditionOperator);
        } else if (attributeName.equals("name()")) {
            String actualAttributeValue = conditionNode.getNodeName();
            returnValue = checkCondition(actualAttributeValue, attributeValue, conditionOperator);
        } else {
            String actualElementValue = XmlUtil.readNodeString(attributeName, conditionNode);
            if (actualElementValue == null) returnValue = false; else returnValue = (actualElementValue != null && checkCondition(actualElementValue, attributeValue, conditionOperator));
        }
        return returnValue;
    }

    public static String readNodeString(String xpathExpression, Node sourceNode) {
        return readNodeString(xpathExpression, sourceNode, null);
    }

    public static String readNodeString(String xpathExpression, Node sourceNode, String valueForNull) {
        Node pNode = XmlUtil.selectSingleNode(xpathExpression, sourceNode);
        if (pNode != null) {
            String result = getTextContent(pNode);
            if (result == null || result.length() == 0) return valueForNull; else return result;
        } else {
            return valueForNull;
        }
    }

    protected static boolean checkCondition(String leftValue, String rightValue, String conditionOperator) {
        boolean returnValue = false;
        if (conditionOperator.equals(XmlUtil.XPATH_CONDITION_OPERATOR_GREATERTHAN)) returnValue = Integer.parseInt(leftValue) > Integer.parseInt(rightValue); else if (conditionOperator.equals(XmlUtil.XPATH_CONDITION_OPERATOR_LESSTHAN)) returnValue = Integer.parseInt(leftValue) < Integer.parseInt(rightValue); else returnValue = leftValue.equals(rightValue);
        return returnValue;
    }

    public static String readAttributeString(String attributeName, Node node) {
        try {
            return readAttributeString(attributeName, node, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readAttributeString(String attributeName, Node node, String valueForNull) {
        Node attributeNode = node.getAttributes().getNamedItem(attributeName);
        if (attributeNode != null) return getTextContent(attributeNode); else return valueForNull;
    }

    public static String getTextContent(Node node) {
        if (node != null) {
            StringBuilder result = new StringBuilder();
            Node child = node.getFirstChild();
            while (child != null) {
                String value = child.getNodeValue();
                if (value != null) result.append(value);
                child = child.getNextSibling();
            }
            return result.toString();
        } else return "";
    }
}
