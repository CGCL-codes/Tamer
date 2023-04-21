package com.khotyn.heresy.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.khotyn.heresy.bean.HeresyErrorMessage;
import com.khotyn.heresy.exception.IllegalUrlParamException;
import com.khotyn.heresy.service.DeletePictureService;
import com.khotyn.heresy.util.KeywordFilter;

/**
 * 删除图片控制类
 * 
 * @author 黄挺
 * 
 */
@Controller
@RequestMapping("/deletePicture.html")
public class DeletePictureController {

    @Autowired
    private DeletePictureService deletePictureService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView onLoad(@RequestParam(value = "pictureID", required = false) String pictureID, @RequestParam(value = "ownerID", required = false) String ownerIDStr, HttpServletRequest request, HttpSession session, ModelMap model) {
        HeresyErrorMessage message = null;
        try {
            validate(pictureID, ownerIDStr, session);
        } catch (IllegalUrlParamException e) {
            return e.getErrorModel();
        }
        try {
            deletePictureService.doService(pictureID);
        } catch (Exception e) {
            message = new HeresyErrorMessage(e.getMessage(), "操作失败", "/Dragonfly");
            return new ModelAndView("message", "message", message);
        }
        model.remove("picture");
        return new ModelAndView("redirect:/album.html?albumID=" + (Integer) session.getAttribute("albumID"));
    }

    public void validate(String pictureID, String ownerIDStr, HttpSession session) throws IllegalUrlParamException {
        HeresyErrorMessage errorMessage = null;
        if (pictureID == null || ownerIDStr == null) {
            errorMessage = new HeresyErrorMessage("空的参数", "操作失败", "/Dragonfly");
        } else if (!KeywordFilter.isNumeric(ownerIDStr) || !KeywordFilter.isNumeric(pictureID)) {
            errorMessage = new HeresyErrorMessage("非法的参数", "操作失败", "/Dragonfly");
        } else if ((Integer) session.getAttribute("userID") != Integer.parseInt(ownerIDStr)) {
            errorMessage = new HeresyErrorMessage("您不是这张图片的所有者，无法删除", "操作失败", "/Dragonfly/picture.html?pictureID=" + pictureID);
        }
        if (errorMessage != null) {
            throw new IllegalUrlParamException(errorMessage);
        }
    }
}
