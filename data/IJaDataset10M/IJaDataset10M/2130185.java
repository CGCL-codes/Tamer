package org.biomage.tools.helpers;

import org.biomage.Description.DatabaseEntry;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author  kjellp
 */
public class MGEDOntologyPropertyEntry extends MGEDOntologyEntry {

    private String mgedOntologyProperty = null;

    private String propType = null;

    /** Creates a new instance of MGEDOntologyEntry */
    public MGEDOntologyPropertyEntry(String propertyName, String propertyType, org.biomage.tools.helpers.OntologyHelper oh) {
        mgedOntologyProperty = propertyName;
        propType = propertyType;
        if (oh.classExists(propertyType)) {
            isAssignable = false;
            this.setCategory(mgedOntologyProperty);
            this.setValue(mgedOntologyProperty);
            this.setOntologyReference(oh.getOntologyReference(mgedOntologyProperty));
            this.addToAssociations(new MGEDOntologyClassEntry(propertyType, oh));
        } else if (propType.equals("enum")) {
            isAssignable = true;
            assignableValues = new ArrayList();
            this.setCategory(mgedOntologyProperty);
            assignableValues = oh.getEnumValues(mgedOntologyProperty);
            this.setOntologyReference(oh.getOntologyReference("placeholder"));
        } else if (propType.equals("any")) {
            isAssignable = false;
            this.setCategory(mgedOntologyProperty);
            this.setValue(mgedOntologyProperty);
            this.setOntologyReference(oh.getOntologyReference(mgedOntologyProperty));
            this.addToAssociations(new MGEDOntologyPropertyEntry(mgedOntologyProperty, "thingFiller", oh));
        } else if (propType.equals("thingFiller") || propType.equals("string")) {
            isAssignable = true;
            this.setCategory(mgedOntologyProperty);
        } else {
            StringOutputHelpers.writeOutput("Unknown propertyType : " + propertyType, 1);
        }
    }

    public void assignValue(Object val) {
        if (isAssignable && !isAssigned) {
            if (propType.equals("enum") && assignableValues.contains(val)) {
                this.setValue((String) val);
                DatabaseEntry ontRef = this.getOntologyReference();
                ontRef.setAccession("#" + (String) val);
                ontRef.setURI(ontRef.getURI().replaceFirst("placeholder", (String) val));
            } else if (propType.equals("thingFiller")) {
                this.setValue((String) val);
            }
            isAssigned = true;
        }
    }

    public boolean legalValue(Object val) {
        if (isAssignable) {
            if (propType.equals("enum") && assignableValues.contains(val)) {
                return true;
            } else if (propType.equals("thingFiller")) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    public void prune() {
        java.util.Iterator it = this.getAssociations().iterator();
        while (it.hasNext()) {
            ((MGEDOntologyEntry) it.next()).prune();
        }
    }
}
