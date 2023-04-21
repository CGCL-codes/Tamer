package preprocessing.automatic.Testing;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.cli.*;
import preprocessing.automatic.Configuration.GeneticConfig;
import preprocessing.automatic.FitnessCalculation.FitnessCalculator;
import preprocessing.automatic.FitnessCalculation.MultipleIndividualEvaluationCalculator;
import preprocessing.automatic.Population.APAIndividual;
import preprocessing.automatic.links.ClassificatorLink;
import preprocessing.automatic.links.J48TreeLink;
import preprocessing.methods.Import.LoadRAWPreprocessor;
import preprocessing.storage.SimplePreprocessingStorage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: lagon
 * Date: 18.11.2009
 * Time: 22:16:47
 * To change this template use File | Settings | File Templates.
 */
public class FitnessStabilityTesting {

    private String trainingDataName;

    private String testingDataName;

    private String dumpDirectoryName;

    private int fitEvalNumber;

    private int numModelsToAverage;

    private String indivPath;

    private SimplePreprocessingStorage training;

    private SimplePreprocessingStorage testing;

    private APAIndividual indiv;

    private ClassificatorLink link;

    private FitnessCalculator calculator;

    private GeneticConfig config;

    private FileWriter fw;

    public FitnessStabilityTesting(String[] args) {
        try {
            parseCmdLine(args);
        } catch (ParseException e) {
            e.printStackTrace();
            System.err.flush();
            System.err.flush();
            System.out.flush();
            System.exit(-1);
        }
        config = GeneticConfig.getConfiguration();
        config.getAdvancedConfig().setDumpDirectory(dumpDirectoryName);
        config.setComputeNetworkRounds(numModelsToAverage);
        link = new J48TreeLink();
        calculator = new MultipleIndividualEvaluationCalculator();
    }

    private void parseCmdLine(String[] args) throws ParseException {
        Options opts = new Options();
        Option opt = OptionBuilder.isRequired().withArgName("t").withLongOpt("training-data").hasArg().withType(String.class).create();
        opts.addOption(opt);
        opt = OptionBuilder.isRequired().withArgName("e").withLongOpt("testing-data").hasArg().withType(String.class).create();
        opts.addOption(opt);
        opt = OptionBuilder.isRequired().withArgName("d").withLongOpt("dump-directory").hasArg().withType(String.class).create();
        opts.addOption(opt);
        opt = OptionBuilder.isRequired().withArgName("n").withLongOpt("num-repetition").hasArg().withType(Integer.class).create();
        opts.addOption(opt);
        opt = OptionBuilder.isRequired().withArgName("m").withLongOpt("models-to-average").hasArg().withType(Integer.class).create();
        opts.addOption(opt);
        opt = OptionBuilder.isRequired().withArgName("i").withLongOpt("individual").hasArg().withType(String.class).create();
        opts.addOption(opt);
        CommandLineParser clp = new GnuParser();
        CommandLine cl = clp.parse(opts, args);
        trainingDataName = cl.getOptionValue("training-data");
        testingDataName = cl.getOptionValue("testing-data");
        dumpDirectoryName = cl.getOptionValue("dump-directory");
        fitEvalNumber = Integer.parseInt(cl.getOptionValue("num-repetition"));
        numModelsToAverage = Integer.parseInt(cl.getOptionValue("models-to-average"));
        indivPath = cl.getOptionValue("individual");
    }

    private void loadData() throws FileNotFoundException {
        training = new SimplePreprocessingStorage();
        testing = new SimplePreprocessingStorage();
        try {
            LoadRAWPreprocessor loader = new LoadRAWPreprocessor();
            loader.getConfigurationClass().getParameterObjByKey("FileName").setValue(trainingDataName);
            loader.init(training);
            loader.run();
            loader.getConfigurationClass().getParameterObjByKey("FileName").setValue(testingDataName);
            loader.init(testing);
            loader.run();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            System.err.flush();
            System.out.flush();
            System.exit(-1);
        }
        XStream xstream = new XStream();
        FileReader fr = new FileReader(indivPath);
        indiv = (APAIndividual) xstream.fromXML(fr);
    }

    private void runSingleTest(int id) throws IOException {
        calculator.setupCalculator(training, config);
        double fitness = calculator.calculateFitness(indiv, link, config, training, testing);
        fw.write("Individual " + id + " fitness " + fitness + "\n");
    }

    public void runTests() throws IOException {
        fw = new FileWriter(dumpDirectoryName + "/fitness-test.txt");
        for (int i = 0; i < fitEvalNumber; i++) {
            runSingleTest(i);
        }
        fw.flush();
        fw.close();
    }

    public static void main(String[] args) {
        FitnessStabilityTesting fst = new FitnessStabilityTesting(args);
        try {
            fst.loadData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.flush();
            System.exit(-1);
        }
        try {
            fst.runTests();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.flush();
            System.exit(-1);
        }
    }
}
