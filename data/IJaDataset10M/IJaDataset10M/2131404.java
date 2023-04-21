package loja.aplication;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Cabecalho implements ActionListener {

    private JPanel p, p1;

    private ImageIcon img;

    private JLabel l;

    public static JButton inicial;

    public static JButton cliente;

    public static JButton produto, nf;

    TelaInicial ti;

    TelaCliente tc;

    TelaProduto tp;

    public JPanel cabec() {
        p = new JPanel();
        p.setLayout(new BorderLayout(2, 2));
        p1 = new JPanel();
        p1.setLayout(new FlowLayout(FlowLayout.RIGHT));
        img = new ImageIcon("images/miniLogoFiorelly.PNG");
        l = new JLabel(img);
        l.setVisible(false);
        inicial = new JButton("In�cio");
        inicial.addActionListener(this);
        inicial.setEnabled(false);
        cliente = new JButton("Cliente");
        cliente.addActionListener(this);
        produto = new JButton("Produto");
        produto.addActionListener(this);
        nf = new JButton("Nota Fiscal");
        nf.addActionListener(this);
        p1.add(inicial);
        p1.add(cliente);
        p1.add(produto);
        p1.add(nf);
        p.add(l, BorderLayout.WEST);
        p.add(p1, BorderLayout.CENTER);
        return p;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == inicial) {
            Index.openInicial();
            Index.jmNovo.setEnabled(false);
            inicial.setEnabled(false);
            cliente.setEnabled(true);
            produto.setEnabled(true);
            nf.setEnabled(true);
            l.setVisible(false);
        }
        if (e.getSource() == cliente) {
            Index.openCliente();
            Index.jmNovo.setEnabled(true);
            inicial.setEnabled(true);
            cliente.setEnabled(false);
            produto.setEnabled(true);
            nf.setEnabled(true);
            l.setVisible(true);
        }
        if (e.getSource() == produto) {
            Index.openProduto();
            Index.jmNovo.setEnabled(true);
            inicial.setEnabled(true);
            cliente.setEnabled(true);
            produto.setEnabled(false);
            nf.setEnabled(true);
            l.setVisible(true);
        }
    }
}
