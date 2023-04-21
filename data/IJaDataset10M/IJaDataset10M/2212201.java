package com.techstar.dmis.web.action;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import com.techstar.dmis.dto.DdClassondutylogDto;
import com.techstar.dmis.dto.DdHloadrecordDto;
import com.techstar.dmis.dto.DdOperationnotebookDto;
import com.techstar.dmis.dto.DdSeqbilldetailDto;
import com.techstar.dmis.dto.DdSwitchseqbillDto;
import com.techstar.dmis.util.DateUtil;
import com.techstar.dmis.web.facade.DdClassondutylogFacade;
import com.techstar.dmis.web.form.DdClassondutylogForm;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.framework.service.dto.UserProfile;
import com.techstar.framework.service.security.SecurityContext;
import com.techstar.framework.ui.entity.GridInfoObj;
import com.techstar.framework.ui.web.facade.SysUiGridFacade;
import com.techstar.framework.ui.web.facade.TagEngineFacade;
import com.techstar.framework.utils.ConfigurationHelper;
import com.techstar.framework.utils.SequenceCreator;
import com.techstar.framework.web.action.BaseDispatchAction;
import com.techstar.dmis.helper.BusinessDetailHelper;
import com.techstar.dmis.helper.dto.BusinessDetailDto;
import com.techstar.dmis.web.facade.DdHloadrecordFacade;
import com.techstar.dmis.web.facade.DdHpoweroperaterrdFacade;
import com.techstar.dmis.web.facade.DdPowerchangrecordFacade;
import com.techstar.dmis.web.facade.DdWaterheightFacade;
import com.techstar.dmis.web.facade.DdSasusprecordFacade;
import com.techstar.dmis.web.facade.DdWaterproofplanFacade;

/**
 * @author
 * @date
 */
public class DdClassondutylogAction extends BaseDispatchAction {

    private DdClassondutylogFacade ddClassondutylogFacade;

    private DdHloadrecordFacade ddHloadrecordFacade;

    DdHpoweroperaterrdFacade ddHpoweroperaterrdFacade;

    DdPowerchangrecordFacade ddPowerchangrecordFacade;

    DdWaterheightFacade ddWaterheightFacade;

    DdSasusprecordFacade ddSasusprecordFacade;

    DdWaterproofplanFacade ddWaterproofplanFacade;

    private SysUiGridFacade sysUiGridFacade;

    public DdClassondutylogAction() {
    }

