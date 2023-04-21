package com.rapidminer.operator.learner.meta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.Statistics;
import com.rapidminer.example.set.ConditionedExampleSet;
import com.rapidminer.example.set.Partition;
import com.rapidminer.example.set.SplittedExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.operator.Model;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.learner.meta.tools.RelationCondition;
import com.rapidminer.operator.learner.meta.tools.RelationDefinitionRules;
import com.rapidminer.operator.learner.treekernel.AbstractMySVMLearnerIE;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeDouble;
import com.rapidminer.parameter.ParameterTypeInt;
import com.rapidminer.parameter.ParameterTypeList;
import com.rapidminer.parameter.ParameterTypeString;
import com.rapidminer.tools.Ontology;

/**
 * An operator which splits a relation-dataset into multiple dataset all
 * containing just one class. A set of rules is used to just embed those
 * examples that are sufficient for the specific relation class.
 * 
 * @author Felix Jungermann
 */
public class IterateMultiClassRelation extends OperatorChain {

    /**
	 * The parameter name for &quot;What strategy should be used for multi class
	 * classifications?&quot;
	 */
    public static final String PARAMETER_CLASSIFICATION_STRATEGIES = "classification_strategies";

    /**
	 * The parameter name for &quot;A multiplier regulating the codeword length
	 * in random code modus.&quot;
	 */
    public static final String PARAMETER_RANDOM_CODE_MULTIPLICATOR = "random_code_multiplicator";

    /**
	 * The parameter name for &quot;Use the given random seed instead of global
	 * random numbers (-1: use global)&quot;
	 */
    public static final String PARAMETER_LOCAL_RANDOM_SEED = "local_random_seed";

    public static final String PARAMETER_EVENT_1 = "event1";

    public static final String PARAMETER_EVENT_2 = "event2";

    public static final String PARAMETER_NULL_LABEL = "null-Label";

    public static final String PARAMETER_CONFIDENCE_THRESHOLD = "confidence";

    public static final String PARAMETER_EPSILON_LIST = "epsilon-list";

    private List<String> maps;

    private List<String> woOlabel;

    private RelationDefinitionRules rules;

    private String ent1AttributeName, ent2AttributeName;

    private String nullLabel;

    private double confThreshold;

    /** This List stores a short description for the generated models. */
    private LinkedList<String> modelNames = new LinkedList<String>();

    /**
	 * A class which stores all necessary information to train a series of
	 * models according to a certain classification strategy.
	 */
    private static class CodePattern {

        String[][] data;

        boolean[][] partitionEnabled;

        public CodePattern(int numberOfClasses, int numberOfFunctions) {
            data = new String[numberOfClasses][numberOfFunctions];
            partitionEnabled = new boolean[numberOfClasses][numberOfFunctions];
            for (int i = 0; i < numberOfClasses; i++) {
                for (int j = 0; j < numberOfFunctions; j++) {
                    partitionEnabled[i][j] = true;
                }
            }
        }
    }

    public IterateMultiClassRelation(OperatorDescription description, String... subprocessNames) {
        super(description, subprocessNames);
    }

