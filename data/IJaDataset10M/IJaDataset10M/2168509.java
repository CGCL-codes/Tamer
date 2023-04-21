package org.mobicents.servlet.sip.pbx.location;

/**
 * @author Thomas Leseney
 */
public class Binding {

    private String aor;

    private String contact;

    private String callId;

    private int cseq;

    private Long id;

    private int expires;

    private long expirationTime;

    public Binding() {
    }

    public Binding(String aor, String contact) {
        this.aor = aor;
        this.contact = contact;
    }

    public String getAor() {
        return aor;
    }

    public String getCallId() {
        return callId;
    }

    public int getCseq() {
        return cseq;
    }

    public String getContact() {
        return contact;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public int getExpires() {
        return expires;
    }

    public Long getId() {
        return id;
    }

    public void setAor(String aor) {
        this.aor = aor;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public void setCseq(int cseq) {
        this.cseq = cseq;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void setExpires(int expires) {
        this.expires = expires;
        expirationTime = System.currentTimeMillis() + expires * 1000;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String toString() {
        return aor + "->" + contact + " (" + callId + "/" + cseq + "/" + expires + ")";
    }
}
