package ostf.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProjectDataAgent {

    public static final String PROJECT_DATA_CENTER_FILE_PATH = "repository";

    public static final String PROJECT_MAIN_TEST_PLAN = "testplan.tpl";

    public static final String PROJECT_USER_PROFILE_DEFINITION = "userprofile.def";

    public static final String PROJECT_USER_PROFILE = "userprofile.xml";

    public static final String TEST_ACTIONSTATS_DUMP = "actionstats.csv";

    public static final String TEST_SERVERSTATS_DUMP = "serverstats.csv";

    public static final String TEST_SERVERENV_DUMP = "serverenv.csv";

    public static final String TEST_ACTIONSUMMARY_DUMP = "actionsummary.csv";

    public static final String TEST_COMPONENTSUMMARY_DUMP = "componentsummary.csv";

    public static final String TEST_SERVERSUMMARY_DUMP = "serversummary.csv";

    public static final String TEST_ACTIONREPORT_DUMP = "actionreport.csv";

    public static final String TEST_SERVERREPORT_DUMP = "serverreport.csv";

    public static final String TEST_DUMP_STATS = "dumpstats";

    public static final String TEST_USER_PROFILE = "testuserprofile";

    public static final String TEST_START_TIME = "ProjectDataAgent.testStartTime";

    public static final String TEST_END_TIME = "ProjectDataAgent.testEndTime";

    public static Log logger = LogFactory.getLog(ProjectDataAgent.class.getName());

    public static String remoteServiceUrl = null;

    public static boolean createEntity(String entity, Map<String, Object> data) {
        if (remoteServiceUrl != null) {
            try {
                return ((Boolean) JMXUtil.invoke(remoteServiceUrl, JMXUtil.retryConnEnv, GlobalNames.getDataCenterName(), "createEntity", new Object[] { entity, data }, new String[] { "java.lang.String", "java.util.Map" })).booleanValue();
            } catch (Exception e) {
                logger.error("Failed to create " + entity, e);
                return false;
            }
        }
        Method method;
        try {
            method = ProjectDataAgent.class.getMethod("create" + firstCapital(entity), new Class[] { data.getClass() });
        } catch (SecurityException e) {
            logger.error("Failed to create " + entity, e);
            return false;
        } catch (NoSuchMethodException e) {
            return DataAccessUtil.insertMapDataIntoTable(DataAccessUtil.PROJECT_DATABASE, entity, data);
        }
        try {
            return (Boolean) method.invoke(null, new Object[] { data });
        } catch (Exception e) {
            logger.error("Failed to create " + entity, e);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> getEntity(String entity, String[] columns, Map<String, Object> condition) {
        if (remoteServiceUrl != null) {
            try {
                return (HashMap<String, Object>) JMXUtil.invoke(remoteServiceUrl, JMXUtil.retryConnEnv, GlobalNames.getDataCenterName(), "getEntity", new Object[] { entity, columns, condition }, new String[] { "java.lang.String", "[Ljava.lang.String;", "java.util.Map" });
            } catch (Exception e) {
                logger.error("Failed to get " + entity, e);
                return null;
            }
        }
        Method method;
        try {
            method = ProjectDataAgent.class.getMethod("get" + firstCapital(entity), new Class[] { columns != null ? columns.getClass() : new String[0].getClass(), condition.getClass() });
        } catch (SecurityException e) {
            logger.error("Failed to get " + entity, e);
            return null;
        } catch (NoSuchMethodException e) {
            return DataAccessUtil.getMapDataFromTable(DataAccessUtil.PROJECT_DATABASE, entity, columns, condition);
        }
        try {
            return (HashMap<String, Object>) method.invoke(null, new Object[] { columns, condition });
        } catch (Exception e) {
            logger.error("Failed to get " + entity, e);
            return null;
        }
    }

    public static boolean updateEntity(String entity, Map<String, Object> data, String[] indexes) {
        if (remoteServiceUrl != null) {
            try {
                return ((Boolean) JMXUtil.invoke(remoteServiceUrl, JMXUtil.retryConnEnv, GlobalNames.getDataCenterName(), "updateEntity", new Object[] { entity, data, indexes }, new String[] { "java.lang.String", "java.util.Map", "[Ljava.lang.String;" })).booleanValue();
            } catch (Exception e) {
                logger.error("Failed to update " + entity, e);
                return false;
            }
        }
        Method method;
        try {
            method = ProjectDataAgent.class.getMethod("update" + firstCapital(entity), new Class[] { data.getClass(), indexes.getClass() });
        } catch (SecurityException e) {
            logger.error("Failed to update " + entity, e);
            return false;
        } catch (NoSuchMethodException e) {
            return DataAccessUtil.updateMapDataToTable(DataAccessUtil.PROJECT_DATABASE, entity, data, indexes);
        }
        try {
            return (Boolean) method.invoke(null, new Object[] { data, indexes });
        } catch (Exception e) {
            logger.error("Failed to update " + entity, e);
            return false;
        }
    }

    public static boolean deleteEntity(String entity, Map<String, Object> data, String[] indexes) {
        if (remoteServiceUrl != null) {
            try {
                return ((Boolean) JMXUtil.invoke(remoteServiceUrl, JMXUtil.retryConnEnv, GlobalNames.getDataCenterName(), "deleteEntity", new Object[] { entity, data, indexes }, new String[] { "java.lang.String", "java.util.Map", "[Ljava.lang.String;" })).booleanValue();
            } catch (Exception e) {
                logger.error("Failed to delete " + entity, e);
                return false;
            }
        }
        Method method;
        try {
            method = ProjectDataAgent.class.getMethod("delete" + firstCapital(entity), new Class[] { data.getClass(), indexes.getClass() });
        } catch (SecurityException e) {
            logger.error("Failed to delete " + entity, e);
            return false;
        } catch (NoSuchMethodException e) {
            return DataAccessUtil.deleteMapDataFromTable(DataAccessUtil.PROJECT_DATABASE, entity, data, indexes);
        }
        try {
            return (Boolean) method.invoke(null, new Object[] { data, indexes });
        } catch (Exception e) {
            logger.error("Failed to delete " + entity, e);
            return false;
        }
    }

    public static boolean createProject(HashMap<String, Object> data) {
        File projectFolder = getProjectFolder(data);
        if (projectFolder == null) return false;
        File testplan = null;
        File userprofile = null;
        boolean status = false;
        try {
            testplan = new File(projectFolder, PROJECT_MAIN_TEST_PLAN);
            File testplan_default = new File(PROJECT_DATA_CENTER_FILE_PATH + File.separator + PROJECT_MAIN_TEST_PLAN);
            if (testplan_default.exists()) DataTransformUtil.copyFile(testplan_default, testplan); else {
                if (testplan.createNewFile()) {
                    FileWriter fw = new FileWriter(testplan);
                    fw.append("<elements/>");
                    fw.close();
                }
            }
            userprofile = new File(projectFolder, PROJECT_USER_PROFILE_DEFINITION);
            File userprofile_default = new File(PROJECT_DATA_CENTER_FILE_PATH + File.separator + PROJECT_USER_PROFILE_DEFINITION);
            if (userprofile_default.exists()) DataTransformUtil.copyFile(userprofile_default, userprofile); else userprofile.createNewFile();
            if (testplan.exists() && userprofile.exists()) {
                data.put(DBColumns.COLUMN_TEST_PLAN, PROJECT_MAIN_TEST_PLAN);
                data.put(DBColumns.COLUMN_USER_PROFILE_DEF, PROJECT_USER_PROFILE_DEFINITION);
                status = DataAccessUtil.insertMapDataIntoTable(DataAccessUtil.PROJECT_DATABASE, DBColumns.TABLE_PROJECT, data);
            }
        } catch (IOException e) {
            logger.error("Failed to create files for project", e);
            status = false;
        }
        if (!status) {
            if (userprofile != null && userprofile.exists()) userprofile.delete();
            if (testplan != null && testplan.exists()) testplan.delete();
            if (projectFolder != null && projectFolder.exists()) projectFolder.delete();
        }
        return status;
    }

    public static boolean deleteProject(HashMap<String, Object> data, String[] indexes) {
        String projectName = (String) data.get(DBColumns.COLUMN_PROJECT_NAME);
        if (projectName == null) {
            data = getEntity(DataAccessUtil.PROJECT_DATABASE, null, data);
            if (data == null) return false;
            projectName = (String) data.get(DBColumns.COLUMN_PROJECT_NAME);
        }
        File projectFolder = new File(PROJECT_DATA_CENTER_FILE_PATH + File.separator + projectName);
        boolean status = false;
        int count = 0;
        File trashFolder = getTrashFolder();
        if (trashFolder == null) return false;
        if (projectFolder.exists()) {
            while (new File(trashFolder, projectName + ".rm" + count).exists()) count++;
            status = projectFolder.renameTo(new File(trashFolder, projectName + ".rm" + count));
            if (status) status = DataAccessUtil.deleteMapDataFromTable(DataAccessUtil.PROJECT_DATABASE, DBColumns.TABLE_PROJECT, data, indexes);
            if (status) return true; else {
                new File(trashFolder, projectName + ".rm" + count).renameTo(new File(PROJECT_DATA_CENTER_FILE_PATH + File.separator + projectName));
                return false;
            }
        } else {
            logger.info("project folder " + projectName + " doesn't exist");
            return true;
        }
    }

    public static boolean createTest(HashMap<String, Object> testInfo) {
        File projectFolder = getProjectFolder(testInfo);
        if (projectFolder == null) return false;
        String connStr = (String) testInfo.get(DataAccessUtil.RUNTIME_DATABASE);
        byte[] userProfile = (byte[]) testInfo.get(TEST_USER_PROFILE);
        if (userProfile == null) {
            logger.error("UserProfile is null in testInfo.");
            return false;
        }
        boolean dumpStats = false;
        if (testInfo.get(TEST_DUMP_STATS) != null) dumpStats = ((Boolean) testInfo.get(TEST_DUMP_STATS)).booleanValue();
        testInfo.remove(DataAccessUtil.RUNTIME_DATABASE);
        testInfo.remove(TEST_USER_PROFILE);
        testInfo.remove(TEST_DUMP_STATS);
        String startTime0 = (String) testInfo.remove(TEST_START_TIME);
        String endTime0 = (String) testInfo.remove(TEST_END_TIME);
        if (!DataAccessUtil.insertMapDataIntoTable(DataAccessUtil.PROJECT_DATABASE, DBColumns.TABLE_TEST, testInfo)) return false;
        String testName = (String) testInfo.get(DBColumns.COLUMN_TEST_NAME);
        File testFolder = new File(projectFolder, testName);
        if (testFolder.mkdir()) {
            Connection conn = null;
            try {
                copyProjectInfoToTestFolder(projectFolder, testFolder);
                File profile = new File(testFolder, PROJECT_USER_PROFILE);
                if (!DataTransformUtil.WriteFile(profile.getAbsolutePath(), userProfile)) return false;
                conn = DataAccessUtil.getDBConnection(connStr);
                String startTime = (String) testInfo.get(DBColumns.COLUMN_START_TIME);
                String endTime = (String) testInfo.get(DBColumns.COLUMN_END_TIME);
                boolean leftMargin = TimerUtil.getTimeFromFormat(startTime) > TimerUtil.getTimeFromFormat(startTime0);
                boolean rightMargin = TimerUtil.getTimeFromFormat(endTime) < TimerUtil.getTimeFromFormat(endTime0);
                boolean tempTable = leftMargin || rightMargin;
                String whereClause = null;
                if (leftMargin && rightMargin) {
                    whereClause = "where time>'" + startTime + "' and time<'" + endTime + "'";
                } else {
                    if (leftMargin) whereClause = "where time>'" + startTime + "'"; else if (rightMargin) whereClause = "where time<'" + endTime + "'";
                }
                if (tempTable) {
                    transferStats(conn, DBColumns.TABLE_ACTION_STATISTICS, DBColumns.TABLE_ACTION_STATISTICS_T, whereClause, true);
                    transferStats(conn, DBColumns.TABLE_SERVER_STATISTICS, DBColumns.TABLE_SERVER_STATISTICS_T, whereClause, true);
                }
                if (dumpStats) {
                    dumpTable(conn, tempTable ? DBColumns.TABLE_ACTION_STATISTICS_T : DBColumns.TABLE_ACTION_STATISTICS, new File(testFolder, TEST_ACTIONSTATS_DUMP));
                    dumpTable(conn, tempTable ? DBColumns.TABLE_SERVER_STATISTICS_T : DBColumns.TABLE_SERVER_STATISTICS, new File(testFolder, TEST_SERVERSTATS_DUMP));
                    dumpTable(conn, tempTable ? DBColumns.VIEW_PERIODICAL_ACTION_T : DBColumns.VIEW_PERIODICAL_ACTION, new File(testFolder, TEST_ACTIONSUMMARY_DUMP));
                    dumpTable(conn, tempTable ? DBColumns.VIEW_PERIODICAL_COMPONENT_T : DBColumns.VIEW_PERIODICAL_COMPONENT, new File(testFolder, TEST_COMPONENTSUMMARY_DUMP));
                    dumpTable(conn, tempTable ? DBColumns.VIEW_PERIODICAL_SERVER_T : DBColumns.VIEW_PERIODICAL_SERVER, new File(testFolder, TEST_SERVERSUMMARY_DUMP));
                }
                File serverenv = new File(testFolder, TEST_SERVERENV_DUMP);
                File actionreport = new File(testFolder, TEST_ACTIONREPORT_DUMP);
                File serverreport = new File(testFolder, TEST_SERVERREPORT_DUMP);
                dumpTable(conn, DBColumns.TABLE_SERVER_ENV, serverenv);
                dumpTable(conn, tempTable ? DBColumns.VIEW_ACTION_REPORT_T : DBColumns.VIEW_ACTION_REPORT, actionreport);
                dumpTable(conn, tempTable ? DBColumns.VIEW_SERVER_REPORT_T : DBColumns.VIEW_SERVER_REPORT, serverreport);
                DataAccessUtil.releaseDBResource(conn, null, null);
                conn = DataAccessUtil.getDBConnection(DataAccessUtil.PROJECT_DATABASE);
                transferStats(testName, conn, DBColumns.TABLE_TEST_ENV, serverenv);
                transferStats(testName, conn, DBColumns.TABLE_ACTION_SUMMARY, actionreport);
                transferStats(testName, conn, DBColumns.TABLE_SERVER_SUMMARY, serverreport);
                DataAccessUtil.releaseDBResource(conn, null, null);
                return true;
            } catch (Exception e) {
                logger.error("Failed to create test", e);
                return false;
            } finally {
                DataAccessUtil.releaseDBResource(conn, null, null);
            }
        }
        logger.error("Can't create folder " + testFolder.getAbsolutePath());
        return false;
    }

    public static boolean deleteTest(HashMap<String, Object> data, String[] indexes) {
        String projectName = (String) data.get(DBColumns.COLUMN_PROJECT_NAME);
        String testName = (String) data.get(DBColumns.COLUMN_TEST_NAME);
        File testFolder = new File(PROJECT_DATA_CENTER_FILE_PATH + File.separator + projectName + File.separator + testName);
        if (testFolder.exists()) {
            if (DataTransformUtil.recursiveDelete(testFolder)) {
                logger.info("Deleted old test folder " + testFolder.getAbsolutePath());
            } else {
                logger.error("Unable to delete old test folder " + testFolder.getAbsolutePath());
            }
        }
        if (!DataAccessUtil.deleteMapDataFromTable(DataAccessUtil.PROJECT_DATABASE, DBColumns.TABLE_TEST_ENV, data, new String[] { DBColumns.COLUMN_TEST_NAME })) return false;
        logger.info("Deleted data from table " + DBColumns.TABLE_TEST_ENV + " for testName=" + testName);
        if (!DataAccessUtil.deleteMapDataFromTable(DataAccessUtil.PROJECT_DATABASE, DBColumns.TABLE_ACTION_SUMMARY, data, new String[] { DBColumns.COLUMN_TEST_NAME })) return false;
        logger.info("Deleted data from table " + DBColumns.TABLE_ACTION_SUMMARY + " for testName=" + testName);
        if (!DataAccessUtil.deleteMapDataFromTable(DataAccessUtil.PROJECT_DATABASE, DBColumns.TABLE_SERVER_SUMMARY, data, new String[] { DBColumns.COLUMN_TEST_NAME })) return false;
        logger.info("Deleted data from table " + DBColumns.TABLE_SERVER_SUMMARY + " for testName=" + testName);
        if (!DataAccessUtil.deleteMapDataFromTable(DataAccessUtil.PROJECT_DATABASE, DBColumns.TABLE_TEST, data, indexes)) return false;
        logger.info("Deleted data from table " + DBColumns.TABLE_TEST + " for testName=" + testName);
        return true;
    }

    public static boolean newTestPlan(HashMap<String, Object> project, String plan) {
        if (remoteServiceUrl != null) {
            try {
                return ((Boolean) JMXUtil.invoke(remoteServiceUrl, JMXUtil.retryConnEnv, GlobalNames.getDataCenterName(), "newTestPlan", new Object[] { project, plan }, new String[] { "java.util.HashMap", "java.lang.String" })).booleanValue();
            } catch (Exception e) {
                logger.error("Failed to newTestPlan " + plan, e);
                return false;
            }
        }
        File projectFolder = getProjectFolder(project);
        if (projectFolder == null) return false;
        File testplan = null;
        boolean status = false;
        try {
            testplan = new File(projectFolder, plan);
            if (testplan.createNewFile()) {
                FileWriter fw = new FileWriter(testplan);
                fw.append("<elements/>");
                fw.close();
                status = updateEntity(DBColumns.TABLE_PROJECT, project, new String[] { DBColumns.COLUMN_PROJECT_NAME });
            }
        } catch (IOException e) {
            logger.error("Failed to create " + plan, e);
            status = false;
        }
        if (!status) {
            if (testplan != null && testplan.exists()) testplan.delete();
        }
        return status;
    }

    public static byte[][] updateTestPlans(String project, String[] plans) {
        if (plans == null || plans.length == 0) return null;
        if (remoteServiceUrl != null) {
            try {
                return (byte[][]) JMXUtil.invoke(remoteServiceUrl, JMXUtil.retryConnEnv, GlobalNames.getDataCenterName(), "updateTestPlans", new Object[] { project, plans }, new String[] { "java.lang.String", "[Ljava.lang.String;" });
            } catch (Exception e) {
                logger.error("Failed to updateTestPlans for project " + project, e);
                return null;
            }
        }
        File projectFolder = new File(PROJECT_DATA_CENTER_FILE_PATH + File.separator + project);
        if (!projectFolder.exists()) {
            logger.error("Project folder for '" + project + "' doesn't exist");
            return null;
        }
        byte[][] result = new byte[plans.length][];
        for (int i = 0; i < plans.length; i++) {
            try {
                result[i] = DataTransformUtil.ReadFile(PROJECT_DATA_CENTER_FILE_PATH + File.separator + project + File.separator + plans[i]);
            } catch (IOException e) {
                logger.error("Failed to read test plan " + plans[i]);
                result[i] = null;
            }
        }
        return result;
    }

    public static boolean commitTestPlans(String project, String[] plans, byte[][] data) {
        if (remoteServiceUrl != null) {
            try {
                return ((Boolean) JMXUtil.invoke(remoteServiceUrl, JMXUtil.retryConnEnv, GlobalNames.getDataCenterName(), "commitTestPlans", new Object[] { project, plans, data }, new String[] { "java.lang.String", "[Ljava.lang.String;", "[[B" })).booleanValue();
            } catch (Exception e) {
                logger.error("Failed to commitTestPlans for project " + project, e);
                return false;
            }
        }
        File projectFolder = new File(PROJECT_DATA_CENTER_FILE_PATH + File.separator + project);
        if (!projectFolder.exists()) {
            logger.error("Project folder for '" + project + "' doesn't exist");
            return false;
        }
        for (int i = 0; plans != null && i < plans.length; i++) {
            File plan = new File(projectFolder, plans[i]);
            try {
                if (data[i] != null && !DataTransformUtil.WriteFile(plan.getAbsolutePath(), data[i])) return false;
            } catch (IOException e) {
                logger.error("Failed to write test plan " + plans[i]);
                return false;
            }
        }
        return true;
    }

    public static boolean removeTestPlan(HashMap<String, Object> project, String plan) {
        if (remoteServiceUrl != null) {
            try {
                return ((Boolean) JMXUtil.invoke(remoteServiceUrl, JMXUtil.retryConnEnv, GlobalNames.getDataCenterName(), "removeTestPlan", new Object[] { project, plan }, new String[] { "java.util.HashMap", "java.lang.String" })).booleanValue();
            } catch (Exception e) {
                logger.error("Failed to removeTestPlan '" + plan + "' for project " + project, e);
                return false;
            }
        }
        File projectFolder = getProjectFolder(project);
        File trashFolder = getTrashFolder();
        if (projectFolder == null || trashFolder == null) return false;
        File testplan = new File(projectFolder, plan);
        boolean status = false;
        int count = 0;
        if (testplan.exists()) {
            while (new File(trashFolder, plan + ".rm" + count).exists()) count++;
            status = testplan.renameTo(new File(trashFolder, plan + ".rm" + count));
            if (status) status = updateEntity(DBColumns.TABLE_PROJECT, project, new String[] { DBColumns.COLUMN_PROJECT_NAME });
            if (status) return true; else {
                new File(trashFolder, plan + ".rm" + count).renameTo(new File(projectFolder, plan));
                return false;
            }
        } else {
            logger.info("Test plan '" + plan + "' doesn't exist");
            return true;
        }
    }

    public static boolean makeUserProfileDef(String project) {
        if (remoteServiceUrl != null) {
            try {
                return ((Boolean) JMXUtil.invoke(remoteServiceUrl, JMXUtil.retryConnEnv, GlobalNames.getDataCenterName(), "makeUserProfileDef", new Object[] { project }, new String[] { "java.lang.String" })).booleanValue();
            } catch (Exception e) {
                logger.error("Failed to makeUserProfileDef for project " + project, e);
                return false;
            }
        }
        String projectFolder = PROJECT_DATA_CENTER_FILE_PATH + File.separator + project;
        if (!new File(projectFolder).exists()) {
            logger.error("Project folder for '" + project + "' doesn't exist");
            return false;
        }
        try {
            UserProfileUtil.generateXsd(projectFolder + File.separator + PROJECT_MAIN_TEST_PLAN, projectFolder + File.separator + PROJECT_USER_PROFILE_DEFINITION);
            return true;
        } catch (IOException e) {
            logger.error("Failed to makeUserProfileDef for project " + project, e);
            return false;
        }
    }

    public static byte[] getUserProfileDef(String project) {
        if (remoteServiceUrl != null) {
            try {
                return (byte[]) JMXUtil.invoke(remoteServiceUrl, JMXUtil.retryConnEnv, GlobalNames.getDataCenterName(), "getUserProfileDef", new Object[] { project }, new String[] { "java.lang.String" });
            } catch (Exception e) {
                logger.error("Failed to getUserProfileDef for project " + project, e);
                return null;
            }
        }
        String projectFolder = PROJECT_DATA_CENTER_FILE_PATH + File.separator + project;
        if (!new File(projectFolder).exists()) {
            logger.error("Project folder for '" + project + "' doesn't exist");
            return null;
        }
        try {
            return DataTransformUtil.ReadFile(projectFolder + File.separator + PROJECT_USER_PROFILE_DEFINITION);
        } catch (IOException e) {
            logger.error("Failed to getUserProfileDef for project " + project, e);
            return null;
        }
    }

    public static boolean updateUserProfile(String project, byte[] data) {
        if (remoteServiceUrl != null) {
            try {
                return ((Boolean) JMXUtil.invoke(remoteServiceUrl, JMXUtil.retryConnEnv, GlobalNames.getDataCenterName(), "updateUserProfile", new Object[] { project, data }, new String[] { "java.lang.String", "[B" })).booleanValue();
            } catch (Exception e) {
                logger.error("Failed to updateUserProfile for project " + project, e);
                return false;
            }
        }
        String projectFolder = PROJECT_DATA_CENTER_FILE_PATH + File.separator + project;
        if (!new File(projectFolder).exists()) {
            logger.error("Project folder for '" + project + "' doesn't exist");
            return false;
        }
        try {
            DataTransformUtil.WriteFile(projectFolder + File.separator + PROJECT_USER_PROFILE, data);
            return true;
        } catch (IOException e) {
            logger.error("Failed to updateUserProfile for project " + project, e);
            return false;
        }
    }

    public static byte[] getUserProfile(String project) {
        if (remoteServiceUrl != null) {
            try {
                return (byte[]) JMXUtil.invoke(remoteServiceUrl, JMXUtil.retryConnEnv, GlobalNames.getDataCenterName(), "getUserProfile", new Object[] { project }, new String[] { "java.lang.String" });
            } catch (Exception e) {
                logger.error("Failed to getUserProfile for project " + project, e);
                return null;
            }
        }
        String projectFolder = PROJECT_DATA_CENTER_FILE_PATH + File.separator + project;
        if (!new File(projectFolder).exists()) {
            logger.error("Project folder for '" + project + "' doesn't exist");
            return null;
        }
        try {
            return DataTransformUtil.ReadFile(projectFolder + File.separator + PROJECT_USER_PROFILE);
        } catch (IOException e) {
            logger.error("Failed to getUserProfile for project " + project, e);
            return null;
        }
    }

    private static void copyProjectInfoToTestFolder(File projectFolder, File testFolder) {
        if (!projectFolder.exists() || !testFolder.exists()) return;
        String[] ls = projectFolder.list();
        for (int idx = 0; idx < ls.length; idx++) {
            File from = new File(projectFolder, ls[idx]);
            File to = new File(testFolder, ls[idx]);
            if (!from.isDirectory()) {
                if (DataTransformUtil.copyFile(from, to)) {
                    logger.info(ls[idx] + " was copied to " + testFolder.getAbsolutePath());
                } else {
                    logger.error("unable to copy " + ls[idx] + " to " + testFolder.getAbsolutePath());
                }
            }
        }
    }

    private static File getProjectFolder(HashMap<String, Object> project) {
        String projectName = (String) project.get(DBColumns.COLUMN_PROJECT_NAME);
        if (projectName == null) return null;
        File projectFolder = new File(PROJECT_DATA_CENTER_FILE_PATH + File.separator + projectName);
        if (!projectFolder.exists() && !projectFolder.mkdir()) {
            logger.error("Fail to create project folder for " + projectName);
            return null;
        }
        return projectFolder;
    }

    private static File getTrashFolder() {
        File trashFolder = new File(new StringBuffer(PROJECT_DATA_CENTER_FILE_PATH).append(File.separator).append("Trash").append(File.separator).toString());
        if (!trashFolder.exists() && !trashFolder.mkdir()) {
            logger.error("Fail to create trash folder");
            return null;
        }
        return trashFolder;
    }

    private static void dumpTable(Connection conn, String table, File dest) throws Exception {
        if (conn == null) throw new SQLException("Getting Null connection when dumping table " + table);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("select * from " + table);
            rs = ps.executeQuery();
            if (rs != null) {
                if (DataTransformUtil.writeCSV(rs, dest)) logger.info("Succeeded in dumping " + table + " to " + dest.getAbsolutePath());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DataAccessUtil.releaseDBResource(null, ps, rs);
        }
    }

    private static void transferStats(Connection conn, String sourceTable, String destTable, String whereClause, boolean purgeDest) throws Exception {
        if (conn == null) throw new SQLException("Getting Null connection when transfering stats from table " + sourceTable + " to table " + destTable);
        PreparedStatement ps = null;
        try {
            if (purgeDest) {
                ps = conn.prepareStatement("delete from " + destTable);
                ps.execute();
            }
            ps.execute();
            ps = conn.prepareStatement("insert into " + destTable + " select * from " + sourceTable + " " + whereClause);
            ps.execute();
        } catch (Exception e) {
            throw e;
        } finally {
            DataAccessUtil.releaseDBResource(null, ps, null);
        }
    }

    private static void transferStats(String testName, Connection conn, String table, File source) throws Exception {
        if (conn == null) throw new SQLException("Getting Null connection when transfering stats from file " + source.getAbsolutePath() + " to table " + table);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("select * from " + table + " where " + DBColumns.COLUMN_TEST_NAME + "='" + testName + "'");
            rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columnSize = meta.getColumnCount();
            int[] columnTypes = new int[columnSize - 1];
            StringBuffer sql = new StringBuffer("insert into ").append(table).append(" values(");
            for (int i = 1; i <= columnSize; i++) {
                if (i > 1) {
                    columnTypes[i - 2] = meta.getColumnType(i);
                    sql.append(",");
                    sql.append("?");
                } else {
                    sql.append("'").append(testName).append("'");
                }
            }
            sql.append(")");
            DataAccessUtil.releaseDBResource(null, ps, rs);
            ps = conn.prepareStatement(sql.toString());
            if (DataTransformUtil.readCSV(ps, columnTypes, source)) logger.info("Succeeded in transferring " + source.getAbsolutePath() + " to " + table);
        } catch (Exception e) {
            throw e;
        } finally {
            DataAccessUtil.releaseDBResource(null, ps, rs);
        }
    }

    public static byte[] readTestPlan(String project, String plan) {
        if (remoteServiceUrl != null) {
            try {
                return (byte[]) JMXUtil.invoke(remoteServiceUrl, JMXUtil.retryConnEnv, GlobalNames.getDataCenterName(), "readTestPlan", new Object[] { project, plan }, new String[] { "java.lang.String", "java.lang.String" });
            } catch (Exception e) {
                logger.error("Failed to readTestPlan '" + plan + "' for project " + project, e);
                return null;
            }
        }
        try {
            return DataTransformUtil.ReadFile(PROJECT_DATA_CENTER_FILE_PATH + File.separator + project + File.separator + plan);
        } catch (IOException e) {
            logger.error("Failed to readTestPlan '" + plan + "' for project " + project, e);
            return null;
        }
    }

    public static byte[] readTestFile(String project, String test, String file) {
        if (remoteServiceUrl != null) {
            try {
                return (byte[]) JMXUtil.invoke(remoteServiceUrl, JMXUtil.retryConnEnv, GlobalNames.getDataCenterName(), "readTestFile", new Object[] { project, test, file }, new String[] { "java.lang.String", "java.lang.String", "java.lang.String" });
            } catch (Exception e) {
                logger.error("Failed to readTestFile '" + file + "' for test " + test, e);
                return null;
            }
        }
        try {
            return DataTransformUtil.ReadFile(PROJECT_DATA_CENTER_FILE_PATH + File.separator + project + File.separator + test + File.separator + file);
        } catch (IOException e) {
            logger.error("Failed to readTestFile '" + file + "' for test " + test, e);
            return null;
        }
    }

    private static String firstCapital(String name) {
        if (name == null || name.length() < 1) return name;
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
