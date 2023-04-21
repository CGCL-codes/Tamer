package iws.testCommerce.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PagamentoOK extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public PagamentoOK() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String s = null;
        s = request.getParameter("thx_id");
        s = request.getParameter("verify_sign");
        s = request.getParameter("payer_id");
        s = request.getParameter("payment_date");
        s = request.getParameter("payment_status");
        s = request.getParameter("payer_name");
        s = request.getParameter("payer_email");
        s = request.getParameter("amount");
        s = request.getParameter("qta");
        s = request.getParameter("custom");
        s = request.getParameter("memo");
    }
}
