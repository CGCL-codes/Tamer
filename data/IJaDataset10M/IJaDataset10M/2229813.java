package jsystem.treeui.publisher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import jsystem.extensions.report.xml.DbPublish;
import jsystem.extensions.report.xml.XmlReporter;
import jsystem.framework.DBProperties;
import jsystem.framework.FrameworkOptions;
import jsystem.framework.JSystemProperties;
import jsystem.framework.report.ListenerstManager;
import jsystem.framework.report.Reporter;
import jsystem.framework.report.RunnerListenersManager;
import jsystem.framework.report.TestReporter;
import jsystem.runner.ErrorLevel;
import jsystem.runner.agent.publisher.Publisher;
import jsystem.runner.agent.publisher.PublisherProgress;
import jsystem.runner.agent.reportdb.tables.Package;
import jsystem.runner.agent.reportdb.tables.PropertiesList;
import jsystem.runner.agent.reportdb.tables.Run;
import jsystem.runner.agent.reportdb.tables.Step;
import jsystem.runner.agent.reportdb.tables.Test;
import jsystem.runner.agent.reportdb.tables.TestName;
import jsystem.runner.agent.reportdb.tables.TestProperties;
import jsystem.runner.agent.tests.PublishTest.ActionType;
import jsystem.runner.remote.RemoteTestRunner.RemoteMessage;
import jsystem.treeui.DBConnectionListener;
import jsystem.treeui.DbGuiUtility;
import jsystem.utils.StringUtils;
import jsystem.utils.UploadRunner;

public class DefaultPublisher implements Publisher, ActionListener, DBConnectionListener {

    boolean debug = true;

    private Run run;

    private PublisherProgress progressBar;

    private static Logger log = Logger.getLogger(DefaultPublisher.class.getName());

    private PublisherRunInfoFrame frame;

    private DbPublish reportInfo;

    private long reportIndex;

    private static Connection conn;

    private UploadRunner uploader;

    public Thread thread;

    private boolean save = false;

    public static Reporter report = ListenerstManager.getInstance();

    boolean uploadLogs = true;

    public boolean isUploadLogs() {
        return uploadLogs;
    }

    public void setUploadLogs(boolean uploadLogs) {
        this.uploadLogs = uploadLogs;
    }

    public void publish() {
        try {
            createReport();
        } catch (Exception e) {
            DbGuiUtility.publishError("Publish error- fail to connect to the Database", "Fail to connect to the Database\n\n" + "Try the following :\n\n" + "1)Check that Database Server is on\n\n" + "2)Check You Database Properties \n\n" + StringUtils.getStackTrace(e), ErrorLevel.Error);
        }
    }

    /**
	 * publish the changed report
	 * 
	 * @param publishProperties
	 *            properties that contains all the test parameters : Description,
	 *            Setup Name,Version,Build
	 * @throws Exception
	 */
    public jsystem.runner.remote.Message publish(Map<String, Object> publishProperties) throws Exception {
        jsystem.runner.remote.Message remoteMessage = null;
        ((RunnerListenersManager) RunnerListenersManager.getInstance()).flush();
        ActionType currentActionType = ActionType.valueOf(publishProperties.get(DBProperties.ACTION_TYPE).toString());
        try {
            if (currentActionType == ActionType.publish || currentActionType == ActionType.publish_and_email) {
                createReport(publishProperties);
                remoteMessage = generateRunDetailsMessage(true);
            } else if (currentActionType == ActionType.email || currentActionType == ActionType.upload_log_files || currentActionType == ActionType.init_reporters_only) {
                remoteMessage = generateRunDetailsMessage(false);
            }
            if (Boolean.parseBoolean(publishProperties.get(DBProperties.UPLOAD_FILES).toString())) {
                uploadLogs();
            }
            if (Boolean.parseBoolean(publishProperties.get(DBProperties.INIT_REPORT).toString())) {
                ((RunnerListenersManager) RunnerListenersManager.getInstance()).initReporters();
            }
        } catch (Exception exception) {
            String title = "Publish error- fail to connect to the Database";
            String msg = "Fail to connect to the Database\n\n" + "Try the following :\n\n" + "1) Check that Database Server is on\n\n" + "2) Check You Database Properties \n\n" + StringUtils.getStackTrace(exception);
            report.report(title, msg, false);
            remoteMessage = generateRunDetailsMessage(false);
        }
        return remoteMessage;
    }

