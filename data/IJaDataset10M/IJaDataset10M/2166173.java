package edu.unika.aifb.rules.machine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import com.Ostermiller.util.CSVParser;
import edu.unika.aifb.rules.agenda.Agenda;
import edu.unika.aifb.rules.agenda.AgendaElement;
import edu.unika.aifb.rules.agenda.AgendaImpl;
import edu.unika.aifb.rules.agenda.CompleteAgenda;
import edu.unika.aifb.rules.agenda.EmptyAgenda;
import edu.unika.aifb.rules.combination.Averaging;
import edu.unika.aifb.rules.combination.Combination;
import edu.unika.aifb.rules.combination.DecisionTree;
import edu.unika.aifb.rules.combination.MachineLearn;
import edu.unika.aifb.rules.combination.ManualWeightsLinear;
import edu.unika.aifb.rules.combination.ManualWeightsSigmoid;
import edu.unika.aifb.rules.input.ExplicitRelation;
import edu.unika.aifb.rules.input.MyOntology;
import edu.unika.aifb.rules.input.Structure;
import edu.unika.aifb.rules.main.Align;
import edu.unika.aifb.rules.main.Parameter;
import edu.unika.aifb.rules.result.ResultList;
import edu.unika.aifb.rules.result.ResultListImpl;
import edu.unika.aifb.rules.result.ResultTable;
import edu.unika.aifb.rules.result.ResultTableImpl;
import edu.unika.aifb.rules.result.Saveold;
import edu.unika.aifb.rules.rules.AllFeaturesRule;
import edu.unika.aifb.rules.rules.EmptyRule;
import edu.unika.aifb.rules.rules.ManualRuleSimple;
import edu.unika.aifb.rules.rules.RulesCompleteForML;
import edu.unika.aifb.rules.rules.Rules;
import edu.unika.aifb.rules.util.UserInterface;

/**
 * This class represents the whole APFEL process. Rules are generated
 * from the predefined rules and rules extracted from an inserted set 
 * of features. Further training examples are determined. After the user
 * has validated these, they are augmented with the results of the 
 * previously created rules. Finally a combiner is learned from the rules 
 * and the training examples. The final rules and the combiner are saved
 * and can be loaded in the normal alignment tool. The work is presented 
 * at WM 05 and a poster at WWW 05.
 * 
 * @author Marc Ehrig
 */
public class Train {

    private static final String[] ONTOLOGYFILES = { "C:/Work/DissData/oaei05/onto101.owl", "C:/Work/DissData/oaei05/onto304.owl" };

    private static final String ALLFEATURES = "";

    private static final int FEATURELEVEL = 2;

    private static final String GENERATEDRULES = "C:/Work/NewTraining/rules3.obj";

    private static final String EXPLICITFILE = "C:/Work/DissData/eon04/onto304abMapCSV.txt";

    private static final String MANUALMAPPINGS = "C:/Work/DissData/eon04/onto304abMapCSV.txt";

    private static final int MAXITERATIONS = 10;

    private static final int STRATEGY = Parameter.DECISIONTREE;

    private static final boolean INTERNALTOO = Parameter.EXTERNAL;

    private static final boolean EFFICIENTAGENDA = Parameter.COMPLETE;

    private static final String CLASSIFIERFOREXAMPLE = "C:/Work/NewTraining/tree12.obj";

    private static final String RULESFOREXAMPLE = "C:/Work/NewTraining/rules3.obj";

    private static final boolean SEMI = Parameter.FULLAUTOMATIC;

    private static final double MAXERROR = 0.95;

    private static final boolean REMOVEDOUBLES = Parameter.REMOVEDOUBLES;

    private static final String RESULTFOREXAMPLE = "C:/Work/DissData/eon04/onto304abres2.txt";

