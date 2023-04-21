package net.ar.webonswing.own.validators.adapters;

import javax.swing.*;
import net.ar.guia.own.interfaces.*;
import net.ar.guia.own.validators.*;
import net.ar.webonswing.swing.components.validators.*;

public class JCompareValidatorAdapter extends JValidatorAdapter implements CompareValidator {

    public Operation getOperation() {
        return getJCompareValidator().getOperation();
    }

    private JCompareValidator getJCompareValidator() {
        return (JCompareValidator) getJValidator();
    }

    public void setComponentToCompare(VisualComponent aComponent) {
        getJCompareValidator().setComponentToCompare((JComponent) getWrappedComponent(aComponent));
    }

    public VisualComponent getComponentToCompare() {
        return getWrapper(getJCompareValidator().getComponentToValidate());
    }

    public void setOperation(Operation aOperation) {
        getJCompareValidator().setOperation(aOperation);
    }

    public Type getType() {
        return getJCompareValidator().getType();
    }

    public void setType(Type aType) {
        getJCompareValidator().setType(aType);
    }

    public Object getValueToCompare() {
        return getJCompareValidator().getValueToCompare();
    }

    public void setValueToCompare(Object aObject) {
        getJCompareValidator().setValueToCompare(aObject);
    }
}
