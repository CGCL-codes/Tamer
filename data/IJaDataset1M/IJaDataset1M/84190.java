package net.turambar.just.jpp;

import net.turambar.just.*;
import net.turambar.just.util.FileSourceReader;
import net.turambar.just.util.FileSourceWriter;
import net.turambar.just.util.FixedSourceLocator;
import java.io.*;
import java.util.*;

/** Java Preprocessor main class. Creates preprocessed versions of given source files.
 *  May be used as an executable or from an application as a facade.
 *  Creates and wires all needed processing classes: {@link net.turambar.just.SourceReader SourceReader},
 *  {@link net.turambar.just.SourceWriter SourceWriter}, {@link net.turambar.just.CommentSniffer CommentSniffer},
 *  {@link ConditionalIncluder ConditionalIncluder} and other helper classes.
 *  <p>
 *  When used from command line (or by calling <code>main()</code>, following arguments are accepted:
 *  <dl>
 *      <dt>-output, -o &lt;directory&gt; </dt><dd> Path to the directory in which the preprocessed files will be put. Required. </dd>
 *      <dt>-srcdir, -d &lt;directory&gt;</dt><dd> Path to the directory containing input files. If not specified, '.' is used. </dd>
 *      <dt>-include &lt;path&gt;</dt><dd> Semicolon separated list of directories which will be searched for include files. </dd>
 *      <dt>-exclude, -x &lt;files&gt;</dt> Space separeted list of files/directories to exclude from preprocessing. </dd>
 *      <dt>-nomsgs </dt> turns off developement messages (todos, bugs, etc.).
 *      <dt>-force</dt><dd>Preprocess input file even if target file with later modify date exists.</dd>
 *      <dt>-quiet </dt><dd> Sets log detail level to {@link Logger#LEVEL_ERROR ERROR}.</dd>
 *      <dt>-verbose</dt><dd> Sets log detail level to {@link Logger#LEVEL_TRACE TRACE}. </dd>
 *      <dt>-debug</dt><dd> Sets log detail level to {@link Logger#LEVEL_DEBUG DEBUG}. </dd>
 *      <dt> [filename]* </dt><dd> an optional list of files to preprocess. The file paths should be relative
 *          to the <tt>input</tt> directory. If no files are specified, all files in the input directory will
 *          be preprocessed, recursively.</dd>
 *  </dl>
 *  For each input file, a corresponding file is created in the output directory. The created file will have the
 *  same relative path to the output directory as the original to the input directory.
 *  <p>
 *  See this package description for available directives.
 *  @author Marcin Moscicki
 */
public class JPP {

    /** The name of resource bundle containing additional macro definitions. {@value}*/
    public static final String DEFINE_RESOURCE_BUNDLE = "define";

    private Logger logger;

    private String encoding;

    private String[] includePaths;

    private boolean messagesEnabled = false;

    private boolean preserveLineNumbering = false;

    private String defineBundleFile;

    private List macros;

    private CommentSnifferFactory commentSnifferFactory = null;

    private static void printUsage() {
        System.err.println("Usage: jpp [<options>] [<source files>] [-x <files>]");
        System.err.println("Options are:");
        System.err.println("\t-output, -o <directory>\t where to place preprocessed files.");
        System.err.println("\t\t\t\tIf not specified, no files will be generated!");
        System.err.println();
        System.err.println("\t-srcdir, -d <directory>\t the base directory of input package structure. " + "\t\t\t\t Assumes current directory if not specified.");
        System.err.println();
        System.err.println("\t-includepath, -i <path>\t directories wich will be searched");
        System.err.println("\t\t\t\t for files included from processed files, separated with a ';'.");
        System.err.println("\t\t\t\t Defaults to \".\"");
        System.err.println("\t-define <fileName> read definitions from the specified properties file.");
        System.err.println("\t-exclude, -x <files>\t list of files/directories excluded from the processing.");
        System.err.println("\t\t\t\t May only occur as the last parameter.");
        System.err.println();
        System.err.println("\t-nomsgs\t\t turns off issue messages (todos, bugs, etc.). [default]");
        System.err.println("\t-msgs\t\t turns on issue messages (todos, bugs, etc.).");
        System.err.println("\t-force\t\t preprocess files even if target file with later modify date exists.");
        System.err.println("\t-preserve-lines, -ln\t makes the generated files preserve line numbering of original files");
        System.err.println("\t-quiet\t\t prints no warnings.");
        System.err.println("\t-verbose\t prints information about preprocessed files.");
        System.err.println("\t-debug\t\t prints debug information.");
        System.err.println("\t-help, --help, -h\t shows this info.");
        System.err.println();
        System.err.println("Source files should be represented by paths relative to the input directory");
        System.err.println("and mirroring the package structure. If no source files are specified, ");
        System.err.println("all files in the input directory will be processed.");
        System.err.println();
    }

