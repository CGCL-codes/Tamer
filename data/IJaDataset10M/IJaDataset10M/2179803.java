package de.mogwai.common.client.looks.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import de.mogwai.common.client.looks.UIInitializer;

public class DefaultTable extends JTable {

    private DefaultScrollPane scrollPane;

    private DefaultPopupMenu contextMenu;

    public DefaultTable() {
        scrollPane = new DefaultScrollPane(this);
        initialize();
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public DefaultPopupMenu getContextMenu() {
        return contextMenu;
    }

    public void setContextMenu(DefaultPopupMenu contaxtMenu) {
        this.contextMenu = contaxtMenu;
        UIInitializer.getInstance().initialize(contaxtMenu);
    }

    private void initialize() {
        setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        setCellSelectionEnabled(false);
        setColumnSelectionAllowed(false);
        setRowSelectionAllowed(true);
        setGridColor(UIInitializer.getInstance().getConfiguration().getDefaultTableGridColor());
        MouseListener theListener = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (((e.getButton() & MouseEvent.BUTTON2) > 0) && (contextMenu != null)) {
                    contextMenu.show(DefaultTable.this, e.getX(), e.getY());
                }
            }
        };
        addMouseListener(theListener);
        scrollPane.addMouseListener(theListener);
    }
}
