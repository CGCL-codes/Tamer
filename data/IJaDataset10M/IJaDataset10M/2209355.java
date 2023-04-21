package org.lsms.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.lsms.dao.impl.DaoImpl;

public class site_setting extends HttpServlet {

    public void destroy() {
        super.destroy();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int[] setting = new int[8];
        setting[0] = Integer.parseInt(request.getParameter("paper_submit"));
        setting[1] = Integer.parseInt(request.getParameter("paper_edit"));
        setting[2] = Integer.parseInt(request.getParameter("user_register"));
        setting[3] = Integer.parseInt(request.getParameter("member_invite"));
        setting[4] = Integer.parseInt(request.getParameter("reviewer_invite"));
        setting[5] = Integer.parseInt(request.getParameter("index_view"));
        setting[6] = Integer.parseInt(request.getParameter("paper_notif_sent"));
        setting[7] = Integer.parseInt(request.getParameter("index_upload"));
        DaoImpl impl = new DaoImpl();
        if (impl.upload_site_setting(setting)) {
            String info = "Site setting has been updated!";
            response.sendRedirect("admin/admin_show_info.jsp?admin_info=" + info);
        } else {
            String info = "Site setting failed!Please try again later.";
            response.sendRedirect("admin/admin_show_info.jsp?admin_info=" + info);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    public void init() throws ServletException {
    }
}
