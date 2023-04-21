package com.gjzq.virtual.stock.action;

import java.util.HashMap;
import java.util.Map;
import com.gjzq.virtual.stock.service.StockFareService;
import com.thinkive.base.jdbc.DBPage;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.util.RequestHelper;
import com.thinkive.plat.system.SysConfig;
import com.thinkive.plat.web.action.BaseAction;
import com.thinkive.plat.web.form.DynaForm;

/**
 * ����:   ģ�⳴�ɽ��׷��ʹ���
 * ��Ȩ:   Copyright (c) 2010
 * ��˾:   ˼�ϿƼ�
 * ����:   ŷ��
 * �汾:   1.0
 * ��������: 2010-09-15
 * ����ʱ��: 16:14:41
 */
public class StockFare extends BaseAction {

    private HashMap dataMap = new HashMap();

    private DynaForm form = new DynaForm();

    public DynaForm getForm() {
        return form;
    }

    public Map getData() {
        return dataMap;
    }

    private StockFareService fareService = new StockFareService();

    public String doDefault() {
        String secType = RequestHelper.getString(getRequest(), "secType");
        int curPage = RequestHelper.getInt(getRequest(), "page");
        curPage = (curPage <= 0) ? 1 : curPage;
        DBPage page = fareService.queryFare(curPage, SysConfig.getRowOfPage(), secType);
        if (page != null) {
            dataMap.put("page", page);
        }
        return LIST;
    }

    public String doAdd() {
        if (isPostBack()) {
            normalize(form);
            DataRow dataRow = new DataRow();
            dataRow.putAll(form);
            String secType = dataRow.getString("sec_type");
            String fareType = dataRow.getString("fare_type");
            DataRow temp = fareService.findFareByType(secType, fareType);
            if (temp != null) {
                addActionError("�������õĹ�Ʊ���ͽ��׷����Ѿ����á���ȷ�ϡ�");
                return MESSAGE;
            }
            fareService.addFare(dataRow);
            addActionMessage("��ӹ�Ʊ���׷��ʳɹ���");
            return MESSAGE;
        }
        return ADD;
    }

    public String doEdit() {
        if (isPostBack()) {
            normalize(form);
            DataRow dataRow = new DataRow();
            dataRow.putAll(form);
            String secType = dataRow.getString("sec_type");
            String fareType = dataRow.getString("fare_type");
            DataRow temp = fareService.findFareByType(secType, fareType);
            if (temp != null) {
                addActionError("�������õĹ�Ʊ���ͽ��׷����Ѿ����á���ȷ�ϡ�");
                return MESSAGE;
            }
            fareService.updateFare(dataRow);
            addActionMessage("��Ʊ���׷����޸ĳɹ���");
            return MESSAGE;
        }
        String num = RequestHelper.getString(getRequest(), "num");
        DataRow dataRow = fareService.findFareByNum(num);
        form.putAll(dataRow);
        return EDIT;
    }

    public String doDelete() {
        String num[] = RequestHelper.getStringArray(getRequest(), "num");
        for (int i = 0; i < num.length; i++) {
            fareService.deleteFare(num[i]);
        }
        addActionMessage("ɾ���Ʊ���׷��ʳɹ���");
        return MAIN;
    }
}
