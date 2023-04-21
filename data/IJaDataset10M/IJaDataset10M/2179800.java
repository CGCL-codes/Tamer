package com.kwoksys.action.contacts;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.action.common.template.*;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.contacts.CompanyUtils;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.CompanyTabs;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.FileUtils;
import com.kwoksys.biz.files.dao.FileQueries;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.system.dto.Link;
import com.kwoksys.framework.system.AppPaths;
import com.kwoksys.framework.configs.ConfigManager;
import com.kwoksys.framework.connection.database.QueryBits;
import com.kwoksys.framework.helper.Counter;
import com.kwoksys.framework.helper.RowClassUiHelper;
import com.kwoksys.framework.system.Access;
import com.kwoksys.framework.system.Localizer;
import com.kwoksys.framework.system.RequestContext;
import com.kwoksys.framework.util.HttpUtils;
import com.kwoksys.framework.util.WidgetUtils;
import com.kwoksys.framework.session.CookieManager;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Action class for display Company file attachments.
 */
public class CompanyFileAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContext requestContext = new RequestContext(request);
        AccessUser user = requestContext.getUser();
        Integer companyId = HttpUtils.getParameter(request, "companyId");
        String orderBy = HttpUtils.getOrSetCookie(request, response, "orderBy", CookieManager.FILES_ORDER_BY, File.NAME);
        String order = HttpUtils.getOrSetCookie(request, response, "order", CookieManager.FILES_SORT_ORDER, QueryBits.ASCENDING);
        ContactService contactService = ServiceProvider.getContactService();
        Company company = contactService.getCompany(companyId);
        boolean canDeleteFile = Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_FILE_DELETE);
        String fileDeletePath = AppPaths.ROOT + AppPaths.CONTACTS_COMPANY_FILE_DELETE + "?companyId=" + companyId + "&fileId=";
        String fileDeleteText = Localizer.getText(requestContext, "form.button.delete");
        boolean canDownloadFile = Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_FILE_DOWNLOAD);
        String fileDownloadPath = AppPaths.ROOT + AppPaths.CONTACTS_COMPANY_FILE_DOWNLOAD + "?companyId=" + companyId + "&fileId=";
        QueryBits query = new QueryBits();
        if (FileUtils.isSortableColumn(orderBy)) {
            query.setOrderByColumn(FileQueries.getOrderByColumn(orderBy));
            query.setSortOrder(order);
        }
        List<File> files = contactService.getCompanyFiles(query, company.getId());
        List dataList = new ArrayList();
        if (!files.isEmpty()) {
            RowClassUiHelper ui = new RowClassUiHelper();
            Counter counter = new Counter();
            for (File file : files) {
                List columns = new ArrayList();
                columns.add(counter.incrCounter() + ".");
                columns.add(WidgetUtils.getFileIconLink(canDownloadFile, file.getLogicalName(), fileDownloadPath + file.getId()));
                columns.add(file.getTitle());
                columns.add(file.getCreationDate());
                columns.add(FileUtils.formatFileSize(requestContext, file.getSize()));
                if (canDeleteFile) {
                    columns.add("<a href=\"" + fileDeletePath + file.getId() + "\">" + fileDeleteText + "</a>");
                }
                Map map = new HashMap();
                map.put("rowClass", ui.getRowClass());
                map.put("columns", columns);
                dataList.add(map);
            }
        }
        List columnHeaders = new ArrayList(Arrays.asList(File.ROWNUM, File.NAME, File.LABEL, File.CREATION_DATE, File.FILE_SIZE));
        if (canDeleteFile) {
            columnHeaders.add("command");
        }
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setTemplatePath(mapping, SUCCESS);
        CompanySpecTemplate tmpl = new CompanySpecTemplate(company);
        standardTemplate.addTemplate(tmpl);
        TableTemplate tableHeader = new TableTemplate();
        standardTemplate.addTemplate(tableHeader);
        tableHeader.setColumnPath(AppPaths.ROOT + AppPaths.CONTACTS_COMPANY_FILES + "?companyId=" + companyId);
        tableHeader.setColumnHeaders(columnHeaders);
        tableHeader.setColumnTextKey("files.colName.");
        tableHeader.setSortableColumnHeaders(FileUtils.getSortableColumns());
        tableHeader.setOrderBy(orderBy);
        tableHeader.setOrder(order);
        tableHeader.setDataList(dataList);
        tableHeader.setEmptyRowMsgKey("files.noAttachments");
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new Object[] { company.getName() });
        FileService fileService = ServiceProvider.getFileService();
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_FILE_ADD)) {
            Link link = new Link();
            link.setTitleKey("files.fileAttach");
            if (fileService.isDirectoryExist(ConfigManager.file.getCompanyFileRepositoryLocation())) {
                link.setPath(AppPaths.ROOT + AppPaths.CONTACTS_COMPANY_FILE_ADD + "?companyId=" + companyId);
                link.setImgSrc(AppPaths.ROOT + AppPaths.FILE_ADD_ICON);
            } else {
                link.setImgAlt("files.warning.invalidPath");
                link.setImgSrc(AppPaths.ROOT + AppPaths.WARNING_ICON);
            }
            header.addHeaderCmds(link);
        }
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_LIST)) {
            Link link = new Link();
            link.setPath(AppPaths.ROOT + AppPaths.CONTACTS_COMPANY_LIST);
            link.setTitleKey("contactMgmt.cmd.companyList");
            header.addHeaderCmds(link);
        }
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(CompanyUtils.companyTabList(requestContext, company));
        tabs.setTabActive(CompanyTabs.FILES_TAB);
        return standardTemplate.findForward(STANDARD_TEMPLATE);
    }
}
