package sh;

import java.io.IOException;
import util.Direction;

public class GameHarbinger6 extends Game {

    public static final String NAME = "Retribution";

    public static final String INTRO = "/maps/36.html";

    private boolean attachToxin_ = false;

    public GameHarbinger6() throws IOException {
        super("/maps/36.shmap");
        getAI().setOverFear(1);
        getAI().setTargetObject(TileType.OBJECT_TOXIN);
    }

    public String getName() {
        return NAME;
    }

    protected boolean useObject(Piece p, int x, int y, int object) {
        if ((object == TileType.OBJECT_AIRPUMP1 || object == TileType.OBJECT_AIRPUMP2 || object == TileType.OBJECT_AIRPUMP3 || object == TileType.OBJECT_AIRPUMP4) && p instanceof Marine) {
            Marine m = (Marine) p;
            if (m.getCarrying() == TileType.OBJECT_TOXIN && p.useActionPoints(2)) {
                attachToxin_ = true;
                getListener().objectUsed(x, y, object, "Toxins attached to air pumps");
            }
            return true;
        } else return super.useObject(p, x, y, object);
    }

    protected boolean testDrop(Marine m, int object) {
        if (object == TileType.OBJECT_TOXIN) return m.useActionPoints(1); else return super.testDrop(m, object);
    }

    protected boolean testPickup(Marine m, int object) {
        if (object == TileType.OBJECT_TOXIN) return m.useActionPoints(2); else return super.testPickup(m, object);
    }

    public boolean canTake(Marine m) {
        return false;
    }

    public int getStatus() {
        if (attachToxin_) return STATUS_COMPLETED; else if (marines_.size() <= 0) return STATUS_FAILED; else return STATUS_NONE;
    }

    public String getStatusText(int status) {
        switch(status) {
            case STATUS_NONE:
                return "Attach toxins to air pumps";
            case STATUS_COMPLETED:
                return "The toxins have been attached, and the airpump is spreading them throughout the hulk. Victory is sweet.";
            case STATUS_FAILED:
                return "The toxins never reached the airpump. We will be forced to destroy the hulk, and lose priceless ancient technology. Most unfortunate.";
            default:
                return null;
        }
    }
}
