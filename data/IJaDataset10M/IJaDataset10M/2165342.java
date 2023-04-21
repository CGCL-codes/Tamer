package de.ibis.permoto.solver.as.tech;

import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import de.ibis.permoto.gui.solver.panels.AnalyticSolverPanel;
import de.ibis.permoto.model.basic.scenario.Class;
import de.ibis.permoto.solver.as.util.AlgorithmInput;
import de.ibis.permoto.solver.as.util.AnalyticQNStation;
import de.ibis.permoto.solver.as.util.HashTable;
import de.ibis.permoto.util.db.ClassResultBean;
import de.ibis.permoto.util.db.ClassStationResultBean;
import de.ibis.permoto.util.db.DBManager;

/**
 * Computes an open queueing network without loaddependent stations. It is
 * analogous to Menasce (Performance by design) page 366.
 * @author Christian Markl
 * @author Oliver Huehn
 */
public class OpenMM1 implements QNAlgorithm {

    /** Logger for this class */
    private static final Logger logger = Logger.getLogger(OpenMM1.class);

    /** The manager for the interaction with the db. */
    private DBManager dbManager = DBManager.getInstance();

    /** The inputparameters for the MVA. */
    private AlgorithmInput inputParameter;

    /** Unique id of the solution which is computed in the db. */
    private int solutionID;

    /** Unique id of the inserted scenario. */
    private int scenarioID;

    /** Start time of the execution. */
    private Timestamp startTime;

    /** Hashtable for the queuelength of a station over all classes. */
    private HashTable tableQueuelengthPerStation;

    /** Hashtable for the queuelength of a station per class. */
    private HashTable tableQueuelengthPerStationAndClass;

    /** Hashtable for the responsetime of a class over all station. */
    private HashTable tableResponsetimePerClass;

    /** Hashtable for the responsetime of a class per station. */
    private HashTable tableResponsetimePerClassAndStation;

    /** Hashtable for the utilisation of a station over all classes. */
    private HashTable tableUtilisationPerStation;

    /** Hashtable for the utilisation of a station per class. */
    private HashTable tableUtilisationPerStationAndClass;

    /**
	 * The constructor of the class.
	 * @param input The input parameters for the algorithm
	 */
    public OpenMM1(final AlgorithmInput input) {
        startTime = new Timestamp(System.currentTimeMillis());
        this.inputParameter = input;
        this.initialiseDB();
    }

    /**
	 * Adds the response times of subservice calls to the responsetimes of the
	 * corresponding parent.
	 */
    public final void addSubserviceCallMetricsToParent() {
        for (Class currClass : this.inputParameter.getCustomerClasses()) {
            for (int i = this.inputParameter.getMaxSubserviceLevel(); i >= 0; i--) {
                for (int j = this.inputParameter.getSubserviceParentRelationships().size() - 1; j >= 0; j--) {
                    List<Object> entries = this.inputParameter.getSubserviceParentRelationships().get(j);
                    if (entries.get(0).equals(currClass.getClassID()) && ((Integer) entries.get(3)).intValue() == i) {
                        this.tableResponsetimePerClassAndStation.write(currClass.getClassID(), (String) entries.get(1), this.tableResponsetimePerClassAndStation.read(currClass.getClassID(), (String) entries.get(1)) + this.tableResponsetimePerClassAndStation.read(currClass.getClassID(), (String) entries.get(2)));
                    }
                }
            }
        }
    }

