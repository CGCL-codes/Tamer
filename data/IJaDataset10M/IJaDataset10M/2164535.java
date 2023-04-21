package org.freedom.modulos.fnc.view.frame.crud.detail;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import javax.swing.SwingConstants;
import net.sf.jasperreports.engine.JasperPrintManager;
import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.DeleteEvent;
import org.freedom.acao.DeleteListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.modulos.fnc.view.frame.crud.tabbed.FConta;

/**
 * Cadastro bordero de receb�veis.
 * 
 * @author Setpoint Inform�tica Ltda./Alex Rodrigues
 * @version 26/08/2009
 */
public class FBordero extends FDetalhe implements CarregaListener, InsertListener, DeleteListener, FocusListener {

    private static final long serialVersionUID = 1L;

    private static final Color GREEN = new Color(45, 190, 60);

    private static final Color YELLOW = new Color(255, 204, 51);

    private JPanelPad panelMaster = new JPanelPad();

    private JPanelPad panelDetalhe = new JPanelPad();

    private JTextFieldPad txtCodBordero = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

    private JTextFieldPad txtCodConta = new JTextFieldPad(JTextFieldPad.TP_STRING, 10, 0);

    private JTextFieldFK txtDescConta = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

    private JTextFieldPad txtDataBordero = new JTextFieldPad(JTextFieldPad.TP_DATE, 10, 0);

    private JTextAreaPad txaObservacao = new JTextAreaPad(300);

    private JButtonPad btCompletar = new JButtonPad(Icone.novo("btOk.gif"));

    private JTextFieldPad txtCodRec = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

    private JTextFieldPad txtNParcItRec = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

    private JTextFieldFK txtVlrParcItRec = new JTextFieldFK(JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin);

    private JTextFieldFK txtVlrApagItRec = new JTextFieldFK(JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin);

    private JTextFieldFK txtVlrPagoItRec = new JTextFieldFK(JTextFieldPad.TP_DECIMAL, 15, Aplicativo.casasDecFin);

    private JTextFieldFK txtDtPagoItRec = new JTextFieldFK(JTextFieldPad.TP_DATE, 10, 0);

    private JTextFieldFK txtDtVencItRec = new JTextFieldFK(JTextFieldPad.TP_DATE, 10, 0);

    private JTextFieldPad txtStatusItRec = new JTextFieldPad(JTextFieldPad.TP_STRING, 2, 0);

    private JTextFieldPad txtCodDoc = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

    private JTextFieldPad txtCodRecReceber = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

    private JLabelPad lbStatus = new JLabelPad("", SwingConstants.CENTER);

    private ListaCampos lcConta = new ListaCampos(this, "CC");

    private ListaCampos lcItReceber = new ListaCampos(this, "RC");

    private ListaCampos lcReceber = new ListaCampos(this, "RC");

    public FBordero() {
        super(false);
        nav.setNavigation(true);
        setTitulo("Bordero");
        setAtribos(50, 50, 575, 500);
        montaListaCampos();
        montaTela();
        lcDet.addCarregaListener(this);
        lcItReceber.addCarregaListener(this);
        lcReceber.addCarregaListener(this);
        lcDet.addInsertListener(this);
        lcDet.addDeleteListener(this);
        lcDet.addPostListener(this);
        btCompletar.addActionListener(this);
        btImp.addActionListener(this);
        btPrevimp.addActionListener(this);
    }

