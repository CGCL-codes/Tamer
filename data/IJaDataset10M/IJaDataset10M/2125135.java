package GUI;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JButton;
import Cliente.FachadaCliente;
import java.awt.Point;
import java.rmi.RemoteException;

public class ExpulsarJugador extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JButton jbAceptar = null;

    private JLabel jlFondo = new JLabel();

    private JFrame FrameAnterior;

    private JLabel fondo;

    private JLabel sonido;

    private JLabel cerrar;

    /**
	 * This is the default constructor
	 */
    public ExpulsarJugador() {
        super();
        initialize();
        jlFondo.setIcon(new ImageIcon("Imagenes//Fondos//juego.png"));
        jContentPane.add(jlFondo);
        jlFondo.setBounds(0, 0, 453, 356);
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(453, 356);
        this.setContentPane(getJContentPane());
        this.setTitle("Salir");
        this.setLocation(new Point(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 453 / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 356 / 2));
        this.setUndecorated(true);
        this.setVisible(true);
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJbAceptar(), null);
        }
        return jContentPane;
    }

    /**
	 * This method initializes jbAceptar	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJbAceptar() {
        if (jbAceptar == null) {
            jbAceptar = new JButton();
            jbAceptar.setText("ACEPTAR");
            jbAceptar.setLocation(new Point(166, 240));
            jbAceptar.setSize(new Dimension(121, 36));
            jbAceptar.setFont(new Font("Cheap Fire", Font.BOLD, 18));
            jbAceptar.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.exit(0);
                }
            });
        }
        return jbAceptar;
    }

    /**
	 * This method initializes jbNo	
	 * 	
	 * @return javax.swing.JButton	
	 */
    public JFrame getFrame() {
        return this;
    }
}
