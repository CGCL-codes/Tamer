package org.modelingvalue.modelsync.expressions;

import org.modelingvalue.modelsync.elements.Variable;
import org.modelingvalue.modelsync.elements.VariableBinding;
import org.modelingvalue.modelsync.messages.RuntimeMessageHandler;
import org.modelingvalue.modelsync.models.*;

/**
 * @author Wim Bast
 *
 */
public interface ContextBinding {

    /**
	 * @param variable the variable
	 * @return the value of a variable
	 */
    VariableBinding<?> getVariableBinding(VariableCall<? extends Variable> variableCall);

    Object evaluateFunction(FunctionCall<? extends Expression> functionCall, Object contextObject, Object... arguments);

    /**
	 * @return the model type
	 */
    OpaqueModel getModel();

    /**
	 * @return the message handler
	 */
    RuntimeMessageHandler getMessageHandler();
}
