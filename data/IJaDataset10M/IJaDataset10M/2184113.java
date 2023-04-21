package preprocessing.automatic.GUI.IndividualApplicator;

import MainConfigurationTree.FileChooserDialog;
import game.utils.Exceptions.InvalidArgument;
import game.utils.Exceptions.NonExistingAttributeException;
import org.apache.log4j.Logger;
import preprocessing.LoggerManager;
import preprocessing.Parameters.Parameter;
import preprocessing.automatic.Configuration.GeneticConfig;
import preprocessing.automatic.GUI.IndividualApplicator.Tabs.IndividualApplicator;
import preprocessing.automatic.GUI.IndividualApplicator.Tabs.ResultManagerIndividualsInterface;
import preprocessing.automatic.GUI.IndividualApplicator.Tabs.ResultManagerIndividualsTabController;
import preprocessing.automatic.GUI.NewPreprocessingModel;
import preprocessing.automatic.Population.APAIndividual;
import preprocessing.automatic.ResultManager.ResultManager;
import preprocessing.methods.PreprocessingUnitHolder;
import preprocessing.methods.Preprocessor;
import preprocessing.storage.SimplePreprocessingStorage;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: cepekm1
 * Date: 1/26/11
 * Time: 10:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndividualApplicatorControl implements ActionListener, ResultManagerIndividualsInterface {

    private IndividualApplicatorView view;

    private NewPreprocessingModel dataModel;

    private Logger logger;

    private SimplePreprocessingStorage trainingData;

    private SimplePreprocessingStorage testingData;

    private ArrayList<IndividualApplicator> tabs;

    private ResultManager resultManager;

    private PreprocessingUnitHolder pHolder;

    public IndividualApplicatorControl(PreprocessingUnitHolder pHolder) {
        GeneticConfig config = new GeneticConfig(new PreprocessingUnitHolder());
        resultManager = new ResultManager(config.getAdvancedConfig().getDumpDirectory());
        this.pHolder = pHolder;
        view = new IndividualApplicatorView(this);
        dataModel = new NewPreprocessingModel(resultManager, config);
        view.setApplyIndividualsEnabled(false);
        view.setDropAllDataEnabled(false);
        view.setDropTestingDataEnabled(false);
        view.setLoadTestingDataEnabled(true);
        view.setDropTrainingDataEnabled(false);
        view.setTestingDataFilename();
        view.setTrainingFilename();
        tabs = new ArrayList<IndividualApplicator>(2);
        ResultManagerIndividualsTabController rmitc = new ResultManagerIndividualsTabController(this, resultManager);
        tabs.add(rmitc);
        view.addTab(rmitc.getTabPanel(), rmitc.getTabName());
        logger = LoggerManager.getInstance().getLogger(LoggerManager.GUILoggerString);
    }

    public JPanel getView() {
        return view;
    }

    private String loadDataTo(SimplePreprocessingStorage store) throws NoSuchFieldException, NonExistingAttributeException, InvalidArgument {
        JFileChooser fc = FileChooserDialog.getInstance().getFileChooser();
        fc.setDialogTitle("Select data to load");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        if (fc.showOpenDialog(view) != JFileChooser.APPROVE_OPTION) {
            logger.warn("User canceled");
            return null;
        }
        Preprocessor ld = pHolder.getMethodByName("Load RAW Data");
        ld.getConfigurationClass().getParameterObjByKey("FileName").setValue(fc.getSelectedFile().getAbsolutePath());
        ld.init(store);
        if (ld.run()) {
            ld.finish();
            return fc.getSelectedFile().getAbsolutePath();
        }
        ld.finish();
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand().compareTo("Load data") == 0) {
            try {
                SimplePreprocessingStorage tmp = new SimplePreprocessingStorage();
                String s = loadDataTo(tmp);
                if (s != null) {
                    trainingData = tmp;
                    view.setTrainingFilename(s);
                    view.setDropTrainingDataEnabled(true);
                    view.setLoadTrainingDataEnabled(false);
                    view.setApplyIndividualsEnabled(true);
                    if (testingData != null) {
                        view.setDropAllDataEnabled(true);
                    }
                }
            } catch (NonExistingAttributeException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                logger.fatal("No configuration field called FileName was found :(. This is something really really odd.");
                System.exit(-1);
            } catch (InvalidArgument invalidArgument) {
                invalidArgument.printStackTrace();
                logger.fatal("Unable to locate RAW data loader.... :(. This is something really really odd.");
                System.exit(-1);
            }
        } else if (actionEvent.getActionCommand().compareTo("Drop training data") == 0) {
            trainingData = null;
            view.setDropTrainingDataEnabled(false);
            view.setLoadTrainingDataEnabled(true);
            view.setDropAllDataEnabled(false);
            view.setTrainingFilename();
            if (testingData == null) {
                view.setApplyIndividualsEnabled(false);
            }
        } else if (actionEvent.getActionCommand().compareTo("Load testing data") == 0) {
            try {
                SimplePreprocessingStorage tmp = new SimplePreprocessingStorage();
                String s = loadDataTo(tmp);
                if (s != null) {
                    testingData = tmp;
                    view.setTestingDataFilename(s);
                    view.setDropTestingDataEnabled(true);
                    view.setLoadTestingDataEnabled(false);
                    view.setApplyIndividualsEnabled(true);
                    if (trainingData != null) {
                        view.setDropAllDataEnabled(true);
                    }
                }
            } catch (NonExistingAttributeException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                logger.fatal("No configuration field called FileName was found :(. This is something really really odd.");
                System.exit(-1);
            } catch (InvalidArgument invalidArgument) {
                invalidArgument.printStackTrace();
                logger.fatal("Unable to locate RAW data loader.... :(. This is something really really odd.");
                System.exit(-1);
            }
        } else if (actionEvent.getActionCommand().compareTo("Drop testing data") == 0) {
            testingData = null;
            view.setDropTestingDataEnabled(false);
            view.setLoadTestingDataEnabled(true);
            view.setDropAllDataEnabled(false);
            view.setTestingDataFilename();
            if (trainingData == null) {
                view.setApplyIndividualsEnabled(false);
            }
        } else if (actionEvent.getActionCommand().compareTo("Apply selected individuals") == 0) {
            try {
                JFileChooser fc = FileChooserDialog.getInstance().getFileChooser();
                fc.setDialogTitle("Select directory to save data to:");
                fc.setMultiSelectionEnabled(false);
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (fc.showSaveDialog(view) != JFileChooser.APPROVE_OPTION) {
                    logger.warn("User canceled");
                    return;
                }
                String dirSep = System.getProperty("file.separator");
                IndividualApplicator applicator = tabs.get(view.getActiveTabIndex());
                Iterator<APAIndividual> iterator = applicator.getSelectedIndividuals();
                Preprocessor st = pHolder.getMethodByName("Export data in Weka ARFF format");
                Parameter p = st.getConfigurationClass().getParameterObjByKey("FileName");
                while (iterator.hasNext()) {
                    APAIndividual indiv = iterator.next();
                    SimplePreprocessingStorage tmpStore;
                    if (trainingData != null) {
                        tmpStore = (SimplePreprocessingStorage) trainingData.copy();
                        indiv.applyIndividual(tmpStore, false);
                        String indName = String.format("Individual-R%05d-G%05d-I%05d.training.arff", indiv.getRunID(), indiv.getGenerationID(), indiv.getIndividualID());
                        p.setValue(fc.getSelectedFile().getAbsolutePath() + dirSep + indName);
                        st.init(tmpStore);
                        st.run();
                        st.finish();
                    }
                    if (testingData != null) {
                        tmpStore = (SimplePreprocessingStorage) testingData.copy();
                        indiv.applyIndividual(tmpStore, true);
                        String indName = String.format("Individual-R%05d-G%05d-I%05d.testing.arff", indiv.getRunID(), indiv.getGenerationID(), indiv.getIndividualID());
                        p.setValue(fc.getSelectedFile().getAbsolutePath() + dirSep + indName);
                        st.init(tmpStore);
                        st.run();
                        st.finish();
                    }
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                System.exit(-1);
            } catch (InvalidArgument invalidArgument) {
                invalidArgument.printStackTrace();
                System.err.printf("Preprocessing method \"Save RAW Data\" does not exist :(.");
                System.exit(-1);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                System.err.printf("Preprocessing method \"Save RAW Data\" does not  ");
            } catch (NonExistingAttributeException e) {
                e.printStackTrace();
            }
        } else if (actionEvent.getActionCommand().compareTo("Drop all data") == 0) {
            trainingData = null;
            testingData = null;
            view.setTestingDataFilename();
            view.setTrainingFilename();
            view.setDropTestingDataEnabled(false);
            view.setLoadTestingDataEnabled(true);
            view.setDropTrainingDataEnabled(false);
            view.setLoadTrainingDataEnabled(true);
            view.setDropAllDataEnabled(false);
            view.setApplyIndividualsEnabled(false);
        }
    }

    public static void main(String[] args) {
        IndividualApplicatorControl ctrl = new IndividualApplicatorControl(new PreprocessingUnitHolder());
        JFrame frame = new JFrame("Individual applicator");
        frame.setContentPane(ctrl.getView());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void loadResultManagerData(String absolutePath) throws IOException {
        dataModel.loadResultManagerData(absolutePath);
    }

    public void individualsLoaded() {
        view.setApplyIndividualsEnabled(true);
    }
}
