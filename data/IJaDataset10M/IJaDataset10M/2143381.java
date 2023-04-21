package com.ibm.celldt.sputiming.parameters;

import java.io.File;
import org.eclipse.ui.console.IOConsole;

public class CompilerParameters {

    private String compilerPath;

    private String compilerFlags;

    private String sourceFile;

    private File workingDirectory;

    private IOConsole console;

    /**
	 * @param compilerPath
	 * @param compilerFlags
	 * @param sourceFile
	 * @param workingDirectory
	 * @param console
	 */
    public CompilerParameters(String compilerPath, String compilerFlags, String sourceFile, File workingDirectory, IOConsole console) {
        this.compilerPath = compilerPath;
        this.compilerFlags = compilerFlags;
        this.sourceFile = sourceFile;
        this.workingDirectory = workingDirectory;
        this.console = console;
    }

    public CompilerParameters() {
    }

    public String getCompilerFlags() {
        return compilerFlags;
    }

    public String getCompilerPath() {
        return compilerPath;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    public IOConsole getConsole() {
        return console;
    }

    public void setCompilerFlags(String compilerFlags) {
        this.compilerFlags = compilerFlags;
    }

    public void setCompilerPath(String compilerPath) {
        this.compilerPath = compilerPath;
    }

    public void setConsole(IOConsole console) {
        this.console = console;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public void setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }
}
