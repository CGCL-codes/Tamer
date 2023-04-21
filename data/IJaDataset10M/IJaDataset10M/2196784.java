package org.oboedit.verify.impl;

import java.util.*;
import org.obo.datamodel.*;
import org.obo.util.TermUtil;
import org.oboedit.controller.SessionManager;
import org.oboedit.controller.VerificationManager;
import org.oboedit.verify.*;
import org.apache.log4j.*;

public class CycleCheck extends AbstractCheck implements OntologyCheck {

    protected static final Logger logger = Logger.getLogger(CycleCheck.class);

    @Override
    protected void initConfiguration() {
        configuration.setCondition((byte) (VerificationManager.SAVE | VerificationManager.REASONER_ACTIVATED | VerificationManager.MANUAL));
    }

    public Collection<CheckWarning> check(OBOSession history, IdentifiedObject currentObject, byte condition, boolean checkObsoletes) {
        Collection<OBOProperty> properties = new HashSet<OBOProperty>();
        Iterator<OBOProperty> it = TermUtil.getRelationshipTypes(history).iterator();
        while (it.hasNext()) {
            OBOProperty property = it.next();
            if (property.isTransitive() && !property.isCyclic()) properties.add(property);
        }
        List<CheckWarning> out = new LinkedList<CheckWarning>();
        if (currentObject != null) {
            if (currentObject instanceof LinkedObject) check((LinkedObject) currentObject, properties, out);
        } else {
            Iterator<IdentifiedObject> ith = history.getObjects().iterator();
            for (int i = 0; ith.hasNext(); i++) {
                Object o = ith.next();
                int percentage = 100 * i / history.getObjects().size();
                setProgressValue(percentage);
                setProgressString("checking object " + (i + 1) + " of " + history.getObjects().size());
                if (o instanceof LinkedObject) {
                    LinkedObject lo = (LinkedObject) o;
                    check(lo, properties, out);
                    if (isCancelled() || out.size() > VerificationManager.MAX_WARNINGS) return out;
                }
            }
        }
        return out;
    }

    protected void check(LinkedObject object, Collection<OBOProperty> properties, List<CheckWarning> warnings) {
        Iterator<OBOProperty> it = properties.iterator();
        while (it.hasNext()) {
            OBOProperty property = it.next();
            if (TermUtil.isCycle(SessionManager.getManager().getCurrentFullLinkDatabase(), property, object)) {
                CheckWarning warning = new CheckWarning(object.getName() + " (" + object.getID() + ") " + "is part of a cycle over the property " + property + ". ", true, this, object);
                warnings.add(warning);
                if (warnings.size() > VerificationManager.MAX_WARNINGS) return;
            }
        }
    }

    @Override
    public String getDescription() {
        return "Cycle Check";
    }

    public String getID() {
        return "CYCLE_CHECK";
    }
}
