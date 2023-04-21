package net.naijatek.myalumni.modules.members.presentation.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.naijatek.myalumni.framework.struts.MyAlumniDispatchAction;
import net.naijatek.myalumni.modules.common.domain.ClassNewsVO;
import net.naijatek.myalumni.modules.common.domain.MemberVO;
import net.naijatek.myalumni.modules.common.presentation.form.ClassNewsForm;
import net.naijatek.myalumni.modules.common.service.IClassNewsService;
import net.naijatek.myalumni.util.BaseConstants;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class MaintainClassNewsAction extends MyAlumniDispatchAction {

    private IClassNewsService classNewsService;

    private static Log logger = LogFactory.getLog(MaintainClassNewsAction.class);

    public MaintainClassNewsAction(final IClassNewsService classNewsService) {
        this.classNewsService = classNewsService;
    }

    public ActionForward listClassNews(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("in listClassNews");
        MemberVO token = getCurrentLoggedInUser(request);
        if (!memberSecurityCheck(request, token)) {
            return mapping.findForward(BaseConstants.FWD_LOGIN);
        }
        List<ClassNewsVO> list = classNewsService.findAllByYearOut(token.getYearOut());
        setRequestObject(request, BaseConstants.LIST_OF_CLASSNEWS, list);
        return mapping.findForward(BaseConstants.FWD_SUCCESS);
    }

    public ActionForward prepareAddClassNews(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("in prepareAddClassNews");
        MemberVO token = getCurrentLoggedInUser(request);
        if (!memberSecurityCheck(request, token)) {
            return mapping.findForward(BaseConstants.FWD_LOGIN);
        }
        ClassNewsForm cnForm = (ClassNewsForm) form;
        cnForm.setFromClassYear(String.valueOf(token.getYearIn()));
        cnForm.setToClassYear(String.valueOf(token.getYearOut()));
        return mapping.findForward(BaseConstants.FWD_SUCCESS);
    }

    public ActionForward addClassNews(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("in prepareAddClassNews");
        if (isCancelled(request)) {
            return mapping.findForward(BaseConstants.FWD_CANCEL);
        }
        ClassNewsForm cnForm = (ClassNewsForm) form;
        ClassNewsVO cnVO = new ClassNewsVO();
        BeanUtils.copyProperties(cnVO, cnForm);
        cnVO.setLastModifiedBy(getLastModifiedBy(request));
        cnVO.setAuthorId(getCurrentUserId(request));
        classNewsService.save(cnVO);
        ActionMessages errors = new ActionMessages();
        errors.add(BaseConstants.INFO_KEY, new ActionMessage("message.classnewssubmitted"));
        saveMessages(request, errors);
        return mapping.findForward(BaseConstants.FWD_SUCCESS);
    }

    public ActionForward viewClassNews(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("in viewClassNews");
        ClassNewsForm cnForm = (ClassNewsForm) form;
        ClassNewsVO cnVO = new ClassNewsVO();
        cnVO = classNewsService.findById(cnForm.getClassNewsId());
        BeanUtils.copyProperties(cnForm, cnVO);
        return mapping.findForward(BaseConstants.FWD_SUCCESS);
    }
}
