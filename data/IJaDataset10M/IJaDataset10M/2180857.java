package se.ramfelt.psn.model;

/**
 * Trophy summary
 */
public class TrophySummary {

    private int level;

    private int platinum;

    private int gold;

    private int silver;

    private int bronze;

    public TrophySummary() {
    }

    public TrophySummary(int level, int platinum, int gold, int silver, int bronze) {
        this.level = level;
        this.platinum = platinum;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPlatinum() {
        return platinum;
    }

    public void setPlatinum(int platinum) {
        this.platinum = platinum;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getSilver() {
        return silver;
    }

    public void setSilver(int silver) {
        this.silver = silver;
    }

    public int getBronze() {
        return bronze;
    }

    public void setBronze(int bronze) {
        this.bronze = bronze;
    }

    public int getCount() {
        return bronze + silver + gold + platinum;
    }

    @Override
    public String toString() {
        return "TrophySummary[level=" + level + ", platinum=" + platinum + ", gold=" + gold + ", silver=" + silver + ", bronze=" + bronze + "]";
    }
}
