package controladores;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import libreria.Cliente;
import servicios.ServicioClientes;
import servicios.ServicioClientesImpl;
import servicios.ServicioException;

/**
 * Servlet implementation class ListadoAutoresServlet
 */
public class ListadoClientesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListadoClientesServlet() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        ServicioClientes servicioClientes = new ServicioClientesImpl();
        Connection conexion = (Connection) request.getSession().getAttribute("connection");
        try {
            pw.print("<h1>Los Clientes! </h1>");
            List<Cliente> clientes = servicioClientes.getClientes(conexion);
            for (Cliente c : clientes) {
                String nombre = c.getNombre();
                String id = Long.toString(c.getId());
                pw.print(nombre + "  <a href='ListadoPedidos?id=" + id + "'>Sus pedidos</a><br>");
            }
        } catch (ServicioException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
        pw.close();
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
