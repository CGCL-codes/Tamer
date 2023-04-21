package utoopia.reception;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utoopia.content.html.BrowserPopUp;
import utoopia.content.producers.ApplicationHomePageProducer;
import utoopia.content.producers.ConfigurePageProducer;
import utoopia.content.producers.EntityFormPageProducer;
import utoopia.content.producers.EntityListPageProducer;
import utoopia.content.producers.EntityPersistencePageProducer;
import utoopia.content.producers.FileMenuPageProducer;
import utoopia.content.producers.ImageProducer;
import utoopia.content.producers.LogInPageProducer;
import utoopia.content.producers.LoggedInPageProducer;
import utoopia.content.producers.LoggedOutPageProducer;
import utoopia.content.producers.SolutionHomePageProducer;
import utoopia.data.db.Database;
import utoopia.models.EntityModel;
import utoopia.models.readers.InfrastructureReader;
import utoopia.models.readers.SolutionReader;
import utoopia.security.SessionChecker;

/**
 * Servlet implementation class for Servlet: Recepcionista
 *
 */
public class Receptionist extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    private static final long serialVersionUID = 1L;

    /**
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
    public Receptionist() {
        super();
    }

    /**
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doProcessPetition(request, response);
    }

    /**
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doProcessPetition(request, response);
    }

    /**
	 * Procesa las peticiones del cliente
	 * @param request Petición del cliente
	 * @param response Respuesta para el cliente
	 * @return Devuelve el código HTML a devolver al cliente
	 * @throws IOException 
	 */
    private void doProcessPetition(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String resultado = "<html><body><h1>Accion desconocida</h1></body></html>";
        String action = request.getParameter("action");
        if (action == null) action = "";
        action = action.toLowerCase();
        int estado = 0;
        if (!action.equals("login") && !action.equals("logout") && !action.equals("getimage")) estado = SessionChecker.doCheckSession(request.getSession());
        if (action.equals("configure") || action.equals("")) {
            InfrastructureReader reader = new InfrastructureReader();
            EntityModel users = reader.getUsersEntityModel();
            if (!Database.database.existsTable(users) || users.count() == 0) {
                estado = 0;
                action = "configure";
            }
        }
        if (estado != 0) {
            boolean expirado = estado == 2;
            resultado = new LogInPageProducer().doProduceContent(request, response, expirado, false);
        } else {
            if (action.equals("getimage")) resultado = new ImageProducer().doProduceContent(request, response);
            if (action.equals("configure")) resultado = new ConfigurePageProducer().doProduceContent(request, response);
            if (action.equals("") || action.equals("solutionhome")) resultado = new SolutionHomePageProducer().doProduceContent(request, response);
            if (action.equals("apphome")) resultado = new ApplicationHomePageProducer().doProduceContent(request, response);
            if (action.equals("file")) resultado = new FileMenuPageProducer().doProduceContent(request, response);
            if (action.equals("addentity")) resultado = new EntityFormPageProducer().doProduceContent(request, response);
            if (action.equals("viewentity")) resultado = new EntityFormPageProducer().doProduceContent(request, response);
            if (action.equals("modifyentity")) resultado = new EntityFormPageProducer().doProduceContent(request, response);
            if (action.equals("listentities")) resultado = new EntityListPageProducer().doProduceContent(request, response);
            if (action.equals("browse")) resultado = new BrowserPopUp().doProduceContent(request, response);
            if (action.equals("login")) resultado = new LoggedInPageProducer().doProduceContent(request, response);
            if (action.equals("logout")) resultado = new LoggedOutPageProducer().doProduceContent(request, response);
            if (action.equals("dbaddentity")) resultado = new EntityPersistencePageProducer().doProduceContent(request, response);
            if (action.equals("dbupdateentity")) resultado = new EntityPersistencePageProducer().doProduceContent(request, response);
            if (action.equals("dbdeleteentity")) resultado = new EntityPersistencePageProducer().doProduceContent(request, response);
        }
        response.getOutputStream().print(resultado);
    }
}
