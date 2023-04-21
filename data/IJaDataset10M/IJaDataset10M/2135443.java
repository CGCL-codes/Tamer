package de.lamasep.simulator;

import java.util.Observable;

/**
 * Repräsentiert den Status eines Abspielvorgangs einer Simulation.
 * Dieser hat einen prozentualen Fortschritt und drei verschiedene Stadien in
 * denen er sich befinden kann:
 * <ul>
 * <li> STOPPED </li>
 * <li> RUNNING </li>
 * <li> PAUSED </li>
 * </ul>
 *
 * Eine Benachrichtigung der Observer findet statt, wenn sich eine
 * Instanzvariable verändert hat.
 * Dabei wird diese als Parameter der <code>notifyObservers()</code>-Methode
 * mitgegeben.
 *
 * @author Anja Kastenmayer
 *
 * @see de.lamasep.simulator.SimulationState.State
 * @see java.util.Observable
 */
public class SimulationState extends Observable {

    /**
     * Beschreibt den aktuellen Zustand.
     * @see de.lamasep.simulator.SimulationState.State
     */
    private State state;

    /**
     * Prozentualer Fortschritt des Abspielvorgangs.
     */
    private int complete;

    /**
     * Konstruktor - Initialisierung mit <code>state</code>.
     * Dabei wird <code>complete</code> mit 0 initialisiert.
     *
     * @param state Status der Simulation, <code>state != null</code>
     * @throws IllegalArgumentException falls <code>state == null</code>
     */
    public SimulationState(final State state) {
        if (state == null) {
            throw new IllegalArgumentException();
        }
        this.complete = 0;
        this.setState(state);
    }

    /**
     * Gibt den prozemtualen Fortschritt des Abspielvorgangs zurück.
     * @return Prozentualer Fortschritt des Abspielvorgangs
     * @see #complete
     */
    public int getComplete() {
        return complete;
    }

    /**
     * Setzt den prozentualen Fortschritt des Abspielvorgangs.
     * Dabei muss gelten: <code>0 <= complete <= 100</code>
     *
     * Falls sich der prozentuale Fortschritt verändert hat, werden alle
     * Observer darüber informiert.
     *
     * @param complete Prozentualer Fortschritt des Abspielvorgangs
     * @throws IllegalArgumentException falls <code>complete < 0 ||
     *         complete > 100</code>
     */
    public void setComplete(final int complete) {
        if (complete < 0) {
            throw new IllegalArgumentException("complete < 0");
        }
        if (complete > 100) {
            throw new IllegalArgumentException("complete > 100");
        }
        if (complete != this.complete) {
            this.complete = complete;
            this.setChanged();
            this.notifyObservers(this.complete);
        }
    }

    /**
     * Gibt den aktuellen Zustand zurück.
     * @return Status der Simulation
     * @see #state
     */
    public State getState() {
        return state;
    }

    /**
     * Setzt den aktuellen Zustand.
     *
     * Falls sich der prozentuale Fortschritt verändert hat, werden alle
     * Observer darüber informiert.
     *
     * @param state Status der Simulation, <code>state != null</code>
     * @throws IllegalArgumentException falls <code>state == null</code>
     *
     * @see #state
     */
    public void setState(final State state) {
        if (state == null) {
            throw new IllegalArgumentException();
        }
        if (state != this.state) {
            this.state = state;
            this.setChanged();
            this.notifyObservers(this.state);
        }
    }

    /**
     * Repräsentiert den Abspiel-Zustand einer Simulation.
     * @author Anja Kastenmayer
     */
    public enum State {

        /**
         * Status: Abspielvorgang ist gestoppt.
         */
        STOPPED, /**
         * Status: Abspielvorgang läuft gerade.
         */
        RUNNING, /**
         * Status: Abspielvorgang ist pausiert.
         */
        PAUSED
    }
}
