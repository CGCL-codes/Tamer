package jp.ac.jaist.ceqea;

import java.io.*;
import java.net.*;

public class CEq_system {

    private static final String run_jar = CEq_0_cli.class.getProtectionDomain().getCodeSource().getLocation().getPath();

    public static final String run_dir = URLdecode(new File(run_jar).getParent() + File.separator);

    public static final String info_dir = run_dir + "info" + File.separator;

    public static final String lib_dir = run_dir + "lib" + File.separator;

    public static final String MIG_dir = run_dir + "MIG" + File.separator;

    public static final String ceqea_out_Name = "CEqEA_out";

    public static final String ceqea_out = System.getProperty(ceqea_out_Name);

    public static final String graphviz_dot_Name = "Graphviz_dot";

    public static final String graphviz_dot = (System.getProperty(graphviz_dot_Name) != null) ? System.getProperty(graphviz_dot_Name) : "dot";

    public static final String ceqea_tmp_Name = "CEqEA_tmp";

    public static final String ceqea_tmp = (System.getProperty(ceqea_tmp_Name) != null) ? System.getProperty(ceqea_tmp_Name) : System.getProperty("java.io.tmpdir");

    public static final String zgrviewer_Name = "ZGRViewer";

    public static final String zgrviewer = (System.getProperty(zgrviewer_Name) != null) ? System.getProperty(zgrviewer_Name) : lib_dir + "zgrviewer-0.9.0-SNAPSHOT.jar";

    private static final String ceqea_zgrviewer_Name = "CEqEA_ZGRViewer";

    private static final String ceqea_zgrviewer = (System.getProperty(ceqea_zgrviewer_Name) != null) ? System.getProperty(ceqea_zgrviewer_Name) : run_dir + "CEqEA2.jar";

    private static final String zgrXmx_Name = "ZGRViewer_Xmx";

    private static final String zgrXmx = (System.getProperty(zgrXmx_Name) != null) ? "-Xmx" + System.getProperty(zgrXmx_Name) : null;

    public static String[] ZGRVplugin(String name) {
        return (CEq_system.zgrXmx == null) ? new String[] { "java", "-jar", CEq_system.zgrviewer, "-pluginList=" + CEq_system.ceqea_zgrviewer, "-pluginMode=jp.ac.jaist.ceqea.F_run.CEq_zgrviewer", name } : new String[] { "java", CEq_system.zgrXmx, "-jar", CEq_system.zgrviewer, "-pluginList=" + CEq_system.ceqea_zgrviewer, "-pluginMode=jp.ac.jaist.ceqea.F_run.CEq_zgrviewer", name };
    }

    public static String[] ZGRVvanilla(String name) {
        return (CEq_system.zgrXmx == null) ? new String[] { "java", "-jar", CEq_system.zgrviewer, name } : new String[] { "java", CEq_system.zgrXmx, "-jar", CEq_system.zgrviewer, name };
    }

    public static String URLdecode(String s) {
        String sd = null;
        try {
            sd = URLDecoder.decode(s, "UTF8");
        } catch (Exception e) {
            System.err.println("Malformed URL");
        }
        return sd;
    }
}
