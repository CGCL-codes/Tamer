package client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class BuddyCellRenderer extends JPanel implements ListCellRenderer {

    private ImageIcon onlineIndicator, shareFilesIndicator;

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Vector v = (Vector) value;
        boolean isOnline = (Boolean) v.get(0);
        boolean sharesFiles = (Boolean) v.get(1);
        String name = (String) v.get(2);
        String state = (String) v.get(3);
        this.setLayout(new BorderLayout());
        Color c;
        ImageIcon stateIcon;
        JPanel rootPane = new JPanel(new BorderLayout());
        JPanel textPane = new JPanel(new BorderLayout());
        JLabel userLabel = new JLabel(name);
        JLabel stateMsg = new JLabel(state);
        textPane.add(userLabel, BorderLayout.CENTER);
        textPane.add(stateMsg, BorderLayout.SOUTH);
        this.setLayout(new BorderLayout());
        if (isOnline) stateIcon = new ImageIcon("client/img/online.png"); else stateIcon = new ImageIcon("client/img/offline.png");
        if (isSelected || cellHasFocus) c = Color.YELLOW; else c = Color.WHITE;
        rootPane.setBackground(c);
        textPane.setBackground(c);
        rootPane.add(new JLabel(stateIcon), BorderLayout.WEST);
        rootPane.add(textPane, BorderLayout.CENTER);
        return rootPane;
    }
}
