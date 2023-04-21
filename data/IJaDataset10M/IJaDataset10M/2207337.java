package com.jy.bookshop.web.controller.users;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import com.jy.bookshop.beans.UserInfo;
import com.jy.bookshop.constants.ErrorCode;
import com.jy.bookshop.service.UserInfoService;
import com.jy.common.nfs.NFS;
import com.jy.common.util.WebUtils;

/**
 * @author wangajing
 * @version 1.0,2010-5-10
 * @see
 * @since 1.0
 * 
 */
public class UpdateUserInfoController extends MultiActionController {

    private NFS nfsService;

    private UserInfoService userInfoService;

    private Logger log = Logger.getLogger(this.getClass());

    public UserInfoService getUserInfoService() {
        return userInfoService;
    }

    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    public NFS getNfsService() {
        return nfsService;
    }

    public void setNfsService(NFS nfsService) {
        this.nfsService = nfsService;
    }

    public ModelAndView uploadImage(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = null;
        try {
            multipartRequest = (MultipartHttpServletRequest) request;
        } catch (Exception ex) {
            multipartRequest = null;
        }
        if (multipartRequest != null) {
            MultipartFile multipartFile = multipartRequest.getFile("image");
            String exName = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase();
            if (exName != null && !exName.equals("bmp") && !exName.equals("jpg") && !exName.equals("png")) {
                return new ModelAndView("uploadimage").addObject("errorMsg", "ͼƬ��ʽ����ȷ!");
            }
            if (multipartFile.getSize() > 10000000) {
                return new ModelAndView("uploadimage").addObject("errorMsg", "�ϴ��ļ�̫��");
            }
            try {
                UserInfo userInfo = WebUtils.getSessionObject(request);
                String fileName = nfsService.saveToNFS(multipartFile.getOriginalFilename(), multipartFile.getBytes());
                userInfo.setImageURL(fileName);
                userInfo.setImageFullURL(nfsService.getHttpPathOf(userInfo.getImageURL()));
                if (userInfoService.updateUserInfo(userInfo) == ErrorCode.ERROR_NO_ERROR) {
                    log.debug("���³ɹ���");
                    response.sendRedirect(request.getContextPath() + "/users/myuserinfo.do");
                    return null;
                } else {
                    log.debug("����ʧ�ܣ�");
                    return new ModelAndView("uploadimage").addObject("errorMsg", "����ʧ�ܣ������ԣ�");
                }
            } catch (IOException e) {
                log.error("�����ļ��쳣", e);
                return new ModelAndView("uploadimage").addObject("errorMsg", "�����ļ��쳣��");
            }
        } else {
            return new ModelAndView("uploadimage").addObject("errorMsg", "�����˴���");
        }
    }

    public ModelAndView uploadImagePage(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("uploadimage");
    }
}
