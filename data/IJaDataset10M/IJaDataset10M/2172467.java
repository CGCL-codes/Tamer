package jp.ac.segakuen;

import java.io.IOException;
import javax.servlet.http.*;

public class MailListTableServlet extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 401759163079797471L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("submit");
        if (action.equals("削除")) {
            MailListTableUtil.resetbyId(req.getParameter("address"));
            resp.sendRedirect("/ajaxmail/listMailData.jsp");
        } else if (action.equals("送信切り替え")) {
            MailListTableUtil.enableOrableSendMail(req.getParameter("address"));
            resp.sendRedirect("/ajaxmail/listMailData.jsp");
        } else if (action.equals("Add")) {
            MailListTableUtil.insertNew(req.getParameter("address"), "送信");
            resp.sendRedirect("/ajaxmail/listMailData.jsp");
        } else resp.sendRedirect("/ajaxmail/listMailData.jsp");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }
}
