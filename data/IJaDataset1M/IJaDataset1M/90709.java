package br.ufmg.lcc.eid.model.unifier;

import java.util.ArrayList;
import java.util.List;
import br.ufmg.lcc.eid.commons.EidClassHelper;
import br.ufmg.lcc.eid.commons.EidException;
import br.ufmg.lcc.eid.dto.EidClass;

public class MultipleInstanceDiscardInactivesUnifier implements IClassUnifier {

    private String propName = "situacao";

    private String propValDiscard = "inativo";

    /**
	 * Unifies a list of classes returning the final list to be considered
	 * 
	 * @param classes
	 *            List of classes to be unified
	 * @return A list of class instances
	 */
    public List<EidClass> unify(List<EidClass> classes) throws EidException {
        ArrayList<EidClass> newClasses = new ArrayList<EidClass>();
        for (EidClass eidClass : classes) {
            if (!eidClass.getEidObject().getUnifiedDomain()) {
                String propVal = (String) eidClass.getPropertyValue(propName);
                if (propVal == null || !propVal.equalsIgnoreCase(propValDiscard)) {
                    newClasses.add(EidClassHelper.cloneInstance(eidClass));
                }
            }
        }
        return newClasses;
    }
}
