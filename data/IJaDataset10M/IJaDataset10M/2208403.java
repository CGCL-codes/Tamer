package cpdetector.test;

import jargs.gnu.CmdLineParser;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import cpdetector.CmdLineArgsInheritor;
import cpdetector.io.CodepageDetectorProxy;
import cpdetector.io.FileFilterExtensions;
import cpdetector.io.ICodepageDetector;
import cpdetector.io.JChardetFacade;
import cpdetector.io.ParsingDetector;
import cpdetector.reflect.SingletonLoader;
import cpdetector.util.FileUtil;

/**
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 *  
 */
public class PerformanceTest extends CmdLineArgsInheritor {

    /**
   * The root folder (directory) under which all files for the collection are
   * found.
   */
    protected File collectionRoot = null;

    /**
   * Just caching for performance reasons (hack).
   */
    private String collectionRootName;

    /**
   * The codepage detection proxy that will be used. Is optionally configured by
   * argument \"-c\".
   */
    protected CodepageDetectorProxy detector;

    /**
   * A list of all Charset implementations of this java version. Used for debug
   * output.
   */
    private Charset[] parseCodepages;

    private static String fileseparator = System.getProperty("file.separator");

    /**
   * Needed for searching the collection root directory recursively for
   * extensions.
   */
    private FileFilter extensionFilter;

    /**
   * Amount of detections repeatedly performed on every document. A high number
   * minimizes the time measurement error (but causes the test to take longer).
   */
    private int iterations = 10;

    private int filecount = 0;

    IStopWatch stopWatch = new StopWatchSimple(false);

    public PerformanceTest() {
        super();
        this.detector = CodepageDetectorProxy.getInstance();
        this.addCmdLineOption("documents", new CmdLineParser.Option.StringOption('r', "documents"));
        this.addCmdLineOption("extensions", new CmdLineParser.Option.StringOption('e', "extensions"));
        this.addCmdLineOption("iterations", new CmdLineParser.Option.IntegerOption('i', "iterations"));
        this.addCmdLineOption("detectors", new CmdLineParser.Option.StringOption('c', "detectors"));
    }

    public void parseArgs(String[] cmdLineArgs) throws Exception {
        super.parseArgs(cmdLineArgs);
        Object collectionOption = this.getParsedCmdLineOption("documents");
        Object extensionsOption = this.getParsedCmdLineOption("extensions");
        Object iterationsOption = this.getParsedCmdLineOption("iterations");
        Object detectorOption = this.getParsedCmdLineOption("detectors");
        if (collectionOption == null) {
            usage();
            throw new MissingResourceException("Parameter for collection root directory is missing.", "String", "-r");
        }
        this.collectionRoot = new File(collectionOption.toString());
        this.collectionRootName = this.collectionRoot.getAbsolutePath();
        if (extensionsOption != null) {
            this.extensionFilter = new FileFilterExtensions(this.parseCSVList(extensionsOption.toString()));
        } else {
            this.extensionFilter = new FileFilter() {

                public boolean accept(File f) {
                    return true;
                }
            };
        }
        if (detectorOption != null) {
            String[] detectors = this.parseCSVList((String) detectorOption);
            if (detectors.length == 0) {
                StringBuffer msg = new StringBuffer();
                msg.append("You specified the codepage detector argument \"-c\" but ommited any comma-separated fully qualified class-name.");
                throw new IllegalArgumentException(msg.toString());
            }
            ICodepageDetector cpDetector = null;
            for (int i = 0; i < detectors.length; i++) {
                try {
                    cpDetector = (ICodepageDetector) SingletonLoader.getInstance().newInstance(detectors[i]);
                    if (cpDetector != null) {
                        this.detector.add(cpDetector);
                    }
                } catch (InstantiationException ie) {
                    System.err.println("Could not instantiate custom ICodepageDetector: " + detectors[i] + " (argument \"-c\"): " + ie.getMessage());
                }
            }
        } else {
            this.detector.add(new ParsingDetector(false));
            this.detector.add(JChardetFacade.getInstance());
        }
        if (iterationsOption != null) {
            this.iterations = ((Integer) iterationsOption).intValue();
        }
        this.loadCodepages();
    }

    private final String[] parseCSVList(String listLiteral) {
        if (listLiteral == null) return null;
        List tmpList = new LinkedList();
        StringTokenizer tok = new StringTokenizer(listLiteral, ";,");
        while (tok.hasMoreElements()) {
            tmpList.add(tok.nextToken());
        }
        return (String[]) tmpList.toArray(new String[tmpList.size()]);
    }

