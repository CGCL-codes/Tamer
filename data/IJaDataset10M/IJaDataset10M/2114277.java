package org.telscenter.sail.webapp.presentation.web.controllers.student.brainstorm;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBElement;
import org.imsglobal.xsd.imsqti_v2p0.ImgType;
import org.imsglobal.xsd.imsqti_v2p0.SimpleChoiceType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.telscenter.sail.webapp.domain.brainstorm.Brainstorm;
import org.telscenter.sail.webapp.domain.brainstorm.Questiontype;
import org.telscenter.sail.webapp.domain.workgroup.WISEWorkgroup;
import org.telscenter.sail.webapp.service.brainstorm.BrainstormService;
import org.telscenter.sail.webapp.service.workgroup.WISEWorkgroupService;

/**
 * @author patrick lawler
 *
 */
public class BrainstormResponseController extends AbstractController {

    private static final String BRAINSTORMID = "brainstormId";

    private static final String WORKGROUPID = "workgroupId";

    private static final String BRAINSTORM = "brainstorm";

    private static final String WORKGROUP = "workgroup";

    private static final String CHOICES = "choices";

    private static final String KEYS = "keys";

    private BrainstormService brainstormService;

    private WISEWorkgroupService workgroupService;

    /**
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Brainstorm brainstorm = brainstormService.getBrainstormById(Long.parseLong(request.getParameter(BRAINSTORMID)));
        WISEWorkgroup workgroup = (WISEWorkgroup) workgroupService.retrieveById(Long.parseLong(request.getParameter(WORKGROUPID)));
        Map<String, Serializable> choiceMap = new LinkedHashMap<String, Serializable>();
        if (brainstorm.getQuestiontype() == Questiontype.SINGLE_CHOICE) {
            choiceMap = BrainstormUtils.getChoiceMap(brainstorm.getQuestion().getChoices());
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(BRAINSTORM, brainstorm);
        modelAndView.addObject(WORKGROUP, workgroup);
        modelAndView.addObject(CHOICES, choiceMap);
        modelAndView.addObject(KEYS, choiceMap.keySet());
        return modelAndView;
    }

    /**
	 * @param brainstormService the brainstormService to set
	 */
    public void setBrainstormService(BrainstormService brainstormService) {
        this.brainstormService = brainstormService;
    }

    /**
	 * @param workgroupService the workgroupService to set
	 */
    public void setWorkgroupService(WISEWorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
    }
}
