package org.fantasy.cpp.core.html.query;

import javax.servlet.http.HttpServletRequest;
import org.fantasy.cpp.core.bean.ParamContext;

/**
 *  普通日期输入框
 * @author: 王文成
 * @version: 1.0
 * @since 2009-11-11
 */
public class DateHtml extends AbstractQueryHtml {

    public DateHtml(HttpServletRequest request, ParamContext param) {
        super(request, param);
    }

    public String getHtml() {
        StringBuffer html = new StringBuffer(1024);
        html.append("<span class='" + SPAN_CLASS + "'> \n");
        html.append(getLabelName());
        html.append("<input id='" + code + "' type='hidden'  name='" + code + "'/>");
        html.append("<input type='text' id='" + getComboId() + "'" + getStyleHtml() + getClassHtml() + "/>");
        html.append("</span>\n");
        html.append(getScript());
        return html.toString();
    }

    public String getScript() {
        StringBuffer html = new StringBuffer(1024);
        html.append("<script type='text/javascript'> \n");
        html.append("$(function(){  	 \n");
        html.append("	var option = {onSelect:function(date){ \n");
        html.append("		$('#" + code + "').val(date.toString('yyyy-MM-dd')); \n");
        html.append("	}}; \n");
        html.append("	var now = new Date(); \n");
        html.append("	$('#" + getComboId() + "').datebox(option); \n");
        html.append("	var defValue = Tools.eval('" + defValue + "'); \n");
        html.append("	$('#" + getComboId() + "').combo('setText',defValue); \n");
        html.append("	$('#" + code + "').val(defValue); \n");
        html.append("});   \n");
        html.append("</script> \n");
        return html.toString();
    }
}
