package com.kwoksys.action.hardware;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.framework.exception.ObjectNotFoundException;
import com.kwoksys.framework.util.HttpUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action class for downloading hardware file.
 */
public class HardwareFileDownloadAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Integer hardwareId = HttpUtils.getParameter(request, "hardwareId");
            HardwareService hardwareService = ServiceProvider.getHardwareService();
            hardwareService.getHardware(hardwareId);
            Integer fileId = HttpUtils.getParameter(request, "fileId");
            File file = hardwareService.getHardwareFile(hardwareId, fileId);
            FileService fileService = ServiceProvider.getFileService();
            fileService.download(response, file);
        } catch (ObjectNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        return null;
    }
}
