package org.jazzteam.ModelHuman;

public abstract class Medicine {

    private Human individual;

    public Medicine(Human human) {
        this.setIndividual(human);
    }

    public abstract void authorityTreat();

    public abstract void incidentallyAffect();

    public final void setIndividual(Human human) {
        this.individual = human;
    }

    public final Human getIndividual() {
        return individual;
    }
}
