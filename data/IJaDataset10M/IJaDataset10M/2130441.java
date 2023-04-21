package com.lullabysoft.pub.tag.tagcatalog;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import com.anaconda.pub.util.StringUtils;

/**
 * <p>Title: eBaoTech System Infrastructure</p>
 * <p>Description: tag class for output html select element </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: eBaoTech Corporation</p>
 * <p>Create Time: 2004/09/10</p>
 * @author qiang.zhao
 * @version 1.0
 * $Id: SelectTag.java,v 1.1 2006/04/12 09:15:59 hank_zhan Exp $
 * $Log: SelectTag.java,v $
 * Revision 1.1  2006/04/12 09:15:59  hank_zhan
 * Hank Commit for the pub folder
 *
 * Revision 1.2  2005/06/21 01:54:15  cvs
 * <No Comment Entered>
 *
 * Revision 1.1  2005/06/20 05:01:10  cvs
 * <No Comment Entered>
 *
 * Revision 1.1  2005/06/02 15:56:55  cvs
 * <No Comment Entered>
 *
 * Revision 1.1  2004/11/26 10:16:35  qiang.zhao
 * <No Comment Entered>
 *
 * Revision 1.3  2004/10/12 05:44:26  qiang.zhao
 * <No Comment Entered>
 *
 * Revision 1.2  2004/09/13 02:22:15  qiang.zhao
 * build
 *
 */
public class SelectTag extends InputTag {

    public SelectTag() {
    }

    /** default value of attribute nullMsg */
    private String dNullMsg = HtmlUtils.NULL_MSG_TEXT;

    /** the data collection */
    private Iterator dataSrc = null;

    /**
   * add onchange attribute to the attributes map
   * @param onchange String
   */
    public void setOnchange(String onchange) {
        addAttribute("onchange", onchange);
    }

    /**
   * set multiple attribute
   * @param multiple boolean
   */
    public void setMultiple(boolean multiple) {
        if (multiple) addAttribute("multiple", String.valueOf(multiple));
    }

    /**
   * set multiple attribute
   * @param multiple Object
   */
    public void setMultiple(Object multiple) {
        Object objMultiple = processDynaAttribute(multiple);
        if (objMultiple != null) {
            String strMultiple = objMultiple.toString();
            if (strMultiple.equalsIgnoreCase("true")) addAttribute("multiple", strMultiple);
        }
    }

    /**
   * set null option attribute
   * @param nullOption String
   */
    public void setNullOption(String nullOption) {
        if ("true".equalsIgnoreCase(nullOption) || "Y".equalsIgnoreCase(nullOption)) {
            addAttribute("nullOption", nullOption);
        }
    }

    /**
   * set all option attribute
   * @param allOption String
   */
    public void setAllOption(String allOption) {
        if ("true".equalsIgnoreCase(allOption) || "Y".equalsIgnoreCase(allOption)) {
            addAttribute("allOption", allOption);
        }
    }

    /**
   * set data collection attriubte
   * @param dataSrc Collection
   */
    public void setDataSrc(Collection dataSrc) {
        try {
            this.dataSrc = dataSrc.iterator();
            addAttribute("dataSrc", this.dataSrc);
        } catch (Exception ex) {
            this.dataSrc = null;
        }
    }

    /**
   * get resultant all onblur event string and put into the attributes map
   */
    protected void processOnblur() {
        String wholeOnblur = "";
        if (!StringUtils.isNullOrEmpty(this.beforeblur)) {
            if (beforeblur.lastIndexOf(";") != (beforeblur.length() - 1)) beforeblur = beforeblur.trim() + ";";
            wholeOnblur += beforeblur;
        }
        String endBracket = "";
        if (!StringUtils.isNullOrEmpty(this.onblur)) {
            if (onblur.lastIndexOf(";") != (onblur.length() - 1)) onblur = onblur.trim() + ";";
            wholeOnblur += onblur;
        }
        wholeOnblur += endBracket;
        if (!StringUtils.isNullOrEmpty(wholeOnblur)) addAttribute("onblur", wholeOnblur);
    }

    /**
   * get default value of attribute nullMsg
   * @return String
   */
    public String getDNullMsg() {
        return dNullMsg;
    }

    /**
   * set option value attribute
   * @param optionValue String
   */
    public void setOptionValue(String optionValue) {
        addAttribute("optionValue", optionValue);
    }

    /**
   * set option value attribute
   * @param optionText String
   */
    public void setOptionText(String optionText) {
        addAttribute("optionText", optionText);
    }

    /**
   * get output all html content of the tag
   * @throws Exception
   * @return String
   */
    protected String getOutHtml() throws Exception {
        return HtmlUtils.getSelect(this.getAttributes());
    }

    /**
   * set not null prompt message
   * @param req HttpServletRequest
   */
    protected void processNullMsg(HttpServletRequest req) {
        String sureNullMsg = this.getDNullMsg();
        if (this.getAttribute("nullMsg") != null) {
            sureNullMsg = (String) this.getAttribute("nullMsg");
        }
        sureNullMsg = "null����";
        this.addAttribute("nullMsg", sureNullMsg);
    }

    /**
   * deal with select option elment attribute
   */
    protected void processOption(HttpServletRequest req) {
        if (getAttribute("allOption") != null) {
            addAttribute("allOption", "���� ����");
        }
        if (getAttribute("nullOption") != null) {
            addAttribute("nullOption", "ȫ��ѡ ����");
        }
    }
}
