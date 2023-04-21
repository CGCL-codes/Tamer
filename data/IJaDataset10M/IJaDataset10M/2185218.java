package storm.finder.selectors;

import java.io.File;
import java.util.List;

/**
 * To be used by classes that select which files will be used for database
 * configuration. Used in ClassFinder.
 * 
 * @author guilherme
 * 
 */
public interface FileSelector {

    public boolean isFileSelected(String fileName, String pckgname) throws Throwable;

    public void processFile(String fileName, String pckgname, List<Class> classes) throws Throwable;
}
