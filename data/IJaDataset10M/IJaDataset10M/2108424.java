package edu.ucla.mbi.curator.actions.curator.feature;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import edu.ucla.mbi.curator.webutils.session.SessionManager;
import edu.ucla.mbi.curator.webutils.model.FeatureModel;
import edu.ucla.mbi.xml.MIF.elements.FeatureDescriptionElements.Feature;
import edu.ucla.mbi.xml.MIF.elements.FeatureDescriptionElements.FeatureRange;
import edu.ucla.mbi.xml.MIF.elements.FeatureDescriptionElements.FeatureBuilder;
import edu.ucla.mbi.xml.MIF.elements.interactionElements.Participant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Jan 19, 2006
 * Time: 1:29:43 PM
 */
public class AddRangeForFeature extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionManager sessionManager = SessionManager.getSessionManager(request);
        FeatureModel featureModel = SessionManager.getFeatureModel(mapping, form, request, response);
        Feature feature;
        Participant participant;
        if ((feature = (Feature) request.getAttribute("feature")) == null) feature = featureModel.getFeatureByInternalId(Integer.valueOf(request.getParameter("featureId")));
        participant = (Participant) feature.getParent();
        assert feature != null && participant != null;
        FeatureBuilder featureBuilder = sessionManager.getFileFactory().getFeatureBuilder();
        featureBuilder.setFeature(feature);
        FeatureRange featureRange = featureBuilder.createFeatureRange();
        feature.getFeatureRangeList().add(featureRange);
        return mapping.findForward("success");
    }
}
