package client.gui.play;

import client.ClientAction;
import client.gui.IErrorPrinter;
import events.*;
import exceptions.TimeCommandException;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Vector;
import javax.swing.JPanel;
import system.ITimeAction;

@SuppressWarnings("serial")
public class PlayModule extends JPanel implements ActionListener, TimeUpdateEventListener, ScoreUpdateEventListener, SystemActionEventListener {

    private ScorePanel scorePanel = null;

    private PlayingPanel playingPanel = null;

    private ITimeAction iTimeAction = null;

    private ActionEventListener actionEventListener = null;

    private IErrorPrinter errorPrinter = null;

    private int nbDisplayed = utils.Constants.DEFAULT_NB_DISPLAYED;

    public PlayModule(String hostName, String guestName, Map<Integer, String> hostPlayers, Map<Integer, String> guestPlayers, int hostScore, int guestScore, Vector<Vector<ClientAction>> hostActions, Vector<Vector<ClientAction>> guestActions, ITimeAction iTimeAction, ActionEventListener actionEventListener, IErrorPrinter errorPrinter, int nbDisplayed) {
        this.iTimeAction = iTimeAction;
        this.actionEventListener = actionEventListener;
        this.errorPrinter = errorPrinter;
        this.nbDisplayed = nbDisplayed;
        initialize(hostName, guestName, hostPlayers, guestPlayers, hostScore, guestScore, hostActions, guestActions);
    }

    private void initialize(String hostName, String guestName, Map<Integer, String> hostPlayers, Map<Integer, String> guestPlayers, int hostScore, int guestScore, Vector<Vector<ClientAction>> hostActions, Vector<Vector<ClientAction>> guestActions) {
        this.setLayout(new BorderLayout());
        scorePanel = new ScorePanel(hostName, guestName, hostScore, guestScore, this);
        playingPanel = new PlayingPanel(hostName, guestName, hostPlayers, guestPlayers, hostActions, guestActions);
        playingPanel.setActionEventListener(actionEventListener);
        playingPanel.setNbDisplayedActions(-1);
        this.add(scorePanel, BorderLayout.NORTH);
        this.add(playingPanel, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals("start")) {
            startMatch();
        } else if (actionCommand.equals("pause")) {
            pauseMatch();
        }
    }

    public void startMatch() {
        try {
            iTimeAction.startMatch();
            scorePanel.setButtonSelected(true);
            playingPanel.setNbDisplayedActions(nbDisplayed);
        } catch (TimeCommandException e) {
            errorPrinter.displayError(e.getMessage());
        }
    }

    public void pauseMatch() {
        try {
            iTimeAction.pauseMatch();
            scorePanel.setButtonSelected(false);
            playingPanel.setNbDisplayedActions(-1);
        } catch (TimeCommandException e) {
            errorPrinter.displayError(e.getMessage());
        }
    }

    public void stopMatch() {
        try {
            iTimeAction.stopMatch();
            scorePanel.setButtonSelected(false);
            playingPanel.setNbDisplayedActions(-1);
        } catch (TimeCommandException e) {
            errorPrinter.displayError(e.getMessage());
        }
    }

    public void cancelLastAction() {
        actionEventListener.cancelLastAction();
    }

    public void timeUpdated(TimeUpdateEvent e) {
        scorePanel.timeUpdated(e);
    }

    public void newSet(int setNumber) {
        playingPanel.newSet(setNumber);
    }

    public void scoreUpdated(ScoreUpdateEvent e) {
        scorePanel.scoreUpdated(e);
    }

    public void systemActionPerformed(SystemActionEvent e) {
        playingPanel.systemActionPerformed(e);
    }

    public void cancelLastAction(boolean host) {
        playingPanel.cancelLastAction(host);
    }

    public void setHostActions(Vector<Vector<ClientAction>> actions) {
        playingPanel.setHostActions(actions);
    }

    public void setGuestActions(Vector<Vector<ClientAction>> actions) {
        playingPanel.setGuestActions(actions);
    }
}
