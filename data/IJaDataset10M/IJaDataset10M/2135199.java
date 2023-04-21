package javanese;

import java.awt.Dimension;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MyTextField extends JTextField {

    public MyTextField() {
        setPreferredSize(new Dimension(100, 20));
    }
}
