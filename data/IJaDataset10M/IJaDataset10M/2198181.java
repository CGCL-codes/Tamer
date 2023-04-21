package seneca.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.html.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;
import seneca.structgen.*;
import seneca.judges.*;
import seneca.core.*;
import compchem.*;

/**
 * This class provides a gui for configuring a FeatureJudge
 * Features of a structure to be judged are RingSize, Hetero-Hetero bonds 
 * and existence of aromaticity.
 */
public class FeatureJudgeConfigurator extends JudgeConfigurator {

    FeatureJudge featureJudge = null;

    JCheckBox hetHetCheckBox = new JCheckBox();

    FeatureJudgeConfigurator(SenecaDataset sd) {
        super(sd, (FeatureJudge) sd.getJudge("FeatureJudge"));
        this.featureJudge = (FeatureJudge) sd.getJudge("FeatureJudge");
    }

    protected Box contructCenterBox() {
        Box centerBox = new Box(BoxLayout.Y_AXIS);
        String title = "Ringsize Scores";
        Border border = BorderFactory.createEtchedBorder();
        JPanel ringPenaltyPanel = new JPanel();
        table = new JTable(new RingScoresTableModel());
        scrollpane = new JScrollPane(table);
        scrollpane.setBorder(BorderFactory.createTitledBorder(border, title));
        centerBox.add(scrollpane);
        title = "Hetero-Hetero Atom Bonds forbidden";
        hetHetCheckBox = new JCheckBox(title);
        System.out.println(hetHetCheckBox);
        hetHetCheckBox.addItemListener(new CheckBoxListener());
        centerBox.add(hetHetCheckBox);
        return centerBox;
    }

    public void autoconfigure() {
        featureJudge.addRingSizeScore(3, -500);
        featureJudge.addRingSizeScore(4, -300);
        featureJudge.setHetHetPenaltyEnabled(true);
    }

    protected void reactOnJudgeDataChange() {
        System.out.println("FeatureJudge data changed");
        table.revalidate();
        System.out.println(featureJudge.isHetHetPenaltyEnabled());
        if (featureJudge.isHetHetPenaltyEnabled() && !hetHetCheckBox.isSelected()) {
            System.out.println("doClick");
            hetHetCheckBox.doClick();
        }
        if (!featureJudge.isHetHetPenaltyEnabled() && hetHetCheckBox.isSelected()) {
            System.out.println("doClick");
            hetHetCheckBox.doClick();
        }
    }

    class RingScoresTableModel extends AbstractTableModel {

        /**
         *
         **/
        String[] names = { "Ring Size", "Score" };

        /**
         *
         **/
        RingScoresTableModel() {
        }

        /**
         *
         **/
        public int getColumnCount() {
            return names.length;
        }

        /**
         *
         **/
        public int getRowCount() {
            if (featureJudge == null) return 0;
            return featureJudge.ringSizeScores.size();
        }

        /**
         *
         **/
        public Object getValueAt(int row, int column) {
            if (featureJudge == null) return "";
            Enumeration en = featureJudge.ringSizeScores.keys();
            int count = 0;
            Integer key;
            do {
                key = (Integer) en.nextElement();
                if (row == count) {
                    if (column == 0) {
                        return key;
                    }
                    if (column == 1) {
                        return ((Integer) featureJudge.ringSizeScores.get(key));
                    }
                }
                count++;
            } while (en.hasMoreElements());
            return "-";
        }

        /**
         *
         **/
        public String getColumnName(int column) {
            return names[column];
        }

        /**
         *
         **/
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /**
         *
         **/
        public boolean isCellEditable(int row, int col) {
            if (col > 1) return true;
            return false;
        }

        /**
         *
         **/
        public void setValueAt(Object aValue, int row, int column) {
        }
    }

    class CheckBoxListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            System.out.println("ItemListener");
            Object source = e.getItemSelectable();
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                featureJudge.setHetHetPenaltyEnabled(false);
            }
            if (e.getStateChange() == ItemEvent.SELECTED) {
                featureJudge.setHetHetPenaltyEnabled(true);
            }
        }
    }
}
