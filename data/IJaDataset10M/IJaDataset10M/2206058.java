package org.freedom.modulos.std.view.dialog.report;

import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.dialog.FFDialogo;
import java.util.Vector;

public class DLRClComis extends FFDialogo {

    private static final long serialVersionUID = 1L;

    private JRadioGroup<?, ?> rgOrdem = null;

    private JRadioGroup<?, ?> rgTipo = null;

    private JLabelPad lbOrdem = new JLabelPad("Ordenar por:");

    public DLRClComis() {
        setTitulo("Ordem do Relat�rio");
        setAtribos(300, 190);
        Vector<String> vLabs = new Vector<String>();
        Vector<String> vVals = new Vector<String>();
        vLabs.addElement("C�digo");
        vLabs.addElement("Descri��o");
        vVals.addElement("C");
        vVals.addElement("D");
        rgOrdem = new JRadioGroup<String, String>(1, 2, vLabs, vVals);
        rgOrdem.setVlrString("D");
        adic(lbOrdem, 7, 0, 80, 15);
        adic(rgOrdem, 7, 20, 280, 30);
        Vector<String> vLabs1 = new Vector<String>();
        Vector<String> vVals1 = new Vector<String>();
        vLabs1.addElement("Grafico");
        vLabs1.addElement("Texto");
        vVals1.addElement("G");
        vVals1.addElement("T");
        rgTipo = new JRadioGroup<String, String>(1, 2, vLabs1, vVals1);
        rgOrdem.setVlrString("T");
        adic(rgTipo, 7, 60, 280, 30);
    }

    public String getValor() {
        String sRetorno = "DESCCLCOMIS";
        if (rgOrdem.getVlrString().compareTo("C") == 0) {
            sRetorno = "CODCLCOMIS";
        }
        return sRetorno;
    }

    public String getTipo() {
        return rgTipo.getVlrString();
    }
}
