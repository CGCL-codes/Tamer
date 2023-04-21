package org.terentich.ox.dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <b>Project:</b> OX Framework <br>
 * <b>Description: </b> <br>
 * <b>Date: </b> 20.10.2008 <br>
 * @author Alexey V. Terentyev
 */
public class OXDialog extends JDialog {

    private JButton[] buttons;

    public OXDialog(JFrame _parent, String _title) {
        super(_parent, _title);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screen.height * 2 / 5;
        int width = screen.width * 2 / 7;
        int int_x = (_parent.getWidth() - width) / 2;
        int int_y = (_parent.getHeight() - height) / 2;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(width, height);
        setLocation(_parent.getX() + int_x, _parent.getY() + int_y);
    }

    public void setActions(JButton[] _buttons, Font _font) {
        buttons = _buttons;
        JPanel panelActions = new JPanel();
        for (JButton button : buttons) {
            button.setFont(_font);
            panelActions.add(button);
        }
        add(panelActions, BorderLayout.SOUTH);
    }

    public static void main(String[] _args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        JPanel panel = new JPanel();
        JButton button = new JButton("Show dialog example");
        panel.add(button);
        frame.add(panel, BorderLayout.SOUTH);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent _e) {
                OXDialog dialog = new OXDialog(frame, "Test dialog");
                dialog.setActions(new JButton[] { new JButton("OK"), new JButton("Cancel") }, new Font("Dialog Input", Font.PLAIN, 18));
                dialog.setVisible(true);
            }
        });
        frame.setVisible(true);
    }
}
