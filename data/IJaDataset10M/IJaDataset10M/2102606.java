package emil.poker.entities;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class HandEntity_CommunityCardsType extends EntityBase implements Serializable {

    private long handValue;

    private long risk;

    public long getRisk() {
        return risk;
    }

    public void setRisk(long risk) {
        this.risk = risk;
    }

    public long getHandValue() {
        return handValue;
    }

    public void setHandValue(long handValue) {
        this.handValue = handValue;
    }
}
