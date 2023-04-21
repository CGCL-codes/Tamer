package org.norecess.nolatte.primitives.function;

import org.norecess.nolatte.ast.Datum;
import org.norecess.nolatte.ast.IGroupOfData;
import org.norecess.nolatte.ast.Primitive;
import org.norecess.nolatte.environments.IEnvironment;
import org.norecess.nolatte.interpreters.IApplication;
import org.norecess.nolatte.interpreters.IInterpreter;
import org.norecess.nolatte.system.NoLatteVariables;
import org.norecess.nolatte.types.IDataTypeFilter;

public class FuncallPrimitive extends Primitive {

    private static final long serialVersionUID = 5646533476908929009L;

    private final IDataTypeFilter myDataTypeFilter;

    public FuncallPrimitive(IDataTypeFilter dataTypeFilter) {
        myDataTypeFilter = dataTypeFilter;
        getParameters().addPositional(NoLatteVariables.FUNCTION);
        getParameters().addRest(NoLatteVariables.ARGS);
    }

    public Datum apply(IInterpreter interpreter) {
        return interpreter.apply(getFunction(interpreter), getArguments(interpreter.getEnvironment()));
    }

    private IApplication getFunction(IInterpreter interpreter) {
        return interpreter.getEnvironment().get(NoLatteVariables.FUNCTION).accept(interpreter.getInterpreterFactory().createApplicationBuilder(interpreter));
    }

    private IGroupOfData getArguments(IEnvironment environment) {
        return myDataTypeFilter.getGroup(environment.get(NoLatteVariables.ARGS));
    }
}
