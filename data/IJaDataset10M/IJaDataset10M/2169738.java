package com.umc.gui.manager;

import java.util.*;
import org.apache.log4j.*;
import com.umc.beans.Dir;
import com.umc.beans.MovieFile;
import com.umc.beans.ViewResult;
import com.umc.dao.DataAccessFactory;
import com.umc.dao.UMCDataAccessInterface;
import com.umc.helper.UMCParams;

/**
 * This class acts as a "Backend" controller class which contains most of the program logic.
 * The class is triggered by the management console.
 * 
 * Each group of views should use its own controller.
 * This means that this controller should be used by all views which handles presenting movies, edit movies and manage movies.
 * 
 * @version 0.1 03 Nov 2008
 * @author 	DonGyros
 * 
 */
public class MovieController {

    public static Logger log = null;

    /**With this action you can retrieve all movies from the database*/
    public static final String ACTION_GET_ALL_MOVIES = "get_dirs";

    public static final String KEY_MOVIES = "all_movies";

    static {
        startLogging();
    }

    /**
	 * This is the main method which is called.
	 * According to the given action parameter the appropriate method is called which contains the logic.
	 * 
	 * @param aAction The action to be executed 
	 * @param Collection with {@link MovieFile} objects(only for saving) or NULL
	 * @return The result as {@link ViewResult ViewResult}.
	 */
    public static ViewResult dispatch(String aAction, Collection<Dir> c) {
        final String sAction = aAction;
        if (sAction != null) {
            if (sAction.equals(ACTION_GET_ALL_MOVIES)) return getAllMovies();
        }
        final ViewResult vres = new ViewResult("Unbekannter Aufruf");
        return vres;
    }

    /**
	 * Initializes the logging
	 */
    private static void startLogging() {
        try {
            synchronized (MovieController.class) {
                if (log != null) return;
                log = Logger.getLogger("com.umc.console");
            }
        } catch (Throwable ex) {
            System.out.println("UMC Klasse " + MovieController.class.getName() + " konnte in kein Logfile schreiben : " + ex);
        }
    }

    /**
	 * Retrieves all movies from the database..
	 * 
	 * @return
	 */
    private static ViewResult getAllMovies() {
        final ViewResult vres = new ViewResult();
        try {
            UMCDataAccessInterface dao = DataAccessFactory.getUMCDataSourceAccessor(DataAccessFactory.DB_TYPE_SQLITE, UMCParams.getInstance().getDBDriverconnect() + UMCParams.getInstance().getDBName(), UMCParams.getInstance().getDBDriver(), UMCParams.getInstance().getDBUser(), UMCParams.getInstance().getDBPwd());
            final Collection<MovieFile> allMovies = dao.getMovies(UMCParams.getInstance().getLanguage());
            final Map parameters = vres.getParameters();
            parameters.put(KEY_MOVIES, allMovies);
        } catch (Throwable exc) {
            log.error("Interner Fehler:", exc);
            vres.setErrorMessage("Interner Fehler getAllMovies(): " + exc.getMessage());
        }
        return vres;
    }
}
