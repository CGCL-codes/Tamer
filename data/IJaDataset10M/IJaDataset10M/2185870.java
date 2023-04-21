package net.sourceforge.sandirc;

import jerklib.ConnectionManager;
import jerklib.ProfileImpl;
import jerklib.Session;
import net.sourceforge.sandirc.gui.IRCWindow;
import net.sourceforge.sandirc.gui.MainWindow;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Propriétaire
 */
public class IRCClient {

    private static IRCClient me;

    private ConnectionManager manager;

    private MainWindow winder = MainWindow.getInstance();

    private Session selectedSession;

    private IRCClient() {
        String userName = System.getProperty("user.name");
        manager = new ConnectionManager(new ProfileImpl(userName, userName, userName + "1", userName + "2"));
        winder.inputBar.addInputListener(new UserInputHandler(manager));
        winder.pane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Session session = ((IRCWindow) winder.pane.getSelectedComponent()).getDocument().getSession();
                if (session != null) {
                    selectedSession = session;
                }
            }
        });
    }

    /**
     *
     * @return
     */
    public Session getSelectedSession() {
        return selectedSession;
    }

    /**
     *
     * @param selectedSession
     */
    public void setSelectedSession(Session selectedSession) {
        this.selectedSession = selectedSession;
    }

    /**
     *
     * @return
     */
    public MainWindow getMainWindow() {
        return winder;
    }

    /**
     *
     * @return
     */
    public static IRCClient getInstance() {
        if (me == null) {
            me = new IRCClient();
        }
        return me;
    }

    public static void main(String[] args) {
        getInstance();
    }
}
