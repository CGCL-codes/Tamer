package flowmap;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import edu.stanford.hci.flowmap.model.UserOptions;

/**
 * Allows the user to choose which scaling function to use
 * @author dphan
 */
public class ScaleChooserPanel extends JPanel implements ActionListener {

    private JRadioButton linearButton, polyButton, logButton;

    private ButtonGroup bg;

    private UserOptions userOptions;

    public static final String linear = "Linear";

    public static final String log = "Log";

    public static final String poly = "Poly";

    public ScaleChooserPanel(UserOptions userOptions) {
        super();
        this.setBackground(Color.white);
        this.userOptions = userOptions;
        linearButton = new JRadioButton(linear);
        linearButton.setActionCommand(linear);
        linearButton.setBackground(Color.white);
        logButton = new JRadioButton(log);
        logButton.setActionCommand(log);
        logButton.setBackground(Color.white);
        polyButton = new JRadioButton(poly);
        polyButton.setActionCommand(poly);
        polyButton.setBackground(Color.white);
        linearButton.setSelected(true);
        userOptions.putBoolean(UserOptions.LINEAR_SCALE, linearButton.isSelected());
        userOptions.putBoolean(UserOptions.LOG_SCALE, logButton.isSelected());
        userOptions.putBoolean(UserOptions.POLY_SCALE, polyButton.isSelected());
        bg = new ButtonGroup();
        bg.add(linearButton);
        bg.add(logButton);
        bg.add(polyButton);
        this.add(linearButton);
        this.add(logButton);
        this.add(polyButton);
        linearButton.addActionListener(this);
        logButton.addActionListener(this);
        polyButton.addActionListener(this);
    }

    public String getSelected() {
        return bg.getSelection().getActionCommand();
    }

    public void actionPerformed(ActionEvent arg0) {
        JRadioButton radioB = (JRadioButton) arg0.getSource();
        if (radioB == linearButton) {
            userOptions.putBoolean(UserOptions.LINEAR_SCALE, radioB.isSelected());
            userOptions.putBoolean(UserOptions.LOG_SCALE, !radioB.isSelected());
            userOptions.putBoolean(UserOptions.POLY_SCALE, !radioB.isSelected());
        } else if (radioB == logButton) {
            userOptions.putBoolean(UserOptions.LINEAR_SCALE, !radioB.isSelected());
            userOptions.putBoolean(UserOptions.LOG_SCALE, radioB.isSelected());
            userOptions.putBoolean(UserOptions.POLY_SCALE, !radioB.isSelected());
        } else if (radioB == polyButton) {
            userOptions.putBoolean(UserOptions.LINEAR_SCALE, !radioB.isSelected());
            userOptions.putBoolean(UserOptions.LOG_SCALE, !radioB.isSelected());
            userOptions.putBoolean(UserOptions.POLY_SCALE, radioB.isSelected());
        }
    }
}
