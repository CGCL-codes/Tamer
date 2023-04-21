package org.wicketopia.persistence.component.link;

import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.IModel;
import org.wicketopia.persistence.PersistenceProvider;

public abstract class CreateLink<T> extends SubmitLink {

    private final PersistenceProvider persistenceProvider;

    public CreateLink(String id, IModel<T> model, PersistenceProvider persistenceProvider) {
        super(id, model);
        this.persistenceProvider = persistenceProvider;
    }

    protected abstract void afterCreate(T object);

    @Override
    @SuppressWarnings("unchecked")
    public final void onSubmit() {
        T object = (T) getDefaultModelObject();
        object = persistenceProvider.create(object);
        afterCreate(object);
    }
}
