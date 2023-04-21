package cn.com.pxto.web.action;

import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import cn.com.pxto.commons.httputil.ParamUtil;
import cn.com.pxto.commons.pagesupport.PageInfo;
import cn.com.pxto.commons.pagesupport.PageUtils;
import cn.com.pxto.model.LessonType;
import cn.com.pxto.service.SchoolManager;

public class LessonTypeAction extends DispatchAction {

    private Log log = LogFactory.getLog(LessonTypeAction.class);

    private SchoolManager schoolManager = null;

    public ActionForward preCreateLessonType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("come in preCreateLessonType method");
        return mapping.findForward("preCreateLessonType");
    }

    public ActionForward doCreateLessonType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("come in doCreateLessonType method");
        DynaActionForm lessonTypeForm = (DynaActionForm) form;
        LessonType lt = (LessonType) lessonTypeForm.get("lessonType");
        boolean s = true;
        if (schoolManager.getLessonTypeByName(lt.getName()) == null) {
            schoolManager.createLessonType(lt);
        } else {
            request.setAttribute("message", "课程分类已存在!");
            s = false;
        }
        request.setAttribute("boolean", s);
        request.getSession().getServletContext().setAttribute("lessonTypeHasNew", true);
        return mapping.findForward("success");
    }

    public ActionForward preUpdateLessonType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("come in preUpdateLessonType method");
        int ltid = ParamUtil.getIntParams(request, "id");
        LessonType lt = schoolManager.getLessonType(ltid);
        request.setAttribute("lessonType", lt);
        return mapping.findForward("preUpdateLessonType");
    }

    public ActionForward doUpdateLessonType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("come in doUpdateLessonType method");
        DynaActionForm lessonTypeForm = (DynaActionForm) form;
        LessonType lt = (LessonType) lessonTypeForm.get("lessonType");
        schoolManager.updateLessonType(lt);
        request.getSession().getServletContext().setAttribute("lessonTypeHasNew", true);
        return listLessonType(mapping, form, request, response);
    }

    public ActionForward deleteLessonType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("come in deleteLessonType method");
        int ltid = ParamUtil.getIntParams(request, "id");
        schoolManager.deleteLessonType(ltid);
        request.getSession().getServletContext().setAttribute("lessonTypeHasNew", true);
        return listLessonType(mapping, form, request, response);
    }

    public ActionForward listLessonType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("come in listLessonType method");
        PageInfo pageInfo = PageUtils.getPageInfo(request);
        List<LessonType> lessonTypeList = schoolManager.listLessonType(pageInfo);
        request.setAttribute("pageInfo", pageInfo);
        request.setAttribute("lessonTypeList", lessonTypeList);
        return mapping.findForward("listLessonType");
    }

    public void setSchoolManager(SchoolManager schoolManager) {
        this.schoolManager = schoolManager;
    }
}
