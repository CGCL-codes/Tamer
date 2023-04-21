package org.nakedobjects.plugins.dnd.combined;

import java.util.ArrayList;
import java.util.List;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFilters;
import org.nakedobjects.plugins.dnd.view.Axes;
import org.nakedobjects.plugins.dnd.view.Content;
import org.nakedobjects.plugins.dnd.view.Toolkit;
import org.nakedobjects.plugins.dnd.view.View;
import org.nakedobjects.plugins.dnd.view.ViewRequirement;
import org.nakedobjects.plugins.dnd.view.composite.CompositeViewSpecification;
import org.nakedobjects.runtime.context.NakedObjectsContext;

public abstract class SplitViewSpecification extends CompositeViewSpecification {

    public SplitViewSpecification() {
        builder = new SplitViewBuilder(this);
    }

    public boolean canDisplay(ViewRequirement requirement) {
        if (requirement.isObject() && requirement.is(ViewRequirement.OPEN) && !requirement.isSubview()) {
            Content fieldContent = determineSecondaryContent(requirement.getContent());
            return fieldContent != null && fieldContent.getNaked() != null;
        } else {
            return false;
        }
    }

    abstract View createMainView(Axes axes, Content mainContent, final Content secondaryContent);

    abstract View createSecondaryView(Axes axes, final Content fieldContent);

    abstract Content determineSecondaryContent(Content content);

    Content field(NakedObjectAssociation field, Content content) {
        NakedObjectSpecification spec = content.getSpecification();
        NakedObject target = content.getNaked();
        return Toolkit.getContentFactory().createFieldContent(field, target);
    }

    List<NakedObjectAssociation> determineAvailableFields(Content content) {
        NakedObjectSpecification spec = content.getSpecification();
        NakedObject target = content.getNaked();
        AuthenticationSession session = NakedObjectsContext.getAuthenticationSession();
        NakedObjectAssociation[] fields = spec.getAssociations(NakedObjectAssociationFilters.dynamicallyVisible(session, target));
        List<NakedObjectAssociation> selectableFields = new ArrayList<NakedObjectAssociation>();
        for (NakedObjectAssociation field : fields) {
            if (validField(field)) {
                selectableFields.add(field);
            }
        }
        return selectableFields;
    }

    abstract boolean validField(NakedObjectAssociation field);
}
