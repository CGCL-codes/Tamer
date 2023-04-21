package net.sf.logsaw.core.query.support;

import net.sf.logsaw.core.field.ALogEntryField;
import net.sf.logsaw.core.query.IRestrictionVisitor;
import net.sf.logsaw.core.query.Operator;
import org.eclipse.core.runtime.Assert;

/**
 * Abstract base class for query restrictions.
 * 
 * @author Philipp Nanz
 * @param <VT> the value type
 */
public abstract class ARestriction<VT> {

    private ALogEntryField<?, VT> field;

    private VT value;

    private Operator operator;

    /**
	 * Constructor.
	 * @param field the log entry field
	 * @param operator the operator to apply
	 * @param value the value
	 */
    public ARestriction(ALogEntryField<?, VT> field, Operator operator, VT value) {
        Assert.isNotNull(field, "field");
        Assert.isNotNull(operator, "operator");
        Assert.isNotNull(value, "value");
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    /**
	 * @return the field
	 */
    public final ALogEntryField<?, VT> getField() {
        return field;
    }

    /**
	 * @return the value
	 */
    public final VT getValue() {
        return value;
    }

    /**
	 * Returns the operator that this restriction is representing.
	 * @return the operator instance
	 */
    public final Operator getOperator() {
        return operator;
    }

    /**
	 * Implements the visitor pattern.
	 * @param visitor the visitor
	 */
    public abstract void visit(IRestrictionVisitor visitor);
}
