package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import org.argouml.cognitive.Designer;
import ru.novosoft.uml.model_management.MModel;

/** A critic to detect when a class can never have instances (of
 *  itself of any subclasses). */
public class CrEmptyPackage extends CrUML {

    public CrEmptyPackage() {
        setHeadline("Add Elements to Package <ocl>self</ocl>");
        addSupportedDecision(CrUML.decMODULARITY);
        addTrigger("ownedElement");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
        if (!(dm instanceof MModel)) return NO_PROBLEM;
        MModel mod = (MModel) dm;
        Collection elms = mod.getOwnedElements();
        if (elms == null || elms.size() == 0) return PROBLEM_FOUND;
        return NO_PROBLEM;
    }
}
