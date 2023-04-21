package ces.sf.oa.leave.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ces.arch.action.CommonBaseAction;
import ces.arch.form.BaseForm;
import ces.com.dbo.DBOperation;
import ces.com.dbo.ERDBOperation;
import ces.sf.oa.leave.bo.LeaveBo;
import ces.sf.oa.util.DBUtil;
import ces.sf.oa.util.StringUtil;

/**
 * 
 * <p>
 * Title: Ϊ���ķ����AJAX������
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: ces
 * </p>
 * 
 * @author �ƺ���
 * @version 1.0.20080110
 */
public class DealFormForAjax extends CommonBaseAction {

    protected ActionForward process(ActionMapping mapping, BaseForm form, HttpServletRequest request, HttpServletResponse response, ActionErrors errors) throws Exception {
        String result = "";
        String sign = request.getParameter("method");
        if (sign != null && !sign.equals("")) {
            if (sign.equals("getInceptNo")) {
                result = this.getInceptNo(request);
            } else if (sign.equals("issue")) {
                result = issue(request);
            }
        } else {
            result = "error";
        }
        response.setContentType("text/html;charset=GBK");
        response.getWriter().print(result);
        return mapping.findForward(null);
    }

    private String getInceptNo(HttpServletRequest request) {
        String result = "";
        LeaveBo bo = new LeaveBo();
        String inceptType = request.getParameter("type") == null ? "" : request.getParameter("type");
        String year = request.getParameter("year") == null ? "" : request.getParameter("year");
        result = bo.getInceptCodeNo(year, inceptType);
        return result;
    }

    /**
	 * �鵵
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param errors
	 * @return
	 * @throws Exception
	 */
    protected String issue(HttpServletRequest request) throws Exception {
        String jhid = request.getParameter("jhid");
        if (StringUtil.isEmpty(jhid)) {
            return "����ID����Ϊ��";
        }
        System.out.println("����ID����Ϊ��" + jhid);
        DBOperation dbo = null;
        Connection conn = null;
        dbo = new ERDBOperation("oa");
        Statement stmt = null;
        try {
            conn = dbo.getConnection();
            stmt = conn.createStatement();
            String sqlStr = "";
            sqlStr = "update t_oa_incept_fileinfo set is_archive='1' where process_instance_id= '" + jhid + "'";
            stmt.execute(sqlStr);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                DBUtil.close(stmt, conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "success";
    }
}
