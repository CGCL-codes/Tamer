package com.kwoksys.action.contracts;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.dto.linking.ContractSoftwareLink;
import com.kwoksys.framework.system.AppPaths;
import com.kwoksys.framework.system.RequestContext;
import com.kwoksys.framework.util.HttpUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action class for adding software to contract.
 */
public class ContractSoftwareAdd2Action extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContext requestContext = new RequestContext(request);
        AccessUser user = requestContext.getUser();
        Integer contractId = HttpUtils.getParameter(request, "contractId");
        Integer softwareId = HttpUtils.getParameter(request, "formSoftwareId");
        ContractService contractService = ServiceProvider.getContractService();
        Contract contract = contractService.getContract(contractId);
        ContractSoftwareLink contractSoftware = new ContractSoftwareLink();
        contractSoftware.setContractId(contractId);
        contractSoftware.setSoftwareId(softwareId);
        contractSoftware.setCreatorId(user.getId());
        ActionMessages errors = contractService.addContractSoftware(requestContext, contractSoftware);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("error");
        } else {
            return redirect(AppPaths.CONTRACTS_ITEMS + "?contractId=" + contract.getId());
        }
    }
}
