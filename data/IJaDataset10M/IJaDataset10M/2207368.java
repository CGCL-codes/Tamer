package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.dom.Attribute;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.dtd.DTD;
import com.volantis.mcs.dom.output.CharacterEncoder;
import com.volantis.mcs.dom.output.DocumentWriter;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.utilities.ReusableStringBuffer;
import com.volantis.mcs.utilities.ReusableStringWriter;
import com.volantis.synergetics.log.LogDispatcher;
import java.io.IOException;

/**
 * This class is used to annotate elements in the dom tree which is being
 * dissected.
 */
public class ElementAnnotation extends NodeAnnotation implements DissectionConstants {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(ElementAnnotation.class);

    protected Element element;

    /**
   * Determines whether the tags have been generated. If true then the
   * open and close tags have been generated, otherwise they have not been.
   */
    protected boolean generatedTags;

    /**
   * The open tag, this is generated when it is needed.
   */
    private ReusableStringBuffer openTag;

    /**
   * The close tag, this is generated at the same time as the open tag if
   * it is needed.
   */
    private ReusableStringBuffer closeTag;

    /**
   * Whether a dividehint is active
   */
    private boolean divideHintActive;

    /**
   * Flag which indicates whether the element is always empty.
   */
    private boolean alwaysEmpty;

    /**
   * Flag which indicates whether we have checked if this element is a
   * keepTogether element.  If true then the method isKeepTogether will always
   * return false.
   */
    private boolean checkedKeepTogether = false;

    public ElementAnnotation() {
    }

    /**
   * Implement this method to return the element.
   */
    protected Node getNode() {
        return element;
    }

    /**
   * Set the value of the element property.
   * @param element The new value of the element property.
   */
    public void setElement(Element element) {
        this.element = element;
    }

    /**
   * Get the value of the element property.
   * @return The value of the element property.
   */
    public Element getElement() {
        return element;
    }

    /**
   * Set the value of the always empty property.
   * @param alwaysEmpty The new value of the always empty property.
   */
    public void setAlwaysEmpty(boolean alwaysEmpty) {
        this.alwaysEmpty = alwaysEmpty;
    }

    /**
   * Get the value of the always empty property.
   * @return The value of the always empty property.
   */
    public boolean getAlwaysEmpty() {
        return alwaysEmpty;
    }

    /**
    * Generate the open and close tags from the element name and the attributes.
    */
    protected void generateTags() {
        CharacterEncoder encoder = protocol.getCharacterEncoder();
        openTag = new ReusableStringBuffer();
        ReusableStringWriter writer = new ReusableStringWriter(openTag);
        DTD dtd = protocol.getProtocolConfiguration().getDTD();
        DocumentWriter dw = dtd.createDocumentWriter(writer);
        try {
            if (dw.outputOpenTag(element, encoder)) {
                closeTag = new ReusableStringBuffer();
                writer.setBuffer(closeTag);
                dw.outputCloseTag(element);
            }
        } catch (IOException e) {
            logger.error("tag-generation-error", e);
        }
        generatedTags = true;
    }

    /**
   * Calculate the size of the open and close tags if any.
   * @return The size of the open and close tags.
   */
    protected int calculateTagsSize() {
        if (!generatedTags) {
            generateTags();
        }
        int size = 0;
        if (openTag != null) {
            size += openTag.length();
        }
        if (closeTag != null) {
            size += closeTag.length();
        }
        return size;
    }

    /**
   * Is a divide hint being processed.
   */
    protected boolean isDivideHintActive() {
        return divideHintActive;
    }

    /**
   * Mark the divide hint as seen
   */
    protected void setDivideHintActive(boolean b) {
        divideHintActive = b;
    }

    /**
   * Implement the abstract calculateContentsSize method.
   */
    protected int calculateContentsSize() {
        int size = calculateTagsSize();
        for (Node child = element.getHead(); child != null; child = child.getNext()) {
            NodeAnnotation annotation = (NodeAnnotation) child.getObject();
            size += annotation.getContentsSize();
        }
        return size;
    }

    /**
   * Implement the abstract calculateFixedContentsSize method.
   */
    protected int calculateFixedContentsSize() {
        int size = calculateTagsSize();
        for (Node child = element.getHead(); child != null; child = child.getNext()) {
            NodeAnnotation annotation = (NodeAnnotation) child.getObject();
            size += annotation.getFixedContentsSize();
        }
        return size;
    }

    /**
   * Implement the abstract calculateOverheadSize method.
   */
    protected int calculateOverheadSize() throws ProtocolException {
        int size = calculateTagsSize();
        return size;
    }

    /**
   * Override this method to handle divide hints properly.
   */
    protected boolean isDivideHintElement() {
        String name = element.getName();
        return (DIVIDE_HINT_ELEMENT.equals(name));
    }

    /**
   * Check to see whether this node is actually a dissection hint element.
   */
    protected boolean isKeepTogetherElement() {
        boolean result = false;
        if (!checkedKeepTogether) {
            result = KEEPTOGETHER_ELEMENT.equals(element.getName());
            checkedKeepTogether = true;
        }
        return result;
    }

