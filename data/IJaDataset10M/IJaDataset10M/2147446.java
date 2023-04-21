package org.obe.xpdl.model.activity;

import org.obe.xpdl.PackageVisitor;
import org.obe.xpdl.model.misc.ExecutionType;
import org.obe.xpdl.model.misc.Invocation;
import java.util.Arrays;

/**
 * A SubFlow represents another workflow process which is executed within the
 * context of the current workflow process.
 *
 * @author Adrian Price
 */
public final class SubFlow extends Invocation implements Implementation {

    private static final long serialVersionUID = -1475378210472793406L;

    /**
     * Constant representing the default execution type (synchronous).
     */
    public static final ExecutionType DEFAULT_EXECUTION = ExecutionType.SYNCHRONOUS;

    private ExecutionType _execution;

    public SubFlow() {
    }

    /**
     * Construct a new SubFlow which represents the specified workflow process
     * ID.
     *
     * @param id The workflow process id
     */
    public SubFlow(String id) {
        setId(id);
    }

    public void accept(PackageVisitor visitor) {
        visitor.visit(this);
        super.accept(visitor);
    }

    public ImplementationType getType() {
        return ImplementationType.SUBFLOW;
    }

    /**
     * Get the execution type (either synchronous or asynchronous).
     *
     * @return The ExecutionType
     */
    public ExecutionType getExecution() {
        return _execution;
    }

    /**
     * Set the execution type.  If the execution parameter is null then the
     * default execution type (synchronous) is used.
     *
     * @param execution The execution type
     */
    public void setExecution(ExecutionType execution) {
        _execution = execution;
    }

    public String toString() {
        return "SubFlow[id=" + _id + ", execution=" + _execution + ", actualParameters" + Arrays.asList(_actualParameter) + ']';
    }
}
