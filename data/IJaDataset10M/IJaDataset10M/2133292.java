package net.sf.myfacessandbox.components.clientsidepane;

import java.net.URL;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import net.sf.myfacessandbox.common.BasePhaseListener;
import net.sf.myfacessandbox.components.scripts.ScriptController;

/**
 * A separate phase listener for handling the tabbedPane Scripting event and to
 * render back the tabbed pane script TODO, we gotta move the basic render
 * script mechanisms into a single, base class the code dup has reached a level
 * where it is justified to do that
 * 
 * 
 * @author Werner Punz werpu@gmx.at
 * 
 */
public class RenderScriptPhaseListener extends BasePhaseListener implements PhaseListener {

    public static final String SCRIPT_RESOURCE_TABBEDPANE = "/net/sf/myfacessandbox/components/clientsidepane/tabbedpane.js";

    public void afterPhase(PhaseEvent event) {
        String rootId = event.getFacesContext().getViewRoot().getViewId();
        if (rootId.endsWith(HtmlTabbedPaneRenderer.TABBEDPANE_VIEW_ID)) handleTabEvent(event);
    }

    void handleTabEvent(PhaseEvent event) {
        renderScript(event, SCRIPT_RESOURCE_TABBEDPANE);
    }

    public void beforePhase(PhaseEvent event) {
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
