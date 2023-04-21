package org.monet.modelling.domain.model;

import org.monet.modelling.domain.metamodel.AttributeMetaModel;

public interface Attribute extends Model, HasParent {

    public void setRequired(boolean value);

    public boolean isRequired();

    public String getName();

    public String getValue();

    public void setValue(String value);

    public void setDescription(String description);

    public String getDescription();

    public AttributeMetaModel getMetaModel();

    public void setMetaModel(AttributeMetaModel metaModel);
}
