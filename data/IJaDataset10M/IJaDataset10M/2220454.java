package AGO.Vista.Paneles.Estrategias;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import AGO.Controlador.Controller;
import AGO.Modelo.Estructuras.Estrategia;
import AGO.Modelo.Estructuras.Fecha;
import AGO.Modelo.Estructuras.ObjetoFinanciero;
import AGO.Vista.ASwing.Componentes.ProyTable;

public class ProyPanel extends JPanel {

    private final JLabel ingreseLosPreciosLabel = new JLabel();

    private final JScrollPane scrollPane = new JScrollPane();

    private ProyTable proyTable;

    private Controller control;

    private final JPanel panel = new JPanel();

    public ProyPanel(Controller control) {
        super();
        this.control = control;
        try {
            jbInit();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        setLayout(new BorderLayout());
        add(scrollPane);
        this.proyTable = new ProyTable(control);
        scrollPane.setViewportView(proyTable);
        this.proyTable.setRowHeaders();
        add(panel, BorderLayout.NORTH);
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(0, 20));
        panel.add(ingreseLosPreciosLabel);
        ingreseLosPreciosLabel.setBounds(10, 0, 424, 16);
        ingreseLosPreciosLabel.setText("Ingrese los precios proyectados para cada instrumento en esta estrategia:");
    }

    public void addFecha(Fecha fecha) {
        this.proyTable.addFecha(fecha);
    }

    public void setEstrategia(Estrategia estr) {
        this.proyTable.setEstrategia(estr);
    }

    public void addInstrumento(ObjetoFinanciero instr) {
        this.proyTable.addInstrumento(instr);
    }

    public void resetInstrumentos() {
        this.proyTable.resetInstrumentos();
    }

    public void resetEstrategias() {
        this.proyTable.resetEstrategias();
    }

    public void resetFechas() {
        this.proyTable.resetFechas();
    }
}
