package org.openware.job.examples.movie.api;

import org.openware.job.data.PersistentManager;
import org.openware.job.data.ClassInfoBase;
import org.openware.job.data.BusinessRules;
import org.openware.job.data.PersistException;
import org.openware.job.examples.movie.persist.MovieGenre;
import org.apache.log4j.Category;

/**
 * Want to be able to assign many genres to movies.  Obviously, many movies can be assigned the same drama.
 * <p>
 * The relationship between Genre and Movie is what is called a many-to-many relationship.  JOB does not directly support many-to-many relationships, so we create the intersection table, MovieGenre.
 * <p>
 * @author JOB
 */
public class MovieGenreManager {

    private static final Category log = Category.getInstance(MovieGenreManager.class.getName());

    private PersistentManager pmanager = null;

    private MovieApi api = null;

    MovieGenreManager(PersistentManager pmanager, MovieApi api) {
        this.pmanager = pmanager;
        this.api = api;
    }

    public MovieGenre create() throws PersistException {
        return new MovieGenre(pmanager);
    }

    /**
     * Add the field validators to the <code>ClassInfo</code> object
     * for the <code>PersistentManager</code>
     */
    static void addValidators(ClassInfoBase cinfo) {
    }

    /**
     * Add the business rules to the <code>ClassInfo</code> object
     * for the <code>PersistentManager</code>
     */
    static void addBusinessRules(BusinessRules br) {
    }
}
