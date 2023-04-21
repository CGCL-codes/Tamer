package net.slashie.serf.ai;

public class RangedActionSpec implements java.io.Serializable {

    private String attackId;

    private int range;

    private int frequency;

    private int chargeCounter;

    private String effectType;

    private String effectID;

    private String effectWav;

    public RangedActionSpec(String pAttackId, int pRange, int pFrequency, String pEffectType, String pEffectString) {
        attackId = pAttackId;
        range = pRange;
        frequency = pFrequency;
        effectType = pEffectType;
        effectID = pEffectString;
    }

    public void setEffectWav(String value) {
        effectWav = value;
    }

    public String getEffectWav() {
        return effectWav;
    }

    public String getAttackId() {
        return attackId;
    }

    public int getRange() {
        return range;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getEffectID() {
        return effectID;
    }

    public String getEffectType() {
        return effectType;
    }

    public int getChargeCounter() {
        return chargeCounter;
    }

    public void setChargeCounter(int chargeCounter) {
        this.chargeCounter = chargeCounter;
    }
}
