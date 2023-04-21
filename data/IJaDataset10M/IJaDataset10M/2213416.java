package de.erdesignerng.visual.editor.sql;

import de.erdesignerng.dialect.Statement;
import de.mogwai.common.client.looks.UIInitializer;
import de.mogwai.common.client.looks.components.DefaultTextArea;
import javax.swing.*;
import java.awt.*;

public class StatementRenderer implements ListCellRenderer {

    private final DefaultTextArea component = new DefaultTextArea();

    private final UIInitializer initializer = UIInitializer.getInstance();

    @Override
    public Component getListCellRendererComponent(JList aList, Object aValue, int aIndex, boolean isSelected, boolean cellHasFocus) {
        Statement theStatement = (Statement) aValue;
        if (theStatement.isExecuted()) {
            component.setForeground(Color.BLACK);
        } else {
            component.setForeground(Color.GRAY);
        }
        component.setText(theStatement.getSql());
        if (isSelected) {
            component.setBackground(initializer.getConfiguration().getDefaultListSelectionBackground());
        } else {
            component.setBackground(initializer.getConfiguration().getDefaultListNonSelectionBackground());
        }
        return component;
    }
}
