package com.barbarianprince.bus.event2;

import com.barbarianprince.bus.condition.Condition;
import com.barbarianprince.bus.enums.GameState;
import com.barbarianprince.bus.turn.Flow;
import com.barbarianprince.main.controllers.BPController;

public class GameOverEvent extends Event {

    private boolean fired;

    private boolean playerWon;

    public GameOverEvent(final boolean won) {
        this(null, won);
    }

    /**
	 * Creates a new instance of GameOverEvent.java.
	 * @param c the <code>Event</code>'s <code>Condition</code>.
	 * @param won
	 */
    protected GameOverEvent(final Condition c, final boolean won) {
        super(c);
        playerWon = won;
        fired = false;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final Event clone() {
        return new GameOverEvent(super.getCondition(), playerWon);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final void fire() {
        if (super.happens()) {
            if (fired) {
                BPController.setState(GameState.INTRO);
            } else {
                fired = true;
                Flow.clear();
                Flow.addEvent(this);
                String title = "";
                StringBuffer message = new StringBuffer();
                if (playerWon) {
                } else {
                    message.append("<p>You have lost your chance to reclaim ");
                    message.append("your father's kingdom.</p>");
                    message.append("<center><h1><strong>");
                    message.append("You have lost.");
                    message.append("</strong></h1></center>");
                }
                Flow.addEvent(this);
                BPController.showMessage(null, title, message.toString());
            }
        } else {
            super.setResolved();
        }
    }
}
