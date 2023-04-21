package com.webmotix.core;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.webmotix.facade.MotixFacadeInsert;

/**
 * @author Sameer Charles
 * @version 2.0 $Id: Path.java 6357 2006-09-13 12:18:04Z scharles $
 */
public final class Path {

    /**
	 * Logger.
	 */
    private static final Logger log = LoggerFactory.getLogger(Path.class);

    private static final String DEFAULT_UNTITLED_NODE_NAME = "untitled";

    private static final String ENCODING_DEFAULT = "UTF-8";

    private static final String JAVAX_FORWARD_SERVLET_PATH = "javax.servlet.forward.servlet_path";

    private static final String MGNL_REQUEST_URI_DECODED = "mgnl.request.uri.decoded";

    /**
     * Utility class, don't instantiate.
     */
    private Path() {
    }

    /**
     * Gets the temporary directory path (cms.upload.tmpdir) as set with Java options while startup or in web.xml.
     * @return Temporary directory path
     */
    public static String getTempDirectoryPath() {
        return getTempDirectory().getAbsolutePath();
    }

    public static File getTempDirectory() {
        String path = SystemProperty.getProperty(SystemProperty.WEBMOTIX_UPLOAD_TMPDIR);
        File dir = isAbsolute(path) ? new File(path) : new File(Path.getAppRootDir(), path);
        dir.mkdirs();
        return dir;
    }

    /**
     * Gets repositories file location as set with Java options while startup or in web.xml.
     * @return file location
     */
    public static String getRepositoriesConfigFilePath() {
        return getRepositoriesConfigFile().getAbsolutePath();
    }

    public static File getRepositoriesConfigFile() {
        String path = SystemProperty.getProperty(SystemProperty.WEBMOTIX_REPOSITORIES_CONFIG);
        return isAbsolute(path) ? new File(path) : new File(Path.getAppRootDir(), path);
    }

    /**
     * Verifica se existe o arquivo no local
     * @param path
     * @return
     */
    public static boolean existsFile(String path) {
        return new File(Path.getAppRootDir(), path).exists();
    }

    /**
     * Gets the root directory for the motix web application.
     * @return motix root dir
     */
    public static File getAppRootDir() {
        return new File(SystemProperty.getProperty(SystemProperty.WEBMOTIX_APP_ROOTDIR));
    }

    /**
     * Gets absolute filesystem path, adds application root if path is not absolute
     */
    public static String getAbsoluteFileSystemPath(String path) {
        if (Path.isAbsolute(path)) {
            return path;
        }
        return new File(Path.getAppRootDir(), path).getAbsolutePath();
    }

    /**
     * Returns the URI of the current request, without the context path.
     * @param req request
     * @return request URI without servlet context
     */
    public static String getURI(HttpServletRequest req) {
        String uri = (String) req.getAttribute(MGNL_REQUEST_URI_DECODED);
        if (uri == null) {
            uri = getDecodedURI(req);
            req.setAttribute(MGNL_REQUEST_URI_DECODED, uri);
        }
        return uri;
    }

    public static void setURI(String uri, HttpServletRequest req) {
        req.setAttribute(MGNL_REQUEST_URI_DECODED, uri);
    }

    /**
     * Returns the URI of the current request, but uses the uri to repository mapping to remove any prefix.
     * @param req request
     * @return request URI without servlet context and without repository mapping prefix
     */
    public static String getHandle(HttpServletRequest req) {
        return (String) req.getAttribute(Aggregator.HANDLE);
    }

    /**
     * If you forward a request, this will return the original requests uri.
     * @param req
     */
    public static String getOriginalURI(HttpServletRequest req) {
        return (String) req.getAttribute(JAVAX_FORWARD_SERVLET_PATH);
    }

