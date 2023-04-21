package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.plugins.dnd.OneToManyFieldElement;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;

public class ClearOneToManyAssociationOption extends AbstractUserAction {

    public ClearOneToManyAssociationOption() {
        super("Clear association");
    }

    @Override
    public Consent disabled(final View view) {
        final OneToManyFieldElement content = (OneToManyFieldElement) view.getContent();
        return content.canClear();
    }

    @Override
    public void execute(final Workspace frame, final View view, final Location at) {
        final OneToManyFieldElement content = (OneToManyFieldElement) view.getContent();
        content.clear();
    }
}
