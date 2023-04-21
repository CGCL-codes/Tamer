package org.peaseplate.domain.lang.command;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.peaseplate.BuildContext;
import org.peaseplate.TemplateLocator;
import org.peaseplate.TemplateRuntimeException;
import org.peaseplate.domain.util.ReflectionException;
import org.peaseplate.domain.util.ReflectionUtils;

/**
 * This command does the whole magic. It invokes the method
 * that matches the identifier and the parameters. Since the 
 * language is not strictly type, this may be complicated
 * some times.
 * 
 * Any method that has a name similar to the identifier,
 * or the identifier prefixed with "get", "is" or "set"
 * (the case is ignored) will be taken into consideration.
 * 
 * Next it checks the number (not the type) of the parameters.
 * 
 * Hopefully this results in just one method. If there are still 
 * multiple methods with name and the same parameter count,
 * it searches for the method that most exactly fits the types
 * of the parameters (this may vary each time the command is
 * called, because the language is not strictly typed).
 * 
 * To get this whole stuff acceptable fast, it stores all possible
 * methods in a list the first time the command is invoked.
 * 
 * Due to the fact, that the language is not strictly typed, it
 * even is possible that the class may change on which
 * this command is invoked. Thus the list with the commands is
 * stored in a hash map with the class as key.
 */
public class QueryCommand extends AbstractObjectCallCommand implements ICommand {

    private final ICommand identifierCommand;

    public QueryCommand(TemplateLocator locator, int line, int column, ICommand command, ICommand identifierCommand, ICommand[] parameterCommands) {
        super(locator, line, column, command, parameterCommands);
        this.identifierCommand = identifierCommand;
    }

    /**
	 * @see org.peaseplate.domain.lang.command.ICommand#call(BuildContext)
	 */
    public Object call(BuildContext context) throws TemplateRuntimeException {
        Object workingObject = callCommand(context);
        if (workingObject == null) return null;
        return callObject(context, workingObject, identifierCommand.call(context));
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractObjectCallCommand#callObject(org.peaseplate.BuildContext, java.lang.Object, java.lang.Object)
	 */
    @Override
    protected Object callObject(BuildContext context, Object onObject, Object identifier) throws TemplateRuntimeException {
        if (onObject == null) return null;
        if (onObject instanceof Map) return callMap(context, (Map<?, ?>) onObject, identifier); else if ((onObject instanceof List) && (identifier instanceof Number)) return callList((List<?>) onObject, ((Number) identifier).intValue()); else if ((onObject.getClass().isArray()) && (identifier instanceof Number)) return callArray((Object[]) onObject, ((Number) identifier).intValue());
        return callFieldOrMethod(context, onObject, String.valueOf(identifier));
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractNativeCallCommand#getField(java.lang.Class, java.lang.String)
	 */
    @Override
    protected Field getField(Class<?> clazz, String identifier) throws TemplateRuntimeException {
        return findField(clazz, identifier);
    }

    /**
	 * @see org.peaseplate.domain.lang.command.AbstractObjectCallCommand#getMethod(java.lang.Class, java.lang.String, int)
	 */
    @Override
    protected Method getMethod(Class<?> clazz, String identifier, int numberOfParameters) throws TemplateRuntimeException {
        try {
            return ReflectionUtils.findMethod(clazz, identifier, numberOfParameters);
        } catch (ReflectionException e) {
            throw new TemplateRuntimeException(getLocator(), getLine(), getColumn(), "Could not find method \"" + identifier + "\" in " + clazz, e);
        }
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (getCommand() != null) result.append(getCommand());
        result.append("[").append(identifierCommand).append("]");
        result.append(super.toString());
        return result.toString();
    }
}
