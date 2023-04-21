package com.rapidminer.operator.preprocessing.transformation.aggregation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DataRowFactory;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.OperatorCreationException;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.OperatorVersion;
import com.rapidminer.operator.ProcessSetupError.Severity;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.annotation.ResourceConsumptionEstimator;
import com.rapidminer.operator.ports.metadata.AttributeMetaData;
import com.rapidminer.operator.ports.metadata.ExampleSetMetaData;
import com.rapidminer.operator.ports.metadata.MDInteger;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.operator.ports.metadata.SetRelation;
import com.rapidminer.operator.ports.metadata.SimpleMetaDataError;
import com.rapidminer.operator.preprocessing.AbstractDataProcessing;
import com.rapidminer.operator.preprocessing.filter.ExampleFilter;
import com.rapidminer.operator.preprocessing.filter.NumericToNominal;
import com.rapidminer.operator.preprocessing.filter.NumericToPolynominal;
import com.rapidminer.operator.preprocessing.filter.attributes.RegexpAttributeFilter;
import com.rapidminer.operator.tools.AttributeSubsetSelector;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeAttribute;
import com.rapidminer.parameter.ParameterTypeAttributes;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeList;
import com.rapidminer.parameter.ParameterTypeStringCategory;
import com.rapidminer.parameter.UndefinedParameterError;
import com.rapidminer.parameter.conditions.BooleanParameterCondition;
import com.rapidminer.tools.Ontology;
import com.rapidminer.tools.OperatorResourceConsumptionHandler;
import com.rapidminer.tools.OperatorService;

/**
 * <p>
 * This operator creates a new example set from the input example set showing the results of arbitrary aggregation functions (as SUM, COUNT
 * etc. known from SQL). Before the values of different rows are aggregated into a new row the rows might be grouped by the values of a
 * multiple attributes (similar to the group-by clause known from SQL). In this case a new line will be created for each group.
 * </p>
 * 
 * <p>
 * Please note that the known HAVING clause from SQL can be simulated by an additional {@link ExampleFilter} operator following this one.
 * </p>
 * 
 * @author Tobias Malbrecht, Ingo Mierswa, Sebastian Land
 */
public class AggregationOperator extends AbstractDataProcessing {

    public static class AggregationTreeNode {

        private TreeMap<Object, AggregationTreeNode> childrenMap = null;

        private TreeMap<Object, LeafAggregationTreeNode> leafMap = null;

        public AggregationTreeNode getOrCreateChild(Object value) {
            if (childrenMap == null) {
                childrenMap = new TreeMap<Object, AggregationTreeNode>();
            }
            AggregationTreeNode childNode = childrenMap.get(value);
            if (childNode == null) {
                childNode = new AggregationTreeNode();
                childrenMap.put(value, childNode);
            }
            return childNode;
        }

        public AggregationTreeNode getChild(Object value) {
            if (childrenMap != null) {
                return childrenMap.get(value);
            }
            return null;
        }

        public Set<Entry<Object, AggregationTreeNode>> getChilds() {
            return childrenMap.entrySet();
        }

        public LeafAggregationTreeNode getOrCreateLeaf(Object value, List<AggregationFunction> aggregationFunctions) {
            if (leafMap == null) {
                leafMap = new TreeMap<Object, LeafAggregationTreeNode>();
            }
            LeafAggregationTreeNode leafNode = leafMap.get(value);
            if (leafNode == null) {
                leafNode = new LeafAggregationTreeNode(aggregationFunctions);
                leafMap.put(value, leafNode);
            }
            return leafNode;
        }

        public LeafAggregationTreeNode getLeaf(Object value) {
            if (leafMap != null) {
                return leafMap.get(value);
            }
            return null;
        }

        public Set<Entry<Object, LeafAggregationTreeNode>> getLeaves() {
            return leafMap.entrySet();
        }

