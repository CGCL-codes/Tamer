package eu.planets_project.tb.gui;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import eu.planets_project.tb.gui.backing.ExperimentBean;
import eu.planets_project.tb.gui.backing.exp.ExperimentInspector;
import eu.planets_project.tb.gui.util.JSFUtil;

/**
 * TODO This is rather awkward, and should really be handled by the faces-config navigation. But, I don't know how else to deal with the ExperimentBean that is placed in the session manually.
 * eu.planets_project.tb.gui.ExpDesignPhaseListener.afterPhase(ExpDesignPhaseListener.java:37)
 * @author AnJackson
 *
 */
public class ExpDesignPhaseListener implements PhaseListener {

    static final long serialVersionUID = 237213472384324l;

    private static Log log = LogFactory.getLog(ExpDesignPhaseListener.class);

    public void afterPhase(PhaseEvent arg0) {
        FacesContext context = arg0.getFacesContext();
        ExperimentInspector ei = (ExperimentInspector) JSFUtil.getManagedObject("ExperimentInspector");
        ei.getExperimentId();
        ExperimentBean expBean = (ExperimentBean) JSFUtil.getManagedObject("ExperimentBean");
        if (context == null || context.getViewRoot() == null) return;
        String viewId = context.getViewRoot().getViewId();
        if (viewId.startsWith("/exp/exp_stage")) {
            if (!viewId.startsWith("/exp/exp_stage1")) {
                if (expBean != null && expBean.getExperiment() == null) expBean = null;
                if (expBean != null && expBean.getExperiment() != null && expBean.getExperiment().getEntityID() == -1) expBean = null;
            }
            ExpDesignPhaseListener.redirectIfRequired(context, "my_experiments", expBean);
        }
        if (viewId.startsWith("/reader/exp_stage") || viewId.startsWith("/admin/manage_exp") || viewId.startsWith("/admin/exp_delete")) {
            ExpDesignPhaseListener.redirectIfRequired(context, "browse_experiments", expBean);
        }
    }

    private static void redirectIfRequired(FacesContext context, String newView, ExperimentBean expBean) {
        if (expBean == null) {
            log.debug("ExperimentBean == null! Redirecting.");
            context.getApplication().getNavigationHandler().handleNavigation(context, "", newView);
        }
        log.debug("ExperimentBean found.");
    }

    public void beforePhase(PhaseEvent arg0) {
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
