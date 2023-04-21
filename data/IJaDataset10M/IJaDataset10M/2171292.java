package ces.platform.system.ui.organize.action;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import ces.platform.system.dbaccess.*;
import java.util.*;
import ces.platform.system.common.*;
import ces.coral.log.*;

/**
 * <p>����:
 * <font class=titlefont>
 * ����֯���Ͳ�������Action����
 * </font>
 * <p>����:
 * <font class=descriptionfont>
 * <br>��ʾ������ѡ�����֯���͵��¼���֯����
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
public class OrgTypeAction extends Action {

    static Logger logger = new Logger(ces.platform.system.ui.organize.action.OrgTypeAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String strOrgTypeID;
        Vector vTemp = null;
        String target;
        try {
            strOrgTypeID = Translate.translate(request.getParameter("orgTypeID"), Constant.PARAM_GET);
            request.setAttribute("orgTypeID", strOrgTypeID);
            OrganizeType oTemp = new OrganizeType(strOrgTypeID);
            vTemp = oTemp.getChildOrganizeType();
            oTemp = null;
        } catch (Exception e) {
            logger.error("OrgTypeAction error:" + e);
            target = "alert";
            request.setAttribute("status", Constant.FAILED_TO_SELF);
            request.setAttribute("messageKey", "system.error.datasource");
        }
        request.setAttribute("rs", vTemp);
        target = "success";
        return (mapping.findForward(target));
    }
}
