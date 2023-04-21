package Alge;

public class NaturalJoin extends Relation {

    public Relation leftR;

    public Relation rightR;

    public NaturalJoin(Relation leftR, Relation rightR) {
        this.leftR = leftR;
        this.rightR = rightR;
    }
}
