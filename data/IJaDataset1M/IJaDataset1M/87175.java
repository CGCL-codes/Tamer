package GUISwing;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.border.BevelBorder;
import Econtyc.Master;
import Econtyc.Spiel;
import Econtyc.data.Strings;

/**
 *
 * Hauptinstanz der Graphischen Benutzeroberfl�che;
 * Erzeugt den Hauptframe mit Men�, Tabs und der unteren Leiste
 * 
 * @author Konstantin
 */
public class GUI {

    private AboutJDialog aboutjDialog = null;

    private HilfeJDialog hilfejDialog = null;

    private JMenuItem aboutMenuItem = null;

    private JMenuItem hilfeMenuItem = null;

    private JMenuItem exitMenuItem = null;

    private Fenster_Optionen fensteroptionen;

    private JMenu fileMenu = null;

    private JMenu helpMenu = null;

    private JPanel jContentPane = null;

    private JFrame jFrame = null;

    private JMenuBar jJMenuBar = null;

    private JPanel jPanel2;

    private Tab_Bauen jPanel3;

    private JTabbedPane jTabbedPane1;

    private konsole.Console konsole;

    private JLabel labelgeld = null;

    private Master master;

    private JMenuItem menu_Datei_Laden;

    private JMenuItem menu_Datei_NeuesSpiel = null;

    private JMenuItem menu_Datei_Optionen;

    private JMenuItem menu_datei_pause;

    private JMenuItem menu_Datei_Speichern;

    private JMenuItem menuitemkonsole;

    private Spiel spiel;

    private Tab_AI tabai;

    private Tab_Rohstoffe tabrohstoffe;

    private Tab_Statistik tabstatistik;

    private Tab_Verwaltung tabverwaltung;

    private JTextField tfgeld = null;

    private JTextField tfnachrichten = null;

    /**
	 * Konstruiert den GUI und aktiviert die Konsole
	 *
	 * @param master 
	 * @param spiel Das Spiel, das der GUI anzeigt
	 */
    public GUI(Master master, Spiel spiel) {
        this.master = master;
        this.spiel = spiel;
        getJFrame().requestFocus();
        getJFrame().setVisible(true);
        konsole = new konsole.Console();
        konsole.getFrame().dispose();
    }

    /**
	 * This method initializes aboutDialog	
	 * 	
	 * @return javax.swing.JDialog
	 */
    private AboutJDialog getAboutJDialog() {
        if (aboutjDialog == null) {
            aboutjDialog = new AboutJDialog(getJFrame());
        }
        return aboutjDialog;
    }

    /**
	 * This method initializes hilfeDialog	
	 * 	
	 * @return javax.swing.JDialog
	 */
    private HilfeJDialog getHilfeJDialog() {
        if (hilfejDialog == null) {
            hilfejDialog = new HilfeJDialog(getJFrame());
        }
        return hilfejDialog;
    }

