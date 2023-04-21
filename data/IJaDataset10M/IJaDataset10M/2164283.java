package com.objectfab.tools.junitdoclet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

public class JUnitDoclet extends Doclet implements JUnitDocletProperties {

    protected static final String OPTION_INPUT_DIR = "-sourcepath";

    protected static final String OPTION_OUTPUT_DIR = "-d";

    protected static final String OPTION_PROPERTIES = "-properties";

    protected static final String OPTION_TESTING = "-testing";

    protected static final String OPTION_WRITING = "-writing";

    protected static final String OPTION_NAMING = "-naming";

    protected static final String OPTION_BUILDALL = "-buildall";

    protected static final String OPTION_HELP = "-help";

    protected static final String OPTION_SUBPACKAGE = "-subpackage";

    protected static final String OPTION_TEST_IN_TEST = "-testintest";

    protected static final String DEFAULT_SOURCE_PATH = "";

    protected static final String USAGE_STRING = "Parameters of JUnitDoclet       (c) 2002 ObjectFab GmbH\n" + "-d <out_dir>                                      (required)\n" + "            Where to write the JUnitTests\n" + "\n" + "-subpackage <sub_package_name>                    (optional)\n" + "            Use a sub package to have the tests close to the\n" + "            application but separate. Usualy the sub-package is named \"test\".\n" + "\n" + "-buildall                                         (optional)\n" + "            All tests are rebuild, even if application is unchanged.\n" + "\n" + "-properties <property_file_name>                  (optional)\n" + "            Holding all templates and definitions\n" + "            (default is junitdoclet.properties)\n" + "\n" + "-naming <naming_strategy_class_name>              (optional)\n" + "            Strategy class to define names\n" + "            (default is com.objectfab.tools.junitdoclet.DefaultNamingStrategy)\n" + "\n" + "-writing <writing_strategy_class_name>            (optional)\n" + "            Strategy class handle file access\n" + "            default is com.objectfab.tools.junitdoclet.DefaultWritingStrategy)\n" + "\n" + "-testing <testing_strategy_class_name>            (optional)\n" + "            Strategy class build the tests\n" + "            default is com.objectfab.tools.junitdoclet.DefaultTestingStrategy)\n" + "\n" + "-testintest                                       (optional)\n" + "            Generate TestCase for all classes that can be found in source path.\n" + "            If set, tests will be generated for all classes that are not \n" + "            TestCases or TestSuites themselves, even if they are in a \n" + "            test subpackage. This option is NOT recommended.\n";

    private String sourcePath;

    private String outputRoot;

    private NamingStrategy namingStrategy;

    private WritingStrategy writingStrategy;

    private TestingStrategy testingStrategy;

    private String namingStrategyName;

    private String writingStrategyName;

    private String testingStrategyName;

    private String propertyFileName;

    private String subPackage;

    private boolean buildAll;

    private boolean isTestInTest;

    private DocErrorReporter docErrorReporter;

    public JUnitDoclet() {
        super();
        init();
    }

    public void init() {
        setBuildAll(false);
        setTestInTest(false);
        setSourcePath(null);
        setOutputRoot(null);
        setNamingStrategy(null);
        setWritingStrategy(null);
        setTestingStrategy(null);
        setNamingStrategyName("com.objectfab.tools.junitdoclet.DefaultNamingStrategy");
        setWritingStrategyName("com.objectfab.tools.junitdoclet.DefaultWritingStrategy");
        setTestingStrategyName("com.objectfab.tools.junitdoclet.DefaultTestingStrategy");
        setPropertyFileName(DefaultConfigurableStrategy.DEFAULT_PROPERTY_FILE_NAME);
        setSubPackage(null);
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String theSourcePath) {
        sourcePath = theSourcePath;
    }

    public String getOutputRoot() {
        return outputRoot;
    }

    public void setOutputRoot(String outputRoot) {
        this.outputRoot = outputRoot;
    }

