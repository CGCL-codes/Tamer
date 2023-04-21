package edu.collablab.brenthecht.wikapidia.sr;

public class PairValueTuple {

    private String word1;

    private String word2;

    private Double gsValue;

    public PairValueTuple(String word1, String word2, double gsValue) {
        this.word1 = word1;
        this.word2 = word2;
        this.gsValue = gsValue;
    }

    public String getWord1() {
        return word1;
    }

    public String getWord2() {
        return word2;
    }

    public Double getGsValue() {
        return gsValue;
    }
}
