package net.sf.javaguard;

import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import net.sf.javaguard.classfile.*;
import net.sf.javaguard.log.*;
import org.apache.oro.text.regex.MalformedPatternException;

/** Classfile database for obfuscation.
 *
 * @author <a href="mailto:theit@gmx.de">Thorsten Heit</a>
 * @author <a href="mailto:markw@retrologic.com">Mark Welsh</a>
 */
public class GuardDB implements ClassConstants {

    /** The file name of the manifest file. */
    private static final String STREAM_NAME_MANIFEST = "META-INF/MANIFEST.MF";

    /** Holds a list of input Jar files to obfuscate. */
    private Vector inputJars = null;

    /** Holds a list of input directories with files to obfuscate. The vector
   * must have exact the same number of elements as the {@link #inputFileFilters}
   * vector.
   */
    private Vector inputDirs = null;

    /** Holds a list of file filter expressions for the input directories. The
   * vector must have exact the same number of elements as the {@link #inputDirs}
   * vector.
   */
    private Vector inputFileFilters = null;

    /** Holds a list of output containers that each manage the output name of
   * the obfuscated Jar file and the file containers that are used as input for
   * the obfuscator.
   */
    private Vector outputContainers = null;

    /** Holds the output file. */
    private File outputFile;

    /** Holds script file to use for the obfuscation. */
    private ScriptFile scriptFile;

    /** Holds the package, class, method and field mapping entries from the
   * script file. */
    private Vector mappings = null;

    /** Holds the log file to use. */
    private FileLogger logfile;

    /** Holds the system logger. */
    private ScreenLogger logger;

    /** Default constructor. Builds a classfile database for obfuscation.
   */
    public GuardDB() {
        logfile = FileLogger.getInstance();
        logger = ScreenLogger.getInstance();
        setScriptFile(null);
    }

    /** Stores the script file to use for the obfuscation.
   * @param scriptFile the script file to use for the obfuscation
   */
    public void setScriptFile(ScriptFile scriptFile) {
        this.scriptFile = scriptFile;
    }

    /** Returns the script file to use for the obfuscation.
   * @return script file; may be null
   * @see #setScriptFile
   */
    private ScriptFile getScriptFile() {
        return scriptFile;
    }

    /** Sets the list of input Jar files and directories files to obfuscate.
   * @param jars a vector that holds the list of input Jar files to obfuscate
   * @param dirs a vector that holds a list of input directories; must have the
   * same size number of elements as the file filter vector
   * @param filters a vector that holds a list of file filter expressions; must
   * have the same number of elements as the directories vector
   * @throws IllegalArgumentException if the directory and file filter vectors
   * don't have the same size
   */
    public void setInput(Vector jars, Vector dirs, Vector filters) throws IllegalArgumentException {
        inputJars = jars;
        if ((null == dirs && null != filters) || (null != dirs && null == filters) || (null != dirs && null != filters && dirs.size() != filters.size())) {
            throw new IllegalArgumentException("Directory and file filter vectors must have the same size");
        }
        inputDirs = dirs;
        inputFileFilters = filters;
    }

    /** Returns the list of input Jar files to obfuscate.
   * @return list of input Jar files; always a valid vector (may be empty)
   * @see #setInput
   */
    private Vector getInputJars() {
        if (null == inputJars) {
            inputJars = new Vector();
        }
        return inputJars;
    }

    /** Returns the list of input directories that contain files to obfuscate.
   * @return list of directories; always a valid vector (may be empty)
   * @see #setInput
   */
    private Vector getInputDirs() {
        if (null == inputDirs) {
            inputDirs = new Vector();
        }
        return inputDirs;
    }

    /** Returns the list of file filter expressions for the input directories.
   * @return list of file filter expressions; always a valid vector (may be
   * empty)
   * @see #setInput
   */
    private Vector getInputFileFilters() {
        if (null == inputFileFilters) {
            inputFileFilters = new Vector();
        }
        return inputFileFilters;
    }

    /** Sets the output Jar file.
   * @param file the output Jar file; must not be null
   */
    public void setOutputFile(File file) {
        outputFile = file;
    }

