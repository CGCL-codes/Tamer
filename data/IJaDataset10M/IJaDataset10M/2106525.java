package horcher;

import java.util.ResourceBundle;
import javax.swing.JOptionPane;

public class Fehler extends Exception {

    private static final long serialVersionUID = 1L;

    String code;

    public Fehler(final String code) {
        this.code = code;
    }

    public void show(final ResourceBundle r) {
        JOptionPane.showMessageDialog(null, r.getString(this.code), r.getString("Fehler"), JOptionPane.ERROR_MESSAGE);
        this.printStackTrace();
    }
}
