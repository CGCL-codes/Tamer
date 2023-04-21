package rra.parser;

public class RRAParserMitigation {

    private String mitigation = null;

    private long damageMitigated = 0;

    public String getMitigation() {
        return mitigation;
    }

    public void setMitigation(String mitigation) {
        this.mitigation = mitigation;
    }

    public long getDamageMitigated() {
        return damageMitigated;
    }

    public void setDamageMitigated(long damageMitigated) {
        this.damageMitigated = damageMitigated;
    }
}
