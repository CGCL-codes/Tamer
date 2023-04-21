package org.sqlexp.util.syntax.definition;

import java.util.ArrayList;
import org.sqlexp.util.syntax.definition.validation.ValidationError;

/**
 * Syntax definition root object.<br>
 * A syntax definition is composed of a vendor-specific set of properties.<br>
 * Entry point for properties operations like code generation.
 * @author Matthieu Réjou
 */
public class SyntaxDefinition {

    private String name;

    private SyntaxComposedProperty root;

    /**
	 * Constructs a new syntax definition object.
	 * @param name to set
	 * @param root property to set
	 */
    public SyntaxDefinition(final String name, final SyntaxComposedProperty root) {
        this.name = name;
        this.root = root;
    }

    /**
	 * Gets the name identifier.
	 * @return the name
	 */
    public final String getName() {
        return name;
    }

    /**
	 * Gets the root property.
	 * @return root composed property
	 */
    public final SyntaxComposedProperty getRoot() {
        return root;
    }

    /**
	 * Gets the root property.
	 * @param values to format
	 * @return vendor-specific SQL string
	 * @see SyntaxComposedProperty#format(SyntaxValueSet, SyntaxOutputBuilder)
	 */
    public final String format(final SyntaxValueSet values) {
        return format(values, null);
    }

    /**
	 * Gets the root property.
	 * @param values to format
	 * @param errors filled by validation errors that occurred during code generation
	 * @return vendor-specific SQL string
	 * @see SyntaxComposedProperty#format(SyntaxValueSet, SyntaxOutputBuilder)
	 */
    public final String format(final SyntaxValueSet values, final ArrayList<ValidationError> errors) {
        SyntaxOutputBuilder builder = new SyntaxOutputBuilder();
        root.format(values, builder);
        errors.addAll(builder.getErrors());
        return builder.getOutput();
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("{\n");
        sb.append(root.toString("  "));
        sb.append("\n}");
        return sb.toString();
    }
}
