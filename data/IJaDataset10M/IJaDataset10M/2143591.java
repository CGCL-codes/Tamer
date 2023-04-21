package net.itsite.special;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.itsite.impl.AbstractCatalog;
import net.itsite.utils.StringsUtils;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

public class SpecialItemPaper extends AbstractPagerHandle {

    @Override
    public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
        if ("title".equals(beanProperty)) {
            final StringBuffer buf = new StringBuffer(32);
            final String specialId = compParameter.getRequestParameter("specialId");
            final String catalogId = compParameter.getRequestParameter("catalogId");
            if (StringsUtils.isNotBlank(specialId)) {
                SpecialBean specialBean = SpecialUtils.applicationModule.getBean(SpecialBean.class, specialId);
                buf.append("<a href=\"/special.html\">专题</a>");
                if (specialBean != null) {
                    buf.append(HTMLBuilder.NAV);
                    buf.append("<a href='/special/v/").append(specialBean.getId()).append(".html'>");
                    buf.append(specialBean.getText()).append("</a>");
                    final SpecialCatalog catalog = SpecialUtils.applicationModule.getBean(SpecialCatalog.class, catalogId);
                    if (catalog != null) {
                        buf.append(HTMLBuilder.NAV);
                        buf.append("<a onclick=\"refreshSpecialPaper('").append(catalog.getId()).append("');\">");
                        buf.append(catalog.getText()).append("</a>");
                    }
                    final String dataModule = compParameter.getRequestParameter("dataModule");
                    if (StringUtils.hasText(dataModule)) {
                        try {
                            final EFunctionModule functionModule = EFunctionModule.valueOf(dataModule);
                            buf.append(HTMLBuilder.NAV);
                            buf.append("<span style=\"color:#B13D0A;font-weight: bold;\">");
                            buf.append(functionModule.toString());
                            buf.append("</span>");
                        } catch (Exception e) {
                        }
                    }
                }
                return buf.toString();
            }
        }
        return super.getBeanProperty(compParameter, beanProperty);
    }

    @Override
    public IDataObjectQuery<?> createDataObjectQuery(ComponentParameter compParameter) {
        final ITableEntityManager tMgr = SpecialUtils.applicationModule.getDataObjectManager();
        final List<Object> ol = new ArrayList<Object>();
        final StringBuffer sql = new StringBuffer();
        final int catalogId = ConvertUtils.toInt(compParameter.getRequestParameter("catalogId"), 0);
        final String dataModule = compParameter.getRequestParameter("dataModule");
        sql.append("select si.* from ");
        sql.append(SpecialApplicationModule.special_item.getName()).append(" si ");
        sql.append("where specialId=?");
        ol.add(compParameter.getRequestParameter("specialId"));
        if (catalogId != 0) {
            sql.append(" and catalogId in(").append(AbstractCatalog.Utils.getJoinCatalog(catalogId, SpecialUtils.applicationModule, SpecialCatalog.class)).append(")");
        }
        if (StringUtils.hasText(dataModule)) {
            ol.add(EFunctionModule.valueOf(dataModule));
            sql.append(" and dataModule=?");
        }
        sql.append(" order by createDate desc");
        tMgr.reset();
        return tMgr.query(new SQLValue(sql.toString(), ol.toArray(new Object[] {})), SpecialItemBean.class);
    }

    @Override
    public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
        final Map<String, Object> parameters = super.getFormParameters(compParameter);
        putParameter(compParameter, parameters, "catalogId");
        putParameter(compParameter, parameters, "specialId");
        putParameter(compParameter, parameters, "dataModule");
        return parameters;
    }
}