    /**
   * Implement the abstract markShardNodesImpl method.
   */
    protected int markShardNodesImpl(int shardNumber, int limit) throws ProtocolException {
        if (atomic) {
            if (logger.isDebugEnabled()) {
                logger.debug(element.getName() + " cannot be broken down");
            }
            return NODE_CANNOT_FIT;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Attempting to break " + this + " into smaller pieces for shard " + shardNumber);
            }
        }
        int overheadSize = getOverheadSize();
        if (overheadSize > limit) {
            if (logger.isDebugEnabled()) {
                logger.debug("Overhead for " + this + " of " + overheadSize + " is greater than the available space " + limit + " left in shard " + shardNumber);
            }
            return NODE_CANNOT_FIT;
        }
        limit -= overheadSize;
        if (logger.isDebugEnabled()) {
            logger.debug("Overhead for " + this + " is " + overheadSize + " space remaining is " + limit + " in shard " + shardNumber);
        }
        int consumed = overheadSize;
        Node child = element.getHead();
        if (child != null) {
            for (; child != null; child = child.getNext()) {
                NodeAnnotation annotation = (NodeAnnotation) child.getObject();
                int result = annotation.markShardNodes(shardNumber, limit);
                switch(result) {
                    case IGNORE_NODE:
                        break;
                    case NODE_CANNOT_FIT:
                        if (consumed == overheadSize) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("No children of " + this + " have been added to shard " + shardNumber);
                            }
                            return NODE_CANNOT_FIT;
                        } else {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Some children of " + this + " have been added to shard " + shardNumber);
                            }
                            return SHARD_COMPLETE;
                        }
                    case SHARD_COMPLETE:
                        if (logger.isDebugEnabled()) {
                            logger.debug("A descendant of this " + this + " was the last node to be" + " added to shard " + shardNumber);
                        }
                        return SHARD_COMPLETE;
                    default:
                        consumed += result;
                        limit -= result;
                        break;
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Last child of " + this + " was added to shard " + shardNumber);
            }
            return consumed;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Leaf node " + this + " will not fit in shard " + shardNumber);
        }
        return NODE_CANNOT_FIT;
    }

    /**
   * Implement the abstract generateContents method.
   */
    public void generateContents(ReusableStringBuffer buffer) {
        if (!generatedTags) {
            generateTags();
        }
        if (openTag != null) {
            buffer.append(openTag);
        }
        for (Node child = element.getHead(); child != null; child = child.getNext()) {
            NodeAnnotation annotation = (NodeAnnotation) child.getObject();
            annotation.generateContents(buffer);
        }
        if (closeTag != null) {
            buffer.append(closeTag);
        }
    }

    /**
   * Implement the abstract generateFixedContents method.
   */
    public void generateFixedContents(ReusableStringBuffer buffer) {
        if (!generatedTags) {
            generateTags();
        }
        if (openTag != null) {
            buffer.append(openTag);
        }
        for (Node child = element.getHead(); child != null; child = child.getNext()) {
            NodeAnnotation annotation = (NodeAnnotation) child.getObject();
            annotation.generateFixedContents(buffer);
        }
        if (closeTag != null) {
            buffer.append(closeTag);
        }
    }

    /**
   * Implement the abstract generateDissectedContents method.
   */
    public void generateDissectedContents(ReusableStringBuffer buffer) throws ProtocolException {
        if (!generatedTags) {
            generateTags();
        }
        if (openTag != null) {
            buffer.append(openTag);
        }
        protocol.getProtocolConfiguration().addCandidateElementAssetURLs(element, ContextInternals.getApplicationContext(protocol.getMarinerPageContext().getRequestContext()).getPackageResources());
        for (Node child = element.getHead(); child != null; child = child.getNext()) {
            NodeAnnotation annotation = (NodeAnnotation) child.getObject();
            annotation.generateDissectedContents(buffer);
        }
        if (closeTag != null) {
            buffer.append(closeTag);
        }
    }

    /**
   * Implement the abstract generateShardContentsImpl method.
   */
    public boolean generateShardContentsImpl(ReusableStringBuffer buffer, int shardNumber, boolean all) {
        if (!generatedTags) {
            generateTags();
        }
        if (openTag != null) {
            buffer.append(openTag);
        }
        protocol.getProtocolConfiguration().addCandidateElementAssetURLs(element, ContextInternals.getApplicationContext(protocol.getMarinerPageContext().getRequestContext()).getPackageResources());
        boolean done = false;
        for (Node child = element.getHead(); child != null; child = child.getNext()) {
            NodeAnnotation annotation = (NodeAnnotation) child.getObject();
            done = annotation.generateShardContents(buffer, shardNumber, all);
            if (done) {
                break;
            }
        }
        if (closeTag != null) {
            buffer.append(closeTag);
        }
        return done;
    }

    /**
   * Implement the abstract generateDebugOutput method.
   */
    protected void generateDebugOutput(ReusableStringBuffer buffer, String indent) {
        String name = element.getName();
        if (name != null) {
            Attribute attribute = element.getAttributes();
            char[] attributeIndent = new char[indent.length() + name.length() + 2];
            for (int i = 0; i < attributeIndent.length; i += 1) {
                attributeIndent[i] = ' ';
            }
            buffer.append(indent).append("<").append(name);
            if (attribute != null) {
                boolean first = true;
                for (; attribute != null; attribute = attribute.getNext()) {
                    if (!first) {
                        buffer.append("\n").append(attributeIndent);
                    } else {
                        buffer.append(" ");
                        first = false;
                    }
                    buffer.append(attribute.getName()).append("=\"").append(attribute.getValue()).append("\"");
                }
            }
            if (alwaysEmpty) {
                buffer.append("/>\n");
                return;
            }
            buffer.append(">\n");
        }
        for (Node child = element.getHead(); child != null; child = child.getNext()) {
            NodeAnnotation annotation = (NodeAnnotation) child.getObject();
            annotation.generateDebugOutput(buffer, indent + "  ");
        }
        if (name != null) {
            buffer.append(indent).append("</").append(name).append(">\n");
        }
    }
}
