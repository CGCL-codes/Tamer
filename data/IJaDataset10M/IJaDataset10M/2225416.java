package com.rapidminer.operator.preprocessing.filter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.AttributeRole;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.SimpleAttributes;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.ViewAttribute;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.preprocessing.PreprocessingModel;
import com.rapidminer.tools.Ontology;
import com.rapidminer.tools.Tools;
import com.rapidminer.tools.container.Pair;

/**
 * The model class for the {@link NominalToNumericModel} operator. Can either
 * transform nominals to numeric by simply replacing the nominal values by the
 * respective integer mapping, or by using effect coding or dummy coding.
 * 
 * @author Marius Helf
 */
class NominalToNumericModel extends PreprocessingModel {

    private static final long serialVersionUID = -4203775081616082145L;

    private int codingType;

    /**
		 * maps a target attribute to the value for which it becomes one (for dummy coding)
		 */
    private Map<String, Double> attributeTo1ValueMap = null;

    /**
		 * maps a target attribute to the value for which it becomes -1 or 1 respectively (for effect coding).
		 * The first value of the pair is the value for 1, the second for -1. 
		 */
    private Map<String, Pair<Double, Double>> attributeToValuesMap = null;

    /**
		 * maps an original attribute name to the list of all its values which occurred in the training data.
		 * Used for dummy and effect coding. 
		 */
    private Map<String, List<String>> attributeToAllNominalValues = null;

    /**
		 * maps source attributes to their comparison group.
		 */
    private Map<String, Double> sourceAttributeToComparisonGroupMap = null;

    /**
		 * maps target attributes in the output set to their respective source attributes in the training set.
		 */
    private Map<String, String> targetAttributeToSourceAttributeMap = null;

    /**
		 * maps source attributes to the comparison group string.
		 * Only used with dummy/effect coding.
		 * 
		 * This map is *only* used for displaying the model (i.e. in toResultString()). 
		 */
    private Map<String, String> sourceAttributeToComparisonGroupStringsMap = null;

    /**
		 * Relevant only when using dummy coding or effect coding.
		 * 
		 * If true, the naming scheme for target attributes is "sourceAttribute_value",
		 * if false, "sourceAttribute = value"
		 */
    private boolean useUnderscoreInName = false;

    private boolean useComparisonGroups = false;

    private int unexpectedValueHandling = NominalToNumeric.ALL_ZEROES_AND_WARNING;

    /**
		 * Constructs a new model. Use this ctor to create a model for value encoding.
		 * @param exampleSet
		 * @param codingType the coding type. Should be NominalToNumeric.INTEGERS when called manually. 
		 */
    protected NominalToNumericModel(ExampleSet exampleSet, int codingType) {
        super(exampleSet);
        this.codingType = codingType;
    }

