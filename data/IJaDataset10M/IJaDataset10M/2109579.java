package com.thinkive.business.other.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.gjzq.WebConstants;
import com.gjzq.service.ZsbxService;
import com.thinkive.base.jdbc.DBPage;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.plat.Constants;
import com.thinkive.plat.web.action.BaseAction;
import com.thinkive.plat.web.form.DynaForm;

/**
 * ����:  ָ����ֺ�̨����
 * ��Ȩ:	 Copyright (c) 2009
 * ��˾:	 ˼�ϿƼ�
 * ����:	 ����
 * �汾:	 1.0
 * ��������: 2011-4-14
 * ����ʱ��: ����04:01:16
 */
public class ZsbxAction extends BaseAction {

    private DynaForm form = new DynaForm();

    private HashMap dataMap = new HashMap();

    public DynaForm getForm() {
        return form;
    }

    public Map getData() {
        return dataMap;
    }

    public String doDefault() {
        normalize(form);
        ZsbxService zsbxService = new ZsbxService();
        int curPage = this.getIntParameter("page");
        curPage = (curPage <= 0) ? 1 : curPage;
        DataRow dr = new DataRow();
        dr.putAll(form);
        DBPage page = zsbxService.findZsbx(dr, curPage, Constants.MAIN_ROW_OF_PAGE);
        dataMap.put("page", page);
        return LIST;
    }
}
