package server;

/**
 * Instantiate this to stop soccer servers.
 *
 * @author Daniel Wood (daniel-g-wood@users.sourceforge.net)
 * @version 2010.02.04
 * @since 0.3
 */
public class StopServerCommand extends AbstractCommand {

    /**
     * Kill the SServers.
     */
    protected StopServerCommand() {
        String command = "killall " + Server.SERVER_APPLICATION;
        cmd = command.getBytes();
        super.start();
    }
}
