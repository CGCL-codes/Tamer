package GUISwing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import Econtyc.Master;
import Econtyc.data.Strings;

/**
 * 
 * Erstellt den Startbildschirm
 * 
 * @author Konstantin
 */
public class Startbildschirm extends javax.swing.JWindow {

    private JButton buttonbeenden;

    private JButton jButton1;

    private JComboBox jComboBox_mod;

    private JButton jButton_GER;

    private JButton jButton_ENG;

    private JButton jButton2;

    private Master master;

    private Fenster_Optionen fensteroptionen;

    /**
	 * Instantiates a new startbildschirm.
	 *
	 * @param master the master
	 */
    public Startbildschirm(Master master) {
        super();
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        initGUI();
        this.master = master;
    }

    private void initGUI() {
        try {
            int sw = (int) (this.getToolkit().getScreenSize().getWidth());
            int sh = (int) (this.getToolkit().getScreenSize().getHeight());
            this.setLocation((int) ((sw - 720) / 2), (int) ((sh - 320) / 2));
            this.requestFocus();
            this.setContentPane(new BackGroundPane("econtyctitel.png"));
            this.setVisible(true);
            this.setPreferredSize(new java.awt.Dimension(722, 320));
            GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
            getContentPane().setLayout(thisLayout);
            thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().addContainerGap().addGroup(thisLayout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(getJButton_ENG(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE).addComponent(getJButton_GER(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addComponent(getJComboBox_mod(), GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addGap(20))).addGap(0, 217, Short.MAX_VALUE).addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(getButtonbeenden(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE).addComponent(getJButton1(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE).addComponent(getJButton2(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)).addContainerGap());
            thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup().addContainerGap().addGroup(thisLayout.createParallelGroup().addComponent(getJButton1(), GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addComponent(getJComboBox_mod(), GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addGap(22))).addGap(156).addComponent(getJButton2(), 0, 130, Short.MAX_VALUE).addGap(116).addGroup(thisLayout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(getJButton_ENG(), GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(getJButton_GER(), GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addGap(0, 40, Short.MAX_VALUE).addComponent(getButtonbeenden(), GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE))).addContainerGap());
            this.setMinimumSize(new java.awt.Dimension(720, 320));
            pack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JButton getButtonbeenden() {
        if (buttonbeenden == null) {
            buttonbeenden = new JButton();
            buttonbeenden.setText(Strings.getString("GUI.2"));
            buttonbeenden.setBounds(582, 268, 130, 41);
            buttonbeenden.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    System.exit(0);
                }
            });
        }
        return buttonbeenden;
    }

    private JButton getJButton1() {
        if (jButton1 == null) {
            jButton1 = new JButton();
            jButton1.setText(Strings.getString("GUI.11"));
            jButton1.setBounds(10, 268, 130, 41);
            jButton1.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    if (fensteroptionen != null) fensteroptionen.dispose();
                    master.start();
                }
            });
        }
        return jButton1;
    }

    private JButton getJButton2() {
        if (jButton2 == null) {
            jButton2 = new JButton();
            jButton2.setText(Strings.getString("GUI.14"));
            jButton2.setBounds(332, 268, 130, 41);
            jButton2.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    if (fensteroptionen == null) {
                        fensteroptionen = new Fenster_Optionen();
                        fensteroptionen.setVisible(true);
                        fensteroptionen.setSize(720, 270);
                    } else fensteroptionen.setVisible(true);
                }
            });
        }
        return jButton2;
    }

    private JButton getJButton_ENG() {
        if (jButton_ENG == null) {
            if (Master.OPTIONEN.getSprache().equals("ENG")) {
                jButton_ENG = new JButton(new ImageIcon(getClass().getClassLoader().getResource("GUISwing/Flaggen/ENG_selected.png")));
                jButton_ENG.setBounds(542, 11, 80, 40);
            } else {
                jButton_ENG = new JButton(new ImageIcon(getClass().getClassLoader().getResource("GUISwing/Flaggen/ENG.png")));
                jButton_ENG.setBounds(542, 11, 80, 40);
            }
            jButton_ENG.addMouseListener(new MouseAdapter() {

                public void mouseExited(MouseEvent evt) {
                    if (Master.OPTIONEN.getSprache() != "ENG") jButton_ENG.setIcon(new ImageIcon(getClass().getClassLoader().getResource("GUISwing/Flaggen/ENG.png")));
                }

                public void mouseEntered(MouseEvent evt) {
                    if (Master.OPTIONEN.getSprache() != "ENG") jButton_ENG.setIcon(new ImageIcon(getClass().getClassLoader().getResource("GUISwing/Flaggen/ENG_hell.png")));
                }
            });
            jButton_ENG.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    Master.OPTIONEN.setSprache("ENG");
                    jButton_ENG.setIcon(new ImageIcon(getClass().getClassLoader().getResource("GUISwing/Flaggen/ENG_selected.png")));
                    jButton_GER.setIcon(new ImageIcon(getClass().getClassLoader().getResource("GUISwing/Flaggen/GER.png")));
                }
            });
        }
        return jButton_ENG;
    }

    private JButton getJButton_GER() {
        if (jButton_GER == null) {
            if (Master.OPTIONEN.getSprache().equals("GER")) {
                jButton_GER = new JButton(new ImageIcon(getClass().getClassLoader().getResource("GUISwing/Flaggen/GER_selected.png")));
                jButton_GER.setBounds(632, 11, 80, 40);
            } else {
                jButton_GER = new JButton(new ImageIcon(getClass().getClassLoader().getResource("GUISwing/Flaggen/GER.png")));
                jButton_GER.setBounds(632, 11, 80, 40);
            }
            jButton_GER.addMouseListener(new MouseAdapter() {

                public void mouseExited(MouseEvent evt) {
                    if (Master.OPTIONEN.getSprache() != "GER") jButton_GER.setIcon(new ImageIcon(getClass().getClassLoader().getResource("GUISwing/Flaggen/GER.png")));
                }

                public void mouseEntered(MouseEvent evt) {
                    if (!Master.OPTIONEN.getSprache().equals("GER")) jButton_GER.setIcon(new ImageIcon(getClass().getClassLoader().getResource("GUISwing/Flaggen/GER_hell.png")));
                }
            });
            jButton_GER.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    Master.OPTIONEN.setSprache("GER");
                    jButton_GER.setIcon(new ImageIcon(getClass().getClassLoader().getResource("GUISwing/Flaggen/GER_selected.png")));
                    jButton_ENG.setIcon(new ImageIcon(getClass().getClassLoader().getResource("GUISwing/Flaggen/ENG.png")));
                }
            });
        }
        return jButton_GER;
    }

    private JComboBox getJComboBox_mod() {
        if (jComboBox_mod == null) {
            ComboBoxModel jComboBox_modModel = new DefaultComboBoxModel(new String[] { "Modern (Original)", "Zelda", "Barbie", "Complex (FIRS)" });
            jComboBox_mod = new JComboBox();
            jComboBox_mod.setModel(jComboBox_modModel);
            jComboBox_mod.setBounds(10, 11, 108, 20);
            jComboBox_mod.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    JComboBox cb = (JComboBox) evt.getSource();
                    int mod = cb.getSelectedIndex();
                    Master.OPTIONEN.setMod(mod);
                }
            });
        }
        return jComboBox_mod;
    }

    /**
	 * The Class BackGroundPane.
	 */
    class BackGroundPane extends JPanel {

        Image img = null;

        /**
         * Instantiates a new back ground pane.
         *
         * @param imagefile the imagefile
         */
        BackGroundPane(String imagefile) {
            if (imagefile != null) {
                MediaTracker mt = new MediaTracker(this);
                img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("GUISwing/" + imagefile));
                mt.addImage(img, 0);
                try {
                    mt.waitForAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}
