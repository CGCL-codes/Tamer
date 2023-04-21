package org.ccpo.operators.core;

import org.ccpo.common.api.IOperator;
import org.ccpo.logging.LogManager;

public class OperatorFactory {

    public static IOperator getOperator(String operatorClass) {
        LogManager.logDebug("OperatorFactory", "getOperator", "Initializing operator for class " + operatorClass);
        IOperator operator = null;
        try {
            Class clazz = Class.forName(operatorClass);
            Object op = clazz.newInstance();
            if (op instanceof IOperator) {
                operator = (IOperator) op;
            } else {
                LogManager.logDebug("OperatorFactory", "getOperator", operatorClass + " is not in instance of IOperator");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return operator;
    }
}
