package com.jgeppert.struts2.jquery.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractClosingTag;
import com.jgeppert.struts2.jquery.components.AccordionItem;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @see AccordionItem
 * @author <a href="http://www.jgeppert.com">Johannes Geppert</a>
 */
public class AccordionItemTag extends AbstractClosingTag {

    private static final long serialVersionUID = -270033824138017378L;

    protected String title;

    protected String onClickTopics;

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new AccordionItem(stack, req, res);
    }

    protected void populateParams() {
        super.populateParams();
        AccordionItem item = (AccordionItem) component;
        item.setTitle(title);
        item.setOnClickTopics(onClickTopics);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOnClickTopics(String onClickTopics) {
        this.onClickTopics = onClickTopics;
    }
}
