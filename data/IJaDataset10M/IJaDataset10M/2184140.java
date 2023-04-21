package org.decisiondeck.xmcda_oo.persist;

import java.util.List;
import org.decisiondeck.xmcda_2_0_0.Parameter;
import org.decisiondeck.xmcda_oo.exc.InvalidInputException;

public class PersistSimpleParameter {

    public org.decisiondeck.xmcda_2_0_0.Parameter writeParameterValue(String value) {
        final org.decisiondeck.xmcda_2_0_0.Parameter xmlParamValue = org.decisiondeck.xmcda_2_0_0.Parameter.Factory.newInstance();
        final org.decisiondeck.xmcda_2_0_0.Value xmlValue = xmlParamValue.addNewValue();
        xmlValue.setLabel(value);
        return xmlParamValue;
    }

    public org.decisiondeck.xmcda_2_0_0.MethodParameters writeMethodParameterValue(String value) {
        final org.decisiondeck.xmcda_2_0_0.MethodParameters xmlMethParamValue = org.decisiondeck.xmcda_2_0_0.MethodParameters.Factory.newInstance();
        xmlMethParamValue.getParameterList().add(writeParameterValue(value));
        return xmlMethParamValue;
    }

    public String readParameterStringValue(org.decisiondeck.xmcda_2_0_0.Parameter value) throws InvalidInputException {
        final org.decisiondeck.xmcda_2_0_0.Value xmlValue = value.getValue();
        if (xmlValue == null) {
            throw new InvalidInputException("Parameter value not found.");
        }
        if (!xmlValue.isSetLabel()) {
            throw new InvalidInputException("Parameter label value not found.");
        }
        return xmlValue.getLabel();
    }

    public float readParameterFloatValue(org.decisiondeck.xmcda_2_0_0.Parameter value) throws InvalidInputException {
        final org.decisiondeck.xmcda_2_0_0.Value xmlValue = value.getValue();
        if (xmlValue == null) {
            throw new InvalidInputException("Parameter value not found.");
        }
        if (!xmlValue.isSetReal()) {
            throw new InvalidInputException("Parameter float value not found.");
        }
        return xmlValue.getReal();
    }

    public String readParameterStringValue(org.decisiondeck.xmcda_2_0_0.MethodParameters value) throws InvalidInputException {
        final List<Parameter> li = value.getParameterList();
        if (li.size() > 1) {
            throw new InvalidInputException("Only one parameter expected.");
        }
        org.decisiondeck.xmcda_2_0_0.Parameter parameter = li.get(0);
        return readParameterStringValue(parameter);
    }

    public float readParameterFloatValue(org.decisiondeck.xmcda_2_0_0.MethodParameters value) throws InvalidInputException {
        final List<Parameter> li = value.getParameterList();
        if (li.size() > 1) {
            throw new InvalidInputException("Only one parameter expected.");
        }
        org.decisiondeck.xmcda_2_0_0.Parameter parameter = li.get(0);
        return readParameterFloatValue(parameter);
    }

    public org.decisiondeck.xmcda_2_0_0.MethodParameters writeMethodParameterValue(float value) {
        final org.decisiondeck.xmcda_2_0_0.MethodParameters xmlMethParamValue = org.decisiondeck.xmcda_2_0_0.MethodParameters.Factory.newInstance();
        xmlMethParamValue.getParameterList().add(writeParameterValue(value));
        return xmlMethParamValue;
    }

    public org.decisiondeck.xmcda_2_0_0.Parameter writeParameterValue(float value) {
        final org.decisiondeck.xmcda_2_0_0.Parameter xmlParamValue = org.decisiondeck.xmcda_2_0_0.Parameter.Factory.newInstance();
        final org.decisiondeck.xmcda_2_0_0.Value xmlValue = xmlParamValue.addNewValue();
        xmlValue.setReal(value);
        return xmlParamValue;
    }
}
