package com.ecyrd.jspwiki.attachment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.security.Permission;
import java.security.Principal;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ecyrd.jspwiki.*;
import com.ecyrd.jspwiki.auth.AuthorizationManager;
import com.ecyrd.jspwiki.auth.permissions.PermissionFactory;
import com.ecyrd.jspwiki.dav.AttachmentDavProvider;
import com.ecyrd.jspwiki.dav.DavPath;
import com.ecyrd.jspwiki.dav.DavProvider;
import com.ecyrd.jspwiki.dav.WebdavServlet;
import com.ecyrd.jspwiki.dav.methods.DavMethod;
import com.ecyrd.jspwiki.dav.methods.PropFindMethod;
import com.ecyrd.jspwiki.filters.RedirectException;
import com.ecyrd.jspwiki.i18n.InternationalizationManager;
import com.ecyrd.jspwiki.providers.ProviderException;
import com.ecyrd.jspwiki.ui.progress.ProgressItem;
import com.ecyrd.jspwiki.util.HttpUtil;

/**
 *  This is the chief JSPWiki attachment management servlet.  It is used for
 *  both uploading new content and downloading old content.  It can handle
 *  most common cases, e.g. check for modifications and return 304's as necessary.
 *  <p>
 *  Authentication is done using JSPWiki's normal AAA framework.
 *  <p>
 *  This servlet is also capable of managing dynamically created attachments.
 *
 *  @author Erik Bunn
 *
 *  @since 1.9.45.
 */
public class AttachmentServlet extends WebdavServlet {

    private static final int BUFFER_SIZE = 8192;

    private static final long serialVersionUID = 3257282552187531320L;

    private WikiEngine m_engine;

    static Logger log = Logger.getLogger(AttachmentServlet.class.getName());

    private static final String HDR_VERSION = "version";

    /** Default expiry period is 1 day */
    protected static final long DEFAULT_EXPIRY = 1 * 24 * 60 * 60 * 1000;

    private String m_tmpDir;

    private DavProvider m_attachmentProvider;

    /**
     *  The maximum size that an attachment can be.
     */
    private int m_maxSize = Integer.MAX_VALUE;

    /**
     *  List of attachment types which are allowed
     */
    private String[] m_allowedPatterns;

    private String[] m_forbiddenPatterns;

    /**
     *  Initializes the servlet from WikiEngine properties.
     *   
     *  {@inheritDoc}
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        m_engine = WikiEngine.getInstance(config);
        Properties props = m_engine.getWikiProperties();
        m_attachmentProvider = new AttachmentDavProvider(m_engine);
        m_tmpDir = m_engine.getWorkDir() + File.separator + "attach-tmp";
        m_maxSize = TextUtil.getIntegerProperty(props, AttachmentManager.PROP_MAXSIZE, Integer.MAX_VALUE);
        String allowed = TextUtil.getStringProperty(props, AttachmentManager.PROP_ALLOWEDEXTENSIONS, null);
        if (allowed != null && allowed.length() > 0) m_allowedPatterns = allowed.toLowerCase().split("\\s"); else m_allowedPatterns = new String[0];
        String forbidden = TextUtil.getStringProperty(props, AttachmentManager.PROP_FORDBIDDENEXTENSIONS, null);
        if (forbidden != null && forbidden.length() > 0) m_forbiddenPatterns = forbidden.toLowerCase().split("\\s"); else m_forbiddenPatterns = new String[0];
        File f = new File(m_tmpDir);
        log.log(Level.SEVERE, "A file already exists where the temporary dir is supposed to be: " + m_tmpDir + ".  Please remove it.");
        log.log(Level.INFO, "UploadServlet initialized. Using " + m_tmpDir + " for temporary storage.");
    }

    private boolean isTypeAllowed(String name) {
        if (name == null || name.length() == 0) return false;
        name = name.toLowerCase();
        for (int i = 0; i < m_forbiddenPatterns.length; i++) {
            if (name.endsWith(m_forbiddenPatterns[i]) && m_forbiddenPatterns[i].length() > 0) return false;
        }
        for (int i = 0; i < m_allowedPatterns.length; i++) {
            if (name.endsWith(m_allowedPatterns[i]) && m_allowedPatterns[i].length() > 0) return true;
        }
        return m_allowedPatterns.length == 0;
    }

    /**
     *  Implements the PROPFIND method.
     *  
     *  @param req The servlet request
     *  @param res The servlet response
     *  @throws IOException If input/output fails
     *  @throws ServletException If the servlet has issues
     */
    public void doPropFind(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        DavMethod dm = new PropFindMethod(m_attachmentProvider);
        String p = new String(req.getPathInfo().getBytes("ISO-8859-1"), "UTF-8");
        DavPath path = new DavPath(p);
        dm.execute(req, res, path);
    }

