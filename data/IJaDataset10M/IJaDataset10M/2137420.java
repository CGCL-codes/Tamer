package uk.ac.ed.ph.jqtiplus.node;

import uk.ac.ed.ph.jqtiplus.attribute.AttributeList;
import uk.ac.ed.ph.jqtiplus.control.JQTIController;
import uk.ac.ed.ph.jqtiplus.control.ValidationContext;
import uk.ac.ed.ph.jqtiplus.exception.QTIParseException;
import uk.ac.ed.ph.jqtiplus.group.NodeGroup;
import uk.ac.ed.ph.jqtiplus.group.NodeGroupList;
import uk.ac.ed.ph.jqtiplus.validation.ValidationResult;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Parent of all xml nodes.
 * 
 * @author Jiri Kajaba
 * @author Jonathon Hare
 */
public abstract class AbstractNode implements XmlNode {

    private static final long serialVersionUID = 1L;

    /** Parent of this node. */
    private final XmlNode parent;

    /** Attributes of this node. */
    private final AttributeList attributes;

    /** Node groups of this node (contains all its children). */
    private final NodeGroupList groups;

    /**
     * Constructs node.
     *
     * @param parent parent of constructed node (can be null for root nodes)
     */
    public AbstractNode(XmlNode parent) {
        this.parent = parent;
        this.attributes = new AttributeList(this);
        this.groups = new NodeGroupList(this);
    }

    public XmlNode getParent() {
        return parent;
    }

    public XmlNode getParentRoot() {
        XmlNode node = this;
        while (node.getParent() != null) {
            node = node.getParent();
        }
        return node;
    }

    public AttributeList getAttributes() {
        return attributes;
    }

    public NodeGroupList getNodeGroups() {
        return groups;
    }

    public void load(JQTIController jqtiController, Element sourceElement) {
        loadAttributes(sourceElement);
        readChildren(jqtiController, sourceElement);
    }

    /**
     * Loads all attributes from given xml source.
     *
     * @param element xml source
     */
    protected void loadAttributes(Element element) {
        attributes.load(element);
    }

    /**
     * Reads all children nodes and/or content from given xml source.
     * Every subclass must implement its own children nodes and/or content reading.
     * If there are no children nodes and content do nothing (you don't even need to override this method).
     * @param jqtiController TODO
     * @param element xml source
     */
    protected void readChildren(JQTIController jqtiController, Element element) {
        groups.load(jqtiController, element);
    }

    /**
     * Reads one child from given xml source.
     * Every subclass must implement its own child reading.
     * @param JQTIController TODO
     * @param node xml source
     */
    protected void readChildNode(@SuppressWarnings("unused") JQTIController jqtiController, Node node) {
        throw new QTIParseException("Unsupported child: " + node.getLocalName());
    }

    public final String toXmlString() {
        return toXmlString(0, false);
    }

    public String toXmlString(int depth, boolean printDefaultAttributes) {
        StringBuilder builder = new StringBuilder();
        if (depth > 0) builder.append(NEW_LINE);
        builder.append(getIndent(depth) + "<" + getClassTag());
        builder.append(attrToXmlString(depth, printDefaultAttributes));
        String body = bodyToXmlString(depth, printDefaultAttributes);
        if (body.length() == 0) builder.append("/>"); else {
            builder.append(">");
            builder.append(body);
            if (body.startsWith(NEW_LINE + getIndent(depth) + INDENT)) builder.append(NEW_LINE + getIndent(depth));
            builder.append("</" + getClassTag() + ">");
        }
        return builder.toString();
    }

    public abstract String getClassTag();

    /**
     * Prints attributes of this node into xml string.
     *
     * @param depth depth in xml tree (root = 0)
     * @param printDefaultAttributes whether print attributes with default values
     * @return xml string with printed attributes of this node
     */
    protected String attrToXmlString(int depth, boolean printDefaultAttributes) {
        return getAttributes().toXmlString(depth, printDefaultAttributes);
    }

    /**
     * Prints body (children and/or text content) of this node into xml string.
     * @param depth depth in xml tree (root = 0)
     * @param printDefaultAttributes whether print attributes with default values
     * @return xml string with printed body (children and/or text content) of this node
     */
    protected String bodyToXmlString(int depth, boolean printDefaultAttributes) {
        return groups.toXmlString(depth + 1, printDefaultAttributes);
    }

    public final String getSimpleName() {
        return getClassTag();
    }

    public String computeXPathComponent() {
        XmlNode parentNode = getParent();
        int position = 1;
        if (parentNode != null) {
            SEARCH: for (NodeGroup nodeGroup : parentNode.getNodeGroups()) {
                position = 1;
                for (XmlNode child : nodeGroup.getChildren()) {
                    if (child == this) {
                        break SEARCH;
                    }
                }
            }
            return getClassTag() + "[" + position + "]";
        }
        return getClassTag();
    }

    public String computeXPath() {
        StringBuilder pathBuilder = new StringBuilder();
        buildXPath(pathBuilder, this);
        return pathBuilder.toString();
    }

    private void buildXPath(StringBuilder pathBuilder, XmlNode node) {
        if (pathBuilder.length() > 0) {
            pathBuilder.insert(0, "/");
        }
        if (node != null) {
            pathBuilder.insert(0, node.computeXPathComponent());
            buildXPath(pathBuilder, node.getParent());
        }
    }

    public ValidationResult validate(ValidationContext context) {
        ValidationResult result = new ValidationResult();
        result.add(validateAttributes(context));
        result.add(validateChildren(context));
        return result;
    }

    /**
     * Validates attributes of this node.
     * @param context TODO
     *
     * @return result of validation
     */
    protected ValidationResult validateAttributes(ValidationContext context) {
        return attributes.validate(context);
    }

    /**
     * Validates children (body) of this node.
     * @param context TODO
     *
     * @return result of validation
     */
    protected ValidationResult validateChildren(ValidationContext context) {
        ValidationResult result = groups.validate(context);
        for (int i = 0; i < groups.size(); i++) {
            NodeGroup node = groups.get(i);
            for (XmlNode child : node.getChildren()) result.add(child.validate(context));
        }
        return result;
    }

    /**
     * Prints indent into xml string.
     *
     * @param depth depth in xml tree (root = 0)
     * @return xml string with printed indent
     */
    public static String getIndent(int depth) {
        StringBuilder builder = new StringBuilder();
        appendIndent(builder, depth);
        return builder.toString();
    }

    /**
     * Prints indent into xml string.
     *
     * @param depth depth in xml tree (root = 0)
     * @return xml string with printed indent
     */
    public static void appendIndent(StringBuilder builder, int depth) {
        for (int i = 0; i < depth; i++) builder.append(INDENT);
    }

    public static String escapeForXmlString(String text, boolean asAttribute) {
        StringBuilder builder = new StringBuilder();
        for (char c : text.toCharArray()) {
            switch(c) {
                case '<':
                    builder.append("&lt;");
                    break;
                case '>':
                    builder.append("&gt;");
                    break;
                case '&':
                    builder.append("&amp;");
                    break;
                case '"':
                    if (asAttribute) {
                        builder.append("&quot;");
                        break;
                    }
                default:
                    builder.append(c);
                    break;
            }
        }
        return builder.toString();
    }

    public boolean hasChildNodes() {
        for (NodeGroup nodeGroup : getNodeGroups()) {
            if (nodeGroup.getChildren().size() > 0) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "<" + getClassTag() + ">@" + hashCode();
    }
}
