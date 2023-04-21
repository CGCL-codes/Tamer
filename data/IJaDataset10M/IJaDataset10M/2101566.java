package fr.ird.database.sample;

import fr.ird.database.Entry;

/**
 * Campagne d'�chantillonage. Plusieurs �chantillons {@link SampleEntry} peuvent provenir de
 * la m�me campagne d'�chantillonage.
 *
 * @version $Id: CruiseEntry.java,v 1.3 2004/11/10 08:41:49 desruisseaux Exp $
 * @author Martin Desruisseaux
 */
public interface CruiseEntry extends Entry {

    /**
     * Retourne le num�ro de la campagne d'�chantillonage.
     */
    public abstract int getID();

    /**
     * {@inheritDoc}
     */
    public abstract String getName();

    /**
     * {@inheritDoc}
     */
    public abstract String getRemarks();
}
