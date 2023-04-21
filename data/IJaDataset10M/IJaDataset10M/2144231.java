package Vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Ventana extends Observable {

    private int xMaxCanvas;

    private int yMaxCanvas;

    private int pixelesXMetro;

    private int xMaxVentana;

    private int yMaxVentana;

    private JFrame ventana;

    private JPanel jContentPane = null;

    private JPanel jPanelPrincipal = null;

    private JButton jButton = null;

    private JLabel jlVelocidad = null;

    private JTextField jtfVelocidad = null;

    private JLabel jlAngulo = null;

    private JTextField jtfAngulo = null;

    private JLabel jlLogo = null;

    private JLabel jlposX = null;

    private JLabel jlposY = null;

    private JLabel jlxMax = null;

    private JLabel jlyMax = null;

    private JLabel jltVuelo = null;

    private JLabel jldatoPosX = null;

    private JLabel jldatoPosY = null;

    private JLabel jldatoXMax = null;

    private JLabel jldatoYMax = null;

    private JLabel jldatoTVuelo = null;

    private int nBoton;

    private int tipoObjeto;

    public static int LANZAR = 1;

    public static int INFORMACION = 2;

    public static int PELOTA = 0;

    public static int BARRA = 1;

    public List<Double> parametros;

    public Ventana(Double xMaxCanvas, Double yMaxCanvas, int pixelesXMetros, DrawImage dimg) {
        this.ventana = new JFrame();
        int tamX = xMaxCanvas.intValue();
        int tamY = yMaxCanvas.intValue();
        tamX *= pixelesXMetros;
        tamY *= pixelesXMetros;
        this.xMaxCanvas = tamX;
        this.yMaxVentana = tamY;
        this.xMaxVentana = tamX;
        tamY += 30;
        this.yMaxVentana = tamY;
        this.ventana.setSize(new Dimension(this.xMaxVentana + 182, this.yMaxVentana));
        parametros = new ArrayList<Double>();
        tipoObjeto = PELOTA;
        this.nBoton = -1;
        this.ventana.setLocation(200, 100);
        this.ventana.setTitle("Lanzamiento del Proyectil - Standalone");
        this.ventana.setContentPane(this.getJContentPane());
        this.ventana.getContentPane().add(dimg);
        this.ventana.setVisible(true);
        this.ventana.setResizable(false);
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(this.getJPanelPrincipal(), null);
        }
        return jContentPane;
    }

    private JPanel getJPanelPrincipal() {
        if (jPanelPrincipal == null) {
            this.ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jlAngulo = new JLabel();
            jlAngulo.setBounds(new Rectangle(782, 410, 61, 26));
            jlAngulo.setText("Angulo [�]");
            jlVelocidad = new JLabel();
            jlVelocidad.setBounds(new Rectangle(767, 366, 98, 28));
            jlVelocidad.setText("Vel. Inicial [m/s]");
            jlLogo = new JLabel(new ImageIcon("Imagenes\\logo.jpg"));
            jlLogo.setVisible(true);
            jlLogo.setBounds(698, 0, 178, 500);
            jlposX = new JLabel("Posici�n eje X: ");
            jlposX.setBounds(new Rectangle(767, 100, 98, 28));
            jldatoPosX = new JLabel("0.0");
            jldatoPosX.setBounds(new Rectangle(767, 120, 98, 28));
            jlposY = new JLabel("Posici�n eje Y: ");
            jlposY.setBounds(new Rectangle(767, 150, 98, 28));
            jldatoPosY = new JLabel("0.0");
            jldatoPosY.setBounds(new Rectangle(767, 170, 98, 28));
            jlxMax = new JLabel("Alcance max X: ");
            jlxMax.setBounds(new Rectangle(767, 200, 98, 28));
            jldatoXMax = new JLabel("0.0");
            jldatoXMax.setBounds(new Rectangle(767, 220, 98, 28));
            jlyMax = new JLabel("Alcance max Y: ");
            jlyMax.setBounds(new Rectangle(767, 250, 98, 28));
            jldatoYMax = new JLabel("0.0");
            jldatoYMax.setBounds(new Rectangle(767, 270, 98, 28));
            jltVuelo = new JLabel("Tiempo Vuelo: ");
            jltVuelo.setBounds(new Rectangle(767, 300, 98, 28));
            jldatoTVuelo = new JLabel("0.0");
            jldatoTVuelo.setBounds(new Rectangle(767, 320, 98, 28));
            jPanelPrincipal = new JPanel();
            jPanelPrincipal.setLayout(null);
            jPanelPrincipal.setBounds(new Rectangle(0, 0, this.xMaxVentana + 180, this.yMaxVentana));
            jPanelPrincipal.add(getJButton(), null);
            jPanelPrincipal.add(jlVelocidad, null);
            jPanelPrincipal.add(getJtfVelocidad(), null);
            jPanelPrincipal.add(jlAngulo, null);
            jPanelPrincipal.add(getJtfAngulo(), null);
            jPanelPrincipal.add(jlposX, null);
            jPanelPrincipal.add(jlposY, null);
            jPanelPrincipal.add(jlxMax, null);
            jPanelPrincipal.add(jlyMax, null);
            jPanelPrincipal.add(jltVuelo, null);
            jPanelPrincipal.add(jldatoPosX, null);
            jPanelPrincipal.add(jldatoPosY, null);
            jPanelPrincipal.add(jldatoXMax, null);
            jPanelPrincipal.add(jldatoYMax, null);
            jPanelPrincipal.add(jldatoTVuelo, null);
            jPanelPrincipal.add(jlLogo, null);
        }
        return jPanelPrincipal;
    }

    public void agregarCanvas(Canvas canvas) {
        canvas.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.setBackground(Color.blue);
        this.jPanelPrincipal.add(canvas, null);
    }

    public List<Double> getParametros() {
        return parametros;
    }

    public void setParametros(List<Double> parametros) {
        this.parametros = parametros;
    }

    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setBounds(new Rectangle(766, 466, 90, 27));
            jButton.setText("Lanzar");
            final Ventana a = this;
            jButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    parametros.clear();
                    a.listarParametros();
                    a.nBoton = LANZAR;
                    a.setChanged();
                    a.notifyObservers();
                }
            });
        }
        return jButton;
    }

    private JTextField getJtfVelocidad() {
        if (jtfVelocidad == null) {
            jtfVelocidad = new JTextField();
            jtfVelocidad.setBounds(new Rectangle(787, 390, 45, 20));
            jtfVelocidad.setText("20");
        }
        return jtfVelocidad;
    }

    private JTextField getJtfAngulo() {
        if (jtfAngulo == null) {
            jtfAngulo = new JTextField();
            jtfAngulo.setBounds(new Rectangle(787, 433, 45, 20));
            jtfAngulo.setText("40");
        }
        return jtfAngulo;
    }

    public void addObservers(FachadaVista a) {
        this.addObserver(a);
    }

    public int getNBoton() {
        return nBoton;
    }

    public void setNBoton(int boton) {
        nBoton = boton;
    }

    public List<Double> listarParametros() {
        parametros.add(Double.parseDouble(this.jtfVelocidad.getText()));
        parametros.add(Double.parseDouble(this.jtfAngulo.getText()));
        return parametros;
    }

    public int getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(int tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }

    public void informarError() {
        JOptionPane mensaje = new JOptionPane();
        mensaje.showMessageDialog(null, "El Lanzamiento excede los limites", "Error de Lanzamiento", JOptionPane.ERROR_MESSAGE);
    }

    public JLabel getJldatoTVuelo() {
        return jldatoTVuelo;
    }

    public void setTVueloLabel(String text) {
        this.jldatoTVuelo.setText(text);
    }

    public void setPosXLabel(String text) {
        this.jldatoPosX.setText(text);
    }

    public void setPosYLabel(String text) {
        this.jldatoPosY.setText(text);
    }

    public void setJXMaxLabel(String text) {
        this.jldatoXMax.setText(text);
    }

    public void setYMaxLabel(String text) {
        this.jldatoYMax.setText(text);
    }
}
