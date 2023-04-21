package com.patientis.client.deploy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import com.patientis.model.common.Converter;
import com.patientis.model.common.DateTimeModel;
import com.patientis.model.common.UserModel;
import com.patientis.framework.utility.FileSystemUtil;
import com.patientis.framework.utility.IHandleFile;
import com.patientis.framework.utility.Software;
import com.patientis.framework.utility.ZipUtil;

/**
 * TODO appserver should write out the start for init server first
start javaw -Djava.naming.factory.initial=org.jnp.interfaces.NamingContextFactory  -Djava.naming.factory.url.pkgs=org.jboss.naming:org.jnp.interfaces  -Djava.naming.provider.url=localhost:7107  -Xms64m  -Xmx512m  -Xverify:none  -cp patientis.jar;../../lib\derby.jar;../../lib\microba-0.4.4.jar;../../lib\binding-1.1.1.jar;../../lib\forms-1.0.7.jar;../../lib\validation-1.3.0.jar;../../lib\looks-2.0.1.jar;../../lib/jboss\log4j.jar;../../lib\substance.jar;../../lib\commons-codec-1.3.jar;../../lib/rules\commons-io-1.1.jar;../../lib\dom4j.jar;../../lib\emma.jar;../../lib\itext-2.0.4.jar;../../lib\jasperreports-1.3.1.jar;../../lib\js.jar;../../lib\junit-4.1.jar;../../lib\layer.jar;../../lib\mysql-connector-java-3.1.11-bin.jar;../../lib\oscache-2.3.2.jar;../../lib\pircbot.jar;../../lib\postgresql-8.1-407.jdbc3.jar;../../lib\quartz-all-1.6.0.jar;../../lib\resources.jar;../../lib\swing-layout-1.0.1.jar;../../lib\swingx.jar;../../lib/jboss\concurrent.jar;../../lib/jboss\hibernate3.jar;../../lib/jboss\hibernate-annotations.jar;../../lib/jboss\hibernate-entitymanager.jar;../../lib/jboss\jbossall-client.jar;../../lib/jboss\jboss-annotations-ejb3.jar;../../lib/jboss\jboss-ejb3x.jar;../../lib/jboss\jnpserver.jar;../../lib/rules\drools-ant-4.0.1.jar;../../lib/rules\drools-compiler-4.0.1.jar;../../lib/rules\drools-core-4.0.1.jar;../../lib/rules\drools-decisiontables-4.0.1.jar;../../lib/rules\drools-jsr94-4.0.1.jar;../../lib/rules\commons-jci-core-1.0.jar;../../lib/rules\commons-jci-eclipse-1.0.jar;../../lib/rules\commons-jci-janino-1.0.jar;../../lib/rules\antlr-runtime-3.0.jar;../../lib/rules\ant-nodeps-1.6.5.jar;../../lib/rules\janino-2.5.7.jar;../../lib/rules\mvel14-1.2rc4rv908.jar com.patientis.client.security.login.InitServer DEMO demo
run.bat -b 0.0.0.0 -Dhibernate.connection.driver_class=org.postgresql.Driver -Dhibernate.connection.url=jdbc:postgresql://dbserver:5432/testis -Dhibernate.dialect=org.hibernate.dialect.PostgreSQLDialect -Dhibernate.connection.username=testis -Dhibernate.connection.password=testis
 *
 * 
 * <br/>  
 */
@SuppressWarnings("unused")
public class Deploy {

    public static final String sep = FileSystemUtil.getFilePathSeparator();

    private static final String root = "C:\\";

    private static final String trunkDir = root + "dev/patientis/trunk/";

    private static final String sourceDeployment = trunkDir + "deploy/";

    private static final String sourceDeployConfigure = trunkDir + "deploy/configure/";

    private static final String sourceJarList = trunkDir + "/build/libraries/jar_files.txt";

    private static final String sourceClient = trunkDir + "/build/libraries/client.txt";

    private static final String sourceClientDLLS = trunkDir + "/deploy/license";

    private static final String targetRootDir = root + "dev/local/deploy/" + DateTimeModel.getNow().toString("yyyyMMdd") + "/";

    private static final String targetRootClientDir = targetRootDir + "/client/";

    private static final String targetRootServerDir = targetRootDir + "/server/";

    private static final String targetRootServerUpgradeDir = targetRootDir + "/serverupgrade/";

    private static final String targetServerDir = targetRootServerDir + "/appserver/";

    private static final String targetServerUpgradeDir = targetRootServerUpgradeDir + "/appserver/";

