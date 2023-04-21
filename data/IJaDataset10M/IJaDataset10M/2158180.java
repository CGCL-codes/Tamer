package br.uesc.computacao.estagio.aplicacao.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import br.uesc.computacao.estagio.aplicacao.util.Tradutor;
import br.uesc.computacao.estagio.apresentacao.GUI.Programas;

/**
 * Classe que controla a tela Programas
 * @author Zilton José Maciel Cordeiro Junior - Orientadora: Martha Ximena Torres Delgado
 * @version 2.0
 */
public class ControladorProgramas implements ActionListener {

    static String ERRO = "Erro";

    public static String ERRO1 = "Erro - Não foi possível abrir a tela anterior!";

    public static String ERRO2 = "Erro - Não foi possível abrir a próxima tela!";

    public static String FECHAR = "Deseja sair do sistema?";

    public static String SAIR = "Sair";

    public static String CANCELAR = "Cancelar";

    public static String PROGRAMA_UTILIZAR = "Informe o programa a ser Utilizado!";

    public static String PROGRAMAS = "Programas";

    public ControladorProgramas() {
        ControladorIGrafu.programas = new Programas();
        ControladorIGrafu.programas.setResizable(false);
        ControladorIGrafu.programas.setVisible(true);
        ControladorIGrafu.programas.repaint();
        ControladorIGrafu.programas.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ControladorIGrafu.programas.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                Object[] opcoes = { SAIR, CANCELAR };
                int opcao = JOptionPane.showOptionDialog(null, FECHAR, "IGRAFU 2.0", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[1]);
                if (opcao == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        ControladorIGrafu.programas.getJButtonAvancar().addActionListener(this);
        ControladorIGrafu.programas.getJButtonVoltar().addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ControladorIGrafu.programas.getJButtonAvancar()) {
            try {
                if (ControladorIGrafu.programas.getJRadioButtonPHYML().isSelected() || ControladorIGrafu.programas.getJRadioButtonDIGRAFU().isSelected() || ControladorIGrafu.programas.getJRadioButtonMrBayes().isSelected() || ControladorIGrafu.programas.getJRadioButtonParcimonia().isSelected()) {
                    if (ControladorIGrafu.programas.getJRadioButtonPHYML().isSelected()) {
                        if (ControladorIGrafu.phyml == null) {
                            ControladorIGrafu.programas.removeNotify();
                            new ControladorPHYML();
                            ControladorPHYML.traduzir();
                            ControladorPHYML.bootstrap();
                            if (Tradutor.getLinguage() == Tradutor.getENGLISH()) ControladorIGrafu.phyml.getJCheckBoxMenuItemIngles().setSelected(true); else ControladorIGrafu.phyml.getJCheckBoxMenuItemPortugues().setSelected(true);
                            ControladorIGrafu.phyml.setVisible(false);
                            ControladorIGrafu.phyml.setVisible(true);
                            ControladorIGrafu.phyml.repaint();
                        } else {
                            ControladorIGrafu.programas.removeNotify();
                            ControladorPHYML.traduzir();
                            ControladorPHYML.bootstrap();
                            if (Tradutor.getLinguage() == Tradutor.getENGLISH()) ControladorIGrafu.phyml.getJCheckBoxMenuItemIngles().setSelected(true); else ControladorIGrafu.phyml.getJCheckBoxMenuItemPortugues().setSelected(true);
                            ControladorIGrafu.phyml.setVisible(false);
                            ControladorIGrafu.phyml.setVisible(true);
                            ControladorIGrafu.phyml.repaint();
                        }
                    }
                    if (ControladorIGrafu.programas.getJRadioButtonDIGRAFU().isSelected()) {
                        if (ControladorIGrafu.digrafu == null) {
                            ControladorIGrafu.programas.removeNotify();
                            new ControladorDIGRAFU();
                            ControladorDIGRAFU.traduzir();
                            if (ControladorModoManualBootstrap.bootstrap) ControladorIGrafu.digrafu.getJButtonArquivoSequencia().setEnabled(false); else ControladorIGrafu.digrafu.getJButtonArquivoSequencia().setEnabled(true);
                            if (Tradutor.getLinguage() == Tradutor.getENGLISH()) ControladorIGrafu.digrafu.getJCheckBoxMenuItemIngles().setSelected(true); else ControladorIGrafu.digrafu.getJCheckBoxMenuItemPortugues().setSelected(true);
                            ControladorIGrafu.digrafu.setVisible(false);
                            ControladorIGrafu.digrafu.setVisible(true);
                            ControladorIGrafu.digrafu.repaint();
                        } else {
                            ControladorIGrafu.programas.removeNotify();
                            ControladorDIGRAFU.traduzir();
                            if (ControladorModoManualBootstrap.bootstrap) ControladorIGrafu.digrafu.getJButtonArquivoSequencia().setEnabled(false); else ControladorIGrafu.digrafu.getJButtonArquivoSequencia().setEnabled(true);
                            if (Tradutor.getLinguage() == Tradutor.getENGLISH()) ControladorIGrafu.digrafu.getJCheckBoxMenuItemIngles().setSelected(true); else ControladorIGrafu.digrafu.getJCheckBoxMenuItemPortugues().setSelected(true);
                            ControladorIGrafu.digrafu.setVisible(false);
                            ControladorIGrafu.digrafu.setVisible(true);
                            ControladorIGrafu.digrafu.repaint();
                        }
                    }
                } else JOptionPane.showMessageDialog(null, PROGRAMA_UTILIZAR, PROGRAMAS, JOptionPane.INFORMATION_MESSAGE);
            } catch (NullPointerException nullPointerException) {
                JOptionPane.showMessageDialog(null, ERRO2, ERRO, JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == ControladorIGrafu.programas.getJButtonVoltar()) {
            try {
                if (ControladorIGrafu.metodos == null) {
                    ControladorIGrafu.programas.removeNotify();
                    new ControladorMetodos();
                    ControladorMetodos.traduzir();
                    ControladorIGrafu.metodos.setVisible(false);
                    ControladorIGrafu.metodos.setVisible(true);
                    ControladorIGrafu.metodos.repaint();
                } else {
                    ControladorIGrafu.programas.removeNotify();
                    ControladorMetodos.traduzir();
                    ControladorIGrafu.metodos.setVisible(false);
                    ControladorIGrafu.metodos.setVisible(true);
                    ControladorIGrafu.metodos.repaint();
                }
            } catch (NullPointerException nullPointerException) {
                JOptionPane.showMessageDialog(null, ERRO1, ERRO, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void traduzir() {
        ControladorIGrafu.programas.getJLabelProgramas().setText(Tradutor.traduzir("Informe o programa a ser utilizado:", "Inform the program to be used:"));
        ControladorIGrafu.programas.getJButtonAvancar().setText(Tradutor.traduzir("Avançar", "Advance"));
        ControladorIGrafu.programas.getJButtonVoltar().setText(Tradutor.traduzir("Voltar", "Back"));
        ControladorIGrafu.programas.getJRadioButtonParcimonia().setText(Tradutor.traduzir("Parcimônia", "Parsimony"));
        ControladorIGrafu.programas.setTitle(Tradutor.traduzir("IGrafu: Programas", "IGrafu: Programs"));
        PROGRAMAS = Tradutor.traduzir("Programas", "Programs");
        PROGRAMA_UTILIZAR = Tradutor.traduzir("Informe o programa a ser Utilizado!", "Inform the program To be used!");
        FECHAR = Tradutor.traduzir("Deseja sair do sistema?", "Do you want to leave the system?");
        SAIR = Tradutor.traduzir("Sair", "Exit");
        CANCELAR = Tradutor.traduzir("Cancelar", "Cancel");
        ERRO = Tradutor.traduzir("Erro", "Error");
        ERRO1 = Tradutor.traduzir("Não foi possível abrir a tela anterior!", "Was not possible open the previous screen!");
        ERRO2 = Tradutor.traduzir("Não foi possível abrir a próxima tela!", "Was not possible open the next screen!");
    }
}
