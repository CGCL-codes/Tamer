package com.jgeppert.struts2.jquery.mobile.views.velocity.components;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.Component;
import com.jgeppert.struts2.jquery.mobile.components.CheckboxList;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 * @see CheckboxList
 * @author <a href="http://www.jgeppert.com">Johannes Geppert</a>
 * 
 */
public class CheckboxListDirective extends JqueryMobileAbstractDirective {

    public String getBeanName() {
        return "checkboxlist";
    }

    protected Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new CheckboxList(stack, req, res);
    }

    public int getType() {
        return BLOCK;
    }
}
