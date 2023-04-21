package jpatch.boundary.action;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import jpatch.boundary.*;
import jpatch.entity.*;
import jpatch.control.edit.*;

public final class AlignPatchesAction extends AbstractAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4600303467349508386L;

    public AlignPatchesAction() {
        super("align patches");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        OLDSelection selection = MainFrame.getInstance().getSelection();
        if (selection != null) {
            HashSet patches = new HashSet();
            for (Iterator it = MainFrame.getInstance().getModel().getPatchSet().iterator(); it.hasNext(); ) {
                Patch patch = (Patch) it.next();
                if (patch.isSelected(selection)) patches.add(patch);
            }
            MainFrame.getInstance().getUndoManager().addEdit(new CompoundAlignPatches(patches));
        } else {
            MainFrame.getInstance().getUndoManager().addEdit(new CompoundAlignPatches());
        }
        MainFrame.getInstance().getJPatchScreen().update_all();
    }
}
