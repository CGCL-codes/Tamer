package br.uesc.computacao.estagio.apresentacao.GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import java.awt.event.WindowEvent;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JProgressBar;

/**
 * Classe que gera a tela inicial da IGrafu
 * @author Zilton José Maciel Cordeiro Junior - Orientadora: Martha Ximena Torres Delgado
 * @version 2.0
 */
public class ModoExecucao extends JFrame {

    /**
	 * Variaveis Globais
	 */
    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JLabel jLabelModoExecucao = null;

    private JRadioButton jRadioButtonModoExecucaoManual = null;

    private JRadioButton jRadioButtonModoExecucaoAutomatico = null;

    private JRadioButton jRadioButtonModoExecucaoPerfil = null;

    private JLabel jLabelArquivoSequencia = null;

    private JTextField jTextFieldArquivoSequencia = null;

    private JButton jButtonArquivoSequencia = null;

    private JButton jButtonAvancar = null;

    private JButton jButtonVisualizar = null;

    private JSeparator jSeparator1 = null;

    private JSeparator jSeparator2 = null;

    private JSeparator jSeparator3 = null;

    private JSeparator jSeparator4 = null;

    private JSeparator jSeparator5 = null;

    private JSeparator jSeparator6 = null;

    private JSeparator jSeparator7 = null;

    private JSeparator jSeparator8 = null;

    private JSeparator jSeparator9 = null;

    private JSeparator jSeparator10 = null;

    private JSeparator jSeparator11 = null;

    private JSeparator jSeparator12 = null;

    private JSeparator jSeparator13 = null;

    private JSeparator jSeparator14 = null;

    private JSeparator jSeparator15 = null;

    private JSeparator jSeparator16 = null;

    private JSeparator jSeparator17 = null;

    private JSeparator jSeparator18 = null;

    private JMenuBar jJMenuBar = null;

    private JMenu jMenuArquivo = null;

    private JMenu jMenuConfiguracao = null;

    private JMenu jMenuAjuda = null;

    private JMenu jMenuIdioma = null;

    private JCheckBoxMenuItem jCheckBoxMenuItemEnglish = null;

    private JCheckBoxMenuItem jCheckBoxMenuItemPortuguese = null;

    private JMenuItem jMenuItemFechar = null;

    private JMenuItem jMenuItemSobre = null;

    private JDialog jDialogSobre = null;

    private JPanel jContentPane1 = null;

    private JPanel jPanelCamaleao = null;

    private JTextArea jTextArea = null;

    private JDialog jDialogModoExecucaoPerfil = null;

    private JPanel jContentPane2 = null;

    private JLabel jLabelPerfil = null;

    private JSeparator jSeparator = null;

    private JSeparator jSeparator19 = null;

    private JSeparator jSeparator20 = null;

    private JSeparator jSeparator191 = null;

    private JSeparator jSeparator21 = null;

    private JSeparator jSeparator211 = null;

    private JSeparator jSeparator22 = null;

    private JSeparator jSeparator221 = null;

    private JSeparator jSeparator2111 = null;

    private JButton jButtonExecutarPerfil = null;

    private JComboBox jComboBoxExecucaoPerfil = null;

    private JMenu jMenuPerfil = null;

    private JMenuItem jMenuItemDeletarPerfil = null;

    private JProgressBar jProgressBarAutomatico = null;

    /**
	 * This method initializes jRadioButtonModoExecucaoManual
	 *
	 * @return javax.swing.JRadioButton
	 */
    public JRadioButton getJRadioButtonModoExecucaoManual() {
        if (jRadioButtonModoExecucaoManual == null) {
            jRadioButtonModoExecucaoManual = new JRadioButton();
            jRadioButtonModoExecucaoManual.setText("Manual");
            jRadioButtonModoExecucaoManual.setSize(new Dimension(88, 21));
            jRadioButtonModoExecucaoManual.setLocation(new Point(426, 110));
            jRadioButtonModoExecucaoManual.setToolTipText("As opções de execução do programa são escolhidas pelo usuário.");
            jRadioButtonModoExecucaoManual.setBackground(new Color(173, 200, 226));
        }
        return jRadioButtonModoExecucaoManual;
    }

