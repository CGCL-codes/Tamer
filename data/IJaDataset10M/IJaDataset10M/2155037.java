package com.itcast.web.action.uploadfile;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.stereotype.Controller;
import com.itcast.bean.uploadfile.UploadFile;
import com.itcast.service.uploadfile.UploadFileService;
import com.itcast.utils.SiteUrl;
import com.itcast.web.formbean.uploadfile.UploadfileForm;

@Controller("/control/uploadfile/manage")
public class UploadfileManageAction extends DispatchAction {

    @Resource(name = "uploadFileServiceBean")
    private UploadFileService uploadFileService;

    /**
	 * 删除
	 */
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UploadfileForm formbean = (UploadfileForm) form;
        List<String> files = uploadFileService.getFilepath(formbean.getFieldids());
        if (files != null) {
            for (String file : files) {
                String realpath = request.getSession().getServletContext().getRealPath(file);
                File deletefile = new File(realpath);
                if (deletefile.exists()) deletefile.delete();
            }
            uploadFileService.delete(UploadFile.class, formbean.getFieldids());
        }
        request.setAttribute("message", "文件删除成功");
        request.setAttribute("urladdress", SiteUrl.readUrl("control.uploadfile.list"));
        return mapping.findForward("message");
    }

    /**
	 * 上传界面
	 */
    public ActionForward uploadUI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("upload");
    }

    /**
	 * 保存上传文件
	 */
    public ActionForward upload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UploadfileForm formbean = (UploadfileForm) form;
        if (formbean.getUploadfile() != null && formbean.getUploadfile().getFileSize() > 0) {
            if (!UploadfileForm.validateFileType(formbean.getUploadfile())) {
                request.setAttribute("message", "文件格式不正确,只允许上传图片/flash动画/word文件/exe文件/pdf文件/TxT文件/xls文件/ppt文件");
                return mapping.findForward("error");
            }
            String ext = UploadfileForm.getExt(formbean.getUploadfile());
            if (("gif".equals(ext) || "jpg".equals(ext) || "bmp".equals(ext) || "png".equals(ext)) && formbean.getUploadfile().getFileSize() > 204800) {
                request.setAttribute("message", "图片不能大于200K");
                return mapping.findForward("error");
            }
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd/HH");
            String pathdir = "images/uploadfile/" + dateformat.format(new Date());
            String realpathdir = request.getSession().getServletContext().getRealPath(pathdir);
            File savedir = new File(realpathdir);
            if (!savedir.exists()) savedir.mkdirs();
            String filename = UUID.randomUUID().toString() + "." + ext;
            FileOutputStream fileoutstream = new FileOutputStream(new File(realpathdir, filename));
            fileoutstream.write(formbean.getUploadfile().getFileData());
            fileoutstream.close();
            String path = pathdir + "/" + filename;
            UploadFile uploadfile = new UploadFile(path);
            uploadFileService.save(uploadfile);
            request.setAttribute("imagepath", uploadfile.getFilepath());
            return mapping.findForward("uploadfinish");
        } else {
            request.setAttribute("error", "请上传文件");
        }
        request.setAttribute("message", "文件上传成功");
        return mapping.findForward("message");
    }
}
