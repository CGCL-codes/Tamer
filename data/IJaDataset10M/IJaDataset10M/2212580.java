package fr.umlv.jacquesBeauheur.model.station;

import fr.umlv.jacquesBeauheur.model.line.Line;

/**
 * The listener interface for receiving events from a Station.
 * @author Boussy Guillaume
 *
 */
public interface StationListener {

    /**
	 * Invoked when the status of a specified station has been modified.
	 * @param line - the line containing the station
	 * @param station - the station that has been changed
	 * @param close - the new value of the station status
	 */
    public void statusChanged(Line line, Station station, boolean close);
}
