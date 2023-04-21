package org.fantasy.cpp.core.html.query;

import javax.servlet.http.HttpServletRequest;
import org.fantasy.cpp.core.bean.ParamContext;

/**
 * 
 * @author 王文成
 * @version: 1.0
 * @since Mar 23, 2010
 */
public class CheckBoxHtml extends AbstractQueryHtml {

    public CheckBoxHtml(HttpServletRequest request, ParamContext param) {
        super(request, param);
    }

    public String getHtml() {
        StringBuffer html = new StringBuffer(512);
        html.append("<input type='hidden' name=\"" + code + "\" value=\"" + defValue + "\"></span>" + "" + name + "</span>");
        return html.toString();
    }
}
