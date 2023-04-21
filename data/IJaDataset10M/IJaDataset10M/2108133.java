package edu.berkeley.guir.quill;

import edu.berkeley.guir.quill.gesturelib.*;
import edu.berkeley.guir.quill.util.*;
import javax.swing.event.*;
import java.util.*;
import javax.swing.*;
import java.awt.datatransfer.Transferable;
import java.awt.Component;

/** 
 * <P>
 * This software is distributed under the 
 * <A HREF="http://guir.cs.berkeley.edu/projects/COPYRIGHT.txt">
 * Berkeley Software License</A>.
 
*/
public class GestureSetPanel extends GestureContainerPanel {

    protected Classifier classifier = null;

    public GestureSetPanel() {
        this(null);
    }

    public GestureSetPanel(GestureSet gs) {
        this(gs, null);
    }

    public GestureSetPanel(GestureSet gs, JScrollPane scrollPane) {
        super(gs, scrollPane);
    }

    public void setGestureSet(GestureSet gs) {
        setGestureContainer(gs);
    }

    public void setGestureContainer(GestureContainer gestureSet) {
        if ((gestureSet != null) && !(gestureSet instanceof GestureSet)) {
            throw new IllegalArgumentException("Only GestureSet is allowed, not " + gestureSet.getClass().getName() + "(" + gestureSet + ")");
        }
        if (classifier != null) {
            classifier.setGestureSet((GestureSet) gestureSet);
        }
        super.setGestureContainer(gestureSet);
    }

    public GestureSet getGestureSet() {
        return (GestureSet) getGestureContainer();
    }

    public int getState() {
        return GestureAcceptor.TEST_RECOGNITION;
    }

    public boolean gestureDrawn(Gesture gesture) {
        MainFrame mf = (MainFrame) SwingUtilities.getAncestorOfClass(MainFrame.class, this);
        mf.recognizeGesture(gesture);
        return false;
    }

    protected boolean canPaste(Transferable t) {
        return t.isDataFlavorSupported(GestureFlavorFactory.GESTURE_GROUP_COLLECTION_FLAVOR) || t.isDataFlavorSupported(GestureFlavorFactory.GESTURE_GROUP_FLAVOR) || t.isDataFlavorSupported(GestureFlavorFactory.GESTURE_CATEGORY_COLLECTION_FLAVOR) || t.isDataFlavorSupported(GestureFlavorFactory.GESTURE_CATEGORY_FLAVOR);
    }

    public boolean isCommandValid(int id) {
        boolean valid;
        switch(id) {
            case NEW_GESTURE_ACTION:
            case NEW_GROUP_ACTION:
                valid = true;
                break;
            default:
                valid = super.isCommandValid(id);
                break;
        }
        return valid;
    }

    public void doCommand(int id) {
        switch(id) {
            case NEW_GESTURE_ACTION:
                getGestureSet().add(new GestureCategory(Gensym.next(GESTURE_CATEGORY_PREFIX)));
                break;
            case NEW_GROUP_ACTION:
                getGestureSet().add(new GestureGroup(Gensym.next(GESTURE_GROUP_PREFIX)));
                break;
            default:
                super.doCommand(id);
                break;
        }
    }
}
