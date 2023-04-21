package systeminformationmonitor.system.object;

/**
 * Messenger Object for TCP stats.
 * @author Leo Xu
 */
public class TCPStatObject {

    private long activeOpens;

    private long passiveOpens;

    private long attemptFails;

    private long estabResets;

    private long currentEstab;

    private long inSegs;

    private long outSegs;

    private long retransSegs;

    private long inErrs;

    private long outRsts;

    /**
     * Getter method for the number of connection resets received
     * @return the number of connection resets received
     */
    public long getEstabResets() {
        return estabResets;
    }

    /**
     * Setter method for the number of connection resets received
     * @param EstabResets the number of connect resets recieved
     */
    public void setEstabResets(long EstabResets) {
        this.estabResets = EstabResets;
    }

    /**
     * Getter method for the number of active open connections
     * @return the number of active open connections
     */
    public long getActiveOpens() {
        return activeOpens;
    }

    /**
     * Setter method for number of active open connections
     * @param activeOpens
     */
    public void setActiveOpens(long activeOpens) {
        this.activeOpens = activeOpens;
    }

    /**
     * Getter method for failed connection attempts.
     * @return the number of failed connection attempts
     */
    public long getAttemptFails() {
        return attemptFails;
    }

    /**
     * Setter method for failed connection attempts.
     * @param attemptFails the number of failed connection attempts
     */
    public void setAttemptFails(long attemptFails) {
        this.attemptFails = attemptFails;
    }

    /**
     * Getter method for connection established.
     * @return the number of connection established
     */
    public long getCurrentEstab() {
        return currentEstab;
    }

    /**
     * Setter method for connection established.
     * @param currentEstab  the number of connection established.
     */
    public void setCurrentEstab(long currentEstab) {
        this.currentEstab = currentEstab;
    }

    /**
     * Getter method for bad segments received.
     * @return number of bad segments received
     */
    public long getInErrs() {
        return inErrs;
    }

    /**
     * Setter method for number of bad segments received.
     * @param inErrs number of bad segments received
     */
    public void setInErrs(long inErrs) {
        this.inErrs = inErrs;
    }

    public long getInSegs() {
        return inSegs;
    }

    public void setInSegs(long inSegs) {
        this.inSegs = inSegs;
    }

    public long getOutRsts() {
        return outRsts;
    }

    public void setOutRsts(long outRsts) {
        this.outRsts = outRsts;
    }

    public long getOutSegs() {
        return outSegs;
    }

    public void setOutSegs(long outSegs) {
        this.outSegs = outSegs;
    }

    public long getPassiveOpens() {
        return passiveOpens;
    }

    public void setPassiveOpens(long passiveOpens) {
        this.passiveOpens = passiveOpens;
    }

    public long getRetransSegs() {
        return retransSegs;
    }

    public void setRetransSegs(long retransSegs) {
        this.retransSegs = retransSegs;
    }
}
