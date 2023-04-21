package glaceo.gui.client.vc;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabPanel;
import glaceo.gui.client.GGuiEvents;
import glaceo.gui.client.model.GAbstractModel;
import glaceo.gui.client.model.GNavCupItem;
import glaceo.gui.client.model.GNavLeagueItem;
import glaceo.gui.client.vc.contest.GCupTabPanel;
import glaceo.gui.client.vc.contest.GLeagueTabPanel;

/**
 * Main country view.
 *
 * @version $Id$
 * @author jjanke
 */
public class GContentView extends View {

    private GContentController d_controller;

    private TabPanel d_tabPanelContent;

    public GContentView(GContentController controller) {
        super(controller);
        d_controller = controller;
    }

    @Override
    protected void initialize() {
    }

    private void loadContent(GAbstractModel model) {
        String strAdditionalHeader = "";
        d_tabPanelContent = null;
        if (model instanceof GNavLeagueItem) {
            d_tabPanelContent = new GLeagueTabPanel(d_controller, (GNavLeagueItem) model);
            strAdditionalHeader = " (" + model.getParent().getLabel() + ")";
        } else if (model instanceof GNavCupItem) {
            d_tabPanelContent = new GCupTabPanel(d_controller, (GNavCupItem) model);
            strAdditionalHeader = " (" + model.getParent().getLabel() + ")";
        }
        if (d_tabPanelContent == null) return;
        ContentPanel panelMainContent = Registry.get(GMainView.PANEL_MAIN);
        panelMainContent.removeAll();
        panelMainContent.setHeading(model.toString() + strAdditionalHeader);
        panelMainContent.setIconStyle(model.getIconStyle());
        panelMainContent.add(d_tabPanelContent);
        panelMainContent.layout();
    }

    @Override
    protected void handleEvent(AppEvent<?> event) {
        switch(event.type) {
            case GGuiEvents.CONTENT_REQUESTED:
                loadContent((GAbstractModel) event.data);
                break;
        }
    }
}