        public Collection<? extends Object> getValues() {
            if (childrenMap != null) return childrenMap.keySet();
            if (leafMap != null) return leafMap.keySet();
            return Collections.emptyList();
        }
    }

    public static class LeafAggregationTreeNode {

        private List<Aggregator> aggregators;

        /**
         * Creates a new {@link LeafAggregationTreeNode} for all the given {@link AggregationFunction}s.
         * For each function, one {@link Aggregator} will be created, that will keep track of the current
         * counted values.
         */
        public LeafAggregationTreeNode(List<AggregationFunction> aggregationFunctions) {
            aggregators = new ArrayList<Aggregator>(aggregationFunctions.size());
            for (AggregationFunction function : aggregationFunctions) {
                aggregators.add(function.createAggregator());
            }
        }

        /**
         * This will count the given examples for all registered {@link Aggregator}s.
         */
        public void count(Example example) {
            for (Aggregator aggregator : aggregators) {
                aggregator.count(example);
            }
        }

        /**
         * This will count the given examples for all registered {@link Aggregator}s with the given weight.
         * If there's no weight attribute available, it is preferable to use the {@link #count(Example)} method,
         * as it might be more efficiently implemented.
         */
        public void count(Example example, double weight) {
            for (Aggregator aggregator : aggregators) {
                aggregator.count(example, weight);
            }
        }

        /**
         * This simply returns the list of all aggregators. They may be used for setting values within
         * the respective data row of the created example set.
         */
        public List<Aggregator> getAggregators() {
            return aggregators;
        }
    }

    public static final String PARAMETER_USE_DEFAULT_AGGREGATION = "use_default_aggregation";

    public static final String PARAMETER_DEFAULT_AGGREGATION_FUNCTION = "default_aggregation_function";

    public static final String PARAMETER_AGGREGATION_ATTRIBUTES = "aggregation_attributes";

    public static final String PARAMETER_AGGREGATION_FUNCTIONS = "aggregation_functions";

    public static final String PARAMETER_GROUP_BY_ATTRIBUTES = "group_by_attributes";

    public static final String PARAMETER_ONLY_DISTINCT = "only_distinct";

    public static final String PARAMETER_IGNORE_MISSINGS = "ignore_missings";

    public static final String PARAMETER_ALL_COMBINATIONS = "count_all_combinations";

    public static final String GENERIC_GROUP_NAME = "group";

    public static final String GENERIC_ALL_NAME = "all";

    private static final OperatorVersion VERSION_5_1_6 = new OperatorVersion(5, 1, 6);

    private final AttributeSubsetSelector attributeSelector = new AttributeSubsetSelector(this, getExampleSetInputPort());

    public AggregationOperator(OperatorDescription desc) {
        super(desc);
    }

