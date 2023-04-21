package client.gui.commands;

import javax.swing.AbstractAction;
import client.MWClient;
import client.protocol.CConnector;

/**
 * Abstract class for GUI Commands
 */
public abstract class CGUICommand extends AbstractAction implements IGUICommand {

    /**
     * 
     */
    private static final long serialVersionUID = -8448209123971531879L;

    String name = "";

    String alias = "";

    String command = "";

    String subcommand = "";

    String GUIprefix = "";

    String delimiter = "";

    String prefix = "";

    MWClient mwclient;

    CConnector Connector;

    public CGUICommand(MWClient mwclient) {
        super();
        this.mwclient = mwclient;
        Connector = mwclient.getConnector();
        GUIprefix = MWClient.GUI_PREFIX;
        delimiter = MWClient.PROTOCOL_DELIMITER;
        prefix = MWClient.PROTOCOL_PREFIX;
    }

    public boolean check(String tname) {
        if (tname.startsWith(GUIprefix)) {
            tname = tname.substring(GUIprefix.length());
        }
        return (name.equals(tname) || alias.equals(tname));
    }

    public boolean execute(String input) {
        return true;
    }

    protected void echo(String input) {
    }

    protected void send(String input) {
        Connector.send(prefix + command + delimiter + input);
    }

    protected void send() {
        Connector.send(prefix + command);
    }

    protected String decompose(String input) {
        if (input.startsWith(GUIprefix)) {
            input = input.substring(GUIprefix.length()).trim();
        }
        if (input.startsWith(name)) {
            input = input.substring(name.length()).trim();
        } else if (input.startsWith(alias)) {
            input = input.substring(alias.length()).trim();
        }
        return input;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public boolean isAlias() {
        return (alias != null && !alias.equals(""));
    }
}
