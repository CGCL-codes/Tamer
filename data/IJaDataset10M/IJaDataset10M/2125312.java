package gui;

import dados.ListaQuestoes;
import gui.modelos.KeyListenerJanela;
import java.io.File;
import java.io.IOException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import dados.PastaCorrecao;
import logica.Arquivos;

/**
 *
 * @author  UltraXP
 */
public class CopiaArquivo extends javax.swing.JDialog {

    File arquivo = null;

    /** Creates new form CopiaArquivo */
    public CopiaArquivo(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
        iniciarCombo();
        Janelas.alinharContainer(this);
        this.addKeyListener(new KeyListenerJanela());
    }

    private void iniciarCombo() {
        int nroQuestoes = ListaQuestoes.getArrayListQuestoes().size();
        String[] vetorQuestao = new String[nroQuestoes];
        for (int i = 0; i <= nroQuestoes - 1; i++) {
            vetorQuestao[i] = "Quest�o " + (i + 1);
        }
        DefaultComboBoxModel modelQuestao = new DefaultComboBoxModel(vetorQuestao);
        cmbQuestao.setModel(modelQuestao);
    }

    private void initComponents() {
        btnPesquisar = new javax.swing.JButton();
        txtArquivo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        cmbQuestao = new javax.swing.JComboBox();
        btnConfirmar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cópia de Arquivo");
        btnPesquisar.setText("...");
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });
        txtArquivo.setEditable(false);
        jLabel1.setText("Caminho do Arquivo");
        jLabel2.setText("Questão");
        jTextArea1.setBackground(new java.awt.Color(238, 238, 238));
        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("Tahoma", 0, 11));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("Insira abaixo o caminho do arquivo a ser copiado e a questão dos alunos para qual o arquivo será copiado. ");
        jTextArea1.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextArea1);
        cmbQuestao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        btnConfirmar.setText("Confirmar");
        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE).add(jLabel2).add(jLabel1).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(btnCancelar).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 175, Short.MAX_VALUE).add(btnConfirmar)).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(org.jdesktop.layout.GroupLayout.LEADING, cmbQuestao, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().add(txtArquivo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 278, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnPesquisar)))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(txtArquivo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(btnPesquisar)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cmbQuestao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 60, Short.MAX_VALUE).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(btnConfirmar).add(btnCancelar)).addContainerGap()));
        pack();
    }

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Arquivos.copiarArquivo(arquivo, cmbQuestao.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "Arquivo copiado com sucesso!", "Copiado!", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {
        UIManager.put("FileChooser.openDialogTitleText", "C�pia de Arquivo");
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int resultado = fc.showOpenDialog(this);
        Janelas.alinharContainer(fc);
        if (resultado == JFileChooser.CANCEL_OPTION) {
            arquivo = null;
        } else {
            arquivo = fc.getSelectedFile();
            txtArquivo.setText(arquivo.getAbsolutePath());
        }
    }

    private javax.swing.JButton btnCancelar;

    private javax.swing.JButton btnConfirmar;

    private javax.swing.JButton btnPesquisar;

    private javax.swing.JComboBox cmbQuestao;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea jTextArea1;

    private javax.swing.JTextField txtArquivo;
}
