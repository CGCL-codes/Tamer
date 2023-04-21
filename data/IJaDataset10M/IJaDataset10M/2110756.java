package shellkk.qiq.jdm.association;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.datamining.ComparisonOperator;
import javax.datamining.SortOrder;
import javax.datamining.association.AssociationRule;
import javax.datamining.association.RuleComponentOption;
import javax.datamining.association.RuleProperty;
import javax.datamining.association.RulesFilter;
import shellkk.qiq.jdm.ComparisonCondition;

public class RulesFilterImpl implements RulesFilter, Comparator<AssociationRule> {

    protected RuleComponentOption systemDefaultOption = RuleComponentOption.antecedentOrConsequent;

    protected SortOrder systemDefaultOrder = SortOrder.descending;

    protected List antecedentInclude = new ArrayList();

    protected List antecedentExclude = new ArrayList();

    protected List consequentInclude = new ArrayList();

    protected List consequentExclude = new ArrayList();

    protected List anyInclude = new ArrayList();

    protected int maxNumberOfRules;

    protected Map<RuleProperty, ComparisonCondition> thresholdConditions = new HashMap();

    protected Map<RuleProperty, ComparisonCondition> maxConditions = new HashMap();

    protected Map<RuleProperty, ComparisonCondition> minConditions = new HashMap();

    protected SortOrder[] orders;

    protected RuleProperty[] orderBys;

    public Object[] getItems(RuleComponentOption opt, boolean include) {
        if (RuleComponentOption.systemDefault.equals(opt)) {
            opt = systemDefaultOption;
        }
        if (RuleComponentOption.antecedent.equals(opt)) {
            return include ? antecedentInclude.toArray() : antecedentExclude.toArray();
        } else if (RuleComponentOption.consequent.equals(opt)) {
            return include ? consequentInclude.toArray() : consequentExclude.toArray();
        } else if (RuleComponentOption.antecedentOrConsequent.equals(opt)) {
            if (include) {
                return anyInclude.toArray();
            } else {
                ArrayList all = new ArrayList();
                all.addAll(antecedentExclude);
                all.addAll(consequentExclude);
                return all.toArray();
            }
        } else {
            return new Object[0];
        }
    }

    public int getMaxNumberOfRules() {
        return maxNumberOfRules;
    }

    public Double getMaxValue(RuleProperty type) {
        ComparisonCondition cond = maxConditions.get(type);
        return cond == null ? null : ((Number) cond.getValue()).doubleValue();
    }

    public Double getMinValue(RuleProperty type) {
        ComparisonCondition cond = minConditions.get(type);
        return cond == null ? null : ((Number) cond.getValue()).doubleValue();
    }

    protected int getSortIndex(RuleProperty orderBy) {
        if (orderBys == null) {
            return -1;
        }
        for (int i = 0; i < orderBys.length; i++) {
            if (orderBys[i].equals(orderBy)) {
                return i;
            }
        }
        return -1;
    }

    public SortOrder getOrderingCondition(RuleProperty orderBy) {
        int index = getSortIndex(orderBy);
        return index >= 0 ? orders[index] : null;
    }

    public RuleProperty[] getOrderingConditions() {
        return orderBys;
    }

    public ComparisonOperator getThresholdOperator(RuleProperty property) {
        ComparisonCondition cond = thresholdConditions.get(property);
        return cond == null ? null : cond.getOperator();
    }

    public Double getThresholdValue(RuleProperty property) {
        ComparisonCondition cond = thresholdConditions.get(property);
        return cond == null ? null : ((Number) cond.getValue()).doubleValue();
    }

    public void setItems(Object[] items, RuleComponentOption opt, boolean include) {
        if (RuleComponentOption.systemDefault.equals(opt)) {
            opt = systemDefaultOption;
        }
        if (RuleComponentOption.antecedent.equals(opt)) {
            if (include) {
                for (Object item : items) {
                    antecedentInclude.add(item);
                }
            } else {
                for (Object item : items) {
                    antecedentExclude.add(item);
                }
            }
        } else if (RuleComponentOption.consequent.equals(opt)) {
            if (include) {
                for (Object item : items) {
                    consequentInclude.add(item);
                }
            } else {
                for (Object item : items) {
                    consequentExclude.add(item);
                }
            }
        } else if (RuleComponentOption.antecedentOrConsequent.equals(opt)) {
            if (include) {
                for (Object item : items) {
                    anyInclude.add(item);
                }
            } else {
                for (Object item : items) {
                    antecedentExclude.add(item);
                    consequentExclude.add(item);
                }
            }
        }
    }

