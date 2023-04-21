package org.mitre.rt.client.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import org.mitre.rt.common.dto.ApplicationInfo;
import org.mitre.rt.client.properties.RTClientProperties;
import org.mitre.rt.client.properties.RTClientSyncProperties;
import org.mitre.rt.client.ui.WorkerDialog;
import org.mitre.rt.server.api.InitializeClient;
import org.mitre.rt.client.ui.applications.ApplicationListDialog;
import org.mitre.rt.client.util.GlobalUITools;
import org.mitre.rt.client.xml.FileTypeHelper;
import org.mitre.rt.common.RequestedApplication;
import org.mitre.rt.common.RequestedFile;
import org.mitre.rt.common.xml.RTDocumentUtilities;
import org.mitre.rt.rtclient.ApplicationType;
import org.mitre.rt.rtclient.RTDocument;
import org.mitre.rt.server.database.DatabaseManager;
import org.mitre.rt.server.database.dao.UtilityDAO;
import org.mitre.rt.server.exceptions.DatabaseException;
import org.mitre.rt.server.exceptions.RTServerConnectionException;
import org.mitre.rt.server.properties.RTServerProperties;

/**
 *
 * @author BAKERJ
 */
public class SynchronizationManager {

    private static final Logger logger = Logger.getLogger(SynchronizationManager.class.getPackage().getName());

    private static SynchronizationManager instance = null;

    /**
     * 
     * static instance method makes this class a singleton.
     * Ensures that only once instance of the class is ever created.
     * 
     * @return
     * @throws org.mitre.rt.exceptions.DataManagerException
     */
    public static SynchronizationManager instance() {
        if (SynchronizationManager.instance == null) {
            instance = new SynchronizationManager();
        }
        return SynchronizationManager.instance;
    }

    private SynchronizationManager() {
    }

