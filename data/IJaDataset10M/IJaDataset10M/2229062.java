package sh;

import java.io.IOException;

public class GameSin6 extends Game {

    public static final String NAME = "Defend";

    public static final String INTRO = "/maps/16.html";

    private final int surviveRounds_ = 16;

    public GameSin6() throws IOException {
        super("/maps/16.shmap");
        getAI().setOverFear(2);
        getAI().setTargetObject(TileType.OBJECT_TOXIN);
    }

    public String getName() {
        return NAME;
    }

    protected boolean useObject(Piece p, int x, int y, int object) {
        if (object == TileType.OBJECT_TOXIN && (p instanceof Blip || p instanceof Stealer)) {
            if (p.useActionPoints(1)) {
                getListener().pieceCloseCombat(p);
                int c = p.getCloseCombatValue(r_);
                if (c >= 6) {
                    getMap().setObject(x, y, 0);
                    getListener().pieceCloseCombatObject(p, x, y, object);
                } else getListener().pieceCloseCombatMiss(p);
            }
            return true;
        } else return super.useObject(p, x, y, object);
    }

    public int getStatus() {
        if (round_ >= surviveRounds_) return STATUS_COMPLETED; else if (hasFailed()) return STATUS_FAILED; else return STATUS_NONE;
    }

    public String getStatusText(int status) {
        switch(status) {
            case STATUS_NONE:
                return "Defend toxins for " + (surviveRounds_ - round_) + " turns";
            case STATUS_COMPLETED:
                return "The toxins have done their work. Thousands of dormant Genestelers have been slain. The remaining active stealers will be easy to destroy. Good work!";
            case STATUS_FAILED:
                return "The air ducts to the cryo chambers have been damaged! The toxin is not getting through! Back to the boats! Thousands of stealers are massing to attack! Abandon the assault!";
            default:
                return null;
        }
    }

    public boolean hasFailed() {
        boolean flame = false;
        for (int x = 15; !flame && x <= 17; ++x) flame = getMap().isFire(x, 23);
        for (int y = 24; !flame && y <= 28; ++y) flame = getMap().isFire(16, y);
        for (int y = 29; !flame && y <= 31; ++y) for (int x = 15; !flame && x <= 17; ++x) flame = getMap().isFire(x, y);
        if (flame) return true; else if (getMap().getObject(16, 31) != TileType.OBJECT_TOXIN) return true; else return marines_.size() <= 0;
    }
}
