package com.calipso.reportgenerator.client;

import com.calipso.reportgenerator.common.exception.CalipsoException;
import com.calipso.reportgenerator.common.exception.InfoException;
import com.calipso.reportgenerator.reportmanager.ReportSource;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.calipso.reportgenerator.common.*;
import com.calipso.reportgenerator.client.ReportManagerFactory;
import com.calipso.reportgenerator.reportdefinitions.ReportView;
import com.calipso.reportgenerator.reportdefinitions.ReportDefinition;
import com.calipso.reportgenerator.reportdefinitions.ReportSourceDefinition;
import com.calipso.reportgenerator.reportcalculator.Matrix;
import java.util.*;

/**
 * 
 */
public class ReportManagerService implements IReportManager {

    private IReportManager reportManager;

    private IReportManager reportManagerPersist;

    private static ReportGeneratorConfiguration reportGeneratorConfiguration;

    private boolean initialized = false;

    private String distributedHost;

    private IReportManager getReportManager() {
        return reportManager;
    }

    public void init(Configuration conf, String distributedHost) throws CalipsoException {
        this.distributedHost = distributedHost;
        reportGeneratorConfiguration = new ReportGeneratorConfiguration(conf);
        ReportManagerFactory factory = new ReportManagerFactory();
        reportManager = factory.newReportManager(reportGeneratorConfiguration, false, distributedHost);
        initialized = true;
    }

