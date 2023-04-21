package org.restfaces.config.setup;

public class ConfigNavigationCase {

    private String fromViewId = null;

    private String fromAction = null;

    private String fromOutcome = null;

    private String toViewId = null;

    private String key = null;

    public String getFromViewId() {
        return (this.fromViewId);
    }

    public void setFromViewId(String fromViewId) {
        this.fromViewId = fromViewId;
    }

    public String getFromAction() {
        return (this.fromAction);
    }

    public void setFromAction(String fromAction) {
        this.fromAction = fromAction;
    }

    public String getFromOutcome() {
        return (this.fromOutcome);
    }

    public void setFromOutcome(String fromOutcome) {
        this.fromOutcome = fromOutcome;
    }

    public String getToViewId() {
        return (this.toViewId);
    }

    public void setToViewId(String toViewId) {
        this.toViewId = toViewId;
    }

    protected String redirect = null;

    public boolean hasRedirect() {
        return null != redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    /**
     * The "key" is defined as the combination of
     * <code>from-view-id</code><code>from-action</code>
     * <code>from-outcome</code>.
     */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("FROM VIEW ID:").append(getFromViewId());
        sb.append("\nFROM ACTION:").append(getFromAction());
        sb.append("\nFROM OUTCOME:").append(getFromOutcome());
        sb.append("\nTO VIEW ID:").append(getToViewId());
        sb.append("\nREDIRECT:").append(hasRedirect());
        return sb.toString();
    }
}