    private static final String[] POSTONTOLOGYFILES = { "C:/Work/DissData/first03/russia1.owl", "C:/Work/DissData/first03/russia2.owl", "C:/Work/DissData/first03/russiaA.owl", "C:/Work/DissData/first03/russiaB.owl", "C:/Work/DissData/first03/russiaC.owl", "C:/Work/DissData/first03/russiaD.owl", "C:/Work/DissData/first03/tourismA.owl", "C:/Work/DissData/first03/tourismB.owl", "C:/Work/DissData/second04/sportEvent.owl", "C:/Work/DissData/second04/sportSoccer.owl", "C:/Work/DissData/i3con04/animalsA.owl", "C:/Work/DissData/i3con04/animalsB.owl", "C:/Work/DissData/i3con04/hotelA.owl", "C:/Work/DissData/i3con04/hotelB.owl", "C:/Work/DissData/i3con04/csA.owl", "C:/Work/DissData/i3con04/csB.owl", "C:/Work/DissData/i3con04/networkA.owl", "C:/Work/DissData/i3con04/networkB.owl", "C:/Work/DissData/i3con04/people+petsA.owl", "C:/Work/DissData/i3con04/people+petsB.owl", "C:/Work/DissData/oaei05/onto101.owl", "C:/Work/DissData/oaei05/onto301.owl", "C:/Work/DissData/oaei05/onto101.owl", "C:/Work/DissData/oaei05/onto302.owl", "C:/Work/DissData/oaei05/onto101.owl", "C:/Work/DissData/oaei05/onto303.owl", "C:/Work/DissData/oaei05/onto101.owl", "C:/Work/DissData/oaei05/onto304.owl", "C:/Work/DissData/oaei05/onto101.owl", "C:/Work/DissData/oaei05/onto202.owl", "C:/Work/DissData/oaei05/onto101.owl", "C:/Work/DissData/oaei05/onto206.owl", "C:/Work/DissData/oaei05/onto101.owl", "C:/Work/DissData/oaei05/onto248.owl", "C:/Work/DissData/oaei05/onto101.owl", "C:/Work/DissData/oaei05/onto250.owl", "C:/Work/DissData/oaei05/onto101.owl", "C:/Work/DissData/oaei05/onto251.owl", "C:/Work/DissData/oaei05/onto101.owl", "C:/Work/DissData/oaei05/onto252.owl", "C:/Work/DissData/oaei05/onto101.owl", "C:/Work/DissData/oaei05/onto201.owl", "C:/Work/DissData/oaei05/onto101.owl", "C:/Work/DissData/oaei05/onto204.owl", "C:/Work/DissData/oaei05/onto101.owl", "C:/Work/DissData/oaei05/onto249.owl", "C:/Work/DissData/oaei05/onto101.owl", "C:/Work/DissData/oaei05/onto205.owl" };

    private static final String[] POSTEXPLICITFILE = { "C:/Work/DissData/first03/russia12Map.txt", "C:/Work/DissData/first03/russiaABMap.txt", "C:/Work/DissData/first03/russiaCDMap.txt", "C:/Work/DissData/first03/tourismABMap.txt", "C:/Work/DissData/second04/sportSoccerEventMap.txt", "C:/Work/DissData/i3con04/animalsABMapCSV.txt", "C:/Work/DissData/i3con04/hotelABMapCSV.txt", "C:/Work/DissData/i3con04/csABMapCSV.txt", "C:/Work/DissData/i3con04/networkABMapCSV.txt", "C:/Work/DissData/i3con04/people+petsABMapCSV.txt", "C:/Work/DissData/oaei05/onto301MapCSV.txt", "C:/Work/DissData/oaei05/onto302MapCSV.txt", "C:/Work/DissData/oaei05/onto303MapCSV.txt", "C:/Work/DissData/oaei05/onto304MapCSV.txt", "C:/Work/DissData/oaei05/onto202MapCSV.txt", "C:/Work/DissData/oaei05/onto206MapCSV.txt", "C:/Work/DissData/oaei05/onto248MapCSV.txt", "C:/Work/DissData/oaei05/onto250MapCSV.txt", "C:/Work/DissData/oaei05/onto251MapCSV.txt", "C:/Work/DissData/oaei05/onto252MapCSV.txt", "C:/Work/DissData/oaei05/onto201MapCSV.txt", "C:/Work/DissData/oaei05/onto204MapCSV.txt", "C:/Work/DissData/oaei05/onto249MapCSV.txt", "C:/Work/DissData/oaei05/onto205MapCSV.txt" };

