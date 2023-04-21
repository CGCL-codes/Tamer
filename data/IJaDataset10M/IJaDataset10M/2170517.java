package astcentric.structure.bl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Declaration of a type. A type corresponds to a class in Haskell.
 */
public final class TypeDeclaration extends Declaration {

    private final Variable _variable;

    private final List<Function> _functions = new ArrayList<Function>();

    private final List<Function> _unmodifiableFunctions = Collections.unmodifiableList(_functions);

    /**
   * Creates an instance for the specified name, variable, and defining source.
   * 
   * @throws IllegalArgumentException if one of the arguments is
   *             <code>null</code>.
   */
    public TypeDeclaration(String name, Variable variable, DefiningSource definingSource) {
        super(name, definingSource);
        if (variable == null) {
            throw new IllegalArgumentException("Unspecified variable for type declaration '" + name + "'.");
        }
        _variable = variable;
    }

    public FunctionWithPatternEvaluatorRegistration addFunctionDeclaration(FunctionDeclaration functionDeclaration, DefiningSource definingSource) {
        assertThisIsNotSealed();
        OverloadableFunction function = new OverloadableFunction(_variable, functionDeclaration, definingSource);
        _functions.add(function);
        return function;
    }

    public List<Function> getFunctions() {
        return _unmodifiableFunctions;
    }

    /**
   * Returns the variable.
   */
    public final Variable getVariable() {
        return _variable;
    }

    @Override
    public TypeDeclaration seal() {
        super.seal();
        return this;
    }

    @Override
    public String toString() {
        VariableNamer namer = new VariableNamer();
        StringBuilder builder = new StringBuilder();
        builder.append("class ").append(getName()).append(" ");
        builder.append(namer.name(_variable)).append(" where");
        for (Function function : _functions) {
            builder.append("\n    ");
            FunctionDeclaration declaration = function.getFunctionDeclaration();
            builder.append(Util.renderFunctionDeclaration(declaration, namer));
        }
        return builder.toString();
    }
}
