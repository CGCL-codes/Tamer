package com.memoire.vainstall;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.CRC32;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @version $Id: VAArchiver.java,v 1.44 2005/10/11 09:51:55 deniger Exp $
 * @author Axel von Arnim
 */
public class VAArchiver {

    private static final String JAVA_HOME = System.getProperty("java.home");

    private static final String[] JAVAC_SEARCH_PATH = new String[] { "../bin", "bin" };

    private static final String JAVAC = "javac";

    private static final String JDK_HOME = System.getProperty("java.home") + File.separator + "..";

    private static String RC_DIR = null;

    private static String VAILOGO = null;

    private static final String[] JAR_FILES_COMMON = new String[] { "com/memoire/vainstall/Language_da_DK.class", "com/memoire/vainstall/Language_de_DE.class", "com/memoire/vainstall/Language_en_UK.class", "com/memoire/vainstall/Language_fr_FR.class", "com/memoire/vainstall/Language_it_IT.class", "com/memoire/vainstall/Language_ja_JP.class", "com/memoire/vainstall/AbstractInstall.class", "com/memoire/vainstall/Setup.class", "com/memoire/vainstall/VAClassLoader.class", "com/memoire/vainstall/SetupFileFilter.class", "com/memoire/vainstall/UpgradeInfo.class", "com/memoire/vainstall/LogInfo.class", "com/memoire/vainstall/Uninstall.class", "com/memoire/vainstall/VAGlobals.class", "com/memoire/vainstall/VAStats.class", "com/memoire/vainstall/VAStep.class", "com/memoire/vainstall/VAStepFactory.class", "com/memoire/vainstall/VAWelcomeStep.class", "com/memoire/vainstall/VAReadmeStep.class", "com/memoire/vainstall/VALanguageStep.class", "com/memoire/vainstall/VALicenseStep.class", "com/memoire/vainstall/VALicenseKeyStep.class", "com/memoire/vainstall/VADirectoryStep.class", "com/memoire/vainstall/VAInstallStep.class", "com/memoire/vainstall/VAUpgradeStep.class", "com/memoire/vainstall/VAShortcutStep.class", "com/memoire/vainstall/VAEndStep.class", "com/memoire/vainstall/VAWizardInterface.class", "com/memoire/vainstall/VALinkDebian.class", "com/memoire/vainstall/VALinkLinux.class", "com/memoire/vainstall/VAShortcutEntry.class", "com/memoire/vainstall/VAConstant.class", "com/memoire/vainstall/VALinkGnome.class", "com/memoire/vainstall/VALinkKDE.class", "com/memoire/vainstall/VALinkWindows.class", "com/memoire/vainstall/VAFile.class", "com/memoire/vainstall/LicenseKeySupport.class", "com/memoire/vainstall/LicenseKeySupport$FieldInfo.class", "com/memoire/vainstall/DefaultLicenseKeySupport.class", "com/memoire/vainstall/TestLicenseKeySupport.class" };

    private static final String[] JAR_FILES_JNISHORTCUT = new String[] { "JNIWinShortcut.dll", "com/memoire/vainstall/JNIWindowsShortcut.class" };

    private static final String[] JAR_FILES_JNIREGISTRY = new String[] { "ICE_JNIRegistry.dll", "com/ice/jni/registry/NoSuchKeyException.class", "com/ice/jni/registry/NoSuchValueException.class", "com/ice/jni/registry/RegBinaryValue.class", "com/ice/jni/registry/RegDWordValue.class", "com/ice/jni/registry/RegMultiStringValue.class", "com/ice/jni/registry/RegStringValue.class", "com/ice/jni/registry/Registry.class", "com/ice/jni/registry/RegistryException.class", "com/ice/jni/registry/RegistryKey.class", "com/ice/jni/registry/RegistryValue.class", "com/ice/text/HexNumberFormat.class", "com/ice/util/AWTUtilities.class", "com/ice/util/ClassUtilities.class", "com/ice/util/FileLog.class", "com/ice/util/HTTPUtilities.class", "com/ice/util/HexDump.class", "com/ice/util/StringUtilities.class", "com/ice/util/URLUtilities.class", "com/ice/util/UserProperties.class" };

    private static final String[] JAR_FILES_TEXT_UI = new String[] { "com/memoire/vainstall/tui/Language_da_DK.class", "com/memoire/vainstall/tui/Language_de_DE.class", "com/memoire/vainstall/tui/Language_en_UK.class", "com/memoire/vainstall/tui/Language_fr_FR.class", "com/memoire/vainstall/tui/Language_it_IT.class", "com/memoire/vainstall/tui/Language_ja_JP.class", "com/memoire/vainstall/tui/NullOutputStream.class", "com/memoire/vainstall/tui/TuiDefaultStep.class", "com/memoire/vainstall/tui/TuiDirectoryStep.class", "com/memoire/vainstall/tui/TuiInstallStep.class", "com/memoire/vainstall/tui/TuiLanguageStep.class", "com/memoire/vainstall/tui/TuiLicenseStep.class", "com/memoire/vainstall/tui/TuiReadmeStep.class", "com/memoire/vainstall/tui/TuiShortcutStep.class", "com/memoire/vainstall/tui/TuiWelcomeStep.class", "com/memoire/vainstall/tui/TuiWizard.class", "com/memoire/vainstall/tui/VATextUI.class", "com/memoire/vainstall/tui/TuiUpgradeStep.class", "com/memoire/vainstall/tui/TuiEndStep.class" };

    private static final String[] JAR_FILES_ANSI_UI = new String[] { "com/memoire/vainstall/aui/VAAnsiUI.class" };

    private static final String[] JAR_FILES_UNATTENDED_UI = new String[] { "com/memoire/vainstall/uui/VAUnattendedUI.class", "com/memoire/vainstall/uui/UuiDefaultStep.class", "com/memoire/vainstall/uui/UuiDirectoryStep.class", "com/memoire/vainstall/uui/UuiEndStep.class", "com/memoire/vainstall/uui/UuiInstallStep.class", "com/memoire/vainstall/uui/UuiLanguageStep.class", "com/memoire/vainstall/uui/UuiLicenseStep.class", "com/memoire/vainstall/uui/UuiReadmeStep.class", "com/memoire/vainstall/uui/UuiShortcutStep.class", "com/memoire/vainstall/uui/UuiUpgradeStep.class", "com/memoire/vainstall/uui/UuiWelcomeStep.class", "com/memoire/vainstall/uui/UuiWizard.class" };

    private static final String[] JAR_FILES_GRAPHIC_UI = new String[] { "com/memoire/vainstall/gui/Language_da_DK.class", "com/memoire/vainstall/gui/Language_de_DE.class", "com/memoire/vainstall/gui/Language_en_UK.class", "com/memoire/vainstall/gui/Language_fr_FR.class", "com/memoire/vainstall/gui/Language_it_IT.class", "com/memoire/vainstall/gui/Language_ja_JP.class", "com/memoire/vainstall/gui/VABlueScreen.class", "com/memoire/vainstall/gui/VABlueScreen$1.class", "com/memoire/vainstall/gui/VAGraphicUI.class", "com/memoire/vainstall/gui/VAWizard.class", "com/memoire/vainstall/gui/VAWizard$1.class", "com/memoire/vainstall/gui/VAPanel.class", "com/memoire/vainstall/gui/VAWelcomePanel.class", "com/memoire/vainstall/gui/VAImagePanel.class", "com/memoire/vainstall/gui/VAInstallPanel.class", "com/memoire/vainstall/gui/VALanguagePanel.class", "com/memoire/vainstall/gui/VALicensePanel.class", "com/memoire/vainstall/gui/VALicenseKeyPanel.class", "com/memoire/vainstall/gui/VALicenseKeyPanel$1.class", "com/memoire/vainstall/gui/VAReadmePanel.class", "com/memoire/vainstall/gui/VADirectoryPanel.class", "com/memoire/vainstall/gui/VADirectoryPanel$1.class", "com/memoire/vainstall/gui/VAUpgradePanel.class", "com/memoire/vainstall/gui/VAShortcutPanel.class", "com/memoire/vainstall/gui/VAEndPanel.class" };