    private static final String[] VALIDATEDEXAMPLES = { "C:/Work/DissData/first03/russia12Map.txt", "C:/Work/DissData/first03/russiaABMap.txt", "C:/Work/DissData/first03/russiaCDMap.txt", "C:/Work/DissData/first03/tourismABMap.txt", "C:/Work/DissData/second04/sportSoccerEventMap.txt", "C:/Work/DissData/i3con04/animalsABMapCSV.txt", "C:/Work/DissData/i3con04/hotelABMapCSV.txt", "C:/Work/DissData/i3con04/csABMapCSV.txt", "C:/Work/DissData/i3con04/networkABMapCSV.txt", "C:/Work/DissData/i3con04/people+petsABMapCSV.txt", "C:/Work/DissData/oaei05/onto301MapCSV.txt", "C:/Work/DissData/oaei05/onto302MapCSV.txt", "C:/Work/DissData/oaei05/onto303MapCSV.txt", "C:/Work/DissData/oaei05/onto304MapCSV.txt", "C:/Work/DissData/oaei05/onto202MapCSV.txt", "C:/Work/DissData/oaei05/onto206MapCSV.txt", "C:/Work/DissData/oaei05/onto248MapCSV.txt", "C:/Work/DissData/oaei05/onto250MapCSV.txt", "C:/Work/DissData/oaei05/onto251MapCSV.txt", "C:/Work/DissData/oaei05/onto252MapCSV.txt", "C:/Work/DissData/oaei05/onto201MapCSV.txt", "C:/Work/DissData/oaei05/onto204MapCSV.txt", "C:/Work/DissData/oaei05/onto249MapCSV.txt", "C:/Work/DissData/oaei05/onto205MapCSV.txt" };

    private static final boolean EFFICIENTAGENDAFORTRAINING = Parameter.COMPLETE;

    private static final String[] SINGLERESULTFORTRAINING = { "C:/Work/NewTraining/russia12res.txt", "C:/Work/NewTraining/russiaABres.txt", "C:/Work/NewTraining/russiaCDres.txt", "C:/Work/NewTraining/tourismABres.txt", "C:/Work/NewTraining/sportSoccerEventres.txt", "C:/Work/NewTraining/animalsabres.txt", "C:/Work/NewTraining/hotelABres.txt", "C:/Work/NewTraining/csabres.txt", "C:/Work/NewTraining/networkabres.txt", "C:/Work/NewTraining/people+petsabres.txt", "C:/Work/NewTraining/onto301abres.txt", "C:/Work/NewTraining/onto302abres.txt", "C:/Work/NewTraining/onto303abres.txt", "C:/Work/NewTraining/onto304abres.txt", "C:/Work/NewTraining/onto202abres.txt", "C:/Work/NewTraining/onto206abres.txt", "C:/Work/NewTraining/onto248abres.txt", "C:/Work/NewTraining/onto250abres.txt", "C:/Work/NewTraining/onto251abres.txt", "C:/Work/NewTraining/onto252abres.txt", "C:/Work/NewTraining/onto201abres.txt", "C:/Work/NewTraining/onto204abres.txt", "C:/Work/NewTraining/onto249abres.txt", "C:/Work/NewTraining/onto205abres.txt" };

    private static final String[] RESULTFORTRAINING = { "C:/Work/NewTraining/russia12res.txt", "C:/Work/NewTraining/russiaABres.txt", "C:/Work/NewTraining/russiaCDres.txt", "C:/Work/NewTraining/tourismABres.txt", "C:/Work/NewTraining/sportSoccerEventres.txt", "C:/Work/NewTraining/animalsabres.txt", "C:/Work/NewTraining/animalsabres.txt", "C:/Work/NewTraining/animalsabres.txt", "C:/Work/NewTraining/animalsabres.txt", "C:/Work/NewTraining/hotelABres.txt", "C:/Work/NewTraining/hotelABres.txt", "C:/Work/NewTraining/hotelABres.txt", "C:/Work/NewTraining/hotelABres.txt", "C:/Work/NewTraining/csabres.txt", "C:/Work/NewTraining/networkabres.txt", "C:/Work/NewTraining/networkabres.txt", "C:/Work/NewTraining/networkabres.txt", "C:/Work/NewTraining/people+petsabres.txt", "C:/Work/NewTraining/people+petsabres.txt", "C:/Work/NewTraining/onto301abres.txt", "C:/Work/NewTraining/onto302abres.txt", "C:/Work/NewTraining/onto303abres.txt", "C:/Work/NewTraining/onto304abres.txt", "C:/Work/NewTraining/onto301abres.txt", "C:/Work/NewTraining/onto302abres.txt", "C:/Work/NewTraining/onto303abres.txt", "C:/Work/NewTraining/onto304abres.txt", "C:/Work/NewTraining/onto202abres.txt", "C:/Work/NewTraining/onto206abres.txt", "C:/Work/NewTraining/onto248abres.txt", "C:/Work/NewTraining/onto250abres.txt", "C:/Work/NewTraining/onto251abres.txt", "C:/Work/NewTraining/onto252abres.txt", "C:/Work/NewTraining/onto201abres.txt", "C:/Work/NewTraining/onto204abres.txt", "C:/Work/NewTraining/onto249abres.txt", "C:/Work/NewTraining/onto205abres.txt" };

