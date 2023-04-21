package ces.platform.system.ui.data.form;

import org.apache.struts.action.ActionForm;

/**
 * <p>����:
 * <font class=titlefont>
 * ���༭Sql��Where���Form����
 * </font>
 * <p>����:
 * <font class=descriptionfont>
 * <br>����transToChinese.jspҳ��Ľ���Ԫ��
 * </font>
 * <p>�汾��:
 * <font class=versionfont>
 * Copyright (c) 2.50.2004.0218
 * </font>
 * <p>��˾:
 * <font class=companyfont>
 * �Ϻ�������Ϣ��չ���޹�˾
 * </font>
 * @author �Ĺ���
 * @version 2.50.2004.0218
 */
public class TransToChineseForm extends ActionForm {

    protected String sqlWhereContent;

    public void setSqlWhereConten(String strContent) {
        this.sqlWhereContent = strContent;
    }

    public String getSqlWhereContent() {
        return this.sqlWhereContent;
    }
}
