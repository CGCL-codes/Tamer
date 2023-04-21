package net.kano.joustsim.oscar.oscar.loginstatus;

public class DisconnectedFailureInfo extends LoginFailureInfo {

    private final boolean onPurpose;

    public DisconnectedFailureInfo(boolean onPurpose) {
        this.onPurpose = onPurpose;
    }

    public final boolean isOnPurpose() {
        return onPurpose;
    }
}
