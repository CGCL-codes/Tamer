package Maquette_Oana_Zak;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class PlanificationPanel extends javax.swing.JPanel {

    {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * The default serial id of the current class
	 */
    private static final long serialVersionUID = 1L;

    private JPanel jPanel9;

    private JLabel jLabel12;

    private JLabel jLabel5;

    private JComboBox ueComboBox;

    private JButton jButton3;

    private JComboBox jComboBox7;

    private JLabel jLabel11;

    private JButton jButton2;

    private JButton jButton1;

    private JPanel jPanel11;

    private JButton jButton18;

    private JLabel jLabel2;

    private JButton jButton17;

    private JLabel jLabel4;

    private JLabel jLabel1;

    private JCheckBox jCheckBox2;

    private JComboBox jComboBox6;

    private JLabel jLabel9;

    private JComboBox groupeComboBox;

    private JLabel jLabel10;

    private JComboBox jComboBox2;

    private JLabel jLabel8;

    private JLabel ueLabel;

    private JComboBox jComboBox4;

    private JComboBox jComboBox5;

    private JLabel jLabel6;

    private JCheckBox jCheckBox5;

    private JCheckBox jCheckBox4;

    private JCheckBox jCheckBox3;

    private JCheckBox jCheckBox1;

    private JLabel jLabel7;

    private JComboBox jComboBox8;

    private JPanel jPanelTimetable;

    private JPanel jPanelDays;

    private JPanel graphicalTimetable;

    private MenuPeriodePleine jPopupMenu1;

    /**
	 * The number of days in the timetable
	 */
    private static int nbTimetableDays = 6;

    /**
	 * The number of cells in a column of the timetable
	 */
    private static int nbColumnCells = 6;

    /**
	 * The height of a panel representing a day
	 */
    private static int jPanelDayHeight = 20;

    /**
	 * The height of a panel representing a cell of the timetable
	 */
    private static int jPanelCellHeight = 30;

    /**
	 * The width of a panel representing a cell of the timetable
	 */
    private static int jPanelCellWidth = 110;

    /**
	 * The height of the timetable panel
	 */
    private static final int timetableHeight = nbColumnCells * jPanelCellHeight + jPanelDayHeight;

    /**
	 * The width of the timetable panel
	 */
    private static final int timetableWidth = nbTimetableDays * jPanelCellWidth;

    /**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new PlanificationPanel());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public PlanificationPanel() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            GroupLayout thisLayout = new GroupLayout((JComponent) this);
            this.setLayout(thisLayout);
            setPreferredSize(new Dimension(800, 600));
            {
                jPanel9 = new JPanel();
                GroupLayout jPanel9Layout = new GroupLayout((JComponent) jPanel9);
                jPanel9.setBorder(BorderFactory.createTitledBorder("General"));
                jPanel9.setLayout(jPanel9Layout);
                {
                    jLabel12 = new JLabel();
                    jLabel12.setText("Semaine");
                }
                {
                    ComboBoxModel jComboBox8Model = new DefaultComboBoxModel(new String[] { "Choisir", "Semaine 1 25/01/2010-31/01/2010", "Semaine 2: 01/02/2010-07/02/2010", "Semaine 3: 08/02/2010-14/02/2010", "Semaine 4: 22/02/2010-28/02/2010", "Semaine 5: 01/03/2010-07/03/2010", "Semaine 6: 08/03/2010-14/03/2010" });
                    jComboBox8 = new JComboBox();
                    jComboBox8.setModel(jComboBox8Model);
                }
                {
                    jLabel5 = new JLabel();
                    jLabel5.setText("Formation");
                }
                {
                    jLabel7 = new JLabel();
                    jLabel7.setText("Annee d'etude");
                }
                {
                    jCheckBox1 = new JCheckBox();
                    jCheckBox1.setText("1");
                }
                {
                    jCheckBox2 = new JCheckBox();
                    jCheckBox2.setText("2");
                }
                {
                    jCheckBox3 = new JCheckBox();
                    jCheckBox3.setText("3");
                }
                {
                    jCheckBox4 = new JCheckBox();
                    jCheckBox4.setText("4");
                }
                {
                    jCheckBox5 = new JCheckBox();
                    jCheckBox5.setText("5");
                }
                {
                    jLabel6 = new JLabel();
                    jLabel6.setText("Semestre");
                }
                {
                    ComboBoxModel jComboBox5Model = new DefaultComboBoxModel(new String[] { "Choisir", "Semestre 5", "Semestre 6", "Semestre 7", "Semestre 8", "Semestre 9", "Semestre 10" });
                    jComboBox5 = new JComboBox();
                    jComboBox5.setModel(jComboBox5Model);
                    jComboBox5.setBounds(343, 26, 103, 18);
                }
                {
                    ComboBoxModel jComboBox4Model = new DefaultComboBoxModel(new String[] { "Choisir", "IG", "PEIP", "STIA", "FQSC", "ERII" });
                    jComboBox4 = new JComboBox();
                    jComboBox4.setModel(jComboBox4Model);
                    jComboBox4.setBounds(343, 26, 103, 18);
                }
                {
                    jLabel1 = new JLabel();
                    jLabel1.setText("2009-2010");
                }
                {
                    jLabel4 = new JLabel();
                    jLabel4.setText("Annee universitaire");
                }
                jPanel9Layout.setHorizontalGroup(jPanel9Layout.createSequentialGroup().addContainerGap().addGroup(jPanel9Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup().addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE).addGap(42)).addComponent(jLabel4, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel9Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup().addComponent(jComboBox4, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE).addGap(35).addGroup(jPanel9Layout.createParallelGroup().addComponent(jComboBox5, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE).addGroup(GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup().addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE).addGap(80))).addGap(16)).addGroup(GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup().addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE).addGap(11).addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox2, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox3, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox4, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox5, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE).addGroup(jPanel9Layout.createParallelGroup().addComponent(jComboBox8, GroupLayout.Alignment.LEADING, 0, 226, Short.MAX_VALUE).addGroup(GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup().addGap(0, 119, Short.MAX_VALUE).addComponent(jLabel12, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE).addGap(50))).addContainerGap());
                jPanel9Layout.setVerticalGroup(jPanel9Layout.createSequentialGroup().addGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jLabel12, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel6, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel5, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel4, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jComboBox8, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jComboBox5, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE).addComponent(jComboBox4, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel1, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jCheckBox1, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel7, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jCheckBox2, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jCheckBox3, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jCheckBox4, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jCheckBox5, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED));
            }
            {
                jButton17 = new JButton();
                jButton17.setText("<");
                jButton17.setBounds(464, 109, 41, 23);
            }
            {
                jLabel2 = new JLabel();
                jLabel2.setText("Semaine 1 / 01/09/2010 - 07/09/2010");
                jLabel2.setHorizontalAlignment(0);
                jLabel2.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
                jLabel2.setBounds(200, 95, 88, 14);
            }
            {
                jButton18 = new JButton();
                jButton18.setText(">");
                jButton18.setBounds(295, 88, 41, 23);
            }
            {
                jPanel11 = new JPanel();
                GroupLayout jPanel11Layout = new GroupLayout((JComponent) jPanel11);
                jPanel11.setBorder(BorderFactory.createTitledBorder("Criteres de visualisation"));
                jPanel11.setLayout(jPanel11Layout);
                {
                    jButton1 = new JButton();
                    jButton1.setText("Imprimer");
                }
                {
                    jButton2 = new JButton();
                    jButton2.setText("R�initialiser");
                }
                {
                    jLabel11 = new JLabel();
                    jLabel11.setText("Type d'enseignement");
                }
                {
                    ComboBoxModel jComboBox7Model = new DefaultComboBoxModel(new String[] { "Choisir", "Cours", "TD", "TP" });
                    jComboBox7 = new JComboBox();
                    jComboBox7.setModel(jComboBox7Model);
                }
                {
                    jButton3 = new JButton();
                    jButton3.setText("Visualiser");
                }
                {
                    ComboBoxModel ueComboBoxModel = new DefaultComboBoxModel(new String[] { "Choisir", "Genie logiciel", "Mathematiques de la decision et optimisation", "Systemes d'Information et Base de Donnees", "Information et Entreprises" });
                    ueComboBox = new JComboBox();
                    ueComboBox.setModel(ueComboBoxModel);
                }
                {
                    ueLabel = new JLabel();
                    ueLabel.setText("UE");
                }
                {
                    jLabel8 = new JLabel();
                    jLabel8.setText("Enseignant");
                }
                {
                    ComboBoxModel jComboBox2Model = new DefaultComboBoxModel(new String[] { "Choisir", "Tiberiu STRATULAT", "Michel SALA", "Vincent BERRY", "Anne LAURENT", "Lisa DI JORIO" });
                    jComboBox2 = new JComboBox();
                    jComboBox2.setModel(jComboBox2Model);
                }
                {
                    jLabel10 = new JLabel();
                    jLabel10.setText("Groupe");
                }
                {
                    ComboBoxModel groupeComboBoxModel = new DefaultComboBoxModel(new String[] { "Tous", "Groupe 1", "Groupe 2", "Groupe 3" });
                    groupeComboBox = new JComboBox();
                    groupeComboBox.setModel(groupeComboBoxModel);
                }
                {
                    jLabel9 = new JLabel();
                    jLabel9.setText("Salle");
                }
                {
                    ComboBoxModel jComboBox6Model = new DefaultComboBoxModel(new String[] { "Choisir", "005", "101", "202" });
                    jComboBox6 = new JComboBox();
                    jComboBox6.setModel(jComboBox6Model);
                }
                jPanel11Layout.setHorizontalGroup(jPanel11Layout.createSequentialGroup().addContainerGap().addGroup(jPanel11Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup().addComponent(jLabel10, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE).addGap(27)).addComponent(jLabel8, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel11Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup().addComponent(groupeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addGap(0, 38, Short.MAX_VALUE).addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE).addGap(10)).addComponent(jComboBox2, GroupLayout.Alignment.LEADING, 0, 151, Short.MAX_VALUE)).addGroup(jPanel11Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup().addComponent(jComboBox6, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addGap(0, 47, GroupLayout.PREFERRED_SIZE).addComponent(jLabel11, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE).addGap(21).addComponent(jComboBox7, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)).addGroup(jPanel11Layout.createSequentialGroup().addGap(39).addGroup(jPanel11Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup().addComponent(ueLabel, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(ueComboBox, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)).addGroup(GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup().addPreferredGap(ueLabel, jButton3, LayoutStyle.ComponentPlacement.INDENT).addComponent(jButton3, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE).addGap(140))))).addGap(41).addGroup(jPanel11Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup().addComponent(jButton2, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)).addComponent(jButton1, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)).addContainerGap(31, 31));
                jPanel11Layout.setVerticalGroup(jPanel11Layout.createSequentialGroup().addContainerGap().addGroup(jPanel11Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jButton2, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jComboBox2, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE).addComponent(ueLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).addComponent(ueComboBox, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addComponent(jLabel8, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(15).addGroup(jPanel11Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jButton1, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE).addComponent(jLabel11, GroupLayout.Alignment.BASELINE, 0, 19, Short.MAX_VALUE).addComponent(groupeComboBox, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE).addComponent(jLabel9, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 12, GroupLayout.PREFERRED_SIZE).addComponent(jComboBox6, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE).addComponent(jComboBox7, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE).addComponent(jLabel10, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 11, GroupLayout.PREFERRED_SIZE)).addGap(15).addComponent(jButton3, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED));
            }
            defineJPanelTimetable();
            this.add(jPanelTimetable);
            thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().addContainerGap().addComponent(jPanel9, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE).addGap(18).addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jButton17, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel2, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE).addComponent(jButton18, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jPanelTimetable, 0, 264, Short.MAX_VALUE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jPanel11, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE).addContainerGap());
            thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup().addContainerGap().addGroup(thisLayout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addComponent(jPanel9, GroupLayout.PREFERRED_SIZE, 726, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 0, Short.MAX_VALUE)).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addComponent(jPanel11, GroupLayout.PREFERRED_SIZE, 724, GroupLayout.PREFERRED_SIZE).addGap(0, 7, Short.MAX_VALUE)).addGroup(thisLayout.createSequentialGroup().addGap(71).addGroup(thisLayout.createParallelGroup().addGroup(thisLayout.createSequentialGroup().addComponent(jPanelTimetable, GroupLayout.PREFERRED_SIZE, 660, GroupLayout.PREFERRED_SIZE).addGap(0, 0, Short.MAX_VALUE)).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addGap(162).addComponent(jButton17, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE).addGap(32).addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE).addGap(33).addComponent(jButton18, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE).addGap(0, 99, Short.MAX_VALUE))))).addContainerGap(57, 57));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildTimeTable() {
        int i, j;
        JPanel cell;
        JLabel text;
        String[] daysLabel = { "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi" };
        if (jPanelDays == null) {
            jPanelDays = new JPanel();
            jPanelDays.setLayout(new GridLayout(1, 6));
            jPanelDays.setPreferredSize(new java.awt.Dimension(timetableWidth, jPanelDayHeight));
            for (j = 1; j <= nbTimetableDays; j++) {
                cell = new JPanel();
                cell.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
                cell.setLayout(new BorderLayout());
                cell.setBackground(Color.white);
                cell.setPreferredSize(new Dimension(jPanelCellWidth, jPanelDayHeight));
                text = new JLabel();
                text.setBackground(Color.white);
                text.setPreferredSize(new Dimension(jPanelCellWidth, jPanelDayHeight));
                cell.add(text, BorderLayout.CENTER);
                jPanelDays.add(cell);
            }
            for (j = 0; j < nbTimetableDays; j++) {
                ((JLabel) ((JPanel) jPanelDays.getComponent(j)).getComponent(0)).setText(daysLabel[j]);
                ((JLabel) ((JPanel) jPanelDays.getComponent(j)).getComponent(0)).setHorizontalAlignment(0);
            }
        }
        if (graphicalTimetable == null) {
            graphicalTimetable = new JPanel();
            jPanelTimetable.add(graphicalTimetable);
            graphicalTimetable.setLayout(new GridLayout(6, 6));
            graphicalTimetable.setPreferredSize(new java.awt.Dimension(timetableWidth, timetableHeight - jPanelDayHeight));
            for (i = 1; i <= nbColumnCells; i++) {
                for (j = 1; j <= nbTimetableDays; j++) {
                    cell = new JPanel();
                    cell.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
                    cell.setLayout(new BorderLayout());
                    cell.setBackground(Color.gray);
                    cell.setPreferredSize(new Dimension(jPanelCellWidth, jPanelCellHeight));
                    text = new JLabel();
                    text.setBackground(Color.gray);
                    text.setPreferredSize(new Dimension(jPanelCellWidth, jPanelCellHeight));
                    cell.add(text, BorderLayout.CENTER);
                    graphicalTimetable.add(cell);
                    jPopupMenu1 = new MenuPeriodePleine();
                    setComponentPopupMenu(jPopupMenu1);
                }
            }
        }
        jPanelTimetable.add(jPanelDays, BorderLayout.NORTH);
        jPanelTimetable.add(graphicalTimetable, BorderLayout.SOUTH);
    }

    /**
	 * Defines the "timetable" tab
	 */
    private void defineJPanelTimetable() {
        if (jPanelTimetable == null) {
            jPanelTimetable = new JPanel();
            jPanelTimetable.setLayout(new BorderLayout());
            jPanelTimetable.setPreferredSize(new Dimension(timetableWidth, timetableHeight));
            buildTimeTable();
        }
    }
}
