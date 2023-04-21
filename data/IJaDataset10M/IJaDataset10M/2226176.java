package com.centraview.udt;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.UserObject;

/**
 * Tag for creating multiple &lt;select&gt; options from a collection.  The
 * associated values displayed to the user may optionally be specified by a
 * second collection, or will be the same as the values themselves.  Each
 * collection may be an array of objects, a Collection, an Enumeration,
 * an Iterator, or a Map.
 * <b>NOTE</b> - This tag requires a Java2 (JDK 1.2 or later) platform.
 *
 * @author Florent Carpentier
 * @author Craig McClanahan
 */
public class CVOptionsTag extends TagSupport {

    /**
     *	status field name string
     */
    protected String fieldname;

    /**
     *	status module name string
     */
    protected String modulename;

    /**
     *	status module name string
     */
    protected int fieldRight = 0;

    /**
     */
    public String getFieldname() {
        return fieldname;
    }

    /**
     */
    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    /**
     */
    public String getModulename() {
        return modulename;
    }

    /**
     */
    public void setModulename(String modulename) {
        this.modulename = modulename;
    }

    /**
      * The message resources for this package.
      */
    protected static MessageResources messages = MessageResources.getMessageResources("ApplicationResources");

    /**
     * The name of the collection containing beans that have properties to
     * provide both the values and the labels (identified by the
     * <code>property</code> and <code>labelProperty</code> attributes).
     */
    protected String collection = null;

    public String getCollection() {
        return (this.collection);
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    /**
     * Should the label values be filtered for HTML sensitive characters?
     */
    protected boolean filter = true;

    public boolean getFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    /**
     * The name of the bean containing the labels collection.
     */
    protected String labelName = null;

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    /**
     * The bean property containing the labels collection.
     */
    protected String labelProperty = null;

    public String getLabelProperty() {
        return labelProperty;
    }

    public void setLabelProperty(String labelProperty) {
        this.labelProperty = labelProperty;
    }

    /**
     * The name of the bean containing the values collection.
     */
    protected String name = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The name of the property to use to build the values collection.
     */
    protected String property = null;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * The style associated with this tag.
     */
    private String style = null;

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * The named style class associated with this tag.
     */
    private String styleClass = null;

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * Process the start of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        return SKIP_BODY;
    }

    /**
     * Process the end of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {
        HttpSession session = ((HttpServletRequest) (pageContext.getRequest())).getSession();
        ModuleFieldRightMatrix mfrmx = ((UserObject) session.getAttribute("userobject")).getUserPref().getModuleAuthorizationMatrix();
        HashMap hm = mfrmx.getFieldRights(getModulename());
        if (hm != null && hm.containsKey(getFieldname())) {
            fieldRight = ((Integer) hm.get(getFieldname())).intValue();
        }
        CVSelectTag selectTag = (CVSelectTag) pageContext.getAttribute(Constants.SELECT_KEY);
        if (selectTag == null) {
            throw new JspException(messages.getMessage("optionsTag.select"));
        }
        StringBuffer sb = new StringBuffer();
        if (collection != null) {
            Iterator collIterator = getIterator(collection, null);
            while (collIterator.hasNext()) {
                Object bean = collIterator.next();
                Object value = null;
                Object label = null;
                try {
                    value = PropertyUtils.getProperty(bean, property);
                    if (value == null) {
                        value = "";
                    }
                } catch (IllegalAccessException e) {
                    throw new JspException(messages.getMessage("getter.access", property, collection));
                } catch (InvocationTargetException e) {
                    Throwable t = e.getTargetException();
                    throw new JspException(messages.getMessage("getter.result", property, t.toString()));
                } catch (NoSuchMethodException e) {
                    throw new JspException(messages.getMessage("getter.method", property, collection));
                }
                try {
                    if (labelProperty != null) {
                        label = PropertyUtils.getProperty(bean, labelProperty);
                    } else {
                        label = value;
                    }
                    if (label == null) {
                        label = "";
                    }
                } catch (IllegalAccessException e) {
                    throw new JspException(messages.getMessage("getter.access", labelProperty, collection));
                } catch (InvocationTargetException e) {
                    Throwable t = e.getTargetException();
                    throw new JspException(messages.getMessage("getter.result", labelProperty, t.toString()));
                } catch (NoSuchMethodException e) {
                    throw new JspException(messages.getMessage("getter.method", labelProperty, collection));
                }
                String stringValue = value.toString();
                if (fieldRight > 0 && fieldRight == ModuleFieldRightMatrix.VIEW_RIGHT) {
                    if (selectTag.isMatched(stringValue)) {
                        addOption(sb, stringValue, label.toString(), selectTag.isMatched(stringValue));
                    }
                }
                if (fieldRight > 0 && fieldRight == ModuleFieldRightMatrix.NONE_RIGHT) {
                    if (selectTag.isMatched(stringValue)) {
                        addOption(sb, "0", " ", selectTag.isMatched(stringValue));
                    }
                } else {
                    addOption(sb, stringValue, label.toString(), selectTag.isMatched(stringValue));
                }
            }
        } else {
            Iterator valuesIterator = getIterator(name, property);
            Iterator labelsIterator = null;
            if ((labelName == null) && (labelProperty == null)) {
                labelsIterator = getIterator(name, property);
            } else {
                labelsIterator = getIterator(labelName, labelProperty);
            }
            while (valuesIterator.hasNext()) {
                Object valueObject = valuesIterator.next();
                if (valueObject == null) {
                    valueObject = "";
                }
                String value = valueObject.toString();
                String label = value;
                if (labelsIterator.hasNext()) {
                    Object labelObject = labelsIterator.next();
                    if (labelObject == null) {
                        labelObject = "";
                    }
                    label = labelObject.toString();
                }
                if (fieldRight > 0 && fieldRight == ModuleFieldRightMatrix.VIEW_RIGHT) {
                    if (selectTag.isMatched(value)) {
                        addOption(sb, value, label, selectTag.isMatched(value));
                    }
                }
                if (fieldRight > 0 && fieldRight == ModuleFieldRightMatrix.NONE_RIGHT) {
                    if (selectTag.isMatched(value)) {
                        addOption(sb, "0", " ", selectTag.isMatched(value));
                    }
                } else {
                    addOption(sb, value, label, selectTag.isMatched(value));
                }
            }
        }
        ResponseUtils.write(pageContext, sb.toString());
        return EVAL_PAGE;
    }

    /**
     * Release any acquired resources.
     */
    public void release() {
        super.release();
        collection = null;
        filter = true;
        labelName = null;
        labelProperty = null;
        name = null;
        property = null;
        style = null;
        styleClass = null;
    }

