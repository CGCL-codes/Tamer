package Interface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Classes.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MenuTodos extends JPanel implements ActionListener, ItemListener {

    private JComboBox box;

    private JButton voltar;

    private String[] nomes;

    private JTable tabela;

    private JScrollPane lista;

    private JTable ranking;

    private JScrollPane scrollRanking;

    private JRadioButton popularidade;

    private JRadioButton estabelecimento;

    private ButtonGroup grupo;

    public MenuTodos() {
        setBackground(Color.WHITE);
        setLayout(null);
        setSize(800, 600);
        nomes = ManipulaEstabelecimento.getNomes();
        botoes();
        montaPainel();
        adicionaEvento();
    }

    public void botoes() {
        voltar = new JButton("Voltar");
        voltar.setBounds(10, 515, 100, 30);
        box = new JComboBox(nomes);
        box.setLocation(10, 100);
        Dimension tamanhoBox = box.getPreferredSize();
        box.setSize(tamanhoBox);
        box.setMaximumRowCount(5);
        box.setVisible(false);
        tabela = new JTable();
        tabela.setEnabled(false);
        ranking = new JTable();
        ranking.setEnabled(false);
        lista = new JScrollPane(tabela);
        lista.setLocation(2, 250);
        lista.setSize(480, 200);
        lista.setVisible(false);
        scrollRanking = new JScrollPane(ranking);
        scrollRanking.setLocation(140, 30);
        scrollRanking.setSize(520, 320);
        scrollRanking.setVisible(false);
        popularidade = new JRadioButton("Popularidade", false);
        popularidade.setBackground(Color.WHITE);
        popularidade.setLocation(10, 30);
        popularidade.setSize(popularidade.getPreferredSize());
        estabelecimento = new JRadioButton("Estabelecimento", false);
        estabelecimento.setBackground(Color.WHITE);
        estabelecimento.setLocation(10, 100);
        estabelecimento.setSize(estabelecimento.getPreferredSize());
        grupo = new ButtonGroup();
        grupo.add(popularidade);
        grupo.add(estabelecimento);
    }

    public void montaPainel() {
        add(box);
        add(voltar);
        add(lista);
        add(scrollRanking);
        add(popularidade);
        add(estabelecimento);
    }

    public void adicionaEvento() {
        voltar.addActionListener(this);
        box.addItemListener(this);
        popularidade.addItemListener(this);
        estabelecimento.addItemListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == voltar) {
            Executa.trocaPainel(new MenuPrincipal());
        }
    }

    @Override
    public void itemStateChanged(ItemEvent event) {
        if (estabelecimento.isSelected()) {
            scrollRanking.setVisible(false);
            box.setVisible(true);
            lista.setVisible(true);
            if (event.getStateChange() == ItemEvent.SELECTED) {
                String[] col = new String[] { "Nome", "Nota" };
                String[][] table = ManipulaUsuario.relaciona(box.getSelectedIndex());
                DefaultTableModel dtm = new DefaultTableModel(table, col);
                tabela.setModel(dtm);
                System.out.println(box.getSelectedIndex());
            }
        }
        if (popularidade.isSelected()) {
            box.setVisible(false);
            lista.setVisible(false);
            scrollRanking.setVisible(true);
            String[] coluna = new String[] { "Estabelecimento", "Pontua��o" };
            String[][] table1 = Caso2.caso2();
            DefaultTableModel dtm1 = new DefaultTableModel(table1, coluna);
            ranking.setModel(dtm1);
        }
    }

    public static void main(String[] args) {
        JFrame j = new JFrame();
        MenuTodos x = new MenuTodos();
        j.getContentPane().add(x);
        j.setSize(700, 400);
        j.setVisible(true);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
