package net.sf.econtycoon.gui.swing;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JWindow;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import net.sf.econtycoon.core.Game;
import net.sf.econtycoon.core.data.Strings;
import net.sf.econtycoon.core.data.ModData;

/**
 * Der Tab, in dem der Player Industry oder Lager bauen kann
 * 
 * @author Konstantin
 */
public class Tab_Bauen extends javax.swing.JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JComboBox jComboBox1;

    private JScrollPane jScrollPane1;

    private JLabel label_lagerpreis1;

    private JPanel jPanel8;

    private JLabel label_lagerpreis2;

    private JLabel label_lagerpreis0;

    private JButton button_lager2;

    private JButton button_lager1;

    private JButton button_lager0;

    private JPanel jPanel7;

    private JLabel[] indname;

    private JLabel[] indpreis;

    private JButton[] buttonkaufen;

    private JWindow[] popup;

    private GUI gui;

    private int lagertyp = 0;

    private Game game;

    /**
	 *
	 * Konstruiert einen neuen Tab-Bauen
	 * @param gui the gui
	 * @param game the game
	 */
    public Tab_Bauen(GUI gui, Game game) {
        super();
        this.gui = gui;
        this.game = game;
        indname = new JLabel[ModData.AMOUNT_OF_INDUSTRIES];
        indpreis = new JLabel[ModData.AMOUNT_OF_INDUSTRIES];
        buttonkaufen = new JButton[ModData.AMOUNT_OF_INDUSTRIES];
        popup = new JWindow[ModData.AMOUNT_OF_INDUSTRIES];
        initGUI();
        lagerbuttonsupdate();
    }

    private void initGUI() {
        try {
            this.setPreferredSize(new java.awt.Dimension(767, 334));
            this.setBackground(net.sf.econtycoon.core.data.ModData.PANELCOLOR_1);
            this.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
            GroupLayout thisLayout = new GroupLayout((JComponent) this);
            this.setLayout(thisLayout);
            thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().addContainerGap().addGroup(thisLayout.createParallelGroup().addComponent(getJScrollPane1(), GroupLayout.Alignment.LEADING, 0, 318, Short.MAX_VALUE).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addComponent(getJPanel7(), 0, 306, Short.MAX_VALUE).addGap(12))));
            thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup().addContainerGap().addComponent(getJPanel7(), GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(getJScrollPane1(), GroupLayout.PREFERRED_SIZE, 519, GroupLayout.PREFERRED_SIZE).addContainerGap(17, Short.MAX_VALUE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel getJPanel7() {
        if (jPanel7 == null) {
            jPanel7 = new JPanel();
            jPanel7.setBackground(net.sf.econtycoon.core.data.ModData.PANELCOLOR_1);
            GroupLayout jPanel7Layout = new GroupLayout((JComponent) jPanel7);
            jPanel7.setBorder(BorderFactory.createTitledBorder(null, Strings.getString("GUI.15"), TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 14)));
            jPanel7.setLayout(jPanel7Layout);
            jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup().addGroup(jPanel7Layout.createSequentialGroup().addGroup(jPanel7Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup().addComponent(getJLabel2(), GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE).addGap(0, 0, Short.MAX_VALUE)).addGroup(jPanel7Layout.createSequentialGroup().addComponent(getJLabel6(), GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE).addGap(0, 0, Short.MAX_VALUE)).addComponent(getJLabel5(), GroupLayout.Alignment.LEADING, 0, 105, Short.MAX_VALUE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel7Layout.createParallelGroup().addComponent(getButton_lager1(), GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE).addComponent(getButton_lager0(), GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE).addComponent(getButton_lager2(), GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)).addContainerGap()).addGroup(GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup().addGap(7).addComponent(getJComboBox1(), GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE).addContainerGap(67, Short.MAX_VALUE)));
            jPanel7Layout.linkSize(SwingConstants.HORIZONTAL, new Component[] { getJLabel2(), getJLabel6(), getJLabel5() });
            jPanel7Layout.setVerticalGroup(jPanel7Layout.createSequentialGroup().addComponent(getJComboBox1(), GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(getButton_lager0(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(getJLabel2(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel7Layout.createParallelGroup().addComponent(getButton_lager1(), GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(getJLabel5(), GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel7Layout.createParallelGroup().addGroup(jPanel7Layout.createSequentialGroup().addComponent(getButton_lager2(), GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addGroup(jPanel7Layout.createSequentialGroup().addComponent(getJLabel6(), GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE))).addContainerGap(173, Short.MAX_VALUE));
            jPanel7Layout.linkSize(SwingConstants.VERTICAL, new Component[] { getJLabel2(), getJLabel6(), getJLabel5() });
        }
        return jPanel7;
    }

    private JButton getButton_lager0() {
        if (button_lager0 == null) {
            button_lager0 = new JButton();
            button_lager0.setBackground(net.sf.econtycoon.core.data.ModData.PANELCOLOR_1);
            button_lager0.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    game.getPlayer().buyStorage(lagertyp, 0);
                    lagerbuttonsupdate();
                }
            });
        }
        return button_lager0;
    }

    private JButton getButton_lager1() {
        if (button_lager1 == null) {
            button_lager1 = new JButton();
            button_lager1.setBackground(net.sf.econtycoon.core.data.ModData.PANELCOLOR_1);
            button_lager1.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    game.getPlayer().buyStorage(lagertyp, 1);
                    lagerbuttonsupdate();
                }
            });
        }
        return button_lager1;
    }

    private JButton getButton_lager2() {
        if (button_lager2 == null) {
            button_lager2 = new JButton();
            button_lager2.setBackground(net.sf.econtycoon.core.data.ModData.PANELCOLOR_1);
            button_lager2.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    game.getPlayer().buyStorage(lagertyp, 2);
                    lagerbuttonsupdate();
                }
            });
        }
        return button_lager2;
    }

    private JLabel getJLabel2() {
        if (label_lagerpreis0 == null) {
            label_lagerpreis0 = new JLabel();
            label_lagerpreis0.setText("Lager1");
            label_lagerpreis0.setFont(new java.awt.Font("Tahoma", 0, 12));
        }
        return label_lagerpreis0;
    }

    private JLabel getJLabel5() {
        if (label_lagerpreis1 == null) {
            label_lagerpreis1 = new JLabel();
            label_lagerpreis1.setText("Lager2");
            label_lagerpreis1.setFont(new java.awt.Font("Tahoma", 0, 12));
        }
        return label_lagerpreis1;
    }

    private JLabel getJLabel6() {
        if (label_lagerpreis2 == null) {
            label_lagerpreis2 = new JLabel();
            label_lagerpreis2.setText("Lager3");
            label_lagerpreis2.setFont(new java.awt.Font("Tahoma", 0, 12));
        }
        return label_lagerpreis2;
    }

    private JPanel getJPanel8() {
        if (jPanel8 == null) {
            jPanel8 = new JPanel();
            jPanel8.setBackground(net.sf.econtycoon.core.data.ModData.PANELCOLOR_1);
            GridBagLayout jPanel8Layout = new GridBagLayout();
            int hoehe[] = new int[23];
            double gewicht[] = new double[23];
            for (int i = 0; i < 23; i++) hoehe[i] = 10;
            jPanel8Layout.columnWidths = new int[] { 7, 7, 7 };
            jPanel8Layout.rowHeights = hoehe;
            jPanel8Layout.columnWeights = new double[] { 0.1, 0.1, 0.1 };
            jPanel8Layout.rowWeights = gewicht;
            jPanel8.setLayout(jPanel8Layout);
            for (int i = 0; i < ModData.AMOUNT_OF_INDUSTRIES; i++) {
                jPanel8.add(getIndname(i), new GridBagConstraints(0, i, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jPanel8.add(getIndpreis(i), new GridBagConstraints(1, i, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jPanel8.add(getButtonkaufen(i), new GridBagConstraints(2, i, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            }
        }
        return jPanel8;
    }

    private JLabel getIndname(int i) {
        if (indname[i] == null) {
            indname[i] = new JLabel();
            indname[i].setText(Strings.getString("Industry." + i));
            final int j = i;
            indname[i].addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent evt) {
                    getPopup(j).setLocation((int) (MouseInfo.getPointerInfo().getLocation().getX()) + 10, (int) (MouseInfo.getPointerInfo().getLocation().getY()) + 10);
                    getPopup(j).setVisible(true);
                    getPopup(j).requestFocus();
                }

                public void mouseExited(MouseEvent evt) {
                    getPopup(j).setVisible(false);
                }
            });
        }
        return indname[i];
    }

    private JWindow getPopup(int i) {
        if (popup[i] == null) {
            popup[i] = new JWindow();
            popup[i].setSize(180, 120);
            popup[i].getContentPane().setBackground(ModData.PANELCOLOR_1.brighter());
            JLabel produkt1 = new JLabel(((Integer) (ModData.INDUSTRY[i][ModData.P1A])).toString() + " " + game.getCommodity(ModData.INDUSTRY[i][ModData.P1]).getName());
            JLabel produkt2 = new JLabel();
            JLabel produkt3 = new JLabel();
            if (ModData.INDUSTRY[i][ModData.P2] != -1) {
                produkt2.setText(((Integer) (ModData.INDUSTRY[i][ModData.P2A])).toString() + " " + game.getCommodity(ModData.INDUSTRY[i][ModData.P2]).getName());
            } else {
                produkt2.setText("---");
            }
            if (ModData.INDUSTRY[i][ModData.P3] != -1) {
                produkt3.setText(((Integer) (ModData.INDUSTRY[i][ModData.P3A])).toString() + " " + game.getCommodity(ModData.INDUSTRY[i][ModData.P3]).getName());
            } else {
                produkt3.setText("---");
            }
            JLabel verbrauch1 = new JLabel();
            JLabel verbrauch2 = new JLabel();
            JLabel verbrauch3 = new JLabel();
            if (ModData.INDUSTRY[i][ModData.N1] != -1) {
                verbrauch1.setText(((Integer) (ModData.INDUSTRY[i][ModData.N1A])).toString() + " " + game.getCommodity(ModData.INDUSTRY[i][ModData.N1]).getName());
            } else {
                verbrauch1.setText("---");
            }
            if (ModData.INDUSTRY[i][ModData.N2] != -1) {
                verbrauch2.setText(((Integer) (ModData.INDUSTRY[i][ModData.N2A])).toString() + " " + game.getCommodity(ModData.INDUSTRY[i][ModData.N2]).getName());
            } else {
                verbrauch2.setText("---");
            }
            if (ModData.INDUSTRY[i][ModData.N3] != -1) {
                verbrauch3.setText(((Integer) (ModData.INDUSTRY[i][ModData.N3A])).toString() + " " + game.getCommodity(ModData.INDUSTRY[i][ModData.N3]).getName());
            } else {
                verbrauch3.setText("---");
            }
            JLabel dauer = new JLabel(((Integer) (ModData.INDUSTRY[i][ModData.DURATION])).toString());
            JLabel unterhalt = new JLabel(((Integer) (ModData.INDUSTRY[i][ModData.MAINTENANCE])).toString());
            JLabel bauzeit = new JLabel(((Integer) (ModData.INDUSTRY[i][ModData.BUILDING_TIME])).toString());
            GridBagLayout popuplayout = new GridBagLayout();
            popup[i].getContentPane().setLayout(popuplayout);
            popup[i].getContentPane().add(new JLabel(Strings.getString("GUI.48")), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            popup[i].getContentPane().add(bauzeit, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            popup[i].getContentPane().add(new JLabel(Strings.getString("GUI.38")), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            popup[i].getContentPane().add(unterhalt, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            popup[i].getContentPane().add(new JLabel(Strings.getString("GUI.39")), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            popup[i].getContentPane().add(dauer, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            popup[i].getContentPane().add(new JLabel(Strings.getString("GUI.36") + "    "), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            popup[i].getContentPane().add(new JLabel(Strings.getString("GUI.40")), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            popup[i].getContentPane().add(produkt1, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            popup[i].getContentPane().add(produkt2, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            popup[i].getContentPane().add(produkt3, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            popup[i].getContentPane().add(verbrauch1, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            popup[i].getContentPane().add(verbrauch2, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            popup[i].getContentPane().add(verbrauch3, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        }
        return popup[i];
    }

    private JLabel getIndpreis(int i) {
        if (indpreis[i] == null) {
            indpreis[i] = new JLabel();
            indpreis[i].setText(((Integer) (ModData.INDUSTRY[i][ModData.PRICE])).toString());
        }
        return indpreis[i];
    }

    private JButton getButtonkaufen(int i) {
        if (buttonkaufen[i] == null) {
            buttonkaufen[i] = new JButton();
            buttonkaufen[i].setText(Strings.getString("GUI.16"));
            buttonkaufen[i].setBackground(net.sf.econtycoon.core.data.ModData.PANELCOLOR_1);
            final int j = i;
            buttonkaufen[i].addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (game.getPlayer().getMoney() > ModData.INDUSTRY[j][ModData.PRICE]) {
                        game.getPlayer().buyIndustry(j);
                        gui.getTabverwaltung().industriebauen();
                        gui.guiUpdate();
                        gui.getTabstatistik().statistikUpdate(Tab_Statistik.VERBRAUCH, game.getPlayer());
                    }
                }
            });
        }
        return buttonkaufen[i];
    }

    private JScrollPane getJScrollPane1() {
        if (jScrollPane1 == null) {
            jScrollPane1 = new JScrollPane();
            jScrollPane1.setBackground(net.sf.econtycoon.core.data.ModData.PANELCOLOR_1);
            jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane1.setBorder(BorderFactory.createTitledBorder(null, Strings.getString("GUI.17"), TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14)));
            jScrollPane1.setViewportView(getJPanel8());
        }
        return jScrollPane1;
    }

    private JComboBox getJComboBox1() {
        if (jComboBox1 == null) {
            ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(new String[] { Strings.getString("Lager.0"), Strings.getString("Lager.1"), Strings.getString("Lager.2"), Strings.getString("Lager.3") });
            jComboBox1 = new JComboBox();
            jComboBox1.setBackground(net.sf.econtycoon.core.data.ModData.TEXTFIELD_COLOR);
            jComboBox1.setModel(jComboBox1Model);
            jComboBox1.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    JComboBox cb = (JComboBox) evt.getSource();
                    lagertyp = cb.getSelectedIndex();
                    lagerbuttonsupdate();
                }
            });
        }
        return jComboBox1;
    }

    /**
	 * Ver�ndert die Buttons beim Lagerkauf, je nach lagertyp und Lagerpreisfaktor
	 */
    public void lagerbuttonsupdate() {
        button_lager0.setText("+ " + ModData.STORAGESIZE[lagertyp][0]);
        button_lager1.setText("+ " + ModData.STORAGESIZE[lagertyp][1]);
        button_lager2.setText("+ " + ModData.STORAGESIZE[lagertyp][2]);
        label_lagerpreis0.setText("" + (int) ((ModData.STORAGEPRICE[lagertyp][0]) * game.getPlayer().getStoragePriceMultiplier()));
        label_lagerpreis1.setText("" + (int) ((ModData.STORAGEPRICE[lagertyp][1]) * game.getPlayer().getStoragePriceMultiplier()));
        label_lagerpreis2.setText("" + (int) ((ModData.STORAGEPRICE[lagertyp][2]) * game.getPlayer().getStoragePriceMultiplier()));
    }
}
