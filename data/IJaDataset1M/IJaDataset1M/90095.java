package ast.checker;

import ast.Visitor;
import ast.action.Choose;
import ast.action.SimpleAction;
import ast.condition.And;
import ast.condition.Leaf;
import ast.condition.Or;
import ast.fighter.Behavior;
import ast.fighter.FighterProp;
import ast.fighter.Strength;
import ast.object.Fighter;

public abstract class DelegateVisitor implements Visitor {

    @Override
    public void visit(And and) {
        and.getLhs().accept(this);
        and.getRhs().accept(this);
    }

    @Override
    public void visit(Leaf leaf) {
    }

    @Override
    public void visit(Or or) {
        or.getLhs().accept(this);
        or.getRhs().accept(this);
    }

    @Override
    public void visit(SimpleAction action) {
    }

    @Override
    public void visit(Choose choose) {
        for (SimpleAction action : choose.getChoices()) {
            action.accept(this);
        }
    }

    @Override
    public void visit(Strength strength) {
    }

    @Override
    public void visit(Behavior behavior) {
        behavior.getCondition().accept(this);
        behavior.getFightAction().accept(this);
        behavior.getMoveAction().accept(this);
    }

    @Override
    public void visit(Fighter fighter) {
        for (FighterProp fighterProp : fighter.getFighterProps()) {
            fighterProp.accept(this);
        }
    }
}
