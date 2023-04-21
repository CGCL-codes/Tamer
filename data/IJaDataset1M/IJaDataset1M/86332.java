package co.edu.unal.ungrid.image.dicom.core;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.swing.tree.TreeNode;

/**
 * <p>
 * An abstract class for representing a node in an internal representation of a
 * structured reporting tree (an instance of
 * {@link co.edu.unal.ungrid.image.dicom.core.StructuredReport  StructuredReport} ).
 * </p>
 * <p>
 * The constructor is protected. Instances of specific types of content items
 * should normally be created by using the
 * {@link co.edu.unal.ungrid.image.dicom.core.ContentItemFactory  ContentItemFactory} .
 * </p>
 * 
 * @see co.edu.unal.ungrid.image.dicom.core.ContentItemFactory
 * @see co.edu.unal.ungrid.image.dicom.core.StructuredReport
 * @see co.edu.unal.ungrid.image.dicom.core.StructuredReportBrowser
 * 
 */
public abstract class ContentItem implements TreeNode {

    /**
	 * @uml.property name="valueType"
	 */
    protected String valueType;

    /**
	 * @uml.property name="relationshipType"
	 */
    protected String relationshipType;

    /**
	 * @uml.property name="conceptName"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
    protected CodedSequenceItem conceptName;

    /**
	 * @uml.property name="referencedContentItemIdentifier"
	 */
    protected String referencedContentItemIdentifier;

    /**
	 * @uml.property name="parent"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
    ContentItem parent;

    /**
	 * @uml.property name="children"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 *                     elementType="co.edu.unal.ungrid.image.dicom.dicom.ContentItem"
	 */
    List<TreeNode> children;

    /**
	 * @uml.property name="list"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
    AttributeList list;

    /**
	 * <p>
	 * Returns the parent node of this node.
	 * </p>
	 * 
	 * @return the parent node, or null if the root
	 */
    public TreeNode getParent() {
        return parent;
    }

    /**
	 * <p>
	 * Returns the child at the specified index.
	 * </p>
	 * 
	 * @param index
	 *            the index of the child to be returned, numbered from 0
	 * @return the child <code>TreeNode</code> at the specified index
	 */
    public TreeNode getChildAt(int index) {
        return children.get(index);
    }

    /**
	 * <p>
	 * Returns the index of the specified child from amongst this node's
	 * children, if present.
	 * </p>
	 * 
	 * @param child
	 *            the child to search for amongst this node's children
	 * @return the index of the child, or -1 if not present
	 */
    public int getIndex(TreeNode child) {
        int n = children.size();
        for (int i = 0; i < n; ++i) {
            if (children.get(i).equals(child)) {
                return i;
            }
        }
        return -1;
    }

    /**
	 * <p>
	 * Always returns true, since children may always be added.
	 * </p>
	 * 
	 * @return always true
	 */
    public boolean getAllowsChildren() {
        return true;
    }

    /**
	 * <p>
	 * Returns true if the receiver is a leaf (has no children).
	 * </p>
	 * 
	 * @return true if the receiver is a leaf
	 */
    public boolean isLeaf() {
        return getChildCount() == 0;
    }

    /**
	 * <p>
	 * Return the number of children that this node contains.
	 * </p>
	 * 
	 * @return the number of children, 0 if none
	 */
    public int getChildCount() {
        return children == null ? 0 : children.size();
    }

    /**
	 * <p>
	 * Returns the children of this node as an
	 * {@link java.util.Enumeration Enumeration}.
	 * </p>
	 * 
	 * @return the children of this node
	 */
    public Enumeration<TreeNode> children() {
        return (children == null ? null : new Vector<TreeNode>(children).elements());
    }

    private void extractCommonAttributes() {
        valueType = Attribute.getSingleStringValueOrDefault(list, TagFromName.ValueType, "");
        relationshipType = Attribute.getSingleStringValueOrDefault(list, TagFromName.RelationshipType, "");
        conceptName = CodedSequenceItem.getSingleCodedSequenceItemOrNull(list, TagFromName.ConceptNameCodeSequence);
        referencedContentItemIdentifier = Attribute.getDelimitedStringValuesOrEmptyString(list, TagFromName.ReferencedContentItemIdentifier);
    }

    /**
	 * <p>
	 * Construct a content item for a list of attributes, and add it as a child
	 * of the specified parent.
	 * </p>
	 * 
	 * <p>
	 * The constructor is protected. Instances of specific types of content
	 * items should normally be created by using the
	 * {@link co.edu.unal.ungrid.image.dicom.core.ContentItemFactory ContentItemFactory}.
	 * </p>
	 * 
	 * @param p
	 *            the parent
	 * @param l
	 *            the list of attributes
	 */
    protected ContentItem(ContentItem p, AttributeList l) {
        parent = p;
        list = l;
        extractCommonAttributes();
    }

    /**
	 * <p>
	 * Add a child to this content item.
	 * </p>
	 * 
	 * @param child
	 *            the child content item to add
	 */
    public void addChild(ContentItem child) {
        if (children == null) {
            children = new LinkedList<TreeNode>();
        }
        children.add(child);
    }

    /**
	 * <p>
	 * Add a sibling to this content item (a child to the parent of this content
	 * item).
	 * </p>
	 * 
	 * @param sibling
	 *            the sibling content item to add
	 * @exception DicomException
	 *                thrown if there is no parent
	 */
    public void addSibling(ContentItem sibling) throws DicomException {
        if (parent == null) {
            throw new DicomException("Internal error - root node with sibling");
        } else {
            parent.addChild(sibling);
        }
    }

