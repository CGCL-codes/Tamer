package anzeige.basics;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Anzeigen eines Textfeldes mit Beschriftung
 * Kann auch ein Passwort sein 
 */
public class TextPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    JLabel label;

    JTextField text;

    JPasswordField passwort;

    boolean pass;

    public TextPanel(String bezeichnung) {
        this(bezeichnung, "");
    }

    public TextPanel(String bezeichnung, String startwert) {
        super(new GridLayout(1, 2));
        label = new JLabel(" " + bezeichnung + ":");
        text = new JTextField(40);
        text.setText(startwert);
        passwort = new JPasswordField(40);
        passwort.setText(startwert);
        add(label, "West");
        add(text, "Center");
    }

    public void setPasswort(boolean passWort) {
        if (passWort == pass) return;
        pass = passWort;
        if (passWort) {
            remove(text);
            add(passwort, "Center");
        } else {
            remove(passwort);
            add(text, "Center");
        }
    }

    public void setText(String inhalt) {
        text.setText(inhalt);
        passwort.setText(inhalt);
    }

    public String toString() {
        if (pass) {
            return new String(passwort.getPassword());
        } else {
            return text.getText();
        }
    }
}
