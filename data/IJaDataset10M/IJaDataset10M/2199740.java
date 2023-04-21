package interfaceGrafica;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import servicos.Servicos;

public class AutenticarGerente extends SuperVendas {

    private Servicos servicos;

    private JTextField txtUsuario;

    private JPasswordField txtSenha;

    private JLabel lblUsuario, lblSenha, lblGamb, lblGamb2;

    private JButton btnOk, btnCancelar;

    private JPanel centro;

    public AutenticarGerente(Servicos servicos) {
        super(servicos);
        this.setTitle("Gerente");
        this.servicos = servicos;
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(200, 150);
        JPanel jPanel1 = new JPanel();
        JPanel jPanel2 = new JPanel();
        JPanel jPanel3 = new JPanel();
        Container janela;
        janela = getContentPane();
        janela.setLayout(new BorderLayout());
        jPanel1.setLayout(new GridLayout(4, 1));
        jPanel2.setLayout(new GridLayout(4, 1));
        jPanel3.setLayout(new FlowLayout());
        lblGamb = new JLabel("");
        jPanel1.add(lblGamb);
        lblGamb2 = new JLabel("");
        jPanel2.add(lblGamb2);
        lblUsuario = new JLabel("Usuario");
        jPanel1.add(lblUsuario);
        lblSenha = new JLabel("Senha");
        jPanel1.add(lblSenha);
        txtUsuario = new JTextField(10);
        jPanel2.add(txtUsuario);
        txtSenha = new JPasswordField(20);
        jPanel2.add(txtSenha);
        btnOk = new JButton("OK");
        Autenticar autenticar = new Autenticar();
        btnOk.addActionListener(autenticar);
        jPanel3.add(btnOk);
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new Cancelar());
        jPanel3.add(btnCancelar);
        janela.add(jPanel1, BorderLayout.WEST);
        janela.add(jPanel2, BorderLayout.CENTER);
        janela.add(jPanel3, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    private class Autenticar implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            txtSenha.selectAll();
            autenticar(txtUsuario.getText(), txtSenha.getSelectedText());
        }
    }

    private class Cancelar implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }
}
