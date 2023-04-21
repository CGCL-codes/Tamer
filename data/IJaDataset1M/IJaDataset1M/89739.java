package apresentacao;

import entidades.Fornecedor;
import java.awt.Frame;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import negocio.Central;
import negocio.Util;

public class TelaCadastroFornecedor extends JDialog {

    Central central;

    boolean modifica;

    Fornecedor fornecedorModificar;

    public TelaCadastroFornecedor(Frame pai, Central central, Fornecedor fornecedor) {
        super(pai, true);
        this.central = central;
        fornecedorModificar = fornecedor;
        this.modifica = (fornecedor != null);
        this.central = central;
        initComponents();
        if (modifica) preencheComponentes(fornecedor);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jTextField_nome = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField_cnpj = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField_telefone = new javax.swing.JTextField();
        jButton_cadastrar = new javax.swing.JButton();
        jButton_modificar = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel1.setText("Fornecedor");
        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel2.setText("Nome:");
        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel3.setText("CNPJ:");
        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel4.setText("Telefone:");
        jButton_cadastrar.setText("Cadastrar");
        jButton_cadastrar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_cadastrarActionPerformed(evt);
            }
        });
        jButton_modificar.setText("Modifica");
        jButton_modificar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_modificarActionPerformed(evt);
            }
        });
        jButton_modificar.setVisible(false);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jTextField_nome, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jTextField_cnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jTextField_telefone, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jButton_modificar, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 178, Short.MAX_VALUE).addComponent(jButton_cadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jTextField_nome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jTextField_cnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4).addComponent(jTextField_telefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton_cadastrar, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE).addComponent(jButton_modificar, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)).addContainerGap()));
        pack();
    }

    private void jButton_cadastrarActionPerformed(java.awt.event.ActionEvent evt) {
        Fornecedor f = new Fornecedor();
        f.setNome(jTextField_nome.getText());
        f.setCNPJ(jTextField_cnpj.getText());
        f.setTelefone(jTextField_telefone.getText());
        if (jTextField_nome.getText().equals("")) JOptionPane.showMessageDialog(null, "Por Favor, insira um nome!", "", JOptionPane.INFORMATION_MESSAGE); else if (jTextField_cnpj.getText().equals("")) JOptionPane.showMessageDialog(null, "Por Favor, insira um CPF!", "", JOptionPane.INFORMATION_MESSAGE); else if (jTextField_telefone.getText().equals("")) JOptionPane.showMessageDialog(null, "Por Favor, insira um numero de telefone!", "", JOptionPane.INFORMATION_MESSAGE); else {
            try {
                central.insereFornecedor(f);
                JOptionPane.showMessageDialog(null, "Fornecedor cadastrado com sucesso!", "", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (IOException ex) {
                Logger.getLogger(TelaCadastroCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void jButton_modificarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Fornecedor f = fornecedorModificar;
            f.setNome(jTextField_nome.getText());
            f.setCNPJ(jTextField_cnpj.getText());
            f.setTelefone(jTextField_telefone.getText());
            if (jTextField_nome.getText().equals("")) throw new IOException("Por Favor, insira um nome!"); else if (jTextField_cnpj.getText().equals("")) throw new IOException("Por Favor, insira um CNPJ!"); else if (jTextField_telefone.getText().equals("")) throw new IOException("Por Favor, insira um numero de telefone!"); else {
                central.modificaFornecedor();
                Util.info("Fornecedor modificado com sucesso!");
                dispose();
            }
        } catch (IOException ex) {
            Util.erro(ex.getMessage());
        }
    }

    private void preencheComponentes(Fornecedor fornecedor) {
        jTextField_nome.setText(fornecedor.getNome());
        jTextField_cnpj.setText(fornecedor.getCNPJ());
        jTextField_telefone.setText(fornecedor.getTelefone() + "");
        jLabel1.setText("Atualizaçao de dados");
        jButton_cadastrar.setVisible(false);
        jButton_modificar.setVisible(true);
    }

    private javax.swing.JButton jButton_cadastrar;

    private javax.swing.JButton jButton_modificar;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JTextField jTextField_cnpj;

    private javax.swing.JTextField jTextField_nome;

    private javax.swing.JTextField jTextField_telefone;
}