    @Override
    protected MetaData modifyMetaData(ExampleSetMetaData metaData) throws UndefinedParameterError {
        ExampleSetMetaData resultMD = metaData.clone();
        resultMD.clear();
        if (isParameterSet(PARAMETER_GROUP_BY_ATTRIBUTES) && !getParameterAsString(PARAMETER_GROUP_BY_ATTRIBUTES).isEmpty()) {
            String attributeRegex = getParameterAsString(PARAMETER_GROUP_BY_ATTRIBUTES);
            Pattern pattern = Pattern.compile(attributeRegex);
            for (AttributeMetaData amd : metaData.getAllAttributes()) {
                if (pattern.matcher(amd.getName()).matches()) {
                    if (amd.isNumerical() && getCompatibilityLevel().isAtMost(VERSION_5_1_6)) {
                        amd.setType(Ontology.NOMINAL);
                        amd.setValueSet(Collections.<String>emptySet(), SetRelation.SUPERSET);
                    }
                    resultMD.addAttribute(amd);
                }
            }
            resultMD.getNumberOfExamples().reduceByUnknownAmount();
        }
        if (resultMD.getAllAttributes().isEmpty() && getCompatibilityLevel().isAtMost(VERSION_5_1_6)) {
            AttributeMetaData allGroup = new AttributeMetaData(GENERIC_GROUP_NAME, Ontology.NOMINAL);
            Set<String> values = new TreeSet<String>();
            values.add(GENERIC_ALL_NAME);
            allGroup.setValueSet(values, SetRelation.EQUAL);
            resultMD.addAttribute(allGroup);
            resultMD.setNumberOfExamples(new MDInteger(1));
        }
        List<String[]> parameterList = this.getParameterList(PARAMETER_AGGREGATION_ATTRIBUTES);
        HashSet<String> explicitDefinedAttributes = new HashSet<String>();
        for (String[] function : parameterList) {
            explicitDefinedAttributes.add(function[0]);
        }
        if (getParameterAsBoolean(PARAMETER_USE_DEFAULT_AGGREGATION)) {
            String defaultFunction = getParameterAsString(PARAMETER_DEFAULT_AGGREGATION_FUNCTION);
            ExampleSetMetaData metaDataSubset = attributeSelector.getMetaDataSubset(metaData, false);
            for (AttributeMetaData amd : metaDataSubset.getAllAttributes()) {
                if (!explicitDefinedAttributes.contains(amd.getName())) {
                    AttributeMetaData newAMD = AggregationFunction.getAttributeMetaData(defaultFunction, amd, getExampleSetInputPort());
                    if (newAMD != null) resultMD.addAttribute(newAMD);
                }
            }
        }
        for (String[] function : parameterList) {
            AttributeMetaData amd = metaData.getAttributeByName(function[0]);
            if (amd != null) {
                AttributeMetaData newMD = AggregationFunction.getAttributeMetaData(function[1], amd, getExampleSetInputPort());
                if (newMD != null) resultMD.addAttribute(newMD);
            } else {
                getExampleSetInputPort().addError(new SimpleMetaDataError(Severity.WARNING, getExampleSetInputPort(), "aggregation.attribute_unknown", function[0]));
                AttributeMetaData newAMD = AggregationFunction.getAttributeMetaData(function[1], new AttributeMetaData(function[0], Ontology.ATTRIBUTE_VALUE), getExampleSetInputPort());
                if (newAMD != null) resultMD.addAttribute(newAMD);
            }
        }
        return resultMD;
    }

