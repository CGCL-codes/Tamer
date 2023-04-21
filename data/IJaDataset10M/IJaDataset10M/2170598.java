package de.javagimmicks.games.jotris.model;

/**
 * Represents all game-related events that can happen in a {@link JoTrisModel}.
 * <p>
 * The current events are:
 * <ul>
 * <li>A {@link Tile} was added to the {@link Field}</li>
 * <li>One or more rows were killed</li>
 * <li>The game was started</li> 
 * <li>The game is over</li> 
 * </ul>
 * @see JoTrisModel
 */
public class GameEvent {

    /**
    * Determines the type of a {@link GameEvent}.
    * @see GameEvent
    */
    public static enum EventType {

        STARTED, OVER, TILE, ROWS
    }

    ;

    private final JoTrisModel _model;

    private final EventType _eventType;

    /**
    * Creates a new {@link GameEvent} from the given model and {@link EventType}.
    * @param model the {@link JoTrisModel} causing this {@link GameEvent}
    * @param eventType the {@link EventType} of this {@link GameEvent}
    */
    public GameEvent(JoTrisModel model, EventType eventType) {
        _model = model;
        _eventType = eventType;
    }

    /**
    * Returns the {@link JoTrisModel} causing this {@link GameEvent}.
    * @return the {@link JoTrisModel} causing this {@link GameEvent}
    */
    public JoTrisModel getModel() {
        return _model;
    }

    /**
    * Returns if one or more rows were killed.
    * @return if one or more rows were killed
    */
    public boolean isRowsKilled() {
        return _eventType == EventType.ROWS;
    }

    /**
    * Returns if a new {@link Tile} was inserted into the {@link Field}.
    * @return if a new {@link Tile} was inserted into the {@link Field}
    */
    public boolean isTileChanged() {
        return _eventType == EventType.TILE;
    }

    /**
    * Returns if the game is over.
    * @return if the game is over
    */
    public boolean isGameOver() {
        return _eventType == EventType.OVER;
    }

    /**
    * Returns if the game was started.
    * @return if the game was started
    */
    public boolean isGameStarted() {
        return _eventType == EventType.STARTED;
    }

    /**
    * Returns the {@link EventType} of this {@link GameEvent}.
    * @return the {@link EventType} of this {@link GameEvent}
    * @see GameEvent
    */
    public EventType getEventType() {
        return _eventType;
    }
}
