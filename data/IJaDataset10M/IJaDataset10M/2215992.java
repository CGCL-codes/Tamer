package edu.miami.cs.research.apg.generator.search.representations.criteria;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import edu.miami.cs.research.apg.agregator.ontology.SongTrack.FieldNames;
import edu.miami.cs.research.apg.generator.search.representations.FieldType;
import edu.miami.cs.research.apg.generator.search.representations.PenaltyCalculator;
import edu.miami.cs.research.apg.generator.search.representations.PlaylistNode;

/**
 * @author Darrius Serrant
 *
 */
public class FractionGlobalConstraint extends GlobalConstraint {

    private int startIndex;

    private int endIndex;

    private double lowerBound;

    private double upperBound;

    private PenaltyCalculator typeConstraint;

    /**
	 * @param field
	 * @param type
	 * @param constraints
	 */
    public FractionGlobalConstraint(FieldNames field, FieldType type, HashSet<Object> constraints, int startIndex, int endIndex, double lowerBound, double upperBound) {
        super(field, type, startIndex, endIndex, (int) lowerBound, (int) upperBound, constraints);
        this.setStartIndex(startIndex);
        this.setEndIndex(endIndex);
        this.setLowerBound(lowerBound);
        this.setUpperBound(upperBound);
        if (FieldType.NOMINAL == type) {
            typeConstraint = new NominalConstraint(constraints);
        } else if (FieldType.NUMERICAL == type) {
            typeConstraint = new NumericConstraint(constraints);
        } else {
            typeConstraint = new DateConstraint(constraints);
        }
    }

    @Override
    public double calculatePenalty(PlaylistNode node) {
        return typeConstraint.calculatePenalty(node);
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    /**
	 * @author Darrius Serrant
	 *
	 */
    protected class NumericConstraint implements PenaltyCalculator {

        private ArrayList<Double> includeList;

        /**
		 * @param constraints 
		 * @param field
		 * @param type
		 * @param constraints
		 * @param index 
		 */
        public NumericConstraint(HashSet<Object> constraints) {
            includeList = new ArrayList<Double>();
            for (Object constraint : constraints) {
                includeList.add(((Number) constraint).doubleValue());
            }
        }

        public ArrayList<Double> getDomainValues() {
            return includeList;
        }

        public double calculatePenalty(PlaylistNode node) {
            double score = 0;
            Method constrainedMethod = resolveFieldToMethod();
            double valueCount = 0;
            try {
                for (int a = getStartIndex(); a <= getEndIndex(); a++) {
                    if (includeList.contains((Double) constrainedMethod.invoke(node.getMusicTitleAtIndex(a), (Object[]) null))) {
                        valueCount++;
                    }
                }
                valueCount /= 12;
                if (getLowerBound() <= valueCount && valueCount <= getUpperBound()) {
                    score = 0;
                } else {
                    score += valueCount;
                }
            } catch (Exception e) {
                score = 1;
            }
            return score;
        }
    }

    /**
	 * @author Darrius Serrant
	 *
	 */
    protected class NominalConstraint implements PenaltyCalculator {

        private HashSet<String> includeList;

        /**
		 * @param constraints 
		 * @param values2 
		 * @param field
		 * @param type
		 * @param constraints
		 * @param index 
		 */
        public NominalConstraint(HashSet<Object> constraints) {
            includeList = new HashSet<String>();
            for (Object constraint : constraints) {
                this.includeList.add((String) constraint);
            }
        }

        public HashSet<String> getDomainValues() {
            return includeList;
        }

        @SuppressWarnings("unchecked")
        public double calculatePenalty(PlaylistNode node) {
            double score = 0;
            Method constrainedMethod = resolveFieldToMethod();
            double valuesCount = 0;
            try {
                for (int a = getStartIndex(); a <= getEndIndex(); a++) {
                    Object valueSet = constrainedMethod.invoke(node.getMusicTitleAtIndex(a), (Object[]) null);
                    if (valueSet instanceof HashSet) {
                        HashSet<String> hashValues = (HashSet<String>) valueSet;
                        for (String value : hashValues) {
                            if (includeList.contains(value)) {
                                valuesCount++;
                            }
                        }
                    } else {
                        String constrainedValue = (String) valueSet;
                        if (includeList.contains(constrainedValue)) {
                            valuesCount++;
                        }
                    }
                }
                valuesCount /= 12;
                if (getLowerBound() <= valuesCount && valuesCount <= getUpperBound()) {
                    score = 0;
                } else {
                    score += valuesCount;
                }
            } catch (Exception e) {
                score = 1;
            }
            return score;
        }
    }

    protected class DateConstraint implements PenaltyCalculator {

        private HashSet<Date> includeList;

        public DateConstraint(HashSet<Object> constraints) {
            includeList = new HashSet<Date>();
            try {
                for (Object constraint : constraints) {
                    includeList.add((Date) constraint);
                }
            } catch (Exception exception) {
            }
        }

        public HashSet<Date> getDomainValues() {
            return includeList;
        }

        public double calculatePenalty(PlaylistNode node) {
            double score = 0;
            Method constrainedMethod = resolveFieldToMethod();
            double valueCount = 0;
            try {
                for (int a = getStartIndex(); a <= getEndIndex(); a++) {
                    if (includeList.contains((Date) constrainedMethod.invoke(node.getMusicTitleAtIndex(a), (Object[]) null))) {
                        valueCount++;
                    }
                }
                valueCount /= 12;
                if (getLowerBound() <= valueCount && valueCount <= getUpperBound()) {
                    score = 0;
                } else {
                    score += valueCount;
                }
            } catch (Exception e) {
                score = 1;
            }
            return score;
        }
    }
}
