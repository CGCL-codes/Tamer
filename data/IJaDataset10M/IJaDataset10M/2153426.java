package megamek.common.event;

/**
 * Instances of this class are sent when Game settings are changed
 */
public class GameSettingsChangeEvent extends GameEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 7470732576407688193L;

    /**
     * @param source
     */
    public GameSettingsChangeEvent(Object source) {
        super(source, GAME_SETTINGS_CHANGE);
    }
}