    private List createRelativeFileList(String baseDir, String[] includes, String[] excludes) {
        if (baseDir == null || baseDir.length() == 0) {
            baseDir = System.getProperty("user.dir");
        }
        File base = new File(baseDir);
        if (!base.isAbsolute()) {
            try {
                baseDir = base.getCanonicalPath();
                base = new File(baseDir);
            } catch (Exception e) {
                logger.error("failed to resolve base directory: " + baseDir, e);
            }
        }
        Set included = new HashSet(includes.length);
        if (includes != null) {
            for (int i = 0; i < includes.length; i++) {
                if (includes[i] != null && includes[i].length() > 0) {
                    File include = new File(includes[i]);
                    try {
                        if (include.isAbsolute()) {
                            include = include.getCanonicalFile();
                            if (!include.getPath().startsWith(baseDir)) {
                                logger.error("absolute input file " + includes[i] + " not in source directory: " + baseDir + "; ignoring.");
                                continue;
                            }
                        } else {
                            include = new File(baseDir + File.separatorChar + includes[i]).getCanonicalFile();
                        }
                        included.add(include);
                    } catch (IOException e) {
                        logger.error("failed to resolve input file " + include, e);
                    }
                }
            }
        }
        if (included.isEmpty()) {
            included.add(base);
        }
        Set excluded = new HashSet(excludes == null ? 0 : excludes.length);
        if (excludes != null) {
            for (int i = 0; i < excludes.length; i++) {
                if (excludes[i] == null || excludes[i].length() == 0) continue;
                File exclude = new File(excludes[i]);
                try {
                    if (exclude.isAbsolute()) {
                        exclude = exclude.getCanonicalFile();
                        if (!exclude.getPath().startsWith(baseDir)) {
                            logger.warn("absolute exclude file " + excludes[i] + " not in source directory: " + baseDir + "; ignoring.");
                            continue;
                        }
                    } else {
                        exclude = new File(baseDir + File.separatorChar + excludes[i]).getCanonicalFile();
                    }
                    excluded.add(exclude);
                } catch (IOException e) {
                    logger.warn("exclude file not found: " + excludes[i]);
                }
            }
        }
        ArrayList res = new ArrayList();
        for (Iterator iterator = included.iterator(); iterator.hasNext(); ) {
            File file = (File) iterator.next();
            createRelativeFileList(base, file, excluded, res);
        }
        return res;
    }

    private void createRelativeFileList(File baseDir, File currFile, Collection excludes, List res) {
        try {
            if (excludes.contains(currFile.getCanonicalFile())) return;
            if (currFile.isFile()) {
                res.add(currFile.getPath().substring(baseDir.getPath().length()));
            } else if (currFile.isDirectory()) {
                File[] files = currFile.listFiles();
                for (int i = 0; i < files.length; ++i) createRelativeFileList(baseDir, files[i], excludes, res);
            }
        } catch (IOException e) {
            logger.error("file not found: " + currFile, e);
        }
    }