    private void montaListaCampos() {
        lcConta.add(new GuardaCampo(txtCodConta, "NumConta", "N� Conta", ListaCampos.DB_PK, false));
        lcConta.add(new GuardaCampo(txtDescConta, "DescConta", "Descri��o da conta", ListaCampos.DB_SI, false));
        lcConta.montaSql(false, "CONTA", "FN");
        lcConta.setReadOnly(true);
        txtCodConta.setTabelaExterna(lcConta, FConta.class.getCanonicalName());
        txtCodConta.setFK(true);
        lcItReceber.add(new GuardaCampo(txtStatusItRec, "StatusItRec", "Status", ListaCampos.DB_SI, false));
        lcItReceber.add(new GuardaCampo(txtCodRec, "CodRec", "C�d.rec.", ListaCampos.DB_PK, false));
        lcItReceber.add(new GuardaCampo(txtNParcItRec, "NParcItRec", "C�d.It.rec.", ListaCampos.DB_PK, false));
        lcItReceber.add(new GuardaCampo(txtDtVencItRec, "DtVencItRec", "Vencimento", ListaCampos.DB_SI, false));
        lcItReceber.add(new GuardaCampo(txtDtPagoItRec, "DtPagoItRec", "Dt.Pagto.", ListaCampos.DB_SI, false));
        lcItReceber.add(new GuardaCampo(txtVlrParcItRec, "VlrParcItRec", "Vlr.titulo", ListaCampos.DB_SI, false));
        lcItReceber.add(new GuardaCampo(txtVlrApagItRec, "VlrApagItRec", "Vlr.Aberto", ListaCampos.DB_SI, false));
        lcItReceber.add(new GuardaCampo(txtVlrPagoItRec, "VlrPagoItRec", "Vlr.Pago", ListaCampos.DB_SI, false));
        lcItReceber.montaSql(false, "ITRECEBER IR", "FN");
        lcItReceber.setQueryCommit(false);
        lcItReceber.setReadOnly(true);
        txtCodRec.setTabelaExterna(lcItReceber, null);
        txtCodRec.setFK(true);
        txtCodRec.setNomeCampo("CodRec");
        txtCodRec.addFocusListener(this);
        txtNParcItRec.setTabelaExterna(lcItReceber, null);
        txtNParcItRec.setFK(true);
        txtNParcItRec.setNomeCampo("NParcItRec");
        lcReceber.add(new GuardaCampo(txtCodDoc, "DocRec", "Doc.Rec.", ListaCampos.DB_PK, false));
        lcReceber.add(new GuardaCampo(txtCodRecReceber, "CodRec", "Cod.Rec.", ListaCampos.DB_SI, false));
        lcReceber.montaSql(false, "RECEBER", "FN");
        lcReceber.setQueryCommit(false);
        lcReceber.setReadOnly(true);
        txtCodDoc.setTabelaExterna(lcReceber, null);
        txtCodDoc.setFK(true);
        txtCodDoc.setNomeCampo("DocRec");
    }

    private void montaTela() {
        setAltCab(170);
        setListaCampos(lcCampos);
        setPainel(panelMaster, pnCliCab);
        adicCampo(txtCodBordero, 7, 20, 80, 20, "CodBor", "C�d.bordero", ListaCampos.DB_PK, true);
        adicCampo(txtCodConta, 90, 20, 90, 20, "NumConta", "N� Conta", ListaCampos.DB_FK, txtDescConta, true);
        adicDescFK(txtDescConta, 183, 20, 260, 20, "DescConta", "Descri��o da conta");
        adicCampo(txtDataBordero, 446, 20, 100, 20, "DtBor", "Data", ListaCampos.DB_SI, true);
        adicDB(txaObservacao, 7, 60, 539, 60, "ObsBor", "Observa��o", false);
        setListaCampos(true, "BORDERO", "FN");
        lcCampos.setQueryInsert(false);
        setAltDet(140);
        setPainel(panelDetalhe, pnDet);
        setListaCampos(lcDet);
        setNavegador(navRod);
        panelDetalhe.adic(new JLabelPad("Doc.Rec"), 7, 0, 90, 20);
        panelDetalhe.adic(txtCodDoc, 7, 20, 90, 20);
        adicCampo(txtCodRec, 7, 60, 90, 20, "CodRec", "C�d.rec.", ListaCampos.DB_PK, true);
        adicCampo(txtNParcItRec, 100, 60, 67, 20, "NParcItRec", "Parcela", ListaCampos.DB_PF, true);
        adicDescFK(txtDtVencItRec, 170, 60, 120, 20, "DtVencItRec", "Vencimento");
        adicDescFK(txtDtPagoItRec, 293, 60, 120, 20, "DtPagoItRec", "Dt.Pagto.");
        adic(lbStatus, 416, 60, 120, 20);
        adicDescFK(txtVlrParcItRec, 170, 100, 120, 20, "VlrParcItRec", "Vlr.titulo");
        adicDescFK(txtVlrPagoItRec, 293, 100, 120, 20, "VlrPagoItRec", "Vlr.Pago");
        adicDescFK(txtVlrApagItRec, 416, 100, 120, 20, "VlrApagItRec", "Vlr.Aberto");
        setListaCampos(true, "ITBORDERO", "FN");
        lcDet.setQueryInsert(false);
        montaTab();
        pnGImp.removeAll();
        pnGImp.setLayout(new GridLayout(1, 3));
        pnGImp.setPreferredSize(new Dimension(93, 26));
        pnGImp.add(btCompletar);
        pnGImp.add(btImp);
        pnGImp.add(btPrevimp);
        setImprimir(true);
        txtCodRec.setFK(true);
        navRod.setAtivo(Navegador.BT_EDITAR, false);
        lbStatus.setForeground(Color.WHITE);
        lbStatus.setFont(new Font("Arial", Font.BOLD, 13));
        lbStatus.setOpaque(true);
        lbStatus.setVisible(false);
    }

