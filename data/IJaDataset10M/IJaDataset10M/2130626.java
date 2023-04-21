package org.fudaa.fudaa.reflux;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import com.memoire.bu.BuGridLayout;

/**
 * Un panel de saisie des valeurs globales de solution initiales.
 *
 * @version      $Revision: 1.6 $ $Date: 2006-09-19 15:11:53 $ by $Author: deniger $
 * @author       Bertrand Marchand
 */
public class PRPnValeursGlobalesSI extends JPanel {

    BuGridLayout lyThis = new BuGridLayout();

    JTextField[] tfVals_ = new JTextField[0];

    /**
   * Cr�ation du panel.
   */
    public PRPnValeursGlobalesSI() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * M�thode d'impl�mentation de l'interface.
   */
    private void jbInit() throws Exception {
        lyThis.setColumns(2);
        lyThis.setHgap(5);
        lyThis.setVgap(5);
        this.setLayout(lyThis);
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    /**
   * Types des solutions initiales possibles.
   *
   * @param _types Les types de solutions initiales.
   */
    public void setTypesSI(int[] _types) {
        tfVals_ = new JTextField[_types.length];
        for (int i = 0; i < _types.length; i++) {
            add(new JLabel(Definitions.conditionToString(_types[i]) + ":", SwingConstants.RIGHT));
            add(tfVals_[i] = new JTextField("0.0", 12));
        }
    }

    public double[] getValeursGlobales() {
        double[] r = new double[tfVals_.length];
        for (int i = 0; i < tfVals_.length; i++) {
            try {
                r[i] = Double.parseDouble(tfVals_[i].getText());
            } catch (NumberFormatException _exc) {
                r[i] = 0;
            }
        }
        return r;
    }
}
