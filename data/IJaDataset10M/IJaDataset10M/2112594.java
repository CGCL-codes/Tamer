package com.linkage.sys.view.params;

import org.apache.tapestry.IRequestCycle;
import com.linkage.appframework.data.IData;
import com.linkage.appframework.data.IDataset;
import com.linkage.component.PageData;
import com.linkage.sys.bean.params.ParamsBean;
import com.linkage.sys.view.common.CashierBasePage;

/**
 * @author chenzg
 *
 */
public abstract class DepartsList extends CashierBasePage {

    public abstract void setInfo(IData info);

    public abstract void setInfos(IDataset infos);

    public abstract void setConditions(IData conditions);

    /**
	 * ������Ŀ�����������ҵ�����Bean
	 */
    private ParamsBean paramsBean = new ParamsBean();

    /**
	 * ҳ���ʼ������
	 * @param cycle
	 * @throws Exception
	 * @author:chenzg
	 * @date:2010-5-12
	 */
    public void init(IRequestCycle cycle) throws Exception {
    }

    /**
	 * ��ѯ�û���Ϣ
	 * @param cycle
	 * @throws Exception
	 * @author:chenzg
	 * @date:2010-5-12
	 */
    public void queryDeparts(IRequestCycle cycle) throws Exception {
        PageData pd = this.getPageData();
        IData params = pd.getData("cond", true);
        IDataset items = paramsBean.queryDeparts(pd, params, pd.getPagination());
        this.setInfos(items);
        this.setConditions(params);
    }
}
