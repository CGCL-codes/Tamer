package janela;

import arvore_b.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.JScrollPane;

/**
 *
 * @author Vitor Villela
 */
public class JanelaInicial extends JFrame implements ActionListener {

    private JLabel lTitulo;

    private JButton bInsere;

    private JButton bRemove;

    private JButton bInsereN;

    private JButton bBusca;

    private JButton bAlteraOrdem;

    private JButton bLimpeza;

    private Arvore mytree;

    private JTextField tCampo;

    private PainelDesenhaArvore pBinTree;

    public JanelaInicial() {
        super();
        this.setTitle("ARVORE-B");
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int alturaTela = d.height;
        int comprimentoTela = d.width;
        this.setSize(comprimentoTela * 2 / 3, alturaTela / 2);
        setLocation((comprimentoTela) / 4, (alturaTela) / 4);
        this.setLayout(new BorderLayout());
        this.setResizable(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        configureMenu();
        mytree = new Arvore(5);
        lTitulo = new JLabel("Ordem = " + Integer.toString(mytree.getMaximoChaves() + 1));
        lTitulo.setForeground(Color.red);
        tCampo = new JTextField(7);
        bInsere = new JButton("INSERIR");
        bBusca = new JButton("BUSCAR");
        bRemove = new JButton("REMOVER");
        bInsereN = new JButton("INSERIR N");
        bLimpeza = new JButton("LIMPAR TUDO");
        bAlteraOrdem = new JButton("ALTERAR ORDEM");
        pBinTree = new PainelDesenhaArvore(mytree);
        bInsere.setToolTipText("Inserir um valor na árvore");
        bRemove.setToolTipText("Remover um valor na árvore");
        bBusca.setToolTipText("Buscar um valor na árvore");
        bInsereN.setToolTipText("Inserir vários valores na árvore");
        bAlteraOrdem.setToolTipText("Alterar a ordem da árvore");
        bLimpeza.setToolTipText("Apagar a árvore inteira");
        bInsere.addActionListener(this);
        bRemove.addActionListener(this);
        bBusca.addActionListener(this);
        bInsereN.addActionListener(this);
        bAlteraOrdem.addActionListener(this);
        bLimpeza.addActionListener(this);
        JScrollPane jsp = new JScrollPane();
        this.getContentPane().add(jsp);
        add(new PainelInicial(lTitulo, tCampo, bInsere, bRemove, bInsereN, bBusca, bAlteraOrdem, bLimpeza), BorderLayout.NORTH);
        jsp.setViewportView(pBinTree);
    }

    public void configureMenu() {
        JMenu menu;
        JMenuBar menuBar;
        JMenuItem item;
        menuBar = new JMenuBar();
        menu = new JMenu("Arquivo");
        menuBar.add(menu);
        item = new JMenuItem("Fechar");
        item.setActionCommand("Fechar");
        item.addActionListener(this);
        menu.add(item);
        menu = new JMenu("Ajuda");
        menuBar.add(menu);
        item = new JMenuItem("Como usar...");
        item.setActionCommand("Como usar...");
        item.addActionListener(this);
        menu.add(item);
        item = new JMenuItem("Sobre");
        item.setActionCommand("Sobre");
        item.addActionListener(this);
        menu.add(item);
        this.setJMenuBar(menuBar);
    }

    public void actionPerformed(ActionEvent e) {
        String s;
        int k;
        if (e.getSource() == bInsere) {
            s = tCampo.getText();
            tCampo.setText("");
            try {
                k = Integer.parseInt(s);
                mytree.insere(k);
                pBinTree.repaint();
            } catch (NumberFormatException exception) {
                if (s.length() == 0) {
                    JOptionPane.showMessageDialog(this, "Você deve digitar algum número inteiro no campo de \n" + "texto e apertar o botão \"Inserir\" para adicioná-lo\n" + "à árvore binária.", "Erro na entrada de dados", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Esta aplicação está restrita a números inteiros.", "Erro na entrada de dados", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (e.getSource() == bRemove) {
            s = tCampo.getText();
            tCampo.setText("");
            try {
                k = Integer.parseInt(s);
                mytree.remove(k);
                pBinTree.repaint();
            } catch (NumberFormatException exception) {
                if (s.length() == 0) {
                    JOptionPane.showMessageDialog(this, "Você deve digitar algum número inteiro no campo de \n" + "texto e apertar o botão \"Inserir\" para adicioná-lo\n" + "à árvore binária.", "Erro na entrada de dados", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Esta aplicação está restrita a números inteiros.", "Erro na entrada de dados", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (e.getSource() == bBusca) {
            s = tCampo.getText();
            tCampo.setText("");
            try {
                k = Integer.parseInt(s);
                if (mytree.buscaChave(mytree.getRaiz(), k) != null) {
                    JOptionPane.showMessageDialog(this, "Chave já existe na arvore !!!");
                } else {
                    JOptionPane.showMessageDialog(this, "Não foi encontrada a chave informada");
                }
                pBinTree.repaint();
            } catch (NumberFormatException exception) {
                if (s.length() == 0) {
                    JOptionPane.showMessageDialog(this, "Você deve digitar algum número inteiro no campo de \n" + "texto e apertar o botão \"Inserir\" para adicioná-lo\n" + "à árvore binária.", "Erro na entrada de dados", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Esta aplicação está restrita a números inteiros.", "Erro na entrada de dados", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (e.getSource() == bInsereN) {
            s = tCampo.getText();
            tCampo.setText("");
            int valor;
            int i;
            try {
                k = Integer.parseInt(s);
                for (i = 0; i < k; i++) {
                    valor = (int) (Math.random() * 100);
                    mytree.insere(valor);
                }
                pBinTree.repaint();
            } catch (NumberFormatException exception) {
                if (s.length() == 0) {
                    JOptionPane.showMessageDialog(this, "Você deve digitar algum número inteiro no campo de \n" + "texto e apertar o botão \"Inserir\" para adicioná-lo\n" + "à árvore binária.", "Erro na entrada de dados", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Esta aplicação está restrita a números inteiros.", "Erro na entrada de dados", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (e.getSource() == bLimpeza) {
            mytree.limpaArvore();
            pBinTree.repaint();
        }
        if (e.getSource() == bAlteraOrdem) {
            String sA;
            sA = tCampo.getText();
            tCampo.setText("");
            try {
                k = Integer.parseInt(sA);
                mytree.limpaArvore();
                mytree.setMaximoChaves(k - 1);
                lTitulo.setText("Ordem = " + Integer.toString(mytree.getMaximoChaves() + 1));
                pBinTree.repaint();
            } catch (NumberFormatException exception) {
                if (sA.length() == 0) {
                    JOptionPane.showMessageDialog(this, "Você deve digitar algum número inteiro no campo de \n" + "texto e apertar o botão \"Alterar Ordem\"  \n" + "para alterar o número máximo de filhos\n" + "à árvore binária.", "Erro na entrada de dados", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Esta aplicação está restrita a números inteiros.", "Erro na entrada de dados", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (e.getActionCommand().equals("Fechar")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("Sobre")) {
            JOptionPane.showMessageDialog(this, " Árvore B\n" + " Desenvolvedores:\n" + " Arthur Mazer\n" + " Valter Henrique \n" + " Vitor Souza Villela", "Sobre", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getActionCommand().equals("Como usar...")) {
            JOptionPane.showMessageDialog(this, "Esta aplicação corresponde ao metodos de inserção ,remoção\n" + "e busca de uma árvore-B. \n" + "Para utilizar o programa, basta digitar um numero inteiro no campo\n" + "de texto correspondente e pressionar o botao desejado\n" + "Caso queira apagar a árvore inteira, basta clicar no botão " + "limpar tudo", "Como utilizar este programa.", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
