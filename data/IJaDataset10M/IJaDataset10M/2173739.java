package net.sf.signs.views.fsm.model.commands;

import java.util.List;
import net.sf.signs.views.fsm.model.FSMState;
import net.sf.signs.views.fsm.model.Transition;
import org.eclipse.gef.commands.Command;

public class ReconnectTargetCommand extends Command {

    protected FSMState source, target;

    protected Transition transition;

    protected FSMState oldTarget;

    public boolean canExecute() {
        if (transition.source.equals(target)) return false;
        List transitions = source.getOutgoingTransitions();
        for (int i = 0; i < transitions.size(); i++) {
            Transition trans = ((Transition) (transitions.get(i)));
            if (trans.target.equals(target) && !trans.target.equals(oldTarget)) return false;
        }
        return true;
    }

    public void execute() {
        if (target != null) {
            oldTarget.removeInput(transition);
            transition.target = target;
            target.addInput(transition);
        }
    }

    public FSMState getSource() {
        return source;
    }

    public FSMState getTarget() {
        return target;
    }

    public Transition getTransition() {
        return transition;
    }

    public void setSource(FSMState state_) {
        source = state_;
    }

    public void setTarget(FSMState state_) {
        target = state_;
    }

    public void setTransition(Transition trans) {
        transition = trans;
        source = trans.source;
        oldTarget = trans.target;
    }

    public void undo() {
        target.removeInput(transition);
        transition.target = oldTarget;
        oldTarget.addInput(transition);
    }
}
