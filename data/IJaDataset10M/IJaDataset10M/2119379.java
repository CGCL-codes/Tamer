package net.sf.openforge.util.exec;

import java.io.*;
import java.util.ArrayList;

/**
 * <code>JavaC</code> supports externally launching the javac
 * compiler.  The user of this class should create a new instance with
 * the desired parameters then, if they desire to print verbose info,
 * call <code>getCommandLine</code> to get the string representation of
 * how the java compiler will be launched, then call <code>compile</code>
 * to perform the compilation, and finally call <code>getExitValue</code>
 * to retrieve the exit code from the process.  Any stdout or stderr
 * produced by calling the java compiler will always be sent to the application
 * supplied stdout and stderr respectively.  If there are no problems, javac
 * runs without printing anything.
 *
 * @version $Id
 * @since 1.0
 */
public class JavaC {

    private static final String rcs_id = "RCS_REVISION: $Rev: 2 $";

    /**
     * <code>sourceFile</code> holds a file path to the source file we
     * will compile.
     *
     */
    private String sourceFile = null;

    /**
     * <code>sourcePath</code> holds a <code>String</code>
     * representation of the source path for where to find Java source
     * files to compile.  The path separator character is platform dependent.
     *
     */
    private String sourcePath = null;

    /**
     * <code>classPath</code> holds a <code>String</code>
     * representation of the class path for where to find Java class
     * files during compilation.  The path separator character is
     * platform dependent.
     *
     */
    private String classPath = null;

    /**
     * <code>destinationDir</code> holds a file path that indicates
     * where the class files should be placed during compilation.
     *
     */
    private String destinationDir = null;

    /**
     * <code>exitValue</code> stores the exit value from the exec of
     * the Java compiler.  If everything is OK, then it is set to 0.
     *
     */
    private int exitValue = -1;

    /**
     * <code>commandArray</code> holds the <code>Strings</code> that
     * represent the desired command line, based on the inputs during
     * construction of this class.
     *
     */
    private String[] commandArray = null;

    /**
     * <code>out</code> holds the <code>PrintStream</code> where the
     * Java compiler's standard output should be sent to.
     * <code>Null</code> indicates quiet mode, and all standard output
     * from the compilation process will be discarded.
     *
     */
    private PrintStream out = null;

    /**
     * <code>err</code> holds the <code>PrintStream</code> where the
     * Java compiler's statndard error output should be sent to.
     * <code>Null</code> indicates quiet mode, and all standard error
     * output from the compilation process will be discarded.
     *
     */
    private PrintStream err = null;

    /**
     * Creates a new <code>JavaC</code> instance.
     *
     * @param sourceFile the file to compile
     * @param out where to send standard out to
     * @param err where to send standard err to
     * @exception IOException if an error occurs
     */
    public JavaC(String sourceFile, PrintStream out, PrintStream err) throws IOException {
        this(sourceFile, null, null, null, out, err);
    }

    /**
     * Creates a new <code>JavaC</code> instance.
     *
     * @param sourceFile the file to compile
     * @param sourcePath the path to Java source files necessary for compilation
     * @param out where to send standard out to
     * @param err where to send standard err to
     * @exception IOException if an error occurs
     */
    public JavaC(String sourceFile, String sourcePath, PrintStream out, PrintStream err) throws IOException {
        this(sourceFile, sourcePath, null, null, out, err);
    }

    /**
     * Creates a new <code>JavaC</code> instance.
     *
     * @param sourceFile the file to compile
     * @param sourcePath the path to Java source files necessary for compilation
     * @param classPath the path to Java class files necessary for compilation
     * @param out where to send standard out to
     * @param err where to send standard err to
     * @exception IOException if an error occurs
     */
    public JavaC(String sourceFile, String sourcePath, String classPath, PrintStream out, PrintStream err) throws IOException {
        this(sourceFile, sourcePath, classPath, null, out, err);
    }

