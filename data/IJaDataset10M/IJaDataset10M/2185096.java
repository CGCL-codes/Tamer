package preprocessing.visual.presentationtier.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Milos Kovalcik. 
 * Class for setting parameter of Mark Missing Value component
 */
public class MissingValuePanel extends JPanel {

    public JTextField missingValueTF;

    /**
     * Creates and fills panel for setting parameters of component
     */
    public MissingValuePanel() {
        missingValueTF = new JTextField(15);
        JButton okBT = new JButton("OK");
        okBT.addActionListener(new OkAction());
        this.setLayout(new FlowLayout());
        this.add(new JLabel("Missing values as string:"));
        this.add(missingValueTF);
        this.add(okBT);
    }
}

class OkAction implements ActionListener {

    @SuppressWarnings("static-access")
    public void actionPerformed(ActionEvent ae) {
        final ComponentDialog cd = ((ComponentDialog) ((JButton) ae.getSource()).getParent().getParent().getParent().getParent().getParent());
        cd.dispose();
    }
}
