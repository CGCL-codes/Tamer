package org.nakedobjects.nof.core.persist;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.spec.NakedObjectSpecification;
import org.nakedobjects.nof.core.util.ToString;

public class TitleCriteria extends AbstractInstancesCriteria {

    private final String requiredTitle;

    public TitleCriteria(final NakedObjectSpecification specification, final String title, final boolean includeSubclasses) {
        super(specification, includeSubclasses);
        this.requiredTitle = title == null ? "" : title.toLowerCase();
    }

    public String getRequiredTitle() {
        return requiredTitle;
    }

    public boolean matches(final NakedObject object) {
        String titleString = object.titleString();
        return matches(titleString);
    }

    public boolean matches(String titleString) {
        String objectTitle = titleString.toLowerCase();
        return objectTitle.indexOf(requiredTitle) >= 0;
    }

    public String toString() {
        ToString str = ToString.createAnonymous(this);
        str.append("spec", getSpecification().getShortName());
        str.append("subclasses", includeSubclasses());
        str.append("title", requiredTitle);
        return str.toString();
    }
}
