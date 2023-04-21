package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JDesktopPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.rmi.RemoteException;
import rmi.Cliente;
import negocio.InfoConductor;

@SuppressWarnings("unused")
public class ConsultarInformacionConductorFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JDesktopPane PanelConsultar = null;

    private JButton infraccion = null;

    private JButton bonificacion = null;

    private JButton salir = null;

    private JLabel DNI = null;

    private JLabel nombre = null;

    private JLabel fnac = null;

    private JLabel fcarnet = null;

    private JLabel pdisponibles = null;

    private JLabel dniI = null;

    private JLabel nombreI = null;

    private JLabel fnacI = null;

    private JLabel fcarnetI = null;

    private JLabel pdisponiblesI = null;

    /**
	 * This is the default constructor
	 */
    public ConsultarInformacionConductorFrame() {
        super();
        initialize();
        PanelConsultar.setBorder(new ImagenFondo());
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(800, 600);
        this.setContentPane(getPanelConsultar());
        this.setLocation(160, 100);
        getContentPane().setBackground(new java.awt.Color(15, 90, 130));
        this.setTitle("PuntoMatik - Gesti�n integrada de las infracciones que cometen los conductores ");
        this.setResizable(false);
    }

    /**
	 * This method initializes PanelConsultar	
	 * 	
	 * @return javax.swing.JDesktopPane	
	 */
    private JDesktopPane getPanelConsultar() {
        InfoConductor infoConductor = null;
        try {
            infoConductor = Cliente.objetoLocal.obtenerInformacionConductor();
        } catch (RemoteException e) {
            System.out.println("Error en Cliente.objetoLocal.obtenerInformacionConductor");
            e.printStackTrace();
        }
        if (PanelConsultar == null) {
            pdisponiblesI = new JLabel();
            pdisponiblesI.setBounds(new Rectangle(360, 350, 220, 50));
            pdisponiblesI.setText(String.valueOf(infoConductor.Puntos));
            pdisponiblesI.setFont(new java.awt.Font("Cambria", 1, 20));
            pdisponiblesI.setForeground(Color.red);
            fcarnetI = new JLabel();
            fcarnetI.setBounds(new Rectangle(405, 270, 220, 50));
            fcarnetI.setText(infoConductor.FechaCarnet);
            fcarnetI.setFont(new java.awt.Font("Cambria", 1, 20));
            fcarnetI.setForeground(Color.red);
            fnacI = new JLabel();
            fnacI.setBounds(new Rectangle(347, 190, 220, 50));
            fnacI.setText(infoConductor.FechaNacimiento);
            fnacI.setFont(new java.awt.Font("Cambria", 1, 20));
            fnacI.setForeground(Color.red);
            nombreI = new JLabel();
            nombreI.setBounds(new Rectangle(362, 110, 220, 50));
            nombreI.setText(infoConductor.Nombre);
            nombreI.setFont(new java.awt.Font("Cambria", 1, 20));
            nombreI.setForeground(Color.red);
            dniI = new JLabel();
            dniI.setBounds(new Rectangle(316, 30, 220, 50));
            dniI.setText(infoConductor.dni);
            dniI.setFont(new java.awt.Font("Cambria", 1, 20));
            dniI.setForeground(Color.red);
            pdisponibles = new JLabel();
            pdisponibles.setBounds(new Rectangle(87, 350, 220, 50));
            pdisponibles.setText("Puntos disponibles:");
            pdisponibles.setFont(new java.awt.Font("Cambria", 1, 23));
            pdisponibles.setForeground(Color.red);
            fcarnet = new JLabel();
            fcarnet.setBounds(new Rectangle(87, 270, 253, 50));
            fcarnet.setText("Fecha obtenci�n carnet:");
            fcarnet.setFont(new java.awt.Font("Cambria", 1, 23));
            fcarnet.setForeground(Color.red);
            fnac = new JLabel();
            fnac.setBounds(new Rectangle(87, 190, 220, 50));
            fnac.setText("Fecha nacimiento:");
            fnac.setFont(new java.awt.Font("Cambria", 1, 23));
            fnac.setForeground(Color.red);
            nombre = new JLabel();
            nombre.setBounds(new Rectangle(87, 110, 220, 50));
            nombre.setText("Nombre conductor:");
            nombre.setFont(new java.awt.Font("Cambria", 1, 23));
            nombre.setForeground(Color.red);
            DNI = new JLabel();
            DNI.setBounds(new Rectangle(87, 30, 220, 50));
            DNI.setText("DNI conductor:");
            DNI.setFont(new java.awt.Font("Cambria", 1, 23));
            DNI.setForeground(Color.red);
            PanelConsultar = new JDesktopPane();
            PanelConsultar.add(getInfraccion(), null);
            PanelConsultar.add(getBonificacion(), null);
            PanelConsultar.add(getSalir(), null);
            PanelConsultar.add(DNI, null);
            PanelConsultar.add(nombre, null);
            PanelConsultar.add(fnac, null);
            PanelConsultar.add(fcarnet, null);
            PanelConsultar.add(pdisponibles, null);
            PanelConsultar.add(dniI, null);
            PanelConsultar.add(nombreI, null);
            PanelConsultar.add(fnacI, null);
            PanelConsultar.add(fcarnetI, null);
            PanelConsultar.add(pdisponiblesI, null);
        }
        return PanelConsultar;
    }

    /**
	 * This method initializes infraccion	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getInfraccion() {
        if (infraccion == null) {
            infraccion = new JButton();
            infraccion.setBounds(new Rectangle(28, 466, 215, 48));
            infraccion.setText("HISTORIAL INFRACCIONES");
            infraccion.setFont(new java.awt.Font("CASTELLAR", 1, 11));
            infraccion.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    dispose();
                    ConsultarInfraccionesConductorFrame infra = new ConsultarInfraccionesConductorFrame();
                    infra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    infra.setVisible(true);
                }
            });
        }
        return infraccion;
    }

    /**
	 * This method initializes bonificacion	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getBonificacion() {
        if (bonificacion == null) {
            bonificacion = new JButton();
            bonificacion.setBounds(new Rectangle(274, 466, 235, 48));
            bonificacion.setText("HISTORIAL BONIFICACIONES");
            bonificacion.setFont(new java.awt.Font("CASTELLAR", 1, 11));
            bonificacion.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    dispose();
                    consultarBonificacionesConductorFrame bon = new consultarBonificacionesConductorFrame();
                    bon.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    bon.setVisible(true);
                }
            });
        }
        return bonificacion;
    }

    /**
	 * This method initializes salir	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getSalir() {
        if (salir == null) {
            salir = new JButton();
            salir.setBounds(new Rectangle(542, 467, 213, 48));
            salir.setText("SALIR");
            salir.setFont(new java.awt.Font("CASTELLAR", 1, 20));
            salir.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    System.exit(0);
                }
            });
        }
        return salir;
    }
}
