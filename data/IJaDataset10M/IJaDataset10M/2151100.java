package org.larOzanam.familiar.controller;

import com.celiosilva.swingDK.dataFields.MaskField;
import com.celiosilva.swingDK.dataTable.Table;
import com.celiosilva.swingDK.frames.InternalFrame;
import java.awt.Component;
import java.util.List;
import org.larOzanam.arquitetura.consulta.Consulta;
import org.larOzanam.arquitetura.consulta.OperadorCorrespondencia;
import org.larOzanam.arquitetura.consulta.OperadorLogico;
import org.larOzanam.arquitetura.consulta.OperadorRelacional;
import org.larOzanam.arquitetura.consulta.RetornoConsulta;
import org.larOzanam.arquitetura.excecoes.ExcecaoNegocio;
import org.larOzanam.arquitetura.excecoes.ExcecaoSistema;
import org.larOzanam.business.FamiliarBusiness;
import org.larOzanam.endereco.business.MunicipioBusiness;
import org.larOzanam.endereco.model.Municipio;
import org.larOzanam.model.Familiar;
import org.larOzanam.model.enums.Sexo;
import org.larOzanam.view.mascaras.MascarasLarOzanam;

/**
 *
 * @author  Administrador
 */
public class FiltrarFamiliarController extends InternalFrame {

    private Table tblFamiliares;

