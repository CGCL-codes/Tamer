package edu.miami.cs.research.apg.generator.search.representations.criteria;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import edu.miami.cs.research.apg.agregator.ontology.SongTrack;
import edu.miami.cs.research.apg.agregator.ontology.SongTrack.FieldNames;
import edu.miami.cs.research.apg.generator.search.representations.FieldType;
import edu.miami.cs.research.apg.generator.search.representations.PenaltyCalculator;
import edu.miami.cs.research.apg.generator.search.representations.PlaylistNode;

/**
 * @author Darrius Serrant
 *
 */
public class EqualBinaryConstraint extends BinaryConstraint {

    private int firstIndex;

    private int secondIndex;

    private PenaltyCalculator typeConstraint;

    /**
	 * @param field
	 * @param type
	 * @param constraints
	 * @param index
	 */
    public EqualBinaryConstraint(FieldNames field, FieldType type, Integer firstIndex, Integer secondIndex, HashSet<Object> values) {
        super(field, type, values);
        this.setFirstIndex(firstIndex);
        this.setSecondIndex(secondIndex);
        if (FieldType.DATE == getFieldType()) {
            setTypeConstraint(new DateConstraint(super.getValuesDomain()));
        } else if (FieldType.NOMINAL == getFieldType()) {
            setTypeConstraint(new NominalConstraint(super.getValuesDomain()));
        } else {
            setTypeConstraint(new NumericConstraint(super.getValuesDomain()));
        }
    }

