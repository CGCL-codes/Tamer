package org.stjs.generator.name;

import org.stjs.generator.scope.VariableWithScope;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.type.TypeWrapper;

/**
 * 
 * @author acraciun
 * 
 *         the implementors should provide the names to be written in the generated Javascript file for the given
 *         elements.
 */
public interface NameProvider {

    public String getTypeName(TypeWrapper type);

    public String getVariableName(VariableWithScope name);

    public String getMethodName(MethodWrapper method);
}
