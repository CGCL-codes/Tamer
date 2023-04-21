package jacg;

/**
 * Diese Klasse stellt die Konfiguration f�r den Template-Compiler zur
 * Verf�gung.
 * 
 * @author                       Carsten Spr�ner
 */
public class CompilerConfig {

    String classpath = "";

    String outputPath = ".";

    String tmpSourcePath = ".";

    String sourcePath = ".";

    String cbgCompilerImpl = "jacg.CBGCompiler14Impl";

    /**
     * Getter for property classpath.
     * 
     * @return Value of property classpath.
     *  
     */
    public String getClasspath() {
        return classpath;
    }

    /**
     * Setter for property classpath.
     * 
     * @param value
     *            New value of property classpath.
     *  
     */
    public void setClasspath(String[] value) {
        clearClasspath();
        for (int i = 0; i < value.length; i++) {
            classpath += value[i] + ";";
        }
    }

    public void addToClasspath(String path) {
        classpath += path + ";";
    }

    public void clearClasspath() {
        classpath = "";
    }

    /**
     * Getter for property outputPath.
     * 
     * @return Value of property outputPath.
     *  
     */
    public String getOutputPath() {
        return outputPath;
    }

    /**
     * Setter for property outputPath.
     * 
     * @param outputPath
     *            New value of property outputPath.
     *  
     */
    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    /**
     * Getter for property tmpSourcePath.
     * 
     * @return Value of property tmpSourcePath.
     *  
     */
    public String getTmpSourcePath() {
        return tmpSourcePath;
    }

    /**
     * Setter for property tmpSourcePath.
     * 
     * @param tmpSourcePath
     *            New value of property tmpSourcePath.
     *  
     */
    public void setTmpSourcePath(String tmpSourcePath) {
        this.tmpSourcePath = tmpSourcePath;
    }

    /**
     * Getter for property sourcePath.
     * 
     * @return Value of property sourcePath.
     *  
     */
    public java.lang.String getSourcePath() {
        return sourcePath;
    }

    /**
     * Setter for property sourcePath.
     * 
     * @param sourcePath
     *            New value of property sourcePath.
     *  
     */
    public void setSourcePath(java.lang.String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getCompilerImpl() {
        return cbgCompilerImpl;
    }

    public void setCompilerImpl(String value) {
        this.cbgCompilerImpl = value;
    }
}