    public void shutdown() {
        if (reportManagerPersist != null) {
            reportManagerPersist = null;
        }
        if (reportManager != null) {
            reportManager = null;
        }
        initialized = false;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setLogger(Log logger) {
        ReportManagerLogger.setLog(logger);
    }

    public IReportManager getReportManagerPersist() throws InfoException {
        if (reportManagerPersist == null) {
            ReportManagerFactory factory = new ReportManagerFactory();
            reportManagerPersist = factory.newReportManager(reportGeneratorConfiguration, true, distributedHost);
        }
        return reportManagerPersist;
    }

    public int PrepareReport(String reportDefID) throws InfoException {
        return getReportManagerPersist().PrepareReport(reportDefID);
    }

    public int PrepareReport(String reportDefId, Map paramValues) throws InfoException {
        return getReportManagerPersist().PrepareReport(reportDefId, paramValues);
    }

    public void ReleaseReport(int handle) throws InfoException {
        getReportManagerPersist().ReleaseReport(handle);
    }

    public void prepareReportSource(String reportSourceDefinitionId) throws InfoException {
        getReportManager().prepareReportSource(reportSourceDefinitionId);
    }

    public Map getReportDefinitions() throws InfoException {
        return getReportManager().getReportDefinitions();
    }

    public Map getReportSourceDefinitions() throws InfoException {
        return getReportManager().getReportSourceDefinitions();
    }

    public Map getReportsForEntity(String entityID) throws InfoException {
        return getReportManager().getReportsForEntity(entityID);
    }

    public void ExecuteAction(int handle, String actionName, Object params) throws InfoException {
        getReportManagerPersist().ExecuteAction(handle, actionName, params);
    }

    public void saveReportDefinition(ReportDefinition reportDefinition) throws InfoException {
        getReportManager().saveReportDefinition(reportDefinition);
    }

    public void saveReportSourceDefinition(ReportSourceDefinition reportSourceDefinition) throws InfoException {
        getReportManager().saveReportSourceDefinition(reportSourceDefinition);
    }

    public void invalidateReportSource(String reportSourceDefinitionId) throws InfoException {
        getReportManager().invalidateReportSource(reportSourceDefinitionId);
    }

    public ReportQuery getReportQuery(int handle) throws InfoException {
        return getReportManagerPersist().getReportQuery(handle);
    }

    public ReportQuery getDefaultReportQuery(int handle) throws InfoException {
        return getReportManagerPersist().getDefaultReportQuery(handle);
    }

    public ReportQuery getReportQuery(String reportDefinitionId) throws InfoException {
        return getReportManager().getReportQuery(reportDefinitionId);
    }

    public ReportQuery getDefaultReportQuery(String reportDefinitionId) throws InfoException {
        return getReportManager().getDefaultReportQuery(reportDefinitionId);
    }

    public ReportResult ExecReportQuery(int handle, Map paramValues) throws InfoException {
        return getReportManagerPersist().ExecReportQuery(handle, paramValues);
    }

    public ReportResult ExecReportQuery(int handle, ReportQuery query) throws InfoException {
        return getReportManagerPersist().ExecReportQuery(handle, query);
    }

    public ReportResult ExecReportQuery(String reportDefinitionID, Map paramValues) throws InfoException {
        return getReportManager().ExecReportQuery(reportDefinitionID, paramValues);
    }

    public ReportResult ExecReportQuery(String reportDefinitionID, ReportQuery query) throws InfoException {
        return getReportManager().ExecReportQuery(reportDefinitionID, query);
    }

    public Log getLogger() {
        return ReportManagerLogger.getLog();
    }

    public ReportQuery getDefaultReportQuery(int handle, String userID) throws InfoException {
        return getReportManagerPersist().getDefaultReportQuery(handle, userID);
    }

    ;

    public Map getReportViews(String reportDefinitionID, String userID) throws InfoException {
        return getReportManager().getReportViews(reportDefinitionID, userID);
    }

    ;

    public Map getReportViews(String reportDefinitionID) throws InfoException {
        return getReportManager().getReportViews(reportDefinitionID);
    }

    ;

    public ReportResult ExecReportQuery(int handle, String reportViewId) throws InfoException {
        return getReportManagerPersist().ExecReportQuery(handle, reportViewId);
    }

    ;

    public String getDefaultReportViewId(String reportDefinitionId, String userId) throws InfoException {
        return getReportManager().getDefaultReportViewId(reportDefinitionId, userId);
    }

    ;

    public ReportSpec getReportSpec(String reportDefinitionId, String reportSourceDefId) throws InfoException {
        return getReportManager().getReportSpec(reportDefinitionId, reportSourceDefId);
    }

    public ReportSpec getReportSpec(ReportDefinition reportDefinition, ReportSourceDefinition reportSourceDef) throws InfoException {
        return getReportManager().getReportSpec(reportDefinition, reportSourceDef);
    }

    public ReportSpec getReportSpec(String reportDefinitionId) throws InfoException {
        return getReportManager().getReportSpec(reportDefinitionId);
    }

    public ReportView getReportView(String reportViewId) throws InfoException {
        return getReportManager().getReportView(reportViewId);
    }

    public void saveReportView(ReportView reportView) throws InfoException {
        getReportManager().saveReportView(reportView);
    }

    public ReportView getReportViewFromID(String id, String reportDefinitionId, String userId) throws InfoException {
        return getReportManager().getReportViewFromID(id, reportDefinitionId, userId);
    }

    public ReportResult ExecReportQuery(int handle, ReportView reportView) throws InfoException {
        return getReportManagerPersist().ExecReportQuery(handle, reportView);
    }

    public void registerDefinitions() throws InfoException {
        getReportManager().registerDefinitions();
    }

    public Vector registerReportSourceDefinitions(Vector vector) throws InfoException {
        return getReportManager().registerReportSourceDefinitions(vector);
    }

    public Vector registerReportDefinitions(Vector vector) throws InfoException {
        return getReportManager().registerReportDefinitions(vector);
    }

    public Vector registerReportViews(Vector vector) throws InfoException {
        return getReportManager().registerReportViews(vector);
    }

    public void deleteAllRepositories() throws InfoException {
        getReportManager().deleteAllRepositories();
    }

    public void deleteAllDefinitions() throws InfoException {
        getReportManager().deleteAllDefinitions();
    }

    public void deleteReportSourceRepository() throws InfoException {
        getReportManager().deleteReportSourceRepository();
    }

    public void deleteReportSourceDefinitionRepository() throws InfoException {
        getReportManager().deleteReportSourceDefinitionRepository();
    }

    public void deleteReportDefinitionRepository() throws InfoException {
        getReportManager().deleteReportDefinitionRepository();
    }

    public void deleteReportViewRepository() throws InfoException {
        getReportManager().deleteReportViewRepository();
    }

    public void deleteReportView(String id, String reportDefinitionId, String userId) throws InfoException {
        getReportManager().deleteReportView(id, reportDefinitionId, userId);
    }

    public void deleteReportSource(String reportSourceDefinitionId) throws InfoException {
        getReportManager().deleteReportSource(reportSourceDefinitionId);
    }

    public void deleteReportSourceDefinition(String reportSourceDefinitionID) throws InfoException {
        getReportManager().deleteReportSourceDefinition(reportSourceDefinitionID);
    }

    public void deleteReportDefinition(String reportDefinitionID) throws InfoException {
        getReportManager().deleteReportDefinition(reportDefinitionID);
    }

    public void assingDefaultView(String id, String reportDefinitionId, String userId) throws InfoException {
        getReportManager().assingDefaultView(id, reportDefinitionId, userId);
    }

    public ReportResult ExecReportQuery(MicroReport microReport) throws InfoException {
        ReportManagerFactory reportManagerFactory = new ReportManagerFactory();
        boolean isDistributed = reportGeneratorConfiguration.getIsDistributed();
        if (isDistributed) {
            reportGeneratorConfiguration.getValues().put("IsDistributed", "false");
        }
        reportManager = reportManagerFactory.newReportManager(reportGeneratorConfiguration, false, distributedHost);
        if (isDistributed) {
            reportGeneratorConfiguration.getValues().put("IsDistributed", "true");
        }
        return reportManager.ExecReportQuery(microReport);
    }

    public int PrepareReport(MicroReport microReport) throws InfoException {
        return getReportManagerPersist().PrepareReport(microReport);
    }

    public String getXML(int handle) throws InfoException {
        return getReportManagerPersist().getXML(handle);
    }

    public String getXML(String reportDefinitionID, Map paramValues) throws InfoException {
        return getReportManager().getXML(reportDefinitionID, paramValues);
    }

    public Set getDimensionValues(int handle, String name) throws InfoException {
        return getReportManagerPersist().getDimensionValues(handle, name);
    }

    public Set getDimensionValues(String reportDefinitionID, Map paramValues, String name) throws InfoException {
        return getReportManager().getDimensionValues(reportDefinitionID, paramValues, name);
    }

    public Vector getUpdatedDataModel(int handle, int mode, int row, int col, boolean isDistributed) throws InfoException {
        return getReportManagerPersist().getUpdatedDataModel(handle, mode, row, col, isDistributed);
    }

    public boolean validateUser(String userName, String password, String userRepositoryPath) throws InfoException {
        return getReportManagerPersist().validateUser(userName, password, userRepositoryPath);
    }

    public boolean validateRol(String[] roles, String userName, String rolRepositoryPath) throws InfoException {
        return getReportManager().validateRol(roles, userName, rolRepositoryPath);
    }

    public void exportReport(String userName, String password, String userRepositoryPath, String reportDefinitionId, Map paramValues, boolean isLandscape, int type, String destinationPath, String name) throws InfoException {
        getReportManager().exportReport(userName, password, userRepositoryPath, reportDefinitionId, paramValues, isLandscape, type, destinationPath, name);
    }

    public byte[] exportReport(Map params) throws InfoException {
        return getReportManager().exportReport(params);
    }

    public boolean addNewUser(String rootPasswd, String userName, String userPassword, String userRepositoryPath) throws InfoException {
        return getReportManager().addNewUser(rootPasswd, userName, userPassword, userRepositoryPath);
    }

    public void addUserData(String userName, String name, String company, String userDataRepositoryPath) throws InfoException {
        getReportManager().addUserData(userName, name, company, userDataRepositoryPath);
    }

    public void addUserRol(String userName, String rol, String rolsRepositoryPath) throws InfoException {
        getReportManager().addUserRol(userName, rol, rolsRepositoryPath);
    }

    public boolean isAcceptedLicence() throws InfoException {
        return getReportManager().isAcceptedLicence();
    }

    public void acceptedLicence(boolean value) throws InfoException {
        getReportManager().acceptedLicence(value);
    }

    public MicroReport getMicroReport(String fileName) throws InfoException {
        return getReportManager().getMicroReport(fileName);
    }

    public MicroReport getMicroReport(String reportDefinitionId, Map param) throws InfoException {
        return getReportManager().getMicroReport(reportDefinitionId, param);
    }

    public Collection getUserData(String userId, String userDataRepositoryPath) throws InfoException {
        return getReportManager().getUserData(userId, userDataRepositoryPath);
    }

    public void logClientData(String clientData) throws InfoException {
        getReportManager().logClientData(clientData);
    }

    public Matrix getMatrix(int handle) throws InfoException {
        return getReportManagerPersist().getMatrix(handle);
    }

    public ReportDefinition getReportDefinitionFromID(String reportDefinitionId) throws InfoException {
        return getReportManager().getReportDefinitionFromID(reportDefinitionId);
    }

    public ReportSourceDefinition getReportSourceDefinitionFromID(String reportSourceDefinitionId) throws InfoException {
        return getReportManager().getReportSourceDefinitionFromID(reportSourceDefinitionId);
    }

    public void init(ReportGeneratorConfiguration reportGeneratorConfiguration) throws InfoException {
    }

    /**
   * Instancia un report manager service
   * @param propertiesPath
   * @param log
   * @return
   * @throws com.calipso.reportgenerator.common.InfoException
   */
    public static IReportManager getReportManagerService(String propertiesPath, Log log, String distributedHost) throws InfoException {
        ReportManagerService reportManagerService = new ReportManagerService();
        try {
            reportManagerService.init(ReportGeneratorConfiguration.getConfiguration(propertiesPath), distributedHost);
        } catch (Exception e) {
            throw new InfoException("Archivo no encontrado: ReportGeneratorConfiguration.properties, " + ReportGeneratorConfiguration.getConfigurationPath(propertiesPath), e);
        }
        reportManagerService.setLogger(getLog(log));
        return reportManagerService;
    }

    /**
   * Obtiene un log
   * @param log
   * @return
   */
    private static Log getLog(Log log) {
        if (log == null) {
            Properties props = new Properties();
            props.setProperty("log4j.rootLogger", "DEBUG, A1");
            props.setProperty("log4j.appender.A1", "org.apache.log4j.ConsoleAppender");
            props.setProperty("log4j.appender.A1.layout", "org.apache.log4j.PatternLayout");
            props.setProperty("log4j.appender.A1.layout", "org.apache.log4j.PatternLayout");
            props.setProperty("log4j.logger.test.reportmanager", "ALL");
            Log localLog = LogFactory.getLog("ReportManager");
            return localLog;
        } else {
            return log;
        }
    }

    public static ReportGeneratorConfiguration getConfiguration() {
        return reportGeneratorConfiguration;
    }
}
