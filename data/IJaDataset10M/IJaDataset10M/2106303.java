package org.dom4j.rule.pattern;

import org.dom4j.Node;
import org.dom4j.rule.Pattern;

/**
 * <p>
 * <code>NodeTypePattern</code> implements a Pattern which matches any node of
 * the given node type.
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.7 $
 */
public class NodeTypePattern implements Pattern {

    /** A pattern which matches any Attribute node */
    public static final NodeTypePattern ANY_ATTRIBUTE = new NodeTypePattern(Node.ATTRIBUTE_NODE);

    /** A pattern which matches any Comment node */
    public static final NodeTypePattern ANY_COMMENT = new NodeTypePattern(Node.COMMENT_NODE);

    /** A pattern which matches any Document node */
    public static final NodeTypePattern ANY_DOCUMENT = new NodeTypePattern(Node.DOCUMENT_NODE);

    /** A pattern which matches any Element node */
    public static final NodeTypePattern ANY_ELEMENT = new NodeTypePattern(Node.ELEMENT_NODE);

    /** A pattern which matches any ProcessingInstruction node */
    public static final NodeTypePattern ANY_PROCESSING_INSTRUCTION = new NodeTypePattern(Node.PROCESSING_INSTRUCTION_NODE);

    /** A pattern which matches any Text node */
    public static final NodeTypePattern ANY_TEXT = new NodeTypePattern(Node.TEXT_NODE);

    private short nodeType;

    public NodeTypePattern(short nodeType) {
        this.nodeType = nodeType;
    }

    public boolean matches(Node node) {
        return node.getNodeType() == nodeType;
    }

    public double getPriority() {
        return Pattern.DEFAULT_PRIORITY;
    }

    public Pattern[] getUnionPatterns() {
        return null;
    }

    public short getMatchType() {
        return nodeType;
    }

    public String getMatchesNodeName() {
        return null;
    }
}