    @Override
    public double calculatePenalty(PlaylistNode value) {
        double penalty = typeConstraint.calculatePenalty(value);
        if (getFieldType() == FieldType.NUMERICAL) {
            super.getFirstDomainValues().retainAll(((NumericConstraint) typeConstraint).getFirstDomain());
            super.getSecondDomainValues().retainAll(((NumericConstraint) typeConstraint).getSecondDomain());
        } else if (getFieldType() == FieldType.NOMINAL) {
            super.getFirstDomainValues().retainAll(((NominalConstraint) typeConstraint).getFirstDomain());
            super.getSecondDomainValues().retainAll(((NominalConstraint) typeConstraint).getSecondDomain());
        } else {
            super.getFirstDomainValues().retainAll(((DateConstraint) typeConstraint).getFirstDomain());
            super.getSecondDomainValues().retainAll(((DateConstraint) typeConstraint).getSecondDomain());
        }
        return penalty;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(int firstIndex) {
        this.firstIndex = firstIndex;
    }

    public int getSecondIndex() {
        return secondIndex;
    }

    public void setSecondIndex(int secondIndex) {
        this.secondIndex = secondIndex;
    }

    public PenaltyCalculator getTypeConstraint() {
        return typeConstraint;
    }

    public void setTypeConstraint(PenaltyCalculator typeConstraint) {
        this.typeConstraint = typeConstraint;
    }

    /**
	 * @author Darrius Serrant
	 *
	 */
    public class NumericConstraint implements PenaltyCalculator {

        public double firstValue;

        public double secondValue;

        private ArrayList<Double> firstDomain;

        private ArrayList<Double> secondDomain;

        /**
		 * @param hashSet 
		 * @param field
		 * @param type
		 * @param constraints
		 * @param index 
		 */
        public NumericConstraint(HashSet<?> hashSet) {
            firstDomain = new ArrayList<Double>();
            secondDomain = new ArrayList<Double>();
            for (Object value : hashSet) {
                firstDomain.add(((Number) value).doubleValue());
                secondDomain.add(((Number) value).doubleValue());
            }
        }

        public ArrayList<Double> getFirstDomain() {
            return firstDomain;
        }

        public ArrayList<Double> getSecondDomain() {
            return secondDomain;
        }

        public double calculatePenalty(PlaylistNode node) {
            double score = 0;
            double firstDoubleValue = 0;
            double secondDoubleValue = 0;
            Method constrainedMethod = resolveFieldToMethod();
            SongTrack firstConstrainedTrack = node.getMusicTitleAtIndex(firstIndex);
            SongTrack secondConstrainedTrack = node.getMusicTitleAtIndex(secondIndex);
            Object firstConstrainedValue = null;
            Object secondConstrainedValue = null;
            try {
                firstConstrainedValue = constrainedMethod.invoke(firstConstrainedTrack, (Object[]) null);
                secondConstrainedValue = constrainedMethod.invoke(secondConstrainedTrack, (Object[]) null);
                if (firstConstrainedValue instanceof Double) {
                    firstDoubleValue = ((Double) firstConstrainedValue).doubleValue();
                    secondDoubleValue = ((Double) secondConstrainedValue).doubleValue();
                } else {
                    firstDoubleValue = ((Integer) firstConstrainedValue).intValue();
                    secondDoubleValue = ((Integer) secondConstrainedValue).intValue();
                }
                ArrayList<Double> secondSingle = new ArrayList<Double>();
                secondSingle.add(secondDoubleValue);
                secondSingle.trimToSize();
                firstDomain.retainAll(secondSingle);
                ArrayList<Double> firstSingle = new ArrayList<Double>();
                firstSingle.add(firstDoubleValue);
                firstSingle.trimToSize();
                secondDomain.remove(firstDoubleValue);
                if (firstDoubleValue != secondDoubleValue) {
                    score = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return score;
        }
    }

    /**
	 * @author Darrius Serrant
	 *
	 */
    public class NominalConstraint implements PenaltyCalculator {

        public HashSet<String> values;

        private ArrayList<String> firstDomain;

        private ArrayList<String> secondDomain;

        /**
		 * @param hashSet 
		 * @param field
		 * @param type
		 * @param constraints
		 * @param index 
		 */
        public NominalConstraint(HashSet<?> hashSet) {
            firstDomain = new ArrayList<String>();
            secondDomain = new ArrayList<String>();
            for (Object value : hashSet) {
                firstDomain.add((String) value);
                secondDomain.add((String) value);
            }
        }

        public Collection<String> getSecondDomain() {
            return secondDomain;
        }

        public Collection<String> getFirstDomain() {
            return firstDomain;
        }

        @SuppressWarnings("unchecked")
        public double calculatePenalty(PlaylistNode node) {
            double score = 0;
            Method constrainedMethod = resolveFieldToMethod();
            SongTrack firstConstrainedTrack = node.getMusicTitleAtIndex(getFirstIndex());
            SongTrack secondConstrainedTrack = node.getMusicTitleAtIndex(getSecondIndex());
            try {
                Object firstValueSet = constrainedMethod.invoke(firstConstrainedTrack, (Object[]) null);
                Object secondValueSet = constrainedMethod.invoke(secondConstrainedTrack, (Object[]) null);
                if (firstValueSet instanceof HashSet) {
                    HashSet<String> firstHashValues = (HashSet<String>) firstValueSet;
                    HashSet<String> secondHashValues = (HashSet<String>) secondValueSet;
                    firstDomain.retainAll(secondHashValues);
                    secondDomain.retainAll(firstHashValues);
                    if (firstHashValues.containsAll(secondHashValues)) {
                        score = 0;
                    } else {
                        score = Math.abs(firstHashValues.size() - secondHashValues.size()) / Math.max(firstHashValues.size(), secondHashValues.size());
                    }
                } else {
                    String firstConstrainedValue = (String) firstValueSet;
                    String secondConstrainedValue = (String) secondValueSet;
                    if (firstConstrainedValue.equals(secondConstrainedValue)) {
                        score = 0;
                    } else {
                        score = 1;
                    }
                }
            } catch (Exception e) {
                score = 1;
            }
            return score;
        }
    }

    public class DateConstraint implements PenaltyCalculator {

        private static final long SCALE_FACTOR = 100000 * 60 * 60 * 24 * 365;

        private ArrayList<Date> firstDomain;

        private ArrayList<Date> secondDomain;

        public DateConstraint(HashSet<?> hashSet) {
            firstDomain = new ArrayList<Date>();
            secondDomain = new ArrayList<Date>();
            for (Object value : hashSet) {
                firstDomain.add((Date) value);
                secondDomain.add((Date) value);
            }
            Collections.sort(firstDomain);
            Collections.sort(secondDomain);
        }

        public ArrayList<Date> getFirstDomain() {
            return firstDomain;
        }

        public ArrayList<Date> getSecondDomain() {
            return secondDomain;
        }

        public double calculatePenalty(PlaylistNode node) {
            double score = 0;
            Method constrainedMethod = resolveFieldToMethod();
            SongTrack firstConstrainedTrack = node.getMusicTitleAtIndex(firstIndex);
            SongTrack secondConstrainedTrack = node.getMusicTitleAtIndex(secondIndex);
            Date firstConstrainedValue = null;
            Date secondConstrainedValue = null;
            try {
                firstConstrainedValue = (Date) constrainedMethod.invoke(firstConstrainedTrack, (Object[]) null);
                secondConstrainedValue = (Date) constrainedMethod.invoke(secondConstrainedTrack, (Object[]) null);
                firstDomain.remove(secondConstrainedValue);
                secondDomain.remove(firstConstrainedValue);
                if (firstConstrainedValue.equals(secondConstrainedValue)) {
                    score = 0;
                } else {
                    score = Math.abs((firstConstrainedValue.getTime() - secondConstrainedValue.getTime()) / (firstConstrainedValue.getTime() + secondConstrainedValue.getTime())) / SCALE_FACTOR;
                }
            } catch (Exception e) {
                score = 2;
            }
            return score;
        }
    }
}
