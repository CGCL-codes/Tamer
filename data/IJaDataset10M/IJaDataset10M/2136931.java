package es.ehrflex.client.service;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;
import es.ehrflex.client.main.model.FileNode;
import es.ehrflex.client.main.user.model.PatientDataModel;

/**
 * Asynchrones oppposite to {@link ArchetypeGWTService}
 * 
 * @author Anton Brass
 * @version 1.0, 18.05.2009
 */
public interface PatientGWTServiceAsync {

    /**
     * @see PatientGWTService#createPatient(es.ehrflex.client.main.user.model.PatientDataModel, String)
     */
    public void createPatient(PatientDataModel patient, String sharedSecret, AsyncCallback<Void> async);

    /**
     * @see PatientGWTService#getPatient(String, String)
     */
    public void getPatient(String pPatientIdentifier, String sharedSecret, AsyncCallback<PatientDataModel> async);

    /**
     * @see PatientGWTService#createPatient(es.ehrflex.client.main.user.model.PatientDataModel, String)
     */
    public void editPatient(PatientDataModel patient, String sharedSecret, AsyncCallback<Void> async);

    /**
     * @see PatientGWTService#deletePatient(es.ehrflex.client.main.user.model.PatientDataModel, String)
     */
    public void deletePatient(PatientDataModel patient, String sharedSecret, AsyncCallback<Void> async);
}
