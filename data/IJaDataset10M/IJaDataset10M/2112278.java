package com.kwoksys.action.portal;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.action.common.template.*;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.portal.PortalService;
import com.kwoksys.biz.portal.PortalUtils;
import com.kwoksys.biz.portal.dao.PortalQueries;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.biz.system.dto.Link;
import com.kwoksys.framework.system.AppPaths;
import com.kwoksys.framework.connection.database.QueryBits;
import com.kwoksys.framework.helper.Counter;
import com.kwoksys.framework.helper.RowClassUiHelper;
import com.kwoksys.framework.system.Access;
import com.kwoksys.framework.system.Localizer;
import com.kwoksys.framework.system.RequestContext;
import com.kwoksys.framework.util.HtmlUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for displaying contract list.
 */
public class SiteCategoryListAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContext requestContext = new RequestContext(request);
        AccessUser user = requestContext.getUser();
        Counter counter = new Counter();
        List<String> columnHeaders = PortalUtils.getCategoryColumnHeaderList();
        QueryBits query = new QueryBits();
        if (PortalUtils.isSortableCategoryColumn(Category.CATEGORY_NAME)) {
            query.setOrderByColumn(PortalQueries.getOrderByColumn(Category.CATEGORY_NAME));
        }
        PortalService portalService = ServiceProvider.getPortalService();
        List<Category> categories = portalService.getCategories(query);
        List dataList = new ArrayList();
        if (!categories.isEmpty()) {
            boolean canEditCategory = Access.hasPermission(user, AppPaths.PORTAL_CATEGORY_EDIT);
            RowClassUiHelper ui = new RowClassUiHelper();
            for (Category category : categories) {
                List columns = new ArrayList();
                for (String column : columnHeaders) {
                    if (column.equals(Category.CATEGORY_NAME)) {
                        columns.add(HtmlUtils.encode(category.getName()));
                    } else if (column.equals(Category.CATEGORY_DESCRIPTION)) {
                        columns.add(HtmlUtils.formatMultiLineDisplay(category.getDescription()));
                    } else if (column.equals(Category.CATEGORY_ACTIONS)) {
                        if (canEditCategory) {
                            columns.add("<a href=\"" + AppPaths.ROOT + AppPaths.PORTAL_CATEGORY_EDIT + "?categoryId=" + category.getId() + "\">" + Localizer.getText(requestContext, "portal.cmd.categoryEdit") + "</a>");
                        } else {
                            columns.add("");
                        }
                    } else if (column.equals(BaseObject.ROWNUM)) {
                        columns.add(counter.incrCounter() + ".");
                    }
                }
                Map map = new HashMap();
                map.put("rowClass", ui.getRowClass());
                map.put("columns", columns);
                dataList.add(map);
            }
        }
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setTemplatePath(mapping, SUCCESS);
        TableTemplate tableTemplate = new TableTemplate();
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setColumnPath(AppPaths.ROOT + AppPaths.PORTAL_CATEGORY_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("portal.siteCategoryList.header");
        header.setTitleClass("noLine");
        if (Access.hasPermission(user, AppPaths.PORTAL_CATEGORY_ADD)) {
            Link link = new Link();
            link.setPath(AppPaths.ROOT + AppPaths.PORTAL_CATEGORY_ADD);
            link.setTitleKey("portal.siteCategoryAdd.header");
            header.addHeaderCmds(link);
        }
        return standardTemplate.findForward(STANDARD_TEMPLATE);
    }
}
