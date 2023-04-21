package tethyspict.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import tethyspict.config.Config;
import tethyspict.database.ThumbDatabase;
import tethyspict.gui.TethysFrame;

public class TaggerPictureListCellRenderer implements ListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        JLabel lbl = new JLabel();
        lbl.setVerticalTextPosition(JLabel.BOTTOM);
        lbl.setHorizontalTextPosition(JLabel.CENTER);
        lbl.setHorizontalAlignment(JLabel.CENTER);
        lbl.setVerticalAlignment(JLabel.CENTER);
        lbl.setOpaque(true);
        if (isSelected) {
            lbl.setBackground(list.getSelectionBackground());
            lbl.setForeground(list.getSelectionForeground());
        } else {
            lbl.setBackground(list.getBackground());
            lbl.setForeground(list.getForeground());
        }
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        DatabasePicture pic = (DatabasePicture) value;
        ImageIcon i = null;
        JPanel south = new JPanel();
        south.setLayout(new BorderLayout());
        try {
            i = ThumbDatabase.getThumbDB().getIcon(pic.getId());
        } catch (SQLException e) {
            TethysFrame.displaySQLException(e);
        }
        if (pic.canReadFile() != null) lbl.setText(pic.getLastPathComponent()); else lbl.setText("<html><font color=red>" + pic.getLastPathComponent() + "</font></html>");
        if (Config.getConfig().getShowTags()) south.add(this.getTagsPanel(pic, isSelected, list), BorderLayout.CENTER);
        if (Config.getConfig().getShowIndirectTags()) south.add(this.getInheritedTagsPnale(pic, isSelected, list), BorderLayout.SOUTH);
        lbl.setIcon(i);
        p.setBorder(loweredetched);
        p.add(lbl, BorderLayout.CENTER);
        p.add(south, BorderLayout.SOUTH);
        return p;
    }

    private JPanel getTagsPanel(DatabasePicture pic, boolean isSelected, JList list) {
        DefaultListModel tagsModel = new DefaultListModel();
        JList tagsList = new JList(tagsModel);
        JLabel lblTags = new JLabel("Tags:");
        JPanel p = new JPanel();
        tagsList.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        if (isSelected) {
            Color bg = list.getSelectionBackground();
            Color fg = list.getSelectionForeground();
            tagsList.setBackground(bg);
            tagsList.setForeground(fg);
            lblTags.setBackground(bg);
            lblTags.setForeground(fg);
            p.setBackground(bg);
            p.setForeground(fg);
        } else {
            Color bg = list.getBackground();
            Color fg = list.getForeground();
            tagsList.setBackground(bg);
            tagsList.setForeground(fg);
            lblTags.setBackground(bg);
            lblTags.setForeground(fg);
            p.setBackground(bg);
            p.setForeground(fg);
        }
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        lblTags.setAlignmentX(Component.LEFT_ALIGNMENT);
        tagsList.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lblTags);
        p.add(Box.createHorizontalGlue());
        p.add(tagsList);
        for (Tag t : pic.getTags()) {
            tagsModel.addElement(t);
        }
        p.add(tagsList, BorderLayout.CENTER);
        return p;
    }

    private JPanel getInheritedTagsPnale(DatabasePicture pic, boolean isSelected, JList list) {
        DefaultListModel inheritedTagsModel = new DefaultListModel();
        JList tagsList = new JList(inheritedTagsModel);
        JLabel lblTags = new JLabel("Inherited tags:");
        JPanel p = new JPanel();
        return p;
    }
}
