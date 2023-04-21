package org.larOzanam.sindicancia.view;

import com.celiosilva.swingDK.dataFields.MaskField;
import com.celiosilva.swingDK.dataFields.TextAreaField;
import com.celiosilva.swingDK.frames.InternalFrame;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.larOzanam.arquitetura.excecoes.ExcecaoAcessoNegado;
import org.larOzanam.arquitetura.excecoes.ExcecaoNegocio;
import org.larOzanam.arquitetura.excecoes.ExcecaoSistema;
import org.larOzanam.arquitetura.util.Funcoes;
import org.larOzanam.business.SindicanteBusiness;
import org.larOzanam.model.PedidoAdmissao;
import org.larOzanam.model.Sindicancia;
import org.larOzanam.model.Sindicante;
import org.larOzanam.view.mascaras.MascarasLarOzanam;

/**
 *
 * @author  Administrador
 */
public abstract class SindicanciaView extends InternalFrame {

    private Set<Sindicante> sindicantesSelecionados = null;

    public abstract void consultarPedidoAdmissaoESindicancia();

    public abstract void salvarSindicancia();

    public abstract void aprovarSindicancia();

    public abstract void reprovarSindicancia();

    public abstract void alterarPedidoAdmissao() throws ExcecaoAcessoNegado;

    public Set<Sindicante> getSindicantesSelecionados() {
        return sindicantesSelecionados;
    }

    public void setSindicantesSelecionados(Set<Sindicante> sindicantesSelecionados) {
        this.sindicantesSelecionados = new LinkedHashSet<Sindicante>(sindicantesSelecionados);
    }

    /**
     * Creates new form AbstractSindicanciaView
     */
    public SindicanciaView() {
        initComponents();
        this.carregarSindicanteResponsavel();
    }

