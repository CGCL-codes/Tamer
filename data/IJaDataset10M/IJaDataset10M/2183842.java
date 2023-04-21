package jpatch.boundary.action;

import java.awt.event.*;
import javax.swing.*;
import jpatch.control.edit.*;
import jpatch.boundary.*;
import jpatch.entity.*;

public final class ChangeTangentModeAction extends AbstractAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4514692916254071819L;

    public static final int PEAK = 0;

    public static final int JPATCH = 1;

    public static final int SPATCH = 2;

    private static final String[] astrNames = new String[] { "peak", "round (JPatch)", "round (sPatch)" };

    private static final int[] aiMapping = new int[] { OLDControlPoint.PEAK, OLDControlPoint.JPATCH_G1, OLDControlPoint.SPATCH_ROUND };

    private int iMode;

    public ChangeTangentModeAction(int mode) {
        super(astrNames[mode]);
        iMode = aiMapping[mode];
    }

    public void actionPerformed(ActionEvent actionEvent) {
        OLDSelection selection = MainFrame.getInstance().getSelection();
        if (selection == null) return;
        if (selection.getDirection() != 0) {
            MainFrame.getInstance().getUndoManager().addEdit(new AtomicChangeControlPoint.TangentMode((OLDControlPoint) selection.getHotObject(), iMode));
            MainFrame.getInstance().getJPatchScreen().update_all();
        } else {
            String name;
            switch(iMode) {
                case OLDControlPoint.PEAK:
                    name = "peak tangents";
                default:
                    name = "round tangents";
            }
            JPatchActionEdit edit = new JPatchActionEdit(name);
            OLDControlPoint[] acp = selection.getControlPointArray();
            for (int i = 0; i < acp.length; i++) {
                OLDControlPoint[] stack = acp[i].getStack();
                for (int j = 0; j < stack.length; j++) {
                    edit.addEdit(new AtomicChangeControlPoint.TangentMode(stack[j], iMode));
                }
            }
            MainFrame.getInstance().getUndoManager().addEdit(edit);
            MainFrame.getInstance().getJPatchScreen().update_all();
        }
    }
}
