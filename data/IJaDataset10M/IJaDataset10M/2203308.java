package widget;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class WidgetUtils {

    public WidgetUtils() {
    }

    public JMenuItem getMenuItem(String caption) {
        JMenuItem mItem = new JMenuItem(caption);
        return mItem;
    }

    public JMenuItem getMenuItem(String caption, ActionListener oAListener) {
        JMenuItem mItem = new JMenuItem(caption);
        mItem.addActionListener(oAListener);
        return mItem;
    }

    public JMenuItem getMenuItem(String caption, ActionListener oAListener, int oKey) {
        JMenuItem mItem = new JMenuItem(caption);
        mItem.addActionListener(oAListener);
        mItem.setMnemonic(oKey);
        return mItem;
    }

    public JMenuItem getMenuItem(String caption, ActionListener oAListener, KeyStroke oKey) {
        JMenuItem mItem = new JMenuItem(caption);
        mItem.addActionListener(oAListener);
        mItem.setAccelerator(oKey);
        return mItem;
    }

    public JMenu getMenu(String caption) {
        JMenu menu = new JMenu(caption);
        return menu;
    }

    public JTextField getTextField() {
        JTextField tField = new JTextField();
        return tField;
    }

    public JPasswordField getPasswordField() {
        JPasswordField tField = new JPasswordField();
        return tField;
    }

    /**
	 * Method to return the dimension at which the Dialog should be placed, keeping
	 * it in the center of teh screen. 
	 */
    public static Dimension getCenterScreenDimension(Dimension frameSize) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension loc = new Dimension();
        loc.width = ((screenSize.width / 2) - (frameSize.width / 2));
        loc.height = ((screenSize.height / 2) - (frameSize.height / 2));
        return loc;
    }
}
