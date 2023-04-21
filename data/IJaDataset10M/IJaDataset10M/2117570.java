package jflex.anttask;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import jflex.Main;
import jflex.Options;
import java.io.*;

/**
 * JFlex task class
 *
 * @author Rafal Mantiuk
 * @version JFlex 1.5, $Revision: 586 $, $Date: 2010-03-07 03:59:36 -0500 (Sun, 07 Mar 2010) $
 */
public class JFlexTask extends Task {

    private File inputFile;

    private String className = null;

    private String packageName = null;

    /** for javac-like dest dir behaviour */
    private File destinationDir;

    /** the actual output directory (outputDir = destinationDir + package)) */
    private File outputDir = null;

    public JFlexTask() {
        setVerbose(false);
        Options.progress = false;
    }

    public void execute() throws BuildException {
        try {
            if (inputFile == null) throw new BuildException("Input file needed. Use <jflex file=\"your_scanner.flex\"/>");
            if (!inputFile.canRead()) throw new BuildException("Cannot read input file " + inputFile);
            try {
                findPackageAndClass();
                normalizeOutdir();
                File destFile = new File(outputDir, className + ".java");
                if (inputFile.lastModified() > destFile.lastModified()) {
                    Main.generate(inputFile);
                    if (!Options.verbose) System.out.println("Generated: " + destFile.getName());
                }
            } catch (IOException e1) {
                throw new BuildException("IOException: " + e1.toString());
            }
        } catch (jflex.GeneratorException e) {
            throw new BuildException("JFlex: generation failed!");
        }
    }

    /**
	 * Peek into .flex file to get package and class name
	 * 
	 * @throws IOException  if there is a problem reading the .flex file 
	 */
    public void findPackageAndClass() throws IOException {
        packageName = null;
        className = null;
        LineNumberReader reader = new LineNumberReader(new FileReader(inputFile));
        while (className == null || packageName == null) {
            String line = reader.readLine();
            if (line == null) break;
            if (packageName == null) {
                int index = line.indexOf("package");
                if (index >= 0) {
                    index += 7;
                    int end = line.indexOf(';', index);
                    if (end >= index) {
                        packageName = line.substring(index, end);
                        packageName = packageName.trim();
                    }
                }
            }
            if (className == null) {
                int index = line.indexOf("%class");
                if (index >= 0) {
                    index += 6;
                    className = line.substring(index);
                    className = className.trim();
                }
            }
        }
        if (className == null) className = "Yylex";
    }

    /**
	 * Sets the actual output directory if not already set. 	
	 *
	 * Uses javac logic to determine output dir = dest dir + package name
	 * If not destdir has been set, output dir = parent of input file
	 * 
	 * Assumes that package name is already set. 
	 */
    public void normalizeOutdir() {
        if (outputDir != null) return;
        File destDir;
        if (destinationDir != null) {
            if (packageName == null) {
                destDir = destinationDir;
            } else {
                String path = packageName.replace('.', File.separatorChar);
                destDir = new File(destinationDir, path);
            }
        } else {
            destDir = new File(inputFile.getParent());
        }
        setOutdir(destDir);
    }

    /**
	 * @return package name of input file
	 * 
	 * @see #findPackageAndClass()
	 */
    public String getPackage() {
        return packageName;
    }

    /**
	 * @return class name of input file
	 * 
	 * @see #findPackageAndClass()
	 */
    public String getClassName() {
        return className;
    }

    public void setDestdir(File destinationDir) {
        this.destinationDir = destinationDir;
    }

    public void setOutdir(File outDir) {
        this.outputDir = outDir;
        Options.setDir(outputDir);
    }

    public void setFile(File file) {
        this.inputFile = file;
    }

    public void setGenerateDot(boolean genDot) {
        setDot(genDot);
    }

    public void setTimeStatistics(boolean displayTime) {
        Options.time = displayTime;
    }

    public void setTime(boolean displayTime) {
        setTimeStatistics(displayTime);
    }

    public void setVerbose(boolean verbose) {
        Options.verbose = verbose;
    }

    public void setSkeleton(File skeleton) {
        Options.setSkeleton(skeleton);
    }

    public void setSkel(File skeleton) {
        setSkeleton(skeleton);
    }

    public void setSkipMinimization(boolean skipMin) {
        setNomin(skipMin);
    }

    public void setNomin(boolean b) {
        Options.no_minimize = b;
    }

    public void setNobak(boolean b) {
        Options.no_backup = b;
    }

    public void setSwitch(boolean b) {
        if (b) {
            Options.gen_method = Options.SWITCH;
        } else {
            Options.gen_method = Options.PACK;
        }
    }

    public void setTable(boolean b) {
        if (b) {
            Options.gen_method = Options.TABLE;
        } else {
            Options.gen_method = Options.PACK;
        }
    }

    public void setPack(boolean b) {
        if (b) {
            Options.gen_method = Options.PACK;
        } else {
            Options.gen_method = Options.SWITCH;
        }
    }

    public void setDot(boolean b) {
        Options.dot = b;
    }

    public void setDump(boolean b) {
        Options.dump = b;
    }

    public void setJLex(boolean b) {
        Options.jlex = b;
    }
}
