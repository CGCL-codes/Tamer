package ostf.gui.frame.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import ostf.data.DataTransformUtil;
import ostf.data.ProjectDataAgent;
import ostf.gui.frame.AdminConsole;
import bsh.EvalError;
import bsh.Interpreter;

public class HTMLReportBuilder {

    public static final String USER_PROFILE_HTML_XSL = "resource/report/html/profile_html.xsl";

    public static Transformer profileTransformer = null;

    private static Interpreter interpreter = new Interpreter();

    static {
        try {
            profileTransformer = TransformerFactory.newInstance().newTransformer(new StreamSource(new FileInputStream(USER_PROFILE_HTML_XSL)));
        } catch (Exception e) {
            AdminConsole.logger.warn("Failed to initialize user profile transformer by xsl file = " + USER_PROFILE_HTML_XSL, e);
        }
    }

    public static void buildClassicHistoryReport(String test, String destination, long runTime, boolean hasResource) throws IOException {
        String content = new String(DataTransformUtil.ReadFile("resource/report/html/classicHistoryReport.html"));
        try {
            interpreter.set("testName", test);
        } catch (EvalError e) {
        }
        try {
            interpreter.set("runTime", runTime);
        } catch (EvalError e) {
        }
        content = translateReport(content, true);
        DataTransformUtil.WriteFile(destination + File.separator + "classicHistoryReport.html", content.getBytes());
        if (hasResource) DataTransformUtil.copyFile(new File("resource/report/html/report.css"), new File(destination + File.separator + "report.css"));
    }

    public static void buildEnhancedHistoryReport(String test, String destination, long runTime, boolean hasResource) throws IOException {
        String content = new String(DataTransformUtil.ReadFile("resource/report/html/enhancedHistoryReport.html"));
        try {
            interpreter.set("testName", test);
        } catch (EvalError e) {
        }
        try {
            interpreter.set("runTime", runTime);
        } catch (EvalError e) {
        }
        content = translateReport(content, true);
        DataTransformUtil.WriteFile(destination + File.separator + "enhancedHistoryReport.html", content.getBytes());
        if (hasResource) {
            copyEnhancedReportResources(destination);
        }
    }

    public static void buildClassicTestReport(String start, String end, String project, String destination, long runTime, boolean hasResource) throws IOException {
        String content = new String(DataTransformUtil.ReadFile("resource/report/html/classicTestReport.html"));
        try {
            interpreter.set("projectName", project);
        } catch (EvalError e) {
        }
        try {
            interpreter.set("runTime", runTime);
        } catch (EvalError e) {
        }
        try {
            interpreter.set("startTime", start);
        } catch (EvalError e) {
        }
        try {
            interpreter.set("endTime", end);
        } catch (EvalError e) {
        }
        content = translateReport(content, false);
        DataTransformUtil.WriteFile(destination + File.separator + "classicTestReport.html", content.getBytes());
        if (hasResource) DataTransformUtil.copyFile(new File("resource/report/html/report.css"), new File(destination + File.separator + "report.css"));
    }

    public static void buildEnhancedTestReport(String start, String end, String project, String destination, long runTime, boolean hasResource) throws IOException {
        String content = new String(DataTransformUtil.ReadFile("resource/report/html/enhancedTestReport.html"));
        try {
            interpreter.set("projectName", project);
        } catch (EvalError e) {
        }
        try {
            interpreter.set("runTime", runTime);
        } catch (EvalError e) {
        }
        try {
            interpreter.set("startTime", start);
        } catch (EvalError e) {
        }
        try {
            interpreter.set("endTime", end);
        } catch (EvalError e) {
        }
        content = translateReport(content, false);
        DataTransformUtil.WriteFile(destination + File.separator + "enhancedTestReport.html", content.getBytes());
        if (hasResource) {
            copyEnhancedReportResources(destination);
        }
    }

    private static String translateReport(String content, boolean history) {
        int beginIndex = content.indexOf("<![CDATA[");
        int endIndex = -1;
        while (beginIndex > 0) {
            endIndex = content.indexOf("]]>", beginIndex);
            if (endIndex < 0) break;
            String beanShell = content.substring(beginIndex + 9, endIndex);
            String output = "";
            try {
                output = (String) interpreter.eval(beanShell);
            } catch (EvalError e) {
                AdminConsole.logger.error("Error in bean shell script", e);
            }
            content = new StringBuffer(content.substring(0, beginIndex)).append(output).append(content.substring(endIndex + 3)).toString();
            beginIndex = content.indexOf("<![CDATA[");
        }
        return content.replaceAll("\\$USER_PROFILE\\$", makeUserProfile(history));
    }

    private static void copyEnhancedReportResources(String destination) throws IOException {
        DataTransformUtil.copyFile(new File("resource/report/html/report.css"), new File(destination + File.separator + "report.css"));
        DataTransformUtil.copyFile(new File("resource/report/html/report.js"), new File(destination + File.separator + "report.js"));
        File gifFolder = new File(destination + File.separator + "gif");
        if (!gifFolder.exists()) gifFolder.mkdir();
        File[] gifs = new File("resource/report/html/gif").listFiles();
        for (int i = 0; i < gifs.length; i++) DataTransformUtil.copyFile(gifs[i], new File(gifFolder, gifs[i].getName()));
    }

    private static String makeUserProfile(boolean history) {
        if (profileTransformer != null) {
            FileInputStream fr = null;
            File profileFile = history ? AdminConsole.getInstance().getCurrentTestPanel().getUserProfile() : new File(AdminConsole.getInstance().getProjectTree().getProjectFolder(), ProjectDataAgent.PROJECT_USER_PROFILE);
            try {
                fr = new FileInputStream(profileFile);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                profileTransformer.transform(new StreamSource(fr), new StreamResult(bao));
                return bao.toString();
            } catch (Exception e) {
                AdminConsole.logger.warn("Failed to transform user profile file = " + profileFile.getAbsolutePath(), e);
            } finally {
                if (fr != null) try {
                    fr.close();
                } catch (IOException e) {
                }
            }
        }
        return "";
    }
}
