package ssnavi.gui;

import java.awt.*;
import javax.swing.border.*;
import javax.swing.*;
import ssnavi.nv.*;

public class GuiTaskListPanel extends JPanel {

    GuiNavigatorControlListener m_listener;

    Border padBorder;

    GridLayout gridLayout1 = new GridLayout();

    public GuiTaskListPanel() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        padBorder = BorderFactory.createEmptyBorder(4, 4, 4, 4);
        this.setLayout(gridLayout1);
        this.setBorder(padBorder);
        gridLayout1.setColumns(1);
        gridLayout1.setRows(0);
        gridLayout1.setVgap(4);
    }

    void addTaskOption(NvOption option) {
        GuiTaskItemPanel itemPanel = new GuiTaskItemPanel();
        itemPanel.setTaskOption(option);
        itemPanel.setNavigatorControlListener(m_listener);
        gridLayout1.setRows(gridLayout1.getRows() + 1);
        this.add(itemPanel);
    }

    int getTaskOptionCount() {
        return this.getComponentCount();
    }

    NvOption getTaskOption(int i) {
        return getTaskOptionItem(i).getTaskOption();
    }

    boolean isSelected(int i) {
        return getTaskOptionItem(i).isSelected();
    }

    private GuiTaskItemPanel getTaskOptionItem(int i) {
        return (GuiTaskItemPanel) this.getComponent(i);
    }

    public void setNavigatorControlListener(GuiNavigatorControlListener listener) {
        m_listener = listener;
    }
}
