package com.alianzamedica.controllers.patient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.objectsearch.sqlsearch.ObjectSearch;
import org.objectsearch.web.tools.Converter;
import org.w3c.dom.Document;
import com.alianzamedica.businessobject.Doctor;
import com.alianzamedica.businessobject.Patient;
import com.alianzamedica.tools.Enviroment;
import com.alianzamedica.view.PatientForm;

/**
 * @author Carlos
 * 
 */
public class PatientSaveAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Enviroment env = Enviroment.getInstance();
        Document doc = env.getDocument();
        PatientForm patientForm = (PatientForm) form;
        Patient patient = new Patient();
        String sId = patientForm.getId();
        Integer id = null;
        if (sId != null && sId.trim().length() > 0) {
            try {
                id = Integer.valueOf(sId);
            } catch (Exception e) {
            }
        }
        patient.setId(id);
        patient.setFirstName(patientForm.getFirstName());
        patient.setLastName(patientForm.getLastName());
        patient.setCard(patientForm.getCard());
        patient.setAge(Converter.string2Integer(patientForm.getAge()));
        patient.setSince(Converter.string2Date(patientForm.getSince()));
        patient.setComment(patientForm.getComment());
        patient.setLocation(patientForm.getLocation());
        patient.setDiagnostic(patientForm.getDiagnostic());
        patient.setSex(patientForm.getSex());
        patient.setWeight(Converter.string2Float(patientForm.getWeight()));
        patient.setPhone(patientForm.getPhone());
        patient.setEmail(patientForm.getEmail());
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor != null) {
            patient.setDoctorId(doctor.getId());
        }
        patient.setFullName(patientForm.getFirstName() + " " + patientForm.getLastName());
        ObjectSearch search = new ObjectSearch(doc, "com.alianzamedica.connection.ConnectionImpl");
        if (id == null) {
            search.insertObject(patient);
            request.setAttribute("mensaje", "se ha agregado al paciente correctamente");
        } else {
            search.updateObject(patient);
            request.setAttribute("mensaje", "se ha actualizado al paciente correctamente");
        }
        request.setAttribute("linkRetorno", "/admin/patient/list.do");
        return mapping.findForward("success");
    }
}
