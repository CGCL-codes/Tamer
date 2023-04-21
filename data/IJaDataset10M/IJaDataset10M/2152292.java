package com.techstar.framework.ui.web.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;
import com.techstar.framework.ui.web.tag.utils.GridConstant;

/**
 * @author Administrator
 * 
 */
public class JHopDateTag extends JHopBaseTag {

    /**
	 * 输入文本框的Id
	 */
    private String name;

    /**
	 * 日期在文本框显示的初始值

	 * 
	 */
    private String initValue;

    /**
	 * 时间日期的格式 
	 */
    private String dateFormat;

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * 
	 */
    private boolean showSecond;

    /**
	 * @return the showSecond
	 */
    public boolean isShowSecond() {
        return showSecond;
    }

    /**
	 * @param showSecond the showSecond to set
	 */
    public void setShowSecond(boolean showSecond) {
        this.showSecond = showSecond;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * 是否需要带文本框 yes---需要 no---不需要
	 */
    private String genText = "yes";

    /**
	 * @return the dateFormat
	 */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
	 * @param dateFormat the dateFormat to set
	 */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
	 * @return the genText
	 */
    public String getGenText() {
        return genText;
    }

    /**
	 * @param genText
	 *            the genText to set
	 */
    public void setGenText(String genText) {
        this.genText = genText;
    }

    /**
	 * @param eventId
	 *            the eventId to set
	 */
    public String getInitValue() {
        return initValue;
    }

    /**
	 * jstl1.0需要用ExpressionUtil进行<c:out>的解析

	 * jstl1.1.2在前段直接用EL表达式${var}则jsp2.0\servlet2.4可以解析
	 * @author caojian 2006-12-25
	 * @param string
	 */
    public void setInitValue(String string) {
        if (null != string && string.indexOf("<c:out") >= 0 && string.indexOf("${") >= 0) {
            String value_ = string.substring(string.indexOf("${"), string.indexOf("}") + 1);
            try {
                initValue = (String) ExpressionUtil.evalNotNull("out", "value", value_, String.class, this, pageContext);
            } catch (JspException jex) {
                initValue = string;
            }
        } else {
            initValue = string;
        }
    }

    /**
	 * 获得事件发生源的id------在日期控件自动生成文本框的时候使用。

	 * 
	 * 
	 */
    private String getEventStrId() {
        String textId = "event" + this.name;
        return textId;
    }

    /**
	 * 生成htm
	 */
    public String getDivHtml() {
        StringBuffer htmlStr = new StringBuffer("");
        if (getGenText().equals("no")) {
            htmlStr.append("<img src='" + contextPath + GridConstant.PAGE_IMAGE_PATH + "data.gif'");
            htmlStr.append(" name=\"ss\" id='" + "' border=\"0\" style=\"cursor:hand\"");
            htmlStr.append(" onclick=\"return showCalendar('" + this.getEventStrId() + "','" + "'" + this.dateFormat + "'" + ");\"");
        } else if (getGenText().equals("yes")) {
            String strValue = "";
            if (showSecond) {
                htmlStr.append("<script> function eventChangeValue(Id,Id1){" + " var v=document.getElementById(Id);var v1=document.getElementById(Id1);" + " changeValue=v1.value;if(v1.value!=null)	{	" + "changeValue=changeValue+':01.0';}v.value=changeValue;}</script>");
                strValue = this.initValue;
                if (strValue != null && strValue.trim().length() > 0) {
                    strValue = strValue.substring(0, 16);
                }
            }
            if (showSecond) {
                htmlStr.append("<input type='text'" + " name='" + this.name + "_1' " + " id='" + this.getEventStrId() + "_1' value='" + strValue + "'");
                htmlStr.append(" size=\"20\"   onpropertychange=\"eventChangeValue('" + this.getEventStrId() + "','" + this.getEventStrId() + "_1')\"/>");
                htmlStr.append("<input type='hidden'" + " name='" + this.name + "' " + " id='" + this.getEventStrId() + "' value='" + this.initValue + "'");
                htmlStr.append(" size=\"20\"   />");
                htmlStr.append("&nbsp;");
                htmlStr.append("<img src='" + contextPath + GridConstant.PAGE_IMAGE_PATH + "data.gif'");
                htmlStr.append(" name=\"ss\" " + "' border=\"0\" style=\"cursor:hand\" ");
                htmlStr.append("  onclick=\"return showCalendar('" + this.getEventStrId() + "_1', " + "'" + this.dateFormat + "'" + ");\" ");
                htmlStr.append("/>");
            } else {
                htmlStr.append("<input type='text'" + " name='" + this.name + "' " + " id='" + this.getEventStrId() + "' value='" + this.initValue + "'");
                htmlStr.append(" size=\"20\"   />");
                htmlStr.append("&nbsp;");
                htmlStr.append("<img src='" + contextPath + GridConstant.PAGE_IMAGE_PATH + "data.gif'");
                htmlStr.append(" name=\"ss\" " + "' border=\"0\" style=\"cursor:hand\" ");
                htmlStr.append("  onclick=\"return showCalendar('" + this.getEventStrId() + "', " + "'" + this.dateFormat + "'" + ");\" ");
                htmlStr.append("/>");
            }
        }
        return htmlStr.toString();
    }

    public int doStartTag() throws JspException {
        try {
            contextPath = ((HttpServletRequest) pageContext.getRequest()).getContextPath();
            pageContext.getOut().print(this.getDivHtml());
        } catch (Exception e) {
        }
        return (SKIP_BODY);
    }

    public int doEndTag() throws JspException {
        return (EVAL_PAGE);
    }

    public void release() {
        super.release();
        name = null;
        dateFormat = null;
        genText = null;
    }
}
