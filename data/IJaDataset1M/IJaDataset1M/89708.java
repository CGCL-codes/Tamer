package net.sf.istcontract.aws.knowledge.bdi;

public class DesireProcess extends Desire {

    private Belief belief;

    public DesireProcess(Belief belief) {
        this.belief = belief;
    }

    public Belief getBelief() {
        return belief;
    }

    public void setBelief(Belief belief) {
        this.belief = belief;
    }
}
