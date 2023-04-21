package it.webscience.kpeople.dal.activity;

import it.webscience.kpeople.be.Activity;
import it.webscience.kpeople.be.Pattern;
import it.webscience.kpeople.be.PatternActivityFilter;
import it.webscience.kpeople.be.Process;
import it.webscience.kpeople.be.User;
import it.webscience.kpeople.client.activiti.ActivitiClient;
import it.webscience.kpeople.client.activiti.IActivitiClient;
import it.webscience.kpeople.client.activiti.exception.KPeopleActivitiGetTaskException;
import it.webscience.kpeople.client.activiti.exception.KPeopleActivitiListTaskException;
import it.webscience.kpeople.client.activiti.exception.KPeopleActivitiPerformTaskOperationException;
import it.webscience.kpeople.client.activiti.object.ActivitiTask;
import it.webscience.kpeople.dal.activity.dao.ActivitiUtil;
import it.webscience.kpeople.dal.activity.dao.ActivityDAOUtil;
import it.webscience.kpeople.dal.activity.dao.ActivitySesameUtil;
import it.webscience.kpeople.dal.cross.UserDAO;
import it.webscience.kpeople.dal.exception.KPeopleDAOException;
import it.webscience.kpeople.dal.exception.KPeopleSesameException;
import it.webscience.kpeople.dal.pattern.PatternDAO;
import it.webscience.kpeople.dal.pattern.dao.PatternDAOUtil;
import it.webscience.kpeople.dal.process.ProcessDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.naming.NamingException;
import org.apache.log4j.Logger;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

/**
 * Classe DAO per a BE Activity.
 * @author gnoni
 */
public class ActivityDAO implements IActivityDAO {

    /** logger. */
    private Logger logger;

