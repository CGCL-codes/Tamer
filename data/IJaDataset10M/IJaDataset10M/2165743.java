package gemini.pollux.ui.server.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import com.google.gwt.user.server.Base64Utils;

public class ReadFileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            ServletFileUpload upload = new ServletFileUpload();
            upload.setSizeMax(1000000);
            res.setContentType("text/plain");
            PrintWriter out = res.getWriter();
            try {
                FileItemIterator iterator = upload.getItemIterator(req);
                while (iterator.hasNext()) {
                    FileItemStream item = iterator.next();
                    InputStream in = item.openStream();
                    if (!item.isFormField()) {
                        String fileContents = null;
                        try {
                            System.out.println(new String(IOUtils.toByteArray(in)));
                            System.out.println(IOUtils.toString(in));
                            System.out.println(Base64Utils.fromBase64(fileContents));
                        } finally {
                            IOUtils.closeQuietly(in);
                        }
                    }
                }
            } catch (SizeLimitExceededException e) {
                out.println("You exceeded the maximum size (" + e.getPermittedSize() + ") of the file (" + e.getActualSize() + ")");
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
}
