package com.kwoksys.action.contracts;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.action.common.template.*;
import com.kwoksys.action.hardware.HardwareListTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.ContractUtils;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.hardware.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dao.SoftwareQueries;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.dto.Link;
import com.kwoksys.framework.system.AppPaths;
import com.kwoksys.framework.connection.database.QueryBits;
import com.kwoksys.framework.helper.Counter;
import com.kwoksys.framework.session.CookieManager;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.system.Access;
import com.kwoksys.framework.system.Localizer;
import com.kwoksys.framework.system.RequestContext;
import com.kwoksys.framework.util.HttpUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Action class for contract detail.
 */
public class ContractRelationshipAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContext requestContext = new RequestContext(request);
        AccessUser accessUser = requestContext.getUser();
        HttpSession session = request.getSession();
        Integer contractId = HttpUtils.getParameter(request, "contractId");
        ContractService contractService = ServiceProvider.getContractService();
        Contract contract = contractService.getContract(contractId);
        String hwOrderBy = HttpUtils.getOrSetCookie(request, response, "hwOrderBy", CookieManager.HARDWARE_ORDER_BY, Hardware.HARDWARE_NAME);
        String hwOrder = HttpUtils.getOrSetCookie(request, response, "hwOrder", CookieManager.HARDWARE_SORT_ORDER, QueryBits.ASCENDING);
        session.setAttribute(SessionManager.CONTRACT_HARDWARE_FORM, null);
        session.setAttribute(SessionManager.CONTRACT_SOFTWARE_FORM, null);
        boolean canRemoveHardware = Access.hasPermission(accessUser, AppPaths.CONTRACTS_HARDWARE_REMOVE_2);
        List hwColumnHeaders = new ArrayList();
        if (canRemoveHardware) {
            hwColumnHeaders.add("");
        }
        hwColumnHeaders.addAll(HardwareUtils.getColumnHeaderList());
        QueryBits query = new QueryBits();
        if (HardwareUtils.isSortableColumn(hwOrderBy)) {
            query.setOrderByColumn(HardwareQueries.getOrderByColumn(hwOrderBy));
            query.setSortOrder(hwOrder);
        }
        Map hiddenVariableMap = new HashMap();
        hiddenVariableMap.put("contractId", contract.getId());
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setTemplatePath(mapping, SUCCESS);
        request.setAttribute("ajaxHardwareDetailPath", AppPaths.ROOT + AppPaths.IT_MGMT_AJAX_GET_HARDWARE_DETAIL + "?hardwareId=");
        request.setAttribute("formRemoveHardwareAction", AppPaths.CONTRACTS_HARDWARE_REMOVE_2);
        HardwareListTemplate listTemplate = new HardwareListTemplate("_hardware");
        standardTemplate.addTemplate(listTemplate);
        listTemplate.setHardwareList(contractService.getContractHardwareList(query, contractId));
        listTemplate.setCanRemoveHardware(canRemoveHardware);
        listTemplate.setColspan(hwColumnHeaders.size());
        listTemplate.setCounter(new Counter());
        listTemplate.setFormHiddenVariableMap(hiddenVariableMap);
        TableEmptyTemplate empty = new TableEmptyTemplate("_hardware");
        standardTemplate.addTemplate(empty);
        empty.setColSpan(hwColumnHeaders.size());
        empty.setRowText(Localizer.getText(requestContext, "itMgmt.hardwareList.emptyTableMessage"));
        TableHeaderTemplate tableHeader = new TableHeaderTemplate("_hardware");
        standardTemplate.addTemplate(tableHeader);
        tableHeader.setColumnList(hwColumnHeaders);
        tableHeader.setSortableColumnList(HardwareUtils.getSortableColumns());
        tableHeader.setColumnPath(AppPaths.ROOT + AppPaths.CONTRACTS_ITEMS + "?contractId=" + contractId);
        tableHeader.setColumnTextKey("common.column.");
        tableHeader.setOrderBy(hwOrderBy);
        tableHeader.setOrderByParamName("hwOrderBy");
        tableHeader.setOrderParamName("hwOrder");
        tableHeader.setOrder(hwOrder);
        String swOrderBy = HttpUtils.getOrSetCookie(request, response, "swOrderBy", CookieManager.SOFTWARE_ORDER_BY, Software.NAME);
        String swOrder = HttpUtils.getOrSetCookie(request, response, "swOrder", CookieManager.SOFTWARE_SORT_ORDER, QueryBits.ASCENDING);
        QueryBits swQuery = new QueryBits();
        if (SoftwareUtils.isSortableColumn(swOrderBy)) {
            swQuery.setOrderByColumn(SoftwareQueries.getOrderByColumn(swOrderBy));
            swQuery.setSortOrder(swOrder);
        }
        List<Software> softwareList = contractService.getContractSoftwareList(swQuery, contractId);
        List<Map> formattedList = SoftwareUtils.formatSoftwareList(requestContext, softwareList, new Counter());
        TableTemplate tableTemplate = new TableTemplate("_software");
        standardTemplate.addTemplate(tableTemplate);
        tableTemplate.setDataList(formattedList);
        tableTemplate.setColumnHeaders(SoftwareUtils.getColumnHeaderList());
        tableTemplate.setColumnPath(AppPaths.ROOT + AppPaths.CONTRACTS_ITEMS + "?contractId=" + contractId);
        tableTemplate.setSortableColumnHeaders(SoftwareUtils.getSortableColumns());
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setEmptyRowMsgKey("itMgmt.softwareList.emptyTableMessage");
        tableTemplate.setOrderBy(swOrderBy);
        tableTemplate.setOrder(swOrder);
        tableTemplate.setOrderByParamName("swOrderBy");
        tableTemplate.setOrderParamName("swOrder");
        tableTemplate.setFormRemoveItemAction(AppPaths.CONTRACTS_SOFTWARE_REMOVE_2);
        tableTemplate.setFormHiddenVariableMap(hiddenVariableMap);
        tableTemplate.setFormRowIdName("formSoftwareId");
        standardTemplate.addTemplate(new ContractSpecTemplate(contract));
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.contractDetail.header", new Object[] { contract.getName() });
        if (Access.hasPermission(accessUser, AppPaths.CONTRACTS_HARDWARE_ADD)) {
            Link link = new Link();
            link.setPath(AppPaths.ROOT + AppPaths.CONTRACTS_HARDWARE_ADD + "?contractId=" + contractId);
            link.setTitleKey("common.linking.linkHardware");
            header.addHeaderCmds(link);
        }
        if (Access.hasPermission(accessUser, AppPaths.CONTRACTS_SOFTWARE_ADD)) {
            Link link = new Link();
            link.setPath(AppPaths.ROOT + AppPaths.CONTRACTS_SOFTWARE_ADD + "?contractId=" + contractId);
            link.setTitleKey("common.linking.linkSoftware");
            header.addHeaderCmds(link);
        }
        Link link = new Link();
        link.setPath(AppPaths.ROOT + AppPaths.CONTRACTS_LIST);
        link.setTitleKey("itMgmt.cmd.contractList");
        header.addHeaderCmds(link);
        TabsTemplate tabs = new TabsTemplate();
        standardTemplate.addTemplate(tabs);
        tabs.setTabList(ContractUtils.contractTabList(requestContext, contract));
        tabs.setTabActive(ContractUtils.HARDWARE_TAB);
        return standardTemplate.findForward(STANDARD_TEMPLATE);
    }
}
