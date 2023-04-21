package com.rapidminer.operator.preprocessing.filter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.AttributeRole;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.NonSpecialAttributesExampleSet;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.ports.metadata.AttributeMetaData;
import com.rapidminer.operator.ports.metadata.ExampleSetMetaData;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.operator.ports.metadata.MetaDataInfo;
import com.rapidminer.operator.preprocessing.AbstractDataProcessing;
import com.rapidminer.operator.tools.AttributeSubsetSelector;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.UndefinedParameterError;

/**
 * This class is for preprocessing operators, which can be restricted to use only a
 * subset of the attributes. The MetaData is changed accordingly in a way equivalent to
 * surrounding the operator with an AttributeSubsetPreprocessing operator.
 * Subclasses must overwrite the methods {@link #applyOnFiltered(ExampleSet)} and 
 * {@link #applyOnFilteredMetaData(ExampleSetMetaData)} in order to provide their
 * functionality and the correct meta data handling.
 * 
 * @author Sebastian Land
 *
 */
public abstract class AbstractFilteredDataProcessing extends AbstractDataProcessing {

    private final AttributeSubsetSelector attributeSelector = new AttributeSubsetSelector(this, getExampleSetInputPort(), getFilterValueTypes());

    ;

    public AbstractFilteredDataProcessing(OperatorDescription description) {
        super(description);
    }

    @Override
    protected final MetaData modifyMetaData(ExampleSetMetaData inputMetaData) {
        ExampleSetMetaData workingMetaData = inputMetaData.clone();
        ExampleSetMetaData subsetAmd = attributeSelector.getMetaDataSubset(workingMetaData, false);
        List<AttributeMetaData> unusedAttributes = new LinkedList<AttributeMetaData>();
        Iterator<AttributeMetaData> iterator = workingMetaData.getAllAttributes().iterator();
        while (iterator.hasNext()) {
            AttributeMetaData amd = iterator.next();
            if (!(subsetAmd.containsAttributeName(amd.getName()) == MetaDataInfo.YES)) {
                unusedAttributes.add(amd);
                iterator.remove();
            } else if (amd.isSpecial()) {
                amd.setRegular();
            }
        }
        ExampleSetMetaData resultMetaData = workingMetaData;
        try {
            resultMetaData = applyOnFilteredMetaData(workingMetaData);
        } catch (UndefinedParameterError e) {
        }
        Iterator<AttributeMetaData> r = resultMetaData.getAllAttributes().iterator();
        while (r.hasNext()) {
            AttributeMetaData newMetaData = r.next();
            AttributeMetaData oldMetaData = inputMetaData.getAttributeByName(newMetaData.getName());
            if (oldMetaData != null) {
                if (oldMetaData.isSpecial()) {
                    String specialName = oldMetaData.getRole();
                    newMetaData.setRole(specialName);
                }
            }
        }
        resultMetaData.addAllAttributes(unusedAttributes);
        return resultMetaData;
    }

    @Override
    public final ExampleSet apply(ExampleSet exampleSet) throws OperatorException {
        ExampleSet workingExampleSet = (ExampleSet) exampleSet.clone();
        Set<Attribute> selectedAttributes = attributeSelector.getAttributeSubset(workingExampleSet, false);
        List<Attribute> unusedAttributes = new LinkedList<Attribute>();
        Iterator<Attribute> iterator = workingExampleSet.getAttributes().allAttributes();
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            if (!selectedAttributes.contains(attribute)) {
                unusedAttributes.add(attribute);
                iterator.remove();
            }
        }
        workingExampleSet = new NonSpecialAttributesExampleSet(workingExampleSet);
        ExampleSet resultSet = applyOnFiltered(workingExampleSet);
        Iterator<AttributeRole> r = resultSet.getAttributes().allAttributeRoles();
        while (r.hasNext()) {
            AttributeRole newRole = r.next();
            AttributeRole oldRole = exampleSet.getAttributes().getRole(newRole.getAttribute().getName());
            if (oldRole != null) {
                if (oldRole.isSpecial()) {
                    String specialName = oldRole.getSpecialName();
                    newRole.setSpecial(specialName);
                }
            }
        }
        if (resultSet.size() != exampleSet.size()) {
            throw new UserError(this, 127, "changing the size of the example set is not allowed if the non-processed attributes should be kept.");
        }
        if (resultSet.getExampleTable().equals(exampleSet.getExampleTable())) {
            for (Attribute attribute : unusedAttributes) {
                AttributeRole role = exampleSet.getAttributes().getRole(attribute);
                resultSet.getAttributes().add(role);
            }
        } else {
            getLogger().warning("Underlying example table has changed: data copy into new table is necessary in order to keep non-processed attributes.");
            for (Attribute oldAttribute : unusedAttributes) {
                AttributeRole oldRole = exampleSet.getAttributes().getRole(oldAttribute);
                Attribute newAttribute = (Attribute) oldAttribute.clone();
                resultSet.getExampleTable().addAttribute(newAttribute);
                AttributeRole newRole = new AttributeRole(newAttribute);
                if (oldRole.isSpecial()) newRole.setSpecial(oldRole.getSpecialName());
                resultSet.getAttributes().add(newRole);
                Iterator<Example> oldIterator = exampleSet.iterator();
                Iterator<Example> newIterator = resultSet.iterator();
                while (oldIterator.hasNext()) {
                    Example oldExample = oldIterator.next();
                    Example newExample = newIterator.next();
                    newExample.setValue(newAttribute, oldExample.getValue(oldAttribute));
                }
            }
        }
        return resultSet;
    }

    /**
	 * Subclasses have to implement this method in order to operate only on the
	 * selected attributes. The results are merged back into the original example set.
	 */
    public abstract ExampleSet applyOnFiltered(ExampleSet exampleSet) throws OperatorException;

    /**
	 * This method has to be implemented in order to specify the changes of the meta data
	 * caused by the application of this operator.
	 */
    public abstract ExampleSetMetaData applyOnFilteredMetaData(ExampleSetMetaData emd) throws UndefinedParameterError;

    /**
	 * Defines the value types of the attributes which are processed or
	 * affected by this operator. Has to be overridden to restrict
	 * the attributes which can be chosen by an {@link AttributeSubsetSelector}.
	 * @return array of value types
	 */
    protected abstract int[] getFilterValueTypes();

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.addAll(attributeSelector.getParameterTypes());
        return types;
    }
}
