package ao.holdem.model.card.chance;

import ao.holdem.model.Avatar;
import ao.holdem.model.Round;
import ao.holdem.model.card.Community;
import ao.holdem.model.card.Hole;
import java.util.Map;

/**
 *
 */
public class LiteralCards implements ChanceCards {

    private Community community;

    private Map<Avatar, Hole> holes;

    public LiteralCards(Community community, Map<Avatar, Hole> holes) {
        this.community = community;
        this.holes = holes;
    }

    public Community community(Round asOf) {
        return community.asOf(asOf);
    }

    public Hole hole(Avatar forPlayer) {
        return holes.get(forPlayer);
    }

    public ChanceCards prototype() {
        return this;
    }
}
