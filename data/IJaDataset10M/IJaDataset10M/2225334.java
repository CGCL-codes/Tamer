package com.plus.fcentre.jobfilter.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import com.plus.fcentre.jobfilter.domain.CV;
import com.plus.fcentre.jobfilter.service.CVManager;
import com.plus.fcentre.jobfilter.web.modelandview.CVModelAndViewBuilder;

/**
 * Web MVC controller for actions on a CV.
 * 
 * @author Steve Jefferson
 */
public class CVActionController extends MultiActionController {

    private CVModelAndViewBuilder cvModelAndViewBuilder;

    private CVManager cvManager;

    /**
	 * Construct controller.
	 */
    public CVActionController() {
        super();
    }

    /**
	 * Retrieve CV ModelAndView builder.
	 * 
	 * @return CV ModelAndView builder.
	 */
    CVModelAndViewBuilder getCVModelAndViewBuilder() {
        return cvModelAndViewBuilder;
    }

    /**
	 * Update CV ModelAndView builder.
	 * 
	 * @param cvModelAndViewBuilder CV ModelAndView builder.
	 */
    public void setCVModelAndViewBuilder(CVModelAndViewBuilder cvModelAndViewBuilder) {
        this.cvModelAndViewBuilder = cvModelAndViewBuilder;
    }

    /**
	 * Retrieve CV manager service object.
	 * 
	 * @return CV manager.
	 */
    CVManager getCVManager() {
        return cvManager;
    }

    /**
	 * Update CV manager service object.
	 * 
	 * @param cvManager CV manager.
	 */
    public void setCVManager(CVManager cvManager) {
        this.cvManager = cvManager;
    }

    /**
	 * Handle request to display a CV.
	 * 
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @return target ModelAndView.
	 * @throws BadRequestException if HTTP request does not identify a CV to show.
	 */
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        CV cv = getCV(request);
        return cvModelAndViewBuilder.createCVDetailsModelAndView(cv);
    }

    /**
	 * Handle request to remove a CV.
	 * 
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @return target ModelAndView.
	 * @throws BadRequestException if HTTP request does not identify a CV to
	 *           remove.
	 */
    public ModelAndView remove(HttpServletRequest request, HttpServletResponse response) {
        CV cv = getCV(request);
        cvManager.removeCV(cv);
        return cvModelAndViewBuilder.createCVListModelAndView();
    }

    /**
	 * Retrieve CV which is the focus of this controller.
	 * 
	 * @param request HTTP request.
	 * @return agent object.
	 * @throws MissingParameterException if id parameter cannot be found in HTTP
	 *           request.
	 * @throws NoSuchEntityException if id within HTTP request does not reference
	 *           a valid CV.
	 */
    private CV getCV(HttpServletRequest request) {
        String cvIdStr = request.getParameter("id");
        if (cvIdStr == null) throw new MissingParameterException("id");
        long cvId = Long.parseLong(cvIdStr);
        CV cv = cvManager.findCVById(cvId);
        if (cv == null) throw new NoSuchEntityException(cvId);
        return cv;
    }
}
