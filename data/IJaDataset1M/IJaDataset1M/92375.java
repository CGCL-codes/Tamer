package org.jxpfw.jsp.tag.form;

import javax.servlet.jsp.JspTagException;
import org.josef.util.CDebug;

/**
 * Base class for all custom JSP tags that must reside within a PropertyTag.
 * <br>This class is responsible for validating that the tag which class is a
 * concrete subclass of this class is indeed nested within a PropertyTag.
 * @author Kees Schotanus
 * @version 1.1 $Revision: 1.13 $
 * @see PropertyTag
 */
public abstract class AbstractPropertyElementTag extends AbstractFormElementTag {

    /**
     * Universal version identifier for this serializable class.
     */
    private static final long serialVersionUID = -6326129630222590974L;

    /**
     * PropertyTag enclosing the tag which class is a concrete subclass of this
     * class.
     */
    private PropertyTag propertyTag;

    /**
     * Gets the PropertyTag that encloses the tag which class is a concrete
     * subclass of this tag.
     * @return The PropertyTag that encloses the tag which class is a concrete
     *  subclass of this tag.
     */
    public PropertyTag getPropertyTag() {
        if (propertyTag == null) {
            propertyTag = PropertyTag.findAncestorPropertyTag(this);
        }
        return propertyTag;
    }

    /**
     * Adds the HTML name attribute to the supplied buffer.
     * <br>Note: The name attribute won't be added to readonly properties to
     * prevent creation of a request parameter. When you do want a name for the
     * input item you can use the html attribute to always add a name.
     * @param buffer StringBuffer to which the name attribute will be added.
     * @throws NullPointerException When the supplied buffer is null.
     */
    @Override
    public void addNameAttribute(final StringBuffer buffer) {
        CDebug.checkParameterNotNull(buffer, "buffer");
        if (Boolean.FALSE.equals(getPropertyTag().isReadonly())) {
            buffer.append(" name=\"");
            buffer.append(getPropertyTag().getName());
            buffer.append('\"');
        }
    }

    /**
     * Gets the id of the property element.
     * <br>When no id attribute has been set then the id is set equal to the
     * name of the property element.<br>
     * Consider:<code><pre>
     * &lt;jxpfw:property name="city"&gt;
     *   &lt;jxpfw:label/&gt;
     *   &lt;jxpfw:text/&gt;
     * &lt;/jxpfw:property&gt;</pre></code>
     * The different input tags can use this method to get the id which in this
     * case would be equal to the name of the enclosing property (city).<br>
     * When the property is "indexed" that is, the property has an index
     * attribute, then the index is appended to the id.
     * @return Id of the property element.
     *  <br>The id of a property element equals the id itself when it has been
     *  set but it equals the name of the property (appended with an optional
     *  index number) when it has not been set.
     */
    @Override
    public String getId() {
        String derivedId = super.getId();
        if (derivedId == null) {
            derivedId = getPropertyTag().getName();
            if (getPropertyTag().getIndexNumber() != null) {
                derivedId += getPropertyTag().getIndexNumber();
            }
        }
        return derivedId;
    }

    /**
     * Validates that the tag which is a concrete subclass of this class, is
     * properly nested within a BeanTag, FormTag and a PropertyTag.
     * @throws JspTagException When this tag is not nested within a BeanTag,
     *  FormTag and PropertyTag.
     */
    @Override
    protected void validate() throws JspTagException {
        super.validate();
        if (getPropertyTag() == null) {
            throw new JspTagException("This tag (" + this.toString() + ") " + "is not nested within a property tag!");
        }
    }

    /**
     * Clears attributes so the next tag won't re-use them incorrectly.
     */
    @Override
    public void release() {
        super.release();
        propertyTag = null;
    }
}
