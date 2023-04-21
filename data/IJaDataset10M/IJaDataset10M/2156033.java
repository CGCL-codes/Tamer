package com.dcivision.framework.taglib.layout;

import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.dcivision.dms.DmsOperationConstant;
import com.dcivision.dms.bean.DmsDefaultProfileSetting;
import com.dcivision.framework.Debugger;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.MessageResourcesFactory;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.SystemFunctionConstant;
import com.dcivision.framework.SystemParameterConstant;
import com.dcivision.framework.SystemParameterFactory;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.Utility;

/**
  ColumnHeaderTag.java

  <p>This class is to extend the BodyTagSupport provided by J2EE. </p>
  <p>Purpose to display the customized column header using pass in String array<p>
  <p>Modification in "layout.tld"<br>
  <pre>
     [FROM]
     &lt;tagclass&gt;javax.servlet.jsp.tagxt.BodyTagSupport&lt;/tagclass&gt;
     [TO]
     &lt;tagclass&gt;com.dcivision.framework.taglib.layout.ColumnHeaderTag&lt;/tagclass&gt;

     [ADDED]
     &lt;attribute&gt;
     &lt;name&gt;labelMap&lt;/name&gt;
     &lt;required&gt;true&lt;/required&gt;
     &lt;rtexprvalue&gt;true&lt;/rtexprvalue&gt;
     &lt;/attribute&gt;
     &lt;attribute&gt;
     &lt;name&gt;form&lt;/name&gt;
     &lt;required&gt;true&lt;/required&gt;
     &lt;rtexprvalue&gt;true&lt;/rtexprvalue&gt;
     &lt;/attribute&gt;
     &lt;attribute&gt;
     &lt;name&gt;sortAttribute&lt;/name&gt;
     &lt;required&gt;true&lt;/required&gt;
     &lt;rtexprvalue&gt;true&lt;/rtexprvalue&gt;
     &lt;/attribute&gt;
     &lt;attribute&gt;
     &lt;name&gt;sortOrder&lt;/name&gt;
     &lt;required&gt;true&lt;/required&gt;
     &lt;rtexprvalue&gt;true&lt;/rtexprvalue&gt;
     &lt;/attribute&gt;
     &lt;attribute&gt;
     &lt;name&gt;checkColumn&lt;/name&gt;
     &lt;required&gt;false&lt;/required&gt;
     &lt;rtexprvalue&gt;true&lt;/rtexprvalue&gt;
     &lt;/attribute&gt;
     &lt;attribute&gt;
     &lt;name&gt;editColumn&lt;/name&gt;
     &lt;required&gt;false&lt;/required&gt;
     &lt;rtexprvalue&gt;true&lt;/rtexprvalue&gt;
     &lt;/attribute&gt;
     &lt;attribute&gt;
     &lt;name&gt;editLabel&lt;/name&gt;
     &lt;required&gt;false&lt;/required&gt;
     &lt;rtexprvalue&gt;true&lt;/rtexprvalue&gt;
     &lt;/attribute&gt;
     &lt;attribute&gt;
     &lt;name&gt;copyColumn&lt;/name&gt;
     &lt;required&gt;false&lt;/required&gt;
     &lt;rtexprvalue&gt;true&lt;/rtexprvalue&gt;
     &lt;/attribute&gt;

  </pre>
  </p>

  <p>USAGE:</p>
  <p>The following example shows how to show the customized column header using pass in String array</p>
  <p><code>&lt;layout:columnHeader <strong>form='listUserRecordForm'</strong> <strong>labelMap='<%=labelMap%>'</strong></p>
  <p><strong> sortAttribute='sortAttr'</strong> <strong>sortOrder='sortOrder'</strong>/&gt;</code></p>

    @author          Zoe Shum
    @company         DCIVision Limited
    @creation date   09/07/2003
    @version         $Revision: 1.46.2.9 $
*/
public class ColumnHeaderTag extends BodyTagSupport {

    public static final String REVISION = "$Revision: 1.46.2.9 $";

    private static final Log log = LogFactory.getLog(ColumnHeaderTag.class);

    public static final String PARA_DM_BG_IMAGE = "LayoutTableHeaderGray.gif";