    public void iniDefaultValue(HttpServletRequest request, DdClassondutylogDto dto) {
        long nCurrentTime = System.currentTimeMillis();
        java.sql.Timestamp fCurTime = new java.sql.Timestamp(nCurrentTime);
        BusinessDetailHelper ss = new BusinessDetailHelper();
        BusinessDetailDto defaultdto = ss.getBusDetailInfo(request);
        dto.setSys_filldept(defaultdto.getUserDeptName());
        dto.setSys_filltime(fCurTime);
        dto.setSys_dataowner(defaultdto.getDataOwnerName());
        dto.setSys_fille(defaultdto.getUserName());
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
        List list = ddClassondutylogFacade.listDdClassondutylog();
        request.getSession().setAttribute("DdClassondutylogList", list);
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
        DdClassondutylogForm ddClassondutylogForm = (DdClassondutylogForm) form;
        DdClassondutylogDto dto = ddClassondutylogForm.getDdClassondutylogDto();
        FormFile content = ddClassondutylogForm.getContent();
        if (content != null) {
            dto.setContent(content.getFileData());
        }
        if (StringUtils.isEmpty(dto.getFid())) {
            dto.setFid(new SequenceCreator().getUID());
            long nCurrentTime = System.currentTimeMillis();
            java.sql.Timestamp fsuccessiontime = new java.sql.Timestamp(nCurrentTime);
            dto.setFsuccessiontime(fsuccessiontime);
            BusinessDetailHelper ss = new BusinessDetailHelper();
            BusinessDetailDto defaultdto = ss.getBusDetailInfo(request);
            dto.setSys_filldept(defaultdto.getUserDeptName());
            dto.setSys_filltime(fsuccessiontime);
            dto.setSys_dataowner(defaultdto.getDataOwnerName());
            dto.setSys_fille(defaultdto.getUserName());
        }
        ddClassondutylogFacade.addDdClassondutylog(dto);
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
        DdClassondutylogForm ddClassondutylogForm = (DdClassondutylogForm) form;
        String fid = request.getParameter("fid");
        if (StringUtils.isNotEmpty(fid)) {
            DdClassondutylogDto dto = ddClassondutylogFacade.getDdClassondutylogById(fid);
            ddClassondutylogForm.setDdClassondutylogDto(dto);
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
        String fid = request.getParameter("fid");
        int version = Integer.parseInt(request.getParameter("version"));
        if (StringUtils.isNotEmpty(fid)) {
            List list = ddClassondutylogFacade.deleteDdClassondutylog(fid, version);
            request.getSession().setAttribute("DdClassondutylogList", list);
        }
        return this.printResponseMes(request, response, mapping, "null", "删除成功");
    }

    public void setDdClassondutylogFacade(DdClassondutylogFacade ddClassondutylogFacade) {
        this.ddClassondutylogFacade = ddClassondutylogFacade;
    }

    public ActionForward download_content(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fid = request.getParameter("fid");
        DdClassondutylogDto dto = ddClassondutylogFacade.getDdClassondutylogById(fid);
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
        hqlSql += " order by t.fsuccessiontime desc";
        System.out.println("kkkkkkkkkkkkkkkkkkkkk");
        System.out.println("hqlsql=" + hqlSql);
        System.out.println("kkkkkkkkkkkkkkkkkkkkk");
        int beginPage = gridInfo.getBeginPage();
        int pageSize = gridInfo.getPageSize();
        String sumSql = gridInfo.getSumSql();
        Map params = gridInfo.getGridPersonalInfo();
        int count = 0;
        List dtolist = new ArrayList();
        List sumInfo = new ArrayList();
        if (!StringUtils.isEmpty(hqlSql)) {
            QueryListObj queryObj = ddClassondutylogFacade.getDdClassondutylogByHql(hqlSql, beginPage, pageSize, sumSql);
            dtolist = queryObj.getElemList();
            count = queryObj.getCount();
            if (count == 0) {
                DdClassondutylogDto dto = new DdClassondutylogDto();
                if (StringUtils.isEmpty(dto.getFid())) {
                    dto.setFid(new SequenceCreator().getUID());
                    long nCurrentTime = System.currentTimeMillis();
                    java.sql.Timestamp fsuccessiontime = new java.sql.Timestamp(nCurrentTime);
                    dto.setFsuccessiontime(fsuccessiontime);
                    BusinessDetailHelper ss = new BusinessDetailHelper();
                    BusinessDetailDto defaultdto = ss.getBusDetailInfo(request);
                    dto.setSys_filldept(defaultdto.getUserDeptName());
                    dto.setSys_filltime(fsuccessiontime);
                    dto.setSys_dataowner(defaultdto.getDataOwnerName());
                    dto.setSys_fille(defaultdto.getUserName());
                }
                ddClassondutylogFacade.addDdClassondutylog(dto);
                QueryListObj queryObj1 = ddClassondutylogFacade.getDdClassondutylogByHql(hqlSql, beginPage, pageSize, sumSql);
                dtolist = queryObj1.getElemList();
                count = queryObj1.getCount();
            }
            sumInfo = queryObj.getPropertySum();
        }
        String xmlStr = sysUiGridFacade.parseParamToGridStr(request, gridInfo.getGridPersonalInfo(), dtolist, gridInfo.getBeginPage(), gridInfo.getPageSize(), count, sumInfo);
        return this.printResponseMes(request, response, mapping, "ddClassondutyloggrid", xmlStr);
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
            QueryListObj queryObj = ddHloadrecordFacade.getDdHloadrecordByHql(hqlSql, beginPage, pageSize, sumSql);
            dtolist = queryObj.getElemList();
            count = queryObj.getCount();
            sumInfo = queryObj.getPropertySum();
        }
        String xmlStr = sysUiGridFacade.parseParamToGridStr(request, gridInfo.getGridPersonalInfo(), dtolist, gridInfo.getBeginPage(), gridInfo.getPageSize(), count, sumInfo);
        return this.printResponseMes(request, response, mapping, "ddClassondutyloggrid1", xmlStr);
    }

