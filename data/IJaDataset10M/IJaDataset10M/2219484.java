package net.cepra.timecard.client;

import net.cepra.timecard.client.activity.editor.ActivityEditor;
import net.cepra.timecard.client.activity.search.ActivitySelector;
import net.cepra.timecard.client.project.editor.ProjectEditor;
import net.cepra.timecard.client.project.selector.ProjectSelector;
import com.google.gwt.core.client.EntryPoint;
import com.gwtaf.client.action.ActionGroup;
import com.gwtaf.client.action.ActionPool;
import com.gwtaf.client.action.HistoryAction;
import com.gwtaf.client.appstate.AppStateManager;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TimeCard implements EntryPoint {

    public TimeCard() {
    }

    private void registerFactories() {
        AppStateManager.get().addFactory(ActivitySelector.FACTORY);
        AppStateManager.get().addFactory(ActivityEditor.FACTORY);
        AppStateManager.get().addFactory(ProjectEditor.FACTORY);
        AppStateManager.get().addFactory(ProjectSelector.FACTORY);
    }

    protected void initGlobalCommands() {
        ActionGroup group = new ActionGroup("Zeitkarte");
        group.add(new HistoryAction(ActivitySelector.ID, "Zeiten&uuml;bersicht"));
        group.add(new HistoryAction(ActivityEditor.ID, "Zeiterfassung"));
        ActionPool.get().add(group);
        group = new ActionGroup("Projekte");
        group.add(new HistoryAction(ProjectSelector.ID, "Projektliste"));
        ActionPool.get().add(group);
    }

    /**
   * This is the entry point method.
   */
    public void onModuleLoad() {
        registerFactories();
        initGlobalCommands();
    }
}
