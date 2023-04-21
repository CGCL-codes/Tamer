package configuration;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import configuration.classifiers.CfgClassifier;
import configuration.fakegame.*;
import configuration.models.CfgModel;
import configuration.models.game.CfgGame;
import configuration.models.game.CfgNeurons;
import configuration.models.game.CfgTrainers;
import configuration.models.game.algorithm.CfgDC;
import configuration.report.*;
import game.utils.UnitLoader;
import org.ytoh.configurations.ConfigurationException;
import org.ytoh.configurations.context.DefaultContext;
import org.ytoh.configurations.context.DefaultPublishingContext;
import org.ytoh.configurations.module.BasicModule;
import org.ytoh.configurations.module.Module;
import org.ytoh.configurations.module.Modules;
import org.ytoh.configurations.ui.SelectionSetModel;
import preprocessing.automatic.Configuration.GeneticConfig;
import preprocessing.methods.Preprocessor;
import weka.core.FastVector;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author ytoh
 */
public class MainConfigurationTree {

    private Module core;

    private static MainConfigurationTree instance = null;

    private Vector<EmbeddedConfiguration> classesWithEmbedConfig;

    private DefaultPublishingContext context;

    private MainConfigurationTree() {
        classesWithEmbedConfig = new Vector<EmbeddedConfiguration>(10);
    }

    private void initEmbeddedConfigs() {
        for (int i = 0; i < classesWithEmbedConfig.size(); i++) {
            classesWithEmbedConfig.elementAt(i).initializeEmbeddedConfiguration();
        }
        classesWithEmbedConfig.clear();
    }

    public void init() throws ConfigurationException {
        core = BasicModule.withName("core", Modules.getRootModule()).withComponent(new Object()).build();
        Module preprocessing = BasicModule.withName("preprocessing", Modules.getRootModule()).withComponent(new Object()).build();
        Module aprep = BasicModule.withName(GeneticConfig.configurationTreeString, preprocessing).withComponent(new GeneticConfig()).build();
        Module extensions = BasicModule.withName("extensions", Modules.getRootModule()).withComponent(new Object()).build();
        Module model = BasicModule.withName("model", core).withComponent(new CfgModel()).build();
        Module classifier = BasicModule.withName("classifier", core).withComponent(new CfgClassifier()).build();
        Module singlec = BasicModule.withName("single", classifier).build();
        Module ensemblec = BasicModule.withName("ensemble", classifier).build();
        Module single = BasicModule.withName("single", model).build();
        Module ensemble = BasicModule.withName("ensemble", model).build();
        Module game = BasicModule.withName("game", model).withComponent(new CfgGame()).build();
        Module algorithm = BasicModule.withName("algorithm", game).withComponent(new CfgDC()).build();
        Module neurons = BasicModule.withName("neurons", game).withComponent(new CfgNeurons()).build();
        Module trainers = BasicModule.withName("trainers", game).withComponent(new CfgTrainers()).build();
        Module reports = BasicModule.withName("reports", extensions).withComponent(new CfgReports()).build();
        Module fake = BasicModule.withName("fake", extensions).withComponent(new CfgReports()).build();
        Module tocReport = BasicModule.withName("toc", reports).withComponent(new TOCSRConfig()).build();
        Module projectTo2DReport = BasicModule.withName("projection 2d", reports).withComponent(new ProjectTo2DSRConfig()).build();
        Module experimentSeriesReport = BasicModule.withName("experiment series", reports).withComponent(new ExperimentSeriesSRConfig()).build();
        Module crossvalidationReport = BasicModule.withName("crossvalidation", reports).withComponent(new CrossValidationSRConfig()).build();
        Module modelReport = BasicModule.withName("model", reports).withComponent(new ModelSRConfig()).build();
        Module evaluationReport = BasicModule.withName("evaluation", reports).withComponent(new EvaluationSRConfig()).build();
        Module crossvalidationoldReport = BasicModule.withName("crossvalidationold", reports).withComponent(new CrossValidationSROldConfig()).build();
        Module featureRankingReport = BasicModule.withName("featureranking", reports).withComponent(new FeatureRankingSRConfig()).build();
        Module datasetVisualizationReport = BasicModule.withName("dataset visualization", reports).withComponent(new DatasetVisualizationSRConfig()).build();
        Module importFileOperation = BasicModule.withName("import file", fake).withComponent(new ImportFileConfig()).build();
        Module dataReduceOperation = BasicModule.withName("data reduce", fake).withComponent(new DataReduceConfig()).build();
        Module featureRankingOperation = BasicModule.withName("feature ranking", fake).withComponent(new FeatureRankingConfig()).build();
        Module projectTo2DOperation = BasicModule.withName("projection 2d", fake).withComponent(new ProjectTo2DConfig()).build();
        Module experimentSeriesOperation = BasicModule.withName("experiment series", fake).withComponent(new ExperimentSeriesConfig()).build();
        Module crossvalidationOperation = BasicModule.withName("crossvalidation", fake).withComponent(new CrossValidationStrategyConfig()).build();
        Module trainigTestingOperation = BasicModule.withName("training testing", fake).withComponent(new TrainingTestingStrategyConfig()).build();
        initEmbeddedConfigs();
        java.util.List<String> trainerList = new ArrayList<String>();
        java.util.List<String> modelList = new ArrayList<String>();
        java.util.List<String> neuronList = new ArrayList<String>();
        java.util.List<String> ensembleList = new ArrayList<String>();
        UnitLoader ul = UnitLoader.getInstance();
        Module[] trainer = new Module[ul.getTrainersNumber()];
        for (int i = 0; i < ul.getTrainersNumber(); i++) {
            trainer[i] = BasicModule.withName(ul.getTrainerName(i), trainers).withComponent(ul.getTrainerConfig(i)).build();
            trainerList.add(ul.getTrainerName(i));
        }
        Module[] neuron = new Module[ul.getNeuronsNumber()];
        for (int i = 0; i < ul.getNeuronsNumber(); i++) {
            neuron[i] = BasicModule.withName(ul.getNeuronName(i), neurons).withComponent(ul.getNeuronConfig(i)).build();
            neuronList.add(ul.getNeuronName(i));
        }
        Module[] models = new Module[ul.getModelsNumber()];
        for (int i = 0; i < ul.getModelsNumber(); i++) {
            models[i] = BasicModule.withName(ul.getModelName(i), single).withComponent(ul.getModelConfig(i)).build();
            modelList.add(ul.getModelName(i));
        }
        Module[] modelensembles = new Module[ul.getModelEnsemblesNumber()];
        for (int i = 0; i < ul.getModelEnsemblesNumber(); i++) {
            models[i] = BasicModule.withName(ul.getModelEnsembleName(i), ensemble).withComponent(ul.getModelEnsembleConfig(i)).build();
            ensembleList.add(ul.getModelEnsembleName(i));
        }
        Module[] classifiers = new Module[ul.getClassifiersNumber()];
        for (int i = 0; i < ul.getClassifiersNumber(); i++) {
            models[i] = BasicModule.withName(ul.getClassiferName(i), singlec).withComponent(ul.getClassiferConfig(i)).build();
        }
        Module[] classifierEnsembles = new Module[ul.getClassifierEnsemblesNumber()];
        for (int i = 0; i < ul.getClassifierEnsemblesNumber(); i++) {
            models[i] = BasicModule.withName(ul.getClassifierEnsembleName(i), ensemblec).withComponent(ul.getClassifierEnsembleConfig(i)).build();
        }
        ArrayList<Preprocessor> localMethods = UnitLoader.getInstance().getListOfMethod().getLocalMethods();
        ArrayList<Preprocessor> globalMethods = UnitLoader.getInstance().getListOfMethod().getGlobalMethods();
        SelectionSetModel<String> localMethodNamesModel = new SelectionSetModel<String>();
        SelectionSetModel<String> globalMethodNamesModel = new SelectionSetModel<String>();
        String[] methodsArray;
        Preprocessor p;
        methodsArray = new String[localMethods.size()];
        for (int i = 0; i < localMethods.size(); i++) {
            p = (Preprocessor) localMethods.get(i);
            methodsArray[i] = p.getPreprocessingMethodName();
        }
        localMethodNamesModel.setElements(methodsArray);
        localMethodNamesModel.enableAllElements();
        methodsArray = new String[globalMethods.size()];
        for (int i = 0; i < globalMethods.size(); i++) {
            p = (Preprocessor) globalMethods.get(i);
            methodsArray[i] = p.getPreprocessingMethodName();
        }
        globalMethodNamesModel.setElements(methodsArray);
        globalMethodNamesModel.enableAllElements();
        GeneticConfig.getConfiguration().getUsedMethodConfig().setLocalMethods(localMethodNamesModel);
        GeneticConfig.getConfiguration().getUsedMethodConfig().setGlobalMethods(globalMethodNamesModel);
        context = getContext();
        context.register(String.class, trainerList, "trainers");
        context.register(String.class, neuronList, "neurons");
        context.register(String.class, modelList, "models");
        context.register(String.class, ensembleList, "ensembles");
        saveConfigurationToFile(game);
    }

