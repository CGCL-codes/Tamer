package net.taylor.uml2.activitydiagram.util;

import java.util.Iterator;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;

public class ModelUtil {

    public static final String TASK = "Task";

    public static final String TIMER = "Timer";

    public static final String INVOKE = "Invoke";

    public static void applyStereotype(Element element, String name) {
        for (Iterator applicableStereotypes = element.getApplicableStereotypes().iterator(); applicableStereotypes.hasNext(); ) {
            Stereotype stereotype = (Stereotype) applicableStereotypes.next();
            if (stereotype.getName().equals(name)) {
                element.applyStereotype(stereotype);
            }
        }
    }

    public static boolean isStereotypeApplied(Element element, String name) {
        for (Iterator applicableStereotypes = element.getAppliedStereotypes().iterator(); applicableStereotypes.hasNext(); ) {
            Stereotype stereotype = (Stereotype) applicableStereotypes.next();
            if (stereotype.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
