package net.sf.buildbox.worker.TODO;

import net.sf.buildbox.util.BbxCommandline;
import net.sf.buildbox.util.BbxSystemUtils;
import net.sf.buildbox.worker.api.ExecutionContext;
import java.io.File;
import java.util.*;

public abstract class AbstractMavenSpecialist {

    private List<String> envVarsToPropagate = new ArrayList<String>();

    private Map<String, String> envVars = new HashMap<String, String>();

    private File jdkHome;

    private File mavenHome;

    private List<String> mavenTargets = new ArrayList<String>(Arrays.asList("install"));

    private Map<String, String> mavenProperties = new HashMap<String, String>();

    private List<String> activeProfiles = new ArrayList<String>();

    public List<String> getEnvVarsToPropagate() {
        return envVarsToPropagate;
    }

    public void setEnvVarsToPropagate(List<String> envVarsToPropagate) {
        this.envVarsToPropagate = envVarsToPropagate;
    }

    public Map<String, String> getEnvVars() {
        return envVars;
    }

    public void setEnvVars(Map<String, String> envVars) {
        this.envVars = envVars;
    }

    public File getJdkHome() {
        return jdkHome;
    }

    public void setJdkHome(File jdkHome) {
        this.jdkHome = jdkHome;
    }

    public File getMavenHome() {
        return mavenHome;
    }

    public void setMavenHome(File mavenHome) {
        this.mavenHome = mavenHome;
    }

    public List<String> getMavenTargets() {
        return mavenTargets;
    }

    public void setMavenTargets(List<String> mavenTargets) {
        this.mavenTargets = mavenTargets;
    }

    public Map<String, String> getMavenProperties() {
        return mavenProperties;
    }

    public void setMavenProperties(Map<String, String> mavenProperties) {
        this.mavenProperties = mavenProperties;
    }

    public List<String> getActiveProfiles() {
        return activeProfiles;
    }

    public void setActiveProfiles(List<String> activeProfiles) {
        this.activeProfiles = activeProfiles;
    }

    protected BbxCommandline createMavenCommandline(ExecutionContext ec, String... additionalOptions) {
        final BbxCommandline cl = new BbxCommandline();
        cl.clear();
        cl.setExecutable(jdkHome.getAbsolutePath() + "/bin/java");
        cl.addEnvironment("JAVA_HOME", jdkHome.getAbsolutePath());
        final String envMavenOpts = envVars.get("MAVEN_OPTS");
        if (envMavenOpts != null) {
            final BbxCommandline.Argument arg = new BbxCommandline.Argument();
            arg.setLine(envMavenOpts);
            cl.addArg(arg);
        }
        cl.addArguments(new String[] { "-classpath", new File(mavenHome, "boot/classworlds-1.1.jar").getAbsolutePath(), "-Dclassworlds.conf=" + mavenHome + "/bin/m2.conf", "-Dmaven.home=" + mavenHome, "org.codehaus.classworlds.Launcher" });
        cl.setWorkingDirectory(ec.getSandbox());
        cl.addArguments(new String[] { "--show-version", "--batch-mode", "--no-plugin-registry", "--no-plugin-updates", "--file", BbxSystemUtils.relpath(ec.getCode().getAbsolutePath() + "/pom.xml", ec.getSandbox()), "--settings", ec.getSettings() + "/settings.xml", "-Dmaven.repo.local=" + ec.getRepository() });
        cl.addArguments(additionalOptions);
        cl.addArguments(mavenTargets.toArray(new String[mavenTargets.size()]));
        for (Map.Entry<String, String> entry : mavenProperties.entrySet()) {
            cl.addArguments(new String[] { String.format("-D%s=%s", entry.getKey(), entry.getValue()) });
        }
        if (!activeProfiles.isEmpty()) {
            final StringBuilder profileList = new StringBuilder();
            for (String activeProfile : activeProfiles) {
                if (profileList.length() > 0) {
                    profileList.append(',');
                }
                profileList.append(activeProfile);
            }
            cl.addArguments(new String[] { "--activate-profiles", profileList.toString() });
        }
        for (String envVarName : envVarsToPropagate) {
            cl.addEnvironment(envVarName, System.getenv(envVarName));
        }
        for (Map.Entry<String, String> entry : envVars.entrySet()) {
            cl.addEnvironment(entry.getKey(), entry.getValue());
        }
        return cl;
    }

    protected void addCleanAsFirstTarget() {
        if (mavenTargets.isEmpty() || !mavenTargets.get(0).equals("clean")) {
            if (mavenTargets.contains("clean")) {
                throw new IllegalArgumentException("'clean' must be specified as the first maven target, or not specified at all (will be added automatically)");
            }
            System.out.println("INFO: adding phase 'clean' as the first target - this is required for incremental build");
            mavenTargets.add(0, "clean");
        }
    }
}
