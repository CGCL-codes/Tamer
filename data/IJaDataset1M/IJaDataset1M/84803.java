package computacaografica;

import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PainelComposta extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int LARG = 320;

    private static final int ALT = 140;

    private JScrollPane scroll;

    private JTable tabela;

    private DefaultTableModel modelo;

    private JButton botaoAdiciona;

    private JButton botaoAplica;

    private FrameTransf frame;

    public PainelComposta(FrameTransf frame) {
        this.frame = frame;
        this.initComponents();
        this.acaoBotaoAdiciona();
        this.acaoBotaoAplica();
    }

    private void initComponents() {
        this.setLayout(null);
        this.setBorder(BorderFactory.createTitledBorder("Transforma��es Compostas"));
        this.initTable();
        this.scroll = new JScrollPane(this.tabela);
        this.add(scroll);
        this.scroll.setBounds(10, 20, 200, 100);
        this.initButtons();
        this.setSize(PainelComposta.LARG, PainelComposta.ALT);
        this.setVisible(true);
    }

    private void initTable() {
        this.modelo = new DefaultTableModel();
        this.tabela = new JTable(this.modelo);
        this.modelo.addColumn("Transforma��es");
        this.tabela.setEnabled(false);
    }

    private void initButtons() {
        this.botaoAdiciona = new JButton("Adiciona");
        this.add(this.botaoAdiciona);
        this.botaoAdiciona.setBounds(220, 20, 90, 30);
        this.botaoAplica = new JButton("Aplica");
        this.add(this.botaoAplica);
        this.botaoAplica.setBounds(220, 60, 90, 30);
    }

    private void acaoBotaoAplica() {
        this.botaoAplica.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBotaoAplicaActionPerformed(evt);
            }
        });
    }

    private void jBotaoAplicaActionPerformed(ActionEvent evt) {
        this.frame.executeAllTransformations();
        int c = this.modelo.getRowCount();
        for (int i = 0; i < c; i++) {
            this.modelo.removeRow(0);
        }
        this.frame.limpaTransf();
    }

    private void acaoBotaoAdiciona() {
        this.botaoAdiciona.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBotaoAdicionaActionPerformed(evt);
            }
        });
    }

    private void jBotaoAdicionaActionPerformed(ActionEvent evt) {
        String transf = this.frame.addMatriz();
        if (!transf.equals("")) this.modelo.addRow(new Object[] { transf });
    }
}
