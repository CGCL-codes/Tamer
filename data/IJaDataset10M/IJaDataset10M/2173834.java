package app.vista;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import app.controlador.ControladorEnvioATienda;
import app.controlador.ControladorNuevoArticulo;
import app.controlador.ControladorReposicionArticulosDeFabrica;
import app.controlador.ControladorSolicitudDeDistribucion;
import app.controlador.ControladorSolicitudDeFabricacion;
import app.modelo.BusinessdelegateCD;

public class Mainframe extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JButton btnSolDist;

    private JButton btnEnvT;

    private JButton btnSolFabr;

    private JButton btnRepAF;

    private JButton btnNuevoArt;

    private JButton btnSalir;

    @SuppressWarnings("unused")
    private final BusinessdelegateCD bd;

    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            new Mainframe();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Mainframe() {
        bd = new BusinessdelegateCD();
        componentes();
        propiedades();
        this.setVisible(true);
    }

    public void componentes() {
        Container c = this.getContentPane();
        c.setLayout(new GridLayout(2, 1, 0, 0));
        c.setSize(new Dimension(320, 129));
        JPanel izquierdo = new JPanel();
        ImageIcon logoZara = new ImageIcon("./data/logo.jpg", "ZARA");
        JLabel etiqueta = new JLabel(logoZara);
        izquierdo.add(etiqueta);
        c.add(izquierdo);
        izquierdo.setPreferredSize(new java.awt.Dimension(200, 80));
        JPanel derecho = new JPanel(new GridLayout(3, 2, 4, 4));
        c.add(derecho, derecho.getName());
        derecho.setPreferredSize(new java.awt.Dimension(607, 20));
        btnSolDist = new JButton("Recibir Solicitud de Distribucion");
        btnSolDist.addActionListener(this);
        btnSolDist.setActionCommand("btnSolDist");
        btnEnvT = new JButton("Generar Envios a Tienda");
        btnEnvT.addActionListener(this);
        btnEnvT.setActionCommand("btnEnvT");
        derecho.add(btnSolDist, btnSolDist.getName());
        derecho.add(btnEnvT);
        btnSolFabr = new JButton("Generar Solicitud de Fabricacion");
        btnSolFabr.addActionListener(this);
        btnSolFabr.setActionCommand("btnSolFabr");
        derecho.add(btnSolFabr);
        btnRepAF = new JButton("Recibir Reposicion de Articulos de Fabrica");
        btnRepAF.addActionListener(this);
        btnRepAF.setActionCommand("btnRepAF");
        derecho.add(btnRepAF);
        btnNuevoArt = new JButton("Recibir Informacion de Nuevo Articulo");
        btnNuevoArt.addActionListener(this);
        btnNuevoArt.setActionCommand("btnNuevoArt");
        derecho.add(btnNuevoArt);
        btnSalir = new JButton("Salir");
        btnSalir.addActionListener(this);
        btnSalir.setActionCommand("btnSalir");
        derecho.add(btnSalir);
    }

    public void propiedades() {
        setTitle("Centro de Distribucion Zara - Uade 2009");
        setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        setLocation(50, 50);
        setIconImage(new ImageIcon("./data/icon.jpg").getImage());
        setResizable(false);
        pack();
    }

    public void centrarPantalla() {
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
    }

    public void actionPerformed(ActionEvent e) {
        if ("btnSolDist".equals(e.getActionCommand())) {
            SolicitudDeDistribucion solicitudDeDistribucion = new SolicitudDeDistribucion(bd);
            @SuppressWarnings("unused") ControladorSolicitudDeDistribucion controladorSolicitudDeDistribucion = new ControladorSolicitudDeDistribucion(bd, solicitudDeDistribucion);
            solicitudDeDistribucion.inicializar();
        }
        if ("btnEnvT".equals(e.getActionCommand())) {
            EnvioATienda envioATienda = new EnvioATienda(bd);
            @SuppressWarnings("unused") ControladorEnvioATienda controladorEnvioATienda = new ControladorEnvioATienda(bd, envioATienda);
            envioATienda.inicializar();
        }
        if ("btnSolFabr".equals(e.getActionCommand())) {
            SolicitudDeFabricacion solicitudDeFabricacion = new SolicitudDeFabricacion(bd);
            @SuppressWarnings("unused") ControladorSolicitudDeFabricacion controladorSolicitudDeFabricacion = new ControladorSolicitudDeFabricacion(bd, solicitudDeFabricacion);
            solicitudDeFabricacion.inicializar();
        }
        if ("btnRepAF".equals(e.getActionCommand())) {
            ReposicionArticulosDeFabrica reposicionArticulosDeFabrica = new ReposicionArticulosDeFabrica(bd);
            @SuppressWarnings("unused") ControladorReposicionArticulosDeFabrica controladorReposicionArticulosDeFabrica = new ControladorReposicionArticulosDeFabrica(bd, reposicionArticulosDeFabrica);
            reposicionArticulosDeFabrica.inicializar();
        }
        if ("btnNuevoArt".equals(e.getActionCommand())) {
            NuevoArticulo nuevoArticulo = new NuevoArticulo(bd);
            @SuppressWarnings("unused") ControladorNuevoArticulo controladorNuevoArticulo = new ControladorNuevoArticulo(bd, nuevoArticulo);
            nuevoArticulo.inicializar();
        }
        if ("btnSalir".equals(e.getActionCommand())) {
            System.exit(0);
        }
    }
}
