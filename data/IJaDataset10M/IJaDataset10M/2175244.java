package de.evandor.easyc.bundlesGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.abator.api.Abator;
import org.apache.ibatis.abator.config.AbatorConfiguration;
import org.apache.ibatis.abator.config.xml.AbatorConfigurationParser;
import org.apache.ibatis.abator.internal.DefaultShellCallback;
import org.apache.maven.embedder.Configuration;
import org.apache.maven.embedder.ConfigurationValidationResult;
import org.apache.maven.embedder.DefaultConfiguration;
import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import de.evandor.easyc.bundlesGenerator.model.BundlesGeneratorModel;
import de.evandor.easyc.bundlesGenerator.model.Column;
import de.evandor.easyc.bundlesGenerator.model.ModelTemplate;
import de.evandor.easyc.bundlesGenerator.model.BundlesGeneratorModel.ConnectionDetails;

public class SketchGenerator {

    private static String outputPath = "C:/gencode";

    private static BundlesGeneratorModel model = new BundlesGeneratorModel(outputPath);

    private static final String COMMON_TEMPLATE_DIR = "templates\\common";

    private static final String SERVER_TEMPLATE_DIR = "templates\\server";

    private static final String PAXRUNNER_TEMPLATE_DIR = "templates\\paxrunner";

    private static final String CLIENTCONSOLE_TEMPLATE_DIR = "templates\\client\\console";

    static void init() throws Exception {
        Velocity.init();
    }

    public static void main(String args[]) throws Exception {
        SketchGenerator.init();
        SketchGenerator.start("", "");
    }

    public static void start(String modelFile, String templateFile) throws Exception {
        ArrayList<String> cpe = new ArrayList<String>();
        cpe.add("C:\\libs\\mysql-connector-java-5.1.6\\mysql-connector-java-5.1.6-bin.jar");
        model.setClassPathEntries(cpe);
        ConnectionDetails jdbcDetails = model.new ConnectionDetails();
        jdbcDetails.setDriver("com.mysql.jdbc.Driver");
        jdbcDetails.setUrl("jdbc:mysql://localhost:3306/osgiportal");
        jdbcDetails.setSchema("osgiportal");
        jdbcDetails.setUser("root");
        jdbcDetails.setPassword("");
        model.setJdbcDetails(jdbcDetails);
        Class.forName(jdbcDetails.getDriver());
        Connection conn = DriverManager.getConnection(jdbcDetails.getUrl(), jdbcDetails.getUser(), jdbcDetails.getPassword());
        DatabaseMetaData metadata = conn.getMetaData();
        List<ModelTemplate> modelTemplates = new ArrayList<ModelTemplate>();
        createModelTemplates(metadata, modelTemplates);
        createCommonPackages(conn, modelTemplates);
        createHibernateArtefakts(conn, modelTemplates);
        createMavenPackages(conn, modelTemplates);
        createServerPackages(conn, modelTemplates);
        createHibernateArtefakts(conn, modelTemplates);
        createServerMavenPackages(conn, modelTemplates);
        createPaxRunnerProject(modelTemplates);
        createClientConsoleBundle(modelTemplates);
    }

    /**
     * for now: manipulates (maybe empty) list of modelTemplates according to
     * information retrieved from database structure
     * 
     * @param metadata
     * @param modelTemplates
     * @throws SQLException
     */
    private static void createModelTemplates(DatabaseMetaData metadata, List<ModelTemplate> modelTemplates) throws SQLException {
        String[] names = { "TABLE" };
        ResultSet tableNames = metadata.getTables(null, "%", "%", names);
        while (tableNames.next()) {
            String tableName = tableNames.getString("TABLE_NAME");
            System.out.println("found table " + tableName);
            System.out.println("--- analysing table ---");
            List<String> primaryKeys = getPrimaryKeysList(metadata, tableName);
            ModelTemplate template = new ModelTemplate();
            template.setIdentifierColumn(primaryKeys.size() > 0 ? primaryKeys.get(0) : null);
            template.setTableName(tableName);
            Map<String, Column> columns = new HashMap<String, Column>();
            ResultSet columnsRS = metadata.getColumns(null, "", tableName, "");
            while (columnsRS.next()) {
                String columnName = columnsRS.getString(4);
                int columnType = columnsRS.getInt(5);
                Column column = new Column();
                column.setColumnName(columnName);
                column.setType(columnType);
                column.setAutoincrement(columnsRS.getBoolean("IS_AUTOINCREMENT"));
                if (primaryKeys.contains(columnName)) {
                    column.setPrimaryPart(true);
                }
                System.out.println("    found column " + column.toString());
                columns.put(columnName, column);
            }
            template.setColumns(columns);
            modelTemplates.add(template);
            System.out.println("--- analysing table done ---");
        }
    }

