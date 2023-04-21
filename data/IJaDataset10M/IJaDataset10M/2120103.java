package com.velocityme.www.interfaces;

import com.velocityme.interfaces.*;
import com.velocityme.valueobjects.FileAttachmentValue;
import com.velocityme.www.actionforms.AttachmentActionForm;
import java.io.BufferedInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import org.apache.struts.action.*;

/**
 *
 * @author  Robert
 */
public class AttachmentAction extends org.apache.struts.action.Action {

    /** Creates a new instance of AttachmentAction */
    public AttachmentAction() {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AttachmentActionForm attachment = (AttachmentActionForm) form;
        HttpSession session = request.getSession();
        KeySession key = (KeySession) session.getAttribute("sessionKey");
        RemoteClientSession cs = RemoteClientSessionUtil.getHome().create();
        FileAttachmentValue fileAttachmentValue = cs.findFileAttachmentValue(key, attachment.getName());
        BufferedInputStream bis = cs.getFileAttachmentInputStream(key, fileAttachmentValue.getPrimaryKey());
        response.reset();
        response.setContentType(fileAttachmentValue.getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileAttachmentValue.getName() + "\";");
        response.setContentLength((int) cs.getFileAttachmentLength(key, fileAttachmentValue.getPrimaryKey()));
        ServletOutputStream bos = response.getOutputStream();
        byte[] buffer = new byte[bis.available()];
        int bytesRead = 0;
        while ((bytesRead = bis.read(buffer, 0, buffer.length)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        bis.close();
        bos.flush();
        bos.close();
        return null;
    }
}
