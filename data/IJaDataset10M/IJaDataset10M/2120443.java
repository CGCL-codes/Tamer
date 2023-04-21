package org.paradise.dms.web.action.lodgemgr;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.directwebremoting.annotations.RemoteProxy;
import org.paradise.dms.poi.NeoPoiUtil;
import org.paradise.dms.poi.PoiUtil;
import org.paradise.dms.services.DormitoryChargeService;
import org.paradise.dms.web.action.DMSBaseAction;
import org.paradise.dms.web.tools.DWRPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * Description: 学生住宿费用管理Action
 * 
 * Copyright (c) 2008-2009 paraDise sTudio(DT). All Rights Reserved.
 * 
 * @version 1.0 Apr 15, 2009 1:48:10 PM 李双江（paradise.lsj@gmail.com）created
 */
@Service
@RemoteProxy(name = "studentDormitoryChargeMgrAction")
public class StudentDormitoryChargeMgrAction extends DMSBaseAction {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(StudentDormitoryChargeMgrAction.class);

    @Autowired
    private DormitoryChargeService dormitoryChargeService;

    /**
	 * 
	 */
    private static final long serialVersionUID = -8730787748931141252L;

    public boolean exportToCharge(String ids, String year) {
        dormitoryChargeService.exportToCharge(ids, year);
        return true;
    }

    /**
	 * 
	 * Description: 条件查询学生住宿缴费的信息
	 * 
	 * @Version1.0 Apr 15, 2009 2:37:49 PM 李双江（paradise.lsj@gmail.com）创建
	 * @param pageNum
	 * @param totalRow
	 * @param enrollyear
	 *            学生的入学年份
	 * 
	 * @param feesyear
	 *            查询的交费年度
	 * 
	 * @param chargestatus
	 *            交费的状态
	 * 
	 * @param lodgechargetype
	 *            交费类型(正常,延期)
	 * 
	 * @param session
	 * @return
	 */
    @SuppressWarnings({ "static-access", "unchecked" })
    public JSONArray searchStudentLodgeChargeInfoPerPage(int pageNum, int totalRow, String enrollyear, String feesyear, String chargestatus, String lodgechargetype, HttpSession session) {
        DWRPage page = new DWRPage();
        page.setPageProperties(totalRow, 12);
        page.setCurrentPage(pageNum);
        session.setAttribute("page", page);
        int cp = (page.getCurrentPage() - 1) * 12;
        int ps = page.getPageSize();
        List list = dormitoryChargeService.getDormitoryCharge(enrollyear, feesyear, Integer.parseInt(chargestatus), Integer.parseInt(lodgechargetype), cp, ps);
        if (list != null & list.size() > 0) {
            JSONArray dlist = new JSONArray().fromObject(list);
            return dlist;
        }
        return null;
    }

    /**
	 * 
	 * Description: 根据条件高级查询未交费的学生
	 * 
	 * @param searchstudentsql
	 * @return
	 */
    @SuppressWarnings({ "static-access", "unchecked" })
    public JSONArray getStudentBySQLSearchConditionsPerPage(int pageNum, int totalCount, String searchstudentsql, HttpSession session) {
        log.info("in action student..");
        List stulist = new ArrayList();
        int totalRow = totalCount;
        DWRPage page = new DWRPage();
        page.setPageProperties(totalRow, 20);
        page.setCurrentPage(pageNum);
        session.setAttribute("page", page);
        int cp = (page.getCurrentPage() - 1) * 20;
        int ps = page.getPageSize();
        stulist = dormitoryChargeService.getStudentBySQLSearchConditions(searchstudentsql, ps, cp);
        if (stulist != null & stulist.size() > 0) {
            JSONArray stulistjson = new JSONArray().fromObject(stulist);
            return stulistjson;
        }
        return null;
    }

    /**
	 * 
	 * Description: 返回总行数
	 * 
	 * @Version1.0 Apr 18, 2009 7:02:14 PM 李双江（paradise.lsj@gmail.com）创建
	 * @param enrollyear
	 * @param feesyear
	 * @param chargestatus
	 * @param lodgechargetype
	 * @return
	 */
    public int getStudentLodgeChargeInfoRows(String enrollyear, String feesyear, String chargestatus, String lodgechargetype) {
        return dormitoryChargeService.getStudentLodgeChargeInfoRows(enrollyear, feesyear, chargestatus, lodgechargetype);
    }

