package view.font;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.text.Document;

public class TextFieldF extends JTextField {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public TextFieldF() {
        setFont(new Font("Eras Light ITC", Font.PLAIN, 12));
        setBackground(Color.GRAY);
        setForeground(Color.white);
    }

    public TextFieldF(final String text) {
        super(text);
        setFont(new Font("Eras Light ITC", Font.PLAIN, 12));
        setBackground(Color.GRAY);
        setForeground(Color.WHITE);
    }

    public TextFieldF(final int columns) {
        super(columns);
        setFont(new Font("Eras Light ITC", Font.PLAIN, 12));
        setBackground(Color.GRAY);
        setForeground(Color.WHITE);
    }

    public TextFieldF(final String text, final int columns) {
        super(text, columns);
        setFont(new Font("Eras Light ITC", Font.PLAIN, 12));
        setBackground(Color.GRAY);
        setForeground(Color.WHITE);
    }

    public TextFieldF(final Document doc, final String text, final int columns) {
        super(doc, text, columns);
        setBackground(Color.GRAY);
        setForeground(Color.WHITE);
    }
}