    /**
	 * create a publish form
	 * 
	 * @throws Exception
	 */
    private void createReport(Map<String, Object> publish_Properties) throws Exception {
        reportIndex = System.currentTimeMillis();
        report.report("publish properties" + publish_Properties.get(DBProperties.UPLOAD_FILES).toString());
        setUploadLogs(Boolean.parseBoolean(publish_Properties.get(DBProperties.UPLOAD_FILES).toString()));
        StringUtils.showMsgWithTime("publish.bypass = true");
        File reportCurrent = new File(JSystemProperties.getInstance().getPreference(FrameworkOptions.LOG_FOLDER), "current");
        ArrayList<TestReporter> listeners = ((RunnerListenersManager) RunnerListenersManager.getInstance()).getAllReporters();
        for (TestReporter currentTestReporter : listeners) {
            if (currentTestReporter.getClass() == XmlReporter.class) {
                ((XmlReporter) currentTestReporter).readElements();
            }
        }
        reportInfo = new DbPublish(reportCurrent);
        run = reportInfo.getRunForForm(reportIndex);
        run.setDescription(publish_Properties.get(DBProperties.DESCRIPTION).toString());
        run.setVersion(publish_Properties.get(DBProperties.VERSION).toString());
        run.setBuild(publish_Properties.get(DBProperties.BUILD).toString());
        run.setScenarioName(publish_Properties.get(DBProperties.SCENARIO_NAME).toString());
        run.setStation(publish_Properties.get(DBProperties.STATION).toString());
        run.setSetupName(publish_Properties.get(DBProperties.SETUP).toString());
        startPublish();
        try {
            DBProperties.getInstance();
        } catch (Exception exception) {
            throw new Exception(DBProperties.DB_PROPERTIES_FILE + " file wasn't found");
        }
    }

    public boolean uploadLogs() throws Exception {
        return uploadFile(reportInfo, reportIndex);
    }

    /**
	 * generates remote message to deliver to publish test. contains all the
	 * parameters that relevant for this scenario
	 * 
	 * @param isPublished
	 *            if false - send parameters only for email, if true - send all
	 *            parameters
	 * @return remote message
	 * @throws Exception
	 */
    private jsystem.runner.remote.Message generateRunDetailsMessage(boolean isPublished) throws Exception {
        File reportCurrent = new File(JSystemProperties.getInstance().getPreference(FrameworkOptions.LOG_FOLDER), "current");
        ArrayList<TestReporter> listeners = ((RunnerListenersManager) RunnerListenersManager.getInstance()).getAllReporters();
        for (TestReporter currentTestReporter : listeners) {
            if (currentTestReporter.getClass() == XmlReporter.class) {
                ((XmlReporter) currentTestReporter).readElements();
            }
        }
        reportInfo = new DbPublish(reportCurrent);
        jsystem.runner.remote.Message remoteMessage = new jsystem.runner.remote.Message();
        remoteMessage.setType(RemoteMessage.M_PUBLISH);
        remoteMessage.addField(reportInfo.getSecnarioName());
        remoteMessage.addField(reportInfo.getSutName());
        remoteMessage.addField(reportInfo.getVersion());
        remoteMessage.addField(reportInfo.getBuild());
        remoteMessage.addField(reportInfo.getUserName());
        remoteMessage.addField(Long.toString(reportInfo.getStartTime()));
        remoteMessage.addField(Integer.toString(reportInfo.getNumberOfTests()));
        remoteMessage.addField(Integer.toString(reportInfo.getNumberOfTestsFail()));
        remoteMessage.addField(Integer.toString(reportInfo.getNumberOfTestsPass()));
        remoteMessage.addField(Integer.toString(reportInfo.getNumberOfTestsWarning()));
        remoteMessage.addField(reportInfo.getStation());
        remoteMessage.addField(Boolean.toString(reportInfo.isCheckTables()));
        remoteMessage.addField(Boolean.toString(reportInfo.isUploadLogs()));
        remoteMessage.addField(Long.toString(reportIndex));
        if (isPublished) {
            remoteMessage.addField(run.getDescription());
            remoteMessage.addField(run.getHtmlDir());
            remoteMessage.addField(Integer.toString(run.getRunIndex()));
        }
        return remoteMessage;
    }

