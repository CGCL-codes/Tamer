package com.linkage.cashiersys.view.staff;

import org.apache.tapestry.IRequestCycle;
import com.linkage.appframework.data.DataMap;
import com.linkage.appframework.data.DatasetList;
import com.linkage.appframework.data.IData;
import com.linkage.appframework.data.IDataset;
import com.linkage.cashiersys.bean.params.ParamsBean;
import com.linkage.cashiersys.bean.staff.StaffBean;
import com.linkage.cashiersys.view.common.CashierBasePage;
import com.linkage.common.bean.util.DualMgr;
import com.linkage.component.PageData;

/**
 * @author chenzg
 *
 */
public abstract class StaffAdd extends CashierBasePage {

    public abstract void setInfo(IData info);

    public abstract void setInfos(IDataset infos);

    public abstract void setConditions(IData conditions);

    public abstract void setHotels(IDataset hotels);

    /**
	 * ����Ա����������ҵ�����Bean
	 */
    private StaffBean staffBean = new StaffBean();

    private ParamsBean paramsBean = new ParamsBean();

    /**
	 * ҳ���ʼ������
	 * @param cycle
	 * @throws Exception
	 * @author:chenzg
	 * @date:2010-5-12
	 */
    public void init(IRequestCycle cycle) throws Exception {
        PageData pd = getPageData();
        IData conditions = pd.getData("cond", true);
        IData data = new DataMap();
        data.put("ITEM_FLAG", "1");
        IDataset departs = paramsBean.queryDeparts(pd, data, null);
        IDataset areas = paramsBean.queryAreas(pd, data, null);
        conditions.put("DEPARTS", departs);
        conditions.put("AREAS", areas);
        this.setConditions(conditions);
    }

    /**
	 * ��ѯ�û���Ϣ
	 * @param cycle
	 * @throws Exception
	 * @author:chenzg
	 * @date:2010-5-12
	 */
    public void addStaff(IRequestCycle cycle) throws Exception {
        PageData pd = this.getPageData();
        String msg = "����Ա����Ϣ�ɹ�����ʼ������Ĭ��Ϊ:123456";
        IData params = pd.getData("cond", true);
        String staffId = params.getString("STAFF_ID");
        IData param = new DataMap();
        param.put("STAFF_ID", staffId);
        IDataset staffDs = this.staffBean.queryStaff(pd, param, null);
        if (staffDs != null && staffDs.size() > 0) {
            common.error("��Ա���š�" + staffId + "���Ѵ��ڣ����������룡");
        }
        params.put("STAFF_PWD", "123456");
        params.put("UPDATE_STAFF_ID", pd.getContext().getStaffId());
        params.put("UPDATE_TIME", DualMgr.getSysDate(pd));
        IDataset dataset = new DatasetList();
        dataset.add(params);
        this.staffBean.addStaffs(pd, dataset);
        StringBuilder strScript = new StringBuilder("");
        strScript.append("parent.document.getElementById('bquery').click();");
        redirectToMsgByScript(msg, strScript.toString());
    }
}
