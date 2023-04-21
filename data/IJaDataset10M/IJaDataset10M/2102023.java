package it.webscience.kpeople.web.portlet.activities.action;

import it.webscience.kpeople.event.login.KpeopleUserUtil;
import it.webscience.kpeople.service.activity.ActivityServiceStub;
import it.webscience.kpeople.service.activity.ActivityServiceStub.Activity;
import it.webscience.kpeople.service.activity.ActivityServiceStub.Pattern;
import it.webscience.kpeople.service.activity.ActivityServiceStub.PatternState;
import it.webscience.kpeople.util.PropsKeys;
import it.webscience.kpeople.util.StubUtil;
import it.webscience.kpeople.web.portlet.activities.util.ActivitiesBrowserConstants;
import com.liferay.portal.kernel.util.Constants;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * Struts action per la ricerca dei processi.
 */
public class ViewAcceptRejectActivity extends Action {

    /** logger. */
    private static Log logger = LogFactoryUtil.getLog(ViewAcceptRejectActivity.class);

    /**
     * <p>
     * Process the specified non-HTTP request, and create the corresponding
     * non-HTTP response (or forward to another web component that will create
     * it), with provision for handling exceptions thrown by the business logic.
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request (if any)
     * @param req
     *            The non-HTTP request we are processing
     * @param res
     *            The non-HTTP response we are creating
     * @exception Exception
     *                if the application business logic throws an exception.
     * @return action forward
     */
    @Override
    public final ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return acceptRejectActivity(mapping, form, req, res);
    }

    /**
     * Restituisce la view della portlet.
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request (if any)
     * @param req
     *            The non-HTTP request we are processing
     * @param res
     *            The non-HTTP response we are creating
     * @return action forward
     * @throws Exception
     *             if the application business logic throws an exception.
     */
    private ActionForward acceptRejectActivity(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("viewActivityState - begin");
        }
        String redirectUrl = ParamUtil.getString(req, ActivitiesBrowserConstants.REDIRECT_URL);
        req.setAttribute(ActivitiesBrowserConstants.REDIRECT_URL, redirectUrl);
        String commandValue = ParamUtil.getString(req, Constants.CMD);
        if (commandValue.equalsIgnoreCase(ActivitiesBrowserConstants.ACTIVITY_ACCEPTED)) {
            return acceptActivity(mapping, form, req, res);
        }
        if (commandValue.equalsIgnoreCase(ActivitiesBrowserConstants.ACTIVITY_REJECTED)) {
            return rejectActivity(mapping, form, req, res);
        }
        if (commandValue.equalsIgnoreCase(ActivitiesBrowserConstants.ACTIVITY_REJECTED_CONFIRM)) {
            return confirmRejectActivity(mapping, form, req, res);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("viewActivityState - end");
        }
        return null;
    }

    /**
     * Restituisce la view della portlet.
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request (if any)
     * @param req
     *            The non-HTTP request we are processing
     * @param res
     *            The non-HTTP response we are creating
     * @return action forward
     * @throws Exception
     *             if the application business logic throws an exception.
     */
    private ActionForward acceptActivity(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("acceptActivity - begin");
        }
        PortletRequest aReq = (PortletRequest) req.getAttribute(JavaConstants.JAVAX_PORTLET_REQUEST);
        Pattern currPattern = (Pattern) aReq.getPortletSession().getAttribute(ActivitiesBrowserConstants.CURR_PATTERN);
        Activity currActivity = (Activity) aReq.getPortletSession().getAttribute(ActivitiesBrowserConstants.CURR_ACTIVITY);
        PatternState pState = null;
        if (currPattern.getPatternState() != null) {
            pState = currPattern.getPatternState();
        } else {
            pState = new PatternState();
        }
        pState.setIdPatternState(ActivitiesBrowserConstants.RICHIESTA_CONTRIBUTO_STATE_ACCETTATA);
        currPattern.setPatternState(pState);
        ActivityServiceStub.User psUser = new ActivityServiceStub.User();
        psUser.setIdUser(KpeopleUserUtil.getCurrUserId(req));
        psUser.setHpmUserId(KpeopleUserUtil.getCurrUserHpmId(req));
        ActivityServiceStub.ActivityMetadata[] arrayMetadata = new ActivityServiceStub.ActivityMetadata[2];
        ActivityServiceStub.ActivityMetadata metadata1 = new ActivityServiceStub.ActivityMetadata();
        metadata1.setKeyname(ActivitiesBrowserConstants.CONTRIBUTE_ACCEPTED);
        metadata1.setValue("true");
        metadata1.setActivitiProcessMetadata(true);
        arrayMetadata[0] = metadata1;
        ActivityServiceStub.ActivityMetadata metadata2 = new ActivityServiceStub.ActivityMetadata();
        metadata2.setKeyname(ActivitiesBrowserConstants.CONTRIBUTE_ACCEPTED_TYPE);
        metadata2.setValue("Boolean");
        metadata2.setActivitiProcessMetadata(true);
        arrayMetadata[1] = metadata2;
        currActivity.setActivityMetadata(arrayMetadata);
        currActivity.setPattern(currPattern);
        currActivity.setActivityType(currActivity.getActivityType());
        String targetEndpoint = new StubUtil().buildTarget(PropsKeys.WSO2SERVER_SERVICE_ACTIVITY_ENDPOINT);
        ActivityServiceStub stub = new ActivityServiceStub(targetEndpoint);
        ActivityServiceStub.ExecuteActivity excuteActivities = new ActivityServiceStub.ExecuteActivity();
        excuteActivities.setPLoggedUser(psUser);
        excuteActivities.setPActivity(currActivity);
        ActivityServiceStub.ExecuteActivityResponse response = stub.executeActivity(excuteActivities);
        Activity activity = response.get_return();
        aReq.setAttribute(ActivitiesBrowserConstants.CURR_ACTIVITY, currActivity);
        if (logger.isDebugEnabled()) {
            logger.debug("acceptActivity - end");
        }
        return mapping.findForward("/activities-browser/activity-state/accept");
    }

    /**
     * Restituisce la view della portlet.
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request (if any)
     * @param req
     *            The non-HTTP request we are processing
     * @param res
     *            The non-HTTP response we are creating
     * @return action forward
     * @throws Exception
     *             if the application business logic throws an exception.
     */
    private ActionForward rejectActivity(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("rejectActivity - begin");
        }
        PortletRequest aReq = (PortletRequest) req.getAttribute(JavaConstants.JAVAX_PORTLET_REQUEST);
        Activity currActivity = (Activity) aReq.getPortletSession().getAttribute(ActivitiesBrowserConstants.CURR_ACTIVITY);
        aReq.setAttribute(ActivitiesBrowserConstants.CURR_ACTIVITY, currActivity);
        if (logger.isDebugEnabled()) {
            logger.debug("rejectActivity - end");
        }
        return mapping.findForward("/activities-browser/activity-state/reject");
    }

    /**
     * Restituisce la view della portlet.
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request (if any)
     * @param req
     *            The non-HTTP request we are processing
     * @param res
     *            The non-HTTP response we are creating
     * @return action forward
     * @throws Exception
     *             if the application business logic throws an exception.
     */
    private ActionForward confirmRejectActivity(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("rejectActivity - begin");
        }
        PortletRequest aReq = (PortletRequest) req.getAttribute(JavaConstants.JAVAX_PORTLET_REQUEST);
        Pattern currPattern = (Pattern) aReq.getPortletSession().getAttribute(ActivitiesBrowserConstants.CURR_PATTERN);
        Activity currActivity = (Activity) aReq.getPortletSession().getAttribute(ActivitiesBrowserConstants.CURR_ACTIVITY);
        PatternState pState = null;
        if (currPattern.getPatternState() != null) {
            pState = currPattern.getPatternState();
        } else {
            pState = new PatternState();
        }
        pState.setIdPatternState(ActivitiesBrowserConstants.RICHIESTA_CONTRIBUTO_STATE_RIFIUTATA);
        currPattern.setPatternState(pState);
        ActivityServiceStub.User psUser = new ActivityServiceStub.User();
        psUser.setIdUser(KpeopleUserUtil.getCurrUserId(req));
        psUser.setHpmUserId(KpeopleUserUtil.getCurrUserHpmId(req));
        ActivityServiceStub.ActivityMetadata[] arrayMetadata = new ActivityServiceStub.ActivityMetadata[3];
        ActivityServiceStub.ActivityMetadata metadata1 = new ActivityServiceStub.ActivityMetadata();
        metadata1.setKeyname(ActivitiesBrowserConstants.CONTRIBUTE_ACCEPTED);
        metadata1.setValue("false");
        metadata1.setActivitiProcessMetadata(true);
        arrayMetadata[0] = metadata1;
        ActivityServiceStub.ActivityMetadata metadata2 = new ActivityServiceStub.ActivityMetadata();
        metadata2.setKeyname(ActivitiesBrowserConstants.CONTRIBUTE_ACCEPTED_TYPE);
        metadata2.setValue("Boolean");
        metadata2.setActivitiProcessMetadata(true);
        arrayMetadata[1] = metadata2;
        ActivityServiceStub.ActivityMetadata metadata3 = new ActivityServiceStub.ActivityMetadata();
        metadata3.setKeyname(ActivitiesBrowserConstants.RICHIESTA_CONTRIBUTO_DESCRIZIONE_RIFIUTO);
        metadata3.setValue(req.getParameter("patternDescription"));
        metadata3.setActivitiProcessMetadata(true);
        arrayMetadata[2] = metadata3;
        currActivity.setActivityMetadata(arrayMetadata);
        currActivity.setPattern(currPattern);
        currActivity.setActivityType(currActivity.getActivityType());
        String targetEndpoint = new StubUtil().buildTarget(PropsKeys.WSO2SERVER_SERVICE_ACTIVITY_ENDPOINT);
        ActivityServiceStub stub = new ActivityServiceStub(targetEndpoint);
        ActivityServiceStub.ExecuteActivity excuteActivities = new ActivityServiceStub.ExecuteActivity();
        excuteActivities.setPLoggedUser(psUser);
        excuteActivities.setPActivity(currActivity);
        ActivityServiceStub.ExecuteActivityResponse response = stub.executeActivity(excuteActivities);
        Activity activity = response.get_return();
        aReq.setAttribute(ActivitiesBrowserConstants.CURR_ACTIVITY, currActivity);
        if (logger.isDebugEnabled()) {
            logger.debug("rejectActivity - end");
        }
        String redirectDettaglio = ParamUtil.getString(req, ActivitiesBrowserConstants.REDIRECT_URL);
        res.sendRedirect(redirectDettaglio);
        return null;
    }
}
