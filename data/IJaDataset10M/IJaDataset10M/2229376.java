package org.vikamine.kernel.subgroup.selectors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.vikamine.kernel.data.Attribute;
import org.vikamine.kernel.data.DataView;
import org.vikamine.kernel.data.NominalAttribute;
import org.vikamine.kernel.data.NumericAttribute;
import org.vikamine.kernel.data.Value;
import org.vikamine.kernel.data.discretization.DiscretizationMethod;
import org.vikamine.kernel.data.discretization.EqualFreqDiscretizer;

/**
 * {@link SGSelectorGenerator} generates a set of selectors for an attribute
 * according to different strategies, e.g., taking all combinations, all extreme
 * values, or all cutpoints (numeric attribute) into account.
 * 
 * @author lemmerich
 * 
 */
public interface SGSelectorGenerator {

    public Collection<SGSelector> getSelectors(Attribute att, DataView dataview);

    public static SGSelectorGenerator SIMPLE_VALUE_GENERATOR = new SimpleValueSelectorGenerator();

    public static SGSelectorGenerator STANDARD_GENERATOR = new SplitSelectorGenerator(new SGSelectorGenerator.SimpleValueSelectorGenerator(), new SGSelectorGenerator.SimpleNumericSelectorGenerator(new EqualFreqDiscretizer(5)));

    public static class SplitSelectorGenerator implements SGSelectorGenerator {

        SGSelectorGenerator nominalSelectorGenerator;

        SGSelectorGenerator numericSelectorGenerator;

        public SplitSelectorGenerator(SGSelectorGenerator nominalSelectorGenerator, SGSelectorGenerator numericSelectorGenerator) {
            super();
            this.nominalSelectorGenerator = nominalSelectorGenerator;
            this.numericSelectorGenerator = numericSelectorGenerator;
        }

        @Override
        public Collection<SGSelector> getSelectors(Attribute att, DataView dataview) {
            if (att.isNominal()) {
                return nominalSelectorGenerator.getSelectors(att, dataview);
            } else if (att.isNumeric()) {
                return numericSelectorGenerator.getSelectors(att, dataview);
            }
            return Collections.EMPTY_LIST;
        }
    }

    public static class SimpleValueSelectorGenerator implements SGSelectorGenerator {

        public SimpleValueSelectorGenerator() {
            super();
        }

        @Override
        public Collection<SGSelector> getSelectors(Attribute att, DataView dataview) {
            List<SGSelector> result = new ArrayList<SGSelector>();
            if (att.isNominal()) {
                NominalAttribute nomAtt = (NominalAttribute) att;
                Iterator<Value> valIterator = nomAtt.allValuesIterator();
                while (valIterator.hasNext()) {
                    Value value = valIterator.next();
                    DefaultSGSelector sel = new DefaultSGSelector(att, value);
                    result.add(sel);
                }
            }
            return result;
        }
    }

    public static class SimpleValueSelectorGeneratorIgnoreDefaults implements SGSelectorGenerator {

        public SimpleValueSelectorGeneratorIgnoreDefaults() {
            super();
        }

        @Override
        public Collection<SGSelector> getSelectors(Attribute att, DataView dataview) {
            List<SGSelector> result = new ArrayList<SGSelector>();
            if (att.isNominal()) {
                NominalAttribute nomAtt = (NominalAttribute) att;
                Iterator<Value> valIterator = nomAtt.allValuesIterator();
                while (valIterator.hasNext()) {
                    Value value = valIterator.next();
                    if (!value.isDefaultValue()) {
                        DefaultSGSelector sel = new DefaultSGSelector(att, value);
                        result.add(sel);
                    }
                }
            }
            return result;
        }
    }

    public abstract static class AbstractNumericSelectorGenerator implements SGSelectorGenerator {

        List<Double> cutpoints;

        DiscretizationMethod discMethod;

        public AbstractNumericSelectorGenerator(DiscretizationMethod discMethod) {
            this.discMethod = discMethod;
        }

        protected void prepareDiscMethod(Attribute numAtt, DataView dataView) {
            discMethod.setAttribute((NumericAttribute) numAtt);
            discMethod.setPopulation(dataView);
        }
    }

    public static class SimpleNumericSelectorGenerator extends AbstractNumericSelectorGenerator {

        public SimpleNumericSelectorGenerator(DiscretizationMethod discMethod) {
            super(discMethod);
        }

