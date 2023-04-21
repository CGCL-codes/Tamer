package jweblite.resource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import jweblite.util.StringUtils;
import jweblite.web.JWebLitePage;
import jweblite.web.JWebLitePageEvent;
import jweblite.web.SkipException;
import jweblite.web.wrapper.JWebLiteRequestWrapper;
import jweblite.web.wrapper.JWebLiteResponseWrapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class StaticWebResource implements JWebLitePage, JWebLitePageEvent, WebResource {

    private static final long serialVersionUID = 1L;

    private static final Log _cat = LogFactory.getLog(StaticWebResource.class);

    /**
	 * Default constructor.
	 */
    public StaticWebResource() {
        super();
    }

    public void doRequest(JWebLiteRequestWrapper req, JWebLiteResponseWrapper resp) throws SkipException {
        try {
            if (this.isIgnoreGzip()) {
                resp.setGZipEnabled(false);
            }
            this.doHeader(req, resp);
            this.doBody(req, resp);
            this.doFinish(req, resp);
        } catch (SkipException se) {
            throw se;
        } catch (Exception e) {
            _cat.warn("Write data failed!", e);
        }
        throw new SkipException();
    }

    public void doHeader(JWebLiteRequestWrapper req, JWebLiteResponseWrapper resp) throws SkipException {
        String contentType = this.getContentType();
        if (contentType != null) {
            resp.setContentType(contentType);
        }
        String encoding = this.getEncoding();
        if (encoding == null) {
            encoding = req.getEncoding();
        }
        String fileName = this.getFileName();
        if (fileName != null && (fileName = StringUtils.encodeDownloadFileName(req, fileName, encoding)) != null) {
            resp.setHeader("Content-Disposition", "filename=".concat(fileName));
        }
        if (this.isCacheable()) {
            resp.setHeader("Pragma", "no-cache");
            resp.setHeader("Cache-Control", "no-cache");
            resp.setDateHeader("Expires", 0);
        }
    }

    public void doBody(JWebLiteRequestWrapper req, JWebLiteResponseWrapper resp) throws SkipException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(this.loadData(req)));
            bos = new BufferedOutputStream(resp.getOutputStream());
            IOUtils.copy(bis, bos);
            bos.flush();
        } catch (Exception e) {
            _cat.warn("Write data failed!", e);
        } finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(bos);
        }
    }

    public void doFinish(JWebLiteRequestWrapper req, JWebLiteResponseWrapper resp) {
    }

    public String getEncoding() {
        return null;
    }

    public boolean isCacheable() {
        return true;
    }

    public boolean isIgnoreGzip() {
        return false;
    }

    /**
	 * load Data
	 * 
	 * @param req
	 *            JWebLiteRequestWrapper
	 * @return File
	 */
    public abstract File loadData(JWebLiteRequestWrapper req);
}
