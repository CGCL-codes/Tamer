package net.sourceforge.gedapi.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sourceforge.gedapi.fileupload.GEDCOMFileItem;
import net.sourceforge.gedapi.fileupload.GEDCOMFileItemFactory;
import net.sourceforge.gedapi.util.GLinkURL;
import net.sourceforge.gedapi.view.GCSContent;
import net.sourceforge.gedapi.view.GCSContentView;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.fileupload.FileItemHeadersSupport;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.fileupload.util.Streams;
import com.liferay.portal.servlet.filters.context.sso.SSOSubject;

public class GEDCOMCentralSiteServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(GEDCOMCentralSiteServlet.class.getName());

    private static final long serialVersionUID = 324124129123213L;

    private static final GEDCOMFileItemFactory fileFactory = new GEDCOMFileItemFactory(new File(GLinkURL.centralSiteRoot));

    private static final ServletFileUpload fileUpload = new ServletFileUpload(fileFactory);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        handleRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        handleRequest(request, response);
    }

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String gedcomFilePath = request.getRequestURI();
        String servletPath = request.getServletPath();
        gedcomFilePath = gedcomFilePath.substring(gedcomFilePath.indexOf(servletPath) + servletPath.length());
        LOG.finest("The GEDCCOM file path is: " + gedcomFilePath);
        if (gedcomFilePath.indexOf("/../") != -1 || gedcomFilePath.indexOf("\\..\\") != -1 || gedcomFilePath.endsWith("/..") || gedcomFilePath.endsWith("\\..")) {
            throw new ServletException("The parent directory path notation '/../' is not allowed in the path :-).");
        }
        if (ServletFileUpload.isMultipartContent(request)) {
            if ("/upload".equals(gedcomFilePath)) {
                SSOSubject authenticated = (SSOSubject) request.getAttribute(SSOSubject.SSOSUBJECT_KEY);
                if (authenticated == null) {
                    throw new ServletException("The request does not have an " + SSOSubject.SSOSUBJECT_KEY + " attribute, this is not allowed!!!");
                }
                LOG.finest("Uploading GEDCOM file...");
                try {
                    String filenames = parseRequest(request, authenticated);
                    String tabIndex = request.getParameter("tabIndex");
                    LOG.finest("The tabIndex from the request is: " + tabIndex);
                    if (tabIndex == null) {
                        tabIndex = "5";
                    }
                    response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/successfulUpload.html?tabIndex=" + tabIndex + "&gcsPath=uploads" + URLEncoder.encode(File.separator + authenticated.getScreenName(), "UTF-8") + "&successfulUpload=" + URLEncoder.encode(filenames, "UTF-8")));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                LOG.finest("The upload action path is not /upload -> '" + gedcomFilePath + "'!!!");
                throw new ServletException("An attempt to upload a GEDCOM file to a path other than '/upload' is being made, security does not allow this!!!");
            }
        } else {
            File gedcomFile = new File(GLinkURL.centralSiteRoot + gedcomFilePath);
            if (gedcomFile.isDirectory() && gedcomFile.exists()) {
                LOG.finest("Displaying GEDCOM Central Site directory layout for: " + gedcomFile);
                FilenameFilter filter = new FilenameFilter() {

                    public boolean accept(File dir, String filename) {
                        return (new File(dir, filename).isDirectory() || isValidFilename(filename));
                    }
                };
                List<String> filenames = Arrays.asList(gedcomFile.list(filter));
                GCSContentView content = new GCSContentView();
                String requestURL = request.getRequestURL().toString();
                if (!requestURL.endsWith("/")) {
                    requestURL += "/";
                }
                long lastModified = 0;
                for (String filename : filenames) {
                    File contentFile = new File(gedcomFile, filename);
                    if (filename.length() == 2 && contentFile.isDirectory()) {
                        File[] contentFiles = contentFile.listFiles();
                        for (File subContentFile : contentFiles) {
                            if (subContentFile.getName().length() == 2 && subContentFile.isDirectory()) {
                                File[] subFiles = subContentFile.listFiles();
                                for (File subFile : subFiles) {
                                    if (isValidFilename(subFile.getName())) {
                                        boolean validHash = GLinkURL.justFileHash(subFile.getName()).equals(contentFile.getName() + File.separator + subContentFile.getName());
                                        if (subFile.isFile() && validHash) {
                                            LOG.finest("1) Adding file: " + subFile.getAbsolutePath());
                                            GCSContent gcsContent = new GCSContent();
                                            if (lastModified < subFile.lastModified()) {
                                                lastModified = subFile.lastModified();
                                            }
                                            gcsContent.setIsFile(subFile.isFile());
                                            gcsContent.setJsContent((gedcomFilePath.length() > 0 ? gedcomFilePath.substring(1) + "/" : gedcomFilePath) + subFile.getName());
                                            gcsContent.setLinkURL(requestURL + subFile.getName());
                                            gcsContent.setLinkContent(subFile.getName());
                                            content.addContent(gcsContent);
                                        } else {
                                            if ((subContentFile.isDirectory() || isValidFilename(subContentFile.getName())) && validHash) {
                                                LOG.finest("2) Adding file: " + subContentFile.getAbsolutePath());
                                                GCSContent gcsContent = new GCSContent();
                                                if (lastModified < subContentFile.lastModified()) {
                                                    lastModified = subContentFile.lastModified();
                                                }
                                                gcsContent.setIsFile(subContentFile.isFile());
                                                gcsContent.setJsContent((gedcomFilePath.length() > 0 ? gedcomFilePath.substring(1) + "/" : gedcomFilePath) + subContentFile.getName());
                                                gcsContent.setLinkURL(requestURL + subContentFile.getName());
                                                gcsContent.setLinkContent(subContentFile.getName());
                                                content.addContent(gcsContent);
                                            }
                                        }
                                    }
                                }
                            } else {
                                LOG.finest("The full path to the file: " + subContentFile.getAbsolutePath());
                                LOG.finest("File hash: " + GLinkURL.justFileHash(subContentFile.getName()));
                                LOG.finest("Two parent dirs: " + contentFile.getParentFile().getName() + File.separator + contentFile.getName());
                                if ((contentFile.isDirectory() || isValidFilename(contentFile.getName())) && !GLinkURL.justFileHash(subContentFile.getName()).equals(contentFile.getParentFile().getName() + File.separator + contentFile.getName()) && isValidFilename(subContentFile.getName())) {
                                    LOG.finest("3) Adding file: " + contentFile.getAbsolutePath());
                                    GCSContent gcsContent = new GCSContent();
                                    if (lastModified < contentFile.lastModified()) {
                                        lastModified = contentFile.lastModified();
                                    }
                                    gcsContent.setIsFile(contentFile.isFile());
                                    gcsContent.setJsContent((gedcomFilePath.length() > 0 ? gedcomFilePath.substring(1) + "/" : gedcomFilePath) + contentFile.getName());
                                    gcsContent.setLinkURL(requestURL + contentFile.getName());
                                    gcsContent.setLinkContent(contentFile.getName());
                                    content.addContent(gcsContent);
                                }
                            }
                        }
                    } else if (!contentFile.isFile() || !GLinkURL.justFileHash(contentFile.getName()).equals(contentFile.getParentFile().getParentFile().getName() + File.separator + contentFile.getParentFile().getName())) {
                        LOG.finest("4) Adding file: " + contentFile.getAbsolutePath());
                        GCSContent gcsContent = new GCSContent();
                        if (lastModified < contentFile.lastModified()) {
                            lastModified = contentFile.lastModified();
                        }
                        gcsContent.setIsFile(contentFile.isFile());
                        gcsContent.setJsContent((gedcomFilePath.length() > 0 ? gedcomFilePath.substring(1) + "/" : gedcomFilePath) + filename);
                        gcsContent.setLinkURL(requestURL + filename);
                        gcsContent.setLinkContent(filename);
                        content.addContent(gcsContent);
                    }
                }
                sendJSONResponse(response, content, lastModified);
            } else {
                if (gedcomFile.exists() && gedcomFile.isFile()) {
                    LOG.finest("FOUND file: " + gedcomFile);
                    if (request.getParameter("plaintext") != null) {
                        LOG.finest("Sending GEDCOM file as plain text: " + gedcomFile);
                        response.setContentType("text/plain");
                    } else {
                        LOG.finest("Forcing download of GEDCOM file: " + gedcomFile);
                        response.setContentType("application/force-download");
                    }
                    OutputStream out = response.getOutputStream();
                    InputStream in = new BufferedInputStream(new FileInputStream(gedcomFile));
                    Streams.copy(in, out, false);
                    out.flush();
                    out.close();
                    in.close();
                } else {
                    LOG.finest("Could not find: " + gedcomFile);
                    throw new ServletException("The requested GEDCOM file path '" + gedcomFilePath + "' is not valid because the GEDCOM file '" + gedcomFile + "' cannot be resolved!!!");
                }
            }
        }
    }

    public void listFilenames(File dir, List<String> filenames, int depth) {
        for (String filename : dir.list()) {
            File nextFile = new File(dir, filename);
            if (nextFile.isDirectory()) {
                listFilenames(nextFile, filenames, depth + 1);
            } else if (isValidFilename(filename)) {
                File currentDir = dir;
                for (int i = 0; i < depth; ++i) {
                    filename = currentDir.getName() + File.separator + filename;
                    currentDir = currentDir.getParentFile();
                }
                LOG.finest("Adding file with name: " + filename);
                filenames.add(filename);
            }
        }
    }

    public boolean isValidFilename(String filename) {
        boolean isValid = false;
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex != -1) {
            if (".ged".equalsIgnoreCase(filename.substring(lastDotIndex))) {
                isValid = true;
            } else {
                lastDotIndex = filename.lastIndexOf('.', lastDotIndex - 1);
                if (lastDotIndex != -1 && ".glink.xml".equalsIgnoreCase(filename.substring(lastDotIndex))) {
                    isValid = true;
                }
            }
        }
        return isValid;
    }

    public void sendJSONResponse(HttpServletResponse response, Object toJSON, long lastModified) throws IOException {
        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(toJSON);
        String jsonObjectString = jsonObject.toString();
        response.setHeader("Cache-Control", "max-age=0");
        response.setContentType("application/json");
        response.setHeader("ETag", GLinkURL.JSON_ETAG_VERSION + "/\"" + jsonObjectString.length() + "-" + String.valueOf(lastModified) + "\"");
        response.setDateHeader("Last-Modified", lastModified);
        PrintWriter printWriter = response.getWriter();
        LOG.finest("Sending json: " + jsonObjectString);
        printWriter.print(jsonObjectString);
        printWriter.flush();
        printWriter.close();
    }

    public String parseRequest(HttpServletRequest request, SSOSubject authenticated) throws FileUploadException {
        try {
            RequestContext ctx = new ServletRequestContext(request);
            FileItemIterator iter = fileUpload.getItemIterator(ctx);
            String filenames = "";
            String[] overrideFilenames = request.getParameterValues("overrideFilenames");
            String overrideFilename;
            int i = 0;
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                LOG.finest("The filename from the browser is: " + item.getName());
                if (overrideFilenames == null || overrideFilenames.length >= i || overrideFilenames[i] == null) {
                    overrideFilename = item.getName();
                } else {
                    overrideFilename = overrideFilenames[i];
                }
                ++i;
                LOG.finest("Using filename: " + overrideFilename);
                if (overrideFilename.trim().length() > 0) {
                    FileItem fileItem = fileFactory.createItem(item.getFieldName(), item.getContentType(), item.isFormField(), overrideFilename, authenticated.getScreenName());
                    LOG.finest("Writing to file: " + ((GEDCOMFileItem) fileItem).getFullPath());
                    try {
                        Streams.copy(item.openStream(), fileItem.getOutputStream(), true);
                    } catch (IOException e) {
                        throw new IOException("Processing of " + FileUploadBase.MULTIPART_FORM_DATA + " request failed. " + e.getMessage());
                    }
                    if (fileItem.getSize() <= 0) {
                        fileItem.delete();
                    } else {
                        if (fileItem instanceof FileItemHeadersSupport) {
                            final FileItemHeaders fih = item.getHeaders();
                            ((FileItemHeadersSupport) fileItem).setHeaders(fih);
                        }
                        filenames += fileItem.getName() + ", ";
                    }
                }
            }
            return (filenames.length() > 0 ? filenames.substring(0, filenames.length() - 2) : filenames);
        } catch (IOException e) {
            throw new FileUploadException(e.getMessage(), e);
        }
    }
}
