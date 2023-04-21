package org.openXpertya.grid;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class Customize extends JDialog {

    /**
     * Constructor de la clase ...
     *
     *
     * @param frame
     */
    public Customize(Frame frame) {
        super(frame, "Customize ", true);
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Constructor de la clase ...
     *
     */
    public Customize() {
        this(null);
    }

    /** Descripción de Campos */
    private JPanel panel1 = new JPanel();

    /** Descripción de Campos */
    private BorderLayout borderLayout1 = new BorderLayout();

    /**
     * Descripción de Método
     *
     *
     * @throws Exception
     */
    void jbInit() throws Exception {
        panel1.setLayout(borderLayout1);
        getContentPane().add(panel1);
    }
}
