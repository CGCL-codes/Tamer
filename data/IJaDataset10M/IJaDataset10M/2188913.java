package ces.platform.system.ui.organize.form;

import org.apache.struts.action.*;

/**
 * <p>����:
 * <font class=titlefont>
 * ����֯����Form����
 * </font>
 * <p>����:
 * <font class=descriptionfont>
 * <br>
 * </font>
 * <p>�汾��:
 * <font class=versionfont>
 * Copyright (c) 2.50.2003.1120
 * </font>
 * <p>��˾:
 * <font class=companyfont>
 * �Ϻ�������Ϣ��չ���޹�˾
 * </font>
 * @author ����
 * @version 2.50.2003.1120
 */
public class OrgOpForm extends ActionForm {

    private String strOrgTypeID;

    private String strOrgID;

    private String strOrgName;

    private String strSelOrgTypeID;

    private String strSelOrgTypeName;

    private String strSelect;

    private String strSel;

    private String[] strChkDel = null;

    public String[] getChkDel() {
        return strChkDel;
    }

    public void setChkDel(String[] strChkDel) {
        this.strChkDel = strChkDel;
    }

    public String getOrgID() {
        return strOrgID;
    }

    public void setOrgID(String strOrgID) {
        this.strOrgID = strOrgID;
    }

    public String getOrgName() {
        return strOrgName;
    }

    public void setOrgName(String strOrgName) {
        this.strOrgName = strOrgName;
    }

    public String getOrgTypeID() {
        return strOrgTypeID;
    }

    public void setOrgTypeID(String strOrgTypeID) {
        this.strOrgTypeID = strOrgTypeID;
    }

    public String getSelect() {
        return strSelect;
    }

    public void setSelect(String strSelect) {
        this.strSelect = strSelect;
    }

    public String getSelOrgTypeID() {
        return strSelOrgTypeID;
    }

    public void setSelOrgTypeID(String strSelOrgTypeID) {
        this.strSelOrgTypeID = strSelOrgTypeID;
    }

    public String getSel() {
        return strSel;
    }

    public void setSel(String strSel) {
        this.strSel = strSel;
    }

    public String getSelOrgTypeName() {
        return strSelOrgTypeName;
    }

    public void setSelOrgTypeName(String strSelOrgTypeName) {
        this.strSelOrgTypeName = strSelOrgTypeName;
    }
}
