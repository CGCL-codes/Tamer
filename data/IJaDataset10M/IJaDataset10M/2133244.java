package org.skirmishgame.financewizard.calculations;

import org.skirmishgame.financewizard.bo.Credit;
import org.skirmishgame.financewizard.bo.FinanceWizardProject;

public class GeneralCalc {

    public static CountValuePair GetLastingCredits(FinanceWizardProject project) {
        if (project.getCredits().size() > 0) {
            CountValuePair pair = new CountValuePair();
            for (Credit cr : project.getCredits()) {
                if (cr.getFullValue() > 0) {
                    pair.value += cr.getFullValue();
                    pair.count++;
                }
            }
            if (pair.count > 0) return pair;
        }
        return null;
    }
}