    /**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getHilfeMenuItem() {
        if (hilfeMenuItem == null) {
            hilfeMenuItem = new JMenuItem();
            hilfeMenuItem.setBackground(Econtyc.data.Werte.TFFARBE);
            hilfeMenuItem.setText(Strings.getString("GUI.0"));
            hilfeMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    HilfeJDialog hilfeDialog = getHilfeJDialog();
                    Point loc = getJFrame().getLocation();
                    loc.translate(20, 20);
                    hilfeDialog.setLocation(loc);
                    hilfeDialog.setVisible(true);
                }
            });
        }
        return hilfeMenuItem;
    }

    /**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getAboutMenuItem() {
        if (aboutMenuItem == null) {
            aboutMenuItem = new JMenuItem();
            aboutMenuItem.setBackground(Econtyc.data.Werte.TFFARBE);
            aboutMenuItem.setText(Strings.getString("GUI.1"));
            aboutMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    AboutJDialog aboutDialog = getAboutJDialog();
                    Point loc = getJFrame().getLocation();
                    loc.translate(20, 20);
                    aboutDialog.setLocation(loc);
                    aboutDialog.setVisible(true);
                }
            });
        }
        return aboutMenuItem;
    }

    /**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getExitMenuItem() {
        if (exitMenuItem == null) {
            exitMenuItem = new JMenuItem();
            exitMenuItem.setBackground(Econtyc.data.Werte.TFFARBE);
            exitMenuItem.setText(Strings.getString("GUI.2"));
            exitMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        }
        return exitMenuItem;
    }

    /**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
    private JMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = new JMenu();
            fileMenu.setBackground(Econtyc.data.Werte.TFFARBE);
            fileMenu.setText(Strings.getString("GUI.3"));
            fileMenu.add(getMenu_Datei_NeuesSpiel());
            fileMenu.add(getMenu_datei_pause());
            fileMenu.add(getMenu_Datei_Speichern());
            fileMenu.add(getMenu_Datei_Laden());
            fileMenu.add(getMenu_Datei_Optionen());
            fileMenu.add(getExitMenuItem());
        }
        return fileMenu;
    }

    /**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
    private JMenu getHelpMenu() {
        if (helpMenu == null) {
            helpMenu = new JMenu();
            helpMenu.setBackground(Econtyc.data.Werte.TFFARBE);
            helpMenu.setText(Strings.getString("GUI.0"));
            helpMenu.add(getHilfeMenuItem());
            helpMenu.add(getAboutMenuItem());
            helpMenu.add(getMenuitemkonsole());
        }
        return helpMenu;
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setBackground(Econtyc.data.Werte.TFFARBE);
            GroupLayout jContentPaneLayout = new GroupLayout((JComponent) jContentPane);
            jContentPane.setLayout(jContentPaneLayout);
            jContentPane.setSize(800, 600);
            jContentPane.setPreferredSize(new java.awt.Dimension(800, 600));
            jContentPane.setBackground(Econtyc.data.Werte.HINTERGRUNDFARBE);
            jContentPaneLayout.setHorizontalGroup(jContentPaneLayout.createSequentialGroup().addContainerGap().addGroup(jContentPaneLayout.createParallelGroup().addComponent(getJTabbedPane1(), GroupLayout.Alignment.LEADING, 0, 772, Short.MAX_VALUE).addComponent(getJPanel2(), GroupLayout.Alignment.CENTER, 0, 772, Short.MAX_VALUE)).addContainerGap());
            jContentPaneLayout.setVerticalGroup(jContentPaneLayout.createSequentialGroup().addContainerGap().addComponent(getJTabbedPane1(), 0, 477, Short.MAX_VALUE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(getJPanel2(), GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE).addContainerGap());
        }
        return jContentPane;
    }

    /**
	 * This method initializes jFrame.
	 *
	 * @return javax.swing.JFrame
	 */
    public JFrame getJFrame() {
        if (jFrame == null) {
            jFrame = new JFrame();
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.setJMenuBar(getJJMenuBar());
            jFrame.setTitle("Econtycoon");
            jFrame.setPreferredSize(new java.awt.Dimension(980, 800));
            jFrame.setSize(980, 800);
            jFrame.setContentPane(getJContentPane());
        }
        return jFrame;
    }

