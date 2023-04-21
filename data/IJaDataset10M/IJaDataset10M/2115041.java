package com.patientos.portal.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.patientos.portal.actions.BaseAction;
import org.patientos.portal.wwwext.controls.ExtUtil;
import com.patientis.business.patient.DefaultPatientSearchResultsController;
import com.patientis.business.patient.DefaultVisitSearchResultsController;
import com.patientis.framework.api.services.ClinicalServer;
import com.patientis.framework.api.services.PatientServer;
import com.patientis.framework.controls.exceptions.ISCancelActionException;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.common.BaseModel;
import com.patientis.model.common.Converter;
import com.patientis.model.common.IBaseModel;
import com.patientis.model.common.ServiceCall;
import com.patientis.model.patient.ViewPatientModel;
import com.patientis.model.patient.ViewVisitModel;
import com.patientis.model.security.ApplicationControlModel;

/**
 * @author gcaulton
 *
 */
public class WebDefaultVisitSearchResultsController extends DefaultVisitSearchResultsController implements IWebController {

    private StringBuffer js = new StringBuffer(1024);

    /**
	 * @see com.patientis.business.controllers.DefaultCustomController#getWebJavascript(com.patientis.model.clinical.FormModel, com.patientis.model.security.ApplicationControlModel, com.patientis.model.common.ServiceCall)
	 */
    @Override
    public String getWebJavascript(FormModel form, ApplicationControlModel acm, ServiceCall call) throws ISCancelActionException, Exception {
        js.append(ExtUtil.getFormPopupMenuFunction(acm));
        return js.toString();
    }

    /**
     * 
     * @param customControllerActionRefId
     * @param request
     * @return
     */
    public void initialize(BaseAction action, ApplicationControlModel acm, HttpServletRequest request, long formTypeRefId, long visitId, long patientId) throws Exception {
        this.acm = acm;
        if (visitId > 0L) {
            this.patient = PatientServer.getPatientForVisitId(visitId);
        } else if (patientId > 0L) {
            this.patient = PatientServer.getPatient(patientId);
        }
        long settingsFormId = acm.getSettingFormValue(0L).getSettingsFormId();
        settingsForm = ClinicalServer.getForm(settingsFormId);
        createRenderer();
        renderer.initialize(new ArrayList<IBaseModel>(), getControlMediator(), acm, getSettings());
    }

    /**
     * @see com.patientos.portal.controllers.IWebController#getIFrameDisplay()
     * @see com.patientos.portal.controllers.IWebController#getIFrameDisplay()
     * @see com.patientos.portal.controllers.IWebController#getIFrameDisplay()
     */
    @Override
    public String getIFrameDisplay() throws Exception {
        StringBuffer sb = new StringBuffer(1024);
        sb.append(" " + "\n<div id='tableContainer");
        sb.append(acm.getId());
        sb.append("' class='tableContainer'>" + "\n<table border='0' cellpadding='0' cellspacing='0' width='100%' class='scrollTable'>");
        sb.append("<thead class='fixedHeader'><tr>");
        for (int i = 0; i < this.renderer.getGridTableModel().getColumnCount(); i++) {
            sb.append("\n<th><a href='#'>");
            String display = renderer.getGridColumns().get(i).getLabel();
            sb.append(Converter.convertHtmlEntities(Converter.removeHtmlBody(Converter.convertDisplayString(display))));
            sb.append("</a></th>");
        }
        sb.append("</tr></thead><tbody class='scrollContent'>");
        for (ViewVisitModel visit : lastVisitList) {
            sb.append("\n<tr>");
            for (int i = 0; i < this.renderer.getGridTableModel().getColumnCount(); i++) {
                sb.append("\n<td>");
                if (i == 1) {
                    sb.append("\n<div id='context" + acm.getId() + "Link'><a href='#' onclick='popup" + acm.getId() + "(" + visit.getViewVisitQueryModel().getId() + "); return false;'>");
                }
                String display = renderer.getTextDisplay(visit, i);
                sb.append(Converter.convertHtmlEntities(Converter.removeHtmlBody(Converter.convertDisplayString(display))));
                sb.append("</a>");
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</tbody></table></div>");
        return sb.toString();
    }
}