    public static final String PARA_DOC_BG_IMAGE = "LayoutTableHeaderBlue.gif";

    public static final String PARA_FORM_BG_IMAGE = "LayoutTableHeaderOrange.gif";

    public static final String PARA_FLOW_BG_IMAGE = "LayoutTableHeaderPurple.gif";

    protected static String COLUMN_NAME[] = { "USER_DEF_1", "USER_DEF_2", "USER_DEF_3", "USER_DEF_4", "USER_DEF_5", "USER_DEF_6", "USER_DEF_7", "USER_DEF_8", "USER_DEF_9", "USER_DEF_10" };

    protected String[][] m_lLabelMap = null;

    protected String m_sForm = "";

    protected String m_sSortOrder = "";

    protected String m_sSortAttribute = "";

    protected String m_sEditLabel = null;

    protected String m_navMode = null;

    protected boolean m_bCheckColumn = true;

    protected boolean m_bEditColumn = true;

    protected boolean m_bCopyColumn = true;

    protected String[][] iconMap = null;

    protected String preference = SystemParameterFactory.getSystemParameter(SystemParameterConstant.PREFERENCE);

    private String m_sCheckColumnNo = "";

    protected int showAfter = 0;

    protected String displayObjListName = null;

    protected String showValue = "";

    protected int showListCount = 0;

    public ColumnHeaderTag() {
        super();
    }

    /**
   *  Method getLabelMap() - Getter of the passed in column headers
   *
   *  @param      No pass in parameter
   *  @return     String[][]                   passing in column header in String[][]
   *  @throws     No exception throws
   */
    public String[][] getLabelMap() {
        return this.m_lLabelMap;
    }

    /**
   *  Method setLabelMap() - Setter of the passed in column headers
   *
   *  @param      String[][]                    passed in column header in String[][]
   *  @return     void
   *  @throws     No exception throws
   */
    public void setLabelMap(String[][] labelMap) {
        this.m_lLabelMap = labelMap;
    }

    /**
   *  Method doStartTag() - the default method called for <layout:columnHeader>; It retrieves the passed in string array
   *                        and format the corresponging sort information for listing.
   *
   *  @param      No pass in parameter
   *  @return     No return value
   *  @throws     JspException
   */
    public int doStartTag() throws JspException {
        if (this.m_lLabelMap == null) {
            throw new JspTagException("ColumnHeaderTag: no header String array defined");
        }
        this.getPreference();
        try {
            pageContext.getOut().print(this.src());
            return (SKIP_BODY);
        } catch (Exception e) {
            throw new JspException(e);
        }
    }

    public String[][] getIconMap() {
        return iconMap;
    }

