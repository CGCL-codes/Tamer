package net.itsite.document.docu.my;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.a.ItSiteUtil;
import net.itsite.document.docu.DocuDelLog;
import net.itsite.document.docu.DocuUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.IGetAccountAware;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.TablePagerColumn;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;

public class MyDelLogTablePaperHandle extends AbstractDbTablePagerHandle {

    @Override
    public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
        if ("jobExecute".equals(beanProperty)) {
            return IJob.sj_account_normal;
        }
        return super.getBeanProperty(compParameter, beanProperty);
    }

    @Override
    public IDataObjectQuery<?> createDataObjectQuery(ComponentParameter compParameter) {
        final ITableEntityManager tMgr = DocuUtils.applicationModule.getDataObjectManager(DocuDelLog.class);
        final List<Object> ol = new ArrayList<Object>();
        final StringBuffer sql = new StringBuffer();
        sql.append(" userId=?");
        final IGetAccountAware accountAware = MySpaceUtils.getAccountAware();
        if (accountAware.isMyAccount(compParameter)) {
            ol.add(ItSiteUtil.getLoginUser(compParameter).getId());
        } else {
            ol.add(compParameter.getRequestParameter(OrgUtils.um().getUserIdParameterName()));
        }
        sql.append(" order by delDate desc");
        return tMgr.query(new ExpressionValue(sql.toString(), ol.toArray(new Object[] {})), DocuDelLog.class);
    }

    @Override
    public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
        return new AbstractTablePagerData(compParameter) {

            @Override
            public Map<String, TablePagerColumn> getTablePagerColumns() {
                final Map<String, TablePagerColumn> column = new LinkedHashMap<String, TablePagerColumn>(super.getTablePagerColumns());
                final IGetAccountAware accountAware = MySpaceUtils.getAccountAware();
                if (!accountAware.isMyAccount(compParameter)) {
                    column.remove("action");
                }
                return column;
            }

            @Override
            protected Map<Object, Object> getRowData(final Object arg0) {
                final DocuDelLog delLog = (DocuDelLog) arg0;
                final Map<Object, Object> rowData = new LinkedHashMap<Object, Object>();
                rowData.put("title", "<a onclick=\"$Actions['showDelLogWindowAct']('delLogId=" + delLog.getId() + "');\">" + delLog.getTitle() + "</a>");
                rowData.put("userId", delLog.getUserText());
                rowData.put("status", delLog.isStatus() ? "用户操作" : "管理员操作");
                rowData.put("delDate", ConvertUtils.toDateString(delLog.getDelDate(), "yyyy-MM-dd HH"));
                rowData.put("action", "<a class='dellogB down_menu_image'></a>");
                return rowData;
            }
        };
    }

    @Override
    public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
        final Map<String, Object> parameters = super.getFormParameters(compParameter);
        putParameter(compParameter, parameters, OrgUtils.um().getUserIdParameterName());
        return parameters;
    }
}
