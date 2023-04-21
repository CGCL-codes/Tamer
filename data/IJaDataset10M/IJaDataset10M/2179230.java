package com.empower.client.view.lists;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import com.empower.client.utils.WidgetProperties;
import com.empower.constants.AppClientConstants;

public class RemisierListFrame extends JInternalFrame {

    private JLabel remisierCodeLbl;

    private JTextField remisierCodeTxf;

    private JButton goBtn;

    private JScrollPane scrollPane;

    private JTable table;

    private JButton newBtn;

    private JButton editBtn;

    private JButton deleteBtn;

    public RemisierListFrame(String frameTitle, boolean isResizable, boolean isClosable, boolean isMaximzable, boolean isMinimizable) {
        super(frameTitle, isResizable, isClosable, isMaximzable, isMinimizable);
        ImageIcon ecsIcon = new ImageIcon(getClassLoader().getResource(AppClientConstants.IMG_PKG_PATH.concat("empower_logo.JPG")));
        setFrameIcon(ecsIcon);
        setPreferredSize(new Dimension(750, 300));
        getContentPane().setLayout(null);
        getContentPane().add(getRemisierCodeLbl());
        getContentPane().add(getRemisierCodeTxf());
        getContentPane().add(getNewBtn());
        getContentPane().add(getEditBtn());
        getContentPane().add(getDeleteBtn());
        getContentPane().add(getScrollPane());
        getContentPane().add(getGoBtn());
    }

    private ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }

    public JLabel getRemisierCodeLbl() {
        if (null == remisierCodeLbl) {
            remisierCodeLbl = new JLabel();
            remisierCodeLbl.setText("Remisier code");
            remisierCodeLbl.setBounds(10, 10, 90, 20);
            WidgetProperties.setLabelProperties(remisierCodeLbl);
        }
        return remisierCodeLbl;
    }

    public JTextField getRemisierCodeTxf() {
        if (null == remisierCodeTxf) {
            remisierCodeTxf = new JTextField();
            remisierCodeTxf.setBounds(105, 10, 145, 20);
            remisierCodeTxf.setName(getRemisierCodeLbl().getName());
        }
        return remisierCodeTxf;
    }

    public JButton getNewBtn() {
        if (newBtn == null) {
            newBtn = new JButton();
            newBtn.setText("New");
            newBtn.setBounds(10, 235, 93, 23);
        }
        return newBtn;
    }

    public JButton getEditBtn() {
        if (editBtn == null) {
            editBtn = new JButton();
            editBtn.setText("Edit");
            editBtn.setBounds(113, 235, 93, 23);
        }
        return editBtn;
    }

    public JButton getDeleteBtn() {
        if (deleteBtn == null) {
            deleteBtn = new JButton();
            deleteBtn.setText("Delete");
            deleteBtn.setBounds(219, 235, 93, 23);
        }
        return deleteBtn;
    }

    public JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setBounds(0, 36, 740, 193);
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

    public JButton getGoBtn() {
        if (goBtn == null) {
            goBtn = new JButton();
            goBtn.setText("GO");
            goBtn.setBounds(255, 10, 50, 20);
        }
        return goBtn;
    }
}
