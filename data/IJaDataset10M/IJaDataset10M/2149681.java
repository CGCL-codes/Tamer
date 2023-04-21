package org.digitall.apps.accionsocial.interfaces;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.border.EtchedBorder;
import org.digitall.apps.accionsocial.classes.Coordinador;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.common.reports.BasicReport;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.sql.LibSQL;

public class PopupImprimirReportes extends JPopupMenu {

    private JMenuItem miEntregasDia = new JMenuItem("Imprimir Entregas del D�a");

    private JMenuItem miListadoPlanSocialPancitas = new JMenuItem("Imprimir Listado de Beneficiarios de Plan Social...");

    private JMenuItem miListadoBeneficiarios = new JMenuItem("Imprimir Listado de Beneficiarios de Planes Sociales");

    private JMenuItem miExit = new JMenuItem("Salir");

    Coordinador coordinador;

    public PopupImprimirReportes(Coordinador _coordinador) {
        coordinador = _coordinador;
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setBackground(new Color(0, 132, 198));
        this.add(miListadoBeneficiarios);
        this.add(miListadoPlanSocialPancitas);
        this.add(miEntregasDia);
        this.add(miExit);
        miEntregasDia.setForeground(Color.white);
        miListadoBeneficiarios.setForeground(Color.white);
        miListadoPlanSocialPancitas.setForeground(Color.white);
        miExit.setForeground(Color.white);
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        miEntregasDia.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                clickEntregasDia();
            }
        });
        miListadoBeneficiarios.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                clickListadoBeneficiarios();
            }
        });
        miListadoPlanSocialPancitas.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                clickListadoBeneficiariosPlanSocialPancitas();
            }
        });
        miExit.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && e.getButton() == e.BUTTON3) {
                    clickExit(e);
                }
            }
        });
    }

    private void clickEntregasDia() {
        BasicReport report = new BasicReport(PanelBusquedaPersona.class.getResource("xml/EntregasDelDia.xml"));
        report.addTableModel("accionsocial.xmlGetEntregasDelDia", "");
        report.setProperty("title", "ENTREGAS DEL D�A");
        report.doReport();
    }

    private void clickListadoBeneficiarios() {
        BasicReport report = new BasicReport(PanelBusquedaPersona.class.getResource("xml/ListadoBeneficiarios.xml"));
        report.addTableModel("accionsocial.xmlgetbeneficiariosplanessociales", "");
        report.setProperty("title", "Listado de Beneficiarios");
        report.doReport();
    }

    private void clickListadoBeneficiariosPlanSocialPancitas() {
        CBInput cbPlanesSociales = new CBInput(0, "Plan Social", false);
        cbPlanesSociales.loadJCombo(LibSQL.exFunction("accionsocial.getallplanessociales", ""));
        Vector<String> _columns = new Vector<String>();
        for (int i = 0; i < cbPlanesSociales.getCombo().getItemsVector().size(); i++) {
            if (cbPlanesSociales.getCombo().getItemsVector().elementAt(i).toString().length() > 0) {
                _columns.add(cbPlanesSociales.getCombo().getItemsVector().elementAt(i).toString());
            }
        }
        String _planSocialSeleccionado = ((String) JOptionPane.showInternalInputDialog(Environment.getActiveDesktop(), "Seleccione el Plan Social", "Planes Sociales", JOptionPane.QUESTION_MESSAGE, null, _columns.toArray(), cbPlanesSociales.getSelectedItem()));
        String xml = "";
        String consulta = "";
        if (_planSocialSeleccionado != null) {
            cbPlanesSociales.setSelectedValue(_planSocialSeleccionado);
            if (Integer.parseInt(cbPlanesSociales.getSelectedValue().toString()) == 1) {
                xml = "xml/ListadoBeneficiariosNutrivida.xml";
                consulta = "accionsocial.xmlgetbeneficiariosplansocialnutrivida";
            } else if (Integer.parseInt(cbPlanesSociales.getSelectedValue().toString()) == 2) {
                xml = "xml/ListadoBeneficiariosPlanSocialPancitas.xml";
                consulta = "accionsocial.xmlgetbeneficiariosplansocialpancitas";
            } else if (Integer.parseInt(cbPlanesSociales.getSelectedValue().toString()) == 3) {
                xml = "xml/ListadoBeneficiariosPlanSocialCeliacos.xml";
                consulta = "accionsocial.xmlgetbeneficiariosplansocialceliacos";
            } else if (Integer.parseInt(cbPlanesSociales.getSelectedValue().toString()) == 4) {
                xml = "xml/ListadoBeneficiariosPlanSocialTBC.xml";
                consulta = "accionsocial.xmlgetbeneficiariosplansocialtbc";
            } else if (Integer.parseInt(cbPlanesSociales.getSelectedValue().toString()) == 5) {
                xml = "xml/ListadoBeneficiariosPlanSocialTarjetaSocial.xml";
                consulta = "accionsocial.xmlgetbeneficiariosplansocialtarjetasocial";
            }
            BasicReport report = new BasicReport(PanelBusquedaPersona.class.getResource(xml));
            report.addTableModel(consulta, cbPlanesSociales.getSelectedValue());
            report.setProperty("title", "Beneficiarios del Plan Social");
            report.setProperty("title1", _planSocialSeleccionado);
            report.doReport();
        }
    }

    private void clickExit(MouseEvent e) {
    }

    public int getAlto() {
        return (this.getComponentCount() * 21);
    }
}