    private static final String[] JAR_FILES_XTRA_UI = new String[] { "com/memoire/vainstall/xui/Language_da_DK.class", "com/memoire/vainstall/xui/Language_de_DE.class", "com/memoire/vainstall/xui/Language_en_UK.class", "com/memoire/vainstall/xui/Language_fr_FR.class", "com/memoire/vainstall/xui/Language_it_IT.class", "com/memoire/vainstall/xui/Language_ja_JP.class", "com/memoire/vainstall/xui/XuiBlueScreen.class", "com/memoire/vainstall/xui/XuiWizard.class", "com/memoire/vainstall/xui/XuiAbstractPanel.class", "com/memoire/vainstall/xui/XuiPanel.class", "com/memoire/vainstall/xui/XuiImagePanel.class", "com/memoire/vainstall/xui/XuiTitle.class", "com/memoire/vainstall/xui/XuiButton.class", "com/memoire/vainstall/xui/XuiButtonBorder.class", "com/memoire/vainstall/xui/XuiLabel.class", "com/memoire/vainstall/xui/XuiList.class", "com/memoire/vainstall/xui/XuiRadioButton.class", "com/memoire/vainstall/xui/XuiOptionPane.class", "com/memoire/vainstall/xui/XuiWelcomePanel.class", "com/memoire/vainstall/xui/XuiReadmePanel.class", "com/memoire/vainstall/xui/XuiLicensePanel.class", "com/memoire/vainstall/xui/XuiLicenseKeyPanel.class", "com/memoire/vainstall/xui/XuiLicenseKeyPanel$1.class", "com/memoire/vainstall/xui/XuiInstallPanel.class", "com/memoire/vainstall/xui/XuiShortcutPanel.class", "com/memoire/vainstall/xui/XuiUpgradePanel.class", "com/memoire/vainstall/xui/XuiLanguagePanel.class", "com/memoire/vainstall/xui/XuiEndPanel.class", "com/memoire/vainstall/xui/XuiDirectoryPanel$1.class", "com/memoire/vainstall/xui/XuiDirectoryPanel.class", "com/memoire/vainstall/xui/VAXtraUI.class" };

    private static final String[] JAR_FILES_CUSTOM_PRE_POST = new String[] { "com/memoire/vainstall/AbstractCustomPrePost.class", "com/memoire/vainstall/InputStreamToDetails.class" };

    private File filelist_;

    private String destPath_;

    private String archMethod_;

    private long archOffset_;

    private long installClassOffset_;

    private long installClassSize_;

    private long jarSize_;

    private String licenseKeySupportClassName_, encodeKey_;

    private LicenseKeySupport licenseKeySupport_;

    private String uiMode_;

    private String uiBluescreen_;

    private String uiBluescreenColor_;

    private String image_;

    private String appName_, appVersion_;

    private String linkSectionName_, linkSectionIcon_, linkEntryName_, linkEntryIcon_;

    private String instClassName_;

    private boolean createUninstallShortcut_;

    private String[] targets_;

    private String currentTarget_;

    private String jarAlias_, jarPassphrase_, jarCodebase_, jarVendor_, jarHomepage_;

    private File license_;

    private File readme_;

    private ByteArrayOutputStream archiveInfos_;

    private int archivecount_;

    private Vector archiveEntryList_;

    private String additionalFiles_;

    private Properties installProperties = new Properties();

    private String customPrePostClassName_;

    private String customPrePostJarfileName_;

    private static String getJavaBinPath() {
        String path = null;
        for (int i = 0; i < JAVAC_SEARCH_PATH.length && path == null; i++) {
            String aPath;
            aPath = JAVA_HOME + File.separator + JAVAC_SEARCH_PATH[i];
            if (new File(aPath).exists()) {
                path = aPath;
            }
        }
        return path;
    }

    public VAArchiver() {
        filelist_ = null;
        destPath_ = null;
        archMethod_ = null;
        installClassOffset_ = -10987654321L;
        installClassSize_ = -1234543210L;
        archOffset_ = -1234567890L;
        uiMode_ = null;
        uiBluescreenColor_ = null;
        uiBluescreen_ = null;
        appName_ = appVersion_ = null;
        linkSectionName_ = linkSectionIcon_ = linkEntryName_ = linkEntryIcon_ = null;
        instClassName_ = null;
        targets_ = new String[0];
        jarAlias_ = jarPassphrase_ = jarCodebase_ = jarHomepage_ = jarVendor_ = null;
        license_ = null;
        readme_ = null;
        archiveInfos_ = null;
        archivecount_ = 0;
        archiveEntryList_ = new Vector();
        licenseKeySupportClassName_ = "com.memoire.vainstall.DefaultLicenseKeySupport";
    }

