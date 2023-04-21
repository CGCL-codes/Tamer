package de.rockon.fuzzy.controller.operators.complex;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import de.rockon.fuzzy.controller.model.FuzzyPoint;
import de.rockon.fuzzy.controller.model.FuzzySet;
import de.rockon.fuzzy.controller.model.helper.Tupel;
import de.rockon.fuzzy.controller.operators.AbstractOperator;
import de.rockon.fuzzy.controller.util.ModelUtil;
import de.rockon.fuzzy.controller.util.factories.IconFactory;
import de.rockon.fuzzy.exceptions.DuplicateXValueException;
import de.rockon.fuzzy.exceptions.ValueOutOfDomainException;

/**
 * Operator welcher mehrere FuzzySet Objekte mit UND verkn�pft und ein neues FuzzySet liefert
 */
public class AndOperator extends AbstractOperator<FuzzySet> {

    @Override
    public FuzzySet execute() {
        if (operands.isEmpty()) {
            return null;
        }
        if (operands.size() == 1) {
            return operands.peek();
        }
        while (operands.size() > 1) {
            List<FuzzySet> fuzzySets = new ArrayList<FuzzySet>();
            fuzzySets.add(operands.poll());
            fuzzySets.add(operands.poll());
            for (FuzzySet set : fuzzySets) {
                ModelUtil.addHelpPoints(set);
            }
            TreeMap<Double, TreeSet<Tupel>> map = ModelUtil.getSortedPointMap(fuzzySets);
            FuzzySet maxSet = map.get(map.firstKey()).last().getValue();
            boolean samePoint = false;
            FuzzySet andSet = new FuzzySet(fuzzySets.get(0).getName() + " AND " + fuzzySets.get(1).getName());
            Double lastX = map.firstKey();
            try {
                for (Double x : map.keySet()) {
                    if (Double.doubleToLongBits(map.get(x).last().getKey()) == Double.doubleToLongBits(map.get(x).first().getKey())) {
                        samePoint = true;
                        andSet.add(new FuzzyPoint(x, map.get(x).last().getKey()));
                    } else {
                        FuzzySet maxEntrySet = map.get(x).last().getValue();
                        if (maxEntrySet != maxSet && !samePoint) {
                            andSet.add(ModelUtil.getIntersection(new FuzzyPoint(lastX, maxSet.getValue(lastX)), new FuzzyPoint(x, maxSet.getValue(x)), new FuzzyPoint(lastX, maxEntrySet.getValue(lastX)), new FuzzyPoint(x, maxEntrySet.getValue(x))));
                        }
                        maxSet = maxEntrySet;
                        lastX = x;
                        andSet.add(new FuzzyPoint(x, map.get(x).first().getKey()));
                        samePoint = false;
                    }
                }
            } catch (ValueOutOfDomainException e) {
                System.err.println(e.getMessage());
            } catch (DuplicateXValueException e) {
                System.err.println(e.getMessage());
            }
            for (FuzzySet set : fuzzySets) {
                ModelUtil.removeHelpPoints(set);
            }
            andSet.setParent(fuzzySets.get(0).getParent());
            operands.offer(andSet);
        }
        return operands.poll();
    }

    @Override
    public String getDescription() {
        return "Performs the And-Operation";
    }

    @Override
    public String getIcon() {
        return IconFactory.ICON_RULE_AND;
    }

    @Override
    public String toString() {
        return "AND";
    }
}
