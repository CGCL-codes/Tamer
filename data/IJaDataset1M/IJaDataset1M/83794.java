package net.sourceforge.jdefprog.annocheck.elements;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public class ElementUtils {

    public static boolean isStatic(Element e) {
        for (Modifier m : e.getModifiers()) {
            if (m.equals(Modifier.STATIC)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAbstract(Element e) {
        for (Modifier m : e.getModifiers()) {
            if (m.equals(Modifier.ABSTRACT)) {
                return true;
            }
        }
        return false;
    }
}