    /**
     * @param metadata
     * @param tableName
     * @return
     * @throws SQLException
     */
    private static List<String> getPrimaryKeysList(DatabaseMetaData metadata, String tableName) throws SQLException {
        ResultSet primaryKeysRS = metadata.getPrimaryKeys(null, "%", tableName);
        List<String> primaryKeys = new ArrayList<String>();
        while (primaryKeysRS.next()) {
            if (primaryKeysRS.getString("COLUMN_NAME") != null) {
                primaryKeys.add(primaryKeysRS.getString("COLUMN_NAME"));
            }
        }
        primaryKeysRS.close();
        return primaryKeys;
    }

    /**
     * traverses "common" templates directory for each model template provided;
     * all files in this directory are merged for each given modelTemplate
     * 
     * @param conn
     * @param tables
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    private static void createCommonPackages(Connection conn, List<ModelTemplate> templates) throws Exception {
        for (ModelTemplate template : templates) {
            traverseCommonDir(template, new File(COMMON_TEMPLATE_DIR));
        }
    }

    private static void createServerPackages(Connection conn, List<ModelTemplate> templates) throws Exception {
        SketchGenerator.class.getMethods();
        for (ModelTemplate template : templates) {
            traverseServerDir(template, new File(SERVER_TEMPLATE_DIR));
        }
    }

    /**
     * @param tables
     */
    private static void createAbatorArtefakts(Connection conn, List<ModelTemplate> templates) {
        for (ModelTemplate template : templates) {
            System.out.println("running ibator for " + template.getIbatorFile());
            List warnings = new ArrayList();
            boolean overwrite = true;
            File configFile = new File(template.getIbatorFile());
            AbatorConfigurationParser cp = new AbatorConfigurationParser(warnings);
            AbatorConfiguration config;
            try {
                config = cp.parseAbatorConfiguration(configFile);
                DefaultShellCallback callback = new DefaultShellCallback(overwrite);
                Abator abator = new Abator(config, callback, warnings);
                abator.generate(null);
                System.out.println(warnings);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void createHibernateArtefakts(Connection conn, List<ModelTemplate> templates) {
        for (ModelTemplate template : templates) {
            System.out.println("running ant for " + template.getHibernateBuildFile());
            File buildFile = new File(template.getHibernateBuildFile());
            Project p = new Project();
            p.setBasedir(buildFile.getParent());
            System.out.println(p.getBaseDir());
            p.setUserProperty("ant.file", buildFile.getAbsolutePath());
            p.init();
            ProjectHelper helper = ProjectHelper.getProjectHelper();
            p.addReference("ant.projectHelper", helper);
            helper.parse(p, buildFile);
            p.executeTarget(p.getDefaultTarget());
        }
    }

    /**
     * @param tables
     */
    private static void createMavenPackages(Connection conn, List<ModelTemplate> templates) throws Exception {
        for (ModelTemplate template : templates) {
            File projectDirectory = new File(template.getIbatorFile()).getParentFile().getParentFile();
            System.out.println("running maven in " + projectDirectory);
            File user = new File(projectDirectory, "settings.xml");
            Configuration configuration = new DefaultConfiguration().setUserSettingsFile(user).setClassLoader(Thread.currentThread().getContextClassLoader());
            ConfigurationValidationResult validationResult = MavenEmbedder.validateConfiguration(configuration);
            if (validationResult.isValid()) {
                MavenEmbedder embedder = new MavenEmbedder(configuration);
                MavenExecutionRequest request = new DefaultMavenExecutionRequest().setBaseDirectory(projectDirectory).setGoals(Arrays.asList(new String[] { "clean", "install" }));
                MavenExecutionResult result = embedder.execute(request);
                if (result.hasExceptions()) {
                    System.out.println(result);
                }
            } else {
                if (!validationResult.isUserSettingsFilePresent()) {
                    System.out.println("The specific user settings file '" + user + "' is not present.");
                } else if (!validationResult.isUserSettingsFileParses()) {
                    System.out.println("Please check your settings file, it is not well formed XML.");
                }
            }
        }
    }

    /**
     * @param tables
     */
    private static void createServerMavenPackages(Connection conn, List<ModelTemplate> templates) throws Exception {
        for (ModelTemplate template : templates) {
            String path = outputPath + "\\" + model.getServerPackageName() + "." + template.getTableName();
            File projectDirectory = new File(path);
            System.out.println("running maven in " + projectDirectory);
            File user = new File(projectDirectory, "settings.xml");
            Configuration configuration = new DefaultConfiguration().setUserSettingsFile(user).setClassLoader(Thread.currentThread().getContextClassLoader());
            ConfigurationValidationResult validationResult = MavenEmbedder.validateConfiguration(configuration);
            if (validationResult.isValid()) {
                MavenEmbedder embedder = new MavenEmbedder(configuration);
                MavenExecutionRequest request = new DefaultMavenExecutionRequest().setBaseDirectory(projectDirectory).setGoals(Arrays.asList(new String[] { "clean", "install" }));
                MavenExecutionResult result = embedder.execute(request);
                if (result.hasExceptions()) {
                    System.out.println(result);
                }
            } else {
                if (!validationResult.isUserSettingsFilePresent()) {
                    System.out.println("The specific user settings file '" + user + "' is not present.");
                } else if (!validationResult.isUserSettingsFileParses()) {
                    System.out.println("Please check your settings file, it is not well formed XML.");
                }
            }
        }
    }

    /**
     * @param modelTemplates
     */
    private static void createPaxRunnerProject(List<ModelTemplate> modelTemplates) throws Exception {
        traversePaxrunnerDir(new File(PAXRUNNER_TEMPLATE_DIR));
        for (ModelTemplate template : modelTemplates) {
            StringBuilder path = new StringBuilder(outputPath + "\\" + model.getCommonPackageName());
            path.append("." + template.getTableName() + "/target/");
            StringBuilder filename = new StringBuilder();
            filename.append("common-" + template.getTableName());
            filename.append("-" + model.getBundleVersion());
            filename.append(".jar");
            System.out.println("copying jars to plugin dir from " + path.toString() + filename.toString());
            String tmpPath = outputPath + "\\de.evandor.easyc.server.paxrunner\\plugins\\";
            String outputFile = tmpPath + "\\" + filename.toString();
            copyfile(path.toString() + filename.toString(), outputFile);
        }
        for (ModelTemplate template : modelTemplates) {
            StringBuilder path = new StringBuilder(outputPath + "\\" + model.getServerPackageName());
            path.append("." + template.getTableName() + "/target/");
            StringBuilder filename = new StringBuilder();
            filename.append("server-" + template.getTableName());
            filename.append("-" + model.getBundleVersion());
            filename.append(".jar");
            System.out.println("copying jars to plugin dir from " + path.toString() + filename.toString());
            String tmpPath = outputPath + "\\de.evandor.easyc.server.paxrunner\\plugins\\";
            String outputFile = tmpPath + "\\" + filename.toString();
            copyfile(path.toString() + filename.toString(), outputFile);
        }
    }

    /**
     * @param modelTemplates
     */
    private static void createClientConsoleBundle(List<ModelTemplate> modelTemplates) throws Exception {
        traverseClientConsoleDir(modelTemplates, new File(CLIENTCONSOLE_TEMPLATE_DIR));
    }

    private static void traverseCommonDir(ModelTemplate template, File file) throws Exception {
        processCommonFiles(template, file);
        if (includeDir(file)) {
            String[] children = file.list();
            for (int i = 0; i < children.length; i++) {
                traverseCommonDir(template, new File(file, children[i]));
            }
        }
    }

    /**
     * @param dir
     * @return
     */
    private static boolean includeDir(File dir) {
        return dir.isDirectory() && !dir.getName().equals("CVS") && !dir.getName().equals(".svn");
    }

    private static void traverseServerDir(ModelTemplate template, File dir) throws Exception {
        processServerDir(template, dir);
        if (includeDir(dir)) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                traverseServerDir(template, new File(dir, children[i]));
            }
        }
    }

    /**
     * @param file
     */
    private static void traversePaxrunnerDir(File dir) throws Exception {
        processPaxRunnerDir(dir);
        if (includeDir(dir)) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                traversePaxrunnerDir(new File(dir, children[i]));
            }
        }
    }

    /**
     * @param file
     */
    private static void traverseClientConsoleDir(List<ModelTemplate> templates, File dir) throws Exception {
        processClientConsoleDir(templates, dir);
        if (includeDir(dir)) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                traverseClientConsoleDir(templates, new File(dir, children[i]));
            }
        }
    }

    private static void processServerDir(ModelTemplate template, File dir) throws Exception {
        System.out.print((dir.isDirectory() ? "[D] : " : "[F] : "));
        System.out.println(dir);
        if (dir.isFile()) {
            VelocityContext context = new VelocityContext();
            model.setEntityName(template.getTableName());
            model.setColumns(template.getColumns());
            context.put("model", model);
            String offset = dir.getParentFile().getPath().toString().substring(SERVER_TEMPLATE_DIR.length());
            String tmpPath = (outputPath + "\\" + model.getServerPackageName() + "." + template.getTableName() + "\\" + offset).replace("$entityName", template.getTableName());
            tmpPath = tmpPath.replace("$basePackageName", model.getBasePackageName().replace(".", "\\"));
            String outputFile = (tmpPath + "/" + dir.getName()).replace("$entityName", model.getEntityNameWithFirstUppercase());
            if (outputFile.contains("\\ibator/") && outputFile.endsWith(".xml")) {
                template.setIbatorFile(outputFile);
            }
            if (outputFile.contains("\\hibernate/") && outputFile.endsWith("build.xml")) {
                template.setHibernateBuildFile(outputFile);
            }
            new File(tmpPath).mkdirs();
            if (outputFile.endsWith(".ftl")) {
                copyfile(dir.getPath(), outputFile);
            } else {
                Template vt = Velocity.getTemplate(dir.getPath());
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
                vt.merge(context, writer);
                writer.flush();
                writer.close();
            }
            System.out.println("File " + outputFile + " generated!");
        }
    }

    /**
     * merges model (from modelTemplate) with provided file
     * 
     * @param modelTemplate
     * @param templateFile
     * @throws Exception
     */
    private static void processCommonFiles(ModelTemplate modelTemplate, File templateFile) throws Exception {
        if (!templateFile.isFile()) return;
        VelocityContext context = new VelocityContext();
        model.setEntityName(modelTemplate.getTableName());
        model.setPrimaryKey(modelTemplate.getIdentifierColumn());
        model.setColumns(modelTemplate.getColumns());
        context.put("model", model);
        String offset = templateFile.getParentFile().getPath().toString().substring(COMMON_TEMPLATE_DIR.length());
        String outputPathForFile = (outputPath + "\\" + model.getCommonPackageName() + "." + modelTemplate.getTableName() + "\\" + offset).replace("$entityName", modelTemplate.getTableName());
        outputPathForFile = outputPathForFile.replace("$basePackageName", model.getBasePackageName().replace(".", "\\"));
        String outputFile = (outputPathForFile + "\\" + templateFile.getName()).replace("$entityName", model.getEntityNameWithFirstUppercase());
        if (outputFile.contains("\\ibator\\") && outputFile.endsWith(".xml")) {
            modelTemplate.setIbatorFile(outputFile);
        }
        if (outputFile.contains("\\hibernate\\") && outputFile.endsWith("build.xml")) {
            modelTemplate.setHibernateBuildFile(outputFile);
        }
        new File(outputPathForFile).mkdirs();
        if (outputFile.endsWith(".ftl")) {
            copyfile(templateFile.getPath(), outputFile);
        } else {
            Template vt = Velocity.getTemplate(templateFile.getPath());
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            vt.merge(context, writer);
            writer.flush();
            writer.close();
        }
    }

    /**
     * @param dir
     */
    private static void processPaxRunnerDir(File dir) throws Exception {
        if (dir.isFile()) {
            String offset = dir.getParentFile().getPath().toString().substring(PAXRUNNER_TEMPLATE_DIR.length());
            String tmpPath = outputPath + "\\de.evandor.easyc.server.paxrunner\\" + offset;
            tmpPath = tmpPath.replace("$basePackageName", model.getBasePackageName().replace(".", "\\"));
            String outputFile = tmpPath + "\\" + dir.getName();
            new File(tmpPath).mkdirs();
            copyfile(dir.getPath(), outputFile);
        }
    }

    /**
     * @param templates
     * @param dir
     */
    private static void processClientConsoleDir(List<ModelTemplate> templates, File dir) throws Exception {
        if (dir.isFile()) {
            VelocityContext context = new VelocityContext();
            context.put("model", model);
            String offset = dir.getParentFile().getPath().toString().substring(CLIENTCONSOLE_TEMPLATE_DIR.length());
            String tmpPath = outputPath + "\\de.evandor.easyc.client.console\\" + offset;
            tmpPath = tmpPath.replace("$basePackageName", model.getBasePackageName().replace(".", "\\"));
            String outputFile = tmpPath + "\\" + dir.getName();
            Template vt = Velocity.getTemplate(dir.getPath());
            new File(tmpPath).mkdirs();
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            vt.merge(context, writer);
            writer.flush();
            writer.close();
        }
    }

    private static void copyfile(String srFile, String dtFile) {
        try {
            File f1 = new File(srFile);
            File f2 = new File(dtFile);
            InputStream in = new FileInputStream(f1);
            OutputStream out = new FileOutputStream(f2);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + " in the specified directory.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
