package net.sf.josas.model;

import java.util.Observable;
import net.sf.josas.om.Association;
import net.sf.josas.persistence.dao.DataObjectAccessor;

/**
 * Generic class for association dedicated managers.
 *
 * @author frederic
 *
 */
public abstract class AbstractManager extends Observable {

    /** Related association. */
    private transient Association association;

    /** Accessor to the persistence layer. */
    private DataObjectAccessor doa = DataObjectAccessor.getInstance();

    /**
     * Constructor. Set the association.
     *
     * @param asso
     *            related association.
     */
    protected AbstractManager(final Association asso) {
        association = asso;
    }

    /**
     * Get the association this manager is working for.
     *
     * @return association
     */
    public final Association getAssociation() {
        return association;
    }

    /**
     * Get dao.
     * @return the dao
     */
    public final DataObjectAccessor getDoa() {
        return doa;
    }
}
