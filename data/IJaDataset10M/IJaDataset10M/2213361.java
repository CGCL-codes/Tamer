package edu.sfourier.interfaz.toolbar;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import edu.sfourier.logica.animacion.Signaler;
import edu.sfourier.logica.procesamientoimagen.Hilo;
import edu.sfourier.logica.GestorConfiguracion;
import edu.sfourier.interfaz.PantallaGrafica;
import javax.swing.JOptionPane;

public class Barra extends JToolBar implements ActionListener {

    public static final long serialVersionUID = 1L;

    JButton imprimir;

    JButton simulacion;

    JButton csimulacion;

    JButton aproximacion;

    JButton reiniciar;

    JButton reproducir;

    JButton detener;

    JButton spectrum;

    JButton mode;

    JButton sMode;

    JButton ayuda;

    JButton salir;

    JButton configuracion;

    Signaler sig;

    int ControladorSesion;

    Hilo hilo;

    GestorConfiguracion setup = GestorConfiguracion.getInstance();

    public Barra(Signaler a) {
        super();
        this.sig = a;
        imprimir = new JButton(new ImageIcon("icons/stock_printers.png"));
        imprimir.addActionListener(this);
        imprimir.setToolTipText("Imprimir la pantalla actual");
        simulacion = new JButton(new ImageIcon("icons/utilities-system-monitor.png"));
        simulacion.addActionListener(this);
        simulacion.setToolTipText("Iniciar la simulaci�n de un cuerda");
        csimulacion = new JButton(new ImageIcon("icons/utilities-system-monitorc.png"));
        csimulacion.addActionListener(this);
        csimulacion.setToolTipText("Iniciar una sesi�n comparativa de cuerdas");
        aproximacion = new JButton(new ImageIcon("icons/accessories-calculator.png"));
        aproximacion.setToolTipText("Iniciar la aproximaci�n de una funci�n");
        aproximacion.addActionListener(this);
        ayuda = new JButton(new ImageIcon("icons/help.png"));
        ayuda.addActionListener(this);
        ayuda.setToolTipText("Consultar la ayuda");
        reiniciar = new JButton(new ImageIcon("icons/media-skip-backward.png"));
        reiniciar.addActionListener(this);
        reiniciar.setToolTipText("Reiniciar simulaci�n");
        reiniciar.setVisible(false);
        reproducir = new JButton(new ImageIcon("icons/player_play.png"));
        reproducir.addActionListener(this);
        reproducir.setToolTipText("Comenzar simulaci�n");
        reproducir.setVisible(false);
        detener = new JButton(new ImageIcon("icons/media-playback-stop.png"));
        detener.addActionListener(this);
        detener.setToolTipText("Detener la simulaci�n");
        detener.setVisible(false);
        spectrum = new JButton(new ImageIcon("icons/gnome-monitor.png"));
        spectrum.addActionListener(this);
        spectrum.setToolTipText("Observar espectro de amplitudes");
        spectrum.setVisible(false);
        mode = new JButton(new ImageIcon("icons/mode.png"));
        mode.addActionListener(this);
        mode.setToolTipText("Observar un modo individual");
        mode.setVisible(false);
        sMode = new JButton(new ImageIcon("icons/math_sum.png"));
        sMode.addActionListener(this);
        sMode.setToolTipText("Observar la suma hasta un modo espec�fico");
        sMode.setVisible(false);
        salir = new JButton(new ImageIcon("icons/exit.png"));
        salir.addActionListener(this);
        salir.setToolTipText("Salir de sFourier");
        configuracion = new JButton(new ImageIcon("icons/conf.png"));
        configuracion.addActionListener(this);
        configuracion.setToolTipText("Preferencias");
        add(imprimir);
        addSeparator();
        add(simulacion);
        add(csimulacion);
        add(aproximacion);
        addSeparator();
        add(spectrum);
        add(mode);
        add(sMode);
        addSeparator();
        add(reiniciar);
        add(reproducir);
        add(detener);
        addSeparator();
        add(ayuda);
        add(configuracion);
        add(salir);
    }

