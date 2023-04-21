package de.hpi.eworld.core.ui.docking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import de.hpi.eworld.core.ui.MainWindow;
import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowListener;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.View;

public abstract class LogicalDockGroup implements ActionListener, DockingWindowListener {

    protected JCheckBoxMenuItem menuItem;

    protected MainWindow parentWindow;

    protected boolean dockAddedToParent = true;

    public abstract void actionPerformed(ActionEvent e);

    public abstract void windowClosed(DockingWindow window);

    public LogicalDockGroup(JCheckBoxMenuItem menuItem) {
        this.menuItem = menuItem;
        menuItem.setSelected(true);
        menuItem.addActionListener(this);
    }

    @Override
    public void viewFocusChanged(View previouslyFocusedView, View focusedView) {
    }

    @Override
    public void windowAdded(DockingWindow addedToWindow, DockingWindow addedWindow) {
    }

    @Override
    public void windowClosing(DockingWindow window) throws OperationAbortedException {
    }

    @Override
    public void windowDocked(DockingWindow window) {
    }

    @Override
    public void windowDocking(DockingWindow window) throws OperationAbortedException {
    }

    @Override
    public void windowHidden(DockingWindow window) {
    }

    @Override
    public void windowMaximized(DockingWindow window) {
    }

    @Override
    public void windowMaximizing(DockingWindow window) throws OperationAbortedException {
    }

    @Override
    public void windowMinimized(DockingWindow window) {
    }

    @Override
    public void windowMinimizing(DockingWindow window) throws OperationAbortedException {
    }

    @Override
    public void windowRemoved(DockingWindow removedFromWindow, DockingWindow removedWindow) {
    }

    @Override
    public void windowRestored(DockingWindow window) {
    }

    @Override
    public void windowRestoring(DockingWindow window) throws OperationAbortedException {
    }

    @Override
    public void windowShown(DockingWindow window) {
    }

    @Override
    public void windowUndocked(DockingWindow window) {
    }

    @Override
    public void windowUndocking(DockingWindow window) throws OperationAbortedException {
    }
}
