package com.gwtent.client.ui.validate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExpressionProcessorRegister {

    private List validates = new ArrayList();

    private ExpressionProcessorRegister() {
    }

    private static ExpressionProcessorRegister register = null;

    public static ExpressionProcessorRegister getInstance() {
        if (register == null) register = new ExpressionProcessorRegister();
        return register;
    }

    public void registValidator(ExpressionProcessor validateProcessor) {
        validates.add(validateProcessor);
    }

    public ExpressionProcessor findProcessor(String expression) {
        Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            ExpressionProcessor processor = (ExpressionProcessor) iterator.next();
            if (processor.canProcess(expression)) {
                return processor;
            }
        }
        return null;
    }

    public Iterator iterator() {
        return validates.iterator();
    }
}
