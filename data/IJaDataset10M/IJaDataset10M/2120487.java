package gov.sns.tools;

import java.net.URL;
import java.io.File;

/**
 * UrlTool is a convenience class of static methods that allow the user to 
 * convert between file path and URL specifications.
 *
 * @author  tap
 */
public class UrlTool {

    /** Creates new UrlTool */
    protected UrlTool() {
    }

    /** Convert a file to a URL specification */
    public static String urlSpecForFile(File file) throws FilePathException {
        try {
            URL url = file.toURL();
            return url.toString();
        } catch (Exception excpt) {
            throw new FilePathException(file.getPath());
        }
    }

    /** Convert a file path to a URL specification */
    public static String urlSpecForFilePath(String filePath) throws FilePathException {
        String urlSpec = null;
        try {
            File mainFile = new File(filePath);
            URL url = mainFile.toURL();
            urlSpec = url.toString();
        } catch (Exception excpt) {
            throw new FilePathException(filePath);
        }
        return urlSpec;
    }

    /** Convert a URL specification to a file path */
    public static String filePathForUrlSpec(String urlSpec) throws UrlSpecException {
        String filePath = null;
        try {
            URL url = new URL(urlSpec);
            filePath = url.getPath();
        } catch (Exception excpt) {
            throw new UrlSpecException(urlSpec);
        }
        return filePath;
    }

    /** Convert a URL to a file path */
    public static String filePathForUrl(URL url) {
        return url.getPath();
    }

    /** 
     * Exception for bad file path specification.
     */
    public static class FilePathException extends RuntimeException {

        private String filePath;

        public FilePathException(String newFilePath) {
            filePath = newFilePath;
        }

        public String filePath() {
            return filePath;
        }

        @Override
        public String toString() {
            return "Invalid file path specification: " + filePath;
        }
    }

    /** 
     * Exception for bad file path specification.
     */
    public static class UrlSpecException extends RuntimeException {

        private String urlSpec;

        public UrlSpecException(String newUrlPath) {
            urlSpec = newUrlPath;
        }

        public String urlSpec() {
            return urlSpec;
        }

        @Override
        public String toString() {
            return "Invalid URL path specification: " + urlSpec;
        }
    }
}
