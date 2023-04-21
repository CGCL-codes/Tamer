package com.techstar.dmis.web.action;

import java.util.List;
import java.util.Map;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.techstar.framework.utils.SequenceCreator;
import org.apache.struts.upload.FormFile;
import com.techstar.framework.web.action.BaseDispatchAction;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.framework.ui.entity.GridInfoObj;
import com.techstar.framework.ui.web.facade.SysUiGridFacade;
import com.techstar.dmis.web.facade.DdDayondutylogFacade;
import com.techstar.dmis.web.form.DdDayondutylogForm;
import com.techstar.dmis.dto.DdDayondutylogDto;

/**
 * @author 
 * @date
 */
public class DdDayondutylogAction extends BaseDispatchAction {

    private DdDayondutylogFacade ddDayondutylogFacade;

    private SysUiGridFacade sysUiGridFacade;

    public DdDayondutylogAction() {
    }

    /**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List list = ddDayondutylogFacade.listDdDayondutylog();
        request.getSession().setAttribute("DdDayondutylogList", list);
        return mapping.findForward("list");
    }

    /**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DdDayondutylogForm ddDayondutylogForm = (DdDayondutylogForm) form;
        DdDayondutylogDto dto = ddDayondutylogForm.getDdDayondutylogDto();
        FormFile content = ddDayondutylogForm.getContent();
        if (content != null) {
            dto.setContent(content.getFileData());
        }
        if (StringUtils.isEmpty(dto.getFondutyid())) {
            dto.setFondutyid(new SequenceCreator().getUID());
        }
        ddDayondutylogFacade.addDdDayondutylog(dto);
        return mapping.findForward("data");
    }

    /**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward initModify(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DdDayondutylogForm ddDayondutylogForm = (DdDayondutylogForm) form;
        String fondutyid = request.getParameter("fondutyid");
        if (StringUtils.isNotEmpty(fondutyid)) {
            DdDayondutylogDto dto = ddDayondutylogFacade.getDdDayondutylogById(fondutyid);
            ddDayondutylogForm.setDdDayondutylogDto(dto);
        }
        return mapping.findForward("data");
    }

    /**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fondutyid = request.getParameter("fondutyid");
        if (StringUtils.isNotEmpty(fondutyid)) {
            List list = ddDayondutylogFacade.deleteDdDayondutylog(fondutyid);
            request.getSession().setAttribute("DdDayondutylogList", list);
        }
        return mapping.findForward("list");
    }

    public void setDdDayondutylogFacade(DdDayondutylogFacade ddDayondutylogFacade) {
        this.ddDayondutylogFacade = ddDayondutylogFacade;
    }

    public ActionForward download_content(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fondutyid = request.getParameter("fondutyid");
        DdDayondutylogDto dto = ddDayondutylogFacade.getDdDayondutylogById(fondutyid);
        response.setContentType("application/x-msdownload");
        OutputStream os = response.getOutputStream();
        os.write(dto.getContent());
        os.flush();
        return null;
    }

    public ActionForward standGridQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String hqlStr = "";
        String userId = "user";
        GridInfoObj gridInfo = sysUiGridFacade.getGridInfo(request, hqlStr, userId);
        String hqlSql = gridInfo.getHqlSql();
        int beginPage = gridInfo.getBeginPage();
        int pageSize = gridInfo.getPageSize();
        String sumSql = gridInfo.getSumSql();
        Map params = gridInfo.getGridPersonalInfo();
        int count = 0;
        List dtolist = new ArrayList();
        List sumInfo = new ArrayList();
        if (!StringUtils.isEmpty(hqlSql)) {
            QueryListObj queryObj = ddDayondutylogFacade.getDdDayondutylogByHql(hqlSql, beginPage, pageSize, sumSql);
            dtolist = queryObj.getElemList();
            count = queryObj.getCount();
            sumInfo = queryObj.getPropertySum();
        }
        String xmlStr = sysUiGridFacade.parseParamToGridStr(request, gridInfo.getGridPersonalInfo(), dtolist, gridInfo.getBeginPage(), gridInfo.getPageSize(), count, sumInfo);
        return this.printResponseMes(request, response, mapping, "ddDayondutyloggrid", xmlStr);
    }

    public ActionForward ddDayondutylogtoolbar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = "<Toolbar>   <Business>      <id>DdDayondutylog</id>      <name>调度日志</name>   </Business>   <ReportUrl>/report.do</ReportUrl></Toolbar>";
        request.setAttribute("xmlData", method);
        return mapping.findForward("ddDayondutylogtoolbar");
    }

    public ActionForward ddDayondutylogtab(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("ddDayondutylogtab");
    }

    public ActionForward ddDayondutylogframe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("ddDayondutylogframe");
    }

    public ActionForward initDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DdDayondutylogForm ddDayondutylogForm = (DdDayondutylogForm) form;
        String mainId = (String) request.getParameter("mainId");
        String isCopy = (String) request.getParameter("isCopy");
        DdDayondutylogDto ddDayondutylogDto = null;
        if (StringUtils.isNotEmpty(isCopy) && isCopy.equalsIgnoreCase("true")) {
            if (StringUtils.isNotEmpty(mainId)) {
                ddDayondutylogDto = ddDayondutylogFacade.getDdDayondutylogById(mainId);
                ddDayondutylogDto.setVersion(0);
            }
        } else {
            if (StringUtils.isNotEmpty(mainId)) {
                ddDayondutylogDto = ddDayondutylogFacade.getDdDayondutylogById(mainId);
            } else {
                ddDayondutylogDto = new DdDayondutylogDto();
            }
        }
        ddDayondutylogForm.setDdDayondutylogDto(ddDayondutylogDto);
        return mapping.findForward("ddDayondutylogdetail");
    }

    public ActionForward saveDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DdDayondutylogForm ddDayondutylogForm = (DdDayondutylogForm) form;
        DdDayondutylogDto ddDayondutylogDto = ddDayondutylogForm.getDdDayondutylogDto();
        ddDayondutylogFacade.addDdDayondutylog(ddDayondutylogDto);
        ddDayondutylogDto = ddDayondutylogFacade.getDdDayondutylogById(ddDayondutylogDto.getFondutyid());
        ddDayondutylogForm.setDdDayondutylogDto(ddDayondutylogDto);
        saveMessages(request, "message.save.success", new String[] { "调度日志Dto" });
        return mapping.findForward("ddDayondutylogdetail");
    }

    public void setSysUiGridFacade(SysUiGridFacade sysUiGridFacade) {
        this.sysUiGridFacade = sysUiGridFacade;
    }
}
