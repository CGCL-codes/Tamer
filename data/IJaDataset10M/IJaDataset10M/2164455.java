package org.activiti.examples.bpmn.servicetask;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.DelegateExecution;

/**
 * Example JavaDelegate that uses an injected
 * {@link Expression}s in fields 'text1' and 'text2'. While executing, 'var1' is set with the reversed result of the
 * method invocation and 'var2' will be the reversed result of the value expression.
 * 
 * @author Frederik Heremans
 */
public class ReverseStringsFieldInjected implements JavaDelegate {

    private Expression text1;

    private Expression text2;

    public void execute(DelegateExecution execution) {
        String value1 = (String) text1.getValue(execution);
        execution.setVariable("var1", new StringBuffer(value1).reverse().toString());
        String value2 = (String) text2.getValue(execution);
        execution.setVariable("var2", new StringBuffer(value2).reverse().toString());
    }
}
