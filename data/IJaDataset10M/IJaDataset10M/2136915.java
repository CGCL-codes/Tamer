package net.sourceforge.pharos.security;

import net.sourceforge.pharos.constants.Constants;
import net.sourceforge.pharos.exception.ApplicationException;
import net.sourceforge.pharos.utils.HttpUtils;
import net.sourceforge.pharos.utils.TypeConverter;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controls the payload which is getting uploaded to the server.
 * Beyond the max file size the request is not entertained.
 * 
 * @author kaushikr
 */
public final class SizeSentinalFilter implements Filter {

    private Long maxFileSize;

    /**
	 * This is the method which is supposed to be called by the servlet container
	 * when the context is getting unloaded or undeployed.
	 * <p>
	 * As of now nothing to add to this method.
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
    public void destroy() {
    }

    /**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
    @SuppressWarnings("unchecked")
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        if (HttpUtils.isHttpRequest(request)) {
            final HttpServletRequest servletRequest = (HttpServletRequest) request;
            if (ServletFileUpload.isMultipartContent(servletRequest)) {
                try {
                    final List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(servletRequest);
                    final Map<String, String[]> parameterMap = new HashMap<String, String[]>();
                    for (FileItem fileItem : items) {
                        if (fileItem.isFormField()) {
                            addFormField(parameterMap, fileItem);
                        } else {
                            addUploadedFile(request, fileItem);
                        }
                    }
                    chain.doFilter(new MultiPartRequest(servletRequest, parameterMap), response);
                } catch (FileUploadException e) {
                    throw new ApplicationException(e);
                }
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
	 * @param request
	 * @param fileItem
	 */
    public void addUploadedFile(final ServletRequest request, final FileItem fileItem) {
        if (fileItem.getName().length() <= 0) {
            request.setAttribute(fileItem.getFieldName(), null);
        } else if (0 < maxFileSize && maxFileSize < fileItem.getSize()) {
            request.setAttribute(fileItem.getFieldName(), new ApplicationException("File size exceeds maximum file size allowed of '" + maxFileSize + " bytes'."));
            fileItem.delete();
        } else {
            request.setAttribute(fileItem.getFieldName(), fileItem);
        }
    }

    /**
	 * @param parameterMap
	 * @param fileItem
	 */
    private void addFormField(final Map<String, String[]> parameterMap, final FileItem fileItem) {
        final String name = fileItem.getFieldName();
        final String value = fileItem.getString();
        final String[] values = parameterMap.get(name);
        if (null == values) {
            parameterMap.put(name, new String[] { value });
        } else {
            final int length = values.length;
            String[] newValues = new String[length + 1];
            System.arraycopy(values, 0, newValues, 0, length);
            newValues[length] = value;
            parameterMap.put(name, newValues);
        }
    }

    /**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
    public void init(final FilterConfig config) throws ServletException {
        this.maxFileSize = TypeConverter.toLong(config.getInitParameter(Constants.MAX_FILE_UPLOAD_SIZE));
    }
}
