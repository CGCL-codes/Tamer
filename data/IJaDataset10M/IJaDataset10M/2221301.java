package jrelcal.labelledrelations;

import jrelcal.relations.Pair;

public class LabelComplex extends Label<Pair<Double, Double>> {

    public LabelComplex() {
        super(new Pair<Double, Double>(0.0, 0.0));
    }

    public LabelComplex(Double a, Double b) {
        super(new Pair<Double, Double>(a, b));
    }

    public LabelComplex join(Label<Pair<Double, Double>> b) {
        return new LabelComplex((this.getValue().getFirst() + b.getValue().getFirst()), ((this.getValue().getSecond() + b.getValue().getSecond()) / 2));
    }

    public LabelComplex meet(Label<Pair<Double, Double>> b) {
        Double d = this.getValue().getFirst() - b.getValue().getFirst();
        Double s = (b.getValue().getSecond() * b.getValue().getFirst()) / (this.getValue().getFirst() + b.getValue().getFirst()) + (this.getValue().getSecond() * this.getValue().getFirst()) / (this.getValue().getFirst() + b.getValue().getFirst());
        return new LabelComplex(d, s);
    }

    public LabelComplex top() {
        return new LabelComplex(Double.MAX_VALUE, 120.0);
    }

    public LabelComplex bot() {
        return new LabelComplex(0.0, 0.0);
    }

    public String toString() {
        return this.getValue().toString();
    }
}
