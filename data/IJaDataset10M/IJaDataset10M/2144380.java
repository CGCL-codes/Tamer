package org.qctools4j.model.permission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jqc.QcCustomizationField.FIELD_TYPE;

/**
 * A field description.
 * 
 * @author tszadel
 */
public class FieldDescription implements Comparable<FieldDescription>, Serializable {

    /** The serialVersionUID. */
    private static final long serialVersionUID = 1L;

    private boolean active;

    private FIELD_TYPE type;

    private boolean canFilter = true;

    private boolean custom;

    private String defaultValue = null;

    private int fieldSize;

    private String fieldType;

    private String label;

    private boolean modifiableByOwnerOnly = false;

    private boolean modifiableByUserOnly = false;

    private final String name;

    private final List<FieldOption> options;

    private boolean readOnly = false;

    private boolean required = false;

    private boolean searchable;

    private boolean system;

    private boolean verfied;

    private boolean visible = true;

    /**
	 * Constructor.
	 * 
	 * @param pName The name of the field (in the Qc Database).
	 */
    public FieldDescription(final String pName) {
        this(pName, new ArrayList<FieldOption>());
    }

    public FieldDescription(final String pName, final List<FieldOption> options) {
        name = pName;
        this.options = options;
    }

    /**
	 * Returns canFilter.
	 * 
	 * @return The canFilter.
	 */
    public boolean canFilter() {
        return canFilter;
    }

    /**
	 * Returns defaultValue.
	 * 
	 * @return The defaultValue.
	 */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
	 * Returns fieldSize.
	 * 
	 * @return The fieldSize.
	 */
    public int getFieldSize() {
        return fieldSize;
    }

    /**
	 * Returns fieldType.
	 * 
	 * @return The fieldType.
	 */
    public String getFieldType() {
        return fieldType;
    }

    /**
	 * Returns label.
	 * 
	 * @return The label.
	 */
    public String getLabel() {
        return label;
    }

    /**
	 * Returns name.
	 * 
	 * @return The name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Returns options.
	 * 
	 * @return The options.
	 */
    public List<FieldOption> getOptions() {
        return options;
    }

    /**
	 * Returns the options as a list.
	 * 
	 * @param pFullNames True if the children must include the parent name.
	 * @return The options.
	 */
    public List<String> getOptionsAsStringList(final boolean pFullNames) {
        final List<String> lList = new ArrayList<String>();
        for (final FieldOption lOpt : options) {
            lList.add(lOpt.getName());
            lList.addAll(lOpt.getChildrenAsStringList(pFullNames));
        }
        return lList;
    }

    /**
	 * Returns true if the field has options.
	 * 
	 * @return True if options.
	 */
    public boolean hasOptions() {
        return !options.isEmpty();
    }

    /**
	 * Returns active.
	 * 
	 * @return The active.
	 */
    public boolean isActive() {
        return active;
    }

    /**
	 * Returns custom.
	 * 
	 * @return The custom.
	 */
    public boolean isCustom() {
        return custom;
    }

    /**
	 * Returns modifiableByOwnerOnly.
	 * 
	 * @return The modifiableByOwnerOnly.
	 */
    public boolean isModifiableByOwnerOnly() {
        return modifiableByOwnerOnly;
    }

    /**
	 * Returns modifiableByUserOnly.
	 * 
	 * @return The modifiableByUserOnly.
	 */
    public boolean isModifiableByUserOnly() {
        return modifiableByUserOnly;
    }

    /**
	 * Returns readOnly.
	 * 
	 * @return The readOnly.
	 */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
	 * Returns required.
	 * 
	 * @return The required.
	 */
    public boolean isRequired() {
        return required;
    }

    /**
	 * Returns searchable.
	 * 
	 * @return The searchable.
	 */
    public boolean isSearchable() {
        return searchable;
    }

    /**
	 * Returns system.
	 * 
	 * @return The system.
	 */
    public boolean isSystem() {
        return system;
    }

    /**
	 * Returns verfied.
	 * 
	 * @return The verfied.
	 */
    public boolean isVerfied() {
        return verfied;
    }

    /**
	 * Returns visible.
	 * 
	 * @return The visible.
	 */
    public boolean isVisible() {
        return visible;
    }

    /**
	 * Sets the active.
	 * 
	 * @param pActive The active.
	 */
    public void setActive(final boolean pActive) {
        active = pActive;
    }

