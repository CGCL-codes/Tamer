package com.mobfee.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import com.mobfee.business.dao.LogonFailedException;
import com.mobfee.business.facade.IMobfeeFacade;
import com.mobfee.domain.GameInfo;
import com.mobfee.domain.UserProfile;
import com.mobfee.web.model.GameInfoForm;

public class AddPhoneModelController extends SimpleFormController {

    private IMobfeeFacade facade;

    public void setFacade(IMobfeeFacade facade) {
        this.facade = facade;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String brandName = request.getParameter("brandName");
            String seriesName = request.getParameter("seriesName");
            String model = request.getParameter("model");
            facade.addPlatform(brandName, seriesName, model);
        } catch (Exception e) {
            return new ModelAndView("Error", "message", e.getLocalizedMessage());
        }
        response.sendRedirect("home.do");
        return null;
    }
}
