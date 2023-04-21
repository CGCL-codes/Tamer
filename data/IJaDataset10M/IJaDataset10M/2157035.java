package com.simpledata.bc.uitools;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import com.simpledata.bc.Resources;

/**
 * This class is meant to comport itself as a JTabbedPane but
 * having the tab themselves stored in a JComboBox
 */
public class ComboTabbedPane extends JPanel {

    private ArrayList tabLabels;

    private ArrayList tabComponents;

    private ArrayList toolTips;

    ComboRenderer myRenderer;

    public ComboTabbedPane() {
        super();
        this.tabLabels = new ArrayList();
        this.tabComponents = new ArrayList();
        this.toolTips = new ArrayList();
        myRenderer = new ComboRenderer(this);
        initComponents();
    }

    private JPanel comboPanel;

    private JComboBox tabCombo;

    private JPanel tabPanel;

    private void initComponents() {
        comboPanel = new JPanel();
        tabCombo = new JComboBox();
        tabPanel = new JPanel(new BorderLayout());
        setLayout(new BorderLayout());
        comboPanel.setLayout(new BorderLayout());
        tabCombo.setRenderer(myRenderer);
        tabCombo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                tabComboActionPerformed();
            }
        });
        tabCombo.addPopupMenuListener(new PopupMenuListener() {

            public void popupMenuWillBecomeVisible(PopupMenuEvent evt) {
                myRenderer.setPopupMode(true);
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent evt) {
                myRenderer.setPopupMode(false);
            }

            public void popupMenuCanceled(PopupMenuEvent evt) {
            }
        });
        comboPanel.add(tabCombo, BorderLayout.CENTER);
        add(comboPanel, BorderLayout.NORTH);
        add(tabPanel, BorderLayout.CENTER);
    }

    void tabComboActionPerformed() {
        int selIndex = tabCombo.getSelectedIndex();
        tabPanel.removeAll();
        if (selIndex > -1) {
            tabPanel.add((Component) this.tabComponents.get(selIndex));
        }
        tabPanel.revalidate();
        tabPanel.repaint();
    }

    /**
     * Adds a component with a tab title defaulting to  the name of the 
     * component which is the result of calling  component.getName.
     */
    public Component add(Component c) {
        return this.add(c.getName(), c);
    }

    /**
     * Adds a component at the specified tab index with a tab  title 
     * defaulting to the name of the component.
     */
    public Component add(Component c, int index) {
        if ((index < 0) || (index > this.tabLabels.size())) {
            return null;
        }
        this.insertTab(c.getName(), null, c, "", index);
        return c;
    }

    /**
     * Adds a component with the specified tab title.
     */
    public Component add(String title, Component c) {
        this.insertTab(title, null, c, "", this.tabLabels.size());
        return c;
    }

    /**
     * Adds a component represented by a title  and no icon.
     */
    public void addTab(String title, Component c) {
        this.addTab(title, null, c);
    }

    /**
     * Adds a component represented by a title  and/or icon, either 
     * of which can be null.
     */
    public void addTab(String title, Icon icon, Component c) {
        this.insertTab(title, icon, c, "", this.tabLabels.size());
    }

    /**
     * Returns the currently selected component for this tabbedpane.
     */
    public Component getSelectedComponent() {
        int index = this.getSelectedIndex();
        if (index > -1) {
            return (Component) this.tabComponents.get(index);
        }
        return null;
    }

    /**
     * Returns the currently selected index for this tabbedpane.
     */
    public int getSelectedIndex() {
        return this.tabCombo.getSelectedIndex();
    }

    /**
     * Returns the number of tabs in this tabbedpane.
     */
    public int getTabCount() {
        return this.tabLabels.size();
    }

    /**
     * Inserts a component, at index,  represented by a title and/or icon,  
     * either of which may be null.
     */
    public void insertTab(String title, Icon icon, Component c, String tip, int index) {
        if ((c == null) || (index < 0) || (index > this.tabLabels.size())) {
            return;
        }
        JLabel jb = new JLabel(title);
        jb.setIcon(icon);
        tabLabels.add(index, jb);
        tabComponents.add(index, c);
        toolTips.add(index, tip);
        tabCombo.insertItemAt(jb, index);
        if (tabLabels.size() == 1) {
            tabCombo.setSelectedIndex(0);
        }
    }

    /**
     * Removes the specified Component from the  JTabbedPane.
     */
    public void remove(Component c) {
        int index = this.tabComponents.indexOf(c);
        this.remove(index);
    }

    /**
     * Removes the tab and component which corresponds to the specified index.
     */
    public void remove(int index) {
        if ((index < 0) || (index >= this.tabLabels.size())) {
            return;
        }
        this.tabCombo.remove(index);
        this.tabLabels.remove(index);
        this.tabComponents.remove(index);
        this.toolTips.remove(index);
    }

    /**
     * Removes all the tabs and their corresponding components  from the tabbedpane.
     */
    public void removeAll() {
        this.tabCombo.removeAllItems();
        this.tabLabels.clear();
        this.tabComponents.clear();
        this.toolTips.clear();
    }

    /**
     * Sets the icon at index to icon which can be  null.
     */
    public void setIconAt(int index, Icon icon) {
        JLabel jb = (JLabel) this.tabLabels.get(index);
        if (jb != null) {
            jb.setIcon(icon);
            this.tabCombo.revalidate();
        }
    }

    /**
     * Sets the selected component for this tabbedpane.
     */
    public void setSelectedComponent(Component c) {
        int index = this.tabComponents.indexOf(c);
        this.setSelectedIndex(index);
    }

    /**
     * Sets the selected index for this tabbedpane.
     */
    public void setSelectedIndex(int index) {
        if ((index > -1) && (index < this.tabLabels.size())) {
            this.tabCombo.setSelectedIndex(index);
        }
    }

    /**
     * Sets the title at index to title which  can be null.
     */
    public void setTitleAt(int index, String title) {
        JLabel jb = (JLabel) this.tabLabels.get(index);
        if (jb != null) {
            jb.setText(title);
            this.tabCombo.revalidate();
        }
    }

    /**
     * Sets the tooltip text at index to toolTipText  which can be null.
     */
    public void setToolTipTextAt(int index, String toolTipText) {
        if ((index > -1) && (index < this.tabLabels.size())) {
            this.toolTips.set(index, toolTipText);
        }
    }
}

