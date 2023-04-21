package ispyb.client.sample;

import ispyb.client.BreadCrumbsForm;
import ispyb.common.util.Constants;
import ispyb.server.data.interfaces.BlSampleFullFacadeLocal;
import ispyb.server.data.interfaces.BlSampleFullFacadeUtil;
import ispyb.server.data.interfaces.BlSampleFullValue;
import ispyb.server.data.interfaces.BlsampleFacadeLocal;
import ispyb.server.data.interfaces.BlsampleFacadeUtil;
import ispyb.server.data.interfaces.BlsampleLightValue;
import ispyb.server.data.interfaces.BlsampleValue;
import ispyb.server.data.interfaces.DiffractionPlanFacadeLocal;
import ispyb.server.data.interfaces.DiffractionPlanFacadeUtil;
import ispyb.server.data.interfaces.DiffractionPlanValue;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @struts.action name="viewSampleForm" path="/user/editDiffractionPlan"
 *                type="ispyb.client.sample.EditDiffractionPlanAction"
 *                input="user.sampleForContainer.create.page" validate="false"
 *                parameter="reqCode" scope="request"
 * 
 * 
 * @struts.action-forward name="sampleForContainerCreatePage" path="user.sample.create.page"
 * 
 * @struts.action-forward name="editSampleForCreate" path="user.sample.create.edit.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * @web.ejb-local-ref name="ejb/BlSampleFullFacade" type="Stateless"
 *                    home="ispyb.server.data.interfaces.BlSampleFullFacadeLocalHome"
 *                    local="ispyb.server.interfaces.BlSampleFullFacadeLocal"
 *                    link="BlSampleFullFacade"
 * 
 * @jboss.ejb-local-ref jndi-name="ispyb/BlSampleFullFacadeLocalHome"
 *                      ref-name="BlSampleFullFacade"
 * 
 *  
 */
public class EditDiffrationPlanAction extends AbstractSampleAction {

    /**
     * 
     * @param mapping
     * @param actForm
     * @param request
     * @param response
     * @return
     */
    public ActionForward saveDifPlan(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
        ActionMessages messages = new ActionMessages();
        ActionMessages mErrors = new ActionMessages();
        String successPage = new String("sampleForContainerCreatePage");
        Integer blsampleId = new Integer(0);
        DiffractionPlanValue newDiffractionPlan;
        try {
            ViewSampleForm form = (ViewSampleForm) actForm;
            DiffractionPlanFacadeLocal diffractionPlan = DiffractionPlanFacadeUtil.getLocalHome().create();
            if (BreadCrumbsForm.getIt(request).getSelectedSample() != null) blsampleId = BreadCrumbsForm.getIt(request).getSelectedSample().getBlSampleId();
            BlsampleFacadeLocal sample = BlsampleFacadeUtil.getLocalHome().create();
            BlsampleLightValue slv = sample.findByPrimaryKeyLight(blsampleId);
            form.setInfo(slv);
            form.setTheCrystalId(slv.getCrystalId());
            Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
            ArrayList crystalInfoList = populateCrystalInfoList(proposalId);
            form.setListCrystal(crystalInfoList);
            BreadCrumbsForm.getIt(request).setSelectedSample(slv);
            if (form.getDifPlanInfo().getDiffractionPlanId() != null && form.getDifPlanInfo().getDiffractionPlanId().intValue() != 0) {
                newDiffractionPlan = form.getDifPlanInfo();
                diffractionPlan.update(newDiffractionPlan);
                if (blsampleId != null && blsampleId.intValue() > 0) {
                    BlSampleFullFacadeLocal blsampleFull = BlSampleFullFacadeUtil.getLocalHome().create();
                    BlSampleFullValue fullValue = blsampleFull.findByPrimaryKey(blsampleId);
                    fullValue.setMinimalResolution(new Double(newDiffractionPlan.getMinimalResolution().doubleValue()));
                    fullValue.setObservedResolution(new Double(newDiffractionPlan.getObservedResolution().doubleValue()));
                }
                form.setDifPlanInfo(newDiffractionPlan);
            } else {
                DiffractionPlanValue newDifPlan = new DiffractionPlanValue();
                newDifPlan = form.getDifPlanInfo();
                newDiffractionPlan = diffractionPlan.create(newDifPlan);
                form.setDifPlanInfo(newDiffractionPlan);
                if (blsampleId.intValue() > 0) {
                    BlsampleFacadeLocal blsample = BlsampleFacadeUtil.getLocalHome().create();
                    BlsampleValue selectedSample = blsample.findByPrimaryKey(blsampleId);
                    selectedSample.setDiffractionPlanId(newDiffractionPlan.getDiffractionPlanId());
                    blsample.update(selectedSample);
                    BlSampleFullFacadeLocal blsampleFull = BlSampleFullFacadeUtil.getLocalHome().create();
                    BlSampleFullValue fullValue = blsampleFull.findByPrimaryKey(blsampleId);
                    fullValue.setDiffractionPlanId(newDiffractionPlan.getDiffractionPlanId());
                }
            }
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.inserted", "Diffraction plan parameters"));
            saveMessages(request, messages);
        } catch (Exception e) {
            mErrors.add(this.HandlesException(e));
        }
        if (!mErrors.isEmpty()) {
            saveErrors(request, mErrors);
            return (mapping.findForward("error"));
        }
        return mapping.findForward(successPage);
    }
}
