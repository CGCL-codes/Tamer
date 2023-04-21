package org.xaware.schemanavigator.actions;

import org.eclipse.jface.action.Action;
import org.xaware.schemanavigator.gui.IGeneratorView;

/**
 * This is the action invoked by the view to show or hide Attributes in the right-panel
 * 
 * @author jtarnowski
 */
public class ShowOrHideAttributesAction extends Action {

    IGeneratorView view;

    /**
     * Constructor
     * 
     * @param viewer
     *            IGeneratorView
     * @param text
     *            String
     */
    public ShowOrHideAttributesAction(final IGeneratorView viewer, final String text) {
        super(text);
        this.view = viewer;
    }

    /**
     * Check all from the currently selected Element
     */
    @Override
    public void run() {
        final boolean show = (!view.isShowAttributes());
        view.setShowAttributes(show);
        if (show) {
            setText("Hide Attributes");
            setToolTipText("Hide all Attributes");
        } else {
            setText("Show Attributes");
            setToolTipText("Show all Attributes");
        }
        view.refreshCheckTreePanel();
    }
}
