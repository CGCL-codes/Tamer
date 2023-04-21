package com.umc.gui.content;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import com.lowagie.text.Font;
import com.umc.ConfigController;
import com.umc.collector.Publisher;
import com.umc.gui.widgets.AbstractWidget;
import com.umc.gui.widgets.IWidget;
import com.umc.helper.UMCConstants;
import com.umc.helper.UMCLanguage;
import de.umcProject.xmlbeans.GuiDocument.Gui.Widgets.Widget;

/**
 * This class provides a {@link JList} with one checkbox for every item.
 * It will fill all available widgets to the list so the user can easily select the widgets
 * he wnats to the on the gui default view.
 * 
 * @author DonGyros
 *
 */
public class WidgetSelectionPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    WidgetPanel parent;

    public WidgetSelectionPanel(WidgetPanel parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        setMinimumSize(new Dimension(350, 200));
        setPreferredSize(new Dimension(350, 200));
        setBackground(new Color(40, 41, 59, 0));
        Map<String, AbstractWidget> widgetMap = Publisher.getInstance().getAllWidgets(false);
        Widget[] widgetsFromConfig = ConfigController.getInstance().getGuiConfig().getWidgets().getWidgetArray();
        CheckListItem[] cli = new CheckListItem[widgetMap.size()];
        int a = 0;
        CheckListItem checkListItem = null;
        for (IWidget iw : widgetMap.values()) {
            checkListItem = new CheckListItem(iw.getName());
            boolean found = false;
            for (Widget widget : widgetsFromConfig) {
                if (widget.getName().equals(iw.getName())) {
                    found = true;
                    break;
                }
            }
            checkListItem.setSelected(found);
            cli[a++] = checkListItem;
        }
        JList list = new JList(cli);
        list.setBackground(new Color(40, 41, 59, 0));
        list.setBorder(null);
        list.setForeground(Color.lightGray);
        list.setCellRenderer(new CheckListRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent event) {
                JList list = (JList) event.getSource();
                int index = list.locationToIndex(event.getPoint());
                CheckListItem item = (CheckListItem) list.getModel().getElementAt(index);
                item.setSelected(!item.isSelected());
                list.repaint(list.getCellBounds(index, index));
            }
        });
        JScrollPane sp = new JScrollPane(list);
        sp.setBorder(null);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(20, 8, 0, 4);
        add(sp, gbc);
        JButton btnCancel = new JButton(UMCLanguage.getText("WidgetSelectionPanel.btnCancel.text", ConfigController.getInstance().getSelectedLanguage()));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                WidgetSelectionPanel.this.parent.initWidgets();
            }
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.insets = new Insets(0, 4, 10, 4);
        add(btnCancel, gbc);
        JButton btnSave = new JButton(UMCLanguage.getText("WidgetSelectionPanel.btnSave.text", ConfigController.getInstance().getSelectedLanguage()));
        btnSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                ConfigController.getInstance().saveGuiConfig(UMCConstants.guiXmlPath);
                WidgetSelectionPanel.this.parent.initWidgets();
            }
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.insets = new Insets(0, 4, 10, 8);
        add(btnSave, gbc);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 10, 10);
        g2.setColor(UMCConstants.guiColor);
        g2.fillRoundRect(7, 7, getWidth() - 14, getHeight() - 14, 10, 10);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(5, 7, getWidth() - 10, 10, 10, 10);
        g2.fillRect(5, 8, getWidth() - 10, 10);
        g2.setColor(Color.DARK_GRAY);
        g2.drawString("Select widgets", 10, 16);
        g2.setColor(Color.darkGray);
        g2.fillOval(332, 7, 10, 10);
        g2.setFont(new java.awt.Font("HELVETICA", Font.BOLD, 11));
        g2.setColor(Color.white);
        g2.drawString("?", 334, 16);
    }

    private class CheckListItem {

        private String label;

        private boolean isSelected = false;

        public CheckListItem(String label) {
            this.label = label;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
            Widget[] widgetsFromConfig = ConfigController.getInstance().getGuiConfig().getWidgets().getWidgetArray();
            boolean found = false;
            if (isSelected) {
                for (int a = 0; a < widgetsFromConfig.length; a++) {
                    if (widgetsFromConfig[a].getName().equals(this.label)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Widget newWidget = ConfigController.getInstance().getGuiConfig().getWidgets().addNewWidget();
                    newWidget.setName(this.label);
                }
            } else {
                int widgetIndex = -1;
                Widget[] widgets = ConfigController.getInstance().getGuiConfig().getWidgets().getWidgetArray();
                for (int i = 0; i < widgets.length; i++) {
                    if (widgets[i].getName().equals(this.label)) {
                        widgetIndex = i;
                        break;
                    }
                }
                if (widgetIndex != -1) {
                    ConfigController.getInstance().getGuiConfig().getWidgets().removeWidget(widgetIndex);
                }
            }
        }

        public String toString() {
            return label;
        }
    }

    private class CheckListRenderer extends JCheckBox implements ListCellRenderer {

        private static final long serialVersionUID = 1L;

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
            setEnabled(list.isEnabled());
            setSelected(((CheckListItem) value).isSelected());
            setFont(list.getFont());
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            setText(value.toString());
            return this;
        }
    }
}
