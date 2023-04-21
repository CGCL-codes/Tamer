package edu.osu.cse.be.persistence;

import edu.osu.cse.be.persistence.dao.*;

/**
 * @author Todd Sahl
 */
public class HibernateDAOFactory {

    public static RefereeDAO getRefereeDAO() {
        return new RefereeDAO();
    }

    public static TeamDAO getTeamDAO() {
        return new TeamDAO();
    }

    public static GameDAO getGameDAO() {
        return new GameDAO();
    }

    public static UserDAO getUserDAO() {
        return new UserDAO();
    }
}
