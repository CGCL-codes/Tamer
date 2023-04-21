package net.sf.freesimrc.vatsim;

/**
 * This interface has to be implemented by all classes, which want to be notified
 * about changes in an {@link AircraftManager} instance. The implementing class
 * has to register via the {@link AircraftManager#addAircraftListener} method of
 * that instance.
 */
public interface AircraftListener {

    /**
     * This method is called by {@link AircraftManager#addAircraft} whenever a
     * new aircraft is added.
     * @param aircraft The data of the new aircraft. Will contain no data except
     * the aircraft's callsign.
     */
    public void aircraftAdded(Aircraft aircraft);

    /**
     * This method is called by {@link AircraftManager#updateAircraft} whenever the
     * data of an aircraft is updated.
     * @param aircraft The updated aircraft.
     */
    public void aircraftUpdated(Aircraft aircraft);

    /**
     * This method is called by {@link AircraftManager#removeAircraft} whenever an
     * aircraft is removed.
     * @param aircraft The data of the aircraft before it was removed.
     */
    public void aircraftRemoved(Aircraft aircraft);

    /**
     * This method is called by {@link AircraftManager#updateFlightplan} whenever
     * the flightplan of an aircraft is changed.
     * @param aircraft The updated aircraft.
     */
    public void flightplanUpdated(Aircraft aircraft);

    /**
     * This method is called by {@link AircraftManager#setExtra} whenever an extra
     * of an aircraft is added/changed.
     * @param aircraft The updated aircraft.
     * @param name The name of the extra which was added/changed.
     */
    public void extraSet(Aircraft aircraft, String name);

    /**
     * This method is called by {@link AircraftManager#removeExtra} whenever an extra
     * of an aircraft is removed.
     * @param aircraft The updated aircraft.
     * @param name The name of the extra which was removed.
     */
    public void extraRemoved(Aircraft aircraft, String name);
}
