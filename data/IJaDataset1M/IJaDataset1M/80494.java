package com.liferay.portlet;

import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.util.Validator;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Locale;
import javax.portlet.CacheControl;
import javax.portlet.MimeResponse;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <a href="MimeResponseImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public abstract class MimeResponseImpl extends PortletResponseImpl implements MimeResponse {

    public boolean isCalledFlushBuffer() {
        return _calledFlushBuffer;
    }

    public void flushBuffer() throws IOException {
        _response.flushBuffer();
        _calledFlushBuffer = true;
    }

    public int getBufferSize() {
        return _response.getBufferSize();
    }

    public CacheControl getCacheControl() {
        return new CacheControlImpl(null, 0, false, false);
    }

    public String getCharacterEncoding() {
        return _response.getCharacterEncoding();
    }

    public String getContentType() {
        return _contentType;
    }

    public Locale getLocale() {
        return _portletRequestImpl.getLocale();
    }

    public OutputStream getPortletOutputStream() throws IOException {
        if (_calledGetWriter) {
            throw new IllegalStateException();
        }
        if (_contentType == null) {
            throw new IllegalStateException();
        }
        _calledGetPortletOutputStream = true;
        return _response.getOutputStream();
    }

    public PrintWriter getWriter() throws IOException {
        if (_calledGetPortletOutputStream) {
            throw new IllegalStateException();
        }
        if (_contentType == null) {
            throw new IllegalStateException();
        }
        _calledGetWriter = true;
        return _response.getWriter();
    }

    public boolean isCalledGetPortletOutputStream() {
        return _calledGetPortletOutputStream;
    }

    public boolean isCalledGetWriter() {
        return _calledGetWriter;
    }

    public boolean isCommitted() {
        return false;
    }

    public void reset() {
        if (_calledFlushBuffer) {
            throw new IllegalStateException();
        }
    }

    public void resetBuffer() {
        _response.resetBuffer();
    }

    public void setBufferSize(int size) {
        _response.setBufferSize(size);
    }

    public void setContentType(String contentType) {
        if (Validator.isNull(contentType)) {
            throw new IllegalArgumentException();
        }
        Enumeration<String> enu = _portletRequestImpl.getResponseContentTypes();
        boolean valid = false;
        if (getLifecycle().equals(PortletRequest.RESOURCE_PHASE) || _portletRequestImpl.getWindowState().equals(LiferayWindowState.EXCLUSIVE)) {
            valid = true;
        } else {
            while (enu.hasMoreElements()) {
                String resContentType = enu.nextElement();
                if (contentType.startsWith(resContentType)) {
                    valid = true;
                    break;
                }
            }
        }
        if (!valid) {
            throw new IllegalArgumentException();
        }
        _contentType = contentType;
        _response.setContentType(contentType);
    }

    protected void init(PortletRequestImpl portletRequestImpl, HttpServletResponse response, String portletName, long companyId, long plid) {
        super.init(portletRequestImpl, response, portletName, companyId, plid);
        _portletRequestImpl = portletRequestImpl;
        _response = response;
    }

    protected void recycle() {
        super.recycle();
        _portletRequestImpl = null;
        _response = null;
        _contentType = null;
        _calledGetPortletOutputStream = false;
        _calledGetWriter = false;
        _calledFlushBuffer = true;
    }

    private PortletRequestImpl _portletRequestImpl;

    private HttpServletResponse _response;

    private String _contentType;

    private boolean _calledGetPortletOutputStream;

    private boolean _calledGetWriter;

    private boolean _calledFlushBuffer;
}
