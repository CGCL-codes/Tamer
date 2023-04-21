package com.empower.client.view.lists;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import com.empower.constants.AppClientConstants;

public class ChartOfAccountTypesFrame extends JInternalFrame {

    private JButton deleteBtn;

    private JButton editBtn;

    private JButton newBtn;

    private JScrollPane scrollPane;

    private JTable table;

    public ChartOfAccountTypesFrame(String frameTitle, boolean isResizable, boolean isClosable, boolean isMaximzable, boolean isMinimizable) {
        super(frameTitle, isResizable, isClosable, isMaximzable, isMinimizable);
        ImageIcon ecsIcon = new ImageIcon(getClassLoader().getResource(AppClientConstants.IMG_PKG_PATH.concat("empower_logo.JPG")));
        setFrameIcon(ecsIcon);
        setPreferredSize(new Dimension(600, 300));
        getContentPane().setLayout(null);
        getContentPane().add(getScrollPane());
        getContentPane().add(getNewBtn());
        getContentPane().add(getEditBtn());
        getContentPane().add(getDeleteBtn());
    }

    private ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }

    public JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setBounds(0, 0, 590, 234);
            scrollPane.setViewportView(getTable());
        }
        return scrollPane;
    }

    public JTable getTable() {
        if (table == null) {
            table = new JTable();
        }
        return table;
    }

    public JButton getNewBtn() {
        if (newBtn == null) {
            newBtn = new JButton();
            newBtn.setText("New");
            newBtn.setBounds(10, 240, 93, 23);
        }
        return newBtn;
    }

    public JButton getEditBtn() {
        if (editBtn == null) {
            editBtn = new JButton();
            editBtn.setText("Edit");
            editBtn.setBounds(109, 240, 93, 23);
        }
        return editBtn;
    }

    public JButton getDeleteBtn() {
        if (deleteBtn == null) {
            deleteBtn = new JButton();
            deleteBtn.setText("Delete");
            deleteBtn.setBounds(208, 240, 93, 23);
        }
        return deleteBtn;
    }
}
