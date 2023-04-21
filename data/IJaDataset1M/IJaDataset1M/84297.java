package com.nitbcn.backoffice.cont;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SendActionActivateAd extends Action {

    public SendActionActivateAd(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        super(request, response, session);
    }

    public int performanceAction() {
        String mod = request.getParameter("mod");
        session.setAttribute("mod", (Object) mod);
        try {
            this.response.sendRedirect("activa-anuncios.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}
