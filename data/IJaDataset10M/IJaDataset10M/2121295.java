package vistas;

import java.awt.GridBagLayout;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import titiritero.vista.Panel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import modelo.Juego;
import modelo.ObservadorDeJugador;
import modelo.TanqueAlgo;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionListener;
import java.awt.Color;

public class VistaJuego extends JFrame implements ObservadorDeJugador {

    private static final long serialVersionUID = -3210439722833694388L;

    private JPanel jContentPane = null;

    private JToolBar jJToolBarBar = null;

    private JMenuItem jMenuItem = null;

    private JMenuItem jMenuItem1 = null;

    private JMenuItem jMenuItem2 = null;

    private Panel panel = null;

    private Juego juego;

    private JPanel jPanelEstado = null;

    private JLabel jLabelPuntos;

    private JLabel jLabelVidas;

    private JLabel jLabelProyectiles;

    private JLabel jLabelArma;

    private JLabel valorProyectiles;

    private JLabel valorVidas;

    private JLabel valorPuntos;

    private JLabel valorEnergia;

    private JLabel valorTanquesDestruidos;

    private JLabel valorTanquesDelNivel;

    private JLabel valorArma;

    /**
	 * This is the default constructor
	 * @param juego 
	 */
    public VistaJuego(Juego juego) {
        super();
        this.juego = juego;
        this.setSize(674, 594);
        this.setContentPane(getJContentPane());
        this.setTitle("AlgoTank");
        this.getSuperficieDeDibujo().setFocusable(true);
        TanqueAlgo tanque = this.juego.getNivelActual().getEscenario().getAlgoTank();
        tanque.agregarObservador(this);
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(NORMAL);
            }
        });
    }

    public void agregarComienzoJuegoListener(ActionListener action) {
        jMenuItem.addActionListener(action);
    }

    public void agregarPausaJuegoListener(ActionListener action) {
        jMenuItem1.addActionListener(action);
    }

    /**
	 * This method initializes jJToolBarBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
    private JToolBar getJJToolBarBar() {
        if (jJToolBarBar == null) {
            jJToolBarBar = new JToolBar();
            jJToolBarBar.setFloatable(false);
            jJToolBarBar.setBounds(new Rectangle(238, 6, 261, 40));
            jJToolBarBar.add(getJMenuItem());
            jJToolBarBar.add(getJMenuItem1());
            jJToolBarBar.add(getJMenuItem2());
        }
        return jJToolBarBar;
    }

    /**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getJMenuItem() {
        if (jMenuItem == null) {
            jMenuItem = new JMenuItem();
            jMenuItem.setText("Jugar");
        }
        return jMenuItem;
    }

    /**
	 * This method initializes jMenuItem1	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getJMenuItem1() {
        if (jMenuItem1 == null) {
            jMenuItem1 = new JMenuItem();
            jMenuItem1.setText("Pausa");
        }
        return jMenuItem1;
    }

    private JMenuItem getJMenuItem2() {
        if (jMenuItem2 == null) {
            jMenuItem2 = new JMenuItem();
            jMenuItem2.setText("Acerca de");
            jMenuItem2.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    VentanaAcercaDe newFrame = new VentanaAcercaDe("Acerca de");
                    newFrame.pack();
                    newFrame.setVisible(true);
                }
            });
        }
        return jMenuItem2;
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    public Panel getSuperficieDeDibujo() {
        if (panel == null) {
            panel = new Panel(500, 500);
            panel.setForeground(Color.BLACK);
            panel.addKeyListener(new KeyAdapter() {
            });
            panel.setLayout(new GridBagLayout());
            panel.setBounds(new Rectangle(150, 52, 500, 500));
        }
        return panel;
    }

    /**
	 * This method initializes jPanelEstado	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanelEstado() {
        if (jPanelEstado == null) {
            jPanelEstado = new JPanel();
            GridBagLayout gbl_jPanelEstado = new GridBagLayout();
            gbl_jPanelEstado.columnWeights = new double[] { 1.0 };
            jPanelEstado.setLayout(gbl_jPanelEstado);
            jPanelEstado.setBounds(new Rectangle(17, 52, 130, 500));
            JLabel lblTanquesDelNivel = new JLabel("Tanques del nivel:");
            GridBagConstraints gbc_lblTanquesDelNivel = new GridBagConstraints();
            gbc_lblTanquesDelNivel.insets = new Insets(0, 0, 5, 0);
            gbc_lblTanquesDelNivel.gridx = 0;
            gbc_lblTanquesDelNivel.gridy = 0;
            jPanelEstado.add(lblTanquesDelNivel, gbc_lblTanquesDelNivel);
            valorTanquesDelNivel = new JLabel("0");
            GridBagConstraints gbc_valorTanquesDelNivel = new GridBagConstraints();
            gbc_valorTanquesDelNivel.insets = new Insets(0, 0, 5, 0);
            gbc_valorTanquesDelNivel.gridx = 0;
            gbc_valorTanquesDelNivel.gridy = 1;
            jPanelEstado.add(valorTanquesDelNivel, gbc_valorTanquesDelNivel);
            JLabel lblTanquesDestruidos = new JLabel("Tanques destruidos:");
            GridBagConstraints gbc_lblTanquesDestruidos = new GridBagConstraints();
            gbc_lblTanquesDestruidos.insets = new Insets(0, 0, 5, 0);
            gbc_lblTanquesDestruidos.gridx = 0;
            gbc_lblTanquesDestruidos.gridy = 2;
            jPanelEstado.add(lblTanquesDestruidos, gbc_lblTanquesDestruidos);
            valorTanquesDestruidos = new JLabel("0");
            GridBagConstraints gbc_label = new GridBagConstraints();
            gbc_label.insets = new Insets(0, 0, 5, 0);
            gbc_label.gridx = 0;
            gbc_label.gridy = 3;
            jPanelEstado.add(valorTanquesDestruidos, gbc_label);
            JLabel jLabelEnergia = new JLabel("Energia:");
            GridBagConstraints gbc_jLabelEnergia = new GridBagConstraints();
            gbc_jLabelEnergia.insets = new Insets(0, 0, 5, 0);
            gbc_jLabelEnergia.gridx = 0;
            gbc_jLabelEnergia.gridy = 4;
            jPanelEstado.add(jLabelEnergia, gbc_jLabelEnergia);
            valorEnergia = new JLabel("0");
            GridBagConstraints gbc_valorEnergia = new GridBagConstraints();
            gbc_valorEnergia.insets = new Insets(0, 0, 5, 0);
            gbc_valorEnergia.gridx = 0;
            gbc_valorEnergia.gridy = 5;
            jPanelEstado.add(valorEnergia, gbc_valorEnergia);
            gbc_jLabelEnergia.insets = new Insets(0, 0, 5, 0);
            gbc_jLabelEnergia.gridx = 0;
            gbc_jLabelEnergia.gridy = 2;
            jLabelProyectiles = new JLabel("Proyectiles:");
            GridBagConstraints gbc_jLabelProyectiles = new GridBagConstraints();
            gbc_jLabelProyectiles.insets = new Insets(0, 0, 5, 0);
            gbc_jLabelProyectiles.gridx = 0;
            gbc_jLabelProyectiles.gridy = 6;
            jPanelEstado.add(jLabelProyectiles, gbc_jLabelProyectiles);
            valorProyectiles = new JLabel("0");
            GridBagConstraints gbc_valorProyectiles = new GridBagConstraints();
            gbc_valorProyectiles.insets = new Insets(0, 0, 5, 0);
            gbc_valorProyectiles.gridx = 0;
            gbc_valorProyectiles.gridy = 7;
            jPanelEstado.add(valorProyectiles, gbc_valorProyectiles);
            GridBagConstraints gbc_jLabelPuntos = new GridBagConstraints();
            gbc_jLabelPuntos.insets = new Insets(0, 0, 5, 0);
            gbc_jLabelPuntos.gridx = 0;
            gbc_jLabelPuntos.gridy = 8;
            jLabelPuntos = new JLabel();
            jLabelPuntos.setText("Puntos:");
            jPanelEstado.add(jLabelPuntos, gbc_jLabelPuntos);
            valorPuntos = new JLabel("0");
            GridBagConstraints gbc_valorPuntos = new GridBagConstraints();
            gbc_valorPuntos.insets = new Insets(0, 0, 5, 0);
            gbc_valorPuntos.gridx = 0;
            gbc_valorPuntos.gridy = 9;
            jPanelEstado.add(valorPuntos, gbc_valorPuntos);
            jLabelVidas = new JLabel("Vidas restantes:");
            GridBagConstraints gbc_jLabelVidas = new GridBagConstraints();
            gbc_jLabelVidas.insets = new Insets(0, 0, 5, 0);
            gbc_jLabelVidas.gridx = 0;
            gbc_jLabelVidas.gridy = 10;
            jPanelEstado.add(jLabelVidas, gbc_jLabelVidas);
            valorVidas = new JLabel("0");
            GridBagConstraints gbc_valorVidas = new GridBagConstraints();
            gbc_valorVidas.gridx = 0;
            gbc_valorVidas.gridy = 11;
            jPanelEstado.add(valorVidas, gbc_valorVidas);
            jLabelArma = new JLabel("Arma:");
            GridBagConstraints gbc_jLabelArma = new GridBagConstraints();
            gbc_jLabelArma.insets = new Insets(0, 0, 5, 0);
            gbc_jLabelArma.gridx = 0;
            gbc_jLabelArma.gridy = 12;
            jPanelEstado.add(jLabelArma, gbc_jLabelArma);
            valorArma = new JLabel("Ametralladora");
            GridBagConstraints gbc_valorArma = new GridBagConstraints();
            gbc_valorArma.gridx = 0;
            gbc_valorArma.gridy = 13;
            jPanelEstado.add(valorArma, gbc_valorArma);
        }
        return jPanelEstado;
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
            jContentPane.add(getJJToolBarBar(), null);
            jContentPane.add(getSuperficieDeDibujo(), null);
            jContentPane.add(getJPanelEstado(), null);
        }
        return jContentPane;
    }

    @Override
    public void actualizarProyectilesJugador() {
        String proyectiles = Integer.toString(this.juego.getNivelActual().getJugador().getBalasRestantes());
        valorProyectiles.setText(proyectiles);
    }

    @Override
    public void actualizarVidasJugador() {
        String vidas = Integer.toString(this.juego.getNivelActual().getJugador().getVidas());
        valorVidas.setText(vidas);
    }

    @Override
    public void actualizarEnergiaJugador() {
        String energia = Double.toString(this.juego.getNivelActual().getJugador().getResistencia());
        valorEnergia.setText(energia);
    }

    public void actualizarArmaJugador() {
        String arma = this.juego.getNivelActual().getJugador().getArma().getTipoDeArma();
        valorArma.setText(arma);
    }

    @Override
    public void actualizarPuntosJugador() {
        String puntos = Integer.toString(this.juego.getNivelActual().getJugador().getPuntos());
        valorPuntos.setText(puntos);
    }

    @Override
    public void actualizarTanquesDestruidos() {
        String tanquesDestruidos = Integer.toString(this.juego.getNivelActual().getJugador().getTanquesDestruidos());
        valorTanquesDestruidos.setText(tanquesDestruidos);
    }

    @Override
    public void actualizarJugador() {
        this.actualizarProyectilesJugador();
        this.actualizarPuntosJugador();
        this.actualizarVidasJugador();
        this.actualizarEnergiaJugador();
        this.actualizarTanquesDestruidos();
        this.actualizarTanquesDelNivel();
        this.actualizarArmaJugador();
    }

    @Override
    public void actualizarTanquesDelNivel() {
        String tanquesDelNivel = Integer.toString(this.juego.getNivelActual().getCantidadEnemigos());
        valorTanquesDelNivel.setText(tanquesDelNivel);
    }
}