    /**
		 * Constructs a new model. Use this ctor to create a model for dummy encoding or effect encoding.
		 * @param exampleSet
		 * @param codingType the coding type. Should be NominalToNumeric.EFFECT_CODING or DUMMY_CODING. 
		 * @param useUnderscoreInName @see NominalToNumericModel#useUnderscoreInName
		 * @param sourceAttributeToComparisonGroupMap @see NominalToNumericModel#sourceAttributeToComparisonGroupMap  @see NominalToNumeric#getSourceAttributeToComparisonGroupMap
		 * @param attributeTo1ValueMap @see NominalToNumericModel#attributeTo1ValueMap. Must be non-null for dummy coding, should be null for effect coding. @see NominalToNumeric#getAttributeTo1ValueMap
		 * @param attributeToValuesMap @see NominalToNumericModel#attributeToValuesMap. Must be non-null for effect coding, should be null for dummy coding. @see NominalToNumeric#getAttributeToValuesMap
		 * @param useComparisonGroup Indicates if comparison groups for dummy coding should be used. Is ignored if codingType == EFFECT_CODING.
		 * @param unexpectedValueHandling Defines how unexpected values are handled. @see NominalToNumericModel#unexpectedValueHandling.
		 */
    protected NominalToNumericModel(ExampleSet exampleSet, int codingType, boolean useUnderscoreInName, Map<String, Double> sourceAttributeToComparisonGroupMap, Map<String, Double> attributeTo1ValueMap, Map<String, Pair<Double, Double>> attributeToValuesMap, boolean useComparisonGroups, int unexpectedValueHandling) {
        this(exampleSet, codingType);
        this.useUnderscoreInName = useUnderscoreInName;
        this.sourceAttributeToComparisonGroupMap = sourceAttributeToComparisonGroupMap;
        this.attributeTo1ValueMap = attributeTo1ValueMap;
        this.attributeToValuesMap = attributeToValuesMap;
        this.useComparisonGroups = useComparisonGroups || (codingType == NominalToNumeric.EFFECT_CODING);
        this.unexpectedValueHandling = unexpectedValueHandling;
        if (useComparisonGroups) {
            assert (sourceAttributeToComparisonGroupMap != null);
            sourceAttributeToComparisonGroupStringsMap = new LinkedHashMap<String, String>();
            for (Map.Entry<String, Double> entry : sourceAttributeToComparisonGroupMap.entrySet()) {
                String attributeName = entry.getKey();
                double comparisonGroup = entry.getValue();
                Attribute attribute = exampleSet.getAttributes().get(attributeName);
                String comparisonGroupString = attribute.getMapping().mapIndex((int) comparisonGroup);
                sourceAttributeToComparisonGroupStringsMap.put(attributeName, comparisonGroupString);
            }
        }
        if (codingType == NominalToNumeric.DUMMY_CODING || codingType == NominalToNumeric.EFFECT_CODING) {
            attributeToAllNominalValues = new HashMap<String, List<String>>();
            for (Attribute attribute : exampleSet.getAttributes()) {
                if (!attribute.isNumerical()) {
                    String attributeName = attribute.getName();
                    List<String> values = new LinkedList<String>();
                    for (String value : attribute.getMapping().getValues()) {
                        values.add(value);
                    }
                    attributeToAllNominalValues.put(attributeName, values);
                }
            }
            targetAttributeToSourceAttributeMap = new HashMap<String, String>();
            for (Attribute sourceAttribute : exampleSet.getAttributes()) {
                if (!sourceAttribute.isNumerical()) {
                    String sourceAttributeName = sourceAttribute.getName();
                    for (String targetAttribute : getTargetAttributesFromSourceAttribute(sourceAttribute)) {
                        targetAttributeToSourceAttributeMap.put(targetAttribute, sourceAttributeName);
                    }
                }
            }
        }
    }

    @Override
    public ExampleSet applyOnData(ExampleSet exampleSet) throws OperatorException {
        switch(codingType) {
            case NominalToNumeric.INTEGERS_CODING:
                return applyOnDataIntegers(exampleSet);
            case NominalToNumeric.DUMMY_CODING:
                return applyOnDataDummyCoding(exampleSet, false);
            case NominalToNumeric.EFFECT_CODING:
                return applyOnDataDummyCoding(exampleSet, true);
            default:
                assert (false);
                return null;
        }
    }

    /**
		 * Returns a list containing the names of those attributes which will
		 * represent the coding of the given source attribute.
		 */
    private List<String> getTargetAttributesFromSourceAttribute(Attribute sourceAttribute) {
        List<String> targetNames = new LinkedList<String>();
        double comparisonGroup = -1;
        if (useComparisonGroups) {
            comparisonGroup = sourceAttributeToComparisonGroupMap.get(sourceAttribute.getName());
        }
        List<String> originalAttributeValues = attributeToAllNominalValues.get(sourceAttribute.getName());
        String comparisonGroupValue = null;
        if (comparisonGroup != -1) {
            comparisonGroupValue = originalAttributeValues.get((int) comparisonGroup);
        }
        for (String currentValue : originalAttributeValues) {
            if ((!useComparisonGroups) || (!currentValue.equals(comparisonGroupValue))) {
                targetNames.add(NominalToNumeric.getTargetAttributeName(sourceAttribute.getName(), currentValue, useUnderscoreInName));
            }
        }
        return targetNames;
    }

