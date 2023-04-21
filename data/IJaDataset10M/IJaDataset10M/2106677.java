package org.jazzteam.edu.algo.matrixgame.mathem.graph;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class Form extends JFrame implements ActionListener {

    private final JMenuBar menuBar;

    private final JMenu mnNewMenu;

    private final JMenuItem mntmNewMenuItem;

    private final JMenuItem mntmNewMenuItem_1;

    private final PanelDraw panel1;

    private final JToolBar toolBar;

    private final JComboBox comboBox;

    public Form() throws HeadlessException {
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        setBounds(20, 20, 392, 330);
        mnNewMenu = new JMenu("File");
        menuBar.add(mnNewMenu);
        mntmNewMenuItem = new JMenuItem("panelDraw");
        mnNewMenu.add(mntmNewMenuItem);
        mntmNewMenuItem_1 = new JMenuItem("Exit");
        mnNewMenu.add(mntmNewMenuItem_1);
        toolBar = new JToolBar();
        menuBar.add(toolBar);
        PanelDraw panelDraw = new PanelDraw();
        panel1 = panelDraw;
        getContentPane().add(panel1, BorderLayout.CENTER);
        comboBox = new JComboBox();
        comboBox.setToolTipText("");
        comboBox.setModel(new DefaultComboBoxModel(new String[] { "Top", "Edge" }));
        comboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent itemevent) {
                if (comboBox.getModel().getSelectedItem() == "Top") {
                    panel1.setString("Top");
                }
                if (comboBox.getModel().getSelectedItem() == "Edge") {
                    panel1.setString("Edge");
                }
            }
        });
        toolBar.add(comboBox);
    }

    @Override
    public void actionPerformed(ActionEvent actionevent) {
    }
}
