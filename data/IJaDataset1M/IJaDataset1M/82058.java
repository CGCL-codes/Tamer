package accpt;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JButton;

/**

Changes layout when clicked.

@author Matthew Pekar

**/
public class DynamicComponent extends JPanel {

    public int timesClicked;

    public DynamicComponent() {
        timesClicked = 0;
        add(buildDynamicButton());
    }

    protected JButton buildDynamicButton() {
        JButton but = new JButton("DoTheDynamic");
        but.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeAll();
                add(buildDynamicButton());
                revalidate();
                repaint(1);
                timesClicked += 1;
            }
        });
        return but;
    }
}
