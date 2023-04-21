package ocumed.teams.teamd.application;

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import ocumed.teams.ILeistung;
import ocumed.teams.OcumedFactory;

/**
 * Ocumed
 * 
 * @author Medisoft (Team D)
 * @version 1.0
 * @see Object
 */
public class LeistungComboBoxModel extends DefaultComboBoxModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6218786578446510447L;

    public LeistungComboBoxModel() {
        super();
        List<ILeistung> lta = OcumedFactory.getInstance().getiLeistungs();
        if (lta != null) {
            for (ILeistung t : lta) {
                addElement(t);
            }
        }
    }

    public int getSelectedLeistungid() {
        return ((ILeistung) getSelectedItem()).getLeistungid();
    }

    public ILeistung getSelectedLeistung() {
        return (ILeistung) getSelectedItem();
    }
}
