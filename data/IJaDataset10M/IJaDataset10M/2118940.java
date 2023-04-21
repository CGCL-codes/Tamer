package fileshare.GUI.ont;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JPanel;

/**
 *
 * @author Eddy_User
 */
public class RetreiverPanel extends JPanel {

    private Vector<TextValueRetreiver> retreivers;

    /** Creates a new instance of RetreiverPane */
    public RetreiverPanel() {
        retreivers = new Vector();
    }

    public HashMap<String, String> getValues() {
        HashMap hm;
        hm = new HashMap<String, String>();
        System.out.println("returned values:");
        for (int i = 0; i < retreivers.size(); ++i) {
            hm.put(retreivers.get(i).getProperty(), retreivers.get(i).getValue());
        }
        return hm;
    }

    public void setValueForProperty(String p, String v) {
        for (int i = 0; i < retreivers.size(); ++i) {
            if (retreivers.get(i).getProperty().equals(p)) {
                retreivers.get(i).setValue(v);
                break;
            }
        }
    }

    public void addRetreiver(TextValueRetreiver r) {
        this.retreivers.add(r);
        initGraphic();
    }

    public void initGraphic() {
        this.setLayout(new java.awt.GridLayout(retreivers.size(), 1, 0, 1));
        for (int i = 0; i < retreivers.size(); ++i) {
            this.add(retreivers.get(i));
        }
    }

    public boolean kewordSet() {
        for (int i = 0; i < retreivers.size(); ++i) {
            if (retreivers.get(i).getProperty().equals("has_theme") && (!retreivers.get(i).getValue().trim().equals(""))) return true;
        }
        return false;
    }

    public RetreiverPanel clone() {
        RetreiverPanel cloned = new RetreiverPanel();
        String property;
        String label;
        for (int i = 0; i < retreivers.size(); ++i) {
            property = retreivers.get(i).getProperty();
            label = retreivers.get(i).getLabel();
            cloned.addRetreiver(new TextValueRetreiver(property, label));
        }
        return cloned;
    }
}