    public void start() {
        if (!processProperties()) return;
        try {
            System.out.println();
            System.out.println(VAGlobals.i18n("VAArchiver_CompressingFiles"));
            archiveInfos_ = new ByteArrayOutputStream();
            File zip = makeArchive("archive.zip");
            zip.deleteOnExit();
            System.out.println();
            System.out.println(VAGlobals.i18n("VAArchiver_CreatingJarFiles"));
            archiveInfos_.flush();
            byte[] infosbytes = archiveInfos_.toByteArray();
            File jar = makeJar("install.jar", zip, license_, readme_, infosbytes);
            jarSize_ = jar.length();
            archiveInfos_.close();
            jar.deleteOnExit();
            File installJavaFile = new File(instClassName_ + ".java");
            installJavaFile.deleteOnExit();
            File instClass = null;
            boolean deleteInstallClass = true;
            for (int i = 0; i < targets_.length; i++) {
                System.out.println();
                System.out.println(targets_[i] + VAGlobals.i18n("VAArchiver_Target"));
                currentTarget_ = targets_[i];
                System.out.println(VAGlobals.i18n("VAArchiver_GeneratingInstallClass"));
                generateInstallCode(installJavaFile, "com/memoire/vainstall/resources/Install.vaitpl", "com/memoire/vainstall/VAClassLoader.class");
                System.out.println(VAGlobals.i18n("VAArchiver_CompilingInstallClass"));
                instClass = compile(installJavaFile.getName());
                installClassSize_ = instClass.length();
                VAGlobals.printDebug("  InstallClass size=" + installClassSize_);
                if ("jar".equals(targets_[i])) {
                    File jarTarget = new File(instClassName_ + ".jar");
                    File mfFile = new File(instClassName_ + ".mf");
                    mfFile.deleteOnExit();
                    System.out.println(VAGlobals.i18n("VAArchiver_CreatingManifestFile"));
                    generateManifestFile(mfFile);
                    System.out.println(VAGlobals.i18n("VAArchiver_UpdatingJarFile"));
                    copy(jar, jarTarget);
                    jar("uvfm", jarTarget, new File[] { mfFile, instClass });
                    if (jarAlias_ != null && !"".equals(jarAlias_.trim()) && jarPassphrase_ != null && !"".equals(jarPassphrase_.trim())) {
                        System.out.println(VAGlobals.i18n("VAArchiver_SigningJarFile"));
                        jarsign(jarPassphrase_, jarTarget, jarAlias_);
                    }
                } else if ("jnlp".equals(targets_[i])) {
                    File jnlpFile = new File(instClassName_ + ".jnlp");
                    System.out.println(VAGlobals.i18n("VAArchiver_CreatingJnlpFile"));
                    generateJnlpFile(jnlpFile);
                } else if ("java".equals(targets_[i])) {
                    installClassOffset_ = -10987654321L;
                    deleteInstallClass = false;
                    if (archMethod_.equals("append")) {
                        archOffset_ = instClass.length();
                        VAGlobals.printDebug(VAGlobals.i18n("VAArchiver_ArchiveOffset") + archOffset_);
                        generateInstallCode(installJavaFile, "com/memoire/vainstall/resources/Install.vaitpl", "com/memoire/vainstall/VAClassLoader.class");
                        System.out.println(VAGlobals.i18n("VAArchiver_CompilingInstallClass"));
                        compile(installJavaFile.getName());
                        System.out.println(VAGlobals.i18n("VAArchiver_AppendingArchive"));
                        appendArchive(instClass);
                    }
                } else if ("unix".equals(targets_[i])) {
                    File unixShellFile = new File(instClassName_ + ".sh");
                    installClassOffset_ = generateUnixInstallShell(unixShellFile, "com/memoire/vainstall/resources/Install-sh.vaitpl", instClass);
                    VAGlobals.printDebug(VAGlobals.i18n("VAArchiver_InstallClassOffset") + installClassOffset_);
                    if (archMethod_.equals("append")) {
                        archOffset_ = unixShellFile.length();
                        VAGlobals.printDebug(VAGlobals.i18n("VAArchiver_ArchiveOffset") + archOffset_);
                        generateInstallCode(installJavaFile, "com/memoire/vainstall/resources/Install.vaitpl", "com/memoire/vainstall/VAClassLoader.class");
                        System.out.println(VAGlobals.i18n("VAArchiver_CompilingInstallClass"));
                        compile(installJavaFile.getName());
                        generateUnixInstallShell(unixShellFile, "com/memoire/vainstall/resources/Install-sh.vaitpl", instClass);
                        System.out.println(VAGlobals.i18n("VAArchiver_AppendingArchive"));
                        appendArchive(unixShellFile);
                    }
                } else if (("win95".equals(targets_[i])) || ("linux-i386".equals(targets_[i]))) {
                    File nativeExeFile = null;
                    if ("win95".equals(targets_[i])) nativeExeFile = new File(instClassName_ + ".exe"); else if ("linux-i386".equals(targets_[i])) nativeExeFile = new File(instClassName_ + ".lin");
                    installClassOffset_ = generateNativeInstallExe(nativeExeFile, "com/memoire/vainstall/resources/Install-" + targets_[i] + "-exe.vaitpl", instClass);
                    VAGlobals.printDebug(VAGlobals.i18n("VAArchiver_InstallClassOffset") + installClassOffset_);
                    if (archMethod_.equals("append")) {
                        archOffset_ = nativeExeFile.length();
                        VAGlobals.printDebug(VAGlobals.i18n("VAArchiver_ArchiveOffset") + archOffset_);
                        generateInstallCode(installJavaFile, "com/memoire/vainstall/resources/Install.vaitpl", "com/memoire/vainstall/VAClassLoader.class");
                        System.out.println(VAGlobals.i18n("VAArchiver_CompilingInstallClass"));
                        compile(installJavaFile.getName());
                        generateNativeInstallExe(nativeExeFile, "com/memoire/vainstall/resources/Install-" + targets_[i] + "-exe.vaitpl", instClass);
                        System.out.println(VAGlobals.i18n("VAArchiver_AppendingArchive"));
                        appendArchive(nativeExeFile);
                        System.out.println("win95 end");
                    }
                }
            }
            if (instClass != null && deleteInstallClass) instClass.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    File lnfFile_;

    private boolean processProperties() {
        String tempString;
        RC_DIR = "com/memoire/vainstall/resources";
        VAILOGO = RC_DIR + "/vailogo.gif";
        VAGlobals.setLanguage("default");
        String tmp = VAProperties.PROPERTIES.getProperty("vainstall.destination.language");
        if (tmp != null && !"".equals(tmp)) {
            installProperties.put("vainstall.destination.language", tmp);
        }
        String useFullPath = VAProperties.PROPERTIES.getProperty("vainstall.script.java.fullpath");
        if (useFullPath != null && "true".equalsIgnoreCase(useFullPath)) {
            installProperties.put("vainstall.script.java.fullpath", "true");
        }
        String minJavaVersion = VAProperties.PROPERTIES.getProperty("vainstall.java.version.min", null);
        String maxJavaVersion = VAProperties.PROPERTIES.getProperty("vainstall.java.version.max", null);
        String javaVendor = VAProperties.PROPERTIES.getProperty("vainstall.java.vendor", null);
        if (minJavaVersion != null && minJavaVersion.trim().length() > 0) {
            installProperties.put("vainstall.java.version.min", minJavaVersion);
        }
        if (maxJavaVersion != null && maxJavaVersion.trim().length() > 0) {
            installProperties.put("vainstall.java.version.max", maxJavaVersion);
        }
        if (javaVendor != null && javaVendor.trim().length() > 0) {
            installProperties.put("vainstall.java.vendor", javaVendor);
        }
        String key = "vainstall.java.download.url";
        String val = VAProperties.PROPERTIES.getProperty(key, null);
        if (val != null) installProperties.put(key, val);
        String shortcutsInInstallDir = VAProperties.PROPERTIES.getProperty("vainstall.shortcut.in.installdir");
        if (shortcutsInInstallDir != null && "true".equalsIgnoreCase(shortcutsInInstallDir)) {
            installProperties.put("vainstall.shortcut.in.installdir", "true");
        }
        tmp = VAProperties.PROPERTIES.getProperty("vainstall.archive.filelist");
        if (tmp == null || "".equals(tmp)) {
            System.err.println("vainstall.archive.filelist null");
            return false;
        }
        filelist_ = new File(tmp);
        if ((!filelist_.exists()) || (!filelist_.canRead())) {
            System.err.println(VAGlobals.i18n("VAArchiver_CanNotRead") + filelist_);
            return false;
        }
        destPath_ = VAProperties.PROPERTIES.getProperty("vainstall.destination.defaultPath");
        if (destPath_ == null || "".equals(destPath_)) {
            System.err.println("vainstall.destination.defaultPath null");
            return false;
        }
        if (!checkVaiPath(destPath_)) {
            System.err.println("vainstall.destination.defaultPath" + VAGlobals.i18n("VAArchiver_HasIncorrectFormat"));
            return false;
        }
        tmp = VAProperties.PROPERTIES.getProperty("vainstall.destination.installMode");
        if (("update".equals(tmp))) {
            destPath_ = "[UPDATE]";
        } else if (("module".equals(tmp))) {
            destPath_ = "[MODULE]";
        } else if (!"install".equals(tmp)) {
            System.err.println(VAGlobals.i18n("VAArchiver_NoValidInstallmode") + ":" + VAGlobals.i18n("VAArchiver_DefaultingTo") + "install");
        }
        tmp = VAProperties.PROPERTIES.getProperty("vainstall.destination.targets");
        if (tmp == null || "".equals(tmp)) {
            System.err.println(VAGlobals.i18n("VAArchiver_NoTargetSpecified") + " (vainstall.destination.targets)");
            return false;
        }
        StringTokenizer tok = new StringTokenizer(tmp, ",");
        Vector v = new Vector();
        while (tok.hasMoreTokens()) {
            String t = tok.nextToken().trim().toLowerCase();
            if ((!"java".equals(t)) && (!"jar".equals(t)) && (!"jnlp".equals(t)) && (!"unix".equals(t)) && (!"win95".equals(t)) && (!"linux-i386".equals(t))) {
                System.err.println(VAGlobals.i18n("VAArchiver_UnknownTarget") + t);
                return false;
            }
            if (!v.contains(t)) v.add(t);
        }
        if (v.contains("jnlp") && !v.contains("jar")) v.add("jar");
        targets_ = new String[v.size()];
        int i = 0;
        if (v.contains("jar")) targets_[i++] = "jar";
        if (v.contains("jnlp")) targets_[i++] = "jnlp";
        if (v.contains("unix")) targets_[i++] = "unix";
        if (v.contains("linux-i386")) targets_[i++] = "linux-i386";
        if (v.contains("win95")) targets_[i++] = "win95";
        if (v.contains("java")) targets_[i++] = "java";
        if (v.contains("jar")) {
            jarAlias_ = VAProperties.PROPERTIES.getProperty("vainstall.jarsigner.alias");
            jarPassphrase_ = VAProperties.PROPERTIES.getProperty("vainstall.jarsigner.passphrase");
        }
        if (v.contains("jnlp")) {
            jarCodebase_ = VAProperties.PROPERTIES.getProperty("vainstall.jnlp.codebase");
            if (jarCodebase_ == null || "".equals(jarCodebase_)) {
                System.err.println("vainstall.jnlp.codebase null");
                return false;
            }
            jarHomepage_ = VAProperties.PROPERTIES.getProperty("vainstall.jnlp.homepage");
            if (jarHomepage_ == null || "".equals(jarHomepage_)) {
                System.err.println("vainstall.jnlp.homepage null");
                return false;
            }
            jarVendor_ = VAProperties.PROPERTIES.getProperty("vainstall.jnlp.vendor");
            if (jarVendor_ == null || "".equals(jarVendor_)) {
                System.err.println("vainstall.jnlp.vendor null");
                return false;
            }
        }
        archMethod_ = VAProperties.PROPERTIES.getProperty("vainstall.archive.archivingMethod");
        if (archMethod_ == null || "".equals(archMethod_)) {
            archMethod_ = "append";
        }
        uiMode_ = VAProperties.PROPERTIES.getProperty("vainstall.destination.ui");
        if (uiMode_ == null || "".equals(uiMode_)) {
            uiMode_ = "graphic";
        }
        if ("graphic".equals(uiMode_)) {
            String lnfClass = VAProperties.PROPERTIES.getProperty("vainstall.lnf.class");
            if (lnfClass != null) {
                installProperties.put("vainstall.lnf.class", lnfClass);
                String lnfFileName = VAProperties.PROPERTIES.getProperty("vainstall.lnf.jar");
                if (lnfFileName != null) {
                    File jarFile = new File(lnfFileName);
                    if (jarFile.exists()) {
                        lnfFile_ = jarFile;
                        installProperties.put("vainstall.lnf.jar", jarFile.getName());
                    } else {
                        System.err.println("lnf jar File does NOT exist " + jarFile.getAbsolutePath());
                    }
                }
            }
        }
        uiBluescreen_ = VAProperties.PROPERTIES.getProperty("vainstall.destination.ui.bluescreen");
        if (uiBluescreen_ == null || "".equals(uiBluescreen_)) {
            uiBluescreen_ = "yes";
        }
        uiBluescreenColor_ = VAProperties.PROPERTIES.getProperty("vainstall.destination.ui.bluescreen.colour");
        if ((uiBluescreenColor_ != null) && (!"".equals(uiBluescreenColor_))) {
            try {
                Integer.parseInt(uiBluescreenColor_, 16);
            } catch (NumberFormatException nfe) {
                System.err.println("vainstall.destination.ui.bluescreen.colour" + VAGlobals.i18n("VAArchiver_HasIncorrectFormat"));
                return false;
            }
        }
        tmp = VAProperties.PROPERTIES.getProperty("vainstall.destination.ui.image");
        if ((tmp == null) || (tmp.equals(""))) {
            System.err.println("vainstall.destination.ui.image null:" + VAGlobals.i18n("VAArchiver_WillUseDefaultImage"));
            image_ = RC_DIR + "/banner.gif";
        } else {
            image_ = tmp;
            File img = new File(image_);
            if ((!img.exists()) || (!img.canRead())) {
                System.err.println(VAGlobals.i18n("VAArchiver_CanNotRead") + image_ + ":" + VAGlobals.i18n("VAArchiver_WillUseDefaultImage"));
                image_ = RC_DIR + "/banner.gif";
            }
        }
        appName_ = VAProperties.PROPERTIES.getProperty("vainstall.destination.appName");
        if (appName_ == null || "".equals(appName_)) {
            System.err.println("vainstall.destination.appName null");
            return false;
        }
        appVersion_ = VAProperties.PROPERTIES.getProperty("vainstall.destination.appVersion");
        if (appVersion_ == null || "".equals(appVersion_)) {
            System.out.println("vainstall.destination.appVersion null, assuming it is an unversioned plugin");
        }
        linkSectionName_ = VAProperties.PROPERTIES.getProperty("vainstall.destination.linkSectionName");
        if (linkSectionName_ == null || "".equals(linkSectionName_)) {
            System.err.println("vainstall.destination.linkSectionName null:" + VAGlobals.i18n("VAArchiver_DefaultingTo") + "\"Applications\"");
            linkSectionName_ = "Applications";
        }
        linkSectionIcon_ = VAProperties.PROPERTIES.getProperty("vainstall.destination.linkSectionIcon");
        if ((linkSectionIcon_ == null) || "".equals(linkSectionIcon_)) {
            System.err.println("vainstall.destination.linkSectionIcon null");
            linkSectionIcon_ = "";
        }
        linkEntryName_ = VAProperties.PROPERTIES.getProperty("vainstall.destination.linkEntryName");
        if (linkEntryName_ == null || "".equals(linkEntryName_)) {
            System.err.println("vainstall.destination.linkEntryName null:" + VAGlobals.i18n("VAArchiver_DefaultingTo") + "\"" + appName_ + "\"");
            linkEntryName_ = appName_;
        }
        linkEntryIcon_ = VAProperties.PROPERTIES.getProperty("vainstall.destination.linkEntryIcon");
        if ((linkEntryIcon_ == null) || "".equals(linkEntryIcon_)) {
            System.err.println("vainstall.destination.linkEntryIcon null");
            linkEntryIcon_ = "";
        }
        tempString = VAProperties.PROPERTIES.getProperty("vainstall.destination.createUninstallShortcut");
        if (tempString != null && tempString.equals("yes")) createUninstallShortcut_ = true; else createUninstallShortcut_ = false;
        instClassName_ = VAProperties.PROPERTIES.getProperty("vainstall.archive.installClassName");
        if (instClassName_ == null || "".equals(instClassName_)) {
            instClassName_ = "Install_" + appName_;
        }
        tmp = VAProperties.PROPERTIES.getProperty("vainstall.archive.license");
        if (tmp == null || "".equals(tmp)) {
            System.err.println("vainstall.archive.license null");
            return false;
        }
        license_ = new File(tmp);
        if ((!license_.exists()) || (!license_.canRead())) {
            System.err.println(VAGlobals.i18n("VAArchiver_CanNotRead") + license_);
            return false;
        }
        try {
            InputStream licenseStream = new FileInputStream(license_);
            String licenseEncoding = VAProperties.PROPERTIES.getProperty("vainstall.archive.license.encoding");
            if (licenseEncoding != null && licenseEncoding.equals("") == false) {
                try {
                    new InputStreamReader(licenseStream, licenseEncoding);
                } catch (UnsupportedEncodingException exc) {
                    System.err.println("Unsuported encoding for license!");
                    return false;
                }
            }
        } catch (Exception exc) {
            System.err.println("Problems reading license file!");
            return false;
        }
        tmp = VAProperties.PROPERTIES.getProperty("vainstall.license.key.support");
        if (tmp != null && !"".equals(tmp)) {
            licenseKeySupportClassName_ = tmp;
        }
        encodeKey_ = VAProperties.PROPERTIES.getProperty("vainstall.license.key.support.encode.key");
        additionalFiles_ = VAProperties.PROPERTIES.getProperty("vainstall.additional.files");
        Class cls = null;
        try {
            cls = Class.forName(licenseKeySupportClassName_);
        } catch (Exception ex) {
            if ((cls == null) && (additionalFiles_ != null)) {
                StringTokenizer fmi = new StringTokenizer(additionalFiles_, ",");
                while (fmi.hasMoreTokens()) {
                    StringTokenizer fm = new StringTokenizer(fmi.nextToken(), "!");
                    String classFound = fm.nextToken();
                    if ((classFound != null) && (classFound.indexOf(licenseKeySupportClassName_) > -1)) {
                        try {
                            URLClassLoader urlcl = new URLClassLoader(new URL[] { new File(classFound).getParentFile().toURL() });
                            cls = urlcl.loadClass(licenseKeySupportClassName_);
                        } catch (Exception ex2) {
                            System.out.println("License key support could not be initialized  with specific URLCLassLoader" + ex2);
                        }
                        break;
                    }
                }
            }
        }
        if (cls == null) {
            throw new RuntimeException("License key support could not be initialized: ");
        }
        try {
            System.out.println(cls.getName());
            licenseKeySupport_ = (LicenseKeySupport) cls.newInstance();
        } catch (Exception ex) {
            System.err.println("LicenseKeySupport can't be instantiated" + ex);
        }
        tmp = VAProperties.PROPERTIES.getProperty("vainstall.archive.readme");
        if (tmp == null || "".equals(tmp)) {
            System.err.println("vainstall.archive.readme null");
            return false;
        }
        readme_ = new File(tmp);
        if ((!readme_.exists()) || (!readme_.canRead())) {
            System.err.println(VAGlobals.i18n("VAArchiver_CanNotRead") + readme_);
            return false;
        }
        try {
            InputStream readmeStream = new FileInputStream(readme_);
            String readmeEncoding = VAProperties.PROPERTIES.getProperty("vainstall.archive.readme.encoding");
            if (readmeEncoding != null && readmeEncoding.equals("") == false) {
                try {
                    new InputStreamReader(readmeStream, readmeEncoding);
                } catch (UnsupportedEncodingException exc) {
                    System.err.println("Unsupported encoding for readme!");
                    return false;
                }
            }
        } catch (Exception exc) {
            System.err.println("Problems reading readme file!");
            return false;
        }
        customPrePostClassName_ = VAProperties.PROPERTIES.getProperty("vainstall.install.customprepost.className");
        if (customPrePostClassName_ == null) {
            customPrePostClassName_ = "";
        } else if (!"com.memoire.vainstall.AbstractCustomPrePost".equals(customPrePostClassName_)) {
            customPrePostJarfileName_ = VAProperties.PROPERTIES.getProperty("vainstall.install.customprepost.jarFileName");
            if (customPrePostJarfileName_ == null || "".equals(customPrePostJarfileName_)) {
                System.err.println("vainstall.install.customprepost.jarFileName null");
                return false;
            }
        }
        installProperties.put("vainstall.install.customprepost.className", customPrePostClassName_);
        return true;
    }

    private File makeArchive(String filename) throws IOException {
        LineNumberReader files = new LineNumberReader(new FileReader(filelist_));
        String line = files.readLine();
        if (line == null) {
            files.close();
            throw new IOException(VAGlobals.i18n("VAArchiver_EmptyFilelist"));
        }
        File zip = new File(filename);
        File parent = zip.getParentFile();
        if ((parent != null) && (!parent.canWrite())) throw new IOException(zip + VAGlobals.i18n("VAArchiver_NotWritten"));
        ZipOutputStream stream = new ZipOutputStream(new GZIPOutputStream(licenseKeySupport_.encodeStream(new FileOutputStream(zip), encodeKey_)));
        stream.setLevel(0);
        Vector archiveExeList = new Vector();
        FileNameGroup fileNameGroup = null;
        ObjectOutputStream infos = new ObjectOutputStream(archiveInfos_);
        Vector scripts = new Vector();
        while (line != null) {
            if ((!line.trim().equals("")) && (!line.trim().startsWith("#"))) {
                if (line.trim().startsWith("{")) {
                    String script = addScript(files, scripts);
                    if (script != null) archiveExeList.add("[SCRIPT]" + script);
                    line = files.readLine();
                    continue;
                }
                fileNameGroup = new FileNameGroup(line);
                File file = new File(convertToLocalPath(fileNameGroup.getOrigin()));
                String entryName = fileNameGroup.getDestination();
                if (!checkVaiPath(entryName)) throw new IOException(VAGlobals.i18n("VAArchiver_InvalidPathInFilelist") + files.getLineNumber() + "): " + entryName);
                if (fileNameGroup.isExecutable()) {
                    archiveExeList.add(entryName);
                }
                FileFilter filter = null;
                try {
                    filter = fileNameGroup.getFileFilter();
                } catch (IllegalArgumentException iae) {
                    throw new IOException(VAGlobals.i18n("VAArchiver_InFilelist") + files.getLineNumber() + "): " + iae.getMessage());
                }
                addToArchive(stream, file, filter, entryName, fileNameGroup.isRecursive() ? Integer.MAX_VALUE : 1);
            }
            line = files.readLine();
        }
        stream.close();
        files.close();
        infos.writeInt(archivecount_);
        infos.writeObject(scripts);
        infos.writeObject(archiveExeList);
        infos.flush();
        return zip;
    }

    private String addScript(LineNumberReader files, Vector scripts) throws IOException {
        String destName = null;
        int blocLineNbr = files.getLineNumber();
        String line = files.readLine();
        String cmd = null;
        Hashtable keywords = new Hashtable();
        if (line != null) cmd = line.trim();
        System.out.println("  script: " + cmd);
        line = files.readLine();
        while ((line != null) && (!line.trim().equals("}"))) {
            line = line.trim();
            int ind = line.indexOf('=');
            if ((ind > 0) && ((ind + 1) < line.length())) {
                keywords.put(line.substring(0, ind), line.substring(ind + 1));
            }
            line = files.readLine();
        }
        if ((cmd == null) || (line == null)) throw new IOException("Unterminated script bloc : line " + blocLineNbr);
        if (cmd.equals("JavaLauncher")) {
            String k = (String) keywords.get("Class");
            if (k == null) throw new IOException("Invalid script bloc : line " + blocLineNbr + " (no Class)");
            System.out.println("    Class=" + k);
        } else if (cmd.equals("JarLauncher")) {
            String k = (String) keywords.get("Jar");
            if (k != null) {
                System.out.println("    Jar=" + k);
            } else throw new IOException("Invalid script bloc : line " + blocLineNbr + " (no Jar)");
        } else {
            throw new IOException("Unsupported script : line " + blocLineNbr + " (" + cmd + ")");
        }
        String k = (String) keywords.get("ScriptName");
        if (k != null) {
            System.out.println("    ScriptName=" + k);
            destName = k;
        } else throw new IOException("Invalid script bloc : line " + blocLineNbr + " (no ScriptName)");
        String lineSep = "\n";
        StringBuffer buf = new StringBuffer();
        buf.append("CMD").append(lineSep).append(cmd);
        for (Iterator it = keywords.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry e = (Map.Entry) it.next();
            if (e.getValue() != null) {
                buf.append(lineSep).append((String) e.getKey()).append(lineSep).append((String) e.getValue());
            }
        }
        if (buf.length() > 0) scripts.add(buf.toString());
        return destName;
    }

    private void addToArchive(ZipOutputStream stream, File file, FileFilter filter, String entryName, int recurseDepth) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] ls = file.listFiles(filter);
                if (ls.length == 0) {
                    if (archiveEntryList_.contains(entryName + "/")) {
                        System.err.println("  warning: duplicate entry: " + entryName + "/" + ". Skipping.");
                    } else {
                        System.out.println("  " + file);
                        ZipEntry newEntry = new ZipEntry(entryName + "/");
                        stream.putNextEntry(newEntry);
                        stream.closeEntry();
                        archiveEntryList_.add(entryName + "/");
                    }
                }
                for (int i = 0; i < ls.length; i++) {
                    String ename = ls[i].getAbsolutePath();
                    ename = entryName + ename.substring(file.getAbsolutePath().length());
                    ename = convertToGenericPath(ename);
                    if (ename.startsWith("/")) ename = ename.substring(1);
                    if (recurseDepth > 0) addToArchive(stream, ls[i], filter, ename, recurseDepth - 1);
                }
            } else if (archiveEntryList_.contains(entryName)) {
                System.err.println("  warning: duplicate entry: " + entryName + ". Skipping.");
            } else {
                System.out.println("  " + file);
                archivecount_++;
                ZipEntry newEntry = new ZipEntry(entryName);
                stream.putNextEntry(newEntry);
                FileInputStream in = new FileInputStream(file);
                byte[] buf = new byte[2048];
                int read = in.read(buf, 0, buf.length);
                while (read > 0) {
                    stream.write(buf, 0, read);
                    read = in.read(buf, 0, buf.length);
                }
                in.close();
                stream.closeEntry();
                archiveEntryList_.add(entryName);
            }
        } else {
            System.err.println("  error: file " + file + " does not exist. Skipping.");
        }
    }

    private String convertToLocalPath(String entry) throws IOException {
        String res = Setup.expandDirectory(entry, true, null);
        if (res == null) throw new IOException("Invalid path: " + entry);
        return res.replace('/', File.separatorChar);
    }

    private String convertToGenericPath(String line) {
        return line.replace(File.separatorChar, '/');
    }

    private void generateInstallCode(File javaFile, String instTemplate, String classLoader) throws IOException {
        String tempString;
        PrintWriter writer = new PrintWriter(new FileWriter(javaFile));
        int read = 0;
        byte[] buf = new byte[128];
        InputStream is = getClass().getResourceAsStream("/" + instTemplate);
        InputStreamReader isr = new InputStreamReader(is);
        LineNumberReader reader = new LineNumberReader(isr);
        System.out.println(VAGlobals.i18n("VAArchiver_GeneratingInstallClassCode"));
        String line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> InstallClassName"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("public class " + instClassName_ + " {");
        writer.println("  private static final Class installClass=new " + instClassName_ + "().getClass();");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> ArchivingMethod"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String ARCH_METHOD=\"" + archMethod_ + "\";");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> TargetType"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String TARGET_TYPE=\"" + currentTarget_ + "\";");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> InstallClassOffset"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static long ICLASS_OFFSET=" + installClassOffset_ + "L;");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> InstallClassSize"))) {
            writer.println(line);
            line = reader.readLine();
        }
        if (installClassSize_ != archOffset_) writer.println("  private static long ICLASS_SIZE=" + installClassSize_ + "L;"); else writer.println("  private static long ICLASS_SIZE=-1234543210L;");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> ArchiveOffset"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static long ARCH_OFFSET=" + archOffset_ + "L;");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> JarSize"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static long JAR_SIZE=" + jarSize_ + "L;");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> UIMode"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String UI_MODE=\"" + uiMode_ + "\";");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> UIBluescreen"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String UI_BLUESCREEN=\"" + uiBluescreen_ + "\";");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> UIBluescreenColor"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String UI_BLUESCREEN_COLOR=\"" + uiBluescreenColor_ + "\";");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> DestPath"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String DEST_PATH=\"" + destPath_ + "\";");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> AppInfo"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String APP_NAME=\"" + appName_ + "\";");
        if (appVersion_ != null) writer.println("  private static String APP_VERSION=\"" + appVersion_ + "\";"); else writer.println("  private static String APP_VERSION=null;");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> LinkInfos"))) {
            System.out.println("fred " + line);
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String LINK_SECTION_NAME=\"" + linkSectionName_ + "\";");
        writer.println("  private static String LINK_SECTION_ICON=\"" + linkSectionIcon_ + "\";");
        writer.println("  private static String LINK_ENTRY_NAME=\"" + linkEntryName_ + "\";");
        writer.println("  private static String LINK_ENTRY_ICON=\"" + linkEntryIcon_ + "\";");
        if (createUninstallShortcut_) tempString = "true"; else tempString = "false";
        writer.println("  private static boolean CREATE_UNINSTALL_SHORTCUT=" + tempString + ";");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> LicenseKey"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String LICENSE_KEY_SUPPORT_NAME=\"" + licenseKeySupportClassName_ + "\";");
        System.out.println(VAGlobals.i18n("VAArchiver_AppendingClassloader"));
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> ClassLoader"))) {
            writer.println(line);
            line = reader.readLine();
        }
        InputStream isClassLoader = getClass().getResourceAsStream("/" + classLoader);
        writer.println("  private static String[] CL_CLASS={");
        read = isClassLoader.read(buf);
        while (read > 0) {
            writer.println("\"" + codeLine(buf, read) + "\",");
            read = isClassLoader.read(buf);
        }
        isClassLoader.close();
        writer.println("  };\n}");
        reader.close();
        writer.close();
        is.close();
        isr.close();
    }

    private void generateJnlpFile(File jnlpFile) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jnlpFile), "UTF-8")));
        out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.println("<jnlp spec=\"1.0\"");
        out.println("      codebase=\"" + jarCodebase_ + "\"");
        out.println("      href=\"" + instClassName_ + ".jnlp\">");
        out.println("  <information>");
        if (appVersion_ != null) out.println("    <title>" + appName_ + " " + appVersion_ + " Installer</title>"); else out.println("    <title>" + appName_ + " " + " Installer</title>");
        out.println("    <vendor>" + jarVendor_ + "</vendor>");
        out.println("    <homepage href=\"" + jarHomepage_ + "\"/>");
        if (appVersion_ != null) out.println("    <description>Installer for " + appName_ + " " + appVersion_ + "</description>"); else out.println("    <description>Installer for " + appName_ + "</description>");
        out.println("    <offline/>");
        out.println("  </information>");
        out.println("  <resources>");
        out.println("    <j2se version=\"1.3 1.2\"/>");
        out.println("    <jar href=\"" + instClassName_ + ".jar\"/>");
        out.println("    <property name=\"DEBUG\" value=\"yes\"/>");
        out.println("  </resources>");
        out.println("  <security>");
        out.println("    <all-permissions/>");
        out.println("  </security>");
        out.println("  <application-desc main-class=\"" + instClassName_ + "\"/>");
        out.println("</jnlp>");
        out.close();
    }

    private void generateManifestFile(File mfFile) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mfFile), "UTF-8")));
        out.println("Manifest-Version: 1.0");
        out.println("Main-Class: " + instClassName_);
        out.close();
    }

    private long generateUnixInstallShell(File unixShellFile, String instTemplate, File instClassFile) throws IOException {
        FileOutputStream byteWriter = new FileOutputStream(unixShellFile);
        InputStream is = getClass().getResourceAsStream("/" + instTemplate);
        InputStreamReader isr = new InputStreamReader(is);
        LineNumberReader reader = new LineNumberReader(isr);
        String content = "";
        String installClassStartStr = "000000000000";
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setGroupingUsed(false);
        nf.setMinimumIntegerDigits(installClassStartStr.length());
        int installClassStartPos = 0;
        long installClassOffset = 0;
        System.out.println(VAGlobals.i18n("VAArchiver_GenerateInstallShell"));
        String line = reader.readLine();
        while ((line != null) && (!line.startsWith("# InstallClassStart"))) {
            content += line + "\n";
            line = reader.readLine();
        }
        content += "InstallClassStart=" + installClassStartStr + "\n";
        installClassStartPos = content.length() - 1 - 1 - installClassStartStr.length();
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("# InstallClassSize"))) {
            content += line + "\n";
            line = reader.readLine();
        }
        content += new String("InstallClassSize=" + instClassFile.length() + "\n");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("# InstallClassName"))) {
            content += line + "\n";
            line = reader.readLine();
        }
        content += new String("InstallClassName=" + instClassName_ + "\n");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("# Install class"))) {
            content += line + "\n";
            line = reader.readLine();
        }
        if (line != null) content += line + "\n";
        byteWriter.write(content.substring(0, installClassStartPos + 1).getBytes());
        byteWriter.write(nf.format(content.length()).getBytes());
        byteWriter.write(content.substring(installClassStartPos + 1 + installClassStartStr.length()).getBytes());
        installClassOffset = content.length();
        content = null;
        FileInputStream classStream = new FileInputStream(instClassFile);
        byte[] buf = new byte[2048];
        int read = classStream.read(buf);
        while (read > 0) {
            byteWriter.write(buf, 0, read);
            read = classStream.read(buf);
        }
        classStream.close();
        reader.close();
        byteWriter.close();
        return installClassOffset;
    }

    private void shiftArray(byte[] array) {
        for (int i = 0; i < (array.length - 1); i++) array[i] = array[i + 1];
        array[array.length - 1] = 0;
    }

    private long generateNativeInstallExe(File nativeInstallFile, String instTemplate, File instClassFile) throws IOException {
        InputStream reader = getClass().getResourceAsStream("/" + instTemplate);
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        String installClassVarStr = "000000000000";
        byte[] buf = new byte[installClassVarStr.length()];
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setGroupingUsed(false);
        nf.setMinimumIntegerDigits(installClassVarStr.length());
        int installClassStopPos = 0;
        long installClassOffset = reader.available();
        int position = 0;
        System.out.println(VAGlobals.i18n("VAArchiver_GenerateInstallExe"));
        reader.read(buf, 0, buf.length);
        position = 1;
        for (int n = 0; n < 3; n++) {
            while ((!new String(buf).equals("clname_here_")) && (!new String(buf).equals("clstart_here")) && (!new String(buf).equals("clstop_here_"))) {
                content.write(buf[0]);
                int nextb = reader.read();
                position++;
                shiftArray(buf);
                buf[buf.length - 1] = (byte) nextb;
            }
            if (new String(buf).equals("clname_here_")) {
                VAGlobals.printDebug("  clname_here_ found at " + (position - 1));
                StringBuffer clnameBuffer = new StringBuffer(64);
                clnameBuffer.append(instClassName_);
                for (int i = clnameBuffer.length() - 1; i < 64; i++) {
                    clnameBuffer.append('.');
                }
                byte[] clnameBytes = clnameBuffer.toString().getBytes();
                for (int i = 0; i < 64; i++) {
                    content.write(clnameBytes[i]);
                    position++;
                }
                reader.skip(64 - buf.length);
                reader.read(buf, 0, buf.length);
            } else if (new String(buf).equals("clstart_here")) {
                VAGlobals.printDebug("  clstart_here found at " + (position - 1));
                buf = nf.format(installClassOffset).getBytes();
                for (int i = 0; i < buf.length; i++) {
                    content.write(buf[i]);
                    position++;
                }
                reader.read(buf, 0, buf.length);
            } else if (new String(buf).equals("clstop_here_")) {
                VAGlobals.printDebug("  clstop_here_ found at " + (position - 1));
                installClassStopPos = position - 1;
                content.write(buf);
                position += 12;
                reader.read(buf, 0, buf.length);
            }
        }
        content.write(buf);
        buf = new byte[2048];
        int read = reader.read(buf);
        while (read > 0) {
            content.write(buf, 0, read);
            read = reader.read(buf);
        }
        reader.close();
        FileInputStream classStream = new FileInputStream(instClassFile);
        read = classStream.read(buf);
        while (read > 0) {
            content.write(buf, 0, read);
            read = classStream.read(buf);
        }
        classStream.close();
        content.close();
        byte[] contentBytes = content.toByteArray();
        installClassVarStr = nf.format(contentBytes.length);
        byte[] installClassVarBytes = installClassVarStr.getBytes();
        for (int i = 0; i < installClassVarBytes.length; i++) {
            contentBytes[installClassStopPos + i] = installClassVarBytes[i];
        }
        FileOutputStream out = new FileOutputStream(nativeInstallFile);
        out.write(contentBytes);
        out.close();
        return installClassOffset;
    }

    private void appendArchive(File instClass) throws IOException {
        FileOutputStream out = new FileOutputStream(instClass.getName(), true);
        FileInputStream zipStream = new FileInputStream("install.jar");
        byte[] buf = new byte[2048];
        int read = zipStream.read(buf);
        while (read > 0) {
            out.write(buf, 0, read);
            read = zipStream.read(buf);
        }
        zipStream.close();
        out.close();
    }

    private void copy(File fin, File fout) throws IOException {
        FileOutputStream out = null;
        FileInputStream in = null;
        try {
            out = new FileOutputStream(fout);
            in = new FileInputStream(fin);
            byte[] buf = new byte[2048];
            int read = in.read(buf);
            while (read > 0) {
                out.write(buf, 0, read);
                read = in.read(buf);
            }
        } catch (IOException _e) {
            throw _e;
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }

    private void jar(String options, File jarFile, File[] files) throws IOException {
        Process p = null;
        Vector argsv = new Vector();
        argsv.add(getJavaBinPath() + File.separator + "jar");
        if (options != null && !options.equals("")) argsv.add(options);
        argsv.add(jarFile.getName());
        for (int i = 0; i < files.length; i++) argsv.add(files[i].getName());
        String[] args = new String[argsv.size()];
        for (int i = 0; i < args.length; i++) args[i] = (String) argsv.get(i);
        try {
            p = Runtime.getRuntime().exec(args);
            p.waitFor();
        } catch (Exception rte) {
            throw new IOException("Runtime exception: check if you have installed the JDK and run java from the JDK\n" + "Exception message: " + rte.getMessage());
        }
        printCmdOutput(p, "jar");
        if (p.exitValue() != 0) throw new RuntimeException("  abnormal exit");
    }

    private void jarsign(String passphrase, File jarFile, String alias) throws IOException {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(new String[] { JDK_HOME + File.separator + "bin" + File.separator + "jarsigner", "-storepass", passphrase, jarFile.getName(), alias });
            p.waitFor();
        } catch (Exception rte) {
            throw new IOException("Runtime exception: check if you have installed the JDK and run java from the JDK\n" + "Exception message: " + rte.getMessage());
        }
        printCmdOutput(p, "jarsign");
        if (p.exitValue() != 0) throw new RuntimeException("  abnormal exit");
    }

    private File compile(String javafile) throws IOException {
        File classFile = null;
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(new String[] { getJavaBinPath() + File.separator + JAVAC, javafile });
            p.waitFor();
        } catch (Exception rte) {
            throw new IOException("Runtime exception: check if you have installed the JDK and run java from the JDK\n" + "Exception message: " + rte.getMessage());
        }
        printCmdOutput(p, "javac");
        if (p.exitValue() != 0) throw new RuntimeException("  abnormal exit");
        classFile = new File(javafile.substring(0, javafile.lastIndexOf('.')) + ".class");
        System.out.println("  " + classFile + " " + classFile.exists());
        return classFile;
    }

    private void printCmdOutput(Process p, String cmdName) throws IOException {
        BufferedReader psIn = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader psErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        System.out.println("  --- start " + cmdName + " ---");
        String inLine = psIn.readLine();
        String errLine = psErr.readLine();
        while ((inLine != null) || (errLine != null)) {
            if (inLine != null) System.out.println("  " + inLine);
            if (errLine != null) System.err.println("  " + errLine);
            inLine = psIn.readLine();
            errLine = psErr.readLine();
        }
        psIn.close();
        psErr.close();
        System.out.println("  --- end   " + cmdName + " ---");
    }

    private File makeJar(String filename, File archive, File license, File readme, byte[] archiveInfos) throws IOException {
        File jar = new File(filename);
        JarOutputStream out = new JarOutputStream(new FileOutputStream(jar));
        copyInternalToJar(out, JAR_FILES_COMMON);
        copyInternalToJar(out, JAR_FILES_JNIREGISTRY);
        copyInternalToJar(out, JAR_FILES_JNISHORTCUT);
        copyInternalToJar(out, JAR_FILES_TEXT_UI);
        copyInternalToJar(out, JAR_FILES_ANSI_UI);
        if (uiMode_.equals("graphic")) copyInternalToJar(out, JAR_FILES_GRAPHIC_UI);
        if (uiMode_.equals("xtra")) copyInternalToJar(out, JAR_FILES_XTRA_UI);
        if (uiMode_.equals("unattended")) copyInternalToJar(out, JAR_FILES_UNATTENDED_UI);
        if (customPrePostClassName_.length() > 0) {
            copyInternalToJar(out, JAR_FILES_CUSTOM_PRE_POST);
            if (!"com.memoire.vainstall.AbstractCustomPrePost".equals(customPrePostClassName_)) {
                copyJarFilesToJar(out, customPrePostJarfileName_);
            }
        }
        addToJar(out, new ByteArrayInputStream(archiveInfos), "com/memoire/vainstall/archive_infos", archiveInfos.length);
        addToJar(out, new FileInputStream(archive), "com/memoire/vainstall/archive.zip", archive.length());
        InputStream licenseStream = new FileInputStream(license);
        String licenseEncoding = VAProperties.PROPERTIES.getProperty("vainstall.archive.license.encoding");
        if (licenseEncoding == null || licenseEncoding.equals("") == true) {
            licenseEncoding = new InputStreamReader(licenseStream).getEncoding();
        }
        InputStreamReader isrLicense = new InputStreamReader(licenseStream, licenseEncoding);
        addToJarEncoded(out, isrLicense, "com/memoire/vainstall/license.txt", license.length());
        InputStream readmeStream = new FileInputStream(readme);
        String readmeEncoding = VAProperties.PROPERTIES.getProperty("vainstall.archive.readme.encoding");
        if (readmeEncoding == null || readmeEncoding.equals("") == true) {
            readmeEncoding = new InputStreamReader(readmeStream).getEncoding();
        }
        InputStreamReader isrReadme = new InputStreamReader(readmeStream, readmeEncoding);
        addToJarEncoded(out, isrReadme, "com/memoire/vainstall/readme.txt", readme.length());
        InputStream invaiimage = null;
        try {
            invaiimage = new FileInputStream(new File(image_));
        } catch (Exception exc) {
            image_ = "/" + image_;
            invaiimage = getClass().getResourceAsStream(image_);
        }
        if (invaiimage == null) {
            invaiimage = getClass().getResourceAsStream("com/memoire/vainstall/resources/banner.gif");
            image_ = "com/memoire/vainstall/resources/banner.gif";
        }
        if (invaiimage != null) {
            addToJar(out, invaiimage, "com/memoire/vainstall/resources/banner.gif", invaiimage.available());
        }
        InputStream invailogo = getClass().getResourceAsStream("/" + VAILOGO);
        addToJar(out, invailogo, VAILOGO, invailogo.available());
        ByteArrayOutputStream poutstream = new ByteArrayOutputStream();
        installProperties.store(poutstream, VAGlobals.NAME + " " + VAGlobals.VERSION);
        ByteArrayInputStream pinstream = new ByteArrayInputStream(poutstream.toByteArray());
        addToJar(out, pinstream, "com/memoire/vainstall/resources/vainstall.properties", poutstream.toByteArray().length);
        if (lnfFile_ != null) {
            System.out.println("add lnf jar file: " + lnfFile_.getAbsolutePath());
            addToJar(out, new FileInputStream(lnfFile_), "com/memoire/vainstall/resources/" + lnfFile_.getName(), lnfFile_.length());
        }
        System.out.println("additionalFiles");
        if (additionalFiles_ != null) {
            StringTokenizer fmi = new StringTokenizer(additionalFiles_, ",");
            while (fmi.hasMoreTokens()) {
                StringTokenizer fm = new StringTokenizer(fmi.nextToken(), "!");
                File sf = new File(fm.nextToken());
                String df = fm.nextToken();
                addToJar(out, new FileInputStream(sf), df, sf.length());
            }
            System.out.println("additionalFiles FIN");
        }
        out.close();
        return jar;
    }

    /**
	 * Copy all files from a jar file from inside a jar file to a target jar
	 * file
	 * 
	 * @param out
	 *            JarOutputStream
	 * @param jarSourceName
	 *            String
	 */
    private void copyInternalToJar(JarOutputStream out, String[] JAR_FILES) throws IOException {
        for (int i = 0; i < JAR_FILES.length; i++) {
            String sourceName = JAR_FILES[i];
            if (sourceName.endsWith(".jar")) {
                copyJarFilesToJar(out, sourceName);
            } else {
                InputStream is = getClass().getResourceAsStream("/" + sourceName);
                if (is == null) {
                    System.out.println("Cannot add the resource " + sourceName + " to the jar, since it cannot be found (bummer!)");
                } else {
                    addToJar(out, is, sourceName, is.available());
                }
            }
        }
    }

    /**
	 * Copy all files from a jar file from inside a jar file to a target jar
	 * file
	 * 
	 * @param out
	 *            JarOutputStream
	 * @param jarSourceName
	 *            String
	 */
    private void copyJarFilesToJar(JarOutputStream out, String jarSourceName) throws IOException {
        byte[] buffer = new byte[2048];
        InputStream isJar = getClass().getResourceAsStream("/" + jarSourceName);
        JarInputStream zin = new JarInputStream(isJar);
        JarEntry entry = null;
        while ((entry = zin.getNextJarEntry()) != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while (true) {
                int read = zin.read(buffer);
                if (read == -1) break;
                bos.write(buffer, 0, read);
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
            addToJar(out, bais, entry.getName(), bos.toByteArray().length);
            bos.close();
            zin.closeEntry();
        }
        zin.close();
        isJar.close();
    }

    private void addToJar(JarOutputStream out, InputStream in, String entryName, long length) throws IOException {
        byte[] buf = new byte[2048];
        ZipEntry entry = new ZipEntry(entryName);
        CRC32 crc = new CRC32();
        entry.setSize(length);
        entry.setCrc(crc.getValue());
        out.putNextEntry(entry);
        int read = in.read(buf);
        while (read > 0) {
            crc.update(buf, 0, read);
            out.write(buf, 0, read);
            read = in.read(buf);
        }
        entry.setCrc(crc.getValue());
        in.close();
        out.closeEntry();
    }

    private void addToJarEncoded(JarOutputStream out, InputStreamReader isr, String entryName, long length) throws IOException {
        StringBuffer buffer = new StringBuffer();
        Reader reader = new BufferedReader(isr);
        int ch;
        while ((ch = reader.read()) > -1) {
            buffer.append((char) ch);
        }
        reader.close();
        isr.close();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(baos, "UTF8");
        osw.write(buffer.toString());
        osw.flush();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        addToJar(out, bais, entryName, length);
    }

    private boolean checkVaiPath(String dir) {
        int index = dir.indexOf(']');
        if (!dir.startsWith("[")) return true;
        if (index <= 2) return false;
        String prefix = dir.substring(1, index).trim();
        if ("HOME".equals(prefix) || "PROGRAM".equals(prefix) || prefix.startsWith("ENV{")) {
            return true;
        }
        if ((prefix.length() == 2) && (prefix.endsWith(":")) && (Character.isLetter(prefix.charAt(0)))) return true;
        return false;
    }

    private String codeLine(byte[] data, int siz) {
        String res = null;
        byte[] convert = new byte[2 * siz];
        for (int i = 0; i < siz; i++) {
            convert[2 * i] = (byte) (65 + (data[i] & 0x0F));
            convert[2 * i + 1] = (byte) (65 + (data[i] & 0xF0) / 16);
        }
        res = new String(convert);
        return res;
    }
}

