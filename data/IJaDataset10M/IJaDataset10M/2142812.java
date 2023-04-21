package org.aspencloud.simple9.builder.gen.src;

import java.util.List;
import java.util.Map;
import org.aspencloud.simple9.builder.dom.ModelAttribute;
import org.aspencloud.simple9.builder.dom.ModelDefinition;
import org.aspencloud.simple9.builder.dom.ModelRelation;
import org.aspencloud.simple9.persist.persistables.PersistableSet;
import org.aspencloud.simple9.util.StringUtils;

public class PropertyDescriptor {

    private String modelType;

    private String type;

    private String fullType;

    private String castType;

    private String typePackage;

    private String variable;

    private String getterName;

    private String hasserName;

    private String setterName;

    private String enumColumn;

    private String enumProp;

    private PropertyBuilder builder;

    private boolean primitive;

    private boolean nullable;

    private boolean readOnly;

    private boolean virtual;

    private boolean timeStamped;

    private ModelDefinition model;

    private ModelRelation opposite;

    private boolean hasMany;

    public PropertyDescriptor(ModelAttribute attribute, boolean hasTimestamps) {
        this(attribute.getType(), attribute.getName());
        modelType = attribute.getModel().getSimpleName();
        getterName = StringUtils.getterName(variable);
        nullable = attribute.isNullable();
        readOnly = attribute.isReadOnly();
        virtual = attribute.isVirtual();
        timeStamped = hasTimestamps;
        hasMany = false;
        builder = new AttributeBuilder(this);
    }

    public PropertyDescriptor(ModelRelation relation, boolean hasTimestamps) {
        this(relation.getType(), relation.getName());
        modelType = relation.getModel().getSimpleName();
        model = relation.getModel();
        opposite = relation.getOpposite();
        nullable = relation.isNullable();
        readOnly = relation.isReadOnly();
        virtual = relation.isVirtual();
        timeStamped = hasTimestamps;
        hasMany = relation.hasMany();
        if (relation.hasMany()) {
            castType = PersistableSet.class.getSimpleName();
            getterName = variable;
        } else {
            getterName = StringUtils.getterName(variable);
        }
        builder = relation.hasMany() ? new HasManyBuilder(this) : new HasOneBuilder(this);
    }

    PropertyDescriptor(String type, String name) {
        this.type = type.substring(type.lastIndexOf('.') + 1);
        variable = name;
        typePackage = type.substring(0, type.lastIndexOf('.'));
        fullType = type;
        castType = this.type;
        enumColumn = StringUtils.columnName(variable);
        enumProp = StringUtils.constant(variable);
        hasserName = StringUtils.hasserName(variable);
        setterName = StringUtils.setterName(variable);
    }

    public String castType() {
        return castType;
    }

    public String enumColumn() {
        return enumColumn;
    }

    public String enumProp() {
        return enumProp;
    }

    public String fullType() {
        return fullType;
    }

    public String getterName() {
        return getterName;
    }

    public String hasserName() {
        return hasserName;
    }

    public boolean isNullable() {
        return nullable;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public boolean hasDeclaration() {
        return !virtual;
    }

    public boolean hasEnum() {
        return true;
    }

    public boolean hasGetter() {
        return !"void".equals(type);
    }

    public boolean hasImport() {
        return typePackage != null;
    }

    public boolean hasOpposite() {
        return opposite != null;
    }

    public boolean hasMany() {
        return hasMany;
    }

    public boolean hasSetter() {
        return !readOnly;
    }

    public boolean hasTimestamps() {
        return timeStamped;
    }

    public List<String> imports() {
        return builder.getImports();
    }

    public boolean isPrimitive() {
        return primitive;
    }

    public boolean isType(Class<?> clazz) {
        return this.type.equals(clazz.getSimpleName());
    }

    public Map<String, String> methods() {
        return builder.getMethods();
    }

    public ModelDefinition model() {
        return model;
    }

    public String modelType() {
        return modelType;
    }

    public ModelRelation opposite() {
        return opposite;
    }

    public String setterName() {
        return setterName;
    }

    public String type() {
        return type;
    }

    public String typePackage() {
        return typePackage;
    }

    public String variable() {
        return variable;
    }

    public Map<String, String> variables() {
        return builder.getDeclarations();
    }
}
