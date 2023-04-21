package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.Defenitions;
import beans.Location;
import dbHandlers.LocationDBHandler;
import dbHandlers.Tables;
import exceptions.NameAlreadyExistsException;
import exceptions.NoSuchLocationException;
import exceptions.OperationFailedException;

public class LocationManager extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static java.util.logging.Logger _logger = java.util.logging.Logger.getLogger("Location Manager");

    public LocationManager() {
        super();
    }

    public enum ACTION {

        ADD_LOCATION {

            @Override
            void perform(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                Location location = new Location();
                String name = request.getParameter(Defenitions.NAME_IE7);
                location.setName(name);
                String strLabId = request.getParameter(Tables.Locations.Cols.lab);
                int labId = Integer.parseInt(strLabId);
                location.setLabId(labId);
                try {
                    int id = new LocationDBHandler().addLocation(location);
                    response.setHeader("id", String.valueOf(id));
                    _logger.info("Location '" + name + "' was succesfully added at lab='" + labId + "'");
                } catch (NameAlreadyExistsException e) {
                    updateResponseWithException(response, e);
                }
            }
        }
        , RENAME_LOCATION {

            @Override
            void perform(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                String strTargetLocationId = request.getParameter(Tables.Locations.Cols.name + "1");
                String newName = request.getParameter(Tables.Locations.Cols.name + "2");
                int locationId = Integer.parseInt(strTargetLocationId);
                LocationDBHandler hand = new LocationDBHandler();
                Location targetLocation = hand.getLocationsById(locationId);
                targetLocation.setName(newName);
                hand.updateLocation(targetLocation);
                _logger.info(" Location (id='" + locationId + "') was succesfully renamed to '" + newName + "'");
            }
        }
        , REMOVE_LOCATION {

            @Override
            void perform(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                String locat = request.getParameter(Defenitions.NAME_IE7);
                try {
                    new LocationDBHandler().removeLocation(Integer.parseInt(locat));
                    _logger.info(" Location (id='" + locat + "') was succesfully removed from its lab");
                } catch (NoSuchLocationException ex) {
                    updateResponseWithException(response, ex);
                }
            }
        }
        ;

        abstract void perform(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

        /**
    	 *  writes an error message as a body of HTTTP response
    	 * */
        private static void updateResponseWithException(HttpServletResponse response, OperationFailedException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.println(ex.getMessage());
            } catch (IOException ignore) {
            } finally {
                out.close();
            }
        }
    }

    private static HashMap<String, ACTION> _actionExecutors = new HashMap<String, LocationManager.ACTION>();

    static {
        for (ACTION action : ACTION.values()) {
            _actionExecutors.put(action.name(), action);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        _actionExecutors.get(action).perform(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
