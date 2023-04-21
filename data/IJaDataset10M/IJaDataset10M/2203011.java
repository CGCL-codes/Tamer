package gui;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class JibView extends JPanel {

    private JLabel text = new JLabel("Jib : 12");

    private JSlider angle = new JSlider(SwingConstants.HORIZONTAL, -50, 50, 0);

    private JComboBox jib = new JComboBox();

    public JibView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(text);
        add(angle);
        add(jib);
    }
}
