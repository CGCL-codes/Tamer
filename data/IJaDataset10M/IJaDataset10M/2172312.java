package uk.icat3.sessionbeans.manager;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import org.apache.log4j.Logger;
import uk.icat3.entity.Parameter;
import uk.icat3.exceptions.SessionException;
import uk.icat3.exceptions.ValidationException;
import uk.icat3.manager.ParameterManager;
import uk.icat3.sessionbeans.ArgumentValidator;
import uk.icat3.sessionbeans.EJBObject;

/**
 * This class implements the stateless session bean for the Parameter management.
 * (Creating, Updating and Deleting)
 * @author Mr. Srikanth Nagella
 */
@Stateless
@Interceptors(ArgumentValidator.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ParameterManagerBean extends EJBObject implements ParameterManagerLocal {

    static Logger log = Logger.getLogger(ParameterManagerBean.class);

    @Override
    public void createParameter(String sessionId, Parameter param) throws ValidationException, SessionException {
        String userId = user.getUserIdFromSessionId(sessionId);
        ParameterManager.createParameter(userId, param, manager);
    }

    @Override
    public Parameter updateParameter(String sessionId, String name, String units, boolean isSearchable, boolean isDatasetParameter, boolean isDatafileParameter, boolean isSampleParameter) throws ValidationException, SessionException {
        String userId = user.getUserIdFromSessionId(sessionId);
        return ParameterManager.updateParameter(userId, name, units, isSearchable, isDatasetParameter, isDatafileParameter, isSampleParameter, manager);
    }

    @Override
    public void removeParameter(String sessionId, String name, String units) throws ValidationException, SessionException {
        String userId = user.getUserIdFromSessionId(sessionId);
        ParameterManager.removeParameter(userId, name, units, manager);
    }
}
