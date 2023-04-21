package gnu.java.net.loader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.StringTokenizer;

/**
 * A <code>FileURLLoader</code> is a type of <code>URLLoader</code>
 * only loading from file url.
 */
public final class FileURLLoader extends URLLoader {

    File dir;

    public FileURLLoader(URLClassLoader classloader, URLStreamHandlerCache cache, URLStreamHandlerFactory factory, URL url, URL absoluteUrl) {
        super(classloader, cache, factory, url, absoluteUrl);
        dir = new File(absoluteUrl.getFile());
    }

    /** get resource with the name "name" in the file url */
    public Resource getResource(String name) {
        try {
            File file = walkPathComponents(name);
            if (file == null) return null;
            return new FileResource(this, file);
        } catch (IOException e) {
        }
        return null;
    }

    /**
   * Walk all path tokens and check them for validity. At no moment, we are
   * allowed to reach a directory located "above" the root directory, stored
   * in "dir" property. We are also not allowed to enter a non existing
   * directory or a non directory component (plain file, symbolic link, ...).
   * An empty or null path is valid. Pathnames components are separated by
   * <code>File.separatorChar</code>
   * 
   * @param resourceFileName the name to be checked for validity.
   * @return the canonical file pointed by the resourceFileName or null if the
   *         walking failed
   * @throws IOException in case of issue when creating the canonical
   *           resulting file
   * @see File#separatorChar
   */
    private File walkPathComponents(String resourceFileName) throws IOException {
        StringTokenizer stringTokenizer = new StringTokenizer(resourceFileName, File.separator);
        File currentFile = dir;
        int tokenCount = stringTokenizer.countTokens();
        for (int i = 0; i < tokenCount - 1; i++) {
            String currentToken = stringTokenizer.nextToken();
            if ("..".equals(currentToken) && currentFile.equals(dir)) return null;
            currentFile = new File(currentFile, currentToken);
            if (!(currentFile.exists() && currentFile.isDirectory())) return null;
        }
        if (tokenCount > 0) {
            String currentToken = stringTokenizer.nextToken();
            if ("..".equals(currentToken) && currentFile.equals(dir)) return null;
            currentFile = new File(currentFile, currentToken);
            if (!currentFile.exists()) return null;
        }
        return currentFile.getCanonicalFile();
    }
}