    private static final String ARFF = "C:/Work/NewTraining/weka.arff";

    private static final String COST = "C:/Work/NewTraining/cost.cost";

    private static final String CLASSIFIERFILE = "C:/Work/NewTraining/classifier13.obj";

    private static final String DECISIONTREEFILE = "C:/Work/NewTraining/tree13.obj";

    private static final String FINALRULESFILE = "C:/Work/NewTraining/finalRules13.obj";

    public static void main(String[] args) {
        postvalidationExamples();
        train();
    }

    /**
	 * Generates rules from existing rule collections e.g. ManualRuleComplex and
	 * adds other features which are part of a list. These will contain domain-specific
	 * features. The rules are saved for later use.
	 */
    public static void generateRules() {
        System.out.println("generation of rules");
        Rules manualRules = new RulesCompleteForML();
        Rules allRules = new EmptyRule(manualRules.total());
        for (int i = 0; i < manualRules.total(); i++) {
            allRules.addIndividualRule(manualRules.rule(i));
        }
        allRules.setPreviousResult(null);
        ObjectIO.save(ObjectIO.toSerializable(allRules), GENERATEDRULES);
        System.out.println(" " + allRules.total() + " rules generated\n");
    }

    /**
	 * To train the system with machine learning, we need examples. These examples
	 * are created with a simpler strategy and given to the user for validation.
	 *
	 */
    public static void generateExamples() {
        System.out.println("generation of example mappings");
        Align align = new Align();
        align.ontology = new MyOntology(ONTOLOGYFILES);
        align.p = new Parameter(MAXITERATIONS, STRATEGY, INTERNALTOO, EFFICIENTAGENDA, CLASSIFIERFOREXAMPLE, RULESFOREXAMPLE, SEMI, MAXERROR, 5, Parameter.ALLOWDOUBLES, 0.5, ONTOLOGYFILES);
        align.explicit = new ExplicitRelation(EXPLICITFILE, align.ontology);
        align.align();
        Saveold.saveCompleteEval(align.ontology, align.resultListLatest, MANUALMAPPINGS, align.p.cutoff, RESULTFOREXAMPLE);
        System.out.println();
    }

