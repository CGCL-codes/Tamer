package com.dcivision.framework.taglib.html;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.taglib.html.TextTag;
import com.dcivision.framework.AutoCompleteInputBoxManager;
import com.dcivision.framework.AutoCompleteInputBoxManagerFactory;
import com.dcivision.framework.Utility;

/**
 AutoCompleteInputBoxTag.java
 A autoSuggest text input tag.
 @author      Jim Zhou
 @company     DCIVision Limited
 @creation date   2006/09/19
 @version     $Revision: 1.3.4.3 $
 */
public class AutoCompleteInputBoxTag extends TextTag {

    private static final Log log = LogFactory.getLog(AutoCompleteInputBoxTag.class);

    private int listSize = 10;

    private String size = "25";

    private long timeOut = 2000;

    private String boxType = null;

    private String name = null;

    private String onReceive = null;

    private String onSelect = null;

    private String onTimeout = null;

    private String value = null;

    private String maxlength = null;

    private String style = null;

    private String id = null;

    private String title = null;

    private String tabindex = null;

    private boolean disabled = false;

    private boolean readonly = false;

    private String parameter = null;

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getTabindex() {
        return tabindex;
    }

    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
   * 
   */
    public AutoCompleteInputBoxTag() {
        super();
    }

    public int doStartTag() throws JspException {
        return this.doTag();
    }

    public int doEndTag() throws JspException {
        return super.doEndTag();
    }

    /**
   * if don't have right boxType,it will be a normal text tag,or else it will be a auto suggest text tag.
   * @throws                                      JspException
   * @return                                      int
   */
    public int doTag() throws JspException {
        AutoCompleteInputBoxManagerFactory autoCompleteInputBoxManagerFactory = AutoCompleteInputBoxManagerFactory.getInstance();
        if (autoCompleteInputBoxManagerFactory.getBoxTypeExists(this.boxType)) {
            AutoCompleteInputBoxManager autoCompleteInputBoxManager = autoCompleteInputBoxManagerFactory.getAutoCompleteInputBoxManager(this.boxType);
            try {
                return this.renderAutoCompleteBox(autoCompleteInputBoxManager);
            } catch (IOException e) {
                log.error(e, e);
                return TextTag.EVAL_BODY_BUFFERED;
            }
        } else {
            return this.renderHtmlInputTag();
        }
    }

    /**
   * render html code and output it.
   * @param autoCompleteInputBoxManager          	AutoCompleteInputBoxManager
   * @throws                 											IOException
   */
    public int renderAutoCompleteBox(AutoCompleteInputBoxManager autoCompleteInputBoxManager) throws IOException {
        JspWriter out = pageContext.getOut();
        StringBuffer outTemp = new StringBuffer();
        outTemp.append(this.renderInputTag());
        outTemp.append(this.renderScriptModule());
        out.println(outTemp.toString());
        return TextTag.EVAL_BODY_INCLUDE;
    }

    /**
   * transfer super.doStartTag().
   * @return                                      int
   */
    public int renderHtmlInputTag() throws JspException {
        return super.doStartTag();
    }

    /**
   * render html code expends attributes.
   * @return                                      html code string.
   */
    private String renderInputTag() {
        StringBuffer outTemp = new StringBuffer();
        outTemp.append("<input type='text' _extended='true' autocomplete='off' ");
        if (!Utility.isEmpty(this.getName())) {
            outTemp.append("id='" + this.getName() + "' ");
        }
        if (!Utility.isEmpty(this.getName())) {
            outTemp.append("name='" + this.getName() + "' ");
        }
        if (!Utility.isEmpty(this.getMaxlength())) {
            outTemp.append("maxlength='" + this.getMaxlength() + "' ");
        }
        if (!Utility.isEmpty(this.getTitle())) {
            outTemp.append("title='" + this.getTitle() + "' ");
        }
        if (!Utility.isEmpty(this.getSize())) {
            outTemp.append("size='" + this.getSize() + "' ");
        }
        if (!Utility.isEmpty(getStyleClass())) {
            outTemp.append("class=\"" + getStyleClass() + "\"");
        }
        if (!Utility.isEmpty(this.getStyle())) {
            outTemp.append("style=\"" + this.getStyle() + "\" ");
        }
        if (!Utility.isEmpty(this.getTabindex())) {
            outTemp.append("tabindex='" + this.getTabindex() + "' ");
        }
        if (!Utility.isEmpty(this.getValue())) {
            outTemp.append("value='" + this.getValue() + "' ");
        }
        if (this.getDisabled()) {
            outTemp.append("disabled='" + String.valueOf(this.getDisabled()) + "' ");
        }
        if (this.getReadonly()) {
            outTemp.append("readonly='" + String.valueOf(this.getReadonly()) + "' ");
        }
        outTemp.append("/>\n");
        return outTemp.toString();
    }

