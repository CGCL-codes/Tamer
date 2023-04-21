package Maquettes_Flo_Gilles;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * @author Gilles MISEREZ
 *
 */
public class PanelCalendrier extends JPanel {

    private JCheckBox checkBoxAE1;

    private JCheckBox checkBoxAE2;

    private JCheckBox checkBoxAE3;

    private JComboBox comboBoxFormations;

    private JTable tableCalendrier;

    private JScrollPane jScrollPane1;

    private JRadioButton radioButtonFerie;

    private ButtonGroup buttonGroup1;

    private JRadioButton radioButtonVacances;

    /**
	 * public constructor for this JPanel containing the GUI for the 4th phase of the createNewSchoolYear assistent
	 */
    public PanelCalendrier() {
        super();
        initGUI();
    }

    /**
	 * initialises this panel
	 */
    private void initGUI() {
        this.setLayout(null);
        {
            jScrollPane1 = new JScrollPane();
            this.add(jScrollPane1);
            jScrollPane1.setBounds(142, 11, 599, 372);
            {
                TableModel tableCalendrierModel = new DefaultTableModel(new String[][] { { "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1" }, { "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2" }, { "3", "3", "3", "3", "3", "3", "3", "3", "3", "3", "3", "3" }, { "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4" }, { "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5" }, { "6", "6", "6", "6", "6", "6", "6", "6", "6", "6", "6", "6" }, { "7", "7", "7", "7", "7", "7", "7", "7", "7", "7", "7", "7" }, { "8", "8", "8", "8", "8", "8", "8", "8", "8", "8", "8", "8" }, { "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9", "9" }, { "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10" }, { "11", "11", "11", "11", "11", "11", "11", "11", "11", "11", "11", "11" }, { "12", "12", "12", "12", "12", "12", "12", "12", "12", "12", "12", "12" }, { "13", "13", "13", "13", "13", "13", "13", "13", "13", "13", "13", "13" }, { "14", "14", "14", "14", "14", "14", "14", "14", "14", "14", "14", "14" }, { "15", "15", "15", "15", "15", "15", "15", "15", "15", "15", "15", "15" }, { "16", "16", "16", "16", "16", "16", "16", "16", "16", "16", "16", "16" }, { "17", "17", "17", "17", "17", "17", "17", "17", "17", "17", "17", "17" }, { "18", "18", "18", "18", "18", "18", "18", "18", "18", "18", "18", "18" }, { "19", "19", "19", "19", "19", "19", "19", "19", "19", "19", "19", "19" }, { "20", "20", "20", "20", "20", "20", "20", "20", "20", "20", "20", "20" }, { "21", "21", "21", "21", "21", "21", "21", "21", "21", "21", "21", "21" }, { "22", "22", "22", "22", "22", "22", "22", "22", "22", "22", "22", "22" }, { "23", "23", "23", "23", "23", "23", "23", "23", "23", "23", "23", "23" }, { "24", "24", "24", "24", "24", "24", "24", "24", "24", "24", "24", "24" }, { "25", "25", "25", "25", "25", "25", "25", "25", "25", "25", "25", "25" }, { "26", "26", "26", "26", "26", "26", "26", "26", "26", "26", "26", "26" }, { "27", "27", "27", "27", "27", "27", "27", "27", "27", "27", "27", "27" }, { "28", "28", "28", "28", "28", "28", "28", "28", "28", "28", "28", "28" }, { "29", "29", "29", "29", "29", "29", "29", "29", "29", "29", "29", "29" }, { "30", "30", "30", "30", "30", "30", "30", "30", "30", "30", "30", "30" }, { "31", "31", "31", "31", "31", "31", "31", "31", "31", "31", "31", "31" } }, new String[] { "Septembre", "Octobre", "Novembre", "D�cembre", "janvier", "F�vrier", "Mars", "Avril", "Mai", "Juin", "Juillet", "Ao�t" });
                tableCalendrier = new JTable();
                jScrollPane1.setViewportView(tableCalendrier);
                tableCalendrier.setModel(tableCalendrierModel);
                tableCalendrier.setBounds(139, 12, 599, 372);
                tableCalendrier.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            }
        }
        {
            ComboBoxModel comboBoxFormationsModel = new DefaultComboBoxModel(new String[] { "IG", "ERII" });
            comboBoxFormations = new JComboBox();
            this.add(comboBoxFormations);
            comboBoxFormations.setModel(comboBoxFormationsModel);
            comboBoxFormations.setBounds(10, 11, 111, 20);
        }
        {
            checkBoxAE3 = new JCheckBox();
            this.add(checkBoxAE3);
            checkBoxAE3.setText("IG5");
            checkBoxAE3.setBounds(26, 37, 43, 23);
        }
        {
            checkBoxAE2 = new JCheckBox();
            this.add(checkBoxAE2);
            checkBoxAE2.setText("IG4");
            checkBoxAE2.setBounds(26, 58, 43, 23);
        }
        {
            checkBoxAE1 = new JCheckBox();
            this.add(checkBoxAE1);
            checkBoxAE1.setText("IG3");
            checkBoxAE1.setBounds(26, 79, 43, 23);
        }
        {
            radioButtonVacances = new JRadioButton();
            this.add(radioButtonVacances);
            radioButtonVacances.setText("Vacances");
            radioButtonVacances.setBounds(12, 111, 109, 20);
        }
        {
            radioButtonFerie = new JRadioButton();
            this.add(radioButtonFerie);
            radioButtonFerie.setText("Feri�");
            radioButtonFerie.setBounds(12, 134, 109, 20);
        }
        {
            buttonGroup1 = new ButtonGroup();
            buttonGroup1.add(this.radioButtonVacances);
            buttonGroup1.add(this.radioButtonFerie);
        }
    }

    /**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(800, 600));
        frame.getContentPane().add(new PanelCalendrier());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