    /** Costruttore. */
    public ActivityDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    @Override
    public final List<Activity> searchToDoActivities(final PatternActivityFilter pFilter, final User pLoggedUser) throws KPeopleDAOException {
        if (logger.isDebugEnabled()) {
            logger.debug("searchToDoActivities - start");
        }
        PatternActivityFilter filter = pFilter;
        Pattern pattern = null;
        if (!filter.getHpmPatternId().equals("")) {
            PatternDAO paDao = new PatternDAO();
            pattern = paDao.getPatternByHpmPatternId(filter.getHpmPatternId());
        }
        Process process = null;
        if (!filter.getHpmProcessId().equals("")) {
            ProcessDAO prDao = new ProcessDAO();
            try {
                process = prDao.getProcessByHpmId(filter.getHpmProcessId());
            } catch (SQLException e) {
                e.printStackTrace();
                throw new KPeopleDAOException(e.getMessage());
            }
        }
        User user = null;
        if (!filter.getHpmUserId().equals("")) {
            UserDAO usDao = new UserDAO();
            try {
                user = usDao.getUserByHpmUserId(filter.getHpmUserId());
            } catch (NamingException e) {
                e.printStackTrace();
                throw new KPeopleDAOException(e.getMessage());
            }
        }
        if (pattern == null && process == null && user == null) {
            throw new KPeopleDAOException("Filtri di ricerca non validi");
        }
        ActivitiUtil acUtil = new ActivitiUtil();
        List<Activity> newToDoActivities = null;
        try {
            newToDoActivities = acUtil.syncToDoActivities(process, pattern, user);
        } catch (KPeopleActivitiListTaskException e) {
            e.printStackTrace();
            throw new KPeopleDAOException(e.getMessage());
        }
        List<Activity> filteredActivities;
        try {
            filteredActivities = filterToDoActivities(process, pattern, user, newToDoActivities);
        } catch (KPeopleSesameException e) {
            e.printStackTrace();
            throw new KPeopleDAOException(e.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("searchToDoActivities - end");
        }
        Collections.sort(filteredActivities);
        return filteredActivities;
    }

    @Override
    public final List<Activity> searchPendingActivities(final PatternActivityFilter pFilter, final User pLoggedUser) throws SQLException, KPeopleDAOException, NamingException {
        if (logger.isDebugEnabled()) {
            logger.debug("searchPendingActivities - start ");
        }
        PatternActivityFilter filter = pFilter;
        Pattern pattern = null;
        if (!filter.getHpmPatternId().equals("")) {
            PatternDAO paDao = new PatternDAO();
            pattern = paDao.getPatternByHpmPatternId(filter.getHpmPatternId());
        }
        Process process = null;
        if (!filter.getHpmProcessId().equals("")) {
            ProcessDAO prDao = new ProcessDAO();
            try {
                process = prDao.getProcessByHpmId(filter.getHpmProcessId());
            } catch (SQLException e) {
                e.printStackTrace();
                throw new KPeopleDAOException(e.getMessage());
            }
        }
        User user = null;
        if (!filter.getHpmUserId().equals("")) {
            UserDAO usDao = new UserDAO();
            try {
                user = usDao.getUserByHpmUserId(filter.getHpmUserId());
            } catch (NamingException e) {
                e.printStackTrace();
                throw new KPeopleDAOException(e.getMessage());
            }
        }
        if (pattern == null && process == null && user == null) {
            throw new KPeopleDAOException("Filtri di ricerca non validi");
        }
        List<String> usersToCheck = null;
        try {
            usersToCheck = calculateUserToCheckOnActiviti(process, pattern, user);
        } catch (KPeopleActivitiListTaskException e1) {
            e1.printStackTrace();
        } catch (RepositoryException e1) {
            e1.printStackTrace();
        } catch (MalformedQueryException e1) {
            e1.printStackTrace();
        } catch (QueryEvaluationException e1) {
            e1.printStackTrace();
        }
        List<Activity> pendingActivity = new ArrayList<Activity>();
        if (usersToCheck != null) {
            for (int i = 0; i < usersToCheck.size(); i++) {
                PatternActivityFilter pFilterToDo = new PatternActivityFilter();
                pFilterToDo.setHpmUserId(usersToCheck.get(i));
                pFilterToDo.setHpmProcessId(pFilter.getHpmProcessId());
                pFilterToDo.setHpmPatternId(pFilter.getHpmPatternId());
                List<Activity> actToDoForUser = searchToDoActivities(pFilterToDo, user);
                for (int j = 0; j < actToDoForUser.size(); j++) {
                    Activity curAct = actToDoForUser.get(j);
                    if (curAct.getPattern().isWaitingActivity() && curAct.getActivityRequestor().getIdUser() == user.getIdUser()) {
                        pendingActivity.add(curAct);
                    }
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("searchPendingActivities - end ");
        }
        Collections.sort(pendingActivity);
        return pendingActivity;
    }

    /**
     * @param pProcess
     *            processo al quale sono associate le attività
     * @param pPattern
     *            pattern al quale sono associate le attività
     * @param pUser
     *            utente che richiede il servizio
     * @param pActivities
     *            lista di attività da filtrare
     * @return lista delle attività filtrate.
     * @throws KPeopleSesameException
     *             eccezione generata durante l'interrogazione del servizio.
     */
    private List<Activity> filterToDoActivities(final Process pProcess, final Pattern pPattern, final User pUser, final List<Activity> pActivities) throws KPeopleSesameException {
        if (logger.isDebugEnabled()) {
            logger.debug("filterToDoActivities - start ");
        }
        Process process = pProcess;
        Pattern pattern = pPattern;
        User user = pUser;
        List<Activity> activities = pActivities;
        ActivitySesameUtil aSes = new ActivitySesameUtil();
        List<String> hpmPatternIdList = new ArrayList<String>();
        if (process != null) {
            hpmPatternIdList = aSes.getHpmPatternIdFromHpmProjectId(process.getHpmProcessId());
        } else {
            if (pattern != null) {
                hpmPatternIdList.add(pattern.getHpmPatternId());
            }
        }
        List<Activity> filteredActivities = new ArrayList<Activity>();
        if (hpmPatternIdList.size() > 0) {
            for (int i = 0; i < activities.size(); i++) {
                Activity currentActivity = activities.get(i);
                if (hpmPatternIdList.contains(currentActivity.getPattern().getHpmPatternId())) {
                    filteredActivities.add(currentActivity);
                }
            }
        } else if (process == null) {
            filteredActivities = activities;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("filterToDoActivities - end ");
        }
        return filteredActivities;
    }

    private List<String> calculateUserToCheckOnActiviti(final Process pProcess, final Pattern pPattern, final User pUser) throws KPeopleActivitiListTaskException, RepositoryException, MalformedQueryException, QueryEvaluationException, SQLException {
        if (logger.isDebugEnabled()) {
            logger.debug("calculateUserToCheckOnActiviti - start ");
        }
        Process process = pProcess;
        Pattern pattern = pPattern;
        User user = pUser;
        List<String> userToCheckOnActivitiList = new ArrayList<String>();
        ActivitySesameUtil aSes = new ActivitySesameUtil();
        PatternDAOUtil pDaoUtil = new PatternDAOUtil();
        ActivityDAOUtil acDaoUtil = new ActivityDAOUtil();
        if (process != null) {
            if (pattern != null) {
                userToCheckOnActivitiList.add(pattern.getPatternProvider().getHpmUserId());
            } else {
                List<String> listHpmPatternId = new ArrayList<String>();
                try {
                    listHpmPatternId = aSes.getHpmPatternIdFromHpmProjectId(process.getHpmProcessId());
                } catch (KPeopleSesameException e) {
                    e.printStackTrace();
                }
                if (listHpmPatternId.size() != 0) {
                    String patternIdStr = "";
                    List<Integer> listPatternId = pDaoUtil.getPatternIdListFromHpmPatternIdList(listHpmPatternId);
                    patternIdStr = pDaoUtil.createPatternIdStringList(listPatternId);
                    List<String> hpmProvidersId = acDaoUtil.getHpmUsersIdByPatternRoleIdAndPatternIdList(Pattern.PATTERN_ROLE_PROVIDER, patternIdStr);
                    for (int i = 0; i < hpmProvidersId.size(); i++) {
                        if (!userToCheckOnActivitiList.contains(hpmProvidersId.get(i))) userToCheckOnActivitiList.add(hpmProvidersId.get(i));
                    }
                }
            }
        } else {
            if (pattern != null) {
                userToCheckOnActivitiList.add(pattern.getPatternProvider().getHpmUserId());
            } else {
                List<String> ret = acDaoUtil.getHpmProviderIdByRequestorId(user.getIdUser());
                for (int i = 0; i < ret.size(); i++) {
                    userToCheckOnActivitiList.add(ret.get(i));
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("calculateUserToCheckOnActiviti - end ");
        }
        return userToCheckOnActivitiList;
    }

    /**
     * @param pActivity
     * @param pUser
     * @return
     * @throws KPeopleActivitiPerformTaskOperationException
     */
    public Activity executeActivity(final Activity pActivity, final User pUser) throws KPeopleActivitiPerformTaskOperationException {
        if (logger.isDebugEnabled()) {
            logger.debug("executeActivity - start ");
        }
        Activity activity = pActivity;
        User user = pUser;
        ActivitiUtil actDaoUtil = new ActivitiUtil();
        activity = actDaoUtil.executeTask(activity, user);
        if (activity.isClosed()) {
            PatternDAOUtil pDaoUt = new PatternDAOUtil();
            try {
                pDaoUt.updatePatternState(activity.getPattern());
            } catch (SQLException e) {
                throw new KPeopleActivitiPerformTaskOperationException(e.getMessage());
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("executeActivity - end ");
        }
        return activity;
    }

    @Override
    public final Activity getActivityByHpmId(final String hpmActivityId, final User user) {
        ActivitiUtil actDaoUtil = new ActivitiUtil();
        Activity activity = actDaoUtil.getActivityFromActiviti(hpmActivityId, user);
        return activity;
    }
}
