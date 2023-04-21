package edu.rice.cs.drjava.model.compiler;

import java.io.File;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import edu.rice.cs.drjava.DrJava;
import edu.rice.cs.drjava.model.DJError;
import edu.rice.cs.drjava.model.DrJavaFileUtils;
import edu.rice.cs.drjava.config.OptionConstants;
import edu.rice.cs.plt.reflect.JavaVersion;
import javax.swing.filechooser.FileFilter;
import edu.rice.cs.drjava.ui.SmartSourceFilter;

/** A CompilerInterface implementation for signifying that no compiler is available.
  * @version $Id: NoCompilerAvailable.java 5395 2010-09-21 15:26:15Z mgricken $
  */
public class NoCompilerAvailable implements CompilerInterface {

    public static final CompilerInterface ONLY = new NoCompilerAvailable();

    private static final String MESSAGE = "No compiler is available.";

    private NoCompilerAvailable() {
    }

    public boolean isAvailable() {
        return false;
    }

    public List<? extends DJError> compile(List<? extends File> files, List<? extends File> classPath, List<? extends File> sourcePath, File destination, List<? extends File> bootClassPath, String sourceVersion, boolean showWarnings) {
        return Arrays.asList(new DJError(MESSAGE, false));
    }

    public JavaVersion version() {
        return JavaVersion.UNRECOGNIZED;
    }

    public String getName() {
        return "(no compiler available)";
    }

    public String getDescription() {
        return getName();
    }

    /** The toString() of this class is displayed in the "Compiler" drop down on the compiler tab.
    * @return "None"
    */
    @Override
    public String toString() {
        return "None";
    }

    public List<File> additionalBootClassPathForInteractions() {
        return Arrays.<File>asList();
    }

    /** Transform the command line to be interpreted into something the Interactions JVM can use.
    * This replaces "java MyClass a b c" with Java code to call MyClass.main(new String[]{"a","b","c"}).
    * "import MyClass" is not handled here.
    * transformCommands should support at least "run", "java" and "applet".
    * @param interactionsString unprocessed command line
    * @return command line with commands transformed */
    public String transformCommands(String interactionsString) {
        return interactionsString;
    }

    /** Always false 
    * @return true if the specified file is a source file for this compiler. */
    public boolean isSourceFileForThisCompiler(File f) {
        return false;
    }

    /** Return the set of source file extensions that this compiler supports.
    * @return the set of source file extensions that this compiler supports. */
    public Set<String> getSourceFileExtensions() {
        return DrJavaFileUtils.getSourceFileExtensions();
    }

    /** Return the suggested file extension that will be appended to a file without extension.
    * @return the suggested file extension */
    public String getSuggestedFileExtension() {
        return DrJavaFileUtils.getSuggestedFileExtension();
    }

    /** Return a file filter that can be used to open files this compiler supports.
    * @return file filter for appropriate source files for this compiler */
    public FileFilter getFileFilter() {
        return new SmartSourceFilter();
    }

    /** Return the extension of the files that should be opened with the "Open Folder..." command.
    * @return file extension for the "Open Folder..." command for this compiler. */
    public String getOpenAllFilesInFolderExtension() {
        return OptionConstants.LANGUAGE_LEVEL_EXTENSIONS[DrJava.getConfig().getSetting(OptionConstants.LANGUAGE_LEVEL)];
    }

    /** Return true if this compiler can be used in conjunction with the language level facility.
    * @return true if language levels can be used. */
    public boolean supportsLanguageLevels() {
        return true;
    }

    /** Return the set of keywords that should be highlighted in the specified file.
    * @param f file for which to return the keywords
    * @return the set of keywords that should be highlighted in the specified file. */
    public Set<String> getKeywordsForFile(File f) {
        return JavacCompiler.JAVA_KEYWORDS;
    }
}
