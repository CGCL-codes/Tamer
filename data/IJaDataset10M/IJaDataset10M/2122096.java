package net.ar.webonswing.own.adapters;

import java.awt.event.*;
import javax.swing.*;
import net.ar.guia.own.interfaces.listeners.*;
import net.ar.guia.plugins.swing.*;

public class HoldingComboBoxSelectionListener implements ActionListener {

    protected SelectionListener selectionListener;

    public HoldingComboBoxSelectionListener(SelectionListener aSelectionListener) {
        selectionListener = aSelectionListener;
    }

    public void actionPerformed(ActionEvent anActionEvent) {
        JComboBox comboBox = (JComboBox) anActionEvent.getSource();
        selectionListener.selectionPerformed(new MultipleSelectionEvent(GuiaToSwingPlugin.getWrapperFor(comboBox)));
    }

    public SelectionListener getSelectionListener() {
        return selectionListener;
    }

    public void setSelectionListener(SelectionListener aSelectionListener) {
        selectionListener = aSelectionListener;
    }
}
