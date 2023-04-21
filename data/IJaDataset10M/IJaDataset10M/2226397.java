package org.poker.prophecy.simulation.action;

import org.apache.log4j.Logger;
import org.poker.prophecy.player.general.Player;
import org.poker.prophecy.simulation.process.CurrentPlayer;
import org.poker.prophecy.simulation.process.ProcessType;
import org.poker.prophecy.simulation.process.RoundProcess;

public class CheckAction extends AbstractAction {

    private static Logger classLogger = Logger.getLogger(CheckAction.class);

    public CheckAction(Player player, RoundProcess roundProcess) {
        super(roundProcess, player, ActionType.CHECK, roundProcess.getCurrentProcessType());
    }

    public void action() {
        if (roundProcess.getCurrentProcess().canICheck(player) == false) {
            classLogger.fatal("Player " + player.getName() + " m�chte Checken obwohl er hier nicht checken kann.");
            System.exit(0);
        }
        roundProcess.getTicker().talk(player, "I check.");
        this.archive();
        int amountPaidBySomeoneYet = roundProcess.getPot().getMaximumAmountPaidByOnePersonInThisProcess(roundProcess.getCurrentProcessType());
        if (roundProcess.getBigBlindPosterThisRound().equals(player) && amountPaidBySomeoneYet == roundProcess.getBigBlindOfCurrentRound() && roundProcess.getCurrentProcessType() == ProcessType.PREFLOP) {
            roundProcess.getTicker().talkDealer("Runde beendet, Big Blind checkt.");
            roundProcess.getCurrentProcess().setCompleted();
            return;
        }
        boolean roundEnd = true;
        for (int i = 0; i < roundProcess.getCurrentPlayers().size(); i++) {
            CurrentPlayer aktPlayer = roundProcess.getCurrentPlayers().elementAt(i);
            if (aktPlayer.isFolded() || roundProcess.getCurrentProcess().hasCheckedLastTime(aktPlayer.getPlayer())) {
            } else {
                roundEnd = false;
            }
        }
        if (roundEnd) {
            roundProcess.getTicker().talkDealer("Runde beendet, alle haben gecheckt.");
            roundProcess.getCurrentProcess().setCompleted();
            return;
        }
    }
}
