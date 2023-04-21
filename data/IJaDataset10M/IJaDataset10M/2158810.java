package com.action.admin;

import java.io.File;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.db.admin.AdminDAO;
import com.db.organization.DeptJobDAO;
import com.db.user.*;

public class AdminSawonInsert extends ActionSupport implements ModelDriven<Object>, Preparable, ServletRequestAware {

    private AdminDAO adminDAO;

    public void setAdminDAO(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    private File upload;

    private File saveFile;

    private String uploadFileName;

    private HttpServletRequest servletRequest;

    private UserVO uVO;

    private DeptJobDAO orgDAO;

    public void setOrgDAO(DeptJobDAO orgDAO) {
        this.orgDAO = orgDAO;
    }

    public void setuVO(UserVO uVO) {
        this.uVO = uVO;
    }

    public void setSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }

    public void setUpload(File upload) {
        this.upload = upload;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    @Override
    public void prepare() throws Exception {
        uVO = new UserVO();
    }

    @Override
    public Object getModel() {
        return uVO;
    }

    public void setServletRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public String execute() {
        String result = "";
        try {
            String path = servletRequest.getSession().getServletContext().getRealPath("/ssteam/upload/user/photo");
            path = "d:\\upload";
            if (upload != null && upload.exists()) {
                saveFile = new File(path + "\\" + uploadFileName);
                FileUtils.copyFile(upload, saveFile);
            }
            uVO.setPhotofn(uploadFileName);
            uVO.setSlevel(orgDAO.getSlevel(uVO.getJobno()));
            System.out.println(uVO.toString());
            adminDAO.InsertUser(uVO);
            result = "success";
        } catch (Exception e) {
            result = "error";
        }
        return "success";
    }
}
