package jpatch.control.edit;

import java.util.*;
import jpatch.entity.*;
import jpatch.boundary.*;

/**
 * Use this class for changing morphs
 */
public class AtomicFlipPatches extends JPatchAtomicEdit implements JPatchRootEdit {

    private List listPatches = new ArrayList();

    public AtomicFlipPatches(OLDSelection selection) {
        for (Iterator it = MainFrame.getInstance().getModel().getPatchSet().iterator(); it.hasNext(); ) {
            Patch patch = (Patch) it.next();
            if (patch.isSelected(selection)) listPatches.add(patch);
        }
        flip();
    }

    public void undo() {
        flip();
    }

    public void redo() {
        flip();
    }

    private void flip() {
        for (Iterator it = listPatches.iterator(); it.hasNext(); ) {
            ((Patch) it.next()).flip();
        }
    }

    public int sizeOf() {
        return 8 + 4 + 4 + 4 + 8 * listPatches.size() * 2;
    }

    public String getName() {
        return "flip patches";
    }
}
