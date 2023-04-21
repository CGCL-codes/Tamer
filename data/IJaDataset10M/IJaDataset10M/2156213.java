package batalha.interfacegrafica;

import batalha.gerencia.GerenciadorJogo;
import batalha.interfacegrafica.jogo.*;
import java.util.ArrayList;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.awt.event.*;
import java.awt.*;

/**
 * @author Renato, Paulo, Alexandre, Mois�s e Marcelo
 */
public class BatalhaNavalWindow extends JFrame {

    private JLabel lbApelido = null, lbOpcao = null, lbIPs = null;

    private JTextField txfApelido = null, txfNovoIP = null;

    private JComboBox cbIPs = null;

    private ButtonGroup bgOpcao = null;

    private JRadioButton jrbServidor = null, jrbCliente = null;

    private JCheckBox chbNovoIP = null;

    private BufferedReader streamEntrada = null;

    private PrintWriter streamSaida = null;

    private static final String ARQUIVO_IPs = "src/batalha/arquivo_ips.txt";

    private CardLayout layoutFrame = null;

    private SpringLayout layoutContainer = null;

    private boolean isServidor = false;

    private ButtonGroupHandler bgHandler = null;

    private String ultimoIP = null;

    private Container cInicial = null;

    private JButton botaoOk = null;

    private String apelidoJogador = null;

    private PainelDoJogo painel = null;

    private Thread jogo;

    private boolean jogoCriado = false;

    private JMenuBar barramenu;

    private JMenu mnuJogo;

    private JMenuItem mnuNovoJogo;

    private JMenuItem mnuSair;

    private JMenu mnuAjuda;

    private JMenuItem mnuComoUsar;

    private JMenu mnuCredito;

    private JMenuItem mnuSobre;

    /**
     * Construtor da classe BatalhaNavalWindow
     */
    public BatalhaNavalWindow() {
        configuraFrame();
    }

