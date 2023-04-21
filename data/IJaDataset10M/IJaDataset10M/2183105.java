package com.dynamicobjects.dd;

/**
 * 
 * @author Hern�n Lorenzini
 */
public class DDAttributeInfo {

    private DDObjectInfo parent;

    private int attributeId;

    private String nameField;

    private String name;

    private DDAttributeType ddtype;

    private boolean persistent;

    private String alias;

    private int keyOrder;

    private String className;

    private boolean updateable;

    private int lengthPrecision;

    private int scale;

    private String defaultValue;

    private boolean autoIncrement;

    private boolean required;

    private boolean override;

    private boolean hiddenMethod;

    private boolean internal;

    private String referencePath;

    private boolean isReference;

    public DDAttributeInfo(String nameField, String alias, DDAttributeType ddtype) {
        this.referencePath = "";
        this.nameField = nameField;
        this.ddtype = ddtype;
        if (alias != null && alias.length() != 0) this.name = alias; else this.name = nameField;
    }

    public DDObjectInfo getParent() {
        return parent;
    }

    public void setParent(DDObjectInfo parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("attributeId=" + attributeId);
        ret.append(",name=" + name);
        ret.append(",dbType=" + ddtype);
        ret.append(",isPersistent=" + persistent);
        ret.append(",nameField=" + nameField);
        ret.append(",alias=" + alias);
        ret.append(",keyOrder=" + keyOrder);
        ret.append(",className=" + className);
        ret.append(",updateable=" + updateable);
        ret.append(",lengthPrecision=" + lengthPrecision);
        ret.append(",scale=" + scale);
        ret.append(",defaultValue=" + defaultValue);
        ret.append(",autoIncrement=" + autoIncrement);
        ret.append(",required=" + required);
        ret.append(",override=" + override);
        ret.append(",hiddenMethod=" + hiddenMethod);
        ret.append(",internal=" + internal);
        ret.append(",referencePath=" + referencePath);
        ret.append(",isReference=" + isReference);
        return ret.toString();
    }

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DDAttributeType getDdtype() {
        return ddtype;
    }

    public void setDdtype(DDAttributeType ddtype) {
        this.ddtype = ddtype;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getKeyOrder() {
        return keyOrder;
    }

    public void setKeyOrder(int keyOrder) {
        this.keyOrder = keyOrder;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isUpdateable() {
        return updateable;
    }

    public void setUpdateable(boolean updateable) {
        this.updateable = updateable;
    }

    public String getNameField() {
        return nameField;
    }

    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    public int getLengthPrecision() {
        return lengthPrecision;
    }

    public void setLengthPrecision(int lengthPrecision) {
        this.lengthPrecision = lengthPrecision;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getReferencePath() {
        return referencePath;
    }

    public void setReferencePath(String referencePath) {
        this.referencePath = referencePath;
        isReference = (referencePath != null && referencePath.length() != 0);
    }

    public boolean isReference() {
        return isReference;
    }

    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    public boolean isHiddenMethod() {
        return hiddenMethod;
    }

    public void setHiddenMethod(boolean hiddenMethod) {
        this.hiddenMethod = hiddenMethod;
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }
}
