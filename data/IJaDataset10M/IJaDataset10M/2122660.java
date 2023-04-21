package vista;

import javax.swing.JOptionPane;

public class Mensaje {

    public static void mostrarAdvertencia(String msj) {
        JOptionPane.showMessageDialog(null, msj, "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
    }

    public static void mostrarError(String msj) {
        JOptionPane.showMessageDialog(null, msj, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean mostrarConfirmacion(String msj) {
        int opcion;
        boolean confirmacion;
        opcion = JOptionPane.showConfirmDialog(null, msj, "CONFIRMACION", JOptionPane.YES_NO_OPTION);
        if (opcion == 0) confirmacion = true; else confirmacion = false;
        return confirmacion;
    }

    public static void mostrarInformacion(String msj) {
        JOptionPane.showMessageDialog(null, msj, "INFORMACION", JOptionPane.INFORMATION_MESSAGE);
    }

    public static String mostrarMensajeEntrada(String msj) {
        return JOptionPane.showInputDialog(msj);
    }
}
