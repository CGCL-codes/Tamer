package com.jgeppert.struts2.jquery.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.Component;
import com.jgeppert.struts2.jquery.components.Progressbar;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 * @see Progressbar
 * @author <a href="http://www.jgeppert.com">Johannes Geppert</a>
 * 
 */
public class ProgressbarTag extends AbstractTopicTag {

    private static final long serialVersionUID = -7636943419071750985L;

    protected String value;

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Progressbar(stack, req, res);
    }

    protected void populateParams() {
        super.populateParams();
        Progressbar bar = (Progressbar) component;
        bar.setValue(value);
    }

    public void setValue(String value) {
        this.value = value;
    }
}
