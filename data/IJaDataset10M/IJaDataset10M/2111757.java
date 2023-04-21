package org.apache.isis.extensions.wicket.ui.components.collection;

import org.apache.isis.extensions.wicket.model.models.EntityCollectionModel;
import org.apache.isis.extensions.wicket.ui.ComponentFactory;
import org.apache.isis.extensions.wicket.ui.ComponentFactoryAbstract;
import org.apache.isis.extensions.wicket.ui.ComponentType;
import org.apache.isis.extensions.wicket.ui.components.collectioncontents.simple.CollectionContentsAsSimpleTable;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

/**
 * {@link ComponentFactory} for {@link CollectionContentsAsSimpleTable}.
 */
public class CollectionPanelFactory extends ComponentFactoryAbstract {

    private static final long serialVersionUID = 1L;

    private static final String NAME = "labelled";

    public CollectionPanelFactory() {
        super(ComponentType.COLLECTION_NAME_AND_CONTENTS, NAME);
    }

    @Override
    public ApplicationAdvice appliesTo(IModel<?> model) {
        if (!(model instanceof EntityCollectionModel)) {
            return ApplicationAdvice.DOES_NOT_APPLY;
        }
        return ApplicationAdvice.APPLIES;
    }

    public Component createComponent(String id, IModel<?> model) {
        EntityCollectionModel collectionModel = (EntityCollectionModel) model;
        return new CollectionPanel(id, collectionModel);
    }
}