    /**
     *  Implements the OPTIONS method.
     *  
     *  @param req The servlet request
     *  @param res The servlet response
     */
    protected void doOptions(HttpServletRequest req, HttpServletResponse res) {
        res.setHeader("DAV", "1");
        res.setHeader("Allow", "GET, PUT, POST, OPTIONS, PROPFIND, PROPPATCH, MOVE, COPY, DELETE");
        res.setStatus(HttpServletResponse.SC_OK);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        WikiContext context = m_engine.createContext(req, WikiContext.ATTACH);
        String version = req.getParameter(HDR_VERSION);
        String nextPage = req.getParameter("nextpage");
        String msg = "An error occurred. Ouch.";
        int ver = WikiProvider.LATEST_VERSION;
        AttachmentManager mgr = m_engine.getAttachmentManager();
        AuthorizationManager authmgr = m_engine.getAuthorizationManager();
        String page = context.getPage().getName();
        if (page == null) {
            log.log(Level.INFO, "Invalid attachment name.");
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        OutputStream out = null;
        InputStream in = null;
        try {
            log.log(Level.INFO, "Attempting to download att " + page + ", version " + version);
            if (version != null) {
                ver = Integer.parseInt(version);
            }
            Attachment att = mgr.getAttachmentInfo(page, ver);
            if (att != null) {
                Permission permission = PermissionFactory.getPagePermission(att, "view");
                if (!authmgr.checkPermission(context.getWikiSession(), permission)) {
                    log.log(Level.INFO, "User does not have permission for this");
                    res.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                if (HttpUtil.checkFor304(req, att)) {
                    log.log(Level.INFO, "Client has latest version already, sending 304...");
                    res.sendError(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }
                String mimetype = getMimeType(context, att.getFileName());
                res.setContentType(mimetype);
                res.addHeader("Content-Disposition", "inline; filename=\"" + att.getFileName() + "\";");
                res.addDateHeader("Last-Modified", att.getLastModified().getTime());
                if (!att.isCacheable()) {
                    res.addHeader("Pragma", "no-cache");
                    res.addHeader("Cache-control", "no-cache");
                }
                if (att.getSize() >= 0) {
                    res.setContentLength((int) att.getSize());
                }
                out = res.getOutputStream();
                in = mgr.getAttachmentStream(context, att);
                int read = 0;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((read = in.read(buffer)) > -1) {
                    out.write(buffer, 0, read);
                }
                if (log.isLoggable(Level.INFO)) {
                    msg = "Attachment " + att.getFileName() + " sent to " + req.getRemoteUser() + " on " + req.getRemoteAddr();
                    log.log(Level.INFO, msg);
                }
                if (nextPage != null) res.sendRedirect(nextPage);
                return;
            }
            msg = "Attachment '" + page + "', version " + ver + " does not exist.";
            log.log(Level.INFO, msg);
            res.sendError(HttpServletResponse.SC_NOT_FOUND, msg);
            return;
        } catch (ProviderException pe) {
            msg = "Provider error: " + pe.getMessage();
            log.log(Level.INFO, "Provider failed while reading", pe);
            try {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg);
            } catch (IllegalStateException e) {
            }
            return;
        } catch (NumberFormatException nfe) {
            msg = "Invalid version number (" + version + ")";
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
            return;
        } catch (SocketException se) {
            log.log(Level.INFO, "I/O exception during download", se);
            return;
        } catch (IOException ioe) {
            msg = "Error: " + ioe.getMessage();
            log.log(Level.INFO, "I/O exception during download", ioe);
            try {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg);
            } catch (IllegalStateException e) {
            }
            return;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     *  Returns the mime type for this particular file.  Case does not matter.
     *
     * @param ctx WikiContext; required to access the ServletContext of the request.
     * @param fileName The name to check for.
     * @return A valid mime type, or application/binary, if not recognized
     */
    private static String getMimeType(WikiContext ctx, String fileName) {
        String mimetype = null;
        HttpServletRequest req = ctx.getHttpRequest();
        if (req != null) {
            ServletContext s = req.getSession().getServletContext();
            if (s != null) {
                mimetype = s.getMimeType(fileName.toLowerCase());
            }
        }
        if (mimetype == null) {
            mimetype = "application/binary";
        }
        return mimetype;
    }

    /**
     * Grabs mime/multipart data and stores it into the temporary area.
     * Uses other parameters to determine which name to store as.
     *
     * <p>The input to this servlet is generated by an HTML FORM with
     * two parts. The first, named 'page', is the WikiName identifier
     * for the parent file. The second, named 'content', is the binary
     * content of the file.
     * 
     * {@inheritDoc}
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try {
            String nextPage = upload(req);
            req.getSession().removeAttribute("msg");
            res.sendRedirect(nextPage);
        } catch (RedirectException e) {
            WikiSession session = WikiSession.getWikiSession(m_engine, req);
            session.addMessage(e.getMessage());
            req.getSession().setAttribute("msg", e.getMessage());
            res.sendRedirect(e.getRedirect());
        }
    }

    /**
     *  {@inheritDoc}
     */
    public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String errorPage = m_engine.getURL(WikiContext.ERROR, "", null, false);
        String p = new String(req.getPathInfo().getBytes("ISO-8859-1"), "UTF-8");
        DavPath path = new DavPath(p);
        try {
            InputStream data = req.getInputStream();
            WikiContext context = m_engine.createContext(req, WikiContext.UPLOAD);
            String wikipage = path.get(0);
            errorPage = context.getURL(WikiContext.UPLOAD, wikipage);
            String changeNote = null;
            boolean created = executeUpload(context, data, path.getName(), errorPage, wikipage, changeNote, req.getContentLength());
            if (created) res.sendError(HttpServletResponse.SC_CREATED); else res.sendError(HttpServletResponse.SC_OK);
        } catch (ProviderException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (RedirectException e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     *  Validates the next page to be on the same server as this webapp.
     *  Fixes [JSPWIKI-46].
     */
    private String validateNextPage(String nextPage, String errorPage) {
        if (nextPage.indexOf("://") != -1) {
            if (!nextPage.startsWith(m_engine.getBaseURL())) {
                log.log(Level.WARNING, "Detected phishing attempt by redirecting to an unsecure location: " + nextPage);
                nextPage = errorPage;
            }
        }
        return nextPage;
    }

    /**
     *  Uploads a specific mime multipart input set, intercepts exceptions.
     *
     *  @param req The servlet request
     *  @return The page to which we should go next.
     *  @throws RedirectException If there's an error and a redirection is needed
     *  @throws IOException If upload fails
     * @throws FileUploadException 
     */
    @SuppressWarnings("unchecked")
    protected String upload(HttpServletRequest req) throws RedirectException, IOException {
        String msg = "";
        String attName = "(unknown)";
        String errorPage = m_engine.getURL(WikiContext.ERROR, "", null, false);
        String nextPage = errorPage;
        String progressId = req.getParameter("progressid");
        if (!ServletFileUpload.isMultipartContent(req)) {
            throw new RedirectException("Not a file upload", errorPage);
        }
        try {
            FileItemFactory factory = new DiskFileItemFactory();
            WikiContext context = m_engine.createContext(req, WikiContext.ATTACH);
            UploadListener pl = new UploadListener();
            m_engine.getProgressManager().startProgress(pl, progressId);
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding("UTF-8");
            upload.setFileSizeMax(m_maxSize);
            upload.setProgressListener(pl);
            List<FileItem> items = upload.parseRequest(req);
            String wikipage = null;
            String changeNote = null;
            FileItem actualFile = null;
            for (FileItem item : items) {
                if (item.isFormField()) {
                    if (item.getFieldName().equals("page")) {
                        wikipage = item.getString("UTF-8");
                        int x = wikipage.indexOf("/");
                        if (x != -1) wikipage = wikipage.substring(0, x);
                    } else if (item.getFieldName().equals("changenote")) {
                        changeNote = item.getString("UTF-8");
                    } else if (item.getFieldName().equals("nextpage")) {
                        nextPage = validateNextPage(item.getString("UTF-8"), errorPage);
                    }
                } else {
                    actualFile = item;
                }
            }
            if (actualFile == null) throw new RedirectException("Broken file upload", errorPage);
            String filename = actualFile.getName();
            long fileSize = actualFile.getSize();
            InputStream in = actualFile.getInputStream();
            try {
                executeUpload(context, in, filename, nextPage, wikipage, changeNote, fileSize);
            } finally {
                in.close();
            }
        } catch (ProviderException e) {
            msg = "Upload failed because the provider failed: " + e.getMessage();
            log.log(Level.WARNING, msg + " (attachment: " + attName + ")", e);
            throw new IOException(msg);
        } catch (IOException e) {
            msg = "Upload failure: " + e.getMessage();
            log.log(Level.WARNING, msg + " (attachment: " + attName + ")", e);
            throw e;
        } catch (FileUploadException e) {
            msg = "Upload failure: " + e.getMessage();
            log.log(Level.WARNING, msg + " (attachment: " + attName + ")", e);
            throw new IOException(msg);
        } finally {
            m_engine.getProgressManager().stopProgress(progressId);
        }
        return nextPage;
    }

    /**
     *
     * @param context the wiki context
     * @param data the input stream data
     * @param filename the name of the file to upload
     * @param errorPage the place to which you want to get a redirection
     * @param parentPage the page to which the file should be attached
     * @param changenote The change note
     * @param contentLength The content length
     * @return <code>true</code> if upload results in the creation of a new page;
     * <code>false</code> otherwise
     * @throws RedirectException If the content needs to be redirected
     * @throws IOException       If there is a problem in the upload.
     * @throws ProviderException If there is a problem in the backend.
     */
    protected boolean executeUpload(WikiContext context, InputStream data, String filename, String errorPage, String parentPage, String changenote, long contentLength) throws RedirectException, IOException, ProviderException {
        boolean created = false;
        try {
            filename = AttachmentManager.validateFileName(filename);
        } catch (WikiException e) {
            throw new RedirectException(context.getBundle(InternationalizationManager.CORE_BUNDLE).getString(e.getMessage()), errorPage);
        }
        if (!context.hasAdminPermissions()) {
            if (contentLength > m_maxSize) {
                throw new RedirectException("File exceeds maximum size (" + m_maxSize + " bytes)", errorPage);
            }
            if (!isTypeAllowed(filename)) {
                throw new RedirectException("Files of this type may not be uploaded to this wiki", errorPage);
            }
        }
        Principal user = context.getCurrentUser();
        AttachmentManager mgr = m_engine.getAttachmentManager();
        log.log(Level.INFO, "file=" + filename);
        if (data == null) {
            log.log(Level.SEVERE, "File could not be opened.");
            throw new RedirectException("File could not be opened.", errorPage);
        }
        Attachment att = mgr.getAttachmentInfo(context.getPage().getName());
        if (att == null) {
            att = new Attachment(m_engine, parentPage, filename);
            created = true;
        }
        att.setSize(contentLength);
        Permission permission = PermissionFactory.getPagePermission(att, "upload");
        if (m_engine.getAuthorizationManager().checkPermission(context.getWikiSession(), permission)) {
            if (user != null) {
                att.setAuthor(user.getName());
            }
            if (changenote != null && changenote.length() > 0) {
                att.setAttribute(WikiPage.CHANGENOTE, changenote);
            }
            try {
                m_engine.getAttachmentManager().storeAttachment(att, data);
            } catch (ProviderException pe) {
                throw new ProviderException(context.getBundle(InternationalizationManager.CORE_BUNDLE).getString(pe.getMessage()));
            }
            log.log(Level.INFO, "User " + user + " uploaded attachment to " + parentPage + " called " + filename + ", size " + att.getSize());
        } else {
            throw new RedirectException("No permission to upload a file", errorPage);
        }
        return created;
    }

    /**
     *  Provides tracking for upload progress.
     *  
     *  @author Janne Jalkanen
     */
    private static class UploadListener extends ProgressItem implements ProgressListener {

        public long m_currentBytes;

        public long m_totalBytes;

        public String m_uid;

        public void update(long recvdBytes, long totalBytes, int item) {
            m_currentBytes = recvdBytes;
            m_totalBytes = totalBytes;
        }

        public int getProgress() {
            return (int) (((float) m_currentBytes / m_totalBytes) * 100 + 0.5);
        }
    }
}
