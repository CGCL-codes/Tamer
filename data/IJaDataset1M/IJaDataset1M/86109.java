package org.argouml.uml.diagram.ui;

import org.argouml.model.Model;

/**
 * A Mode to interpret user input while creating a permission edge.
 * The permission can connect any model elements including those represented
 * by edges as well as nodes.
 * <p>
 * TODO: In UML 2.x, Permission becomes PackageImport and the class hierarchy
 * has changed so that it is no longer a subtype of Dependency, so this may
 * need to be refactored to work correctly.
 */
public final class ModeCreatePermission extends ModeCreateDependency {

    protected final Object getMetaType() {
        return Model.getMetaTypes().getPackageImport();
    }
}
