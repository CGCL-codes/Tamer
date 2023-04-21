package net.itsite.pt;

import net.itsite.i.IItSiteApplicationModule;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;

public interface IPTApplicationModule extends IItSiteApplicationModule {

    /**
	 * 获得相应的分类目录
	 */
    IQueryEntitySet<PTCatalog> queryCatalogs(final PageRequestResponse requestResponse, ITreeBeanAware parent);

    /**
	 * 显示开源软件时调用的方法，增加阅读量
	 * 
	 * @param requestResponse
	 * @return
	 */
    PTBean getViewPtBean(PageRequestResponse requestResponse);

    /**
	 * 获得竞标人
	 * 
	 * @param requestResponse
	 * @return
	 */
    PTUserBidBean getPtUserBidBean(final Object userId, final Object projectId);

    PTUserBean getPtUserBean(final Object userId);

    /**
	 * 根据分类Id获取该类型下的所有开源软件
	 */
    IQueryEntitySet<PTBean> queryPtBeans(final Object catalogId);

    /**
	 * 查询竞标的用户
	 * @return
	 */
    IQueryEntitySet<PTUserBidBean> queryPtUserBidBeans(final Object projectId);

    void doAttentionSent(final ComponentParameter compParameter, final PTBean osBean);
}