    /**
     * Add an option element to the specified StringBuffer based on the
     * specified parameters.
     *<p>
     * Note that this tag specifically does not support the
     * <code>styleId</code> tag attribute, which causes the HTML
     * <code>id</code> attribute to be emitted.  This is because the HTML
     * specification states that all "id" attributes in a document have to be
     * unique.  This tag will likely generate more than one <code>option</code>
     * element element, but it cannot use the same <code>id</code> value.  It's
     * conceivable some sort of mechanism to supply an array of <code>id</code>
     * values could be devised, but that doesn't seem to be worth the trouble.
     *
     * @param sb StringBuffer accumulating our results
     * @param value Value to be returned to the server for this option
     * @param label Value to be shown to the user for this option
     * @param matched Should this value be marked as selected?
     */
    protected void addOption(StringBuffer sb, String value, String label, boolean matched) {
        sb.append("<option value=\"");
        sb.append(value);
        sb.append("\"");
        if (matched) {
            sb.append(" selected=\"selected\"");
        }
        if (style != null) {
            sb.append(" style=\"");
            sb.append(style);
            sb.append("\"");
        }
        if (styleClass != null) {
            sb.append(" class=\"");
            sb.append(styleClass);
            sb.append("\"");
        }
        sb.append(">");
        if (filter) {
            sb.append(ResponseUtils.filter(label));
        } else {
            sb.append(label);
        }
        sb.append("</option>\r\n");
    }

    /**
     * Return an iterator for the option labels or values, based on our
     * configured properties.
     *
     * @param name Name of the bean attribute (if any)
     * @param property Name of the bean property (if any)
     *
     * @exception JspException if an error occurs
     */
    protected Iterator getIterator(String name, String property) throws JspException {
        String beanName = name;
        if (beanName == null) {
            beanName = Constants.BEAN_KEY;
        }
        Object bean = RequestUtils.lookup(pageContext, beanName, null);
        if (bean == null) {
            throw new JspException(messages.getMessage("getter.bean", beanName));
        }
        Object collection = bean;
        if (property != null) {
            try {
                collection = PropertyUtils.getProperty(bean, property);
                if (collection == null) {
                    throw new JspException(messages.getMessage("getter.property", property));
                }
            } catch (IllegalAccessException e) {
                throw new JspException(messages.getMessage("getter.access", property, name));
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                throw new JspException(messages.getMessage("getter.result", property, t.toString()));
            } catch (NoSuchMethodException e) {
                throw new JspException(messages.getMessage("getter.method", property, name));
            }
        }
        if (collection.getClass().isArray()) {
            collection = Arrays.asList((Object[]) collection);
        }
        if (collection instanceof Collection) {
            return (((Collection) collection).iterator());
        } else if (collection instanceof Iterator) {
            return ((Iterator) collection);
        } else if (collection instanceof Map) {
            return (((Map) collection).entrySet().iterator());
        } else if (collection instanceof Enumeration) {
            return IteratorUtils.asIterator((Enumeration) collection);
        } else {
            throw new JspException(messages.getMessage("optionsTag.iterator", collection.toString()));
        }
    }
}
