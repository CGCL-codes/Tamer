package struts.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import struts.form.ReturnForm;
import util.DBPool;

/** 
 * MyEclipse Struts
 * Creation date: 12-05-2008
 * 
 * XDoclet definition:
 * @struts.action path="/return" name="returnForm" input="/return.jsp" scope="request" validate="true"
 * @struts.action-forward name="late" path="/returnLate.jsp"
 * @struts.action-forward name="success" path="/returnSuccess.jsp"
 */
public class ReturnAction extends Action {

    /** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ReturnForm returnForm = (ReturnForm) form;
        String bookBarcode = returnForm.getBookBarcode();
        String bookName = null;
        Connection conn;
        PreparedStatement ps;
        ResultSet rs;
        conn = DBPool.getConnection();
        String sql_getDeadline = "select deadline from t_checkout where bookBarcode=?";
        String sql_return = "delete from t_checkout where bookBarcode=?";
        String sql_update = "update t_book set flag=1 where barcode=?";
        String sql_getBookName = "select name from t_book where barcode=?";
        try {
            ps = conn.prepareStatement(sql_getDeadline);
            ps.setString(1, bookBarcode);
            rs = ps.executeQuery();
            if (!rs.next()) {
                request.setAttribute("errorMessage", "Sorry, the book with the barcode doesn't exist or has not been borrowed!");
                return mapping.findForward("failed");
            }
            java.sql.Date sqlDeadline = rs.getDate(1);
            java.util.Date deadline = sqlDeadline;
            java.util.Date date = new java.util.Date();
            if (date.after(deadline)) {
                request.setAttribute("errorMessage", "Sorry, you return the book out of deadline, please refer to the ��inquiry desk��!");
                return mapping.findForward("failed");
            } else {
                try {
                    ps = conn.prepareStatement(sql_return);
                    ps.setString(1, bookBarcode);
                    ps.executeUpdate();
                    ps = conn.prepareStatement(sql_update);
                    ps.setString(1, bookBarcode);
                    ps.executeUpdate();
                    ps = conn.prepareStatement(sql_getBookName);
                    ps.setString(1, bookBarcode);
                    rs = ps.executeQuery();
                    rs.next();
                    bookName = rs.getString(1);
                    request.setAttribute("bookName", bookName);
                } catch (SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("errorMessage", "Sorry, system error");
                    return mapping.findForward("failed");
                }
            }
        } catch (SQLException e1) {
            request.setAttribute("errorMessage", "Sorry, system error");
            return mapping.findForward("failed");
        }
        return mapping.findForward("success");
    }
}
