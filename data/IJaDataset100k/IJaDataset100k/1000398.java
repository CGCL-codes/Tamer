package org.digitall.apps.personalfiles.interfaces;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JComboBox;
import org.digitall.common.personalfiles.classes.Persona;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.common.reports.BasicReport;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.BorderPanel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.PrintButton;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.environment.Environment;

public class NotaInformePanel extends BasicPrimitivePanel {

    private BasicPanel content = new BasicPanel();

    private CBInput meses = new CBInput(0, "Mes", false);

    private TFInput TFResolucion = new TFInput(DataTypes.STRING, "Resoluci�n", true);

    private TFInput TFMotivo = new TFInput(DataTypes.STRING, "Motivo", true);

    private TFInput TFEmisor = new TFInput(DataTypes.STRING, "Emisor", true);

    private PrintButton btnPrintNota = new PrintButton();

    private CloseButton btnClose = new CloseButton();

    private Vector fieldsVector = new Vector();

    private Vector mesesVector = new Vector();

    public NotaInformePanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(400, 210));
        content.setLayout(null);
        content.setBorder(BorderPanel.getBorderPanel(""));
        meses.setBounds(new Rectangle(30, 10, 345, 35));
        cargarVectorMeses();
        cargarMeses();
        btnPrintNota.setToolTipText("Imprimir Nota al Intendente");
        btnClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnClose_actionPerformed(e);
            }
        });
        btnPrintNota.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnPrintNota_actionPerformed(e);
            }
        });
        TFResolucion.setBounds(new Rectangle(30, 45, 345, 35));
        TFMotivo.setBounds(new Rectangle(30, 85, 345, 35));
        TFEmisor.setBounds(new Rectangle(30, 120, 345, 35));
        fieldsVector.add(TFResolucion);
        fieldsVector.add(TFMotivo);
        fieldsVector.add(TFEmisor);
        content.add(TFEmisor, null);
        content.add(TFMotivo, null);
        content.add(TFResolucion, null);
        content.add(meses, null);
        addButton(btnClose);
        addButton(btnPrintNota);
        this.add(content, BorderLayout.CENTER);
    }

    private void cargarMeses() {
        for (int i = 0; i < mesesVector.size(); i++) {
            meses.addItem(mesesVector.elementAt(i).toString());
        }
    }

    private void cargarVectorMeses() {
        mesesVector.add("Enero");
        mesesVector.add("Febrero");
        mesesVector.add("Marzo");
        mesesVector.add("Abril");
        mesesVector.add("Mayo");
        mesesVector.add("Junio");
        mesesVector.add("Julio");
        mesesVector.add("Agosto");
        mesesVector.add("Septiembre");
        mesesVector.add("Octubre");
        mesesVector.add("Noviembre");
        mesesVector.add("Diciembre");
    }

    private void btnPrintNota_actionPerformed(ActionEvent e) {
        imprimir();
    }

    private void imprimir() {
        if (pasoControles()) {
            BasicReport report = new BasicReport(DependenciaTree.class.getResource("xml/NotaMensualIntendente.xml"));
            String currentMes = "" + mesesVector.elementAt(Integer.parseInt(Environment.currentMonth) - 1).toString();
            String intendente = getNameIntendente();
            report.setProperty("intendente", intendente);
            String fechaNota = "" + Environment.defaultLocation + ", " + Environment.currentDay + " de " + currentMes + " del " + Environment.currentYear + ".";
            String nota = "De mi mayor consideraci�n: \n\n Tengo el agrado de dirigirme a Ud. a los efectos de adjuntarle el informe mensual correspondiente al mes de " + currentMes + ", por el motivo: " + TFMotivo.getValue().toString() + ", conforme a lo estipulado seg�n Resoluci�n N�: " + TFResolucion.getValue().toString() + ".-\n\n\t Sin otro particular saludo a Ud. cordialmente.";
            report.setProperty("nota", nota);
            report.setProperty("fechanota", fechaNota);
            report.setProperty("emisor", TFEmisor.getValue().toString());
            report.addTableModel("personalfiles.getPerson", "290");
            report.doReport();
        } else {
            Advisor.messageBox("Debe completar todos los campos.", "Error");
        }
    }

    private String getNameIntendente() {
        String resultado = "";
        Persona persona = new Persona();
        persona.setIdPerson(290);
        persona.retrieveData();
        resultado = "" + persona.getFirstName() + " " + persona.getLastName();
        return (resultado);
    }

    private boolean pasoControles() {
        boolean incompleto = false;
        int i = 0;
        while ((i < fieldsVector.size()) && (!incompleto)) {
            TFInput TFAux = (TFInput) fieldsVector.elementAt(i);
            if (!TFAux.getValue().toString().equals("")) {
                i++;
            } else {
                incompleto = true;
            }
        }
        return (!incompleto);
    }

    private void btnClose_actionPerformed(ActionEvent e) {
        getParentInternalFrame().close();
    }
}
