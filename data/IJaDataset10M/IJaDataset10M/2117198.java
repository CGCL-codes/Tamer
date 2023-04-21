package model.db.factory;

import java.math.*;
import java.sql.*;
import java.net.URL;
import java.io.Serializable;
import model.db.dao.*;
import model.db.impl.*;

public class DAOFactoryImpl implements Serializable, DAOFactory {

    private static final Aiport_taxDAOImpl aiport_taxdaoimpl = new Aiport_taxDAOImpl();

    public Aiport_taxDAO createAiport_taxDAO() {
        return aiport_taxdaoimpl;
    }

    private static final Flight_purchaseDAOImpl flight_purchasedaoimpl = new Flight_purchaseDAOImpl();

    public Flight_purchaseDAO createFlight_purchaseDAO() {
        return flight_purchasedaoimpl;
    }

    private static final FlightsDAOImpl flightsdaoimpl = new FlightsDAOImpl();

    public FlightsDAO createFlightsDAO() {
        return flightsdaoimpl;
    }

    private static final UsersDAOImpl usersdaoimpl = new UsersDAOImpl();

    public UsersDAO createUsersDAO() {
        return usersdaoimpl;
    }
}
