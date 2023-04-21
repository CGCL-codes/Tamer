package unclej.utasks.compile;

import unclej.filepath.FileSpec;
import unclej.filepath.Path;
import unclej.framework.UTask;
import java.io.File;

/**
 * Compiles Java source code.
 * @author scottv
 */
public interface CompileJavaUTask extends UTask {

    void addSourceFiles(FileSpec fileSpec);

    void setClassPath(Path classPath);

    void setSourcePath(Path sourcePath);

    void setOutputDir(File outputDirectory);

    void setEncoding(String encoding);

    void setSourceVersion(String sourceVersion);

    void setTargetVersion(String targetVersion);

    void setNoWarn(boolean noWarn);

    void setDeprecation(boolean deprecation);

    void setDebugLines(boolean debugLines);

    void setDebugVars(boolean debugVars);

    void setDebugSource(boolean debugSource);

    void setDebugAll(boolean debug);

    String VERSION_1_2 = "1.2";

    String VERSION_1_3 = "1.3";

    String VERSION_1_4 = "1.4";

    String VERSION_1_5 = "1.5";
}
