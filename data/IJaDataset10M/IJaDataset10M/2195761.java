package org.sf.pmmda.development.utilities;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ch.bbv.cmdlineparser.CmdLineBuilder;

/**
 * The jar file checker provides a set of functions to analyze a set of jar
 * files and discover duplicate class versions. A command file interface is
 * defined to control the behavior of the program.
 * <dl>
 * <dt>-m --multiple</dt>
 * <dd>list all class files with multiple instances in the set of jar files.
 * </dd>
 * <dt>-l --list</dt>
 * <dd>list all class files in the set of jar files.</dd>
 * <dt>-f --files</dt>
 * <dd>interpret all arguments after this option as the list of jar files to
 * parse.</dd>
 * <dt>-d --directory</dt>
 * <dd>interpret all arguments after this option as the list of directory to
 * traverse and parse the jar files stored in them.</dd>
 * </dl>
 * @author Marcel Baumann
 * @version $Revision: 1.1 $
 */
public class JarFileChecker {

    /**
   * Logger for all instances of the class.
   */
    private static Log log = LogFactory.getLog(JarFileChecker.class);

    /**
   * The structure tracking which class files are stored in which archive files.
   */
    private Map<String, Map<String, String>> classfiles;

    /**
   * Main function of the class and entry point of the checker. The command line
   * is parsed and the requested actions are executed.
   * @param args command line arguments of the checker.
   */
    public static void main(String[] args) {
        JarFileChecker checker = new JarFileChecker();
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        options.addOption(CmdLineBuilder.createNoArgOption("h", "help", "display the list of available commands", false));
        group.addOption(CmdLineBuilder.createNoArgOption("v", "verbose", "set the logging level to verbose", false));
        group.addOption(CmdLineBuilder.createNoArgOption("q", "quiet", "set the logging level to quiet", false));
        options.addOptionGroup(group);
        options.addOption(CmdLineBuilder.createNoArgOption("m", "multiple", "detects multiple classfiles in the set of jar files", false));
        options.addOption(CmdLineBuilder.createNoArgOption("l", "list", "provides list of classfiles in the set of jar files", false));
        options.addOption(CmdLineBuilder.createArgsOption("f", "file", "jar or zip file to parse", "file", false, String.class));
        options.addOption(CmdLineBuilder.createArgsOption("d", "directory", "directory to parse", "directory", false, String.class));
        options.addOption(CmdLineBuilder.createJavaProperty());
        Parser parser = new PosixParser();
        try {
            CommandLine line = parser.parse(options, args, true);
            if (line.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Jar File Checker", options);
                return;
            }
            if (line.hasOption("v")) {
                log.info("logging level set to verbose");
            }
            if (line.hasOption("q")) {
                log.info("logging level set to quiet");
            }
            checker.parseJarFiles(args);
            checker.printDuplicates();
        } catch (ParseException e) {
            log.fatal(e);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
   * Default constructor of the class.
   */
    public JarFileChecker() {
        classfiles = new HashMap<String, Map<String, String>>();
    }

    /**
   * Resets the jar file checker.
   */
    public void reset() {
        classfiles.clear();
    }

    /**
   * Parses recursively a set of directories.
   * @param directories list of directory full names to parse.
   * @throws IOException if an error occured when reading a file.
   * @see #parseDirectory(String)
   */
    public void parseDirectories(String[] directories) throws IOException {
        for (int i = 0; i < directories.length; i++) {
            parseDirectory(directories[i]);
        }
    }

    /**
   * Parses recursively a directory and checks all contained jar and zip files
   * discovered to detect multiple instances of class files. The supported
   * extensions are the same as the ones supported in the Java virtual machine.
   * @param root root directory to check.
   * @throws IOException if an error occured when reading a file.
   */
    public void parseDirectory(String root) throws IOException {
        File dir = new File(root);
        if (dir.isDirectory()) {
            String[] filenames = dir.list();
            for (int i = 0; i < filenames.length; i++) {
                File file = new File(filenames[i]);
                if (file.isDirectory()) {
                    parseDirectory(filenames[i]);
                } else if (file.isFile() && (filenames[i].endsWith(".jar") || filenames[i].endsWith(".zip"))) {
                    parseJarFile(filenames[i]);
                }
            }
        }
    }

    /**
   * Parses a set of jar and zip files and process the class file inside.
   * @param filenames names of the files to process.
   * @throws IOException if an error occured when reading a file.
   * @see #parseJarFile(String)
   */
    public void parseJarFiles(String[] filenames) throws IOException {
        for (int i = 0; i < filenames.length; i++) {
            parseJarFile(filenames[i]);
        }
    }

    public void parseJarFile(String filename) throws IOException {
        File file = new File(filename);
        if (file.exists() && file.canRead()) {
            JarFile jarFile = new JarFile(file);
            Enumeration entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                addEntry((JarEntry) entries.nextElement(), filename);
            }
        } else {
            System.err.println("Cannot access jar file " + filename);
        }
    }

    /**
   * Prints a list of all found class names. The class names are sorted before
   * being printed.
   */
    public void printClassnames() {
        List<String> classnames = new ArrayList<String>(classfiles.keySet());
        Collections.sort(classnames);
        for (String classname : classnames) {
            System.out.println(classname);
        }
    }

    /**
   * Prints a list of all found class duplicates with the format qualified class
   * name and list of jar files containing it. The class names are sorted before
   * being printed.
   */
    public void printDuplicates() {
        List<String> classnames = new ArrayList<String>(classfiles.keySet());
        Collections.sort(classnames);
        for (Iterator i = classnames.iterator(); i.hasNext(); ) {
            String name = (String) i.next();
            Map<String, String> jarfiles = classfiles.get(name);
            if (jarfiles.size() > 1) {
                System.out.println("Duplicate class " + name);
                for (String classname : jarfiles.keySet()) {
                    System.out.println("\t" + classname);
                }
            }
        }
    }

    /**
   * Adds the class name and the associated jar file name as entry to the
   * checker. Only class name are tracked.
   * @param entry name of the jar entry to check.
   * @param filename name of the jar file containing the entry.
   */
    private void addEntry(JarEntry entry, String filename) {
        String name = entry.getName();
        if (name.endsWith(".class")) {
            if (!classfiles.containsKey(name)) {
                classfiles.put(name, new HashMap<String, String>());
            }
            Map<String, String> jarfiles = classfiles.get(name);
            jarfiles.put(filename, filename);
        }
    }
}
