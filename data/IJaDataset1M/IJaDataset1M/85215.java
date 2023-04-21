package com.rapidminer.operator.preprocessing.filter.attributes;

import java.util.LinkedList;
import java.util.List;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.set.ConditionCreationException;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.metadata.AttributeMetaData;
import com.rapidminer.operator.ports.metadata.MetaDataInfo;
import com.rapidminer.parameter.ParameterHandler;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeAttributes;

/**
 * A filter condition for subsets of attributes.
 * 
 * @author Tobias Malbrecht
 */
public class SubsetAttributeFilter extends AbstractAttributeFilterCondition {

    public static final String PARAMETER_ATTRIBUTES = "attributes";

    private String attributeNames;

    @Override
    public void init(ParameterHandler operator) throws UserError, ConditionCreationException {
        attributeNames = operator.getParameterAsString(PARAMETER_ATTRIBUTES);
        if ((attributeNames == null) || (attributeNames.length() == 0)) throw new UserError((operator instanceof Operator) ? (Operator) operator : null, 904, "The condition for a single attribute needs a non-empty attributes parameter string.");
    }

    public MetaDataInfo isFilteredOutMetaData(AttributeMetaData attribute, ParameterHandler handler) throws ConditionCreationException {
        if ((attributeNames == null) || (attributeNames.length() == 0)) throw new ConditionCreationException("The condition for a single attribute needs a non-empty attribute parameter string.");
        boolean found = false;
        for (String attributeName : attributeNames.split("\\|")) {
            if (attribute.getName().equals(attributeName)) {
                found = true;
            }
        }
        return found ? MetaDataInfo.NO : MetaDataInfo.YES;
    }

    public ScanResult beforeScanCheck(Attribute attribute) throws UserError {
        for (String attributeName : attributeNames.split("\\|")) {
            if (attribute.getName().equals(attributeName)) {
                return ScanResult.KEEP;
            }
        }
        return ScanResult.REMOVE;
    }

    @Override
    public List<ParameterType> getParameterTypes(ParameterHandler operator, final InputPort inPort, int... valueTypes) {
        List<ParameterType> types = new LinkedList<ParameterType>();
        ParameterType type = new ParameterTypeAttributes(PARAMETER_ATTRIBUTES, "The attribute which should be chosen.", inPort, valueTypes);
        type.setExpert(false);
        types.add(type);
        return types;
    }
}
