package org.j2eebuilder.view;

import java.io.IOException;
import java.beans.Expression;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.j2eebuilder.ComponentDefinition;
import org.j2eebuilder.AttributeDefinition;
import org.j2eebuilder.NonManagedBeanDefinition;
import org.j2eebuilder.util.LogManager;
import org.j2eebuilder.util.UtilityBean;

/**
 * @author Sandeep Dixit
 * @version 1.0 $date 2004/03/12
 * This tag sets a value of an attribute of a component
 */
public class SetAttributeTag extends ComponentTag {

    private static transient LogManager log = new LogManager(SetAttributeTag.class);

    private boolean isNew = false;

    private AttributeDefinition attributeDefinition = null;

    private Object attributeValue = null;

    private String attributeName = null;

    public boolean isNew() {
        return this.isNew;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public void setAttributeValue(Object attributeValue) {
        this.attributeValue = attributeValue;
    }

    public AttributeDefinition getAttributeDefinition() {
        return this.attributeDefinition;
    }

    public void setAttributeDefinition(AttributeDefinition attributeDefinition) {
        this.attributeDefinition = attributeDefinition;
    }

    public boolean isValid() {
        boolean bValid = false;
        try {
            super.isValid();
            this.attributeDefinition = this.getComponentDefinition().getAttributesDefinition().findAttributeDefinitionByName(this.getAttributeName());
            if (this.attributeDefinition == null) {
                StringBuffer msg = new StringBuffer();
                msg.append(this.getClass().getName());
                msg.append(".isValid(): Attribute definition [");
                msg.append(this.getAttributeName());
                msg.append("] was not found.");
                throw new org.j2eebuilder.DefinitionException(msg.toString());
            }
            Object target = getJspContext().findAttribute(this.getComponentDefinition().getNonManagedBeansDefinition().findNonManagedBeanDefinitionByTypeAndScope(NonManagedBeanDefinition.TYPE_TRANSIENTOBJECT, NonManagedBeanDefinition.SCOPE_SESSION).getId());
            if (target == null) {
                StringBuffer buf = new StringBuffer();
                buf.append("Unable to set the value [");
                buf.append(this.attributeValue);
                buf.append("] of attribute [");
                buf.append(this.attributeDefinition.getName());
                buf.append("] of target [");
                buf.append(target);
                buf.append("].");
                throw new org.j2eebuilder.InstanceException(buf.toString());
            }
            if (target instanceof org.j2eebuilder.model.ManagedTransientObject) this.isNew = ((org.j2eebuilder.model.ManagedTransientObject) target).getNew().booleanValue();
            String methodName = UtilityBean.getWriteMethod(this.attributeDefinition.getName());
            Expression expression = new Expression(target, methodName, new Object[] { this.attributeValue });
            expression.execute();
            bValid = true;
        } catch (org.j2eebuilder.InstanceException e) {
            log.warn(e.toString());
        } catch (org.j2eebuilder.DefinitionException e) {
            log.warn(e.toString());
        } catch (Exception e) {
            log.printStackTrace(e, LogManager.ERROR);
        }
        return bValid;
    }

    public String getHtmlElementID() throws JspException {
        return this.getComponentName() + "_" + this.getAttributeName();
    }

    public StringBuffer toHtml() {
        return (new StringBuffer()).append("<!-- setAttribute successfully called. -->");
    }
}
