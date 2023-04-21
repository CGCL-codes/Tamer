package com.techstar.dmis.web.action;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.techstar.framework.service.dto.UserProfile;
import com.techstar.framework.service.security.SecurityContext;
import com.techstar.framework.utils.SequenceCreator;
import org.apache.struts.upload.FormFile;
import com.techstar.framework.web.action.BaseDispatchAction;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.framework.ui.entity.GridInfoObj;
import com.techstar.framework.ui.web.facade.SysUiGridFacade;
import com.techstar.dmis.web.facade.ZdhReceptionFacade;
import com.techstar.dmis.web.form.ZdhReceptionForm;
import com.techstar.dmis.web.form.ZdhSrapplicationForm;
import com.techstar.dmis.web.form.ZdhTransrecordForm;
import com.techstar.dmis.common.ZdhConstants;
import com.techstar.dmis.dto.ZdhOpexaminelistDto;
import com.techstar.dmis.dto.ZdhReceptionDto;
import com.techstar.dmis.dto.ZdhSrapplicationDto;
import com.techstar.dmis.dto.ZdhWorkbillDto;
import com.techstar.dmis.dto.ZdhTransrecordDto;
import com.techstar.dmis.helper.dto.WorkflowHandleDto;
import com.techstar.dmis.util.TypeTransUtil;

/**
 * @author 
 * @date
 */
public class ZdhReceptionAction extends BaseDispatchAction {

    private ZdhReceptionFacade zdhReceptionFacade;

    private SysUiGridFacade sysUiGridFacade;

    public ZdhReceptionAction() {
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
        List list = zdhReceptionFacade.listZdhReception();
        request.getSession().setAttribute("ZdhReceptionList", list);
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
        ZdhReceptionForm zdhReceptionForm = (ZdhReceptionForm) form;
        ZdhReceptionDto dto = zdhReceptionForm.getZdhReceptionDto();
        FormFile freceptiongraph = zdhReceptionForm.getFreceptiongraph();
        if (freceptiongraph != null) {
            dto.setFreceptiongraph(freceptiongraph.getFileData());
        }
        FormFile freceptionreport = zdhReceptionForm.getFreceptionreport();
        if (freceptionreport != null) {
            dto.setFreceptionreport(freceptionreport.getFileData());
        }
        FormFile fsequence = zdhReceptionForm.getFsequence();
        if (fsequence != null) {
            dto.setFsequence(fsequence.getFileData());
        }
        FormFile ftranrec = zdhReceptionForm.getFtranrec();
        if (ftranrec != null) {
            dto.setFtranrec(ftranrec.getFileData());
        }
        if (StringUtils.isEmpty(dto.getFapplicationid())) {
            dto.setFapplicationid(new SequenceCreator().getUID());
        }
        zdhReceptionFacade.addZdhReception(dto);
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
        ZdhReceptionForm zdhReceptionForm = (ZdhReceptionForm) form;
        String fapplicationid = request.getParameter("fapplicationid");
        if (StringUtils.isNotEmpty(fapplicationid)) {
            ZdhReceptionDto dto = zdhReceptionFacade.getZdhReceptionById(fapplicationid);
            zdhReceptionForm.setZdhReceptionDto(dto);
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
        String fapplicationid = request.getParameter("fapplicationid");
        if (StringUtils.isNotEmpty(fapplicationid)) {
            List list = zdhReceptionFacade.deleteZdhReception(fapplicationid);
            request.getSession().setAttribute("ZdhReceptionList", list);
        }
        return mapping.findForward("list");
    }

    public void setZdhReceptionFacade(ZdhReceptionFacade zdhReceptionFacade) {
        this.zdhReceptionFacade = zdhReceptionFacade;
    }

    public ActionForward download_freceptiongraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fapplicationid = request.getParameter("fapplicationid");
        ZdhReceptionDto dto = zdhReceptionFacade.getZdhReceptionById(fapplicationid);
        response.setContentType("application/x-msdownload");
        OutputStream os = response.getOutputStream();
        os.write(dto.getFreceptiongraph());
        os.flush();
        return null;
    }

