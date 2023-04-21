package net.sf.sasl.language.placeholder.aop.interpreter;

import java.util.List;
import net.sf.sasl.language.placeholder.syntax.ASTNode;
import net.sf.sasl.language.placeholder.syntax.PlaceholderNode;
import net.sf.sasl.language.placeholder.syntax.ScriptNode;
import net.sf.sasl.language.placeholder.syntax.types.StringNode;
import net.sf.sasl.language.placeholder.syntax.types.TypeValueNode;
import org.springframework.util.Assert;

/**
 * Small interpreter that can interpreter a placeholder script given as an
 * abstract placeholder syntax tree.<br>
 * The targetMethod, targetObject, occuredException, targetReturnValue and
 * targetArguments properties of {@link net.sf.sasl.language.placeholder.aop.interpreter.IEnvironment
 * IEnvironment} will be also offered as interpreter environment variables.
 * 
 * @author Philipp Förmer
 * @since 0.0.1 (sasl-common-aspect-library)
 */
public class ASTInterpreter implements IInterpreter {

    /**
	 * Fast lookup map for placeholder resolvers for a given placeholder name
	 * symbol.
	 * 
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    private IPlaceholderResolverSymbolTable placeholderSymbolTable;

    /**
	 * The interpreting environment which will be passed to placeholder
	 * resolvers.
	 * 
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    private Environment environment = new Environment(this);

    /**
	 * Script that should get interpreted by the interpreter.
	 * 
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    private ScriptNode scriptNode;

    /**
	 * @param script
	 *            non null
	 * @param resolverList
	 *            non null
	 * @param environment
	 *            non null
	 * @throws IllegalArgumentException
	 *             if one of the input parameters is null or the environment has
	 *             got a null execution phase, target method or target object.
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    public ASTInterpreter(ScriptNode script, List<IPlaceholderResolver> resolverList, IEnvironment environment) throws IllegalArgumentException {
        init(script, new PlaceholderResolverSymbolTable(resolverList), environment);
    }

    /**
	 * @param script
	 *            non null.
	 * @param symbolTable
	 *            non null.
	 * @param environment
	 *            non null.
	 * @throws IllegalArgumentException
	 *             if one of the input parameters is null or the environment has
	 *             got a null execution phase, target method or target object.
	 * @since 0.0.2 (sasl-common-aspect-library)
	 */
    public ASTInterpreter(ScriptNode script, IPlaceholderResolverSymbolTable symbolTable, IEnvironment environment) throws IllegalArgumentException {
        init(script, symbolTable, environment);
    }

    /**
	 * @param script
	 *            non null.
	 * @param symbolTable
	 *            non null.
	 * @param environment
	 *            non null.
	 * @throws IllegalArgumentException
	 * @since 0.0.2 (sasl-common-aspect-library)
	 */
    private void init(ScriptNode script, IPlaceholderResolverSymbolTable symbolTable, IEnvironment environment) throws IllegalArgumentException {
        Assert.notNull(script, "Parameter script must be non null!");
        Assert.notNull(symbolTable, "Parameter symbolTable must be non null!");
        Assert.notNull(environment, "Parameter environment must be non null!");
        Assert.notNull(environment.getExecutionPhase(), "The executionPhase of the environment must be non null!");
        Assert.notNull(environment.getTargetMethod(), "The targetMethod of the environment must be non null!");
        Assert.notNull(environment.getTargetObject(), "The targetObject of the environment must be non null!");
        this.environment.setEnvironmentProperties(environment.getEnvironmentProperties());
        this.environment.setExecutionPhase(environment.getExecutionPhase());
        this.environment.setOccurredException(environment.getOccurredException());
        this.environment.setEnvironmentProperty("occuredException", this.environment.getOccurredException());
        this.environment.setTargetArguments(environment.getTargetArguments());
        this.environment.setEnvironmentProperty("targetArguments", this.environment.getTargetArguments());
        this.environment.setTargetMethod(environment.getTargetMethod());
        this.environment.setEnvironmentProperty("targetMethod", this.environment.getTargetMethod());
        this.environment.setTargetObject(environment.getTargetObject());
        this.environment.setEnvironmentProperty("targetObject", this.environment.getTargetObject());
        this.environment.setTargetReturnValue(environment.getTargetReturnValue());
        this.environment.setEnvironmentProperty("targetReturnValue", this.environment.getTargetReturnValue());
        this.scriptNode = script;
        this.placeholderSymbolTable = symbolTable;
    }

    /**
	 * Adds a shared environment variable, which could be accessed by
	 * placeholder resolvers, to the interpreter environment.
	 * 
	 * @param name
	 *            null or non null
	 * @param value
	 *            null or non null
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    public void setEnvironmentVariable(String name, Object value) {
        environment.setEnvironmentProperty(name, value);
    }

    /**
	 * @see net.sf.sasl.language.placeholder.aop.interpreter.IInterpreter#interpret()
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    public String interpret() throws InterpreterException {
        StringBuilder strBuilder = new StringBuilder();
        for (ASTNode childNode : scriptNode.getChildNodes()) {
            Object result = doInterpret(childNode);
            strBuilder.append(result);
        }
        return strBuilder.toString();
    }

    /**
	 * Runs the interpretation for the given node recursive.
	 * 
	 * @param node
	 *            non null
	 * @return null or non null
	 * @throws InterpreterException
	 */
    private Object doInterpret(ASTNode node) throws InterpreterException {
        if (node instanceof StringNode) {
            return ((StringNode) node).getTypeValue();
        }
        PlaceholderNode placeholderNode = (PlaceholderNode) node;
        String placeholderName = placeholderNode.getPlaceholderName();
        IPlaceholderResolver resolver = placeholderSymbolTable.lookUp(placeholderName);
        if (resolver == null) {
            throw new InterpreterException("Placeholder " + placeholderName + " is not defined.");
        }
        List<ASTNode> argumentList = placeholderNode.getArgumentList();
        Object[] placeholderArguments = new Object[argumentList.size()];
        int idx = 0;
        for (ASTNode argument : argumentList) {
            if (argument instanceof TypeValueNode<?>) {
                placeholderArguments[idx] = ((TypeValueNode<?>) argument).getTypeValue();
            } else {
                placeholderArguments[idx] = doInterpret(argument);
            }
            idx++;
        }
        try {
            return resolver.resolve(placeholderName, placeholderArguments, environment);
        } catch (ResolveException exception) {
            throw new InterpreterException("Exception during resolving of placeholder '" + placeholderName + "'", exception);
        }
    }
}
