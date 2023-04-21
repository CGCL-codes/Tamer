package desmoj.extensions.db;

import desmoj.core.dist.Distribution;
import desmoj.core.simulator.Condition;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Parameter;
import desmoj.core.simulator.TimeInstant;
import desmoj.extensions.db.dao.*;
import desmoj.extensions.db.util.DistributionData;
import desmoj.extensions.db.util.ExperimentData;
import java.sql.SQLException;
import java.util.List;
import java.util.Collection;

public class Database {

    private DBConnection connection;

    private ModelDAO modelDAO;

    private ParameterDAO parameterDAO;

    private DistributionDAO distributionDAO;

    private ExperimentDAO experimentDAO;

    private ParameterValueDAO parameterValueDAO;

    private DistributionInitialSeedDAO initialSeedDAO;

    private Model model;

    private Experiment experiment;

    private Boolean saveReport;

    public Database(Model model, Experiment experiment) {
        this.model = model;
        this.experiment = experiment;
        saveReport = false;
    }

    public void connect(String url, String user, String pass) {
        if (isConnected()) {
            disconnect();
        }
        connection = new DBConnection(url, user, pass);
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.finalize();
                connection = null;
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    private void createModelDAOs() {
        if (modelDAO == null) {
            modelDAO = new ModelDAO(connection, model.getName(), null);
        }
        if (parameterDAO == null) {
            parameterDAO = new ParameterDAO(connection, modelDAO);
        }
        if (distributionDAO == null) {
            distributionDAO = new DistributionDAO(connection, modelDAO);
        }
    }

    private void createExperimentConfigurationDAOs() {
        if (experimentDAO == null) {
            experimentDAO = new ExperimentDAO(connection, experiment.getName(), modelDAO, model);
        }
        if (parameterValueDAO == null) {
            parameterValueDAO = new ParameterValueDAO(connection, experimentDAO, parameterDAO);
        }
        if (initialSeedDAO == null) {
            initialSeedDAO = new DistributionInitialSeedDAO(connection, experimentDAO, distributionDAO);
        }
    }

    private boolean isModelLoaded() {
        return (modelDAO != null) && modelDAO.existsInDatabase();
    }

    private boolean isExperimentLoaded() {
        return (experimentDAO != null) && experimentDAO.existsInDatabase();
    }

    public void saveModel() {
        if (isConnected()) {
            modelDAO = new ModelDAO(connection, model.getName(), null);
            parameterDAO = new ParameterDAO(connection, modelDAO);
            distributionDAO = new DistributionDAO(connection, modelDAO);
            connection.startTransaction();
            modelDAO.save(model);
            parameterDAO.saveAll(model.getParameterManager().getParameters());
            distributionDAO.saveAll(experiment.getDistributionManager().getDistributions());
            connection.endTransaction();
        } else {
        }
    }

    public void saveExperimentConfiguration() throws Exception {
        if (isConnected()) {
            if (isModelLoaded()) {
                if (!isExpConfigInDatabase()) {
                    connection.startTransaction();
                    experimentDAO.save(new ExperimentData(experiment));
                    parameterValueDAO.saveAll(model.getParameterManager().getParameters());
                    List<DistributionData> dd = new java.util.ArrayList<DistributionData>();
                    List<Distribution> a = experiment.getDistributionManager().getDistributions();
                    for (Distribution distribution : a) {
                        dd.add(new DistributionData(distribution.getName(), distribution.getInitialSeed()));
                    }
                    initialSeedDAO.saveAll(dd);
                    connection.endTransaction();
                }
            } else throw new Exception("model not loaded - can't save experimentconfiguration");
        } else {
        }
    }

    public boolean isExpConfigInDatabase() {
        createExperimentConfigurationDAOs();
        return experimentDAO.existsInDatabase();
    }

    public boolean isModelIdenticalToModelInDatabase() {
        boolean result = false;
        if (isConnected()) {
            createModelDAOs();
            List<Parameter> al = new java.util.ArrayList<Parameter>(model.getParameterManager().getParameters());
            List<Distribution> dl = new java.util.ArrayList<Distribution>(experiment.getDistributionManager().getDistributions());
            connection.startTransaction();
            result = modelDAO.identicalToDatabase(model);
            result &= parameterDAO.identicalToDatabase(al);
            result &= distributionDAO.identicalToDatabase(dl);
            connection.endTransaction();
        }
        return result;
    }

    public void loadExperimentConfiguration() {
        if (isConnected()) {
            if (isModelLoaded()) {
                createExperimentConfigurationDAOs();
                connection.startTransaction();
                ExperimentData exp = experimentDAO.load();
                Collection<Parameter> loadedParams = parameterValueDAO.loadAll();
                Collection<DistributionData> loadedDistsData = initialSeedDAO.loadAll();
                connection.endTransaction();
                experiment.setDescription(exp.getDescription());
                if (exp.hasStoptime()) try {
                    experiment.stop(new TimeInstant((long) exp.getStopTime()));
                } catch (Exception e) {
                }
                if (exp.getStopCondition() != null) {
                    experiment.removeStopConditions();
                    experiment.stop(exp.getStopCondition());
                }
                List<Parameter> al = new java.util.ArrayList<Parameter>(model.getParameterManager().getParameters());
                for (Parameter loadedParam : loadedParams) {
                    for (Parameter param : al) {
                        if (param.getName().equals(loadedParam.getName())) {
                            param.setValue(loadedParam.getValue());
                            break;
                        }
                    }
                }
                List<Distribution> a = experiment.getDistributionManager().getDistributions();
                for (DistributionData loadedDistData : loadedDistsData) {
                    for (Distribution dist : a) {
                        if (dist.getName().equals(loadedDistData.getName())) {
                            dist.reset(loadedDistData.getInitialSeed());
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * @javadoc Enables saving the trace into database.
     */
    public void saveTrace() {
        if (isConnected()) {
            if (isExperimentLoaded()) {
                experiment.addTraceReceiver(new DBTraceOutput(connection, new DBTraceDAO(connection, experimentDAO)));
            } else {
                System.out.println("Noch kein Experiment gespeichert: Trace kann nicht in die Datenbank geschrieben werden.");
            }
        }
    }

    /**
     * @javadoc Enables saving error notes into database.
     */
    public void saveError() {
        if (isConnected()) {
            if (isExperimentLoaded()) {
                experiment.addErrorReceiver(new DBErrorOutput(connection, new DBErrorDAO(connection, experimentDAO)));
            } else {
                System.out.println("Noch kein Experiment gespeichert: Error kann nicht in die Datenbank geschrieben werden.");
            }
        } else {
        }
    }

    /**
     * @javadoc Enables saving debug informations into database.
     */
    public void saveDebug() {
        if (isConnected()) {
            if (isExperimentLoaded()) {
                experiment.addDebugReceiver(new DBDebugOutput(connection, new DBDebugDAO(connection, experimentDAO)));
            } else {
                System.out.println("Noch kein Experiment gespeichert: Debug kann nicht in die Datenbank geschrieben werden.");
            }
        }
    }

    /**
     * @javadoc Enables saving reportables into database at a later time (when DBExperiment.report() is called).
     */
    public void saveReportable() {
        saveReport = true;
    }

    /**
     * @javadoc Informs whether reportables are to be saved to database.
     *
     * @return Boolean saveReport: If true, reportables will be saved to database.
     */
    public Boolean isReportSave() {
        return saveReport;
    }

    /**
     * @javadoc Collects data from reportables (compare Experiment_report.html) and saves them to database.
     *                  Be aware: start experiment before or useless data will be stored. Better use saveReportables() plus DBExperiment.report().
     */
    public void report() {
        if (isConnected()) {
            if (isExperimentLoaded()) {
                try {
                    new DBReportableOutput(model, connection, experimentDAO);
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            } else {
                System.out.println("Noch kein Experiment gespeichert: Reportables koennen nicht in die Datenbank geschrieben werden.");
            }
        }
    }

    /**
     * @javadoc Enables saving debug informations, trace and error notes into database.
     */
    public void saveErrorTraceDebug() {
        saveTrace();
        saveError();
        saveDebug();
    }

    /**
     * @javadoc Enables saving debug informations, trace, error notes and data from reportables into database.
     */
    public void saveAll() throws Exception {
        saveModel();
        saveExperimentConfiguration();
        saveErrorTraceDebug();
        saveReportable();
    }

    public ExperimentDAO getExperimentDAO() {
        return experimentDAO;
    }

    public DBConnection getDBConnection() {
        return connection;
    }
}
