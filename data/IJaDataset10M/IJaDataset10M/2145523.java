package org.freedom.acao;

import org.freedom.library.swing.component.JTablePad;

public class TabelaEditEvent {

    private JTablePad tab = null;

    public TabelaEditEvent(JTablePad tb) {
        tab = tb;
    }

    public JTablePad getTabela() {
        return tab;
    }
}