    /**
	 * <p>
	 * Get the parent content item of this content item.
	 * </p>
	 * 
	 * <p>
	 * This method saves the caller from having to cast the value returned from
	 * {@link javax.swing.tree.TreeNode#getParent() TreeNode.getParent()}.
	 * </p>
	 * 
	 * @return the parent content item
	 */
    public ContentItem getParentAsContentItem() {
        return parent;
    }

    /**
	 * <p>
	 * Get the attribute list of this content item.
	 * </p>
	 * 
	 * @return the attribute list of this content item
	 */
    public AttributeList getAttributeList() {
        return list;
    }

    /**
	 * <p>
	 * Get the value type of this content item.
	 * </p>
	 * 
	 * @return the value type (the string used in the DICOM standard in the
	 *         Value Type attribute)
	 * @uml.property name="valueType"
	 */
    public String getValueType() {
        return valueType;
    }

    /**
	 * <p>
	 * Get the Referenced SOP Class UID of this content item, if present and
	 * applicable.
	 * </p>
	 * 
	 * @return the Referenced SOP Class UID, or null
	 */
    public String getReferencedSOPClassUID() {
        return null;
    }

    /**
	 * <p>
	 * Get the Referenced SOP Instance UID of this content item, if present and
	 * applicable.
	 * </p>
	 * 
	 * @return the Referenced SOP Instance UID, or null
	 */
    public String getReferencedSOPInstanceUID() {
        return null;
    }

    /**
	 * <p>
	 * Get the Graphic Type of this content item, if present and applicable.
	 * </p>
	 * 
	 * @return the Graphic Type, or null
	 */
    public String getGraphicType() {
        return null;
    }

    /**
	 * <p>
	 * Get the Graphic Data of this content item, if present and applicable.
	 * </p>
	 * 
	 * @return the Graphic Data, or null
	 */
    public float[] getGraphicData() {
        return null;
    }

    /**
	 * <p>
	 * Get a string representation of the concept name and the value of the
	 * concept.
	 * </p>
	 * 
	 * <p>
	 * The exact form of the returned string is specific to the type of
	 * ContentItem.
	 * </p>
	 * 
	 * @return a String representation of the name and value, or an empty string
	 */
    public String getConceptNameAndValue() {
        return getConceptNameCodeMeaning() + " " + getConceptValue();
    }

    /**
	 * <p>
	 * Get a string representation of the value of the concept.
	 * </p>
	 * <p>
	 * The exact form of the returned string is specific to the type of
	 * ContentItem.
	 * </p>
	 * 
	 * @return a String representation of the name and value, or an empty string
	 * @uml.property name="conceptValue"
	 */
    public abstract String getConceptValue();

    /**
	 * <p>
	 * Get the value of the code meaning of the Concept Name as a string, if
	 * present and applicable.
	 * </p>
	 * 
	 * @return the code meaning of the Concept Name, or an empty string
	 */
    public String getConceptNameCodeMeaning() {
        return conceptName == null ? "" : conceptName.getCodeMeaning();
    }

    /**
	 * <p>
	 * Get the value of the code value of the Concept Name as a string, if
	 * present and applicable.
	 * </p>
	 * 
	 * @return the code value of the Concept Name, or an empty string
	 */
    public String getConceptNameCodeValue() {
        return conceptName == null ? "" : conceptName.getCodeValue();
    }

    /**
	 * <p>
	 * Get the value of the coding scheme designator of the Concept Name as a
	 * string, if present and applicable.
	 * </p>
	 * 
	 * @return the coding scheme designator of the Concept Name, or an empty
	 *         string
	 */
    public String getConceptNameCodingSchemeDesignator() {
        return conceptName == null ? "" : conceptName.getCodingSchemeDesignator();
    }

    /**
	 * <p>
	 * Get the Referenced Content Item Identifier, if present.
	 * </p>
	 * 
	 * @return the backslash delimited item references, or an empty string
	 * @uml.property name="referencedContentItemIdentifier"
	 */
    public String getReferencedContentItemIdentifier() {
        return referencedContentItemIdentifier == null ? "" : referencedContentItemIdentifier;
    }

    /**
	 * <p>
	 * Get a human-readable string representation of the content item.
	 * </p>
	 * 
	 * @return the string representation of the content item
	 */
    public String toString() {
        return (referencedContentItemIdentifier == null || referencedContentItemIdentifier.length() == 0 ? "" : "R-") + relationshipType + ": " + (valueType == null || valueType.length() == 0 ? "" : (valueType + ": ")) + (conceptName == null ? "" : conceptName.getCodeMeaning()) + (referencedContentItemIdentifier == null || referencedContentItemIdentifier.length() == 0 ? "" : referencedContentItemIdentifier);
    }

    /**
	 * Retrieve the named child as defined by its ConceptName
	 * 
	 * @param codingSchemeDesignator
	 * @param codeValue
	 * @return the (first, if multiple) named child, or null if absent
	 */
    public ContentItem getNamedChild(String codingSchemeDesignator, String codeValue) {
        ContentItem child = null;
        if (codingSchemeDesignator != null && codeValue != null) {
            int n = getChildCount();
            for (int i = 0; i < n; ++i) {
                ContentItem test = (ContentItem) getChildAt(i);
                if (test != null) {
                    String csd = test.getConceptNameCodingSchemeDesignator();
                    String cv = test.getConceptNameCodeValue();
                    if (csd != null && csd.equals(codingSchemeDesignator) && cv != null && cv.equals(codeValue)) {
                        child = test;
                        break;
                    }
                }
            }
        }
        return child;
    }
}
