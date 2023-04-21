package uk.ac.ed.ph.jqtiplus.validation;

import uk.ac.ed.ph.jqtiplus.node.XmlNode;

/**
 * One item of validation (the smallest piece of indivisible information).
 * 
 * @author Jiri Kajaba
 */
public interface ValidationItem {

    /**
     * Gets type of this item.
     *
     * @return type of this item
     * @see ValidationType
     */
    public ValidationType getType();

    /**
     * Gets source of this item.
     *
     * @return source of this item
     */
    public Validatable getSource();

    /**
     * Gets source node of this item.
     * <p>
     * <ul>
     * <li>If source of this item is node, this method returns this node.</li>
     * <li>If source of this item is not node (e.g. attribute), this method returns source's (e.g. attribute's) parent node.</li>
     * </ul>
     *
     * @return source node of this item
     */
    public XmlNode getNode();

    public void setNode(XmlNode node);

    /**
     * Gets message of this item.
     *
     * @return message of this item
     */
    public String getMessage();
}
