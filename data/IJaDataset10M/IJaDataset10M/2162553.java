package vista.ventanas;

import modelo.Taller;
import modelo.componente.neumaticos.*;
import modelo.componente.*;
import java.util.LinkedList;
import java.util.Iterator;
import javax.swing.*;
import modelo.componente.Componente;
import vista.imagenTramo.Imagen;
import vista.imagenTramo.Posicion;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;
import java.awt.Color;
import vista.visorDeImagenes.PanelVisorDeImagenes;
import vista.imagenAuto.imagenesDeComponentes.*;

public class VentanaTaller extends JFrame {

    private PanelComponente panelComponente = null;

    private modelo.Usuario usuario = null;

    private JPanel panelVisor = null;

    private JPanel panelBotones = null;

    private JScrollPane panelInfoInferior = null;

    private JTextArea textoInferior = null;

    private JTextArea textoInfoSuperior = null;

    private VentanaMenuPrincipal ventanaMenu = null;

    private Dimension dimensionImagenBoton = new Dimension(10, 10);

    private Taller taller = null;

    private AdministradorDeImagenesYEtiquetasDeComponentes administrador = null;

    private LinkedList<Componente> componentesEnOferta = null;

    public VentanaTaller(JFrame ventanaMenu, modelo.Usuario usuario) {
        JFrame.setDefaultLookAndFeelDecorated(false);
        this.setUsuario(usuario);
        this.getContentPane().setBackground(Color.black);
        this.taller = new Taller(this.usuario, this);
        this.setLayout(null);
        this.ventanaMenu = (VentanaMenuPrincipal) ventanaMenu;
        this.setSize(800, 700);
        this.setBackground(Color.black);
        this.setTitle("Taller - Car Tunnning Experience 2008 " + usuario.getNombre());
        this.setLocationRelativeTo(null);
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent e) {
                getVentanaMenu().setVisible(true);
                dispose();
            }
        });
        this.setResizable(false);
        this.setAlwaysOnTop(false);
        this.setVisible(false);
        this.crearPaneles();
        this.refrescarContenido();
        this.crearComponentesEnOferta();
        this.setVisible(true);
    }

    public void refrescarPanelVisorDeImagenes() {
        try {
            this.remove(panelVisor);
        } catch (Exception e) {
        }
        panelVisor = new PanelVisorDeImagenes(new Dimension((int) (this.getSize().width * 0.5), (int) (this.getSize().height * 0.40)), new Posicion(), "src//vista//imagenAuto//imagenes//DodgeViper");
        this.add(panelVisor);
    }

    public void crearPaneles() {
        this.textoInfoSuperior = new JTextArea();
        this.textoInfoSuperior.setBackground(Color.black);
        textoInfoSuperior.setForeground(Color.white);
        textoInfoSuperior.setEditable(false);
        this.add(this.textoInfoSuperior);
        Dimension dimensionInfo = new Dimension((int) (this.getSize().width * 0.5), 100);
        textoInfoSuperior.setBounds(dimensionInfo.width, 0, dimensionInfo.width, dimensionInfo.height);
        textoInfoSuperior.setVisible(true);
        this.panelBotones = new JPanel();
        panelBotones.setBackground(Color.black);
        this.add(panelBotones);
        this.textoInferior = new JTextArea();
        this.textoInferior.setBackground(Color.black);
        textoInferior.setForeground(Color.white);
        textoInferior.setEditable(false);
        textoInferior.setBounds(400, 100, dimensionInfo.width, 300);
        textoInferior.setVisible(true);
        this.panelInfoInferior = new JScrollPane(textoInferior);
        panelInfoInferior.setBounds(400, 100, dimensionInfo.width, 175);
        panelInfoInferior.setVisible(true);
        this.add(this.panelInfoInferior);
    }

    public void refrescarContenido() {
        this.administrador = new AdministradorDeImagenesYEtiquetasDeComponentes();
        this.refrescarPanelVisorDeImagenes();
        this.refrescarInfo();
        this.refrescarPanelBotones();
    }

    public void refrescarInfo() {
        this.textoInfoSuperior.setText(usuario.getAuto().toString() + '\n' + "Dinero Jugador: " + usuario.getDinero().toStringConUnidades());
    }

    public void refrescarPanelBotones() {
        panelBotones.removeAll();
        System.gc();
        panelBotones.setBounds(0, panelVisor.getHeight(), this.getWidth(), (int) (this.getHeight() * 0.55));
        panelBotones.setVisible(true);
        panelBotones.setLayout(new GridLayout(6, 6));
        LinkedList<Componente> lista = this.usuario.getAuto().obtenerComponentes();
        Iterator<Componente> it = lista.iterator();
        while (it.hasNext()) {
            this.agregarBoton(it.next());
        }
        JButton boton = new JButton("Menu Principal");
        boton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    getVentanaMenu().setVisible(true);
                    dispose();
                } catch (NullPointerException exception) {
                }
                ;
            }
        });
        boton.setBackground(Color.LIGHT_GRAY);
        this.panelBotones.add(boton);
    }

    public void actualizarTextoInferior(String cadena) {
        this.textoInferior.setText(cadena);
    }

    private void agregarBoton(Componente componente) {
        DatoClase dato = administrador.getDatoClase(componente.getClass());
        BotonComponente boton = new BotonComponente(componente, dato, this);
        boton.setVisible(true);
        this.panelBotones.add(boton);
    }

    public void pressBotonComponente(Componente componente, DatoClase dato) {
        try {
            this.remove(this.panelComponente);
            panelComponente.removeAll();
            panelComponente = null;
            System.gc();
        } catch (NullPointerException e) {
        }
        ;
        Dimension dimension = new Dimension(this.panelBotones.getSize());
        this.panelBotones.setVisible(false);
        this.panelComponente = new PanelComponente(componente, dato, dimension, this);
        this.add(panelComponente);
        panelComponente.setVisible(true);
    }

    public void pressBotonOferta(Componente componenteActual, Componente componenteNuevo) {
        this.taller.reemplazo(componenteActual, componenteNuevo);
    }

    public void setUsuario(modelo.Usuario usuario) {
        this.usuario = usuario;
    }

    /**
	 * @return the panelComponente
	 */
    public PanelComponente getPanelComponente() {
        return panelComponente;
    }

    /**
	 * @param panelComponente the panelComponente to set
	 */
    public void setPanelComponente(PanelComponente panelComponente) {
        this.panelComponente = panelComponente;
    }

    /**
	 * @return the panelBotones
	 */
    public JPanel getPanelBotones() {
        return panelBotones;
    }

    /**
	 * @param panelBotones the panelBotones to set
	 */
    public void setPanelBotones(JPanel panelBotones) {
        this.panelBotones = panelBotones;
    }

    /**
	 * @return the ventanaMenu
	 */
    public JFrame getVentanaMenu() {
        return ventanaMenu;
    }

    /**
	 * @param ventanaMenu the ventanaMenu to set
	 */
    public void setVentanaMenu(VentanaMenuPrincipal ventanaMenu) {
        this.ventanaMenu = ventanaMenu;
    }

    /**
	 * @return the administrador
	 */
    public AdministradorDeImagenesYEtiquetasDeComponentes getAdministrador() {
        return administrador;
    }

    /**
	 * @param administrador the administrador to set
	 */
    public void setAdministrador(AdministradorDeImagenesYEtiquetasDeComponentes administrador) {
        this.administrador = administrador;
    }

    /**
	 * @return the usuario
	 */
    public modelo.Usuario getUsuario() {
        return usuario;
    }

    /**
	 * @return the panelVisor
	 */
    public JPanel getPanelVisor() {
        return panelVisor;
    }

    /**
	 * @param panelVisor the panelVisor to set
	 */
    public void setPanelVisor(JPanel panelVisor) {
        this.panelVisor = panelVisor;
    }

    /**
	 * @return the dimensionImagenBoton
	 */
    public Dimension getDimensionImagenBoton() {
        return dimensionImagenBoton;
    }

    /**
	 * @param dimensionImagenBoton the dimensionImagenBoton to set
	 */
    public void setDimensionImagenBoton(Dimension dimensionImagenBoton) {
        this.dimensionImagenBoton = dimensionImagenBoton;
    }

    /**
	 * @return the taller
	 */
    public Taller getTaller() {
        return taller;
    }

    /**
	 * @param taller the taller to set
	 */
    public void setTaller(Taller taller) {
        this.taller = taller;
    }

    /**
	 * @return the componentesEnOfeta
	 */
    public LinkedList<Componente> getComponentesEnOfeta() {
        return componentesEnOferta;
    }

    /**
	 * @param componentesEnOfeta the componentesEnOfeta to set
	 */
    public void setComponentesEnOfeta(LinkedList<Componente> componentesEnOfeta) {
        this.componentesEnOferta = componentesEnOfeta;
    }

    private void agregarListaDeComponentesEnOferta(LinkedList<Componente> lista) {
        try {
            Iterator<Componente> it = lista.iterator();
            while (it.hasNext()) try {
                this.componentesEnOferta.add(it.next());
            } catch (NullPointerException e) {
            }
            ;
        } catch (NullPointerException e) {
        }
        ;
    }

    private void crearComponentesEnOferta() {
        this.componentesEnOferta = new LinkedList<Componente>();
        this.agregarListaDeComponentesEnOferta(Automatica.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(Carburador.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(Carroceria.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(Combustible.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(Eje.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(Embrague.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(Escape.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(Freno.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(FrenoABS.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(FrenoCinta.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(FrenoDisco.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(Inyeccion.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(Llanta.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(Manual.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(Motor.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(Nitro.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(Secuencial.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(SistemaDeRefrigeracion.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(Suspension.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(Turbo.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(NeumaticoInvierno.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(NeumaticoMixto.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(NeumaticoLluvia.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(NeumaticoSlick.createVariosComponentesDistintos());
        this.agregarListaDeComponentesEnOferta(NeumaticoTodoTerreno.createVariosComponentesDistintos());
    }

    /**
	 * @return the textoInferior
	 */
    public JTextArea getTextoInferior() {
        return textoInferior;
    }

    /**
	 * @param textoInferior the textoInferior to set
	 */
    public void setTextoInferior(JTextArea textoInferior) {
        this.textoInferior = textoInferior;
    }

    /**
	 * @return the textoInfoSuperior
	 */
    public JTextArea getTextoInfoSuperior() {
        return textoInfoSuperior;
    }

    /**
	 * @param textoInfoSuperior the textoInfoSuperior to set
	 */
    public void setTextoInfoSuperior(JTextArea textoInfoSuperior) {
        this.textoInfoSuperior = textoInfoSuperior;
    }

    /**
	 * @return the panelInfoInferior
	 */
    public JScrollPane getPanelInfoInferior() {
        return panelInfoInferior;
    }

    /**
	 * @param panelInfoInferior the panelInfoInferior to set
	 */
    public void setPanelInfoInferior(JScrollPane panelInfoInferior) {
        this.panelInfoInferior = panelInfoInferior;
    }
}
