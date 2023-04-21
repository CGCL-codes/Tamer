package Tetris_interface.components;

import Tetris_interface.Interface;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import online.util.PlayerDescriptor;

/**
 *
 * @author gustavo
 */
public class CellRendererWithImage extends JLabel implements ListCellRenderer {

    static ImageIcon offline = null;

    static ImageIcon playing = null;

    static ImageIcon online = null;

    public CellRendererWithImage(Interface i) {
        if (offline == null) offline = new ImageIcon(i.getClass().getResource("imgs/OFFLINE.png"));
        if (playing == null) playing = new ImageIcon(i.getClass().getResource("imgs/PLAYING.png"));
        if (online == null) online = new ImageIcon(i.getClass().getResource("imgs/ONLINE.png"));
    }

    /**
         * This is the only method defined by ListCellRenderer.
         * We just reconfigure the JLabel each time we're called.
         * @param list defines the list of the players.
         * @param value to be displayed.
         * @param index cell index.
         * @param isSelected informes if the cell is selected.
         * @param cellHasFocus the list and the cell have the focus.
         * @return
         */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        PlayerDescriptor player = (PlayerDescriptor) value;
        String s = value.toString();
        setText(s);
        switch(player.getState()) {
            case OFFLINE:
                setIcon(offline);
                break;
            case ONLINE:
                setIcon(online);
                break;
            case PLAYING:
                setIcon(playing);
                break;
        }
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }
}
