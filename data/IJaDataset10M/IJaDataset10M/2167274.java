package Sintactico.Exceptions;

import javax.swing.JOptionPane;

public class ParserException extends Exception {

    public ParserException(String mensaje) {
        super(mensaje);
        Sintactico.Sym.err = true;
        JOptionPane.showMessageDialog(null, mensaje, "Parser Error", 0);
    }

    public ParserException(String mensaje, int tipo, String titulo) {
        super(mensaje);
        Sintactico.Sym.err = true;
        JOptionPane.showMessageDialog(null, mensaje, titulo, tipo);
    }
}
