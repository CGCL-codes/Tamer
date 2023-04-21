package org.monet.modelling.domain.metamodel.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.monet.modelling.domain.metamodel.AttributeMetaModel;
import org.monet.modelling.domain.metamodel.DeclarationMetaModelReference;
import org.monet.modelling.domain.metamodel.DefinitionMetaModel;
import org.monet.modelling.domain.metamodel.PropertyMetaModel;

public class DefinitionMetaModelImpl implements DefinitionMetaModel {

    private String name;

    private String tag;

    private String description;

    private List<PropertyMetaModel> properties;

    private List<AttributeMetaModel> attributes;

    private List<DeclarationMetaModelReference> includes;

    public DefinitionMetaModelImpl(String name, String tag) {
        this.name = name;
        this.tag = tag;
        this.description = "";
        this.properties = new ArrayList<PropertyMetaModel>();
        this.attributes = new ArrayList<AttributeMetaModel>();
        this.includes = new ArrayList<DeclarationMetaModelReference>();
    }

    public String getName() {
        return name;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public List<PropertyMetaModel> getPropertyMetaModels() {
        return this.properties;
    }

    @Override
    public void addPropertyMetaModel(PropertyMetaModel metaModel) {
        this.properties.add(metaModel);
    }

    @Override
    public void setPropertyMetaModels(List<PropertyMetaModel> metaModels) {
        this.properties = metaModels;
    }

    @Override
    public List<AttributeMetaModel> getAttributeMetaModels() {
        return this.attributes;
    }

    @Override
    public void addAttributeMetaModel(AttributeMetaModel metaModel) {
        this.attributes.add(metaModel);
    }

    @Override
    public void setAttributeMetaModels(List<AttributeMetaModel> metaModels) {
        this.attributes = metaModels;
    }

    @Override
    public List<DeclarationMetaModelReference> getIncludes() {
        return this.includes;
    }

    @Override
    public void addInclude(DeclarationMetaModelReference include) {
        this.includes.add(include);
    }

    @Override
    public void setIncludes(List<DeclarationMetaModelReference> includes) {
        this.includes = includes;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DefinitionMetaModel)) return false;
        if (this == obj) return true;
        return equals((DefinitionMetaModel) obj);
    }

    @Override
    public int hashCode() {
        HashCodeBuilder b = new HashCodeBuilder();
        b.append(name).append(tag);
        return b.toHashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name: ").append(name);
        sb.append("tag: ").append(tag);
        sb.append("properties count: ").append(properties.size());
        sb.append("attributes count: ").append(attributes.size());
        sb.append("includes count: ").append(includes.size());
        return super.toString();
    }

    private boolean equals(DefinitionMetaModel metaModel) {
        EqualsBuilder b = new EqualsBuilder();
        b.append(name, metaModel.getName());
        b.append(tag, metaModel.getTag());
        return b.isEquals();
    }
}
