package grafico;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import voxToolkit.VoxButton;
import voxToolkit.VoxCombo;
import voxToolkit.VoxTextField;
import main.Contexto;

public class WndPrincipal extends JFrame {

    static final long serialVersionUID = 1;

    private JPanel painel_superior;

    private JPanel painel_inferior;

    private JEditorPane painel_texto;

    private JPanel controle_esquerda;

    private JPanel controle_centro;

    private JPanel controle_direita;

    private JPanel painel_navegacao;

    private JPanel painel_velo_nivel;

    private JPanel painel_botoes;

    private JPanel painel_pagina;

    private VoxButton btnNavPlay;

    private VoxButton btnNavStop;

    private VoxButton btnNavPrevious;

    private VoxButton btnNavVolta;

    private VoxButton btnNavAvanca;

    private VoxButton btnNavProximo;

    private VoxButton btnSemSom;

    private VoxButton btnComSom;

    private VoxButton btnBtnListar;

    private VoxButton btnBtnAbrir;

    private VoxButton btnBtnSemTexto;

    private VoxButton btnBtnComTexto;

    private VoxButton btnBtnBusca;

    private VoxButton btnBtnHelp;

    private VoxButton btnBtnInfo;

    private VoxButton btnBtnConfig;

    private JLabel lbMecDaisy;

    private JButton logoDaisy;

    private JButton logoNCE;

    private JButton logoMEC;

    private JPanel playerMecDaisy;

    private JPanel pnLogos;

    private JPanel painel_nivel;

    private JLabel lbNivel;

    private VoxCombo cmbNivel;

    private JLabel lbPagina;

    private VoxTextField txPagina;

    private GerenteGUI gerente;

    private ControleEventos ctrlEventos;

    private Contexto contexto;

    public WndPrincipal(GerenteGUI pai) {
        super();
        gerente = pai;
        contexto = Contexto.instancia();
        ctrlEventos = ControleEventos.instancia();
        initGUI();
        addWindowListener(ctrlEventos);
    }

    public JEditorPane getTxtPane() {
        return painel_texto;
    }

    public void setLayoutModoContinuo() {
        btnNavPlay.setVisible(false);
        btnNavStop.setVisible(true);
    }

    public void setLayoutModoParagrafo() {
        btnNavPlay.setVisible(false);
        btnNavStop.setVisible(true);
    }

    public void setLayoutModoParado() {
        btnNavPlay.setVisible(true);
        btnNavStop.setVisible(false);
    }

