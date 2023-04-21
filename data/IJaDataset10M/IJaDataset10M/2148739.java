package br.ufrgs.wumpus.elements;

public class CellPerception {

    private boolean smell;

    private boolean shine;

    private boolean freshAir;

    private boolean roar;

    private boolean impact;

    public boolean isSmell() {
        return smell;
    }

    public void setSmell(boolean smell) {
        this.smell = smell;
    }

    public boolean isShine() {
        return shine;
    }

    public void setShine(boolean shine) {
        this.shine = shine;
    }

    public boolean isFreshAir() {
        return freshAir;
    }

    public void setFreshAir(boolean freshAir) {
        this.freshAir = freshAir;
    }

    public boolean isRoar() {
        return roar;
    }

    public void setRoar(boolean roar) {
        this.roar = roar;
    }

    public boolean isImpact() {
        return impact;
    }

    public void setImpact(boolean impact) {
        this.impact = impact;
    }
}
