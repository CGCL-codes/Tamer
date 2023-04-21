package com.hifi.plugin.layout.manager;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import org.java.plugin.Plugin;
import com.hifi.core.api.ui.constants.UIModuleEnum;
import com.hifi.core.api.ui.modules.IUIModule;
import com.hifi.core.api.ui.modules.frame.IMainFrame;
import com.hifi.plugin.ui.ext.ILayoutManager;

public class DefaultLayoutManagerPlugin extends Plugin implements ILayoutManager {

    public static final String PLUGIN_ID = "com.hifi.plugin.layout.manager.default";

    private Map<UIModuleEnum, JComponent> components = new HashMap<UIModuleEnum, JComponent>();

    private IMainFrame main;

    @Override
    protected void doStart() throws Exception {
    }

    @Override
    protected void doStop() throws Exception {
    }

    @Override
    public void init(IMainFrame main) {
        this.main = main;
    }

    @Override
    public void addUIModule(IUIModule module) {
        components.put(module.getId(), module.getComponent());
    }

    @Override
    public void doLayout() {
        HFLayoutManager manager = new HFLayoutManager(main, components);
        manager.doLayout();
    }
}