    private void configuraFrame() {
        try {
            Thread.sleep(9000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        Som.playAudio(Som.BEM_VINDO);
        final ImageIcon image = new ImageIcon("src/imagens/gato.gif");
        setIconImage(image.getImage());
        barramenu = new JMenuBar();
        mnuJogo = new JMenu("Jogo");
        mnuJogo.setMnemonic('J');
        mnuNovoJogo = new JMenuItem("Novo Jogo");
        mnuNovoJogo.setMnemonic('N');
        KeyStroke F2 = KeyStroke.getKeyStroke("F2");
        mnuNovoJogo.setAccelerator(F2);
        mnuSair = new JMenuItem("Fechar");
        mnuSair.setMnemonic('S');
        KeyStroke altF4 = KeyStroke.getKeyStroke("alt F4");
        mnuSair.setAccelerator(altF4);
        mnuAjuda = new JMenu("Ajuda");
        mnuAjuda.setMnemonic('A');
        mnuComoUsar = new JMenuItem("como jogar...");
        mnuComoUsar.setMnemonic('C');
        KeyStroke ajudaF1 = KeyStroke.getKeyStroke("F1");
        mnuComoUsar.setAccelerator(ajudaF1);
        mnuCredito = new JMenu("Creditos");
        mnuCredito.setMnemonic('C');
        mnuSobre = new JMenuItem("Sobre");
        mnuSobre.setMnemonic('S');
        mnuCredito.add(mnuSobre);
        mnuJogo.add(mnuNovoJogo);
        mnuJogo.addSeparator();
        mnuJogo.add(mnuSair);
        mnuAjuda.add(mnuComoUsar);
        mnuCredito.add(mnuCredito);
        barramenu.add(mnuJogo);
        barramenu.add(mnuAjuda);
        barramenu.add(mnuCredito);
        this.setJMenuBar(barramenu);
        mnuSair.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (!jogoCriado) {
                    System.exit(0);
                }
            }
        });
        mnuSobre.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                new Credito();
            }
        });
        mnuComoUsar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JOptionPane novo = new JOptionPane();
                novo.showMessageDialog(null, "\n\t\t                                   MAMP&R - Batalha Naval 1.0\n\n" + "1 - Inicie o jogo colocando seu Nick, e escolhendo entre cliente e servidor\n" + "2 - Caso escolhido servidor, clique no bot�o  - Ok Estou Pronto!\n" + "3 - Caso escolhido cliente, selecione o Ip do servidor," + "\n     se desejar adicionar novo Ip habilite o checkbox e insira o novo Ip e aperte o bot�o - Ok Estou Pronto!\n" + "4 - Posicione suas pe�as no tabuleiro da esquerda e aperte OK\n" + "5 - O jogador que pressionar primeiro o bot�o OK ser� o primeiro a jogar\n" + "6 - As vezes entre os jogadores v�o se alternando enquanto acertar as pe�as\n" + " do advers�rio, se acertar �gua a vez � passado para outro jogador\n" + "7 - O jogo termina quando as 18 pe�as de um jogador forem destruidas\n" + "8 - Caso deseja jogar novamente aperte F2\n\n" + "\t\t\t\t                         BOA DIVERS�O\n\n" + "\t\t\t                                        � 2007 MAMP&R\n\n ", "Como Jogar", JOptionPane.WARNING_MESSAGE, image);
            }
        });
        cInicial = new Container();
        painel = new PainelDoJogo(this.mnuSair, this.mnuNovoJogo);
        layoutContainer = new SpringLayout();
        cInicial.setLayout(layoutContainer);
        cInicial.setPreferredSize(new Dimension(620, 400));
        lbApelido = new JLabel("Meu apelido: ");
        lbOpcao = new JLabel("Quero ser: ");
        lbIPs = new JLabel("Quero me conectar no IP: ");
        lbApelido.setPreferredSize(new Dimension(80, 20));
        lbOpcao.setPreferredSize(new Dimension(80, 20));
        lbIPs.setPreferredSize(new Dimension(160, 20));
        txfApelido = new JTextField(20);
        txfApelido.setPreferredSize(new Dimension(40, 20));
        txfNovoIP = new JTextField(16);
        txfNovoIP.setPreferredSize(new Dimension(35, 20));
        txfNovoIP.setEnabled(false);
        try {
            inicializaComboBox();
        } catch (Exception e) {
            e.printStackTrace();
        }
        jrbServidor = new JRadioButton("Servidor");
        jrbCliente = new JRadioButton("Cliente");
        chbNovoIP = new JCheckBox("Quero colocar um novo ip");
        chbNovoIP.addItemListener(new CheckBoxHandler());
        chbNovoIP.setEnabled(false);
        bgHandler = new ButtonGroupHandler();
        jrbServidor.addItemListener(bgHandler);
        jrbCliente.addItemListener(bgHandler);
        jrbServidor.setSelected(true);
        bgOpcao = new ButtonGroup();
        bgOpcao.add(jrbServidor);
        bgOpcao.add(jrbCliente);
        botaoOk = new JButton("Ok! Estou pronto!");
        botaoOk.setPreferredSize(new Dimension(140, 30));
        botaoOk.setEnabled(true);
        botaoOk.addActionListener(new ButtonHandler());
        this.setContentPane(cInicial);
        cInicial.add(lbApelido);
        cInicial.add(lbOpcao);
        cInicial.add(lbIPs);
        cInicial.add(txfApelido);
        cInicial.add(cbIPs);
        cInicial.add(jrbServidor);
        cInicial.add(jrbCliente);
        cInicial.add(chbNovoIP);
        cInicial.add(txfNovoIP);
        cInicial.add(botaoOk);
        configuraLayout();
        this.pack();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension posicao = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((posicao.width - this.getSize().width) / 2, (posicao.height - this.getSize().height) / 2);
        this.setVisible(true);
    }

    /**
     * Configura o layout do frame
     */
    private void configuraLayout() {
        layoutContainer.putConstraint(SpringLayout.NORTH, lbApelido, 50, SpringLayout.NORTH, cInicial);
        layoutContainer.putConstraint(SpringLayout.WEST, lbApelido, 100, SpringLayout.WEST, cInicial);
        layoutContainer.putConstraint(SpringLayout.NORTH, txfApelido, 50, SpringLayout.NORTH, cInicial);
        layoutContainer.putConstraint(SpringLayout.WEST, txfApelido, 10, SpringLayout.EAST, lbApelido);
        layoutContainer.putConstraint(SpringLayout.NORTH, lbOpcao, 25, SpringLayout.SOUTH, lbApelido);
        layoutContainer.putConstraint(SpringLayout.WEST, lbOpcao, 100, SpringLayout.WEST, cInicial);
        layoutContainer.putConstraint(SpringLayout.NORTH, jrbServidor, 25, SpringLayout.SOUTH, lbApelido);
        layoutContainer.putConstraint(SpringLayout.WEST, jrbServidor, 25, SpringLayout.EAST, lbOpcao);
        layoutContainer.putConstraint(SpringLayout.NORTH, jrbCliente, 25, SpringLayout.SOUTH, lbApelido);
        layoutContainer.putConstraint(SpringLayout.WEST, jrbCliente, 15, SpringLayout.EAST, jrbServidor);
        layoutContainer.putConstraint(SpringLayout.NORTH, lbIPs, 25, SpringLayout.SOUTH, lbOpcao);
        layoutContainer.putConstraint(SpringLayout.WEST, lbIPs, 100, SpringLayout.WEST, cInicial);
        layoutContainer.putConstraint(SpringLayout.NORTH, cbIPs, 25, SpringLayout.SOUTH, lbOpcao);
        layoutContainer.putConstraint(SpringLayout.WEST, cbIPs, 15, SpringLayout.EAST, lbIPs);
        layoutContainer.putConstraint(SpringLayout.NORTH, chbNovoIP, 25, SpringLayout.SOUTH, lbIPs);
        layoutContainer.putConstraint(SpringLayout.WEST, chbNovoIP, 10, SpringLayout.EAST, lbIPs);
        layoutContainer.putConstraint(SpringLayout.NORTH, txfNovoIP, 25, SpringLayout.SOUTH, chbNovoIP);
        layoutContainer.putConstraint(SpringLayout.WEST, txfNovoIP, 10, SpringLayout.EAST, lbIPs);
        layoutContainer.putConstraint(SpringLayout.NORTH, botaoOk, 55, SpringLayout.SOUTH, txfNovoIP);
        layoutContainer.putConstraint(SpringLayout.WEST, botaoOk, 5, SpringLayout.WEST, chbNovoIP);
    }

    /**
     * Inicializa a combo box com os IPs. Este m�todo pode lan�ar uma excess�o se o arquivo n�o for encontrado.
     */
    private void inicializaComboBox() throws IOException {
        ArrayList<String> ips = new ArrayList<String>();
        try {
            streamEntrada = new BufferedReader(new FileReader(ARQUIVO_IPs));
            String l = null;
            l = streamEntrada.readLine();
            if (l == null) {
                ips.add(" ");
            } else {
                String ipsStr[] = l.split("/");
                for (String ip : ipsStr) ips.add(ip);
            }
            if (ips.size() > 0) ultimoIP = ips.get(0);
            cbIPs = new JComboBox(ips.toArray());
            cbIPs.setPreferredSize(new Dimension(200, 25));
            cbIPs.setEditable(false);
            cbIPs.addItemListener(new ComboBoxHandler());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (streamEntrada != null) streamEntrada.close();
        }
    }

    /**
     * Adiciona um novo ip no arquivo
     */
    private void adicionarNovoIP(String novoIP) {
        try {
            streamSaida = new PrintWriter(new FileWriter(ARQUIVO_IPs, true));
            CharSequence cs = novoIP.subSequence(0, novoIP.length() - 1) + "/";
            streamSaida.append(cs);
            streamSaida.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (streamSaida != null) streamSaida.close();
        }
    }

    /**
     * ButtonGroupHandler.java
     *
     * Criado em 25 de Agosto de 2007, 22:08
     *
     * O prop�sito desta classe � implementar um listener para os eventos do gerenciador de bot�es de r�dio
     */
    private class ButtonGroupHandler implements ItemListener {

        public void itemStateChanged(ItemEvent ae) {
            if (ae.getStateChange() == ItemEvent.SELECTED) {
                if (jrbServidor == (JRadioButton) ae.getSource()) {
                    isServidor = true;
                    cbIPs.setEnabled(false);
                    chbNovoIP.setEnabled(false);
                } else {
                    isServidor = false;
                    cbIPs.setEnabled(true);
                    chbNovoIP.setEnabled(true);
                }
            }
        }
    }

    /**
     * ComboBoxHandler.java
     *
     * Criado em 25 de Agosto de 2007, 22:28
     *
     * O prop�sito desta classe � lidar com eventos sobre a combo box
     */
    private class ComboBoxHandler implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ultimoIP = (String) ((JComboBox) e.getSource()).getSelectedItem();
                System.out.printf("Ultimo Ip Selecionado: %s", (ultimoIP == null) ? "nenhum" : ultimoIP);
            }
        }
    }

    /**
     * CheckBoxHandler.java
     *
     * Criado em 25 de Agosto de 2007, 22:28
     *
     * O prop�sito desta classe � lidar com eventos sobre o checkbox
     */
    private class CheckBoxHandler implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                txfNovoIP.setEnabled(true);
                cbIPs.setEnabled(false);
            }
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                txfNovoIP.setEnabled(false);
                cbIPs.setEnabled(true);
            }
        }
    }

    /**
     * ButtonHandler.java
     *
     * Criado em 25 de Agosto de 2007, 22:28
     *
     * O prop�sito desta classe � lidar com eventos sobre o bot�o de jogar
     */
    private class ButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            BatalhaNavalWindow.this.remove(BatalhaNavalWindow.this.getContentPane());
            if (txfNovoIP.isEnabled() && txfNovoIP.getText() != null) {
                adicionarNovoIP(txfNovoIP.getText() + "\n");
                ultimoIP = txfNovoIP.getText();
            }
            painel.setNick(txfApelido.getText());
            if (chbNovoIP.isSelected()) {
                painel.setIp(txfNovoIP.getText());
            } else {
                painel.setIp((String) cbIPs.getSelectedItem());
            }
            painel.setServidor(isServidor);
            painel.setVisible(true);
            jogoCriado = true;
            jogo = new GerenciadorJogo(painel);
            BatalhaNavalWindow.this.setContentPane(painel);
            BatalhaNavalWindow.this.validate();
            BatalhaNavalWindow.this.pack();
            SwingUtilities.updateComponentTreeUI(BatalhaNavalWindow.this);
            Som.playAudio(Som.SOM_CONFIG);
            if (painel.getServidor()) setTitle("Servidor - " + painel.getNick()); else setTitle("Cliente - " + painel.getNick());
            jogo.start();
        }
    }

    public JMenuItem getNovoJogo() {
        return this.mnuNovoJogo;
    }

    public JMenuItem getSair() {
        return this.mnuSair;
    }
}
