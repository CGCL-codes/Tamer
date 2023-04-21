package edu.gmu.projects.fireresponder;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class RelevanceEngineTester {

    public static enum RelevanceScale {

        NOT_RELEVANT, SLIGHT_RELEVANCE, LOW_RELEVANCE, MODERATE_RELEVANCE, HIGH_RELEVANCE, VERY_HIGH_RELEVANCE
    }

    ;

    public static RelevanceScale getRelevanceScale(double relevance) {
        if (0 < relevance && relevance < 0.2) {
            return RelevanceScale.SLIGHT_RELEVANCE;
        } else if (0.2 <= relevance && relevance < 0.4) {
            return RelevanceScale.LOW_RELEVANCE;
        } else if (0.4 <= relevance && relevance < 0.6) {
            return RelevanceScale.MODERATE_RELEVANCE;
        } else if (0.6 <= relevance && relevance < 0.8) {
            return RelevanceScale.HIGH_RELEVANCE;
        } else if (0.8 <= relevance && relevance <= 1) {
            return RelevanceScale.VERY_HIGH_RELEVANCE;
        }
        return RelevanceScale.NOT_RELEVANT;
    }

    public static String getRelevanceScaleText(RelevanceScale scale) {
        switch(scale) {
            case NOT_RELEVANT:
                return "NONE";
            case SLIGHT_RELEVANCE:
                return "SLIGHT";
            case LOW_RELEVANCE:
                return "LOW";
            case MODERATE_RELEVANCE:
                return "MODERATE";
            case HIGH_RELEVANCE:
                return "HIGH";
            case VERY_HIGH_RELEVANCE:
                return "VERY HIGH";
        }
        return "UNKNOWN";
    }

    public static String getRelevanceScaleText(double value) {
        return getRelevanceScaleText(getRelevanceScale(value));
    }

    public static void main(String[] args) {
        PrintStream stream;
        try {
            stream = new PrintStream("relevance.txt");
            for (double probOfFire = 0; probOfFire <= 1; probOfFire += 0.1) {
                for (double probOfPeople = 0; probOfPeople <= 1; probOfPeople += 0.1) {
                    for (int estimatedTimeTo = 0; estimatedTimeTo <= 3600; estimatedTimeTo += 600) {
                        for (double probOfFireThen = 0; probOfFireThen <= 1; probOfFireThen += 0.1) {
                            double relevance = calculateRelevanceValue(probOfFire, probOfPeople, estimatedTimeTo, probOfFireThen);
                            String relevanceString = String.format("%.6f %.6f %d %.6f %.6f", probOfFire, probOfPeople, estimatedTimeTo, probOfFireThen, relevance);
                            System.out.println(relevanceString);
                            stream.println(relevanceString);
                        }
                    }
                }
            }
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static double calculateRelevanceValue(double probOfFire, double probOfPeople, int estimatedTimeTo, double probOfFireThen) {
        double estimatedTimeToNorm = estimatedTimeTo > 3600 ? 0 : estimatedTimeTo / 3600.0;
        double scaledTimeTerm = 1.0 - Math.pow(estimatedTimeToNorm, 2);
        return 0.5 * (probOfPeople * probOfFire) + 0.2 * scaledTimeTerm + 0.3 * probOfFireThen;
    }
}
