package com.meleva.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.meleva.bll.UsuarioBll;
import com.meleva.model.Cidade;
import com.meleva.model.Usuario;

@WebServlet("/ListUsuariosXML")
public class ListUsuariosXML extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public ListUsuariosXML() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        UsuarioBll usuarioBll = new UsuarioBll();
        response.setContentType("text/xml");
        response.setCharacterEncoding("UTF-8");
        String xml = "<?xml version='1.0'?>";
        if (request.getParameter("Email") != null) {
            xml += "<return>" + usuarioBll.getBoolByExistsEmail(request.getParameter("Email")) + "</return>";
        } else if (request.getParameter("Cpf") != null) {
            xml = "<return>" + usuarioBll.getBoolByExistsCpf(request.getParameter("Cpf")) + "</return>";
        }
        out.print(xml);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
