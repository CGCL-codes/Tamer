package net.sf.fractopt.ui.Graphe;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.sf.fractopt.core.db.DataBaseManager;
import net.sf.fractopt.core.entity.Player;

public class JpanelGrapheOptions extends JPanel {

    private JLabel jLabelPersonnage;

    private JComboBox jComboBoxPersonnage;

    private JPanel jPanelChoix;

    private JCheckBox jCheckBox4;

    private JCheckBox jCheckBox3;

    private JCheckBox jCheckBox2;

    private JCheckBox jCheckBox1;

    private Player selectedPlayer = null;

    private Vector<Player> playersList = null;

    public JpanelGrapheOptions() {
        playersList = new Vector(DataBaseManager.query(Player.class));
        if (playersList.size() > 0) {
            selectedPlayer = playersList.get(0);
        }
        initGUI();
    }

    private void initGUI() {
        try {
            GridBagLayout thisLayout = new GridBagLayout();
            thisLayout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
            thisLayout.rowHeights = new int[] { 7, 7, 7, 7 };
            thisLayout.columnWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
            thisLayout.columnWidths = new int[] { 7, 7, 7, 7 };
            this.setLayout(thisLayout);
            this.setPreferredSize(new java.awt.Dimension(283, 318));
            {
                jLabelPersonnage = new JLabel();
                this.add(jLabelPersonnage, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jLabelPersonnage.setText("Personnage");
            }
            {
                ComboBoxModel jComboBoxPersonnageModel = new DefaultComboBoxModel(playersList);
                jComboBoxPersonnage = new JComboBox();
                this.add(jComboBoxPersonnage, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jComboBoxPersonnage.setModel(jComboBoxPersonnageModel);
            }
            {
                jPanelChoix = new JPanel();
                GridLayout jPanelChoixLayout = new GridLayout(3, 3);
                jPanelChoixLayout.setHgap(5);
                jPanelChoixLayout.setVgap(5);
                jPanelChoixLayout.setColumns(1);
                jPanelChoix.setLayout(jPanelChoixLayout);
                this.add(jPanelChoix, new GridBagConstraints(1, 2, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                {
                    jCheckBox1 = new JCheckBox();
                    jPanelChoix.add(jCheckBox1);
                    jCheckBox1.setText("Eau");
                }
                {
                    jCheckBox2 = new JCheckBox();
                    jPanelChoix.add(jCheckBox2);
                    jCheckBox2.setText("M�dicament");
                }
                {
                    jCheckBox3 = new JCheckBox();
                    jPanelChoix.add(jCheckBox3);
                    jCheckBox3.setText("Nourriture");
                }
                {
                    jCheckBox4 = new JCheckBox();
                    jPanelChoix.add(jCheckBox4);
                    jCheckBox4.setText("Combat");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Player getSelectedPlayer() {
        return selectedPlayer;
    }
}
