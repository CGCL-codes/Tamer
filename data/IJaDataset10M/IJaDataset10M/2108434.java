package net.sf.keytabgui.tests.dir;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import net.sf.keytabgui.tests.dir.command.ICommand;
import net.sf.keytabgui.tests.dir.command.ReadJavaFilesCommand;
import net.sf.keytabgui.tests.dir.command.ReadPropertiesFilesCommand;

public class Dir<T extends ICommand<File>> {

    /**
	 * 
	 * @return list of resource strings found in the project
	 */
    public Set<String> getResourceStrings() {
        ReadJavaFilesCommand c = new ReadJavaFilesCommand();
        FilenameFilter javaFiles = new ExtensionFilenameFilter(".java");
        walkTheTree(new File("./src/main/java"), javaFiles, (T) c);
        return c.getResourceStrings();
    }

    public Map<File, Properties> findPropFiles() {
        FilenameFilter propertiesFiles = new ExtensionFilenameFilter(".properties");
        ReadPropertiesFilesCommand d = new ReadPropertiesFilesCommand();
        walkTheTree(new File("./src/main/resources"), propertiesFiles, (T) d);
        return d.getMap();
    }

    private void walkTheTree(File file, FilenameFilter filenameFilter, T task) {
        for (File f : file.listFiles(filenameFilter)) {
            if (f.isDirectory()) {
                walkTheTree(f, filenameFilter, task);
            } else {
                task.execute(f);
            }
        }
    }
}