    /**
     * Creates new form FiltrarPedidoAdmController
     */
    public FiltrarFamiliarController(Component comp, Table tblFamiliares) {
        super(comp);
        this.tblFamiliares = tblFamiliares;
        initComponents();
        this.setTitle("FFA - Filtrar Familiar");
        try {
            List<Municipio> municipios = MunicipioBusiness.getInstance().consultarTodosMunicipios();
            this.cmbMunicipio.setValued(municipios);
        } catch (ExcecaoNegocio ex) {
            this.statusBar.setMessage(ex);
        } catch (ExcecaoSistema ex) {
            ex.mostrarExcecao(this);
        }
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtDtCadAte = new com.celiosilva.swingDK.dataFields.DateField();
        txtDtCadDe = new com.celiosilva.swingDK.dataFields.DateField();
        txtVrRendimentoDe = new com.celiosilva.swingDK.dataFields.CurrencyField();
        cmbDtNascimento = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        cmbEOUDtCad = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        cmbEOUDtNascimento = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        cmbDtCad = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        cmbVrRendimento = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        cmbNmFamiliar = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        cmbSexo = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtDtNascAte = new com.celiosilva.swingDK.dataFields.DateField();
        txtDtNascDe = new com.celiosilva.swingDK.dataFields.DateField();
        txtVrRendimentoAte = new com.celiosilva.swingDK.dataFields.CurrencyField();
        txtNmFamiliar = new com.celiosilva.swingDK.dataFields.LetterField();
        cmbEOUVrRendimento = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        cmbEOUNmFamiliar = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        cmbEOUSexo = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        jLabel16 = new javax.swing.JLabel();
        cmbNrTelefone = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        cmbEOUNrTelefone = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        cmbMunicipio = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        jLabel18 = new javax.swing.JLabel();
        cmbEOUMunicipio = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        cmbMaxRegRetornados = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        txtMaxRegRetornados = new com.celiosilva.swingDK.dataFields.IntegerField();
        jLabel20 = new javax.swing.JLabel();
        cmbEOUNrCelular = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        cmbNrCelular = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        cmbEOUDsProfissao = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        jLabel21 = new javax.swing.JLabel();
        cmbDsProfissao = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        txtDsProfissao = new com.celiosilva.swingDK.dataFields.LetterField();
        cmbEOUEmail = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        jLabel22 = new javax.swing.JLabel();
        cmbEmail = new com.celiosilva.swingDK.dataFields.ComboBoxField();
        jLabel1 = new javax.swing.JLabel();
        txtNrCelular = new MaskField(MascarasLarOzanam.getMascaraTelefone());
        txtNrTelefone = new MaskField(MascarasLarOzanam.getMascaraTelefone());
        txtEmail = new com.celiosilva.swingDK.dataFields.StringField();
        btnConsultar = new com.celiosilva.swingDK.buttons.BtnConsultar();
        btnSair = new com.celiosilva.swingDK.buttons.BtnSair();
        statusBar = new com.celiosilva.swingDK.dataFields.StatusBar();
        setExitButton(btnSair);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filtrar Por Informações do Pedido de Admissão"));
        jLabel2.setText("Data Cadastro");
        cmbDtNascimento.setFirstIsEmpty(true);
        cmbDtNascimento.setValued(OperadorRelacional.values());
        cmbEOUDtCad.setFirstIsEmpty(true);
        cmbEOUDtCad.setValued(OperadorLogico.values());
        cmbEOUDtCad.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEOUDtCadActionPerformed(evt);
            }
        });
        cmbEOUDtNascimento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "" }));
        cmbEOUDtNascimento.setFirstIsEmpty(true);
        cmbEOUDtNascimento.setValued(OperadorLogico.values());
        cmbDtCad.setFirstIsEmpty(true);
        cmbDtCad.setValued(OperadorRelacional.values());
        cmbVrRendimento.setFirstIsEmpty(true);
        cmbVrRendimento.setValued(OperadorRelacional.values());
        cmbNmFamiliar.setFirstIsEmpty(true);
        cmbNmFamiliar.setValued(OperadorCorrespondencia.values());
        cmbSexo.setFirstIsEmpty(true);
        cmbSexo.setValued(Sexo.values());
        jLabel4.setText("Vr Rendimento");
        jLabel5.setText("Dt Nascimento");
        jLabel7.setText("Nome Familiar");
        jLabel8.setText("Sexo");
        cmbEOUVrRendimento.setFirstIsEmpty(true);
        cmbEOUVrRendimento.setValued(OperadorLogico.values());
        cmbEOUNmFamiliar.setFirstIsEmpty(true);
        cmbEOUNmFamiliar.setValued(OperadorLogico.values());
        cmbEOUSexo.setFirstIsEmpty(true);
        cmbEOUSexo.setValued(OperadorLogico.values());
        jLabel16.setText("Nr Telefone");
        cmbNrTelefone.setFirstIsEmpty(true);
        cmbNrTelefone.setValued(OperadorCorrespondencia.values());
        cmbEOUNrTelefone.setFirstIsEmpty(true);
        cmbEOUNrTelefone.setValued(OperadorLogico.values());
        cmbMunicipio.setFirstIsEmpty(true);
        cmbMunicipio.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {

            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }

            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }

            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                cmbMunicipioPopupMenuWillBecomeVisible(evt);
            }
        });
        cmbMunicipio.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMunicipioActionPerformed(evt);
            }
        });
        jLabel18.setText("Municipio Fam.");
        cmbEOUMunicipio.setFirstIsEmpty(true);
        cmbEOUMunicipio.setValued(OperadorLogico.values());
        cmbMaxRegRetornados.setValued(RetornoConsulta.values());
        jLabel20.setText("Nr Celular");
        cmbEOUNrCelular.setFirstIsEmpty(true);
        cmbEOUNrCelular.setValued(OperadorLogico.values());
        cmbNrCelular.setFirstIsEmpty(true);
        cmbNrCelular.setValued(OperadorCorrespondencia.values());
        cmbEOUDsProfissao.setFirstIsEmpty(true);
        cmbEOUDsProfissao.setValued(OperadorLogico.values());
        jLabel21.setText("Ds Profissão");
        cmbDsProfissao.setFirstIsEmpty(true);
        cmbDsProfissao.setValued(OperadorCorrespondencia.values());
        txtDsProfissao.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDsProfissaoActionPerformed(evt);
            }
        });
        cmbEOUEmail.setFirstIsEmpty(true);
        cmbEOUEmail.setValued(OperadorLogico.values());
        jLabel22.setText("E-Mail");
        cmbEmail.setFirstIsEmpty(true);
        cmbEmail.setValued(OperadorCorrespondencia.values());
        jLabel1.setText("Registros");
        txtEmail.setUpperCase(false);
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(cmbEOUSexo, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE).addGap(64, 64, 64).addComponent(jLabel8)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(cmbEOUVrRendimento, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE).addComponent(cmbEOUDtNascimento, 0, 0, Short.MAX_VALUE).addComponent(cmbEOUDtCad, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)).addGap(18, 18, 18).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel2).addComponent(jLabel5).addComponent(jLabel4))).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup().addComponent(cmbEOUNmFamiliar, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE).addGap(22, 22, 22).addComponent(jLabel7))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(cmbNmFamiliar, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtNmFamiliar, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(cmbVrRendimento, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(cmbDtNascimento, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE).addComponent(cmbDtCad, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtDtCadDe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE).addComponent(txtDtNascDe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE).addComponent(txtVrRendimentoDe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(txtVrRendimentoAte, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(txtDtNascAte, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(txtDtCadAte, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(jPanel2Layout.createSequentialGroup().addComponent(cmbSexo, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE).addGap(230, 230, 230)))).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(cmbEOUNrTelefone, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE).addGap(8, 8, 8)).addGroup(jPanel2Layout.createSequentialGroup().addComponent(cmbEOUNrCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(24, 24, 24).addComponent(jLabel16).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cmbNrTelefone, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addGap(33, 33, 33).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel20).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cmbNrCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbMaxRegRetornados, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(2, 2, 2))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(txtNrTelefone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(txtNrCelular, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)).addGap(96, 96, 96)).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cmbEOUMunicipio, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbEOUDsProfissao, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel18).addComponent(jLabel21).addComponent(jLabel22))).addComponent(cmbEOUEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(4, 4, 4).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cmbDsProfissao, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(txtMaxRegRetornados, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE).addComponent(txtDsProfissao, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))))).addComponent(cmbMunicipio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)))).addContainerGap()));
        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { cmbDsProfissao, cmbDtCad, cmbDtNascimento, cmbEmail, cmbMaxRegRetornados, cmbNmFamiliar, cmbNrCelular, cmbNrTelefone, cmbSexo, cmbVrRendimento });
        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { cmbEOUDsProfissao, cmbEOUDtCad, cmbEOUDtNascimento, cmbEOUEmail, cmbEOUMunicipio, cmbEOUNmFamiliar, cmbEOUNrCelular, cmbEOUNrTelefone, cmbEOUSexo, cmbEOUVrRendimento });
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(cmbEOUDtCad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbDtCad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtDtCadAte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtDtCadDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cmbEOUDtNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbDtNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel5).addComponent(txtDtNascAte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtDtNascDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cmbVrRendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4).addComponent(cmbEOUVrRendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtVrRendimentoAte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtVrRendimentoDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cmbEOUNmFamiliar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtNmFamiliar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbNmFamiliar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel7)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cmbEOUSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel8)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cmbEOUNrTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbNrTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel16).addComponent(txtNrTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cmbEOUNrCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbNrCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel20).addComponent(txtNrCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cmbEOUMunicipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbMunicipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel18)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cmbEOUDsProfissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel21).addComponent(cmbDsProfissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtDsProfissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cmbEOUEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel22).addComponent(cmbEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtMaxRegRetornados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1).addComponent(cmbMaxRegRetornados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        btnConsultar.setText("btnConsultar1");
        btnConsultar.setStatusBar(statusBar);
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });
        btnSair.setText("btnSair1");
        btnSair.setStatusBar(statusBar);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(statusBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 473, Short.MAX_VALUE).addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE).addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)));
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        pack();
    }

    private void cmbEOUDtCadActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void txtDsProfissaoActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void cmbMunicipioPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
        this.initMunicipioFamiliar();
    }

    private void cmbMunicipioActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {
        this.tblFamiliares.cleanTable();
        Consulta consDtCad = new Consulta((OperadorLogico) cmbEOUDtCad.getSelectedValue());
        consDtCad.setAtributo("dtCadastro").setOperador((OperadorRelacional) cmbDtCad.getSelectedValue()).setValorProcurado(txtDtCadDe.getValued(), txtDtCadAte.getValued());
        Consulta consDtNasc = new Consulta((OperadorLogico) cmbEOUDtNascimento.getSelectedValue());
        consDtNasc.setAtributo("dtNascimentoFamiliar").setOperador((OperadorRelacional) cmbDtNascimento.getSelectedValue()).setValorProcurado(txtDtNascDe.getValued(), txtDtNascAte.getValued());
        Consulta consRendimento = new Consulta((OperadorLogico) cmbEOUVrRendimento.getSelectedValue());
        consRendimento.setAtributo("vrRendimento").setOperador((OperadorRelacional) cmbVrRendimento.getSelectedValue()).setValorProcurado(txtVrRendimentoDe.getValued(), txtVrRendimentoAte.getValued());
        Consulta consNmFamiliar = new Consulta((OperadorLogico) cmbEOUNmFamiliar.getSelectedValue());
        consNmFamiliar.setAtributo("nmFamiliar").setOperador((OperadorCorrespondencia) cmbNmFamiliar.getSelectedValue()).setValorProcurado(txtNmFamiliar.getValued());
        Consulta consSexo = new Consulta((OperadorLogico) cmbEOUSexo.getSelectedValue());
        consSexo.setAtributo("sexo").setOperador(OperadorRelacional.IGUAL).setValorProcurado(cmbSexo.getSelectedValue());
        Consulta consNrTelefone = new Consulta((OperadorLogico) cmbEOUNrTelefone.getSelectedValue());
        consNrTelefone.setAtributo("nrTelefoneFamiliar").setOperador((OperadorCorrespondencia) cmbNrTelefone.getSelectedValue()).setValorProcurado(txtNrTelefone.getValued());
        Consulta consNrCelular = new Consulta((OperadorLogico) cmbEOUNrCelular.getSelectedValue());
        consNrTelefone.setAtributo("nrCelularFamiliar").setOperador((OperadorCorrespondencia) cmbNrCelular.getSelectedValue()).setValorProcurado(txtNrCelular.getValued());
        Consulta consMunicipio = new Consulta((OperadorLogico) cmbEOUMunicipio.getSelectedValue());
        consMunicipio.setAtributo("endereco.bairro.municipio").setOperador(OperadorRelacional.IGUAL).setValorProcurado(cmbMunicipio.getSelectedValue());
        Consulta consEmail = new Consulta((OperadorLogico) cmbEOUEmail.getSelectedValue());
        consEmail.setAtributo("dsEmailFamiliar").setOperador((OperadorCorrespondencia) cmbEmail.getSelectedValue()).setValorProcurado(txtEmail.getValued());
        Consulta consDsProfissao = new Consulta((OperadorLogico) cmbEOUDsProfissao.getSelectedValue());
        consDsProfissao.setAtributo("dsProfissao").setOperador((OperadorCorrespondencia) cmbDsProfissao.getSelectedValue()).setValorProcurado(txtDsProfissao.getValued());
        RetornoConsulta maxRetorno = (RetornoConsulta) cmbMaxRegRetornados.getSelectedValue();
        maxRetorno.setMaxRegistros(txtMaxRegRetornados.getValued());
        List<Familiar> lista;
        try {
            lista = FamiliarBusiness.getInstance().consultarPorFiltro(maxRetorno, consDsProfissao, consDtCad, consDtNasc, consEmail, consMunicipio, consNmFamiliar, consNrCelular, consNrTelefone, consRendimento, consSexo);
            this.tblFamiliares.setData(lista);
        } catch (ExcecaoNegocio ex) {
            this.statusBar.setMessage(ex);
        } catch (ExcecaoSistema ex) {
            ex.mostrarExcecao(this);
        }
    }

    private void initMunicipioFamiliar() {
        try {
            cmbMunicipio.clear();
            List<Municipio> municipios = MunicipioBusiness.getInstance().consultarTodosMunicipios();
            cmbMunicipio.setValued(municipios);
        } catch (ExcecaoSistema ex) {
            ex.mostrarExcecao(this);
        } catch (ExcecaoNegocio ex) {
            this.statusBar.setMessage(ex);
        }
    }

    protected com.celiosilva.swingDK.buttons.BtnConsultar btnConsultar;

    protected com.celiosilva.swingDK.buttons.BtnSair btnSair;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbDsProfissao;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbDtCad;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbDtNascimento;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbEOUDsProfissao;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbEOUDtCad;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbEOUDtNascimento;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbEOUEmail;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbEOUMunicipio;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbEOUNmFamiliar;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbEOUNrCelular;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbEOUNrTelefone;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbEOUSexo;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbEOUVrRendimento;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbEmail;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbMaxRegRetornados;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbMunicipio;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbNmFamiliar;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbNrCelular;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbNrTelefone;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbSexo;

    protected com.celiosilva.swingDK.dataFields.ComboBoxField cmbVrRendimento;

    protected javax.swing.JLabel jLabel1;

    protected javax.swing.JLabel jLabel16;

    protected javax.swing.JLabel jLabel18;

    protected javax.swing.JLabel jLabel2;

    protected javax.swing.JLabel jLabel20;

    protected javax.swing.JLabel jLabel21;

    protected javax.swing.JLabel jLabel22;

    protected javax.swing.JLabel jLabel4;

    protected javax.swing.JLabel jLabel5;

    protected javax.swing.JLabel jLabel7;

    protected javax.swing.JLabel jLabel8;

    protected javax.swing.JPanel jPanel1;

    protected javax.swing.JPanel jPanel2;

    protected com.celiosilva.swingDK.dataFields.StatusBar statusBar;

    protected com.celiosilva.swingDK.dataFields.LetterField txtDsProfissao;

    protected com.celiosilva.swingDK.dataFields.DateField txtDtCadAte;

    protected com.celiosilva.swingDK.dataFields.DateField txtDtCadDe;

    protected com.celiosilva.swingDK.dataFields.DateField txtDtNascAte;

    protected com.celiosilva.swingDK.dataFields.DateField txtDtNascDe;

    protected com.celiosilva.swingDK.dataFields.StringField txtEmail;

    protected com.celiosilva.swingDK.dataFields.IntegerField txtMaxRegRetornados;

    protected com.celiosilva.swingDK.dataFields.LetterField txtNmFamiliar;

    protected com.celiosilva.swingDK.dataFields.MaskField txtNrCelular;

    protected com.celiosilva.swingDK.dataFields.MaskField txtNrTelefone;

    protected com.celiosilva.swingDK.dataFields.CurrencyField txtVrRendimentoAte;

    protected com.celiosilva.swingDK.dataFields.CurrencyField txtVrRendimentoDe;
}
