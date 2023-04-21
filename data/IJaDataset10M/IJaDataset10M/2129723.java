package logiklabor.data.formulas;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import logiklabor.data.GateType;

/**
 * This class represents a formula being a simple sub-formula (see {@link LogicFormula}).
 * 
 * @author Sebastian Mehrbreier
 * 
 */
public class LogicSubFormula extends LogicFormula {

    private final List<LogicFormula> subFormulas;

    private int depth = 0;

    private GateType gateType;

    public LogicSubFormula(List<LogicFormula> subformulas, GateType gateType) {
        this.gateType = gateType;
        switch(gateType) {
            case NOT:
                if (subformulas.size() != 1) throw new IllegalArgumentException("Only one subformula allowed for 'NOT'");
                break;
            case AND:
            case OR:
                if (subformulas.size() < 2) throw new IllegalArgumentException("Each gates needs at least two subformulas");
                break;
        }
        this.subFormulas = subformulas;
        for (LogicFormula formula : subformulas) {
            depth = Math.max(depth, formula.getDepth());
        }
        depth = depth + 1;
    }

    @Override
    public String toString() {
        String operatorString = null;
        switch(gateType) {
            case NOT:
                if (subFormulas.get(0).bracketsNeededForGateType(gateType)) {
                    return "!(" + subFormulas.get(0).toString() + ")";
                } else {
                    return "!" + subFormulas.get(0).toString();
                }
            case AND:
                operatorString = "&";
                break;
            case OR:
                operatorString = "|";
                break;
            default:
                throw new IllegalStateException("The formula has an illegal operator");
        }
        Iterator<LogicFormula> it = subFormulas.iterator();
        LogicFormula next = it.next();
        String formulaString = "";
        if (next.bracketsNeededForGateType(gateType)) {
            formulaString = "(" + next.toString() + ")";
        } else {
            formulaString = next.toString();
        }
        while (it.hasNext()) {
            next = it.next();
            if (next.bracketsNeededForGateType(gateType)) {
                formulaString = formulaString + operatorString + "(" + next.toString() + ")";
            } else {
                formulaString = formulaString + operatorString + next.toString();
            }
        }
        return formulaString;
    }

    @Override
    public boolean bracketsNeededForGateType(GateType gateType) {
        switch(gateType) {
            case NOT:
                if (this.gateType == GateType.NOT) return false; else return true;
            case AND:
                if (this.gateType == GateType.NOT) return false; else return true;
            case OR:
                if (this.gateType == GateType.NOT) return false; else return true;
        }
        return false;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    public final List<LogicFormula> getSubFormulas() {
        return subFormulas;
    }

    public GateType getGateType() {
        return gateType;
    }

    public void exchangeSubFormula(LogicFormula oldFormula, LogicFormula newFormula) {
        for (int index = 0; index < subFormulas.size(); index++) {
            if (subFormulas.get(index) == oldFormula) {
                exchangeSubFormula(index, newFormula);
                return;
            }
        }
        throw new IllegalArgumentException("The formula " + oldFormula.toString() + " is not a subFormula of " + toString());
    }

    private void exchangeSubFormula(int index, LogicFormula newFormula) {
        subFormulas.set(index, newFormula);
    }

    @Override
    public Set<LogicVariable> getUsedVariables() {
        Set<LogicVariable> usedVariables = new HashSet<LogicVariable>();
        for (LogicFormula formula : getSubFormulas()) {
            usedVariables.addAll(formula.getUsedVariables());
        }
        return usedVariables;
    }
}
