package it.webscience.kpeople.bll.proxy;

import it.webscience.kpeople.be.Pattern;
import it.webscience.kpeople.be.PatternState;
import it.webscience.kpeople.be.PatternType;
import it.webscience.kpeople.be.Process;
import it.webscience.kpeople.be.User;
import it.webscience.kpeople.bll.exception.KPeopleBLLException;
import it.webscience.kpeople.client.activiti.exception.KPeopleActivitiStartPatternException;
import it.webscience.kpeople.dal.exception.KPeopleActivitiException;
import it.webscience.kpeople.dal.exception.KPeopleDAOException;
import it.webscience.kpeople.dal.pattern.IPatternDAO;
import it.webscience.kpeople.dal.pattern.PatternDAO;
import java.sql.SQLException;
import java.util.List;

/**
 * Proxy per la classe PatternDAO.
 */
public class PatternDAOProxy implements IPatternDAO {

    /**
     * @param pIgnoreShowInList
     *            specifica se visualizzare o meno i patternType con il flag
     *            ShowInList su tre.
     * @param pLoggedUser
     *            utente che richiede il servizio.
     * @throws SQLException
     *             eccezioni db
     * @return ritorna l'elenco dei pattern type
     */
    @Override
    public final List<PatternType> getPatternTypes(final boolean pIgnoreShowInList, final User pLoggedUser) throws SQLException {
        IPatternDAO patternDao = new PatternDAO();
        return patternDao.getPatternTypes(pIgnoreShowInList, pLoggedUser);
    }

    /**
     * @param pPattern
     *            pattern che deve essere avviato.
     * @param pUser
     *            utente che richiama il servizio.
     * @param pProcess
     *            processo associato al pattern
     * @throws KPeopleDAOException
     *             eccezioni generata durante il salvataggio del pattern sui
     *             diversi repository.
     * @return ritorna l'elenco dei pattern type
     */
    @Override
    public final Pattern startPattern(final Pattern pPattern, final User pUser, final Process pProcess) throws KPeopleDAOException {
        Pattern pattern = pPattern;
        User user = pUser;
        Process process = pProcess;
        IPatternDAO patternDao = new PatternDAO();
        return patternDao.startPattern(pattern, user, process);
    }

    /**
     * @param pPattern
     *            pattern che deve essere avviato.
     * @param pUser
     *            utente che richiama il servizio.
     * @param pProcess
     *            processo associato al pattern
     * @throws KPeopleDAOException
     *             eccezioni generata durante il salvataggio del pattern sui
     *             diversi repository.
     * @return ritorna l'elenco dei pattern type
     */
    @Override
    public final Pattern patternDetailByHpmPatternId(final Pattern pPattern, final User pUser) throws KPeopleDAOException {
        Pattern pattern = pPattern;
        User user = pUser;
        IPatternDAO patternDao = new PatternDAO();
        return patternDao.patternDetailByHpmPatternId(pattern, user);
    }

    /**
     * 
     */
    @Override
    public Pattern getPatternByHpmPatternId(final String pHpmPatternId) throws KPeopleDAOException {
        String hpmPatternId = pHpmPatternId;
        IPatternDAO patternDao = new PatternDAO();
        return patternDao.getPatternByHpmPatternId(hpmPatternId);
    }

    @Override
    public PatternType getPatternTypeByPatternTypeId(PatternType pPatternType, User pLoggedUser) throws KPeopleDAOException {
        IPatternDAO patternDao = new PatternDAO();
        return patternDao.getPatternTypeByPatternTypeId(pPatternType, pLoggedUser);
    }

    @Override
    public Pattern closePattern(Pattern pPattern, User pLoggedUser) throws KPeopleDAOException {
        IPatternDAO patternDao = new PatternDAO();
        return patternDao.closePattern(pPattern, pLoggedUser);
    }

    @Override
    public PatternState getPatternStateByPatternStateId(PatternState pPatternState, User pLoggedUser) throws KPeopleDAOException {
        IPatternDAO patternDao = new PatternDAO();
        return patternDao.getPatternStateByPatternStateId(pPatternState, pLoggedUser);
    }

    @Override
    public boolean closePatternFromActiviti(final String pActivitiProcessInstanceId, final String pHpmUserId) throws KPeopleDAOException {
        IPatternDAO patternDao = new PatternDAO();
        return patternDao.closePatternFromActiviti(pActivitiProcessInstanceId, pHpmUserId);
    }
}
