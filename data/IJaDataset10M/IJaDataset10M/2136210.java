package ocumed.applikation.terminverwaltung;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import ocumed.applikation.domainhandling.BenutzerErzeugung;
import ocumed.applikation.domainhandling.DomainHandler;
import ocumed.applikation.domainhandling.PatientenErzeugung;
import ocumed.applikation.domainhandling.TerminErzeugung;
import ocumed.applikation.exceptions.BenutzerNotFoundException;
import ocumed.applikation.exceptions.NoArztException;
import ocumed.applikation.exceptions.NoFehlzeitException;
import ocumed.applikation.exceptions.NoOrdZeitenException;
import ocumed.applikation.exceptions.NoPatientException;
import ocumed.applikation.exceptions.WrongDayException;
import ocumed.domaene.benutzerverwaltung.arztverwaltung.DArzt;
import ocumed.domaene.benutzerverwaltung.arztverwaltung.DOrdinationsZeit;
import ocumed.domaene.patientenverwaltung.DPatient;
import ocumed.domaene.terminverwaltung.DTermin;
import ocumed.domaene.terminverwaltung.DTerminArt;
import ocumed.persistenz.dao.BenutzerDAO;
import ocumed.persistenz.dao.DAOFactory;
import ocumed.persistenz.dao.FehlzeitDAO;
import ocumed.persistenz.dao.GruppeDAO;
import ocumed.persistenz.dao.LandDAO;
import ocumed.persistenz.dao.OrdinationDAO;
import ocumed.persistenz.dao.OrtDAO;
import ocumed.persistenz.dao.PatientDAO;
import ocumed.persistenz.dao.TerminDAO;
import ocumed.persistenz.dao.TerminartDAO;
import ocumed.persistenz.hibernate.OcBenutzer;
import ocumed.persistenz.hibernate.OcFehlzeit;
import ocumed.persistenz.hibernate.OcPatient;
import ocumed.persistenz.hibernate.OcTerminart;
import ocumed.teams.IOrdination;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TerminVergeben {

    private static final Log log = LogFactory.getLog(TerminVergeben.class);

    private DomainHandler dh;

    private TerminDAO m_terminDAO;

    private BenutzerDAO m_benutzerDAO;

    private TerminartDAO m_terminArtDAO;

    private PatientDAO m_patientDAO;

    private OrdinationDAO m_ordinationDAO;

    private GruppeDAO m_gruppeDAO;

    private LandDAO m_landDAO;

    private OrtDAO m_ortDAO;

    public TerminVergeben() {
        dh = DomainHandler.getInstance();
        m_terminDAO = DAOFactory.get(DAOFactory.RDBMS).getTerminDAO();
        m_terminArtDAO = DAOFactory.get(DAOFactory.RDBMS).getTerminartDAO();
        m_benutzerDAO = DAOFactory.get(DAOFactory.RDBMS).getBenutzerDAO();
        m_patientDAO = DAOFactory.get(DAOFactory.RDBMS).getPatientDAO();
        m_ordinationDAO = DAOFactory.get(DAOFactory.RDBMS).getOrdinationDAO();
        m_gruppeDAO = DAOFactory.get(DAOFactory.RDBMS).getGruppeDAO();
        m_landDAO = DAOFactory.get(DAOFactory.RDBMS).getLandDAO();
        m_ortDAO = DAOFactory.get(DAOFactory.RDBMS).getOrtDAO();
    }

    /**
	 * Gets pretreatment information about an appointment.
	 * 
	 * @param terminId
	 *            The appointment to get the information from.
	 * @return true, if the appointment needs pretreatment, false otherwise.
	 */
    public boolean getTerminVorbehandlung(int terminId) {
        try {
            return DomainHandler.te.getDTermin(terminId).getTerminmitvorbehandlung();
        } catch (NoArztException e) {
            e.printStackTrace();
        } catch (BenutzerNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Integer getPatientId(int terminId) {
        return DomainHandler.te.getDTermin(terminId).getPatient().getPatientid();
    }

    public String getVersicherungsnummer(int patientId) {
        try {
            return DomainHandler.pe.getDPatient(patientId).getPatientsvn();
        } catch (NoArztException e) {
            e.printStackTrace();
        } catch (BenutzerNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Gets a list of all doctors registered in the system.
	 * 
	 * @return HashMap containing all doctors IDs and last names.
	 */
    public HashMap<Integer, String> getArztListe() {
        log.debug("gui request: getArztListe()");
        m_benutzerDAO.start();
        List<OcBenutzer> arztListe = m_benutzerDAO.findByGroup(BenutzerDAO.typArzt);
        log.debug("aerzte count:" + arztListe.size());
        HashMap<Integer, String> aerzte = new HashMap<Integer, String>();
        for (OcBenutzer a : arztListe) {
            aerzte.put(a.getBenutzerid(), a.getBenutzernachname());
        }
        return aerzte;
    }

    /**
	 * Gets a list of all appointment types.
	 * 
	 * @return HashMap containing the ID and the name of the appointment type.
	 */
    public HashMap<Integer, String> getTerminArten() {
        m_terminArtDAO.start();
        List<OcTerminart> terminArtListe = m_terminArtDAO.findAll();
        ListIterator<OcTerminart> iter = terminArtListe.listIterator();
        HashMap<Integer, String> terminArten = new HashMap<Integer, String>();
        OcTerminart currentTA = new OcTerminart();
        while (iter.hasNext()) {
            currentTA = iter.next();
            terminArten.put(currentTA.getTerminartid(), currentTA.getTerminartbezeichnung());
        }
        return terminArten;
    }

    /**
	 * Gets a list of all patients and returns their id and last name.
	 * 
	 * @return HashMap containing all patients id and last name.
	 */
    public HashMap<Integer, String> getPatientenListe() throws NoPatientException {
        m_patientDAO.start();
        List<OcPatient> patListe = m_patientDAO.findAll();
        if (patListe.size() == 0) {
            throw new NoPatientException();
        }
        ListIterator<OcPatient> iter = patListe.listIterator();
        HashMap<Integer, String> patienten = new HashMap<Integer, String>();
        OcPatient currentPatient = new OcPatient();
        while (iter.hasNext()) {
            currentPatient = iter.next();
            String name = currentPatient.getPatientvorname() + " " + currentPatient.getPatientnachname();
            patienten.put(currentPatient.getPatientid(), name);
        }
        return patienten;
    }

    /**
	 * Gets the patients appointed doctors id.
	 * 
	 * @param patientId
	 *            The patient who's doctors id should be returned.
	 * 
	 * @return the user id of the patients doctor.
	 */
    public int getArztId(int patientId) {
        int id = 0;
        try {
            id = DomainHandler.pe.getDPatient(patientId).getiBenutzerByArztid().getBenutzerid();
        } catch (NoArztException e) {
            e.printStackTrace();
        } catch (BenutzerNotFoundException e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
	 * Gets the specified doctors work times for the specified weekday.
	 * 
	 * @param arztId
	 *            the doctor, whose working times are required.
	 * @param tag
	 *            the weekday for which to get the working times, in the form
	 *            "mo", "di" and so on.
	 * @return a HashMap containing the working times for the specified weekday
	 *         (HashMap<Date, Date>), or null, if no work times were found for
	 *         that day.
	 * @throws WrongDayException,
	 *             if the format of "tag" is not as specified above.
	 * @throws NoOrdZeitenException
	 * @throws NoArztException
	 * @throws BenutzerNotFoundException
	 */
    public HashMap<Date, Date> getOrdinationszeiten(int arztId, String tag) throws WrongDayException, NoOrdZeitenException, NoArztException, BenutzerNotFoundException {
        log.debug("gui request: getOrdinationszeiten() for " + arztId + ", " + tag);
        HashMap<Date, Date> times = new HashMap<Date, Date>();
        DArzt arzt = DomainHandler.be.getDArzt(arztId);
        Vector<DOrdinationsZeit> ordZeiten = arzt.getOrdZeiten(tag);
        log.debug("ordzeiten count for :" + arzt.getBenutzerid() + " " + ordZeiten.size());
        for (IOrdination oz : ordZeiten) {
            times.put(oz.getAnfang().getTime(), oz.getEnde().getTime());
        }
        return times;
    }

    /**
	 * Gets all times of absence for a specific doctor on a specific day.
	 * 
	 * @param arztId
	 *            The doctor, who's times of absence are required.
	 * @param tag
	 *            The day, for which the times of absence must be checked.
	 * @return the times of absence of the doctor on the specified day.
	 * @throws NoFehlzeitException
	 */
    public HashMap<Date, Date> getFehlzeiten(int arztId, Date tag) throws NoFehlzeitException {
        FehlzeitDAO fDAO = DAOFactory.get(DAOFactory.RDBMS).getFehlzeitDAO();
        fDAO.start();
        m_benutzerDAO.start();
        OcBenutzer ocArzt = m_benutzerDAO.findById(arztId);
        Calendar anfang = Calendar.getInstance();
        Calendar ende = Calendar.getInstance();
        Calendar t = Calendar.getInstance();
        t.setTime(tag);
        t.clear(Calendar.SECOND);
        t.clear(Calendar.MILLISECOND);
        t.set(Calendar.HOUR_OF_DAY, 0);
        t.set(Calendar.MINUTE, 0);
        List<OcFehlzeit> fz = fDAO.findByBenutzer(ocArzt.getBenutzerid(), t);
        HashMap<Date, Date> fehlZeiten = new HashMap<Date, Date>();
        Iterator<OcFehlzeit> iter = fz.iterator();
        OcFehlzeit cfz = new OcFehlzeit();
        if (fz != null) {
            while (iter.hasNext()) {
                cfz = iter.next();
                anfang = cfz.getFehlzeitvon();
                ende = cfz.getFehlzeitbis();
                if (anfang.before(t)) {
                    anfang.set(t.get(Calendar.YEAR), t.get(Calendar.MONTH), t.get(Calendar.DAY_OF_MONTH), 0, 0);
                }
                t.add(Calendar.DAY_OF_MONTH, 1);
                if (ende.after(t)) {
                    ende.set(t.get(Calendar.YEAR), t.get(Calendar.MONTH), t.get(Calendar.DAY_OF_MONTH), 0, 0);
                }
                fehlZeiten.put(anfang.getTime(), ende.getTime());
            }
        } else {
            throw new NoFehlzeitException();
        }
        return fehlZeiten;
    }

    /**
	 * Gets all appointments of the specified doctor on the specified day.
	 * 
	 * @param arztId
	 *            The doctor who's appointments to get.
	 * @param tag
	 *            The day for which to get the appointments.
	 * @return The appointments of the doctor on the day.
	 */
    public HashMap<Integer, String> getTermine(int arztId, Date tag) {
        log.debug("gui request: getTermine() for " + arztId + ", " + tag);
        HashMap<Integer, String> terminListe = new HashMap<Integer, String>();
        Calendar calTag = Calendar.getInstance();
        calTag.setTime(tag);
        DArzt arzt = null;
        try {
            arzt = DomainHandler.be.getDArzt(arztId);
        } catch (NoArztException e) {
            e.printStackTrace();
        } catch (BenutzerNotFoundException e) {
            e.printStackTrace();
        }
        Vector<DTermin> termine = arzt.getKalender().getTermine(calTag);
        log.debug("termine count: " + termine.size());
        for (DTermin t : termine) {
            terminListe.put(t.getTerminid(), t.getPatient().getPatientvorname().substring(0, 1) + ". " + t.getPatient().getPatientnachname());
        }
        return terminListe;
    }

    /**
	 * Gets the start time of the specified appointment.
	 * 
	 * @param terminId
	 *            The id of the appointment.
	 * @return start time of the appointment.
	 */
    public Date getTerminAnfang(int terminId) {
        try {
            return DomainHandler.te.getDTermin(terminId).getTerminZeitpunkt().getTime();
        } catch (NoArztException e) {
            e.printStackTrace();
        } catch (BenutzerNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Gets the comment for an appointment.
	 */
    public String getTerminAnmerkung(int terminId) {
        try {
            return DomainHandler.te.getDTermin(terminId).getTermintext();
        } catch (NoArztException e) {
            e.printStackTrace();
        } catch (BenutzerNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Gets the end time of the specified appointment.
	 * 
	 * @param terminId
	 *            The id of the appointment.
	 * @return The end time of the appointment.
	 */
    public Date getTerminEnde(int terminId) {
        try {
            return DomainHandler.te.getDTermin(terminId).getTerminEnde().getTime();
        } catch (NoArztException e) {
            e.printStackTrace();
        } catch (BenutzerNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Gets the specified number of suggestions for an appointment time.
	 * 
	 * @param arztId
	 *            The doctor, to whom this appointment is to be assigned
	 * @param anzahl
	 *            The number of suggestions to get
	 * @param dauer
	 *            The required duration of the appointment
	 * @return 'anzahl' number of appointment suggestions.
	 * @throws NoArztException
	 *             if the specified 'arztId' is not the ID of a doctor.
	 * @throws BenutzerNotFoundException
	 *             if the specified user 'arztId' could not be found.
	 */
    public ArrayList<Date> getTerminVorschlaege(Integer arztId, Integer anzahl, int dauer) throws NoArztException, BenutzerNotFoundException {
        DArzt arzt = DomainHandler.be.getDArzt(arztId);
        return arzt.getTerminVorschlaege(anzahl, dauer);
    }

    /**
	 * Gets the duration of the specified appointment type.
	 * 
	 * @param terminartId
	 *            The id of the appointment type.
	 * @return the duration of this type of appointment.
	 */
    public Integer getTerminArtDauer(int terminartId) {
        return DomainHandler.te.getDTerminArt(terminartId).getDauer();
    }

    public String getPatientVorname(Integer patientId) {
        try {
            return DomainHandler.pe.getDPatient(patientId).getPatientvorname();
        } catch (NoArztException e) {
            e.printStackTrace();
        } catch (BenutzerNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPatientNachname(Integer patientId) {
        try {
            return DomainHandler.pe.getDPatient(patientId).getPatientnachname();
        } catch (NoArztException e) {
            e.printStackTrace();
        } catch (BenutzerNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Creates a new termin data object and persists it to the database.
	 * 
	 * @param patientId
	 *            ID of the patient who's appointment this is.
	 * @param arztId
	 *            ID of the doctor to who'm this appointment is assigned.
	 * @param terminArt
	 *            ID of the appointment type.
	 * @param terminDauer
	 *            Duration of the appointment.
	 * @param zeitpunkt
	 *            Start time of the appointment.
	 * @param mitVorbehandlung
	 *            true -> pre-treatment necessary, false -> no pre-treatment.
	 * @param anmerkung
	 *            Comment for the appointment.
	 * 
	 * @return true if appointment could be stored
	 */
    public Integer neuerTermin(Integer patientId, Integer arztId, Integer terminArt, Integer terminDauer, Date zeitpunkt, Boolean mitVorbehandlung, String anmerkung) {
        DTerminArt ta = DomainHandler.te.getDTerminArt(terminArt);
        DArzt arzt = null;
        DPatient p = null;
        try {
            arzt = DomainHandler.be.getDArzt(arztId);
            p = DomainHandler.pe.getDPatient(patientId);
        } catch (NoArztException e) {
            e.printStackTrace();
        } catch (BenutzerNotFoundException e) {
            e.printStackTrace();
        }
        Calendar zeit = Calendar.getInstance();
        zeit.setTime(zeitpunkt);
        char mitVorbeh = 0;
        if (mitVorbehandlung) {
            mitVorbeh = TerminDAO.mitVorbehandlung;
        } else {
            mitVorbeh = TerminDAO.ohneVorbehandlung;
        }
        DTermin termin = new DTermin(arzt, ta, p, zeit, terminDauer, mitVorbeh, TerminDAO.terminNichtWahrgenommen, anmerkung);
        Integer ret = null;
        if (DomainHandler.te.persist(termin)) {
            ret = termin.getTerminid();
        }
        return ret;
    }
}
