package org.nakedobjects.webapp.view.field;

import java.util.HashMap;
import java.util.Map;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;

public class LinkedFieldsBlock extends InclusionList {

    private Map<String, LinkedObject> linkedFields = new HashMap<String, LinkedObject>();

    public void link(String field, String variable, String scope, String forwardView) {
        linkedFields.put(field, new LinkedObject(variable, scope, forwardView));
    }

    public LinkedObject[] linkedFields(NakedObjectAssociation[] fields) {
        LinkedObject[] includedFields = new LinkedObject[fields.length];
        for (int i = 0; i < fields.length; i++) {
            String id2 = fields[i].getId();
            if (fields[i].isOneToOneAssociation() && linkedFields.containsKey(id2)) {
                includedFields[i] = linkedFields.get(id2);
            }
        }
        return includedFields;
    }
}
