package com.grooveapp.taglib;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.grooveapp.widget.Widget;
import com.grooveapp.widget.WidgetContext;
import com.grooveapp.widget.WidgetUtils;

public class EditTag extends BaseGrooveTag {

    protected Log log = LogFactory.getLog(getClass());

    private static final long serialVersionUID = 6697970491664649784L;

    protected Object object = null;

    protected String type = null;

    protected String field = null;

    @Override
    public int doEndTag() throws JspException {
        if (object != null) {
            log.debug("Found object [" + object + "] of type:" + object.getClass().getName());
            Widget widget = WidgetUtils.getWidgetForField(object.getClass(), field);
            log.debug("Got widget:" + widget.getClass() + " for field " + field);
            try {
                pageContext.getOut().write(widget.edit(new WidgetContext(pageContext, getAttributeMap()), object, field));
            } catch (IOException e) {
                log.error(e);
                throw new JspException(e);
            }
        } else if (type != null) {
            try {
                Class clazz = Class.forName(type);
                Widget widget = WidgetUtils.getWidgetForField(clazz, field);
                log.debug("Got widget:" + widget.getClass() + " for field " + field);
                pageContext.getOut().write(widget.edit(new WidgetContext(pageContext, getAttributeMap()), clazz.getSimpleName().toLowerCase(), field, ""));
            } catch (Exception e) {
                log.error(e);
                throw new JspException(e);
            }
        } else {
            throw new JspException("Both object and type can not be null!");
        }
        return EVAL_PAGE;
    }

    @Override
    public int doStartTag() throws JspException {
        return SKIP_BODY;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setObject(String object) {
        if (object.indexOf("${") == -1) {
            this.object = pageContext.findAttribute(object);
        } else {
            this.object = eval(object);
        }
    }

    public void setType(String type) {
        this.type = eval(type);
    }
}
