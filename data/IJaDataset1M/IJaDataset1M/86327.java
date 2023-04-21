package palus.model;

import randoop.util.Files;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import palus.PalusUtil;

/**
 * A repository containing the classes to model during processing.
 * 
 * The tool will only model classes defined in this class.
 * 
 * @author saizhang@google.com (Sai Zhang)
 *
 */
public final class ClassesToModel {

    /**
   * An option, indicating whether only model user-provided classes.
   * If it is false, the tool builds model for every class
   * */
    public static boolean only_model_user_provided = false;

    /**
     * Check whether this class should be included in the model or not
     * @param clz the class to be check
     * @return true if the class needs to be modelled, false if else.
     * */
    public static boolean modelThisClass(Class<?> clz) {
        if (Modifier.isAbstract(clz.getModifiers()) || Modifier.isInterface(clz.getModifiers())) {
            return false;
        }
        if (clz.isPrimitive() || PalusUtil.isPrimitive(clz) || clz.isArray() || PalusUtil.isStringType(clz)) {
            return false;
        }
        if (clz.getName().indexOf("$") != -1) {
            return false;
        }
        if (clz.getPackage() == null) {
            return true;
        }
        if (clz.getPackage().getName().startsWith("palus.")) {
            return false;
        }
        if (clz.getPackage().getName().startsWith("org.objectweb.asm") || clz.getPackage().getName().startsWith("java.") || clz.getPackage().getName().startsWith("junit.") || clz.getPackage().getName().startsWith("org.xmlpull.")) {
            return false;
        }
        if (!only_model_user_provided) {
            return true;
        } else {
            return classesToModel.contains(clz);
        }
    }

    /**
	 * All classes users provided
	 * */
    private static Set<Class<?>> classesToModel = new LinkedHashSet<Class<?>>();

    /**
	 * Initialize the user-provided class set. The set is intended to be initialized
	 * only once. It will throw an exception if the code changes the class set later on.
	 * */
    public static void initializeClassesToModel(Collection<Class<?>> classes) {
        PalusUtil.checkTrue(classesToModel.isEmpty(), "The classesToModel set should not " + "be already set!");
        classesToModel.addAll(classes);
    }

    /**
	 * A map keeping the instance number to process for each class.
	 * Map from class name to the number of instance to process.
	 * */
    private static Map<String, Integer> instanceForClasses = new LinkedHashMap<String, Integer>();

    /**
	 * Initializes a map initialize the number of instances to process for every class
	 * */
    public static void initializeInstanceToProcess(String fileName) {
        File file = new File(fileName);
        PalusUtil.checkTrue(file.exists(), "The file: " + file.getAbsolutePath() + " does not exist!");
        try {
            List<String> list = Files.readWhole(file);
            for (String line : list) {
                line = line.trim();
                if (line.equals("") || line.startsWith("#")) {
                    continue;
                }
                String[] splits = line.split(" ");
                if (splits.length != 2) {
                    System.err.println("Ignore the wrong format line: " + line);
                    System.err.println("The line format should be : class-name  instance-num.");
                    continue;
                }
                String className = splits[0];
                Integer instanceNum = Integer.valueOf(splits[1]);
                instanceForClasses.put(className, instanceNum);
            }
        } catch (IOException e) {
            throw new RuntimeException("Errors in reading file: " + file.getAbsolutePath());
        }
    }

    /**
	 * Looks up the instance number to process for a given class.
	 * */
    public static int instanceToProcess(String className) {
        if (!instanceForClasses.containsKey(className)) {
            return -1;
        }
        int num = instanceForClasses.get(className);
        PalusUtil.checkTrue(num > 0, "The instance num (" + num + ") to process should > 0 for className: " + className);
        return num;
    }
}
