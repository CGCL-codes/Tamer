package algoritmos;

import javax.swing.JOptionPane;

public class Error {

    public static void emiteError(String arg0) {
        JOptionPane.showMessageDialog(null, new String("Error: " + arg0), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