    private void initGUI() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setTitle("MECDaisy - v1.0");
            getRootPane().getActionMap().put("SpaceEvent", ctrlEventos.getSpaceAction());
            InputMap im = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "SpaceEvent");
            Container principal = getContentPane();
            principal.setLayout(new BorderLayout());
            principal.setBackground(Fabrica.background);
            principal.setPreferredSize(new Dimension(800, 600));
            principal.setMaximumSize(new Dimension(800, 600));
            principal.setMinimumSize(new Dimension(800, 600));
            painel_superior = Fabrica.painelFundoAzul();
            painel_superior.setPreferredSize(new java.awt.Dimension(800, 90));
            painel_superior.setLayout(new BoxLayout(painel_superior, BoxLayout.X_AXIS));
            principal.add(painel_superior, BorderLayout.NORTH);
            painel_texto = new JEditorPane();
            painel_texto.setEditable(false);
            painel_texto.setContentType("text/html");
            painel_texto.setBackground(gerente.toColor(contexto.getCorFundo()));
            painel_texto.setFocusable(false);
            principal.add(painel_texto, BorderLayout.CENTER);
            painel_inferior = Fabrica.painelFundoAzul();
            painel_inferior.setLayout(new BorderLayout());
            painel_inferior.setPreferredSize(new Dimension(800, 50));
            playerMecDaisy = Fabrica.painelFundoAzul();
            playerMecDaisy.setPreferredSize(new Dimension(450, 50));
            lbMecDaisy = Fabrica.labelBranco("MECDaisy");
            lbMecDaisy.setFont(Fabrica.fonteGrande);
            lbMecDaisy.setFocusable(false);
            playerMecDaisy.add(lbMecDaisy);
            painel_inferior.add(playerMecDaisy, BorderLayout.WEST);
            pnLogos = Fabrica.painelFundoAzul();
            pnLogos.setLayout(new BorderLayout());
            pnLogos.setPreferredSize(new Dimension(300, 50));
            pnLogos.setMaximumSize(new Dimension(300, 50));
            pnLogos.setMinimumSize(new Dimension(300, 50));
            painel_inferior.add(pnLogos, BorderLayout.EAST);
            logoDaisy = Fabrica.botaoLogo("daisy");
            logoDaisy.setFocusable(false);
            logoNCE = Fabrica.botaoLogo("nce");
            logoNCE.setFocusable(false);
            logoMEC = Fabrica.botaoLogo("mec");
            logoMEC.setFocusable(false);
            pnLogos.add(logoDaisy, BorderLayout.WEST);
            pnLogos.add(logoMEC, BorderLayout.CENTER);
            pnLogos.add(logoNCE, BorderLayout.EAST);
            principal.add(painel_inferior, BorderLayout.SOUTH);
            controle_esquerda = Fabrica.painelFundoAzul();
            painel_superior.add(Box.createRigidArea(new Dimension(15, 90)));
            painel_superior.add(controle_esquerda, 1);
            controle_esquerda.setLayout(new BoxLayout(controle_esquerda, BoxLayout.Y_AXIS));
            painel_navegacao = Fabrica.painelFundoAzul();
            painel_navegacao.setLayout(new BoxLayout(painel_navegacao, BoxLayout.X_AXIS));
            controle_esquerda.add(painel_navegacao);
            painel_velo_nivel = Fabrica.painelFundoAzul();
            painel_velo_nivel.setLayout(new BoxLayout(painel_velo_nivel, BoxLayout.X_AXIS));
            controle_esquerda.add(painel_velo_nivel);
            btnNavPrevious = Fabrica.botaoComImagem("nivel_anterior", "Nível Anterior");
            painel_navegacao.add(btnNavPrevious);
            btnNavPrevious.addActionListener(ControleEventos.instancia());
            btnNavPrevious.addKeyListener(ControleEventos.instancia());
            btnNavVolta = Fabrica.botaoComImagem("retrocede", "Retroceder");
            painel_navegacao.add(btnNavVolta);
            btnNavVolta.addActionListener(ControleEventos.instancia());
            btnNavVolta.addKeyListener(ControleEventos.instancia());
            btnNavPlay = Fabrica.botaoComImagem("play", "Tocar");
            btnNavStop = Fabrica.botaoComImagem("stop", "Parar");
            btnNavPlay.addActionListener(ControleEventos.instancia());
            btnNavPlay.addKeyListener(ControleEventos.instancia());
            btnNavStop.addActionListener(ControleEventos.instancia());
            btnNavStop.addKeyListener(ControleEventos.instancia());
            btnNavPlay.setReverso(btnNavStop);
            btnNavStop.setReverso(btnNavPlay);
            btnNavStop.setVisible(false);
            painel_navegacao.add(btnNavPlay);
            painel_navegacao.add(btnNavStop);
            btnNavAvanca = Fabrica.botaoComImagem("avanca", "Avançar");
            painel_navegacao.add(btnNavAvanca);
            btnNavAvanca.addActionListener(ControleEventos.instancia());
            btnNavAvanca.addKeyListener(ControleEventos.instancia());
            btnNavProximo = Fabrica.botaoComImagem("proximo_nivel", "Próximo Nível");
            painel_navegacao.add(btnNavProximo);
            btnNavProximo.addActionListener(ControleEventos.instancia());
            btnNavProximo.addKeyListener(ControleEventos.instancia());
            btnSemSom = Fabrica.botaoComImagem("sem_som", "Tirar o Som");
            btnSemSom.setAlignmentX(CENTER_ALIGNMENT);
            btnComSom = Fabrica.botaoComImagem("com_som", "Tocar Som");
            btnComSom.setAlignmentX(CENTER_ALIGNMENT);
            btnSemSom.setReverso(btnComSom);
            btnComSom.setReverso(btnSemSom);
            btnComSom.setVisible(false);
            btnSemSom.addActionListener(ControleEventos.instancia());
            btnSemSom.addKeyListener(ControleEventos.instancia());
            btnComSom.addActionListener(ControleEventos.instancia());
            btnComSom.addKeyListener(ControleEventos.instancia());
            painel_navegacao.add(btnSemSom);
            painel_navegacao.add(btnComSom);
            controle_centro = Fabrica.painelFundoAzul();
            controle_centro.setPreferredSize(new Dimension(200, 90));
            controle_centro.setMinimumSize(new Dimension(200, 90));
            controle_centro.setMaximumSize(new Dimension(200, 90));
            controle_centro.setLayout(new BoxLayout(controle_centro, BoxLayout.Y_AXIS));
            painel_pagina = Fabrica.painelFundoAzul();
            painel_pagina.setLayout(new BoxLayout(painel_pagina, BoxLayout.X_AXIS));
            lbPagina = Fabrica.labelBranco("Página: ");
            lbPagina.setFocusable(false);
            painel_pagina.add(lbPagina);
            txPagina = new VoxTextField("Página");
            txPagina.setFocusable(true);
            painel_pagina.add(txPagina);
            controle_centro.add(Box.createRigidArea(new Dimension(10, 10)));
            controle_centro.add(painel_pagina);
            painel_nivel = Fabrica.painelFundoAzul();
            painel_nivel.setLayout(new BoxLayout(painel_nivel, BoxLayout.X_AXIS));
            lbNivel = Fabrica.labelBranco("Nível: ");
            lbNivel.setFocusable(false);
            painel_nivel.add(lbNivel);
            String[] niveis = { "1", "2", "3", "4", "5", "6" };
            cmbNivel = new VoxCombo(niveis, "cmbNivel", "Nível");
            cmbNivel.setFocusable(true);
            painel_nivel.add(cmbNivel);
            controle_centro.add(Box.createRigidArea(new Dimension(10, 10)));
            controle_centro.add(painel_nivel);
            controle_centro.add(Box.createRigidArea(new Dimension(10, 10)));
            painel_superior.add(Box.createRigidArea(new Dimension(20, 90)));
            painel_superior.add(controle_centro);
            painel_superior.add(Box.createRigidArea(new Dimension(20, 90)));
            controle_direita = new JPanel();
            controle_direita.setBackground(new Color(51, 51, 102));
            painel_superior.add(controle_direita);
            painel_superior.add(Box.createRigidArea(new Dimension(15, 90)));
            controle_direita.setLayout(new BoxLayout(controle_direita, BoxLayout.Y_AXIS));
            painel_botoes = new JPanel();
            painel_botoes.setLayout(new BoxLayout(painel_botoes, BoxLayout.X_AXIS));
            controle_direita.add(painel_botoes);
            btnBtnAbrir = Fabrica.botaoComImagem("abrir", "Abrir");
            painel_botoes.add(btnBtnAbrir);
            btnBtnAbrir.addActionListener(ControleEventos.instancia());
            btnBtnAbrir.addKeyListener(ControleEventos.instancia());
            btnBtnListar = Fabrica.botaoComImagem("listar", "Mostrar Estrutura");
            painel_botoes.add(btnBtnListar);
            btnBtnListar.addActionListener(ControleEventos.instancia());
            btnBtnListar.addKeyListener(ControleEventos.instancia());
            btnBtnSemTexto = Fabrica.botaoComImagem("sem_texto", "Sem Texto");
            btnBtnSemTexto.setAlignmentX(CENTER_ALIGNMENT);
            btnBtnSemTexto.addActionListener(ControleEventos.instancia());
            btnBtnSemTexto.addKeyListener(ControleEventos.instancia());
            btnBtnComTexto = Fabrica.botaoComImagem("com_texto", "Exibir Texto");
            btnBtnComTexto.setAlignmentX(CENTER_ALIGNMENT);
            btnBtnComTexto.addActionListener(ControleEventos.instancia());
            btnBtnComTexto.addKeyListener(ControleEventos.instancia());
            btnBtnSemTexto.setReverso(btnBtnComTexto);
            btnBtnComTexto.setReverso(btnBtnSemTexto);
            btnBtnComTexto.setVisible(false);
            painel_botoes.add(btnBtnSemTexto);
            painel_botoes.add(btnBtnComTexto);
            btnBtnBusca = Fabrica.botaoComImagem("busca", "Buscar");
            painel_botoes.add(btnBtnBusca);
            btnBtnBusca.addActionListener(ControleEventos.instancia());
            btnBtnBusca.addKeyListener(ControleEventos.instancia());
            btnBtnHelp = Fabrica.botaoComImagem("help", "Ajuda");
            painel_botoes.add(btnBtnHelp);
            btnBtnHelp.addActionListener(ControleEventos.instancia());
            btnBtnHelp.addKeyListener(ControleEventos.instancia());
            btnBtnInfo = Fabrica.botaoComImagem("info", "Informações sobre o texto");
            painel_botoes.add(btnBtnInfo);
            btnBtnInfo.addActionListener(ControleEventos.instancia());
            btnBtnInfo.addKeyListener(ControleEventos.instancia());
            btnBtnConfig = Fabrica.botaoComImagem("config", "Configurações");
            painel_botoes.add(btnBtnConfig);
            btnBtnConfig.addActionListener(ControleEventos.instancia());
            btnBtnConfig.addKeyListener(ControleEventos.instancia());
            this.setSize(800, 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
