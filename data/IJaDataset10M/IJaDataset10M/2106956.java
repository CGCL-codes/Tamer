package org.jazzteam.ModelHuman;

public class Rimantadine extends Medicine {

    private static final int adverseEffects = -15;

    private static final int therapeuticEffect = 35;

    public Rimantadine(Human human) {
        super(human);
        System.out.println("Adopted Remantadin!");
    }

    public void authorityTreat() {
        this.getIndividual().getTrunk().getHeart().setStateHealth(therapeuticEffect);
    }

    public void incidentallyAffect() {
        this.getIndividual().getTrunk().getStomach().setStateHealth(adverseEffects);
    }
}
