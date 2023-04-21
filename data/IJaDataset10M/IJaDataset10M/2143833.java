package jbprocess.bpel;

import java.util.List;
import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.BPELFactory;
import org.eclipse.bpel.model.Catch;
import org.eclipse.bpel.model.CatchAll;
import org.eclipse.bpel.model.FaultHandler;
import org.eclipse.bpel.model.Scope;
import org.eclipse.bpel.model.Sequence;
import org.eclipse.bpel.model.Variable;
import org.eclipse.bpel.model.Variables;

public class BpelScopeImpl implements BpelScope {

    Scope scope;

    Sequence sequence;

    Activity activity;

    FaultHandler faultHandler;

    public BpelScopeImpl() {
        scope = BPELFactory.eINSTANCE.createScope();
        scope.setVariables(BPELFactory.eINSTANCE.createVariables());
        sequence = BPELFactory.eINSTANCE.createSequence();
        faultHandler = BPELFactory.eINSTANCE.createFaultHandler();
    }

    @SuppressWarnings("unchecked")
    public void addActivity(Activity activity) {
        if (this.activity == null && this.sequence.getActivities().size() == 0) {
            this.activity = activity;
            scope.setActivity(this.activity);
        } else if (this.activity != null && this.sequence.getActivities().size() == 0) {
            sequence.getActivities().add(this.activity);
            sequence.getActivities().add(activity);
            scope.setActivity(sequence);
        } else {
            sequence.getActivities().add(activity);
        }
    }

    @SuppressWarnings("unchecked")
    public void addVariable(Variable variable) {
        scope.getVariables().getChildren().add(variable);
    }

    @SuppressWarnings("unchecked")
    public Variable getVariable(String name) {
        List<Variable> vars = scope.getVariables().getChildren();
        for (Variable v : vars) {
            if (v.getName().equals(name)) return v;
        }
        return null;
    }

    public Activity getActivity() {
        if (scope.getVariables().getChildren().size() > 0 || (scope.getFaultHandlers() != null && (scope.getFaultHandlers().getCatch().size() > 0 || scope.getFaultHandlers().getCatchAll() != null))) return scope; else return scope.getActivity();
    }

    public Variables getVariables() {
        return scope.getVariables();
    }

    @SuppressWarnings("unchecked")
    public void addCatch(Catch cat) {
        if (scope.getFaultHandlers() == null) scope.setFaultHandlers(faultHandler);
        scope.getFaultHandlers().getCatch().add(cat);
    }

    public void setCatchAll(CatchAll call) {
        if (scope.getFaultHandlers() == null) scope.setFaultHandlers(faultHandler);
        scope.getFaultHandlers().setCatchAll(call);
    }
}
