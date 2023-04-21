package cn.myapps.core.filemanager;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.justobjects.pushlet.util.Log;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import cn.myapps.util.file.FileOperate;

/**
 * Servlet implementation class FileManagerUploadServlet
 */
public class FileManagerUploadServlet extends HttpServlet {

    private static final long serialVersionUID = 760033634814977480L;

    private File saveFile;

    private int mm = 0;

    String tempFileName = "";

    @SuppressWarnings("unused")
    private static FileOperate foperate = new FileOperate();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
	 * @SuppressWarnings parseRequest 方法不支持泛型
	 */
    @SuppressWarnings("unchecked")
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        String realPath = request.getParameter("realPath");
        File f1 = new File(realPath);
        if (!f1.exists()) {
            if (!f1.mkdirs()) {
                Log.warn("Failed to create folder at " + realPath + "");
                throw new IOException("Failed to create folder at " + realPath + "");
            }
        }
        DiskFileItemFactory fac = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(fac);
        upload.setHeaderEncoding("utf-8");
        List<FileItem> fileList = null;
        try {
            fileList = upload.parseRequest(request);
        } catch (FileUploadException ex) {
            ex.printStackTrace();
            return;
        }
        Iterator<FileItem> it = fileList.iterator();
        String name = "";
        String fileName = "";
        String extName = "";
        while (it.hasNext()) {
            FileItem item = it.next();
            if (!item.isFormField()) {
                name = item.getName();
                if (name != null) {
                    fileName = name.substring(0, name.lastIndexOf("."));
                }
                tempFileName = fileName;
                if (name != null && name.lastIndexOf(".") >= 0) {
                    extName = name.substring(name.lastIndexOf("."));
                }
                if (name == null || name.trim().equals("")) {
                    continue;
                }
                fileName += reNameFile(realPath, fileName, extName);
                try {
                    saveFile = new File(realPath + "\\" + fileName + extName);
                    item.write(saveFile);
                    mm = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        response.getWriter().print("aa");
    }

    String str = "";

    public String reNameFile(String realPath, String fileName, String extName) {
        File file = new File(realPath + "\\" + fileName + extName);
        str = "";
        if (file.exists()) {
            mm++;
            str = String.valueOf(mm);
            reNameFile(realPath, tempFileName + str, extName);
        } else {
            if (mm != 0) {
                str = String.valueOf(mm);
            }
        }
        return str;
    }
}
