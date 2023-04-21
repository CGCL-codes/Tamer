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
import com.techstar.dmis.web.facade.StdManufacturerFacade;
import com.techstar.dmis.web.form.StdManufacturerForm;
import com.techstar.dmis.dto.StdManufacturerDto;

/**
 * @author 
 * @date
 */
public class StdManufacturerAction extends BaseDispatchAction {

    private StdManufacturerFacade stdManufacturerFacade;

    private SysUiGridFacade sysUiGridFacade;

    public StdManufacturerAction() {
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
        List list = stdManufacturerFacade.listStdManufacturer();
        request.getSession().setAttribute("StdManufacturerList", list);
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
        StdManufacturerForm stdManufacturerForm = (StdManufacturerForm) form;
        StdManufacturerDto dto = stdManufacturerForm.getStdManufacturerDto();
        if (StringUtils.isEmpty(dto.getId())) {
            dto.setId(new SequenceCreator().getUID());
        }
        stdManufacturerFacade.addStdManufacturer(dto);
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
        StdManufacturerForm stdManufacturerForm = (StdManufacturerForm) form;
        String id = request.getParameter("id");
        if (StringUtils.isNotEmpty(id)) {
            StdManufacturerDto dto = stdManufacturerFacade.getStdManufacturerById(id);
            stdManufacturerForm.setStdManufacturerDto(dto);
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
        String id = request.getParameter("id");
        if (StringUtils.isNotEmpty(id)) {
            List list = stdManufacturerFacade.deleteStdManufacturer(id);
            request.getSession().setAttribute("StdManufacturerList", list);
        }
        return mapping.findForward("list");
    }

    public void setStdManufacturerFacade(StdManufacturerFacade stdManufacturerFacade) {
        this.stdManufacturerFacade = stdManufacturerFacade;
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
            QueryListObj queryObj = stdManufacturerFacade.getStdManufacturerByHql(hqlSql, beginPage, pageSize, sumSql);
            dtolist = queryObj.getElemList();
            count = queryObj.getCount();
            sumInfo = queryObj.getPropertySum();
        }
        String xmlStr = sysUiGridFacade.parseParamToGridStr(request, gridInfo.getGridPersonalInfo(), dtolist, gridInfo.getBeginPage(), gridInfo.getPageSize(), count, sumInfo);
        return this.printResponseMes(request, response, mapping, "stdManufacturergrid", xmlStr);
    }

    public ActionForward stdManufacturertoolbar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = "<Toolbar>   <Business>      <id>StdManufacturer</id>      <name>厂家</name>   </Business>   <ReportUrl>/report.do</ReportUrl></Toolbar>";
        request.setAttribute("xmlData", method);
        return mapping.findForward("stdManufacturertoolbar");
    }

    public ActionForward stdManufacturertab(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("stdManufacturertab");
    }

    public ActionForward stdManufacturerframe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("stdManufacturerframe");
    }

    public ActionForward initDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        StdManufacturerForm stdManufacturerForm = (StdManufacturerForm) form;
        String mainId = (String) request.getParameter("mainId");
        String isCopy = (String) request.getParameter("isCopy");
        StdManufacturerDto stdManufacturerDto = null;
        if (StringUtils.isNotEmpty(isCopy) && isCopy.equalsIgnoreCase("true")) {
            if (StringUtils.isNotEmpty(mainId)) {
                stdManufacturerDto = stdManufacturerFacade.getStdManufacturerById(mainId);
                stdManufacturerDto.setVersion(0);
            }
        } else {
            if (StringUtils.isNotEmpty(mainId)) {
                stdManufacturerDto = stdManufacturerFacade.getStdManufacturerById(mainId);
            } else {
                stdManufacturerDto = new StdManufacturerDto();
            }
        }
        stdManufacturerForm.setStdManufacturerDto(stdManufacturerDto);
        return mapping.findForward("stdManufacturerdetail");
    }

    public ActionForward saveDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        StdManufacturerForm stdManufacturerForm = (StdManufacturerForm) form;
        StdManufacturerDto stdManufacturerDto = stdManufacturerForm.getStdManufacturerDto();
        stdManufacturerFacade.addStdManufacturer(stdManufacturerDto);
        stdManufacturerDto = stdManufacturerFacade.getStdManufacturerById(stdManufacturerDto.getId());
        stdManufacturerForm.setStdManufacturerDto(stdManufacturerDto);
        saveMessages(request, "message.save.success", new String[] { "厂家Dto" });
        return mapping.findForward("stdManufacturerdetail");
    }

    public void setSysUiGridFacade(SysUiGridFacade sysUiGridFacade) {
        this.sysUiGridFacade = sysUiGridFacade;
    }
}
