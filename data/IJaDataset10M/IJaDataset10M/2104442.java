package org.xaware.ide.xadev.gui.actions;

import org.eclipse.ui.views.navigator.IResourceNavigator;
import org.eclipse.ui.views.navigator.MainActionGroup;

/**
 * Main action group for XAProjectExplorer. This action group adds XARefactorActionGroup in place of navigator
 * RefactorActionGroup.
 * 
 * @author blueAlly.
 * 
 */
public class XAMainActionGroup extends MainActionGroup {

    /**
     * Creates XAMainActionGroup instance.
     * 
     * @param navigator
     *            Resource Navigator.
     */
    public XAMainActionGroup(IResourceNavigator navigator) {
        super(navigator);
        refactorGroup = new XARefactorActionGroup(navigator);
    }
}