    private SplittedExampleSet constructClassPartitionSet(ExampleSet inputSet) {
        ExampleSet result = null;
        rules = new RelationDefinitionRules();
        Attribute id = AttributeFactory.createAttribute(Attributes.ID_NAME, Ontology.NUMERICAL);
        inputSet.getExampleTable().addAttribute(id);
        inputSet.getAttributes().setId(id);
        try {
            int c = 0;
            for (Example e : inputSet) {
                e.setId(c++);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = (ExampleSet) inputSet.clone();
        result.recalculateAllAttributeStatistics();
        Attribute classLabel = result.getAttributes().getLabel();
        maps = new ArrayList<String>();
        for (int i = 0; i < classLabel.getMapping().size(); i++) {
            String mapping = classLabel.getMapping().getValues().get(i);
            if (result.getStatistics(classLabel, Statistics.COUNT, mapping) > 0) {
                maps.add(mapping);
            }
        }
        int numberOfClasses = maps.size();
        int[] examples = new int[result.size()];
        Iterator<Example> exampleIterator = result.iterator();
        int i = 0;
        while (exampleIterator.hasNext()) {
            Example e = exampleIterator.next();
            examples[i] = maps.indexOf(e.getValueAsString(classLabel));
            i++;
        }
        Partition separatedClasses = new Partition(examples, numberOfClasses);
        result = new SplittedExampleSet(result, separatedClasses);
        return (SplittedExampleSet) result;
    }

    /**
	 * Trains a series of models depending on the classification method
	 * specified by a certain code pattern.
	 */
    private Model[] applyCodePatternNEW(ExampleSet eSet, Attribute classLabel, CodePattern cP) throws OperatorException {
        try {
            int numberOfFunctions = cP.data[0].length;
            Model[] models = new Model[numberOfFunctions];
            List<String[]> specificEpsilons = getParameterList(PARAMETER_EPSILON_LIST);
            int counter = 0;
            for (int currentFunction = 0; currentFunction < numberOfFunctions; currentFunction++) {
                String label = woOlabel.get(currentFunction);
                if (!label.equals(this.getNullLabel())) {
                    RelationCondition rc = new RelationCondition(rules, label, ent1AttributeName, ent2AttributeName);
                    ExampleSet workingSet = new ConditionedExampleSet((ExampleSet) eSet.clone(), rc, false);
                    Attribute workingLabel = AttributeFactory.createAttribute("multiclass_working_label", Ontology.BINOMINAL);
                    workingSet.getExampleTable().addAttribute(workingLabel);
                    workingSet.getAttributes().addRegular(workingLabel);
                    int currentIndex = 0;
                    Iterator<Example> iterator = workingSet.iterator();
                    while (iterator.hasNext()) {
                        Example e = iterator.next();
                        currentIndex = woOlabel.indexOf(e.getValueAsString(classLabel));
                        if (currentIndex != currentFunction) {
                            e.setValue(workingLabel, workingLabel.getMapping().mapString("all_other_classes"));
                        } else {
                            if (cP.partitionEnabled[currentIndex][currentFunction]) {
                                e.setValue(workingLabel, workingLabel.getMapping().mapString(cP.data[currentIndex][currentFunction]));
                            }
                        }
                    }
                    workingSet.getAttributes().setLabel(workingLabel);
                    workingSet.getStatistics(workingLabel, Statistics.COUNT);
                    double oldEpsilon = 0.0001;
                    for (String[] s : specificEpsilons) {
                        if (s[0].equals(new Integer(counter).toString())) {
                            for (Operator o : getAllInnerOperators()) {
                                if (o instanceof AbstractMySVMLearnerIE) {
                                    oldEpsilon = ((AbstractMySVMLearnerIE) o).getParameterAsDouble(AbstractMySVMLearnerIE.PARAMETER_CONVERGENCE_EPSILON);
                                    double eps = oldEpsilon * new Double(s[1]).doubleValue();
                                    ((AbstractMySVMLearnerIE) o).setParameter(AbstractMySVMLearnerIE.PARAMETER_CONVERGENCE_EPSILON, new Double(eps).toString());
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    inApplyLoop();
                    for (Operator o : getAllInnerOperators()) {
                        if (o instanceof AbstractMySVMLearnerIE) {
                            ((AbstractMySVMLearnerIE) o).setParameter(AbstractMySVMLearnerIE.PARAMETER_CONVERGENCE_EPSILON, new Double(oldEpsilon).toString());
                            break;
                        }
                    }
                    workingSet.getAttributes().setLabel(classLabel);
                    workingSet.getExampleTable().removeAttribute(workingLabel);
                    counter++;
                }
            }
            return models;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getWoOlabel() {
        return woOlabel;
    }

    public void setWoOlabel(List<String> woOlabel) {
        this.woOlabel = woOlabel;
    }

    public String getEnt1AttributeName() {
        return ent1AttributeName;
    }

    public void setEnt1AttributeName(String ent1AttributeName) {
        this.ent1AttributeName = ent1AttributeName;
    }

    public String getEnt2AttributeName() {
        return ent2AttributeName;
    }

    public void setEnt2AttributeName(String ent2AttributeName) {
        this.ent2AttributeName = ent2AttributeName;
    }

    /**
	 * Builds a code pattern according to the "1 against all" classification
	 * scheme.
	 */
    private CodePattern buildCodePattern_ONE_VS_ALL(Attribute classLabel) {
        try {
            woOlabel = new ArrayList<String>();
            for (int i = 0; i < maps.size(); i++) {
                woOlabel.add(maps.get(i));
            }
            int numberOfClasses = woOlabel.size();
            CodePattern cP = new CodePattern(numberOfClasses, numberOfClasses);
            modelNames.clear();
            for (int i = 0; i < numberOfClasses; i++) {
                for (int j = 0; j < numberOfClasses; j++) {
                    if (i == j) {
                        String currentClass = woOlabel.get(i);
                        modelNames.add(currentClass + " vs. all other");
                        cP.data[i][j] = currentClass;
                    } else {
                        cP.data[i][j] = "all_other_classes";
                    }
                }
            }
            return cP;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getNullLabel() {
        return nullLabel;
    }

    public void setNullLabel(String nullLabel) {
        this.nullLabel = nullLabel;
    }

    public double getConfThreshold() {
        return confThreshold;
    }

    public void setConfThreshold(double confThreshold) {
        this.confThreshold = confThreshold;
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeString(PARAMETER_EVENT_1, "What is the attribute name containing the first entity?", null));
        types.add(new ParameterTypeString(PARAMETER_EVENT_2, "What is the attribute name containing the second entity?", null));
        types.add(new ParameterTypeString(PARAMETER_NULL_LABEL, "Which one is the null-Label and should be tagged if no other class is confident enough?", null));
        types.add(new ParameterTypeDouble(PARAMETER_CONFIDENCE_THRESHOLD, "Which confidence should be used as threshold for tagging?", 0.0d, 1.0d, false));
        types.add(new ParameterTypeList(PARAMETER_EPSILON_LIST, "Every internal learner can get its own convergence epsilon.", new ParameterTypeInt("learner number", "The number of the learner which should get another epsilon than internally chosen.", 0, Integer.MAX_VALUE), new ParameterTypeDouble("epsilon", "The convergence epsilon value for this learner.", 0.0d, Double.POSITIVE_INFINITY, 1.0d)));
        return types;
    }

    public List<String> getMaps() {
        return maps;
    }

    public void setMaps(List<String> maps) {
        this.maps = maps;
    }

    public RelationDefinitionRules getRules() {
        return rules;
    }

    public void setRules(RelationDefinitionRules rules) {
        this.rules = rules;
    }
}
