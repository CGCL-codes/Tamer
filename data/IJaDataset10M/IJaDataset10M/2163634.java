package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pos.domain.Usuario;
import pos.domain.UsuarioStore;
import pos.domain.Voto;
import pos.domain.VotoImpl;
import pos.domain.VotoStore;
import pos.utils.UIDGenerator;

/**
 * Servlet implementation class ServletVotarAFavor
 */
public class ServletVotarAFavor extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletVotarAFavor() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UIDGenerator id = UIDGenerator.getInstance();
        String idVoto = id.getKey();
        Boolean valor = true;
        HttpSession sesion = request.getSession();
        Usuario user = (Usuario) sesion.getAttribute("usuario");
        String idAplicacion = request.getParameter("idAplicacion");
        String idUsuario = user.getIdUser();
        VotoStore store = VotoStore.getInstance();
        Voto voto = new VotoImpl();
        voto.setIDVoto(idVoto);
        voto.setUsuario(idUsuario);
        voto.setAplicacion(idAplicacion);
        voto.setValor(valor);
        store.crearVoto(voto);
        UsuarioStore storeUser = new UsuarioStore();
        Usuario userNuevo = storeUser.recuperarUsuarioByIdUsuario(user.getIdUser());
        storeUser.actualizaKarmaUsuario(user, 5);
        sesion.setAttribute("usuario", userNuevo);
        request.getRequestDispatcher("aplicaciones.jsp").include(request, response);
    }
}