    public ActionForward standGridQuery2(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String hqlStr = "";
        String userId = "user";
        String swhere = request.getParameter("extWheres");
        System.out.println("########################");
        System.out.println(swhere);
        System.out.println("########################");
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
            QueryListObj queryObj = ddSasusprecordFacade.getDdSasusprecordByHql(hqlSql, beginPage, pageSize, sumSql);
            dtolist = queryObj.getElemList();
            count = queryObj.getCount();
            sumInfo = queryObj.getPropertySum();
        }
        String xmlStr = sysUiGridFacade.parseParamToGridStr(request, gridInfo.getGridPersonalInfo(), dtolist, gridInfo.getBeginPage(), gridInfo.getPageSize(), count, sumInfo);
        return this.printResponseMes(request, response, mapping, "ddClassondutyloggrid2", xmlStr);
    }

    public ActionForward standGridQuery3(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
            QueryListObj queryObj = ddClassondutylogFacade.getDdOperationnotebookByHql(hqlSql, beginPage, pageSize, sumSql);
            dtolist = queryObj.getElemList();
            count = queryObj.getCount();
            sumInfo = queryObj.getPropertySum();
        }
        String xmlStr = sysUiGridFacade.parseParamToGridStr(request, gridInfo.getGridPersonalInfo(), dtolist, gridInfo.getBeginPage(), gridInfo.getPageSize(), count, sumInfo);
        return this.printResponseMes(request, response, mapping, "ddClassondutyloggrid3", xmlStr);
    }

    public ActionForward standGridQuery4(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String hqlStr = "";
        String userId = "user";
        String swhere = request.getParameter("extWheres");
        System.out.print("########################");
        System.out.print(swhere);
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
            QueryListObj queryObj = this.ddPowerchangrecordFacade.getDdPowerchangrecordByHql(hqlSql, beginPage, pageSize, sumSql);
            dtolist = queryObj.getElemList();
            count = queryObj.getCount();
            sumInfo = queryObj.getPropertySum();
        }
        String xmlStr = sysUiGridFacade.parseParamToGridStr(request, gridInfo.getGridPersonalInfo(), dtolist, gridInfo.getBeginPage(), gridInfo.getPageSize(), count, sumInfo);
        return this.printResponseMes(request, response, mapping, "ddClassondutyloggrid4", xmlStr);
    }

    public ActionForward standGridQuery5(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
            QueryListObj queryObj = this.ddHpoweroperaterrdFacade.getDdHpoweroperaterrdByHql(hqlSql, beginPage, pageSize, sumSql);
            dtolist = queryObj.getElemList();
            count = queryObj.getCount();
            sumInfo = queryObj.getPropertySum();
        }
        String xmlStr = sysUiGridFacade.parseParamToGridStr(request, gridInfo.getGridPersonalInfo(), dtolist, gridInfo.getBeginPage(), gridInfo.getPageSize(), count, sumInfo);
        return this.printResponseMes(request, response, mapping, "ddClassondutyloggrid5", xmlStr);
    }

    public ActionForward standGridQuery6(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
            QueryListObj queryObj = this.ddWaterheightFacade.getDdWaterheightByHql(hqlSql, beginPage, pageSize, sumSql);
            dtolist = queryObj.getElemList();
            count = queryObj.getCount();
            sumInfo = queryObj.getPropertySum();
        }
        String xmlStr = sysUiGridFacade.parseParamToGridStr(request, gridInfo.getGridPersonalInfo(), dtolist, gridInfo.getBeginPage(), gridInfo.getPageSize(), count, sumInfo);
        return this.printResponseMes(request, response, mapping, "ddClassondutyloggrid6", xmlStr);
    }

    public ActionForward standGridQuery7(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
            QueryListObj queryObj = this.ddWaterproofplanFacade.getDdWaterproofplanByHql(hqlSql, beginPage, pageSize, sumSql);
            dtolist = queryObj.getElemList();
            count = queryObj.getCount();
            sumInfo = queryObj.getPropertySum();
        }
        String xmlStr = sysUiGridFacade.parseParamToGridStr(request, gridInfo.getGridPersonalInfo(), dtolist, gridInfo.getBeginPage(), gridInfo.getPageSize(), count, sumInfo);
        return this.printResponseMes(request, response, mapping, "ddClassondutyloggrid7", xmlStr);
    }

    public ActionForward ddClassondutylogtoolbar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = "<Toolbar>";
        method += "<Business>";
        method += "<id>DdClassondutylog</id>";
        method += "<name>调度交接班记录</name>";
        method += "</Business>";
        method += "<ReportUrl>/report.do</ReportUrl>";
        method += "<OperItem Text='交接班' id='turnOver' href='turnOver' title=''></OperItem>";
        method += "<OperItem Text='按照指定的格式生成打印预览文件' id='PrintView' href='PrintView' title=''></OperItem>";
        method += "</Toolbar>";
        request.setAttribute("xmlData", method);
        return mapping.findForward("ddClassondutylogtoolbar");
    }

    public ActionForward ddClassondutylogtab(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("ddClassondutylogtab");
    }

    public ActionForward ddClassondutylogframe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("ddClassondutylogframe");
    }

    public ActionForward initDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DdClassondutylogForm ddClassondutylogForm = (DdClassondutylogForm) form;
        String mainId = (String) request.getParameter("mainId");
        String isCopy = (String) request.getParameter("isCopy");
        DdClassondutylogDto ddClassondutylogDto = null;
        if (StringUtils.isNotEmpty(isCopy) && isCopy.equalsIgnoreCase("true")) {
            if (StringUtils.isNotEmpty(mainId)) {
                ddClassondutylogDto = ddClassondutylogFacade.getDdClassondutylogById(mainId);
                ddClassondutylogDto.setVersion(0);
            }
        } else {
            if (StringUtils.isNotEmpty(mainId)) {
                ddClassondutylogDto = ddClassondutylogFacade.getDdClassondutylogById(mainId);
            } else {
                ddClassondutylogDto = new DdClassondutylogDto();
                iniDefaultValue(request, ddClassondutylogDto);
            }
        }
        ddClassondutylogForm.setDdClassondutylogDto(ddClassondutylogDto);
        return mapping.findForward("ddClassondutylogdetail");
    }

    public ActionForward saveDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DdClassondutylogForm ddClassondutylogForm = (DdClassondutylogForm) form;
        DdClassondutylogDto ddClassondutylogDto = ddClassondutylogForm.getDdClassondutylogDto();
        ddClassondutylogFacade.addDdClassondutylog(ddClassondutylogDto);
        ddClassondutylogDto = ddClassondutylogFacade.getDdClassondutylogById(ddClassondutylogDto.getFid());
        ddClassondutylogForm.setDdClassondutylogDto(ddClassondutylogDto);
        saveMessages(request, "message.save.success", new String[] { "调度交接班记录Dto" });
        return mapping.findForward("ddClassondutylogdetail");
    }

    public ActionForward initDetail1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("ddClassondutylogdetail1");
    }

    public ActionForward saveDetail1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("ddClassondutylogdetail1");
    }

    public void setSysUiGridFacade(SysUiGridFacade sysUiGridFacade) {
        this.sysUiGridFacade = sysUiGridFacade;
    }

    public ActionForward PrintView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.printResponseMes(request, response, mapping, "null", "打印预览");
    }

    /**
	 *交接班 ,生成下一班的记录
	 */
    public ActionForward turnover(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fid = request.getParameter("fid");
        DdClassondutylogDto Curdutylogdto = ddClassondutylogFacade.getDdClassondutylogById(fid);
        DdClassondutylogDto Nextdutylogdto = new DdClassondutylogDto();
        Nextdutylogdto.setFid(new SequenceCreator().getUID());
        long nCurrentTime = System.currentTimeMillis();
        java.sql.Timestamp fsuccessiontime = new java.sql.Timestamp(nCurrentTime);
        Nextdutylogdto.setFsuccessiontime(fsuccessiontime);
        Nextdutylogdto.setFdelresponser(Curdutylogdto.getFminister());
        Nextdutylogdto.setFdelattworkgroup(Curdutylogdto.getFmainattendant());
        Nextdutylogdto.setFdelattworkgroup(Curdutylogdto.getFsecondattendant());
        BusinessDetailHelper ss = new BusinessDetailHelper();
        BusinessDetailDto defaultdto = ss.getBusDetailInfo(request);
        Nextdutylogdto.setSys_filldept(defaultdto.getUserDeptName());
        Nextdutylogdto.setSys_filltime(fsuccessiontime);
        Nextdutylogdto.setSys_dataowner(defaultdto.getDataOwnerName());
        Nextdutylogdto.setSys_fille(defaultdto.getUserName());
        ddClassondutylogFacade.addDdClassondutylog(Nextdutylogdto);
        Curdutylogdto.setFhandovertime(DateUtil.getCurrentTimestamp());
        Curdutylogdto.setFddpowerplantoperlog4(null);
        Curdutylogdto.setFddoperationnotebook4(null);
        Curdutylogdto.setFddsasusprecord3(null);
        Curdutylogdto.setFddwaterheight3(null);
        Curdutylogdto.setFddwaterproofplan2(null);
        Curdutylogdto.setFddhloadrecord4(null);
        Curdutylogdto.setFddpowerchangrecord4(null);
        Curdutylogdto.setFddhpoweroperaterrd5(null);
        ddClassondutylogFacade.addDdClassondutylog(Curdutylogdto);
        request.getSession().removeAttribute(SecurityContext.SECURITY_AUTHENTICATION_KEY);
        response.getWriter().print("交接班完成");
        return null;
    }

    /**
	 * 保存运行记事
	 */
    public ActionForward saveddOperationnotebookGrid(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String head = request.getParameter("head");
            String dates = request.getParameter("dates");
            String parentid = request.getParameter("parentid");
            response.setContentType("text/xml;charset=utf-8");
            System.out.println(head);
            System.out.println(dates);
            File[] files = ConfigurationHelper.getGridDisFile();
            String classPath = "";
            if (files.length > 0) {
                String path = files[0].getParent();
                classPath += path + "\\" + "DdClassondutylogDto3" + ".xml";
            }
            TagEngineFacade engine = new TagEngineFacade();
            String filePath = classPath;
            DdOperationnotebookDto dto = new DdOperationnotebookDto();
            List list = engine.getDtoList(head, dates, dto, filePath);
            List listnew = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                DdOperationnotebookDto dto1 = (DdOperationnotebookDto) list.get(i);
                BusinessDetailHelper ss = new BusinessDetailHelper();
                BusinessDetailDto defaultdto = ss.getBusDetailInfo(request);
                dto1.setSys_filldept(defaultdto.getUserDeptName());
                dto1.setSys_filltime(DateUtil.getCurrentTimestamp());
                dto1.setSys_dataowner(defaultdto.getDataOwnerName());
                dto1.setSys_fille(defaultdto.getUserName());
                dto1.setZddoperationnotebook5(null);
                listnew.add(dto1);
            }
            ddClassondutylogFacade.saveOrUpdateDdOperationnotebook(list);
            response.getWriter().println("1");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("0");
        }
        return null;
    }

    /**
	 * 删除运行记事
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward deleteddOperationnotebookGrid(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String ids = request.getParameter("ids");
            String version = request.getParameter("version");
            response.setContentType("text/xml;charset=utf-8");
            System.out.println(ids);
            String[] idds = ids.split(";");
            String[] versions = version.split(";");
            String fid = "";
            for (int i = 0; i < idds.length; i++) {
                DdOperationnotebookDto dto = new DdOperationnotebookDto();
                dto.setFid(idds[i]);
                dto.setVersion(Integer.parseInt(versions[i]));
                dto.setZddoperationnotebook5(null);
                dto.setZddoperationnotebook4(null);
                ddClassondutylogFacade.deleteDdOperationnotebook(dto);
            }
            response.getWriter().println("1");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("0");
        }
        return null;
    }

    /**
	 * 
	 * @param ddHloadrecordFacade
	 */
    public void setDdHloadrecordFacade(DdHloadrecordFacade ddHloadrecordFacade) {
        this.ddHloadrecordFacade = ddHloadrecordFacade;
    }

    public void setDdHpoweroperaterrdFacade(DdHpoweroperaterrdFacade ddHpoweroperaterrdFacade) {
        this.ddHpoweroperaterrdFacade = ddHpoweroperaterrdFacade;
    }

    public void setDdPowerchangrecordFacade(DdPowerchangrecordFacade ddPowerchangrecordFacade) {
        this.ddPowerchangrecordFacade = ddPowerchangrecordFacade;
    }

    public void setDdWaterheightFacade(DdWaterheightFacade ddWaterheightFacade) {
        this.ddWaterheightFacade = ddWaterheightFacade;
    }

    public void setDdSasusprecordFacade(DdSasusprecordFacade ddSasusprecordFacade) {
        this.ddSasusprecordFacade = ddSasusprecordFacade;
    }

    public void setDdWaterproofplanFacade(DdWaterproofplanFacade ddWaterproofplanFacade) {
        this.ddWaterproofplanFacade = ddWaterproofplanFacade;
    }
}
