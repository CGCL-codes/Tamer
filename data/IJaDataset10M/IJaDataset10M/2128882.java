package com.googlecode.dmutti;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dmutti@gmail.com
 */
public class CarteiroController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB(name = "carteiro")
    Carteiro carteiro;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Carta carta = new Carta();
        carta.setNome(req.getParameter("nome"));
        carta.setEndereco(req.getParameter("end"));
        carta.setTexto(req.getParameter("texto"));
        try {
            carteiro.envia(carta);
            resp.sendRedirect("sucesso.html");
        } catch (Exception e) {
            resp.sendRedirect("erro.html");
            e.printStackTrace();
        }
    }
}