    private void showStatus() {
        lbStatus.setVisible(true);
        if ("RB".equals(txtStatusItRec.getVlrString())) {
            lbStatus.setVisible(false);
        } else if ("CR".equals(txtStatusItRec.getVlrString())) {
            lbStatus.setText("Cancelada");
            lbStatus.setBackground(Color.DARK_GRAY);
        } else if ("RP".equals(txtStatusItRec.getVlrString()) && txtVlrApagItRec.getVlrBigDecimal().doubleValue() == 0) {
            lbStatus.setText("Recebida");
            lbStatus.setBackground(GREEN);
        } else if (txtVlrPagoItRec.getVlrBigDecimal().doubleValue() > 0) {
            lbStatus.setText("Rec. parcial");
            lbStatus.setBackground(Color.BLUE);
        }
        if (txtDtVencItRec.getVlrDate() != null) {
            if (txtDtVencItRec.getVlrDate().before(Calendar.getInstance().getTime())) {
                lbStatus.setText("Vencida");
                lbStatus.setBackground(Color.RED);
            } else if (txtDtVencItRec.getVlrDate().after(Calendar.getInstance().getTime())) {
                lbStatus.setText("� vencer");
                lbStatus.setBackground(YELLOW);
            }
        } else {
            lbStatus.setVisible(false);
        }
    }

