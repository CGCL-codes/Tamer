package com.onetwork.core.ui.components.dialog.actions;

import com.onetwork.core.ui.components.dialog.ListagemDialog;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

public class ProximoRegistroAction extends AbstractAction {

    private ListagemDialog listagemDialog;

    public ProximoRegistroAction(ListagemDialog listagemDialog) {
        super("Proximo registro", new ImageIcon(ProximoRegistroAction.class.getResource("/imagens/navigate_right.png")));
        this.putValue(SHORT_DESCRIPTION, "Posiciona no proximo registro");
        this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, ActionEvent.ALT_MASK));
        this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_PAGE_UP));
        this.listagemDialog = listagemDialog;
    }

    public void actionPerformed(ActionEvent event) {
        this.listagemDialog.proximoRegistro();
    }
}
