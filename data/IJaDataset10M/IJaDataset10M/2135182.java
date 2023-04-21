package com.khotyn.heresy.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.khotyn.heresy.bean.HeresyErrorMessage;
import com.khotyn.heresy.bean.PictureDetail;
import com.khotyn.heresy.dao.CollectionDAO;
import com.khotyn.heresy.exception.IllegalUrlParamException;
import com.khotyn.heresy.service.PictureService;

/**
 * 图片显示控制器
 * 
 * @author 黄挺
 * 
 */
@Controller
@RequestMapping("/picture.html")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @Autowired
    private CollectionDAO collectionDAO;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView OnLoad(@RequestParam(value = "pictureID", required = false) String pictureID, HttpSession session, HttpServletRequest request, ModelMap model) {
        HeresyErrorMessage message = null;
        try {
            validate(pictureID);
        } catch (IllegalUrlParamException e) {
            return e.getErrorModel();
        }
        Integer userID = (Integer) session.getAttribute("userID");
        PictureDetail picture = new PictureDetail();
        if (userID != null) picture.getPicOwner().setUserID(userID);
        try {
            picture = pictureService.doService(pictureID, userID, request.getRequestURL().toString());
        } catch (Exception e) {
            message = new HeresyErrorMessage(e.getMessage(), "权限错误", "/Dragonfly");
            return new ModelAndView("message", "message", message);
        }
        Integer collectionID = collectionDAO.selectOnePic(userID, pictureID);
        if (collectionID == null) model.addAttribute("isInCollection", false); else model.addAttribute("isInCollection", true);
        return new ModelAndView("picture", "picture", picture);
    }

    private void validate(String pictureID) throws IllegalUrlParamException {
        HeresyErrorMessage message = null;
        if (NumberUtils.isDigits(pictureID)) {
            message = new HeresyErrorMessage("非法的参数", "操作失败", "/Dragonfly");
        }
        if (message != null) {
            throw new IllegalUrlParamException(message);
        }
    }
}
