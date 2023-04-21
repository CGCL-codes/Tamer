package org.goet.gui;

import java.awt.*;
import javax.swing.*;
import org.goet.gui.event.*;
import org.goet.datamodel.*;
import org.goet.datamodel.impl.*;

public abstract class AbstractEditor extends JPanel {

    protected Controller controller;

    public abstract void load(NodePropertyValue value);

    public abstract HistoryItem getHistoryItem();

    public void commit() {
        HistoryItem item = getHistoryItem();
        if (item != null) {
            controller.getEditorKit().getOperationModel().apply(item);
            controller.getHistory().add(item);
            controller.fireReload(new ReloadEvent(this));
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
