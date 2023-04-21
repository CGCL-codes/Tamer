package emprestimo.visao;

import emprestimo.modelo.dao.JConta_UsuarioDao;
import emprestimo.modelo.dao.JFuncionarioDao;
import emprestimo.modelo.modelo.JConta_Usuario;
import emprestimo.modelo.modelo.JFuncionario;
import emprestimo.modelo.modelo.JNivel_Acesso;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * @author Carlos Alexandre
 */
public class JCadastraUsuario extends javax.swing.JDialog {

    private JConta_Usuario usuario;

    private JConta_UsuarioDao usuarioDao;

    private JNivel_Acesso nivel;

    private JFuncionario funcionario;

    private JFuncionarioDao funcionarioDao;

    private List<JFuncionario> listaFuncionarios = new ArrayList<JFuncionario>();

    /** Creates new form JCadastraUsuario */
    public JCadastraUsuario(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        usuarioDao = new JConta_UsuarioDao();
    }

    public void limpar() {
        txtLogin.setText("");
        passSenha.setText("");
        passRepitaSenha.setText("");
        lblBuscaFoto.setIcon(null);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        panCadUsuario = new javax.swing.JPanel();
        lblLogin = new javax.swing.JLabel();
        txtLogin = new javax.swing.JTextField();
        lblSenha = new javax.swing.JLabel();
        passSenha = new javax.swing.JPasswordField();
        lblRepitaSenha = new javax.swing.JLabel();
        passRepitaSenha = new javax.swing.JPasswordField();
        lblNivelAcesso = new javax.swing.JLabel();
        cbxNivelAcesso = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        lblBuscaFoto = new javax.swing.JLabel();
        btnBuscaFoto = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnBuscar = new javax.swing.JButton();
        panFoto = new javax.swing.JPanel();
        lblFoto = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cbxFuncionarios = new javax.swing.JComboBox();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        jPanel1.setBackground(new java.awt.Color(255, 153, 0));
        jPanel1.setForeground(new java.awt.Color(240, 240, 240));
        jPanel1.setPreferredSize(new java.awt.Dimension(460, 60));
        lblTitulo.setFont(new java.awt.Font("Algerian", 0, 24));
        lblTitulo.setText("Cadastro de Usuário");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(lblTitulo).addContainerGap(188, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(lblTitulo).addContainerGap(17, Short.MAX_VALUE)));
        panCadUsuario.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastra Novo Usuário", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 255)));
        lblLogin.setText("Login:");
        lblSenha.setText("Senha:");
        lblRepitaSenha.setText("Repetir Senha:");
        lblNivelAcesso.setText("Nível de Acesso:");
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lblBuscaFoto.setText("Foto");
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lblBuscaFoto, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lblBuscaFoto, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE));
        btnBuscaFoto.setText("Buscar");
        btnBuscaFoto.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscaFotoActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout panCadUsuarioLayout = new javax.swing.GroupLayout(panCadUsuario);
        panCadUsuario.setLayout(panCadUsuarioLayout);
        panCadUsuarioLayout.setHorizontalGroup(panCadUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panCadUsuarioLayout.createSequentialGroup().addContainerGap().addGroup(panCadUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panCadUsuarioLayout.createSequentialGroup().addComponent(lblNivelAcesso).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(cbxNivelAcesso, 0, 114, Short.MAX_VALUE).addGap(56, 56, 56)).addComponent(lblRepitaSenha).addGroup(panCadUsuarioLayout.createSequentialGroup().addGroup(panCadUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lblLogin).addComponent(lblSenha)).addGap(58, 58, 58).addGroup(panCadUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(panCadUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(passRepitaSenha, javax.swing.GroupLayout.Alignment.LEADING).addComponent(passSenha, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))))).addGap(33, 33, 33).addGroup(panCadUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(panCadUsuarioLayout.createSequentialGroup().addGap(10, 10, 10).addComponent(btnBuscaFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(22, 22, 22)));
        panCadUsuarioLayout.setVerticalGroup(panCadUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panCadUsuarioLayout.createSequentialGroup().addContainerGap().addGroup(panCadUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panCadUsuarioLayout.createSequentialGroup().addGroup(panCadUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblLogin).addComponent(txtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(panCadUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblSenha).addComponent(passSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(panCadUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblRepitaSenha).addComponent(passRepitaSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(20, 20, 20).addGroup(panCadUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lblNivelAcesso).addComponent(cbxNivelAcesso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(panCadUsuarioLayout.createSequentialGroup().addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnBuscaFoto))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Adicionar Foto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 255)));
        jPanel3.setLayout(null);
        btnBuscar.setText("Buscar");
        jPanel3.add(btnBuscar);
        btnBuscar.setBounds(40, 70, 74, 23);
        panFoto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblFoto.setText("Foto");
        javax.swing.GroupLayout panFotoLayout = new javax.swing.GroupLayout(panFoto);
        panFoto.setLayout(panFotoLayout);
        panFotoLayout.setHorizontalGroup(panFotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panFotoLayout.createSequentialGroup().addGap(37, 37, 37).addComponent(lblFoto).addContainerGap(41, Short.MAX_VALUE)));
        panFotoLayout.setVerticalGroup(panFotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panFotoLayout.createSequentialGroup().addContainerGap(55, Short.MAX_VALUE).addComponent(lblFoto).addGap(52, 52, 52)));
        jPanel3.add(panFoto);
        panFoto.setBounds(180, 20, 102, 123);
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setText("Funcionário:");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addGap(18, 18, 18).addComponent(cbxFuncionarios, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(161, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 271, Short.MAX_VALUE).addComponent(btnSalvar).addGap(18, 18, 18).addComponent(btnCancelar).addContainerGap()).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(panCadUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { btnCancelar, btnSalvar });
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(21, 21, 21).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(cbxFuncionarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(panCadUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(6, 6, 6).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(22, 22, 22).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 11, Short.MAX_VALUE)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnCancelar).addComponent(btnSalvar))).addContainerGap()));
        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width - dialogSize.width) / 2, (screenSize.height - dialogSize.height) / 2);
    }

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {
        String usuario_novo = txtLogin.getText().trim();
        String senha = new String(passSenha.getPassword()).trim();
        String rep_senha = new String(passRepitaSenha.getPassword()).trim();
        if (!usuario_novo.isEmpty()) {
            if (senha.equals(rep_senha)) {
                usuario = new JConta_Usuario();
                nivel = new JNivel_Acesso();
                funcionario = new JFuncionario();
                usuario.setUsu_login(usuario_novo);
                usuario.setUsu_senha(senha);
                usuario.setNivel(nivel);
                usuario.setFuncionario(funcionario);
                try {
                    usuarioDao.insert(usuario);
                } catch (Exception ex) {
                    Logger.getLogger(JCadastraUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "As Senhas são Incompatíveis!", "Atenção!", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Favor, digitar um Login para Usuário!", "Atenção!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
    }

    private void btnBuscaFotoActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        funcionarioDao = new JFuncionarioDao();
        try {
            listaFuncionarios = funcionarioDao.retornaListaTodosFuncionarios(null);
        } catch (Exception ex) {
            Logger.getLogger(JCadastraUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (JFuncionario umFuncionario : listaFuncionarios) {
            getCbxFuncionarios().addItem(umFuncionario.getFunc_nome());
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                JCadastraUsuario dialog = new JCadastraUsuario(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnBuscaFoto;

    private javax.swing.JButton btnBuscar;

    private javax.swing.JButton btnCancelar;

    private javax.swing.JButton btnSalvar;

    private javax.swing.JComboBox cbxFuncionarios;

    private javax.swing.JComboBox cbxNivelAcesso;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JLabel lblBuscaFoto;

    private javax.swing.JLabel lblFoto;

    private javax.swing.JLabel lblLogin;

    private javax.swing.JLabel lblNivelAcesso;

    private javax.swing.JLabel lblRepitaSenha;

    private javax.swing.JLabel lblSenha;

    private javax.swing.JLabel lblTitulo;

    private javax.swing.JPanel panCadUsuario;

    private javax.swing.JPanel panFoto;

    private javax.swing.JPasswordField passRepitaSenha;

    private javax.swing.JPasswordField passSenha;

    private javax.swing.JTextField txtLogin;

    public javax.swing.JComboBox getCbxFuncionarios() {
        return cbxFuncionarios;
    }
}
