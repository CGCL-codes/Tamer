package net.frontlinesms.ui.i18n.legacy;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.i18n.TextResourceKeyOwner;

/**
 * Main class for running the language tools from the CommandLine.
 * @author Alex
 */
public class Main {

    /** Directory to output generated files to */
    private static final String OUTPUT_DIRECTORY = "temp/tools/lang";

    /** Directory of the language files, hardcoded for now */
    private static final String LANGUAGEBUNDLE_DIRECTORY = "src/main/resources/resources/languages";

    /** Java package names containing the {@link TextResourceKeyOwner}s */
    private static final String[] DEFAULT_TEXTKEYRESOURCE_PACKAGE_NAMES = { "net.frontlinesms", "net.frontlinesms.ui", "net.frontlinesms.ui.handler", "net.frontlinesms.ui.handler.contacts", "net.frontlinesms.ui.handler.core", "net.frontlinesms.ui.handler.email", "net.frontlinesms.ui.handler.keyword", "net.frontlinesms.ui.handler.message" };

    /** Directory of the XML files, hardcoded for now */
    private static final String[] UI_XML_LAYOUT_DIRECTORIES = { "src/main/resources/ui/core", "src/main/resources/ui/dialog", "src/main/resources/ui/smsdevice", "src/main/resources/ui/wizard" };

    /**
	 * Run the checker and produce a report.
	 * @param args 
	 * @throws Exception 
	 */
    public static void main(String[] args) throws Exception {
        if ((args.length != 0 && args.length != 4 && args.length != 5) || args.length > 0 && args[0].equals("--help")) {
            printUsage(System.out);
            return;
        }
        String[] tRKOPackageNames;
        String languagebundleDirectoryPath;
        String[] uiXmlLayoutDirectories;
        String outputDirectoryPath = OUTPUT_DIRECTORY;
        String resourceFileBaseName;
        if (args.length == 0) {
            tRKOPackageNames = DEFAULT_TEXTKEYRESOURCE_PACKAGE_NAMES;
            uiXmlLayoutDirectories = UI_XML_LAYOUT_DIRECTORIES;
            languagebundleDirectoryPath = LANGUAGEBUNDLE_DIRECTORY;
            resourceFileBaseName = "frontlineSMS";
        } else {
            tRKOPackageNames = splitArg(args[0]);
            uiXmlLayoutDirectories = splitArg(args[1]);
            languagebundleDirectoryPath = args[2];
            resourceFileBaseName = args[3];
            if (args.length > 4) outputDirectoryPath = args[4];
        }
        HashSet<Class<?>> tRKOSet = new HashSet<Class<?>>();
        for (String packageName : tRKOPackageNames) {
            tRKOSet.addAll(getAnnotatedClasses(TextResourceKeyOwner.class, getClasses(getClassNames(packageName))));
        }
        Class<?>[] uiJavaControllerClasses = tRKOSet.toArray(new Class<?>[0]);
        doReport(uiJavaControllerClasses, uiXmlLayoutDirectories, languagebundleDirectoryPath, resourceFileBaseName, outputDirectoryPath);
    }

    /**
	 * Splits a single commandline argument value into separate strings.
	 * @param arg
	 * @return
	 */
    private static String[] splitArg(String arg) {
        return arg.split("\\s");
    }