    @Override
    public ExampleSet apply(ExampleSet exampleSet) throws OperatorException {
        List<AggregationFunction> aggregationFunctions = createAggreationFunctions(exampleSet);
        Attribute[] groupAttributes = getMatchingAttributes(exampleSet.getAttributes(), getParameterAsString(PARAMETER_GROUP_BY_ATTRIBUTES));
        Attribute weightAttribute = exampleSet.getAttributes().getWeight();
        boolean useWeights = weightAttribute != null;
        AggregationTreeNode rootNode = new AggregationTreeNode();
        LeafAggregationTreeNode leafNode = null;
        if (groupAttributes.length == 0) {
            leafNode = new LeafAggregationTreeNode(aggregationFunctions);
        }
        for (Example example : exampleSet) {
            if (groupAttributes.length > 0) {
                AggregationTreeNode currentNode = rootNode;
                for (int i = 0; i < groupAttributes.length - 1; i++) {
                    Attribute currentAttribute = groupAttributes[i];
                    if (currentAttribute.isNominal()) {
                        currentNode = currentNode.getOrCreateChild(example.getValueAsString(currentAttribute));
                    } else {
                        currentNode = currentNode.getOrCreateChild(example.getValue(currentAttribute));
                    }
                }
                Attribute currentAttribute = groupAttributes[groupAttributes.length - 1];
                if (currentAttribute.isNominal()) {
                    leafNode = currentNode.getOrCreateLeaf(example.getValueAsString(currentAttribute), aggregationFunctions);
                } else {
                    leafNode = currentNode.getOrCreateLeaf(example.getValue(currentAttribute), aggregationFunctions);
                }
            }
            if (!useWeights) leafNode.count(example); else leafNode.count(example, example.getValue(weightAttribute));
        }
        boolean isCountingAllCombinations = getParameterAsBoolean(PARAMETER_ALL_COMBINATIONS);
        Attribute[] newAttributes = new Attribute[groupAttributes.length + aggregationFunctions.size()];
        for (int i = 0; i < groupAttributes.length; i++) {
            newAttributes[i] = AttributeFactory.createAttribute(groupAttributes[i]);
        }
        int i = groupAttributes.length;
        for (AggregationFunction function : aggregationFunctions) {
            newAttributes[i] = function.getTargetAttribute();
            i++;
        }
        MemoryExampleTable table = new MemoryExampleTable(newAttributes);
        ;
        DataRowFactory factory = new DataRowFactory(DataRowFactory.TYPE_DOUBLE_ARRAY, '.');
        double[] dataOfUpperLevels = new double[groupAttributes.length];
        if (groupAttributes.length > 0) {
            parseTree(rootNode, groupAttributes, dataOfUpperLevels, 0, table, factory, newAttributes, isCountingAllCombinations, aggregationFunctions);
        } else {
            parseLeaf(leafNode, dataOfUpperLevels, table, factory, newAttributes, aggregationFunctions);
        }
        if (getCompatibilityLevel().isAtMost(VERSION_5_1_6)) {
            if (groupAttributes.length == 0) {
                Attribute resultGroupAttribute = AttributeFactory.createAttribute(GENERIC_GROUP_NAME, Ontology.NOMINAL);
                table.addAttribute(resultGroupAttribute);
                table.getDataRow(0).set(resultGroupAttribute, resultGroupAttribute.getMapping().mapString(GENERIC_ALL_NAME));
                ExampleSet resultSet = table.createExampleSet();
                for (Attribute attribute : newAttributes) {
                    resultSet.getAttributes().remove(attribute);
                    resultSet.getAttributes().addRegular(attribute);
                }
                return resultSet;
            } else {
                try {
                    NumericToNominal toNominalOperator = OperatorService.createOperator(NumericToPolynominal.class);
                    toNominalOperator.setParameter(AttributeSubsetSelector.PARAMETER_FILTER_TYPE, AttributeSubsetSelector.CONDITION_REGULAR_EXPRESSION + "");
                    toNominalOperator.setParameter(RegexpAttributeFilter.PARAMETER_REGULAR_EXPRESSION, getParameterAsString(PARAMETER_GROUP_BY_ATTRIBUTES));
                    toNominalOperator.setParameter(AttributeSubsetSelector.PARAMETER_INCLUDE_SPECIAL_ATTRIBUTES, "true");
                    return toNominalOperator.apply(table.createExampleSet());
                } catch (OperatorCreationException e) {
                    return table.createExampleSet();
                }
            }
        }
        return table.createExampleSet();
    }

    private void parseLeaf(LeafAggregationTreeNode node, double[] dataOfUpperLevels, MemoryExampleTable table, DataRowFactory factory, Attribute[] newAttributes, List<AggregationFunction> aggregationFunctions) {
        double[] newData = new double[newAttributes.length];
        System.arraycopy(dataOfUpperLevels, 0, newData, 0, dataOfUpperLevels.length);
        DoubleArrayDataRow row = new DoubleArrayDataRow(newData);
        if (node != null) {
            int i = dataOfUpperLevels.length;
            for (Aggregator aggregator : node.getAggregators()) {
                aggregator.set(newAttributes[i], row);
                i++;
            }
        } else {
            int i = dataOfUpperLevels.length;
            for (AggregationFunction function : aggregationFunctions) {
                function.setDefault(newAttributes[i], row);
                i++;
            }
        }
        table.addDataRow(row);
    }

