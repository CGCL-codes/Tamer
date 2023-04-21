package org.argouml.ui;

import java.util.List;

/**
 * Interface for a factory of context popups, which are created based on a
 * given object.
 * 
 * The factory creates an additional popup on the context menu for selected
 * elements
 * @author Enrico Da Ros
 */
public interface ContextActionFactory {

    /**
     * This method shall test the type of the given object,
     * and if recognised, create a new Vector of Action 
     * derived classes and null (for the separators). <p>
     * 
     * If the object type is not something this factory 
     * knows how to deal with, then null shall be returned
     * (do NOT throw an exception). <p>
     * 
     * If the given object falls within a class of objects 
     * that this factory is the exclusive factory for,
     * then it is allowed to throw an exception if 
     * the object is invalid/unknown. However,
     * be careful not to break the possibility to extend ArgoUML. 
     * @param element the object to create a new context popup for
     * @return the new menu structure or null
     */
    List createContextPopupActions(Object element);
}
