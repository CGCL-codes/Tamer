package astcentric.structure.bl;

import java.util.Arrays;
import java.util.List;

/**
 * Definition of the signature of a function.
 */
public class FunctionSignature implements FunctionArgument {

    private final List<FunctionArgument> _arguments;

    private final DataTypeArgument _returnValue;

    /**
   * Creates an instance with the specified return value type and
   * array of argument types.
   * 
   * @throws IllegalArgumentException if <code>returnValue == null</code>.
   */
    public FunctionSignature(DataTypeArgument returnValue, FunctionArgument... arguments) {
        this(returnValue, Arrays.asList(arguments));
    }

    /**
   * Creates an instance with the specified return value type and
   * optional list of argument types.
   * 
   * @throws IllegalArgumentException if <code>returnValue == null</code>.
   */
    public FunctionSignature(DataTypeArgument returnValue, List<FunctionArgument> arguments) {
        if (returnValue == null) {
            throw new IllegalArgumentException("Unspecified return value.");
        }
        _returnValue = returnValue;
        _arguments = Util.createUnmodifiableList(arguments);
    }

    /**
   * Returns the definition of return value argument.
   */
    public final DataTypeArgument getReturnValue() {
        return _returnValue;
    }

    /**
   * Returns the definitions of the input arguments.
   * @return
   */
    public final List<FunctionArgument> getArguments() {
        return _arguments;
    }

    /**
   * Accepts only {@link FunctionSignatureVisitor} instances.
   */
    public void accept(FunctionArgumentVisitor visitor) {
        if (visitor instanceof FunctionSignatureVisitor) {
            ((FunctionSignatureVisitor) visitor).visit(this);
        } else {
            throw new IllegalArgumentException("Does not implement " + FunctionSignatureVisitor.class.getSimpleName() + ": " + visitor);
        }
    }
}