    private void parseTree(AggregationTreeNode node, Attribute[] groupAttributes, double[] dataOfUpperLevels, int groupLevel, MemoryExampleTable table, DataRowFactory factory, Attribute[] newAttributes, boolean isCountingAllCombinations, List<AggregationFunction> aggregationFunctions) throws UserError {
        Attribute currentAttribute = groupAttributes[groupLevel];
        if (currentAttribute.isNominal()) {
            Collection<? extends Object> nominalValues = null;
            if (isCountingAllCombinations) {
                nominalValues = currentAttribute.getMapping().getValues();
            } else {
                nominalValues = node.getValues();
            }
            for (Object nominalValue : nominalValues) {
                dataOfUpperLevels[groupLevel] = newAttributes[groupLevel].getMapping().mapString(nominalValue.toString());
                if (groupLevel + 1 < groupAttributes.length) {
                    parseTree(node.getOrCreateChild(nominalValue), groupAttributes, dataOfUpperLevels, groupLevel + 1, table, factory, newAttributes, isCountingAllCombinations, aggregationFunctions);
                } else {
                    parseLeaf(node.getLeaf(nominalValue), dataOfUpperLevels, table, factory, newAttributes, aggregationFunctions);
                }
            }
        } else if (currentAttribute.isNumerical() || Ontology.ATTRIBUTE_VALUE_TYPE.isA(currentAttribute.getValueType(), Ontology.DATE_TIME)) {
            for (Object numericalValue : node.getValues()) {
                dataOfUpperLevels[groupLevel] = (Double) numericalValue;
                if (groupLevel + 1 < groupAttributes.length) {
                    parseTree(node.getOrCreateChild(numericalValue), groupAttributes, dataOfUpperLevels, groupLevel + 1, table, factory, newAttributes, isCountingAllCombinations, aggregationFunctions);
                } else {
                    parseLeaf(node.getLeaf(numericalValue), dataOfUpperLevels, table, factory, newAttributes, aggregationFunctions);
                }
            }
        } else {
            throw new UserError(this, "aggregation_operator.unsupported_value_type", currentAttribute.getName(), Ontology.ATTRIBUTE_VALUE_TYPE.getNames()[currentAttribute.getValueType()]);
        }
    }

