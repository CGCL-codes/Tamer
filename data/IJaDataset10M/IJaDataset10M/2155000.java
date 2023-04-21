package au.gov.naa.digipres.dpr.task;

import au.gov.naa.digipres.dpr.core.DPRClient;

public class ChangePasswordTask extends Task {

    public static final String NAME = "Change Password Task";

    ChangePasswordTask(DPRClient dprClient) {
        super(dprClient);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
