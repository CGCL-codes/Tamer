package com.kwoksys.action.contracts;

import com.kwoksys.action.common.template.FooterTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.biz.ServiceAPI;
import com.kwoksys.biz.system.dto.Link;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.framework.configs.AppPaths;
import com.kwoksys.framework.connection.database.QueryBits;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.system.Access;
import com.kwoksys.framework.system.Localization;
import com.kwoksys.framework.util.NumberUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class of Contract index page.
 */
public class ContractIndexAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccessUser user = Access.getUser(request);
        HttpSession session = request.getSession();
        ContractService contractService = ServiceAPI.getContractService();
        boolean hasContractsAccess = Access.hasPermission(user, AppPaths.CONTRACTS_LIST_PATH);
        List links = new ArrayList();
        if (hasContractsAccess) {
            Map linkMap;
            if (session.getAttribute(SessionManager.CONTRACT_SEARCH_CRITERIA_MAP) != null) {
                linkMap = new HashMap();
                linkMap.put("linkPath", AppPaths.CONTRACTS_LIST_PATH);
                linkMap.put("linkText", Localization.getContent(request, "common.search.showLastSearch"));
                links.add(linkMap);
            }
            linkMap = new HashMap();
            linkMap.put("linkPath", AppPaths.CONTRACTS_LIST_PATH + "?cmd=showNonExpired");
            linkMap.put("linkText", Localization.getContent(request, "contracts.filter.currentContracts"));
            links.add(linkMap);
            linkMap = new HashMap();
            linkMap.put("linkPath", AppPaths.CONTRACTS_LIST_PATH + "?cmd=showAll");
            linkMap.put("linkText", Localization.getContent(request, "contracts.filter.allContracts"));
            links.add(linkMap);
        }
        List<Map> summary = contractService.getContractsSummary();
        List formattedSummary = new ArrayList();
        for (Map map : summary) {
            Map formattedMap = new HashMap();
            if (NumberUtils.replaceNull(map.get("count")) > 0) {
                formattedMap.put("text", Localization.getContent(request, "contracts.search.expire_" + map.get("interval")));
                formattedMap.put("path", "<a href=\"" + AppPaths.ROOT + AppPaths.CONTRACTS_LIST_PATH + "?cmd=search&contractExpire=" + map.get("interval") + "\">" + map.get("count") + "</a>");
                formattedSummary.add(formattedMap);
            }
        }
        request.setAttribute("numContractRecords", contractService.getContractCount(new QueryBits()));
        request.setAttribute("contractFilters", links);
        request.setAttribute("contractsSummary", formattedSummary);
        ContractSearchTemplate searchTemplate = new ContractSearchTemplate();
        searchTemplate.setFormAction(AppPaths.CONTRACTS_LIST_PATH);
        searchTemplate.apply(request);
        HeaderTemplate header = new HeaderTemplate();
        header.setTitleKey("core.moduleName.3");
        if (Access.hasPermission(user, AppPaths.CONTRACTS_ADD)) {
            Link link = new Link();
            link.setPath(AppPaths.ROOT + AppPaths.CONTRACTS_ADD);
            link.setTitleKey("itMgmt.contractAdd.title");
            header.addHeaderCmds(link);
        }
        header.apply(request);
        new FooterTemplate().apply(request);
        return mapping.findForward("success");
    }
}
