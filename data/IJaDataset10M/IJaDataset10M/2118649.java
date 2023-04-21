package interfaceGrafica;

import negocio.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * Esta classe cria um molde de janela, a ser ulilizada para construcao de
 * tabelas verdades para Logica Paraconsistente de da Costa, desta forma apenas os
 * caracteres '1', '2' e '3' serao aceitos como entrada na caixa de texto, uma
 * vez que nao existe outros caractes possives na solucao do exercicio.
 *
 * @author Gustavo Henrique Lopes Machado
 **/
public class LogicaParaconsistente extends JanelaBaseExercicios implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final String TIPO = "Lógica Paraconsistente - Tabela Valor-Verdade";

    private JLabel titulo, enunciado1, enunciado2;

    private JPanel pCentral, pOeste, pSuperior, pTitulo, pAux, pEnun, pEnun2, pColuna;

    private String resposta[][];

    private JTextField preencher[][];

    /** 
    * Construtor responsavel por criar a janela para tabela verdade, os seus
    * argumentos especificam o conteudo da janela conforme eh explicitado abaixo
    * @param tipoDeExercicio - Especifica o titulo da janela
    * @param ordem           - String que contem o enunciado do exercicio.
    * @param valores[][]     - Matriz que contem os valores da tabela.
    * @param preposicao      - String que contem a preposicao cujos valores
    *                        - verdade deverao ser preenchidos                
    **/
    public LogicaParaconsistente(String tipoDeExercicio, String ordem, String ordem2, String[][] valores, String preposicao) {
        super(Usuario.statusDoExercicio(InterfaceDados.getTipoDoExercicioCorrente(), InterfaceDados.obtemNumeroDoExercicioCorrente()));
        pSuperior = new JPanel(new GridLayout(2, 1));
        pTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titulo = new JLabel(tipoDeExercicio);
        titulo.setFont(new Font("", 0, 25));
        titulo.setForeground(new Color(0, 0, 128));
        pTitulo.add(titulo);
        pSuperior.add(pTitulo);
        preposicao = alteraString(preposicao);
        valores = alteraMatrizDeStrings(valores);
        pEnun = new JPanel(new GridLayout(2, 1));
        enunciado1 = new JLabel(this.retornaEspacoAjustado("    ") + ordem);
        enunciado1.setFont(new Font("", 1, 13));
        enunciado1.setForeground(new Color(0, 0, 128));
        pEnun.add(enunciado1);
        pEnun2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        enunciado2 = new JLabel(this.retornaEspacoAjustado("    ") + ordem2);
        enunciado2.setFont(new Font("", 1, 13));
        enunciado2.setForeground(new Color(0, 0, 128));
        pEnun2.add(enunciado2);
        enunciado2 = new JLabel(preposicao);
        enunciado2.setFont(new Font("Times", 1, 14));
        enunciado2.setForeground(new Color(1, 0, 128));
        pEnun2.add(enunciado2);
        pEnun.add(pEnun2);
        pSuperior.add(pEnun);
        getContentPane().add("North", pSuperior);
        pOeste = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pOeste.add(new JLabel(this.retornaEspacoAjustado("  ")));
        getContentPane().add("East", pOeste);
        pOeste.add(new JLabel(this.retornaEspacoAjustado("    ")));
        getContentPane().add("West", pOeste);
        pCentral = new JPanel(new GridLayout(1, valores[0].length + 1));
        pColuna = new JPanel(new GridLayout(valores.length, 1));
        String[][] valor = new String[valores[1].length][valores.length];
        for (int i = 0; i < valor.length; i++) for (int j = 0; j < valor[i].length; j++) {
            valor[i][j] = valores[j][i];
        }
        int total = 0;
        for (int i = 0; i < valor.length; i++) {
            if (valor[i][1].equals("nada")) total++;
        }
        preencher = new JTextField[total + 1][valor[1].length - 1];
        resposta = new String[total + 1][valor[1].length - 1];
        int aux = 0;
        for (int i = 0; i <= valor.length; i++) {
            pColuna = new JPanel(new GridLayout(valor[1].length, 1));
            for (int j = 0; j < valor[1].length; j++) {
                if (i == valor.length) {
                    if (j == 0) {
                        pAux = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JLabel prep = new JLabel(preposicao);
                        prep.setFont(new Font("Times", 0, 12));
                        prep.setForeground(new Color(0, 0, 128));
                        pAux.add(prep);
                    } else {
                        pAux = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        preencher[total][j - 1] = new JTextField("", 3);
                        preencher[total][j - 1].setDocument(new DefinePadrao(1));
                        preencher[total][j - 1].addKeyListener(this);
                        pAux.add(preencher[total][j - 1]);
                    }
                } else {
                    if ((valor[i][j]).equals("nada")) {
                        pAux = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        preencher[aux][j - 1] = new JTextField("", 3);
                        preencher[aux][j - 1].setDocument(new DefinePadrao(1));
                        preencher[aux][j - 1].addKeyListener(this);
                        pAux.add(preencher[aux][j - 1]);
                        if (j == valor[i].length - 1) aux++;
                    } else {
                        pAux = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JLabel vals = new JLabel(valor[i][j]);
                        if (j == 0) {
                            vals.setFont(new Font("Times", 0, 13));
                            vals.setForeground(new Color(0, 0, 128));
                        }
                        pAux.add(vals);
                    }
                }
                pColuna.add(pAux);
            }
            pCentral.add(pColuna);
        }
        getContentPane().add("Center", pCentral);
    }

    /**
     * Trata eventos(correcao dos exercicios).
     **/
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() == conferir) {
            for (int i = 0; i < this.preencher.length; i++) for (int j = 0; j < this.preencher[i].length; j++) {
                this.resposta[i][j] = this.preencher[i][j].getText();
            }
            InterfaceDados.confereExercicio(TIPO, this.resposta);
        }
    }

    public void keyPressed(KeyEvent ek) {
        super.keyPressed(ek);
        if (ek.getKeyCode() == KeyEvent.VK_ENTER) {
            if (ek.getSource() == conferir) {
                for (int i = 0; i < this.preencher.length; i++) for (int j = 0; j < this.preencher[i].length; j++) {
                    this.resposta[i][j] = this.preencher[i][j].getText();
                }
                InterfaceDados.confereExercicio(TIPO, this.resposta);
            } else if (ek.getSource() instanceof JTextField) {
                for (int i = 0; i < this.preencher.length; i++) for (int j = 0; j < this.preencher[i].length; j++) {
                    this.resposta[i][j] = this.preencher[i][j].getText();
                }
                InterfaceDados.confereExercicio(TIPO, this.resposta);
            }
        }
    }

    public void keyTyped(KeyEvent ek) {
    }

    public void keyReleased(KeyEvent ek) {
    }

    private String alteraString(String s) {
        char c[] = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '=') c[i] = 8801; else if (c[i] == '>') c[i] = 8835; else if (c[i] == '<') c[i] = 8834;
        }
        return String.valueOf(c);
    }

    private String[][] alteraMatrizDeStrings(String[][] s) {
        for (int i = 0; i < s.length; i++) for (int j = 0; j < s[i].length; j++) {
            s[i][j] = alteraString(s[i][j]);
        }
        return s;
    }

    public String[][] logicaParaconsistente() {
        return this.resposta;
    }
}

class DefinePadrao extends DefineTamanhoDoTexto {

    private static final long serialVersionUID = 1L;

    public DefinePadrao(int tamanho) {
        super(tamanho);
    }

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (((str.charAt(0) == '1') || (str.charAt(0) == '2') || (str.charAt(0) == '3'))) {
            super.insertString(offset, str, attr);
        } else {
            str = null;
            super.insertString(offset, str, attr);
        }
    }
}
