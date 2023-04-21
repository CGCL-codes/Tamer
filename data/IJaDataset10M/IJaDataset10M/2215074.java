package br.edu.fcsl.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JButton;

public class TelaContato extends JFrame {

    private JPanel contentPane;

    private JTextField txtCodigo;

    private JTextField txtNome;

    private JTextField txtTelefone;

    private JTextField txtEmail;

    private JTextField txtEndereco;

    /**
	 * Launch the application.
	 */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    TelaContato frame = new TelaContato();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
	 * Create the frame.
	 */
    public TelaContato() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 501, 250);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        JLabel lblNome = new JLabel("Nome:");
        JLabel lblCdigo = new JLabel("Código:");
        JLabel lblTelefone = new JLabel("Telefone:");
        JLabel lblEmail = new JLabel("E-mail:");
        JLabel lblEndereo = new JLabel("Endereço:");
        txtCodigo = new JTextField();
        txtCodigo.setColumns(10);
        txtNome = new JTextField();
        txtNome.setColumns(10);
        txtTelefone = new JTextField();
        txtTelefone.setColumns(10);
        txtEmail = new JTextField();
        txtEmail.setColumns(10);
        txtEndereco = new JTextField();
        txtEndereco.setColumns(10);
        JPanel panel = new JPanel();
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup().addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE).addGroup(gl_contentPane.createSequentialGroup().addContainerGap().addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane.createSequentialGroup().addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(lblCdigo).addComponent(lblNome)).addGap(18).addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(txtNome, GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE).addComponent(txtCodigo, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))).addGroup(gl_contentPane.createSequentialGroup().addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(lblTelefone).addComponent(lblEmail)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, 393, GroupLayout.PREFERRED_SIZE).addComponent(txtTelefone, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE))).addGroup(gl_contentPane.createSequentialGroup().addComponent(lblEndereo).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(txtEndereco, GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE))))).addGap(98)));
        gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane.createSequentialGroup().addGap(20).addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblCdigo).addComponent(txtCodigo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblNome).addComponent(txtNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblTelefone).addComponent(txtTelefone, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblEmail).addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblEndereo).addComponent(txtEndereco, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(18).addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap(25, Short.MAX_VALUE)));
        JButton btnSalvar = new JButton("Salvar");
        panel.add(btnSalvar);
        contentPane.setLayout(gl_contentPane);
    }
}
