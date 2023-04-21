package br.ita.trucocearense.cliente.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import br.ita.trucocearense.cliente.core.Jogo;
import br.ita.trucocearense.cliente.core.Mesa;
import br.ita.trucocearense.cliente.core.Usuario;
import br.ita.trucocearense.cliente.core.interfaces.padraobserver.Observer;

public class TelaSalaDeJogo implements Observer {

    private TelaMesaDeJogo telaMesaDeJogo;

    private JFrame frameTelaSalaDeJogo;

    private Usuario usuario;

    private Mesa mesa;

    private JButton buttonENTRARPOSICAO1a;

    private JButton buttonENTRARPOSICAO1b;

    private JButton buttonENTRARPOSICAO2a;

    private JButton buttonENTRARPOSICAO2b;

    private JButton buttonSAIRDAMESA;

    private JButton buttonLOGOUT;

    private JLabel labelTITULO;

    private JLabel labelBEMVINDO;

    private JLabel labelLOGIN;

    private JLabel labelSCORE;

    private JLabel labelMESA;

    private JLabel labelEQUIPE1;

    private JLabel labelEQUIPE2;

    private JLabel labelAPELIDO;

    private JLabel labelLOGINUsuario;

    private JLabel labelSCOREUsuario;

    private String[] nome = new String[4];

