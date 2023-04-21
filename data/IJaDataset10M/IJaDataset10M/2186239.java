package org.weblayouttag.tag.key;

import org.apache.commons.beanutils.BeanUtils;
import org.weblayouttag.tag.AbstractManagerTag;
import org.weblayouttag.tag.WebLayoutTagException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy Marek
 * @version Jul 27, 2005
 * @jsp.tag
 * 	name="key"
 * 	body-content="JSP"
 * 
 * TODO Write JSP tag description.
 */
public class KeyTagImpl extends AbstractManagerTag implements KeyTag {

    private List arguments = new ArrayList();

    private String attribute;

    private String bundle;

    private String key;

    public int doStartTag() throws JspException {
        super.doStartTag();
        return BodyTag.EVAL_BODY_BUFFERED;
    }

    public int doEndTag() throws JspException {
        Object tag = PARENT_FINDER.getLastTag(this.getPageContext(), this);
        if (tag == null) {
            handleException(new WebLayoutTagException("This tag must be nested inside of another tag"));
        }
        String message = this.getMvcManager().message(this.getPageContext(), this.getBundle(), null, this.getKey(), this.getArguments().toArray(), null);
        try {
            BeanUtils.setProperty(tag, this.getAttribute(), message);
        } catch (Exception e) {
            handleException(e);
        }
        return super.doEndTag();
    }

    public void release() {
        this.arguments = new ArrayList();
        this.attribute = null;
        this.bundle = null;
        this.key = null;
        super.release();
    }

    public void addArgument(String value) {
        this.getArguments().add(value);
    }

    public List getArguments() {
        return arguments;
    }

    /**
	 * @jsp.attribute
	 * 	required="true"
	 * 	rtexprvalue="true"
	 * 	type="java.lang.String"
	 * 
	 * TODO Write attribute description.
	 */
    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
	 * @jsp.attribute
	 * 	required="false"
	 * 	rtexprvalue="true"
	 * 	type="java.lang.String"
	 * 
	 * TODO Write attribute description.
	 */
    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    /**
	 * @jsp.attribute
	 * 	required="true"
	 * 	rtexprvalue="true"
	 * 	type="java.lang.String"
	 * 
	 * TODO Write attribute description.
	 */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