class FileNameGroup {

    String originBase_, destBase_, commonPath_, extensions_;

    boolean executable_;

    boolean recursive_;

    public FileNameGroup(String line) {
        executable_ = false;
        recursive_ = true;
        if (line.startsWith("(E)")) {
            line = line.substring(3);
            executable_ = true;
        }
        if (line.startsWith("(N)")) {
            line = line.substring(3);
            recursive_ = false;
        }
        StringTokenizer tok = new StringTokenizer(new String(line), "|", true);
        originBase_ = tok.nextToken();
        if (originBase_.endsWith("/")) originBase_ = originBase_.substring(0, originBase_.length() - 1);
        tok.nextToken();
        destBase_ = tok.nextToken();
        if (destBase_.equals("|")) destBase_ = null; else {
            if (destBase_.endsWith("/")) destBase_ = destBase_.substring(0, destBase_.length() - 1);
            tok.nextToken();
        }
        extensions_ = null;
        commonPath_ = null;
        if (tok.hasMoreTokens()) {
            commonPath_ = tok.nextToken();
            if (commonPath_.equals("|")) commonPath_ = null;
            if (tok.hasMoreTokens()) {
                if (commonPath_ != null) tok.nextToken();
                if (tok.hasMoreTokens()) extensions_ = tok.nextToken();
            }
        }
    }

