package org.actioncenters.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.actioncenters.core.binary.object.svc.IBinaryObjectService;
import org.actioncenters.core.constants.ACConstants;
import org.actioncenters.core.contribution.svc.exception.BinaryObjectServiceException;
import org.actioncenters.core.spring.ApplicationContextHelper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.context.ApplicationContext;
import android.webkit.MimeTypeMap;

public class UploadFileServlet extends HttpServlet {

    private static final long serialVersionUID = 1343668904867455761L;

    /** The system settings service. */
    private static IBinaryObjectService binaryObjectService = null;

    /** The Application Context. */
    private static ApplicationContext ac = null;

    /**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
    @Override
    public void init() throws ServletException {
        ac = ApplicationContextHelper.getApplicationContext("actioncenters.xml");
        binaryObjectService = (IBinaryObjectService) ac.getBean("binaryObjectService");
    }

    /**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    /**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    /**
	 * The handleRequest method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
    @SuppressWarnings("unchecked")
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (ServletFileUpload.isMultipartContent(request)) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload sfu = new ServletFileUpload(factory);
            try {
                List items = sfu.parseRequest(request);
                Iterator iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();
                    if (!item.isFormField()) {
                        String extension = MimeTypeMap.getFileExtensionFromName(item.getName());
                        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                        try {
                            InputStream is = item.getInputStream();
                            String fid = binaryObjectService.addBinaryObject(item.getName(), mimeType, is);
                            if (fid != null) {
                                PrintWriter pout = response.getWriter();
                                pout.print("FileKey: ");
                                pout.print(ACConstants.GLOBAL_DELIMITER);
                                pout.print(fid);
                                pout.print(ACConstants.GLOBAL_DELIMITER);
                                pout.print("Extension: ");
                                pout.print(ACConstants.GLOBAL_DELIMITER);
                                pout.print(extension);
                                pout.print(ACConstants.GLOBAL_DELIMITER);
                                pout.print("MimeType: ");
                                pout.print(ACConstants.GLOBAL_DELIMITER);
                                pout.print(mimeType);
                                pout.print(ACConstants.GLOBAL_DELIMITER);
                                pout.flush();
                                pout.close();
                                return;
                            }
                        } catch (IOException e) {
                            returnError(response, e);
                        } catch (BinaryObjectServiceException e) {
                            returnError(response, e);
                        }
                    }
                }
            } catch (FileUploadException e1) {
                returnError(response, e1);
            }
        }
    }

    private void returnError(HttpServletResponse response, Exception e) throws IOException {
        response.setContentType("text/html");
        PrintWriter pout = response.getWriter();
        pout.println("ERROR: ");
        pout.print(e.getMessage());
        pout.flush();
        pout.close();
        return;
    }
}
