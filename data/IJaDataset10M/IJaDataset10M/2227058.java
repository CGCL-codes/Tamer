package com.volantis.styling.debug;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.Styles;
import com.volantis.styling.NestedStyles;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.MutableStylePropertySetImpl;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertySet;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;
import java.util.Iterator;

/**
 * Write debug information for {@link Styles}.
 */
public class DebugStyles {

    /**
     * The buffer into which the debug information is added.
     */
    protected final StringBuffer debug;

    /**
     * The properties that should be output.
     */
    protected final StylePropertySet interestingProperties;

    protected final boolean onlyExplicitlySpecified;

    /**
     * Mark those properties which were explicitly specified with a '*'.
     */
    protected final boolean explicitlySpecifiedMarked;

    public DebugStyles(StylePropertySet interestingProperties, boolean onlyExplicitlySpecified) {
        this(interestingProperties, onlyExplicitlySpecified, false);
    }

    /**
     * Initialise.
     *
     * @param interestingProperties The properties that should be output.
     */
    public DebugStyles(StylePropertySet interestingProperties, boolean onlyExplicitlySpecified, boolean explicitlySpecifiedMarked) {
        if (interestingProperties == null) {
            MutableStylePropertySet mutable = new MutableStylePropertySetImpl();
            mutable.addAll();
            interestingProperties = mutable;
        }
        this.interestingProperties = interestingProperties;
        this.onlyExplicitlySpecified = onlyExplicitlySpecified;
        this.explicitlySpecifiedMarked = explicitlySpecifiedMarked;
        debug = new StringBuffer(1024);
    }

    public DebugStyles() {
        this(null, false, false);
    }

    public String output(Styles styles, String indent) {
        return output(styles, null, indent);
    }

    public String output(Styles styles, PropertyValues parent, String indent) {
        debug.setLength(0);
        if (styles != null) {
            output(indent, "", styles, parent);
        }
        return debug.toString();
    }

    protected void output(String indent, String entityRepresentation, Styles styles, PropertyValues parent) {
        if (entityRepresentation != null && entityRepresentation.length() != 0) {
            debug.append(entityRepresentation).append(" ");
        }
        output(styles.getPropertyValues());
        Iterator iterator = styles.iterator();
        while (iterator.hasNext()) {
            debug.append("\n");
            NestedStyles nestedStyles = (NestedStyles) iterator.next();
            PseudoStyleEntity entity = nestedStyles.getPseudoStyleEntity();
            debug.append(indent);
            output(indent + DebugHelper.getIndent(entityRepresentation.length() + 1), entityRepresentation + entity.getCSSRepresentation(), nestedStyles, parent);
        }
    }

    protected void output(MutablePropertyValues values) {
        debug.append("{");
        outputDeclarationBody(values);
        debug.append("}");
    }

    protected void outputDeclarationBody(MutablePropertyValues values) {
        Iterator iterator = values.stylePropertyIterator();
        boolean appended = false;
        while (iterator.hasNext()) {
            StyleProperty property = (StyleProperty) iterator.next();
            if (isOutputtable(values, property)) {
                StyleValue value = values.getComputedValue(property);
                if (value != null) {
                    if (appended) {
                        debug.append("; ");
                    } else {
                        appended = true;
                    }
                    debug.append(property.getName()).append(": ").append(value.getStandardCSS());
                    if (explicitlySpecifiedMarked && values.wasExplicitlySpecified(property)) {
                        debug.append("*");
                    }
                }
            }
        }
    }

    protected boolean isOutputtable(MutablePropertyValues values, StyleProperty property) {
        if (!interestingProperties.contains(property)) {
            return false;
        }
        if (onlyExplicitlySpecified) {
            MutablePropertyValues extended = values;
            if (!extended.wasExplicitlySpecified(property)) {
                return false;
            }
        }
        return true;
    }
}
