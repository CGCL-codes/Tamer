package net.sourceforge.webflowtemplate.appuser.mgr.webflow;

import java.io.Serializable;
import net.sourceforge.webflowtemplate.appuser.bean.wrapper.AppUserListWrapper;
import net.sourceforge.webflowtemplate.appuser.criteria.UserMgrSearchCriteria;
import net.sourceforge.webflowtemplate.appuser.mgr.business.DelegateUserMgrService;
import net.sourceforge.webflowtemplate.utility.exception.NoWorkToDoException;
import net.sourceforge.webflowtemplate.utility.exception.WebflowExceptionHelp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class WebflowDelegateUserMgrServiceImpl extends MultiAction implements Serializable {

    private static final long serialVersionUID = 1;

    private final Logger LOGGER = LoggerFactory.getLogger(WebflowDelegateUserMgrServiceImpl.class);

    public WebflowDelegateUserMgrServiceImpl() {
    }

    public Event enterManageUsers(RequestContext pRequestContext, UsernamePasswordAuthenticationToken pCurrentUser) {
        final String METHOD_NAME = "enterDelegateStandardUsers()";
        final DelegateUserMgrService BUSINESS_SERVICE = new DelegateUserMgrService();
        LOGGER.debug("{} -> called", METHOD_NAME);
        try {
            final MutableAttributeMap FLOW_SCOPE = pRequestContext.getFlowScope();
            final String USERNAME = pCurrentUser.getName();
            final String PASSWORD = pCurrentUser.getCredentials().toString();
            final UserMgrSearchCriteria SEARCH_CRITERIA = new UserMgrSearchCriteria();
            final AppUserListWrapper USER_LIST = new AppUserListWrapper();
            USER_LIST.setUserMgrSearchCriteria(SEARCH_CRITERIA);
            FLOW_SCOPE.put("dataset", USER_LIST);
            FLOW_SCOPE.put("criteria", SEARCH_CRITERIA);
            FLOW_SCOPE.put("dbrlData", BUSINESS_SERVICE.getListRefData(USERNAME, PASSWORD).get("DBRL"));
            return success();
        } catch (DataAccessException dae) {
            LOGGER.error("{} -> exception -> {}", METHOD_NAME, dae.toString());
            WebflowExceptionHelp.manageDataAccessException(dae, pRequestContext.getMessageContext());
        } catch (Exception e) {
            LOGGER.error("{} -> exception -> {}", METHOD_NAME, e.toString());
            WebflowExceptionHelp.manageUnexpectedException(e, pRequestContext.getMessageContext());
        } finally {
            WebflowExceptionHelp.closeResources(BUSINESS_SERVICE, pRequestContext.getMessageContext());
        }
        return error();
    }

    public Event searchUsers(RequestContext pRequestContext, UsernamePasswordAuthenticationToken pCurrentUser, UserMgrSearchCriteria pUserMgrSearchCriteria) {
        final String METHOD_NAME = "searchUsers()";
        final DelegateUserMgrService BUSINESS_SERVICE = new DelegateUserMgrService();
        LOGGER.debug("{} -> called", METHOD_NAME);
        try {
            final MutableAttributeMap FLOW_SCOPE = pRequestContext.getFlowScope();
            final String USERNAME = pCurrentUser.getName();
            final String PASSWORD = pCurrentUser.getCredentials().toString();
            final AppUserListWrapper USER_LIST = BUSINESS_SERVICE.getUsers(USERNAME, PASSWORD, pUserMgrSearchCriteria);
            USER_LIST.setUserMgrSearchCriteria(pUserMgrSearchCriteria);
            FLOW_SCOPE.put("dataset", USER_LIST);
            return success();
        } catch (DataAccessException dae) {
            LOGGER.error("{} -> exception -> {}", METHOD_NAME, dae.toString());
            WebflowExceptionHelp.manageDataAccessException(dae, pRequestContext.getMessageContext());
        } catch (Exception e) {
            LOGGER.error("{} -> exception -> {}", METHOD_NAME, e.toString());
            WebflowExceptionHelp.manageUnexpectedException(e, pRequestContext.getMessageContext());
        } finally {
            WebflowExceptionHelp.closeResources(BUSINESS_SERVICE, pRequestContext.getMessageContext());
        }
        return error();
    }

    public Event updateUsers(RequestContext pRequestContext, UsernamePasswordAuthenticationToken pCurrentUser, AppUserListWrapper pAppUserListWrapper) {
        final String METHOD_NAME = "updateDelegateUsers()";
        final DelegateUserMgrService BUSINESS_SERVICE = new DelegateUserMgrService();
        LOGGER.debug("{} -> called", METHOD_NAME);
        try {
            final String USERNAME = pCurrentUser.getName();
            final String PASSWORD = pCurrentUser.getCredentials().toString();
            BUSINESS_SERVICE.updateUsers(USERNAME, PASSWORD, pAppUserListWrapper);
            BUSINESS_SERVICE.commitWork();
            return success();
        } catch (DataAccessException dae) {
            LOGGER.error("{} -> exception -> {}", METHOD_NAME, dae.toString());
            WebflowExceptionHelp.manageDataAccessException(dae, pRequestContext.getMessageContext());
        } catch (NoWorkToDoException nwtde) {
            WebflowExceptionHelp.manageNoWorkToDoException(nwtde, pRequestContext.getMessageContext());
        } catch (Exception e) {
            LOGGER.error("{} -> exception -> {}", METHOD_NAME, e.toString());
            WebflowExceptionHelp.manageUnexpectedException(e, pRequestContext.getMessageContext());
        } finally {
            WebflowExceptionHelp.closeResources(BUSINESS_SERVICE, pRequestContext.getMessageContext());
        }
        return error();
    }
}