    /**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getFileMenu());
            jJMenuBar.add(getHelpMenu());
            jJMenuBar.setBackground(Econtyc.data.Werte.TFFARBE);
        }
        return jJMenuBar;
    }

    /**
	 * This method initializes aboutContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private Tab_Rohstoffe getJPanel1() {
        if (tabrohstoffe == null) {
            tabrohstoffe = new Tab_Rohstoffe(this, spiel);
            tabrohstoffe.setPreferredSize(new java.awt.Dimension(767, 508));
            tabrohstoffe.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
            tabrohstoffe.addComponentListener(new ComponentAdapter() {

                public void componentShown(ComponentEvent evt) {
                    tabrohstoffe.tfUpdate();
                }
            });
        }
        return tabrohstoffe;
    }

    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            jPanel2 = new JPanel();
            jPanel2.setBackground(Econtyc.data.Werte.PANELFARBE1);
            GroupLayout jPanel2Layout = new GroupLayout((JComponent) jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            jPanel2.setMaximumSize(new java.awt.Dimension(32767, 45));
            {
                labelgeld = new JLabel();
                labelgeld.setBounds(new Rectangle(660, 510, 46, 31));
                labelgeld.setText(Strings.getString("GUI.4"));
            }
            jPanel2Layout.setHorizontalGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(getTfnachrichten(), 0, 757, Short.MAX_VALUE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(labelgeld, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(getTfgeld(), 0, 130, Short.MAX_VALUE).addContainerGap());
            jPanel2Layout.setVerticalGroup(jPanel2Layout.createSequentialGroup().addGap(6).addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(getTfnachrichten(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(labelgeld, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(getTfgeld(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)).addGap(6));
        }
        return jPanel2;
    }

    private Tab_Bauen getJPanel3() {
        if (jPanel3 == null) {
            jPanel3 = new Tab_Bauen(this, spiel);
            jPanel3.setPreferredSize(new java.awt.Dimension(767, 334));
            jPanel3.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
        }
        return jPanel3;
    }

    private Tab_AI getJPanel6() {
        if (tabai == null) {
            tabai = new Tab_AI(this, spiel);
            GroupLayout jPanel6Layout = new GroupLayout((JComponent) tabai);
            tabai.setPreferredSize(new java.awt.Dimension(767, 365));
            tabai.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
            jPanel6Layout.setVerticalGroup(jPanel6Layout.createSequentialGroup());
            jPanel6Layout.setHorizontalGroup(jPanel6Layout.createSequentialGroup());
            tabai.addComponentListener(new ComponentAdapter() {

                public void componentShown(ComponentEvent evt) {
                    tabai.tfUpdate();
                }
            });
        }
        return tabai;
    }

    private JTabbedPane getJTabbedPane1() {
        if (jTabbedPane1 == null) {
            jTabbedPane1 = new JTabbedPane();
            jTabbedPane1.setBackground(Econtyc.data.Werte.PANELFARBE1);
            jTabbedPane1.setBorder(BorderFactory.createCompoundBorder(null, null));
            jTabbedPane1.addTab(Strings.getString("GUI.5"), null, getJPanel1(), null);
            jTabbedPane1.addTab(Strings.getString("GUI.6"), null, getJPanel3(), null);
            jTabbedPane1.addTab(Strings.getString("GUI.7"), null, getTabverwaltung(), null);
            jTabbedPane1.addTab(Strings.getString("GUI.8"), null, getTabstatistik(), null);
            if (master.getSpiel().getSpielerzahl() >= 2) jTabbedPane1.addTab("AI", null, getJPanel6(), null);
        }
        return jTabbedPane1;
    }

    private JMenuItem getMenu_Datei_Laden() {
        if (menu_Datei_Laden == null) {
            menu_Datei_Laden = new JMenuItem();
            menu_Datei_Laden.setBackground(Econtyc.data.Werte.TFFARBE);
            menu_Datei_Laden.setText(Strings.getString("GUI.10"));
            menu_Datei_Laden.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    master.laden();
                }
            });
        }
        return menu_Datei_Laden;
    }

    /**
	 * This method initializes menu_Datei_NeuesSpiel	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getMenu_Datei_NeuesSpiel() {
        if (menu_Datei_NeuesSpiel == null) {
            menu_Datei_NeuesSpiel = new JMenuItem();
            menu_Datei_NeuesSpiel.setBackground(Econtyc.data.Werte.TFFARBE);
            menu_Datei_NeuesSpiel.setText(Strings.getString("GUI.11"));
            menu_Datei_NeuesSpiel.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    master.neuesSpiel();
                }
            });
        }
        return menu_Datei_NeuesSpiel;
    }

    private JMenuItem getMenu_Datei_Optionen() {
        if (menu_Datei_Optionen == null) {
            menu_Datei_Optionen = new JMenuItem();
            menu_Datei_Optionen.setBackground(Econtyc.data.Werte.TFFARBE);
            menu_Datei_Optionen.setText(Strings.getString("Optionen.0"));
            menu_Datei_Optionen.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    if (fensteroptionen == null) {
                        fensteroptionen = new Fenster_Optionen();
                        fensteroptionen.setVisible(true);
                        fensteroptionen.setSize(720, 270);
                    } else fensteroptionen.setVisible(true);
                }
            });
        }
        return menu_Datei_Optionen;
    }

    /**
	 * Gets the menu_datei_pause.
	 *
	 * @return the menu_datei_pause
	 */
    public JMenuItem getMenu_datei_pause() {
        if (menu_datei_pause == null) {
            menu_datei_pause = new JMenuItem();
            menu_datei_pause.setBackground(Econtyc.data.Werte.TFFARBE);
            menu_datei_pause.setText(Strings.getString("GUI.12"));
            menu_datei_pause.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    if (master.isPause()) master.setPause(false); else master.setPause(true);
                }
            });
        }
        return menu_datei_pause;
    }

    private JMenuItem getMenu_Datei_Speichern() {
        if (menu_Datei_Speichern == null) {
            menu_Datei_Speichern = new JMenuItem();
            menu_Datei_Speichern.setBackground(Econtyc.data.Werte.TFFARBE);
            menu_Datei_Speichern.setText(Strings.getString("GUI.13"));
            menu_Datei_Speichern.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    master.speichern();
                }
            });
        }
        return menu_Datei_Speichern;
    }

    private JMenuItem getMenuitemkonsole() {
        if (menuitemkonsole == null) {
            menuitemkonsole = new JMenuItem();
            menuitemkonsole.setBackground(Econtyc.data.Werte.TFFARBE);
            menuitemkonsole.setText(Strings.getString("Optionen.5"));
            menuitemkonsole.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    if (konsole == null) {
                        konsole = new konsole.Console();
                    } else {
                        konsole.getFrame().setVisible(true);
                        konsole.getFrame().requestFocus();
                    }
                }
            });
        }
        return menuitemkonsole;
    }

    /**
	 * Gets the spiel.
	 *
	 * @return the spiel
	 */
    public Spiel getSpiel() {
        return spiel;
    }

    /**
	 * Gets the tabstatistik.
	 *
	 * @return the tabstatistik
	 */
    public Tab_Statistik getTabstatistik() {
        if (tabstatistik == null) {
            tabstatistik = new Tab_Statistik(this, spiel);
            tabstatistik.addComponentListener(new ComponentAdapter() {

                public void componentShown(ComponentEvent evt) {
                    tabstatistik.statistikUpdate(Tab_Statistik.DPREIS, null);
                }
            });
        }
        return tabstatistik;
    }

    /**
	 * Gets the tabverwaltung.
	 *
	 * @return the tabverwaltung
	 */
    public Tab_Verwaltung getTabverwaltung() {
        if (tabverwaltung == null) {
            tabverwaltung = new Tab_Verwaltung(this, spiel);
            tabverwaltung.setPreferredSize(new java.awt.Dimension(767, 365));
            tabverwaltung.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
            tabverwaltung.addComponentListener(new ComponentAdapter() {

                public void componentShown(ComponentEvent evt) {
                    tabverwaltung.tfUpdate();
                }
            });
        }
        return tabverwaltung;
    }

    /**
	 * This method initializes tfgeld	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getTfgeld() {
        if (tfgeld == null) {
            tfgeld = new JTextField();
            tfgeld.setBackground(Econtyc.data.Werte.TFFARBE);
            tfgeld.setBounds(new Rectangle(705, 510, 76, 31));
            tfgeld.setText("0");
        }
        return tfgeld;
    }

    /**
	 * This method initializes tfnachrichten.
	 *
	 * @return javax.swing.JTextField
	 */
    public JTextField getTfnachrichten() {
        if (tfnachrichten == null) {
            tfnachrichten = new JTextField();
            tfnachrichten.setBackground(Econtyc.data.Werte.TFFARBE);
            tfnachrichten.setBounds(new Rectangle(15, 510, 631, 31));
        }
        return tfnachrichten;
    }

    /**
	 * Resetet den GUI (v.a. den Verwaltungstab) und l�d alle Textfelder neu
	 */
    public void reset() {
        tabverwaltung.reset();
        tabstatistik.reset();
        for (int i = 0; i < 5; i++) {
            tabstatistik.statistikUpdate(i, spiel.getSpieler());
        }
        tfUpdate();
    }

    /**
	 * Sets the nachrichtentext.
	 *
	 * @param nachricht the new nachrichtentext
	 */
    public void setNachrichtentext(String nachricht) {
        tfnachrichten.setText(nachricht);
    }

    /**
	 * Sets the spiel.
	 *
	 * @param spiel the new spiel
	 */
    public void setSpiel(Spiel spiel) {
        this.spiel = spiel;
    }

    /**
	 * Bringt alle Textfelder im aktiven Tab auf den neusten Stand.
	 */
    public void tfUpdate() {
        tfgeld.setText(((Integer) (master.getSpiel().getSpieler().getGeld())).toString());
        if (tabrohstoffe.isVisible()) tabrohstoffe.tfUpdate();
        if (tabverwaltung.isVisible()) tabverwaltung.tfUpdate();
        if (tabstatistik.isVisible()) {
            tabstatistik.statistikUpdate(Tab_Statistik.DPREIS, null);
            tabstatistik.statistikUpdate(Tab_Statistik.SPIELERRANKING, null);
            tabstatistik.statistikUpdate(Tab_Statistik.PRODUZIERTVERBRAUCHT, spiel.getSpieler());
        }
        if (tabai != null) if (tabai.isVisible()) tabai.tfUpdate();
    }
}
