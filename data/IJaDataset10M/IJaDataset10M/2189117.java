package org.openimmunizationsoftware.dqa.construct;

import org.openimmunizationsoftware.dqa.db.model.SubmitterProfile;

public class ConstructFactory {

    public static ConstructerInterface getConstructer(SubmitterProfile profile) {
        return new VaccinationUpdateConstructer(profile);
    }
}
