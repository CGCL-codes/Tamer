package de.erdesignerng.visual.common;

import de.mogwai.common.client.looks.components.action.ActionEventProcessor;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class UICommand implements ActionEventProcessor, ActionListener {

    public UICommand() {
    }

    protected ERDesignerWorldConnector getWorldConnector() {
        return ERDesignerComponent.getDefault().getWorldConnector();
    }

    protected JComponent getDetailComponent() {
        return ERDesignerComponent.getDefault().getDetailComponent();
    }

    public abstract void execute();

    @Override
    public void processActionEvent(ActionEvent e) {
        execute();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        execute();
    }

    /**
     * Refresh the display of a specific object.
     */
    public void refreshDisplayAndOutline() {
        ERDesignerComponent component = ERDesignerComponent.getDefault();
        component.repaintGraph();
        OutlineComponent.getDefault().refresh(component.getModel());
    }
}
