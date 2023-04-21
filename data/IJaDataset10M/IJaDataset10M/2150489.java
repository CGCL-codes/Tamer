package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;

/**
 * Rule for Transition->Effect.
 *
 * @author MarkusK
 */
public class GoTransitiontoEffect extends AbstractPerspectiveRule {

    public String getRuleName() {
        return Translator.localize("misc.transition.effect");
    }

    public Collection getChildren(Object parent) {
        if (Model.getFacade().isATransition(parent)) {
            Object effect = Model.getFacade().getEffect(parent);
            if (effect != null) {
                Collection col = new ArrayList();
                col.add(effect);
                return col;
            }
        }
        return Collections.EMPTY_SET;
    }

    public Set getDependencies(Object parent) {
        if (Model.getFacade().isATransition(parent)) {
            Set set = new HashSet();
            set.add(parent);
            return set;
        }
        return Collections.EMPTY_SET;
    }
}
