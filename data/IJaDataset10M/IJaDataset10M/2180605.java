package sh;

import java.io.IOException;
import util.Direction;

public class GameHarbinger1 extends Game {

    public static final String NAME = "Seize and Secure";

    public static final String INTRO = "/maps/31.html";

    public GameHarbinger1() throws IOException {
        super("/maps/31.shmap");
        getAI().setOverFear(1);
    }

    public String getName() {
        return NAME;
    }

    public int getStatus() {
        if (bulkHeads_ <= 2) blipsPerTurn_ = bulkHeads_;
        if (bulkHeads_ <= 0) return STATUS_COMPLETED; else if (marines_.size() <= 0) if (bulkHeads_ <= 1) return STATUS_DRAWN; else return STATUS_FAILED; else return STATUS_NONE;
    }

    public String getStatusText(int status) {
        switch(status) {
            case STATUS_NONE:
                return "Seal " + bulkHeads_ + " entry points";
            case STATUS_COMPLETED:
                return "The area is secured. The remainder of the force will now begin landing. Now we will show these beasts the meaning of fear!";
            case STATUS_FAILED:
                return "All contact with Squads Laertes and Tycho has been lost. We must assume the worst.";
            case STATUS_DRAWN:
                return "The perimeter is adequate, though more Marines will be required to seal it. This is not a good start to a campaign.";
            default:
                return null;
        }
    }
}