    /**
     * Creates a new <code>JavaC</code> instance.
     *
     * @param sourceFile the file to compile
     * @param sourcePath the path to Java source files necessary for compilation
     * @param classPath the path to Java class files necessary for
     * compilation
     * @param destinationDir the file path to the destination
     * directory of the generated class files
     * @param out where to send standard out to
     * @param err where to send standard err to
     * @exception IOException if an error occurs
     */
    public JavaC(String sourceFile, String sourcePath, String classPath, String destinationDir, PrintStream out, PrintStream err) throws IOException {
        this.sourceFile = sourceFile;
        this.sourcePath = sourcePath;
        this.classPath = classPath;
        this.destinationDir = destinationDir;
        this.out = out;
        this.err = err;
        if (isJavaSource()) {
            String javaHome = System.getProperty("java.home");
            String javac = javaHome + File.separator + ".." + File.separator + "bin" + File.separator + "javac";
            File javacFile = new File(javac);
            File javacFileExe = new File(javac + ".exe");
            String javacCmd = "javac";
            if (javacFile.exists() || javacFileExe.exists()) {
                javacCmd = javacFile.getPath();
            }
            ArrayList al = new ArrayList();
            al.add(javacCmd);
            al.add("-g");
            if (destinationDir != null) {
                al.add("-d");
                al.add(destinationDir);
            }
            if (sourcePath != null) {
                al.add("-sourcepath");
                al.add(sourcePath);
            }
            if (classPath != null) {
                al.add("-classpath");
                al.add(classPath);
            }
            al.add(sourceFile);
            Object[] tmpobj = al.toArray();
            commandArray = new String[tmpobj.length];
            for (int i = 0; i < commandArray.length; i++) {
                commandArray[i] = (String) tmpobj[i];
            }
        } else {
            throw new IllegalArgumentException("javac can only compile files with .java extension, given: " + sourceFile);
        }
    }

