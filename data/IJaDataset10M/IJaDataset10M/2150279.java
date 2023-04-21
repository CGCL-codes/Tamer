package fairVote.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ErrorPanel extends JPanel {

    public static final long serialVersionUID = 0;

    public ErrorPanel(String err) {
        setBackground(Color.RED);
        setLayout(new BorderLayout());
        JButton msg = new JButton(err);
        add(msg, BorderLayout.CENTER);
    }
}