    /**
   * render javascript code expends attributes.
   * use DWR function and Script.aculo.us function.
   * @return                                      javascript code string.
   */
    private String renderScriptModule() {
        StringBuffer outTemp = new StringBuffer();
        outTemp.append("<div ");
        if (!Utility.isEmpty(this.getName())) {
            outTemp.append("id='" + this.getName() + "update' ");
        }
        outTemp.append(" style=' font-weight:normal; DISPLAY: none; BORDER-RIGHT: black 1px solid; BORDER-TOP: black 1px solid; BORDER-LEFT: black 1px solid; BORDER-BOTTOM: black 1px solid; BACKGROUND-COLOR: white' ></div>\n");
        StringBuffer opt = new StringBuffer();
        opt.append("fullSearch:true");
        if (this.getListSize() > 0) {
            opt.append(",choices:" + String.valueOf(this.getListSize()));
        }
        if (!Utility.isEmpty(this.getOnReceive())) {
            opt.append(",onReceive:'" + this.getOnReceive() + "'");
        }
        if (!Utility.isEmpty(this.getOnSelect())) {
            opt.append(",onSelect:'" + this.getOnSelect() + "'");
        }
        String timeOutEventHandle = "timeOutError";
        if (!Utility.isEmpty(this.getOnTimeout())) {
            timeOutEventHandle = this.getOnTimeout();
        }
        StringBuffer paramResult = new StringBuffer();
        if (!Utility.isEmpty(this.getParameter())) {
            String param = new String(this.getParameter());
            String[] params = param.split(",");
            for (int i = 0; i < params.length; i++, paramResult.append(",")) {
                paramResult.append("'" + params[i] + "'");
            }
            paramResult.deleteCharAt(paramResult.length() - 1);
        }
        String functionName = new String(this.getName().replace('-', '_'));
        outTemp.append("<script language=javascript type=text/javascript charset=utf-8>\n");
        outTemp.append("<!-- \n");
        outTemp.append(" autoCompleteBoxDWRFacade.getResultList([" + paramResult.toString() + "],'" + this.getBoxType() + "',{callback:function(data){eval('" + functionName + "Function(data)');},errorHandler:function(message){eval('" + timeOutEventHandle + "(message)');},timeout:" + String.valueOf(this.getTimeOut()) + "}); \n");
        outTemp.append(" function " + functionName + "Function (list){ \n");
        outTemp.append(" if (list != null && typeof list == 'object') {\n");
        if (!Utility.isEmpty(this.getOnReceive())) {
            outTemp.append("  eval('" + this.getOnReceive() + "(list)');\n");
        }
        outTemp.append(" // <![CDATA[ \n");
        outTemp.append(" new Autocompleter.Local('" + this.getName() + "','" + this.getName() + "update',list,{" + opt + "}); \n");
        outTemp.append(" // ]]> \n");
        outTemp.append(" }else{ \n");
        outTemp.append(" }\n");
        outTemp.append(" }\n");
        outTemp.append("//-->\n");
        outTemp.append("</script>\n");
        return outTemp.toString();
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    public int getListSize() {
        return listSize;
    }

    public void setListSize(int listSize) {
        if (listSize <= 0) {
            this.listSize = 10;
        } else {
            this.listSize = listSize;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOnReceive() {
        return onReceive;
    }

    public void setOnReceive(String onReceive) {
        this.onReceive = onReceive;
    }

    public String getOnSelect() {
        return onSelect;
    }

    public void setOnSelect(String onSelect) {
        this.onSelect = onSelect;
    }

    public String getOnTimeout() {
        return onTimeout;
    }

    public void setOnTimeout(String onTimeout) {
        this.onTimeout = onTimeout;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        if (timeOut <= 0) {
            this.timeOut = 2000;
        } else {
            this.timeOut = timeOut;
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }
}