    private static final String serverSetupDir = targetRootServerDir + "/configure/";

    private static final String targetClientBinDir = targetRootClientDir + "/bin/";

    private static final String targetClientBinWindows = targetClientBinDir + "patientos.bat";

    private static final String targetClientBinUNIX = targetClientBinDir + "patientos.sh";

    private static final String jarSignFile = targetRootClientDir + "/signjars.bat";

    private static final String javaDir = trunkDir + "tools/";

    private static final String javaVersion = "jre1.6.0_02";

    private static final String sourceDatabase = sourceDeployment + "/database/";

    private static final String splashFile = root + "dev/patientis/trunk/deploy/images/splash.png";

    private static final String sourceWebsite = root + "dev/patientis/output/website/build/site/";

    private static final String targetWebsite = targetRootDir + "/website/";

    private static final String sourceJavadoc = root + "dev/patientis/trunk/src/doc";

    private static final String targetJavadoc = root + "dev/patientis/output/website/build/site/software/javadoc";

    private static final String sourceWebsiteDocRoot = root + "dev/patientis/trunk/website/src/documentation/content/xdocs";

    private static final String targetWebsiteDocRoot = root + "dev/patientis/output/website/build/site";

    private static final String sourceDemoDb = trunkDir + "/deploy/upgrade/hsqldb";

    private static final String targetDemoDb = targetRootServerDir + "/appserver/bin";

    private static final String mirthCleanDir = root + "dev/patientis/trunk/tools/mirth_1_6_1";

    private static final String targetInterfacesDir = targetRootServerDir + "/interfaces/";

    private static final String sourceDeployChannels = trunkDir + "deploy/channels/";

    private static final String srcPostgreSQL = sourceDeployment + "postgresql";

    private static final String targetPostgreSQL = targetRootDir + "/dbserver/";

    private static final String targetRootUpgradeDir = targetRootDir + "/upgrade/";

    private static final String sourceRootUpgradeDir = trunkDir + "/deploy/upgrade";

    private static final String sourceWrapperDir = trunkDir + "/deploy/wrapper";

    /**
	 * Clean untouched version of jboss
	 */
    private static final String jbossCleanDir = root + "dev/patientis/trunk/tools/jboss-4.2.0.GA/";

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            Deploy deploy = new Deploy();
            List<JarFile> clientJarFiles = deploy.createClient();
            deploy.copyJava(targetRootDir);
            List<JarFile> serverJarFiles = deploy.createAppServer();
            createInterfaces();
            createUpgrade(clientJarFiles, serverJarFiles);
            cleanupSubversion();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void cleanupSubversion() throws Exception {
        File rootDir = new File(targetRootDir);
        FileSystemUtil.navigateFilesystem(rootDir, new IHandleFile() {

            @Override
            public boolean isValidDir(File fileDir) throws Exception {
                return true;
            }

            @Override
            public void handleFile(File file) throws Exception {
                if (file.isDirectory()) {
                    for (File f : file.listFiles()) {
                        if (f.getName().endsWith(".svn")) {
                            FileUtils.deleteDirectory(f);
                        }
                    }
                }
            }
        });
    }

    public static void createUpgradeDataOnly() throws Exception {
        Deploy deploy = new Deploy();
        deploy.createClient();
        deploy.createAppServer();
        createUpgrade(new ArrayList<JarFile>(), new ArrayList<JarFile>());
        FileUtils.deleteDirectory(new File(targetRootClientDir));
        FileUtils.deleteDirectory(new File(targetRootServerDir));
    }

    /**
	 * Deploy files
	 */
    private List<JarFile> createClient() throws Exception {
        FileSystemUtil.createDirectory(targetRootClientDir);
        FileSystemUtil.createDirectory(targetClientBinDir);
        FileUtils.copyFile(new File(sourceDeployConfigure + "/checkinstall.bat"), new File(targetClientBinDir + "/checkinstall.bat"));
        FileUtils.copyFile(new File(sourceDeployConfigure + "/checkinstall.sh"), new File(targetClientBinDir + "/checkinstall.bat"));
        List<JarFile> clientJars = JarFile.createTargetJars(sourceJarList, trunkDir, targetRootClientDir, true, false);
        Client client = Client.load(sourceClient);
        client.writeRunBatchFile(targetClientBinWindows, clientJars, ";", "../", true, javaVersion, true);
        client.writeRunBatchFile(targetClientBinUNIX, clientJars, ":", "../", true, javaVersion, false);
        client.writeJarsigner(jarSignFile, clientJars);
        FileUtils.copyFile(new File(splashFile), new File(targetClientBinDir + "splash.gif"));
        for (File file : FileSystemUtil.getFiles(sourceClientDLLS)) {
            if (!file.isDirectory()) {
                FileUtils.copyFile(file, new File(targetClientBinDir + "/" + file.getName()));
            }
        }
        StringBuffer jnlp = new StringBuffer();
        for (JarFile jar : clientJars) {
            jnlp.append("    <jar href=\"client/lib/");
            jnlp.append(jar.getLibraryFile());
            jnlp.append("\"/>\r\n");
        }
        FileSystemUtil.createDirectory(targetServerDir);
        FileSystemUtil.createFile(targetServerDir + "jnlp-resources.txt", jnlp.toString());
        return clientJars;
    }

