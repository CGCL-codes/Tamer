package de.haumacher.timecollect;

import java.util.Arrays;

public class Ticket {

    /**
	 * Live-cycle state of {@link Ticket}s.
	 * 
	 * @author Bernhard Haumacher
	 */
    public enum LCState {

        /** Ticket only exists in local DB, because it is potentially referenced from the an {@link Activity}. */
        INACTIVE, /** Ticket is matched by the current query but should not be displayed as selection in the UI. */
        DROPPED, /** Ticket is matched by the current query and should be displayed as potential selection in the UI. */
        MATCHED, /** Ticket is explicitly selected and should be displayed, even if it is not matched by the current query. */
        SELECTED
    }

    private int id;

    private int sid;

    private int rid;

    private String summary;

    private String type;

    private String component;

    private String state;

    private LCState lc;

    private String mnemonic;

    public Ticket(int sid, int rid, String mnemonic, String summary, String type, String component, String state, LCState lc) {
        this.sid = sid;
        this.rid = rid;
        this.mnemonic = mnemonic;
        this.summary = summary;
        this.type = type;
        this.component = component;
        this.state = state;
        this.lc = lc;
    }

    public Ticket(int id) {
        this.id = id;
    }

    public void initId(int id) {
        assert this.id == 0 : "ID must be assigned only once.";
        this.id = id;
    }

    public void resetId() {
        this.id = 0;
    }

    public int getId() {
        return id;
    }

    public int getSystemId() {
        return sid;
    }

    public void setSystemId(int sid) {
        this.sid = sid;
    }

    public boolean isLocal() {
        return sid == 0;
    }

    public int getRemoteId() {
        return rid;
    }

    public void setRemoteId(int rid) {
        this.rid = rid;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LCState getLiveCycleState() {
        return lc;
    }

    public void setLiveCycleState(LCState lc) {
        this.lc = lc;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Ticket)) {
            return false;
        }
        return equalsTicket(this, (Ticket) obj);
    }

    private boolean equalsTicket(Ticket ticket1, Ticket ticket2) {
        int id1 = ticket1.getId();
        int id2 = ticket2.getId();
        return (id1 == id2) && (id1 != 0);
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public final String toString() {
        StringBuilder buffer = new StringBuilder(this.getClass().getSimpleName());
        buffer.append('(');
        appendProperties(buffer);
        buffer.append(')');
        return buffer.toString();
    }

    protected void appendProperties(StringBuilder buffer) {
        buffer.append("id=");
        buffer.append(id);
    }

    public Object key() {
        if (getSystemId() == 0) {
            return Arrays.<Object>asList(getSystemId(), getComponent(), getSummary());
        } else {
            return Arrays.<Object>asList(getSystemId(), getRemoteId());
        }
    }
}