    /**
     * Returns the decoded URI of the current request, without the context path.
     * @param req request
     * @return request URI without servlet context
     */
    private static String getDecodedURI(HttpServletRequest req) {
        String encoding = StringUtils.defaultString(req.getCharacterEncoding(), ENCODING_DEFAULT);
        String decodedURL = null;
        try {
            decodedURL = URLDecoder.decode(req.getRequestURI(), encoding);
        } catch (UnsupportedEncodingException e) {
            decodedURL = req.getRequestURI();
        }
        return StringUtils.substringAfter(decodedURL, req.getContextPath());
    }

    public static String getExtension(HttpServletRequest req) {
        String ext = (String) req.getAttribute(Aggregator.EXTENSION);
        if (ext == null) {
            ext = StringUtils.substringAfterLast(req.getRequestURI(), ".");
            req.setAttribute(Aggregator.EXTENSION, ext);
        }
        return ext;
    }

    public static boolean isAbsolute(String path) {
        if (path == null) {
            return false;
        }
        if (path.startsWith("/") || path.startsWith(File.separator)) {
            return true;
        }
        if (path.length() >= 3 && Character.isLetter(path.charAt(0)) && path.charAt(1) == ':') {
            return true;
        }
        return false;
    }

    /**
     * <p>
     * Replace illegal characters by [_] [0-9], [A-Z], [a-z], [-], [_]
     * </p>
     * @param label label to validate
     * @return validated label
     */
    public static String getValidatedLabel(String label) {
        StringBuffer s = new StringBuffer(label);
        StringBuffer newLabel = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            int charCode = s.charAt(i);
            if (((charCode >= 48) && (charCode <= 57)) || ((charCode >= 65) && (charCode <= 90)) || ((charCode >= 97) && (charCode <= 122)) || charCode == 45 || charCode == 95) {
                newLabel.append(s.charAt(i));
            } else {
                newLabel.append("-");
            }
        }
        if (newLabel.length() == 0) {
            newLabel.append(DEFAULT_UNTITLED_NODE_NAME);
        }
        return newLabel.toString();
    }

    /**
     * @param baseName
     */
    private static String createUniqueName(String baseName) {
        int pos;
        for (pos = baseName.length() - 1; pos >= 0; pos--) {
            char c = baseName.charAt(pos);
            if (c < '0' || c > '9') {
                break;
            }
        }
        String base;
        int cnt;
        if (pos == -1) {
            if (baseName.length() > 1) {
                pos = baseName.length() - 2;
            }
        }
        if (pos == -1) {
            base = baseName;
            cnt = -1;
        } else {
            pos++;
            base = baseName.substring(0, pos);
            if (pos == baseName.length()) {
                cnt = -1;
            } else {
                cnt = new Integer(baseName.substring(pos)).intValue();
            }
        }
        return (base + ++cnt);
    }

    public static String getAbsolutePath(String path, String label) {
        if (StringUtils.isEmpty(path) || (path.equals("/"))) {
            return "/" + label;
        }
        return path + "/" + label;
    }

    public static String getAbsolutePath(String path) {
        if (!path.startsWith("/")) {
            return "/" + path;
        }
        return path;
    }

    public static String getNodePath(String path, String label) {
        if (StringUtils.isEmpty(path) || (path.equals("/"))) {
            return label;
        }
        return getNodePath(path + "/" + label);
    }

    public static String getNodePath(String path) {
        if (path.startsWith("/")) {
            return path.replaceFirst("/", StringUtils.EMPTY);
        }
        return path;
    }

    public static String getParentPath(String path) {
        int lastIndexOfSlash = path.lastIndexOf("/");
        if (lastIndexOfSlash > 0) {
            return StringUtils.substringBefore(path, "/");
        }
        return "/";
    }

    public static String getTemplateJSPPath() {
        final String template = StringUtils.replace(SystemParameter.getProperty(SystemParameter.WEBMOTIX_TEMPLATE_ROOTDIR), "/", File.separator);
        String path = new File(SystemProperty.getProperty(SystemProperty.WEBMOTIX_APP_ROOTDIR) + template).getAbsolutePath();
        if (StringUtils.upperCase(System.getProperty("os.name")).contains("WINDOWS")) {
            path = path.replace("\\", "\\\\\\\\");
        }
        return path;
    }
}