    public String getPropertyFileName() {
        return propertyFileName;
    }

    public void setPropertyFileName(String propertyFileName) {
        this.propertyFileName = propertyFileName;
        getNamingStrategy().setPropertyFileName(propertyFileName);
        getWritingStrategy().setPropertyFileName(propertyFileName);
        getTestingStrategy().setPropertyFileName(propertyFileName);
    }

    public NamingStrategy getNamingStrategy() {
        if (namingStrategy == null) {
            setNamingStrategy((NamingStrategy) createByClassName(getNamingStrategyName()));
        }
        return namingStrategy;
    }

    public void setNamingStrategy(NamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
        if (namingStrategy != null) {
            namingStrategy.setPropertyFileName(getPropertyFileName());
            namingStrategy.setSubPackage(getSubPackage());
            namingStrategy.setTestInTest(isTestInTest());
        }
    }

    public WritingStrategy getWritingStrategy() {
        if (writingStrategy == null) {
            setWritingStrategy((WritingStrategy) createByClassName(getWritingStrategyName()));
        }
        return writingStrategy;
    }

    public void setWritingStrategy(WritingStrategy writingStrategy) {
        this.writingStrategy = writingStrategy;
        if (writingStrategy != null) {
            writingStrategy.setPropertyFileName(getPropertyFileName());
        }
    }

    public TestingStrategy getTestingStrategy() {
        if (testingStrategy == null) {
            setTestingStrategy((TestingStrategy) createByClassName(getTestingStrategyName()));
        }
        return testingStrategy;
    }

    public void setTestingStrategy(TestingStrategy testingStrategy) {
        this.testingStrategy = testingStrategy;
        if (testingStrategy != null) {
            testingStrategy.setPropertyFileName(getPropertyFileName());
        }
    }

    public String getNamingStrategyName() {
        return namingStrategyName;
    }

    public void setNamingStrategyName(String namingStrategyName) {
        this.namingStrategyName = namingStrategyName;
        setNamingStrategy(null);
    }

    public String getWritingStrategyName() {
        return writingStrategyName;
    }

    public void setWritingStrategyName(String writingStrategyName) {
        this.writingStrategyName = writingStrategyName;
        setWritingStrategy(null);
    }

    public String getTestingStrategyName() {
        return testingStrategyName;
    }

    public void setTestingStrategyName(String testingStrategyName) {
        this.testingStrategyName = testingStrategyName;
        setTestingStrategy(null);
    }

    public boolean isBuildAll() {
        return buildAll;
    }

    public void setBuildAll(boolean buildAll) {
        this.buildAll = buildAll;
    }

    public boolean isTestInTest() {
        return isTestInTest;
    }

    public void setTestInTest(boolean testInTest) {
        isTestInTest = testInTest;
    }

    public String getSubPackage() {
        return subPackage;
    }

    public void setSubPackage(String subPackage) {
        this.subPackage = subPackage;
        getNamingStrategy().setSubPackage(subPackage);
    }

    private Object createByClassName(String className) {
        Object returnValue = null;
        Class clazz;
        try {
            clazz = Class.forName(className);
            returnValue = clazz.newInstance();
        } catch (Exception e) {
        }
        return returnValue;
    }