    private void concluiBordero() {
        if (txtCodBordero.getVlrInteger() <= 0) {
            return;
        }
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE FNBORDERO SET STATUSBOR='BC' WHERE CODEMP=? AND CODFILIAL=? AND CODBOR=?");
            ps.setInt(1, Aplicativo.iCodEmp);
            ps.setInt(2, ListaCampos.getMasterFilial("FNBORDERO"));
            ps.setInt(3, txtCodBordero.getVlrInteger());
            ps.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void recuperaCodDoc() {
        if (txtCodRec.getVlrString().trim().length() == 0) return;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DocRec FROM FNRECEBER IR WHERE CODEMP=? AND CODFILIAL = ? AND CODREC=?");
        PreparedStatement pstmt;
        try {
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setInt(1, Aplicativo.iCodEmp);
            pstmt.setInt(2, ListaCampos.getMasterFilial("FNRECEBER"));
            pstmt.setInt(3, txtCodRec.getVlrInteger());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) txtCodDoc.setVlrInteger(rs.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void imprimir(boolean visualizar) {
        if (txtCodBordero.getVlrInteger() == 0) {
            return;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CL.CPFCLI,CL.CNPJCLI,CL.FONECLI, B.CODBOR, B.NUMCONTA, C.DESCCONTA, B.NUMCONTABOR, C2.DESCCONTA DESCCONTABOR, B.DTBOR, B.OBSBOR,");
        sql.append("IB.CODREC, IB.NPARCITREC, R.CODCLI, CL.RAZCLI, IR.VLRITREC, IR.DTVENCITREC,R.DOCREC,C.AGENCIACONTA ");
        sql.append("FROM FNBORDERO B, FNITBORDERO IB, FNCONTA C, FNCONTA C2, ");
        sql.append("FNITRECEBER IR, FNRECEBER R, VDCLIENTE CL ");
        sql.append("WHERE B.CODEMP=? AND B.CODFILIAL=? AND B.CODBOR=? AND ");
        sql.append("C.CODEMP=B.CODEMPCC AND C.CODFILIAL=B.CODFILIALCC AND C.NUMCONTA=B.NUMCONTA AND ");
        sql.append("C2.CODEMP=B.CODEMPCB AND C2.CODFILIAL=B.CODFILIALCB AND C2.NUMCONTA=B.NUMCONTABOR AND ");
        sql.append("IB.CODEMP=B.CODEMP AND IB.CODFILIAL=B.CODFILIAL AND IB.CODBOR=B.CODBOR AND ");
        sql.append("IR.CODEMP=IB.CODEMPRC AND IR.CODFILIAL=IB.CODFILIALRC AND IR.CODREC=IB.CODREC AND IR.NPARCITREC=IB.NPARCITREC AND ");
        sql.append("R.CODEMP=IR.CODEMP AND R.CODFILIAL=IR.CODFILIAL AND R.CODREC=IR.CODREC AND ");
        sql.append("CL.CODEMP=R.CODEMPCL AND CL.CODFILIAL=R.CODFILIALCL AND CL.CODCLI=R.CODCLI ");
        sql.append("ORDER BY IB.NPARCITREC");
        ResultSet rs = null;
        try {
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setInt(1, Aplicativo.iCodEmp);
            ps.setInt(2, ListaCampos.getMasterFilial("FNBORDERO"));
            ps.setInt(3, txtCodBordero.getVlrInteger());
            rs = ps.executeQuery();
            FPrinterJob dlGr = new FPrinterJob("relatorios/FBordero.jasper", "Bordero de receb�veis", null, rs, null, this);
            if (visualizar) {
                dlGr.setVisible(true);
            } else {
                try {
                    JasperPrintManager.printReport(dlGr.getRelatorio(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                    Funcoes.mensagemErro(this, "Erro na impress�o!" + e.getMessage(), true, con, e);
                }
            }
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            Funcoes.mensagemErro(this, "Erro na pesquisa!" + e.getMessage(), true, con, e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btCompletar) {
            concluiBordero();
        } else if (e.getSource() == btImp) {
            imprimir(false);
        } else if (e.getSource() == btPrevimp) {
            imprimir(true);
        } else {
            super.actionPerformed(e);
        }
    }

    public void beforeCarrega(CarregaEvent e) {
    }

    public void afterCarrega(CarregaEvent e) {
        if (e.getListaCampos() == lcItReceber) {
            showStatus();
        } else if (e.getListaCampos() == lcReceber) {
            txtCodRec.setVlrInteger(txtCodRecReceber.getVlrInteger());
        }
    }

    public void beforeInsert(InsertEvent e) {
        lbStatus.setVisible(false);
    }

    public void afterInsert(InsertEvent e) {
    }

    public void beforeDelete(DeleteEvent e) {
    }

    public void afterDelete(DeleteEvent e) {
        lbStatus.setVisible(false);
    }

    public void focusGained(FocusEvent arg0) {
    }

    public void focusLost(FocusEvent evt) {
        if (evt.getSource() == txtCodRec) {
            txtCodRecReceber.setVlrInteger(txtCodRec.getVlrInteger());
            recuperaCodDoc();
            txtNParcItRec.requestFocus();
        }
    }

    @Override
    public void afterPost(PostEvent e) {
        super.afterPost(e);
        showStatus();
    }

    public void setConexao(DbConnection con) {
        super.setConexao(con);
        lcConta.setConexao(con);
        lcItReceber.setConexao(con);
        lcReceber.setConexao(con);
    }
}
