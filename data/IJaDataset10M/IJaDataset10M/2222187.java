package view.componentes;

import java.awt.Font;
import javax.swing.JLabel;

public class JLabelX extends JLabel {

    private static final long serialVersionUID = 1L;

    public JLabelX(String title) {
        this.setFont(new Font("", Font.PLAIN, 10));
        this.setText(title);
    }
}