    /** Runs the preprocessor.
     *  See {@linkplain JPP class description} for details.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        JPP jpp = new JPP();
        jpp.setLogger(new Logger());
        boolean parseMsgs = false;
        if (args.length == 0) {
            jpp.logger.info("No options specified. Parsing all files in current directory for issue messages.");
            jpp.logger.info("Is that what you want? Run with -help for parameters description.");
            parseMsgs = true;
        }
        try {
            String outputDir = null;
            String inputDir = null;
            int shift = 0;
            String includePath = System.getProperty("user.dir");
            boolean force = false;
            String excludes[] = null;
            while (shift < args.length && args[shift].startsWith("-")) {
                if (args[shift].equals("--help") || args[shift].equals("-help") || args[shift].equals("-h")) {
                    printUsage();
                    return;
                } else if (args[shift].equals("-o") || args[shift].equals("-output")) {
                    if (shift == args.length - 1) {
                        printUsage();
                        return;
                    }
                    outputDir = args[shift + 1];
                    shift += 2;
                } else if (args[shift].equals("-d") || args[shift].equals("-srcdir")) {
                    if (shift == args.length - 1) {
                        printUsage();
                        return;
                    }
                    inputDir = args[shift + 1];
                    shift += 2;
                } else if (args[shift].equals("-force")) {
                    force = true;
                    ++shift;
                } else if (args[shift].equals("-verbose")) {
                    jpp.logger.setLevel(Logger.LEVEL_TRACE);
                    ++shift;
                } else if (args[shift].equals("-quiet")) {
                    jpp.logger.setLevel(Logger.LEVEL_ERROR);
                    ++shift;
                } else if (args[shift].equals("-debug")) {
                    jpp.logger.setLevel(Logger.LEVEL_DEBUG);
                    ++shift;
                } else if (args[shift].equals("-includepath") || args[shift].equals("-i")) {
                    includePath = args[shift + 1];
                    shift += 2;
                } else if (args[shift].equals("-exclude") || args[shift].equals("-x")) {
                    excludes = new String[args.length - shift - 1];
                    ++shift;
                    for (int i = 0; i < excludes.length; ++i, ++shift) excludes[i] = args[shift];
                } else if (args[shift].equals("-define")) {
                    String fileName = args[++shift];
                    jpp.setDefineBundleFile(fileName);
                    ++shift;
                } else if (args[shift].equals("-nomsgs")) {
                    parseMsgs = false;
                    ++shift;
                } else if (args[shift].equals("-msgs")) {
                    parseMsgs = true;
                    ++shift;
                } else if (args[shift].equals("-ln") || args[shift].equals("-preserve-lines")) {
                    jpp.setPreserveLineNumbering(true);
                    ++shift;
                }
            }
            if (outputDir == null) {
                jpp.logger.info("No output directory specified. Searching input files for tags with no output generation.");
            }
            jpp.setIncludePath(includePath);
            String[] files = null;
            jpp.setMessagesEnabled(parseMsgs);
            int count = shift;
            for (; count < args.length && !args[count].startsWith("-"); ++count) ;
            if (count > shift) {
                files = new String[count - shift];
                for (int j = 0; j < files.length; ++j) {
                    files[j] = args[j + shift];
                }
            }
            if (count < args.length) {
                if (!(args[count].equals("-x") || args[count].equals("-exclude"))) throw new IllegalArgumentException("option " + args[count] + " is not allowed here.");
                excludes = new String[args.length - count - 1];
                ++count;
                for (int j = 0; j < excludes.length; ++j, ++count) excludes[j] = args[count];
            }
            jpp.process(files, excludes, inputDir, outputDir, force);
        } catch (Exception e) {
            jpp.getLogger().fatal("Failed to preprocess the files", e);
        }
    }

    /** Specifies if developement messages - todo, fixme, bug and so on - will be printed on the output. */
    public void setMessagesEnabled(boolean parseMsgs) {
        messagesEnabled = parseMsgs;
    }

    /** Checks if developement messages get printed. */
    public boolean isMessagesEnabled() {
        return messagesEnabled;
    }

    /** Sets this instance include path.
     *  The include path specifies the directories which will be searched
     *  for files included with <tt>#include</tt> instruction.
     *  All '/' and '\' characters are changed to system file separator character.
     * @param includePath paths to include directories separated with ';'.
     * @see #setIncludePath(String[])
     */
    public void setIncludePath(String includePath) {
        ArrayList paths = new ArrayList();
        int start = 0;
        int end;
        while ((end = includePath.indexOf(';', start)) > 0) {
            String p = includePath.substring(start, end).trim();
            if (p.length() > 0) paths.add(FileIncludeLocator.convertedPath(p));
            start = end + 1;
        }
        String p = includePath.substring(start).trim();
        if (p.length() > 0) paths.add(FileIncludeLocator.convertedPath(p));
        includePaths = new String[paths.size()];
        paths.toArray(includePaths);
    }

