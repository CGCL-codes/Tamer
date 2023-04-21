package org.allcolor.services.servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

public class FileUploadFilter implements Filter {

    private static final String KEY = FileUploadFilter.class.getName();

    private static final Logger LOG = Logger.getLogger(FileUploadFilter.class);

    public void destroy() {
        FileUploadFilter.LOG.info("Destroy filter.");
    }

    public void doFilter(final ServletRequest req, final ServletResponse resp, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) resp;
        if (request.getAttribute(FileUploadFilter.KEY) != null) {
            chain.doFilter(request, response);
        } else {
            if (ServletFileUpload.isMultipartContent(request)) {
                FileUploadFilter.LOG.info("Received Multipart content for : " + request.getRequestURI());
                try {
                    final FileItemFactory factory = new DiskFileItemFactory();
                    final ServletFileUpload upload = new ServletFileUpload(factory);
                    @SuppressWarnings("unchecked") final List<FileItem> files = upload.parseRequest(request);
                    try {
                        final HttpServletRequest requestWrapper = new RequestWrapper(request, files);
                        request.setAttribute(FileUploadFilter.KEY, new Object());
                        chain.doFilter(requestWrapper, response);
                    } finally {
                        RequestWrapper.resetFiles();
                        for (final FileItem item : files) {
                            if (!item.isFormField()) {
                                try {
                                    item.delete();
                                } catch (final Exception ignore) {
                                }
                            }
                        }
                    }
                } catch (final ServletException e) {
                    if (request.getAttribute(FileUploadFilter.KEY) == null) {
                        request.setAttribute(FileUploadFilter.KEY, new Object());
                        chain.doFilter(request, response);
                    }
                    throw e;
                } catch (final Exception e) {
                    if (request.getAttribute(FileUploadFilter.KEY) == null) {
                        request.setAttribute(FileUploadFilter.KEY, new Object());
                        chain.doFilter(request, response);
                    }
                    throw new ServletException(e);
                }
            } else {
                request.setAttribute(FileUploadFilter.KEY, new Object());
                chain.doFilter(request, response);
            }
        }
    }

    public void init(final FilterConfig arg0) throws ServletException {
        FileUploadFilter.LOG.info("Init filter.");
    }
}
