package spellcast.ui;

/**
 * This interface contains all the UI properties that can be fired 
 * in a PropertyChangeEvent.  Classes the support property change events
 * should implement this class to obtain these values within local scope.
 *
 * @author Barrie Treloar
 */
public interface UIProperties {

    public static final String MESSAGE_PROP = "message";

    public static final String OK_PROP = "ok";

    public static final String CONNECT_PROP = "connect";

    public static final String DISCONNECT_PROP = "disconnect";

    public static final String NEW_GAME_PROP = "new_game";

    public static final String SHOW_CONNECTION_DIALOG_PROP = "dialog.connection";

    public static final String SHOW_NEW_GAME_DIALOG_PROP = "dialog.new_game";
}
