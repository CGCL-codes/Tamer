package build;

import java.io.File;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import tools.CommandWrapper;
import tools.Files;
import tools.TemplateParser;
import model.AndroidProject;
import model.AndroidTarget;
import model.Attributes;
import model.KeyStore;

/**
 * 构建某个项目
 * @author 13leaf
 *
 */
public class PackManager {

    private String projectPath;

    private String projectName;

    protected AndroidTarget target;

    protected AndroidProject project;

    protected CommandWrapper cmdWrapper;

    protected Attributes attributes;

    /**
	 * 使用Attribute构建项目,修正AndroidTarget的ant bug。其的default.Properties总会比预计高出一号
	 * @param attributes
	 */
    public PackManager(Attributes attributes) {
        this.attributes = attributes;
        project = attributes.getAndroidProject();
        projectName = project.projectName;
        projectPath = project.projectPath;
        if (attributes.getProperty("targetID") != null) {
            target = AndroidTarget.getPlatformTarget(Integer.parseInt(attributes.getProperty("targetAndroidID")));
            attributes.setAndroidTarget(target);
        }
        initAttributes();
        init();
    }

    /**
	 * 根据项目路径构建打包器，使用项目默认的target。
	 * @param projectPath
	 */
    public PackManager(String projectPath) {
        this(projectPath, null);
    }

    /**
	 * 设置指定的target
	 * @param projectPath
	 * @param target
	 */
    public PackManager(String projectPath, AndroidTarget target) {
        this.projectPath = projectPath;
        this.target = target;
        initAttributes();
        init();
    }

    /**
	 * 子类请在此处初始化Attributes。<br>
	 * public void initAttributes()<br>
	 * {<br>
	 * super.initAttributes();<br>
	 * do your init Attributes here <br>
	 * }<br>
	 */
    protected void initAttributes() {
        if (projectName == null) {
            projectPath = projectPath.replaceAll("/", "\\");
            projectName = projectPath.substring(projectPath.lastIndexOf('\\') + 1);
        }
        System.out.println(projectPath);
        String projectEncode = "";
        File prefsFile = new File(projectPath, ".settings\\org.eclipse.core.resources.prefs");
        if (prefsFile.exists()) {
            String encodeExp = "encoding/<project>=(\\S+)";
            Matcher encodeMatcher = Files.parseFile(prefsFile, encodeExp);
            if (encodeMatcher.find()) {
                projectEncode = encodeMatcher.group(1);
            }
        }
        if (target == null) {
            File defaultPropertiesFile = new File(projectPath, "default.properties");
            String defaultTargetReg = "target\\s*=.+?[:-]{1}(\\d+)";
            Matcher targetMatcher = Files.parseFile(defaultPropertiesFile, defaultTargetReg);
            if (targetMatcher.find()) target = AndroidTarget.getPlatformTarget(Integer.parseInt(targetMatcher.group(1)));
            System.out.println(target);
        }
        String projectVersion = "";
        File manifestFile = new File(projectPath, "AndroidManifest.xml");
        Matcher versionMatcher = Files.parseFile(manifestFile, "android:versionName=\"(.+?)\"");
        if (versionMatcher.find()) {
            projectVersion = versionMatcher.group(1);
            System.out.println("version" + projectVersion);
        }
        File classpathFile = new File(projectPath, ".classpath");
        String regExp = "classpathentry\\s+" + "kind\\s*=\\s*\"lib\"\\s+" + "path\\s*=\\s*\"(.+?)\"";
        Matcher matcher = Files.parseFile(classpathFile, regExp);
        List<String> libs = new LinkedList<String>();
        while (matcher.find()) {
            libs.add(new File(projectPath, matcher.group(1)).getAbsolutePath());
        }
        String[] libsPath = libs.toArray(new String[libs.size()]);
        AndroidProject project = new AndroidProject();
        project.projectName = this.projectName;
        project.projectPath = this.projectPath;
        project.libsPath = libsPath;
        project.projectEncode = projectEncode;
        project.projectVersion = projectVersion;
        this.project = project;
        if (attributes == null) attributes = new Attributes();
        attributes.setAndroidProject(project);
        attributes.setAndroidTarget(target);
        final String releaseApk = "${projectName}-unsigned.apk";
        attributes.put("apkPath", new TemplateParser().parseTemplate(releaseApk, attributes));
    }