    /**
		 * Creates a dummy coding or effect coding from the given example set.
		 * @param effectCoding If true, the function does effect coding. If false, dummy coding. 
		 */
    private ExampleSet applyOnDataDummyCoding(ExampleSet exampleSet, boolean effectCoding) {
        LinkedList<Attribute> nominalAttributes = new LinkedList<Attribute>();
        LinkedList<Attribute> transformedAttributes = new LinkedList<Attribute>();
        for (Attribute attribute : exampleSet.getAttributes()) {
            if (!attribute.isNumerical()) {
                nominalAttributes.add(attribute);
                List<String> targetNames = getTargetAttributesFromSourceAttribute(attribute);
                for (String targetName : targetNames) {
                    transformedAttributes.add(AttributeFactory.createAttribute(targetName, Ontology.INTEGER));
                }
            }
        }
        exampleSet.getExampleTable().addAttributes(transformedAttributes);
        for (Attribute attribute : transformedAttributes) {
            exampleSet.getAttributes().addRegular(attribute);
        }
        for (Example example : exampleSet) {
            for (Attribute nominalAttribute : nominalAttributes) {
                double sourceValue = example.getValue(nominalAttribute);
                for (String targetName : getTargetAttributesFromSourceAttribute(nominalAttribute)) {
                    Attribute targetAttribute = exampleSet.getAttributes().get(targetName);
                    example.setValue(targetAttribute, getValue(targetAttribute, sourceValue));
                }
            }
        }
        for (Attribute nominalAttribute : nominalAttributes) {
            exampleSet.getAttributes().remove(nominalAttribute);
        }
        return exampleSet;
    }

    /**
		 * Transforms the numerical attributes to integer values (corresponding to the internal mapping).
		 */
    private ExampleSet applyOnDataIntegers(ExampleSet exampleSet) {
        LinkedList<Attribute> nominalAttributes = new LinkedList<Attribute>();
        LinkedList<Attribute> transformedAttributes = new LinkedList<Attribute>();
        for (Attribute attribute : exampleSet.getAttributes()) {
            if (!attribute.isNumerical()) {
                nominalAttributes.add(attribute);
                transformedAttributes.add(AttributeFactory.createAttribute(attribute.getName(), Ontology.NUMERICAL));
            }
        }
        exampleSet.getExampleTable().addAttributes(transformedAttributes);
        for (Example example : exampleSet) {
            Iterator<Attribute> target = transformedAttributes.iterator();
            for (Attribute attribute : nominalAttributes) {
                example.setValue(target.next(), example.getValue(attribute));
            }
        }
        Attributes attributes = exampleSet.getAttributes();
        for (Attribute attribute : exampleSet.getAttributes()) {
            if (!attribute.isNumerical()) attributes.replace(attribute, transformedAttributes.poll());
        }
        return exampleSet;
    }

    @Override
    public Attributes getTargetAttributes(ExampleSet parentSet) {
        SimpleAttributes attributes = new SimpleAttributes();
        Iterator<AttributeRole> specialRoles = parentSet.getAttributes().specialAttributes();
        while (specialRoles.hasNext()) {
            attributes.add(specialRoles.next());
        }
        for (Attribute attribute : parentSet.getAttributes()) {
            if (!attribute.isNumerical()) {
                if (codingType == NominalToNumeric.EFFECT_CODING || codingType == NominalToNumeric.DUMMY_CODING) {
                    double comparisonGroup = -1;
                    if (useComparisonGroups) {
                        comparisonGroup = sourceAttributeToComparisonGroupMap.get(attribute.getName());
                    }
                    List<String> valueList = attributeToAllNominalValues.get(attribute.getName());
                    if (valueList != null) {
                        int currentValue = 0;
                        for (String attributeValue : valueList) {
                            if (currentValue != comparisonGroup) {
                                ViewAttribute viewAttribute = new ViewAttribute(this, attribute, NominalToNumeric.getTargetAttributeName(attribute.getName(), attributeValue, useUnderscoreInName), Ontology.INTEGER, null);
                                attributes.addRegular(viewAttribute);
                            }
                            ++currentValue;
                        }
                    }
                } else if (codingType == NominalToNumeric.INTEGERS_CODING) {
                    attributes.addRegular(new ViewAttribute(this, attribute, attribute.getName(), Ontology.INTEGER, null));
                } else {
                    assert (false);
                }
            } else {
                attributes.addRegular(attribute);
            }
        }
        return attributes;
    }