    /**
	 * Create application server
	 * 
	 * @throws Exception
	 */
    private List<JarFile> createAppServer() throws Exception {
        FileSystemUtil.createDirectory(targetServerDir);
        copyServerDir("bin");
        for (int i = 1; i < 10; i++) {
            File toDir = new File(targetServerDir + sep + "bin/../../data/" + i);
            File fromDir = new File("C:\\dev/local/devpos/data/" + i);
            if (fromDir.exists()) {
                FileUtils.copyDirectory(fromDir, toDir);
                ;
            } else if (i == 1) {
                System.err.println("not found " + fromDir);
                System.exit(1);
            }
        }
        copyServerDir("lib");
        copyServerDir("server/default");
        FileSystemUtil.createDirectory(targetServerDir + "/server/default/log");
        List<JarFile> jarFiles = JarFile.createTargetJars(sourceJarList, trunkDir, targetServerDir, false, true);
        JBoss.updatePorts(targetServerDir, 7106);
        JBoss.doNotUseHostName(targetServerDir);
        JBoss.updateJbossStandard(targetServerDir, serverSetupDir, sourceDeployment);
        writeAppServer(targetServerDir + "bin/appserver.bat", true, "org.postgresql.Driver", "org.hibernate.dialect.PostgreSQLDialect", "jdbc:postgresql://localhost:5432/demopos", "demopos", "demopos");
        writeAppServer(targetServerDir + "bin/appserver.sh", false, "org.postgresql.Driver", "org.hibernate.dialect.PostgreSQLDialect", "jdbc:postgresql://localhost:5432/demopos", "demopos", "demopos");
        FileUtils.copyFile(new File(sourceDeployConfigure + "/pos.server.properties"), new File(targetServerDir + "bin/pos.server.properties"));
        FileUtils.copyFile(new File(sourceDeployConfigure + "/pos.keystore"), new File(targetServerDir + "bin/pos.keystore"));
        return jarFiles;
    }

    /**
	 * Copy if new
	 * 
	 * @param fromDir
	 * @param toDir
	 * @throws Exception
	 */
    private void copyServerDir(String subdir) throws Exception {
        File fromDir = new File(jbossCleanDir + sep + subdir + "/");
        File toDir = new File(targetServerDir + sep + subdir + "/");
        FileUtils.copyDirectory(fromDir, toDir);
        for (String subd : new String[] { "bin", "conf", "lib" }) {
            File wrapperBinDirFile = new File(sourceWrapperDir + sep + subd);
            for (File file : FileSystemUtil.getFiles(wrapperBinDirFile.getAbsolutePath())) {
                if (!file.isDirectory()) {
                    FileUtils.copyFile(file, new File(targetServerDir + sep + subd + sep + file.getName()));
                }
            }
        }
    }

    private void copyJava(String rootDir) throws Exception {
        File fromDir = new File(javaDir + javaVersion + "/");
        File toDir = new File(rootDir + javaVersion + "/");
        FileUtils.copyDirectory(fromDir, toDir);
    }

    private void writeAppServer(String appserverFilename, boolean windows, String driverClass, String dialect, String url, String username, String password) throws Exception {
        StringBuffer sb = new StringBuffer(256);
        if (windows) {
            sb.append("set JAVA_HOME=..\\..\\..\\" + javaVersion + "\r\n");
            sb.append("set PATH=../../../" + javaVersion + "/bin;%PATH%" + "\r\n");
            sb.append("run.bat -b 0.0.0.0 -Dremoting.bind_by_host=false\r\n");
        } else {
            sb.append("JAVA_HOME=../../../" + javaVersion + "\n");
            sb.append("export JAVA_HOME" + "\n");
            sb.append("PATH=../../../" + javaVersion + "/bin:$PATH" + "\n");
            sb.append("export PATH" + "\n");
            sb.append("./run.sh -b 0.0.0.0 -Dremoting.bind_by_host=false\n");
        }
        FileSystemUtil.createFile(appserverFilename, sb.toString());
    }

