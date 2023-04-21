package org.freedom.modulos.std.view.dialog.utility;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.dialog.DLRelatorio;

public class DLChecaLFSaida extends DLRelatorio {

    private static final long serialVersionUID = 1L;

    public JTablePad tab = new JTablePad();

    private JScrollPane spnTab = new JScrollPane(tab);

    private JPanelPad pnCliente = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

    public DLChecaLFSaida() {
        setTitulo("Inconsist�ncias de Vendas");
        setAtribos(600, 320);
        c.add(pnCliente, BorderLayout.CENTER);
        pnCliente.add(spnTab, BorderLayout.CENTER);
        tab.adicColuna("Pedido");
        tab.adicColuna("S�rie");
        tab.adicColuna("Nota");
        tab.adicColuna("Emiss�o");
        tab.adicColuna("Inconsist�ncia");
        tab.setTamColuna(80, 0);
        tab.setTamColuna(40, 1);
        tab.setTamColuna(80, 2);
        tab.setTamColuna(100, 3);
        tab.setTamColuna(200, 4);
    }

    public void imprimir(boolean bVal) {
        if (bVal) {
            System.out.println("imprimiu");
        }
    }
}

;
