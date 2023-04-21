package fi.kaila.suku.swing.util;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import fi.kaila.suku.util.Resurses;

/**
 * Surety field component.
 * 
 * @author Kalle
 */
public class SukuSuretyField extends JPanel {

    private static final long serialVersionUID = 1L;

    /** The surety. */
    JComboBox surety;

    /**
	 * constructor to setup from resources.
	 */
    public SukuSuretyField() {
        setLayout(null);
        String[] suretys = Resurses.getString("DATA_SURETY_VALUES").split(";");
        surety = new JComboBox(suretys);
        add(surety);
        surety.setBounds(0, 0, 100, 20);
    }

    /**
	 * Gets the surety.
	 * 
	 * @return surtety value [0,20,40,60,80,100]
	 */
    public int getSurety() {
        return (5 - surety.getSelectedIndex()) * 20;
    }

    /**
	 * Sets the surety.
	 * 
	 * @param value
	 *            surety value [0,20,40,60,80,100]
	 */
    public void setSurety(int value) {
        int idx = 5 - (value + 10) / 20;
        surety.setSelectedIndex(idx);
    }
}
