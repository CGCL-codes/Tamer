package com.vlee.servlet.ecommerce;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import com.vlee.ejb.user.*;
import com.vlee.util.*;
import com.vlee.ejb.ecommerce.*;

public class DoEImageManager extends ActionDo implements Action {

    String formName = "";

    String folder_id = "0";

    MultipartRequest mRequest = null;

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String type = null;
        String type1 = req.getHeader("Content-Type");
        String type2 = req.getContentType();
        if (type1 == null && type2 != null) type = type2; else if (type2 == null && type1 != null) type = type1; else if (type1 != null && type2 != null) type = type1.length() <= type2.length() ? type2 : type1;
        if (type != null && type.toLowerCase().startsWith("multipart/form-data")) {
            mRequest = new MultipartRequest(req);
            formName = mRequest.getParameter("formName");
        } else {
            formName = req.getParameter("formName");
        }
        System.out.println("formName>>>>>>>>>>>>>>>>>>>>>" + formName);
        if (formName == null) {
            return new ActionRouter("ecommerce-image-manager-page");
        }
        if (formName.equals("deleteFolder")) {
            try {
                fnDeleteFolder(servlet, req, res);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (formName.equals("editFolder")) {
            try {
                fnEditFolder(servlet, req, res);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (formName.equals("addFolder")) {
            try {
                fnAddFolder(servlet, req, res);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (formName.equals("moveFolder")) {
            try {
                fnMoveFolder(servlet, req, res);
                req.setAttribute("formName", "browseImage");
                fnBrowseImage(servlet, req, res);
                return new ActionRouter("ecommerce-image-browse-page");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (formName.equals("uploadImage")) {
            try {
                fnUploadImage(servlet, req, res);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (formName.equals("browseImage") || formName.equals("browseImagePopUp")) {
            try {
                if (formName.equals("browseImagePopUp")) {
                    String tempStr = "";
                    String formType = "";
                    tempStr = req.getParameter("formType");
                    if (tempStr != null) formType = tempStr;
                    req.setAttribute("formType", formType);
                    String imageParam = "";
                    tempStr = req.getParameter("imageParam");
                    if (tempStr != null) imageParam = tempStr;
                    req.setAttribute("imageParam", imageParam);
                    String typeStr = "";
                    tempStr = req.getParameter("type");
                    if (tempStr != null) typeStr = tempStr;
                    req.setAttribute("type", typeStr);
                }
                fnBrowseImage(servlet, req, res);
                return new ActionRouter("ecommerce-image-browse-page");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (formName.equals("deleteImage")) {
            try {
                fnDeleteImage(servlet, req, res);
                req.setAttribute("formName", "browseImage");
                fnBrowseImage(servlet, req, res);
                return new ActionRouter("ecommerce-image-browse-page");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return new ActionRouter("ecommerce-image-manager-page");
    }

    public void fnBrowseImage(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        if (formName.equals("browseImage") || formName.equals("browseImagePopUp")) folder_id = (String) req.getParameter("folder_id");
        String current_dir = "";
        Hashtable htImageFolder = new Hashtable();
        if (folder_id.equals("0")) {
            htImageFolder = EImageNut.getListFolder();
        } else current_dir = EImageNut.getFolderName(new Long(folder_id));
        req.setAttribute("htImageFolder", htImageFolder);
        req.setAttribute("current_dir", current_dir);
        QueryObject query = new QueryObject(new String[] { EImageBean.FOLDER_ID + " = " + folder_id + " " });
        query.setOrder(" ORDER BY " + EImageBean.NAME);
        Vector vecImage = new Vector(EImageNut.getObjects(query));
        req.setAttribute("vecImage", vecImage);
        req.setAttribute("formName", formName);
    }

    public void fnAddFolder(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String folderName = (String) req.getParameter("folderName");
        if (folderName == null) {
            Log.printDebug("Null Folder Name");
            return;
        }
        EImageNut.insertImageFolder(folderName);
    }

    public void fnEditFolder(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        folder_id = (String) req.getParameter("folder_id");
        String folderName = (String) req.getParameter("folderName");
        if (folderName == null) {
            Log.printDebug("Null Folder Name");
            return;
        }
        EImageNut.storeImageFolder(new Long(folder_id), folderName);
    }

    public void fnDeleteFolder(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        folder_id = (String) req.getParameter("folder_id");
        if (folder_id == null) {
            Log.printDebug("Null Folder Name");
            return;
        }
        EImageNut.deleteImageFolder(new Long(folder_id));
    }

    public void fnMoveFolder(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        folder_id = (String) req.getParameter("moveFolder_id");
        if (folder_id == null) {
            Log.printDebug("Null Folder Name");
            return;
        }
        String strPkid = "";
        String[] image_id = (String[]) req.getParameterValues("imagePkid");
        for (int i = 0; i < image_id.length; i++) {
            strPkid += image_id[i];
            if (i != (image_id.length - 1)) strPkid += ",";
        }
        EImageNut.moveImageFolder(new Long(folder_id), strPkid);
    }

    public void fnUploadImage(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        EImageObject imageObj = new EImageObject();
        folder_id = mRequest.getParameter("folder_id");
        imageObj.folderId = new Long(folder_id);
        req.setAttribute("folder_id", folder_id);
        Enumeration uploadedFile = mRequest.getFileNames();
        while (uploadedFile.hasMoreElements()) {
            String name = (String) uploadedFile.nextElement();
            String fileName = mRequest.getFilesystemName(name);
            if (fileName != null && !fileName.equals("")) {
                File imageFile = mRequest.getFile(name);
                InputStream in = new FileInputStream(imageFile);
                ImageInfo ii = new ImageInfo();
                ii.setInput(in);
                if (!ii.check()) {
                    System.err.println("Not a supported image file format.");
                    return;
                }
                String token = new String();
                String fileExt = new String();
                int pos = 0;
                int i = 0;
                int start = 0;
                int end = 0;
                start = fileName.lastIndexOf('.') + 1;
                end = fileName.length();
                fileExt = fileName.substring(start, end);
                fileName = fileName.substring(0, start - 1);
                imageObj.name = fileName;
                imageObj.extension = fileExt;
                imageObj.mime = ii.getMimeType();
                imageObj.width = ii.getWidth();
                imageObj.heigth = ii.getHeight();
                imageObj.size = new Long(imageFile.length());
                EImage imageEJB = EImageNut.fnCreate(imageObj, imageFile);
            }
        }
    }

    public void fnDeleteImage(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String strPkid = req.getParameter("pkid");
        folder_id = (String) req.getParameter("folder_id");
        try {
            Long pkid = new Long(strPkid);
            EImage stEJB = EImageNut.getHandle(pkid);
            stEJB.remove();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
