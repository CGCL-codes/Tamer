package java2metadpp;

import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Position.LineMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 2009 Alexis Ferreyra
 */
public class Main {

    static Arguments arguments;

    static final String DPP_EXTENSION = ".dpp";

    static final String JAVA_EXTENSION = ".java";

    public static LineMap lineMap;

    public static String currentFileName;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        arguments = ArgumentsProcessor.processArguments(args);
        if (arguments != null) {
            if (arguments.getFileNames().size() == 0) {
                printHelp();
                return;
            }
            printInit();
            for (String filename : arguments.getFileNames()) {
                processFile(filename);
            }
            printOut();
        } else {
            printHelp();
        }
    }

    /**
     * adds an error to conversion process
     * @param errorString
     */
    static void addError(String errorString) {
        addError(errorString, null);
    }

    static void addError(String errorString, JCTree tree) {
        System.out.println("Error: " + errorString);
    }

    static void addWarning(String errorString) {
        addWarning(errorString);
    }

    static void addWarning(String errorString, JCTree tree) {
        System.out.println(printLineNumber(tree) + "Warning: " + errorString);
    }

    static String printLineNumber(JCTree tree) {
        return currentFileName + "(" + Integer.toString(lineMap.getLineNumber(tree.getStartPosition())) + ") - ";
    }

    /**
     * Parse a simple java file and print as Meta D++ source
     * @param filename input filename
     */
    static void processFile(String filename) {
        FileWriter writer = null;
        try {
            JavaCompiler compiler = new JavaCompiler(new Context());
            compiler.keepComments = true;
            compiler.lineDebugInfo = true;
            JCCompilationUnit compilationUnit = compiler.parse(filename);
            lineMap = compilationUnit.getLineMap();
            currentFileName = filename;
            String outputfilename = getOutputFilename(filename);
            writer = new FileWriter(outputfilename);
            MetaDppPrinter printer = new MetaDppPrinter(writer, true, arguments);
            printer.printUnit(compilationUnit, null);
            System.out.println(filename + " -> " + outputfilename);
            writer.close();
            writer = null;
            if (arguments.getOption(Arguments.CALLMETADPPC) != null) buildMetaDpp(outputfilename);
            if (arguments.getOption(Arguments.REMOVE_DPP) != null) removeMetaDpp(outputfilename);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Get the output filename from input filename
     * @param inputFilename full path input file
     * @return full path output file
     */
    static String getOutputFilename(String inputFilename) {
        int index = inputFilename.lastIndexOf(File.separatorChar);
        if (index > 0) inputFilename = inputFilename.substring(index + 1);
        return arguments.getOutputPath() + File.separator + inputFilename.replace(JAVA_EXTENSION, "") + DPP_EXTENSION;
    }

    static void printOut() {
        System.out.println();
        System.out.println(arguments.getFileNames().size() + " files processed.");
    }

    static void printInit() {
        System.out.println("Java2MetaDpp compiler - V0.5 2009");
        System.out.println();
    }

    static void printHelp() {
        String helpStr = "Java2MetaDpp compiler" + "\n\n" + "Usage:" + "\n" + "\tJava2MetaDpp [options] [java source files]" + "\n" + "\nOptions:" + "\n" + "\t-opath:[output path]" + "\n\t\tset output path for dpp files.\n" + "\t-cdpp" + "\n\t\tcompile with metadppc.\n" + "\t-czoe" + "\n\t\tcompile files with zoec.\n" + "\t-s" + "\n\t\tsilent.\n" + "\t-dppargs:[metadppc arguments]" + "\n\t\targuments for metadppc.\n" + "\t-zoeargs:[zoec arguments]" + "\n\t\targuments for zoec.\n" + "\t-rzoe" + "\n\t\tremove meta d++ files.\n";
        System.out.println(helpStr);
    }

    private static void buildMetaDpp(String outputfilename) {
        System.out.println("Calling Meta D++ compiler on " + outputfilename + "...");
        runProcess("metadppc", new String[] { "metadppc.exe", "-s", outputfilename, arguments.getOption(Arguments.METADPPC_ARGUMENTS) });
    }

    private static void removeMetaDpp(String outputfilename) {
        File fileToDelete = new File(outputfilename);
        fileToDelete.delete();
    }

    /**
     * Run a process on the host system and prints the output to the console
     * @param processName any description for the process
     * @param args the command line to execute and arguments
     */
    private static void runProcess(String processName, String[] args) {
        try {
            if (processName == null || args == null) return;
            for (int n = 0; n < args.length; n++) if (args[n] == null) args[n] = "";
            Process proc = Runtime.getRuntime().exec(args);
            BufferedReader cleanUp = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = cleanUp.readLine()) != null) {
                System.out.println("[" + processName + "] " + line);
            }
            cleanUp.close();
            proc.waitFor();
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
