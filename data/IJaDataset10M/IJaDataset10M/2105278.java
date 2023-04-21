package org.colombbus.tangara.ide.controller.action;

import java.awt.event.ActionEvent;
import org.colombbus.tangara.ide.view.codeeditor.CodeEditorTabManager;

/**
 * Maximize {@link CodeEditorTabManager} action.
 *
 * @author Aurelien Bourdon <aurelien.bourdon@gmail.com>
 */
@SuppressWarnings("serial")
public class MaximizeCodeEditorTabAction extends TangaraAction {

    public MaximizeCodeEditorTabAction() {
        putValue(LARGE_ICON_KEY, RESOURCE_BUNDLE.getIcon("MaximizeCodeEditorTabAction.largeIcon"));
        putValue(SHORT_DESCRIPTION, RESOURCE_BUNDLE.getString("MaximizeCodeEditorTabAction.shortDescription"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Maximize!");
    }
}
