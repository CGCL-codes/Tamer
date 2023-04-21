package net.taylor.mda.properties.associations;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;

/**
 * The section for the isNavigable property of the second end Property of an
 * Association Object.
 */
public class SecondEndIsNavigablePropertySection extends AbstractIsNavigablePropertySection {

    protected Property getProperty(Association association) {
        if (association.getMemberEnds() != null && association.getMemberEnds().size() > 1) {
            return (Property) association.getMemberEnds().get(1);
        }
        return null;
    }

    protected String getLabelText() {
        return "IsNavigable";
    }
}
