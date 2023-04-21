package org.freedom.modulos.crm.view.dialog.utility;

import java.awt.Component;
import java.awt.event.ActionEvent;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;

/**
 * Dialogo de ajuste para campos n�o compat�veis entre Contato e Cliente.
 * 
 * @author Setpoint Inform�tica Ltda./Fernando Oliveira da Silva
 * @version 09/09/2009 - Alex Rodrigues
 */
public class DLContToCli extends FFDialogo {

    private static final long serialVersionUID = 1L;

    private JTextFieldPad txtCodTipoCli = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

    private JTextFieldFK txtDescTipoCli = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

    private JTextFieldPad txtCodClasCli = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

    private JTextFieldFK txtDescClasCli = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

    private JTextFieldPad txtCodSetor = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

    private JTextFieldFK txtDescSetor = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

    private ListaCampos lcTipoCli = new ListaCampos(this, "");

    private ListaCampos lcClasCli = new ListaCampos(this, "");

    private ListaCampos lcSetor = new ListaCampos(this, "");

    public DLContToCli(Component cOrig, Integer codsetor, Integer codtipocli) {
        super(cOrig);
        setTitulo("Migrar contato para cliente");
        setAtribos(435, 230);
        lcTipoCli.add(new GuardaCampo(txtCodTipoCli, "CodTipoCli", "C�d.Cli.", ListaCampos.DB_PK, true));
        lcTipoCli.add(new GuardaCampo(txtDescTipoCli, "DescTipoCli", "Raz�o Social", ListaCampos.DB_SI, false));
        lcTipoCli.montaSql(false, "TIPOCLI", "VD");
        lcTipoCli.setReadOnly(true);
        txtCodTipoCli.setTabelaExterna(lcTipoCli, null);
        txtCodTipoCli.setFK(true);
        txtCodTipoCli.setNomeCampo("CodTipoCli");
        txtCodTipoCli.setVlrInteger(codtipocli);
        lcClasCli.add(new GuardaCampo(txtCodClasCli, "CodClasCli", "C�digo", ListaCampos.DB_PK, txtDescClasCli, true));
        lcClasCli.add(new GuardaCampo(txtDescClasCli, "DescClasCli", "Descri�ao", ListaCampos.DB_SI, false));
        lcClasCli.montaSql(false, "CLASCLI", "VD");
        lcClasCli.setReadOnly(true);
        txtCodClasCli.setTabelaExterna(lcClasCli, null);
        txtCodClasCli.setFK(true);
        txtCodClasCli.setNomeCampo("CodClasCli");
        lcSetor.add(new GuardaCampo(txtCodSetor, "CodSetor", "C�digo", ListaCampos.DB_PK, txtDescSetor, false));
        lcSetor.add(new GuardaCampo(txtDescSetor, "DescSetor", "Descri�ao", ListaCampos.DB_SI, false));
        lcSetor.montaSql(false, "SETOR", "VD");
        lcSetor.setReadOnly(true);
        txtCodSetor.setTabelaExterna(lcSetor, null);
        txtCodTipoCli.setFK(true);
        txtCodSetor.setNomeCampo("CodSetor");
        txtCodSetor.setVlrInteger(codsetor);
        adic(new JLabelPad("C�d.tp.cli."), 7, 5, 100, 20);
        adic(txtCodTipoCli, 7, 25, 100, 20);
        adic(new JLabelPad("Descri��o do tipo de cliente"), 110, 5, 300, 20);
        adic(txtDescTipoCli, 110, 25, 300, 20);
        adic(new JLabelPad("C�d.clas.cli"), 7, 45, 100, 20);
        adic(txtCodClasCli, 7, 65, 100, 20);
        adic(new JLabelPad("Descri��o da classifica��o do cliente"), 110, 45, 300, 20);
        adic(txtDescClasCli, 110, 65, 300, 20);
        adic(new JLabelPad("C�d.setor"), 7, 85, 100, 20);
        adic(txtCodSetor, 7, 105, 100, 20);
        adic(new JLabelPad("Descri��o do setor"), 110, 85, 300, 20);
        adic(txtDescSetor, 110, 105, 300, 20);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btOK) {
            if (txtCodTipoCli.getText().trim().length() == 0) {
                Funcoes.mensagemInforma(this, "O campo tipo de cliente est� em branco! ! !");
                txtCodTipoCli.requestFocus();
                return;
            }
            if (txtCodClasCli.getText().trim().length() == 0) {
                Funcoes.mensagemInforma(this, "O campo classifica��o do cliente est� em branco! ! !");
                txtCodClasCli.requestFocus();
                return;
            }
        }
        super.actionPerformed(e);
    }

    public ContatoClienteBean getValores() {
        ContatoClienteBean contatoClienteBean = new ContatoClienteBean();
        contatoClienteBean.setTipo(txtCodTipoCli.getVlrInteger());
        contatoClienteBean.setClassificacao(txtCodClasCli.getVlrInteger());
        contatoClienteBean.setSetor(txtCodSetor.getVlrInteger());
        return contatoClienteBean;
    }

    public void setConexao(DbConnection cn) {
        lcTipoCli.setConexao(cn);
        lcClasCli.setConexao(cn);
        lcSetor.setConexao(cn);
        lcSetor.carregaDados();
        lcTipoCli.carregaDados();
    }

    public class ContatoClienteBean {

        private Integer tipo;

        private Integer classificacao;

        private Integer setor;

        public Integer getTipo() {
            return tipo;
        }

        public void setTipo(Integer tipo) {
            this.tipo = tipo;
        }

        public Integer getClassificacao() {
            return classificacao;
        }

        public void setClassificacao(Integer classificacao) {
            this.classificacao = classificacao;
        }

        public Integer getSetor() {
            return setor;
        }

        public void setSetor(Integer setor) {
            this.setor = setor;
        }
    }
}
