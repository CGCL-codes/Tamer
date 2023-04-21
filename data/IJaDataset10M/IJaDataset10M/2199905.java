package org.perfectjpattern.jee.integration.dao.jpa;

import javax.ejb.*;
import javax.persistence.*;
import org.perfectjpattern.example.datamodel.*;
import org.perfectjpattern.jee.api.integration.dao.*;
import org.perfectjpattern.jee.integration.dao.*;

/**
 * EJB implementation of {@link IMovieBaseDao}. Exposes managed 
 * EJB reusing all of the PerfectJPattern's {@link IBaseDao} facilities
 * 
 * @author <a href="mailto:bravegag@hotmail.com">Giovanni Azua</a>
 * @version $Revision: 1.0 $Date: Feb 11, 2009 11:31:24 AM $
 */
@Stateful(name = "IMovieBaseDao")
public class MovieBaseDao extends AbstractJpaManagedBaseDao<Long, Movie> implements IMovieBaseDao {

    public MovieBaseDao() {
        super(Movie.class);
    }

    @PersistenceContext(unitName = "movie", type = PersistenceContextType.EXTENDED)
    public void setEntityManager(EntityManager anEntityManager) {
        super.setEntityManager(anEntityManager);
    }
}
