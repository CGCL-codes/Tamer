package com.scs.base.web.location;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import com.scs.base.model.location.LocationType;
import com.scs.base.service.location.LocationTypeService;

public class LocationTypeAction extends DispatchAction {

    private static Log log = LogFactory.getLog(LocationTypeAction.class);

    private LocationTypeService locationTypeService = null;

    public void setLocationTypeService(LocationTypeService locationTypeService) {
        this.locationTypeService = locationTypeService;
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'delete' method...");
        }
        String locationTypeId = request.getParameter("locationType.id");
        locationTypeService.removeLocationType(new Long(locationTypeId));
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("locationType.deleted"));
        saveMessages(request, messages);
        return list(mapping, form, request, response);
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'edit' method...");
        }
        DynaValidatorForm locationTypeForm = (DynaValidatorForm) form;
        String locationTypeId = request.getParameter("id");
        if (locationTypeId != null) {
            LocationType locationType = locationTypeService.getLocationType(new Long(locationTypeId));
            if (locationType == null) {
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("locationType.missing"));
                saveErrors(request, errors);
                return mapping.findForward("list");
            }
            locationTypeForm.set("locationType", locationType);
        }
        return mapping.findForward("edit");
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'list' method...");
        }
        request.setAttribute("locationTypes", locationTypeService.getLocationTypes());
        return mapping.findForward("list");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'cancel' method...");
        }
        request.setAttribute("locationTypes", locationTypeService.getLocationTypes());
        return mapping.findForward("list");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'save' method...");
        }
        ActionMessages errors = form.validate(mapping, request);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("edit");
        }
        DynaValidatorForm locationTypeForm = (DynaValidatorForm) form;
        locationTypeService.saveLocationType((LocationType) locationTypeForm.get("locationType"));
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("locationType.saved"));
        saveMessages(request, messages);
        return list(mapping, form, request, response);
    }

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return list(mapping, form, request, response);
    }
}
