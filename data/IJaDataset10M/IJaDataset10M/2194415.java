package org.gapjump.security.keystone.ui;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JInternalFrame;

/**
 * Action used for navigating forwards and backwards through a looped set of JInternalFrames in a JDesktopPane.
 * Each instance of this action is configured to either always move backwards, or always move forwards, based on the
 * foward boolean.
 * @author Will Gage <will at gapjump dot oh ahr gee>
 *
 */
public class CycleInternalFramesAction extends AbstractAction {

    private static final int NOT_FOUND = -1;

    private boolean forward = true;

    private List<JInternalFrame> internalFrames;

    /**
	 * Does this action move forward through the list of JInternalFrames
	 * @return true if forward, false if backward
	 */
    public boolean isForward() {
        return forward;
    }

    /**
	 * @param forward the forward to set
	 */
    public void setForward(boolean forward) {
        this.forward = forward;
    }

    /**
	 * @return the internalFrames
	 */
    public List<JInternalFrame> getInternalFrames() {
        return internalFrames;
    }

    /**
	 * @param internalFrames the internalFrames to set
	 */
    public void setInternalFrames(List<JInternalFrame> internalFrames) {
        this.internalFrames = internalFrames;
    }

    public CycleInternalFramesAction(String name, List<JInternalFrame> newInternalFrames) {
        super(name);
        setInternalFrames(newInternalFrames);
    }

    public CycleInternalFramesAction(String name, Icon icon, List<JInternalFrame> newInternalFrames) {
        super(name, icon);
        setInternalFrames(newInternalFrames);
    }

    /**
	 * When recieves an ActionEvent, cycles through the frames.
	 */
    public void actionPerformed(ActionEvent e) {
        cycle();
    }

    private int findSelectedIndex() {
        int index = NOT_FOUND;
        int i = 0;
        for (JInternalFrame frame : getInternalFrames()) {
            if (frame.isSelected()) {
                index = i;
            }
            i++;
        }
        return index;
    }

    private void cycle() {
        int len = getInternalFrames().size();
        int selected = findSelectedIndex();
        if (selected == NOT_FOUND && len > 0) {
            throw new IllegalStateException("At least one JInternalFrame must be selected");
        }
        int newSelected = NOT_FOUND;
        if (isForward()) {
            newSelected = (selected + 1) % len;
        } else {
            newSelected = (selected + 2 * len - 1) % len;
        }
        JInternalFrame frame = getInternalFrames().get(newSelected);
        frame.moveToFront();
        try {
            if (frame.isIcon()) {
                frame.setIcon(false);
            }
            frame.setSelected(true);
            frame.requestFocus();
        } catch (java.beans.PropertyVetoException ex) {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
