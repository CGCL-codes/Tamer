package br.uesc.computacao.estagio.aplicacao.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import br.uesc.computacao.estagio.aplicacao.util.Navegar;
import br.uesc.computacao.estagio.aplicacao.util.Tradutor;
import br.uesc.computacao.estagio.apresentacao.GUI.Seqboot;

/**
 * @author Zilton José Maciel Cordeiro Junior - Orientadora: Martha Ximena Torres Delgado
 * @version 2.0
 */
public class ControladorSeqboot implements ActionListener {

    private ButtonGroup buttonGroupConsenseRaiz = null;

    private ButtonGroup buttonGroupIdioma = null;

    public static String ERRO = "Erro";

    public static String ERRO1 = "Erro - Não foi possível abrir a tela anterior!";

    public static String ERRO2 = "Erro - Não foi possível abrir a próxima tela!";

    public static String FECHAR = "Deseja sair do sistema?";

    public static String SAIR = "Sair";

    public static String CANCELAR = "Cancelar";

    public static String guardaNomeSequencia = "";

    public static String trataParametrosSeqboot = "";

    public static String trataParametrosConsense = "";

    public static GeraParametrosSeqboot geraParametrosSeqboot = null;

    public float field = 0;

    public ControladorSeqboot() {
        ControladorIGrafu.seqboot = new Seqboot();
        init();
        ControladorIGrafu.seqboot.setVisible(true);
        System.gc();
        ControladorIGrafu.seqboot.repaint();
        ControladorIGrafu.seqboot.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ControladorIGrafu.seqboot.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                Object[] opcoes = { SAIR, CANCELAR };
                int opcao = JOptionPane.showOptionDialog(null, FECHAR, "IGRAFU 2.0", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[1]);
                if (opcao == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    public void init() {
        if (Tradutor.getLinguage() == Tradutor.getENGLISH()) ControladorIGrafu.seqboot.getJCheckBoxMenuItemIngles().setSelected(true); else ControladorIGrafu.seqboot.getJCheckBoxMenuItemPortugues().setSelected(true);
        if (ControladorIGrafu.modoExecucao != null) {
            if (ControladorIGrafu.modoExecucao.getJTextFieldArquivoSequencia().getText() != "") {
                ControladorIGrafu.seqboot.getJTextFieldArquivoSequencia().setText(ControladorIGrafu.modoExecucao.getJTextFieldArquivoSequencia().getText());
            }
        }
        buttonGroupConsenseRaiz();
        buttonGroupIdioma();
        inicializaCampos();
        ControladorIGrafu.seqboot.getJButtonAvancar().addActionListener(this);
        ControladorIGrafu.seqboot.getJButtonVoltar().addActionListener(this);
        ControladorIGrafu.seqboot.getJComboBoxAvaliacao().addActionListener(this);
        ControladorIGrafu.seqboot.getJButtonArquivoSequencia().addActionListener(this);
        ControladorIGrafu.seqboot.getJButtonCategoriasSitios().addActionListener(this);
        ControladorIGrafu.seqboot.getJButtonPesos().addActionListener(this);
        ControladorIGrafu.seqboot.getJButtonArquivoMisturado().addActionListener(this);
        ControladorIGrafu.seqboot.getJButtonArquivoAncestral().addActionListener(this);
        ControladorIGrafu.seqboot.getJButtonArvore().addActionListener(this);
        ControladorIGrafu.seqboot.getJCheckBoxFracaoTempoUsar().addActionListener(this);
        ControladorIGrafu.seqboot.getJCheckBoxMenuItemIngles().addActionListener(this);
        ControladorIGrafu.seqboot.getJCheckBoxMenuItemPortugues().addActionListener(this);
        ControladorIGrafu.seqboot.getJButtonEditor().addActionListener(this);
        ControladorIGrafu.seqboot.getJMenuItemFechar().addActionListener(this);
        ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().addActionListener(this);
        ControladorIGrafu.seqboot.getJComboBoxModelo().addActionListener(this);
        ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaDados().addActionListener(this);
        ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaPesos().addActionListener(this);
        ControladorIGrafu.seqboot.getJRadioButtonRaizSim().setSelected(true);
    }

    /**
	 * os botoes do campo Raiz da Aba-Consense, somente um pode ficar selecionado
	 */
    public void buttonGroupConsenseRaiz() {
        if (buttonGroupConsenseRaiz == null) {
            buttonGroupConsenseRaiz = new ButtonGroup();
            buttonGroupConsenseRaiz.add(ControladorIGrafu.seqboot.getJRadioButtonRaizSim());
            buttonGroupConsenseRaiz.add(ControladorIGrafu.seqboot.getJRadioButtonRaizNao());
        }
    }

    /**
	 * Os botoes do menu Idioma English e Portuguese, somente um dos dois pode ficar selecionado
	 */
    public void buttonGroupIdioma() {
        if (buttonGroupIdioma == null) {
            buttonGroupIdioma = new ButtonGroup();
            buttonGroupIdioma.add(ControladorIGrafu.seqboot.getJCheckBoxMenuItemIngles());
            buttonGroupIdioma.add(ControladorIGrafu.seqboot.getJCheckBoxMenuItemPortugues());
        } else {
            buttonGroupIdioma.add(ControladorIGrafu.seqboot.getJCheckBoxMenuItemIngles());
            buttonGroupIdioma.add(ControladorIGrafu.seqboot.getJCheckBoxMenuItemPortugues());
        }
    }

    /**
     * Metodo utilizado para retornar se existem mais de um caracter igual no texto
     * Este metodo sera utilizado posteriormente para nao permitir que o usuario digite caracteres invalidos com 1..5 e sim 1.5
     *
     * @return boolean
     */
    public boolean possuiVarios(String str, String igual) {
        int aux = 0;
        if (igual.length() > 1) return false;
        for (int i = 1; i <= str.length(); i++) {
            if (str.substring(i - 1, i).equals(igual)) aux++;
        }
        if (aux > 1) return true; else return false;
    }

    /**
	 * Metodo utilizado para controlar os valores dos campos em todas as abas
	 *
	 */
    private void inicializaCampos() {
        ControladorIGrafu.seqboot.getJNumberFieldBloco().setText("1");
        ControladorIGrafu.seqboot.getJNumberFloatFieldFracaoTempo().setText("0.5");
        ControladorIGrafu.seqboot.getJCheckBoxFracaoTempoUsar().setEnabled(false);
        try {
            field = Float.parseFloat(ControladorIGrafu.seqboot.getJNumberFloatFieldFracaoTempo().getText());
        } catch (NumberFormatException nfe) {
        }
        ControladorIGrafu.seqboot.getJNumberFloatFieldFracaoTempo().addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                if (field > 1 || field < 0.5) ControladorIGrafu.seqboot.getJCheckBoxFracaoTempoUsar().setSelected(false);
                if (field < 0.5 || field > 1) ControladorIGrafu.seqboot.getJCheckBoxFracaoTempoUsar().setSelected(false);
                if (possuiVarios(ControladorIGrafu.seqboot.getJNumberFloatFieldFracaoTempo().getText(), ".")) {
                    ControladorIGrafu.seqboot.getJNumberFloatFieldFracaoTempo().setText("0.5");
                    try {
                        field = Float.parseFloat(ControladorIGrafu.seqboot.getJNumberFloatFieldFracaoTempo().getText());
                    } catch (NumberFormatException nfe) {
                    }
                }
                try {
                    field = Float.parseFloat(ControladorIGrafu.seqboot.getJNumberFloatFieldFracaoTempo().getText());
                } catch (NumberFormatException nfe) {
                }
            }
        });
        ControladorIGrafu.seqboot.getJNumberFieldSemente().setText("3");
        ControladorIGrafu.seqboot.getJNumberFieldSemente().addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                if (((ControladorIGrafu.seqboot.getJNumberFieldSemente().getValue() % 2) == 0)) {
                    if (ControladorIGrafu.seqboot.getJNumberFieldSemente().getValue() != 0) {
                        ControladorIGrafu.seqboot.getJNumberFieldSemente().setText("3");
                    }
                }
            }
        });
        inicializaToolTipeText();
    }

    /**
	 * Metodo que inicializa o toolTipeText para fornecer ajuda ao usuario
	 * Existem Tags html no texto para formatacao deste
	 * Parando o mouse 2 segundos sobre o campo, caso exista ajuda ela é exibida
	 *
	 */
    public void inicializaToolTipeText() {
        ControladorIGrafu.seqboot.getJNumberFieldSemente().setToolTipText("<html>Informe somente valor ímpar.<br><b>- Parâmetro obrigatório -<html>");
        ControladorIGrafu.seqboot.getJButtonArquivoSequencia().setToolTipText("<html>Indicar o diretorio/arquivo para leitura<br>do arquivo de entrada.<br> <b>- Parâmetro obrigatório - </html>");
        ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setToolTipText("<html>Arquivo de informacoes extra sobre as<br>categorias de sitios ou fatores ou alelos.<br> <b>- Parâmetro não obrigatório - </html>");
        ControladorIGrafu.seqboot.getJNumberFieldReplicas().setText("100");
        ControladorIGrafu.seqboot.getJNumberFieldReplicas().setToolTipText("<html><b>- Parâmetro obrigatório -</html>");
        ControladorIGrafu.seqboot.getJTextFieldCategoriasSitios().setToolTipText("<html><b>- Parâmetro não obrigatório -</html>");
        ControladorIGrafu.seqboot.getJCheckBoxEnzimas().setToolTipText("<html>Selecione caso no arquivo de<br>entrada seja informado as enzimas.<br> <b>- Parâmetro não obrigatório -</html>");
        ControladorIGrafu.seqboot.getJNumberFieldBloco().setToolTipText("<html>Informe o tamanho do bloco que divide a seqüência.<br><b>- Parâmetro não obrigatório -</html>");
        ControladorIGrafu.seqboot.getJNumberFieldAmostras().setToolTipText("<html>Informe a porcentagem das amostras.<br><b>- Parâmetro não obrigatório -</html>");
        ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().setToolTipText("<html><b>- Parâmetro obrigatório -</html>");
        ControladorIGrafu.seqboot.getJComboBoxModelo().setToolTipText("<html><b>- Parâmetro obrigatório -</html>");
        ControladorIGrafu.seqboot.getJComboBoxFormato().setToolTipText("<html><b>- Parâmetro obrigatório -</html>");
        ControladorIGrafu.seqboot.getJComboBoxTipoDado().setToolTipText("<html><b>- Parâmetro obrigatório -</html>");
        ControladorIGrafu.seqboot.getJButtonPesos().setToolTipText("<html>Arquivo que contem os pesos do caracter.<br><b>- Parâmetro não obrigatório -</html>");
        ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaPesos().setToolTipText("<html><b>- Parâmetro não obrigatório -</html>");
        ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaDados().setToolTipText("<html><b>- Parâmetro não obrigatório -</html>");
        ControladorIGrafu.seqboot.getJButtonArquivoMisturado().setToolTipText("<html><b>- Parâmetro não obrigatório -</html>");
        ControladorIGrafu.seqboot.getJButtonArquivoAncestral().setToolTipText("<html><b>- Parâmetro não obrigatório -</html>");
        ControladorIGrafu.seqboot.getJComboBoxFormatoSaida().setToolTipText("<html>Formato de saída para rescrever o arquivo.<br><b>- Parâmetro não obrigatório -</html>");
        ControladorIGrafu.seqboot.getJRadioButtonRaizSim().setToolTipText("<html>Tratar a árvore com ou sem raiz.<br><b>- Parâmetro obrigatório -</html>");
        ControladorIGrafu.seqboot.getJComboBoxAvaliacao().setToolTipText("<html><b>- Parâmetro obrigatório -</html>");
        ControladorIGrafu.seqboot.getJNumberFieldMaiorAncestral().setToolTipText("<html>Definir raiz(maior ancestral)<br><b>- Parâmetro nãoobrigatório -</html>");
        ControladorIGrafu.seqboot.getJCheckBoxEspeciesArquivoSaidaIndicar().setToolTipText("<html>Indicar as especies no arquivo saída.<br><b>- Parâmetro nãoobrigatório -</html>");
        ControladorIGrafu.seqboot.getJCheckBoxExecucaoIndicar().setToolTipText("<html><b>- Parâmetro não obrigatório -</html>");
        ControladorIGrafu.seqboot.getJCheckBoxArvoreArquivoSaidaDesenhar().setToolTipText("<html>Desenhar árvore resultado no arquivo saída.<br><b>- Parâmetro não obrigatório -</html>");
        ControladorIGrafu.seqboot.getJButtonArvore().setToolTipText("<html>Gera um arquivo para visualizar<br>a árvore no Hypertree.<br><b>- Parâmetro não obrigatório -</html>");
        ControladorIGrafu.seqboot.getJCheckBoxFracaoTempoUsar().setToolTipText("<html>Fração de tempo somente considerado se<br>a opção Avaliação = ML.<br><b>- Parâmetro não obrigatório -</html>");
        ControladorIGrafu.seqboot.getJNumberFloatFieldFracaoTempo().setToolTipText("<html>Informe somente valores entre [0.5 e 1.0].<br><b>- Parâmetro não obrigatório -</html>");
        ControladorIGrafu.seqboot.getJButtonAvancar().setToolTipText("Avançar para tela Método.");
        ControladorIGrafu.seqboot.getJButtonVoltar().setToolTipText("Voltar para tela Bootstrap.");
        ControladorIGrafu.seqboot.getJButtonEditor().setToolTipText("Abrir o editor de texto.");
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaDados()) {
            if (ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaDados().isSelected()) {
                ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaPesos().setSelected(false);
            }
        }
        if (e.getSource() == ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaPesos()) {
            if (ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaPesos().isSelected()) {
                ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaDados().setSelected(false);
            }
        }
        if (e.getSource() == ControladorIGrafu.seqboot.getJCheckBoxMenuItemIngles() || e.getSource() == ControladorIGrafu.seqboot.getJCheckBoxMenuItemPortugues()) {
            if (ControladorIGrafu.seqboot.getJCheckBoxMenuItemIngles().isSelected()) {
                Tradutor.setLinguage(1);
                traduzir();
            } else {
                Tradutor.setLinguage(0);
                traduzir();
            }
        }
        if (e.getSource() == ControladorIGrafu.seqboot.getJComboBoxAvaliacao()) {
            if (ControladorIGrafu.seqboot.getJComboBoxAvaliacao().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxAvaliacao().getItemAt(3)) {
                ControladorIGrafu.seqboot.getJNumberFloatFieldFracaoTempo().setEnabled(true);
                ControladorIGrafu.seqboot.getJCheckBoxFracaoTempoUsar().setEnabled(true);
            } else {
                ControladorIGrafu.seqboot.getJNumberFloatFieldFracaoTempo().setEnabled(false);
                ControladorIGrafu.seqboot.getJCheckBoxFracaoTempoUsar().setSelected(false);
                ControladorIGrafu.seqboot.getJCheckBoxFracaoTempoUsar().setEnabled(false);
            }
        }
        if (e.getSource() == ControladorIGrafu.seqboot.getJButtonAvancar()) {
            if (GeraParametrosSeqboot.trataAbasSequenciaSeqboot()) {
                if (GeraParametrosSeqboot.trataAbaConsense()) {
                    try {
                        if (ControladorIGrafu.metodos == null) {
                            ControladorIGrafu.seqboot.removeNotify();
                            new ControladorMetodos();
                            ControladorMetodos.traduzir();
                            ControladorIGrafu.metodos.setVisible(false);
                            ControladorIGrafu.metodos.setVisible(true);
                            ControladorIGrafu.metodos.repaint();
                        } else {
                            ControladorIGrafu.seqboot.removeNotify();
                            ControladorMetodos.traduzir();
                            ControladorIGrafu.metodos.setVisible(false);
                            ControladorIGrafu.metodos.setVisible(true);
                            ControladorIGrafu.metodos.repaint();
                        }
                    } catch (NullPointerException nullPointerException) {
                        JOptionPane.showMessageDialog(null, ERRO2, ERRO, JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        if (e.getSource() == ControladorIGrafu.seqboot.getJButtonArquivoSequencia()) {
            if (Navegar.navegar == null) {
                Navegar.navegar = new Navegar();
                Navegar.navegar.abreArquivos();
                if (Navegar.getReturnVal() == JFileChooser.APPROVE_OPTION) ControladorIGrafu.seqboot.getJTextFieldArquivoSequencia().setText(Navegar.file.getAbsolutePath());
            } else {
                Navegar.navegar.abreArquivos();
                if (Navegar.getReturnVal() == JFileChooser.APPROVE_OPTION) ControladorIGrafu.seqboot.getJTextFieldArquivoSequencia().setText(Navegar.file.getAbsolutePath());
            }
            if (Navegar.getReturnVal() == JFileChooser.APPROVE_OPTION) {
                ControladorIGrafu.modoExecucao.getJTextFieldArquivoSequencia().setText(Navegar.file.getAbsolutePath());
                if (ControladorIGrafu.phyml != null) ControladorIGrafu.phyml.getJTextFieldArquivoSequencia().setText(Navegar.file.getAbsolutePath());
                if (ControladorIGrafu.digrafu != null) ControladorIGrafu.digrafu.getJTextFieldArquivoSequencia().setText(Navegar.file.getAbsolutePath());
            }
        }
        if (e.getSource() == ControladorIGrafu.seqboot.getJButtonCategoriasSitios()) {
            if (Navegar.navegar == null) {
                Navegar.navegar = new Navegar();
                Navegar.navegar.abreArquivos();
                if (Navegar.getReturnVal() == JFileChooser.APPROVE_OPTION) ControladorIGrafu.seqboot.getJTextFieldCategoriasSitios().setText(Navegar.file.getAbsolutePath());
            } else {
                Navegar.navegar.abreArquivos();
                if (Navegar.getReturnVal() == JFileChooser.APPROVE_OPTION) ControladorIGrafu.seqboot.getJTextFieldCategoriasSitios().setText(Navegar.file.getAbsolutePath());
            }
        }
        if (e.getSource() == ControladorIGrafu.seqboot.getJButtonPesos()) {
            if (Navegar.navegar == null) {
                Navegar.navegar = new Navegar();
                Navegar.navegar.abreArquivos();
                if (Navegar.getReturnVal() == JFileChooser.APPROVE_OPTION) ControladorIGrafu.seqboot.getJTextFieldPesos().setText(Navegar.file.getAbsolutePath());
            } else {
                Navegar.navegar.abreArquivos();
                if (Navegar.getReturnVal() == JFileChooser.APPROVE_OPTION) ControladorIGrafu.seqboot.getJTextFieldPesos().setText(Navegar.file.getAbsolutePath());
            }
        }
        if (e.getSource() == ControladorIGrafu.seqboot.getJButtonArquivoMisturado()) {
            if (Navegar.navegar == null) {
                Navegar.navegar = new Navegar();
                Navegar.navegar.abreArquivos();
                if (Navegar.getReturnVal() == JFileChooser.APPROVE_OPTION) ControladorIGrafu.seqboot.getJTextFieldArquivoMisturado().setText(Navegar.file.getAbsolutePath());
            } else {
                Navegar.navegar.abreArquivos();
                if (Navegar.getReturnVal() == JFileChooser.APPROVE_OPTION) ControladorIGrafu.seqboot.getJTextFieldArquivoMisturado().setText(Navegar.file.getAbsolutePath());
            }
        }
        if (e.getSource() == ControladorIGrafu.seqboot.getJCheckBoxFracaoTempoUsar()) {
            if (field < 0.5) ControladorIGrafu.seqboot.getJNumberFloatFieldFracaoTempo().setText("0.5"); else if (field > 1) ControladorIGrafu.seqboot.getJNumberFloatFieldFracaoTempo().setText("0.5");
        }
        if (e.getSource() == ControladorIGrafu.seqboot.getJButtonArquivoAncestral()) {
            if (Navegar.navegar == null) {
                Navegar.navegar = new Navegar();
                Navegar.navegar.abreArquivos();
                if (Navegar.getReturnVal() == JFileChooser.APPROVE_OPTION) ControladorIGrafu.seqboot.getJTextFieldArquivoAncestral().setText(Navegar.file.getAbsolutePath());
            } else {
                Navegar.navegar.abreArquivos();
                if (Navegar.getReturnVal() == JFileChooser.APPROVE_OPTION) ControladorIGrafu.seqboot.getJTextFieldArquivoAncestral().setText(Navegar.file.getAbsolutePath());
            }
        }
        if (e.getSource() == ControladorIGrafu.seqboot.getJButtonArvore()) {
            if (Navegar.navegar == null) {
                Navegar.navegar = new Navegar();
                Navegar.navegar.abreArquivos();
                if (Navegar.getReturnVal() == JFileChooser.APPROVE_OPTION) ControladorIGrafu.seqboot.getJTextFieldArvore().setText(Navegar.file.getAbsolutePath());
            } else {
                Navegar.navegar.abreArquivos();
                if (Navegar.getReturnVal() == JFileChooser.APPROVE_OPTION) ControladorIGrafu.seqboot.getJTextFieldArvore().setText(Navegar.file.getAbsolutePath());
            }
        } else if (e.getSource() == ControladorIGrafu.seqboot.getJButtonVoltar()) {
            try {
                if (ControladorIGrafu.modoManualBootstrap == null) {
                    ControladorIGrafu.seqboot.removeNotify();
                    new ControladorModoManualBootstrap();
                    ControladorModoManualBootstrap.traduzir();
                    ControladorIGrafu.modoManualBootstrap.setVisible(false);
                    ControladorIGrafu.modoManualBootstrap.setVisible(true);
                    ControladorIGrafu.modoManualBootstrap.repaint();
                } else {
                    ControladorIGrafu.seqboot.removeNotify();
                    ControladorModoManualBootstrap.traduzir();
                    ControladorIGrafu.modoManualBootstrap.setVisible(false);
                    ControladorIGrafu.modoManualBootstrap.setVisible(true);
                    ControladorIGrafu.modoManualBootstrap.repaint();
                }
            } catch (NullPointerException nullPointerException) {
                JOptionPane.showMessageDialog(null, ERRO1, ERRO, JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == ControladorIGrafu.seqboot.getJButtonEditor()) {
            new ControladorEditor();
            ControladorEditor.traduzir();
        }
        if (e.getSource() == ControladorIGrafu.seqboot.getJMenuItemFechar()) {
            Object[] opcoes = { SAIR, CANCELAR };
            int opcao = JOptionPane.showOptionDialog(null, FECHAR, "IGRAFU 2.0", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[1]);
            if (opcao == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
        if (e.getSource() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia()) {
            if (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(0)) {
                ControladorIGrafu.seqboot.getJButtonArquivoAncestral().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonArquivoMisturado().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(true);
                ControladorIGrafu.seqboot.getJCheckBoxEnzimas().setEnabled(false);
            } else if (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(1)) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonArquivoAncestral().setEnabled(true);
                ControladorIGrafu.seqboot.getJButtonArquivoMisturado().setEnabled(true);
                ControladorIGrafu.seqboot.getJCheckBoxEnzimas().setEnabled(false);
            } else if (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(2)) {
                ControladorIGrafu.seqboot.getJCheckBoxEnzimas().setEnabled(true);
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonArquivoAncestral().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonArquivoMisturado().setEnabled(false);
            } else if (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(3)) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonArquivoAncestral().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonArquivoMisturado().setEnabled(false);
                ControladorIGrafu.seqboot.getJCheckBoxEnzimas().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(1)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(1))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(2)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(1))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(3)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(1))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonArquivoAncestral().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonArquivoMisturado().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(4)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(1))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonArquivoAncestral().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonArquivoMisturado().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(0)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(2))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(1)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(2))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(2)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(2))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(0)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(3))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(1)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(3))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
            }
        }
        if (e.getSource() == ControladorIGrafu.seqboot.getJComboBoxModelo()) {
            if (ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(0)) {
                ControladorIGrafu.seqboot.getJNumberFieldBloco().setEnabled(true);
                ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaDados().setEnabled(true);
                ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaPesos().setEnabled(true);
                ControladorIGrafu.seqboot.getJButtonPesos().setEnabled(true);
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(true);
                ControladorIGrafu.seqboot.getJNumberFieldAmostras().setEnabled(true);
            } else if (ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(1)) {
                ControladorIGrafu.seqboot.getJNumberFieldBloco().setEnabled(false);
                ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaDados().setEnabled(true);
                ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaPesos().setEnabled(true);
                ControladorIGrafu.seqboot.getJButtonPesos().setEnabled(true);
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(true);
                ControladorIGrafu.seqboot.getJNumberFieldAmostras().setEnabled(true);
            } else if (ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(2)) {
                ControladorIGrafu.seqboot.getJNumberFieldBloco().setEnabled(false);
                ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaDados().setEnabled(false);
                ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaPesos().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonPesos().setEnabled(true);
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(true);
                ControladorIGrafu.seqboot.getJNumberFieldAmostras().setEnabled(false);
            } else if (ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(3)) {
                ControladorIGrafu.seqboot.getJNumberFieldBloco().setEnabled(false);
                ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaDados().setEnabled(false);
                ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaPesos().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonPesos().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
                ControladorIGrafu.seqboot.getJNumberFieldAmostras().setEnabled(false);
            } else if (ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(4)) {
                ControladorIGrafu.seqboot.getJNumberFieldBloco().setEnabled(false);
                ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaDados().setEnabled(false);
                ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaPesos().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonPesos().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
                ControladorIGrafu.seqboot.getJNumberFieldAmostras().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(1)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(1))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(2)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(1))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(3)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(1))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonArquivoAncestral().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonArquivoMisturado().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(4)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(1))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonArquivoAncestral().setEnabled(false);
                ControladorIGrafu.seqboot.getJButtonArquivoMisturado().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(0)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(2))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(1)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(2))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(2)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(2))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(0)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(3))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(1)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(3))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
            }
            if ((ControladorIGrafu.seqboot.getJComboBoxModelo().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxModelo().getItemAt(2)) && (ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getSelectedItem() == ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().getItemAt(3))) {
                ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setEnabled(false);
            }
        }
    }

    public static void traduzir() {
        ControladorIGrafu.seqboot.getJButtonAvancar().setText(Tradutor.traduzir("Avançar", "Advance"));
        ControladorIGrafu.seqboot.getJButtonVoltar().setText(Tradutor.traduzir("Voltar", "Back"));
        ControladorIGrafu.seqboot.getJButtonEditor().setText(Tradutor.traduzir("Editor", "Edition"));
        FECHAR = Tradutor.traduzir("Deseja sair do sistema?", "Do you want to leave the system?");
        SAIR = Tradutor.traduzir("Sair", "Exit");
        CANCELAR = Tradutor.traduzir("Cancelar", "Cancel");
        ERRO = Tradutor.traduzir("Erro", "Error");
        ERRO1 = Tradutor.traduzir("Não foi possível abrir a tela anterior!", "Was not possible open the previous screen!");
        ERRO2 = Tradutor.traduzir("Não foi possível abrir a próxima tela!", "Was not possible open the next screen!");
        ControladorIGrafu.seqboot.getJLabelArquivoSequencia().setText(Tradutor.traduzir("Seqüência", "Sequence"));
        ControladorIGrafu.seqboot.getJLabelCategoriaSitios().setText(Tradutor.traduzir("Categorias de Sítios", "Categories of sites"));
        ControladorIGrafu.seqboot.getJLabelTipoDado().setText(Tradutor.traduzir("Tipo de dado", "Data type"));
        ControladorIGrafu.seqboot.getJLabelTipoSequencia().setText(Tradutor.traduzir("Tipo de Seqüência", "Sequence type"));
        ControladorIGrafu.seqboot.getJLabelModelo().setText(Tradutor.traduzir("Modelo", "Model"));
        ControladorIGrafu.seqboot.getJLabelFormato().setText(Tradutor.traduzir("Formato", "Format"));
        ControladorIGrafu.seqboot.getJLabelSemente().setText(Tradutor.traduzir("Semente", "Seed"));
        ControladorIGrafu.seqboot.getJLabelReplicas().setText(Tradutor.traduzir("Replicas", "Replicates"));
        ControladorIGrafu.seqboot.getJCheckBoxEnzimas().setText(Tradutor.traduzir("Enzimas", "Enzymes"));
        ControladorIGrafu.seqboot.getJLabelBloco().setText(Tradutor.traduzir("Bloco", "Block"));
        ControladorIGrafu.seqboot.getJLabelAmostras().setText(Tradutor.traduzir("Amostras", "Samples"));
        ControladorIGrafu.seqboot.getJLabelPesos().setText(Tradutor.traduzir("Pesos", "Weights"));
        ControladorIGrafu.seqboot.getJLabelArquivoMisturado().setText(Tradutor.traduzir("Arquivo Misturado", "Mixture file"));
        ControladorIGrafu.seqboot.getJLabelFormatoSaida().setText(Tradutor.traduzir("Formato de saída", "Exit format"));
        ControladorIGrafu.seqboot.getJLabelArquivoAncestral().setText(Tradutor.traduzir("Arquivo Ancestral", "Ancestral file"));
        ControladorIGrafu.seqboot.getJLabelMultiplosArquivosSaida().setText(Tradutor.traduzir("Múltiplos arquivos de saída", "Multiples exit files"));
        ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaDados().setText(Tradutor.traduzir("Dados", "Data"));
        ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaPesos().setText(Tradutor.traduzir("Pesos", "Weights"));
        ControladorIGrafu.seqboot.getJCheckBoxExecucaoIndicar().setText(Tradutor.traduzir("Indicar", "Indicate"));
        ControladorIGrafu.seqboot.getJLabelArvore().setText(Tradutor.traduzir("Árvore", "Tree"));
        ControladorIGrafu.seqboot.getJLabelRaiz().setText(Tradutor.traduzir("Raíz", "Root"));
        ControladorIGrafu.seqboot.getJLabelAvaliacao().setText(Tradutor.traduzir("Avaliação", "Evaluation"));
        ControladorIGrafu.seqboot.getJLabelMaiorAncestral().setText(Tradutor.traduzir("Maior Ancestral", "Ancestral greater"));
        ControladorIGrafu.seqboot.getJLabelEspeciesArquivoSaida().setText(Tradutor.traduzir("Espécie no arquivo de saída", "Species in exit file"));
        ControladorIGrafu.seqboot.getJLabelExecucao().setText(Tradutor.traduzir("Execução", "Execution"));
        ControladorIGrafu.seqboot.getJLabelArvoreArquivoSaida().setText(Tradutor.traduzir("Árvore no arquivo de saída", "Tree in exit file"));
        ControladorIGrafu.seqboot.getJLabelFracaoTempo().setText(Tradutor.traduzir("Fração de tempo", "Time Fraction"));
        ControladorIGrafu.seqboot.getJRadioButtonRaizNao().setText(Tradutor.traduzir("Não", "No"));
        ControladorIGrafu.seqboot.getJRadioButtonRaizSim().setText(Tradutor.traduzir("Sim", "yes"));
        ControladorIGrafu.seqboot.getJCheckBoxEspeciesArquivoSaidaIndicar().setText(Tradutor.traduzir("Indicar", "Indicate"));
        ControladorIGrafu.seqboot.getJCheckBoxExecucaoIndicar().setText(Tradutor.traduzir("Indicar", "Indicate"));
        ControladorIGrafu.seqboot.getJCheckBoxArvoreArquivoSaidaDesenhar().setText(Tradutor.traduzir("Desenhar", "Draw"));
        ControladorIGrafu.seqboot.getJCheckBoxFracaoTempoUsar().setText(Tradutor.traduzir("Usar", "Use"));
        ControladorIGrafu.seqboot.getJMenuArquivo().setText(Tradutor.traduzir("Arquivo", "File"));
        ControladorIGrafu.seqboot.getJMenuConfiguracoes().setText(Tradutor.traduzir("Configurações", "Configurations"));
        ControladorIGrafu.seqboot.getJMenuIdiomas().setText(Tradutor.traduzir("Idiomas", "Languages"));
        ControladorIGrafu.seqboot.getJCheckBoxMenuItemIngles().setText(Tradutor.traduzir("Inglês", "English"));
        ControladorIGrafu.seqboot.getJCheckBoxMenuItemPortugues().setText(Tradutor.traduzir("Português", "Portuguese"));
        ControladorIGrafu.seqboot.getJMenuItemFechar().setText(Tradutor.traduzir("Sair", "Exit"));
        ControladorIGrafu.seqboot.getJTabbedPaneSeqboot().setTitleAt(0, Tradutor.traduzir("Seqüência", "Sequence"));
        ControladorIGrafu.seqboot.getJTabbedPaneSeqboot().setTitleAt(1, Tradutor.traduzir("Parâmetros", "Parameters"));
        if (Tradutor.getLinguage() == Tradutor.getENGLISH()) {
            ControladorIGrafu.seqboot.getJComboBoxFormato().removeItemAt(0);
            ControladorIGrafu.seqboot.getJComboBoxFormato().insertItemAt("Sequential", 0);
            ControladorIGrafu.seqboot.getJComboBoxFormato().removeItemAt(1);
            ControladorIGrafu.seqboot.getJComboBoxFormato().insertItemAt("Interleaved", 1);
            ControladorIGrafu.seqboot.getJComboBoxTipoDado().removeItemAt(2);
            ControladorIGrafu.seqboot.getJComboBoxTipoDado().insertItemAt("Protein", 2);
            ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().removeItemAt(0);
            ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().insertItemAt("Molecular sequence", 0);
            ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().removeItemAt(1);
            ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().insertItemAt("Discrete Morphology", 1);
            ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().removeItemAt(2);
            ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().insertItemAt("Restriction Sites", 2);
            ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().removeItemAt(3);
            ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().insertItemAt("Gene Frequencies", 3);
        } else if (Tradutor.getLinguage() == Tradutor.getPORTUGUESE()) {
            ControladorIGrafu.seqboot.getJComboBoxFormato().removeItemAt(0);
            ControladorIGrafu.seqboot.getJComboBoxFormato().insertItemAt("Seqüencial", 0);
            ControladorIGrafu.seqboot.getJComboBoxFormato().removeItemAt(1);
            ControladorIGrafu.seqboot.getJComboBoxFormato().insertItemAt("Intercalado", 1);
            ControladorIGrafu.seqboot.getJComboBoxTipoDado().removeItemAt(2);
            ControladorIGrafu.seqboot.getJComboBoxTipoDado().insertItemAt("Proteína", 2);
            ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().removeItemAt(0);
            ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().insertItemAt("Seqüência Molecular", 0);
            ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().removeItemAt(1);
            ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().insertItemAt("Morfologia Discreta", 1);
            ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().removeItemAt(2);
            ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().insertItemAt("Restricão de Sítios", 2);
            ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().removeItemAt(3);
            ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().insertItemAt("Freqüência de Genes", 3);
        }
        traduzirToolTipeText();
    }

    /**
	 * Metodo que traduz o toolTipeText para fornecer ajuda ao usuario
	 * Existem Tags html no texto para formatacao deste
	 * Parando o mouse 2 segundos sobre o campo, caso exista ajuda ela é exibida
	 *
	 */
    public static void traduzirToolTipeText() {
        ControladorIGrafu.seqboot.getJNumberFieldSemente().setToolTipText(Tradutor.traduzir("<html>Informe somente valor ímpar.<br><b>- Parâmetro obrigatório -<html>", "<html>Only inform uneven value.<br> <b>- Obligator parameter -<html>"));
        ControladorIGrafu.seqboot.getJButtonArquivoSequencia().setToolTipText(Tradutor.traduzir("<html>Indicar o diretorio/arquivo para leitura<br>do arquivo de entrada.<br><b>- Parâmetro obrigatório - </html>", "<html>Indicate the directory/file for reading<br>of the input file.<br> <b>- Obligator parameter - </html>"));
        ControladorIGrafu.seqboot.getJButtonCategoriasSitios().setToolTipText(Tradutor.traduzir("<html>Arquivo de informações extra sobre as<br>categorias de sítios ou fatores ou alelos.<br> <b>- Parâmetro não obrigatório - </html>", "<html>Extra file of information about categories<br>of sites or factors or alelos.<br> <b>- Obligator parameter - </html>"));
        ControladorIGrafu.seqboot.getJNumberFieldReplicas().setToolTipText(Tradutor.traduzir("<html><b>- Parâmetro obrigatório -</html>", "<html><b>- Obligator parameter"));
        ControladorIGrafu.seqboot.getJTextFieldCategoriasSitios().setToolTipText(Tradutor.traduzir("<html><b>- Parâmetro não obrigatório -</html>", "<html><b>- Not obligator parameter"));
        ControladorIGrafu.seqboot.getJCheckBoxEnzimas().setToolTipText(Tradutor.traduzir("<html>Selecione caso no arquivo de<br>entrada seja informado as enzimas.<br> <b>- Parâmetro não obrigatório -</html>", "<html>Select case in the input file<br>is informed enzymes.<br> <b>- Not obligator parameter -</html>"));
        ControladorIGrafu.seqboot.getJNumberFieldBloco().setToolTipText(Tradutor.traduzir("<html>Informe o tamanho do bloco que divide a seqüência.<br><b>- Parâmetro não obrigatório -</html>", "<html>Inform the size of the block that divide the sequence.<br><b>- Not obligator parameter -</html>"));
        ControladorIGrafu.seqboot.getJNumberFieldAmostras().setToolTipText(Tradutor.traduzir("<html>Informe a porcentagem das amostras.<br><b>- Parâmetro não obrigatório -</html>", "<html>Inform the percentage of the samples.<br><b>- Not obligator parameter -</html>"));
        ControladorIGrafu.seqboot.getJComboBoxTipoSequencia().setToolTipText(Tradutor.traduzir("<html><b>- Parâmetro obrigatório -</html>", "<html><b>- Obligator parameter"));
        ControladorIGrafu.seqboot.getJComboBoxModelo().setToolTipText(Tradutor.traduzir("<html><b>- Parâmetro obrigatório -</html>", "<html><b>- Obligator parameter"));
        ControladorIGrafu.seqboot.getJComboBoxFormato().setToolTipText(Tradutor.traduzir("<html><b>- Parâmetro obrigatório -</html>", "<html><b>- Obligator parameter"));
        ControladorIGrafu.seqboot.getJComboBoxTipoDado().setToolTipText(Tradutor.traduzir("<html><b>- Parâmetro obrigatório -</html>", "<html><b>- Obligator parameter"));
        ControladorIGrafu.seqboot.getJButtonPesos().setToolTipText(Tradutor.traduzir("<html>Arquivo que contém os pesos do caracter.<br><b>- Parâmetro não obrigatório -</html>", "<html>File that contains the weights of caracter.<br><b>- Not obligator parameter -</html>"));
        ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaPesos().setToolTipText(Tradutor.traduzir("<html><b>- Parâmetro não obrigatório -</html>", "<html><b>- Not obligator parameter"));
        ControladorIGrafu.seqboot.getJCheckBoxMultiplosArquivosSaidaDados().setToolTipText(Tradutor.traduzir("<html><b>- Parâmetro não obrigatório -</html>", "<html><b>- Not obligator parameter"));
        ControladorIGrafu.seqboot.getJButtonArquivoMisturado().setToolTipText(Tradutor.traduzir("<html><b>- Parâmetro não obrigatório -</html>", "<html><b>- Not obligator parameter"));
        ControladorIGrafu.seqboot.getJButtonArquivoAncestral().setToolTipText(Tradutor.traduzir("<html><b>- Parâmetro não obrigatório -</html>", "<html><b>- Not obligator parameter"));
        ControladorIGrafu.seqboot.getJComboBoxFormatoSaida().setToolTipText(Tradutor.traduzir("<html>Formato de saída para rescrever o arquivo.<br><b>- Parâmetro não obrigatório -</html>", "<html>Format of exit to rewrite the file.<br><b>- Not obligator parameter -</html>"));
        ControladorIGrafu.seqboot.getJRadioButtonRaizSim().setToolTipText(Tradutor.traduzir("<html>Tratar a árvore com ou sem raiz.<br><b>- Parâmetro obrigatório -</html>", "<html>Treat the tree with or without root.<br><b>- Obligator parameter -</html>"));
        ControladorIGrafu.seqboot.getJComboBoxAvaliacao().setToolTipText(Tradutor.traduzir("<html><b>- Parâmetro obrigatório -</html>", "<html><b>- Obligator parameter"));
        ControladorIGrafu.seqboot.getJNumberFieldMaiorAncestral().setToolTipText(Tradutor.traduzir("<html>Definir raiz (maior ancestral)<br><b>- Parâmetro não obrigatório -</html>", "<html>Define root (bigger ancestral)<br><b>- Not obligator parameter -</html>"));
        ControladorIGrafu.seqboot.getJCheckBoxEspeciesArquivoSaidaIndicar().setToolTipText(Tradutor.traduzir("<html>Indicar as espécies no arquivo saída.<br><b>- Parâmetro não obrigatório -</html>", "<html>Indicate the species in the exit file.<br><b>- Not obligator parameter -</html>"));
        ControladorIGrafu.seqboot.getJCheckBoxExecucaoIndicar().setToolTipText(Tradutor.traduzir("<html><b>- Parâmetro não obrigatório -</html>", "<html><b>- Not obligator parameter"));
        ControladorIGrafu.seqboot.getJCheckBoxArvoreArquivoSaidaDesenhar().setToolTipText(Tradutor.traduzir("<html>Desenhar árvore resultado no arquivo saída.<br><b>- Parâmetro não obrigatório -</html>", "<html>Draw tree resulted in the exit file.<br><b>- Not obligator parameter -</html>"));
        ControladorIGrafu.seqboot.getJButtonArvore().setToolTipText(Tradutor.traduzir("<html>Gera um arquivo para visualizar<br>a árvore no Hypertree.<br><b>- Parâmetro não obrigatório -</html>", "<html>Generates an file to visualize<br>the tree in the Hypertree.<br><b>- Not obligator parameter -</html>"));
        ControladorIGrafu.seqboot.getJCheckBoxFracaoTempoUsar().setToolTipText(Tradutor.traduzir("<html>Fração de tempo somente considerado se<br>a opção Avaliação = ML.<br><b>- Parâmetro não obrigatório -</html>", "<html>Fraction of time only considered if<br>the option Evaluation = ML.<br><b>- Not obligator parameter -</html>"));
        ControladorIGrafu.seqboot.getJNumberFloatFieldFracaoTempo().setToolTipText(Tradutor.traduzir("<html>Informe somente valores entre [0.5 e 1.0].<br><b>- Parâmetro não obrigatório -</html>", "<html>only inform values between [0.5 e 1.0].<br><b>- Not obligator parameter -</html>"));
        ControladorIGrafu.seqboot.getJButtonAvancar().setToolTipText(Tradutor.traduzir("Avançar para tela Métodos.", "Advance for screen Methods."));
        ControladorIGrafu.seqboot.getJButtonVoltar().setToolTipText(Tradutor.traduzir("Voltar para tela Bootstrap.", "Back toward screen Bootstrap."));
        ControladorIGrafu.seqboot.getJButtonEditor().setToolTipText(Tradutor.traduzir("Abrir o Editor.", "Open the Editor."));
    }
}
