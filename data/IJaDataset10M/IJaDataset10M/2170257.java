package com.consultcare2.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.consultcare2.classe.Login;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        Login login = new Login();
        login.setLogin(request.getParameter("login"));
        login.setSenha(request.getParameter("senha"));
        System.out.println("login: " + login.getLogin());
        System.out.println("senha: " + login.getSenha());
        String log = "plinio";
        String sen = "123456";
        String user = "Pl�nio Figueredo";
        if (login.getLogin().equals(log) && login.getSenha().equals(sen)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("usuario", user);
            response.sendRedirect("agenda/minhaagenda.jsp");
        } else {
            request.setAttribute("logerror", "login ou senha inv�lido");
            RequestDispatcher r = request.getRequestDispatcher("index.jsp");
            r.forward(request, response);
        }
    }
}
