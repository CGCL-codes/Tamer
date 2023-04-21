package org.gaea.common.metadata.compiler;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.Vector;
import javax.tools.Diagnostic;
import org.gaea.common.Constants;
import org.gaea.common.GaeaLogger;
import org.gaea.common.GaeaLoggerFactory;
import org.gaea.common.command.Command;
import org.gaea.common.command.CommandManager;
import org.gaea.common.command.CommandVerb;
import org.gaea.common.command.IConnector;
import org.gaea.common.command.QueueExecutionHandler;
import org.gaea.common.command.QueueManagementHandler;
import org.gaea.common.command.database.IGaeaEnhancer;
import org.gaea.common.command.operator.ISelectionOperator;
import org.gaea.common.exception.GaeaException;
import org.gaea.common.metadata.__gaea_classes;
import org.gaea.common.metadata.memory.EnhancedFileModel;
import org.gaea.common.metadata.memory.EnhancementModel;
import org.gaea.common.util.DatabaseUtility;
import org.gaea.common.util.FileHelper;
import org.gaea.common.util.FileSystemHelper;
import org.gaea.common.util.JavaCodeHelper;

/**
 * This class combines functions from the compiler and the enhancer to to
 * facilitate usage.
 * 
 * @author bdevost
 */
public class GaeaMetadataCompiler {

    private static GaeaLogger _logger = GaeaLoggerFactory.getLogger("org.gaea.common.metadata.GaeaMetadataCompiler");

    public static String getWorkFolderFromConnector(IConnector connector) {
        return Constants.Folder.getHomeFolder() + Constants.Folder.DEFAULT_METADATA + Constants.Folder.DEFAULT_FILE_SEPARATOR + connector.getProfileName();
    }

    public static String[] compileClasses(String[] cls, IConnector connector) throws GaeaException {
        String workFolderFromConnector = getWorkFolderFromConnector(connector);
        File srcDir = new File(workFolderFromConnector + Constants.Folder.DEFAULT_FILE_SEPARATOR + "src");
        File binDir = new File(Constants.Folder.getMetadataClassesCompiledFolder());
        return compileClasses(cls, srcDir, binDir);
    }

    /**
	 * @param classes
	 *            a list of class names
	 * @param inputfolder
	 *            the input folder
	 * @param outputfolder
	 *            the output folder
	 * 
	 * @return all successfully compiled classes
	 * @throws GaeaException
	 */
    public static String[] compileClasses(String[] classes, File inputfolder, File outputfolder) throws GaeaException {
        if (classes.length == 0) {
            return classes;
        }
        Vector<File> paths = new Vector<File>();
        GaeaCompiler compiler = new GaeaCompiler(inputfolder);
        for (String cls : classes) {
            String inpath = inputfolder.getPath() + Constants.Folder.DEFAULT_FILE_SEPARATOR;
            String[] packages = cls.split("\\.");
            for (int x = 0; x < packages.length - 1; x++) {
                inpath += packages[x] + Constants.Folder.DEFAULT_FILE_SEPARATOR;
            }
            inpath += packages[packages.length - 1] + ".java";
            paths.add(new File(inpath));
        }
        if (compiler.compile(paths.toArray(new File[] {}), outputfolder)) {
            return classes;
        }
        StringBuilder builder = new StringBuilder();
        for (Object obj : compiler.getDiagnostic().getDiagnostics()) {
            Diagnostic diag = (Diagnostic) obj;
            builder.append(diag);
            builder.append(Constants.Folder.DEFAULT_LINE_SEPARATOR);
        }
        throw new GaeaException(GaeaException.Error.COMPILER_Error, builder.toString());
    }

    /**
	 * This method enhances all given Gaea classes.
	 * 
	 * @param compiled
	 *            a list of successfully compiled classes
	 * @param enhancer
	 *            enhancer to use
	 * @param connector
	 *            the connector to use
	 * @return a return FLAG
	 * @throws GaeaException
	 */
    public static int enhanceClasses(String[] compiled, IGaeaEnhancer enhancer, IConnector connector) throws GaeaException {
        return enhanceClasses(compiled, enhancer, new File(Constants.Folder.getMetadataClassesCompiledFolder()));
    }

