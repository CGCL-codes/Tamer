package sh;

import java.io.IOException;
import util.Direction;
import java.util.Vector;
import util.Point2I;

public class GameHarbinger3 extends Game {

    public static final String NAME = "Data Download";

    public static final String INTRO = "/maps/33.html";

    private boolean attachTransmitter_ = false;

    public GameHarbinger3() throws IOException {
        super("/maps/33.shmap");
        getAI().setOverFear(1);
    }

    public String getName() {
        return NAME;
    }

    protected boolean useObject(Piece p, int x, int y, int object) {
        if (object == TileType.OBJECT_CARGO && p instanceof Marine) {
            if (p.useActionPoints(4)) {
                attachTransmitter_ = true;
                getListener().objectUsed(x, y, object, "Data downloaded");
            }
            return true;
        } else return super.useObject(p, x, y, object);
    }

    public int getStatus() {
        if (bulkHeads_ <= 1) blipsPerTurn_ = bulkHeads_;
        if (attachTransmitter_) return STATUS_COMPLETED; else if (marines_.size() <= 0) return STATUS_FAILED; else return STATUS_NONE;
    }

    public String getStatusText(int status) {
        switch(status) {
            case STATUS_NONE:
                return "Download from computer console";
            case STATUS_COMPLETED:
                return "The data is being transmitted to the battlefleet computer. Commanders will be notified of tactical analysis as required.";
            case STATUS_FAILED:
                return "The data transmission never arrived. Both squads reported they were close to the objective, then nothing. It would be unfortunate if we were unable to obtain the computer's data.";
            default:
                return null;
        }
    }
}