    public Module getRootModule() {
        return Modules.getRootModule();
    }

    public Module loadModuleFromFile(Module module) {
        String dirname = "cfg/" + Modules.getModuleFullName(module.getParent());
        return loadModuleFromFile(new File(dirname, module.getName() + ".properties").toString());
    }

    public Module loadModuleFromFile(String filename) {
        XStream xstream = new XStream(new DomDriver());
        xstream.setClassLoader(getClass().getClassLoader());
        Module m = null;
        try {
            File mfile = new File(filename);
            FileReader fr = new FileReader(mfile.getAbsolutePath());
            m = (Module) xstream.fromXML(fr);
            fr.close();
        } catch (Exception e) {
            return null;
        }
        return m;
    }

    public void saveConfigurationToFile(Module module) {
        XStream xstream = new XStream();
        try {
            String dirname = "cfg/" + Modules.getModuleFullName(module.getParent());
            (new File(dirname)).mkdirs();
            FileWriter fw = new FileWriter(new File(dirname, module.getName() + ".properties"));
            xstream.toXML(module, fw);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object loadObjectFromFile(String filename) {
        XStream xstream = new XStream(new DomDriver());
        xstream.setClassLoader(Thread.currentThread().getContextClassLoader());
        Object m = null;
        try {
            File mfile = new File(filename);
            FileReader fr = new FileReader(mfile.getAbsolutePath());
            m = xstream.fromXML(fr);
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return m;
    }

    public static void saveObjectToFile(Object module, String filename) {
        XStream xstream = new XStream();
        xstream.setClassLoader(Thread.currentThread().getContextClassLoader());
        try {
            FileWriter fw = new FileWriter(new File(filename));
            xstream.toXML(module, fw);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Module getAutomaticPreprocessingModule() {
        return Modules.getRootModule().findConfigurationByName(GeneticConfig.configurationTreeString);
    }

    public DefaultPublishingContext getContext() {
        if (context == null) {
            context = new DefaultPublishingContext(new DefaultContext());
        }
        return context;
    }

    public static MainConfigurationTree getInstance() {
        if (instance == null) {
            instance = new MainConfigurationTree();
            instance.init();
        }
        return instance;
    }

    public void registerClassWithEmbeddedConfig(EmbeddedConfiguration config) {
        classesWithEmbedConfig.add(config);
    }
}
