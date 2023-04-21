package es.phoneixs.prymd5.visual;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;

/**
 * Permite mostrar dialogos que contienen errores ocurridos.
 * 
 * @author Javier Alfonso
 * @version 1
 */
public class DialogoErrores extends JDialog implements ActionListener {

    /**
     * Indica que se quiere mostrar más información.
     */
    private static final String MAS_INFORMACION = "Más información";

    /**
     * Indica que se quiere cerrar el dialogo.
     */
    private static final String ACEPTAR = "Aceptar";

    /**
     * Crea una nueva ventana de error para mostrar el error indicado. Y lo hace
     * visible.
     * 
     * @param mensaje
     *            El mensaje que se mostrará para explicar lo ocurrido.
     * 
     * @param e
     *            El error que hay que mostrar (no puede ser null).
     * @param owner
     *            El propietario del dialogo.
     * @param modal
     *            Indica si será un dialogo modal o no.
     * @param title
     *            El título del dialogo.
     */
    public static void showError(String mensaje, Exception e, Frame owner, boolean modal, String title) {
        DialogoErrores dialogo = new DialogoErrores(mensaje, e, owner, modal, title);
        dialogo.pack();
        dialogo.setVisible(true);
        dialogo = null;
    }

    private JLabel texto;

    private JTextArea traza;

    private JButton btnMásInfo;

    private JScrollPane spTraza;

    /**
     * Crea un nuevo dialogo de errores.
     * 
     * @param mensaje
     *            El mensaje que se mostrará al usuario.
     * 
     * @param e
     *            El error que se quiere mostrar.
     * @param owner
     *            La ventana a la que pertenece el dialogo.
     * @param modal
     *            Si será un dialogo modal o no.
     * @param title
     *            El título del dialogo.
     */
    public DialogoErrores(String mensaje, Exception e, Frame owner, boolean modal, String title) {
        super(owner, title, modal);
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        this.texto = new JLabel(mensaje);
        this.btnMásInfo = new JButton("Más información");
        this.btnMásInfo.setToolTipText("Muestra más información sobre el error");
        this.btnMásInfo.addActionListener(this);
        this.btnMásInfo.setActionCommand(MAS_INFORMACION);
        this.btnMásInfo.setMnemonic(KeyEvent.VK_M);
        if (e != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            this.traza = new JTextArea(sw.toString());
        } else {
            this.traza = new JTextArea("Sin detalles");
        }
        this.traza.setToolTipText("Muestra la traza del error");
        this.spTraza = new JScrollPane(this.traza);
        this.spTraza.setVisible(false);
        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setToolTipText("Cierra el dialogo");
        btnAceptar.setActionCommand(ACEPTAR);
        btnAceptar.addActionListener(this);
        btnAceptar.setMnemonic(KeyEvent.VK_A);
        Component cv = Box.createHorizontalStrut(0);
        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(this.texto).addGroup(layout.createSequentialGroup().addComponent(this.btnMásInfo).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btnAceptar)).addComponent(this.spTraza).addComponent(cv));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(this.texto).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup().addComponent(this.btnMásInfo).addComponent(btnAceptar)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.spTraza).addComponent(cv));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        if (comando.equals(MAS_INFORMACION)) {
            this.másInfo(!this.spTraza.isVisible());
        } else if (comando.equals(ACEPTAR)) {
            this.cerrar();
        }
    }

    /**
     * Muestra u oculta la información detallada.
     * 
     * @param mas
     *            Indica si se quiere ocultar (<code>false</code>) o mostrar (
     *            <code>true</code>) más información.
     */
    private void másInfo(boolean mas) {
        if (mas) {
            this.btnMásInfo.setText("Más información");
            this.btnMásInfo.setToolTipText("Muestra más información sobre el error");
        } else {
            this.btnMásInfo.setText("Menos información");
            this.btnMásInfo.setToolTipText("Oculta la información adicional sobre el error");
        }
        this.spTraza.setVisible(mas);
        this.pack();
    }

    /**
     * Oculta el dialogo.
     */
    private void cerrar() {
        this.setVisible(false);
    }
}