    /**
	 * The validated examples are now run with all rules. The values are required
	 * for the training.
	 *
	 */
    public static void postvalidationExamples() {
        System.out.println("value calculation for validated examples");
        int numberOfOntologyPairs = POSTONTOLOGYFILES.length / 2;
        for (int i = 0; i < numberOfOntologyPairs; i++) {
            String[] myOntologyFiles = new String[2];
            myOntologyFiles[0] = POSTONTOLOGYFILES[2 * i];
            myOntologyFiles[1] = POSTONTOLOGYFILES[2 * i + 1];
            System.out.print(myOntologyFiles[0] + " ");
            System.out.println(myOntologyFiles[1]);
            Align align = new Align();
            align.ontology = new MyOntology(myOntologyFiles);
            align.p = new Parameter(MAXITERATIONS, STRATEGY, INTERNALTOO, EFFICIENTAGENDAFORTRAINING, CLASSIFIERFOREXAMPLE, RULESFOREXAMPLE, SEMI, MAXERROR, 5, Parameter.ALLOWDOUBLES, 0.5, myOntologyFiles);
            if (i <= 4) {
                align.p.efficientAgenda = Parameter.EFFICIENT;
            }
            ExplicitRelation explicit = new ExplicitRelation("", align.ontology);
            align.explicit = explicit;
            align.align();
            ResultList resultList = align.resultListLatest;
            Structure ontology = new MyOntology(myOntologyFiles);
            Agenda agenda = new EmptyAgenda();
            Rules prerules = (Rules) ObjectIO.load(RULESFOREXAMPLE);
            Rules rules = ObjectIO.fromSerializable(prerules);
            rules.setPreviousResult(new ResultTableImpl());
            Combination combination = new DecisionTree(CLASSIFIERFOREXAMPLE, rules, ontology);
            Rules prerules2 = (Rules) ObjectIO.load(GENERATEDRULES);
            Rules rules2 = ObjectIO.fromSerializable(prerules2);
            Combination combination2 = new Averaging(rules2.total(), rules2.total(), rules2.total());
            agenda = new AgendaImpl();
            Agenda agenda1 = new CompleteAgenda();
            agenda1.create(ontology, Parameter.EXTERNAL);
            agenda.add(agenda1);
            ResultTable lastResult = new ResultTableImpl();
            lastResult.copy(resultList, 5, 0.0);
            rules.setPreviousResult(lastResult);
            rules2.setPreviousResult(lastResult);
            ResultList resultListOld = resultList;
            resultList = new ResultListImpl(5);
            int counter = 0;
            agenda.iterate();
            while (agenda.hasNext()) {
                counter++;
                if (counter % 1000 == 0) {
                    System.out.print("|");
                    if (counter % 100000 == 0) {
                        System.out.println();
                    }
                }
                AgendaElement element = agenda.next();
                Object object1 = element.object1;
                Object object2 = element.object2;
                combination.reset();
                combination.setObjects(object1, object2);
                combination.process();
                combination2.reset();
                combination2.setObjects(object1, object2);
                for (int j = 0; j < rules2.total(); j++) {
                    double value2 = rules2.process(object1, object2, j, ontology);
                    combination2.setValue(j, value2);
                }
                combination2.process();
                if (explicit.checkFor(object1, object2) || explicit.checkFor(object2, object1)) {
                    resultList.set(object1, object2, 1.0, combination2.getAddInfo());
                    resultList.set(object2, object1, 1.0, combination2.getAddInfo());
                } else {
                    resultList.set(object1, object2, ((combination.result() + combination2.result()) / 2.0), combination2.getAddInfo());
                    resultList.set(object2, object1, ((combination.result() + combination2.result()) / 2.0), combination2.getAddInfo());
                }
            }
            if (i < 14) {
                Saveold.saveCompleteEval(ontology, resultList, VALIDATEDEXAMPLES[i], 0.95, SINGLERESULTFORTRAINING[i]);
            } else {
                Saveold.saveCompleteEvalwoInstances(ontology, resultList, VALIDATEDEXAMPLES[i], 0.95, SINGLERESULTFORTRAINING[i]);
            }
        }
        System.out.println("\nrules applied\n");
    }

