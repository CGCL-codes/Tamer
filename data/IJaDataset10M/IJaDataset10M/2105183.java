package com.wrupple.muba.desktop.client.bootstrap.state.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.wrupple.muba.common.shared.StateTransition;
import com.wrupple.muba.desktop.client.bootstrap.state.LoadModuleActivities;
import com.wrupple.muba.desktop.client.module.DesktopModuleController;
import com.wrupple.muba.desktop.client.module.ModuleController;
import com.wrupple.muba.desktop.domain.DesktopLoadingStateHolder;

public class LoadModuleActivitiesImpl implements LoadModuleActivities {

    private Map<String, ModuleController> modules;

    DesktopModuleController desktopModuleController;

    @Inject
    public LoadModuleActivitiesImpl(DesktopModuleController desktopModuleController) {
        super();
        this.desktopModuleController = desktopModuleController;
    }

    @Override
    public void start(DesktopLoadingStateHolder parameter, StateTransition<DesktopLoadingStateHolder> onDone, EventBus eventBus) {
        Collection<ModuleController> modulesToInit = modules.values();
        Map<String, Activity> activityMap = new HashMap<String, Activity>();
        for (ModuleController module : modulesToInit) {
            module.initialize();
            activityMap.putAll(module.getActivities());
        }
        activityMap.putAll(desktopModuleController.getActivities());
        parameter.registerAll(activityMap);
        onDone.setResultAndFinish(parameter);
    }

    @Override
    public void setDesktopModules(Map<String, ModuleController> assignedModules) {
        this.modules = assignedModules;
    }
}
