package de.fmf;

import java.io.File;

/**
 * @author fma (Falko Macheleidt) 27.04.2009
 * 
 */
public class IRepos {

    public static final String SvnLocalhost = "svn://localhost";

    public static final String JDK16 = "javac1.6";

    public static final String JDK15 = "javac1.5";

    public static final String JDK14 = "javac1.4";

    public static final String JDK13 = "javac1.3";

    public static final String JDK12 = "javac1.2";

    public static final String JDK11 = "javac1.1";

    public static final File testRpospDir = new File("./ut_data/repos");

    public static final File dumpFileDir = new File("./ut_data/dumpFiles");

    public static final String odtBuildProjectsRepoName = "r_odt_build_projects";

    public static final String javaVersionRepoName = "r_java_version_check";

    public static final String javaVerionRepoDumpFileName = "r_java_version_check.dump.gz";

    public static final String odtBuildProjectsDumpFileName = "r_odt_build_projects.dump.gz";

    public static final String svnUser = "fma";

    public static final String svnPwd = "";

    public static final String UT_DEPLOY_HOME = System.getProperty("user.dir") + "/ut_deploy_home";

    public static String[] getDefaultOdtJunitStartArgs() {
        String[] defaultArgs = new String[7];
        defaultArgs[0] = "-listener";
        defaultArgs[1] = "de.orisa.odt.ODTBuildListener";
        defaultArgs[2] = "-buildfile";
        defaultArgs[3] = new File(System.getProperty("user.dir") + "/bin", "bootstrap-build.xml").getAbsolutePath();
        defaultArgs[4] = "-Dodt.home=" + System.getProperty("user.dir");
        defaultArgs[5] = "-Ddeploy.home=" + UT_DEPLOY_HOME;
        defaultArgs[6] = "-v";
        return defaultArgs;
    }
}
