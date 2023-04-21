package interfaz;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import dominio.*;
import javax.swing.ImageIcon;

public class VentanaJuego extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final int LAB_DIFX = -3, LAB_DIFY = 0;

    private JPanel jContentPane = null;

    private JLabel labelMapa = null;

    private JLabel jLabel = null;

    private JScrollPane scroll = null;

    private DataLoader loader = null;

    private String AtkFrom;

    private boolean mousePressed = false;

    private Clock clock = null;

    private static int MouseX, MouseY;

    private static Region MovingFrom, MovingTo;

    private Sistema modelo = null;

    private boolean iniciado;

    private JMenuBar menuBar = null;

    private JRadioButtonMenuItem mitemAtacar = null, mitemMover = null;

    private JMenuItem mitemNueva = null, mitemCargar = null, mitemGuardar = null, mitemSalir = null;

    private JMenuItem mitemCanjear = null, mitemPasar = null;

    private JMenuItem mitemObjetivo = null, mitemEstadistica = null;

    private JMenuItem mitemComoJugar = null, mitemReglas = null, mitemAcercaDe = null;

    private PanelAcciones panelAcciones = null;

    private Territorio origen = null, destino = null;

    private VentanaPrincipal padre = null;

    /**
	 * This is the default constructor
	 */
    public VentanaJuego(Sistema unSistema, VentanaPrincipal ventana) throws FileNotFoundException, IOException {
        super();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        modelo = unSistema;
        padre = ventana;
        loader = new DataLoader();
        loader.load();
        panelAcciones = new PanelAcciones(this);
        iniciado = false;
        initialize();
        this.setResizable(false);
        clock = new Clock();
        panelAcciones.setVisible(true);
        panelAcciones.setAlwaysOnTop(true);
        panelAcciones.setLocation(getX(), getY() + 340);
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(1200, 734);
        this.setBounds(10, 10, 1200, 734);
        this.setContentPane(getJContentPane());
        setJMenuBar(getMenu());
        this.setTitle("JaWAR");
        this.setIconImage(new ImageIcon("data/jawar.png").getImage());
        this.addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentResized(java.awt.event.ComponentEvent e) {
                scroll.setBounds(0, 0, getWidth() - 10, getHeight() - 50);
            }
        });
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent e) {
                int res = JOptionPane.showConfirmDialog(null, "Deseas guardar la partida?", "Confirme", JOptionPane.YES_NO_CANCEL_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    try {
                        guardarPartida();
                    } catch (Exception f) {
                        JOptionPane.showMessageDialog(null, "Un error crítico ha ocurrido mientras se guardaba", "Error", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        dispose();
                        panelAcciones.dispose();
                        padre.setVisible(true);
                    }
                } else if (res != JOptionPane.CANCEL_OPTION) {
                    dispose();
                    panelAcciones.dispose();
                    padre.actualizarHighscores();
                    padre.setVisible(true);
                }
            }
        });
    }

    private JMenuBar getMenu() {
        if (menuBar == null) {
            menuBar = new JMenuBar();
            JMenu menuJuego = new JMenu("Juego");
            menuJuego.setMnemonic('J');
            mitemNueva = new JMenuItem("Nueva");
            mitemNueva.setMnemonic('N');
            mitemNueva.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.Event.CTRL_MASK));
            mitemGuardar = new JMenuItem("Guardar");
            mitemGuardar.setMnemonic('G');
            mitemGuardar.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.Event.CTRL_MASK));
            mitemCargar = new JMenuItem("Cargar");
            mitemCargar.setMnemonic('C');
            mitemCargar.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.Event.CTRL_MASK));
            mitemSalir = new JMenuItem("Salir");
            menuJuego.add(mitemNueva);
            menuJuego.addSeparator();
            menuJuego.add(mitemGuardar);
            menuJuego.add(mitemCargar);
            menuJuego.addSeparator();
            menuJuego.add(mitemSalir);
            menuBar.add(menuJuego);
            JMenu menuAccion = new JMenu("Acción");
            menuAccion.setMnemonic('A');
            mitemAtacar = new JRadioButtonMenuItem("Atacar");
            mitemAtacar.setMnemonic('A');
            mitemAtacar.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.Event.CTRL_MASK));
            mitemMover = new JRadioButtonMenuItem("Mover");
            mitemMover.setMnemonic('M');
            mitemMover.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, java.awt.Event.CTRL_MASK));
            ButtonGroup bg = new ButtonGroup();
            bg.add(mitemAtacar);
            bg.add(mitemMover);
            mitemCanjear = new JMenuItem("Canjear");
            mitemCanjear.setMnemonic('C');
            mitemCanjear.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.Event.CTRL_MASK));
            mitemCanjear.setEnabled(false);
            mitemPasar = new JMenuItem("Pasar");
            mitemPasar.setMnemonic('P');
            mitemPasar.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, java.awt.Event.CTRL_MASK));
            menuAccion.add(mitemAtacar);
            menuAccion.add(mitemMover);
            menuAccion.addSeparator();
            menuAccion.add(mitemCanjear);
            menuAccion.addSeparator();
            menuAccion.add(mitemPasar);
            menuBar.add(menuAccion);
            JMenu menuVer = new JMenu("Ver");
            menuVer.setMnemonic('V');
            mitemObjetivo = new JMenuItem("Objetivo");
            mitemEstadistica = new JMenuItem("Estadísticas");
            mitemEstadistica.setMnemonic('E');
            mitemEstadistica.setEnabled(false);
            menuVer.add(mitemObjetivo);
            menuVer.add(mitemEstadistica);
            menuBar.add(menuVer);
            JMenu menuayuda = new JMenu("Ayuda");
            menuayuda.setMnemonic('Y');
            mitemComoJugar = new JMenuItem("Cómo jugar?");
            mitemComoJugar.setMnemonic('J');
            mitemReglas = new JMenuItem("Reglas");
            mitemReglas.setMnemonic('R');
            mitemAcercaDe = new JMenuItem("Acerca de...");
            mitemAcercaDe.setMnemonic('A');
            menuayuda.add(mitemComoJugar);
            menuayuda.add(mitemReglas);
            menuayuda.addSeparator();
            menuayuda.add(mitemAcercaDe);
            menuBar.add(menuayuda);
            mitemNueva.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    int res = JOptionPane.showConfirmDialog(null, "Desea guardar la partida antes de salir?", "", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (res == JOptionPane.YES_OPTION) {
                        guardarPartida();
                    }
                    if (res != JOptionPane.CANCEL_OPTION) {
                        VentanaPrevia vPrevia = new VentanaPrevia(null, modelo);
                        res = vPrevia.showDialog();
                        if (res == 0) {
                            try {
                                loadState();
                            } catch (Exception f) {
                                JOptionPane.showMessageDialog(null, "Un error grave ha ocurrido. El juego deber� cerrarse", "ERROR FATAL", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            });
            mitemCargar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    cargarPartida();
                    loadState();
                }
            });
            mitemGuardar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    guardarPartida();
                }
            });
            mitemSalir.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    int res = JOptionPane.showConfirmDialog(null, "Deseas guardar la partida?", "Confirme", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (res == JOptionPane.YES_OPTION) {
                        try {
                            guardarPartida();
                        } catch (Exception f) {
                            JOptionPane.showMessageDialog(null, "Un error crítico ha ocurrido mientras se guardaba", "Error", JOptionPane.ERROR_MESSAGE);
                        } finally {
                            dispose();
                            panelAcciones.dispose();
                            padre.setVisible(true);
                        }
                    } else if (res != JOptionPane.CANCEL_OPTION) {
                        dispose();
                        panelAcciones.dispose();
                        padre.setVisible(true);
                    }
                }
            });
            mitemAtacar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    panelAcciones.setButtonSelected(PanelAcciones.BUTTON_ATTACK, true);
                }
            });
            mitemMover.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    panelAcciones.setButtonSelected(PanelAcciones.BUTTON_MOVE, true);
                }
            });
            mitemCanjear.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    canjear();
                }
            });
            mitemPasar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    pasar();
                }
            });
            mitemObjetivo.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, modelo.getPartida().getJugadorActual().getObjetivo().toString(), "Objetivo", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            mitemEstadistica.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            mitemComoJugar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Usa los botones 'Atacar' y 'Mover' para seleccionar la acción.\n Luego solo arrastre el cursor desde el territorio origen hasta el territorio destino.", "Cómo  Jugar", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            mitemReglas.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Lee el archivo Reglas.pdf dentro de la carpeta del juego", "Reglas", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            mitemAcercaDe.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "JaWAR v1.0 \nAutor: Rodrigo Machado Laprebendere\nrml22388@gmail.com", "Acerca de...", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("data/jawar.png"));
                }
            });
        }
        return menuBar;
    }

    private void refresh() {
        dibujarEtiquetas();
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jLabel = new JLabel();
            jLabel.setText("");
            labelMapa = new JLabel();
            labelMapa.setBounds(0, 0, this.getWidth(), this.getHeight());
            labelMapa.setText("");
            labelMapa.setIcon(new ImageIcon("data/mapa.jpg"));
            labelMapa.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

                public void mouseMoved(java.awt.event.MouseEvent e) {
                    String pos = e.getX() + " - " + e.getY();
                    MouseX = e.getX();
                    MouseY = e.getY();
                    Region region = buscarTerritorioEx(e.getX(), e.getY());
                    if (region != null) {
                        String reg = region.getNombre();
                        jLabel.setText(pos + "        " + reg);
                        if (!mousePressed) jLabel.setText(pos + "        " + reg);
                        MovingFrom = MovingTo;
                        MovingTo = region;
                    } else MovingTo = null;
                    try {
                        if (!clock.isAlive()) clock.start();
                    } catch (IllegalThreadStateException f) {
                    }
                    try {
                        if ((MovingFrom == null && MovingTo != null) || (MovingTo == null && MovingFrom != null) || !MovingFrom.equals(MovingTo)) moverEtiquetaSeleccionada(MovingFrom, MovingTo);
                    } catch (NullPointerException f) {
                    }
                }

                public void mouseDragged(MouseEvent e) {
                    String pos = e.getX() + " - " + e.getY();
                    String region = buscarTerritorio(e.getX(), e.getY());
                    jLabel.setText(pos + "        " + AtkFrom + " ===> " + region);
                }
            });
            labelMapa.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseReleased(java.awt.event.MouseEvent e) {
                    Territorio actual = modelo.getPartida().getTerritorio(buscarTerritorio(e.getX(), e.getY()));
                    Territorio origen = modelo.getPartida().getTerritorio(AtkFrom);
                    if (origen != null && actual != null && origen != actual) {
                        if (!origen.getJugador().equals(modelo.getPartida().getJugadorActual())) return;
                        if (panelAcciones.isButtonSelected(PanelAcciones.BUTTON_ATTACK) && panelAcciones.isButtonEnabled(PanelAcciones.BUTTON_ATTACK)) {
                            if (actual.getJugador().getPassword().length() > 0) mostrarLogin(actual.getJugador());
                            DialogoBatalla dbat = new DialogoBatalla(null, true, modelo, origen, actual);
                            dbat.setLocation(getX() + getWidth() / 2 - dbat.getWidth() / 2, getY() + getHeight() / 2 - dbat.getHeight() - 2);
                            dbat.setVisible(true);
                            if (modelo.getPartida().ganoBatalla()) {
                                if (origen.getEjercito() == 2) {
                                    origen.setEjercito(1);
                                    actual.setEjercito(1);
                                } else {
                                    DialogoMover dmover = new DialogoMover(null, true, origen.getNombre(), origen.getEjercito(), actual.getNombre(), actual.getEjercito(), 1, 3);
                                    dmover.setLocation(getX() + getWidth() / 2 - dmover.getWidth() / 2, getY() + getHeight() / 2 - dmover.getHeight() / 2);
                                    dmover.setVisible(true);
                                    modelo.getPartida().avanzar(origen, actual, dmover.CantidadMovido());
                                }
                            }
                            panelAcciones.setButtonEnabled(PanelAcciones.BUTTON_EXCHANGE, modelo.getPartida().puedeCanjear());
                            mitemCanjear.setEnabled(modelo.getPartida().puedeCanjear());
                            if (modelo.getPartida().finalizo()) {
                                panelAcciones.disableAll();
                                mitemAtacar.setEnabled(false);
                                mitemMover.setEnabled(false);
                                mitemCanjear.setEnabled(false);
                                mitemPasar.setEnabled(false);
                                JOptionPane.showMessageDialog(null, "La partida ha terminado! " + modelo.getPartida().getGanador().toString().toUpperCase() + " HA GANADO!", "Felicitaciones!", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else if (panelAcciones.isButtonSelected(PanelAcciones.BUTTON_MOVE) && panelAcciones.isButtonEnabled(PanelAcciones.BUTTON_MOVE)) {
                            DialogoMover dmover = new DialogoMover(null, true, origen.getNombre(), origen.getEjercito(), actual.getNombre(), actual.getEjercito());
                            dmover.setLocation(getX() + getWidth() / 2 - dmover.getWidth() / 2, getY() + getHeight() / 2 - dmover.getHeight() / 2);
                            dmover.setVisible(true);
                            modelo.getPartida().mover(origen, actual, dmover.CantidadMovido());
                            if (!modelo.getPartida().puedeAtacar()) {
                                panelAcciones.setButtonEnabled(PanelAcciones.BUTTON_ATTACK, false);
                                mitemAtacar.setEnabled(false);
                                panelAcciones.setButtonEnabled(PanelAcciones.BUTTON_MOVE, false);
                                mitemMover.setEnabled(false);
                            }
                            panelAcciones.setButtonEnabled(PanelAcciones.BUTTON_EXCHANGE, modelo.getPartida().puedeCanjear());
                        }
                    }
                    mousePressed = false;
                    AtkFrom = "";
                }

                public void mousePressed(java.awt.event.MouseEvent e) {
                    mousePressed = true;
                    AtkFrom = buscarTerritorio(e.getX(), e.getY());
                }
            });
            scroll = new JScrollPane();
            scroll.setBounds(0, 0, getWidth() - 10, getHeight() - 50);
            scroll.setViewportView(labelMapa);
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(scroll, null);
            jContentPane.addKeyListener(new java.awt.event.KeyAdapter() {

                public void keyTyped(java.awt.event.KeyEvent evt) {
                    jLabel.setText("===>>>" + evt.getKeyCode());
                }

                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jLabel.setText("===>>>" + evt.getKeyCode());
                }

                public void keyReleased(java.awt.event.KeyEvent evt) {
                    jLabel.setText("===>>>" + evt.getKeyCode());
                }
            });
            int posY = scroll.getY() + scroll.getHeight();
            jLabel.setBounds(500, posY, 1000, 30);
            jLabel.setText("::::::::::::::::::::");
            jContentPane.add(jLabel, null);
        }
        return jContentPane;
    }

    public void canjear() {
        DialogoCanje dcanje = new DialogoCanje(null, true, modelo, modelo.getPartida().getJugadorActual());
        dcanje.setLocation(getX() + getWidth() / 2 - dcanje.getWidth() / 2, getY() + getHeight() / 2 - dcanje.getHeight() / 2);
        dcanje.setVisible(true);
        if (modelo.getPartida().finalizo()) {
            panelAcciones.disableAll();
            mitemAtacar.setEnabled(false);
            mitemMover.setEnabled(false);
            mitemCanjear.setEnabled(false);
            mitemPasar.setEnabled(false);
            JOptionPane.showMessageDialog(null, "La partida ha terminado! " + modelo.getPartida().getGanador().toString().toUpperCase() + " HA GANADO!", "Felicitaciones!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void pasar() {
        modelo.getPartida().pasarTurno();
        pasarTurno();
        panelAcciones.setButtonEnabled(PanelAcciones.BUTTON_EXCHANGE, true);
        mitemCanjear.setEnabled(true);
    }

    public void seleccion(int boton) {
        if (boton == PanelAcciones.BUTTON_ATTACK) mitemAtacar.setSelected(true);
        if (boton == PanelAcciones.BUTTON_MOVE) mitemMover.setSelected(true);
    }

    public void start() {
        if (!iniciado) {
            if (!clock.isAlive()) clock.start();
            pasarTurno();
            iniciado = true;
        }
    }

    public void loadState() {
        if (!iniciado) {
            if (!clock.isAlive()) clock.start();
            iniciado = true;
        }
        boolean botAtacar = modelo.getPartida().puedeAtacar();
        boolean botCanjear = modelo.getPartida().puedeCanjear();
        boolean botRepetir = modelo.getPartida().ataco() && modelo.getPartida().getUltimoAtacado() != null && modelo.getPartida().getUltimoAtacante() != null;
        panelAcciones.setButtonEnabled(PanelAcciones.BUTTON_ATTACK, botAtacar);
        mitemAtacar.setEnabled(botAtacar);
        panelAcciones.setButtonEnabled(PanelAcciones.BUTTON_MOVE, botAtacar);
        mitemMover.setEnabled(botAtacar);
        panelAcciones.setButtonSelected(PanelAcciones.BUTTON_ATTACK, true);
        mitemAtacar.setSelected(true);
        panelAcciones.setButtonEnabled(PanelAcciones.BUTTON_EXCHANGE, botCanjear);
        Color color = modelo.getPartida().getColores().get(modelo.getPartida().getJugadorActual());
        panelAcciones.setButtonBackground(PanelAcciones.BUTTON_FINISH, color);
        panelAcciones.setButtonForeground(PanelAcciones.BUTTON_FINISH, foregroundFor(color));
        this.setTitle("WAR  -  Turno de " + modelo.getPartida().getJugadorActual());
    }

    private void pasarTurno() {
        panelAcciones.setButtonEnabled(PanelAcciones.BUTTON_ATTACK, true);
        mitemAtacar.setEnabled(true);
        panelAcciones.setButtonEnabled(PanelAcciones.BUTTON_MOVE, true);
        mitemMover.setEnabled(true);
        panelAcciones.setButtonSelected(PanelAcciones.BUTTON_ATTACK, true);
        mitemAtacar.setSelected(true);
        Carta carta = modelo.getPartida().getCartaExtraida();
        if (carta != null) {
            DialogoCarta dcarta = new DialogoCarta(null, true, carta);
            dcarta.setLocation(getX() + getWidth() / 2 - dcarta.getWidth() / 2, getY() + getHeight() / 2 - dcarta.getHeight() / 2);
            dcarta.setVisible(true);
        }
        Jugador jug = modelo.getPartida().getJugadorActual();
        if (!jug.getPassword().equals("")) mostrarLogin(); else JOptionPane.showMessageDialog(null, "Es el turno de " + jug, "", JOptionPane.PLAIN_MESSAGE);
        if (modelo.getPartida().esPrimeraFase()) JOptionPane.showMessageDialog(this, modelo.getPartida().getJugadorActual().getObjetivo().toString(), "Objetivo", JOptionPane.INFORMATION_MESSAGE);
        if (!modelo.getPartida().esSegundaFase()) {
            int refuerzos = modelo.getPartida().calcularRefuerzos();
            DialogoRefuerzos dref = new DialogoRefuerzos((null), true, modelo.getPartida(), refuerzos);
            dref.setLocation(getX() + getWidth() / 2 - dref.getWidth() / 2, getY() + getHeight() / 2 - dref.getHeight() / 2);
            dref.setVisible(true);
            HashMap<Sistema.CONTINENTE, Integer> tabla = modelo.getPartida().calcularRefuerzosContinentes();
            Iterator iter = tabla.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry mentry = (Map.Entry) iter.next();
                refuerzos = (Integer) mentry.getValue();
                if (refuerzos == 0) continue;
                Sistema.CONTINENTE continente = (Sistema.CONTINENTE) mentry.getKey();
                dref = new DialogoRefuerzos((null), true, modelo.getPartida(), refuerzos, continente);
                dref.setLocation(getX() + getWidth() / 2 - dref.getWidth() / 2, getY() + getHeight() / 2 - dref.getHeight() / 2);
                dref.setVisible(true);
            }
        }
        if (modelo.getPartida().esPrimeraFase()) {
            panelAcciones.setButtonEnabled(PanelAcciones.BUTTON_ATTACK, false);
            mitemAtacar.setEnabled(false);
            panelAcciones.setButtonEnabled(PanelAcciones.BUTTON_MOVE, false);
            mitemMover.setEnabled(false);
        } else {
            panelAcciones.setButtonEnabled(PanelAcciones.BUTTON_EXCHANGE, true);
            mitemCanjear.setEnabled(true);
        }
        if (modelo.getPartida().finalizo()) {
            panelAcciones.disableAll();
            mitemAtacar.setEnabled(false);
            mitemMover.setEnabled(false);
            mitemCanjear.setEnabled(false);
            mitemPasar.setEnabled(false);
            JOptionPane.showMessageDialog(null, "La partida ha terminado! " + modelo.getPartida().getGanador().toString().toUpperCase() + " HA GANADO!", "Felicitaciones!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void dibujarEtiquetas() {
        Graphics2D g = (Graphics2D) labelMapa.getGraphics();
        g.setFont(new Font(null, Font.BOLD, 10));
        Iterator iter = loader.getRegiones().iterator();
        while (iter.hasNext()) {
            Region reg = (Region) iter.next();
            Iterator iter2 = reg.getPEtiquetas().entrySet().iterator();
            while (iter2.hasNext()) {
                Map.Entry me = (Map.Entry) iter2.next();
                String nom = ((String) me.getKey()).toUpperCase();
                Point p = (Point) me.getValue();
                g.setColor(Color.BLACK);
                g.drawString(nom, LAB_DIFX + p.x, LAB_DIFY + p.y);
            }
            Territorio terr = modelo.getPartida().getTerritorio(reg.getNombre());
            Jugador owner = terr.getJugador();
            if (MovingTo != null && reg.equals(MovingTo)) {
                dibujarEtiquetaRegion(reg);
            } else {
                Color color = modelo.getPartida().getColores().get(owner);
                g.setColor(color);
                g.fillRect(reg.getPCentro().x - 18, reg.getPCentro().y - 3, 26, 21);
                g.setColor(foregroundFor(color));
                g.drawString(terr.getEjercito() + "", reg.getPCentro().x - 10, reg.getPCentro().y + 10);
            }
        }
        Color color = modelo.getPartida().getColores().get(modelo.getPartida().getJugadorActual());
        panelAcciones.setButtonBackground(PanelAcciones.BUTTON_FINISH, color);
        panelAcciones.setButtonForeground(PanelAcciones.BUTTON_FINISH, foregroundFor(color));
        this.setTitle("WAR  -  Turno de " + modelo.getPartida().getJugadorActual());
    }

    private void moverEtiquetaSeleccionada(Region from, Region to) {
        Graphics2D g = (Graphics2D) labelMapa.getGraphics();
        g.setFont(new Font(null, Font.BOLD, 10));
        if (from != null) {
            Territorio terr = modelo.getPartida().getTerritorio(from.getNombre());
            Jugador owner = terr.getJugador();
            Color color = modelo.getPartida().getColores().get(owner);
            g.setColor(color);
            g.fillRect(from.getPCentro().x - 18, from.getPCentro().y - 3, 26, 21);
            g.setColor(foregroundFor(color));
            g.drawString(terr.getEjercito() + "", from.getPCentro().x - 10, from.getPCentro().y + 10);
        }
        if (to != null) {
            dibujarEtiquetaRegion(to);
        }
    }

    private void dibujarEtiquetaRegion(Region reg) {
        Territorio terr = modelo.getPartida().getTerritorio(reg.getNombre());
        Jugador owner = terr.getJugador();
        Graphics2D g = (Graphics2D) labelMapa.getGraphics();
        g.setFont(new Font(null, Font.BOLD, 10));
        Color color = modelo.getPartida().getColores().get(owner);
        g.setColor(foregroundFor(color));
        g.fillRect(reg.getPCentro().x - 18, reg.getPCentro().y - 3, 26, 21);
        g.setColor(color);
        g.fillRect(reg.getPCentro().x - 15, reg.getPCentro().y, 20, 15);
        g.setColor(foregroundFor(color));
        g.drawString(terr.getEjercito() + "", reg.getPCentro().x - 10, reg.getPCentro().y + 10);
    }

    private void mostrarLogin() {
        Jugador jug = modelo.getPartida().getJugadorActual();
        String res = "";
        JPasswordField passField = new JPasswordField(20);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Ingrese el password:"));
        panel.add(passField);
        do {
            JOptionPane.showMessageDialog(null, new Object[] { "Es el turno de " + jug, panel }, "", JOptionPane.PLAIN_MESSAGE);
            res = new String(passField.getPassword());
        } while (res != null && !res.equals(jug.getPassword()));
    }

    private void mostrarLogin(Jugador jug) {
        String res = "";
        JPasswordField passField = new JPasswordField(20);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Ingrese el password:"));
        panel.add(passField);
        do {
            JOptionPane.showMessageDialog(null, new Object[] { "Batalla con " + jug, panel }, "", JOptionPane.PLAIN_MESSAGE);
            res = new String(passField.getPassword());
        } while (res != null && !res.equals(jug.getPassword()));
    }

    private String buscarTerritorio(int x, int y) {
        HashSet<Region> regs = loader.getRegiones();
        Iterator it = regs.iterator();
        while (it.hasNext()) {
            Region reg = (Region) it.next();
            if (dentroElipse(new Point(x, y), reg.getPCentro(), reg.getRadioX(), reg.getRadioY())) {
                return reg.getNombre();
            }
        }
        return "";
    }

    private Region buscarTerritorioEx(int x, int y) {
        HashSet<Region> regs = loader.getRegiones();
        Iterator it = regs.iterator();
        while (it.hasNext()) {
            Region reg = (Region) it.next();
            if (dentroElipse(new Point(x, y), reg.getPCentro(), reg.getRadioX(), reg.getRadioY())) {
                return reg;
            }
        }
        return null;
    }

    private boolean dentroElipse(Point p, Point c, int rX, int rY) {
        return Math.pow(p.x - c.x, 2) / Math.pow(rX, 2) + Math.pow(p.y - c.y, 2) / Math.pow(rY, 2) - 1 <= 0;
    }

    private Color foregroundFor(Color color) {
        if (color.equals(Color.WHITE)) return Color.BLACK;
        if (color.equals(Color.GREEN)) return Color.BLACK;
        if (color.equals(Color.YELLOW)) return Color.BLACK;
        return Color.WHITE;
    }

    private Color highlightedFor(Color color) {
        if (color.equals(Color.WHITE)) return Color.DARK_GRAY;
        if (color.equals(Color.GREEN)) return Color.DARK_GRAY;
        if (color.equals(Color.YELLOW)) return Color.DARK_GRAY;
        return Color.YELLOW;
    }

    private void cargarPartida() {
        JFileChooser fchooser = new JFileChooser("");
        int res = fchooser.showOpenDialog(this);
        if (res == JFileChooser.CANCEL_OPTION) return;
        ObjectInputStream out = null;
        try {
            out = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fchooser.getSelectedFile().getAbsolutePath())));
            modelo.setPartida((Partida) out.readObject());
            out.close();
            modelo.getPartida().setPath(fchooser.getSelectedFile().getAbsolutePath());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ha ocurrido un error al guardar la partida", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarPartida() {
        if (modelo.getPartida().getPath().equals("")) {
            JFileChooser fchooser = new JFileChooser("");
            int res = fchooser.showSaveDialog(this);
            if (res == JFileChooser.CANCEL_OPTION) return;
            modelo.getPartida().setPath(fchooser.getSelectedFile().getAbsolutePath());
        }
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(modelo.getPartida().getPath())));
            out.writeObject(modelo.getPartida());
            out.flush();
            out.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ha ocurrido un error al guardar la partida", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class Clock extends Thread {

        public void run() {
            while (true) {
                try {
                    if (System.currentTimeMillis() % 500 == 0) refresh();
                } catch (NullPointerException e) {
                    break;
                }
            }
        }
    }
}
