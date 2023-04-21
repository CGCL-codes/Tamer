package de.volkerbenders.jarwiz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

/**
 * @author 11111
 *
 */
public class JarWiz {

    public static final String JAR_FILE_NAME = "JarWiz-0.3.2.one-jar.jar";

    public static final String SRC_PATH_PARAM = "jarwiz.srcPath";

    public static final String JAR_PATH_PARAM = "jarwiz.jarPath";

    private static final Logger LOG = LoggerFactory.getLogger(JarWiz.class);

    public static final String PATH_SEPARATOR_CHAR = System.getProperty("path.separator");

    public static final String EXTENSION_JAVA_SRC = ".java";

    public static final String EXTENSION_JAR_ARCHIVE = ".jar";

    /**
	 * Second implementation of looking for files.
	 * 
	 * @param file
	 * @param extension
	 * @return
	 */
    public Set<String> findFilesByExtension(File file, String extension) {
        Set<String> matches = new HashSet<String>();
        if (file.isDirectory()) {
            File[] listing = getDirectoryListing(file, extension);
            for (File f : listing) {
                if (f.isFile()) {
                    System.out.print("f");
                    matches.add(f.getAbsolutePath());
                } else {
                    System.out.print("d");
                    matches.addAll(findFilesByExtension(f, extension));
                }
            }
        } else {
            matches.add(file.getAbsolutePath());
        }
        return matches;
    }

