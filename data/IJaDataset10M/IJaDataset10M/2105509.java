package org.nakedobjects.nos.client.dnd.tree;

import org.nakedobjects.noa.adapter.Naked;
import org.nakedobjects.noa.adapter.NakedCollection;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.reflect.Consent;
import org.nakedobjects.noa.reflect.NakedObjectAction.Type;
import org.nakedobjects.noa.spec.Features;
import org.nakedobjects.nof.core.reflect.Allow;
import org.nakedobjects.nos.client.dnd.UserAction;
import org.nakedobjects.nos.client.dnd.UserActionSet;
import org.nakedobjects.nos.client.dnd.View;
import org.nakedobjects.nos.client.dnd.Workspace;
import org.nakedobjects.nos.client.dnd.drawing.Location;

public class TreeDisplayRules {

    private static boolean showCollectionsOnly = false;

    private TreeDisplayRules() {
    }

    public static void menuOptions(final UserActionSet options) {
        if (true) return;
        UserAction option = new UserAction() {

            public void execute(final Workspace workspace, final View view, final Location at) {
                showCollectionsOnly = !showCollectionsOnly;
            }

            public String getName(final View view) {
                return showCollectionsOnly ? "Show collections only" : "Show all references";
            }

            public Consent disabled(final View view) {
                return Allow.DEFAULT;
            }

            public String getDescription(final View view) {
                return "This option makes the system only show collections within the trees, and not single elements";
            }

            public Type getType() {
                return USER;
            }

            public String getHelp(final View view) {
                return "";
            }
        };
        options.add(option);
    }

    public static boolean isCollectionsOnly() {
        return showCollectionsOnly;
    }

    public static boolean canDisplay(final Naked object) {
        boolean lookupView = object != null && Features.isBoundedSet(object.getSpecification());
        boolean showNonCollections = !TreeDisplayRules.isCollectionsOnly();
        boolean objectView = object instanceof NakedObject && showNonCollections;
        boolean collectionView = object instanceof NakedCollection;
        return (objectView || collectionView) && !lookupView;
    }
}