    /**
	 * 初始化cmd进程，生成ant脚本。拷贝.classpath中引用的文件至libs中
	 */
    private void init() {
        File apkFile = new File(projectPath, "bin\\" + projectName + "-unsigned.apk");
        if (apkFile.exists()) apkFile.delete();
        if (project.projectEncode.length() != 0) Files.defaultCharset = Charset.forName(project.projectEncode);
        System.out.println("project encode:" + project.projectEncode);
        String path = attributes.getProperty("path");
        if (path != null) CommandWrapper.INIT_PATH = path;
        cmdWrapper = CommandWrapper.getCommandWrapper();
        System.out.println(new TemplateParser().parseTemplate(UPDATE_TEMPLATE, attributes));
        cmdWrapper.doCommand(UPDATE_TEMPLATE, attributes);
        cmdWrapper.doCommand(projectPath.substring(0, projectPath.indexOf(':') + 1));
        cmdWrapper.doCommand("cd " + projectPath);
        File libsDir = new File(projectPath, "libs");
        if (!libsDir.exists()) libsDir.mkdir();
        for (String libFile : project.libsPath) {
            File src = new File(libFile);
            File destination = new File(libsDir, src.getName());
            Files.copyFile(src, destination);
        }
    }

    /**
	 * 打包前做些修改操作,如渠道号等。
	 * @param modify
	 * @return
	 */
    public boolean doPreModify(IModify modify, File file, int[] lines, String[] modifyContents) {
        return modify.modify(file, lines, modifyContents, attributes);
    }

    /**
	 * 增加version号，生成apk，签名，并重命名。
	 * @param outDir
	 * @param name
	 * @param keyStore 要想将apk发布出去,keystore是必不可少的。不签名，这个打包工具也变得没有意义了
	 * @param template
	 * @return 返回是否成功
	 */
    public boolean doPackage(File outDir, KeyStore keyStore, IName name, String template) {
        if (!outDir.exists()) outDir.mkdirs();
        ManifestModify modify = new ManifestModify(new File(projectPath, "AndroidManifest.xml"));
        modify.plusVersionCode();
        cmdWrapper.doCommand(RELEASE_COMMAND);
        attributes.setKeyStore(keyStore);
        cmdWrapper.doCommand("cd bin");
        cmdWrapper.doCommand(SIGN_TEMPLATE, attributes);
        File apkFile = new File(projectPath, "bin\\" + projectName + "-unsigned.apk");
        cmdWrapper.waitForExit();
        if (apkFile.exists()) {
            apkFile = name.name(attributes, template, apkFile);
            File outApk = new File(outDir, apkFile.getName());
            Files.copyFile(apkFile, outApk);
            apkFile.delete();
            System.err.println(outApk.getAbsolutePath() + " release complete!");
            return true;
        }
        cmdWrapper.waitForExit();
        System.err.println("release fail");
        return false;
    }

    /**
	 * 获得属性集
	 * @return
	 */
    public Attributes getAttributes() {
        return attributes;
    }

    public static void main(String[] args) {
        PackManager manager = new PackManager("E:\\workSpace\\packageTest\\IfengVideo3_SVN");
        manager.doPreModify(new SimpleModify(), new File("E:\\workSpace\\IfengVideo3_SVN\\src\\com\\ifeng\\newvideo\\util\\PConfig.java"), new int[] { 36 }, new String[] { "publishid=\"${pubID:testPub}\";" });
        manager.doPackage(new File("D:\\Backup\\我的文档\\ifeng_apks"), KeyStore.getTestKeyStore(), new SimpleNaming(), "${projectName}_android_${targetVersion}_v${projectVersion}_${pubID}.apk");
    }

    public static final String UPDATE_TEMPLATE = "android update project --target ${targetID} --name ${projectName} --path ${projectPath}";

    public static final String RELEASE_COMMAND = "ant release";

    public static final String SIGN_TEMPLATE = "jarsigner -verbose -keystore ${keyPath} -keypass ${keyPass} -storepass ${storePass} ${apkPath} ${keyAlias}";
}
