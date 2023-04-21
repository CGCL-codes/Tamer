package fr.gfi.gfinet.web.struts.gestionAgence.action;

import java.util.List;
import javax.portlet.RenderRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.liferay.portal.kernel.util.JavaConstants;
import fr.gfi.gfinet.server.AgenceService;
import fr.gfi.gfinet.server.CostCenterService;
import fr.gfi.gfinet.server.info.Agence;
import fr.gfi.gfinet.server.info.CostCenter;

public class ViewAction extends org.apache.struts.action.Action {

    public static final String GLOBAL_FORWARD_welcome = "welcome";

    public static final String FORWARD_view = "view";

    private AgenceService agenceService;

    private CostCenterService costCenterService;

    public ViewAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RenderRequest renderRequest = (RenderRequest) request.getAttribute(JavaConstants.JAVAX_PORTLET_REQUEST);
        String portalId = renderRequest.getRemoteUser();
        if (portalId == null) {
            return mapping.findForward("remoteUserNull");
        }
        List<Agence> agenceList = agenceService.listAgence();
        List<CostCenter> costCenterList = costCenterService.listCostCenter();
        request.setAttribute("listAgences", agenceList);
        request.setAttribute("listCostCenter", costCenterList);
        return mapping.findForward(FORWARD_view);
    }

    public void setAgenceService(AgenceService agenceService) {
        this.agenceService = agenceService;
    }

    public void setCostCenterService(CostCenterService costCenterService) {
        this.costCenterService = costCenterService;
    }
}
