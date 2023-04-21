package intranetchatv3.display;

import intranetchatv3.df.User;
import intranetchatv3.df.VariableStore;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Philip
 */
public class userListRenderer extends JLabel implements ListCellRenderer {

    private VariableStore values;

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        values = VariableStore.getInstance();
        User u = (User) value;
        setText(u.getName());
        if (isSelected) {
            setForeground(Color.BLUE);
        } else {
            setForeground(list.getForeground());
        }
        if (u.getID().compareTo(values.networkID + "") == 0) {
            setFont(list.getFont().deriveFont(Font.BOLD));
        } else {
            setFont(list.getFont());
        }
        return this;
    }
}
