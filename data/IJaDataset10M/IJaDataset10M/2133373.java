package org.weblayouttag.tag.field;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.weblayouttag.config.ConfigProperties;
import javax.servlet.jsp.PageContext;

/**
 * @author Andy Marek
 * @version May 22, 2005
 * @jsp.tag
 * 	name="select"
 * 	body-content="JSP"
 * 	description="Renders an HTML &lt;select&gt; element, associated with a bean property specified by our 
 * 		attributes.  This tag is only valid when nested inside a form tag body."
 */
public class SelectFieldTag extends AbstractSelectFieldTag {

    private boolean multiple = false;

    private String size;

    public void doStart(Element thisElement, PageContext pc) {
        super.doStart(thisElement, pc);
        this.createAttributeElement(this.getFieldElement(), "multiple", String.valueOf(this.isMultiple()));
        this.createAttributeElement(this.getFieldElement(), "size", this.getSize());
    }

    public void reset() {
        super.reset();
        this.multiple = false;
        this.size = null;
    }

    protected Element createFieldElement() {
        return DocumentHelper.createElement("select");
    }

    protected boolean getDefaultFilter() {
        return true;
    }

    protected boolean getDefaultRedisplay() {
        return this.getFormConfig().getPropertyAsBoolean(ConfigProperties.FIELDS_SELECT_REDISPLAY, true);
    }

    protected String getDefaultValue() {
        return this.getFormConfig().getProperty(ConfigProperties.FIELDS_SELECT_VALUE, StringUtils.EMPTY);
    }

    /**
	 * @jsp.attribute
	 * 	required="false"
	 * 	rtexprvalue="true"
	 * 	type="boolean"
	 * 	description="When set, it specifies that multiple items can be selected at a time."
	 */
    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    /**
	 * @jsp.attribute
	 * 	required="false"
	 * 	rtexprvalue="true"
	 * 	type="java.lang.String"
	 * 	description="Defines the number of visible items in the drop-down list."
	 */
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
