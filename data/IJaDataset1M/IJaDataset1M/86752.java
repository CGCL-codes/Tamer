package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;

/**
 * Rule for SubmachineState to StateMachine. This enables the user to easily
 * browse the submachine states.
 *
 * @author MarkusK
 */
public class GoSubmachineStateToStateMachine extends AbstractPerspectiveRule {

    public String getRuleName() {
        return Translator.localize("misc.submachine-state.state-machine");
    }

    public Collection getChildren(Object parent) {
        if (Model.getFacade().isASubmachineState(parent)) {
            List list = new ArrayList();
            list.add(Model.getFacade().getSubmachine(parent));
            return list;
        }
        return Collections.EMPTY_SET;
    }

    public Set getDependencies(Object parent) {
        if (Model.getFacade().isASubmachineState(parent)) {
            Set set = new HashSet();
            set.add(parent);
            return set;
        }
        return Collections.EMPTY_SET;
    }
}
