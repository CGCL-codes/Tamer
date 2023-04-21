package org.mathig.poker.player;

import org.mathig.poker.Action;
import org.mathig.poker.AfterBetActions;
import org.mathig.poker.AfterNothingActions;
import org.mathig.poker.Card;
import org.mathig.poker.Context;
import org.mathig.poker.Player;

public class TestPlayer implements Player {

    public boolean doYouMuck(Context table) {
        return true;
    }

    public String getName() {
        return "";
    }

    public void receive(Context context, Card[] cards) {
    }

    public Action response(Context table, AfterNothingActions actions) {
        return null;
    }

    public Action response(Context table, AfterBetActions actions) {
        return null;
    }
}
