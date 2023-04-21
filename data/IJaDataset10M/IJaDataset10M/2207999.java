package com.hanxun.struts;

import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.struts.sql.DomSql;

public class UajaxAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String orgNumSql = (String) session.getAttribute("orgNumSql");
        String qureyUserName = (String) session.getAttribute("qureyUserName");
        String sql = "select u.FName,u.FPwd,o.FName,o.FNumber from Ta_User as u,Ta_Orgs as o  where u.FOrganNumber = o.FNumber and u.FOrganNumber like '" + orgNumSql + "%' and u.FName like '%" + qureyUserName + "%'";
        DomSql ds = new DomSql();
        ResultSet rs = ds.querySql(sql);
        while (rs.next()) {
            out.println("<tr><td>" + rs.getString(3) + "</td><td>" + rs.getString(1) + "</td><td>编辑X</td><td>删除X</td><td>123" + qureyUserName + "321</td></tr>");
        }
        return null;
    }
}
