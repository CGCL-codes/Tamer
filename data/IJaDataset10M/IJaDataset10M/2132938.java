package com.thinkive.business.stock.action;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.thinkive.base.jdbc.DBPage;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.util.DateHelper;
import com.thinkive.base.util.StringHelper;
import com.thinkive.business.stock.service.BranchCatalogCustomService;
import com.thinkive.business.stock.service.BranchService;
import com.thinkive.plat.web.action.BaseAction;
import com.thinkive.plat.web.form.DynaForm;

/**
 * ����:  
 * ��Ȩ:	 Copyright (c) 2009
 * ��˾:	 ˼�ϿƼ�
 * ����:	 ���
 * �汾:	 1.0
 * ��������: 2011-5-27
 * ����ʱ��: ����09:33:42
 */
public class BranchCatalogCustomAction extends BaseAction {

    private static Logger logger = Logger.getLogger(BranchCatalogCustomAction.class);

    private HashMap dataMap = new HashMap();

    private DynaForm form = new DynaForm();

    public DynaForm getForm() {
        return form;
    }

    public Map getData() {
        return dataMap;
    }

    /**
	 * 
	 * @������
	 * @���ߣ����
	 * @ʱ�䣺2011-5-27 ����10:35:46
	 * @return
	 */
    public String doDefault() {
        String siteNo = getSiteNo();
        int curPage = this.getIntParameter("page");
        curPage = (curPage <= 0) ? 1 : curPage;
        String branchNo = "";
        if (isSystemAdmin() || isAdministratorsRole()) {
            branchNo = getStrParameter("branchNo");
            BranchService branchService = new BranchService();
            List branchs = branchService.getAll();
            dataMap.put("branchs", branchs);
            form.put("isSystemAdmin", "1");
        } else {
            branchNo = getBranchNo();
        }
        BranchCatalogCustomService customService = new BranchCatalogCustomService();
        DBPage page = customService.findPage(curPage, 20, branchNo, siteNo);
        dataMap.put("page", page);
        return LIST;
    }

    /**
	 * 
	 * @������
	 * @���ߣ����
	 * @ʱ�䣺2011-5-27 ����10:35:39
	 * @return
	 */
    public String doAdd() {
        if (isPostBack()) {
            BranchCatalogCustomService customService = new BranchCatalogCustomService();
            normalize(form);
            DataRow data = new DataRow();
            data.putAll(form);
            data.set("siteno", getSiteNo());
            data.set("create_by", getUID());
            data.set("create_date", DateHelper.formatDate(new Date()));
            data.set("modify_by", getUID());
            data.set("modify_date", DateHelper.formatDate(new Date()));
            customService.addCustomCatalog(data);
            return MESSAGE;
        } else {
            if (isSystemAdmin() || isAdministratorsRole()) {
                BranchService branchService = new BranchService();
                List branchs = branchService.getAll();
                dataMap.put("branchs", branchs);
                form.put("isSystemAdmin", "1");
            } else {
                form.put("userBranchNo", getBranchNo());
            }
            return ADD;
        }
    }

    /**
	 * 
	 * @������
	 * @���ߣ����
	 * @ʱ�䣺2011-5-27 ����10:35:35
	 * @return
	 */
    public String doEdit() {
        BranchCatalogCustomService customService = new BranchCatalogCustomService();
        if (isPostBack()) {
            normalize(form);
            DataRow data = new DataRow();
            data.putAll(form);
            data.set("modify_by", getUID());
            data.set("modify_date", DateHelper.formatDate(new Date()));
            customService.editCustomCatalog(data);
            return MESSAGE;
        } else {
            int id = getIntParameter("id");
            DataRow data = customService.findDataById(id);
            form.putAll(data);
            if (isSystemAdmin() || isAdministratorsRole()) {
                BranchService branchService = new BranchService();
                List branchs = branchService.getAll();
                dataMap.put("branchs", branchs);
                form.put("isSystemAdmin", "1");
            } else {
                form.put("userBranchNo", getBranchNo());
            }
            return EDIT;
        }
    }

    /**
	 * 
	 * @������
	 * @���ߣ����
	 * @ʱ�䣺2011-5-27 ����10:35:52
	 * @return
	 */
    public String doDelete() {
        BranchCatalogCustomService customService = new BranchCatalogCustomService();
        int[] idArray = getIntArrayParameter("id");
        for (int i = 0; i < idArray.length; i++) {
            customService.delete(idArray[i]);
        }
        return MAIN;
    }

    /**
	 * ����:���uid��ò��ű��
	 * @return
	 */
    private String findBranchNo() {
        String branchNo = getBranchNo();
        if (isSystemAdmin() || isAdministratorsRole() || "0000".equals(branchNo)) branchNo = "";
        return branchNo;
    }
}