class ComboRenderer implements ListCellRenderer {

    private boolean popupMode;

    private int selectedIndex;

    private ComboTabbedPane owner;

    private JLabel checkNotSelected;

    private JLabel checkSelected;

    private JLabel notCheckedNotSelected;

    public ComboRenderer(ComboTabbedPane ctb) {
        super();
        this.owner = ctb;
        this.popupMode = false;
        initComponents();
    }

    private void initComponents() {
        this.checkNotSelected = new JLabel();
        this.checkNotSelected.setIcon(Resources.tabbedPaneCheckedNotSelected);
        this.checkSelected = new JLabel();
        this.checkSelected.setIcon(Resources.tabbedPaneCheckedSelected);
        this.notCheckedNotSelected = new JLabel();
        this.notCheckedNotSelected.setPreferredSize(this.checkNotSelected.getPreferredSize());
    }

    public synchronized void setPopupMode(boolean b) {
        this.popupMode = b;
        this.selectedIndex = this.owner.getSelectedIndex();
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JPanel jp = new JPanel(new BorderLayout());
        JLabel tmp = (JLabel) value;
        JLabel jb = new JLabel();
        jb.setIcon(tmp.getIcon());
        jb.setText(tmp.getText());
        jb.setFont(tmp.getFont());
        if (isSelected) {
            jp.setBackground(list.getSelectionBackground());
            jp.setForeground(list.getSelectionForeground());
        } else {
            jp.setBackground(list.getBackground());
            jp.setForeground(list.getForeground());
        }
        if (popupMode) {
            if (isSelected) {
                jb.setForeground(list.getSelectionForeground());
            }
        }
        if (this.popupMode) {
            JLabel leftComponent;
            if (index == this.selectedIndex) {
                if (isSelected) {
                    if ((list.getForeground()).equals(list.getSelectionForeground())) {
                        leftComponent = this.checkNotSelected;
                    } else {
                        leftComponent = this.checkSelected;
                    }
                } else {
                    leftComponent = this.checkNotSelected;
                }
            } else {
                leftComponent = this.notCheckedNotSelected;
            }
            jp.add(leftComponent, BorderLayout.WEST);
            JLabel rightComponent = jb;
            jp.add(rightComponent, BorderLayout.CENTER);
            jp.setPreferredSize(new Dimension(leftComponent.getPreferredSize().width + rightComponent.getPreferredSize().width, Math.max(leftComponent.getPreferredSize().height, rightComponent.getPreferredSize().height)));
        } else {
            jp.add(jb, BorderLayout.CENTER);
            jp.setPreferredSize(jb.getPreferredSize());
        }
        return jp;
    }
}
