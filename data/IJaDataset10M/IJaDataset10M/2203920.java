package piramide.interaction.reasoner.creator.membershipgenerator.regionaccumulator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

abstract class AbstractRegion {

    protected final Map<Number, Double> values2trends;

    protected final List<Number> orderedKeys;

    protected final double sumOfValues;

    AbstractRegion(Map<Number, Double> values2trends) {
        this.values2trends = values2trends;
        this.orderedKeys = Arrays.asList(values2trends.keySet().toArray(new Number[] {}));
        Collections.sort(this.orderedKeys, new Comparator<Number>() {

            @Override
            public int compare(Number number1, Number number2) {
                return new Double(number1.doubleValue()).compareTo(Double.valueOf(number2.doubleValue()));
            }
        });
        this.sumOfValues = calculateSumOfValues();
    }

    Map<Number, Double> getResults2trends() {
        return this.values2trends;
    }

    private double calculateSumOfValues() {
        double total = 0.0;
        for (double value : this.values2trends.values()) {
            total += value;
        }
        return total;
    }
}
