package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Descargas extends JInternalFrame implements ActionListener {

    public static final long serialVersionUID = 3;

    JPanel p;

    JScrollPane sp;

    public void actionPerformed(ActionEvent e) {
        Descarga tmp;
        for (int x = 0; x < p.getComponentCount(); x++) {
            tmp = (Descarga) p.getComponent(x);
            if (!tmp.ls.isAlive()) {
                p.remove(x);
                x--;
            }
        }
        validate();
        repaint();
    }

    private void cerrar() {
        setVisible(false);
    }

    public Descargas() {
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cerrar();
            }
        });
        btnLimpiar.addActionListener(this);
        setTitle("Apeiron [Descargas]");
        setResizable(true);
        setMaximizable(true);
        setClosable(true);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        p = new JPanel(new LayoutManager() {

            public void addLayoutComponent(String name, Component comp) {
            }

            public void layoutContainer(Container parent) {
                int r = 0;
                for (int x = 0; x < parent.getComponentCount(); x++) {
                    parent.getComponent(x).setBounds(0, r, parent.getWidth(), 100);
                    r += 100;
                }
            }

            public Dimension minimumLayoutSize(Container parent) {
                return new Dimension(150, parent.getComponentCount() * 100);
            }

            public Dimension preferredLayoutSize(Container parent) {
                return new Dimension(150, parent.getComponentCount() * 100);
            }

            public void removeLayoutComponent(Component comp) {
            }
        });
        JPanel tmp = new JPanel(new FlowLayout());
        tmp.add(btnLimpiar);
        tmp.add(btnCerrar);
        getContentPane().setLayout(new BorderLayout());
        sp = new JScrollPane(p);
        getContentPane().add(sp, "Center");
        getContentPane().add(tmp, "South");
        setSize(400, 300);
    }

    public void agregar(Component c) {
        p.add(c);
    }
}
