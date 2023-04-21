package org.xmlcml.textalign.algorithm;

public class PenaltyCalculator {

    private PenaltyCalculationStrategy strategy;

    public PenaltyCalculator(PenaltyCalculationStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculateDistance() {
        return strategy.calculateDistance();
    }

    public void setStrings(String s1, String s2) {
        strategy.setStrings(s1, s2);
    }

    public static void main(String[] args) {
        PenaltyCalculator penaltyCalculator;
        String s1 = "a";
        String s2 = "b";
        AlphabetMatrix alphabetMatrix = new AlphabetMatrix();
        penaltyCalculator = new PenaltyCalculator(new AlignStringsStrategy(alphabetMatrix));
        penaltyCalculator.setStrings(s1, s2);
        double d1 = penaltyCalculator.calculateDistance();
        penaltyCalculator = new PenaltyCalculator(new StringEqualityStrategy());
        penaltyCalculator.setStrings(s1, s2);
        double d2 = penaltyCalculator.calculateDistance();
        penaltyCalculator = new PenaltyCalculator(new StringOverlapStrategy());
        penaltyCalculator.setStrings(s1, s2);
        double d3 = penaltyCalculator.calculateDistance();
    }
}