    /**
	 * This method enhances all given Gaea classes.
	 * 
	 * @param compiled
	 *            a list of successfully compiled classes
	 * @param enhancer
	 *            enhancer to use
	 * @param input
	 *            the base folder where the classes are
	 * @return a return FLAG
	 * @throws GaeaException
	 */
    public static int enhanceClasses(String[] compiled, IGaeaEnhancer enhancer, File input) throws GaeaException {
        try {
            int rv = enhancer.generateDescriptiveFile(compiled, input);
            enhancer.enhance(input);
            return rv;
        } catch (Exception e) {
            _logger.log(e);
            throw new GaeaException(GaeaException.Error.ENHANCER_Error, e.getMessage());
        }
    }

    /**
	 * Compiles and enhances all metadata present on the server to which the
	 * connector refers.
	 * 
	 * @param connector
	 *            the connector to use
	 * @throws GaeaException
	 */
    public static void compileMetadata(IConnector connector) throws GaeaException {
        Vector<String> classList = new Vector<String>();
        Vector<EnhancedFileModel> enhancedFileList = new Vector<EnhancedFileModel>();
        File baseDir = new File(getWorkFolderFromConnector(connector));
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        File srcDir = new File(baseDir, "src");
        if (!srcDir.exists()) {
            srcDir.mkdirs();
        }
        File binDir = new File(Constants.Folder.getMetadataClassesCompiledFolder());
        String enhancementModelFilePath = getWorkFolderFromConnector(connector) + Constants.Folder.DEFAULT_FILE_SEPARATOR + "model.xml";
        if (!DatabaseUtility.isClassExistingOnServer(connector, __gaea_classes.class)) {
            QueueManagementHandler queue = CommandManager.getInstance().startQueue();
            queue.append(CommandVerb.Begin, null);
            __gaea_classes cls = new __gaea_classes();
            queue.append(CommandVerb.Insert, cls);
            queue.append(CommandVerb.Delete, cls);
            queue.append(CommandVerb.Commit, null);
            QueueExecutionHandler handler = queue.end();
            try {
                handler.execute(connector);
            } catch (GaeaException e) {
                throw e;
            }
        }
        GaeaMetadataCompiler.clearCodeData(connector);
        Command command = CommandManager.getInstance().create(CommandVerb.Select);
        ((ISelectionOperator) command).setup(__gaea_classes.class);
        Collection classes = DatabaseUtility.selectAndRetrieve(connector, command);
        for (Object obj : classes) {
            __gaea_classes cls = (__gaea_classes) obj;
            String javacode = cls.getJavacode();
            if (javacode == "" || javacode == null) {
                _logger.error("Error for class " + cls.getName() + ": no java code entered. The class will be skipped.");
                continue;
            }
            String pack = JavaCodeHelper.getPackage(javacode);
            String className = JavaCodeHelper.getClassName(javacode);
            String fullClassName = (pack == null ? "" : pack + ".") + className;
            if (!fullClassName.equals(cls.getName())) {
                _logger.warning("Faulty __gaea_classes name for class " + fullClassName + ".");
            }
            classList.add(fullClassName);
            File packageDir = new File(srcDir, (pack == null ? "" : pack.replaceAll("\\.", "/")));
            if (!packageDir.exists()) {
                packageDir.mkdirs();
            }
            File localFile = new File(Constants.Folder.getMetadataClassesEnhancedFolder(), fullClassName.replaceAll("\\.", "/") + ".class");
            EnhancedFileModel model = new EnhancedFileModel(fullClassName.replaceAll("\\.", "/") + ".class", (localFile.exists() ? localFile.lastModified() : 0), cls.getDateUpdated().getTime());
            enhancedFileList.add(model);
            try {
                FileHelper.save(javacode, new File(packageDir, className + ".java"));
            } catch (IOException e) {
                _logger.error(e.getMessage());
            }
        }
        Vector<EnhancedFileModel> diff = diffEnhancementModel(enhancementModelFilePath, enhancedFileList);
        if (diff.size() == 0 && enhancedFileList.size() > 0) {
            return;
        }
        GaeaMetadataCompiler.clearMetadata();
        String[] compiled = GaeaMetadataCompiler.compileClasses(classList.toArray(new String[] {}), srcDir, binDir);
        IGaeaEnhancer enhancer = connector.getDatabaseImpl().getEnhancer(connector);
        Properties props = connector.getDatabaseImpl().getProperties();
        enhancer.makePropertiesFile(props);
        GaeaMetadataCompiler.enhanceClasses(compiled, enhancer, binDir);
        updateEnhancementModel(enhancementModelFilePath, enhancedFileList);
    }

