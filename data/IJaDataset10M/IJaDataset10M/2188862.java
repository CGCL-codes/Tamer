package ocumed.persistenz.dao;

/**
 * abstract factory pattern for DAO
 */
public abstract class DAOFactory {

    /**
     * Using a RDBMS Technology
     */
    public static final int RDBMS = 1;

    /**
     * @return A Data Access Object
     */
    public abstract AnamneseDAO getAnamneseDAO();

    /**
     * @return A Data Access Object
     */
    public abstract BehandlungDAO getBehandlungDAO();

    /**
     * @return A Data Access Object
     */
    public abstract BenutzerDAO getBenutzerDAO();

    /**
     * @return A Data Access Object
     */
    public abstract BrillenverDAO getBrillenverDAO();

    /**
     * @return A Data Access Object
     */
    public abstract FehlzeitDAO getFehlzeitDAO();

    /**
     * @return A Data Access Object
     */
    public abstract GruppeDAO getGruppeDAO();

    /**
     * @return A Data Access Object
     */
    public abstract HauptversDAO getHauptversDAO();

    /**
     * @return A Data Access Object
     */
    public abstract InstitutDAO getInstitutDAO();

    /**
     * @return A Data Access Object
     */
    public abstract LandDAO getLandDAO();

    /**
     * @return A Data Access Object
     */
    public abstract LeistungDAO getLeistungDAO();

    /**
     * @return A Data Access Object
     */
    public abstract MedikamentDAO getMedikamentDAO();

    /**
     * @return A Data Access Object
     */
    public abstract MediverDAO getMediverDAO();

    /**
     * @return A Data Access Object
     */
    public abstract OrdinationDAO getOrdinationDAO();

    /**
     * @return A Data Access Object
     */
    public abstract OrtDAO getOrtDAO();

    /**
     * @return A Data Access Object
     */
    public abstract PatientDAO getPatientDAO();

    /**
     * @return A Data Access Object
     */
    public abstract PatientloginDAO getPatientloginDAO();

    /**
     * @return A Data Access Object
     */
    public abstract PositionDAO getPositionDAO();

    /**
     * @return A Data Access Object
     */
    public abstract RechnungDAO getRechnungDAO();

    /**
     * @return A Data Access Object
     */
    public abstract RechtDAO getRechtDAO();

    /**
     * @return A Data Access Object
     */
    public abstract RezeptDAO getRezeptDAO();

    /**
     * @return A Data Access Object
     */
    public abstract TerminDAO getTerminDAO();

    /**
     * @return A Data Access Object
     */
    public abstract TerminartDAO getTerminartDAO();

    /**
     * @return A Data Access Object
     */
    public abstract UeberweisungDAO getUeberweisungDAO();

    /**
     * @return A Data Access Object
     */
    public abstract VertretungDAO getVertretungDAO();

    /**
     * @return A Data Access Object
     */
    public abstract WartepositionDAO getWartepositionDAO();

    /**
     * @return A Data Access Object
     */
    public abstract ZeitbestaetigungDAO getZeitbestaetigungDAO();

    /**
     * return a factory for a given type
     * 
     * @param type 
     * @return
     */
    public static DAOFactory get(int type) {
        switch(type) {
            default:
            case RDBMS:
                return new DAOFactoryRDBMS();
        }
    }
}
