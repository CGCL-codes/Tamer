package view.mainButton;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ButtonQuit extends JButton {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ButtonQuit() {
        super("Exit ");
        setMargin(new Insets(0, 0, 0, 0));
        initialize();
        setToolTipText("Exit game ");
    }

    private void initialize() {
        setIcon(new ImageIcon("custom/images/icons/Close_24x24-32.png"));
    }

    public void addActionListenerQuit(ActionListener quit) {
        addActionListener(quit);
    }
}