    public boolean processPackage(PackageDoc[] docs, int index) {
        boolean returnValue = true;
        StringBuffer newCode, oldCode;
        String fullTestSuiteName;
        TestingStrategy testing;
        WritingStrategy writing;
        NamingStrategy naming;
        testing = getTestingStrategy();
        writing = getWritingStrategy();
        naming = getNamingStrategy();
        if (testing.isTestablePackage(docs[index], naming)) {
            fullTestSuiteName = naming.getFullTestSuiteName(docs[index].name());
            oldCode = writing.loadClassSource(getOutputRoot(), fullTestSuiteName);
            if ((oldCode == null) || testing.isValid(oldCode.toString())) {
                newCode = new StringBuffer();
                returnValue = testing.codeTestSuite(docs, index, naming, newCode, testing.getProperties());
                if (testing.isValid(newCode.toString())) {
                    writing.indent(newCode);
                    if (testing.merge(newCode, oldCode, fullTestSuiteName)) {
                        if (isWritingNeeded(newCode, oldCode)) {
                            printNotice("Writing TestSuite " + fullTestSuiteName + ".");
                            writing.writeClassSource(getOutputRoot(), fullTestSuiteName, newCode);
                        }
                    }
                } else {
                    printError("Could not generate TestSuite " + fullTestSuiteName + " (possible reason: missing or wrong properties).");
                }
            } else {
                if (oldCode != null) {
                    printWarning("TestSuite " + fullTestSuiteName + " is invalid. It's not overwritten.");
                }
            }
        }
        return returnValue;
    }

