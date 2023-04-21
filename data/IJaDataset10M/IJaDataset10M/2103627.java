package jsaf.astelements;

import java.util.ArrayList;
import java.util.List;
import jsaf.visitor.SAFElement;
import jsaf.visitor.SAFElementVisitor;

public class Behaviour implements SAFElement {

    private ConditionChoices conditionChoices;

    private List<String> moveChoices;

    private List<String> attackChoices;

    public Behaviour() {
        moveChoices = new ArrayList<String>();
        attackChoices = new ArrayList<String>();
    }

    public void addMove(String moveChoice) {
        this.moveChoices.add(moveChoice);
    }

    public void addAttack(String attackChoice) {
        this.attackChoices.add(attackChoice);
    }

    public ConditionChoices getConditionChoices() {
        return conditionChoices;
    }

    public void setConditionChoices(ConditionChoices conditionChoices) {
        this.conditionChoices = conditionChoices;
    }

    public List<String> getMoveChoices() {
        return moveChoices;
    }

    public void setMoveChoices(List<String> moveChoices) {
        this.moveChoices = moveChoices;
    }

    public List<String> getAttackChoices() {
        return attackChoices;
    }

    public void setAttackChoices(List<String> attackChoices) {
        this.attackChoices = attackChoices;
    }

    @Override
    public void accept(SAFElementVisitor visitor) {
        for (ConditionGroup conditionGroup : conditionChoices.getConditionGroups()) {
            conditionGroup.accept(visitor);
        }
        visitor.visit(this);
    }
}
