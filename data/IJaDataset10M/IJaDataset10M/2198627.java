package ispyb.client.results;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.BreadCrumbsForm;
import ispyb.client.util.ClientLogger;
import ispyb.common.util.Constants;
import ispyb.server.data.interfaces.ScreeningFacadeLocal;
import ispyb.server.data.interfaces.ScreeningFacadeUtil;
import ispyb.server.data.interfaces.ScreeningInputLightValue;
import ispyb.server.data.interfaces.ScreeningOutputFacadeLocal;
import ispyb.server.data.interfaces.ScreeningOutputFacadeUtil;
import ispyb.server.data.interfaces.ScreeningOutputLatticeLightValue;
import ispyb.server.data.interfaces.ScreeningOutputLightValue;
import ispyb.server.data.interfaces.ScreeningOutputValue;
import ispyb.server.data.interfaces.ScreeningRankLightValue;
import ispyb.server.data.interfaces.ScreeningStrategyLightValue;
import ispyb.server.data.interfaces.ScreeningValue;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

/**
 * Class to handle DNA screening results from one datacollection
 * 
 * @struts.action name="viewResultsForm" path="/user/viewScreening"
 *                input="user.results.view.page"
 *                validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * @struts.action-forward name="success" path="user.results.viewScreening.page"
 *
 * @struts.action-forward name="viewDNAImages"
 *                        path="user.results.DNAImages.page"
 * 
 * 
 * @web.ejb-local-ref name="ejb/ScreeningFacade" type="Stateless"
 *                    home="ispyb.server.data.interfaces.ScreeningFacadeLocalHome"
 *                    local="ispyb.server.interfaces.ScreeningFacadeLocal"
 *                    link="ScreeningFacade"
 * 
 * @jboss.ejb-local-ref jndi-name="ispyb/ScreeningFacadeLocalHome"
 *                      ref-name="ScreeningFacade"
 */
public class ViewScreeningAction extends DispatchAction {

    ActionMessages errors = new ActionMessages();

    /**
     * To display all the Screening parameters associated to a screening.
     * at the moment we suppose that 1 screening gives :
     * 					1 screeningInput, 1 screeningRank
     * 					1 screeningOuptput, 1 screeningOutputLattice and several screeningStrategys
     * 
     * @param mapping
     * @param actForm
     * @param request
     * @param response
     * @return
     */
    public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
        try {
            String screeningIdst = request.getParameter(Constants.SCREENING_ID);
            Integer screeningId = new Integer(screeningIdst);
            ScreeningInputLightValue silv = new ScreeningInputLightValue();
            ScreeningRankLightValue srlv = new ScreeningRankLightValue();
            ScreeningOutputLightValue solv = new ScreeningOutputLightValue();
            ScreeningStrategyLightValue sslv = new ScreeningStrategyLightValue();
            ScreeningOutputLatticeLightValue sollv = new ScreeningOutputLatticeLightValue();
            ScreeningOutputValue sov = new ScreeningOutputValue();
            ScreeningStrategyLightValue[] screeningStrategyList;
            ClientLogger.getInstance().debug("display screening for id = " + screeningId);
            ScreeningFacadeLocal screening = ScreeningFacadeUtil.getLocalHome().create();
            ScreeningOutputFacadeLocal screeningOutput = ScreeningOutputFacadeUtil.getLocalHome().create();
            ScreeningValue sv = screening.findByPrimaryKey(screeningId);
            if (sv.getScreeningInputs().length > 0) silv = sv.getScreeningInputs()[0];
            if (sv.getScreeningRanks().length > 0) srlv = sv.getScreeningRanks()[0];
            if (sv.getScreeningOutputs().length > 0) {
                solv = sv.getScreeningOutputs()[0];
                sov = screeningOutput.findByPrimaryKey(solv.getPrimaryKey());
            }
            ViewResultsForm form = (ViewResultsForm) actForm;
            if (sov.getScreeningStrategys().length > 0) {
                sslv = sov.getScreeningStrategys()[0];
                screeningStrategyList = sov.getScreeningStrategys();
                form.setScreeningStrategyList(screeningStrategyList);
            }
            if (sov.getScreeningOutputLattices().length > 0) sollv = sov.getScreeningOutputLattices()[0];
            form.setScreeningInput(silv);
            form.setScreeningRank(srlv);
            form.setScreeningOutput(solv);
            form.setScreeningOutputLattice(sollv);
            form.setScreeningStrategy(sslv);
            FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);
            BreadCrumbsForm.getIt(request).setSelectedScreening(sv);
        } catch (Exception e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.screening.view"));
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
            e.printStackTrace();
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        } else return mapping.findForward("success");
    }
}
