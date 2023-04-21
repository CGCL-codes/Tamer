package com.cms.bi.bistat.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.d3.bi.basecommon.util.Tools;
import com.d3.bi.basecommon.vo.BaseVo;
import com.d3.bi.basecommon.vo.CommonListVo;
import com.d3.bi.basecommon.vo.CommonResultMessageVO;
import com.d3.bi.webcommon.action.BaseAction;
import com.cms.bi.bistat.pojo.User;
import com.cms.bi.bistat.service.UserManager;
import com.cms.bi.bistat.util.Constant;
import com.cms.bi.bistat.web.form.UserForm;

/**
 * 
 * @author dylan
 * 
 */
public class UserAction extends BaseAction {

    /**
	 * DOCUMENT ME!
	 */
    private final Log logger = LogFactory.getLog(UserAction.class);

    private UserManager service;

    /**
	 * DOCUMENT ME!
	 * 
	 * @param mapping
	 *            DOCUMENT ME!
	 * @param form
	 *            DOCUMENT ME!
	 * @param request
	 *            DOCUMENT ME!
	 * @param response
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return listInfo(mapping, form, request, response);
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param mapping
	 *            DOCUMENT ME!
	 * @param actionForm
	 *            DOCUMENT ME!
	 * @param request
	 *            DOCUMENT ME!
	 * @param response
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
    public ActionForward listInfo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BaseVo vo = new BaseVo();
        Map map = (Map) request.getSession().getAttribute(Constant.USERMAP);
        vo.setObj(map);
        setForwardStr("listInfo");
        CommonListVo commonListVo;
        try {
            commonListVo = (CommonListVo) service.listInfo(vo);
            commonListVo.setActionTo(mapping.getPath().toString() + ".do");
            request.setAttribute("commonListVo", commonListVo);
        } catch (RuntimeException e) {
            validateAll(e, request, "failure");
        }
        return mapping.findForward(forwardStr);
    }

    /**
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward viewInfo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserForm UserForm = (UserForm) actionForm;
        Long id = UserForm.getId();
        BaseVo vo = new BaseVo();
        if (id != null) vo.setKey(id);
        setForwardStr("viewInfo");
        try {
            BaseVo resultVo = (BaseVo) service.viewInfo(vo);
            if (resultVo != null) {
                if (id != null) {
                    User tblUser = (User) resultVo.getObj();
                    if (tblUser != null) {
                        Tools.populateForm(tblUser, UserForm);
                        UserForm.setId(tblUser.getId());
                    }
                }
            }
        } catch (RuntimeException e) {
            validateAll(e, request, "failure");
        }
        return mapping.findForward(forwardStr);
    }

    /**
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward saveInfo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserForm userForm = (UserForm) actionForm;
        User tblUser = new User();
        Tools.populateForm(userForm, tblUser);
        Integer addNew = new Integer(0);
        if (userForm.getAddNew() != null) {
            addNew = userForm.getAddNew();
        }
        BaseVo vo = new BaseVo();
        vo.setAddNew(addNew);
        vo.setObj(tblUser);
        setForwardStr("resultPage");
        try {
            service.saveInfo(vo);
        } catch (RuntimeException e) {
            validateAll(e, request, "failure");
        }
        CommonResultMessageVO cvo = new CommonResultMessageVO();
        List<String[]> list = new ArrayList<String[]>();
        String[] temp1 = new String[2];
        temp1[0] = "OK";
        if ("true".equals(userForm.getFromreg())) {
            temp1[1] = "self.location.href='/userlogin.jsp'";
        } else {
            temp1[1] = "self.location.href='/cms_user_action.do'";
        }
        list.add(temp1);
        String message = "success！";
        cvo.setHrefList(list);
        cvo.setMessage(message);
        request.setAttribute("RESULT_MESSAGE_VO", cvo);
        return mapping.findForward(forwardStr);
    }

    /**
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward removeInfo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ids[] = request.getParameterValues("checkBox");
        BaseVo vo = new BaseVo();
        vo.setIds(ids);
        setForwardStr("resultPage");
        BaseVo resultVo = (BaseVo) service.removeInfo(vo);
        String message = "success！";
        CommonResultMessageVO cvo = new CommonResultMessageVO();
        if (resultVo != null && StringUtils.isNotEmpty(resultVo.getErrorInfo())) {
            message = resultVo.getErrorInfo();
        }
        List<String[]> list = new ArrayList<String[]>();
        String[] temp1 = new String[2];
        temp1[0] = "OK";
        temp1[1] = "self.location.href='/cms_user_action.do'";
        list.add(temp1);
        cvo.setHrefList(list);
        cvo.setMessage(message);
        request.setAttribute("RESULT_MESSAGE_VO", cvo);
        return mapping.findForward(forwardStr);
    }

    public void setService(UserManager service) {
        this.service = service;
    }
}
