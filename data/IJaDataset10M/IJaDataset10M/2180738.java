package net.turambar.just.jpp;

import net.turambar.just.SourceReader;
import net.turambar.just.util.FileSourceReader;
import java.io.File;
import java.io.FileNotFoundException;

/** An <code>IncludeLocator</code> which searches the include path for a file representing included resource.
 *  Each instance has a include path, wich is a set of directories.
 *  When a resource is requested, they are searched for a file of the specified name (path).
 *  @author Marcin Moscicki
 */
public class FileIncludeLocator implements IncludeLocator {

    private String[] includes;

    /** Creates an instance using the given include path.
     * @param includes the paths to the directories which will be searched for the file to include.
     */
    public FileIncludeLocator(String[] includes) {
        this.includes = includes;
    }

    /** Returns a a {@link FileSourceReader FileSourceReader} representing included file.
     *  All '\' and '/' characters in <code>includeString</code> are changed to system specific path separator.
     *  The list of directories is searched in the fixed order and a file path is created by concatenation of
     *  the directory path, path separator and the resource name. First existing file is used to create
     *  a {@link FileSourceReader FileSourceReader} instance.
     * @param includeString a relative system path of a file to include.
     * @return a <code>FileSourceReader</code> assigned to the given file.
     */
    public SourceReader getIncludeReader(String includeString) {
        includeString = convertedPath(includeString);
        for (int i = 0; i < includes.length; ++i) {
            String fileName = includes[i] + File.separator + includeString;
            try {
                return new FileSourceReader(fileName);
            } catch (FileNotFoundException e) {
            }
        }
        return null;
    }

    static String convertedPath(String path) {
        StringBuffer sb = new StringBuffer(path.length());
        for (int i = 0; i < path.length(); ++i) {
            switch(path.charAt(i)) {
                case '/':
                case '\\':
                    sb.append(File.separator);
                    break;
                default:
                    sb.append(path.charAt(i));
            }
        }
        return sb.toString();
    }
}
