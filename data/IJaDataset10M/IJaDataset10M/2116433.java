package org.pvs.superpalitos.sp_lite.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.pvs.superpalitos.sp_lite.SuperPalitos;

public class MainPanel extends JPanel {

    public MainPanel() {
        setPreferredSize(SuperPalitos.SIZE);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);
        JButton btnJugar = new JButton("Jugar");
        btnJugar.setFont(new Font("Dialog", Font.BOLD, 14));
        btnJugar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SuperPalitos.getInstance().prePlay();
            }
        });
        GridBagConstraints gbc_btnJugar = new GridBagConstraints();
        gbc_btnJugar.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnJugar.insets = new Insets(0, 0, 5, 5);
        gbc_btnJugar.gridx = 1;
        gbc_btnJugar.gridy = 1;
        add(btnJugar, gbc_btnJugar);
        JButton btnAcercaDe = new JButton("Acerca de");
        final Component c = this;
        btnAcercaDe.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(c, SuperPalitos.ABOUT, "Acerca de", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        GridBagConstraints gbc_btnAcercaDe = new GridBagConstraints();
        gbc_btnAcercaDe.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnAcercaDe.anchor = GridBagConstraints.ABOVE_BASELINE;
        gbc_btnAcercaDe.insets = new Insets(0, 0, 5, 5);
        gbc_btnAcercaDe.gridx = 1;
        gbc_btnAcercaDe.gridy = 2;
        add(btnAcercaDe, gbc_btnAcercaDe);
        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SuperPalitos.getInstance().exit();
            }
        });
        GridBagConstraints gbc_btnSalir = new GridBagConstraints();
        gbc_btnSalir.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnSalir.insets = new Insets(0, 0, 5, 5);
        gbc_btnSalir.gridx = 1;
        gbc_btnSalir.gridy = 3;
        add(btnSalir, gbc_btnSalir);
    }

    private static final long serialVersionUID = -457150841083909110L;

    private static final String IMAGE_FILE = "/icons/title.png";

    private static final Image FONDO = Toolkit.getDefaultToolkit().getImage(TableroPanel.class.getResource(IMAGE_FILE));

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics g2 = g.create();
        g2.drawImage(FONDO, 0, 0, null);
    }
}
