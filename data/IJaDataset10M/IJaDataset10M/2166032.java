package org.wicketopia.domdrides.component.link;

import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.IModel;
import org.domdrides.entity.Entity;
import org.domdrides.repository.Repository;
import java.io.Serializable;

/**
 * @since 1.0
 */
public abstract class CreateEntityLink<EntityType extends Entity<IdType>, IdType extends Serializable> extends SubmitLink {

    private final Repository<EntityType, IdType> repository;

    protected CreateEntityLink(String id, Repository<EntityType, IdType> repository, IModel<EntityType> model) {
        super(id);
        setDefaultModel(model);
        this.repository = repository;
    }

    protected abstract void afterCreate(EntityType entity);

    @Override
    @SuppressWarnings("unchecked")
    public void onSubmit() {
        EntityType entity = (EntityType) getDefaultModelObject();
        entity = repository.add(entity);
        setDefaultModelObject(entity);
        afterCreate(entity);
    }
}
