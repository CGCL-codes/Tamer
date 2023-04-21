package de.ibis.permoto.gui.preddesigner.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ListIterator;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import de.ibis.permoto.gui.preddesigner.model.mediator.AnalyticSolverMediator;
import de.ibis.permoto.gui.preddesigner.model.mediator.ClassMediator;
import de.ibis.permoto.gui.preddesigner.model.mediator.ClassPartMediator;
import de.ibis.permoto.gui.preddesigner.model.mediator.DescriptionMediator;
import de.ibis.permoto.gui.preddesigner.model.mediator.PredictionMediator;
import de.ibis.permoto.gui.preddesigner.model.mediator.SimulationSolverMediator;
import de.ibis.permoto.gui.preddesigner.model.mediator.StationMediator;
import de.ibis.permoto.gui.preddesigner.model.tree.PValuesTreeModel;
import de.ibis.permoto.gui.preddesigner.model.tree.PVlauesTreeRenderer;
import de.ibis.permoto.gui.preddesigner.model.tree.TreeNodeElementType;
import de.ibis.permoto.gui.solver.PerMoToSolverGUI;
import de.ibis.permoto.model.basic.predictionbusinesscase.PredictionBusinessCase;
import de.ibis.permoto.model.basic.scenario.Class;
import de.ibis.permoto.model.definitions.IBusinessCase;
import de.ibis.permoto.model.definitions.IPredictionBusinessCase;
import de.ibis.permoto.model.definitions.IStationSection;
import de.ibis.permoto.model.definitions.impl.PerMoToBusinessCase;
import de.ibis.permoto.model.definitions.impl.PerMoToPredictionBusinessCase;
import de.ibis.permoto.model.util.ModelPersistenceManager;

/**
 * @author Slavko Segota
 *
 */
public class PredictionDesignerModelManager {

    private boolean isModelLoaded;

    private IPredictionBusinessCase pbc;

    private Vector<StructuredWorkflow> workflows;

    @SuppressWarnings("unused")
    private JFrame owner;

    private File currentXMLFile;

    private final String predictionFileExtension = "ppm";

    private AnalyticSolverMediator analyticMediator;

    private SimulationSolverMediator simulativeMediator;

    private DescriptionMediator descriptionMediator;

    private ArrayList<PredictionMediator> predictionMediators;

    private PValuesTreeModel treeModel;

    private DefaultMutableTreeNode treeRoot;

    private Hashtable<String, StationMediator> stationMediators;

    public PredictionDesignerModelManager(JFrame owner) {
        this.predictionMediators = new ArrayList<PredictionMediator>();
        this.isModelLoaded = false;
        this.owner = owner;
    }

    public IPredictionBusinessCase getPredictionBusinessCase() {
        return this.pbc;
    }

