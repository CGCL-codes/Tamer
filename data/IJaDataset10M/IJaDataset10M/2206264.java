package com.linkage.sys.view.group;

import org.apache.tapestry.IRequestCycle;
import com.linkage.appframework.data.DataMap;
import com.linkage.appframework.data.IData;
import com.linkage.appframework.data.IDataset;
import com.linkage.component.PageData;
import com.linkage.sys.bean.group.GroupBean;
import com.linkage.sys.bean.product.ProductBean;
import com.linkage.sys.view.common.CashierBasePage;

public abstract class GroupList extends CashierBasePage {

    public abstract void setInfo(IData info);

    public abstract void setInfos(IDataset infos);

    public abstract void setConditions(IData conditions);

    /**
	 * ҳ���ʼ������
	 * @param cycle
	 * @throws Exception
	 * @author:wull
	 */
    public void init(IRequestCycle cycle) throws Exception {
        PageData pd = getPageData();
        IData conditions = pd.getData("cond", true);
    }

    /**
	 * �����Ʒ��������ҵ�����Bean
	 */
    private GroupBean groupBean = new GroupBean();

    /**
	 * ��ѯ���й�˾/����
	 * @param cycle
	 * @throws Exception
	 */
    public void queryGroupList(IRequestCycle cycle) throws Exception {
        PageData pd = this.getPageData();
        IData params = pd.getData("cond", true);
        IDataset GroupList = groupBean.queryGroupLists(pd, params, pd.getPagination());
        this.setInfos(GroupList);
    }
}
