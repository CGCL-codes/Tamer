package org.az.hhp.params;

import org.az.hhp.domain.Claim;
import org.az.hhp.domain.Dict;

public class PrimaryConditionGroupParam implements ClaimParameter {

    @Override
    public String valueOf(final Claim c) {
        return Dict.primaryConditionGroups.getCodeName(c.getPrimaryConditionGroup());
    }
}
