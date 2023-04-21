package edu.umich.soar;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.regex.Matcher;
import sml.Agent;
import sml.Kernel;

public class SoarProperties {

    private static final String SOAR_HOME = "SOAR_HOME";

    private static final String VERSION = "9_3_2";

    public String getVersion() {
        return VERSION;
    }

    public String getPrefix() {
        String prefix = tryEnvironment();
        if (prefix != null) return convertBackslashes(prefix);
        prefix = tryJarLocation();
        if (prefix != null) return convertBackslashes(prefix);
        return null;
    }

    private String convertBackslashes(String prefix) {
        return prefix.replaceAll(Matcher.quoteReplacement("\\"), Matcher.quoteReplacement("/"));
    }

    private String tryEnvironment() {
        String env = System.getenv(SOAR_HOME);
        if (env != null) if (!env.endsWith(File.separator)) return env.concat(File.separator);
        return env;
    }

    private String tryJarLocation() {
        String codeLoc = SoarProperties.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        if (codeLoc.endsWith("jar")) {
            for (int i = 0; i < 3; ++i) {
                int index = codeLoc.lastIndexOf(File.separator);
                if (index < 0) {
                    return null;
                }
                codeLoc = codeLoc.substring(0, index);
            }
            return codeLoc + File.separator;
        }
        return null;
    }

    public static void main(String[] args) {
        SoarProperties p = new SoarProperties();
        System.out.println("getPrefix: " + p.getPrefix());
        System.out.println("tryEnvironment: " + p.tryEnvironment());
        System.out.println("tryJarLocation: " + p.tryJarLocation());
    }

    public int getPid() {
        String[] nameSplit = ManagementFactory.getRuntimeMXBean().getName().split("@");
        try {
            if (nameSplit != null && nameSplit.length > 0) {
                return Integer.valueOf(nameSplit[0]);
            }
        } catch (NumberFormatException e) {
        }
        return -1;
    }

    @Deprecated
    public void spawnDebugger(Kernel kernel, Agent agent) {
        spawnDebugger(agent);
    }

    public void spawnDebugger(Agent agent) {
        int pid = getPid();
        String prefix = getPrefix();
        if (pid > 0) {
            System.out.println("spawning debugger for pid " + pid + " library loc " + prefix);
            agent.SpawnDebugger(pid, prefix);
        } else {
            int port = agent.GetKernel().GetListenerPort();
            System.out.println("spawning debugger for port " + port + " library loc " + prefix);
            agent.SpawnDebugger(port, prefix);
        }
    }
}
