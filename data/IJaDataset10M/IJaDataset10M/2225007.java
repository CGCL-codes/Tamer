package gov.nist.scap.xccdf.scoring;

import gov.nist.scap.xccdf.document.Rule;
import java.math.BigDecimal;

public class FlatScoringModel extends AbstractFlatScoringModel {

    public FlatScoringModel() {
        super("urn:xccdf:scoring:flat");
    }

    @Override
    protected BigDecimal getWeight(Rule rule) {
        return rule.getWeight();
    }
}