    /**
	 * Get contents of Directory.
	 * 
	 * @param dir
	 *            Directory to search
	 * @param extension
	 *            extension of files to list
	 * @return
	 */
    private File[] getDirectoryListing(File dir, final String extension) {
        File[] files = null;
        if (dir.isDirectory()) {
            files = dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    if (pathname.getName().endsWith(extension) || pathname.isDirectory()) return true;
                    return false;
                }
            });
        }
        return files;
    }

    /**
	 * Find import statements in java file.
	 * 
	 * @param javaFile
	 *            the file to inspect
	 * @throws IOException
	 *             problem occured
	 */
    public Set<String> getImportsFromJavaFile(File javaFile) throws IOException {
        FileReader fr = new FileReader(javaFile);
        if (LOG.isDebugEnabled()) {
            LOG.debug("getting imports from '" + javaFile.getCanonicalPath() + "'");
        }
        Set<String> imprtStmts = new HashSet<String>();
        if (javaFile.isHidden()) {
            return imprtStmts;
        }
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        String importStmt = null;
        while (br.ready()) {
            line = br.readLine().trim();
            if (line.startsWith(ImportKeyWordsEnum.IMPORT_STATIC.getKeyWord())) {
                importStmt = extractImport(line);
                imprtStmts.add(importStmt);
            } else if (line.startsWith(ImportKeyWordsEnum.IMPORT.getKeyWord())) {
                importStmt = extractImport(line);
                imprtStmts.add(importStmt);
            } else if (line.startsWith("public ") || line.startsWith("private ") || line.startsWith("class ")) {
                break;
            }
        }
        return imprtStmts;
    }

    /**
	 * Extract the Import Statement.
	 * Refactored to avoid code duplication.
	 * @param line Line as read from Source file
	 * @return just the imported class as a String
	 */
    private String extractImport(String line) {
        return line.substring(line.lastIndexOf(' '), line.indexOf(';')).trim();
    }

    /**
	 * Map Jar Files to the contained class files. The returned Map maps the absolute jar location to a
	 * Set of Classes/Types provided. 
	 * 
	 * @param jarFiles
	 *            Set of JarF Files to process
	 * @return Map to map jar files and the found class files.
	 */
    public Map<String, Set<String>> evaluateJarFiles(Set<String> jarFiles) {
        Map<String, Set<String>> jarContents = new HashMap<String, Set<String>>();
        for (String jarFile : jarFiles) {
            File f = new File(jarFile);
            try {
                jarContents.put(f.getAbsolutePath(), getClassFilesFromJar(f));
            } catch (IOException ioEx) {
                LOG.warn("Problem reading contents of " + f.getAbsolutePath(), ioEx);
            }
        }
        LOG.debug("mapping #" + jarContents.keySet().size() + " jar files to their contents...");
        return jarContents;
    }

    /**
	 * Extract list of class files in a jar file.
	 * 
	 * @param jarFile
	 *            jar file to extract from
	 * @return Set of Classes contained in the given jar file.
	 * 
	 * @throws IOException
	 *             Problem processing the given jar file.
	 */
    public Set<String> getClassFilesFromJar(File jarFile) throws IOException {
        Set<String> classFiles = new HashSet<String>();
        JarFile jf = null;
        try {
            jf = new JarFile(jarFile);
        } catch (java.util.zip.ZipException zipEx) {
            LOG.error("Error reading compressed content from '" + jarFile + "'!", zipEx);
            return classFiles;
        }
        Enumeration<JarEntry> jeEnum = jf.entries();
        while (jeEnum.hasMoreElements()) {
            JarEntry jarEntry = jeEnum.nextElement();
            if (jarEntry.getName().endsWith(".class")) {
                String entryName = jarEntry.getName();
                entryName = entryName.substring(0, entryName.indexOf('.'));
                entryName = entryName.replace('/', '.');
                classFiles.add(entryName);
            }
        }
        return classFiles;
    }

    /**
	 * Get all Types declared from classes jar files. This is required to detect transitive dependencies 
	 * between jar files.
	 * First load the class, get
	 * Currently not used but will be in next release.
	 * @param jarFile
	 * @param jarEntry
	 * @throws IOException 
	 */
    public void inspectJar(File jarFile) throws IOException {
        JarClassLoader jcl = new JarClassLoader();
        jcl.add(jarFile.toURI().toURL());
        String jarFilePath = jarFile.getAbsolutePath();
        Set<String> clazzes = null;
        if (jarContents.containsKey(jarFilePath)) {
            clazzes = jarContents.get(jarFilePath);
        } else {
            clazzes = getClassFilesFromJar(jarFile);
        }
        JclObjectFactory f = JclObjectFactory.getInstance();
        Object o = null;
        for (String clazz : clazzes) {
            clazz = clazz.indexOf('$') > 0 ? clazz.substring(0, clazz.indexOf('$')) : clazz;
            try {
                o = f.create(jcl, clazz);
                LOG.debug("Clazz '" + clazz + "' is public and has a default constructor :-");
                if (o != null) {
                    Field[] fields = o.getClass().getDeclaredFields();
                    checkFields(fields);
                    Method[] methods = o.getClass().getDeclaredMethods();
                    checkMethods(methods);
                }
            } catch (Exception e) {
            }
        }
    }

    /**
	 * list of jars that are not directly referenced (imported) in the sourcecode but required 
	 * by jar files.
	 */
    Set<String> transDeps = new HashSet<String>();

    /**
	 * Check methods for transitive depenencies. Verify their return type and their argument(s) types are known.
	 * TODO needs extensive testing
	 * @param methods
	 */
    private void checkMethods(Method[] methods) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Starting to inspect #" + methods.length + " methods for transitive dependencies...");
        }
        for (Method method : methods) {
            if (!method.getReturnType().isPrimitive()) {
                String returnTypeName = method.getReturnType().getName();
                boolean found = searchTypeInKnownJars(returnTypeName);
                if (!found && transDeps.isEmpty()) {
                    LOG.warn("transitive dep '" + returnTypeName + "' can not be resolved!");
                }
            }
            for (Class<?> paramType : method.getParameterTypes()) {
                String paramTypeName = paramType.getName();
                if (!paramType.isPrimitive() && (!paramTypeName.startsWith("java.") && !paramTypeName.startsWith("javax.") && !paramTypeName.startsWith("sun."))) {
                    boolean found = searchTypeInKnownJars(paramTypeName);
                    if (!found && transDeps.isEmpty()) {
                        LOG.warn("transitive dep '" + paramTypeName + "' can not be resolved!");
                    }
                }
            }
        }
    }

    /**
	 * Check Fields for transitive dependencies.
	 * TODO needs extensive testing
	 * @param fields
	 */
    private void checkFields(Field[] fields) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Starting to inspect #" + fields.length + " fields for transitive dependencies...");
        }
        String fieldTypeName = null;
        for (Field field : fields) {
            fieldTypeName = field.getType().getName();
            if (field.getType().isPrimitive()) {
                LOG.debug("Parameter Type '" + fieldTypeName + "' of Field '" + field.getName() + "' is a java primitive");
            } else {
                if (!field.getType().isArray()) {
                    if (!fieldTypeName.startsWith("java.") && !fieldTypeName.startsWith("javax.") && !fieldTypeName.startsWith("sun.")) {
                        LOG.debug("Field Type '" + fieldTypeName + "' of Field '" + field.getName() + "' is NO java primitive NOR java buildin class");
                    } else {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Arrays require different treatment");
                        }
                    }
                    boolean found = searchTypeInKnownJars(fieldTypeName);
                    if (!found && transDeps.isEmpty()) {
                        LOG.warn("transitive dep '" + fieldTypeName + "' can not be resolved!");
                    } else {
                        LOG.info("found match for ParameterTYpe '" + fieldTypeName + "' of Field '" + field.getName() + "'");
                    }
                }
            }
        }
    }

    /**
	 * @param fieldTypeName
	 * @return true if the given type name contained inthe list of jars - false otherwise.
	 */
    private boolean searchTypeInKnownJars(String fieldTypeName) {
        boolean found = false;
        for (String jar : jarContents.keySet()) {
            Set<String> classes = jarContents.get(jar);
            if (classes.contains(fieldTypeName)) {
                found = true;
                if (!jarFilesToConsider.contains(jar)) {
                    transDeps.add(jar);
                }
            }
        }
        return found;
    }

    /**
	 * Check jar files for transitive dependencies. Iterate all found jar files, and look into each class 
	 * and verify types of all fields / methods are "known".
	 * @throws IOException 
	 */
    public void checkForTransitiveDeps(Set<String> jarFiles) throws IOException {
        for (String jarFile : jarFiles) {
            inspectJar(new File(jarFile));
        }
    }

    /**
	 * Map a Name of a Jar File to a Set with Names of contained public Classes w default constructor
	 * @return Set with unique classnames if jarFile exists - NULL otherwise
	 */
    Map<String, Set<String>> publicClassesInJar = new HashMap<String, Set<String>>();

    public Set<String> getPublicClassesInJar(String jarFile) {
        if (publicClassesInJar != null && publicClassesInJar.containsKey(jarFile)) {
            return publicClassesInJar.get(jarFile);
        } else {
            return null;
        }
    }

    /**
	 * @param classToJarFileMap
	 */
    public void showContentsOfJarFiles(Map<String, String> classToJarFileMap) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("show contents of classToJarFileMap...");
            LOG.debug("values: # " + classToJarFileMap.values().size());
            LOG.debug("keys: # " + classToJarFileMap.keySet().size());
        }
        for (String className : classToJarFileMap.keySet()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("found class '" + className + "' provided by: " + classToJarFileMap.get(className));
            }
        }
    }

    Set<String> jarFilesToConsider = null;

    public static void main(String... strings) throws IOException {
        BasicConfigurator.configure();
        String javaPath = strings != null && strings.length > 0 ? strings[0] : null;
        if (javaPath == null) {
            System.err.println("Usage:");
            System.err.println("> java -jar " + JAR_FILE_NAME + " <javaSrcPath> [<jarPath>]");
            System.err.println(">   javaSrcPath: Search java source code here (defaults to ./ )");
            System.err.println(">   jarPath: Searchpath for jar archives (defaults to <javaSrcPath> if not specified)");
            System.exit(-1);
        }
        String jarPath = strings != null && strings.length > 1 ? strings[1] : javaPath;
        JarWiz jarWiz = new JarWiz();
        try {
            jarWiz.jarFilesToConsider = jarWiz.searchJarFilesForSrc(javaPath, jarPath);
        } catch (IOException e) {
            System.err.println("reading java src files failed " + e);
        }
        jarWiz.checkForTransitiveDeps(jarWiz.jarFilesToConsider);
        jarWiz.writeResultsToFile("./JarWiz-results.csv", jarWiz.jarFiles, jarWiz.jarFilesToConsider, jarWiz.transDeps);
    }

    Set<String> jarFiles = new HashSet<String>();

    /**
	 * @param javaPath
	 * @param jarPath
	 * @return
	 * @throws IOException
	 */
    private Set<String> searchJarFilesForSrc(String javaPath, String jarPath) throws IOException {
        Collection<String> javaFiles = new HashSet<String>();
        Scanner sc = new Scanner(javaPath);
        sc.useDelimiter(PATH_SEPARATOR_CHAR);
        while (sc.hasNext()) {
            String f = sc.next();
            javaFiles.addAll(findFilesByExtension(new File(f), EXTENSION_JAVA_SRC));
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("found #" + javaFiles.size() + " java files");
        }
        Set<String> importsInJavaFiles = new HashSet<String>();
        for (String s : javaFiles) {
            importsInJavaFiles.addAll(getImportsFromJavaFile(new File(s)));
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("found #" + importsInJavaFiles.size() + " distinct import statements in #" + javaFiles.size() + " java files");
        }
        Scanner jarScanner = new Scanner(jarPath);
        jarScanner.useDelimiter(PATH_SEPARATOR_CHAR);
        while (jarScanner.hasNext()) {
            String f = jarScanner.next();
            jarFiles.addAll(findFilesByExtension(new File(f), EXTENSION_JAR_ARCHIVE));
        }
        if (jarFiles == null || jarFiles.size() == 0) {
            throw new RuntimeException("No Jar Files found at " + jarPath);
        }
        jarContents = evaluateJarFiles(jarFiles);
        Set<String> jarFilesToConsider = new HashSet<String>();
        for (String jar : jarContents.keySet()) {
            Set<String> classes = jarContents.get(jar);
            for (String importStatement : importsInJavaFiles) {
                if (classes.contains(importStatement)) {
                    jarFilesToConsider.add(jar);
                }
            }
        }
        LOG.debug("\nnumber of jars providing imported classes: " + jarFilesToConsider.size());
        return jarFilesToConsider;
    }

    Map<String, Set<String>> jarContents = null;

    /**
	 * Write search result to file.
	 * 
	 * @param file
	 * @param allJars Set of all found jar archives in the given path
	 * @param jarToConsider Set of jars providing classes imported by src code in the given path
	 * @throws IOException
	 */
    private void writeResultsToFile(String file, Set<String> allJars, Set<String> jarToConsider, Set<String> transientDependencies) throws IOException {
        File outFile = new File(file);
        BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
        bw.append("Jar Archives you may want to include");
        bw.newLine();
        List<String> jars = new ArrayList(jarToConsider);
        Collections.sort(jars);
        int i = 0;
        Iterator<String> it = jars.iterator();
        String jar = null;
        while (it.hasNext()) {
            jar = it.next();
            bw.append(Integer.toString(i));
            bw.append(";");
            bw.append(jar);
            bw.newLine();
            i++;
        }
        bw.newLine();
        bw.append("------------------------------------------");
        bw.newLine();
        bw.append("Jar Archives w/o any imported class");
        bw.newLine();
        bw.newLine();
        bw.flush();
        jars = new ArrayList(allJars);
        Collections.sort(jars);
        it = jars.iterator();
        jar = null;
        while (it.hasNext()) {
            jar = it.next();
            if (!jarToConsider.contains(jar)) {
                bw.append(Integer.toString(i));
                bw.append(";");
                bw.append(jar);
                bw.newLine();
            }
            i++;
        }
        bw.newLine();
        bw.append("------------------------------------------");
        bw.newLine();
        bw.append("Jar Archives potentially fullfilling transitive dependencies");
        bw.newLine();
        if (!transientDependencies.isEmpty()) {
            bw.newLine();
            bw.flush();
            jars = new ArrayList(transientDependencies);
            Collections.sort(jars);
            it = jars.iterator();
            while (it.hasNext()) {
                jar = it.next();
                bw.append(Integer.toString(i));
                bw.append(";");
                bw.append(jar);
                bw.newLine();
                i++;
            }
        } else {
            bw.append("None found!");
        }
        bw.close();
    }

    /**
	 * @param jarFilesToConsider
	 */
    private void showSearchResult(Set<String> jarFilesToConsider) {
        System.out.println();
        String preFix = jarFilesToConsider != null && jarFilesToConsider.size() > 0 ? "Heureka! " : "Hmmm?! ";
        List<String> jars = new ArrayList(jarFilesToConsider);
        Collections.sort(jars);
        LOG.info(preFix + " We determined #" + jarFilesToConsider.size() + " jars (from #" + jarFiles.size() + " found ) containing classes your src code imports.");
        for (String jar : jars) {
            LOG.info("you may want to include this jar file : '" + jar + "'...");
        }
    }
}