    public void setMaxNumberOfRules(int maxRules) {
        maxNumberOfRules = maxRules;
    }

    public void setOrderingCondition(RuleProperty[] orderByArray, SortOrder[] sortOrderArray) {
        orderBys = orderByArray;
        orders = sortOrderArray;
    }

    public void setRange(RuleProperty type, double minValue, double maxValue) {
        ComparisonCondition max = new ComparisonCondition(ComparisonOperator.lessOrEqual, maxValue);
        ComparisonCondition min = new ComparisonCondition(ComparisonOperator.greaterOrEqual, minValue);
        maxConditions.put(type, max);
        minConditions.put(type, min);
        thresholdConditions.remove(type);
    }

    public void setThreshold(RuleProperty property, ComparisonOperator compOp, double thresholdValue) {
        ComparisonCondition cond = new ComparisonCondition(compOp, thresholdValue);
        thresholdConditions.put(property, cond);
        maxConditions.remove(property);
        minConditions.remove(property);
    }

    protected double getRuleValue(AssociationRule rule, RuleProperty prop) {
        if (RuleProperty.confidence.equals(prop)) {
            return rule.getConfidence();
        } else if (RuleProperty.length.equals(prop)) {
            return rule.getLength();
        } else if (RuleProperty.lift.equals(prop)) {
            return rule.getLift();
        } else if (RuleProperty.support.equals(prop)) {
            return rule.getSupport();
        } else {
            return 0;
        }
    }

    public int compare(AssociationRule r1, AssociationRule r2) {
        if (orderBys != null) {
            for (int i = 0; i < orderBys.length; i++) {
                RuleProperty prop = orderBys[i];
                double v1 = getRuleValue(r1, prop);
                double v2 = getRuleValue(r2, prop);
                double v = v1 - v2;
                if (v != 0) {
                    SortOrder order = orders[i];
                    if (SortOrder.systemDefault.equals(order)) {
                        order = systemDefaultOrder;
                    }
                    if (SortOrder.ascending.equals(order)) {
                        return v > 0 ? 1 : -1;
                    } else if (SortOrder.descending.equals(order)) {
                        return v > 0 ? -1 : 1;
                    }
                }
            }
            return 0;
        } else {
            return 0;
        }
    }

    protected boolean evaluate(AssociationRule rule) {
        ArrayList ante = new ArrayList();
        for (Object item : rule.getAntecedent().getItems()) {
            ante.add(item);
        }
        ArrayList cons = new ArrayList();
        for (Object item : rule.getConsequent().getItems()) {
            cons.add(item);
        }
        if (!ante.containsAll(antecedentInclude)) {
            return false;
        }
        for (Object item : antecedentExclude) {
            if (ante.contains(item)) {
                return false;
            }
        }
        if (!cons.containsAll(consequentInclude)) {
            return false;
        }
        for (Object item : consequentExclude) {
            if (cons.contains(item)) {
                return false;
            }
        }
        for (Object item : anyInclude) {
            if (!ante.contains(item) && !cons.contains(item)) {
                return false;
            }
        }
        for (RuleProperty prop : thresholdConditions.keySet()) {
            ComparisonCondition cond = thresholdConditions.get(prop);
            double v = getRuleValue(rule, prop);
            if (!cond.evaluate(v)) {
                return false;
            }
        }
        for (RuleProperty prop : maxConditions.keySet()) {
            ComparisonCondition cond = maxConditions.get(prop);
            double v = getRuleValue(rule, prop);
            if (!cond.evaluate(v)) {
                return false;
            }
        }
        for (RuleProperty prop : minConditions.keySet()) {
            ComparisonCondition cond = minConditions.get(prop);
            double v = getRuleValue(rule, prop);
            if (!cond.evaluate(v)) {
                return false;
            }
        }
        return true;
    }

    public List<AssociationRule> filter(Collection<AssociationRule> rules) {
        ArrayList<AssociationRule> all = new ArrayList();
        for (AssociationRule rule : rules) {
            if (evaluate(rule)) {
                all.add(rule);
            }
        }
        Collections.sort(all, this);
        int size = Math.min(all.size(), maxNumberOfRules > 0 ? maxNumberOfRules : all.size());
        return all.subList(0, size);
    }
}
