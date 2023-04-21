package innerbus.logtrap.main;

import innerbus.logtrap.common.ProjectHandler;
import innerbus.logtrap.common.ProjectListXmlParser;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class StartCollector {

    private String LQC_HOME;

    private String fileAbsolutePath;

    private final String SCRIPT_FILE = "lqc-deamon.sh";

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        StartCollector collector = new StartCollector();
        try {
            collector.readFile(args);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private StartCollector() {
        setVariables();
    }

    /**
	 * set variables
	 */
    private void setVariables() {
        LQC_HOME = System.getenv("LQC_HOME");
        if (LQC_HOME == null) {
            LQC_HOME = ProjectListXmlParser.getInstance().getRootPath();
        }
        fileAbsolutePath = new File(LQC_HOME, ProjectListXmlParser.CONFIG_FILENAME).getAbsolutePath();
    }

    private void readFile(String[] selectedProjects) throws IOException, InterruptedException {
        String projectName = null;
        ProjectHandler handler = null;
        ProjectListXmlParser parser = ProjectListXmlParser.getInstance();
        parser.setFilePath(fileAbsolutePath);
        boolean isNone = false;
        List projectList = null;
        if (selectedProjects.length > 0) {
            isNone = selectedProjects[0].equals("-none");
            if (isNone) {
                projectList = parser.getProjectList();
            } else {
                projectList = Arrays.asList(selectedProjects);
            }
        } else {
            projectList = parser.getProjectList();
        }
        Iterator<String> projectNames = projectList.iterator();
        while (projectNames.hasNext()) {
            projectName = projectNames.next();
            handler = parser.find(projectName);
            if (!isNone && handler.isRun) {
                executeScript(handler);
            }
        }
        if (!isNone && projectList.size() > 0) {
            parser.changeRun(projectList, true);
        }
    }

    private void executeScript(ProjectHandler handler) throws IOException, InterruptedException {
        String sKind = handler.iKind + "";
        String projectName = handler.sName;
        String savePAth = "";
        String serverIP = "";
        String serverPort = "";
        String isCompress = "";
        String compressType = "";
        String isDivide = "";
        String dateFormat = "";
        String isStoreOriginalLog = "";
        switch(handler.iKind) {
            case ProjectHandler.UDP:
                savePAth = handler.sSYSSavePath + "/" + projectName;
                serverIP = handler.sSYSIP;
                serverPort = handler.iSYSPort + "";
                isCompress = "null";
                compressType = handler.isCompress ? "zip" : "txt";
                isDivide = "isDivide";
                dateFormat = handler.sDateFormat;
                isStoreOriginalLog = handler.isSourceLogDelete ? "0" : "1";
                break;
            case ProjectHandler.FTP:
                savePAth = LQC_HOME;
                serverIP = handler.sFTPServer;
                serverPort = handler.iFTPPort + "";
                break;
            default:
                break;
        }
        String cmd = LQC_HOME + "/shell/" + SCRIPT_FILE + " start " + sKind + " " + projectName + " " + savePAth + " " + serverIP + " " + serverPort + " " + isCompress + " " + compressType + " " + isDivide + " " + dateFormat + " " + isStoreOriginalLog;
        System.out.println("[StartCollector][executeScript] command: " + cmd);
        Process process = Runtime.getRuntime().exec(cmd);
        System.out.println("" + process.waitFor());
    }
}