    /**
	 * Controls the computation of the queueing network. For each loadfactor one
	 * result is computed.
	 * @return int analysisID to initialize resultGUI
	 */
    public final int compute(final boolean fromWebService) {
        for (int i = 0; i < this.inputParameter.getLoadfactors().length; i++) {
            logger.info("Calculate load factor " + this.inputParameter.getLoadfactors()[i]);
            AnalyticSolverPanel.whatIfNextStepStatusChange();
            this.generateHashtablesForNewIteration();
            final boolean loadfactorIsAllowed = this.computeUtilisations(this.inputParameter.getLoadfactors()[i]);
            if (loadfactorIsAllowed) {
                final int station = this.computeQueuelengths(this.inputParameter.getLoadfactors()[i]);
                if (station == -1) {
                    this.computeResponsetimes(this.inputParameter.getLoadfactors()[i]);
                    if (i == 0) {
                        this.solutionID = this.dbManager.insertScenarioAnalysis(this.scenarioID, "OpenMM1", "Description", this.inputParameter.getWhatIFParameters(), startTime);
                    }
                    logger.debug("SolutionID in Derby DBMS is " + this.solutionID);
                    this.addSubserviceCallMetricsToParent();
                    this.insertScenarioAndSolution(this.inputParameter.getLoadfactors()[i]);
                } else {
                    if (i == 0) {
                        logger.info("The model can�t be solved because the maximum queuelength is exceeded at least" + " at one station!");
                        if (!fromWebService) {
                            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "The station " + ((AnalyticQNStation) this.inputParameter.getStations().get(station)).getStationID() + " " + ((AnalyticQNStation) this.inputParameter.getStations().get(station)).getStationName() + " has got a greater queue length than the allowed one! The computation is aborted.", "Information", JOptionPane.INFORMATION_MESSAGE);
                        }
                        i++;
                        while (i < this.inputParameter.getLoadfactors().length) {
                            AnalyticSolverPanel.whatIfNextStepStatusChange();
                            i++;
                        }
                        this.dbManager.deleteScenarioDescription(this.scenarioID);
                        return -1;
                    } else {
                        logger.info("The model can�t be solved because the maximum queuelength is exceeded at least" + " at one station!");
                        logger.info("Last complete computed loadfactor was " + this.inputParameter.getLoadfactors()[i - 1]);
                        if (!fromWebService) {
                            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "The station " + ((AnalyticQNStation) this.inputParameter.getStations().get(station)).getStationID() + " " + ((AnalyticQNStation) this.inputParameter.getStations().get(station)).getStationName() + " has got a greater queue length than the allowed one! The computation is aborted.", "Information", JOptionPane.INFORMATION_MESSAGE);
                        }
                        String newWhatIFParameters = "MaxLoadFactor: " + this.inputParameter.getLoadfactors()[i - 1] + " NrOfSteps: " + i;
                        this.dbManager.updateWhatIFParameters(this.solutionID, newWhatIFParameters);
                        i++;
                        while (i < this.inputParameter.getLoadfactors().length) {
                            AnalyticSolverPanel.whatIfNextStepStatusChange();
                            i++;
                        }
                        break;
                    }
                }
            } else {
                if (i == 0) {
                    logger.info("Stations utilisation is at 100% for the model!");
                    if (!fromWebService) {
                        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Stations utilisation is at 100% for the model!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    }
                    i++;
                    while (i < this.inputParameter.getLoadfactors().length) {
                        AnalyticSolverPanel.whatIfNextStepStatusChange();
                        i++;
                    }
                    this.dbManager.deleteScenarioDescription(this.scenarioID);
                    return -1;
                } else {
                    logger.info("Stations utilisation is at 100%! The computation is aborted..." + " Last complete computed loadfactor was " + this.inputParameter.getLoadfactors()[i - 1]);
                    if (!fromWebService) {
                        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Stations utilisation is at 100%! The computation is aborted..." + " Last complete computed loadfactor was " + this.inputParameter.getLoadfactors()[i - 1], "Information", JOptionPane.INFORMATION_MESSAGE);
                    }
                    String newWhatIFParameters = "MaxLoadFactor: " + this.inputParameter.getLoadfactors()[i - 1] + " NrOfSteps: " + i;
                    this.dbManager.updateWhatIFParameters(this.solutionID, newWhatIFParameters);
                    i++;
                    while (i < this.inputParameter.getLoadfactors().length) {
                        AnalyticSolverPanel.whatIfNextStepStatusChange();
                        i++;
                    }
                    break;
                }
            }
        }
        return (int) this.solutionID;
    }

    /**
	 * Computes the queuelengths of the open queueing network.
	 * @param loadfactor actual loadfactor for which the model is solved
	 * @return -1 if computation successful, else station at which the queue
	 *         length exceeds the maximum allowed
	 */
    private int computeQueuelengths(final double loadfactor) {
        for (int s = 0; s < this.inputParameter.getStations().size(); s++) {
            final AnalyticQNStation station = (AnalyticQNStation) this.inputParameter.getStations().get(s);
            double queuelength = 0.0;
            for (int c = 0; c < this.inputParameter.getCustomerClasses().size(); c++) {
                final Class customerClass = (Class) this.inputParameter.getCustomerClasses().get(c);
                final double utilisationOfThisStationAtThisClass = this.tableUtilisationPerStationAndClass.read(station.getStationID(), customerClass.getClassID());
                final double utilisationOfThisStation = this.tableUtilisationPerStation.read(station.getStationID());
                double queuelengthPerClass = 0.0;
                if (station.isDelay()) {
                    queuelengthPerClass = utilisationOfThisStationAtThisClass;
                } else {
                    queuelengthPerClass = utilisationOfThisStationAtThisClass / (1 - utilisationOfThisStation);
                }
                logger.debug("Queuelength at station " + station.getStationID() + " " + station.getStationName() + " for class " + customerClass.getClassID() + " " + customerClass.getClassName() + " for loadfactor " + loadfactor + " = " + queuelengthPerClass);
                queuelength = queuelength + queuelengthPerClass;
                this.tableQueuelengthPerStationAndClass.write(station.getStationID(), customerClass.getClassID(), queuelengthPerClass);
            }
            logger.debug("Complete queuelength at station " + station.getStationID() + " " + station.getStationName() + " for loadfactor " + loadfactor + " = " + queuelength);
            this.tableQueuelengthPerStation.write(station.getStationID(), queuelength);
            if (station.getQueueLength() != -1 && queuelength > station.getQueueLength()) {
                logger.info("The maximum allowed queuelength at the station " + station.getStationID() + " " + station.getStationName() + " is exceeded for loadfactor " + loadfactor);
                return s;
            }
        }
        return -1;
    }

    /**
	 * Computes the responstimes of the open queueing network.
	 * @param loadfactor actual loadfactor for which the model is solved
	 */
    private void computeResponsetimes(final double loadfactor) {
        for (int c = 0; c < this.inputParameter.getCustomerClasses().size(); c++) {
            final Class customerClass = (Class) this.inputParameter.getCustomerClasses().get(c);
            double responsetime = 0.0;
            for (int s = 0; s < this.inputParameter.getStations().size(); s++) {
                final AnalyticQNStation station = (AnalyticQNStation) this.inputParameter.getStations().get(s);
                double responsetimePerClass = 0.0;
                final double utilisationOfThisStation = this.tableUtilisationPerStation.read(station.getStationID());
                if (station.isDelay()) {
                    responsetimePerClass = this.inputParameter.getServiceDemand(station.getStationID(), customerClass.getClassID());
                } else if (station.isLI()) {
                    responsetimePerClass = this.inputParameter.getServiceDemand(station.getStationID(), customerClass.getClassID()) / (1 - utilisationOfThisStation);
                }
                logger.debug("Responsetime for class " + customerClass.getClassID() + " " + customerClass.getClassName() + " at station " + station.getStationID() + " " + station.getStationName() + " for loadfactor " + loadfactor + " = " + responsetimePerClass);
                responsetime = responsetime + responsetimePerClass;
                this.tableResponsetimePerClassAndStation.write(customerClass.getClassID(), station.getStationID(), responsetimePerClass);
            }
            logger.debug("Complete responsetime for class " + customerClass.getClassID() + " " + customerClass.getClassName() + " for loadfactor " + loadfactor + " = " + responsetime);
            this.tableResponsetimePerClass.write(customerClass.getClassID(), responsetime);
        }
    }

    /**
	 * Computes the utilisation of the open queueing network. If the utilisation
	 * is greater or equal to 100% the algorithm returns false.
	 * @param loadfactor actual loadfactor for which the model is solved
	 * @return true if utilisation is less than 100% false if utilisation is
	 *         greater or equal to 100%
	 */
    private boolean computeUtilisations(final double loadfactor) {
        for (int s = 0; s < this.inputParameter.getStations().size(); s++) {
            final AnalyticQNStation station = (AnalyticQNStation) this.inputParameter.getStations().get(s);
            double utilisationOfThisStation = 0.0;
            for (int c = 0; c < this.inputParameter.getCustomerClasses().size(); c++) {
                final Class customerClass = (Class) this.inputParameter.getCustomerClasses().get(c);
                double utilisationOfThisStationForThisClass = 0.0;
                if (!station.isDelay()) {
                    final double serviceDemand = this.inputParameter.getServiceDemand(station.getStationID(), customerClass.getClassID());
                    final double arrivalRate = loadfactor * this.inputParameter.getArrivalrate(customerClass.getClassID());
                    utilisationOfThisStationForThisClass = arrivalRate * serviceDemand;
                    logger.debug("Utilisation of station " + station.getStationID() + " " + station.getStationName() + " for class " + customerClass.getClassID() + " " + customerClass.getClassName() + " for loadfactor " + loadfactor + " = " + utilisationOfThisStationForThisClass);
                }
                if (utilisationOfThisStationForThisClass >= 1) {
                    return false;
                } else {
                    this.tableUtilisationPerStationAndClass.write(station.getStationID(), customerClass.getClassID(), utilisationOfThisStationForThisClass);
                }
                utilisationOfThisStation = utilisationOfThisStation + utilisationOfThisStationForThisClass;
            }
            logger.debug("Complete utilisation of station " + station.getStationID() + " " + station.getStationName() + " for loadfactor " + loadfactor + " = " + utilisationOfThisStation);
            if (utilisationOfThisStation >= 1) {
                return false;
            } else {
                this.tableUtilisationPerStation.write(station.getStationID(), utilisationOfThisStation);
            }
        }
        return true;
    }

    /**
	 * Generates all HashTables at the beginning of each iteration.
	 */
    public final void generateHashtablesForNewIteration() {
        this.tableUtilisationPerStationAndClass = new HashTable("AS TEMP Utilisation Per Station And Class");
        this.tableUtilisationPerStation = new HashTable("AS TEMP Utilisation Per Station");
        this.tableQueuelengthPerStationAndClass = new HashTable("AS TEMP Queuelength Per Station And Class");
        this.tableQueuelengthPerStation = new HashTable("AS TEMP MVA Queuelength Per Station");
        this.tableResponsetimePerClassAndStation = new HashTable("AS TEMP Responsetime Per Class And Station");
        this.tableResponsetimePerClass = new HashTable("AS TEMP Responsetime Per Class");
    }

    /**
	 * Initializes the Database for the algorithm. Writes the model to be solved
	 * into the database.
	 */
    public final void initialiseDB() {
        boolean success;
        if (!this.dbManager.databaseExists()) {
            success = this.dbManager.initializeTables();
            if (success) {
                logger.debug("Initialized the tables");
            } else {
                logger.debug("Tables not initialized!");
            }
        } else {
            logger.debug("DB already initialized!");
        }
        this.scenarioID = this.dbManager.insertScenarioDescription(this.inputParameter.getBc());
    }

    /**
	 * Initializes the Database for the algorithm. Writes the model to be solved
	 * into the database.
	 * @param loadfactor The actual load factor for which the results are
	 *            written into the database
	 */
    public void insertScenarioAndSolution(final double loadfactor) {
        double arrivalrate = 0;
        final long executionID = this.dbManager.insertScenarioDefinitions(this.solutionID, "Description");
        double scenarioThroughput = 0.0;
        for (int c = 0; c < this.inputParameter.getCustomerClasses().size(); c++) {
            final Class customerclass = (Class) this.inputParameter.getCustomerClasses().get(c);
            arrivalrate = loadfactor * this.inputParameter.getArrivalrate(customerclass.getClassID());
            scenarioThroughput += arrivalrate;
            this.dbManager.insertClassScenario(executionID, customerclass.getClassID(), this.scenarioID, arrivalrate, loadfactor);
        }
        this.dbManager.insertAnalyticScenarioResults(executionID, scenarioThroughput);
        Vector<ClassResultBean> crbVector = new Vector<ClassResultBean>();
        for (int c = 0; c < this.inputParameter.getCustomerClasses().size(); c++) {
            ClassResultBean crb = new ClassResultBean();
            Class customerclass = (Class) this.inputParameter.getCustomerClasses().get(c);
            crb.setClassID(customerclass.getClassID());
            crb.setExecutionID(executionID);
            double responseTime = this.tableResponsetimePerClass.read(customerclass.getClassID());
            crb.setResponseTime(responseTime);
            double throughput = loadfactor * this.inputParameter.getArrivalrate(customerclass.getClassID());
            crb.setThroughput(throughput);
            crbVector.add(crb);
        }
        this.dbManager.insertAnalyticTotalClassResultsBatch(crbVector);
        for (int s = 0; s < this.inputParameter.getStations().size(); s++) {
            final AnalyticQNStation station = (AnalyticQNStation) this.inputParameter.getStations().get(s);
            this.dbManager.insertAnalyticTotalStationResults(station.getStationID(), executionID, this.tableQueuelengthPerStation.read(station.getStationID()), this.tableUtilisationPerStation.read(station.getStationID()), -1);
            Vector<ClassStationResultBean> csrbVector = new Vector<ClassStationResultBean>();
            for (int c = 0; c < this.inputParameter.getCustomerClasses().size(); c++) {
                final Class customerclass = (Class) this.inputParameter.getCustomerClasses().get(c);
                ClassStationResultBean csrBean = new ClassStationResultBean();
                csrBean.setClassID(customerclass.getClassID());
                csrBean.setExecutionID(executionID);
                csrBean.setQueueLength(this.tableQueuelengthPerStationAndClass.read(station.getStationID(), customerclass.getClassID()));
                csrBean.setResponseTime(this.tableResponsetimePerClassAndStation.read(customerclass.getClassID(), station.getStationID()));
                csrBean.setStationID(station.getStationID());
                csrBean.setUtilization(this.tableUtilisationPerStationAndClass.read(station.getStationID(), customerclass.getClassID()));
                csrbVector.add(csrBean);
            }
            this.dbManager.insertAnalyticClassStationResultsBatch(csrbVector);
        }
    }
}
