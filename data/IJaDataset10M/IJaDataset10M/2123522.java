package org.peony.client.web.components.form;

import org.peony.exceptions.RenderException;
import org.peony.standard.ui.SIComponent;
import org.peony.tools.StringUtil;

/**
 *  
 *
 *  @author     陈慧然
 *  @version    Id: Label.java, v 0.000 2008-5-1 下午01:59:55 陈慧然 Exp
 */
public class Label extends org.peony.client.web.components.WebComponents {

    private SIComponent value;

    private String forInput = StringUtil.empty;

    public String toStandardString() throws RenderException {
        if (StringUtil.isEmpty(getId())) throw new RenderException("Label(A Component) Can NOT has no id!");
        if (StringUtil.isEmpty(getForInput())) {
            return StringUtil.empty;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<label ").append("id= \"").append(getId()).append("\"").append(" for=\"").append(getForInput()).append("\"").append(StringUtil.isNotEmpty(getUIClass()) ? " class=\"" + getUIClass() + "\">" : ">").append(value == null ? empty : value.toStandardString()).append("</label>");
        return sb.toString();
    }

    public SIComponent getValue() {
        return value;
    }

    public void setValue(SIComponent value) {
        this.value = value;
    }

    public String getForInput() {
        return forInput;
    }

    public void setForInput(String forInput) {
        this.forInput = forInput;
    }
}