    private Attribute[] getMatchingAttributes(Attributes attributes, String regex) throws OperatorException {
        Pattern pattern = null;
        try {
            pattern = Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            throw new UserError(this, 206, regex, e.getMessage());
        }
        List<Attribute> attributeList = new LinkedList<Attribute>();
        Iterator<Attribute> iterator = attributes.allAttributes();
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            if (pattern.matcher(attribute.getName()).matches()) {
                attributeList.add(attribute);
            }
        }
        Attribute[] attributesArray = new Attribute[attributeList.size()];
        attributesArray = attributeList.toArray(attributesArray);
        return attributesArray;
    }

    private List<AggregationFunction> createAggreationFunctions(ExampleSet exampleSet) throws OperatorException {
        boolean ignoreMissings = getParameterAsBoolean(PARAMETER_IGNORE_MISSINGS);
        boolean countOnlyDistinct = getParameterAsBoolean(PARAMETER_ONLY_DISTINCT);
        List<AggregationFunction> aggregationFunctions = new LinkedList<AggregationFunction>();
        Set<Attribute> explicitlyAggregatedAttributes = new HashSet<Attribute>();
        List<String[]> aggregationFunctionPairs = getParameterList(PARAMETER_AGGREGATION_ATTRIBUTES);
        for (String[] aggregationFunctionPair : aggregationFunctionPairs) {
            Attribute attribute = exampleSet.getAttributes().get(aggregationFunctionPair[0]);
            if (attribute == null) {
                throw new UserError(this, "aggregation.aggregation_attribute_not_present", aggregationFunctionPair[0]);
            }
            AggregationFunction function = AggregationFunction.createAggregationFunction(aggregationFunctionPair[1], attribute, ignoreMissings, countOnlyDistinct);
            if (!function.isCompatible()) {
                throw new UserError(this, "aggregation.incompatible_attribute_type", attribute.getName(), aggregationFunctionPair[1]);
            }
            explicitlyAggregatedAttributes.add(attribute);
            aggregationFunctions.add(function);
        }
        if (getParameterAsBoolean(PARAMETER_USE_DEFAULT_AGGREGATION)) {
            String defaultAggregationFunctionName = getParameterAsString(PARAMETER_DEFAULT_AGGREGATION_FUNCTION);
            for (Attribute attribute : exampleSet.getAttributes()) {
                if (!explicitlyAggregatedAttributes.contains(attribute)) {
                    AggregationFunction function = AggregationFunction.createAggregationFunction(defaultAggregationFunctionName, attribute, ignoreMissings, countOnlyDistinct);
                    if (function.isCompatible()) {
                        aggregationFunctions.add(function);
                    }
                }
            }
        }
        return aggregationFunctions;
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeBoolean(PARAMETER_USE_DEFAULT_AGGREGATION, "If checked you can select a default aggregation function for a subset of the attributes.", false, false));
        List<ParameterType> parameterTypes = attributeSelector.getParameterTypes();
        for (ParameterType type : parameterTypes) {
            type.registerDependencyCondition(new BooleanParameterCondition(this, PARAMETER_USE_DEFAULT_AGGREGATION, false, true));
            types.add(type);
        }
        String[] functions = AggregationFunction.getAvailableAggregationFunctionNames();
        ParameterType type = new ParameterTypeStringCategory(PARAMETER_DEFAULT_AGGREGATION_FUNCTION, "The type of the used aggregation function for all default attributes.", functions, functions[0]);
        type.registerDependencyCondition(new BooleanParameterCondition(this, PARAMETER_USE_DEFAULT_AGGREGATION, false, true));
        type.setExpert(false);
        types.add(type);
        types.add(new ParameterTypeList(PARAMETER_AGGREGATION_ATTRIBUTES, "The attributes which should be aggregated.", new ParameterTypeAttribute("aggregation_attribute", "Specifies the attribute which is aggregated.", getExampleSetInputPort()), new ParameterTypeStringCategory(PARAMETER_AGGREGATION_FUNCTIONS, "The type of the used aggregation function.", functions, functions[0]), false));
        types.add(new ParameterTypeAttributes(PARAMETER_GROUP_BY_ATTRIBUTES, "Performs a grouping by the values of the attributes whose names match the given regular expression.", getExampleSetInputPort(), true, false));
        types.add(new ParameterTypeBoolean(PARAMETER_ALL_COMBINATIONS, "Indicates that all possible combinations of the values of the group by attributes are counted, even if they don't occur. Please handle with care, since the number might be enormous.", false));
        type = new ParameterTypeBoolean(PARAMETER_ONLY_DISTINCT, "Indicates if only rows with distinct values for the aggregation attribute should be used for the calculation of the aggregation function.", false);
        type.registerDependencyCondition(new BooleanParameterCondition(this, PARAMETER_ALL_COMBINATIONS, false, false));
        types.add(type);
        types.add(new ParameterTypeBoolean(PARAMETER_IGNORE_MISSINGS, "Indicates if missings should be ignored and aggregation should be based only on existing values or not. In the latter case the aggregated value will be missing in the presence of missing values.", true));
        return types;
    }

    @Override
    public OperatorVersion[] getIncompatibleVersionChanges() {
        return new OperatorVersion[] { VERSION_5_1_6 };
    }

    @Override
    public boolean writesIntoExistingData() {
        return false;
    }

    @Override
    public ResourceConsumptionEstimator getResourceConsumptionEstimator() {
        return OperatorResourceConsumptionHandler.getResourceConsumptionEstimator(getInputPort(), AggregationOperator.class, null);
    }
}
