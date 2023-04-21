package ces.platform.system.ui.code.action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ces.platform.system.common.Constant;
import ces.platform.system.common.Translate;
import ces.platform.system.dbaccess.CodeCategory;
import ces.platform.system.dbaccess.CodeData;
import ces.platform.system.ui.code.form.CodeDataForm;

/**
 * <p>����:
 * <font class=titlefont>
 * ���޸��û���ϢAction����
 * </font>
 * <p>����:
 * <font class=descriptionfont>
 * <br>�޸�ѡ�����û���Ϣ
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
public class ModifyDataAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String target = "";
        try {
            CodeDataForm code = (CodeDataForm) form;
            int dataID = Integer.parseInt(request.getParameter("dataID"));
            int ctgrID = code.getCtgrID();
            String exFlag = code.getExFlag();
            String dataKey = Translate.translate(code.getDataKey(), Constant.PARAM_FORMBEAN);
            String dataValue = Translate.translate(code.getDataValue(), Constant.PARAM_FORMBEAN);
            String dataValidate = Translate.translate(code.getDataValidate(), Constant.PARAM_FORMBEAN);
            String dataRemark = Translate.translate(code.getDataRemark(), Constant.PARAM_FORMBEAN);
            String davalue = Translate.translate(code.getDavalue(), Constant.PARAM_FORMBEAN);
            String dataCount = request.getParameter("dataCount");
            CodeCategory ctgr = new CodeCategory();
            ctgr.setCtgrID(ctgrID);
            ctgr.load();
            CodeData coda = new CodeData();
            coda.setDataID(dataID);
            String strTemp = this.addTail(ctgr.getCtgrRuleFlag(), davalue, dataValue);
            coda.load(exFlag, String.valueOf(ctgrID));
            if (!strTemp.equals(coda.getDataValue()) && coda.isExist(ctgrID, strTemp, coda.getDataLayer())) {
                target = "alert";
                request.setAttribute("status", Constant.FAILED_TO_SELF_BACK);
                request.setAttribute("messageKey", "system.code.data.norepeat");
            } else {
                coda.setCtgrID(ctgrID);
                coda.setDataKey(dataKey);
                coda.setDataValue(strTemp);
                coda.setDataValidate(dataValidate);
                coda.setDataRemark(dataRemark);
                coda.doUpdate();
                target = "alert";
                request.setAttribute("status", Constant.SUCCESS_TO_UP);
                request.setAttribute("messageKey", "system.code.midfy.sucess");
            }
        } catch (Exception e) {
            e.printStackTrace();
            target = "alert";
            request.setAttribute("status", Constant.FAILED_TO_SELF);
            request.setAttribute("messageKey", "system.error.datasource");
        }
        return (mapping.findForward(target));
    }

    private String addTail(String ruleFlag, String value, String dataValue) {
        if ("1".equals(ruleFlag)) {
            if (value != null && !"null".equals(value) && value.length() > 0) return value + dataValue; else return dataValue;
        } else {
            return dataValue;
        }
    }
}
