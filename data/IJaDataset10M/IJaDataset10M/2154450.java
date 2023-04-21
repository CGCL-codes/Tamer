package org.unitmetrics.java.ui.views;

import org.eclipse.jdt.core.IJavaElement;
import org.unitmetrics.util.UnsafeCast;

/** 
 * Utility class to provide support of JDT components.
 * @author Martin Kersten 
 */
public class JdtUtil {

    public static <T> T getAncestor(IJavaElement element, Class<T> ancestorType) {
        IJavaElement parent = element.getParent();
        while (parent != null) {
            if (ancestorType.isInstance(parent)) return UnsafeCast.cast(parent, ancestorType);
            parent = parent.getParent();
        }
        return null;
    }

    public static IJavaElement getNearestAncestor(IJavaElement element, Class[] ancestorTypes) {
        IJavaElement parent = element.getParent();
        while (parent != null) {
            for (Class ancestorType : ancestorTypes) if (ancestorType.isInstance(parent)) return UnsafeCast.cast(parent, IJavaElement.class);
            parent = parent.getParent();
        }
        return null;
    }

    public static boolean isAncestor(IJavaElement element, IJavaElement parent) {
        return parent.equals(getAncestor(element, parent.getClass()));
    }
}
