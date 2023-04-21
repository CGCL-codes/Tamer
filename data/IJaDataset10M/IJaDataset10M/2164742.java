package ces.platform.system.ui.user.action;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import java.util.*;
import ces.platform.system.dbaccess.*;
import ces.platform.system.common.*;
import ces.platform.system.ui.organize.form.OrgOpForm;
import ces.coral.log.*;

/**
 * <p>����:
 * <font class=titlefont>
 * ���û��б��¼������Action����
 * </font>
 * <p>����:
 * <font class=descriptionfont>
 * <br>���б��������ƶ���¼����
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
public class UserListMoveAction extends Action {

    static Logger logger = new Logger(ces.platform.system.ui.user.action.UserListMoveAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String target = "";
        try {
            String strSelShowOrder = request.getParameter("selChkShowOrder");
            String strSelUserID = request.getParameter("selChkUserID");
            String strTargetShowOrder = request.getParameter("targetShowOrder");
            String strTargetUserID = request.getParameter("targetUserID");
            String strMovDirection = Translate.translate(request.getParameter("moveDirection"), Constant.PARAM_GET);
            String organizeId = request.getParameter("organizeId");
            StringTokenizer stkShowOrder = new StringTokenizer(strSelShowOrder, ",");
            StringTokenizer stkUserID = new StringTokenizer(strSelUserID, ",");
            int nNum = stkShowOrder.countTokens();
            String[] showOrder = new String[nNum];
            String[] userID = new String[nNum];
            for (int i = 0; i < nNum; i++) {
                showOrder[i] = (String) stkShowOrder.nextToken();
                userID[i] = (String) stkUserID.nextToken();
            }
            if (strMovDirection.equals("prior")) {
                new User().movePrior(showOrder, userID, strTargetShowOrder, strTargetUserID, organizeId);
                target = "alert";
                request.setAttribute("status", Constant.SUCCESS_TO_UP);
            }
            if (strMovDirection.equals("next")) {
                new User().moveNext(showOrder, userID, strTargetShowOrder, strTargetUserID, organizeId);
                target = "alert";
                request.setAttribute("status", Constant.SUCCESS_TO_UP);
            }
        } catch (Exception e) {
            logger.error("UserListMoveAction error:" + e);
            request.setAttribute("status", Constant.FAILED_TO_UP);
            request.setAttribute("messageKey", "system.error.datasource");
            target = "alert";
        }
        return (mapping.findForward(target));
    }
}
