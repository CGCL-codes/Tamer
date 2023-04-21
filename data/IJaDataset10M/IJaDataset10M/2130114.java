package com.alianzamedica.businessobject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.objectsearch.sqlsearch.ObjectSearch;
import org.w3c.dom.Document;
import com.alianzamedica.tools.Enviroment;

/**
 * @author Carlos
 * 
 */
public class Prescription extends BusinessObject {

    private Integer id;

    private String nombre;

    private Integer doctorId;

    private Integer patientId;

    private String tag;

    private Date expedition;

    /**
	 * detalles de la prescripcion
	 */
    public ArrayList<PrescriptionDetail> detail = new ArrayList<PrescriptionDetail>();

    /**
	 * regresa el id de prescripcion
	 * 
	 * @return el id generado
	 */
    public Integer getId() {
        return id;
    }

    /**
	 * @param id
	 */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * nombre.
	 * 
	 * @return nombre.
	 */
    public String getNombre() {
        return nombre;
    }

    /**
	 * nombre.
	 * 
	 * @param nombre
	 *            nombre.
	 */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
	 * id del doctor.
	 * 
	 * @return id del doctor.
	 */
    public Integer getDoctorId() {
        return doctorId;
    }

    /**
	 * id del doctor.
	 * 
	 * @param doctorId
	 *            id del doctor.
	 */
    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    /**
	 * @param patientId
	 */
    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    /**
	 * id del doctor.
	 * 
	 * @return id del doctor.
	 */
    public Integer getPatientId() {
        return patientId;
    }

    /**
	 * tag.
	 * 
	 * @return tag.
	 */
    public String getTag() {
        return tag;
    }

    /**
	 * tag.
	 * 
	 * @param tag
	 *            tag.
	 */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
	 * fecha de expedicion.
	 * 
	 * @return fecha de expedicion.
	 */
    public Date getExpedition() {
        return expedition;
    }

    /**
	 * @param expedition
	 */
    public void setExpedition(Date expedition) {
        this.expedition = expedition;
    }

    /**
	 * regresa los detalles de la prescripcion.
	 * 
	 * @return los detalles de la prescripcion.
	 * @throws Exception
	 *             si ocurre error.
	 */
    @SuppressWarnings("unchecked")
    public ArrayList<PrescriptionDetail> getDetails() throws Exception {
        ArrayList<PrescriptionDetail> details = null;
        Enviroment env = Enviroment.getInstance();
        Document doc = env.getDocument();
        ObjectSearch search = new ObjectSearch(doc, "com.alianzamedica.connection.ConnectionImpl");
        PrescriptionDetail pd = new PrescriptionDetail();
        pd.setPrescriptionId(this.id);
        details = search.searchObjects(pd);
        return details;
    }

    /**
	 * regresa el doctor asignado a la prescripcion.
	 * 
	 * @return doctor asignado a la prescripcion.
	 * @throws Exception
	 *             si ocurre error.
	 */
    @SuppressWarnings("unchecked")
    public Doctor getDoctor() throws Exception {
        Doctor d = new Doctor();
        Enviroment env = Enviroment.getInstance();
        Document doc = env.getDocument();
        ObjectSearch search = new ObjectSearch(doc, "com.alianzamedica.connection.ConnectionImpl");
        d.setId(this.doctorId);
        Iterator<Doctor> iterator = search.searchObjects(d).iterator();
        Doctor doctor = null;
        while (iterator.hasNext()) {
            doctor = (Doctor) iterator.next();
        }
        return doctor;
    }

    /**
	 * @return el paciente asignado.
	 * @throws Exception
	 *             si ocurre algun error.
	 */
    @SuppressWarnings("unchecked")
    public Patient getPatient() throws Exception {
        Enviroment env = Enviroment.getInstance();
        Document doc = env.getDocument();
        ObjectSearch search = new ObjectSearch(doc, "com.alianzamedica.connection.ConnectionImpl");
        Patient patient = null;
        Patient p = new Patient();
        if (patientId != null) {
            p.setId(patientId);
            Iterator<Patient> iterator = search.searchObjects(p).iterator();
            while (iterator.hasNext()) {
                patient = iterator.next();
            }
        }
        return patient;
    }

    /**
	 * detalle.
	 * 
	 * @return detalle.
	 */
    public ArrayList<PrescriptionDetail> getDetail() {
        return detail;
    }

    /**
	 * detalle.
	 * 
	 * @param detail
	 *            detalle.
	 */
    public void setDetail(ArrayList<PrescriptionDetail> detail) {
        this.detail = detail;
    }

    @Override
    public void setValueToSearch(String name) {
    }
}
