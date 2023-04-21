package Vista;

import Modelo.Constantes;
import Controlador.Controlador;
import Modelo.Comida;
import Modelo.Criatura;
import Modelo.Escenario;
import Modelo.Objeto;
import Modelo.Objeto.ObserverObjeto;
import Vista.Debug.VCirculo;
import Vista.Debug.VSectorCircular;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author  Jose Maria
 */
public class Vista extends javax.swing.JFrame implements Escenario.ObserverEscenario {

    private Controlador logica_;

    private Screen pantalla_;

    private ArrayList vobjetos_;

    private HashMap<Objeto, ObjetoVisual> mapaObjetos_;

    private Timer reloj_;

    public void objetoCreado(Objeto objeto) {
        switch(objeto.getTipo_()) {
            case Criatura:
                {
                    Criatura criatura = (Criatura) objeto;
                    VCriatura vcriatura = new VCriatura();
                    vcriatura.posicionCambiada(criatura.getPosicion_());
                    criatura.addObserver(vcriatura);
                    criatura.addObserverCriatura(vcriatura);
                    vobjetos_.add(vcriatura);
                    mapaObjetos_.put(objeto, vcriatura);
                    if (Constantes.MODO_DEBUG) {
                        VSectorCircular vsector = new VSectorCircular();
                        criatura.getVista_().addObserver(vsector);
                        vobjetos_.add(vsector);
                        VCirculo vcirculo = new VCirculo();
                        criatura.getOlfato_().addObserver(vcirculo);
                        vobjetos_.add(vcirculo);
                    }
                }
                break;
            case Comida:
                {
                    Comida comida = (Comida) objeto;
                    VComida vcomida = new VComida();
                    vcomida.posicionCambiada(comida.getPosicion_());
                    comida.addObserver(vcomida);
                    vobjetos_.add(vcomida);
                    mapaObjetos_.put(objeto, vcomida);
                    break;
                }
        }
    }

    public void objetoEliminado(Objeto objeto) {
        ObjetoVisual v = mapaObjetos_.get(objeto);
        objeto.deleteObserver((ObserverObjeto) v);
        vobjetos_.remove(v);
    }

    public void cerrarJuego() {
        reloj_.cancel();
        reloj_ = null;
        mapaObjetos_.clear();
        vobjetos_.clear();
    }

    /** Creates new form GUI */
    public Vista(Controlador log) {
        initComponents();
        mapaObjetos_ = new HashMap<Objeto, ObjetoVisual>();
        logica_ = log;
        vobjetos_ = new ArrayList();
        pantalla_ = new Screen(logica_);
        this.screen_panel.add(pantalla_);
        this.setVisible(true);
        pantalla_.setSize(screen_panel.getSize());
        pantalla_.inicializarDobleBuffer();
        reloj_ = new Timer();
        reloj_.scheduleAtFixedRate(timerTask, 0, 10);
    }

    TimerTask timerTask = new TimerTask() {

        public void run() {
            dibujarse();
        }
    };

    public void dibujarse() {
        pantalla_.dibujarse(vobjetos_);
    }

    private void initComponents() {
        screen_panel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        javax.swing.GroupLayout screen_panelLayout = new javax.swing.GroupLayout(screen_panel);
        screen_panel.setLayout(screen_panelLayout);
        screen_panelLayout.setHorizontalGroup(screen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 566, Short.MAX_VALUE));
        screen_panelLayout.setVerticalGroup(screen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 527, Short.MAX_VALUE));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Controles"));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 171, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 497, Short.MAX_VALUE));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(screen_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(screen_panel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        pack();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Vista(null).setVisible(true);
            }
        });
    }

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel screen_panel;
}
