package ch.olsen.routes.manager;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ch.olsen.servicecontainer.commongwt.client.SessionException;
import ch.olsen.servicecontainer.service.ServiceInterface;
import ch.olsen.servicecontainer.service.StartServiceException;

@ServiceInterface(name = "routes")
public interface RoutesManagerInterface {

    void createStorage(String session, String name) throws StartServiceException, SessionException;

    void createQuery(String session, String name) throws StartServiceException, SessionException;

    void createGrid(String session, String name) throws StartServiceException, SessionException;

    void createChart(String session, String name) throws StartServiceException, SessionException;

    void close(String session, String name) throws SessionException;

    Collection<String> getAvailableStorageEngines();

    Collection<String> getAvailableQueryEngines();

    Collection<String> getAvailableGrids();

    Collection<String> getAvailableCharts();

    public void handleServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    public static class StorageException extends Exception {

        private static final long serialVersionUID = 1L;

        public StorageException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
