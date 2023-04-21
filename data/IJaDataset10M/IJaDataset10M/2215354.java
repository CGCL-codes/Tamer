package com.zara.store.client.view.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import com.zara.store.client.view.EnvtView;
import com.zara.store.client.view.OfadView;
import com.zara.store.client.view.PalcView;
import com.zara.store.client.view.RetailView;
import com.zara.store.main.Main;

public class MenuForm extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JPanel dummy;

    private final Container container;

    private JButton buttonOfad;

    private JButton buttonPalc;

    private JButton buttonEnvt;

    private JButton buttonRetail;

    private RetailView retailView;

    private PalcView palcView;

    private OfadView ofadView;

    private EnvtView envtView;

    public MenuForm(RetailView retailView, PalcView palcView, EnvtView envtView, OfadView ofadView) {
        super();
        this.retailView = retailView;
        this.palcView = palcView;
        this.ofadView = ofadView;
        this.envtView = envtView;
        dummy = new JPanel(null);
        buttonOfad = new JButton(Main.createImageIcon("/com/zara/store/client/resources/ofad.gif"));
        buttonEnvt = new JButton(Main.createImageIcon("/com/zara/store/client/resources/envt.gif"));
        buttonPalc = new JButton(Main.createImageIcon("/com/zara/store/client/resources/palc.gif"));
        buttonRetail = new JButton(Main.createImageIcon("/com/zara/store/client/resources/retail.gif"));
        container = getContentPane();
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(Main.createImageIcon("/com/zara/store/client/resources/menu.gif").getImage());
        setTitle("Zara Tienda");
        buttonEnvt.setText("<html><larger>Env�o a Tienda</larger></html>");
        buttonPalc.setText("<html><larger>Pedido a La Coru�a</larger></html>");
        buttonOfad.setText("<html><larger>Oferta de Art�culos Disponibles</larger></html>");
        buttonRetail.setText("<html><larger>Venta de Art�culos</larger></html>");
        addListeners();
        container.setLayout(new GridBagLayout());
        JLabel label = new JLabel("<html><br><h1><font size=\"25\" color=\"FFFFFF\" face=\"Serif\">ZARA</font></h1><br></html>", SwingConstants.CENTER);
        JPanel labelPanel = new JPanel();
        labelPanel.add(label);
        labelPanel.setBackground(new Color(0x333333));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 0, 10, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        container.add(dummy, constraints);
        constraints.weightx = 1;
        constraints.gridx = 1;
        constraints.gridy = 1;
        container.add(labelPanel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.weighty = 1;
        JTabbedPane tab = new JTabbedPane();
        tab.addTab(buttonRetail.getText(), buttonRetail.getIcon(), retailView.getRetailForm().getContentPane());
        tab.addTab(buttonOfad.getText(), buttonOfad.getIcon(), ofadView.getOfadForm().getContentPane());
        tab.addTab(buttonPalc.getText(), buttonPalc.getIcon(), palcView.getPalcForm().getContentPane());
        tab.addTab(buttonEnvt.getText(), buttonEnvt.getIcon(), envtView.getEnvtForm().getContentPane());
        container.add(tab, constraints);
        pack();
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }

    private void addListeners() {
        buttonRetail.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                retailView.display();
            }
        });
        buttonPalc.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                palcView.display();
            }
        });
        buttonOfad.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ofadView.display();
            }
        });
        buttonEnvt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                envtView.display();
            }
        });
    }
}
