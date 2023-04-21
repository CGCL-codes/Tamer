package code.from.internet;

import java.awt.event.*;
import javax.swing.*;

public class JListUtility {

    private static final KeyStroke ENTER = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);

    public static void addAction(JList source, Action action) {
        InputMap im = source.getInputMap();
        im.put(ENTER, ENTER);
        source.getActionMap().put(ENTER, action);
        source.addMouseListener(new ActionMouseListener());
    }

    static class ActionMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                JList list = (JList) e.getSource();
                Action action = list.getActionMap().get(ENTER);
                if (action != null) {
                    ActionEvent event = new ActionEvent(list, ActionEvent.ACTION_PERFORMED, "");
                    action.actionPerformed(event);
                }
            }
        }
    }

    public static void main(String[] args) {
        Action displayAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JList list = (JList) e.getSource();
                System.out.println(list.getSelectedValue());
            }
        };
        String[] data = { "zero", "one", "two", "three", "four", "five" };
        JList list = new JList(data);
        JListUtility.addAction(list, displayAction);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JScrollPane(list));
        frame.setSize(400, 100);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
