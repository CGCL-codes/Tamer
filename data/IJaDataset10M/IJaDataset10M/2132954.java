package org.jaffa.components.filters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.jaffa.datatypes.Parser;
import org.jaffa.security.SecurityManager;
import org.jaffa.util.StringHelper;
import org.jaffa.util.URLHelper;

/**
 * @author PaulE
 * @version 1.0
 */
public final class FileExplorerFilter implements Filter {

    private static final Logger log = Logger.getLogger(FileExplorerFilter.class);

    /**
     * The filter configuration object we are associated with.  If this value
     * is null, this filter instance is not currently configured.
     */
    private FilterConfig filterConfig = null;

    private String m_docRoot = null;

    private File m_root = null;

    private String m_context = null;

    private ServletContext servletContext;

    private String m_error = null;

    private boolean m_canCreateFolders = false;

    private boolean m_canDeleteFiles = false;

    private boolean m_canDeleteFolders = false;

    private boolean m_canDownloadFiles = false;

    private boolean m_canRecurseFolders = false;

    private boolean m_canRenameFiles = false;

    private boolean m_canRenameFolders = false;

    private boolean m_canShowFolders = false;

    private boolean m_canUploadFiles = false;

    private String[] m_deleteAllowedRoles = null;

    private String[] m_deleteDisallowedRoles = null;

    private String[] m_downloadAllowedRoles = null;

    private String[] m_downloadDisallowedRoles = null;

    private String[] m_fileExcludes = null;

    private String[] m_fileIncludes = null;

    private String[] m_folderExcludes = null;

    private String[] m_folderIncludes = null;

    private String[] m_renameAllowedRoles = null;

    private String[] m_renameDisallowedRoles = null;

    private String[] m_uploadAllowedRoles = null;

    private String[] m_uploadDisallowedRoles = null;

    private String m_renderPage = "/jaffa/jsp/fileExplorer.jsp";

