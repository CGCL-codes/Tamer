package com.gampire.pc.view.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;

public class PCIIComboBoxMouseAdapter extends PCIIMouseDragAdapter {

    private final MouseListener originalMouseListener;

    public PCIIComboBoxMouseAdapter(JComponent comp, MouseListener originalMouseListener) {
        super(comp);
        this.originalMouseListener = originalMouseListener;
    }

    public void mouseReleasedButNoDrag(MouseEvent e) {
        originalMouseListener.mouseReleased(e);
    }
}
