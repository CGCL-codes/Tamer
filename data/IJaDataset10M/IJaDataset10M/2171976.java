package componente.usuario.view;

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.swing.*;
import model.TblUsuario;
import model.TblUsuarioHome;
import org.jgenesis.swing.JGNumberTextField;
import org.jgenesis.swing.JGTextField;
import pattern.data_access_object.DAOFactory;
import pattern.util.HCalendar;
import componente.usuario.control.AlterarComboUsuarioCommand;
import componente.usuario.control.DesativarAtivarCommand;
import componente.usuario.control.EditarUsuarioCommand;
import componente.usuario.control.IncluirUsuarioCommand;
import componente.usuario.control.MudancaComboTipoUsuario;
import componente.usuario.control.OkIncluirUsuarioCommand;
import componente.usuario.control.UsuarioControl;
import componente.usuario.model.UsuarioDAO;
import componente.usuario.model.UsuarioEntity;

public class PanelUsuario extends JPanel {

    public JComboBox usuarios, tipo;

    public JCheckBox admSist;

    public JPanel p1, p2, buttons1, buttons2;

    public JTextField nome, login, tel, email, data, end, crm, consultorio, cidade, estado;

    public JPasswordField senha;

    public JGNumberTextField tempoCons;

    public JButton incluir, desativar, mudarSenha, ok, cancela;

    public UsuarioControl controle;

    private static int id_usuario = 0;

    public PanelUsuario(UsuarioControl control) {
        this.setLayout(new GridLayout(2, 1));
        controle = control;
        mntPanels();
        this.add(p1);
        this.add(buttons1);
    }

    public void mntP1() {
        p1 = new JPanel();
        p1.setLayout(null);
        p1.add(new JLabel("Nome do Usu�rio")).setBounds(150, 20, 100, 25);
        TblUsuarioHome usuarioHome = new TblUsuarioHome();
        usuarios = new JComboBox(usuarioHome.listUsuario().toArray());
        p1.add(usuarios).setBounds(150, 50, 300, 25);
        TblUsuario entity = usuarioHome.findByNome(usuarios.getSelectedItem() + "");
        usuarios.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                new AlterarComboUsuarioCommand();
            }
        });
        desativar = new JButton("DESATIVAR");
        desativar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new DesativarAtivarCommand();
            }
        });
        if (entity.isBlAdm()) {
            this.desativar.setText("DESATIVAR");
        } else {
            this.desativar.setText("ATIVAR");
        }
    }

    public void mntP2() {
        p2 = new JPanel();
        p2.setLayout(new GridLayout(9, 4, 10, 5));
        p2.add(new JLabel("Nome", JLabel.RIGHT));
        nome = new JTextField();
        p2.add(nome);
        p2.add(new JLabel("Data", JLabel.RIGHT));
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) + 6);
        data = new JTextField(HCalendar.getStringddmmyyy(c));
        data.setEnabled(false);
        p2.add(data);
        p2.add(new JLabel("Login", JLabel.RIGHT));
        login = new JGTextField("***************");
        p2.add(login);
        p2.add(new JLabel("Senha", JLabel.RIGHT));
        senha = new JPasswordField("");
        p2.add(senha);
        p2.add(new JLabel("Tel", JLabel.RIGHT));
        tel = new JGTextField("**************");
        p2.add(tel);
        p2.add(new JLabel("e-mail", JLabel.RIGHT));
        email = new JTextField();
        p2.add(email);
        p2.add(new JLabel("Tipo", JLabel.RIGHT));
        tipo = new JComboBox();
        tipo.addItem("SECRETARIA");
        tipo.addItem("M�DICO");
        tipo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new MudancaComboTipoUsuario();
            }
        });
        p2.add(tipo);
        p2.add(new JLabel(""));
        p2.add(new JLabel(""));
        p2.add(new JLabel("CRM", JLabel.RIGHT));
        crm = new JGTextField("********************");
        p2.add(crm);
        p2.add(new JLabel("Consult�rio", JLabel.RIGHT));
        consultorio = new JTextField();
        p2.add(consultorio);
        p2.add(new JLabel("Tempo da Consulta", JLabel.RIGHT));
        tempoCons = new JGNumberTextField();
        p2.add(tempoCons);
        p2.add(new JLabel("Endere�o", JLabel.RIGHT));
        end = new JGTextField("***************************************");
        p2.add(end);
        p2.add(new JLabel("Cidade", JLabel.RIGHT));
        cidade = new JGTextField("***************");
        p2.add(cidade);
        p2.add(new JLabel("Estado", JLabel.RIGHT));
        estado = new JGTextField("**");
        p2.add(estado);
        p2.add(new JLabel("Administrador do sistema", JLabel.RIGHT));
        admSist = new JCheckBox("");
        p2.add(admSist);
        crm.setEnabled(false);
        consultorio.setEnabled(false);
        tempoCons.setEnabled(false);
        end.setEnabled(false);
        cidade.setEnabled(false);
        estado.setEnabled(false);
    }

    public void mntPButtons1() {
        buttons1 = new JPanel();
        buttons1.setLayout(new GridBagLayout());
        incluir = new JButton("INCLUIR");
        incluir.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new IncluirUsuarioCommand();
            }
        });
        buttons1.add(incluir);
        buttons1.add(desativar);
        mudarSenha = new JButton("ALTERAR");
        mudarSenha.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                new EditarUsuarioCommand();
            }
        });
        buttons1.add(mudarSenha);
    }

    public void mntPButtons2() {
        buttons2 = new JPanel();
        buttons2.setLayout(new GridBagLayout());
        ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new OkIncluirUsuarioCommand();
            }
        });
        buttons2.add(ok);
        cancela = new JButton("CANCELAR");
        cancela.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    remove(p2);
                    remove(buttons2);
                    validate();
                    repaint();
                    add(p1);
                    add(buttons1);
                } catch (Exception e4) {
                }
                validate();
                repaint();
            }
        });
        buttons2.add(cancela);
    }

    public void mntPanels() {
        mntP1();
        p1.validate();
        p1.repaint();
        mntP2();
        p2.validate();
        p2.repaint();
        mntPButtons1();
        buttons1.validate();
        buttons1.repaint();
        mntPButtons2();
        buttons2.validate();
        buttons2.repaint();
    }

    /**
	 * Metodo que envia uma mesagem para a classe controladora
	 * @param string
	 */
    protected void enviaMensagemAcontrole(String string) {
        controle.trataMensagem(string);
    }

    public static int getId_usuario() {
        return id_usuario;
    }

    public static void setId_usuario(int id_usuario1) {
        id_usuario = id_usuario1;
    }
}
