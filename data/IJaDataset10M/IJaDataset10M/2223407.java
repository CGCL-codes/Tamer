package org.monet.modelling.domain.model.builder.impl;

import org.monet.modelling.domain.metamodel.AttributeMetaModel;
import org.monet.modelling.domain.metamodel.PropertyMetaModel;
import org.monet.modelling.domain.model.Attribute;
import org.monet.modelling.domain.model.Property;
import org.monet.modelling.domain.model.builder.ModelBuilder;
import org.monet.modelling.domain.model.impl.PropertyImpl;

public class PropertyBuilderImpl implements ModelBuilder<PropertyMetaModel, Property> {

    private ModelBuilder<AttributeMetaModel, Attribute> attributeBuilder;

    public PropertyBuilderImpl(ModelBuilder<AttributeMetaModel, Attribute> attributeBuilder) {
        this.attributeBuilder = attributeBuilder;
    }

    public Property build(PropertyMetaModel metaModel) {
        Property property = new PropertyImpl(metaModel.getName());
        property.setDescription(metaModel.getDescription());
        property.setMultiple(metaModel.isMultiple());
        property.setRequired(metaModel.isRequired());
        property.setMetaModel(metaModel);
        for (AttributeMetaModel attributeMetaModel : metaModel.getAttributeMetaModels()) {
            Attribute attribute = attributeBuilder.build(attributeMetaModel);
            property.addAttribute(attribute);
        }
        return property;
    }
}
