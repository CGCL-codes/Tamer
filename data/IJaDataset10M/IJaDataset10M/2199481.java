package com.g2inc.scap.library.validators.xccdf;

import java.util.List;
import com.g2inc.scap.library.domain.Anomaly;
import com.g2inc.scap.library.domain.SCAPElement;
import com.g2inc.scap.library.domain.Anomaly.ErrorLevel;
import com.g2inc.scap.library.domain.xccdf.Ident;
import com.g2inc.scap.library.domain.xccdf.Rule;

public class Validate18 extends AbstractValidator {

    @Override
    public void applyRule(List<Anomaly> anomList, SCAPElement scapElement) {
        if (scapElement instanceof Rule) {
            Rule rule = (Rule) scapElement;
            String weight = rule.getWeightAsString();
            if (weight == null) {
                anomList.add(anomaly("weight attribute in Rule " + rule.getId() + " is missing"));
            } else {
                if (!weight.equals("10.0")) {
                    boolean foundCCE = false;
                    List<Ident> identList = rule.getIdentList();
                    for (Ident ident : identList) {
                        if (ident.getValue().indexOf("CCE-") != -1) {
                            foundCCE = true;
                            break;
                        }
                    }
                    if (foundCCE) {
                        anomList.add(anomaly("weight attribute in Rule " + rule.getId() + " is " + weight + ", not the expected value of 10.0 for CCE Rules"));
                    }
                }
            }
        }
    }

    @Override
    public Anomaly anomaly(String message) {
        Anomaly anomaly = new Anomaly();
        anomaly.setErrorLevel(ErrorLevel.ERROR);
        anomaly.setRequirementId(18);
        anomaly.setSection("4.2.5");
        anomaly.setMessage(message);
        return anomaly;
    }
}
