package nuts.ext.struts2.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import nuts.core.io.IOUtils;
import nuts.core.lang.StringUtils;
import nuts.core.servlet.HttpServletResponseUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * FileDownloadAction
 */
public class FileDownloadAction extends CommonServletAction implements ActionConstants {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = -8130806945179830764L;

    /**
	 * log
	 */
    private static Log log = LogFactory.getLog(FileDownloadAction.class);

    /**
	 * WORKDIR = System.getProperty("java.io.tmpdir");
	 */
    public static String WORKDIR = System.getProperty("java.io.tmpdir");

    private String filename;

    private Boolean cache = true;

    protected String workdir;

    protected Integer bufferSize;

    /**
	 * @return the filename
	 */
    public String getFilename() {
        return filename;
    }

    /**
	 * @param filename the filename to set
	 */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
	 * @return the cache
	 */
    public Boolean getCache() {
        return cache;
    }

    /**
	 * @param cache the cache to set
	 */
    public void setCache(Boolean cache) {
        this.cache = cache;
    }

    /**
	 * @return the filename
	 */
    public String getFn() {
        return filename;
    }

    /**
	 * @param filename the filename to set
	 */
    public void setFn(String filename) {
        this.filename = filename;
    }

    /**
	 * @return the cache
	 */
    public Boolean getC() {
        return cache;
    }

    /**
	 * @param cache the cache to set
	 */
    public void setC(Boolean cache) {
        this.cache = cache;
    }

    /**
	 * @return the workdir
	 */
    protected String getWorkdir() {
        return workdir;
    }

    /**
	 * @param workdir the workdir to set
	 */
    protected void setWorkdir(String workdir) {
        this.workdir = workdir;
    }

    /**
	 * @return the bufferSize
	 */
    protected Integer getBufferSize() {
        return bufferSize;
    }

    /**
	 * @param bufferSize the bufferSize to set
	 */
    protected void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    /**
	 * execute
	 * 
	 * @return result name
	 * @throws Exception if an error occurs
	 */
    public String execute() throws Exception {
        try {
            download();
            return NONE;
        } catch (FileNotFoundException e) {
            log.warn("File not found - " + e.getMessage());
            addActionError(getText(ERROR_FILE_NOT_FOUND, filename));
            return ERROR;
        }
    }

    protected void download() throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            if (StringUtils.isEmpty(filename)) {
                return;
            }
            if (workdir == null) {
                workdir = getText(FILE_STORAGE_DIR, WORKDIR);
            }
            File file = new File(workdir, filename);
            if (!file.exists() || !file.isFile()) {
                throw new FileNotFoundException(file.getPath());
            }
            HttpServletResponse response = getServletResponse();
            Map<String, Object> header = new HashMap<String, Object>();
            header.put("contentType", IOUtils.getContentTypeFor(filename));
            header.put("contentLength", Integer.valueOf((int) file.length()));
            header.put("filename", filename);
            header.put("cache", cache);
            HttpServletResponseUtils.setHeader(response, header);
            os = response.getOutputStream();
            if (bufferSize == null) {
                bufferSize = getTextForInt(FILE_DOWNLOAD_BUFFER_SIZE, 4096);
            }
            is = new FileInputStream(file);
            byte[] buf = new byte[bufferSize];
            int sz;
            while (-1 != (sz = is.read(buf))) {
                os.write(buf, 0, sz);
            }
            os.flush();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }
}