    /** Sets this instance include path.
     *  The include path specifies the directories which will be searched
     *  for files included with <tt>#include</tt> instruction.
     *  The paths have to be system specific and use an appropriate separator character.
     * @param includePath an array of paths to include directories.
     * @see #setIncludePath(String)
     */
    public void setIncludePath(String[] includePath) {
        includePaths = includePath;
    }

    /** Sets the encoding in which input files are assumed to be; generated files will use the same encoding */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /** Creates an instance using <code>Logger</code> obtained by {@link Logger#getInstance Logger.getInstance()}. */
    public JPP() {
        logger = Logger.getInstance();
        includePaths = new String[0];
        macros = new ArrayList();
    }

    /** Sets the logger which will be used during next call to {@link #process process()}. */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /** Returns the logger used to print diagnostic output. */
    public Logger getLogger() {
        return logger;
    }

    /** Sets the file from which defines will be read. */
    public void setDefineBundleFile(String defineBundleFile) {
        this.defineBundleFile = defineBundleFile;
    }

    public void define(String name, String value) {
        macros.add(new Macro(name, value));
    }

    /** If this flag is set, preprocessed files will preserve the line numbering of the original files.
     *  Lines excluded by the preprocessor will be replaced by empty lines. This is very helpfull
     *  in debuging the preprocessed code. This feature is turned off by default.
     */
    public void setPreserveLineNumbering(boolean preserveLineNumbering) {
        this.preserveLineNumbering = preserveLineNumbering;
    }

    /** Checks if output files' lines have the same numbers as in the input files.*/
    public boolean getPreserveLineNumbering(boolean preserveLineNumbering) {
        return preserveLineNumbering;
    }

    /** Sets the <code>CommentSnifferFactory</code> which will be used by this instance to
     *  create comment sniffers for particular files. You may provide your own factory, or the default
     *  factory with registered other file types.
     * @param factory a factory of comment sniffers.
     */
    public void setCommentSnifferFactory(CommentSnifferFactory factory) {
        this.commentSnifferFactory = factory;
    }

    /** Creates preprocessed versions of given files.
     *  Output files' names are created by preceding input files' names with output directory.
     *
     * @param inputFiles list of files to preprocess. If null, all files in <code>inputDir</code> directory
     *  are preprocessed. Paths should be relative to the <code>inputDir</code> directory
     * @param inputDir base directory for the <code>inputFiles</code>. If null, current directory is assumed.
     * @param outputDir directory where to place preprocessed files. If null, no output files will be generated!
     * @param force if an output file should be replaced even if it's newer then its input file.
     */
    public void process(String[] inputFiles, String[] excludeFiles, String inputDir, String outputDir, boolean force) {
        List relativeNames = createRelativeFileList(inputDir, inputFiles, excludeFiles);
        inputFiles = (String[]) relativeNames.toArray(new String[relativeNames.size()]);
        File[] input = new File[inputFiles.length];
        File[] output = new File[inputFiles.length];
        for (int i = 0; i < inputFiles.length; ++i) {
            input[i] = new File(inputDir + File.separatorChar + inputFiles[i]).getAbsoluteFile();
            if (outputDir != null) output[i] = new File(outputDir + File.separatorChar + inputFiles[i]).getAbsoluteFile();
        }
        process(input, output, force);
    }

