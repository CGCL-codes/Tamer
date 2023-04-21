package org.freedom.modulos.crm.view.frame.crud.plain;

import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

public class FOrigContato extends FDados {

    private static final long serialVersionUID = 1L;

    private JTextFieldPad txtCodOrigCont = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 10, 0);

    private JTextFieldPad txtDescOrigCont = new JTextFieldPad(JTextFieldPad.TP_STRING, 40, 0);

    private JCheckBoxPad cbWeb = new JCheckBoxPad("Publicar na web", "S", "N");

    public FOrigContato() {
        super();
        setTitulo("Origem do contato");
        setAtribos(15, 30, 400, 185);
        montaTela();
    }

    private void montaTela() {
        adicCampo(txtCodOrigCont, 7, 25, 70, 20, "CodOrigCont", "C�d.Orig.", ListaCampos.DB_PK, true);
        adicCampo(txtDescOrigCont, 80, 25, 290, 20, "DescOrigCont", "Origem do contato", ListaCampos.DB_SI, true);
        adicDB(cbWeb, 7, 50, 120, 20, "web", "", true);
        setListaCampos(true, "ORIGCONT", "TK");
    }
}