    public String getOrigin() {
        if (commonPath_ == null) return originBase_;
        return originBase_ + "/" + commonPath_;
    }

    public String getDestination() {
        if ((destBase_ == null) && (commonPath_ == null)) return "";
        if (destBase_ == null) return commonPath_;
        if (commonPath_ == null) return destBase_;
        return destBase_ + "/" + commonPath_;
    }

    public FileFilter getFileFilter() throws IllegalArgumentException {
        String[] ext = null;
        if (extensions_ != null) {
            StringTokenizer t = new StringTokenizer(extensions_, ",");
            ext = new String[t.countTokens()];
            for (int i = 0; i < ext.length; i++) {
                ext[i] = t.nextToken();
                if ((ext[i].length() >= 2) && (ext[i].startsWith("*"))) ext[i] = ext[i].substring(1); else if ((ext[i].length() >= 3) && (ext[i].startsWith("!*"))) ext[i] = "!" + ext[i].substring(2); else throw new IllegalArgumentException("invalid pattern: " + ext[i]);
            }
        }
        return new VaiFileFilter(ext);
    }

    public boolean isExecutable() {
        return executable_;
    }

    public boolean isRecursive() {
        return recursive_;
    }
}

class VaiFileFilter implements FileFilter {

    String[] ext_;

    public VaiFileFilter(String[] ext) {
        ext_ = ext;
    }

    public boolean accept(File pathname) {
        boolean a = true;
        if (ext_ == null) return true;
        String p = pathname.getName();
        for (int i = 0; i < ext_.length; i++) {
            if (ext_[i].startsWith("!")) {
                a = a && (!p.endsWith(ext_[i].substring(1)));
            } else {
                a = a && (p.endsWith(ext_[i]));
            }
        }
        return a;
    }
}
