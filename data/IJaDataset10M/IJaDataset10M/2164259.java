package com.guanghua.brick.html.fileupload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.guanghua.brick.biz.BizUtil;
import com.guanghua.brick.html.Constant;
import com.guanghua.brick.util.XmlUtil;

public class FileUploadServlet extends HttpServlet {

    private static Log logger = LogFactory.getLog(FileUploadServlet.class);

    private String fileBizBeanId = "brick.biz.fileupload";

    private String tempDir = "/WEB-INF/filetemp";

    private int maxMemory = 1024 * 1024;

    /**
	 * Constructor of the object.
	 */
    public FileUploadServlet() {
        super();
    }

    /**
	 * Destruction of the servlet. <br>
	 */
    public void destroy() {
        super.destroy();
    }

    /**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo.equals("/list")) {
            listAction(request, response);
        } else if (pathInfo.equals("/remove")) {
            removeAction(request, response);
        } else if (pathInfo.equals("/progress")) {
            progressAction(request, response);
        } else if (pathInfo.equals("/download")) {
            downloadAction(request, response);
        }
    }

    /**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo.equals("/upload")) {
            uploadAction(request, response);
        } else doGet(request, response);
    }

    private void progressAction(HttpServletRequest request, HttpServletResponse response) {
    }

    /**
	 * 下载文件的action
	 */
    private void downloadAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileId = request.getParameter(Constant.GLOBE_FILEUPLOAD_FILEID);
        response.setHeader("content-type", "application");
        response.setBufferSize(1024 * 1024);
        OutputStream out = response.getOutputStream();
        IFileUpload fu = (IFileUpload) BizUtil.getBizBean(dealFileBeanId(request));
        String fileName = null;
        try {
            fileName = fu.downloadFile(new Long(fileId), response.getOutputStream());
        } catch (Exception e) {
            logger.error("download file error:" + fileId, e);
            fileName = "";
        }
        response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes("GBK"), "iso-8859-1"));
        out.flush();
        out.close();
    }

    /**
	 * 上传文件的action
	 * @throws IOException 
	 * @throws ServletException 
	 */
    private void uploadAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String objectId = request.getParameter(Constant.GLOBE_FILEUPLOAD_OBJECTID);
        String objectType = request.getParameter(Constant.GLOBE_FILEUPLOAD_OBJECTTYPE);
        IFileUpload fu = (IFileUpload) BizUtil.getBizBean(dealFileBeanId(request));
        FileItemFactory factory = new DiskFileItemFactory(this.maxMemory, new File(request.getSession().getServletContext().getRealPath(this.tempDir)));
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setProgressListener(new ProgressListener() {

            public void update(long byteReaded, long contentLength, int items) {
                logger.debug("have readed file:[" + byteReaded / 1024 + "KB]");
                logger.debug("total files size:[" + contentLength / 1024 + "KB]");
                logger.debug("now reading file:[" + items + "]");
            }
        });
        Map<String, InputStream> map = new HashMap<String, InputStream>();
        try {
            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (!item.isFormField()) {
                    if (item.getName() != null && item.getName().length() != 0) map.put(item.getName(), item.getInputStream());
                } else {
                    if (Constant.GLOBE_FILEUPLOAD_OBJECTID.equals(item.getFieldName())) {
                        objectId = item.getString();
                        objectId = (objectId != null && objectId.length() != 0) ? objectId : null;
                    } else if (Constant.GLOBE_FILEUPLOAD_OBJECTTYPE.equals(item.getFieldName())) {
                        objectType = item.getString();
                        objectType = (objectType != null && objectType.length() != 0) ? objectType : null;
                    } else {
                        request.setAttribute(item.getFieldName(), item.getString());
                    }
                }
            }
            Iterator<String> ite = map.keySet().iterator();
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            while (ite.hasNext()) {
                String fileName = ite.next();
                Long fileId = null;
                InputStream is = map.get(fileName);
                fileName = dealFileName(fileName);
                if (objectType == null && objectId == null) fileId = fu.uploadFile(is, fileName); else if (objectId != null && objectType == null) fileId = fu.uploadFile(new Long(objectId), is, fileName); else if (objectId != null && objectType != null) fileId = fu.uploadFile(new Long(objectId), new Long(objectType), is, fileName);
                Map<String, String> row = new HashMap<String, String>();
                row.put("fileId", String.valueOf(fileId));
                row.put("fileName", fileName);
                list.add(row);
            }
            request.setAttribute("fileList", list);
            request.setAttribute("fileMsg", "上传成功");
        } catch (Exception e) {
            logger.error("upload file error", e);
            request.setAttribute("fileMsg", "上传失败");
        }
        request.getRequestDispatcher("/brick/uploadsuccess.jsp").forward(request, response);
    }

    /**
	 * 移除文件的action
	 */
    private void removeAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileId = request.getParameter(Constant.GLOBE_FILEUPLOAD_FILEID);
        IFileUpload fu = (IFileUpload) BizUtil.getBizBean(dealFileBeanId(request));
        String ret = "OK";
        try {
            fu.removeUploadFile(new Long(fileId));
        } catch (Exception e) {
            logger.error("remove file error:" + fileId, e);
            ret = "ERROR";
        }
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(XmlUtil.stringToXml(ret));
        out.flush();
        out.close();
    }

    /**
	 * 获取已经存在的文件列表
	 */
    private void listAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String objectId = request.getParameter(Constant.GLOBE_FILEUPLOAD_OBJECTID);
        String objectType = request.getParameter(Constant.GLOBE_FILEUPLOAD_OBJECTTYPE);
        objectId = (objectId != null && objectId.length() != 0) ? objectId : null;
        objectType = (objectType != null && objectType.length() != 0) ? objectType : null;
        IFileUpload fu = (IFileUpload) BizUtil.getBizBean(dealFileBeanId(request));
        List<Map<String, String>> list = null;
        try {
            if (objectType == null) list = fu.listUploadFile(new Long(objectId)); else list = fu.listUploadFile(new Long(objectId), new Long(objectType));
        } catch (Exception e) {
            logger.error("list file error, object id:" + objectId, e);
            list = new ArrayList<Map<String, String>>();
        }
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(XmlUtil.mapArrayToXml((Map[]) list.toArray(new Map[0])));
        out.flush();
        out.close();
    }

    /**
	 * 处理文件名
	 */
    private String dealFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf('\\') + 1);
    }

    /**
	 * 处理biz bean id
	 */
    private String dealFileBeanId(HttpServletRequest request) {
        String r = request.getParameter(Constant.GLOBE_FILEUPLOAD_BEANID);
        if (r != null && r.trim().length() != 0) return r; else return fileBizBeanId;
    }

    /**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
    public void init() throws ServletException {
        if (this.getInitParameter("brick.fileupload.bean") != null) fileBizBeanId = this.getInitParameter("brick.fileupload.bean");
        if (this.getInitParameter("brick.fileupload.tempDir") != null) tempDir = this.getInitParameter("brick.fileupload.tempDir");
        if (this.getInitParameter("brick.fileupload.maxMemory") != null) maxMemory = Integer.parseInt(this.getInitParameter("brick.fileupload.maxMemory"));
    }
}
