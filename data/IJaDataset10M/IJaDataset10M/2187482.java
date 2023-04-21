package com.techstar.dmis.web.action;

import java.util.List;
import java.util.Map;
import java.io.File;
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
import com.techstar.framework.utils.ConfigurationHelper;
import com.techstar.framework.utils.SequenceCreator;
import org.apache.struts.upload.FormFile;
import com.techstar.framework.web.action.BaseDispatchAction;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.framework.ui.entity.GridInfoObj;
import com.techstar.framework.ui.web.facade.SysUiGridFacade;
import com.techstar.framework.ui.web.facade.TagEngineFacade;
import com.techstar.dmis.web.facade.DdAccidentbriefFacade;
import com.techstar.dmis.web.facade.DdDoutageplanFacade;
import com.techstar.dmis.web.facade.DdSwitchseqbillFacade;
import com.techstar.dmis.web.form.DdAccidentbriefForm;
import com.techstar.dmis.web.form.DdSwitchseqbillForm;
import com.techstar.dmis.common.DispatchConstants;
import com.techstar.dmis.dto.DdAccidentbriefDto;
import com.techstar.dmis.dto.DdAccidentbriefprocessDto;
import com.techstar.dmis.dto.DdCutoffsequenceDto;
import com.techstar.dmis.dto.DdDoutageplanDto;
import com.techstar.dmis.dto.DdMoutageplanDto;
import com.techstar.dmis.dto.DdSwitchseqbillDto;
import com.techstar.dmis.entity.DdAccidentbrief;
import com.techstar.dmis.entity.DdAccidentbriefprocess;
import com.techstar.dmis.entity.DdCutoffsequence;
import com.techstar.dmis.helper.BusinessDetailHelper;
import com.techstar.dmis.helper.dto.BusinessDetailDto;
import com.techstar.dmis.helper.dto.WorkflowHandleDto;

/**
 * @author
 * @date
 */
public class DdAccidentbriefAction extends BaseDispatchAction {

    private DdAccidentbriefFacade ddAccidentbriefFacade;

    private DdSwitchseqbillFacade ddSwitchseqbillFacade;

    private DdDoutageplanFacade ddDoutageplanFacade;

    private SysUiGridFacade sysUiGridFacade;