    @Override
    public double getValue(Attribute targetAttribute, double value) {
        if (codingType == NominalToNumeric.DUMMY_CODING) {
            String targetName = targetAttribute.getName();
            Double oneValue = attributeTo1ValueMap.get(targetName);
            if (oneValue != null && oneValue == value) {
                return 1;
            } else {
                if (unexpectedValueHandling != NominalToNumeric.ALL_ZEROES_AND_NO_WARNING && !isValueInTrainingSet(targetAttribute, value)) {
                    handleUnexpectedValue(targetName);
                }
                return 0;
            }
        } else if (codingType == NominalToNumeric.EFFECT_CODING) {
            String targetName = targetAttribute.getName();
            Pair<Double, Double> storedValue = attributeToValuesMap.get(targetName);
            if (storedValue.getFirst() == value) {
                return 1;
            } else if (storedValue.getSecond() == value) {
                return -1;
            } else {
                if (unexpectedValueHandling != NominalToNumeric.ALL_ZEROES_AND_NO_WARNING && !isValueInTrainingSet(targetAttribute, value)) {
                    handleUnexpectedValue(targetName);
                }
                return 0;
            }
        } else if (codingType == NominalToNumeric.INTEGERS_CODING) {
            return value;
        } else {
            assert (false);
            return Double.NaN;
        }
    }

    private int handleUnexpectedValue(String targetName) {
        switch(unexpectedValueHandling) {
            case NominalToNumeric.ALL_ZEROES_AND_WARNING:
                getLog().logWarning("unexpected value during application of Nominal to Numerical Model for attribute '" + targetName + "'. Setting to 0.");
                return 0;
            case NominalToNumeric.ALL_ZEROES_AND_NO_WARNING:
                return 0;
            default:
                assert (false);
                return 0;
        }
    }

    private boolean isValueInTrainingSet(Attribute targetAttribute, double value) {
        String sourceAttribute = targetAttributeToSourceAttributeMap.get(targetAttribute.getName());
        if (sourceAttribute != null) {
            List<String> trainingValues = attributeToAllNominalValues.get(sourceAttribute);
            if (trainingValues != null) {
                int valueCount = trainingValues.size();
                if (value >= valueCount) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getName() {
        return "Nominal2Numerical Model";
    }

    @Override
    public String toResultString() {
        StringBuilder builder = new StringBuilder();
        Attributes trainAttributes = getTrainingHeader().getAttributes();
        builder.append(getName() + Tools.getLineSeparators(2));
        String codingTypeString = "";
        switch(codingType) {
            case NominalToNumeric.INTEGERS_CODING:
                codingTypeString = "unique integers";
                break;
            case NominalToNumeric.DUMMY_CODING:
                codingTypeString = "dummy coding";
                break;
            case NominalToNumeric.EFFECT_CODING:
                codingTypeString = "effect coding";
                break;
        }
        builder.append("Coding Type: " + codingTypeString + Tools.getLineSeparator());
        if (codingType == NominalToNumeric.INTEGERS_CODING) {
            builder.append("Model covering " + trainAttributes.size() + " attributes:" + Tools.getLineSeparator());
            for (Attribute attribute : trainAttributes) {
                builder.append(" - " + attribute.getName() + Tools.getLineSeparator());
            }
        } else {
            builder.append("Model covering " + trainAttributes.size() + " attributes (with comparison group):" + Tools.getLineSeparator());
            for (Attribute attribute : trainAttributes) {
                builder.append(" - " + attribute.getName() + " ('" + sourceAttributeToComparisonGroupStringsMap.get(attribute.getName()) + "')" + Tools.getLineSeparator());
            }
        }
        return builder.toString();
    }
}