    /** Returns the output Jar file.
   * @return output Jar file; may be null
   * @see #setOutputFile
   */
    private File getOutputFile() {
        return outputFile;
    }

    /** Reads all class files from the Jar files and the local directories and
   * builds the classfile database.
   * @param dump true if the parsed class tree should be dumped to the console
   * before it is obfuscated; false else
   * @throws IOException if an I/O error occurs
   * @throws MalformedPatternException if an error occurs during the compilation
   * of regular expressions
   */
    public void obfuscate(boolean dump) throws IOException, MalformedPatternException {
        logger.log("Building the file containers...");
        OutputContainer oc = new OutputContainer(getOutputFile());
        Vector jars = getInputJars();
        for (int i = 0; i < jars.size(); i++) {
            FileContainer fc = new JarFileContainer(new JarFile((File) jars.elementAt(i)));
            oc.addFileContainer(fc);
        }
        Vector dirs = getInputDirs();
        Vector fileFilters = getInputFileFilters();
        for (int i = 0; i < dirs.size(); i++) {
            FileContainer fc = new LocalDirectoryFileContainer((File) dirs.elementAt(i), (String) fileFilters.elementAt(i));
            oc.addFileContainer(fc);
        }
        Vector vec = oc.getFileContainers();
        for (int i = 0; i < vec.size(); i++) {
            FileContainer fc = (FileContainer) vec.elementAt(i);
            for (int j = i + 1; j < vec.size(); j++) {
                FileContainer other = (FileContainer) vec.elementAt(j);
                other.removeDuplicates(fc);
            }
        }
        addOutputContainer(oc);
        startObfuscate(getOutputContainers(), dump);
    }

