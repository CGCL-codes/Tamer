package pt.igeo.snig.mig.editor.ui.recordEditor.formFiller.forms.identificationInfoCDG;

import java.util.Collection;
import pt.igeo.snig.mig.editor.constants.Constants;
import pt.igeo.snig.mig.editor.list.FixedList;
import pt.igeo.snig.mig.editor.list.ListValueManager;
import pt.igeo.snig.mig.editor.record.identification.IdentificationInfoCDG;
import fi.mmm.yhteinen.swing.core.YModel;

/**
 * Model for the identification info.
 * 
 * @author Ant�nio Silva
 * @version $Revision: 9247 $
 * @since 1.0
 */
public class IdentificationInfoCDGModel extends YModel {

    /** the current selected identificationInfoCDG */
    IdentificationInfoCDG currentIdentificationInfoCDG = null;

    /**
	 * 
	 * @return the current selected identificationInfoCDG
	 */
    public IdentificationInfoCDG getIdentificationInfoCDG() {
        return currentIdentificationInfoCDG;
    }

    /**
	 * Sets the current identificationInfoCDG to the one selected in the tree
	 * @param info 
	 */
    public void setIdentificationInfoCDG(IdentificationInfoCDG info) {
        currentIdentificationInfoCDG = info;
        notifyObservers();
    }

    /**
	 * Getter for the combo model
	 * @return a collection of fixed list items
	 */
    public Collection<FixedList> getLanguageCode() {
        return ListValueManager.getInstance().getFixedLists(Constants.languageCodeList);
    }

    /**
	 * setter for the combo model: must exist
	 * @param o 
	 */
    public void setLanguageCode(Collection<FixedList> o) {
    }
}
