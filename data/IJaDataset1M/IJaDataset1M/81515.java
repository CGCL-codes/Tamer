package com.thinkive.wap.action;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.thinkive.base.jdbc.DBPage;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.util.RequestHelper;
import com.thinkive.base.util.ScriptHelper;
import com.thinkive.plat.web.action.BaseAction;
import com.thinkive.plat.web.form.DynaForm;
import com.thinkive.wap.service.ProdTypeService;

/**
 * ����: ԤԼ��Ϣ
 * ��Ȩ: Copyright (c) 2011
 * ��˾: ˼�ϿƼ� 
 * ����: ��ѧ��
 * �汾: 1.0 
 * ��������: 2011-12-05 
 * ����ʱ��: ����11:09:16
 */
public class ProdTypeAction extends BaseAction {

    private static Logger logger = Logger.getLogger(ProdTypeAction.class);

    private HashMap dataMap = new HashMap();

    private DynaForm form = new DynaForm();

    public DynaForm getForm() {
        return form;
    }

    public Map getData() {
        return dataMap;
    }

    /**
	 * ȱʡ�Ĳ���(function=""ʱ����)
	 * �г����еĲ�Ʒ������Ϣ
	 *
	 * @return
	 */
    public String doDefault() {
        int curPage = this.getIntParameter("page");
        String key = getStrParameter("key");
        String begin_time = getStrParameter("begin_time");
        String end_time = getStrParameter("end_time");
        String siteno = getSiteNo();
        curPage = (curPage <= 0) ? 1 : curPage;
        ProdTypeService service = new ProdTypeService();
        DBPage page = service.findTypePage(curPage, 20, key, begin_time, end_time);
        dataMap.put("page", page);
        return DEFAULT;
    }

    /**
	 * ��������Ӳ�Ʒ������Ϣ
	 * @return
	 */
    public String doAdd() {
        if (isPostBack()) {
            normalize(form);
            ProdTypeService service = new ProdTypeService();
            DataRow data = new DataRow();
            data.putAll(form);
            int num = service.findConunt(data.getString("type_id"), data.getString("type_name"));
            if (num > 0) {
                this.addActionError("��Ʒ���Ѿ�������ͬ�����Ͳ�Ʒ��");
            } else {
                data.set("orderline", data.getString("type_id"));
                service.add(data);
                addLog("���WAP��Ʒ������Ϣ", "���WAP��Ʒ������Ϣ");
            }
            return MESSAGE;
        } else {
            return ADD;
        }
    }

    /**
	 * �༭����
	 * @return
	 */
    public String doEdit() {
        ProdTypeService service = new ProdTypeService();
        if (isPostBack()) {
            normalize(form);
            DataRow data = new DataRow();
            data.putAll(form);
            service.update(data);
            addLog("�༭WAP��Ʒ��Ϣ", "�༭WAP��Ʒ��Ϣ");
            return MESSAGE;
        } else {
            String id = getStrParameter("type_id");
            DataRow data = service.findData(id);
            if (data != null) form.putAll(data);
            return EDIT;
        }
    }

    /**
	 * ɾ������
	 * @return
	 */
    public String doDelete() {
        int[] idArray = getIntArrayParameter("id");
        ProdTypeService service = new ProdTypeService();
        for (int i = 0; i < idArray.length; i++) {
            int num = service.findProdConunt(idArray[i]);
            if (num > 0) {
                return MAIN;
            } else {
                service.delete(idArray[i]);
                addLog("ɾ��WAP��Ʒ��Ϣ", "ɾ��WAP��Ʒ��Ϣ");
            }
        }
        return MAIN;
    }
}
