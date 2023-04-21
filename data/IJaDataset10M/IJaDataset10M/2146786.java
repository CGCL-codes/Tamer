package net.itsite.special;

import java.util.Map;
import net.itsite.datas.DataQueryUtils;
import net.itsite.datas.DataQueryUtils.DataModule;
import net.itsite.impl.AbstractCommonAjaxAction;
import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.content.IContentApplicationModule;
import net.simpleframework.core.id.LongID;
import net.simpleframework.organization.IJob;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;

public class SpecialPagerAction extends AbstractCommonAjaxAction {

    @Override
    public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
        if ("jobExecute".equals(beanProperty)) {
            return IJob.sj_account_normal;
        }
        return super.getBeanProperty(compParameter, beanProperty);
    }

    /**
	 * 添加专题
	 * @param compParameter
	 * @return
	 */
    public IForward addSpecialItem(final ComponentParameter compParameter) {
        return jsonForward(compParameter, new JsonCallback() {

            @Override
            public void doAction(Map<String, Object> json) throws Exception {
                final String dataModule = compParameter.getRequestParameter("dataModule");
                final String refId = compParameter.getRequestParameter("refId");
                final String catalogId = compParameter.getRequestParameter("catalogId");
                final DataModule dm = DataQueryUtils.getDataModuleTitle(dataModule, refId);
                if (dm.valid & catalogId != null) {
                    final SpecialCatalog catalog = SpecialUtils.applicationModule.getBean(SpecialCatalog.class, catalogId);
                    if (catalog == null) {
                        return;
                    }
                    SpecialItemBean itemBean = SpecialUtils.applicationModule.getBeanByExp(SpecialItemBean.class, "refId=? and specialId=?", new Object[] { refId, catalog.getSpecialId() });
                    if (itemBean == null) {
                        itemBean = new SpecialItemBean();
                        itemBean.setCatalogId(catalog.getId());
                        itemBean.setRefId(new LongID(refId));
                        itemBean.setSpecialId(catalog.getSpecialId());
                        itemBean.setDataModule(EFunctionModule.valueOf(dataModule));
                        itemBean.setTitle(dm.title);
                        itemBean.setUserId(dm.userId);
                        itemBean.setContent(dm.content);
                        itemBean.setCreateDate(dm.createDate);
                        SpecialUtils.applicationModule.doUpdate(itemBean, new TableEntityAdapter() {

                            @Override
                            public void afterInsert(ITableEntityManager manager, Object[] objects) {
                                final SpecialBean specialBean = SpecialUtils.applicationModule.getBean(SpecialBean.class, catalog.getSpecialId());
                                if (specialBean != null) {
                                    specialBean.setCounter(specialBean.getCounter() + 1);
                                    SpecialUtils.applicationModule.doUpdate(specialBean);
                                }
                                catalog.setCounter(catalog.getCounter() + 1);
                                SpecialUtils.applicationModule.doUpdate(catalog);
                            }
                        });
                    }
                    json.put("act", "已经加入专题");
                }
            }
        });
    }

    @Override
    public IContentApplicationModule getApplicationModule() {
        return SpecialUtils.applicationModule;
    }

    public IForward indexRebuild(final ComponentParameter compParameter) {
        SpecialUtils.applicationModule.createLuceneManager(compParameter).rebuildAll(true);
        return jsonForward(compParameter, new JsonCallback() {

            @Override
            public void doAction(final Map<String, Object> json) {
                json.put("info", LocaleI18n.getMessage("manager_tools.6"));
            }
        });
    }

    @Override
    protected void doStatRebuild() {
        SpecialUtils.doStatRebuild();
    }

    @Override
    protected String getAttentionParameter() {
        return SpecialUtils.specialId;
    }

    /**
	 */
    public IForward deleteSpecialItem(final ComponentParameter compParameter) throws Exception {
        return jsonForward(compParameter, new JsonCallback() {

            @Override
            public void doAction(final Map<String, Object> json) throws Exception {
                final SpecialItemBean itemBean = SpecialUtils.applicationModule.getBean(SpecialItemBean.class, compParameter.getRequestParameter("specialItemId"));
                if (itemBean != null) {
                    SpecialUtils.applicationModule.doDelete(itemBean, new TableEntityAdapter() {

                        @Override
                        public void afterDelete(ITableEntityManager manager, IDataObjectValue dataObjectValue) {
                            final SpecialBean specialBean = SpecialUtils.applicationModule.getBean(SpecialBean.class, itemBean.getSpecialId());
                            if (specialBean != null) {
                                specialBean.setCounter(specialBean.getCounter() - 1);
                                SpecialUtils.applicationModule.doUpdate(specialBean);
                            }
                            final SpecialCatalog catalog = SpecialUtils.applicationModule.getBean(SpecialCatalog.class, itemBean.getCatalogId());
                            if (catalog != null) {
                                catalog.setCounter(catalog.getCounter() - 1);
                                SpecialUtils.applicationModule.doUpdate(catalog);
                            }
                        }
                    });
                }
            }
        });
    }
}
