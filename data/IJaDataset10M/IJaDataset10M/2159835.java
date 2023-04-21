package org.ghutchis.xtggui;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.ghutchis.xtgcore.TBGFMult;

/** Implements the GUI selector for the Galois Field Multiplier
 * @author hutch
 *
 */
public class XTG_GFMult extends XTG_Component {

    JTextField width_field, partial_field, name_field, poly_field;

    TBGFMult gfm;

    public XTG_GFMult(JPanel comp_con) {
        super(comp_con);
        comp_con.removeAll();
        comp_con.setLayout(new GridLayout(0, 2));
        comp_con.add(new JLabel("Module base name"));
        name_field = new JTextField();
        name_field.setText("gfmult");
        comp_con.add(name_field);
        comp_con.add(new JLabel("Multiplier width"));
        width_field = new JTextField("128");
        comp_con.add(width_field);
        comp_con.add(new JLabel("Partial Product Size"));
        partial_field = new JTextField("64");
        comp_con.add(partial_field);
        comp_con.add(new JLabel("Multiplier Polynomial"));
        poly_field = new JTextField("E1000000000000000000000000000000");
        comp_con.add(poly_field);
    }

    @Override
    public String generate() {
        int width, partial;
        try {
            width = Integer.parseInt(width_field.getText());
            partial = Integer.parseInt(partial_field.getText());
            gfm = new TBGFMult("gfmult" + width + "_" + partial, partial, width);
            gfm.setPoly(poly_field.getText());
            gfm.build();
            return gfm.toString();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Must enter an integer value");
            return "";
        }
    }

    @Override
    public String getName() {
        if (gfm == null) return "NoName"; else return gfm.getName();
    }
}
