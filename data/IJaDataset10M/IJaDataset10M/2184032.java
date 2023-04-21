package org.fantasy.cpp.core.html.edit;

import javax.servlet.http.HttpServletRequest;
import org.fantasy.cpp.core.bean.ParamContext;

/**
 *  简单文本框
 * @author: wzy
 * @version: 1.0
 * @since 2010-4-22
 */
public class TextAreaHtml extends AbstractEditHtml {

    public TextAreaHtml(HttpServletRequest request, ParamContext param) {
        super(request, param);
    }

    public String getHtml() {
        StringBuffer html = new StringBuffer(1024);
        html.append("<textarea " + getIsReadonly(name) + getValidate() + " id='" + name + "'" + getClassHtml() + " name='" + name + "'" + getStyleHtml() + ">" + getValue());
        html.append("</textarea>\n");
        return html.toString();
    }
}