    public JButton getAproximacion() {
        return aproximacion;
    }

    public JButton getAyuda() {
        return ayuda;
    }

    public JButton getSimulacion() {
        return simulacion;
    }

    public JButton getCSimulacion() {
        return csimulacion;
    }

    public void enableSimulacion() {
        simulacion.setVisible(true);
        csimulacion.setVisible(true);
        aproximacion.setVisible(false);
    }

    public void disableMainControls() {
        simulacion.setVisible(false);
        csimulacion.setVisible(false);
        aproximacion.setVisible(false);
    }

    public void enableMainControls() {
        simulacion.setVisible(true);
        csimulacion.setVisible(true);
        aproximacion.setVisible(true);
    }

    public void enableAproximacion() {
        simulacion.setVisible(false);
        csimulacion.setVisible(false);
        aproximacion.setVisible(true);
    }

    public void disableGeneralControls() {
        sMode.setVisible(false);
        spectrum.setVisible(false);
        mode.setVisible(false);
    }

    public void enableGeneralControls() {
        sMode.setVisible(true);
        spectrum.setVisible(true);
        mode.setVisible(true);
    }

    public void disableControls() {
        reiniciar.setVisible(false);
        reproducir.setVisible(false);
        detener.setVisible(false);
    }

    public void enableControls() {
        reiniciar.setVisible(true);
        reproducir.setVisible(true);
        detener.setVisible(true);
    }

    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == aproximacion) {
            ControladorSesion = PantallaGrafica.MODO_EDICION_FUNCION;
            disableControls();
            sig.command.setText("begin function");
            sig.process();
            sig.command.setText("define");
            sig.process();
            enableGeneralControls();
        }
        if (arg0.getSource() == simulacion) {
            ControladorSesion = PantallaGrafica.MODO_EDICION_CUERDA;
            enableControls();
            sig.command.setText("begin string");
            sig.process();
            sig.command.setText("define");
            sig.process();
        }
        if (arg0.getSource() == csimulacion) {
            ControladorSesion = PantallaGrafica.MODO_EDICION_COMPARACION;
            enableControls();
            sig.command.setText("begin comparison");
            sig.process();
            sig.command.setText("define string a");
            sig.process();
        }
        if (arg0.getSource() == spectrum) {
            switch(ControladorSesion) {
                case 0:
                    Toolkit.getDefaultToolkit().beep();
                    break;
                case PantallaGrafica.MODO_EDICION_FUNCION:
                    sig.command.setText("showSpectrum");
                    sig.process();
                    break;
                case PantallaGrafica.MODO_EDICION_CUERDA:
                case PantallaGrafica.MODO_EDICION_COMPARACION:
                    enableSimulacion();
                    sig.command.setText("play");
                    sig.process();
                    sig.command.setText("showSpectrum");
                    sig.process();
                    disableGeneralControls();
                    reproducir.setVisible(false);
                    disableMainControls();
                    break;
            }
        }
        if (arg0.getSource() == mode) {
            switch(ControladorSesion) {
                case 0:
                    Toolkit.getDefaultToolkit().beep();
                    break;
                case PantallaGrafica.MODO_EDICION_FUNCION:
                    int modosTempFuncion = setup.getTerminosF();
                    String[] modosFuncion = new String[modosTempFuncion];
                    for (int i = 1; i <= modosTempFuncion; i++) {
                        modosFuncion[i - 1] = Integer.toString(i);
                    }
                    Object modoSelFuncion = JOptionPane.showInputDialog(null, "Seleccione el modo a visualizar:", "Modo a visualizar", JOptionPane.INFORMATION_MESSAGE, null, modosFuncion, modosFuncion[0]);
                    String selModoFuncion = modoSelFuncion.toString();
                    System.out.println("Imprimiendo modo: " + selModoFuncion);
                    sig.command.setText("showMode:" + selModoFuncion);
                    sig.process();
                    break;
                case PantallaGrafica.MODO_EDICION_CUERDA:
                case PantallaGrafica.MODO_EDICION_COMPARACION:
                    int modosTempCuerda = setup.getTerminosC();
                    String[] modosCuerda = new String[modosTempCuerda];
                    for (int i = 1; i <= modosTempCuerda; i++) {
                        modosCuerda[i - 1] = Integer.toString(i);
                    }
                    Object modoSelCuerda = JOptionPane.showInputDialog(null, "Seleccione el modo a visualizar:", "Modo a visualizar", JOptionPane.INFORMATION_MESSAGE, null, modosCuerda, modosCuerda[0]);
                    String selModoCuerda = modoSelCuerda.toString();
                    sig.command.setText("play");
                    sig.process();
                    sig.command.setText("showMode:" + selModoCuerda);
                    sig.process();
                    disableMainControls();
                    reproducir.setVisible(false);
                    disableGeneralControls();
                    break;
            }
        }
        if (arg0.getSource() == sMode) {
            switch(ControladorSesion) {
                case 0:
                    Toolkit.getDefaultToolkit().beep();
                    break;
                case PantallaGrafica.MODO_EDICION_FUNCION:
                    int sumModosFuncion = setup.getTerminosF();
                    String[] sumatoriaModosFuncion = new String[sumModosFuncion];
                    for (int i = 1; i <= sumModosFuncion; i++) {
                        sumatoriaModosFuncion[i - 1] = Integer.toString(i);
                    }
                    Object sumatoriaModosFuncionSel = JOptionPane.showInputDialog(null, "Visualizar la suma hasta el modo:", "Visualizar suma de modos", JOptionPane.INFORMATION_MESSAGE, null, sumatoriaModosFuncion, sumatoriaModosFuncion[0]);
                    String selSumModoFuncion = sumatoriaModosFuncionSel.toString();
                    System.out.println("Imprimiendo modo: " + selSumModoFuncion);
                    sig.command.setText("showSumModes:" + selSumModoFuncion);
                    sig.process();
                    break;
                case PantallaGrafica.MODO_EDICION_CUERDA:
                case PantallaGrafica.MODO_EDICION_COMPARACION:
                    int sumModosCuerda = setup.getTerminosC();
                    String[] sumatoriaModosCuerda = new String[sumModosCuerda];
                    for (int i = 1; i <= sumModosCuerda; i++) {
                        sumatoriaModosCuerda[i - 1] = Integer.toString(i);
                    }
                    Object sumatoriaModosCuerdaSel = JOptionPane.showInputDialog(null, "Visualizar la suma hasta el modo:", "Visualizar suma de modos", JOptionPane.INFORMATION_MESSAGE, null, sumatoriaModosCuerda, sumatoriaModosCuerda[0]);
                    String selSumModoCuerda = sumatoriaModosCuerdaSel.toString();
                    sig.command.setText("play");
                    sig.process();
                    sig.command.setText("showSumModes:" + selSumModoCuerda);
                    sig.process();
                    disableMainControls();
                    reproducir.setVisible(false);
                    disableGeneralControls();
                    break;
            }
        }
        if (arg0.getSource() == reproducir) {
            sig.command.setText("play");
            sig.process();
            disableGeneralControls();
            disableMainControls();
            reproducir.setVisible(false);
        }
        if (arg0.getSource() == detener) {
            sig.command.setText("stop");
            sig.process();
            enableGeneralControls();
            enableMainControls();
            reproducir.setVisible(true);
        }
        if (arg0.getSource() == reiniciar) {
            sig.command.setText("stop");
            sig.process();
            sig.command.setText("play");
            sig.process();
            disableGeneralControls();
            reproducir.setVisible(false);
        }
        if (arg0.getSource() == imprimir) {
            sig.command.setText("print");
            sig.process();
        }
        if (arg0.getSource() == ayuda) {
            sig.command.setText("help");
            sig.process();
        }
        if (arg0.getSource() == salir) {
            sig.command.setText("quit");
            sig.process();
        }
        if (arg0.getSource() == configuracion) {
            sig.command.setText("configure");
            sig.process();
        }
    }
}
