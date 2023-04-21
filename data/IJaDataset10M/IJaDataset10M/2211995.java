package org.thirdway.domain.family;

import org.thirdway.domain.common.Strike;

/**
 * @author crosenq
 */
public class FamilyStrike {

    Family family;

    Strike strike;

    public FamilyStrike(Family family, Strike strike) {
        this.strike = strike;
        this.family = family;
    }

    public Family getFamily() {
        return this.family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Strike getStrike() {
        return this.strike;
    }

    public void setStrike(Strike strike) {
        this.strike = strike;
    }
}
