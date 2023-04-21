package bufferings.ktr.wjr.server.logic;

import static bufferings.ktr.wjr.shared.util.Preconditions.*;
import java.io.File;
import java.io.FileFilter;
import bufferings.ktr.wjr.server.util.WjrServerUtils;
import bufferings.ktr.wjr.shared.model.WjrStore;

/**
 * The loader of the WjrStore.
 * 
 * @author bufferings[at]gmail.com
 */
public abstract class WjrStoreLoader {

    protected static final String CLASSFILE_EXT = ".class";

    protected static final String FILE_SEPARATOR_REGEX = "[\\\\/]";

    protected static final String PACKAGE_SEPARATOR = ".";

    /**
   * Loads the WjrStore from the classes in the classesDirPath.
   * 
   * @param searchRootDirPath
   *          The search root directory path.
   * @return The WjrStore.
   * @throws NullPointerException
   *           When the searchRootDirPath is null.
   * @throws IllegalArgumentException
   *           When the searchRootDirPath does not exists.
   * @throws IllegalArgumentException
   *           When the searchRootDirPath isn't a directory.
   */
    public WjrStore loadWjrStore(String searchRootDirPath) {
        checkNotNull(searchRootDirPath, "The searchRootDirPath parameter is null.");
        File classesDir = new File(searchRootDirPath);
        checkArgument(classesDir.exists(), "%s not exists.", searchRootDirPath);
        checkArgument(classesDir.isDirectory(), "%s isn't a directory.", searchRootDirPath);
        WjrStore wjrStore = new WjrStore();
        findAndStoreTests(wjrStore, searchRootDirPath, classesDir);
        wjrStore.updateAllSummaries();
        return wjrStore;
    }

    /**
   * Finds the tests and checks and stores it recursively.
   * 
   * @param store
   *          The store to store.
   * @param searchRootDirPath
   *          The search root directory path.
   * @param targetDir
   *          The directory to check.
   */
    protected void findAndStoreTests(WjrStore store, String searchRootDirPath, File targetDir) {
        File[] classFiles = listClassFiles(targetDir);
        for (File classFile : classFiles) {
            String className = getClassNameFromClassFile(searchRootDirPath, classFile);
            Class<?> clazz = loadClass(className);
            checkAndStoreTestClass(store, clazz);
        }
        File[] dirs = listDirectories(targetDir);
        for (File dir : dirs) {
            findAndStoreTests(store, searchRootDirPath, dir);
        }
    }

    /**
   * Lists the class files.
   * 
   * @param dir
   *          The search directory.
   * @return Found class files.
   */
    protected File[] listClassFiles(File dir) {
        return dir.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return !pathname.isDirectory() && pathname.getName().endsWith(CLASSFILE_EXT);
            }
        });
    }

    /**
   * List the directories.
   * 
   * @param dir
   *          The search directory.
   * @return Found directories
   */
    protected File[] listDirectories(File dir) {
        return dir.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
    }

    /**
   * Gets the class name from the class file.
   * 
   * @param searchRootDirPath
   *          The search root directory path.
   * @param classFile
   *          the class file.
   * @return The class name.
   */
    protected String getClassNameFromClassFile(String searchRootDirPath, File classFile) {
        String path = classFile.getPath();
        int beginIndex = searchRootDirPath.length() + 1;
        int endIndex = path.length() - CLASSFILE_EXT.length();
        return path.substring(beginIndex, endIndex).replaceAll(FILE_SEPARATOR_REGEX, PACKAGE_SEPARATOR);
    }

    /**
   * Loads the class from the class name.
   * 
   * @param className
   *          The class name.
   * @return The loaded class.
   * @throws NullPointerException
   *           When the className parameter is null.
   * @throws RuntimeException
   *           When the class is not found.
   */
    protected Class<?> loadClass(String className) {
        return WjrServerUtils.loadClass(className);
    }

    /**
   * Checks if the class is the target one and has test method(s), then stores
   * it.
   * 
   * @param store
   *          The store.
   * @param clazz
   *          The class.
   */
    protected abstract void checkAndStoreTestClass(WjrStore store, Class<?> clazz);
}
