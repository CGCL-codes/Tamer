package com.googlecode.pondskum.gui.swing.suite;

import com.googlecode.pondskum.client.BigpondUsageInformation;
import com.googlecode.pondskum.config.ConfigFileLoaderException;
import com.googlecode.pondskum.gui.swing.notifyer.ConnectionStatusForm;
import com.googlecode.pondskum.gui.swing.notifyer.DefaultDisplayDetailsPack;
import com.googlecode.pondskum.gui.swing.notifyer.ErrorPanel;
import com.googlecode.pondskum.gui.swing.notifyer.ProgressionPanel;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static com.googlecode.pondskum.gui.swing.suite.DefaultGuiFactory.PROGRESSION_HEIGHT;
import static com.googlecode.pondskum.gui.swing.suite.DefaultGuiFactory.PROGRESSION_WIDTH;

public final class ProgressionGui implements GUI {

    private ProgressionPanel progressionPanel;

    private ConnectionStatusForm connectionStatusForm;

    private JFrame parentFrame;

    private MouseListener mouseListener;

    private BigpondUsageInformation bigpondUsageInformation;

    private String currentStatus;

    private JPopupMenu contextMenu;

    public ProgressionGui(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        resetForReuse();
    }

    @Override
    public void resetForReuse() {
        progressionPanel = new ProgressionPanel();
        connectionStatusForm = new ConnectionStatusForm();
        parentFrame.getContentPane().removeAll();
        parentFrame.getContentPane().add(connectionStatusForm.getContentPanel());
        removeListener();
        contextMenu = null;
        mouseListener = null;
        bigpondUsageInformation = null;
        currentStatus = "";
    }

    private void createContextMenu(final StateChangeListener stateChangeListener) {
        contextMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Minimize to tray");
        menuItem.addActionListener(ContextMenuActions.createMinimizeToTrayTransition(this, stateChangeListener));
        contextMenu.add(menuItem);
        menuItem = new JMenuItem("Show Pondskum Tablet");
        menuItem.addActionListener(ContextMenuActions.createTabletTransition(this, stateChangeListener));
        contextMenu.add(menuItem);
    }

    private void removeListener() {
        progressionPanel.getContentPanel().removeMouseListener(mouseListener);
    }

    public void display() {
        parentFrame.setLocationRelativeTo(null);
        parentFrame.setVisible(true);
    }

    @Override
    public void dispose() {
        parentFrame.setVisible(false);
        resetForReuse();
    }

    @Override
    public void setStateChangeListener(final StateChangeListener stateChangeListener) {
        createContextMenu(stateChangeListener);
        mouseListener = new ContextMenuMouseListener();
        progressionPanel.getContentPanel().addMouseListener(mouseListener);
    }

    @Override
    public BigpondUsageInformation getUsageInfo() {
        return bigpondUsageInformation;
    }

    @Override
    public String getCurrentStatus() {
        return currentStatus;
    }

    @Override
    public void updateWithExistingUsage(final BigpondUsageInformation bigpondUsageInformation) {
        if (bigpondUsageInformation != null) {
            this.bigpondUsageInformation = bigpondUsageInformation;
            updateWithUsageInformation();
            parentFrame.getContentPane().validate();
        }
    }

    @Override
    public void updateWithCurrentStatus(final String currentStatus) {
        notifyStatusChange(currentStatus);
    }

    @Override
    public void notifyStatusChange(final String status) {
        currentStatus = status;
        connectionStatusForm.setProgress(status);
    }

    @Override
    public void connectionSucceeded(final BigpondUsageInformation bigpondUsageInformation) {
        this.bigpondUsageInformation = bigpondUsageInformation;
        hideStatusForm();
        updateWithUsageInformation();
    }

    private void updateWithUsageInformation() {
        parentFrame.getContentPane().add(progressionPanel.getContentPanel());
        progressionPanel.setUsageInfo(bigpondUsageInformation);
    }

    private void hideStatusForm() {
        parentFrame.getContentPane().remove(connectionStatusForm.getContentPanel());
    }

    @Override
    public void connectionFailed(final Exception exception) {
        hideStatusForm();
        String errorMessage = exception.getMessage();
        ErrorPanel errorPanel = new ErrorPanel(new DefaultDisplayDetailsPack(), errorMessage);
        errorPanel.showSeeLogsMessage(!ConfigFileLoaderException.class.isAssignableFrom(exception.getClass()));
        parentFrame.getContentPane().add(errorPanel.getContentPanel());
        parentFrame.setSize(PROGRESSION_WIDTH, PROGRESSION_HEIGHT);
        parentFrame.getContentPane().validate();
    }

    private class ContextMenuMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(final MouseEvent e) {
            showContextMenu(e);
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
            showContextMenu(e);
        }

        private void showContextMenu(final MouseEvent e) {
            if (e.isPopupTrigger()) {
                contextMenu.show(progressionPanel.getContentPanel(), e.getX(), e.getY());
            }
        }
    }
}
