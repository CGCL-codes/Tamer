package com.kwoksys.action.software;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.action.common.template.*;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.FileUtils;
import com.kwoksys.biz.files.dao.FileQueries;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dto.Software;
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
 * Action class for displaying software file.
 */
public class SoftwareFileAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContext requestContext = new RequestContext(request);
        AccessUser accessUser = requestContext.getUser();
        Integer softwareId = HttpUtils.getParameter(request, "softwareId");
        String orderBy = HttpUtils.getOrSetCookie(request, response, "orderBy", CookieManager.FILES_ORDER_BY, File.NAME);
        String order = HttpUtils.getOrSetCookie(request, response, "order", CookieManager.FILES_SORT_ORDER, QueryBits.ASCENDING);
        SoftwareService softwareService = ServiceProvider.getSoftwareService();
        Software software = softwareService.getSoftware(softwareId);
        String deleteFileText = Localizer.getText(requestContext, "form.button.delete");
        String fileDownloadPath = AppPaths.ROOT + AppPaths.SOFTWARE_FILE_DOWNLOAD + "?softwareId=" + softwareId + "&fileId=";
        String fileDeletePath = AppPaths.ROOT + AppPaths.SOFTWARE_FILE_DELETE + "?softwareId=" + softwareId + "&fileId=";
        boolean canDeleteFile = Access.hasPermission(accessUser, AppPaths.SOFTWARE_FILE_DELETE);
        boolean canDownloadFile = Access.hasPermission(accessUser, AppPaths.SOFTWARE_FILE_DOWNLOAD);
        QueryBits query = new QueryBits();
        if (FileUtils.isSortableColumn(orderBy)) {
            query.setOrderByColumn(FileQueries.getOrderByColumn(orderBy));
            query.setSortOrder(order);
        }
        List<File> files = softwareService.getSoftwareFiles(query, softwareId);
        List dataList = new ArrayList();
        if (!files.isEmpty()) {
            RowClassUiHelper ui = new RowClassUiHelper();
            Counter counter = new Counter();
            for (File file : files) {
                List columns = new ArrayList();
                columns.add(counter.incrCounter() + ".");
                String path = fileDownloadPath + file.getId();
                columns.add(WidgetUtils.getFileIconLink(canDownloadFile, file.getLogicalName(), path));
                columns.add(file.getTitle());
                columns.add(file.getCreationDate());
                columns.add(FileUtils.formatFileSize(requestContext, file.getSize()));
                if (canDeleteFile) {
                    columns.add("<a href=\"" + fileDeletePath + file.getId() + "\">" + deleteFileText + "</a>");
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
        standardTemplate.setTemplatePath(mapping);
        TableTemplate tableHeader = new TableTemplate();
        standardTemplate.addTemplate(tableHeader);
        tableHeader.setColumnPath(AppPaths.ROOT + AppPaths.SOFTWARE_FILE + "?softwareId=" + softwareId);
        tableHeader.setColumnHeaders(columnHeaders);
        tableHeader.setColumnTextKey("files.colName.");
        tableHeader.setSortableColumnHeaders(FileUtils.getSortableColumns());
        tableHeader.setOrderBy(orderBy);
        tableHeader.setOrder(order);
        tableHeader.setDataList(dataList);
        tableHeader.setEmptyRowMsgKey("files.noAttachments");
        SoftwareSpecTemplate tmpl = new SoftwareSpecTemplate(software);
        standardTemplate.addTemplate(tmpl);
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.softwareDetail.header", new Object[] { software.getName() });
        List<Link> list = SoftwareUtils.getSoftwareHeaderCommands(accessUser, softwareId);
        header.getHeaderCmds().addAll(list);
        FileService fileService = ServiceProvider.getFileService();
        if (Access.hasPermission(accessUser, AppPaths.SOFTWARE_FILE_ADD)) {
            Link link = new Link();
            link.setTitleKey("files.fileAttach");
            if (fileService.isDirectoryExist(ConfigManager.file.getSoftwareFileRepositoryLocation())) {
                link.setPath(AppPaths.ROOT + AppPaths.SOFTWARE_FILE_ADD + "?softwareId=" + softwareId);
                link.setImgSrc(AppPaths.ROOT + AppPaths.FILE_ADD_ICON);
            } else {
                link.setImgAlt("files.warning.invalidPath");
                link.setImgSrc(AppPaths.ROOT + AppPaths.WARNING_ICON);
            }
            header.addHeaderCmds(link);
        }
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(SoftwareUtils.softwareTabList(requestContext, software));
        tabs.setTabActive(SoftwareUtils.FILES_TAB);
        return standardTemplate.findForward(STANDARD_TEMPLATE);
    }
}
