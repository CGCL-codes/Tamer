package ocumed.domaene.behandlung;

import java.util.Date;
import ocumed.domaene.patientenverwaltung.DPatient;
import ocumed.teams.IBehandlung;
import ocumed.teams.IMedikament;
import ocumed.teams.IMediver;
import ocumed.teams.IPatient;

public class DMedikamentVerordnung implements IMediver {

    private Date mediVerBis;

    private int mediVerMenge;

    private Date mediVerVon;

    private String mediverdosierung;

    private int mediverid;

    private IMedikament medikament;

    private IPatient patient;

    public DMedikamentVerordnung(Date mediVerBis, int mediVerMenge, Date mediVerVon, String mediverdosierung, int mediverid, IPatient patient, IMedikament medikament) {
        super();
        this.mediVerBis = mediVerBis;
        this.mediVerMenge = mediVerMenge;
        this.mediVerVon = mediVerVon;
        this.mediverdosierung = mediverdosierung;
        this.mediverid = mediverid;
        this.patient = patient;
        this.medikament = medikament;
    }

    /**
	 * Constructor with no references.
	 * 
	 * @param mediVerBis
	 * @param mediVerMenge
	 * @param mediVerVon
	 * @param mediverdosierung
	 * @param mediverid
	 * @param behandlung
	 * @param medikament
	 */
    public DMedikamentVerordnung(Date mediVerBis, int mediVerMenge, Date mediVerVon, String mediverdosierung, int mediverid) {
        super();
        this.mediVerBis = mediVerBis;
        this.mediVerMenge = mediVerMenge;
        this.mediVerVon = mediVerVon;
        this.mediverdosierung = mediverdosierung;
        this.mediverid = mediverid;
    }

    public DMedikamentVerordnung(DPatient patient) {
        this.patient = patient;
    }

    public Date getMediVerBis() {
        return mediVerBis;
    }

    public void setMediVerBis(Date mediVerBis) {
        this.mediVerBis = mediVerBis;
    }

    public int getMediVerMenge() {
        return mediVerMenge;
    }

    public void setMediVerMenge(int mediVerMenge) {
        this.mediVerMenge = mediVerMenge;
    }

    public Date getMediVerVon() {
        return mediVerVon;
    }

    public void setMediVerVon(Date mediVerVon) {
        this.mediVerVon = mediVerVon;
    }

    public String getMediverdosierung() {
        return mediverdosierung;
    }

    public void setMediverdosierung(String mediverdosierung) {
        this.mediverdosierung = mediverdosierung;
    }

    public int getMediverid() {
        return mediverid;
    }

    public void setMediverid(int mediverid) {
        this.mediverid = mediverid;
    }

    public IPatient getiPatient() {
        return patient;
    }

    public void setiPatient(IPatient patient) {
        this.patient = patient;
    }

    public IMedikament getiMedikament() {
        return medikament;
    }

    public void setiMedikament(IMedikament medikament) {
        this.medikament = medikament;
    }
}
