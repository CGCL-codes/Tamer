package medimagesuite.plugins.workspaces.workspace2D;

import medimagesuite.plugins.core.Nucleus;
import medimagesuite.plugins.core.Workspace;
import medimagesuite.util.messages.MessageProvider;
import org.java.plugin.Plugin;

/**
 * The 2D extension of the <code>Workspace</code> extension-point. It will make the system be able to
 * manipulate 2D images and stuff.
 * @author Guilherme Mauro Germoglio - germoglio@gmail.com
 *
 */
public class WS2DPlugin extends Plugin implements Workspace {

    public static final String PLUGIN_ID = "medimagesuite.workspace2d";

    /**
	 * Workspace languages library name.
	 */
    private static final String WORKSPACE2D_LANGUAGES = "languages";

    /**
	 * The workspace 2D.
	 */
    private Workspace2D workspace2d;

    /**
     * @see Plugin#doStart()
     */
    protected void doStart() throws Exception {
    }

    /**
	 * @see Plugin#doStop()
	 */
    protected void doStop() throws Exception {
    }

    /**
	 * @see Workspace#init(Nucleus)
	 */
    public void init(Nucleus nucleus) {
        if (workspace2d == null) {
            MessageProvider.getMessageProvider().addMessagesFromPlugin(this.getClass(), WS2DPlugin.PLUGIN_ID, WORKSPACE2D_LANGUAGES);
            this.workspace2d = new Workspace2D(nucleus, this);
        } else {
            workspace2d.init();
        }
    }
}