    /**
	 * create a publish form
	 * 
	 * @throws Exception
	 */
    private void createReport() throws Exception {
        reportIndex = System.currentTimeMillis();
        DbGuiUtility.showMsgWithTime("publish.bypass = true");
        File reportCurrent = new File(JSystemProperties.getInstance().getPreference(FrameworkOptions.LOG_FOLDER), "current");
        reportInfo = new DbPublish(reportCurrent);
        run = reportInfo.getRunForForm(reportIndex);
        frame = new PublisherRunInfoFrame(run);
        progressBar = PublisherRunInfoFrame.getProgress();
        progressBar.setBarValue(0);
        frame.addActionListener(this);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                frame.showMy();
            }
        });
        if (!checkDBPropertiesFile()) {
            return;
        }
        frame.enableButtons(false);
    }

    /**
	 * start the publishing: a. zip and upload file b. publish to DB
	 * 
	 */
    public void startPublish() {
        (new Thread() {

            public void run() {
                frame.setStartedPublish(true);
                int numOfTests = reportInfo.getNumberOfTests() - 1;
                progressBar.setBarMinMax(0, (numOfTests + 1) * 4);
                progressBar.setBarValue(0);
                if (!uploadFile(reportInfo, reportIndex)) {
                    publishAborted();
                    return;
                }
                if (!writeToDB(reportInfo, reportIndex, conn, numOfTests)) {
                    publishAborted();
                    return;
                }
                DBProperties.closeConnectionIfExists(conn);
            }
        }).start();
    }

    /**
	 * signal that a publish was terminated and do the necessary actions
	 * 
	 */
    public void publishAborted() {
        progressBar.setBarValue(0);
        frame.setStartedPublish(false);
    }

    /**
	 * check that there is a db.properties file present, creates one if not
	 * 
	 * @return true if file exists
	 */
    private boolean checkDBPropertiesFile() {
        try {
            DBProperties.getInstance();
            return true;
        } catch (Exception e) {
            frame.showDBProperties();
        }
        return false;
    }

    /**
	 * zip and upload log files to server
	 * 
	 * 
	 * 1.You need to add to db.properties line serverIP="you server ip"
	 * 
	 * 2.If you server use port other than 8080 you must also change this in
	 * db.properties ->browser.port
	 * 
	 * 
	 * serverUploadUrl=http://"you server ip"/reports/upload
	 * 
	 */
    private boolean uploadFile(DbPublish reportInfo, long reportIndex) {
        File tempDir = new File(JSystemProperties.getInstance().getPreference(FrameworkOptions.LOG_FOLDER));
        File tempCurrentDir = new File(tempDir, "current");
        uploader = new UploadRunner(tempCurrentDir, reportIndex);
        DbGuiUtility.showMsgWithTime("Uploading file to reports Server");
        int max = progressBar.getMaxValue();
        try {
            uploader.zipFile();
        } catch (Exception e) {
            DbGuiUtility.publishError("Publish error- fail to zip file for publishing", "check that there is enought space for zipping\n\n" + StringUtils.getStackTrace(e), ErrorLevel.Error);
            return false;
        }
        progressBar.setBarValue(max / 4);
        try {
            uploader.upload();
        } catch (Exception e) {
            DbGuiUtility.publishError("Publish error- fail to upload file to reports server", "check that the reports server is running\n\n" + StringUtils.getStackTrace(e), ErrorLevel.Error);
            return false;
        }
        progressBar.setBarValue(max / 2);
        return true;
    }

    /**
	 * write all data to DB
	 * 
	 * @param reportInfo
	 *            the DbPublish object
	 * @param reportIndex
	 *            the long id of the report
	 * @param conn
	 *            the connection to the DB
	 * @param numOfTests
	 *            the number of tests to publish
	 * @return true if all succeeded
	 */
    private boolean writeToDB(DbPublish reportInfo, long reportIndex, Connection conn, int numOfTests) {
        try {
            DbGuiUtility.showMsgWithTime("creating report information from xml files");
            final int runIndex = reportInfo.addRunInfo(reportIndex, conn);
            if (runIndex == -1) {
                return false;
            }
            reportInfo.publishTestsInfo(conn, progressBar, runIndex);
            progressBar.setBarValue(numOfTests);
            DbGuiUtility.showMsgWithTime("setings tests data");
            DbGuiUtility.showMsgWithTime("finished processing tests information for publish");
            frame.close();
            PublisherTreePanel.setPublishBtnEnable(true);
            DbGuiUtility.showMsgWithTime("finished publishing");
            log.log(Level.FINEST, "finished publishing");
            return true;
        } catch (Exception e) {
            frame.setStartedPublish(false);
            DbGuiUtility.publishError("Publish error- fail to to connect to the Database", "Fail to connect to the Database\n\n" + "Try the following :\n\n" + "1)Check that Database Server is on\n\n" + "2)Check You Database Properties \n\n" + StringUtils.getStackTrace(e), ErrorLevel.Error);
        }
        return false;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(PublisherRunInfoFrame.okButton)) {
            run = frame.saveRunData();
            startPublish();
        } else if (source.equals(PublisherRunInfoFrame.cancelButton)) {
            frame.dispose();
            DBProperties.closeConnectionIfExists(conn);
        } else if (source.equals(DefineDbPropertiesDialog.okButton)) {
            save = true;
        } else if (source.equals(DefineDbPropertiesDialog.cancelButton)) {
            DbGuiUtility.validateDialogParams(this, false);
        }
    }

    public void connectionIsOk(boolean status, Connection con) {
        DefaultPublisher.conn = con;
        frame.connectionToDbExist(status);
        if (save && status) {
        }
        save = false;
    }

    /**
	 * This method will cast to DBConnectionListener as it was build for this
	 * reason. casting is not dangerous if implements wasn't changed in
	 * DBGuiUtils
	 * 
	 * @param dbSettingParams
	 *            represents the following: (host, port, driver, type, dbHost,
	 *            dbName, dbUser, dbPassword);
	 */
    @Override
    public boolean validatePublisher(Object object, String... dbSettingParams) {
        DBConnectionListener listener = (DBConnectionListener) object;
        String host = dbSettingParams[0];
        String port = dbSettingParams[1];
        String driver = dbSettingParams[2];
        String type = dbSettingParams[3];
        String dbHost = dbSettingParams[4];
        String dbName = dbSettingParams[5];
        String dbUser = dbSettingParams[6];
        String dbPassword = dbSettingParams[7];
        boolean validPublisher = false;
        String url = "http://" + host + ":" + port + "/reports";
        try {
            URL _url = new URL(url);
            _url.openConnection().connect();
            validPublisher = true;
        } catch (Exception e) {
            log.log(Level.FINE, "Failed validating url " + url, e);
        }
        if (validPublisher) {
            Connection conn;
            try {
                if (driver != null) {
                    conn = DBProperties.getInstance().getConnection(driver, dbHost, dbName, type, dbUser, dbPassword);
                } else {
                    conn = DBProperties.getInstance().getConnection();
                }
            } catch (Exception e) {
                conn = null;
                listener.connectionIsOk(false, null);
                validPublisher = false;
            }
            if (validPublisher) {
                if (!allNecessaryTablesCreated(conn)) {
                    conn = null;
                    listener.connectionIsOk(false, null);
                    validPublisher = false;
                }
                listener.connectionIsOk(true, conn);
            }
        } else {
            listener.connectionIsOk(false, null);
        }
        return validPublisher;
    }

    /**
	 * Description: Verify the database contains all necessary tables for proper
	 * operation
	 * 
	 * @param conn
	 *            - A Connection object to the database.
	 * @return: True - If database contains all necessary tables False - If
	 *          database does not contain all necessary tables
	 */
    private static boolean allNecessaryTablesCreated(Connection conn) {
        boolean allCreated = true;
        try {
            allCreated = new Run().check(conn) && new TestProperties().check(conn) && new Package().check(conn) && new PropertiesList().check(conn) && new Step().check(conn) && new Test().check(conn) && new TestName().check(conn);
        } catch (SQLException e) {
            allCreated = false;
        }
        return allCreated;
    }
}
