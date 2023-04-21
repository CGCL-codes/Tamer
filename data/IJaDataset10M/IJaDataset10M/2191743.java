package org.ximtec.igesture.geco.gui.action;

import javax.swing.Action;
import org.ximtec.igesture.geco.gui.MainView;

/**
 * Provides access to available actions. Each action is instantiated only once.
 * @version 0.9, Nov 22, 2007
 * @author Michele Croci, mcroci@gmail.com
 * @author Beat Signer, signer@inf.ethz.ch
 */
public class ActionHandler {

    private MainView view;

    private AboutAction aboutAction;

    private AddMappingAction addMappingAction;

    private EditMappingAction editMappingAction;

    private ExitApplicationAction exitApplicationAction;

    private LoadGestureSetAction loadGestureSetAction;

    private MinimizeAction minimizeAction;

    private NewProjectAction newProjectAction;

    private OpenProjectAction openGestureMapAction;

    private OptionsAction optionsAction;

    private RemoveMappingAction removeMappingAction;

    private SaveProjectAction saveProjectAction;

    private SaveProjectAsAction saveProjectAsAction;

    public ActionHandler(MainView view) {
        this.view = view;
    }

    public Action getAboutAction() {
        if (aboutAction == null) {
            aboutAction = new AboutAction();
        }
        return aboutAction;
    }

    public Action getAddMappingAction() {
        if (addMappingAction == null) {
            addMappingAction = new AddMappingAction(view);
        }
        return addMappingAction;
    }

    public Action getEditMappingAction() {
        if (editMappingAction == null) {
            editMappingAction = new EditMappingAction(view);
        }
        return editMappingAction;
    }

    public Action getExitApplicationAction() {
        if (exitApplicationAction == null) {
            exitApplicationAction = new ExitApplicationAction(view);
        }
        return exitApplicationAction;
    }

    public Action getLoadGestureSetAction() {
        if (loadGestureSetAction == null) {
            loadGestureSetAction = new LoadGestureSetAction(view);
        }
        return loadGestureSetAction;
    }

    public Action getMinimizeAction() {
        if (minimizeAction == null) {
            minimizeAction = new MinimizeAction(view);
        }
        return minimizeAction;
    }

    public Action getNewProjectAction() {
        if (newProjectAction == null) {
            newProjectAction = new NewProjectAction(view);
        }
        return newProjectAction;
    }

    public Action getOpenProjectAction() {
        if (openGestureMapAction == null) {
            openGestureMapAction = new OpenProjectAction(view);
        }
        return openGestureMapAction;
    }

    public Action getOptionsAction() {
        if (optionsAction == null) {
            optionsAction = new OptionsAction(view);
        }
        return optionsAction;
    }

    public Action getRemoveMappingAction() {
        if (removeMappingAction == null) {
            removeMappingAction = new RemoveMappingAction(view);
        }
        return removeMappingAction;
    }

    public Action getSaveProjectAction() {
        if (saveProjectAction == null) {
            saveProjectAction = new SaveProjectAction(view);
        }
        return saveProjectAction;
    }

    public Action getSaveProjectAsAction() {
        if (saveProjectAsAction == null) {
            saveProjectAsAction = new SaveProjectAsAction(view);
        }
        return saveProjectAsAction;
    }
}