    /**
	 * Clears all java code from the metadata.
	 * 
	 * @param connector the used connector
	 */
    private static void clearCodeData(IConnector connector) {
        File codeDir = new File(getWorkFolderFromConnector(connector));
        for (File subfiles : codeDir.listFiles()) {
            FileSystemHelper.recursiveDelete(subfiles);
        }
    }

    /**
	 * Clears all local compiled metadata.
	 */
    public static void clearMetadata() {
        File compiledDir = new File(Constants.Folder.getMetadataClassesCompiledFolder());
        for (File subfiles : compiledDir.listFiles()) {
            FileSystemHelper.recursiveDelete(subfiles);
        }
        File enhancedDir = new File(Constants.Folder.getMetadataClassesEnhancedFolder());
        for (File subfiles : enhancedDir.listFiles()) {
            FileSystemHelper.recursiveDelete(subfiles);
        }
    }

    /**
	 * Updates the enhancement model with the new metadata information.
	 * 
	 * @param filePath
	 *            xml file to save
	 * @param fileList
	 *            a list of all enhanced files
	 */
    public static void updateEnhancementModel(final String filePath, Vector<EnhancedFileModel> fileList) {
        EnhancementModel model = new EnhancementModel();
        for (EnhancedFileModel file : fileList) {
            File fsf = new File(Constants.Folder.getMetadataClassesEnhancedFolder(), file.getPath());
            if (fsf.exists()) {
                file.setLocalCode(fsf.lastModified());
            }
            model.addFile(file);
        }
        model.save(filePath);
    }

    /**
	 * Calculates the difference between the current metadata and the metadata
	 * from the server.
	 * 
	 * @param filePath
	 *            xml file to save
	 * @param fileList
	 *            a list of all enhanced files
	 * @return a list of all differences
	 */
    public static Vector<EnhancedFileModel> diffEnhancementModel(final String filePath, Vector<EnhancedFileModel> fileList) {
        EnhancementModel model = EnhancementModel.load(filePath);
        Vector<EnhancedFileModel> toRemove = new Vector<EnhancedFileModel>();
        for (EnhancedFileModel fileModel : fileList) {
            try {
                File file = new File(Constants.Folder.getMetadataClassesEnhancedFolder(), fileModel.getPath());
                if (file.exists()) {
                    for (EnhancedFileModel localModel : model.getFiles()) {
                        if (localModel.getPath().equals(fileModel.getPath())) {
                            boolean isSameLocal = localModel.getLocalCode() == fileModel.getLocalCode();
                            boolean isSameServer = localModel.getServerCode() == fileModel.getServerCode();
                            if (isSameLocal && isSameServer) {
                                toRemove.add(fileModel);
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        Vector<EnhancedFileModel> rv = new Vector<EnhancedFileModel>();
        for (EnhancedFileModel fileModel : fileList) {
            if (!toRemove.contains(fileModel)) {
                rv.add(fileModel);
            }
        }
        return rv;
    }
}
