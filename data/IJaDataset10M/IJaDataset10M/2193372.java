package mail.gui;

import java.awt.Dimension;
import javax.swing.*;
import mail.*;
import mail.node.ID;

public class MailStoreUI extends JFrame {

    private static final long serialVersionUID = 1347965910498977868L;

    public MailStoreUI(MailStore store) {
        JTextArea text = new JTextArea();
        text.setEditable(false);
        for (ID d : store.keySet()) {
            text.append(d.send() + "\n");
            for (Mail m : store.get(d)) {
                text.append("    " + m.getID() + "\n");
            }
        }
        getContentPane().add(text);
        setMinimumSize(new Dimension(200, 200));
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
