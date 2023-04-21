package net.sf.jvibes.ui.views;

import java.util.ArrayList;
import net.infonode.docking.DockingWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.properties.DockingWindowProperties;
import net.sf.jvibes.kernel.elements.Model;
import net.sf.jvibes.ui.Modeller;
import net.sf.jvibes.ui.Workspace.Event;
import org.apache.log4j.Logger;

/**
 * @author b|m
 * 
 */
public class WorkspaceTabView extends TabWindow {

    private static final Logger __logger = Logger.getLogger(WorkspaceTabView.class);

    /**
	 * 
	 */
    public WorkspaceTabView(Modeller ws) {
        ArrayList<Model> models = ws.getData();
        for (Model model : models) {
            addTab(new ModelView(model));
        }
        DockingWindowProperties properties = getWindowProperties();
        properties.setCloseEnabled(false);
        properties.setUndockEnabled(false);
        properties.setDockEnabled(false);
        ws.addListener(new Modeller.Adapter() {

            @Override
            public void dataAdded(Event<Model> evt) {
                Model model = evt.getData();
                addTab(new ModelView(model));
            }

            @Override
            public void selectedChanged(Event<Model> evt) {
                Model model = evt.getData();
                setSelectedTab(model);
            }
        });
    }

    void setSelectedTab(Model model) {
        for (int i = 0; i < getChildWindowCount(); i++) {
            DockingWindow window = getChildWindow(i);
            if (window instanceof ModelView) {
                ModelView mView = (ModelView) window;
                if (mView.getModel() == model) {
                    setSelectedTab(i);
                    return;
                }
            }
        }
    }
}
