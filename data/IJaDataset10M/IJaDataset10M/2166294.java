package org.apache.struts.webapp.upload;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * This class takes the UploadForm and retrieves the text value
 * and file attributes and puts them in the request for the display.jsp
 * page to display them
 *
 * @author Mike Schachter
 * @version $Revision: 1.1.1.1 $ $Date: 2005/12/03 23:51:05 $
 */
public class UploadAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (form instanceof UploadForm) {
            String encoding = request.getCharacterEncoding();
            if ((encoding != null) && (encoding.equalsIgnoreCase("utf-8"))) {
                response.setContentType("text/html; charset=utf-8");
            }
            UploadForm theForm = (UploadForm) form;
            String text = theForm.getTheText();
            String queryValue = theForm.getQueryParam();
            FormFile file = theForm.getTheFile();
            String fileName = file.getFileName();
            String contentType = file.getContentType();
            boolean writeFile = theForm.getWriteFile();
            String size = (file.getFileSize() + " bytes");
            String data = null;
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream stream = file.getInputStream();
                if (!writeFile) {
                    if (file.getFileSize() < (4 * 1024000)) {
                        byte[] buffer = new byte[8192];
                        int bytesRead = 0;
                        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                            baos.write(buffer, 0, bytesRead);
                        }
                        data = new String(baos.toByteArray());
                    } else {
                        data = new String("The file is greater than 4MB, " + " and has not been written to stream." + " File Size: " + file.getFileSize() + " bytes. This is a" + " limitation of this particular web application, hard-coded" + " in org.apache.struts.webapp.upload.UploadAction");
                    }
                } else {
                    OutputStream bos = new FileOutputStream(theForm.getFilePath());
                    int bytesRead = 0;
                    byte[] buffer = new byte[8192];
                    while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                        bos.write(buffer, 0, bytesRead);
                    }
                    bos.close();
                    data = "The file has been written to \"" + theForm.getFilePath() + "\"";
                }
                stream.close();
            } catch (FileNotFoundException fnfe) {
                return null;
            } catch (IOException ioe) {
                return null;
            }
            request.setAttribute("text", text);
            request.setAttribute("queryValue", queryValue);
            request.setAttribute("fileName", fileName);
            request.setAttribute("contentType", contentType);
            request.setAttribute("size", size);
            request.setAttribute("data", data);
            file.destroy();
            return mapping.findForward("display");
        }
        return null;
    }
}
