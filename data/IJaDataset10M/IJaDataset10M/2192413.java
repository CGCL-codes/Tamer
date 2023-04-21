package jpoco.client;

public class Email {

    public static final String HOME = "home";

    public static final String WORK = "work";

    private String value;

    private String type;

    private boolean primary = false;

    public String getValue() {
        return value;
    }

    public void setValue(String v) {
        this.value = v;
    }

    public String getType() {
        return type;
    }

    public void setType(String t) {
        this.type = t;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean b) {
        this.primary = b;
    }

    public boolean isWork() {
        return WORK.equalsIgnoreCase(this.getType());
    }

    public boolean isHome() {
        return HOME.equalsIgnoreCase(this.getType());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getType() != null) {
            sb.append(this.getType());
            sb.append(": ");
        }
        sb.append(this.getValue());
        return sb.toString();
    }
}
