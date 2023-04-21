package org.ghutchis.xtggui;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.ghutchis.xtgcore.TBHammingCheck;

/** Implements the GUI selector for the Hamming Code Generator
 * 
 * @author hutch
 *
 */
public class XTG_Hamchk extends XTG_Hamgen {

    TBHammingCheck hamchk;

    JCheckBox correct_box;

    public XTG_Hamchk(JPanel comp_con) {
        super(comp_con);
        name_field.setText("hamchk");
        comp_con.add(new JLabel("Data Correction"));
        correct_box = new JCheckBox();
        correct_box.setSelected(true);
        comp_con.add(correct_box);
    }

    public String generate() {
        int sz;
        try {
            sz = Integer.parseInt(bits_field.getText());
            hamchk = new TBHammingCheck(name_field.getText() + sz, sz, twobit_box.isSelected(), correct_box.isSelected());
            hamchk.build();
            return hamchk.toString();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Must enter an integer value");
            return "";
        }
    }

    public String getName() {
        return hamchk.getName();
    }
}
