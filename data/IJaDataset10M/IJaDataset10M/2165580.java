package ch.olsen.routes.chart;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ch.olsen.routes.cell.service.AtomService;
import ch.olsen.routes.manager.RoutesService;
import ch.olsen.servicecontainer.service.ServiceInterface;

@ServiceInterface(name = "chart")
public interface ChartServiceInterface extends AtomService, RoutesService {

    /**
	 * call this method to make sure the chart service frees all its
	 * connections
	 */
    void unlinkAll();

    /**
	 * connects the gwt application
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
    public void handleServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
