package com.volantis.mcs.themes.impl;

import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.themes.SelectorVisitor;
import com.volantis.mcs.themes.TypeSelector;
import com.volantis.mcs.themes.impl.parsing.ElementSelectorParser;

/**
 * A basic implementation of the {@link com.volantis.mcs.themes.TypeSelector} interface.
 */
public class DefaultTypeSelector extends AbstractSelector implements TypeSelector {

    /**
     * The type that this selector will match.
     */
    private String type;

    /**
     * The namespace that this selector will match.
     */
    private String namespacePrefix;

    /**
     * Used to identify the type property of this class when logging
     * validation errors.
     */
    PropertyIdentifier TYPE = new PropertyIdentifier(TypeSelector.class, "type");

    /**
     * Initialise.
     */
    public DefaultTypeSelector() {
    }

    /**
     * Initialise.
     *
     * @param namespacePrefix The namespace, may be null.
     * @param type      The element type.
     */
    public DefaultTypeSelector(String namespacePrefix, String type) {
        this.namespacePrefix = namespacePrefix;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String newType) {
        type = newType;
    }

    public String getNamespacePrefix() {
        return namespacePrefix;
    }

    public void setNamespacePrefix(String newNamespacePrefix) {
        namespacePrefix = newNamespacePrefix;
    }

    public void validate(ValidationContext context) {
        Step step;
        step = context.pushPropertyStep(TYPE);
        if (!VALID_TYPES.contains(type)) {
            context.addDiagnostic(this, DiagnosticLevel.ERROR, context.createMessage("theme-invalid-type", type));
        }
        context.popStep(step);
        validateNamespacePrefix(context, NAMESPACE, namespacePrefix);
    }

    public boolean equals(Object o) {
        boolean equal = false;
        if (o != null && o.getClass() == DefaultTypeSelector.class) {
            DefaultTypeSelector other = (DefaultTypeSelector) o;
            if ((type == null ? other.type == null : type.equals(other.type)) && (namespacePrefix == null ? other.namespacePrefix == null : namespacePrefix.equals(other.namespacePrefix))) {
                equal = true;
            }
        }
        return equal;
    }

    public int hashCode() {
        int hash = 8375;
        hash = hash * 19 + type == null ? 0 : type.hashCode();
        hash = hash * 19 + namespacePrefix == null ? 0 : namespacePrefix.hashCode();
        return hash;
    }

    public String toString() {
        return new ElementSelectorParser().objectToText(this);
    }

    public void accept(SelectorVisitor visitor) {
        visitor.visit(this);
    }

    public Object copy() {
        TypeSelector copy = new DefaultTypeSelector(namespacePrefix, type);
        return copy;
    }
}
