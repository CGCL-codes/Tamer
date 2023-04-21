package com.leclercb.taskunifier.gui.components.plugins.list;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import com.leclercb.taskunifier.gui.api.plugins.Plugin;
import com.leclercb.taskunifier.gui.translations.Translations;

public class PluginListRenderer implements ListCellRenderer {

    private JPanel panel;

    private JLabel icon;

    private JLabel text;

    public PluginListRenderer() {
        this.panel = new JPanel(new BorderLayout());
        this.icon = new JLabel();
        this.text = new JLabel();
        this.icon.setOpaque(false);
        this.text.setOpaque(false);
        this.icon.setBorder(null);
        this.text.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.panel.add(this.icon, BorderLayout.EAST);
        this.panel.add(this.text, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Plugin plugin = (Plugin) value;
        String price = null;
        if (plugin.getPrice() == null || plugin.getPrice().length() == 0) {
            price = Translations.getString("plugin.price.free");
        } else {
            price = plugin.getPrice() + " (" + Translations.getString("plugin.price.free_trial") + ")";
        }
        StringBuffer text = new StringBuffer();
        text.append("<b>" + plugin.getName() + "</b> - " + plugin.getVersion() + "<br />");
        text.append(Translations.getString("plugin.author") + ": " + plugin.getAuthor() + "<br />");
        text.append(Translations.getString("plugin.price") + ": " + price);
        if (isSelected) {
            this.panel.setBackground(UIManager.getColor("List.selectionBackground"));
            this.text.setForeground(UIManager.getColor("List.selectionForeground"));
        } else {
            this.panel.setBackground(UIManager.getColor("List.background"));
            this.text.setForeground(UIManager.getColor("List.foreground"));
        }
        this.icon.setIcon(plugin.getLogo());
        this.text.setText("<html>" + text.toString() + "</html>");
        return this.panel;
    }
}
