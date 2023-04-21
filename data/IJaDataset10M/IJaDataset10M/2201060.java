package objectif.lyon.designer.gui.component.models;

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import objectif.lyon.data.Destination;

public class DestinationComboBoxModel extends DefaultComboBoxModel<Destination> {

    /**
	 * Num�ro de version pour s�rialisation
	 */
    private static final long serialVersionUID = -4225184989814798272L;

    public DestinationComboBoxModel(List<Destination> destinations) {
        for (Destination d : destinations) {
            addElement(d);
        }
    }
}
