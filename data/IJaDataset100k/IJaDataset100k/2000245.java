package org.waveprotocol.wave.client.common.webdriver;

import com.google.common.base.Preconditions;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.UIObject;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Helper methods for adding debugclasses to the DOM Elements.
 *
 */
public class DebugClassHelper {

    /** The name of the debug-class attribute. */
    private static final String DEBUG_CLASS_ATTRIBUTE = "dc";

    /** Whether debug class should be set (e.g. for testing purposes). */
    private static boolean debugClassEnabled = false;

    /** Disallow construction. */
    private DebugClassHelper() {
    }

    /**
   * Adds a debug class to a UIObject.
   *
   * @param uiObject the object to modify
   * @param debugClass to be added
   */
    public static final void addDebugClass(UIObject uiObject, String debugClass) {
        Preconditions.checkNotNull(uiObject, "uiObject cannot be null");
        addDebugClass(uiObject.getElement(), debugClass);
    }

    /**
   * Adds a debug class to an Element.
   *
   * @param elem the object to modify
   * @param debugClass to be added
   */
    public static final void addDebugClass(Element elem, String debugClass) {
        Preconditions.checkNotNull(elem, "addDebugClass: Element must not be null");
        if (debugClassEnabled) {
            Set<String> debugClasses = getDebugClasses(elem);
            if (debugClasses.add(debugClass)) {
                elem.setAttribute(DEBUG_CLASS_ATTRIBUTE, joinDebugClasses(debugClasses));
            }
        }
    }

    /**
   * Replaces a debug class name from an Element with another one.
   *
   * @param elem the object to modify
   * @param oldClassName previous class name to be removed
   * @param newClassName new class name to be added
   */
    public static final void replaceDebugClass(Element elem, String oldClassName, String newClassName) {
        Preconditions.checkNotNull(elem, "replaceDebugClass: Element must not be null");
        if (debugClassEnabled) {
            Set<String> debugClasses = getDebugClasses(elem);
            boolean removed = debugClasses.remove(oldClassName);
            if (debugClasses.add(newClassName) || removed) {
                elem.setAttribute(DEBUG_CLASS_ATTRIBUTE, joinDebugClasses(debugClasses));
            }
        }
    }

    /**
   * Removes a debug class from a UIObject.
   *
   * @param uiObject the object to modify
   * @param debugClass to be removed
   */
    public static final void removeDebugClass(UIObject uiObject, String debugClass) {
        Preconditions.checkNotNull(uiObject, "uiObject cannot be null");
        removeDebugClass(uiObject.getElement(), debugClass);
    }

    /**
   * Remove a debug class from an Element.
   *
   * @param elem the object to modify
   * @param debugClass to be removed
   */
    public static final void removeDebugClass(Element elem, String debugClass) {
        if (debugClassEnabled && null != elem) {
            Set<String> debugClasses = getDebugClasses(elem);
            if (debugClasses.remove(debugClass)) {
                elem.setAttribute(DEBUG_CLASS_ATTRIBUTE, joinDebugClasses(debugClasses));
            }
        }
    }

    /**
   * Removes all debug classes from a UIObject.
   * @param uiObject The UIObject to remove debug classes from.
   */
    public static void clearDebugClasses(UIObject uiObject) {
        clearDebugClasses(uiObject.getElement());
    }

    /**
   * Removes all debug classes from an Element.
   * @param elem The element to remove debug classes from.
   */
    public static void clearDebugClasses(Element elem) {
        elem.removeAttribute(DEBUG_CLASS_ATTRIBUTE);
    }

    /**
   * Join a set of Strings into a space-separated String.
   * @param debugClasses to be joined
   * @return a String of debug classes
   */
    private static final String joinDebugClasses(Set<String> debugClasses) {
        StringBuilder result = new StringBuilder();
        for (String debugClass : debugClasses) {
            result.append(debugClass);
            result.append(" ");
        }
        return result.toString().trim();
    }

    /**
   * Get a set of debug classes present in an element.
   * @param elem from which debug classes will be read from
   * @return the set of debug classes
   */
    private static final Set<String> getDebugClasses(Element elem) {
        Set<String> result = new HashSet<String>();
        String debugClasses = elem.getAttribute(DEBUG_CLASS_ATTRIBUTE);
        result.addAll(Arrays.asList(debugClasses.split(" ")));
        return result;
    }

    /**
   * Set whether to enable debug classes (for testing purposes).
   *
   * @param enableDebugClass the new value
   */
    public static void setDebugClassEnabled(boolean enableDebugClass) {
        debugClassEnabled = enableDebugClass;
    }
}
