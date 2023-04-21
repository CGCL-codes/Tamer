package bebop.handler.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;
import bebop.engine.Environment;
import bebop.handler.servlet.ServletRequestModelHandler;
import bebop.model.ModelManager;
import bebop.util.io.FileInfo;

/**
 * MultipartModelHandler extends ModelHandler to add support
 * for multipart/form-data encoded HTTP requests.  This is
 * commonly used to support file upload.
 *
 * <p>If a file is uploaded, it may be stored in a FileInfo
 * component.  You can configure important details about how
 * file uploads are handled-- such as maximum size and
 * whether to store the file contents in a temp directory or
 * in memory.
 *
 * <p>Normal form fields are dealt with identically to the
 * superclass ModelHandler.  However, if you are not
 * using multipart/form-data encoding, it will be much
 * more efficient to simply use the servlet ModelHandler.
 *
 * <p>This class requires com.oreilly.servlet (cos) JAR
 * available at Jason Hunter's
 * <a href="http://www.servlets.com">servlets.com</a>
 * website.  The software has some restrictions on
 * commercial usage, please consult the licensing terms
 * for details.
 *
 * @see bebop.handler.servlet.ServletRequestModelHandler
 * @see bebop.util.io.FileInfo
 *
 */
public class MultipartRequestModelHandler extends ServletRequestModelHandler {

    private int maxSize = 1024 * 1024;

    private File tempDir = null;

    private boolean useTemp = true;

    /**
	 * Gets the maximum size in bytes of the HTTP POST.
	 * By default, this is 1 MB.
	 *
	 * 
	 * @returns the maximum szie of the HTTP POST
	 *
	 */
    public int getMaxSize() {
        return maxSize;
    }

    /**
	 * Gets the directory to store temporary files,
	 * if specified.  By default, this is null.
	 *
	 * <p>If this is null and useTempFile is true,
	 * then the default temp directory is used.
	 *
	 *
	 * @returns the directory to store temporary files 
	 *
	 */
    public File getTempDir() {
        return tempDir;
    }

    /**
	 * Flags whether uploaded files should be saved to a
	 * temp file, or maintained in memory.  By default, this
	 * is true (indicating a temporary file will be used
	 * for file uploads).
	 *
	 * @returns true if a temp file will be used, false
	 *          otherwise 
	 *
	 */
    public boolean getUseTempFile() {
        return useTemp;
    }

    /**
	 * Overrides superclass's handleModels() with
	 * functionality to deal with multipart/form-data
	 * encoded HTTP requests.
	 *
	 */
    public void handleModels(String id, Object event, ModelManager modelManager, Environment env) throws Exception {
        if (event == null) {
            return;
        }
        HttpServletRequest req = (HttpServletRequest) event;
        MultipartParser parser = null;
        try {
            parser = new MultipartParser(req, maxSize);
        } catch (Exception ex) {
            super.handleModels(id, event, modelManager, env);
            return;
        }
        Part part = null;
        while ((part = parser.readNextPart()) != null) {
            String key = part.getName();
            if (part instanceof ParamPart) {
                ParamPart paramPart = (ParamPart) part;
                String value = paramPart.getStringValue();
                addKeyValuePair(key, value, modelManager);
                continue;
            } else if (part instanceof FilePart) {
                FilePart filePart = (FilePart) part;
                if (key.indexOf('^') != -1 && key.indexOf('*') != -1) {
                    continue;
                }
                if (key.indexOf('.') == -1 && key.indexOf('[') == -1) {
                    Object obj = modelManager.getComponent(key, ModelManager.REQUEST_SCOPE);
                    if (!(obj instanceof FileInfo)) {
                        continue;
                    }
                }
                try {
                    if (useTemp) {
                        String prefix = filePart.getFileName();
                        String suffix = null;
                        if (prefix != null && prefix.lastIndexOf('.') > -1) {
                            suffix = prefix.substring(prefix.lastIndexOf('.'), prefix.length());
                        }
                        File tempFile = File.createTempFile(prefix, suffix, tempDir);
                        FileOutputStream out = new FileOutputStream(tempFile);
                        filePart.writeTo(out);
                        out.close();
                        FileInfo fileInfo = new FileInfo(filePart.getContentType(), new File(filePart.getFilePath()), tempFile);
                        modelManager.setComponent(key, fileInfo, ModelManager.REQUEST_SCOPE);
                    } else {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        filePart.writeTo(out);
                        out.close();
                        byte[] contents = out.toByteArray();
                        FileInfo fileInfo = new FileInfo(filePart.getContentType(), new File(filePart.getFilePath()), contents);
                        modelManager.setComponent(key, fileInfo, ModelManager.REQUEST_SCOPE);
                    }
                } catch (Exception ex) {
                }
            }
        }
    }

    /**
	 * Sets the maximum size in bytes of the HTTP POST.
	 * By default, this is 1 MB.
	 *
	 * 
	 * @param maxSize the maximum szie of the HTTP POST
	 *
	 */
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
	 * Sets the directory to store temporary files,
	 * if specified.  By default, this is null.
	 *
	 * <p>If this is null and useTempFile is true,
	 * then the default temp directory is used.
	 *
	 *
	 * @param tempDir the directory to store temporary files 
	 *
	 */
    public void setTempDir(File tempDir) {
        this.tempDir = tempDir;
    }

    /**
	 * Sets whether uploaded files should be saved to a
	 * temp file, or maintained in memory.  By default, this
	 * is true (indicating a temporary file will be used
	 * for file uploads).
	 *
	 * @param useTemp   true to set to use temp files,
	 *                  false otherwise
	 *
	 */
    public void setUseTempFile(boolean useTemp) {
        this.useTemp = useTemp;
    }
}
