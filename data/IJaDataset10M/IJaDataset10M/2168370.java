package com.hanxun.struts;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.LazyValidatorForm;
import com.struts.sql.DomSql;

public class UpdateUserAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        LazyValidatorForm f = (LazyValidatorForm) form;
        String uName = (String) f.get("uName");
        String oName = null;
        try {
            oName = new String(f.get("oName").toString().getBytes("ISO8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String sql = "update Ta_User set FOrganNumber =(select FNumber from Ta_Orgs where FName = '" + oName + "')  where FName = '" + uName + "'";
        DomSql dom = new DomSql();
        dom.updateSql(sql);
        try {
            response.sendRedirect("UserAction.do");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