    public ActionForward download_freceptionreport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fapplicationid = request.getParameter("fapplicationid");
        ZdhReceptionDto dto = zdhReceptionFacade.getZdhReceptionById(fapplicationid);
        response.setContentType("application/x-msdownload");
        OutputStream os = response.getOutputStream();
        os.write(dto.getFreceptionreport());
        os.flush();
        return null;
    }

    public ActionForward download_fsequence(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fapplicationid = request.getParameter("fapplicationid");
        ZdhReceptionDto dto = zdhReceptionFacade.getZdhReceptionById(fapplicationid);
        response.setContentType("application/x-msdownload");
        OutputStream os = response.getOutputStream();
        os.write(dto.getFsequence());
        os.flush();
        return null;
    }

    public ActionForward download_ftranrec(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fapplicationid = request.getParameter("fapplicationid");
        ZdhReceptionDto dto = zdhReceptionFacade.getZdhReceptionById(fapplicationid);
        response.setContentType("application/x-msdownload");
        OutputStream os = response.getOutputStream();
        os.write(dto.getFtranrec());
        os.flush();
        return null;
    }

    public ActionForward download_runeqpcontent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fapplicationid = request.getParameter("fapplicationid");
        ZdhReceptionDto dto = zdhReceptionFacade.getZdhReceptionById(fapplicationid);
        response.setContentType("application/x-msdownload");
        OutputStream os = response.getOutputStream();
        os.write(dto.getRuneqpcontent());
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
            QueryListObj queryObj = zdhReceptionFacade.getZdhReceptionByHql(hqlSql, beginPage, pageSize, sumSql);
            dtolist = queryObj.getElemList();
            count = queryObj.getCount();
            sumInfo = queryObj.getPropertySum();
        }
        String xmlStr = sysUiGridFacade.parseParamToGridStr(request, gridInfo.getGridPersonalInfo(), dtolist, gridInfo.getBeginPage(), gridInfo.getPageSize(), count, sumInfo);
        return this.printResponseMes(request, response, mapping, "zdhReceptiongrid", xmlStr);
    }

    public ActionForward standGridQuery1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
            QueryListObj queryObj = zdhReceptionFacade.getZdhReceptionByHql(hqlSql, beginPage, pageSize, sumSql);
            dtolist = queryObj.getElemList();
            count = queryObj.getCount();
            sumInfo = queryObj.getPropertySum();
        }
        String xmlStr = sysUiGridFacade.parseParamToGridStr(request, gridInfo.getGridPersonalInfo(), dtolist, gridInfo.getBeginPage(), gridInfo.getPageSize(), count, sumInfo);
        return this.printResponseMes(request, response, mapping, "zdhReceptiongrid1", xmlStr);
    }

    public ActionForward zdhReceptiontoolbar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = "<Toolbar>   <Business>      <id>ZdhReception</id>      <name>基改建工程自动化设备验收投运</name>   </Business>   <ReportUrl>/report.do</ReportUrl>   ";
        method += "<OperItem Text='工作流-处理历史' id='wf_history' href='wf_history' title=''></OperItem>";
        method += "</Toolbar>";
        request.setAttribute("xmlData", method);
        return mapping.findForward("zdhReceptiontoolbar");
    }

    /**
	 	 * 改变状态(归档、作废)
	 	 */
    public ActionForward changeStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String mainId = request.getParameter("fapplicationid");
        String excutestatus = request.getParameter("updateFlag");
        String message = "改变状态成功";
        try {
            String[] zdReceptionId = mainId.split("\\^");
            for (int i = 0; i < zdReceptionId.length; i++) {
                ZdhReceptionDto dto = zdhReceptionFacade.getZdhReceptionById(zdReceptionId[i]);
                dto.setFexcutestatus(excutestatus);
                zdhReceptionFacade.addZdhReception(dto);
            }
            saveMessages(request, "message.save.success", new String[] { "基改建Dto" });
        } catch (Exception e) {
            message = "改变状态失败";
            e.printStackTrace();
        }
        return this.printResponseMes(request, response, mapping, "null", message);
    }

    public ActionForward zdhReceptiontab(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("zdhReceptiontab");
    }

    public ActionForward zdhReceptionframe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("zdhReceptionframe");
    }

    public ActionForward initDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ZdhReceptionForm zdhReceptionForm = (ZdhReceptionForm) form;
        String mainId = (String) request.getParameter("mainId");
        String isCopy = (String) request.getParameter("isCopy");
        ZdhReceptionDto zdhReceptionDto = null;
        String project_data = "";
        String strFexcutestatus = "";
        if (StringUtils.isNotEmpty(isCopy) && isCopy.equalsIgnoreCase("true")) {
            if (StringUtils.isNotEmpty(mainId)) {
                zdhReceptionDto = zdhReceptionFacade.getZdhReceptionById(mainId);
                zdhReceptionDto.setVersion(0);
            }
            request.setAttribute("project_data", "0");
        } else {
            if (StringUtils.isNotEmpty(mainId)) {
                zdhReceptionDto = zdhReceptionFacade.getZdhReceptionById(mainId);
                strFexcutestatus = zdhReceptionDto.getFexcutestatus();
                if (StringUtils.isEmpty(strFexcutestatus) || strFexcutestatus.equals(ZdhConstants.ZdhReception_WORKFLOW_CITY_NEW) || strFexcutestatus.equals(ZdhConstants.ZdhReception_WORKFLOW_CITY_MODIFY_OK) || strFexcutestatus.equals(ZdhConstants.ZdhReception_WORKFLOW_CITY_AUDITING) || strFexcutestatus.equals(ZdhConstants.ZdhReception_WORKFLOW_CITY_ACCEPT_YES) || strFexcutestatus.equals(ZdhConstants.ZdhReception_WORKFLOW_CITY_ACCEPT_NO)) {
                    request.setAttribute("project_data", "0");
                } else {
                    request.setAttribute("project_data", "1");
                }
            } else {
                zdhReceptionDto = new ZdhReceptionDto();
                request.setAttribute("project_data", "0");
            }
        }
        zdhReceptionForm.setZdhReceptionDto(zdhReceptionDto);
        return mapping.findForward("zdhReceptiondetail");
    }

    public ActionForward saveDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ZdhReceptionForm zdhReceptionForm = (ZdhReceptionForm) form;
        ZdhReceptionDto zdhReceptionDto = zdhReceptionForm.getZdhReceptionDto();
        FormFile freceptiongraph = zdhReceptionForm.getFreceptiongraph();
        if (freceptiongraph != null) {
            zdhReceptionDto.setFreceptiongraph(freceptiongraph.getFileData());
        }
        FormFile freceptionreport = zdhReceptionForm.getFreceptionreport();
        if (freceptionreport != null) {
            zdhReceptionDto.setFreceptionreport(freceptionreport.getFileData());
        }
        FormFile fsequence = zdhReceptionForm.getFsequence();
        if (fsequence != null) {
            zdhReceptionDto.setFsequence(fsequence.getFileData());
        }
        FormFile ftranrec = zdhReceptionForm.getFtranrec();
        if (ftranrec != null) {
            zdhReceptionDto.setFtranrec(ftranrec.getFileData());
        }
        if (StringUtils.isEmpty(zdhReceptionDto.getFapplicationid())) {
            zdhReceptionDto.setFstatus(ZdhConstants.ZdhReception_WORKFLOW_CITY_STATUS_NEW);
            zdhReceptionDto.setFexcutestatus(ZdhConstants.ZdhReception_WORKFLOW_CITY_NEW);
            zdhReceptionDto.setSys_dataowner(ZdhConstants.ZdhSrappl_WORKFLOW_CITY_IsArea_CITY);
        }
        String project_data = request.getParameter("project_data");
        request.setAttribute("project_data", project_data);
        zdhReceptionFacade.addZdhReception(zdhReceptionDto);
        zdhReceptionDto = zdhReceptionFacade.getZdhReceptionById(zdhReceptionDto.getFapplicationid());
        zdhReceptionForm.setZdhReceptionDto(zdhReceptionDto);
        saveMessages(request, "message.save.success", new String[] { "基改建工程自动化设备验收投运Dto" });
        return mapping.findForward("zdhReceptiondetail");
    }

    public ActionForward initDetail1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ZdhReceptionForm zdhReceptionForm = (ZdhReceptionForm) form;
        String mainId = (String) request.getParameter("mainId");
        String isCopy = (String) request.getParameter("isCopy");
        ZdhReceptionDto zdhReceptionDto = null;
        String strFexcutestatus = "";
        String isShow = "";
        if (StringUtils.isNotEmpty(isCopy) && isCopy.equalsIgnoreCase("true")) {
            if (StringUtils.isNotEmpty(mainId)) {
                zdhReceptionDto = zdhReceptionFacade.getZdhReceptionById(mainId);
                zdhReceptionDto.setVersion(0);
                request.setAttribute("isShow", "0");
            }
        } else {
            if (StringUtils.isNotEmpty(mainId)) {
                zdhReceptionDto = zdhReceptionFacade.getZdhReceptionById(mainId);
                zdhReceptionForm.setRuneqpcontent(TypeTransUtil.BlobToString(zdhReceptionDto.getRuneqpcontent()));
                strFexcutestatus = zdhReceptionDto.getFexcutestatus();
                if (StringUtils.isNotEmpty(strFexcutestatus) && strFexcutestatus.equals(ZdhConstants.ZdhReception_WORKFLOW_CITY_APP)) {
                    request.setAttribute("isShow", "1");
                } else if (StringUtils.isNotEmpty(strFexcutestatus) && strFexcutestatus.equals(ZdhConstants.ZdhReception_WORKFLOW_CITY_RECPETION_YES)) {
                    request.setAttribute("isShow", "2");
                } else {
                    request.setAttribute("isShow", "0");
                }
            } else {
                zdhReceptionDto = new ZdhReceptionDto();
                request.setAttribute("isShow", "0");
            }
        }
        zdhReceptionForm.setZdhReceptionDto(zdhReceptionDto);
        return mapping.findForward("zdhReceptiondetail1");
    }

    public ActionForward saveDetail1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ZdhReceptionForm zdhReceptionForm = (ZdhReceptionForm) form;
        ZdhReceptionDto zdhReceptionDto = zdhReceptionForm.getZdhReceptionDto();
        zdhReceptionDto.setRuneqpcontent(TypeTransUtil.stringToBlob(zdhReceptionForm.getRuneqpcontent()));
        String project_data = request.getParameter("project_data");
        request.setAttribute("project_data", project_data);
        zdhReceptionFacade.addZdhReception(zdhReceptionDto);
        zdhReceptionDto = zdhReceptionFacade.getZdhReceptionById(zdhReceptionDto.getFapplicationid());
        zdhReceptionForm.setZdhReceptionDto(zdhReceptionDto);
        saveMessages(request, "message.save.success", new String[] { "基改建工程自动化设备验收投运Dto" });
        return mapping.findForward("zdhReceptiondetail1");
    }

    public void setSysUiGridFacade(SysUiGridFacade sysUiGridFacade) {
        this.sysUiGridFacade = sysUiGridFacade;
    }

    /**
	 * 成批删除
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author sbt 2007-4-18 
	 */
    public ActionForward deleteRows(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String strFapplicationid = request.getParameter("fapplicationid");
        String versionStr = request.getParameter("versions");
        System.out.println("主键参数:" + strFapplicationid);
        System.out.println("版本参数:" + versionStr);
        try {
            List dtos = new ArrayList();
            String[] fapplicationidArr = strFapplicationid.split("\\^");
            String[] versionArr = versionStr.split("\\^");
            for (int i = 0; i < fapplicationidArr.length; i++) {
                ZdhReceptionDto zdhReceptionDto = new ZdhReceptionDto();
                zdhReceptionDto.setFapplicationid(fapplicationidArr[i]);
                zdhReceptionDto.setVersion(Integer.valueOf(versionArr[i]).intValue());
                dtos.add(zdhReceptionDto);
            }
            zdhReceptionFacade.deleteZdhReception(dtos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 工作流处理
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward workflowHandle(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserProfile userP = (UserProfile) (request.getSession().getAttribute(SecurityContext.SECURITY_AUTHENTICATION_KEY));
        String userId = userP.getUserName();
        String roleId = "dd";
        String busId = request.getParameter("wf_busId");
        String taskId = request.getParameter("wf_taskId");
        String taskInstanceId = request.getParameter("wf_taskInstanceId");
        String status = request.getParameter("wf_status");
        String notices = request.getParameter("notice");
        String messages = request.getParameter("message");
        String sentPersons = request.getParameter("sentPersons");
        String isSms = request.getParameter("isSms");
        String[] nextTaskRoles = request.getParameterValues("nextTaskRoles");
        String transitionFlag = request.getParameter("transitionFlag");
        ZdhReceptionDto zdhReceptionDto = zdhReceptionFacade.getZdhReceptionById(busId);
        String strDataowner = zdhReceptionDto.getSys_dataowner();
        WorkflowHandleDto dto = new WorkflowHandleDto();
        dto.setBusId(busId);
        dto.setIsSms(isSms);
        dto.setMessages(messages);
        dto.setNextTaskRoles(nextTaskRoles);
        dto.setNotices(notices);
        dto.setRoleId(roleId);
        dto.setSentPersons(sentPersons);
        dto.setStatus(status);
        dto.setTaskId(taskId);
        dto.setTaskInstanceId(taskInstanceId);
        dto.setTransitionFlag(transitionFlag);
        dto.setUserId(userId);
        if (StringUtils.isNotEmpty(busId)) {
            int statusFlag = Integer.parseInt(status);
            switch(statusFlag) {
                case 1:
                    zdhReceptionFacade.start(dto);
                    break;
                case 2:
                    if (!StringUtils.isEmpty(strDataowner)) {
                        if (strDataowner.equals(ZdhConstants.ZdhReception_CITY)) {
                            if (transitionFlag.equals("1")) {
                                dto.setTransitionFlag(ZdhConstants.ZdhReception_WORKFLOW_STATUS_CITY_YES);
                            } else if (transitionFlag.equals("-1")) {
                                dto.setTransitionFlag(ZdhConstants.ZdhReception_WORKFLOW_STATUS_CITY_NO);
                            }
                        } else if (strDataowner.equals(ZdhConstants.ZdhReception_AREA)) {
                            if (transitionFlag.equals("1")) {
                                dto.setTransitionFlag(ZdhConstants.ZdhReception_WORKFLOW_STATUS_AREA_YES);
                            } else if (transitionFlag.equals("-1")) {
                                dto.setTransitionFlag(ZdhConstants.ZdhReception_WORKFLOW_STATUS__AREA_NO);
                            }
                        }
                    }
                    zdhReceptionFacade.projectrecptionSpecApprove(dto);
                    break;
                case 3:
                    zdhReceptionFacade.projectrecptionModify(dto);
                    break;
                case 4:
                    zdhReceptionFacade.projectrecptionReport(dto);
                    break;
                case 5:
                    if (!StringUtils.isEmpty(strDataowner)) {
                        if (strDataowner.equals(ZdhConstants.ZdhReception_CITY)) {
                            if (transitionFlag.equals("1")) {
                                dto.setTransitionFlag(ZdhConstants.ZdhReception_WORKFLOW_STATUS_CITY_YES);
                            } else if (transitionFlag.equals("-1")) {
                                dto.setTransitionFlag(ZdhConstants.ZdhReception_WORKFLOW_STATUS_CITY_NO);
                            }
                        } else if (strDataowner.equals(ZdhConstants.ZdhReception_AREA)) {
                            if (transitionFlag.equals("1")) {
                                dto.setTransitionFlag(ZdhConstants.ZdhReception_WORKFLOW_STATUS_AREA_YES);
                            } else if (transitionFlag.equals("-1")) {
                                dto.setTransitionFlag(ZdhConstants.ZdhReception_WORKFLOW_STATUS__AREA_NO);
                            }
                        }
                    }
                    zdhReceptionFacade.projectrecptionApproveFill(dto);
                    break;
                case 6:
                    zdhReceptionFacade.projectrecptionRunApply(dto);
                    break;
                case 7:
                    if (!StringUtils.isEmpty(strDataowner)) {
                        if (strDataowner.equals(ZdhConstants.ZdhReception_CITY)) {
                            if (transitionFlag.equals("1")) {
                                dto.setTransitionFlag(ZdhConstants.ZdhReception_WORKFLOW_STATUS_CITY_YES);
                            } else if (transitionFlag.equals("-1")) {
                                dto.setTransitionFlag(ZdhConstants.ZdhReception_WORKFLOW_STATUS_CITY_NO);
                            }
                        } else if (strDataowner.equals(ZdhConstants.ZdhReception_AREA)) {
                            if (transitionFlag.equals("1")) {
                                dto.setTransitionFlag(ZdhConstants.ZdhReception_WORKFLOW_STATUS_AREA_YES);
                            } else if (transitionFlag.equals("-1")) {
                                dto.setTransitionFlag(ZdhConstants.ZdhReception_WORKFLOW_STATUS__AREA_NO);
                            }
                        }
                    }
                    zdhReceptionFacade.projectrecptionRunApprove(dto);
                    break;
                case 8:
                    zdhReceptionFacade.projectrecptionExec(dto);
                    break;
                case 9:
                    zdhReceptionFacade.projectrecptionKeepon(dto);
                    break;
            }
        }
        return mapping.findForward("");
    }

    public ActionForward showReceptiondetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String mainId = "";
        mainId = (String) request.getParameter("mainId");
        ZdhReceptionForm zdhReceptionForm = (ZdhReceptionForm) form;
        ZdhReceptionDto zdhReceptionDto = null;
        zdhReceptionDto = zdhReceptionFacade.getZdhReceptionById(mainId);
        zdhReceptionForm.setZdhReceptionDto(zdhReceptionDto);
        return mapping.findForward("zdhTransrecordshowReceptiondetail");
    }

    /**
	 * 显示图片
	 * 
	 */
    public ActionForward showImg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ZdhReceptionForm zdhReceptionForm = (ZdhReceptionForm) form;
        String mainId = (String) request.getParameter("primaryInfo");
        if (StringUtils.isNotEmpty(mainId)) mainId = mainId.substring(mainId.lastIndexOf("^") + 1);
        String strType = (String) request.getParameter("type");
        ZdhReceptionDto zdhReceptionDto = null;
        zdhReceptionDto = zdhReceptionFacade.getZdhReceptionById(mainId);
        zdhReceptionForm.setZdhReceptionDto(zdhReceptionDto);
        request.setAttribute("zdhReceptionDto", zdhReceptionDto);
        request.setAttribute("type", strType);
        return mapping.findForward("zdhReceptionshowImg");
    }

    public ActionForward toolbarOperation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String hqlStr = "";
        GridInfoObj gridInfo = sysUiGridFacade.getGenGridInfo(request, hqlStr);
        String hqlSql = gridInfo.getHqlSql();
        int beginPage = gridInfo.getBeginPage();
        int pageSize = gridInfo.getPageSize();
        String sumSql = gridInfo.getSumSql();
        int count = 0;
        List dtolist = new ArrayList();
        List sumInfo = new ArrayList();
        if (!StringUtils.isEmpty(hqlSql)) {
            QueryListObj qObj = zdhReceptionFacade.getZdhReceptionByHql(hqlSql, beginPage, pageSize, sumSql);
            dtolist = qObj.getElemList();
        }
        ZdhReceptionDto zdhReceptionDto = (ZdhReceptionDto) dtolist.get(0);
        String mainId = zdhReceptionDto.getFapplicationid();
        return this.printResponseMes(request, response, mapping, "null", mainId + "");
    }
}