    /**
	 * The actual training step. The similarity values are to be interpreted in a way
	 * that the correct classification (aligned or not-aligned) is met. Different WEKA classifiers
	 * can be used for this.
	 *
	 */
    public static void train() {
        System.out.println("training started");
        String resultForTraining[] = RESULTFORTRAINING;
        String array1[][][] = new String[resultForTraining.length][][];
        String array2[][][] = new String[resultForTraining.length][][];
        int length = 0;
        int length2 = 0;
        for (int i = 0; i < resultForTraining.length; i++) {
            try {
                System.out.println("loading " + resultForTraining[i]);
                InputStream inputStream = new FileInputStream(resultForTraining[i]);
                CSVParser csvparser = new CSVParser(inputStream, "", "", "");
                csvparser.changeDelimiter(';');
                array1[i] = csvparser.getAllValues();
                inputStream.close();
                length = array1[i].length + length;
                if (i < 27) {
                    inputStream = new FileInputStream(resultForTraining[i]);
                    csvparser = new CSVParser(inputStream, "", "", "");
                    csvparser.changeDelimiter(';');
                    array2[i] = csvparser.getAllValues();
                    inputStream.close();
                    length2 = array1[i].length + length2;
                }
            } catch (Exception e) {
                UserInterface.print(e.getMessage());
            }
        }
        System.out.println("emphasizing structure");
        String array[][] = { { "" } };
        array = new String[length + length2][array1[1][0].length];
        int l = 0;
        for (int i = 0; i < resultForTraining.length; i++) {
            array1[i] = setColumnRand(array1[i], 0, "-0.3", 0.5);
            array1[i] = setColumnRand(array1[i], 6, "0.0", 0.5);
            array1[i] = setColumnRand(array1[i], 7, "0.0", 0.5);
            for (int j = 0; j < array1[i].length; j++) {
                array[l] = array1[i][j];
                l++;
            }
            if (i < 27) {
                array2[i] = setColumnRand(array2[i], 0, "-0.3", 0.9);
                array2[i] = setColumnRand(array2[i], 4, "-0.3", 0.9);
                array2[i] = setColumnRand(array2[i], 5, "-0.3", 0.9);
                array2[i] = setColumnRand(array2[i], 6, "0.0", 0.9);
                array2[i] = setColumnRand(array2[i], 7, "0.0", 0.9);
                for (int j = 0; j < array2[i].length; j++) {
                    array[l] = array2[i][j];
                    l++;
                }
            }
        }
        int numberOfRules = array[0].length - 6;
        int numberOfInstances = array.length;
        System.out.println(numberOfInstances);
        System.out.println("discretizing");
        for (int i = 0; i < (numberOfRules - 1); i++) {
            array = discretize(array, i);
        }
        System.out.println("saving and loading arff");
        Instances instances = getInstances(array);
        array1 = new String[1][1][1];
        array2 = new String[1][1][1];
        array = new String[1][1];
        System.out.println("oversampling");
        oversampling(instances);
        System.out.println("learning classifier");
        Classifier classifier = learnClassifier(instances);
        double quality = getQuality(instances, classifier);
        J48 j48 = new J48();
        String info = "";
        System.out.println("checking rules for importance");
        try {
            j48 = (J48) classifier;
            info = j48.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean ruleNecessary[] = new boolean[numberOfRules];
        int numberOfRulesLeft = 0;
        for (int i = 0; i < numberOfRules; i++) {
            Integer number = new Integer(i);
            ruleNecessary[i] = true;
            if (info.indexOf("rule" + number.toString() + " ") != -1) {
                ruleNecessary[i] = true;
                System.out.println(i);
                numberOfRulesLeft++;
            } else {
                ruleNecessary[i] = false;
            }
        }
        System.out.println("saving");
        Rules newRules = new EmptyRule(numberOfRulesLeft);
        Rules rules = (Rules) ObjectIO.load(GENERATEDRULES);
        try {
            FileWriter out = new FileWriter(new File(DECISIONTREEFILE));
            System.out.println(j48.toString());
            out.write(j48.toString() + "\n\n");
            int c = 0;
            for (int k = 0; k < rules.total(); k++) {
                if (ruleNecessary[k]) {
                    newRules.addIndividualRule(rules.rule(k));
                    c++;
                    System.out.println(k);
                    System.out.println(" " + rules.rule(k).feature1.toString());
                    System.out.println(" " + rules.rule(k).feature2.toString());
                    System.out.println(" " + rules.rule(k).heuristic.toString());
                    out.write(k + "\n");
                    out.write(" " + rules.rule(k).feature1.toString() + "\n");
                    out.write(" " + rules.rule(k).feature2.toString() + "\n");
                    out.write(" " + rules.rule(k).heuristic.toString() + "\n");
                }
            }
            out.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ObjectIO.save(newRules, FINALRULESFILE);
        Combination machinelearned = new MachineLearn(classifier, instances, newRules.total());
        ObjectIO.save(machinelearned, CLASSIFIERFILE);
        System.out.println("\nend\n");
    }

    private static String[][] setColumn(String[][] array, int column, String value) {
        for (int i = 0; i < array.length; i++) {
            if (Double.valueOf(array[i][column + 5]).doubleValue() >= 0.0) {
                array[i][column + 5] = value;
            }
        }
        return array;
    }

    private static String[][] setColumnRand(String[][] array, int column, String value, double rand) {
        for (int i = 0; i < array.length; i++) {
            if (Double.valueOf(array[i][column + 5]).doubleValue() >= 0.0) {
                double number = Math.random();
                if (number > rand) {
                    array[i][column + 5] = value;
                }
            }
        }
        return array;
    }

    private static String[][] discretize(String[][] array, int column) {
        for (int i = 0; i < array.length; i++) {
            double old = Double.parseDouble(array[i][column + 5]);
            String newv = array[i][column + 5];
            if ((0.0 <= old) && (old < 0.05)) newv = "0.0";
            if ((0.05 <= old) && (old < 0.1)) newv = "0.05";
            if ((0.1 <= old) && (old < 0.2)) newv = "0.1";
            if ((0.2 <= old) && (old < 0.3)) newv = "0.2";
            if ((0.3 <= old) && (old < 0.4)) newv = "0.3";
            if ((0.4 <= old) && (old < 0.5)) newv = "0.4";
            if ((0.5 <= old) && (old < 0.6)) newv = "0.5";
            if ((0.6 <= old) && (old < 0.7)) newv = "0.6";
            if ((0.7 <= old) && (old < 0.8)) newv = "0.7";
            if ((0.8 <= old) && (old < 0.85)) newv = "0.8";
            if ((0.85 <= old) && (old < 0.9)) newv = "0.85";
            if ((0.9 <= old) && (old < 0.95)) newv = "0.9";
            if ((0.95 <= old) && (old < 1.0)) newv = "0.95";
            if (1.0 <= old) newv = "1.0";
            array[i][column + 5] = newv;
        }
        return array;
    }

    private static Instances getInstances(String array[][]) {
        WekaConnector.saveData(array, ARFF);
        Instances instances = WekaConnector.loadData(ARFF, COST);
        return instances;
    }

    /**
	 * The parameters for the learning process have to be set here.
	 */
    private static Classifier learnClassifier(Instances instances) {
        J48 classifier = new J48();
        try {
            classifier.setConfidenceFactor((float) 0.25);
            classifier.setMinNumObj(100);
            classifier.setNumFolds(50);
            classifier.setReducedErrorPruning(false);
            classifier.setUseLaplace(true);
            classifier.setSubtreeRaising(true);
            classifier.buildClassifier(instances);
        } catch (Exception e) {
            UserInterface.print(e.getMessage());
        }
        return classifier;
    }

    private static double getQuality(Instances instances, Classifier classifier) {
        int qualityPP = 0;
        int qualityPN = 0;
        int qualityNN = 0;
        int qualityNP = 0;
        int numberOfInstances = instances.numInstances();
        try {
            for (int i = 0; i < numberOfInstances; i++) {
                Instance instance = instances.instance(i);
                double resultcalc = classifier.classifyInstance(instance);
                double resultassi = instance.value(instance.numAttributes() - 1);
                if (resultassi == 1) {
                    if (resultcalc == 1) {
                        qualityPP++;
                    } else {
                        qualityPN++;
                    }
                } else {
                    if (resultcalc == 1) {
                        qualityNP++;
                    } else {
                        qualityNN++;
                    }
                }
            }
        } catch (Exception e) {
            UserInterface.print(e.getMessage());
        }
        System.out.println("quality: " + qualityPP + " " + qualityPN + " " + qualityNN + " " + qualityNP);
        double quality = 2 * qualityPP + qualityNN;
        return quality;
    }

    private static String[][] buildNewArray(int numberOfRules, boolean ruleNecessary[], int numberOfInstances, String array[][]) {
        int numberOfRulesLeft = 0;
        for (int k = 0; k < numberOfRules; k++) {
            if (ruleNecessary[k] == true) {
                numberOfRulesLeft++;
            }
        }
        String array2[][] = new String[numberOfInstances][numberOfRulesLeft + 6];
        for (int j = 0; j < numberOfInstances; j++) {
            array2[j][0] = array[j][0];
            array2[j][1] = array[j][1];
            array2[j][2] = array[j][2];
            array2[j][3] = array[j][3];
            array2[j][4] = array[j][4];
            int position = 0;
            for (int k = 0; k < numberOfRules; k++) {
                if (ruleNecessary[k]) {
                    array2[j][position + 5] = array[j][k + 5];
                    position++;
                }
            }
            array2[j][position + 5] = array[j][numberOfRules + 5];
        }
        return array2;
    }

    private static void oversampling(Instances instances) {
        int numberOfInstances = instances.numInstances();
        int positives = 0;
        int negatives = 0;
        try {
            for (int i = 0; i < numberOfInstances; i++) {
                Instance instance = instances.instance(i);
                double resultassi = instance.value(instance.numAttributes() - 1);
                if (resultassi == 1) {
                    positives++;
                } else {
                    negatives++;
                }
            }
            while (negatives > positives) {
                int ran = (int) Math.round(Math.random() * numberOfInstances);
                Instance instance = instances.instance(ran);
                double resultassi = instance.value(instance.numAttributes() - 1);
                if (resultassi == 1) {
                    Instance newInst = new Instance(instance);
                    instances.add(newInst);
                    positives++;
                }
            }
            while (positives > negatives) {
                int ran = (int) Math.round(Math.random() * numberOfInstances);
                Instance instance = instances.instance(ran);
                double resultassi = instance.value(instance.numAttributes() - 1);
                if (resultassi == 0) {
                    Instance newInst = new Instance(instance);
                    instances.add(newInst);
                    negatives++;
                }
            }
        } catch (Exception e) {
        }
    }
}
