package com.khotyn.heresy.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.khotyn.heresy.bean.Album;
import com.khotyn.heresy.bean.HeresyErrorMessage;
import com.khotyn.heresy.exception.IllegalUrlParamException;
import com.khotyn.heresy.service.CopyPictureService;
import com.khotyn.heresy.util.KeywordFilter;

/**
 * 复制图片控制器
 * 
 * @author khotyn
 * 
 */
@Controller
@RequestMapping("/copyPicture.html")
@SessionAttributes("albumID")
public class CopyPictureController {

    @Autowired
    CopyPictureService copyPictureService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView onLoad(HttpSession session, @RequestParam(value = "pictureID", required = false) String pictureID, @RequestParam(value = "ownerID", required = false) String ownerIDStr) {
        Integer userID = (Integer) session.getAttribute("userID");
        ModelAndView errorModel = validate(pictureID, ownerIDStr, userID);
        if (errorModel != null) {
            return errorModel;
        }
        List<Album> albums = copyPictureService.getAlbumDAO().selectAlbumByUserID(userID);
        return new ModelAndView("copyPicture", "albums", albums);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(HttpSession session, HttpServletRequest request, @RequestParam(value = "albumID", required = false) String albumIDStr, @RequestParam(value = "pictureID", required = false) String pictureID, @RequestParam(value = "ownerID", required = false) String ownerIDStr, @ModelAttribute("albumID") Integer orgAlbumID) {
        Integer userID = (Integer) session.getAttribute("userID");
        try {
            validate(albumIDStr, pictureID, ownerIDStr, orgAlbumID, userID);
        } catch (IllegalUrlParamException e) {
            return e.getErrorModel();
        }
        Integer albumID = Integer.parseInt(albumIDStr);
        copyPictureService.doService(albumID, pictureID, userID, request.getRequestURL().toString());
        return new ModelAndView("redirect:/picture.html?pictureID=" + pictureID);
    }

    private void validate(String albumIDStr, String pictureID, String ownerIDStr, Integer orgAlbumID, Integer userID) throws IllegalUrlParamException {
        HeresyErrorMessage errorMessage = null;
        if (userID == null) {
            errorMessage = new HeresyErrorMessage("您尚未登录，请先登录", "未登录", "login.html");
        } else if (!NumberUtils.isDigits(albumIDStr) || !NumberUtils.isDigits(ownerIDStr) || !NumberUtils.isDigits(pictureID)) {
            errorMessage = new HeresyErrorMessage("非法的参数", "操作失败", "/");
        } else if (userID != Integer.parseInt(ownerIDStr)) {
            errorMessage = new HeresyErrorMessage("您无权操作这张图片", "操作失败", "picture.html?pictureID=" + pictureID);
        } else if (Integer.parseInt(albumIDStr) == orgAlbumID) {
            errorMessage = new HeresyErrorMessage("不能将图片复制到同一个相册", "操作失败", "picture.html?pictureID=" + pictureID);
        }
        if (errorMessage != null) {
            throw new IllegalUrlParamException(errorMessage);
        }
    }

    private ModelAndView validate(String pictureID, String ownerIDStr, Integer userID) {
        HeresyErrorMessage message;
        if (userID == null) {
            message = new HeresyErrorMessage("您尚未登录，请先登录", "未登录", "login.html");
            return new ModelAndView("message", "message", message);
        }
        if (pictureID == null || ownerIDStr == null) {
            message = new HeresyErrorMessage("空的参数", "操作失败", "/");
            return new ModelAndView("message", "message", message);
        }
        if (!KeywordFilter.isNumeric(ownerIDStr) || !KeywordFilter.isNumeric(pictureID)) {
            message = new HeresyErrorMessage("非法的参数", "操作失败", "/");
            return new ModelAndView("message", "message", message);
        }
        return null;
    }
}