    public TelaSalaDeJogo(Usuario usuario) {
        this.usuario = usuario;
        this.mesa = new Mesa(usuario);
        this.mesa.registerObserver(this);
        Font fonte1 = new Font("arial", Font.BOLD, 20);
        Font fonte2 = new Font("arial", Font.BOLD, 16);
        Font fonte3 = new Font("arial", Font.BOLD, 12);
        frameTelaSalaDeJogo = new JFrame("Sala de Jogo");
        frameTelaSalaDeJogo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameTelaSalaDeJogo.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(WindowEvent winEvt) {
                sairDoJogo();
            }
        });
        PainelTelaSalaDeJogo painelTelaSalaDeJogo = new PainelTelaSalaDeJogo();
        painelTelaSalaDeJogo.setLocation(0, 0);
        painelTelaSalaDeJogo.setSize(600, 400);
        frameTelaSalaDeJogo.setLayout(null);
        painelTelaSalaDeJogo.setLayout(null);
        buttonENTRARPOSICAO1a = new JButton("Entrar");
        buttonENTRARPOSICAO1b = new JButton("Entrar");
        buttonENTRARPOSICAO2a = new JButton("Entrar");
        buttonENTRARPOSICAO2b = new JButton("Entrar");
        buttonSAIRDAMESA = new JButton("SAIR DA MESA");
        buttonLOGOUT = new JButton("LOGOUT");
        labelTITULO = new JLabel("SALA DE JOGO");
        labelBEMVINDO = new JLabel("BEM VINDO");
        labelLOGIN = new JLabel("LOGIN:");
        labelSCORE = new JLabel("SCORE:");
        labelMESA = new JLabel("MESA");
        labelEQUIPE1 = new JLabel("EQUIPE 1");
        labelEQUIPE2 = new JLabel("EQUIPE 2");
        labelAPELIDO = new JLabel(this.usuario.getApelido());
        labelLOGINUsuario = new JLabel(this.usuario.getLogin());
        labelSCOREUsuario = new JLabel(Integer.toString(this.usuario.getScore()));
        painelTelaSalaDeJogo.add(this.labelTITULO);
        painelTelaSalaDeJogo.add(this.labelBEMVINDO);
        painelTelaSalaDeJogo.add(this.labelLOGIN);
        painelTelaSalaDeJogo.add(this.labelSCORE);
        painelTelaSalaDeJogo.add(this.labelMESA);
        painelTelaSalaDeJogo.add(this.labelEQUIPE1);
        painelTelaSalaDeJogo.add(this.labelEQUIPE2);
        painelTelaSalaDeJogo.add(this.labelAPELIDO);
        painelTelaSalaDeJogo.add(this.labelLOGINUsuario);
        painelTelaSalaDeJogo.add(this.labelSCOREUsuario);
        painelTelaSalaDeJogo.add(this.buttonSAIRDAMESA);
        painelTelaSalaDeJogo.add(this.buttonLOGOUT);
        painelTelaSalaDeJogo.add(this.buttonENTRARPOSICAO1a);
        painelTelaSalaDeJogo.add(this.buttonENTRARPOSICAO1b);
        painelTelaSalaDeJogo.add(this.buttonENTRARPOSICAO2a);
        painelTelaSalaDeJogo.add(this.buttonENTRARPOSICAO2b);
        frameTelaSalaDeJogo.getContentPane().add(painelTelaSalaDeJogo);
        this.labelTITULO.setFont(fonte1);
        this.labelTITULO.setSize(150, 16);
        this.labelTITULO.setLocation(220, 13);
        this.labelTITULO.setVisible(true);
        this.labelBEMVINDO.setFont(fonte2);
        this.labelBEMVINDO.setSize(92, 15);
        this.labelBEMVINDO.setLocation(38, 45);
        this.labelBEMVINDO.setVisible(true);
        this.labelLOGIN.setFont(fonte3);
        this.labelLOGIN.setSize(58, 14);
        this.labelLOGIN.setLocation(15, 135);
        this.labelLOGIN.setVisible(true);
        this.labelSCORE.setFont(fonte3);
        this.labelSCORE.setSize(50, 14);
        this.labelSCORE.setLocation(15, 185);
        this.labelSCORE.setVisible(true);
        this.labelMESA.setFont(fonte1);
        this.labelMESA.setSize(70, 16);
        this.labelMESA.setLocation(350, 68);
        this.labelMESA.setVisible(true);
        this.labelEQUIPE1.setFont(fonte1);
        this.labelEQUIPE1.setSize(120, 16);
        this.labelEQUIPE1.setLocation(250, 100);
        this.labelEQUIPE1.setVisible(true);
        this.labelEQUIPE2.setFont(fonte1);
        this.labelEQUIPE2.setSize(120, 16);
        this.labelEQUIPE2.setLocation(420, 100);
        this.labelEQUIPE2.setVisible(true);
        this.labelAPELIDO.setFont(fonte3);
        this.labelAPELIDO.setSize(70, 14);
        this.labelAPELIDO.setLocation(45, 63);
        this.labelAPELIDO.setVisible(true);
        this.labelLOGINUsuario.setFont(fonte3);
        this.labelLOGINUsuario.setSize(60, 14);
        this.labelLOGINUsuario.setLocation(70, 135);
        this.labelLOGINUsuario.setVisible(true);
        this.labelSCOREUsuario.setFont(fonte3);
        this.labelSCOREUsuario.setSize(60, 14);
        this.labelSCOREUsuario.setLocation(70, 185);
        this.labelSCOREUsuario.setVisible(true);
        this.buttonSAIRDAMESA.setSize(116, 25);
        this.buttonSAIRDAMESA.setLocation(27, 275);
        this.buttonSAIRDAMESA.addActionListener(new buttonSAIRDAMESAListener());
        this.buttonSAIRDAMESA.setVisible(true);
        this.buttonSAIRDAMESA.setEnabled(false);
        this.buttonLOGOUT.setSize(116, 25);
        this.buttonLOGOUT.setLocation(27, 310);
        this.buttonLOGOUT.addActionListener(new buttonSAIRDOJOGOListener());
        this.buttonLOGOUT.setVisible(true);
        this.buttonLOGOUT.setEnabled(true);
        this.buttonENTRARPOSICAO1a.setSize(106, 25);
        this.buttonENTRARPOSICAO1a.setLocation(240, 160);
        this.buttonENTRARPOSICAO1a.addActionListener(new buttonENTRARPOSICAO1aListener());
        this.buttonENTRARPOSICAO1a.setVisible(true);
        this.buttonENTRARPOSICAO1b.setSize(106, 25);
        this.buttonENTRARPOSICAO1b.setLocation(240, 260);
        this.buttonENTRARPOSICAO1b.addActionListener(new buttonENTRARPOSICAO1bListener());
        this.buttonENTRARPOSICAO1b.setVisible(true);
        this.buttonENTRARPOSICAO2a.setSize(106, 25);
        this.buttonENTRARPOSICAO2a.setLocation(410, 160);
        this.buttonENTRARPOSICAO2a.addActionListener(new buttonENTRARPOSICAO2aListener());
        this.buttonENTRARPOSICAO2a.setVisible(true);
        this.buttonENTRARPOSICAO2b.setSize(106, 25);
        this.buttonENTRARPOSICAO2b.setLocation(410, 260);
        this.buttonENTRARPOSICAO2b.addActionListener(new buttonENTRARPOSICAO2bListener());
        this.buttonENTRARPOSICAO2b.setVisible(true);
        Dimension dim = frameTelaSalaDeJogo.getToolkit().getScreenSize();
        frameTelaSalaDeJogo.setSize(600, 400);
        int x = (int) (dim.getWidth() - frameTelaSalaDeJogo.getSize().getWidth()) / 2;
        int y = (int) (dim.getHeight() - frameTelaSalaDeJogo.getSize().getHeight()) / 2;
        frameTelaSalaDeJogo.setLocation(x, y);
        frameTelaSalaDeJogo.setResizable(false);
        frameTelaSalaDeJogo.setVisible(true);
        Image icone = new ImageIcon("IconeTrucoCearenseFRAME.GIF").getImage();
        frameTelaSalaDeJogo.setIconImage(icone);
        this.atualizarSalaDeJogo();
    }

    class PainelTelaSalaDeJogo extends JPanel {

        private static final long serialVersionUID = 1L;

        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawRoundRect(10, 40, 150, 326, 15, 15);
            g2d.drawRoundRect(180, 40, 400, 326, 15, 15);
            g2d.drawRect(210, 60, 340, 286);
            g2d.drawRect(210, 90, 340, 256);
            g2d.drawRect(210, 90, 170, 256);
        }
    }

    public void atualizaNomes() {
        if (nome[0] != null || this.usuario.getPosicaoMesa() != -1) {
            this.buttonENTRARPOSICAO1a.setLabel((nome[0] == null ? "" : nome[0]));
            this.buttonENTRARPOSICAO1a.setEnabled(false);
        } else {
            this.buttonENTRARPOSICAO1a.setLabel("Entrar");
            this.buttonENTRARPOSICAO1a.setEnabled(true);
        }
        if (nome[2] != null || this.usuario.getPosicaoMesa() != -1) {
            this.buttonENTRARPOSICAO1b.setLabel((nome[2] == null ? "" : nome[2]));
            this.buttonENTRARPOSICAO1b.setEnabled(false);
        } else {
            this.buttonENTRARPOSICAO1b.setLabel("Entrar");
            this.buttonENTRARPOSICAO1b.setEnabled(true);
        }
        if (nome[1] != null || this.usuario.getPosicaoMesa() != -1) {
            this.buttonENTRARPOSICAO2a.setLabel((nome[1] == null ? "" : nome[1]));
            this.buttonENTRARPOSICAO2a.setEnabled(false);
        } else {
            this.buttonENTRARPOSICAO2a.setLabel("Entrar");
            this.buttonENTRARPOSICAO2a.setEnabled(true);
        }
        if (nome[3] != null || this.usuario.getPosicaoMesa() != -1) {
            this.buttonENTRARPOSICAO2b.setLabel((nome[3] == null ? "" : nome[3]));
            this.buttonENTRARPOSICAO2b.setEnabled(false);
        } else {
            this.buttonENTRARPOSICAO2b.setLabel("Entrar");
            this.buttonENTRARPOSICAO2b.setEnabled(true);
        }
    }

    public void atualizarSalaDeJogo() {
        this.mesa.sincronizaMesa();
        this.setNome(this.mesa.getUsuariosMesa());
        this.atualizaNomes();
    }

    public void iniciarJogo() {
        frameTelaSalaDeJogo.setVisible(false);
        frameTelaSalaDeJogo.setEnabled(false);
        this.telaMesaDeJogo = new TelaMesaDeJogo(this.mesa.getUsuariosMesa(), this.mesa, this);
    }

    public void habilitaTelaSalaDeJogo() {
        this.getMesa().getUserAtual().setPosicaoMesa(-1);
        this.getMesa().getUserAtual().atualizaScore();
        this.labelSCOREUsuario.setText(Integer.toString(this.getMesa().getUserAtual().getScore()));
        this.atualizarSalaDeJogo();
        this.telaMesaDeJogo = null;
        this.buttonSAIRDAMESA.setEnabled(false);
        frameTelaSalaDeJogo.setVisible(true);
        frameTelaSalaDeJogo.setEnabled(true);
    }

    @Override
    public void update(String reference) {
        if (reference.equals("atualizarSalaDeJogo")) {
            this.atualizarSalaDeJogo();
        }
        if (reference.equals("iniciarJogo")) {
            this.iniciarJogo();
        }
    }

    public String[] getNome() {
        return nome;
    }

    public void setNome(String[] nome) {
        this.nome = nome;
    }

    class buttonSAIRDAMESAListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            mesa.removeUsuarioMesa();
            buttonSAIRDAMESA.setEnabled(false);
        }
    }

    class buttonSAIRDOJOGOListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            logout();
            frameTelaSalaDeJogo.dispose();
            new TelaInicial();
        }
    }

    class buttonENTRARPOSICAO1aListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ev) {
            getMesa().insereUsuarioMesa(0);
            buttonSAIRDAMESA.setEnabled(true);
        }
    }

    class buttonENTRARPOSICAO1bListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ev) {
            getMesa().insereUsuarioMesa(2);
            buttonSAIRDAMESA.setEnabled(true);
        }
    }

    class buttonENTRARPOSICAO2aListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ev) {
            getMesa().insereUsuarioMesa(1);
            buttonSAIRDAMESA.setEnabled(true);
        }
    }

    class buttonENTRARPOSICAO2bListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ev) {
            getMesa().insereUsuarioMesa(3);
            buttonSAIRDAMESA.setEnabled(true);
        }
    }

    public Mesa getMesa() {
        return mesa;
    }

    private void sairDoJogo() {
        this.logout();
        System.exit(0);
    }

    private void logout() {
        try {
            if (this.mesa.getUserAtual().getPosicaoMesa() != -1) this.mesa.removeUsuarioMesa();
            this.getMesa().getUserAtual().fazerLogout();
            this.getMesa().getServer().removeObserver(this.mesa);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
