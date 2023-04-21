package org.freedom.modulos.std.view.dialog.report;

import java.awt.Component;
import java.util.Vector;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.dialog.FFDialogo;

public class DLRBanco extends FFDialogo {

    private static final long serialVersionUID = 1L;

    private JRadioGroup<?, ?> rgOrdem = null;

    private JLabelPad lbOrdem = new JLabelPad("Ordenar por:");

    private Vector<String> vLabs = new Vector<String>();

    private Vector<String> vVals = new Vector<String>();

    public DLRBanco(Component cOrig) {
        super(cOrig);
        setTitulo("Ordem do Relat�rio");
        setAtribos(300, 140);
        vLabs.addElement("C�digo");
        vLabs.addElement("Nome");
        vVals.addElement("C");
        vVals.addElement("N");
        rgOrdem = new JRadioGroup<String, String>(1, 2, vLabs, vVals);
        rgOrdem.setVlrString("N");
        adic(lbOrdem, 7, 0, 80, 15);
        adic(rgOrdem, 7, 20, 280, 30);
    }

    public String getValor() {
        String sRetorno = "";
        if (rgOrdem.getVlrString().compareTo("C") == 0) sRetorno = "CODBANCO"; else if (rgOrdem.getVlrString().compareTo("N") == 0) sRetorno = "NOMEBANCO";
        return sRetorno;
    }
}