    /**
	 * Sets the can Filter.
	 * 
	 * @param canFilter The canFilter.
	 */
    public void setCanFilter(final boolean canFilter) {
        this.canFilter = canFilter;
    }

    /**
	 * Sets the custom.
	 * 
	 * @param pCustom The custom.
	 */
    public void setCustom(final boolean pCustom) {
        custom = pCustom;
    }

    /**
	 * Sets the defaultValue.
	 * 
	 * @param pDefaultValue The defaultValue.
	 */
    public void setDefaultValue(final String pDefaultValue) {
        defaultValue = pDefaultValue;
    }

    /**
	 * Sets the fieldSize.
	 * 
	 * @param pFieldSize The fieldSize.
	 */
    public void setFieldSize(final int pFieldSize) {
        fieldSize = pFieldSize;
    }

    /**
	 * Sets the fieldType.
	 * 
	 * @param pFieldType The fieldType.
	 */
    public void setFieldType(final String pFieldType) {
        fieldType = pFieldType;
    }

    /**
	 * Sets the label.
	 * 
	 * @param pLabel The label.
	 */
    public void setLabel(final String pLabel) {
        label = pLabel;
    }

    /**
	 * Sets the modifiableByOwnerOnly.
	 * 
	 * @param pModifiableByOwnerOnly The modifiableByOwnerOnly.
	 */
    public void setModifiableByOwnerOnly(final boolean pModifiableByOwnerOnly) {
        modifiableByOwnerOnly = pModifiableByOwnerOnly;
    }

    /**
	 * Sets the modifiableByUserOnly.
	 * 
	 * @param pModifiableByUserOnly The modifiableByUserOnly.
	 */
    public void setModifiableByUserOnly(final boolean pModifiableByUserOnly) {
        modifiableByUserOnly = pModifiableByUserOnly;
    }

    /**
	 * Sets the readOnly.
	 * 
	 * @param pReadOnly The readOnly.
	 */
    public void setReadOnly(final boolean pReadOnly) {
        readOnly = pReadOnly;
    }

    /**
	 * Sets the required.
	 * 
	 * @param pRequired The required.
	 */
    public void setRequired(final boolean pRequired) {
        required = pRequired;
    }

    /**
	 * Sets the searchable.
	 * 
	 * @param pSearchable The searchable.
	 */
    public void setSearchable(final boolean pSearchable) {
        searchable = pSearchable;
    }

    /**
	 * Sets the system.
	 * 
	 * @param pSystem The system.
	 */
    public void setSystem(final boolean pSystem) {
        system = pSystem;
    }

    /**
	 * Sets the verfied.
	 * 
	 * @param pVerfied The verfied.
	 */
    public void setVerfied(final boolean pVerfied) {
        verfied = pVerfied;
    }

    /**
	 * Sets the visible.
	 * 
	 * @param pVisible The visible.
	 */
    public void setVisible(final boolean pVisible) {
        visible = pVisible;
    }

    /**
	 * Overrides toString. {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        final StringBuilder lStr = new StringBuilder();
        lStr.append(name);
        lStr.append(": label=");
        lStr.append(getLabel());
        lStr.append(", visible=");
        lStr.append(isVisible());
        lStr.append(", required=");
        lStr.append(isRequired());
        lStr.append(", readOnly=");
        lStr.append(isReadOnly());
        lStr.append(", active=");
        lStr.append(isActive());
        lStr.append(", searchable=");
        lStr.append(isSearchable());
        lStr.append(", custom=");
        lStr.append(isCustom());
        lStr.append(", field type=");
        lStr.append(getFieldType());
        lStr.append(", field size=");
        lStr.append(getFieldSize());
        lStr.append(", system=");
        lStr.append(isVerfied());
        lStr.append(", verified=");
        lStr.append(isSystem());
        return lStr.toString();
    }

    /**
	 * Overrides compareTo.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    public int compareTo(final FieldDescription pField2) {
        int lRet = new Boolean(isCustom()).compareTo(pField2.isCustom());
        if (lRet == 0) {
            lRet = getLabel().compareToIgnoreCase(pField2.getLabel());
            if (lRet == 0) {
                lRet = getName().compareToIgnoreCase(pField2.getName());
            }
        }
        return lRet;
    }

    public FIELD_TYPE getType() {
        return type;
    }

    public void setType(final FIELD_TYPE type) {
        this.type = type;
    }
}
