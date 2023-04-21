package com.ext.portlet.entrepriseadmin.action;

import java.io.File;
import java.io.FileInputStream;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.context.ApplicationContext;
import com.liferay.portal.DuplicateOrganizationException;
import com.liferay.portal.NoSuchCountryException;
import com.liferay.portal.NoSuchListTypeException;
import com.liferay.portal.NoSuchOrganizationException;
import com.liferay.portal.OrganizationNameException;
import com.liferay.portal.OrganizationParentException;
import com.liferay.portal.RequiredOrganizationException;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.impl.OrganizationImpl;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.service.OrganizationServiceUtil;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portlet.enterpriseadmin.action.ActionUtil;
import com.liferay.util.servlet.SessionErrors;
import fr.gfi.gfinet.server.AgenceService;
import fr.gfi.gfinet.server.CollaboratorService;
import fr.gfi.gfinet.server.CostCenterService;
import fr.gfi.gfinet.server.info.Agence;
import fr.gfi.gfinet.server.info.Collaborator;
import fr.gfi.gfinet.server.info.Document;
import fr.gfi.gfinet.server.util.ServerTools;

/**
 * <a href="EditOrganizationAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class EditOrganizationAction extends PortletAction {

    private CollaboratorService collaboratorService;

    private AgenceService agenceService;

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) throws Exception {
        String cmd = ParamUtil.getString(req, Constants.CMD);
        try {
            Organization organization = null;
            if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
                organization = updateOrganization(req);
            } else if (cmd.equals("comments")) {
                organization = updateComments(req);
            } else if (cmd.equals(Constants.DELETE)) {
                deleteOrganizations(req);
            }
            String redirect = null;
            if (organization != null) {
                redirect = ParamUtil.getString(req, "redirect") + organization.getOrganizationId();
            }
            sendRedirect(req, res, redirect);
        } catch (Exception e) {
            if (e instanceof NoSuchOrganizationException || e instanceof PrincipalException) {
                SessionErrors.add(req, e.getClass().getName());
                setForward(req, "portlet.enterprise_admin.error");
            } else if (e instanceof DuplicateOrganizationException || e instanceof NoSuchCountryException || e instanceof NoSuchListTypeException || e instanceof OrganizationNameException || e instanceof OrganizationParentException || e instanceof RequiredOrganizationException) {
                SessionErrors.add(req, e.getClass().getName());
                if (e instanceof RequiredOrganizationException) {
                    res.sendRedirect(ParamUtil.getString(req, "redirect"));
                }
            } else {
                throw e;
            }
        }
    }

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        try {
            ActionUtil.getOrganization(req);
        } catch (Exception e) {
            if (e instanceof NoSuchOrganizationException || e instanceof PrincipalException) {
                SessionErrors.add(req, e.getClass().getName());
                return mapping.findForward("portlet.enterprise_admin.error");
            } else {
                throw e;
            }
        }
        return mapping.findForward(getForward(req, "portlet.enterprise_admin.edit_organization"));
    }

    protected boolean isLocation() {
        return _LOCATION;
    }

    protected void deleteOrganizations(ActionRequest req) throws Exception {
        long[] deleteOrganizationIds = StringUtil.split(ParamUtil.getString(req, "deleteOrganizationIds"), 0L);
        for (int i = 0; i < deleteOrganizationIds.length; i++) {
            OrganizationServiceUtil.deleteOrganization(deleteOrganizationIds[i]);
        }
    }

    protected Organization updateComments(ActionRequest req) throws Exception {
        long organizationId = ParamUtil.getLong(req, "organizationId");
        String comments = ParamUtil.getString(req, "comments");
        return OrganizationServiceUtil.updateOrganization(organizationId, comments);
    }

    protected Organization updateOrganization(ActionRequest req) throws Exception {
        long organizationId = ParamUtil.getLong(req, "organizationId");
        long parentOrganizationId = ParamUtil.getLong(req, "parentOrganizationId", OrganizationImpl.DEFAULT_PARENT_ORGANIZATION_ID);
        String name = ParamUtil.getString(req, "name");
        boolean recursable = ParamUtil.getBoolean(req, "recursable");
        int statusId = ParamUtil.getInteger(req, "statusId");
        long regionId = ParamUtil.getLong(req, "regionId");
        long countryId = ParamUtil.getLong(req, "countryId");
        ApplicationContext ctx = getWebApplicationContext();
        collaboratorService = (CollaboratorService) ctx.getBean("fr.gfi.gfinet.server.CollaboratorService");
        agenceService = (AgenceService) ctx.getBean("fr.gfi.gfinet.server.AgenceService");
        String agenceView = ParamUtil.getString(req, "agenceView");
        long idResponsible = ParamUtil.getLong(req, "responsible");
        Organization organization = null;
        if (organizationId <= 0) {
            organization = OrganizationServiceUtil.addOrganization(parentOrganizationId, name, isLocation(), recursable, regionId, countryId, statusId);
        } else {
            organization = OrganizationServiceUtil.updateOrganization(organizationId, parentOrganizationId, name, isLocation(), recursable, regionId, countryId, statusId);
        }
        System.out.println("Photo : " + agenceView);
        if (agenceView.compareTo("") != 0) {
            try {
                byte[] result = ouvrirFichier(agenceView);
                Agence a = agenceService.getAgenceByPortalId(String.valueOf(organization.getOrganizationId()));
                a.setPhoto(new Document("photo", result));
                agenceService.saveAgence(a);
            } catch (Exception e) {
            }
        }
        if (idResponsible != 0) {
            try {
                Agence a = agenceService.getAgenceByPortalId(String.valueOf(organization.getOrganizationId()));
                Collaborator collab = collaboratorService.getCollaboratorbyPortalId(String.valueOf(idResponsible));
                a.setLeResponsable(collab);
                agenceService.saveAgence(a);
            } catch (Exception e) {
            }
        }
        return organization;
    }

    private byte[] ouvrirFichier(String filename) {
        try {
            File fichier = new File(filename);
            byte[] result = new byte[(int) fichier.length()];
            FileInputStream in = new FileInputStream(filename);
            in.read(result);
            in.close();
            return result;
        } catch (Exception e) {
            System.out.println("Probleme lors de la lecture du fichier: " + e.getMessage());
            return null;
        }
    }

    private static final boolean _LOCATION = false;
}