    private void initComponents() {
        txtProsAdmissao1 = new javax.swing.JTextArea();
        statusBarSindicancia = new com.celiosilva.swingDK.dataFields.StatusBar();
        tbpneEquipe_Avaliacao = new javax.swing.JTabbedPane();
        pneEquipeSindicancia = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstEquipeSindicante = new javax.swing.JList();
        btnRemoverSindicante = new com.celiosilva.swingDK.buttons.BtnCancelar();
        btnAdicionarSindicante = new com.celiosilva.swingDK.buttons.BtnIncluir();
        jLabel6 = new javax.swing.JLabel();
        txtDtInclusaoSindicancia = new com.celiosilva.swingDK.dataFields.DateField();
        jLabel3 = new javax.swing.JLabel();
        txtDtSindicanciaAgendada = new com.celiosilva.swingDK.dataFields.DateField();
        txtHrSindicanca = new MaskField(MascarasLarOzanam.getMascaraHorario());
        labelhora = new javax.swing.JLabel();
        cmbSindicanteResponsavel = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        jLabel2 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        pneAvaliacaoSindicancia = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        chkSindicanteAprovam = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        txtDtAprovacaoSindicancia = new com.celiosilva.swingDK.dataFields.DateField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtProsAdmissao = new TextAreaField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtContraAdmissao = new TextAreaField();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtConsensoGeral = new TextAreaField();
        btnSalvarSindicancia = new com.celiosilva.swingDK.buttons.BtnSalvar();
        btnSair1 = new com.celiosilva.swingDK.buttons.BtnSair();
        btnReprovarSindicancia = new com.celiosilva.swingDK.buttons.BtnReprovar();
        btnAprovarSindicancia = new com.celiosilva.swingDK.buttons.BtnAprovar();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCodPedidoAdmissao = new com.celiosilva.swingDK.dataFields.IntegerField();
        jLabel5 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtEstadoPedidoAdmissao = new com.celiosilva.swingDK.dataFields.LetterField();
        btnConsultarPedidoAdmissao = new com.celiosilva.swingDK.buttons.BtnConsultar();
        txtDtPedidoAdmissao = new com.celiosilva.swingDK.dataFields.DateField();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtNmIdoso = new com.celiosilva.swingDK.dataFields.LetterField();
        jLabel8 = new javax.swing.JLabel();
        txtNrIdentidade = new com.celiosilva.swingDK.dataFields.AlphaNumericField();
        txtNrCPF = new MaskField(MascarasLarOzanam.getMascaraCPF());
        jLabel9 = new javax.swing.JLabel();
        btnAjuda1 = new com.celiosilva.swingDK.buttons.BtnAjuda();
        btnAlterarPedidoAdmissao = new com.celiosilva.swingDK.buttons.BtnAlterar();
        txtProsAdmissao1.setColumns(20);
        txtProsAdmissao1.setRows(5);
        setExitButton(btnSair1);
        lstEquipeSindicante.setNextFocusableComponent(btnAdicionarSindicante);
        jScrollPane1.setViewportView(lstEquipeSindicante);
        btnRemoverSindicante.setText("btnCancelar1");
        btnRemoverSindicante.setMessage("Remover Sindicante");
        btnRemoverSindicante.setNextFocusableComponent(btnSalvarSindicancia);
        btnRemoverSindicante.setStatusBar(statusBarSindicancia);
        btnRemoverSindicante.setVerifyInputWhenFocusTarget(false);
        btnRemoverSindicante.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverSindicanteActionPerformed(evt);
            }
        });
        btnAdicionarSindicante.setText("btnIncluir1");
        btnAdicionarSindicante.setMessage("Incluir Sindicante");
        btnAdicionarSindicante.setNextFocusableComponent(btnRemoverSindicante);
        btnAdicionarSindicante.setStatusBar(statusBarSindicancia);
        btnAdicionarSindicante.setVerifyInputWhenFocusTarget(false);
        btnAdicionarSindicante.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarSindicanteActionPerformed(evt);
            }
        });
        jLabel6.setText("Equipe de Sindicantes");
        txtDtInclusaoSindicancia.setText("");
        txtDtInclusaoSindicancia.setNextFocusableComponent(cmbSindicanteResponsavel);
        jLabel3.setText("Data Sindicância");
        txtDtSindicanciaAgendada.setNextFocusableComponent(txtHrSindicanca);
        txtHrSindicanca.setText("  ");
        txtHrSindicanca.setNextFocusableComponent(lstEquipeSindicante);
        labelhora.setText("Horário Sindicância");
        cmbSindicanteResponsavel.setNextFocusableComponent(txtDtSindicanciaAgendada);
        cmbSindicanteResponsavel.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {

            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }

            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }

            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                cmbSindicanteResponsavelPopupMenuWillBecomeVisible(evt);
            }
        });
        cmbSindicanteResponsavel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSindicanteResponsavelActionPerformed(evt);
            }
        });
        jLabel2.setText("Sindicante Responsável");
        jLabel14.setText("Data Inclusão");
        javax.swing.GroupLayout pneEquipeSindicanciaLayout = new javax.swing.GroupLayout(pneEquipeSindicancia);
        pneEquipeSindicancia.setLayout(pneEquipeSindicanciaLayout);
        pneEquipeSindicanciaLayout.setHorizontalGroup(pneEquipeSindicanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pneEquipeSindicanciaLayout.createSequentialGroup().addContainerGap().addGroup(pneEquipeSindicanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, pneEquipeSindicanciaLayout.createSequentialGroup().addGroup(pneEquipeSindicanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(txtDtInclusaoSindicancia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(pneEquipeSindicanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cmbSindicanteResponsavel, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE).addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE))).addGroup(javax.swing.GroupLayout.Alignment.LEADING, pneEquipeSindicanciaLayout.createSequentialGroup().addGroup(pneEquipeSindicanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(txtDtSindicanciaAgendada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(pneEquipeSindicanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(txtHrSindicanca, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(labelhora, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING).addGroup(pneEquipeSindicanciaLayout.createSequentialGroup().addComponent(btnAdicionarSindicante, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnRemoverSindicante, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        pneEquipeSindicanciaLayout.setVerticalGroup(pneEquipeSindicanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pneEquipeSindicanciaLayout.createSequentialGroup().addContainerGap().addGroup(pneEquipeSindicanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(pneEquipeSindicanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cmbSindicanteResponsavel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtDtInclusaoSindicancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(pneEquipeSindicanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(labelhora)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(pneEquipeSindicanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtDtSindicanciaAgendada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtHrSindicanca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(pneEquipeSindicanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(btnRemoverSindicante, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(btnAdicionarSindicante, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        tbpneEquipe_Avaliacao.addTab("Equipe de Sindicancia", pneEquipeSindicancia);
        jLabel12.setText("Consenso Geral Para Admissão");
        chkSindicanteAprovam.setText("Sindicantes Aprovam Sindicância");
        chkSindicanteAprovam.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jLabel11.setText("Data Aprovação");
        jLabel15.setText("Contras Para  Admissão");
        jLabel16.setText("Prós  Para Admissão");
        txtProsAdmissao.setColumns(20);
        txtProsAdmissao.setRows(5);
        jScrollPane2.setViewportView(txtProsAdmissao);
        txtContraAdmissao.setColumns(20);
        txtContraAdmissao.setRows(5);
        jScrollPane3.setViewportView(txtContraAdmissao);
        txtConsensoGeral.setColumns(20);
        txtConsensoGeral.setRows(5);
        jScrollPane4.setViewportView(txtConsensoGeral);
        javax.swing.GroupLayout pneAvaliacaoSindicanciaLayout = new javax.swing.GroupLayout(pneAvaliacaoSindicancia);
        pneAvaliacaoSindicancia.setLayout(pneAvaliacaoSindicanciaLayout);
        pneAvaliacaoSindicanciaLayout.setHorizontalGroup(pneAvaliacaoSindicanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pneAvaliacaoSindicanciaLayout.createSequentialGroup().addContainerGap().addGroup(pneAvaliacaoSindicanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE).addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE).addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, pneAvaliacaoSindicanciaLayout.createSequentialGroup().addComponent(chkSindicanteAprovam).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE).addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtDtAprovacaoSindicancia, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)).addContainerGap()));
        pneAvaliacaoSindicanciaLayout.setVerticalGroup(pneAvaliacaoSindicanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pneAvaliacaoSindicanciaLayout.createSequentialGroup().addContainerGap().addGroup(pneAvaliacaoSindicanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(chkSindicanteAprovam).addComponent(jLabel11).addComponent(txtDtAprovacaoSindicancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel16).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel15).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel12).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        tbpneEquipe_Avaliacao.addTab("Avaliação Sindicância", pneAvaliacaoSindicancia);
        btnSalvarSindicancia.setText("btnSalvar1");
        btnSalvarSindicancia.setMessage("Salvar sindincância");
        btnSalvarSindicancia.setNextFocusableComponent(btnAprovarSindicancia);
        btnSalvarSindicancia.setStatusBar(statusBarSindicancia);
        btnSalvarSindicancia.setVerifyInputWhenFocusTarget(false);
        btnSalvarSindicancia.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarSindicanciaActionPerformed(evt);
            }
        });
        btnSair1.setText("btnSair1");
        btnSair1.setNextFocusableComponent(txtCodPedidoAdmissao);
        btnSair1.setStatusBar(statusBarSindicancia);
        btnSair1.setVerifyInputWhenFocusTarget(false);
        btnReprovarSindicancia.setText("btnReprovar1");
        btnReprovarSindicancia.setMessage("Reprovar sindincância");
        btnReprovarSindicancia.setNextFocusableComponent(btnAjuda1);
        btnReprovarSindicancia.setStatusBar(statusBarSindicancia);
        btnReprovarSindicancia.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReprovarSindicanciaActionPerformed(evt);
            }
        });
        btnAprovarSindicancia.setText("btnAprovar1");
        btnAprovarSindicancia.setMessage("Aprovar sindincância");
        btnAprovarSindicancia.setNextFocusableComponent(btnReprovarSindicancia);
        btnAprovarSindicancia.setStatusBar(statusBarSindicancia);
        btnAprovarSindicancia.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAprovarSindicanciaActionPerformed(evt);
            }
        });
        jPanel2.setLayout(new java.awt.GridLayout(2, 1));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações do Idoso"));
        jLabel1.setText("Num Pedido  ");
        txtCodPedidoAdmissao.setActionToPerform(btnConsultarPedidoAdmissao);
        txtCodPedidoAdmissao.setNextFocusableComponent(btnConsultarPedidoAdmissao);
        jLabel5.setText("Data Pedido");
        jLabel13.setText("Estado");
        btnConsultarPedidoAdmissao.setText("btnConsultar1");
        btnConsultarPedidoAdmissao.setMessage("Consultar Pedido de Admissão / Sindicância");
        btnConsultarPedidoAdmissao.setNextFocusableComponent(txtDtInclusaoSindicancia);
        btnConsultarPedidoAdmissao.setStatusBar(statusBarSindicancia);
        btnConsultarPedidoAdmissao.setVerifyInputWhenFocusTarget(false);
        btnConsultarPedidoAdmissao.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarPedidoAdmissaoActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE).addComponent(txtCodPedidoAdmissao, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnConsultarPedidoAdmissao, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel13).addComponent(txtEstadoPedidoAdmissao, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtDtPedidoAdmissao, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE).addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(btnConsultarPedidoAdmissao, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jLabel13).addComponent(jLabel5)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtEstadoPedidoAdmissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtDtPedidoAdmissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(txtCodPedidoAdmissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel2.add(jPanel3);
        jLabel4.setText("Nome Idoso");
        jLabel8.setText("Identidade");
        txtNrIdentidade.setText("  ");
        txtNrCPF.setText("  ");
        jLabel9.setText("CPF");
        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtNmIdoso, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtNrIdentidade, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addComponent(jLabel9).addGap(97, 97, 97)).addGroup(jPanel6Layout.createSequentialGroup().addComponent(txtNrCPF, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE).addContainerGap()))));
        jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(jLabel8)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtNmIdoso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtNrIdentidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(jPanel6Layout.createSequentialGroup().addComponent(jLabel9).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtNrCPF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel2.add(jPanel6);
        btnAjuda1.setText("btnAjuda1");
        btnAjuda1.setNextFocusableComponent(btnAlterarPedidoAdmissao);
        btnAjuda1.setStatusBar(statusBarSindicancia);
        btnAlterarPedidoAdmissao.setText("btnAlterar1");
        btnAlterarPedidoAdmissao.setEnabled(false);
        btnAlterarPedidoAdmissao.setMessage("Alterar Pedido de Admissão");
        btnAlterarPedidoAdmissao.setNextFocusableComponent(btnSair1);
        btnAlterarPedidoAdmissao.setStatusBar(statusBarSindicancia);
        btnAlterarPedidoAdmissao.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarPedidoAdmissaoActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(statusBarSindicancia, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(tbpneEquipe_Avaliacao, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE).addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)).addContainerGap()).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(btnSalvarSindicancia, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnAprovarSindicancia, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnReprovarSindicancia, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnAjuda1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnAlterarPedidoAdmissao, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 261, Short.MAX_VALUE).addComponent(btnSair1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(9, 9, 9).addComponent(tbpneEquipe_Avaliacao, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(btnSalvarSindicancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(btnAprovarSindicancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(btnReprovarSindicancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(btnSair1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(btnAjuda1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(btnAlterarPedidoAdmissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE).addComponent(statusBarSindicancia, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)));
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 543) / 2, (screenSize.height - 562) / 2, 543, 562);
    }

    private void btnAlterarPedidoAdmissaoActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            this.alterarPedidoAdmissao();
        } catch (ExcecaoAcessoNegado ex) {
            ex.mostrarExcecao(this);
        }
    }

    private void btnConsultarPedidoAdmissaoActionPerformed(java.awt.event.ActionEvent evt) {
        this.consultarPedidoAdmissaoESindicancia();
    }

    private void btnAprovarSindicanciaActionPerformed(java.awt.event.ActionEvent evt) {
        this.aprovarSindicancia();
    }

    private void btnReprovarSindicanciaActionPerformed(java.awt.event.ActionEvent evt) {
        this.reprovarSindicancia();
    }

    private void btnSalvarSindicanciaActionPerformed(java.awt.event.ActionEvent evt) {
        this.salvarSindicancia();
    }

    private void cmbSindicanteResponsavelActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void cmbSindicanteResponsavelPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
        carregarSindicanteResponsavel();
    }

    private void btnAdicionarSindicanteActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            List<Sindicante> sindicantes = SindicanteBusiness.getInstance().listarTodosSindicantesAtivos();
            if (sindicantesSelecionados == null) sindicantesSelecionados = new LinkedHashSet<Sindicante>();
            sindicantes.removeAll(sindicantesSelecionados);
            sindicantesSelecionados.addAll(SelecionarSindicantesView.selecionarSindicantes(this, sindicantes));
            this.MostrarSindicanteSelecionado(sindicantesSelecionados);
        } catch (ExcecaoSistema ex) {
            ex.mostrarExcecao(this);
        } catch (ExcecaoNegocio ex) {
            this.statusBarSindicancia.setMessage(ex);
        }
    }

    private void btnRemoverSindicanteActionPerformed(java.awt.event.ActionEvent evt) {
        List<Sindicante> sindicantesRemovidos = new ArrayList<Sindicante>();
        for (Object o : this.lstEquipeSindicante.getSelectedValues()) {
            sindicantesRemovidos.add((Sindicante) o);
        }
        this.sindicantesSelecionados.removeAll(sindicantesRemovidos);
        this.lstEquipeSindicante.setListData(this.sindicantesSelecionados.toArray());
    }

    protected com.celiosilva.swingDK.buttons.BtnIncluir btnAdicionarSindicante;

    protected com.celiosilva.swingDK.buttons.BtnAjuda btnAjuda1;

    protected com.celiosilva.swingDK.buttons.BtnAlterar btnAlterarPedidoAdmissao;

    protected com.celiosilva.swingDK.buttons.BtnAprovar btnAprovarSindicancia;

    protected com.celiosilva.swingDK.buttons.BtnConsultar btnConsultarPedidoAdmissao;

    protected com.celiosilva.swingDK.buttons.BtnCancelar btnRemoverSindicante;

    protected com.celiosilva.swingDK.buttons.BtnReprovar btnReprovarSindicancia;

    protected com.celiosilva.swingDK.buttons.BtnSair btnSair1;

    protected com.celiosilva.swingDK.buttons.BtnSalvar btnSalvarSindicancia;

    protected javax.swing.JCheckBox chkSindicanteAprovam;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbSindicanteResponsavel;

    protected javax.swing.JLabel jLabel1;

    protected javax.swing.JLabel jLabel11;

    protected javax.swing.JLabel jLabel12;

    protected javax.swing.JLabel jLabel13;

    protected javax.swing.JLabel jLabel14;

    protected javax.swing.JLabel jLabel15;

    protected javax.swing.JLabel jLabel16;

    protected javax.swing.JLabel jLabel2;

    protected javax.swing.JLabel jLabel3;

    protected javax.swing.JLabel jLabel4;

    protected javax.swing.JLabel jLabel5;

    protected javax.swing.JLabel jLabel6;

    protected javax.swing.JLabel jLabel8;

    protected javax.swing.JLabel jLabel9;

    protected javax.swing.JPanel jPanel2;

    protected javax.swing.JPanel jPanel3;

    protected javax.swing.JPanel jPanel6;

    protected javax.swing.JScrollPane jScrollPane1;

    protected javax.swing.JScrollPane jScrollPane2;

    protected javax.swing.JScrollPane jScrollPane3;

    protected javax.swing.JScrollPane jScrollPane4;

    protected javax.swing.JLabel labelhora;

    protected javax.swing.JList lstEquipeSindicante;

    protected javax.swing.JPanel pneAvaliacaoSindicancia;

    protected javax.swing.JPanel pneEquipeSindicancia;

    protected com.celiosilva.swingDK.dataFields.StatusBar statusBarSindicancia;

    protected javax.swing.JTabbedPane tbpneEquipe_Avaliacao;

    protected com.celiosilva.swingDK.dataFields.IntegerField txtCodPedidoAdmissao;

    protected javax.swing.JTextArea txtConsensoGeral;

    protected javax.swing.JTextArea txtContraAdmissao;

    protected com.celiosilva.swingDK.dataFields.DateField txtDtAprovacaoSindicancia;

    protected com.celiosilva.swingDK.dataFields.DateField txtDtInclusaoSindicancia;

    protected com.celiosilva.swingDK.dataFields.DateField txtDtPedidoAdmissao;

    protected com.celiosilva.swingDK.dataFields.DateField txtDtSindicanciaAgendada;

    protected com.celiosilva.swingDK.dataFields.LetterField txtEstadoPedidoAdmissao;

    protected com.celiosilva.swingDK.dataFields.MaskField txtHrSindicanca;

    protected com.celiosilva.swingDK.dataFields.LetterField txtNmIdoso;

    protected com.celiosilva.swingDK.dataFields.MaskField txtNrCPF;

    protected com.celiosilva.swingDK.dataFields.AlphaNumericField txtNrIdentidade;

    protected javax.swing.JTextArea txtProsAdmissao;

    protected javax.swing.JTextArea txtProsAdmissao1;

    public void carregarSindicanteResponsavel() {
        try {
            List<Sindicante> sindicantes = SindicanteBusiness.getInstance().listarTodosSindicantesAtivos();
            cmbSindicanteResponsavel.setValued(sindicantes);
        } catch (ExcecaoSistema ex) {
            ex.mostrarExcecao(this);
        } catch (ExcecaoNegocio ex) {
            this.statusBarSindicancia.setMessage(ex);
        }
    }

    public void mostarPedidoAdmissao(PedidoAdmissao pedido) {
        txtCodPedidoAdmissao.setValued(pedido.getNrPedidoAdmissao());
        txtEstadoPedidoAdmissao.setValued(pedido.getEstadoPedido().toString());
        txtDtPedidoAdmissao.setValued(pedido.getDtPedidoAdmissao());
        txtNmIdoso.setValued(pedido.getNmIdoso());
        txtNrIdentidade.setValued(pedido.getNrIdentidade());
        txtNrCPF.setValued(pedido.getNrCPF());
    }

    public void MostrarSindicanteSelecionado(Collection<Sindicante> sindicantes) {
        lstEquipeSindicante.removeAll();
        lstEquipeSindicante.setListData(sindicantes.toArray());
    }

    public void mostrarSindicancia(Sindicancia s) {
        mostarPedidoAdmissao(s.getPedidoAdmissao());
        this.setSindicantesSelecionados(s.getSindicantes());
        MostrarSindicanteSelecionado(s.getSindicantes());
        mostrarInformacoesSindicancia(s);
        mostrarInformacoesResultadoSindicancia(s);
    }

    private void mostrarInformacoesSindicancia(Sindicancia s) {
        txtDtSindicanciaAgendada.setValued(s.getDtAgendadaSindicancia());
        txtHrSindicanca.setValued(Funcoes.formatarHorario(s.getDtAgendadaSindicancia()));
        txtDtInclusaoSindicancia.setValued(s.getDtInclusaoSindicancia());
        cmbSindicanteResponsavel.setSelectedValue(s.getSindicanteResponsavel());
    }

    private void mostrarInformacoesResultadoSindicancia(Sindicancia s) {
        chkSindicanteAprovam.setSelected(s.getSindicantesAprovam() == null ? false : true);
        txtDtAprovacaoSindicancia.setValued(s.getDtAprovacao());
        txtConsensoGeral.setText(s.getDsConsensoGeral());
        txtContraAdmissao.setText(s.getDsContrasAdmissao());
        txtProsAdmissao.setText(s.getDsProsAdmissao());
        lstEquipeSindicante.setListData(s.getSindicantes().toArray());
    }

    public void limparFormulario() {
        txtCodPedidoAdmissao.clear();
        txtEstadoPedidoAdmissao.clear();
        txtDtPedidoAdmissao.clear();
        txtNmIdoso.clear();
        txtNrIdentidade.clear();
        txtNrCPF.clear();
        txtDtSindicanciaAgendada.clear();
        txtHrSindicanca.clear();
        txtDtInclusaoSindicancia.clear();
        cmbSindicanteResponsavel.clear();
        Object[] listData = new Object[0];
        lstEquipeSindicante.setListData(listData);
        chkSindicanteAprovam.setSelected(false);
        txtDtAprovacaoSindicancia.clear();
        txtProsAdmissao.setText("");
        txtContraAdmissao.setText("");
        txtConsensoGeral.setText("");
    }
}
