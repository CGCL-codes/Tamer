package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;

/**
 * Go rule to navigate from a classifier to the behavioral
 * features owned by that classifier.  <p>
 * Classifier->BehavioralFeature
 *
 * @since Jul 13, 2004
 * @author jaap.branderhorst@xs4all.nl
 */
public class GoClassifierToBehavioralFeature extends AbstractPerspectiveRule {

    public String getRuleName() {
        return Translator.localize("misc.classifier.behavioralfeature");
    }

    public Collection getChildren(Object parent) {
        if (Model.getFacade().isAClassifier(parent)) {
            return Model.getCoreHelper().getBehavioralFeatures(parent);
        }
        return Collections.EMPTY_SET;
    }

    public Set getDependencies(Object parent) {
        if (Model.getFacade().isAClassifier(parent)) {
            Set set = new HashSet();
            set.add(parent);
            return set;
        }
        return Collections.EMPTY_SET;
    }
}
