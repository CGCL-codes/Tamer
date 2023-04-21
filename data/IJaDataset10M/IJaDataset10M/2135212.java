package org.xebra.scp.db.persist;

import org.xebra.scp.db.exception.PersistException;
import org.xebra.scp.db.model.Series;

/**
 * Persister for the {@link org.xebra.scp.db.model.Series} class.  Contains methods 
 * to write Series objects to the database.
 * 
 * <p>Instantiate this object using the {@link LoaderFactory}.</p>
 * 
 * <blockquote>
 * <code>
 * SeriesPersister persister = (SeriesPersister)LoaderFactory.getLoader(Series.class);
 * </code>
 * </blockquote>
 * 
 * @author Rafael Chargel
 * @version $Revision: 1.1.1.1 $
 */
public class SeriesPersister extends AbstractPersister<Series> {

    private static final SeriesPersister persister = new SeriesPersister();

    /**
	 * Constructor for the SeriesPersister class.
	 * 
	 */
    private SeriesPersister() {
        super();
    }

    /**
	 * Gets an instance of this persister.
	 * @return Returns the persister.
	 */
    protected static SeriesPersister getInstance() {
        return persister;
    }

    public void persist(Series object) throws PersistException {
        super.persist(Series.class, object);
    }

    public void deleteByUID(String uid) throws PersistException {
        super.deleteByUID(Series.class, uid);
    }
}
