package net.sourceforge.robotnik.poker.players;

import java.util.Arrays;
import net.sourceforge.robotnik.poker.Decision;

public class BetOrRaisePlayer extends PokerConcretePlayer {

    @Override
    public Decision makeDecision(Decision[] legalDecisions) {
        if (Arrays.asList(legalDecisions).contains(Decision.RAISE)) {
            return raise();
        } else if (Arrays.asList(legalDecisions).contains(Decision.BET)) {
            return bet();
        } else {
            return call();
        }
    }
}