    /** Starts the obfuscation process for the given list of output containers.
   * @param vec a vector that holds output containers; may not be null
   * @param dump true if the parsed class tree should be dumped to the console
   * before it is obfuscated; false else
   * @throws IOException if an I/O error occurs
   * @throws MalformedPatternException if an error occurs during the compilation
   * of regular expressions
   */
    private void startObfuscate(Vector vec, boolean dump) throws IOException, MalformedPatternException {
        Iterator iter = vec.iterator();
        while (iter.hasNext()) {
            ClassTree classTree = new ClassTree();
            ClassFile.resetDangerHeader();
            parseScriptFile(classTree);
            OutputContainer oc = (OutputContainer) iter.next();
            Vector fileContainers = oc.getFileContainers();
            logger.log("Building the class tree...");
            for (int j = 0; j < fileContainers.size(); j++) {
                FileContainer fc = (FileContainer) fileContainers.elementAt(j);
                logger.log(Log.INFO, "Reading the contents from '" + fc.getName() + "'");
                addClasses(classTree, fc, dump);
            }
            writeLogHeader(getOutputFile().getName(), fileContainers);
            generateMappingTable(classTree);
            writeMappingTable(classTree);
            JarOutputStream jos = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(oc.getOutputFile())));
            jos.setComment(Version.getJarComment());
            for (int j = 0; j < fileContainers.size(); j++) {
                FileContainer fc = (FileContainer) fileContainers.elementAt(j);
                obfuscateFiles(jos, classTree, fc, oc.getManifestContainer());
            }
            JarEntry outEntry = new JarEntry(STREAM_NAME_MANIFEST);
            jos.putNextEntry(outEntry);
            DataOutputStream manifest = new DataOutputStream(new BufferedOutputStream(jos));
            oc.getManifestContainer().write(manifest);
            jos.closeEntry();
            jos.close();
            logfile.printMethodWarnings();
            logfile.printWarnings();
        }
    }

    /** Go through database marking certain entities for retention, while
   * maintaining polymorphic integrity.
   * @param classTree the class tree to which the script file entries are
   * applied
   * @throws MalformedPatternException if an error occurs during the compilation
   * of regular expressions
   * @throws IllegalArgumentException if a script entry contains an illegal type
   */
    public void parseScriptFile(ClassTree classTree) throws MalformedPatternException, IllegalArgumentException {
        if (null != getScriptFile()) {
            Iterator iterator = getScriptFile().iterator();
            while (iterator.hasNext()) {
                ScriptEntry entry = (ScriptEntry) iterator.next();
                switch(entry.getType()) {
                    case ScriptConstants.TYPE_ATTRIBUTE:
                    case ScriptConstants.TYPE_RENAME:
                    case ScriptConstants.TYPE_PRESERVE:
                        classTree.retainAttribute(entry);
                        break;
                    case ScriptConstants.TYPE_PACKAGE:
                    case ScriptConstants.TYPE_PACKAGE_MAP:
                    case ScriptConstants.TYPE_CLASS:
                    case ScriptConstants.TYPE_CLASS_MAP:
                    case ScriptConstants.TYPE_METHOD:
                    case ScriptConstants.TYPE_METHOD_MAP:
                    case ScriptConstants.TYPE_FIELD:
                    case ScriptConstants.TYPE_FIELD_MAP:
                        addMapping(entry);
                        break;
                    case ScriptConstants.TYPE_IGNORE:
                        classTree.addIgnoreDefaultRegex(entry.getName());
                        break;
                    case ScriptConstants.TYPE_IGNORE_PACKAGE:
                        classTree.addIgnorePackageRegex(entry.getName());
                        break;
                    case ScriptConstants.TYPE_IGNORE_CLASS:
                        classTree.addIgnoreClassRegex(entry.getName());
                        break;
                    case ScriptConstants.TYPE_IGNORE_METHOD:
                        classTree.addIgnoreMethodRegex(entry);
                        break;
                    case ScriptConstants.TYPE_IGNORE_FIELD:
                        classTree.addIgnoreFieldRegex(entry);
                        break;
                    case ScriptConstants.TYPE_OBFUSCATE:
                        classTree.addObfuscateDefaultRegex(entry.getName());
                        break;
                    case ScriptConstants.TYPE_OBFUSCATE_PACKAGE:
                        classTree.addObfuscatePackageRegex(entry.getName());
                        break;
                    case ScriptConstants.TYPE_OBFUSCATE_CLASS:
                        classTree.addObfuscateClassRegex(entry.getName());
                        break;
                    case ScriptConstants.TYPE_OBFUSCATE_METHOD:
                        classTree.addObfuscateMethodRegex(entry);
                        break;
                    case ScriptConstants.TYPE_OBFUSCATE_FIELD:
                        classTree.addObfuscateFieldRegex(entry);
                        break;
                    default:
                        throw new IllegalArgumentException("Illegal type in script file");
                }
            }
        }
    }

    /** Adds the files from the given file container to the internal class
   * database.
   * @param classTree the class tree to which the available classes from the
   * file container may be added
   * @param fc the file container
   * @param dump true if the parsed class tree should be dumped to the console
   */
    private void addClasses(ClassTree classTree, FileContainer fc, boolean dump) {
        Enumeration enumeration = fc.enumeration();
        while (enumeration.hasMoreElements()) {
            FileEntry entry = (FileEntry) enumeration.nextElement();
            if (entry.isClassFile()) {
                DataInputStream dis = entry.getInputStream();
                ClassFile cf = null;
                try {
                    cf = ClassFile.create(classTree, dis);
                } catch (IOException ioex) {
                    logger.println("Error: " + ioex.getMessage());
                    logger.printStackTrace(ioex);
                    logfile.println("# ERROR - corrupt class file: " + entry.getName());
                    logfile.printStackTrace(ioex);
                } finally {
                    try {
                        dis.close();
                    } catch (IOException ioex) {
                    }
                }
                cf.logDangerousMethods();
                classTree.addClassFile(cf);
                if (dump) {
                    PrintWriter pw = new PrintWriter(System.out);
                    cf.dump(pw);
                }
            }
        }
    }

    /** Generates the mapping table for the obfuscation step.
   * @param classTree the class tree for which the mapping table should be
   * generated
   * @throws MalformedPatternException If the compiled expression does not
   * conform to the grammar understood by the PatternCompiler or if some other
   * error in the expression is encountered.
   */
    private void generateMappingTable(ClassTree classTree) throws MalformedPatternException {
        logger.log("Generating the mapping table for the whole class tree...");
        classTree.parseObfuscateAndIgnoreList();
        classTree.markRemoteClasses();
        classTree.retainMappings(getMappings());
        classTree.retainHardcodedReferences();
        classTree.retainSerializableElements();
        classTree.generateNames();
        classTree.retainRemoteClasses();
        classTree.resolveClasses();
    }

    /** Obfuscate all entries in the file container.
   * @param jos the Jar file output stream
   * @param classTree the class tree the file container belongs to
   * @param fileContainer the file container that holds all input files
   * @param manifestContainer the manifest container that manages the Manifest
   * file
   * @throws IOException if an I/O error occurs
   */
    private void obfuscateFiles(JarOutputStream jos, ClassTree classTree, FileContainer fileContainer, ManifestContainer manifestContainer) throws IOException {
        logger.log(Log.INFO, "Obfuscating the entries in the file container: " + fileContainer.getName());
        Enumeration enumeration = fileContainer.enumeration();
        while (enumeration.hasMoreElements()) {
            FileEntry entry = (FileEntry) enumeration.nextElement();
            DataInputStream inStream = entry.getInputStream();
            try {
                if (entry.isClassFile()) {
                    ClassFile cf = ClassFile.create(classTree, inStream);
                    cf.remap(classTree);
                    logger.log(Log.DEBUG, "Reading:     " + entry.getName());
                    logger.log(Log.DEBUG, "  -> writing " + cf.getName() + CLASS_EXT);
                    JarEntry outEntry = new JarEntry(cf.getName() + CLASS_EXT);
                    jos.putNextEntry(outEntry);
                    MessageDigest shaDigest = MessageDigest.getInstance("SHA-1");
                    MessageDigest md5Digest = MessageDigest.getInstance("MD5");
                    DataOutputStream classOutputStream = new DataOutputStream(new DigestOutputStream(new BufferedOutputStream(new DigestOutputStream(jos, shaDigest)), md5Digest));
                    cf.write(classOutputStream);
                    classOutputStream.flush();
                    jos.closeEntry();
                    MessageDigest[] digests = { shaDigest, md5Digest };
                    manifestContainer.updateManifest(entry.getName(), cf.getName() + CLASS_EXT, digests);
                } else {
                    long size = entry.getSize();
                    if (size != -1) {
                        byte[] bytes = new byte[(int) size];
                        inStream.readFully(bytes);
                        String outName = classTree.getOutputFileName(entry.getName());
                        if (!entry.getName().equals(outName)) {
                            logfile.log(Log.VERBOSE, "# renaming resource: " + entry.getName() + " -> " + outName);
                        }
                        logger.log(Log.DEBUG, "Reading:     " + entry.getName());
                        logger.log(Log.DEBUG, "  -> writing " + outName);
                        JarEntry outEntry = new JarEntry(outName);
                        jos.putNextEntry(outEntry);
                        MessageDigest shaDigest = MessageDigest.getInstance("SHA");
                        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
                        DataOutputStream dataOutputStream = new DataOutputStream(new DigestOutputStream(new BufferedOutputStream(new DigestOutputStream(jos, shaDigest)), md5Digest));
                        dataOutputStream.write(bytes, 0, bytes.length);
                        dataOutputStream.flush();
                        jos.closeEntry();
                        MessageDigest[] digests = { shaDigest, md5Digest };
                        manifestContainer.updateManifest(entry.getName(), outName, digests);
                    }
                }
            } catch (NoSuchAlgorithmException nae) {
                logger.println("Cannot find message digest algorithm:");
                logger.println(nae.getMessage());
                logger.printStackTrace(nae);
            } finally {
                if (inStream != null) {
                    inStream.close();
                }
            }
        }
    }

    /** Writes a header out to the log file.
   * @param outName the name of the output Jar file
   * @param vec a vector containing the file containers used for the obfuscation
   */
    private void writeLogHeader(String outName, Vector vec) {
        logger.log(Log.INFO, "Writing log header...");
        logfile.println("# If this log is to be used for incremental obfuscation / patch generation, ");
        logfile.println("# add any '.class', '.method', '.field' and '.attribute' restrictions here:");
        logfile.println();
        logfile.println();
        logfile.println("#-DO-NOT-EDIT-BELOW-THIS-LINE------------------DO-NOT-EDIT-BELOW-THIS-LINE--");
        logfile.println("#");
        logfile.println("# JavaGuard Bytecode Obfuscator, version " + Version.getVersion());
        logfile.println("#");
        logfile.println("# Logfile created on " + new Date().toString());
        logfile.println("#");
        if (null != vec) {
            for (int i = 0; i < vec.size(); i++) {
                FileContainer fc = (FileContainer) vec.elementAt(i);
                logfile.println("# Input taken for obfuscation: " + fc.getName());
            }
        }
        logfile.println("# Output Jar file:             " + outName);
        logfile.println("# JavaGuard script file used:          " + (null != getScriptFile() ? getScriptFile().getName() : "(none, defaults used)"));
        logfile.println("#");
        logfile.println();
    }

    /** Write the obfuscation steps to the log file.
   * @param classTree the class tree whose mapping table should be written
   * @see ClassTree#dump
   */
    private void writeMappingTable(ClassTree classTree) {
        Runtime rt = Runtime.getRuntime();
        rt.gc();
        logfile.println();
        logfile.println("#");
        logfile.println("# Memory in use after class data structure built: " + Long.toString(rt.totalMemory() - rt.freeMemory()) + " bytes");
        logfile.println("# Total memory available                        : " + Long.toString(rt.totalMemory()) + " bytes");
        logfile.println("#");
        logfile.println();
        classTree.dump();
    }

    /** Returns the vector that holds the package, class, method and field
   * mapping entries from the script file.
   * @return vector that contains the script file entries; always non-null
   */
    private Vector getMappings() {
        if (null == mappings) {
            mappings = new Vector();
        }
        return mappings;
    }

    /** Adds an element to the list of mapping entries.
   * @param entry the entry from the script file
   * @see #getMappings
   */
    private void addMapping(ScriptEntry entry) {
        getMappings().addElement(entry);
    }

    /** Returns the vector that stores all output containers.
   * @return vector with output containers; always non-null
   * @see #addOutputContainer
   */
    private Vector getOutputContainers() {
        if (null == outputContainers) {
            outputContainers = new Vector();
        }
        return outputContainers;
    }

    /** Adds a new output container to the list of all available output
   * containers.
   * @param oc the output container to add
   * @see #getOutputContainers
   */
    private void addOutputContainer(OutputContainer oc) {
        getOutputContainers().addElement(oc);
    }

    /** A manager class for output containers. Each output container holds a list
   * of file containers (that manage which input files may be put into the
   * output file), a manifest container (that maintains the Manifest file for
   * the output Jar file) and the assigned output file.
   */
    private static class OutputContainer {

        /** A vector that stores all assigned file containes. */
        private Vector fileContainers = new Vector();

        /** Manages the Manifest file. */
        private ManifestContainer manifestContainer = new ManifestContainer();

        /** Holds the output file. */
        private File outputFile;

        /** Creates a new output container and assignes it to the given output
     * Jar file.
     * @param file the output Jar file
     */
        OutputContainer(File file) {
            this.outputFile = file;
        }

        /** Adds a new file container to the internal list of file containers.
     * @param fc the file container to add
     * @see #getFileContainers
     * @throws IOException if an I/O error occurs
     */
        void addFileContainer(FileContainer fc) throws IOException {
            getFileContainers().addElement(fc);
            getManifestContainer().addManifest(fc.getManifest());
        }

        /** Returns the vector that contains all assigned file containers.
     * @return vector with file containers
     * @see #addFileContainer
     */
        Vector getFileContainers() {
            return fileContainers;
        }

        /** Returns the manifest container assigned to this object.
     * @return manifest container
     */
        ManifestContainer getManifestContainer() {
            return manifestContainer;
        }

        /** Returns the output file assigned to this object.
     * @return the output file assigned to this object
     */
        File getOutputFile() {
            return outputFile;
        }
    }
}
