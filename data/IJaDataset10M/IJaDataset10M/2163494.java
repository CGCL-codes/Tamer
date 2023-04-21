package com.ecomponentes.util.upload.action;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import com.ecomponentes.util.upload.form.UploadForm;

/**
 * This class takes the UploadForm and retrieves the text value
 * and file attributes and puts them in the request for the display.jsp
 * page to display them
 *
 * @author Mike Schachter
 * @version $Rev: 54929 $ $Date: 2007/07/30 00:32:45 $
 */
public class UploadAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (form instanceof UploadForm) {
            String encoding = request.getCharacterEncoding();
            if ((encoding != null) && (encoding.equalsIgnoreCase("utf-8"))) {
                response.setContentType("text/html; charset=utf-8");
            }
            UploadForm theForm = (UploadForm) form;
            String queryValue = theForm.getQueryParam();
            FormFile[] file = new FormFile[1];
            file[0] = theForm.getTheFile1();
            String data = null;
            ResourceBundle rb = ResourceBundle.getBundle("com.ecomponentes.util.RBFilesPath");
            String mapa = rb.getString(theForm.getFilePath() + ".upload.diretorio");
            theForm.setFilePath(rb.getString("upload.diretorio").concat(rb.getString(theForm.getFilePath() + ".upload.diretorio")).concat("/"));
            try {
                for (int i = 0; i < file.length; i++) {
                    if (!file[i].getFileName().equals("")) {
                        InputStream stream = file[i].getInputStream();
                        OutputStream bos = new FileOutputStream(theForm.getFilePath().concat(file[i].getFileName()));
                        int bytesRead = 0;
                        byte[] buffer = new byte[8192];
                        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }
                        bos.close();
                        data = "The file has been written to \"" + theForm.getFilePath() + "\"";
                        stream.close();
                    }
                }
            } catch (FileNotFoundException fnfe) {
                System.out.println(fnfe);
                return null;
            } catch (IOException ioe) {
                return null;
            }
            request.setAttribute("queryValue", queryValue);
            request.setAttribute("files", file);
            request.setAttribute("data", data);
            request.setAttribute("diretorio", mapa);
            request.getSession().setAttribute("uploadfile", file[0].getFileName());
            for (int i = 0; i < file.length; i++) {
                if (!file[i].getFileName().equals("")) {
                    file[i].destroy();
                }
            }
            return mapping.findForward(mapa);
        }
        return null;
    }
}
