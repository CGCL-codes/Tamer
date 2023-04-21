package fr.lip6.sma.simulacion.app;

/**
 * Base class for windows of applications for players.
 *
 * @author Paul Guyot <paulguyot@acm.org>
 * @version $Revision: 115 $
 *
 * @see "aucun test d�fini."
 */
public abstract class GameApplicationWindow extends ApplicationWindow {

    /**
	 * Constructor from the Game application.
	 *
	 * @param inApplication		application object.
	 */
    public GameApplicationWindow(GameApplication inApplication) {
        super(inApplication);
    }

    /**
	 * Accessor on the application object.
	 *
	 * @return a reference to the application object.
	 */
    public final GameApplication getGameApplication() {
        return (GameApplication) getApplication();
    }
}