    public boolean loadPerMoToModelBusinessCaseFromFile(File toLoad) {
        try {
            this.pbc = new PerMoToPredictionBusinessCase((PerMoToBusinessCase) ModelPersistenceManager.loadIBusinessCaseFromFile(toLoad));
            String fileName = toLoad.getAbsolutePath();
            fileName = fileName.substring(0, fileName.length() - 3);
            fileName += predictionFileExtension;
            this.pbc.setFullScenarioName(fileName);
            this.pbc.setPredictionScenarioName((fileName.subSequence(fileName.lastIndexOf(File.separator) + 1, fileName.length())).toString());
            this.pbc.getBasicBusinessCase().setFullScenarioName(toLoad.getAbsolutePath());
            this.pbc.getBasicBusinessCase().setScenarioName(toLoad.getName());
            this.currentXMLFile = new File(fileName);
            this.initializeModelData();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadPerMoToPredictionBusinessCaseFromFile(File toLoad) {
        try {
            this.pbc = ModelPersistenceManager.loadPredictionBusinessCaseFromFile(toLoad);
            this.pbc.setFullScenarioName(toLoad.getAbsolutePath());
            this.pbc.setPredictionScenarioName(toLoad.getName());
            this.pbc.getBasicBusinessCase().setFullScenarioName(this.pbc.getDescriptionSection().getPmtFileName());
            this.pbc.getBasicBusinessCase().setScenarioName(this.pbc.getDescriptionSection().getPmtFileName());
            this.currentXMLFile = toLoad;
            this.initializeModelData();
            this.treeModel.updateNodes();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void reloadPerMoToPredictionBusinessCaseFromFileForExport() {
        try {
            File toLoad = new File(this.pbc.getFullPredictionScenarioName());
            this.pbc = ModelPersistenceManager.loadPredictionBusinessCaseFromFile(toLoad);
            this.pbc.setFullScenarioName(toLoad.getAbsolutePath());
            this.pbc.setPredictionScenarioName(toLoad.getName());
            this.pbc.getBasicBusinessCase().setFullScenarioName(this.pbc.getDescriptionSection().getPmtFileName());
            this.pbc.getBasicBusinessCase().setScenarioName(this.pbc.getDescriptionSection().getPmtFileName());
            this.currentXMLFile = toLoad;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeModelData(IPredictionBusinessCase pbc) {
        this.pbc = pbc;
        this.initializeModelData();
    }

    public boolean isModelLoaded() {
        return this.isModelLoaded;
    }

    public File getCurrentXMLFile() {
        return this.currentXMLFile;
    }

    public Vector<StructuredWorkflow> getStructuredWorkFlows() {
        return this.workflows;
    }

    private void initializeModelData() {
        if (this.pbc != null) {
            this.isModelLoaded = true;
            this.analyticMediator = new AnalyticSolverMediator(this.pbc.getPredictionSolverSection(), this.pbc.getBasicBusinessCase());
            this.simulativeMediator = new SimulationSolverMediator(this.pbc);
            this.descriptionMediator = new DescriptionMediator(this.pbc);
            this.stationMediators = this.createStationMediators();
            this.predictionMediators.addAll(this.stationMediators.values());
            this.createWorkFlowStructure();
            this.createTreeStrucutre();
        }
    }

    /**
	 * @return
	 */
    private Hashtable<String, StationMediator> createStationMediators() {
        Hashtable<String, StationMediator> stationMediators = new Hashtable<String, StationMediator>();
        IStationSection iss = this.pbc.getBasicBusinessCase().getStationSection();
        ListIterator<String> stationIDIterator = iss.getAllStationIDs().listIterator();
        String stationID;
        while (stationIDIterator.hasNext()) {
            stationID = stationIDIterator.next();
            stationMediators.put(stationID, new StationMediator(this.pbc.getPredictionStationSection(), iss.getStationByID(stationID)));
        }
        return stationMediators;
    }

    private void createWorkFlowStructure() {
        this.workflows = new Vector<StructuredWorkflow>();
        ListIterator<Class> i = this.pbc.getBasicBusinessCase().getClassSection().getAllClasses().listIterator();
        while (i.hasNext()) {
            this.workflows.add(new StructuredWorkflow(i.next(), this.pbc.getBasicBusinessCase().getClassSection(), this.pbc.getBasicBusinessCase().getStationSection()));
        }
    }

    private void createTreeStrucutre() {
        this.treeRoot = new DefaultMutableTreeNode("Workflows");
        this.treeModel = new PValuesTreeModel(treeRoot);
        ListIterator<StructuredWorkflow> wf = this.workflows.listIterator();
        while (wf.hasNext()) {
            StructuredWorkflow s = wf.next();
            DefaultMutableTreeNode workflow = new DefaultMutableTreeNode();
            ClassMediator cm = new ClassMediator(this.pbc.getPredictionClassSection(), s.getClazz(), treeModel, workflow);
            treeModel.registerForUpdates(cm);
            workflow.setUserObject(cm);
            this.predictionMediators.add(cm);
            ListIterator<StructuredItem> it = s.getItems().listIterator();
            while (it.hasNext()) {
                StructuredItem i = it.next();
                DefaultMutableTreeNode item = new DefaultMutableTreeNode();
                ClassPartMediator cpm = new ClassPartMediator(this.pbc, i.getClassPart(), TreeNodeElementType.ITEM, treeModel, item, this.stationMediators.get(i.getClassPart().getStationID()));
                treeModel.registerForUpdates(cpm);
                item.setUserObject(cpm);
                this.predictionMediators.add(cpm);
                ListIterator<StructuredService> services = i.getServices().listIterator();
                while (services.hasNext()) {
                    this.extractServicesRecursively(item, services.next());
                }
                workflow.add(item);
            }
            treeRoot.add(workflow);
        }
    }

    private void extractServicesRecursively(DefaultMutableTreeNode parent, StructuredService service) {
        DefaultMutableTreeNode serviceNode = new DefaultMutableTreeNode();
        ClassPartMediator cpm2 = new ClassPartMediator(this.pbc, service.getClassPart(), TreeNodeElementType.SERVICE, treeModel, serviceNode, this.stationMediators.get(service.getClassPart().getStationID()));
        treeModel.registerForUpdates(cpm2);
        serviceNode.setUserObject(cpm2);
        ListIterator<StructuredService> services = service.getServices().listIterator();
        while (services.hasNext()) {
            this.extractServicesRecursively(serviceNode, services.next());
        }
        parent.add(serviceNode);
        this.predictionMediators.add(cpm2);
    }

    public JTree createJTreeFromWorkflowStructure() {
        if (this.isModelLoaded()) {
            JTree tree = new JTree(this.treeModel);
            PVlauesTreeRenderer renderer = new PVlauesTreeRenderer();
            tree.setCellRenderer(renderer);
            return tree;
        } else {
            return null;
        }
    }

    public AnalyticSolverMediator getAnalyticSolverMediator() {
        return this.analyticMediator;
    }

    public SimulationSolverMediator getSimulationSolverMediator() {
        return this.simulativeMediator;
    }

    public DescriptionMediator getDescriptionMediator() {
        return this.descriptionMediator;
    }

    public boolean existChangesToSave() {
        if (this.isModelLoaded()) {
            if (this.analyticMediator.isChangedDuringSession()) {
                return true;
            }
            if (this.simulativeMediator.isChangedDuringSession()) {
                return true;
            }
            if (this.descriptionMediator.isChangedDuringSession()) {
                return true;
            }
            ListIterator<PredictionMediator> iterator = this.predictionMediators.listIterator();
            while (iterator.hasNext()) {
                if (iterator.next().isChangedDuringSession()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean havePredictionMediatorsNotAppliedChanges() {
        ListIterator<PredictionMediator> iterator = this.predictionMediators.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next().hasNotAppliedChanges()) {
                return true;
            }
        }
        return false;
    }

    public PerMoToSolverGUI createSolverInstanceWithExportedModel() {
        this.reloadPerMoToPredictionBusinessCaseFromFileForExport();
        if (this.isModelLoaded) {
            this.analyticMediator.applyToModel();
            this.simulativeMediator.applyToModel();
            return new PerMoToSolverGUI(this.pbc);
        } else {
            return null;
        }
    }

    public boolean writePrecitionBusinessCaseToFile() {
        if (this.isModelLoaded()) {
            try {
                this.analyticMediator.applyToModel();
                this.simulativeMediator.applyToModel();
                this.descriptionMediator.applyToModel();
                this.currentXMLFile = new File(this.pbc.getFullPredictionScenarioName());
                ModelPersistenceManager.savePredictionBusinessCase((PredictionBusinessCase) this.pbc, this.currentXMLFile);
                this.resetIsChangedDuringSession();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    private void resetIsChangedDuringSession() {
        ListIterator<PredictionMediator> li = this.predictionMediators.listIterator();
        while (li.hasNext()) {
            li.next().setIsChangedDuringSession(false);
        }
        this.analyticMediator.setIsChangedDuringSession(false);
        this.simulativeMediator.setIsChangedDuringSession(false);
        this.descriptionMediator.setIsChangedDuringSession(false);
    }

    public boolean writePredictionBusinessCaseToNewFile(File newFile) {
        if (this.isModelLoaded) {
            try {
                this.analyticMediator.applyToModel();
                this.simulativeMediator.applyToModel();
                this.pbc.setFullScenarioName(newFile.getAbsolutePath());
                this.pbc.setPredictionScenarioName(newFile.getName());
                this.currentXMLFile = newFile;
                ModelPersistenceManager.savePredictionBusinessCase((PredictionBusinessCase) this.pbc, this.currentXMLFile);
                this.descriptionMediator.reloadValuesFromModel(true);
                this.resetIsChangedDuringSession();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public IBusinessCase getBasicBusinessCase() {
        if (this.isModelLoaded) {
            return this.pbc.getBasicBusinessCase();
        } else {
            return null;
        }
    }

    public String checkPredictionBusinessCaseForSave() {
        if (this.isModelLoaded()) {
            return this.pbc.checkForSave();
        } else {
            return "No model loaded!";
        }
    }

    public String getPredictionScenarioName() {
        if (this.isModelLoaded()) {
            return this.pbc.getPredictionScenarioName();
        } else {
            return "No model loaded";
        }
    }

    public Hashtable<String, StationMediator> getStationMediators() {
        return this.stationMediators;
    }

    public void applyAllChangesToModel() {
        ListIterator<PredictionMediator> mediators = this.predictionMediators.listIterator();
        PredictionMediator mediator = null;
        while (mediators.hasNext()) {
            mediator = mediators.next();
            if (mediator.hasNotAppliedChanges()) {
                mediator.applyToModel();
            }
        }
    }

    public void removeAllPredictedValuesFromModel() {
        ListIterator<PredictionMediator> mediators = this.predictionMediators.listIterator();
        PredictionMediator mediator = null;
        while (mediators.hasNext()) {
            mediator = mediators.next();
            if (mediator.hasPredictedValues()) {
                mediator.removePredictedValuesFromModel();
            }
        }
    }

    public void reloadAllDataFromModel() {
        ListIterator<PredictionMediator> mediators = this.predictionMediators.listIterator();
        PredictionMediator mediator = null;
        while (mediators.hasNext()) {
            mediator = mediators.next();
            if (mediator.hasNotAppliedChanges()) {
                mediator.reloadValuesFromModel(true);
            }
        }
    }
}
