package edu.rice.cs.cunit.threadCheck;

import edu.rice.cs.cunit.instrumentors.threadCheck.AAddThreadCheckStrategy;
import edu.rice.cs.cunit.util.XMLConfig;
import edu.rice.cs.cunit.util.Debug;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Check whether XML concurrency definition files are formatted correctly.
 * @author Mathias Ricken
 */
public class XMLFileTools {

    /**
     * Options format.
     */
    private final String _optionsFormat = "[-{?|h}] [-o:<xmlfile>] [-cd] [-cp:\"<path" + File.pathSeparator + "path" + File.pathSeparator + "...>\"] <xmlfile> [<xmlfile> [<xmlfile> [...]]]";

    /**
     * XML configurations to load.
     */
    private HashSet<File> _xmlConfigs = new HashSet<File>();

    /**
     * List of class path entries.
     */
    private ArrayList<String> _classPath = new ArrayList<String>();

    /**
     * Output as concurrency definition format? Implies join.
     */
    private boolean _concDefFormat = false;

    /**
     * File name to save XML configuration with annotations to, or null if no output desired.
     */
    private String _xmlAnnotsFileName = null;

    /**
     * Static main method.
     *
     * @param args command line arguments
     */
    public static void main(String args[]) {
        (new XMLFileTools()).run(args);
    }

    /**
     * Read the command line arguments or ask for them in a dialog.
     *
     * @param args command line arguments
     */
    private void readArgs(String[] args) throws IOException {
        if (args.length == 0) {
            String cmdLine = (String) JOptionPane.showInputDialog(null, "Please enter the command line arguments to CheckXMLConcDef.\n" + _optionsFormat, "Command line: ", JOptionPane.QUESTION_MESSAGE, null, null, "");
            if (cmdLine == null) {
                System.exit(0);
            }
            List<String> list = new ArrayList<String>();
            int begin = 0;
            int end = 0;
            while (begin < cmdLine.length()) {
                while ((begin < cmdLine.length()) && (Character.isWhitespace(cmdLine.charAt(begin)))) {
                    ++begin;
                }
                if (begin >= cmdLine.length()) {
                    break;
                }
                end = begin;
                StringBuilder sb = new StringBuilder();
                boolean condition;
                boolean inQuote = cmdLine.charAt(begin) == '"';
                do {
                    if ('"' != cmdLine.charAt(end)) {
                        sb.append(cmdLine.charAt(end));
                    }
                    ++end;
                    condition = false;
                    if (end >= cmdLine.length()) {
                        break;
                    } else if (Character.isWhitespace(cmdLine.charAt(end)) && inQuote) {
                        condition = true;
                    } else if ('\\' == cmdLine.charAt(end)) {
                        sb.append(cmdLine.charAt(end));
                        ++end;
                        condition = true;
                    } else if ('"' == cmdLine.charAt(end)) {
                        ++end;
                        inQuote = !inQuote;
                        condition = true;
                    } else if (!Character.isWhitespace(cmdLine.charAt(end))) {
                        condition = true;
                    }
                } while (condition);
                list.add(sb.toString());
                begin = end + 1;
            }
            args = list.toArray(new String[] {});
        }
        int mainClassIndex = 0;
        while (args[mainClassIndex].startsWith("-")) {
            if (args[mainClassIndex].equalsIgnoreCase("-?") || args[mainClassIndex].equalsIgnoreCase("-h")) {
                help();
            } else if (args[mainClassIndex].equalsIgnoreCase("-cd")) {
                _concDefFormat = true;
            } else if (args[mainClassIndex].toLowerCase().startsWith("-cp:")) {
                String s = args[mainClassIndex].substring(4);
                for (String path : s.split(File.pathSeparator)) {
                    _classPath.add(path);
                }
            } else if (args[mainClassIndex].toLowerCase().startsWith("-o:")) {
                _xmlAnnotsFileName = args[mainClassIndex].substring(3);
            }
            ++mainClassIndex;
        }
        if (_classPath.isEmpty()) {
            _classPath.add(System.getProperty("java.home") + File.separatorChar + "lib" + File.separatorChar + "rt.jar");
            for (String path : System.getProperty("java.class.path").split(File.pathSeparator)) {
                _classPath.add(path);
            }
        }
        if (_concDefFormat && _xmlAnnotsFileName == null) {
            System.err.println("When specifying -cd, -o must also be used to specify the output file.");
            help();
        }
        for (int i = mainClassIndex; i < args.length; ++i) {
            File f = new File(args[i]);
            if (_xmlConfigs.contains(f)) {
                System.out.println("Duplicate input file: " + args[i]);
            }
            _xmlConfigs.add(f);
        }
    }

    /**
     * Display help.
     */
    private void help() {
        System.out.println("Checks, merges and converts ThreadChecker XML files.");
        System.out.println("Syntax: java edu.rice.cs.cunit.threadCheck.CheckXMLConcDef " + _optionsFormat);
        System.out.println("-{?|h}              - Display this help");
        System.out.println("-o:<xmlfile>        - Write XML file (default: by-class format))");
        System.out.println("-cd                 - Use XML concurrency definition format; requires -o)");
        System.out.println("-cp:\"path" + File.pathSeparator + "path" + File.pathSeparator + "...\" - " + File.pathSeparator + "-separated class path entries");
        System.exit(0);
    }

    /**
     * Main method.
     *
     * @param args command line arguments
     */
    private void run(String[] args) {
        try {
            readArgs(args);
            XMLAnnotUtils.ConcurrentyDefinitions defsAccum = new XMLAnnotUtils.ConcurrentyDefinitions();
            for (File f : _xmlConfigs) {
                if (!f.isFile()) {
                    System.err.println(f + " is not a file");
                    continue;
                }
                if (!f.canRead()) {
                    System.err.println(f + " is not readable");
                    continue;
                }
                XMLConfig xc;
                try {
                    xc = new XMLConfig(f);
                } catch (Exception e) {
                    System.err.println(f + " caused the following problem:");
                    e.printStackTrace(System.err);
                    continue;
                }
                if (xc == null) {
                    System.err.println(f + " could not be loaded");
                    continue;
                }
                Set<ThreadCheckException> tces = AAddThreadCheckStrategy.checkXMLConcDef(xc);
                for (ThreadCheckException e : tces) {
                    System.out.println(f + ": " + e.getMessage());
                    System.out.flush();
                }
                defsAccum = XMLAnnotUtils.joinXMLConcDefs(xc, defsAccum);
            }
            if (_xmlAnnotsFileName != null) {
                XMLConfig concDefOut;
                if (_concDefFormat) {
                    concDefOut = XMLAnnotUtils.convertConcDefsToConcDefBasedXML(defsAccum);
                } else {
                    concDefOut = XMLAnnotUtils.convertConcDefsToClassBasedXML(defsAccum);
                }
                concDefOut.save(_xmlAnnotsFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
