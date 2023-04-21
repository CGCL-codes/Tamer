package fitlibrary.definedAction;

import java.util.List;
import fitlibrary.traverse.workflow.caller.ValidCall;

public class DefinedAction extends DefinedMultiAction {

    private int formalsCount;

    public DefinedAction(String name, int formalsCount) {
        super(name);
        this.formalsCount = formalsCount;
    }

    public void findCall(String textCall, List<ValidCall> results) {
        ValidCall.parseDefinedAction(textCall, name, results);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DefinedAction)) return false;
        DefinedAction other = (DefinedAction) obj;
        return formalsCount == other.formalsCount && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + formalsCount;
    }

    @Override
    public String toString() {
        return "DefinedAction[" + name + "/" + formalsCount + "]";
    }
}