    public boolean processClass(ClassDoc doc, PackageDoc packageDoc) {
        boolean returnValue = true;
        StringBuffer oldCode, newCode;
        String fullTestCaseName;
        String fullClassName;
        TestingStrategy testing;
        WritingStrategy writing;
        NamingStrategy naming;
        testing = getTestingStrategy();
        writing = getWritingStrategy();
        naming = getNamingStrategy();
        if (packageDoc == null) {
            packageDoc = doc.containingPackage();
        }
        fullClassName = doc.qualifiedTypeName();
        fullTestCaseName = naming.getFullTestCaseName(fullClassName);
        if (testing.isTestableClass(doc, naming)) {
            if (isGenerationNeeded(fullClassName, fullTestCaseName)) {
                oldCode = writing.loadClassSource(getOutputRoot(), fullTestCaseName);
                if ((oldCode == null) || testing.isValid(oldCode.toString())) {
                    newCode = new StringBuffer();
                    returnValue = testing.codeTestCase(doc, packageDoc, getNamingStrategy(), newCode, testing.getProperties());
                    if (testing.isValid(newCode.toString())) {
                        writing.indent(newCode);
                        if (testing.merge(newCode, oldCode, fullTestCaseName)) {
                            if (isWritingNeeded(newCode, oldCode)) {
                                printNotice("Writing TestCase " + fullTestCaseName + ".");
                                writing.writeClassSource(getOutputRoot(), fullTestCaseName, newCode);
                            } else {
                                printNotice("TestCase " + fullTestCaseName + " did not change but " + fullClassName + " did.");
                            }
                        }
                    } else {
                        printError("Could not generate TestCase " + fullTestCaseName + " (possible reason: missing or wrong properties).");
                    }
                } else {
                    if (oldCode != null) {
                        printWarning("TestCase " + fullTestCaseName + " is invalid. It's not overwritten.");
                    }
                }
            } else {
                returnValue = true;
            }
        } else {
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * Checks if file of application class is modified later than TestCase file.
     */
    public boolean isGenerationNeeded(String fullClassName, String fullTestCaseName) {
        boolean returnValue;
        returnValue = isBuildAll();
        returnValue = returnValue || !getWritingStrategy().isExistingAndNewer(getOutputRoot(), fullTestCaseName, getSourcePath(), fullClassName);
        return returnValue;
    }

    public boolean isWritingNeeded(StringBuffer newCode, StringBuffer oldCode) {
        boolean returnValue;
        returnValue = isBuildAll();
        returnValue = returnValue || (oldCode == null);
        returnValue = returnValue || ((newCode != null) && (oldCode != null) && (!newCode.toString().equals(oldCode.toString())));
        return returnValue;
    }

    public static boolean start(RootDoc doc) {
        JUnitDoclet instance;
        instance = new JUnitDoclet();
        instance.setOptions(doc.options());
        instance.setDocErrorReporter(doc);
        return instance.execute(doc);
    }

    public void setDocErrorReporter(DocErrorReporter doc) {
        docErrorReporter = doc;
        getNamingStrategy().setDocErrorReporter(doc);
        getWritingStrategy().setDocErrorReporter(doc);
        getTestingStrategy().setDocErrorReporter(doc);
    }

    public boolean execute(RootDoc doc) {
        boolean returnValue = true;
        PackageDoc[] packageDocs;
        ClassDoc[] classDocs;
        printNotice("Generating TestSuites and TestCases.");
        classDocs = doc.specifiedClasses();
        for (int i = 0; i < classDocs.length; i++) {
            returnValue = returnValue && processClass(classDocs[i], null);
        }
        packageDocs = doc.specifiedPackages();
        for (int i = 0; i < packageDocs.length; i++) {
            classDocs = packageDocs[i].ordinaryClasses();
            for (int j = 0; j < classDocs.length; j++) {
                returnValue = returnValue && processClass(classDocs[j], packageDocs[i]);
            }
            returnValue = returnValue && processPackage(packageDocs, i);
        }
        return returnValue;
    }

    public void setOptions(String[][] options) {
        for (int i = 0; (i < options.length); i++) {
            if (options[i][0].equals(OPTION_BUILDALL)) {
                setBuildAll(true);
            }
            if (options[i][0].equals(OPTION_TEST_IN_TEST)) {
                setTestInTest(true);
            }
            if (options[i][0].equals(OPTION_INPUT_DIR)) {
                setSourcePath(options[i][1]);
            }
            if (options[i][0].equals(OPTION_OUTPUT_DIR)) {
                setOutputRoot(options[i][1]);
            }
            if (options[i][0].equals(OPTION_SUBPACKAGE)) {
                setSubPackage(options[i][1]);
            }
            if (options[i][0].equals(OPTION_PROPERTIES)) {
                setPropertyFileName(options[i][1]);
            }
            if (options[i][0].equals(OPTION_TESTING)) {
                setTestingStrategyName(options[i][1]);
            }
            if (options[i][0].equals(OPTION_WRITING)) {
                setWritingStrategyName(options[i][1]);
            }
            if (options[i][0].equals(OPTION_NAMING)) {
                setNamingStrategyName(options[i][1]);
            }
        }
    }

    public static int optionLength(String s) {
        int returnValue = 0;
        if (s.equals(OPTION_HELP)) {
            printUsage();
        }
        if (s.equals(OPTION_BUILDALL)) {
            returnValue = 1;
        }
        if (s.equals(OPTION_TEST_IN_TEST)) {
            returnValue = 1;
        }
        if (s.equals(OPTION_OUTPUT_DIR)) {
            returnValue = 2;
        }
        if (s.equals(OPTION_PROPERTIES)) {
            returnValue = 2;
        }
        if (s.equals(OPTION_SUBPACKAGE)) {
            returnValue = 2;
        }
        if (s.equals(OPTION_TESTING)) {
            returnValue = 2;
        }
        if (s.equals(OPTION_WRITING)) {
            returnValue = 2;
        }
        if (s.equals(OPTION_NAMING)) {
            returnValue = 2;
        }
        return returnValue;
    }

    public static boolean validOptions(String[][] strings, DocErrorReporter reporter) {
        boolean returnValue = true;
        boolean foundOutput = false;
        boolean isTestInTest = false;
        String subPackage = null;
        String sourcePath = DEFAULT_SOURCE_PATH;
        String outputPath = null;
        for (int i = 0; (i < strings.length); i++) {
            if (strings[i][0].equals(OPTION_OUTPUT_DIR)) {
                if (!ValidationHelper.isDirectoryName(strings[i][1])) {
                    returnValue = false;
                    if (reporter != null) {
                        reporter.printError("Error:" + strings[i][1] + " is not a directory.");
                    }
                } else {
                    outputPath = strings[i][1];
                }
                foundOutput = true;
            }
            if (strings[i][0].equals(OPTION_INPUT_DIR)) {
                sourcePath = strings[i][1];
            }
            if (strings[i][0].equals(OPTION_TEST_IN_TEST)) {
                isTestInTest = true;
            }
            if (strings[i][0].equals(OPTION_PROPERTIES)) {
                if (!ValidationHelper.isPropertyName(strings[i][1])) {
                    returnValue = false;
                    if (reporter != null) {
                        reporter.printError("Error:" + strings[i][1] + " is not a property file.");
                    }
                }
            }
            if (strings[i][0].equals(OPTION_SUBPACKAGE)) {
                if (!ValidationHelper.isPackageName(strings[i][1])) {
                    returnValue = false;
                    if (reporter != null) {
                        reporter.printError("Error:" + strings[i][1] + " is not a valid package name.");
                    }
                } else {
                    subPackage = strings[i][1];
                }
            }
            if (strings[i][0].equals(OPTION_NAMING)) {
                if (!ValidationHelper.isClassName(strings[i][1])) {
                    returnValue = false;
                    if (reporter != null) {
                        reporter.printError("Error:" + strings[i][1] + " is not a class.");
                    }
                }
            }
            if (strings[i][0].equals(OPTION_TESTING)) {
                if (!ValidationHelper.isClassName(strings[i][1])) {
                    returnValue = false;
                    if (reporter != null) {
                        reporter.printError("Error:" + strings[i][1] + " is not a class.");
                    }
                }
            }
            if (strings[i][0].equals(OPTION_WRITING)) {
                if (!ValidationHelper.isClassName(strings[i][1])) {
                    returnValue = false;
                    if (reporter != null) {
                        reporter.printError("Error:" + strings[i][1] + " is not a class.");
                    }
                }
            }
        }
        returnValue = returnValue && foundOutput;
        if ((returnValue) && (subPackage == null) && !isTestInTest) {
            returnValue = !isOutputMatchingAnySource(sourcePath, outputPath, reporter);
        }
        return returnValue;
    }

    public static boolean isOutputMatchingAnySource(String sourcePath, String outputPath, DocErrorReporter reporter) {
        boolean returnValue = false;
        String canonicalSourcePath;
        String canonicalOutputPath = null;
        StringTokenizer sourcePathTokenizer = new StringTokenizer(sourcePath, File.pathSeparator);
        try {
            canonicalOutputPath = new File(outputPath).getCanonicalPath();
        } catch (IOException e) {
            if (reporter != null) {
                reporter.printError("Error: File '" + outputPath + "' not found (" + e.toString() + ").");
            }
            returnValue = true;
        }
        if (!returnValue && (canonicalOutputPath != null)) {
            while (sourcePathTokenizer.hasMoreTokens()) {
                try {
                    canonicalSourcePath = new File(sourcePathTokenizer.nextToken()).getCanonicalPath();
                    if (canonicalOutputPath.equals(canonicalSourcePath)) {
                        returnValue = true;
                    }
                } catch (IOException e) {
                }
            }
            if (returnValue) {
                if (reporter != null) {
                    reporter.printError("Error: value of " + OPTION_OUTPUT_DIR + " must not be in value of " + OPTION_INPUT_DIR + ".\n" + "You may override this restriction with " + OPTION_TEST_IN_TEST + ".");
                }
            }
        }
        return returnValue;
    }

    private static void printUsage() {
        System.out.println(USAGE_STRING);
    }

    private void printError(String msg) {
        if (docErrorReporter != null) {
            docErrorReporter.printError(msg);
        } else {
            System.err.println(msg);
        }
    }

    private void printWarning(String msg) {
        if (docErrorReporter != null) {
            docErrorReporter.printWarning(msg);
        } else {
            System.err.println(msg);
        }
    }

    private void printNotice(String msg) {
        if (docErrorReporter != null) {
            docErrorReporter.printNotice(msg);
        } else {
            System.out.println(msg);
        }
    }
}