    private void copyDemoDb() throws Exception {
        FileUtils.copyFile(new File(sourceDemoDb + "/demo.properties"), new File(targetDemoDb + "/demo.properties"));
        FileUtils.copyFile(new File(sourceDemoDb + "/demo.script"), new File(targetDemoDb + "/demo.script"));
    }

    /**
	 * Create application server
	 * 
	 * @throws Exception
	 */
    private static void createInterfaces() throws Exception {
        FileSystemUtil.createDirectory(targetInterfacesDir);
        File fromDir = new File(mirthCleanDir);
        File toDir = new File(targetInterfacesDir + "/bin/");
        FileUtils.copyDirectory(fromDir, toDir);
        FileUtils.copyDirectory(new File(sourceDeployChannels), new File(targetInterfacesDir + "channels/"));
        FileSystemUtil.createDirectory(targetInterfacesDir + "activity/");
        FileSystemUtil.createDirectory(targetInterfacesDir + "activity/adt_in/");
        FileSystemUtil.createDirectory(targetInterfacesDir + "activity/adt_out/");
        FileSystemUtil.createDirectory(targetInterfacesDir + "activity/lab_results_in/");
        FileSystemUtil.createDirectory(targetInterfacesDir + "activity/lab_orders_out/");
        FileSystemUtil.createDirectory(targetInterfacesDir + "activity/oru_xml_in/");
        FileUtils.copyFile(new File(sourceDeployment + "/channels/interfaces.bat"), new File(targetInterfacesDir + "/bin/interfaces.bat"));
        FileUtils.copyFile(new File(sourceDeployment + "/channels/interfaces.sh"), new File(targetInterfacesDir + "/bin/interfaces.sh"));
    }

    /**
	 * Upgrade
	 * @throws Exception
	 */
    public static void createUpgrade(List<JarFile> clientJarFiles, List<JarFile> serverJarFiles) throws Exception {
        FileSystemUtil.createDirectory(targetRootUpgradeDir);
        copyUpgradeDir("/01schema/");
        FileUtils.copyFile(new File(trunkDir + "/build/deploypos_schema.sql"), new File(targetRootUpgradeDir + "/01schema/schema_only.sql"));
        copyUpgradeDir("/02data/");
        File fromLibDir = new File(targetRootClientDir + "/lib");
        File toLibDir = new File(targetRootUpgradeDir + "/02data/client/lib");
        FileSystemUtil.createDirectory(targetRootUpgradeDir + "/02data/client");
        FileUtils.copyDirectory(fromLibDir, toLibDir);
        copyUpgradeDir("/03server/");
        FileUtils.copyFile(new File(targetServerDir + "/server/default/deploy/patientis.ejb3"), new File(targetRootUpgradeDir + "/03server/patientis.ejb3"));
        FileUtils.copyFile(new File(targetServerDir + "/server/default/deploy/patientos.war"), new File(targetRootUpgradeDir + "/03server/patientos.war"));
        for (JarFile jarfile : serverJarFiles) {
            if (Converter.isTrimmedSameIgnoreCase(jarfile.getVersion(), Software.getVersion())) {
                FileUtils.copyFile(jarfile.getSourceFile(), new File(targetRootUpgradeDir + "/03server/" + jarfile.getSourceFile().getName()));
            }
        }
        copyUpgradeDir("/04client/");
        FileUtils.copyFile(new File(targetRootClientDir + "/lib/patientis.jar"), new File(targetRootUpgradeDir + "/04client/patientis.jar"));
        FileUtils.copyFile(new File(targetRootClientDir + "/lib/resources.jar"), new File(targetRootUpgradeDir + "/04client/resources.jar"));
        for (JarFile jarfile : clientJarFiles) {
            if (Converter.isTrimmedSameIgnoreCase(jarfile.getVersion(), Software.getVersion())) {
                FileUtils.copyFile(jarfile.getSourceFile(), new File(targetRootUpgradeDir + "/04client/" + jarfile.getSourceFile().getName()));
            }
        }
    }

    /**
	 * Upgrade dir
	 * 
	 * @param subdir
	 * @throws Exception
	 */
    private static void copyUpgradeDir(String subdir) throws Exception {
        File fromDir = new File(sourceRootUpgradeDir + subdir);
        File toDir = new File(targetRootUpgradeDir + subdir);
        FileUtils.copyDirectory(fromDir, toDir);
    }
}