    public void process(File[] inputFiles, File[] outputFiles, boolean force) {
        FileSourceReader root = new FileSourceReader();
        root.setLogger(logger);
        FileSourceWriter writer = new FileSourceWriter();
        if (commentSnifferFactory == null) commentSnifferFactory = new CommentSnifferFactory(logger);
        IssueInspector todoFinder = null;
        if (messagesEnabled) todoFinder = new IssueInspector(logger);
        DefinitionEnvironment defines = createDefinitionEnvironment();
        int processed = 0;
        for (int i = 0; i < inputFiles.length; ++i) {
            try {
                ConditionalIncluder preprocessor = createPreprocessor(defines);
                preprocessor.setOutput(writer);
                CommentSniffer cp = commentSnifferFactory.getSnifferForFile(inputFiles[i].getName());
                File input = inputFiles[i];
                boolean process = true;
                if (outputFiles[i] != null) {
                    File output = null;
                    output = outputFiles[i];
                    if (!force && output.exists() && output.lastModified() > input.lastModified()) continue;
                    logger.trace(inputFiles[i] + " -> " + outputFiles[i]);
                    writer.setFile(output, encoding);
                    if (cp != null) {
                        preprocessor.setSource(cp);
                        root.setOutput(cp);
                    } else {
                        logger.info("Unknown file type: " + inputFiles[i] + ", copying without modification.");
                        process = false;
                        copyFile(input, output);
                    }
                } else {
                    if (cp != null) {
                        logger.trace("processing " + inputFiles[i]);
                        cp.setOutput(null);
                        root.setOutput(cp);
                    } else {
                        logger.info("Unknown file type: " + inputFiles[i] + ", ignoring.");
                        continue;
                    }
                }
                if (process) {
                    if (cp != null && todoFinder != null) todoFinder.registerTo(cp);
                    root.setFile(input, encoding);
                    root.read();
                    if (writer != null) writer.close();
                }
                ++processed;
            } catch (IOException e) {
                logger.error(e);
                try {
                    writer.close();
                } catch (IOException ioe) {
                }
            } catch (JUSTException e) {
                logger.error("Failed to preprocess file '" + inputFiles[i] + "'.", e);
                try {
                    writer.close();
                } catch (IOException ioe) {
                }
            }
        }
        logger.info("processed successfully " + processed + " of " + inputFiles.length + " files");
    }

    private void copyFile(File input, File output) throws IOException {
        InputStream is = new FileInputStream(input);
        File parent = output.getParentFile();
        if (parent != null) parent.mkdirs();
        OutputStream os = new FileOutputStream(output);
        byte[] buffer = new byte[8096];
        int n;
        while ((n = is.read(buffer)) > 0) {
            os.write(buffer, 0, n);
        }
        os.close();
        is.close();
    }

    private ConditionalIncluder createPreprocessor(DefinitionEnvironment defines) {
        ConditionalIncluder preprocessor = new ConditionalIncluder(defines, logger);
        preprocessor.setIncludeLocator(new FileIncludeLocator(includePaths));
        preprocessor.setSourceLocator(null);
        preprocessor.setLineNumberingPreserved(preserveLineNumbering);
        return preprocessor;
    }

    private DefinitionEnvironment createDefinitionEnvironment() {
        ResourceBundle defineBundle = null;
        SourceLocator sourceLocator = null;
        if (defineBundleFile != null) {
            try {
                defineBundle = new PropertyResourceBundle(new FileInputStream(defineBundleFile));
                sourceLocator = new FixedSourceLocator(defineBundleFile + " bundle");
            } catch (IOException e) {
                logger.error("Could not read properties from the specified file " + defineBundleFile, e);
            }
        } else {
            try {
                defineBundle = ResourceBundle.getBundle(DEFINE_RESOURCE_BUNDLE);
                sourceLocator = new FixedSourceLocator(DEFINE_RESOURCE_BUNDLE + " bundle");
            } catch (MissingResourceException e) {
                logger.info("No \"" + DEFINE_RESOURCE_BUNDLE + "\" resource bundle.");
            }
        }
        DefinitionEnvironment defines = new DefinitionEnvironment();
        if (defineBundle != null) {
            for (Enumeration e = defineBundle.getKeys(); e.hasMoreElements(); ) {
                String property = (String) e.nextElement();
                String value = defineBundle.getString(property);
                if (value != null && value.trim().length() != 0) {
                    defines.define(property, value);
                    logger.debug(sourceLocator, "define " + property + "=" + value);
                } else {
                    defines.define(property);
                    logger.debug(sourceLocator, "define " + property);
                }
            }
        }
        sourceLocator = new FixedSourceLocator("<external definition>");
        for (Iterator iterator = macros.iterator(); iterator.hasNext(); ) {
            Macro macro = (Macro) iterator.next();
            defines.define(macro);
            logger.debug(sourceLocator, "define " + macro);
        }
        return defines;
    }
}
