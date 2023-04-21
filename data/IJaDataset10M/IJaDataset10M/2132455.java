package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.EditorAction;

/**
 * 
 * BottomAlignAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class BottomAlignAction extends EditorAction {

    private static String BOTTOM_ACTION_ICON = "/icons/bottom_align.png";

    public BottomAlignAction() {
        setId(ALIGNMENT_BOTTOM);
        setText(Messages.BottomAlignAction_Bottom_Alignment_in_Row);
        setToolTipText(Messages.BottomAlignAction_Bottom_Alignment_in_Row);
        setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(BOTTOM_ACTION_ICON));
        setEnabled(false);
    }
}