    /**
   * <p>
   * Recursive depth first search for all documents with .txt - ending
   * (case-insensitive).
   * </p>
   * <p>
   * The given list is filled with all files with a ".txt" extension that were
   * found in the directory subtree of the argument f.
   * </p>
   * <p>
   * No check for null or existance of f is made here, so keep it private.
   * </p>
   * 
   * @param f
   *          The current directory or file (if we visit a leaf).
   */
    private void processRecursive(File f) throws Exception {
        if (f == null) {
            throw new IllegalArgumentException("File argument is null!");
        }
        if (!f.exists()) {
            throw new IllegalArgumentException(f.getAbsolutePath() + " does not exist.");
        }
        if (f.isDirectory()) {
            File[] childs = f.listFiles();
            for (int i = childs.length - 1; i >= 0; i--) {
                processRecursive(childs[i]);
            }
        } else if (this.extensionFilter.accept(f)) {
            this.process(f);
        }
    }

    public final void process() throws Exception {
        this.verifyFiles();
        this.describe();
        this.processRecursive(this.collectionRoot);
        System.out.println("Processed " + this.filecount + " * " + this.iterations + " = " + (this.filecount * this.iterations) + " in " + this.stopWatch.getPureMilliSeconds() + " ms.");
    }

    /**
   * All three Files are validated if null, existant and the right type
   * (directory vs. file).
   * 
   * @throws Exception
   *           Sth. does not seem to be valid.
   */
    protected void verifyFiles() throws IllegalArgumentException {
        StringBuffer msg = new StringBuffer();
        if (this.collectionRoot == null) {
            msg.append("-> Collection root directory is null!\n");
        } else {
            if (!this.collectionRoot.exists()) {
                msg.append("-> Collection root directory:\"");
                msg.append(this.collectionRoot.getAbsolutePath());
                msg.append("\" does not exist!\n");
            }
        }
        if (msg.length() > 0) {
            throw new IllegalArgumentException(msg.toString());
        } else {
            System.out.println("All parameters are valid.");
        }
    }

    /**
   * @param files
   */
    private void process(File document) throws Exception {
        Charset charset = null;
        String filename = document.getAbsolutePath();
        int length = (int) document.length();
        filename = filename.substring(this.collectionRootName.length());
        System.out.println("Processing: " + filename);
        InputStream cache = FileUtil.readCache(document);
        this.stopWatch.start();
        for (int i = this.iterations; i > 0; i--) {
            cache.reset();
            charset = this.detector.detectCodepage(cache, length);
        }
        this.stopWatch.stop();
        System.out.println("Detected: " + String.valueOf(charset));
        this.filecount++;
    }

    protected void describe() {
        StringBuffer msg = new StringBuffer();
        msg.append("Setup:\n");
        msg.append("  Collection-Root         : ").append(this.collectionRoot.getAbsolutePath()).append('\n');
        msg.append("  iterations per document : ").append(this.iterations).append('\n');
        msg.append("  detection algorithm     : ");
        msg.append("\n").append(this.detector.toString());
        System.out.println(msg.toString());
    }

    protected void usage() {
        StringBuffer tmp = new StringBuffer();
        tmp.append("usage: java -jar codepageProcessor.jar [options]").append('\n');
        tmp.append("options: \n");
        tmp.append("\n  Optional:\n");
        tmp.append("  -e <extensions> : A comma- or semicolon- separated string for document extensions like \"-e txt,dat\" (without dot or space!).\n");
        tmp.append("  -i              : Iterations per document. A high value decreases time measurement error \n");
        tmp.append("                     but causes the test to take longer.\n");
        tmp.append("  -c              : Semicolon-separated list of fully qualified classnames. \n");
        tmp.append("                    These classes will be casted to ICodepageDetector instances \n");
        tmp.append("                    and used in the order specified.\n");
        tmp.append("                    If this argument is ommited, a HTMLCodepageDetector followed by .\n");
        tmp.append("                    a JChardetFacade is used by default.\n");
        tmp.append("  Mandatory:\n");
        tmp.append("  -r            : Root directory containing the collection (recursive).\n");
        tmp.append("  -o            : Output directory containing the sorted collection.\n");
        System.out.print(tmp.toString());
    }

    void loadCodepages() {
        SortedMap charSets = Charset.availableCharsets();
        Iterator csIt = charSets.entrySet().iterator();
        Map.Entry entry;
        Iterator aliasIt;
        Set aliases;
        Charset cs;
        this.parseCodepages = new Charset[charSets.size()];
        int index = 0;
        while (csIt.hasNext()) {
            entry = (Map.Entry) csIt.next();
            cs = (Charset) entry.getValue();
            this.parseCodepages[index] = cs;
            index++;
        }
    }

    public static void main(String[] args) {
        PerformanceTest detector = new PerformanceTest();
        try {
            detector.parseArgs(args);
            try {
                detector.process();
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        } catch (Exception e) {
            System.err.println("\nError: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
