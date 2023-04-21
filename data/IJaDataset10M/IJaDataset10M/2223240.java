package de.javagimmicks.games.jotris.model;

/**
 * Defines a listener which is capable of receiving events from a JoTris game
 * that runs inside of a {@link JoTrisModel}.
 * @see JoTrisModel
 */
public interface JoTrisModelListener {

    /**
    * Denotes the change to the grid of a {@link JoTrisModel} which means the
    * change to it's {@link Field} or {@link Tile}.
    * @param e the respective information in form of a {@link GridEvent}
    * @see GridEvent
    */
    public void gridEventOccured(GridEvent e);

    /**
    * Denotes that one or more game related events have happened inside of a
    * {@link JoTrisModel}.
    * @param e the respective information in form of a {@link GameEvent}
    * @see GameEvent
    */
    public void gameEventOccured(GameEvent e);
}