    /**
	 * 
	 * Description: 返回待收费学生的总行数
	 * @param enrollyear
	 * @param feesyear
	 * @param chargestatus
	 * @param lodgechargetype
	 * @return
	 */
    public int getCount(String searchsql) {
        return dormitoryChargeService.getCount(searchsql);
    }

    /**
	 * 
	 * Description: 高级查询页面全选所有查询到的学生
	 * 
	 * @Version1.0 Apr 21, 2009 4:08:42 PM 郑旭（zhengxu2006@gmail.com）创建
	 * @param searchstudentsql
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public JSONArray getStudentBySQL(String searchstudentsql) {
        try {
            List slist_id = new ArrayList();
            slist_id = this.dormitoryChargeService.getStudentBySQL(searchstudentsql);
            log.info("根据sql查询到的学生数目" + slist_id.size());
            if (slist_id != null & slist_id.size() > 0) {
                {
                    JSONArray jsonArray = JSONArray.fromObject(slist_id);
                    log.info(slist_id.get(0));
                    log.info(jsonArray.get(0) + "$" + jsonArray.get(1));
                    return jsonArray;
                }
            }
            return null;
        } catch (Exception e) {
            log.info("DMS_error:高级查询页面全选所有查询到的学生有误！");
            log.info("DMS_error:错误原因！" + e);
            return null;
        }
    }

    /**
	 * 
	 * Description:返回DWR页面分页对象
	 * 
	 * @Version1.0 2009-3-28 上午11:13:26 李双江（paradise.lsj@gmail.com）创建
	 * @param session
	 * @return
	 */
    @SuppressWarnings("static-access")
    public JSONObject getPage(HttpSession session) {
        JSONObject joa = new JSONObject().fromObject((DWRPage) session.getAttribute("page"));
        return joa;
    }

    public DormitoryChargeService getDormitoryChargeService() {
        return dormitoryChargeService;
    }

    public void setDormitoryChargeService(DormitoryChargeService dormitoryChargeService) {
        this.dormitoryChargeService = dormitoryChargeService;
    }

    @SuppressWarnings("unused")
    private PoiUtil poiUtil;

    private String enrollyear;

    private String feesyear;

    private String chargestatus;

    private String lodgechargetype;

    @SuppressWarnings("unchecked")
    public String exportToExcel() {
        List listForExport = new ArrayList();
        String[] chargeFeesRowValues = { "学生学号", "学生姓名", "交费年度", "交费金额 " };
        List list = dormitoryChargeService.getDormitoryChargeForExport(this.getEnrollyear(), this.getFeesyear(), Integer.parseInt(this.getChargestatus()), Integer.parseInt(this.getLodgechargetype()));
        System.out.println("=============交费记录条数：" + list.size());
        for (int i = 0; i < list.size(); i++) {
            Object[] object = new Object[7];
            Object[] obj = (Object[]) list.get(i);
            object[0] = obj[1];
            object[1] = obj[2];
            object[2] = obj[3];
            object[3] = obj[4];
            listForExport.add(object);
        }
        HttpServletResponse response = ServletActionContext.getResponse();
        String fname = "DormitoryChargeList";
        OutputStream os;
        try {
            os = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + fname + ".xls");
            response.setContentType("application/msexcel");
            NeoPoiUtil.poiWriteExcel(chargeFeesRowValues, listForExport, os);
            return SUCCESS;
        } catch (IOException e) {
            log.error("DMS_error:无法取到OutputStream");
            log.error("DMS_error:错误原因" + e);
            return INPUT;
        }
    }

    public String getEnrollyear() {
        return enrollyear;
    }

    public void setEnrollyear(String enrollyear) {
        this.enrollyear = enrollyear;
    }

    public String getFeesyear() {
        return feesyear;
    }

    public void setFeesyear(String feesyear) {
        this.feesyear = feesyear;
    }

    public String getChargestatus() {
        return chargestatus;
    }

    public void setChargestatus(String chargestatus) {
        this.chargestatus = chargestatus;
    }

    public String getLodgechargetype() {
        return lodgechargetype;
    }

    public void setLodgechargetype(String lodgechargetype) {
        this.lodgechargetype = lodgechargetype;
    }
}
