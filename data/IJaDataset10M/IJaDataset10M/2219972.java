package com.kwoksys.action.contracts;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.action.common.template.*;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.ContractUtils;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.FileUtils;
import com.kwoksys.biz.files.dao.FileQueries;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.system.dto.Link;
import com.kwoksys.framework.system.AppPaths;
import com.kwoksys.framework.connection.database.QueryBits;
import com.kwoksys.framework.helper.Counter;
import com.kwoksys.framework.helper.RowClassUiHelper;
import com.kwoksys.framework.system.*;
import com.kwoksys.framework.util.HttpUtils;
import com.kwoksys.framework.util.WidgetUtils;
import com.kwoksys.framework.configs.ConfigManager;
import com.kwoksys.framework.session.CookieManager;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Action class for contract detail.
 */
public class ContractDetailAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContext requestContext = new RequestContext(request);
        AccessUser user = requestContext.getUser();
        Integer contractId = HttpUtils.getParameter(request, "contractId");
        String orderBy = HttpUtils.getOrSetCookie(request, response, "orderBy", CookieManager.FILES_ORDER_BY, File.NAME);
        String order = HttpUtils.getOrSetCookie(request, response, "order", CookieManager.FILES_SORT_ORDER, QueryBits.ASCENDING);
        FileService fileService = ServiceProvider.getFileService();
        ContractService contractService = ServiceProvider.getContractService();
        Contract contract = contractService.getContract(contractId);
        String deleteFileText = Localizer.getText(requestContext, "form.button.delete");
        String fileDownloadPath = AppPaths.ROOT + AppPaths.CONTRACTS_FILE_DOWNLOAD + "?contractId=" + contractId + "&fileId=";
        String fileDeletePath = AppPaths.ROOT + AppPaths.CONTRACTS_FILE_DELETE + "?contractId=" + contractId + "&fileId=";
        boolean canDeleteFile = Access.hasPermission(user, AppPaths.CONTRACTS_FILE_DELETE);
        boolean canDownloadFile = Access.hasPermission(user, AppPaths.CONTRACTS_FILE_DOWNLOAD);
        QueryBits query = new QueryBits();
        if (FileUtils.isSortableColumn(orderBy)) {
            query.setOrderByColumn(FileQueries.getOrderByColumn(orderBy));
            query.setSortOrder(order);
        }
        List dataList = new ArrayList();
        List<File> fileList = contractService.getContractFiles(query, contractId);
        if (!fileList.isEmpty()) {
            RowClassUiHelper ui = new RowClassUiHelper();
            Counter counter = new Counter();
            for (File file : fileList) {
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
        standardTemplate.setTemplatePath(mapping, SUCCESS);
        TableTemplate tableHeader = new TableTemplate();
        standardTemplate.addTemplate(tableHeader);
        tableHeader.setColumnPath(AppPaths.ROOT + AppPaths.CONTRACTS_DETAIL + "?contractId=" + contractId);
        tableHeader.setColumnHeaders(columnHeaders);
        tableHeader.setColumnTextKey("files.colName.");
        tableHeader.setSortableColumnHeaders(FileUtils.getSortableColumns());
        tableHeader.setOrderBy(orderBy);
        tableHeader.setOrder(order);
        tableHeader.setDataList(dataList);
        tableHeader.setEmptyRowMsgKey("files.noAttachments");
        ContractSpecTemplate template = new ContractSpecTemplate(contract);
        standardTemplate.addTemplate(template);
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.contractDetail.header", new Object[] { contract.getName() });
        if (Access.hasPermission(user, AppPaths.CONTRACTS_EDIT)) {
            Link link = new Link();
            link.setPath(AppPaths.ROOT + AppPaths.CONTRACTS_EDIT + "?contractId=" + contractId);
            link.setTitleKey("itMgmt.cmd.contractEdit");
            header.addHeaderCmds(link);
        }
        if (Access.hasPermission(user, AppPaths.CONTRACTS_DELETE)) {
            Link link = new Link();
            link.setPath(AppPaths.ROOT + AppPaths.CONTRACTS_DELETE + "?contractId=" + contractId);
            link.setTitleKey("itMgmt.cmd.contractDelete");
            header.addHeaderCmds(link);
        }
        if (Access.hasPermission(user, AppPaths.CONTRACTS_FILE_ADD)) {
            Link link = new Link();
            link.setTitleKey("files.fileAttach");
            if (fileService.isDirectoryExist(ConfigManager.file.getContractFileRepositoryLocation())) {
                link.setPath(AppPaths.ROOT + AppPaths.CONTRACTS_FILE_ADD + "?contractId=" + contractId);
                link.setImgSrc(AppPaths.ROOT + AppPaths.FILE_ADD_ICON);
            } else {
                link.setImgAlt("files.warning.invalidPath");
                link.setImgSrc(AppPaths.ROOT + AppPaths.WARNING_ICON);
            }
            header.addHeaderCmds(link);
        }
        Link link = new Link();
        link.setPath(AppPaths.ROOT + AppPaths.CONTRACTS_LIST);
        link.setTitleKey("itMgmt.cmd.contractList");
        header.addHeaderCmds(link);
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(ContractUtils.contractTabList(requestContext, contract));
        tabs.setTabActive(ContractUtils.FILES_TAB);
        CustomFieldsTemplate customFieldsTemplate = new CustomFieldsTemplate();
        standardTemplate.addTemplate(customFieldsTemplate);
        customFieldsTemplate.setObjectTypeId(ObjectTypes.CONTRACT);
        customFieldsTemplate.setObjectId(contractId);
        customFieldsTemplate.setObjectAttrTypeId(contract.getType());
        return standardTemplate.findForward(STANDARD_TEMPLATE);
    }
}
