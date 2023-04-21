package ppine.utils;

import cytoscape.Cytoscape;
import javax.swing.JOptionPane;

public class Messenger {

    public static void message(Object message) {
        JOptionPane.showMessageDialog(Cytoscape.getDesktop(), message);
    }

    public static void error(Exception ex) {
        JOptionPane.showMessageDialog(Cytoscape.getDesktop(), ex.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
    }

    public static int confirmWarning(Object message) {
        return JOptionPane.showConfirmDialog(Cytoscape.getDesktop(), message, "Warning.", JOptionPane.WARNING_MESSAGE);
    }

    public static int confirmInfo(Object message) {
        return JOptionPane.showConfirmDialog(Cytoscape.getDesktop(), message, "Info.", JOptionPane.INFORMATION_MESSAGE);
    }
}
