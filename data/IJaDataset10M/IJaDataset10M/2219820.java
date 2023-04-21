package net.sf.gap.mc.qagesa.agents.fuzzy;

import fuzzy.*;

/**
 *
 * @author Giovanni Novelli
 */
public class DelayQualityLoss extends AbstractQualityLoss {

    private FuzzyEngine fuzzyEngine;

    private FuzzyBlockOfRules fuzzyRules;

    private LinguisticVariable lvDelay;

    private LinguisticVariable lvEPS;

    private LinguisticVariable lvUQ;

    private LinguisticVariable lvQuality;

    private LinguisticVariable lvQualityLoss;

    public DelayQualityLoss() {
        this.initFuzzyEngine();
    }

    private void initFuzzyEngine() {
        fuzzyEngine = new FuzzyEngine();
    }

    public double predict(double delay, double minQuality, double currentQuality) {
        lvEPS = new LinguisticVariable("eps");
        lvEPS.add("SMALL", 0.0, 0.0, 0.0, 0.5);
        lvEPS.add("MEDIUM", 0.0, 0.5, 0.5, 1.0);
        lvEPS.add("LARGE", 0.5, 1.0, 1.0, 1.0);
        fuzzyEngine.register(lvEPS);
        lvDelay = new LinguisticVariable("delay");
        lvDelay.add("NH", -1.0, -1.0, -0.5, -0.5);
        lvDelay.add("NL", -0.5, -0.5, -0.5, -0.0);
        lvDelay.add("N", -1.0, -1.0, -0.5, -0.0);
        lvDelay.add("Z", -0.5, -0.0, 0.0, 0.25);
        lvDelay.add("P", -0.25, 0.25, 1.0, 1.0);
        lvDelay.add("PL", 0.0, 0.25, 0.5, 1.0);
        lvDelay.add("PH", 0.5, 0.5, 1.0, 1.0);
        fuzzyEngine.register(lvDelay);
        double aQL = (1.0 - minQuality);
        double lq = 0.0;
        double hq = 1.0;
        double iq = hq - lq;
        double a0 = iq * 0.0;
        double a1 = iq * 0.25;
        double a2 = iq * 0.5;
        double a3 = iq * 0.75;
        double a4 = iq;
        lvUQ = new LinguisticVariable("uq");
        lvUQ.add("BAD", a0, a0, a0, a1);
        lvUQ.add("POOR", a0, a1, a1, a2);
        lvUQ.add("FAIR", a1, a2, a2, a3);
        lvUQ.add("GOOD", a2, a3, a3, a4);
        lvUQ.add("EXCELLENT", a3, a4, a4, a4);
        fuzzyEngine.register(lvUQ);
        lvQuality = new LinguisticVariable("q");
        lvQuality.add("BAD", a0, a0, a0, a1);
        lvQuality.add("POOR", a0, a1, a1, a2);
        lvQuality.add("FAIR", a1, a2, a2, a3);
        lvQuality.add("GOOD", a2, a3, a3, a4);
        lvQuality.add("EXCELLENT", a3, a4, a4, a4);
        lvQualityLoss = new LinguisticVariable("qualityloss");
        lvQualityLoss.add("DH", -1.0 * aQL, -1.0 * aQL, -0.8 * aQL, -0.5);
        lvQualityLoss.add("DL", -0.8 * aQL, -0.5 * aQL, -0.25 * aQL, -0.0);
        lvQualityLoss.add("D", -1.0 * aQL, -1.0 * aQL, -0.25 * aQL, -0.0);
        lvQualityLoss.add("S", -0.25 * aQL, -0.0 * aQL, 0.0 * aQL, 0.25);
        lvQualityLoss.add("I", 0.0 * aQL, 0.25 * aQL, 1.0 * aQL, 1.0);
        lvQualityLoss.add("IL", 0.0 * aQL, 0.25 * aQL, 0.5 * aQL, 1.0);
        lvQualityLoss.add("IH", 0.5 * aQL, 0.8 * aQL, 1.0 * aQL, 1.0);
        lvQualityLoss.add("SMALL", 0.0 * aQL, 0.0 * aQL, 0.0 * aQL, 0.5);
        lvQualityLoss.add("MEDIUM", 0.0 * aQL, 0.5 * aQL, 0.5 * aQL, 1.0);
        lvQualityLoss.add("LARGE", 0.5 * aQL, 1.0 * aQL, 1.0 * aQL, 1.0);
        fuzzyEngine.register(lvQualityLoss);
        String[] rules = { "if delay is N then qualityloss is D", "if delay is Z then qualityloss is S", "if delay is P then qualityloss is I", "if delay is PL then qualityloss is IL", "if delay is PH then qualityloss is IH" };
        fuzzyRules = new FuzzyBlockOfRules(rules);
        fuzzyEngine.register(fuzzyRules);
        try {
            fuzzyRules.parseBlock();
        } catch (fuzzy.RulesParsingException e) {
            e.printStackTrace();
        }
        double qualityLoss = 0.0;
        try {
            double aUQ = 1.0;
            if (delay > 0) {
                aUQ = Math.exp(-delay);
            }
            double aEPS = Math.max(0.0, aUQ - currentQuality);
            lvEPS.setInputValue(aEPS);
            lvUQ.setInputValue(aUQ);
            lvDelay.setInputValue(delay + 0.125);
            lvQualityLoss.setInputValue(1.0 - currentQuality);
            fuzzyRules.evaluateBlock();
            try {
                qualityLoss = lvQualityLoss.defuzzify();
                double updateQuality = lvQuality.defuzzify();
            } catch (fuzzy.NoRulesFiredException e) {
            }
        } catch (fuzzy.EvaluationException e) {
            e.printStackTrace();
        }
        currentQuality = currentQuality - qualityLoss;
        if (currentQuality > 1.0) {
            currentQuality = 1.0;
        }
        if (currentQuality < minQuality) {
            currentQuality = minQuality;
        }
        return currentQuality;
    }
}
