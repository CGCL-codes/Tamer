package fr.vtt.gattieres.gcs.gui.common;

public interface RaceListener {

    public void categoryInserted(RaceEvent evt);

    public void categoryDeleted(RaceEvent evt);

    public void challengerInserted(RaceEvent evt);

    public void challengerDeleted(RaceEvent evt);

    public void challengerChanged(RaceEvent evt);

    public void lapsTimeAdded(RaceEvent evt);

    public void chronometerStarted(RaceEvent evt);

    public void chronometerStopped(RaceEvent evt);
}
