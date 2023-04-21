package fullflow;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author blackmoon
 */
public class Ventana extends JFrame {

    public String slash = "\\";

    public ArrayList<JFigura> figurasACopiar = new ArrayList<JFigura>();

    public boolean pegarHabilitado = false;

    public boolean copiarHabilitado = false;

    private BarraFiguras barraFiguras;

    private BarraPrincipal barraAcciones;

    private PanelCentral panelCentral;

    private JPanel panelSur;

    private PanelFont panelConfFuente;

    private PanelNombre panelConfNombre;

    private String sistemaOperativo = System.getProperty("os.name");

    Image icon;

    public Font fuente;

    public Ventana() throws HeadlessException {
        this.definirSlash();
        this.configuraciones();
        panelConfFuente = new PanelFont(this);
        panelConfNombre = new PanelNombre(this);
        panelSur = new JPanel();
        this.configuracionPanelSur();
        barraFiguras = new BarraFiguras(this);
        barraAcciones = new BarraPrincipal(this);
        panelCentral = new PanelCentral(this);
        this.add(barraAcciones, BorderLayout.NORTH);
        this.add(barraFiguras, BorderLayout.WEST);
        this.add(panelCentral, BorderLayout.CENTER);
        this.add(panelSur, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    public void configuraciones() {
        this.setTitle("FullFlow");
        this.setSize(1200, 700);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void mostrarBarraFiguras() {
        barraFiguras.setVisible(true);
    }

    public void esconderBarraFigura() {
        this.remove(barraFiguras);
    }

    private void definirSlash() {
        if (sistemaOperativo.equals("Linux")) {
            this.slash = "/";
        }
    }

    public String getSlash() {
        return slash;
    }

    public void setSlash(String slash) {
        this.slash = slash;
    }

    public void agregarPanelNombre() {
        this.panelConfNombre.setVisible(true);
        panelConfNombre.getNombreDelProyecto().requestFocus();
    }

    public void agregarPanelFont() {
        this.panelConfFuente.setVisible(true);
    }

    private void configuracionPanelSur() {
        panelSur.setLayout(new BorderLayout());
        this.panelSur.add(panelConfFuente, BorderLayout.SOUTH);
        this.panelSur.add(panelConfNombre, BorderLayout.CENTER);
    }

    void agregarPestaña(String text) {
        panelCentral.agregarPestañas(text);
    }

    void eliminarTab() {
        panelCentral.elinarPestaña();
    }

    public BarraPrincipal getBarraAcciones() {
        return barraAcciones;
    }

    public void setBarraAcciones(BarraPrincipal barraAcciones) {
        this.barraAcciones = barraAcciones;
    }

    public BarraFiguras getBarraFiguras() {
        return barraFiguras;
    }

    public void setBarraFiguras(BarraFiguras barraFiguras) {
        this.barraFiguras = barraFiguras;
    }

    public PanelCentral getPanelCentral() {
        return panelCentral;
    }

    public void setPanelCentral(PanelCentral panelCentral) {
        this.panelCentral = panelCentral;
    }

    public PanelFont getPanelConfFuente() {
        return panelConfFuente;
    }

    public void setPanelConfFuente(PanelFont panelConfFuente) {
        this.panelConfFuente = panelConfFuente;
    }

    public PanelNombre getPanelConfNombre() {
        return panelConfNombre;
    }

    public void setPanelConfNombre(PanelNombre panelConfNombre) {
        this.panelConfNombre = panelConfNombre;
    }

    public JPanel getPanelSur() {
        return panelSur;
    }

    public void setPanelSur(JPanel panelSur) {
        this.panelSur = panelSur;
    }
}
