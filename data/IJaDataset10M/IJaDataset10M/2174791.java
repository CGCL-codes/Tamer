package se.trixon.nbmpc.core.actions;

import javax.swing.AbstractAction;
import se.trixon.nbmpc.Options;

/**
 *
 * @author Patrik Karlsson <patrik@trixon.se>
 */
public abstract class MpdAction extends AbstractAction {

    protected Options options = Options.getInstance();

    protected String host;

    protected int port;

    protected String password;
}
