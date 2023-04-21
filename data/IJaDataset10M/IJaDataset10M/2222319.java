package org.argouml.uml.cognitive.critics;

import javax.swing.Icon;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Critic;
import org.argouml.kernel.Wizard;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.foundation.core.MModelElement;

public class CrMissingStateName extends CrUML {

    public CrMissingStateName() {
        setHeadline("Choose a Name");
        addSupportedDecision(CrUML.decNAMING);
        setKnowledgeTypes(Critic.KT_COMPLETENESS, Critic.KT_SYNTAX);
        addTrigger("name");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
        if (!(dm instanceof MModelElement)) return NO_PROBLEM;
        MModelElement e = (MModelElement) dm;
        String myName = e.getName();
        if (myName == null || myName.equals("") || myName == null || myName.length() == 0) return PROBLEM_FOUND;
        return NO_PROBLEM;
    }

    public Icon getClarifier() {
        return ClClassName.TheInstance;
    }

    public void initWizard(Wizard w) {
        if (w instanceof WizMEName) {
            ToDoItem item = w.getToDoItem();
            MModelElement me = (MModelElement) item.getOffenders().elementAt(0);
            String ins = "Set the name of this state.";
            String sug = "StateName";
            if (me instanceof MStateVertex) {
                MStateVertex sv = (MStateVertex) me;
                int count = 1;
                if (sv.getContainer() != null) count = sv.getContainer().getSubvertices().size();
                sug = "S" + (count + 1);
            }
            ((WizMEName) w).setInstructions(ins);
            ((WizMEName) w).setSuggestion(sug);
        }
    }

    public Class getWizardClass(ToDoItem item) {
        return WizMEName.class;
    }
}
