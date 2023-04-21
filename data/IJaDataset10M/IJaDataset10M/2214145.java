package Tienda;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CarritoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();
        pw.println("<html><head>");
        pw.println("<TITLE>Carrito de la compra</TITLE>");
        pw.println("</head><body>");
        pw.println("<h2>CARRITO</h2>");
        List<Producto> carrito = (List<Producto>) req.getSession().getAttribute(AtributosConstantes.carrito.toString());
        for (Producto producto : carrito) {
            pw.println("Producto: " + producto.nombre + " Precio: " + producto.precio + "<br>");
        }
        pw.println("</body>");
        pw.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