    /**
	 * This method initializes jRadioButtonModoExecucaoManual
	 *
	 * @return javax.swing.JRadioButton
	 */
    public JRadioButton getJRadioButtonModoExecucaoPerfil() {
        if (jRadioButtonModoExecucaoPerfil == null) {
            jRadioButtonModoExecucaoPerfil = new JRadioButton();
            jRadioButtonModoExecucaoPerfil.setText("Perfil");
            jRadioButtonModoExecucaoPerfil.setSize(new Dimension(88, 21));
            jRadioButtonModoExecucaoPerfil.setLocation(new Point(426, 147));
            jRadioButtonModoExecucaoPerfil.setToolTipText("Carrega as opções do perfil.");
            jRadioButtonModoExecucaoPerfil.setBackground(new Color(173, 200, 226));
        }
        return jRadioButtonModoExecucaoPerfil;
    }

    /**
	 * This method initializes jRadioButtonModoExecucaoAutomatico
	 *
	 * @return javax.swing.JRadioButton
	 */
    public JRadioButton getJRadioButtonModoExecucaoAutomatico() {
        if (jRadioButtonModoExecucaoAutomatico == null) {
            jRadioButtonModoExecucaoAutomatico = new JRadioButton();
            jRadioButtonModoExecucaoAutomatico.setText("Automático");
            jRadioButtonModoExecucaoAutomatico.setSize(new Dimension(111, 21));
            jRadioButtonModoExecucaoAutomatico.setLocation(new Point(426, 184));
            jRadioButtonModoExecucaoAutomatico.setToolTipText("As opções de execução do programa são escolhidas em relação ao arquivo da seqüência passado pelo usuário, gerando ao final a árvore filogenética.");
            jRadioButtonModoExecucaoAutomatico.setBackground(new Color(173, 200, 226));
        }
        return jRadioButtonModoExecucaoAutomatico;
    }

    /**
	 * This method initializes jTextFieldArquivoSequencia
	 *
	 * @return javax.swing.JTextField
	 */
    public JTextField getJTextFieldArquivoSequencia() {
        if (jTextFieldArquivoSequencia == null) {
            jTextFieldArquivoSequencia = new JTextField();
            jTextFieldArquivoSequencia.setLocation(new Point(399, 294));
            jTextFieldArquivoSequencia.setSize(new Dimension(150, 20));
            jTextFieldArquivoSequencia.setEditable(false);
        }
        return jTextFieldArquivoSequencia;
    }