    public void setIconMap(String[][] iconMap) {
        this.iconMap = iconMap;
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    /**
   *  Method src() - content of html codes for column header
   *
   *  @param      No pass in parameter
   *  @return     StringBuffer             The StringBuffer of html codes for coulmn header
   *  @throws     JspException
   */
    protected java.lang.StringBuffer src() throws javax.servlet.jsp.JspException {
        SessionContainer sessionContainer = (SessionContainer) this.pageContext.getSession().getAttribute(GlobalConstant.SESSION_CONTAINER_KEY);
        StringBuffer buffer = new StringBuffer();
        StringBuffer buffer1 = new StringBuffer();
        StringBuffer buffer2 = new StringBuffer();
        String contextPath = (String) this.pageContext.getServletContext().getAttribute(GlobalConstant.CONTEXT_PATH_KEY);
        String functionCode = (String) pageContext.getRequest().getAttribute(GlobalConstant.FUNCTION_CODE_KEY);
        try {
            for (int i = 0; i < m_lLabelMap.length; i++) {
                if (i == 0) {
                    if (m_bCheckColumn) {
                        if (!Utility.isEmpty(m_sCheckColumnNo) && !"null".equals(m_sCheckColumnNo)) {
                            buffer.append("<th nowrap><div class=\"sep\"><input type=\"checkbox\" name=\"PARADM_CB_ALL_NONE" + m_sCheckColumnNo + "\" onclick=\"OpToggleSelectAllNone" + m_sCheckColumnNo + "(this, " + m_sForm + ")\"></div></th>\n");
                        } else {
                            buffer.append("<th nowrap><div class=\"sep\"><input type=\"checkbox\" name=\"PARADM_CB_ALL_NONE\" onclick=\"OpToggleSelectAllNone(this, " + m_sForm + ")\"></div></th>\n");
                        }
                    }
                    if (m_bEditColumn) {
                        if (m_sEditLabel == null) {
                            buffer.append("<th nowrap><div class=\"sep\"><nobr>" + MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "common.label.edit") + "</nobr></div></th>\n");
                        } else if ("".equals(m_sEditLabel)) {
                            buffer.append("<th nowrap><div class=\"sep\">&nbsp;</div></th>\n");
                        } else {
                            buffer.append("<th nowrap><div class=\"sep\"><nobr>" + MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), this.m_sEditLabel) + "</nobr></div></th>\n");
                        }
                    }
                    if (m_bCopyColumn) {
                        buffer.append("<th nowrap><div class=\"sep\"><nobr>" + MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "common.label.copy") + "</nobr></div></th>\n");
                    }
                } else {
                    List objList = (List) this.pageContext.getSession().getServletContext().getAttribute("DMS_DEF_PROFILE");
                    if (showAfter > 0 && i == showAfter) {
                        if (!Utility.isEmpty(displayObjListName) && !Utility.isEmpty(showValue) && !Utility.isEmpty(objList)) {
                            String[] customFieldArray = TextUtility.splitString(showValue, "|");
                            String columnName = "";
                            if ((customFieldArray != null) && (customFieldArray.length > 0)) {
                                if (!Utility.isEmpty(customFieldArray[0]) && !"0".equals(customFieldArray[0])) {
                                    String[] selectDefaultProfile = TextUtility.splitString(customFieldArray[0], ",");
                                    for (int x = 0; x < selectDefaultProfile.length; x++) {
                                        Integer pos = new Integer(selectDefaultProfile[x]);
                                        if (pos.intValue() > 0) {
                                            columnName = COLUMN_NAME[TextUtility.parseInteger(selectDefaultProfile[x]) - 1];
                                            DmsDefaultProfileSetting setting = (DmsDefaultProfileSetting) objList.get((pos.intValue() - 1));
                                            buffer1.append(getAdditionalColumn(setting.getFieldName(), contextPath, columnName));
                                        }
                                    }
                                }
                                if (customFieldArray.length > 1) {
                                    if (!Utility.isEmpty(customFieldArray[1]) && !"0".equals(customFieldArray[1])) {
                                        String[] selectDefaultProfile = TextUtility.splitString(customFieldArray[1], ",");
                                        for (int x = 0; x < selectDefaultProfile.length; x++) {
                                            if (!functionCode.equals(SystemFunctionConstant.DMS_EMPTY_FILE)) {
                                                if ("1".equals(selectDefaultProfile[x])) {
                                                    buffer2.append(getAdditionalColumn(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "dms.label.reference_no"), contextPath, "REFERENCE_NO"));
                                                }
                                            }
                                            if ("2".equals(selectDefaultProfile[x])) {
                                                buffer2.append(getAdditionalColumn(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "dms.label.description"), contextPath, "DESCRIPTION"));
                                            }
                                        }
                                    }
                                }
                                if (customFieldArray.length > 2 && DmsOperationConstant.DEFAULT_PROFILE_SETTING_ASC.equals(customFieldArray[2])) {
                                    buffer.append(buffer1);
                                    buffer.append(buffer2);
                                } else {
                                    buffer.append(buffer2);
                                    buffer.append(buffer1);
                                }
                            }
                        }
                    }
                    buffer.append("<th nowrap><div class=\"sep\">");
                    if (showListCount == 0) {
                        if (!GlobalConstant.NOT_AVAILABLE.equals(m_lLabelMap[i][1])) {
                            if (GlobalConstant.LIST_ACSENDING.equals(m_sSortOrder) && m_lLabelMap[i][1].equals(m_sSortAttribute)) {
                                buffer.append("<a href=\"#\"   class=\"sortable_column_header\"  onClick=\"return(OpList(" + m_sForm + ",'" + m_lLabelMap[i][1] + "','" + GlobalConstant.LIST_DECSENDING + "','" + m_navMode + "'));\">");
                            } else {
                                buffer.append("<a href=\"#\"   class=\"sortable_column_header\"  onClick=\"return(OpList(" + m_sForm + ",'" + m_lLabelMap[i][1] + "','" + GlobalConstant.LIST_ACSENDING + "','" + m_navMode + "'));\">");
                            }
                        }
                    } else {
                        if (!GlobalConstant.NOT_AVAILABLE.equals(m_lLabelMap[i][1])) {
                            if (GlobalConstant.LIST_ACSENDING.equals(m_sSortOrder) && m_lLabelMap[i][1].equals(m_sSortAttribute)) {
                                buffer.append("<a href=\"#\"   class=\"sortable_column_header\" onClick=\"return(OpList" + showListCount + "(" + m_sForm + ",'" + m_lLabelMap[i][1] + "','" + GlobalConstant.LIST_DECSENDING + "','" + m_navMode + "'));\">");
                            } else {
                                buffer.append("<a href=\"#\"  class=\"sortable_column_header\" onClick=\"return(OpList" + showListCount + "(" + m_sForm + ",'" + m_lLabelMap[i][1] + "','" + GlobalConstant.LIST_ACSENDING + "','" + m_navMode + "'));\">");
                            }
                        }
                    }
                    if (!GlobalConstant.NOT_AVAILABLE.equals(m_lLabelMap[i][1])) {
                        if (GlobalConstant.LIST_ACSENDING.equals(m_sSortOrder) && m_lLabelMap[i][1].equals(m_sSortAttribute)) {
                            buffer.append("<div class=\"sortAsc\">");
                        } else if (GlobalConstant.LIST_DECSENDING.equals(m_sSortOrder) && m_lLabelMap[i][1].equals(m_sSortAttribute)) {
                            buffer.append("<div class=\"sortDesc\">");
                        }
                    }
                    if (m_lLabelMap[i][0].indexOf("*_") >= 0) {
                        buffer.append(m_lLabelMap[i][0].substring(2, m_lLabelMap[i][0].length()));
                    } else {
                        String iconFile = null;
                        if (!Utility.isEmpty(this.iconMap)) {
                            for (int j = 0; j < this.iconMap.length; j++) {
                                if (m_lLabelMap[i][0].equals(this.iconMap[j][0])) {
                                    iconFile = this.iconMap[j][1];
                                    break;
                                }
                            }
                        }
                        if (!Utility.isEmpty(iconFile)) {
                            buffer.append("<img src=\"" + contextPath + "/theme/pref" + this.preference + "/img/common/" + iconFile + "\" align=\"absmiddle\" border=\"0\">");
                        } else {
                            if (Debugger.isShowLabelKey(pageContext)) {
                                buffer.append("[" + m_lLabelMap[i][0] + "]:");
                            }
                            buffer.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), m_lLabelMap[i][0]));
                        }
                    }
                    if (!GlobalConstant.NOT_AVAILABLE.equals(m_lLabelMap[i][1])) {
                        buffer.append("</a>");
                    }
                    if (m_lLabelMap[i][1].equals(m_sSortAttribute)) {
                        buffer.append("</div>");
                    }
                    buffer.append("</div></th>\n");
                }
            }
        } catch (Exception e) {
            log.error(e, e);
        }
        adjustColumnSeparator(buffer);
        return buffer;
    }

    /**
   *  Method getForm() - Getter of the passed in form name
   *
   *  @param      No pass in parameter
   *  @return     String                      Value property form name
   *  @throws     No exception throws
   */
    public String getForm() {
        return this.m_sForm;
    }

    /**
   *  Method setForm() - Setter of the passed in form name
   *
   *  @param      form                        Value property form name
   *  @return     void
   *  @throws     No exception throws
   */
    public void setForm(String form) {
        this.m_sForm = form;
    }

    /**
   *  Method getSortOrder() - Getter of the passed in sort order
   *
   *  @param      No pass in parameter
   *  @return     String                      Value property sort order
   *  @throws     No exception throws
   */
    public String getSortOrder() {
        return this.m_sSortOrder;
    }

    /**
   *  Method setSortOrder() - Setter of the passed in sort order
   *
   *  @param      sortOrder              Value property passed in sort order
   *  @return     void
   *  @throws     No exception throws
   */
    public void setSortOrder(String sortOrder) {
        this.m_sSortOrder = sortOrder;
    }

    /**
   *  Method getSortAttribute() - Getter of the passed in sort attribute
   *
   *  @param      No pass in parameter
   *  @return     String                      Value property sort attribute
   *  @throws     No exception throws
   */
    public String getSortAttribute() {
        return this.m_sSortAttribute;
    }

    /**
   *  Method setSortAttribute() - Setter of the passed in sort attribute
   *
   *  @param      sortAttribute              Value property passed in sort attribute
   *  @return     void
   *  @throws     No exception throws
   */
    public void setSortAttribute(String sortAttribute) {
        this.m_sSortAttribute = sortAttribute;
    }

    /**
   *  Method getNavMode() - Getter of the passed in form name
   *
   *  @param      No pass in parameter
   *  @return     String                      Value property form name
   *  @throws     No exception throws
   */
    public String getNavMode() {
        return this.m_navMode;
    }

    /**
   *  Method setNavMode() - Setter of the passed in form name
   *
   *  @param      form                        Value property form name
   *  @return     void
   *  @throws     No exception throws
   */
    public void setNavMode(String navMode) {
        this.m_navMode = navMode;
    }

    /**
   *  Method getCheckColumn() - Getter of the boolean value to have "check all/none" column header or not.
   *
   *  @param      No pass in parameter
   *  @return     boolean                      Value property of boolean value
   *  @throws     No exception throws
   */
    public boolean getCheckColumn() {
        return this.m_bCheckColumn;
    }

    /**
   *  Method setCheckColumn() - Setter of boolean value to have "check all/none" column header or not.
   *
   *  @param      checkColumn              Value property passed to have "check all/none" column header
   *  @return     void
   *  @throws     No exception throws
   */
    public void setCheckColumn(boolean checkColumn) {
        this.m_bCheckColumn = checkColumn;
    }

    /**
   * Method getCheckColumnNo() - Setter of checkColumn Name for multiple Lists in a page. example: ListFormSubmissionDetail.jsp
   * @return
   */
    public String getCheckColumnNo() {
        return this.m_sCheckColumnNo;
    }

    /**
   * setCheckColumnNo() - Getter of checkColumn Name for multiple Lists in a page. example: ListFormSubmissionDetail.jsp
   * @param checkColumnNo
   */
    public void setCheckColumnNo(String checkColumnNo) {
        this.m_sCheckColumnNo = checkColumnNo;
    }

    /**
   *  Method getEditColumn() - Getter of the boolean value to have the "edit" column or not.
   *
   *  @param      No pass in parameter
   *  @return     boolean                      "boolean" value  property of having "edit" column
   *  @throws     No exception throws
   */
    public boolean getEditColumn() {
        return this.m_bEditColumn;
    }

    /**
   *  Method setEditColumn() - Setter of the boolean value to have the "edit" column or not.
   *
   *  @param      editColumn                    "boolean" value property of having "edit" column
   *  @return     void
   *  @throws     No exception throws
   */
    public void setEditColumn(boolean editColumn) {
        this.m_bEditColumn = editColumn;
    }

    /**
   *  Method getEditLabel() - Getter of the boolean value to have the "edit" column or not.
   *
   *  @param      No pass in parameter
   *  @return     boolean                      "boolean" value  property of having "edit" column
   *  @throws     No exception throws
   */
    public String getEditLabel() {
        return this.m_sEditLabel;
    }

    /**
   *  Method setEditLabel() - Setter of the boolean value to have the "edit" column or not.
   *
   *  @param      editLabel                    "boolean" value property of having "edit" column
   *  @return     void
   *  @throws     No exception throws
   */
    public void setEditLabel(String editLabel) {
        this.m_sEditLabel = editLabel;
    }

    /**
   *  Method getCopyColumn() - Getter of the boolean value to have the "copy" column or not.
   *
   *  @param      No pass in parameter
   *  @return     boolean                      "boolean" value  property of having "copy" column
   *  @throws     No exception throws
   */
    public boolean getCopyColumn() {
        return this.m_bCopyColumn;
    }

    /**
   *  Method setCopyColumn() - Setter of the boolean value to have the "copy" column or not.
   *
   *  @param      copyColumn                    "boolean" value property of having "copy" column
   *  @return     void
   *  @throws     No exception throws
   */
    public void setCopyColumn(boolean copyColumn) {
        this.m_bCopyColumn = copyColumn;
    }

    public int getShowAfter() {
        return this.showAfter;
    }

    public void setShowAfter(int showAfter) {
        this.showAfter = showAfter;
    }

    public String getDisplayObjListName() {
        return this.displayObjListName;
    }

    public void setDisplayObjListName(String displayObjListName) {
        this.displayObjListName = displayObjListName;
    }

    public String getShowValue() {
        return this.showValue;
    }

    public void setShowValue(String showValue) {
        this.showValue = showValue;
    }

    public int getShowListCount() {
        return showListCount;
    }

    public void setShowListCount(int showlistCount) {
        showListCount = showlistCount;
    }

    /**
   *  Method release()
   */
    public void release() {
        super.release();
        m_lLabelMap = null;
        m_sForm = null;
        m_sSortOrder = null;
        m_sSortAttribute = null;
        m_sEditLabel = null;
        m_navMode = null;
        m_bCheckColumn = true;
        m_bEditColumn = true;
        m_bCopyColumn = true;
        preference = null;
    }

    /** create the image path of the calendar icon
   * @return the string of the calendar icon image path
   * @throws JspException throws JspException
   */
    protected String imgSrc(String buttonFileName) throws javax.servlet.jsp.JspException {
        String contextPath = (String) this.pageContext.getServletContext().getAttribute(GlobalConstant.CONTEXT_PATH_KEY);
        return (contextPath + "/img/" + this.preference + "/common/" + buttonFileName);
    }

    protected StringBuffer getAdditionalColumn(String labelMessage, String contextPath, String columnName) throws javax.servlet.jsp.JspException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<th nowrap><div class=\"sep\">");
        if (GlobalConstant.LIST_ACSENDING.equals(m_sSortOrder)) {
            buffer.append("<a href=\"#\" onClick=\"return(OpList(" + m_sForm + ",'" + columnName + "','" + GlobalConstant.LIST_DECSENDING + "','" + m_navMode + "'));\">");
        } else {
            buffer.append("<a href=\"#\" onClick=\"return(OpList(" + m_sForm + ",'" + columnName + "','" + GlobalConstant.LIST_ACSENDING + "','" + m_navMode + "'));\">");
        }
        if (GlobalConstant.LIST_ACSENDING.equals(m_sSortOrder) && columnName.equals(m_sSortAttribute)) {
            buffer.append("<div class=\"sortAsc\">");
        } else if (GlobalConstant.LIST_DECSENDING.equals(m_sSortOrder) && columnName.equals(m_sSortAttribute)) {
            buffer.append("<div class=\"sortDesc\">");
        }
        buffer.append(labelMessage);
        buffer.append("</a>");
        if (columnName.equals(m_sSortAttribute)) {
            buffer.append("</div>");
        }
        buffer.append("</div></th>\n");
        return buffer;
    }

    protected void adjustColumnSeparator(StringBuffer sb) {
        sb.replace(0, "<th nowrap><div class=\"sep\">".length(), "<th nowrap><div class=\"firstsep\">");
    }

    /**
   * get the perference
   */
    private void getPreference() {
        SessionContainer sessionContainer = (SessionContainer) this.pageContext.getSession().getAttribute(GlobalConstant.SESSION_CONTAINER_KEY);
        if ((sessionContainer != null) && (sessionContainer.getUserRecord() != null)) {
            if (sessionContainer.getUserRecord().getPreference() != null) {
                this.preference = sessionContainer.getUserRecord().getPreference().toString();
            }
        }
    }
}
