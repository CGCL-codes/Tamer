package uk.ac.ebi.intact.application.hierarchView.struts;

import uk.ac.ebi.intact.application.hierarchView.business.PropertyLoader;
import uk.ac.ebi.intact.application.hierarchView.business.graph.*;
import uk.ac.ebi.intact.application.hierarchView.business.image.*;
import uk.ac.ebi.intact.application.hierarchView.business.*;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

/**
 * Implementation of <strong>Action</strong> that validates a visualize submisson.
 *
 * @author Samuel Kerrien
 * @version 
 */
public final class VisualizeAction extends Action {

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Locale locale = getLocale(request);
        MessageResources messages = getResources();
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        String AC = null;
        String depth = null;
        boolean hasNoDepthLimit = false;
        String methodLabel = null;
        String methodClass = null;
        String behaviourDefault = null;
        if (null != form) {
            AC = ((VisualizeForm) form).getAC();
            depth = ((VisualizeForm) form).getDepth();
            hasNoDepthLimit = ((VisualizeForm) form).getHasNoDepthLimit();
            methodLabel = ((VisualizeForm) form).getMethod();
            Properties properties = PropertyLoader.load(Constants.PROPERTY_FILE);
            if (null != properties) {
                methodClass = properties.getProperty("highlightment.source." + methodLabel + ".class");
                behaviourDefault = properties.getProperty("highlighting.behaviour.default.class");
            }
        }
        form = new VisualizeForm();
        VisualizeForm visualizeForm = (VisualizeForm) form;
        boolean value = (new Boolean(hasNoDepthLimit)).booleanValue();
        visualizeForm.setHasNoDepthLimit(value);
        if ("request".equals(mapping.getScope())) request.setAttribute(mapping.getAttribute(), form); else session.setAttribute(mapping.getAttribute(), form);
        if (!errors.empty()) {
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        } else {
            session.setAttribute(Constants.ATTRIBUTE_AC, AC);
            session.setAttribute(Constants.ATTRIBUTE_DEPTH, depth);
            session.setAttribute(Constants.ATTRIBUTE_NO_DEPTH_LIMIT, new Boolean(hasNoDepthLimit));
            session.setAttribute(Constants.ATTRIBUTE_METHOD_LABEL, methodLabel);
            session.setAttribute(Constants.ATTRIBUTE_METHOD_CLASS, methodClass);
            session.setAttribute(Constants.ATTRIBUTE_BEHAVIOUR, behaviourDefault);
            int depthInt = 0;
            InteractionNetwork in = null;
            try {
                GraphHelper gh = new GraphHelper();
                depthInt = Integer.parseInt(depth);
                in = (InteractionNetwork) gh.getInteractionNetwork(AC, depthInt);
            } catch (Exception e) {
                errors.add("InteractionNetwork", new ActionError("error.interactionNetwork.notCreated"));
                saveErrors(request, errors);
                return (mapping.findForward("error"));
            }
            if (null == in) {
                errors.add("InteractionNetwork", new ActionError("error.interactionNetwork.notCreated"));
            } else {
                if (0 == in.sizeNodes()) {
                    errors.add("InteractionNetwork", new ActionError("error.interactionNetwork.noProteinFound"));
                }
            }
            if (!errors.empty()) {
                saveErrors(request, errors);
                return (mapping.findForward("error"));
            }
            String dataTlp = in.exportTlp();
            System.out.println("\n\n\n\n\n\n toto \n" + dataTlp + "\n\n\n\n\n\n");
            in.importDataToImage(dataTlp);
            GraphToImage te = new GraphToImage(in);
            te.draw();
            ImageBean ib = te.getImageBean();
            if (null == ib) {
                errors.add("ImageBean", new ActionError("error.ImageBean.build"));
                saveErrors(request, errors);
                return (mapping.findForward("error"));
            }
            session.setAttribute(Constants.ATTRIBUTE_IMAGE_BEAN, ib);
            session.setAttribute(Constants.ATTRIBUTE_GRAPH, in);
        }
        if (servlet.getDebug() >= 1) servlet.log(" Populating form from " + visualizeForm);
        if (servlet.getDebug() >= 1) servlet.log("VisualizeAction: AC=" + AC + " depth=" + depth + " noDepthLimit=" + hasNoDepthLimit + " methodLabel=" + methodLabel + " methodClass=" + methodClass + "\nlogged on in session " + session.getId());
        return (mapping.findForward("success"));
    }
}