    private static void doReport(Class<?>[] uiJavaControllerClasses, String[] uiXmlLayoutDirectories, String languagebundleDirectoryPath, String resourceFileBaseName, String outputDirectoryPath) throws Exception {
        LanguageChecker checker = new LanguageChecker(uiJavaControllerClasses, uiXmlLayoutDirectories);
        output(System.out, checker);
        File outputDirectory = new File(outputDirectoryPath);
        outputDirectory.mkdirs();
        String extraTextResourcePath = languagebundleDirectoryPath + '/' + resourceFileBaseName + ".properties";
        Map<String, String> extraTextResources = InternationalisationUtils.loadTextResources(extraTextResourcePath, new FileInputStream(extraTextResourcePath));
        String baseTranslationFileName = "src/main/resources/resources/languages/frontlineSMS.properties";
        Map<String, String> baseTextResources = null;
        if (!baseTranslationFileName.equals(extraTextResourcePath)) {
            baseTextResources = InternationalisationUtils.loadTextResources(baseTranslationFileName, new FileInputStream(baseTranslationFileName));
            Map<String, String> newMap = new HashMap<String, String>();
            InternationalisationUtils.mergeMaps(newMap, baseTextResources);
            InternationalisationUtils.mergeMaps(newMap, extraTextResources);
            extraTextResources = newMap;
        }
        TranslationEmitter emitter = new TranslationEmitter(extraTextResources, outputDirectory);
        for (File languageBundle : new File(languagebundleDirectoryPath).listFiles(getTextResourceFileFilter(resourceFileBaseName))) {
            I18nReport report = checker.produceReport(baseTextResources, languageBundle);
            report.output(System.out, true);
            emitter.processBundle(languageBundle, report);
        }
    }

    /**
	 * Gets a list of all classes in the supplied package.
	 * @param packageName
	 * @return list of fully-qualified java class names
	 */
    private static Collection<String> getClassNames(String packageName) {
        FilenameFilter javaSourceFileFilter = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".java");
            }
        };
        String[] classFileNames = new File("src/main/java/" + packageName.replace('.', '/')).list(javaSourceFileFilter);
        HashSet<String> classNames = new HashSet<String>();
        for (String classFileName : classFileNames) {
            classNames.add(packageName + "." + classFileName.substring(0, classFileName.length() - ".java".length()));
        }
        return classNames;
    }

    /**
	 * Gets the {@link Class} objects for the supplied list of class names.
	 * @param classNames
	 * @return A list of classes.
	 * @throws ClassNotFoundException if there was a problem getting a reference to one of the classes.
	 */
    private static Collection<Class<?>> getClasses(Collection<String> classNames) throws ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        for (String className : classNames) {
            classes.add(Class.forName(className));
        }
        return classes;
    }

    /**
	 * @param <T>
	 * @param annotation 
	 * @param potentialSubclasses
	 * @return All classes in the supplied list who have the requested annotation.
	 */
    @SuppressWarnings("unchecked")
    private static <T extends Annotation> Collection<Class<T>> getAnnotatedClasses(Class<T> annotation, Collection<Class<?>> potentialSubclasses) {
        Set<Class<T>> annotated = new HashSet<Class<T>>();
        for (Iterator iterator = potentialSubclasses.iterator(); iterator.hasNext(); ) {
            Class<?> potentialSubclass = (Class<?>) iterator.next();
            if (potentialSubclass.isAnnotationPresent(annotation)) {
                annotated.add((Class<T>) potentialSubclass);
            }
        }
        return annotated;
    }

    /**
	 * Show the usage of the {@link Main} class.
	 * @param out
	 */
    private static void printUsage(PrintStream out) {
        out.println("FrontlineSMS i18n Tools - USAGE");
        System.out.println("\tuse with no args for processing FrontlineSMS core");
        System.out.println("\targs\t <packages containing text key resources> <directories containing thinlet xml layouts> <language bundle directory path> <language resource file basename> <[optional]output directory>");
    }

    /**
	 * Print the details of a report
	 * @param out
	 * @param checker 
	 */
    private static void output(PrintStream out, LanguageChecker checker) {
        out.println(checker.getClass().getName() + " REPORT START ----------");
        out.println("\tin code: " + checker.getI18nKeysInCode().size());
        out.println("\tin XML : " + checker.getI18nKeysInXml().size());
        out.println("---------- REPORT START " + checker.getClass().getName());
    }

    /**
	 * @param resourceFileBaseName
	 * @return file filter for getting text resource files
	 */
    private static FileFilter getTextResourceFileFilter(final String resourceFileBaseName) {
        return new FileFilter() {

            public boolean accept(File file) {
                return file.getName().startsWith(resourceFileBaseName) && file.getName().endsWith(".properties");
            }
        };
    }
}
