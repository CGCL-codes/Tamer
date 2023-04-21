package net.sf.jvibes.ui.views;

import net.infonode.docking.SplitWindow;
import net.infonode.docking.util.ViewMap;
import net.sf.jvibes.JVibes;
import net.sf.jvibes.kernel.elements.Model;
import net.sf.jvibes.ui.Modeller;
import net.sf.jvibes.ui.views.properties.PropertiesView;

public class ModellerWindow extends SplitWindow {

    private final WorkspaceTabView _tabView = new WorkspaceTabView(JVibes.getWorkspace());

    private final ModelBrowser _modelBrowser = new ModelBrowser();

    private final PropertiesView _propertiesView = new PropertiesView();

    private final ViewMap _perspective = new ViewMap();

    public ModellerWindow() {
        super(true);
        setDividerLocation(0.25f);
        _perspective.addView(0, _modelBrowser);
        _perspective.addView(1, _propertiesView);
        setWindows(new SplitWindow(false, 0.5f, _modelBrowser, _propertiesView), new SplitWindow(false, 0.9f, _tabView, new TasksView()));
        Modeller ws = JVibes.getWorkspace();
        if (ws.getData().size() == 0) {
            Model model = ws.createSystem();
            ws.add(model);
        }
    }
}
