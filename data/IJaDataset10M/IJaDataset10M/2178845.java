package com.worthsoln.patientview;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.worthsoln.actionutils.ActionUtils;
import com.worthsoln.database.DatabaseDAO;
import com.worthsoln.database.action.DatabaseAction;
import com.worthsoln.patientview.diagnosis.DiagnosisUtils;
import com.worthsoln.patientview.edtacode.EdtaCodeUtils;
import com.worthsoln.patientview.logging.AddLog;
import com.worthsoln.patientview.logon.LogonUtils;
import com.worthsoln.patientview.uktransplant.UktUtils;

public class PatientDetailsAdminViewAction extends DatabaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String nhsno = ActionUtils.retrieveStringPropertyValue("nhsno", form, request);
        String unitcode = ActionUtils.retrieveStringPropertyValue("unitcode", form, request);
        request.getSession().setAttribute("patientBeingViewedNhsNo", nhsno);
        request.getSession().setAttribute("patientBeingViewedUnitcode", unitcode);
        DatabaseDAO dao = getDao(request);
        Patient patient = PatientUtils.retrievePatient(nhsno, unitcode, dao);
        if (patient != null) {
            EdtaCodeUtils.addEdtaCodeToRequest(patient.getDiagnosis(), "edtaCode", dao, request);
            EdtaCodeUtils.addEdtaCodeToRequest(patient.getTreatment(), "treatmentCode", dao, request);
            UktUtils.addUktStatusToRequest(patient.getNhsno(), dao, request);
            request.setAttribute("otherDiagnoses", DiagnosisUtils.getOtherDiagnoses(patient.getNhsno(), patient.getCentreCode()));
            request.setAttribute("patient", patient);
            AddLog.addLog(request.getUserPrincipal().getName(), AddLog.PATIENT_VIEW, "", patient.getNhsno(), patient.getCentreCode(), "");
        }
        EdtaCodeUtils.addEdtaCodeToRequest("static", "staticLinks", dao, request);
        ActionUtils.setUpNavLink(mapping.getParameter(), request);
        return LogonUtils.logonChecks(mapping, request);
    }

    public String getDatabaseName() {
        return "patientview";
    }

    public String getIdentifier() {
        return "edtaCode";
    }
}