    /** Take this filter out of service. */
    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * Main service method for filter. Expects the requetsing URI to be the file or folder
     * to be processed, and looks for an optional 'action' request parameter to see if
     * and special processing is needed. Example actions are delete,rename,upload. By default
     * this will either display a folder listing, or serve the requested file (based on
     * the security setting configured for this filter)
     * @param response Output from the filter
     * @param request The servlet request we are processing
     * @param chain The filter chain we are processing
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (filterConfig == null) {
            if (m_error != null) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, m_error);
            }
            return;
        }
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            String servletURI = req.getAttribute("javax.servlet.forward.servlet_path");
            log.debug("Make sure context " + req.getRequestURI() + " starts with " + m_context);
            if (req.getRequestURI().startsWith(m_context)) {
                File file = m_root;
                String relName = req.getRequestURI().substring(m_context.length());
                log.debug("Relative Name = " + relName);
                if (relName != null && relName.length() > 0 && !"/".equals(relName)) {
                    file = new File(m_root.getCanonicalPath() + relName.replace('/', File.separatorChar));
                } else relName = "";
                String action = req.getParameter("action");
                if (action == null || action.length() == 0) action = "show";
                log.debug("Action=" + action);
                filterConfig.getServletContext().log(filterConfig.getFilterName() + ": Request for " + relName + " action=" + action);
                if (!file.exists() || file.isHidden()) {
                    returnPageNotFound(req, resp, file, relName);
                    return;
                }
                if ("delete".equals(action)) {
                    if (!canDelete(relName, file)) {
                        returnAccessDenied(req, resp, file, relName, action);
                        return;
                    }
                    throw new ServletException("@TODO - Delete");
                } else if ("rename".equals(action)) {
                    if (!canRename(relName, file)) {
                        returnAccessDenied(req, resp, file, relName, action);
                        return;
                    }
                    throw new ServletException("@TODO - Rename");
                } else if ("upload".equals(action)) {
                    if (!canUpload(relName, file)) {
                        returnAccessDenied(req, resp, file, relName, action);
                        return;
                    }
                    throw new ServletException("@TODO - upload");
                } else if ("show".equals(action)) {
                    if (!canDownload(relName, file)) {
                        returnAccessDenied(req, resp, file, relName, action);
                        return;
                    }
                    if (file.isDirectory()) returnListFiles(req, resp, file, relName); else {
                        if (m_root == null) chain.doFilter(request, response); else returnDownloadFile(req, resp, file, relName);
                    }
                } else {
                    return;
                }
            }
        }
    }

    /**
     * Place this filter into service.
     * <p>Example Filter Configuration
     * <pre>
     * &lt;filter&gt;
     *   &lt;filter-name&gt;MyMusic&lt;/filter-name&gt;
     *   &lt;filter-class&gt;org.jaffa.components.filters.FileExplorerFilter&lt;/filter-class&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;context&lt;/param-name&gt;
     *     &lt;param-value&gt;/test/music&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;docRoot&lt;/param-name&gt;
     *     &lt;param-value&gt;C:/My Music&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;folders.show&lt;/param-name&gt;
     *     &lt;param-value&gt;true&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;folders.recurse&lt;/param-name&gt;
     *     &lt;param-value&gt;true&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;folders.includes&lt;/param-name&gt;
     *     &lt;param-value&gt;**&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;folders.excludes&lt;/param-name&gt;
     *     &lt;param-value&gt;.*,**\/.*,CVS,**\/CVS&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;folders.create.enabled&lt;/param-name&gt;
     *     &lt;param-value&gt;false&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;folders.rename.enabled&lt;/param-name&gt;
     *     &lt;param-value&gt;false&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;folders.delete.enabled&lt;/param-name&gt;
     *     &lt;param-value&gt;false&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;files.includes&lt;/param-name&gt;
     *     &lt;param-value&gt;**&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;files.excludes&lt;/param-name&gt;
     *     &lt;param-value&gt;.*,**\/.*,CVS,**\/CVS&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;files.download.enabled&lt;/param-name&gt;
     *     &lt;param-value&gt;false&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;files.upload.enabled&lt;/param-name&gt;
     *     &lt;param-value&gt;false&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;files.rename.enabled&lt;/param-name&gt;
     *     &lt;param-value&gt;false&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;files.delete.enabled&lt;/param-name&gt;
     *     &lt;param-value&gt;false&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;download.enabled.roles&lt;/param-name&gt;
     *     &lt;param-value&gt;administrator&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;download.disabled.roles&lt;/param-name&gt;
     *     &lt;param-value&gt;guest&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;rename.enabled.roles&lt;/param-name&gt;
     *     &lt;param-value&gt;administrator&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;rename.disabled.roles&lt;/param-name&gt;
     *     &lt;param-value&gt;guest&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;upload.enabled.roles&lt;/param-name&gt;
     *     &lt;param-value&gt;administrator&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;upload.disabled.roles&lt;/param-name&gt;
     *     &lt;param-value&gt;guest&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;delete.enabled.roles&lt;/param-name&gt;
     *     &lt;param-value&gt;administrator&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     *   &lt;init-param&gt;
     *     &lt;param-name&gt;delete.disabled.roles&lt;/param-name&gt;
     *     &lt;param-value&gt;guest&lt;/param-value&gt;
     *   &lt;/init-param&gt;
     * &lt;/filter&gt;
     * </pre>
     * @param filterConfig The filter configuration object
     * @throws ServletException Thrown if there are errors in the configuration parameters
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        servletContext = filterConfig.getServletContext();
        this.filterConfig = filterConfig;
        m_context = filterConfig.getInitParameter("context");
        if (m_context == null) {
            m_error = "Parameter 'context' is required for Filter " + filterConfig.getFilterName();
            log.error(m_error);
            this.filterConfig = null;
            return;
        }
        if (!m_context.startsWith("/")) m_context = "/" + m_context;
        if (m_context.endsWith("/") && m_context.length() > 1) m_context = m_context.substring(0, m_context.length() - 2);
        m_docRoot = filterConfig.getInitParameter("docRoot");
        if (m_docRoot != null) try {
            m_root = new File(m_docRoot);
        } catch (Exception e) {
            m_error = "Document Root '" + m_docRoot + "' not found for Filter " + filterConfig.getFilterName();
            log.error(m_error, e);
            this.filterConfig = null;
            return;
        } else {
            throw new ServletException("@TODO");
        }
        Boolean b = Parser.parseBoolean(filterConfig.getInitParameter("folders.create.enabled"));
        m_canCreateFolders = b == null ? false : b.booleanValue();
        b = Parser.parseBoolean(filterConfig.getInitParameter("files.delete.enabled"));
        m_canDeleteFiles = b == null ? false : b.booleanValue();
        b = Parser.parseBoolean(filterConfig.getInitParameter("folders.delete.enabled"));
        m_canDeleteFolders = b == null ? false : b.booleanValue();
        b = Parser.parseBoolean(filterConfig.getInitParameter("files.download.enabled"));
        m_canDownloadFiles = b == null ? false : b.booleanValue();
        b = Parser.parseBoolean(filterConfig.getInitParameter("folders.recurse"));
        m_canRecurseFolders = b == null ? false : b.booleanValue();
        b = Parser.parseBoolean(filterConfig.getInitParameter("files.rename.enabled"));
        m_canRenameFiles = b == null ? false : b.booleanValue();
        b = Parser.parseBoolean(filterConfig.getInitParameter("folders.rename.enabled"));
        m_canRenameFolders = b == null ? false : b.booleanValue();
        b = Parser.parseBoolean(filterConfig.getInitParameter("folders.show"));
        m_canShowFolders = b == null ? false : b.booleanValue();
        b = Parser.parseBoolean(filterConfig.getInitParameter("files.upload.enabled"));
        m_canUploadFiles = b == null ? false : b.booleanValue();
        m_deleteAllowedRoles = StringHelper.parseString(filterConfig.getInitParameter("delete.enabled.roles"), ",");
        m_deleteDisallowedRoles = StringHelper.parseString(filterConfig.getInitParameter("delete.disabled.roles"), ",");
        m_downloadAllowedRoles = StringHelper.parseString(filterConfig.getInitParameter("download.enabled.roles"), ",");
        m_downloadDisallowedRoles = StringHelper.parseString(filterConfig.getInitParameter("download.disabled.roles"), ",");
        m_fileExcludes = parseRegex(filterConfig.getInitParameter("files.excludes"));
        m_fileIncludes = parseRegex(filterConfig.getInitParameter("files.includes"));
        m_folderExcludes = parseRegex(filterConfig.getInitParameter("folders.excludes"));
        m_folderIncludes = parseRegex(filterConfig.getInitParameter("folders.includes"));
        m_renameAllowedRoles = StringHelper.parseString(filterConfig.getInitParameter("rename.enabled.roles"), ",");
        m_renameDisallowedRoles = StringHelper.parseString(filterConfig.getInitParameter("rename.disabled.roles"), ",");
        m_uploadAllowedRoles = StringHelper.parseString(filterConfig.getInitParameter("upload.enabled.roles"), ",");
        m_uploadDisallowedRoles = StringHelper.parseString(filterConfig.getInitParameter("upload.disabled.roles"), ",");
        log.info("Started Filter " + filterConfig.getFilterName() + ". Context is " + this.m_context + ", DocRoot is " + this.m_docRoot);
    }

    /**
     * Can this file be deleted
     * @param relName
     * @param f
     * @return
     */
    public boolean canDelete(String relName, File f) {
        return (f.isDirectory() ? m_canDeleteFolders && inRange(relName, m_folderIncludes, m_folderExcludes) : m_canDeleteFiles && inRange(relName, m_fileIncludes, m_fileExcludes)) && inRole(m_deleteAllowedRoles, m_deleteDisallowedRoles);
    }

    /**
     * Can this file be downloaded, or traversed if a folder
     * @param relName
     * @param f
     * @return
     */
    public boolean canDownload(String relName, File f) {
        if (f.isDirectory()) {
            boolean inRange = inRange(relName, m_folderIncludes, m_folderExcludes);
            log.debug("canDownload:" + f.getAbsolutePath() + " recurse=:" + m_canRecurseFolders + " , inRange(" + m_folderIncludes + "," + m_folderExcludes + ")=" + inRange);
            return m_canRecurseFolders && inRange;
        } else {
            boolean inRange = inRange(relName, m_folderIncludes, m_folderExcludes);
            log.debug("canDownload:" + f.getAbsolutePath() + " download=:" + m_canRecurseFolders + " , inRange(" + m_fileIncludes + "," + m_fileExcludes + ")=" + inRange);
            return m_canDownloadFiles && inRange;
        }
    }

    /**
     * Can this file be renamed
     * @param relName
     * @param f
     * @return
     */
    public boolean canRename(String relName, File f) {
        return (f.isDirectory() ? m_canRenameFolders && inRange(relName, m_folderIncludes, m_folderExcludes) : m_canRenameFiles && inRange(relName, m_fileIncludes, m_fileExcludes)) && inRole(m_renameAllowedRoles, m_renameDisallowedRoles);
    }

    /**
     * Can this folder be uploaded to
     * @param relName
     * @param f
     * @return
     */
    public boolean canUpload(String relName, File f) {
        return (f.isDirectory() ? m_canUploadFiles && inRole(m_uploadAllowedRoles, m_uploadDisallowedRoles) : false);
    }

    /**
     * Does file name match the include/exclude clause
     * @param name
     * @param includes
     * @param excludes
     * @return
     */
    public static boolean inRange(String name, String[] includes, String[] excludes) {
        if (name == null) return false;
        if (includes != null && includes.length > 0) for (int i = 0; i < includes.length; i++) if (name.matches(includes[i])) return true;
        if (excludes != null && excludes.length > 0) for (int i = 0; i < excludes.length; i++) if (name.matches(excludes[i])) return false;
        return includes == null || includes.length == 0;
    }

    /**
     * Does the users current access name match the include/exclude role clause
     * @param includes
     * @param excludes
     * @return
     */
    public static boolean inRole(String[] includes, String[] excludes) {
        List l = SecurityManager.getUserRoles();
        if (l == null) return false;
        for (Iterator it = l.iterator(); it.hasNext(); ) {
            String role = (String) it.next();
            if (includes != null && includes.length > 0) for (int i = 0; i < includes.length; i++) if (role.matches(includes[i])) return true;
        }
        for (Iterator it = l.iterator(); it.hasNext(); ) {
            String role = (String) it.next();
            if (excludes != null && excludes.length > 0) for (int i = 0; i < excludes.length; i++) if (role.matches(excludes[i])) return false;
        }
        return true;
    }

    /**
     * Convert comma seperated 'ant like' filter list, and make it a list of regular expressions
     * @param list
     * @return
     */
    public static String[] parseRegex(String list) {
        String[] x = null;
        if (list != null && list.length() > 0) {
            x = StringHelper.parseString(list, ",");
            if (x != null && x.length > 0) for (int i = 0; i < x.length; i++) {
                x[i] = StringHelper.replace(x[i], ".", "\0x01");
                x[i] = StringHelper.replace(x[i], "**", "\0x02");
                x[i] = StringHelper.replace(x[i], "*", "[^/\\\\]*");
                x[i] = StringHelper.replace(x[i], "\0x01", "\\.");
                x[i] = StringHelper.replace(x[i], "\0x02", ".*");
            }
        }
        return x;
    }

    private void returnPageNotFound(HttpServletRequest request, HttpServletResponse response, File file, String relName) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, relName);
        log.info("Page Not Found For " + relName);
    }

    private void returnAccessDenied(HttpServletRequest request, HttpServletResponse response, File file, String relName, String action) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, relName);
        log.info("Access Denied For " + relName);
    }

    private void returnListFiles(HttpServletRequest request, HttpServletResponse response, File path, String relName) throws ServletException, IOException {
        log.info("Get List of File For " + relName);
        FileExplorerBean bean = new FileExplorerBean();
        bean.setFolderName(relName);
        bean.setParentAvailable(relName != null && relName.length() > 0);
        bean.setUploadAllowed(canUpload(relName, path));
        File[] dirs = path.listFiles(new FolderFilter(m_root.getAbsolutePath().length()));
        if (dirs != null) for (int i = 0; i < dirs.length; i++) {
            log.debug(relName + ") Found Folder " + dirs[i].getAbsolutePath());
            String name = relName + "/" + dirs[i].getName();
            bean.addFolder(dirs[i], canDownload(name, dirs[i]), canRename(name, dirs[i]), canDelete(name, dirs[i]));
        }
        File[] files = path.listFiles(new FileFilter(m_root.getAbsolutePath().length()));
        if (files != null) for (int i = 0; i < files.length; i++) {
            log.debug(relName + ") Found File " + files[i].getAbsolutePath());
            String name = relName + "/" + files[i].getName();
            bean.addFile(files[i], canDownload(name, files[i]), canRename(name, files[i]), canDelete(name, files[i]));
        }
        request.setAttribute("FileExplorerBean", bean);
        log.debug("Forwarding to " + m_renderPage);
        filterConfig.getServletContext().getRequestDispatcher(m_renderPage).forward(request, response);
        return;
    }

    private void returnDownloadFile(HttpServletRequest request, HttpServletResponse response, File file, String relName) throws IOException {
        String contentType = filterConfig.getServletContext().getMimeType(relName);
        if (contentType != null) response.setContentType(contentType);
        try {
            response.setBufferSize(2048);
        } catch (IllegalStateException e) {
        }
        InputStream in = new FileInputStream(file);
        ServletOutputStream out = response.getOutputStream();
        try {
            byte[] buffer = new byte[1000];
            while (in.available() > 0) out.write(buffer, 0, in.read(buffer));
            out.flush();
        } catch (IOException e) {
            log.error("Problem Serving Resource " + relName, e);
        } finally {
            out.close();
        }
    }

    /** class used to filter for directories */
    class FolderFilter implements FilenameFilter {

        private int rootLength = 0;

        public FolderFilter(int rootLength) {
            this.rootLength = rootLength;
        }

        /** Tests if a specified file should be included in a file list.
         *
         * @param dir the directory in which the file was found
         * @param name the name of the file
         * @return <CODE>true</CODE> if and only if the name should be included in the file list; <CODE>false</CODE> otherwise
         */
        public boolean accept(File dir, String name) {
            File f = new File(dir, name);
            String relName = f.getPath().substring(rootLength);
            boolean accept = f.isDirectory() && (m_canShowFolders && inRange(relName, m_folderIncludes, m_folderExcludes));
            log.debug("Folder " + name + " included? " + accept);
            return accept;
        }
    }

    /** class used to filter for images */
    class FileFilter implements FilenameFilter {

        private int rootLength = 0;

        public FileFilter(int rootLength) {
            this.rootLength = rootLength;
        }

        /** Tests if a specified file should be included in a file list.
         *
         * @param dir the directory in which the file was found
         * @param name the name of the file
         * @return <CODE>true</CODE> if and only if the name should be included in the file list; <CODE>false</CODE> otherwise
         */
        public boolean accept(File dir, String name) {
            File f = new File(dir, name);
            String relName = f.getPath().substring(rootLength);
            boolean accept = !f.isDirectory() && inRange(relName, m_fileIncludes, m_fileExcludes);
            log.debug("File " + name + " included? " + accept);
            return accept;
        }
    }

    public static String printArray(String[] s) {
        StringBuffer sb = new StringBuffer("[");
        if (s == null) sb.append("null"); else for (int i = 0; i < s.length; i++) sb.append(i == 0 ? "" : ",").append('"').append(s[i]).append('"');
        return sb.append("]").toString();
    }

    /** Test Routine
     * @param args no args required
     */
    public static void main(String[] args) {
        String[] includes = parseRegex("**");
        String[] excludes = parseRegex(".*,**/.*,CVS,**/CVS");
        System.out.println("Includes=" + printArray(includes));
        System.out.println("Excludes=" + printArray(excludes));
        String file = null;
        System.out.println("In Range: " + file + "=" + inRange(file, includes, excludes));
        file = "";
        System.out.println("In Range: " + file + "=" + inRange(file, includes, excludes));
        file = "hello.test";
        System.out.println("In Range: " + file + "=" + inRange(file, includes, excludes));
        file = "me/hello.test";
        System.out.println("In Range: " + file + "=" + inRange(file, includes, excludes));
        file = "CVS/hello.test";
        System.out.println("In Range: " + file + "=" + inRange(file, includes, excludes));
        file = ".me";
        System.out.println("In Range: " + file + "=" + inRange(file, includes, excludes));
        file = "me/.test";
        System.out.println("In Range: " + file + "=" + inRange(file, includes, excludes));
    }
}
