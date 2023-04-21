package net.sourceforge.vigilog.ui.colorfilter;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventListModel;
import net.sourceforge.vigilog.ui.GuiUtilities;
import net.sourceforge.vigilog.ui.filter.ColorFilterGroup;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ColorFilterGroupsDialog extends JDialog {

    private static ResourceBundle bundle = ResourceBundle.getBundle("language");

    private static Logger log = Logger.getLogger(ColorFilterGroupsDialog.class);

    private JXList colorFilterGroupsList;

    private EventList<ColorFilterGroup> colorFilterGroups;

    private AddEditRemoveButtonPanel addEditRemoveButtonPanel;

    public ColorFilterGroupsDialog(Frame owner, java.util.List<ColorFilterGroup> colorFilterGroups) {
        super(owner, bundle.getString("color.filter.groups.dialog.title"), true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.colorFilterGroups = new BasicEventList<ColorFilterGroup>();
        if (colorFilterGroups != null) {
            this.colorFilterGroups.addAll(colorFilterGroups);
        }
        setContentPane(createContentPanel(this.colorFilterGroups));
        addWindowListener(new DisposeWindowAdapter());
    }

    public java.util.List<ColorFilterGroup> getColorFilterGroups() {
        java.util.List<ColorFilterGroup> result = null;
        if (colorFilterGroups != null) {
            result = new ArrayList<ColorFilterGroup>(colorFilterGroups.size());
            result.addAll(colorFilterGroups);
        }
        return result;
    }

    private Component createButtonPanel() {
        JPanel result = new JPanel();
        result.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton(bundle.getString("button.ok"));
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                log.debug("OK pressed...");
                ColorFilterGroupsDialog.this.dispose();
            }
        });
        result.add(okButton);
        JButton cancelButton = new JButton(bundle.getString("button.cancel"));
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                colorFilterGroups = null;
                ColorFilterGroupsDialog.this.dispose();
            }
        });
        result.add(cancelButton);
        GuiUtilities.makeButtonWidthEqual(okButton, cancelButton);
        return result;
    }

    private Component createCRUDButtonPanel() {
        addEditRemoveButtonPanel = new AddEditRemoveButtonPanel(new AddButtonActionListener(), new EditButtonActionListener(), new RemoveButtonActionListener());
        return addEditRemoveButtonPanel;
    }

    private JXList createColorFilterSetsList(EventList<ColorFilterGroup> colorFilterGroups) {
        EventListModel<ColorFilterGroup> issuesListModel = new EventListModel<ColorFilterGroup>(colorFilterGroups);
        colorFilterGroupsList = new JXList(issuesListModel);
        colorFilterGroupsList.setCellRenderer(new ColorFilterGroupsListCellRenderer());
        colorFilterGroupsList.getSelectionModel().addListSelectionListener(new GroupsListSelectionListener());
        colorFilterGroupsList.setPreferredSize(new Dimension(150, 50));
        return colorFilterGroupsList;
    }

    private JPanel createContentPanel(EventList<ColorFilterGroup> colorFilterGroups) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(7, 7));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.add(new JLabel(bundle.getString("color.filter.group.dialog.title")), BorderLayout.NORTH);
        panel.add(new JScrollPane(createColorFilterSetsList(colorFilterGroups)), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);
        panel.add(createCRUDButtonPanel(), BorderLayout.EAST);
        return panel;
    }

    private static class ColorFilterGroupsListCellRenderer extends DefaultListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setText(((ColorFilterGroup) value).getName());
            return label;
        }
    }

    private class AddButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ColorFilterGroupDialog dialog = new ColorFilterGroupDialog(ColorFilterGroupsDialog.this, new ColorFilterGroup());
            dialog.pack();
            dialog.setLocationRelativeTo(ColorFilterGroupsDialog.this);
            dialog.setVisible(true);
            if (dialog.getColorFilterGroup() != null) {
                log.debug("Added new group: " + dialog.getColorFilterGroup());
                colorFilterGroups.add(dialog.getColorFilterGroup());
                colorFilterGroupsList.validate();
                colorFilterGroupsList.repaint();
            }
        }
    }

    private class EditButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ColorFilterGroup colorFilterGroup = (ColorFilterGroup) colorFilterGroupsList.getSelectedValue();
            int oldIndex = colorFilterGroups.indexOf(colorFilterGroup);
            ColorFilterGroupDialog dialog = new ColorFilterGroupDialog(ColorFilterGroupsDialog.this, colorFilterGroup);
            dialog.pack();
            dialog.setLocationRelativeTo(ColorFilterGroupsDialog.this);
            dialog.setVisible(true);
            if (dialog.getColorFilterGroup() != null) {
                log.debug("Updated group: " + dialog.getColorFilterGroup());
                colorFilterGroups.remove(oldIndex);
                colorFilterGroups.add(oldIndex, dialog.getColorFilterGroup());
                colorFilterGroupsList.validate();
                colorFilterGroupsList.repaint();
            }
        }
    }

    private class RemoveButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ColorFilterGroup colorFilterGroup = (ColorFilterGroup) colorFilterGroupsList.getSelectedValue();
            colorFilterGroups.remove(colorFilterGroup);
            colorFilterGroupsList.validate();
            colorFilterGroupsList.repaint();
        }
    }

    private class DisposeWindowAdapter extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            colorFilterGroups = null;
        }
    }

    private class GroupsListSelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                addEditRemoveButtonPanel.getRemoveButton().setEnabled(colorFilterGroupsList.getSelectedIndex() != -1);
                addEditRemoveButtonPanel.getEditButton().setEnabled(colorFilterGroupsList.getSelectedIndex() != -1);
            }
        }
    }
}