    /**
     * Describe <code>isJavaSource</code> method here.
     *
     * @return a <code>boolean</code> value
     */
    private boolean isJavaSource() {
        if (sourceFile.toUpperCase().endsWith(".JAVA")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <code>compileOK</code> indicates if compilation of the supplied
     * Java file completed without errors.  The result is
     * <code>false</code> until after the <code>compile</code> method
     * has been invoked, then the result of this method reflects the
     * result of the actual compile.
     *
     * @return a <code>boolean</code> value
     */
    public boolean compileOK() {
        return (exitValue == 0);
    }

    /**
     * Returns the exit value from the most recent compile.
     *
     */
    public int exitValue() {
        return exitValue;
    }

    /**
     * <code>commandLine</code> is a cached copy of the command line
     * string for printing.
     *
     */
    private String commandLine = null;

    /**
     * <code>getCommandLine</code> generates the <code>String</code>
     * equivalent of the command line used by the exec function.
     *
     * @return the full command line
     */
    public String getCommandLine() {
        if (commandLine != null) {
            return (commandLine);
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < commandArray.length; i++) {
            if (i != 0) {
                sb.append(" ");
            }
            sb.append(commandArray[i]);
        }
        commandLine = sb.toString();
        return (commandLine);
    }

    /**
     * <code>compile</code> performs the actual Java compilation.
     *
     * @exception IOException if an error occurs
     */
    public void compile() throws IOException {
        if (!isJavaSource()) {
            return;
        }
        Process process = Runtime.getRuntime().exec(commandArray);
        Drain d1 = new Drain(process.getInputStream(), out);
        Drain d2 = new Drain(process.getErrorStream(), err);
        boolean done = false;
        while (!done) {
            try {
                this.exitValue = process.waitFor();
                d1.waitFor();
                d2.waitFor();
                done = true;
            } catch (InterruptedException ie) {
            }
        }
    }

    /**
     * Executes the compilation.  This is an alternative to {@link JavaC#compile()}
     * that executes without launching a separate JVM.  Since it depends on the
     * com.sun.* packages, it is not pure Java, portable, or supported.  However, it
     * doesn't seem to stop lots of other applications from taking advantage of it
     * (e.g., Apache JSP).  We might want to give it a try to see if it helps
     * performance.
     * <P>
     * Requires that $JAVA_HOME/lib/tools.jar be added to the classpath.
     */
    public void compileInternal() {
        if (isJavaSource()) {
            final PrintStream oldOut = System.out;
            final PrintStream oldErr = System.err;
            System.setOut(out);
            System.setErr(err);
            final String[] args = new String[commandArray.length - 1];
            System.arraycopy(commandArray, 1, args, 0, args.length);
            System.setOut(oldOut);
            System.setErr(oldErr);
        }
    }

    /**
     * <code>getOutputClass</code> returns the file path location of
     * the generated class file for the supplied source file.
     * <code>Null</code> is returned if compilation failed.
     *
     * @return the location of the generated class file.
     * @exception FileNotFoundException if the source file doesn't exist.
     */
    public String getOutputClass() throws FileNotFoundException {
        if (compileOK()) {
            if (destinationDir == null) {
                return (sourceFile.substring(0, (sourceFile.length() - ".java".length())) + ".class");
            } else {
                PackageKeywordParser sp = new PackageKeywordParser(sourceFile);
                String pckg = sp.getPackage();
                pckg = pckg.replace('.', File.separatorChar);
                File srcFile = new File(sourceFile.substring(0, (sourceFile.length() - ".java".length())) + ".class");
                File destFile = new File(destinationDir);
                File classFile = new File(destFile, pckg + File.separator + srcFile.getName());
                return (classFile.getAbsolutePath());
            }
        } else {
            return (null);
        }
    }

    /**
     * <code>StatementParser</code> is an inner helper class that
     * sorts through a Java source file and determines what package
     * the class is contained in.
     *
     */
    class PackageKeywordParser {

        /**
         * <code>st</code> holds the <code>StreamTokenizer</code> used
         * by this class to parse the Java source file.
         *
         */
        private StreamTokenizer st;

        /**
         * <code>foundOpenBrace</code> tracks internal state of the
         * Java source line parser concerning open brace characters.
         *
         */
        private boolean foundOpenBrace = false;

        /**
         * Creates a new <code>PackageKeywordParser</code> instance.
         *
         * @param fileToParse the Java source file to parse
         * @exception FileNotFoundException if an error occurs
         */
        PackageKeywordParser(String fileToParse) throws FileNotFoundException {
            st = new StreamTokenizer(new BufferedReader(new FileReader(fileToParse)));
            st.resetSyntax();
            st.slashSlashComments(true);
            st.slashStarComments(true);
            st.eolIsSignificant(false);
        }

        /**
         * <code>next</code> parses the next Java source line,
         * stopping at EOF, ;, {, or }.
         *
         * @return a <code>String</code> value
         * @exception IOException if an error occurs
         */
        private String next() throws IOException {
            String result = "";
            int ttype = 0;
            while (((ttype = st.nextToken()) != StreamTokenizer.TT_EOF) && (ttype != ';') && (ttype != '{') && (ttype != '}')) {
                result += (char) ttype;
            }
            if (ttype == '{') foundOpenBrace = true; else foundOpenBrace = false;
            return result.trim();
        }

        /**
         * @return <code>true</code> if the End Of File has been
         * encountered, <code>false</code> otherwise.
         */
        private boolean eof() {
            return (st.ttype == StreamTokenizer.TT_EOF);
        }

        /**
         * @return <code>true</code> if an open brace character has
         * been encountered.
         */
        private boolean foundOpenBrace() {
            return (foundOpenBrace);
        }

        /**
         * <code>getPackage</code> parses the Java source file and
         * returns the package the class is contained in, or an empty
         * string if there is no package declared.
         *
         * @return a <code>String</code> value
         */
        public String getPackage() {
            do {
                String statement;
                try {
                    statement = next();
                } catch (IOException ioe) {
                    return "";
                }
                if (statement.startsWith("package")) {
                    String result = statement.substring("package".length(), statement.length());
                    return result.trim();
                }
            } while (!eof() && !foundOpenBrace());
            return "";
        }
    }
}