    public DdAccidentbriefAction() {
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
        List list = ddAccidentbriefFacade.listDdAccidentbrief();
        request.getSession().setAttribute("DdAccidentbriefList", list);
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
        DdAccidentbriefForm ddAccidentbriefForm = (DdAccidentbriefForm) form;
        DdAccidentbriefDto dto = ddAccidentbriefForm.getDdAccidentbriefDto();
        if (StringUtils.isEmpty(dto.getFaccidentid())) {
            dto.setFaccidentid(new SequenceCreator().getUID());
        }
        ddAccidentbriefFacade.addDdAccidentbrief(dto);
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
        DdAccidentbriefForm ddAccidentbriefForm = (DdAccidentbriefForm) form;
        String faccidentid = request.getParameter("faccidentid");
        if (StringUtils.isNotEmpty(faccidentid)) {
            DdAccidentbriefDto dto = ddAccidentbriefFacade.getDdAccidentbriefById(faccidentid);
            ddAccidentbriefForm.setDdAccidentbriefDto(dto);
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
        request.setCharacterEncoding("utf-8");
        String faccidentidStr = request.getParameter("faccidentid");
        String versionStr = request.getParameter("version");
        String[] primArra = faccidentidStr.split("\\^");
        String[] versionArra = versionStr.split("\\^");
        for (int i = 0; i < primArra.length; i++) {
            String faccidentid = primArra[i];
            String version = versionArra[i];
            if (StringUtils.isNotEmpty(faccidentid)) {
                ddAccidentbriefFacade.deleteDdAccidentbrief(faccidentid, version);
            }
        }
        return this.printResponseMes(request, response, mapping, "null", "删除成功");
    }

    /**
	 *
	 * 盖执行章
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward resume(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        String faccidentidStr = request.getParameter("faccidentid");
        String[] primArra = faccidentidStr.split("\\^");
        for (int i = 0; i < primArra.length; i++) {
            String faccidentid = primArra[i];
            if (StringUtils.isNotEmpty(faccidentid)) {
                DdAccidentbriefDto dto = ddAccidentbriefFacade.getDdAccidentbriefById(faccidentid);
                dto.setFstatus(DispatchConstants.ddAccidentbrief_resume);
                dto.setFbhactionrecord10(null);
                dto.setFddaccidentbriefprocess1(null);
                ddAccidentbriefFacade.addDdAccidentbrief(dto);
            }
        }
        return this.printResponseMes(request, response, mapping, "null", "盖执行章成功");
    }

    public void setDdAccidentbriefFacade(DdAccidentbriefFacade ddAccidentbriefFacade) {
        this.ddAccidentbriefFacade = ddAccidentbriefFacade;
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
            QueryListObj queryObj = ddAccidentbriefFacade.getDdAccidentbriefByHql(hqlSql, beginPage, pageSize, sumSql);
            dtolist = queryObj.getElemList();
            count = queryObj.getCount();
            sumInfo = queryObj.getPropertySum();
        }
        String xmlStr = sysUiGridFacade.parseParamToGridStr(request, gridInfo.getGridPersonalInfo(), dtolist, gridInfo.getBeginPage(), gridInfo.getPageSize(), count, sumInfo);
        return this.printResponseMes(request, response, mapping, "ddAccidentbriefgrid", xmlStr);
    }

    public ActionForward standGridQueryProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String parentid = request.getParameter("parentid");
        String hqlStr = "";
        String userId = "user";
        GridInfoObj gridInfo = sysUiGridFacade.getGridInfo(request, hqlStr, userId);
        int count = 0;
        String hql = "from DdAccidentbriefprocess D where D.zddaccidentbriefprocess1.faccidentid='" + parentid + "'" + " order by D.fprocesstime";
        List dtolist = new ArrayList();
        List sumInfo = new ArrayList();
        QueryListObj queryObj = ddAccidentbriefFacade.getDdAccidentbriefProcessByHql(hql);
        dtolist = queryObj.getElemList();
        count = queryObj.getCount();
        sumInfo = queryObj.getPropertySum();
        String xmlStr = sysUiGridFacade.parseParamToGridStr(request, gridInfo.getGridPersonalInfo(), dtolist, gridInfo.getBeginPage(), gridInfo.getPageSize(), count, sumInfo);
        return this.printResponseMes(request, response, mapping, "", xmlStr);
    }

    public ActionForward ddAccidentbrieftoolbar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = "<Toolbar>   <Business>      <id>DdAccidentbrief</id>      <name>故障简报</name>   </Business>   <ReportUrl>/report.do</ReportUrl>   <OperItem Text='拟定操作票' id='saveSwitchseqbill' href='saveSwitchseqbill' title=''></OperItem><OperItem Text='工作流-处理历史' id='wf_history' href='wf_history' title=''></OperItem>   </Toolbar>";
        request.setAttribute("xmlData", method);
        return mapping.findForward("ddAccidentbrieftoolbar");
    }

    public ActionForward ddAccidentbrieftab(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("ddAccidentbrieftab");
    }

    public ActionForward ddAccidentbriefframe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("ddAccidentbriefframe");
    }

    public ActionForward initDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DdAccidentbriefForm ddAccidentbriefForm = (DdAccidentbriefForm) form;
        String mainId = (String) request.getParameter("mainId");
        String isCopy = (String) request.getParameter("isCopy");
        DdAccidentbriefDto ddAccidentbriefDto = null;
        if (StringUtils.isNotEmpty(isCopy) && isCopy.equalsIgnoreCase("true")) {
            if (StringUtils.isNotEmpty(mainId)) {
                ddAccidentbriefDto = ddAccidentbriefFacade.getDdAccidentbriefById(mainId);
                ddAccidentbriefDto.setVersion(0);
                ddAccidentbriefDto.setFaccidentid(null);
                ddAccidentbriefDto.setFexcutestatus(DispatchConstants.ddAccidentbrief_new);
                ddAccidentbriefDto.setFstatus(DispatchConstants.ddAccidentbrief_WORKFLOW_CITY_STATUS_NEW);
            }
        } else {
            if (StringUtils.isNotEmpty(mainId)) {
                ddAccidentbriefDto = ddAccidentbriefFacade.getDdAccidentbriefById(mainId);
            } else {
                ddAccidentbriefDto = new DdAccidentbriefDto();
            }
        }
        ddAccidentbriefForm.setDdAccidentbriefDto(ddAccidentbriefDto);
        return mapping.findForward("ddAccidentbriefdetail");
    }

    public ActionForward saveDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DdAccidentbriefForm ddAccidentbriefForm = (DdAccidentbriefForm) form;
        DdAccidentbriefDto ddAccidentbriefDto = ddAccidentbriefForm.getDdAccidentbriefDto();
        if ((ddAccidentbriefDto.getFaccidentid() == null) || (ddAccidentbriefDto.getFaccidentid().equals(""))) {
            ddAccidentbriefDto.setFexcutestatus(DispatchConstants.ddAccidentbrief_new);
            ddAccidentbriefDto.setFstatus(DispatchConstants.ddAccidentbrief_WORKFLOW_CITY_STATUS_NEW);
        }
        ddAccidentbriefFacade.addDdAccidentbrief(ddAccidentbriefDto);
        ddAccidentbriefDto = ddAccidentbriefFacade.getDdAccidentbriefById(ddAccidentbriefDto.getFaccidentid());
        ddAccidentbriefForm.setDdAccidentbriefDto(ddAccidentbriefDto);
        saveMessages(request, "message.save.success", new String[] { "故障简报" });
        return mapping.findForward("ddAccidentbriefdetail");
    }

    public void setSysUiGridFacade(SysUiGridFacade sysUiGridFacade) {
        this.sysUiGridFacade = sysUiGridFacade;
    }

    public ActionForward deleteModechangeGrid(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String ids = request.getParameter("ids");
            String version = request.getParameter("version");
            response.setContentType("text/xml;charset=utf-8");
            System.out.println(ids);
            String[] idds = ids.split(";");
            String[] versions = version.split(";");
            ArrayList list = new ArrayList();
            for (int i = 0; i < idds.length; i++) {
                DdAccidentbriefprocess ddAccidentbriefprocess = new DdAccidentbriefprocess();
                ddAccidentbriefprocess.setFstepid(idds[i]);
                ddAccidentbriefprocess.setVersion(Integer.parseInt(versions[i]));
                list.add(ddAccidentbriefprocess);
            }
            ddAccidentbriefFacade.deleteDdAccidentbriefprocess(list);
            response.getWriter().println("1");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("0");
        }
        return null;
    }

    public ActionForward saveModechangeGrid(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String head = request.getParameter("head");
            String dates = request.getParameter("dates");
            response.setContentType("text/xml;charset=utf-8");
            System.out.println(head);
            System.out.println(dates);
            File[] files = ConfigurationHelper.getGridDisFile();
            String classPath = "";
            if (files.length > 0) {
                String path = files[0].getParent();
                classPath += path + "\\" + "DdAccidentbriefProcessDto.xml";
            }
            TagEngineFacade engine = new TagEngineFacade();
            String filePath = classPath;
            DdAccidentbriefprocessDto modechange = new DdAccidentbriefprocessDto();
            List list = engine.getDtoList(head, dates, modechange, filePath);
            ddAccidentbriefFacade.addDdAccidentbriefprocessByList(list);
            response.getWriter().println("1");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("0");
        }
        return null;
    }

    /**
	 *
	 * 从故障简报转操作票
	 *
	 * 周玮
	 *
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward saveSwitchseqbill(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        String faccidentidStr = request.getParameter("faccidentid");
        String[] primArra = faccidentidStr.split("\\^");
        for (int i = 0; i < primArra.length; i++) {
            String faccidentid = primArra[i];
            if (StringUtils.isNotEmpty(faccidentid)) {
                DdAccidentbriefDto ddAccidentbriefDto = ddAccidentbriefFacade.getDdAccidentbriefById(faccidentid);
                DdSwitchseqbillDto ddSwitchseqbillDto = new DdSwitchseqbillDto();
                BusinessDetailHelper ss = new BusinessDetailHelper();
                BusinessDetailDto defaultdto = ss.getBusDetailInfo(request);
                ddSwitchseqbillDto.setSys_filldept(defaultdto.getUserDeptName());
                ddSwitchseqbillDto.setSys_filltime(defaultdto.getCurrentTime());
                ddSwitchseqbillDto.setSys_dataowner(defaultdto.getDataOwnerName());
                ddSwitchseqbillDto.setSys_fille(defaultdto.getUserName());
                ddSwitchseqbillDto.setFdate(defaultdto.getCurrentDate());
                ddSwitchseqbillDto.setFexcutestatus(DispatchConstants.ddSwitchSeqbill_NEW);
                ddSwitchseqbillDto.setFstatus(DispatchConstants.DdSwitchseqBill_WORKFLOW_CITY_STATUS_NEW);
                ddSwitchseqbillDto.setFmanualno(ddSwitchseqbillFacade.getMaxfmanualno());
                ddSwitchseqbillDto.setFmission(ddAccidentbriefDto.getFaccidentname());
                ddSwitchseqbillDto.setFbillsource(DispatchConstants.ddAccidentbrief_Switchseqbill_billsource);
                ddSwitchseqbillDto.setFsrcbillno(ddAccidentbriefDto.getFaccidentid());
                ddSwitchseqbillFacade.addDdSwitchseqbill(ddSwitchseqbillDto);
                request.getSession().setAttribute("ddadddid", ddSwitchseqbillDto.getFcomputerno());
            }
        }
        return this.printResponseMes(request, response, mapping, "null", "拟定操作票成功");
    }

    /**
	 * 转日计划 已经作废
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward saveDayPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        String faccidentidStr = request.getParameter("faccidentid");
        String[] primArra = faccidentidStr.split("\\^");
        for (int i = 0; i < primArra.length; i++) {
            String faccidentid = primArra[i];
            if (StringUtils.isNotEmpty(faccidentid)) {
                DdAccidentbriefDto ddAccidentbriefDto = ddAccidentbriefFacade.getDdAccidentbriefById(faccidentid);
                DdDoutageplanDto ddDoutageplanDto = new DdDoutageplanDto();
                BusinessDetailHelper ss = new BusinessDetailHelper();
                BusinessDetailDto defaultdto = ss.getBusDetailInfo(request);
                ddDoutageplanDto.setSys_filldept(defaultdto.getUserDeptName());
                ddDoutageplanDto.setSys_filltime(defaultdto.getCurrentTime());
                ddDoutageplanDto.setSys_dataowner(defaultdto.getDataOwnerName());
                ddDoutageplanDto.setSys_fille(defaultdto.getUserName());
                ddDoutageplanDto.setFapplytime(defaultdto.getCurrentDate());
                ddDoutageplanDto.setFstatus(DispatchConstants.DdDoutageplan_WORKFLOW_CITY_STATUS_NEW);
                ddDoutageplanDto.setFexcutestatus(DispatchConstants.CITY_DdDoutageplan_NEW);
                ddDoutageplanDto.setFplansourcetype(DispatchConstants.ddAccidentbrief_Switchseqbill_billsource);
                ddDoutageplanDto.setFplansourceno(ddAccidentbriefDto.getFaccidentid());
                ddDoutageplanDto.setFremark(ddAccidentbriefDto.getFaccidentname());
                ddDoutageplanFacade.addDdDoutageplan(ddDoutageplanDto);
                ddAccidentbriefDto.setFexcutestatus(DispatchConstants.ddAccidentbrief_send_to_dayplan);
                ddAccidentbriefDto.setFbhactionrecord10(null);
                ddAccidentbriefDto.setFddaccidentbriefprocess1(null);
                ddAccidentbriefFacade.addDdAccidentbrief(ddAccidentbriefDto);
                request.getSession().setAttribute("ddadddid", ddDoutageplanDto.getFdayplanno());
            }
        }
        return this.printResponseMes(request, response, mapping, "null", "转日计划成功");
    }

    public void setDdSwitchseqbillFacade(DdSwitchseqbillFacade ddSwitchseqbillFacade) {
        this.ddSwitchseqbillFacade = ddSwitchseqbillFacade;
    }

    public void setDdDoutageplanFacade(DdDoutageplanFacade ddDoutageplanFacade) {
        this.ddDoutageplanFacade = ddDoutageplanFacade;
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
        String transitionFlag = request.getParameter("transitionFlag");
        WorkflowHandleDto dto = new WorkflowHandleDto();
        dto.setBusId(busId);
        dto.setIsSms(isSms);
        dto.setMessages(messages);
        dto.setNotices(notices);
        dto.setRoleId(roleId);
        dto.setSentPersons(sentPersons);
        dto.setStatus(status);
        dto.setTaskId(taskId);
        dto.setTaskInstanceId(taskInstanceId);
        dto.setTransitionFlag(transitionFlag);
        dto.setUserId(userId);
        if (StringUtils.isNotEmpty(busId)) {
            if (status.equals(DispatchConstants.ddAccidentbrief_WORKFLOW_CITY_STATUS_NEW)) {
                System.out.println("转审核....");
                ddAccidentbriefFacade.start(dto);
                DdAccidentbriefDto ddAccidentbrief = ddAccidentbriefFacade.getDdAccidentbriefById(busId);
                ddAccidentbrief.setFstatus(DispatchConstants.ddAccidentbrief_WORKFLOW_CITY_STATUS_APPROVE);
                ddAccidentbrief.setFexcutestatus(DispatchConstants.ddAccidentbrief_auditing);
                ddAccidentbrief.setFbhactionrecord10(null);
                ddAccidentbrief.setFddaccidentbriefprocess1(null);
                ddAccidentbriefFacade.addDdAccidentbrief(ddAccidentbrief);
                System.out.println("转审核结束....");
            }
            if (status.equals(DispatchConstants.ddAccidentbrief_WORKFLOW_CITY_STATUS_MODIFY)) {
                System.out.println("修改->审核....");
                ddAccidentbriefFacade.modify(dto);
                DdAccidentbriefDto ddAccidentbrief = ddAccidentbriefFacade.getDdAccidentbriefById(busId);
                ddAccidentbrief.setFbhactionrecord10(null);
                ddAccidentbrief.setFddaccidentbriefprocess1(null);
                ddAccidentbrief.setFstatus(DispatchConstants.ddAccidentbrief_WORKFLOW_CITY_STATUS_APPROVE);
                ddAccidentbriefFacade.addDdAccidentbrief(ddAccidentbrief);
                System.out.println("修改->审核 结束....");
            }
            if (status.equals(DispatchConstants.ddAccidentbrief_WORKFLOW_CITY_STATUS_APPROVE)) {
                System.out.println("开始审核....");
                ddAccidentbriefFacade.approve(dto);
                System.out.println("开始审核############....");
                if (dto.getTransitionFlag().equals("-1")) {
                    DdAccidentbriefDto ddAccidentbrief = ddAccidentbriefFacade.getDdAccidentbriefById(busId);
                    ddAccidentbrief.setFstatus(DispatchConstants.ddAccidentbrief_WORKFLOW_CITY_STATUS_MODIFY);
                    ddAccidentbrief.setFbhactionrecord10(null);
                    ddAccidentbrief.setFddaccidentbriefprocess1(null);
                    ddAccidentbriefFacade.addDdAccidentbrief(ddAccidentbrief);
                }
                System.out.println("审核结束....");
            }
            if (status.equals(DispatchConstants.ddAccidentbrief_WORKFLOW_CITY_STATUS_EXE)) {
                System.out.println("开始执行....");
                ddAccidentbriefFacade.execute(dto);
                if (dto.getTransitionFlag().equals("1")) {
                    DdAccidentbriefDto ddAccidentbrief = ddAccidentbriefFacade.getDdAccidentbriefById(busId);
                    ddAccidentbrief.setFstatus(DispatchConstants.ddAccidentbrief_WORKFLOW_CITY_STATUS_OVER);
                    ddAccidentbrief.setFexcutestatus(DispatchConstants.ddAccidentbrief_resume);
                    ddAccidentbrief.setFbhactionrecord10(null);
                    ddAccidentbrief.setFddaccidentbriefprocess1(null);
                    ddAccidentbriefFacade.addDdAccidentbrief(ddAccidentbrief);
                }
                if (dto.getTransitionFlag().equals("-1")) {
                    DdAccidentbriefDto ddAccidentbrief = ddAccidentbriefFacade.getDdAccidentbriefById(busId);
                    ddAccidentbrief.setFstatus(DispatchConstants.ddAccidentbrief_WORKFLOW_CITY_STATUS_OVER);
                    ddAccidentbrief.setFexcutestatus(DispatchConstants.ddAccidentbrief_send_to_dayplan);
                    ddAccidentbrief.setFbhactionrecord10(null);
                    ddAccidentbrief.setFddaccidentbriefprocess1(null);
                    ddAccidentbriefFacade.addDdAccidentbrief(ddAccidentbrief);
                    DdDoutageplanDto ddDoutageplanDto = new DdDoutageplanDto();
                    BusinessDetailHelper ss = new BusinessDetailHelper();
                    BusinessDetailDto defaultdto = ss.getBusDetailInfo(request);
                    ddDoutageplanDto.setSys_filldept(defaultdto.getUserDeptName());
                    ddDoutageplanDto.setSys_filltime(defaultdto.getCurrentTime());
                    ddDoutageplanDto.setSys_dataowner(defaultdto.getDataOwnerName());
                    ddDoutageplanDto.setSys_fille(defaultdto.getUserName());
                    ddDoutageplanDto.setFapplytime(defaultdto.getCurrentDate());
                    ddDoutageplanDto.setFstatus(DispatchConstants.DdDoutageplan_WORKFLOW_CITY_STATUS_NEW);
                    ddDoutageplanDto.setFexcutestatus(DispatchConstants.CITY_DdDoutageplan_NEW);
                    ddDoutageplanDto.setFplansourcetype(DispatchConstants.ddAccidentbrief_Switchseqbill_billsource);
                    ddDoutageplanDto.setFplansourceno(ddAccidentbrief.getFaccidentid());
                    ddDoutageplanDto.setFremark(ddAccidentbrief.getFaccidentname());
                    ddDoutageplanFacade.addDdDoutageplan(ddDoutageplanDto);
                    request.getSession().setAttribute("ddadddid", ddDoutageplanDto.getFdayplanno());
                }
                System.out.println("审核执行....");
            }
        }
        return mapping.findForward("");
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
            QueryListObj qObj = ddAccidentbriefFacade.getDdAccidentbriefByHql(hqlSql, beginPage, pageSize, sumSql);
            dtolist = qObj.getElemList();
        }
        DdAccidentbriefDto ddAccidentbriefDto = (DdAccidentbriefDto) dtolist.get(0);
        String mainId = ddAccidentbriefDto.getFaccidentid();
        return this.printResponseMes(request, response, mapping, "null", mainId + "");
    }
}