    /**
     * Queries the server for the set of Applications available in the
     * database, but are not yet included in the local RTdoc. In the 
     * event of a problem, logs the error, informs the users.
     * 
     * @param rtServerName The IP address or hostname of the server
     */
    public void getApplications(String rtServerName) {
        final InitializeClient ic = new InitializeClient();
        Connection conn = null;
        try {
            logger.debug("Starting client data initialization process with " + rtServerName);
            ic.init();
            List<ApplicationInfo> allApplications = ic.getAvailableApplications();
            if (allApplications == null) {
                allApplications = new ArrayList<ApplicationInfo>(0);
            }
            final RTDocument rtDoc = DataManager.instance().getRTDocument();
            List<ApplicationType> myDocApps = null;
            List<ApplicationInfo> unRetrievedApps = null;
            if (!rtDoc.getRT().isSetApplications()) {
                unRetrievedApps = allApplications;
            } else {
                myDocApps = rtDoc.getRT().getApplications().getApplicationList();
                unRetrievedApps = new ArrayList<ApplicationInfo>(allApplications.size());
                for (ApplicationInfo serverApp : allApplications) {
                    boolean found = false;
                    for (ApplicationType localApp : myDocApps) {
                        if (serverApp.getId().equals(localApp.getId())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        unRetrievedApps.add(serverApp);
                    }
                }
            }
            final ApplicationListDialog appListDialog = new ApplicationListDialog(MetaManager.getMainWindow(), true, unRetrievedApps);
            if (!appListDialog.isCanceled() && !appListDialog.getSelectedApplications().isEmpty()) {
                List<ApplicationInfo> selectedApplications = appListDialog.getSelectedApplications();
                final List<String> applicationIds = new ArrayList<String>();
                for (ApplicationInfo appInfo : selectedApplications) {
                    logger.debug(appInfo.getName());
                    applicationIds.add(appInfo.getId());
                }
                final String workerMessage = "Retrieving Application" + ((applicationIds.size() > 1) ? "s" : "") + "...";
                final WorkerDialog wd = new WorkerDialog(MetaManager.getMainWindow(), workerMessage);
                final SwingWorker<Object, Void> worker = new SwingWorker<Object, Void>() {

                    private Throwable t = null;

                    @Override
                    protected Object doInBackground() throws Exception {
                        try {
                            retrieveNewApplications(rtDoc, ic, applicationIds);
                        } catch (Exception ex) {
                            t = ex;
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        wd.dispose();
                        if (t != null) {
                            logger.error("Failed to select application data.", t);
                            GlobalUITools.displayExceptionMessage(null, "Unable to retrieve application data", t);
                        }
                    }
                };
                wd.setWorker(worker);
                wd.doWork();
            }
            ic.shutdown();
            MetaManager.getMainWindow().getApplicationBar().updateApplicationList(true);
            appListDialog.dispose();
        } catch (RTServerConnectionException ex) {
            logger.error(ex);
            GlobalUITools.displayExceptionMessage(MetaManager.getMainWindow(), "Server Connection Error", ex);
        } catch (DatabaseException ex) {
            logger.error(ex);
            GlobalUITools.displayExceptionMessage(MetaManager.getMainWindow(), "Database Error", ex);
        } catch (Exception ex) {
            logger.error("Failed to select application data.", ex);
            GlobalUITools.displayExceptionMessage(null, "Unable to retrieve application data", ex);
        } finally {
            if (ic != null) ic.shutdown();
            if (conn != null) DatabaseManager.close(conn);
        }
    }

    public void retrieveNewApplications(final RTDocument rtDoc, final InitializeClient ic, final List<String> applicationIds) throws Exception {
        final List<RequestedApplication> requestedApps = ic.getApplications(applicationIds);
        final FileTypeHelper fileHelper = new FileTypeHelper();
        if (!rtDoc.getRT().isSetApplications()) {
            rtDoc.getRT().addNewApplications();
        }
        for (RequestedApplication requestedApp : requestedApps) {
            if (!requestedApp.getStatusFlag()) {
                continue;
            }
            ApplicationType newApp = rtDoc.getRT().getApplications().addNewApplication();
            newApp.set(requestedApp.getApplication());
            List<RequestedFile> rfs = requestedApp.getFileRequests();
            if (rfs != null) {
                for (RequestedFile rf : rfs) {
                    fileHelper.saveTempFile(newApp, rf.getFileType(), rf.getFile());
                }
            }
        }
        DataManager.setModified(true);
    }

    /**
     * Queries the server for the set of Applications available. In the 
     * event of a problem, logs the error, informs the users.
     * 
     * @param rtServerName The IP address or hostname of the server
     */
    public void initializeClientData(String rtServerName) {
        Connection conn = null;
        InitializeClient ic = null;
        try {
            logger.debug("Starting client data initialization process with " + rtServerName);
            RTClientProperties clientProps = RTClientProperties.instance();
            RTClientSyncProperties syncProps = RTClientSyncProperties.instance();
            RTServerProperties serverProps = RTServerProperties.instance();
            File initFile = null;
            Reader initReader = null;
            if (serverProps.isDbStandAlone()) {
                URL baseXMLURL = getClass().getResource("/org/mitre/rt/client/resources/rt.base.client.xml");
                logger.debug("initializeClientData: stand-alone xml file URL " + baseXMLURL.toURI());
                initReader = new InputStreamReader(baseXMLURL.openStream());
            } else {
                ic = new InitializeClient();
                ic.init();
                initFile = ic.getInitialData();
                FileInputStream fileInputStream = new FileInputStream(initFile);
                initReader = new InputStreamReader(fileInputStream, "UTF8");
                ic.shutdown();
                ic = null;
            }
            RTDocument rtDoc = RTDocumentUtilities.parseDocument(initReader);
            File rtFile = new File(clientProps.getOfflineXMLFilePath());
            if (!rtFile.exists()) {
                rtFile.createNewFile();
            }
            RTDocumentUtilities.saveDocument(rtDoc, rtFile);
            if (!serverProps.isDbStandAlone()) {
                UtilityDAO utilityDAO = new UtilityDAO();
                if (conn == null || conn.isClosed()) conn = DatabaseManager.getConnection();
                Timestamp timestamp = utilityDAO.getDateFromDb(conn);
                syncProps.setLastGlobalUpdateDate(timestamp);
                syncProps.setLastGlobalUpdateSystemDate();
            }
        } catch (RTServerConnectionException ex) {
            logger.debug(ex);
            GlobalUITools.displayWarningMessage(MetaManager.getMainWindow(), "Server Connection Error", ex.getMessage());
        } catch (Exception ex) {
            logger.error("Failed to initialize client data.", ex);
            GlobalUITools.displayExceptionMessage(null, "Unable to initailze client data with server", ex);
        } finally {
            if (ic != null) ic.shutdown();
            if (conn != null) DatabaseManager.close(conn);
        }
    }
}
