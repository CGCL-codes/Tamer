package saf.moves;

import saf.*;

public class WalkAway extends StepAway {

    public WalkAway(Bot b) {
        super(b);
        positionsPerTick = Math.round(bot.getSpeed() / 2);
    }

    public String toString() {
        return "WalkAway";
    }
}
