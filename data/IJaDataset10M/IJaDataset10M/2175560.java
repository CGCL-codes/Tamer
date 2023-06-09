package carrancao.gui;

import carrancao.controlador.Fachada;
import carrancao.entidades.Categoria;
import carrancao.exception.CadastrarCategoriaException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Lubnnia
 */
public class CategoriaGUI extends javax.swing.JFrame {

    /** Creates new form CadastrarCategoriaGUI */
    public CategoriaGUI() {
        initComponents();
        setLocationRelativeTo(null);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabelNomeCategoria = new javax.swing.JLabel();
        jTextFieldCampoCategoria = new javax.swing.JTextField();
        jButtonSalvar = new javax.swing.JButton();
        jButtonFechar = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de Categoria");
        setResizable(false);
        jLabelNomeCategoria.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabelNomeCategoria.setText("Categoria:");
        jTextFieldCampoCategoria.setFont(new java.awt.Font("Tahoma", 0, 12));
        jTextFieldCampoCategoria.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCampoCategoriaActionPerformed(evt);
            }
        });
        jButtonSalvar.setFont(new java.awt.Font("Tahoma", 0, 12));
        jButtonSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrancao/imagens/icon_ok.png")));
        jButtonSalvar.setText("Salvar");
        jButtonSalvar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarActionPerformed(evt);
            }
        });
        jButtonFechar.setFont(new java.awt.Font("Tahoma", 0, 12));
        jButtonFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/carrancao/imagens/icon_close.png")));
        jButtonFechar.setText("Fechar");
        jButtonFechar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFecharActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(51, 51, 51).addComponent(jLabelNomeCategoria).addGap(10, 10, 10).addComponent(jTextFieldCampoCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(89, Short.MAX_VALUE).addComponent(jButtonSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(33, 33, 33).addComponent(jButtonFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(80, 80, 80)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(33, 33, 33).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(3, 3, 3).addComponent(jLabelNomeCategoria)).addComponent(jTextFieldCampoCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(35, 35, 35).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButtonSalvar).addComponent(jButtonFechar)).addGap(36, 36, 36)));
        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { jButtonSalvar, jTextFieldCampoCategoria });
        pack();
    }

    private void limparCampos() {
        jTextFieldCampoCategoria.setText("");
    }

    private void jButtonFecharActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void jButtonSalvarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (validarCampos()) {
                String categoriaAux = jTextFieldCampoCategoria.getText().toUpperCase();
                String statusAux = "ATIVO";
                Categoria categoria = new Categoria(categoriaAux, statusAux);
                Fachada.getInstance().getCategoriaControl().cadastrarCategoria(categoria);
                limparCampos();
            }
        } catch (CadastrarCategoriaException ex) {
            Logger.getLogger(CategoriaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void jTextFieldCampoCategoriaActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (validarCampos()) {
                String categoriaAux = jTextFieldCampoCategoria.getText().toUpperCase();
                String statusAux = "ATIVO";
                Categoria categoria = new Categoria(categoriaAux, statusAux);
                Fachada.getInstance().getCategoriaControl().cadastrarCategoria(categoria);
                limparCampos();
            }
        } catch (CadastrarCategoriaException ex) {
            Logger.getLogger(CategoriaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new CategoriaGUI().setVisible(true);
            }
        });
    }

    private javax.swing.JButton jButtonFechar;

    private javax.swing.JButton jButtonSalvar;

    private javax.swing.JLabel jLabelNomeCategoria;

    private javax.swing.JTextField jTextFieldCampoCategoria;

    private boolean validarCampos() {
        boolean resultado = true;
        if (jTextFieldCampoCategoria.getText().equals("") || jTextFieldCampoCategoria.getText().length() < 3) {
            JOptionPane.showMessageDialog(null, "Preencha o campo CATEGORIA corretamente.", "Carrancão Hamburgueria", JOptionPane.WARNING_MESSAGE);
            jTextFieldCampoCategoria.requestFocus();
            resultado = false;
        }
        return resultado;
    }
}
