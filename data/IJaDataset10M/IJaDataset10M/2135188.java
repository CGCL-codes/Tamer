package com.rapidminer.operator.preprocessing.filter;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ProcessSetupError.Severity;
import com.rapidminer.operator.SimpleProcessSetupError;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.annotation.ResourceConsumptionEstimator;
import com.rapidminer.operator.ports.metadata.AttributeMetaData;
import com.rapidminer.operator.ports.metadata.ExampleSetMetaData;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.operator.preprocessing.AbstractDataProcessing;
import com.rapidminer.operator.tools.AttributeSubsetSelector;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeRegexp;
import com.rapidminer.parameter.ParameterTypeString;
import com.rapidminer.parameter.UndefinedParameterError;
import com.rapidminer.tools.OperatorResourceConsumptionHandler;

/**
 * <p>
 * This operator replaces parts of the attribute names (like whitespaces, parentheses, or other unwanted characters) by
 * a specified replacement. The replace_what parameter can be defined as a regular expression (please refer to the annex
 * of the RapidMiner tutorial for a description). The replace_by parameter can be defined as an arbitrary string. Empty
 * strings are also allowed. Capturing groups of the defined regular expression can be accessed with $1, $2, $3...
 * </p>
 * 
 * @author Ingo Mierswa, Tobias Malbrecht
 */
public class ChangeAttributeNamesReplace extends AbstractDataProcessing {

    public static final String PARAMETER_REPLACE_WHAT = "replace_what";

    public static final String PARAMETER_REPLACE_BY = "replace_by";

    private final AttributeSubsetSelector attributeSelector = new AttributeSubsetSelector(this, getExampleSetInputPort());

    public ChangeAttributeNamesReplace(OperatorDescription description) {
        super(description);
    }

    @Override
    protected MetaData modifyMetaData(ExampleSetMetaData exampleSetMetaData) {
        try {
            ExampleSetMetaData subsetMetaData = attributeSelector.getMetaDataSubset(exampleSetMetaData, false);
            Pattern replaceWhatPattern = Pattern.compile(getParameterAsString(PARAMETER_REPLACE_WHAT));
            String replaceByString = isParameterSet(PARAMETER_REPLACE_BY) ? getParameterAsString(PARAMETER_REPLACE_BY) : "";
            for (AttributeMetaData attributeMetaData : subsetMetaData.getAllAttributes()) {
                String name = attributeMetaData.getName();
                exampleSetMetaData.getAttributeByName(name).setName(replaceWhatPattern.matcher(name).replaceAll(replaceByString));
            }
        } catch (UndefinedParameterError e) {
        } catch (IndexOutOfBoundsException e) {
            addError(new SimpleProcessSetupError(Severity.ERROR, getPortOwner(), "capturing_group_undefined", PARAMETER_REPLACE_BY, PARAMETER_REPLACE_WHAT));
        }
        return exampleSetMetaData;
    }

    @Override
    public ExampleSet apply(ExampleSet exampleSet) throws OperatorException {
        Set<Attribute> attributeSubset = attributeSelector.getAttributeSubset(exampleSet, false);
        Pattern replaceWhatPattern = Pattern.compile(getParameterAsString(PARAMETER_REPLACE_WHAT));
        String replaceByString = isParameterSet(PARAMETER_REPLACE_BY) ? getParameterAsString(PARAMETER_REPLACE_BY) : "";
        try {
            for (Attribute attribute : attributeSubset) {
                attribute.setName(replaceWhatPattern.matcher(attribute.getName()).replaceAll(replaceByString));
            }
        } catch (IndexOutOfBoundsException e) {
            throw new UserError(this, 215, replaceByString, PARAMETER_REPLACE_WHAT);
        }
        return exampleSet;
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.addAll(attributeSelector.getParameterTypes());
        ParameterType type = new ParameterTypeRegexp(PARAMETER_REPLACE_WHAT, "A regular expression defining what should be replaced in the attribute names.", "\\W");
        type.setShowRange(false);
        type.setExpert(false);
        types.add(type);
        types.add(new ParameterTypeString(PARAMETER_REPLACE_BY, "This string is used as replacement for all parts of the matching attributes where the parameter '" + PARAMETER_REPLACE_WHAT + "' matches.", true, false));
        return types;
    }

    @Override
    public boolean writesIntoExistingData() {
        return false;
    }

    @Override
    public ResourceConsumptionEstimator getResourceConsumptionEstimator() {
        return OperatorResourceConsumptionHandler.getResourceConsumptionEstimator(getInputPort(), ChangeAttributeNamesReplace.class, attributeSelector);
    }
}
