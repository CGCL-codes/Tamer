package net.videgro.oma.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.videgro.oma.managers.MemberManager;
import net.videgro.oma.managers.SettingManager;
import net.videgro.oma.managers.StudyManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class AddMemberController extends SimpleFormController {

    protected final Log logger = LogFactory.getLog(getClass());

    private MemberManager memberManager;

    private StudyManager studyManager;

    public void setMemberManager(MemberManager memberManager) {
        this.memberManager = memberManager;
    }

    public void setStudyManager(StudyManager studyManager) {
        this.studyManager = studyManager;
    }

    @SuppressWarnings("unchecked")
    protected Map referenceData(HttpServletRequest request) throws Exception {
        RequestParameters rp = new RequestParameters();
        rp.parse(request);
        Map model = new HashMap();
        model.put("now", (new java.util.Date()).toString());
        model.put("htmlheader", rp.getHtmlheader());
        model.put("publicUrl", SettingManager.getSettings().getAppPublicUrl());
        model.put("studyInstitutions", studyManager.getStudyInstitutionNames());
        model.put("studyNames", studyManager.getStudyNamesList());
        return model;
    }

    public Object formBackingObject(HttpServletRequest request) throws ServletException {
        RequestParameters rp = new RequestParameters();
        rp.parse(request);
        AddMemberBean addMemberBean = new AddMemberBean();
        addMemberBean.setDepartment(rp.getDepartment());
        addMemberBean.setPassword(net.videgro.oma.utils.Password.generatePassword(8));
        addMemberBean.setRemoteAddr(request.getRemoteAddr());
        return addMemberBean;
    }

    @SuppressWarnings("unchecked")
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws ServletException, IOException {
        ModelAndView mav = null;
        AddMemberBean addMemberBean = (AddMemberBean) command;
        logger.info("Process membership form");
        String message = null;
        int result = memberManager.addMember(addMemberBean);
        switch(result) {
            case MemberManager.ERROR_DUPLICATE_MEMBER:
                message = "Duplicate member";
                break;
            case MemberManager.ERROR_NO_STUDY:
                message = "No study";
                break;
            case MemberManager.ERROR_IN_FIELDS:
                message = "Error in fields";
                break;
        }
        if (message != null) {
            errors.reject("Error adding member", message);
            Map myModel = new HashMap();
            myModel.put("error", message);
            mav = new ModelAndView("add-member-error", "model", myModel);
        }
        if (mav == null) {
            try {
                mav = super.onSubmit(request, response, command, errors);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return mav;
    }
}
