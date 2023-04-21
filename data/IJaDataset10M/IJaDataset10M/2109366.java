package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.TransferLog;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.ArchiveHelper;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.FileDescriptorParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.hibernate.Session;

public class DownloadFileServlet extends HttpServlet {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadFileServlet.class);

    private FileDescriptorParser parser = null;

    private ArchiveHelper archiveHelper = new ArchiveHelper();

    private String serverName = "";

    public void init() {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        serverName = req.getServerName();
        if (Constants.REQUIRE_SECURE_REMOTE && !req.isSecure()) {
            if (req.getRemoteAddr().equals(InetAddress.getLocalHost().getHostAddress()) || req.getRemoteAddr().equals("127.0.0.1") || InetAddress.getByName(req.getRemoteAddr()).isLoopbackAddress()) {
                log.debug("Requested from local host");
            } else {
                log.error("Accessing secure command over non-secure line from remote host is not allowed");
                response.setContentType("text/html");
                response.getOutputStream().println("<html><head><title>Error</title></head>");
                response.getOutputStream().println("<body><b>");
                response.getOutputStream().println("Secure connection is required. Prefix your request with 'https: " + "<br>");
                response.getOutputStream().println("</body>");
                response.getOutputStream().println("</html>");
                return;
            }
        }
        if (req.getParameter("mode") != null && !req.getParameter("mode").equals("")) {
            archiveHelper.setMode(req.getParameter("mode"));
        }
        parser = (FileDescriptorParser) req.getSession().getAttribute(CacheFileDownloadList.SESSION_KEY_FILE_DESCRIPTOR_PARSER);
        if (parser == null) {
            log.error("Unable to get file descriptor parser from session");
            return;
        }
        SecurityAdvisor secAdvisor = null;
        try {
            secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
            if (secAdvisor != null) {
                response.setContentType("application/x-download");
                response.setHeader("Content-Disposition", "attachment;filename=gnomex.zip");
                response.setHeader("Cache-Control", "max-age=0, must-revalidate");
                Session sess = secAdvisor.getHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");
                DictionaryHelper dh = DictionaryHelper.getInstance(sess);
                archiveHelper.setTempDir(dh.getPropertyDictionary(PropertyDictionary.TEMP_DIRECTORY));
                parser.parse();
                ZipOutputStream zipOut = null;
                TarArchiveOutputStream tarOut = null;
                if (archiveHelper.isZipMode()) {
                    zipOut = new ZipOutputStream(response.getOutputStream());
                } else {
                    tarOut = new TarArchiveOutputStream(response.getOutputStream());
                }
                int totalArchiveSize = 0;
                for (Iterator i = parser.getRequestNumbers().iterator(); i.hasNext(); ) {
                    String requestNumber = (String) i.next();
                    Request request = findRequest(sess, requestNumber);
                    if (request == null) {
                        log.error("Unable to find request " + requestNumber + ".  Bypassing download for user " + req.getUserPrincipal().getName() + ".");
                        continue;
                    }
                    if (!secAdvisor.canRead(request)) {
                        log.error("Insufficient permissions to read request " + requestNumber + ".  Bypassing download for user " + req.getUserPrincipal().getName() + ".");
                        continue;
                    }
                    List fileDescriptors = parser.getFileDescriptors(requestNumber);
                    for (Iterator i1 = fileDescriptors.iterator(); i1.hasNext(); ) {
                        FileDescriptor fd = (FileDescriptor) i1.next();
                        if (fd.getType().equals("dir")) {
                            continue;
                        }
                        TransferLog xferLog = new TransferLog();
                        xferLog.setFileName(fd.getZipEntryName());
                        xferLog.setStartDateTime(new java.util.Date(System.currentTimeMillis()));
                        xferLog.setTransferType(TransferLog.TYPE_DOWNLOAD);
                        xferLog.setTransferMethod(TransferLog.METHOD_HTTP);
                        xferLog.setPerformCompression("Y");
                        xferLog.setIdRequest(request.getIdRequest());
                        xferLog.setIdLab(request.getIdLab());
                        String requestNumberBase = Request.getBaseRequestNumber(requestNumber);
                        if (!requestNumberBase.equalsIgnoreCase(fd.getMainFolderName(dh, serverName))) {
                            boolean isAuthorizedDirectory = false;
                            for (Iterator i2 = request.getSequenceLanes().iterator(); i2.hasNext(); ) {
                                SequenceLane lane = (SequenceLane) i2.next();
                                if (lane.getFlowCellChannel() != null && lane.getFlowCellChannel().getFlowCell().getNumber().equals(fd.getMainFolderName(dh, serverName))) {
                                    isAuthorizedDirectory = true;
                                    break;
                                }
                            }
                            if (!isAuthorizedDirectory) {
                                log.error("Request number " + requestNumber + " does not correspond to the directory " + fd.getMainFolderName(dh, serverName) + " for attempted download on " + fd.getFileName() + ".  Bypassing download.");
                                continue;
                            }
                        }
                        InputStream in = archiveHelper.getInputStreamToArchive(fd.getFileName(), fd.getZipEntryName());
                        if (archiveHelper.isZipMode()) {
                            zipOut.putNextEntry(new ZipEntry(archiveHelper.getArchiveEntryName()));
                        } else {
                            TarArchiveEntry entry = new TarArchiveEntry(archiveHelper.getArchiveEntryName());
                            entry.setSize(archiveHelper.getArchiveFileSize());
                            tarOut.putArchiveEntry(entry);
                        }
                        OutputStream out = null;
                        if (archiveHelper.isZipMode()) {
                            out = zipOut;
                        } else {
                            out = tarOut;
                        }
                        int size = archiveHelper.transferBytes(in, out);
                        totalArchiveSize += size;
                        xferLog.setFileSize(new BigDecimal(size));
                        xferLog.setEndDateTime(new java.util.Date(System.currentTimeMillis()));
                        sess.save(xferLog);
                        if (archiveHelper.isZipMode()) {
                            zipOut.closeEntry();
                        } else {
                            tarOut.closeArchiveEntry();
                        }
                        archiveHelper.removeTemporaryFile();
                    }
                }
                if (archiveHelper.isZipMode()) {
                    zipOut.finish();
                    zipOut.flush();
                } else {
                    tarOut.finish();
                }
                sess.flush();
            } else {
                response.setStatus(999);
                System.out.println("DownloadFileServlet: You must have a SecurityAdvisor in order to run this command.");
            }
        } catch (Exception e) {
            response.setStatus(999);
            System.out.println("DownloadFileServlet: An exception occurred " + e.toString());
            e.printStackTrace();
        } finally {
            if (secAdvisor != null) {
                try {
                    secAdvisor.closeHibernateSession();
                } catch (Exception e) {
                }
            }
            req.getSession().setAttribute(CacheFileDownloadList.SESSION_KEY_FILE_DESCRIPTOR_PARSER, null);
            archiveHelper.removeTemporaryFile();
        }
    }

    public static Request findRequest(Session sess, String requestNumber) {
        Request request = null;
        List requests = sess.createQuery("SELECT req from Request req where req.number = '" + requestNumber + "'").list();
        if (requests.size() == 1) {
            request = (Request) requests.get(0);
        }
        return request;
    }
}
