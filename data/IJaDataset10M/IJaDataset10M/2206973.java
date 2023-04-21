package com.jgeppert.struts2.jquery.mobile.views.velocity.components;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.Component;
import com.jgeppert.struts2.jquery.mobile.components.FlipSwitch;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 * @see FlipSwitch
 * @author <a href="http://www.jgeppert.com">Johannes Geppert</a>
 * 
 */
public class FlipSwitchDirective extends JqueryMobileAbstractDirective {

    public String getBeanName() {
        return "flipSwitch";
    }

    protected Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new FlipSwitch(stack, req, res);
    }

    public int getType() {
        return BLOCK;
    }
}
