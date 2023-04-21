package org.elogistics.domain.interfaces.list;

import org.elogistics.domain.entities.transportplan.tour.Station;
import org.elogistics.domain.exception.DomainException;

/**
 * @author Jurkschat, Oliver
 *
 */
public interface IStationList {

    /**
	 * @see	{@link IList#addElement(Object)}
	 */
    void addStation(final Station newStation) throws DomainException;

    /**
	 * @see {@link IList#removeElement(Object)}
	 */
    void removeStation(final Station toRemove) throws DomainException;

    /**
	 * Entfernt alle Stationen aus der Liste und
	 * l___scht sie aus der Datenbank.
	 */
    void disposeAllStations() throws DomainException;

    /**
	 *	@see {@link IList#resetElementIterator()}
	 */
    void resetStationIterator();

    /**
	 * @see {@link IList#getElement(Object)}
	 */
    Station getStation(final Station searchStation);

    /**
	 * 
	 * @param position
	 * @return
	 */
    Station getStation(int position);

    /**
	 * @see {@link IList#nextElement()}
	 */
    Station nextStation();

    /**
	 * @see	{link {@link IList#getElementCount()}
	 */
    int getStationCount();

    /**
	 * @see	{@link IList#hasNextElement}
	 */
    boolean hasNextStation();
}