        @Override
        public Collection<SGSelector> getSelectors(Attribute numAtt, DataView dataview) {
            if (!(numAtt instanceof NumericAttribute)) {
                throw new IllegalArgumentException("This SelectorGenerator is only applicable for numeric attributes!");
            }
            prepareDiscMethod(numAtt, dataview);
            cutpoints = discMethod.getCutpoints();
            cutpoints.remove(0);
            cutpoints.add(0, Double.NEGATIVE_INFINITY);
            cutpoints.remove(cutpoints.size() - 1);
            cutpoints.add(Double.POSITIVE_INFINITY);
            List<SGSelector> result = new ArrayList<SGSelector>();
            for (int i = 0; i < cutpoints.size() - 1; i++) {
                double lowerBound = cutpoints.get(i);
                double upperBound = cutpoints.get(i + 1);
                NumericSelector sel = new NumericSelector((NumericAttribute) numAtt, lowerBound, upperBound, true, false);
                if (!result.contains(sel)) {
                    result.add(sel);
                }
            }
            return result;
        }
    }

    public static class AllCombinationsSelectorGenerator extends AbstractNumericSelectorGenerator {

        public AllCombinationsSelectorGenerator(DiscretizationMethod discMethod) {
            super(discMethod);
        }

        @Override
        public Collection<SGSelector> getSelectors(Attribute numAtt, DataView dataview) {
            if (!(numAtt instanceof NumericAttribute)) {
                throw new IllegalArgumentException("This SelectorGenerator is only applicable for numeric attributes!");
            }
            prepareDiscMethod(numAtt, dataview);
            cutpoints = discMethod.getCutpoints();
            cutpoints.remove(0);
            cutpoints.add(0, Double.NEGATIVE_INFINITY);
            cutpoints.remove(cutpoints.size() - 1);
            cutpoints.add(Double.POSITIVE_INFINITY);
            if (!(numAtt instanceof NumericAttribute)) {
                throw new IllegalArgumentException("This SelectorGenerator is only applicable for numeric attributes!");
            }
            Collection<SGSelector> resultSet = new HashSet<SGSelector>();
            for (int i = 0; i < cutpoints.size() - 1; i++) {
                for (int j = i + 1; j < cutpoints.size(); j++) {
                    double lowerBound = cutpoints.get(i);
                    double upperBound = cutpoints.get(j);
                    if ((i != 0) || (j != cutpoints.size() - 1)) {
                        NumericSelector sel = new NumericSelector((NumericAttribute) numAtt, lowerBound, upperBound, true, false);
                        resultSet.add(sel);
                    }
                }
            }
            return resultSet;
        }
    }

    public static class AllExtremeValueBasedSelectorGenerator extends AbstractNumericSelectorGenerator {

        boolean includeSimpleIntervals;

        public AllExtremeValueBasedSelectorGenerator(DiscretizationMethod discMethod, boolean simpleIntervals) {
            super(discMethod);
            this.includeSimpleIntervals = simpleIntervals;
        }

        @Override
        public Collection<SGSelector> getSelectors(Attribute numAtt, DataView dataview) {
            if (!(numAtt instanceof NumericAttribute)) {
                throw new IllegalArgumentException("This SelectorGenerator is only applicable for numeric attributes!");
            }
            prepareDiscMethod(numAtt, dataview);
            cutpoints = discMethod.getCutpoints();
            cutpoints.remove(0);
            cutpoints.add(0, Double.NEGATIVE_INFINITY);
            cutpoints.remove(cutpoints.size() - 1);
            cutpoints.add(Double.POSITIVE_INFINITY);
            List<SGSelector> result = new ArrayList<SGSelector>();
            if (includeSimpleIntervals) {
                for (int i = 0; i < cutpoints.size() - 1; i++) {
                    double lowerBound = cutpoints.get(i);
                    double upperBound = cutpoints.get(i + 1);
                    NumericSelector sel = new NumericSelector((NumericAttribute) numAtt, lowerBound, upperBound, true, false);
                    if (!result.contains(sel)) {
                        result.add(sel);
                    }
                }
            }
            for (int i = 1; i < cutpoints.size() - 1; i++) {
                double lowerBound = cutpoints.get(0);
                double upperBound = cutpoints.get(i);
                NumericSelector sel = new NumericSelector((NumericAttribute) numAtt, lowerBound, upperBound, true, false);
                if (!result.contains(sel)) {
                    result.add(sel);
                }
            }
            for (int i = 1; i < cutpoints.size() - 1; i++) {
                double lowerBound = cutpoints.get(i);
                double upperBound = cutpoints.get(cutpoints.size() - 1);
                NumericSelector sel = new NumericSelector((NumericAttribute) numAtt, lowerBound, upperBound, true, false);
                if (!result.contains(sel)) {
                    result.add(sel);
                }
            }
            return result;
        }
    }
}
