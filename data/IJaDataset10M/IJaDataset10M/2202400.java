package org.kineticsystem.commons.data.view.actions;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.kineticsystem.commons.data.controller.AdvancedAction;
import org.kineticsystem.commons.data.controller.DataNavigator;
import org.kineticsystem.commons.util.Localizer;
import org.kineticsystem.commons.util.ResourceLoader;

/**
 * Action connected to an editor controller and used to cancel an update, or an
 * insertion.
 * @author Giovanni Remigi
 * @version $Revision: 145 $
 * @see org.kineticsystem.commons.data.controller.DataNavigator
 */
public class CancelAction extends AdvancedAction {

    /** Name of the action used to cancel an update, or an insertion. */
    public static final String ACTION_NAME = "CancelAction";

    /** Resource bundle class. */
    private static final String NAVIGATOR_BUNDLE = "org.kineticsystem.commons.data.view.bundle.NavigatorBundle";

    /** Name of the package containing all requested images. */
    static final String NAVIGATOR_RESOURCE = "org/kineticsystem/commons/data/view/images/";

    /** The editor controller instance. */
    private final DataNavigator controller;

    /**
     * Constructor.
     * @param controller The editor controller instance.
     */
    public CancelAction(DataNavigator controller) {
        this.controller = controller;
        putValue(Action.SMALL_ICON, ResourceLoader.getIcon(NAVIGATOR_RESOURCE + "Reload16.png"));
        putValue(Action.NAME, Localizer.localizeString(NAVIGATOR_BUNDLE, "CancelAction"));
        putValue(Action.SHORT_DESCRIPTION, Localizer.localizeString(NAVIGATOR_BUNDLE, "CancelAction_Description"));
        setEnabled(false);
    }

    /** {@inheritDoc} */
    public void actionPerformed(ActionEvent actionEvent) {
        if (controller.getState() == DataNavigator.EDITING_STATE) {
            controller.cancelChange();
        } else if (controller.getState() == DataNavigator.CREATION_STATE) {
            controller.cancelCreation();
        }
    }
}
