package net.itsite.special;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.a.ItSiteUtil;
import net.itsite.impl.AItSiteAppclicationModule;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ExecutorRunnable;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ITaskExecutorAware;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.component.login.LoginUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ui.tabs.EMatchMethod;
import net.simpleframework.web.page.component.ui.tabs.TabHref;
import net.simpleframework.web.page.component.ui.tabs.TabsUtils;

public class SpecialApplicationModule extends AItSiteAppclicationModule implements ISpecialApplicationModule {

    private String deployName = "special";

    @Override
    public void init(IInitializer initializer) {
        try {
            super.init(initializer);
            doInit(SpecialUtils.class, deployName);
            ((ITaskExecutorAware) getApplication()).getTaskExecutor().addScheduledTask(60 * 10, DateUtils.DAY_PERIOD, new ExecutorRunnable() {

                @Override
                public void task() {
                    SpecialUtils.doStatRebuild();
                }
            });
        } catch (Exception e) {
        }
    }

    static final Table special = new Table("it_special");

    static final Table special_item = new Table("it_special_item");

    static final Table special_catalog = new Table("it_special_catalog");

    static final Table special_remark = new Table("it_special_remark");

    @Override
    protected void putTables(Map<Class<?>, Table> tables) {
        super.putTables(tables);
        tables.put(SpecialBean.class, special);
        tables.put(SpecialCatalog.class, special_catalog);
        tables.put(SpecialItemBean.class, special_item);
        tables.put(SpecialRemark.class, special_remark);
    }

    @Override
    public AbstractComponentBean getComponentBean(PageRequestResponse requestResponse) {
        return null;
    }

    @Override
    public String getViewUrl(Object id) {
        final StringBuilder sb = new StringBuilder();
        sb.append("/special").append("/v/").append(id);
        sb.append(".html");
        return sb.toString();
    }

    @Override
    public String tabs(PageRequestResponse requestResponse) {
        final List<TabHref> tabHrefs = new ArrayList<TabHref>();
        final String applicationUrl = getApplicationUrl(requestResponse);
        TabHref href = new TabHref("全部", WebUtils.addParameters(applicationUrl, "t=all"));
        href.setMatchMethod(EMatchMethod.startsWith);
        tabHrefs.add(href);
        final IAccount login = AccountSession.getLogin(requestResponse.getSession());
        href = new TabHref("关注", login == null ? LoginUtils.getLocationPath() : WebUtils.addParameters(applicationUrl, "t=attention"));
        href.setMatchMethod(EMatchMethod.startsWith);
        tabHrefs.add(href);
        href = new TabHref("我的代码", login == null ? LoginUtils.getLocationPath() : WebUtils.addParameters(applicationUrl, "t=my"));
        href.setMatchMethod(EMatchMethod.startsWith);
        tabHrefs.add(href);
        return TabsUtils.tabs(requestResponse, tabHrefs.toArray(new TabHref[tabHrefs.size()]));
    }

    @Override
    protected String getParameters(PageRequestResponse requestResponse) {
        final StringBuffer param = new StringBuffer();
        final String t = requestResponse.getRequestParameter("t");
        if (StringUtils.hasText(t)) {
            param.append("t=").append(t);
        } else {
            param.append("t=all");
        }
        final String projectId = requestResponse.getRequestParameter("projectId");
        if (StringUtils.hasText(projectId)) {
            param.append("&projectId=").append(projectId);
        }
        return param.toString();
    }

    @Override
    public String tabs2(PageRequestResponse requestResponse) {
        return null;
    }

    @Override
    public String getApplicationUrl(PageRequestResponse requestResponse) {
        return "/special.html";
    }

    @Override
    public String getDeployPath() {
        return null;
    }

    @Override
    public Class<?> getEntityBeanClass() {
        return SpecialBean.class;
    }

    public SpecialBean getViewSpecialBean(PageRequestResponse requestResponse) {
        final SpecialBean specialBean = getBean(SpecialBean.class, requestResponse.getRequestParameter(SpecialUtils.specialId));
        try {
            final ITableEntityManager tMgr = getDataObjectManager(SpecialBean.class);
            synchronized (tMgr) {
                final String attributeName = "views_" + specialBean.getId();
                final boolean views = ConvertUtils.toBoolean(requestResponse.getSessionAttribute(attributeName), false);
                if (!views) {
                    specialBean.setViews(specialBean.getViews() + 1);
                    tMgr.update(new String[] { "views" }, specialBean);
                    requestResponse.setSessionAttribute(attributeName, Boolean.TRUE);
                }
            }
        } catch (final Exception e) {
        }
        return specialBean;
    }

    @Override
    public EFunctionModule getEFunctionModule() {
        return EFunctionModule.special;
    }

    @Override
    public IQueryEntitySet<SpecialCatalog> querySpecialCatalog(Object catalogId, final Object specialId) {
        final ITableEntityManager catalog_mgr = getDataObjectManager(SpecialCatalog.class);
        final StringBuilder sql = new StringBuilder();
        final ArrayList<Object> al = new ArrayList<Object>();
        if (catalogId == null) {
            sql.append(Table.nullExpr(catalog_mgr.getTable(), "parentid"));
        } else {
            sql.append("parentid=?");
            al.add(catalogId);
        }
        sql.append(" and specialId=?");
        al.add(specialId);
        sql.append(" order by oorder desc");
        return catalog_mgr.query(new ExpressionValue(sql.toString(), al.toArray()), SpecialCatalog.class);
    }

    @Override
    public String getActionHTML(PageRequestResponse requestResponse, SpecialBean specialBean) {
        final StringBuffer buf = new StringBuffer();
        final String text = "关注";
        if (ItSiteUtil.isLogin(requestResponse)) {
            buf.append("<a class='a2' param='specialId=" + specialBean.getId() + "'  id='__doAttention'>");
            buf.append(ItSiteUtil.isAttention(requestResponse, getEFunctionModule(), specialBean.getId(), "取消" + text, text));
            buf.append("</a>(<a class='a2' id='__attentionWindow' param='vtype=").append(getEFunctionModule().name()).append("&attentionId=");
            buf.append(specialBean.getId()).append("'>").append(specialBean.getAttentions()).append("</a>)");
        }
        return buf.toString();
    }

    @Override
    public IQueryEntitySet<SpecialBean> querySpecialBean(Object catalogId) {
        final ITableEntityManager catalog_mgr = getDataObjectManager(SpecialBean.class);
        final StringBuilder sql = new StringBuilder();
        final ArrayList<Object> al = new ArrayList<Object>();
        if (catalogId == null) {
            sql.append(Table.nullExpr(catalog_mgr.getTable(), "parentid"));
        } else {
            sql.append("parentid=?");
            al.add(catalogId);
        }
        sql.append(" order by oorder desc");
        return catalog_mgr.query(new ExpressionValue(sql.toString(), al.toArray()), SpecialBean.class);
    }

    @Override
    protected String getTypeName() {
        return "专题";
    }
}
