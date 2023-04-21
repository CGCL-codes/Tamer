package jpatch.boundary.action;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import jpatch.control.edit.*;
import jpatch.boundary.*;
import jpatch.entity.*;

public final class MakeFivePointPatchAction extends AbstractAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public MakeFivePointPatchAction() {
        super("", new ImageIcon(ClassLoader.getSystemResource("jpatch/images/fivepointpatch.png")));
        putValue(Action.SHORT_DESCRIPTION, "make five point patch");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Patch patch = checkSelection();
        if (patch == null) return;
        OLDModel model = MainFrame.getInstance().getModel();
        OLDControlPoint[] acp = patch.getControlPoints();
        OLDControlPoint[] acp5 = new OLDControlPoint[] { acp[0].trueHead(), acp[2].trueHead(), acp[4].trueHead(), acp[6].trueHead(), acp[8].trueHead() };
        model.getCandidateFivePointPatchList().add(acp5);
        MainFrame.getInstance().getUndoManager().addEdit(new AtomicAddPatch(patch));
        MainFrame.getInstance().getJPatchScreen().update_all();
    }

    public static Patch checkSelection() {
        OLDSelection selection = MainFrame.getInstance().getSelection();
        OLDModel model = MainFrame.getInstance().getModel();
        ArrayList points = new ArrayList();
        if (selection == null || MainFrame.getInstance().getAnimation() != null) return null;
        for (Iterator it = model.getCurveSet().iterator(); it.hasNext(); ) {
            for (OLDControlPoint cp = (OLDControlPoint) it.next(); cp != null; cp = cp.getNextCheckNextLoop()) {
                if (selection.contains(cp)) points.add(cp); else if (cp.getParentHook() != null && selection.contains(cp.getParentHook().getHead())) {
                    points.add(cp);
                }
            }
        }
        OLDControlPoint[] acp = new OLDControlPoint[points.size()];
        points.toArray(acp);
        HashMap mapHeadList = new HashMap();
        HashSet setHeads = new HashSet();
        for (int i = 0; i < acp.length; i++) {
            OLDControlPoint head = acp[i].trueHead();
            setHeads.add(head);
            if (acp[i].getParentHook() != null) {
                ArrayList neighbors = (ArrayList) mapHeadList.get(head);
                if (neighbors == null) {
                    neighbors = new ArrayList();
                    mapHeadList.put(head, neighbors);
                }
                if (acp[i].getNext() != null) {
                    neighbors.add(acp[i]);
                    neighbors.add(acp[i].getNext());
                }
                if (acp[i].getPrev() != null) {
                    neighbors.add(acp[i]);
                    neighbors.add(acp[i].getPrev());
                }
            }
        }
        for (int i = 0; i < acp.length; i++) {
            OLDControlPoint head = acp[i].trueHead();
            if (acp[i].getParentHook() == null) {
                ArrayList neighbors = (ArrayList) mapHeadList.get(head);
                if (neighbors == null) {
                    neighbors = new ArrayList();
                    mapHeadList.put(head, neighbors);
                }
                OLDControlPoint[] stack = acp[i].getStack();
                for (int j = 0; j < stack.length; j++) {
                    if (stack[j].getNext() != null) {
                        neighbors.add(stack[j]);
                        neighbors.add(stack[j].getNext());
                    }
                    if (stack[j].getPrev() != null) {
                        neighbors.add(stack[j]);
                        neighbors.add(stack[j].getPrev());
                    }
                }
            }
        }
        if (setHeads.size() == 5) {
            ArrayList fivePointPatch = new ArrayList();
            ArrayList toGoList = new ArrayList(setHeads);
            OLDControlPoint neighborA;
            OLDControlPoint neighborB;
            OLDControlPoint currentHead = (OLDControlPoint) toGoList.get(0);
            OLDControlPoint first = currentHead;
            toGoList.remove(currentHead);
            for (int i = 0; i < 5; i++) {
                ArrayList neighborList = (ArrayList) mapHeadList.get(currentHead);
                Iterator neighborIterator = neighborList.iterator();
                boolean found = false;
                loop: while (neighborIterator.hasNext()) {
                    neighborA = (OLDControlPoint) neighborIterator.next();
                    neighborB = (OLDControlPoint) neighborIterator.next();
                    if (toGoList.contains(neighborB.trueHead()) || (toGoList.size() == 0 && neighborB.trueHead() == first)) {
                        fivePointPatch.add(neighborA);
                        fivePointPatch.add(neighborB);
                        currentHead = neighborB.trueHead();
                        toGoList.remove(currentHead);
                        found = true;
                        break loop;
                    }
                }
                if (!found) {
                    return null;
                }
            }
            if (fivePointPatch.size() == 10) {
                acp = new OLDControlPoint[10];
                for (int i = 0; i < 10; i++) {
                    acp[i] = (OLDControlPoint) fivePointPatch.get(i);
                }
                if (acp[1].trueCp() != acp[2].trueCp() && acp[3].trueCp() != acp[4].trueCp() && acp[5].trueCp() != acp[6].trueCp() && acp[7].trueCp() != acp[8].trueCp() && acp[9].trueCp() != acp[0].trueCp()) {
                    Patch patch = new Patch(acp);
                    return patch;
                }
            }
        }
        return null;
    }
}
