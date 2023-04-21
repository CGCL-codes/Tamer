package net.sourceforge.processdash.util.lock;

/**
 * Exception indicating that the lock could not be obtained because some other
 * process owns it, and we were unable to contact them.
 */
public class AlreadyLockedException extends LockFailureException {

    private String extraInfo;

    public AlreadyLockedException(String extraInfo) {
        super("Already Locked" + (extraInfo != null ? " by " + extraInfo : ""));
        this.extraInfo = extraInfo;
    }

    /**
     * Get the extra info that was written into the lock file by the owner of
     * this lock. If no extra information was provided by the owner of the lock,
     * returns null.
     */
    public String getExtraInfo() {
        return extraInfo;
    }
}