    /**
	 * This method initializes jButtonArquivoSequencia
	 *
	 * @return javax.swing.JButton
	 */
    public JButton getJButtonArquivoSequencia() {
        if (jButtonArquivoSequencia == null) {
            jButtonArquivoSequencia = new JButton();
            jButtonArquivoSequencia.setIcon(new ImageIcon(getClass().getResource("/br/uesc/computacao/estagio/apresentacao/figuras/folder.png")));
            jButtonArquivoSequencia.setLocation(new Point(560, 290));
            jButtonArquivoSequencia.setToolTipText("Abrir arquivo");
            jButtonArquivoSequencia.setSize(new Dimension(30, 29));
            jButtonArquivoSequencia.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        return jButtonArquivoSequencia;
    }

    /**
	 * This method initializes jButtonAvancar
	 *
	 * @return javax.swing.JButton
	 */
    public JButton getJButtonAvancar() {
        if (jButtonAvancar == null) {
            jButtonAvancar = new JButton();
            jButtonAvancar.setBounds(new Rectangle(655, 384, 110, 30));
            jButtonAvancar.setText("Avançar");
            jButtonAvancar.setFont(new Font("Dialog", Font.BOLD, 10));
            jButtonAvancar.setIcon(new ImageIcon(getClass().getResource("/br/uesc/computacao/estagio/apresentacao/figuras/avancar.png")));
            jButtonAvancar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        return jButtonAvancar;
    }

    /**
	 * This method initializes jButtonVisualizar
	 *
	 * @return javax.swing.JButton
	 */
    public JButton getJButtonVisualizar() {
        if (jButtonVisualizar == null) {
            jButtonVisualizar = new JButton();
            jButtonVisualizar.setBounds(new Rectangle(655, 340, 110, 30));
            jButtonVisualizar.setText("Visualizar");
            jButtonVisualizar.setFont(new Font("Dialog", Font.BOLD, 10));
            jButtonVisualizar.setIcon(new ImageIcon(getClass().getResource("/br/uesc/computacao/estagio/apresentacao/figuras/HyperTree.gif")));
            jButtonVisualizar.setVisible(false);
            jButtonVisualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        return jButtonVisualizar;
    }

    /**
	 * This method initializes jSeparator1
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator1() {
        if (jSeparator1 == null) {
            jSeparator1 = new JSeparator();
            jSeparator1.setOrientation(SwingConstants.VERTICAL);
            jSeparator1.setLocation(new Point(390, 71));
            jSeparator1.setSize(new Dimension(10, 26));
        }
        return jSeparator1;
    }

    /**
	 * This method initializes jSeparator2
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator2() {
        if (jSeparator2 == null) {
            jSeparator2 = new JSeparator();
            jSeparator2.setSize(new Dimension(131, 10));
            jSeparator2.setLocation(new Point(390, 70));
        }
        return jSeparator2;
    }

    /**
	 * This method initializes jSeparator3
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator3() {
        if (jSeparator3 == null) {
            jSeparator3 = new JSeparator();
            jSeparator3.setOrientation(SwingConstants.VERTICAL);
            jSeparator3.setSize(new Dimension(10, 26));
            jSeparator3.setLocation(new Point(521, 71));
        }
        return jSeparator3;
    }

    /**
	 * This method initializes jSeparator4
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator4() {
        if (jSeparator4 == null) {
            jSeparator4 = new JSeparator();
            jSeparator4.setLocation(new Point(391, 97));
            jSeparator4.setSize(new Dimension(130, 10));
        }
        return jSeparator4;
    }

    /**
	 * This method initializes jSeparator5
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator5() {
        if (jSeparator5 == null) {
            jSeparator5 = new JSeparator();
            jSeparator5.setSize(new Dimension(49, 10));
            jSeparator5.setLocation(new Point(341, 83));
        }
        return jSeparator5;
    }

    /**
	 * This method initializes jSeparator6
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator6() {
        if (jSeparator6 == null) {
            jSeparator6 = new JSeparator();
            jSeparator6.setLocation(new Point(522, 83));
            jSeparator6.setSize(new Dimension(68, 10));
        }
        return jSeparator6;
    }

    /**
	 * This method initializes jSeparator7
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator7() {
        if (jSeparator7 == null) {
            jSeparator7 = new JSeparator();
            jSeparator7.setOrientation(SwingConstants.VERTICAL);
            jSeparator7.setLocation(new Point(340, 83));
            jSeparator7.setSize(new Dimension(10, 126));
        }
        return jSeparator7;
    }

    /**
	 * This method initializes jSeparator8
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator8() {
        if (jSeparator8 == null) {
            jSeparator8 = new JSeparator();
            jSeparator8.setBounds(new Rectangle(590, 83, 10, 126));
            jSeparator8.setOrientation(SwingConstants.VERTICAL);
        }
        return jSeparator8;
    }

    /**
	 * This method initializes jSeparator9
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator9() {
        if (jSeparator9 == null) {
            jSeparator9 = new JSeparator();
            jSeparator9.setBounds(new Rectangle(340, 209, 251, 10));
        }
        return jSeparator9;
    }

    /**
	 * This is the default constructor
	 */
    public ModoExecucao() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 *
	 * @return void
	 */
    public void initialize() {
        this.setSize(790, 470);
        this.setJMenuBar(getJJMenuBar());
        this.setBackground(new Color(173, 200, 226));
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/uesc/computacao/estagio/apresentacao/figuras/IGrafuAF.png")));
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dimension.width - getSize().width) / 2, (dimension.height - getSize().height) / 2);
        this.setContentPane(getJContentPane());
        this.setTitle("IGRAFU");
    }

    /**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
    public JPanel getJContentPane() {
        if (jContentPane == null) {
            jLabelArquivoSequencia = new JLabel();
            jLabelArquivoSequencia.setBounds(new Rectangle(230, 295, 157, 16));
            jLabelArquivoSequencia.setText("Arquivo da Seqüência");
            jLabelModoExecucao = new JLabel();
            jLabelModoExecucao.setText("Modo de execução:");
            jLabelModoExecucao.setSize(new Dimension(126, 16));
            jLabelModoExecucao.setLocation(new Point(393, 76));
            final ImageIcon modoExecucao = new ImageIcon(getClass().getResource("/br/uesc/computacao/estagio/apresentacao/figuras/rna.png"));
            jContentPane = new JPanel() {

                public static final long serialVersionUID = 1L;

                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    modoExecucao.paintIcon(this, g, 0, 0);
                }
            };
            jContentPane.setLayout(null);
            jContentPane.setBackground(new Color(173, 200, 226));
            jContentPane.add(jLabelModoExecucao, null);
            jContentPane.add(getJRadioButtonModoExecucaoManual(), null);
            jContentPane.add(getJRadioButtonModoExecucaoAutomatico(), null);
            jContentPane.add(getJRadioButtonModoExecucaoPerfil(), null);
            jContentPane.add(jLabelArquivoSequencia, null);
            jContentPane.add(getJTextFieldArquivoSequencia(), null);
            jContentPane.add(getJButtonArquivoSequencia(), null);
            jContentPane.add(getJButtonAvancar(), null);
            jContentPane.add(getJButtonVisualizar(), null);
            jContentPane.add(getJSeparator1(), null);
            jContentPane.add(getJSeparator2(), null);
            jContentPane.add(getJSeparator3(), null);
            jContentPane.add(getJSeparator4(), null);
            jContentPane.add(getJSeparator5(), null);
            jContentPane.add(getJSeparator6(), null);
            jContentPane.add(getJSeparator7(), null);
            jContentPane.add(getJSeparator8(), null);
            jContentPane.add(getJSeparator9(), null);
            jContentPane.add(getJSeparator10(), null);
            jContentPane.add(getJSeparator11(), null);
            jContentPane.add(getJSeparator12(), null);
            jContentPane.add(getJSeparator13(), null);
            jContentPane.add(getJSeparator14(), null);
            jContentPane.add(getJSeparator15(), null);
            jContentPane.add(getJSeparator16(), null);
            jContentPane.add(getJSeparator17(), null);
            jContentPane.add(getJSeparator18(), null);
            jContentPane.add(getJProgressBarAutomatico(), null);
        }
        return jContentPane;
    }

    /**
	 * This method initializes jSeparator10
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator10() {
        if (jSeparator10 == null) {
            jSeparator10 = new JSeparator();
            jSeparator10.setOrientation(SwingConstants.VERTICAL);
            jSeparator10.setSize(new Dimension(10, 22));
            jSeparator10.setLocation(new Point(226, 292));
        }
        return jSeparator10;
    }

    /**
	 * This method initializes jSeparator11
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator11() {
        if (jSeparator11 == null) {
            jSeparator11 = new JSeparator();
            jSeparator11.setSize(new Dimension(161, 10));
            jSeparator11.setLocation(new Point(227, 291));
        }
        return jSeparator11;
    }

    /**
	 * This method initializes jSeparator12
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator12() {
        if (jSeparator12 == null) {
            jSeparator12 = new JSeparator();
            jSeparator12.setOrientation(SwingConstants.VERTICAL);
            jSeparator12.setSize(new Dimension(10, 22));
            jSeparator12.setLocation(new Point(387, 292));
        }
        return jSeparator12;
    }

    /**
	 * This method initializes jSeparator13
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator13() {
        if (jSeparator13 == null) {
            jSeparator13 = new JSeparator();
            jSeparator13.setLocation(new Point(227, 314));
            jSeparator13.setSize(new Dimension(161, 10));
        }
        return jSeparator13;
    }

    /**
	 * This method initializes jSeparator14
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator14() {
        if (jSeparator14 == null) {
            jSeparator14 = new JSeparator();
            jSeparator14.setLocation(new Point(320, 265));
            jSeparator14.setOrientation(SwingConstants.VERTICAL);
            jSeparator14.setSize(new Dimension(10, 27));
        }
        return jSeparator14;
    }

    /**
	 * This method initializes jSeparator15
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator15() {
        if (jSeparator15 == null) {
            jSeparator15 = new JSeparator();
            jSeparator15.setSize(new Dimension(294, 10));
            jSeparator15.setLocation(new Point(320, 264));
        }
        return jSeparator15;
    }

    /**
	 * This method initializes jSeparator16
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator16() {
        if (jSeparator16 == null) {
            jSeparator16 = new JSeparator();
            jSeparator16.setBounds(new Rectangle(614, 265, 10, 78));
            jSeparator16.setOrientation(SwingConstants.VERTICAL);
        }
        return jSeparator16;
    }

    /**
	 * This method initializes jSeparator17
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator17() {
        if (jSeparator17 == null) {
            jSeparator17 = new JSeparator();
            jSeparator17.setOrientation(SwingConstants.VERTICAL);
            jSeparator17.setSize(new Dimension(10, 27));
            jSeparator17.setLocation(new Point(320, 314));
        }
        return jSeparator17;
    }

    /**
	 * This method initializes jSeparator18
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator18() {
        if (jSeparator18 == null) {
            jSeparator18 = new JSeparator();
            jSeparator18.setSize(new Dimension(295, 10));
            jSeparator18.setLocation(new Point(320, 341));
        }
        return jSeparator18;
    }

    public JLabel getJLabelArquivoSequencia() {
        return jLabelArquivoSequencia;
    }

    public JLabel getJLabelModoExecucao() {
        return jLabelModoExecucao;
    }

    /**
	 * This method initializes jJMenuBar
	 *
	 * @return javax.swing.JMenuBar
	 */
    public JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getJMenuArquivo());
            jJMenuBar.add(getJMenuConfiguracao());
            jJMenuBar.add(getJMenuAjuda());
        }
        return jJMenuBar;
    }

    /**
	 * This method initializes jMenuArquivo
	 *
	 * @return javax.swing.JMenu
	 */
    public JMenu getJMenuArquivo() {
        if (jMenuArquivo == null) {
            jMenuArquivo = new JMenu();
            jMenuArquivo.setText("Arquivo");
            jMenuArquivo.add(getJMenuItemFechar());
        }
        return jMenuArquivo;
    }

    /**
	 * This method initializes jMenuConfiguracao
	 *
	 * @return javax.swing.JMenu
	 */
    public JMenu getJMenuConfiguracao() {
        if (jMenuConfiguracao == null) {
            jMenuConfiguracao = new JMenu();
            jMenuConfiguracao.setText("Configuração");
            jMenuConfiguracao.add(getJMenuIdioma());
            jMenuConfiguracao.add(getJMenuPerfil());
        }
        return jMenuConfiguracao;
    }

    /**
	 * This method initializes jMenuAjuda
	 *
	 * @return javax.swing.JMenu
	 */
    public JMenu getJMenuAjuda() {
        if (jMenuAjuda == null) {
            jMenuAjuda = new JMenu();
            jMenuAjuda.setText("Ajuda");
            jMenuAjuda.add(getJMenuItemSobre());
        }
        return jMenuAjuda;
    }

    /**
	 * This method initializes jMenuIdioma
	 *
	 * @return javax.swing.JMenu
	 */
    public JMenu getJMenuIdioma() {
        if (jMenuIdioma == null) {
            jMenuIdioma = new JMenu();
            jMenuIdioma.setText("Idioma");
            jMenuIdioma.add(getJCheckBoxMenuItemEnglish());
            jMenuIdioma.add(getJCheckBoxMenuItemPortuguese());
        }
        return jMenuIdioma;
    }

    /**
	 * This method initializes jCheckBoxMenuItemEnglish
	 *
	 * @return javax.swing.JCheckBoxMenuItem
	 */
    public JCheckBoxMenuItem getJCheckBoxMenuItemEnglish() {
        if (jCheckBoxMenuItemEnglish == null) {
            jCheckBoxMenuItemEnglish = new JCheckBoxMenuItem();
            jCheckBoxMenuItemEnglish.setText("Inglês");
        }
        return jCheckBoxMenuItemEnglish;
    }

    /**
	 * This method initializes jCheckBoxMenuItemPortuguese
	 *
	 * @return javax.swing.JCheckBoxMenuItem
	 */
    public JCheckBoxMenuItem getJCheckBoxMenuItemPortuguese() {
        if (jCheckBoxMenuItemPortuguese == null) {
            jCheckBoxMenuItemPortuguese = new JCheckBoxMenuItem();
            jCheckBoxMenuItemPortuguese.setText("Português");
        }
        return jCheckBoxMenuItemPortuguese;
    }

    /**
	 * This method initializes jMenuItemFechar
	 *
	 * @return javax.swing.JMenuItem
	 */
    public JMenuItem getJMenuItemFechar() {
        if (jMenuItemFechar == null) {
            jMenuItemFechar = new JMenuItem();
            jMenuItemFechar.setText("Sair");
            jMenuItemFechar.setIcon(new ImageIcon(getClass().getResource("/br/uesc/computacao/estagio/apresentacao/figuras/fechar.gif")));
        }
        return jMenuItemFechar;
    }

    /**
	 * This method initializes jMenuItemSobre
	 *
	 * @return javax.swing.JMenuItem
	 */
    public JMenuItem getJMenuItemSobre() {
        if (jMenuItemSobre == null) {
            jMenuItemSobre = new JMenuItem();
            jMenuItemSobre.setText("Sobre a IGRAFU");
        }
        return jMenuItemSobre;
    }

    /**
	 * This method initializes jDialogSobre
	 *
	 * @return javax.swing.JDialog
	 */
    public JDialog getJDialogSobre() {
        if (jDialogSobre == null) {
            jDialogSobre = new JDialog(this);
            jDialogSobre.setSize(new Dimension(535, 200));
            jDialogSobre.setTitle("Sobre a IGRAFU");
            jDialogSobre.setResizable(false);
            jDialogSobre.setContentPane(getJContentPane1());
            jDialogSobre.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(WindowEvent evt) {
                    jDialogSobre.removeNotify();
                }
            });
        }
        return jDialogSobre;
    }

    /**
	 * This method initializes jContentPane1
	 *
	 * @return javax.swing.JPanel
	 */
    public JPanel getJContentPane1() {
        if (jContentPane1 == null) {
            jContentPane1 = new JPanel();
            jContentPane1.setLayout(null);
            jContentPane1.setBackground(Color.white);
            jContentPane1.add(getJPanelCamaleao(), null);
            jContentPane1.add(getJTextArea(), null);
        }
        return jContentPane1;
    }

    /**
	 * This method initializes jPanelCamaleao
	 *
	 * @return javax.swing.JPanel
	 */
    public JPanel getJPanelCamaleao() {
        if (jPanelCamaleao == null) {
            final ImageIcon modoExecucao = new ImageIcon(getClass().getResource("/br/uesc/computacao/estagio/apresentacao/figuras/camaleao.png"));
            jPanelCamaleao = new JPanel() {

                public static final long serialVersionUID = 1L;

                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    modoExecucao.paintIcon(this, g, 0, 5);
                }
            };
            jPanelCamaleao.setLayout(null);
            jPanelCamaleao.setLocation(new Point(0, 0));
            jPanelCamaleao.setBackground(Color.white);
            jPanelCamaleao.setSize(new Dimension(143, 143));
        }
        return jPanelCamaleao;
    }

    /**
	 * This method initializes jTextArea
	 *
	 * @return javax.swing.JTextArea
	 */
    public JTextArea getJTextArea() {
        if (jTextArea == null) {
            jTextArea = new JTextArea();
            jTextArea.setText("IGRAFU \nVersão: 2.0\nEste produto inclui os programas:\n   " + "- DIGRAFU (Cristiano Martins)\n   - PHYML\n   - Seqboot\n   " + "- Consense.\n\nAutores: Zilton Junior & Martha Ximena");
            jTextArea.setLocation(new Point(150, 5));
            jTextArea.setFont(new Font("Dialog", Font.BOLD, 14));
            jTextArea.setEditable(false);
            jTextArea.setSize(new Dimension(370, 164));
        }
        return jTextArea;
    }

    /**
	 * This method initializes jDialogModoExecucaoAutomatico
	 *
	 * @return javax.swing.JDialog
	 */
    public JDialog getJDialogModoExecucaoPerfil() {
        if (jDialogModoExecucaoPerfil == null) {
            jDialogModoExecucaoPerfil = new JDialog(this);
            jDialogModoExecucaoPerfil.setSize(new Dimension(297, 202));
            jDialogModoExecucaoPerfil.setTitle("Executar Perfil");
            jDialogModoExecucaoPerfil.setResizable(false);
            jDialogModoExecucaoPerfil.setContentPane(getJContentPane2());
        }
        return jDialogModoExecucaoPerfil;
    }

    /**
	 * This method initializes jContentPane2
	 *
	 * @return javax.swing.JPanel
	 */
    public JPanel getJContentPane2() {
        if (jContentPane2 == null) {
            jLabelPerfil = new JLabel();
            jLabelPerfil.setText("Perfil");
            jLabelPerfil.setLocation(new Point(119, 33));
            jLabelPerfil.setSize(new Dimension(46, 15));
            jContentPane2 = new JPanel();
            jContentPane2.setLayout(null);
            jContentPane2.setBackground(new Color(173, 200, 226));
            jContentPane2.add(jLabelPerfil, null);
            jContentPane2.add(getJSeparator(), null);
            jContentPane2.add(getJSeparator19(), null);
            jContentPane2.add(getJSeparator20(), null);
            jContentPane2.add(getJSeparator191(), null);
            jContentPane2.add(getJSeparator21(), null);
            jContentPane2.add(getJSeparator211(), null);
            jContentPane2.add(getJSeparator22(), null);
            jContentPane2.add(getJSeparator221(), null);
            jContentPane2.add(getJSeparator2111(), null);
            jContentPane2.add(getJButtonExecutarPerfil(), null);
            jContentPane2.add(getJComboBoxExecucaoPerfil(), null);
        }
        return jContentPane2;
    }

    /**
	 * This method initializes jSeparator
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator() {
        if (jSeparator == null) {
            jSeparator = new JSeparator();
            jSeparator.setOrientation(SwingConstants.VERTICAL);
            jSeparator.setSize(new Dimension(10, 20));
            jSeparator.setLocation(new Point(115, 30));
        }
        return jSeparator;
    }

    /**
	 * This method initializes jSeparator19
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator19() {
        if (jSeparator19 == null) {
            jSeparator19 = new JSeparator();
            jSeparator19.setLocation(new Point(115, 29));
            jSeparator19.setSize(new Dimension(51, 10));
        }
        return jSeparator19;
    }

    /**
	 * This method initializes jSeparator20
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator20() {
        if (jSeparator20 == null) {
            jSeparator20 = new JSeparator();
            jSeparator20.setOrientation(SwingConstants.VERTICAL);
            jSeparator20.setSize(new Dimension(10, 20));
            jSeparator20.setLocation(new Point(166, 30));
        }
        return jSeparator20;
    }

    /**
	 * This method initializes jSeparator191
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator191() {
        if (jSeparator191 == null) {
            jSeparator191 = new JSeparator();
            jSeparator191.setBounds(new Rectangle(115, 50, 52, 10));
        }
        return jSeparator191;
    }

    /**
	 * This method initializes jSeparator21
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator21() {
        if (jSeparator21 == null) {
            jSeparator21 = new JSeparator();
            jSeparator21.setBounds(new Rectangle(167, 39, 89, 10));
        }
        return jSeparator21;
    }

    /**
	 * This method initializes jSeparator211
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator211() {
        if (jSeparator211 == null) {
            jSeparator211 = new JSeparator();
            jSeparator211.setSize(new Dimension(88, 10));
            jSeparator211.setLocation(new Point(28, 39));
        }
        return jSeparator211;
    }

    /**
	 * This method initializes jSeparator22
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator22() {
        if (jSeparator22 == null) {
            jSeparator22 = new JSeparator();
            jSeparator22.setOrientation(SwingConstants.VERTICAL);
            jSeparator22.setLocation(new Point(27, 40));
            jSeparator22.setSize(new Dimension(10, 81));
        }
        return jSeparator22;
    }

    /**
	 * This method initializes jSeparator221
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator221() {
        if (jSeparator221 == null) {
            jSeparator221 = new JSeparator();
            jSeparator221.setOrientation(SwingConstants.VERTICAL);
            jSeparator221.setSize(new Dimension(10, 81));
            jSeparator221.setLocation(new Point(256, 40));
        }
        return jSeparator221;
    }

    /**
	 * This method initializes jSeparator2111
	 *
	 * @return javax.swing.JSeparator
	 */
    public JSeparator getJSeparator2111() {
        if (jSeparator2111 == null) {
            jSeparator2111 = new JSeparator();
            jSeparator2111.setBounds(new Rectangle(27, 121, 230, 10));
        }
        return jSeparator2111;
    }

    /**
	 * This method initializes jButtonExecutarAutomatico
	 *
	 * @return javax.swing.JButton
	 */
    public JButton getJButtonExecutarPerfil() {
        if (jButtonExecutarPerfil == null) {
            jButtonExecutarPerfil = new JButton();
            jButtonExecutarPerfil.setText("Executar");
            jButtonExecutarPerfil.setLocation(new Point(92, 136));
            jButtonExecutarPerfil.setSize(new Dimension(103, 22));
        }
        return jButtonExecutarPerfil;
    }

    /**
	 * This method initializes jComboBoxExecucaoAutomatico
	 *
	 * @return javax.swing.JComboBox
	 */
    public JComboBox getJComboBoxExecucaoPerfil() {
        if (jComboBoxExecucaoPerfil == null) {
            jComboBoxExecucaoPerfil = new JComboBox();
            jComboBoxExecucaoPerfil.setBounds(new Rectangle(74, 71, 144, 24));
        }
        return jComboBoxExecucaoPerfil;
    }

    /**
	 * This method initializes jMenuPerfil
	 *
	 * @return javax.swing.JMenu
	 */
    public JMenu getJMenuPerfil() {
        if (jMenuPerfil == null) {
            jMenuPerfil = new JMenu();
            jMenuPerfil.setText("Perfil");
            jMenuPerfil.add(getJMenuItemDeletarPerfil());
        }
        return jMenuPerfil;
    }

    /**
	 * This method initializes jMenuItemDeletarPerfil
	 *
	 * @return javax.swing.JMenuItem
	 */
    public JMenuItem getJMenuItemDeletarPerfil() {
        if (jMenuItemDeletarPerfil == null) {
            jMenuItemDeletarPerfil = new JMenuItem();
            jMenuItemDeletarPerfil.setText("Deletar Perfil");
        }
        return jMenuItemDeletarPerfil;
    }

    /**
	 * This method initializes jProgressBarAutomatico
	 *
	 * @return javax.swing.JProgressBar
	 */
    public JProgressBar getJProgressBarAutomatico() {
        if (jProgressBarAutomatico == null) {
            jProgressBarAutomatico = new JProgressBar();
            jProgressBarAutomatico.setSize(new Dimension(194, 15));
            jProgressBarAutomatico.setLocation(new Point(225, 394));
            jProgressBarAutomatico.setVisible(false);
        }
        return jProgressBarAutomatico;
    }

    public JLabel getJLabelPerfil() {
        return jLabelPerfil;
    }
}
